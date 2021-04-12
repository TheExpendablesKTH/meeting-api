package com.doogod.video.meetingapi.db.models;

import java.beans.ConstructorProperties;

public class Residency {

    public int id;
    public String name;
    public String devicePassphrase;

    @ConstructorProperties({"id", "name", "device_passphrase"})
    public Residency(int id, String name, String devicePassphrase) {
        this.id = id;
        this.name = name;
        this.devicePassphrase = devicePassphrase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevicePassphrase() {
        return devicePassphrase;
    }

    public void setDevicePassphrase(String devicePassphrase) {
        this.devicePassphrase = devicePassphrase;
    }
}
