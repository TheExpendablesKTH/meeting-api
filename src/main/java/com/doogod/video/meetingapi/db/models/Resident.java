package com.doogod.video.meetingapi.db.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.beans.ConstructorProperties;

public class Resident {
    public String name;
    public int id;
    public Integer residencyId;

    @ConstructorProperties({"id", "name", "residency_id"})
    public Resident(int id, String name, Integer residencyId) {
        this.id = id;
        this.name = name;
        this.residencyId = residencyId;
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
