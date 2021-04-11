package com.doogod.video.meetingapi.security.authentication;

import com.doogod.video.meetingapi.db.models.Identity;

public interface Identifiable {
    public Identity createIdentity();
}
