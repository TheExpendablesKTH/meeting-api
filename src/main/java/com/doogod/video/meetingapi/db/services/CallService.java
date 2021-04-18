package com.doogod.video.meetingapi.db.services;

import com.doogod.video.meetingapi.db.exceptions.CallAttendeeNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.CallInviteNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.CallNotFoundException;
import com.doogod.video.meetingapi.db.models.*;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallService {

    @Autowired
    Jdbi jdbi;

    public void insert(Call call) {
        var id = jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO\n" +
                "calls(meeting_id, audio_host_url, audio_fallback_url, screen_data_url, screen_sharing_url, screen_viewing_url, signaling_url, turn_control_url)\n" +
                "values(:meeting_id, :audio_host_url, :audio_fallback_url, :screen_data_url, :screen_sharing_url, :screen_viewing_url, :signaling_url, :turn_control_url)")
                .bind("meeting_id", call.getMeetingId())
                .bind("audio_host_url", call.getAudioHostUrl())
                .bind("audio_fallback_url", call.getAudioFallbackUrl())
                .bind("screen_data_url", call.getScreenDataUrl())
                .bind("screen_sharing_url", call.getScreenSharingUrl())
                .bind("screen_viewing_url", call.getScreenViewingUrl())
                .bind("signaling_url", call.getSignalingUrl())
                .bind("turn_control_url", call.getSignalingUrl())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one()
        );

        call.setId(id.intValue());
    }

    public void insert(CallAttendee callAttendee) {
        var id = jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO\n" +
                "calls_attendees(call_id, attendee_id, join_token, resident_id, relative_id)\n" +
                "values(:call_id, :attendee_id, :join_token, :resident_id, :relative_id)")
                .bind("call_id", callAttendee.getCallId())
                .bind("attendee_id", callAttendee.getAttendeeId())
                .bind("join_token", callAttendee.getJoinToken())
                .bind("resident_id", callAttendee.getResidentId())
                .bind("relative_id", callAttendee.getRelativeId())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one()
        );

        callAttendee.setId(id.intValue());
    }

    public void insert(CallInvite invite) {
        var id = jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO\n" +
                "calls_invites(calls_attendees_id, code)\n" +
                "values(:calls_attendees_id, :code)")
                .bind("calls_attendees_id", invite.getCallsAttendeesId())
                .bind("code", invite.getCode())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one()
        );

        invite.setId(id.intValue());
    }

    public Call findCallById(Integer id) throws CallNotFoundException {
        try {
            return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM calls WHERE id = :id;")
                    .bind("id", id)
                    .registerRowMapper(ConstructorMapper.factory(Call.class))
                    .mapTo(Call.class)
                    .one()
            );
        } catch (IllegalStateException e) {
            throw new CallNotFoundException();
        }
    }

    public Call findCallByMeetingId(String meetingId) throws CallNotFoundException {
        try {
            return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM calls WHERE meeting_id = :meeting_id;")
                    .bind("meeting_id", meetingId)
                    .registerRowMapper(ConstructorMapper.factory(Call.class))
                    .mapTo(Call.class)
                    .one()
            );
        } catch (IllegalStateException e) {
            throw new CallNotFoundException();
        }
    }

    public CallAttendee getMostRecentForResident(Resident resident) throws CallAttendeeNotFoundException {
        try {
            return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM calls_attendees WHERE resident_id = :resident_id ORDER BY id DESC;")
                    .bind("resident_id", resident.getId())
                    .registerRowMapper(ConstructorMapper.factory(CallAttendee.class))
                    .mapTo(CallAttendee.class)
                    .first()
            );
        } catch (IllegalStateException e) {
            throw new CallAttendeeNotFoundException();
        }
    }

    public CallAttendee getMostRecentForRelative(Relative relative) throws CallAttendeeNotFoundException {
        try {
            return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM calls_attendees WHERE relative_id = :relative_id ORDER BY id DESC;")
                    .bind("relative_id", relative.getId())
                    .registerRowMapper(ConstructorMapper.factory(CallAttendee.class))
                    .mapTo(CallAttendee.class)
                    .first()
            );
        } catch (IllegalStateException e) {
            throw new CallAttendeeNotFoundException();
        }
    }

    public CallAttendee findByAttendeeId(Integer id) throws CallAttendeeNotFoundException {
        try {
            return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM calls_attendees WHERE id = :id;")
                    .bind("id", id)
                    .registerRowMapper(ConstructorMapper.factory(CallAttendee.class))
                    .mapTo(CallAttendee.class)
                    .first()
            );
        } catch (IllegalStateException e) {
            throw new CallAttendeeNotFoundException();
        }
    }

    public CallAttendee findAttendeeByInvitationCode(String code) throws CallInviteNotFoundException, CallAttendeeNotFoundException {
        CallInvite invite;
        try {
            invite = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM calls_invites WHERE code = :code;")
                    .bind("code", code)
                    .registerRowMapper(ConstructorMapper.factory(CallInvite.class))
                    .mapTo(CallInvite.class)
                    .one()
            );
        } catch (IllegalStateException e) {
            throw new CallInviteNotFoundException();
        }

        return this.findByAttendeeId(invite.getCallsAttendeesId());
    }

    public boolean invitationCodeExists(String code) {
        try {
            jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM calls_invites WHERE code = :code;")
                    .bind("code", code)
                    .registerRowMapper(ConstructorMapper.factory(CallInvite.class))
                    .mapTo(CallInvite.class)
                    .one()
            );
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
