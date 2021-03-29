package com.doogod.video.meetingapi.chime;

public class ChimeConnectionDetails {
    public String meetingId;
    public String audioHostUrl;
    public String audioFallbackUrl;
    public String screenDataUrl;
    public String screenSharingUrl;
    public String screenViewingUrl;
    public String signalingUrl;
    public String turnControlUrl;
    public String externalUserId;
    public String attendeeId;
    public String joinToken;

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
