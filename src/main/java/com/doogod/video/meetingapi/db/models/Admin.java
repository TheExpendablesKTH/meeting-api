package com.doogod.video.meetingapi.db.models;

import com.doogod.video.meetingapi.security.authentication.Identifiable;
import com.doogod.video.meetingapi.security.permissions.Permissions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONPropertyName;

import java.beans.ConstructorProperties;

public class Admin implements Identifiable {

    private Integer id;
    private Integer residencyId;
    private String username;
    private String password;
    private String name;

    @ConstructorProperties({"id", "residency_id", "username", "password", "name"})
    public Admin(int id, Integer residencyId, String username, String password, String name) {
        this.id = id;
        this.residencyId = residencyId;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    @JsonIgnore
    public Identity createIdentity() {
        return new Identity(null, "admin", this.residencyId, this.username, this.password, this.id, null, null);
    }

    @JsonIgnore
    public Permissions getPermissions() {
        var permissions = new Permissions();
        permissions.add(Permissions.CAN_LIST_RESIDENTS);
        permissions.add(Permissions.CAN_CREATE_RESIDENTS);
        return permissions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("residency_id")
    @JSONPropertyName("residency_id")
    public Integer getResidencyId() {
        return residencyId;
    }

    public void setResidencyId(Integer residencyId) {
        this.residencyId = residencyId;
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
