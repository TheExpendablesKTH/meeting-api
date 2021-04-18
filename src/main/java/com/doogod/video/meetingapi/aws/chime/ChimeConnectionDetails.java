package com.doogod.video.meetingapi.aws.chime;

import com.doogod.video.meetingapi.db.models.Call;
import com.doogod.video.meetingapi.db.models.CallAttendee;
import com.doogod.video.meetingapi.db.models.Relative;
import com.doogod.video.meetingapi.db.models.Resident;

public class ChimeConnectionDetails {
    public String meetingId;
    public String audioHostUrl;
    public String audioFallbackUrl;
    public String screenDataUrl;
    public String screenSharingUrl;
    public String screenViewingUrl;
    public String signalingUrl;
    public String turnControlUrl;

    // Attendee specific properties
    public String externalUserId;
    public String attendeeId;
    public String joinToken;

    public ChimeConnectionDetails(Call call, CallAttendee callAttendee, Resident resident) {
        this.setMeetingId(call.getMeetingId());
        this.setAudioHostUrl(call.getAudioHostUrl());
        this.setAudioFallbackUrl(call.getAudioFallbackUrl());
        this.setScreenDataUrl(call.getScreenDataUrl());
        this.setScreenSharingUrl(call.getScreenSharingUrl());
        this.setScreenViewingUrl(call.getScreenViewingUrl());
        this.setSignalingUrl(call.getSignalingUrl());
        this.setTurnControlUrl(call.getTurnControlUrl());
        this.setAttendeeId(callAttendee.getAttendeeId());
        this.setJoinToken(callAttendee.getJoinToken());
        this.setExternalUserId(makeExternalUserId(resident));
    }

    public ChimeConnectionDetails(Call call, CallAttendee callAttendee, Relative relative) {
        this.setMeetingId(call.getMeetingId());
        this.setAudioHostUrl(call.getAudioHostUrl());
        this.setAudioFallbackUrl(call.getAudioFallbackUrl());
        this.setScreenDataUrl(call.getScreenDataUrl());
        this.setScreenSharingUrl(call.getScreenSharingUrl());
        this.setScreenViewingUrl(call.getScreenViewingUrl());
        this.setSignalingUrl(call.getSignalingUrl());
        this.setTurnControlUrl(call.getTurnControlUrl());
        this.setAttendeeId(callAttendee.getAttendeeId());
        this.setJoinToken(callAttendee.getJoinToken());
        this.setExternalUserId(makeExternalUserId(relative));
    }

    public static String makeExternalUserId(Resident resident) {
        return resident.getName() + " (#res" + resident.getId() + ")";
    }

    public static String makeExternalUserId(Relative relative) {
        return relative.getName() + " (#rel" + relative.getId() + ")";
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getAudioHostUrl() {
        return audioHostUrl;
    }

    public void setAudioHostUrl(String audioHostUrl) {
        this.audioHostUrl = audioHostUrl;
    }

    public String getAudioFallbackUrl() {
        return audioFallbackUrl;
    }

    public void setAudioFallbackUrl(String audioFallbackUrl) {
        this.audioFallbackUrl = audioFallbackUrl;
    }

    public String getScreenDataUrl() {
        return screenDataUrl;
    }

    public void setScreenDataUrl(String screenDataUrl) {
        this.screenDataUrl = screenDataUrl;
    }

    public String getScreenSharingUrl() {
        return screenSharingUrl;
    }

    public void setScreenSharingUrl(String screenSharingUrl) {
        this.screenSharingUrl = screenSharingUrl;
    }

    public String getScreenViewingUrl() {
        return screenViewingUrl;
    }

    public void setScreenViewingUrl(String screenViewingUrl) {
        this.screenViewingUrl = screenViewingUrl;
    }

    public String getSignalingUrl() {
        return signalingUrl;
    }

    public void setSignalingUrl(String signalingUrl) {
        this.signalingUrl = signalingUrl;
    }

    public String getTurnControlUrl() {
        return turnControlUrl;
    }

    public void setTurnControlUrl(String turnControlUrl) {
        this.turnControlUrl = turnControlUrl;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getJoinToken() {
        return joinToken;
    }

    public void setJoinToken(String joinToken) {
        this.joinToken = joinToken;
    }
}
