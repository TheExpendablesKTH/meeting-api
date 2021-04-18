package com.doogod.video.meetingapi.db.models;

import java.beans.ConstructorProperties;

public class CallAttendee {

    private Integer id;
    private Integer callId;
    private String attendeeId;
    private String joinToken;
    private Integer residentId;
    private Integer relativeId;

    @ConstructorProperties({"id", "call_id", "attendee_id", "join_token", "resident_id", "relative_id"})
    public CallAttendee(Integer id, Integer callId, String attendeeId, String joinToken, Integer residentId, Integer relativeId) {
        this.id = id;
        this.callId = callId;
        this.attendeeId = attendeeId;
        this.joinToken = joinToken;
        this.residentId = residentId;
        this.relativeId = relativeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCallId() {
        return callId;
    }

    public void setCallId(Integer callId) {
        this.callId = callId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getJoinToken() {
        return joinToken;
    }

    public void setJoinToken(String joinToken) {
        this.joinToken = joinToken;
    }

    public Integer getResidentId() {
        return residentId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }

    public Integer getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(Integer relativeId) {
        this.relativeId = relativeId;
    }
}
