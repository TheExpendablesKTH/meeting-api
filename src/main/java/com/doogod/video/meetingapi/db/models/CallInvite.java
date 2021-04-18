package com.doogod.video.meetingapi.db.models;

import java.beans.ConstructorProperties;

public class CallInvite {

    private Integer id;
    private Integer callsAttendeesId;
    public String code;

    @ConstructorProperties({"id", "calls_attendees_id", "code"})
    public CallInvite(Integer id, Integer callsAttendeesId, String code) {
        this.id = id;
        this.callsAttendeesId = callsAttendeesId;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCallsAttendeesId() {
        return callsAttendeesId;
    }

    public void setCallsAttendeesId(Integer callsAttendeesId) {
        this.callsAttendeesId = callsAttendeesId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
