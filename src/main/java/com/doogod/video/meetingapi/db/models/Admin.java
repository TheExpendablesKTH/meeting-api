package com.doogod.video.meetingapi.db.models;

import com.doogod.video.meetingapi.security.authentication.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.ConstructorProperties;

public class Admin implements Identifiable {

    private Integer id;
    private String username;
    private String password;
    private String name;

    @ConstructorProperties({"id", "username", "password", "name"})
    public Admin(int id, String username, String password, String name) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    @JsonIgnore
    public Identity createIdentity() {
        return new Identity(null, "admin", this.username, this.password, this.id, null);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getAdminPassword() {
        return password;
    }

    public void setAdminPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
