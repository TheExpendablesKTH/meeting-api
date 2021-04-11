package com.doogod.video.meetingapi.db.models;

import com.doogod.video.meetingapi.security.authentication.Identifiable;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Device implements Identifiable {
    public Integer id;
    public Integer residencyId;
    public String passphrase;

    @ConstructorProperties({"id", "residency_id", "passphrase"})
    public Device(Integer id, Integer residencyId, String passphrase) {
        this.id = id;
        this.residencyId = residencyId;
        this.passphrase = passphrase;
    }

    @Override
    public Identity getIdentity() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String now = df.format(new Date());

        String username = "device_created_at:" + now;
        return new Identity(null, "device", username, null, null, this.id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("residency_id")
    public Integer getResidencyId() {
        return residencyId;
    }

    public void setResidencyId(Integer residencyId) {
        this.residencyId = residencyId;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
}
