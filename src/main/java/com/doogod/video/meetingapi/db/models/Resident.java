package com.doogod.video.meetingapi.db.models;

import com.doogod.video.meetingapi.security.authentication.Identifiable;
import com.doogod.video.meetingapi.security.permissions.Permissions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.beans.ConstructorProperties;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Resident implements Identifiable {
    public String name;
    public int id;
    public Integer residencyId;

    @ConstructorProperties({"id", "name", "residency_id"})
    public Resident(int id, String name, Integer residencyId) {
        this.id = id;
        this.name = name;
        this.residencyId = residencyId;
    }

    @Override
    public Identity createIdentity() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        String now = df.format(new Date());

        String username = "resident_" + this.id + "_logged_in_at:" + now;
        return new Identity(null, "resident", this.residencyId, username, null, null, null, this.id);
    }

    @JsonIgnore
    public Permissions getPermissions() {
        var permissions = new Permissions();
        permissions.add(Permissions.CAN_LIST_CALLS);
        permissions.add(Permissions.CAN_START_CALLS);
        return permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getResidencyId() {
        return residencyId;
    }

    public void setResidencyId(Integer residencyId) {
        this.residencyId = residencyId;
    }
}
