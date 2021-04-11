package com.doogod.video.meetingapi.security.authentication;

import com.doogod.video.meetingapi.db.models.Identity;
import com.doogod.video.meetingapi.security.permissions.Permissions;

public interface Identifiable {
    public Identity createIdentity();
    public Permissions getPermissions();
}
