package com.doogod.video.meetingapi.aws.chime;

import com.amazonaws.services.chime.AmazonChimeClient;
import com.amazonaws.services.chime.model.*;
import com.doogod.video.meetingapi.aws.chime.exceptions.MeetingExpiredException;
import com.doogod.video.meetingapi.aws.chime.exceptions.NoSuchCallException;
import com.doogod.video.meetingapi.aws.sns.SMSSendingService;
import com.doogod.video.meetingapi.db.exceptions.CallAttendeeNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.CallNotFoundException;
import com.doogod.video.meetingapi.db.models.*;
import com.doogod.video.meetingapi.db.services.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ChimeService {

    @Autowired
    CallService callService;

    @Autowired
    SMSSendingService smsSendingService;

    @Autowired
    AmazonChimeClient chime;

    public ChimeConnectionDetails createMeeting(Resident resident) {
        Meeting meeting = chime.createMeeting(new CreateMeetingRequest()).getMeeting();

        Call call = new Call(
                null,
                meeting.getMeetingId(),
                meeting.getMediaPlacement().getAudioHostUrl(),
                meeting.getMediaPlacement().getAudioFallbackUrl(),
                meeting.getMediaPlacement().getScreenDataUrl(),
                meeting.getMediaPlacement().getScreenSharingUrl(),
                meeting.getMediaPlacement().getScreenViewingUrl(),
                meeting.getMediaPlacement().getSignalingUrl(),
                meeting.getMediaPlacement().getTurnControlUrl()
        );
        callService.insert(call);

        CreateAttendeeRequest attendeeRequest = new CreateAttendeeRequest();
        attendeeRequest.setMeetingId(meeting.getMeetingId());
        attendeeRequest.setExternalUserId(resident.getName());
        CreateAttendeeResult attendee = chime.createAttendee(attendeeRequest);

        CallAttendee callAttendee = new CallAttendee(
                null,
                call.getId(),
                attendee.getAttendee().getAttendeeId(),
                attendee.getAttendee().getJoinToken(),
                resident.getId(),
                null
        );
        callService.insert(callAttendee);

        return new ChimeConnectionDetails(call, callAttendee, resident);
    }

    public ChimeConnectionDetails getMeetingForResident(Resident resident) throws NoSuchCallException, MeetingExpiredException {
        Call call;
        CallAttendee callAttendee;
        try {
            callAttendee = callService.getMostRecentForResident(resident);
        } catch (CallAttendeeNotFoundException e) {
            throw new NoSuchCallException();
        }

        try {
            call = callService.findCallById(callAttendee.getCallId());
        } catch (CallNotFoundException e) {
            throw new NoSuchCallException();
        }

        this.throwIfCallExpired(call.getMeetingId());

        return new ChimeConnectionDetails(call, callAttendee, resident);
    }

    public ChimeConnectionDetails getMeetingForRelative(Relative relative) throws NoSuchCallException, MeetingExpiredException {
        Call call;
        CallAttendee callAttendee;
        try {
            callAttendee = callService.getMostRecentForRelative(relative);
        } catch (CallAttendeeNotFoundException e) {
            throw new NoSuchCallException();
        }

        try {
            call = callService.findCallById(callAttendee.getCallId());
        } catch (CallNotFoundException e) {
            throw new NoSuchCallException();
        }

        this.throwIfCallExpired(call.getMeetingId());

        return new ChimeConnectionDetails(call, callAttendee, relative);
    }

    public ChimeConnectionDetails addRelativeToMeeting(String meetingId, Relative relative, Resident resident) throws NoSuchCallException {
        Call call;
        try {
            call = callService.findCallByMeetingId(meetingId);
        } catch (CallNotFoundException e) {
            throw new NoSuchCallException();
        }

        CreateAttendeeRequest attendeeRequest = new CreateAttendeeRequest();
        attendeeRequest.setMeetingId(meetingId);
        attendeeRequest.setExternalUserId(ChimeConnectionDetails.makeExternalUserId(relative));
        CreateAttendeeResult attendee = chime.createAttendee(attendeeRequest);

        CallAttendee callAttendee = new CallAttendee(
                null,
                call.getId(),
                attendee.getAttendee().getAttendeeId(),
                attendee.getAttendee().getJoinToken(),
                null,
                relative.getId()
        );
        callService.insert(callAttendee);

        this.generateAndSendCode(relative, resident, callAttendee);

        return new ChimeConnectionDetails(call, callAttendee, relative);
    }

    private void generateAndSendCode(Relative relative, Resident resident, CallAttendee callAttendee) {
        String firstChunk = this.getThreeRandomLetters();
        String lastChunk = this.getThreeRandomLetters();

        String code = firstChunk + "-" + lastChunk;

        if (callService.invitationCodeExists(code)) {
            this.generateAndSendCode(relative, resident, callAttendee);
            return;
        }

        CallInvite callInvite = new CallInvite(null, callAttendee.getId(), code);
        callService.insert(callInvite);

        String message = "";
        message += "Hi " + relative.getName() + "!";
        message += " You have been invited to a video call with " + resident.getName();
        message += "\n\n";
        message += "Go to http://join.dogood.se and login with the code:\n";
        message += code;

        smsSendingService.sendTextMessage(relative.getPhone(), message);
    }

    private String getThreeRandomLetters() {
        int lower = 48; // '0'
        int upper = 122; // 'z'
        Random random = new Random();

        String str = random.ints(lower, upper + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(3)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return str.toUpperCase();
    }

    public void throwIfCallExpired(String meetingId) throws MeetingExpiredException {
        ListMeetingsResult meetings = chime.listMeetings(new ListMeetingsRequest());
        if (meetings.getMeetings().isEmpty()) {
            throw new MeetingExpiredException();
        }

        for (Meeting meeting : meetings.getMeetings()) {
            String src = meeting.getMeetingId();
            if (src.equals(meetingId)) {
                return;
            }
        }

        throw new MeetingExpiredException();
    }
}
