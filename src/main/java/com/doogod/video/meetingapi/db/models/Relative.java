package com.doogod.video.meetingapi.db.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONPropertyIgnore;

import java.beans.ConstructorProperties;

public class Relative {
    public int id;
    public String name;
    public String phone;
    public Integer residentId;

    @ConstructorProperties({"id", "name", "phone"})
    public Relative(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JSONPropertyIgnore
    @JsonIgnore
    public Integer getResidentId() {
        return residentId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }
}
