package com.doogod.video.meetingapi.chime;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.AmazonChimeClient;
import com.amazonaws.services.chime.model.*;

public class SimpleJoin {

    private String accessKey;
    private String secretKey;

    public SimpleJoin(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public ChimeConnectionDetails createMeetingIfItDoesNotExistAndAddAttendeeWithExternalId(String externalId) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.accessKey, this.secretKey);
        AmazonChime chime = AmazonChimeClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        ListMeetingsResult meetings = chime.listMeetings(new ListMeetingsRequest());
        Meeting meeting;
        if (meetings.getMeetings().isEmpty()) {
            meeting = chime.createMeeting(new CreateMeetingRequest()).getMeeting();
        } else {
            meeting = meetings.getMeetings().get(0);
        }

        CreateAttendeeRequest attendeeRequest1 = new CreateAttendeeRequest();
        attendeeRequest1.setMeetingId(meeting.getMeetingId());
        attendeeRequest1.setExternalUserId(externalId);
        CreateAttendeeResult attendee1 = chime.createAttendee(attendeeRequest1);

        ChimeConnectionDetails res = new ChimeConnectionDetails();
        res.setExternalUserId(attendee1.getAttendee().getExternalUserId());
        res.setJoinToken(attendee1.getAttendee().getJoinToken());
        res.setAttendeeId(attendee1.getAttendee().getAttendeeId());

        res.setMeetingId(meeting.getMeetingId());
        res.setAudioHostUrl(meeting.getMediaPlacement().getAudioHostUrl());
        res.setAudioFallbackUrl(meeting.getMediaPlacement().getAudioFallbackUrl());
        res.setScreenDataUrl(meeting.getMediaPlacement().getScreenDataUrl());
        res.setScreenDataUrl(meeting.getMediaPlacement().getScreenSharingUrl());
        res.setScreenViewingUrl(meeting.getMediaPlacement().getScreenViewingUrl());
        res.setSignalingUrl(meeting.getMediaPlacement().getSignalingUrl());
        res.setTurnControlUrl(meeting.getMediaPlacement().getTurnControlUrl());

        return res;
    }
}
