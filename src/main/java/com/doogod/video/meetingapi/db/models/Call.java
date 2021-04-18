package com.doogod.video.meetingapi.db.models;

import java.beans.ConstructorProperties;

public class Call {

    private Integer id;
    private String meetingId;
    private String audioHostUrl;
    private String audioFallbackUrl;
    private String screenDataUrl;
    private String screenSharingUrl;
    private String screenViewingUrl;
    private String signalingUrl;
    private String turnControlUrl;

    @ConstructorProperties({"id", "meeting_id", "audio_host_url", "audio_fallback_url", "screen_data_url", "screen_sharing_url", "screen_viewing_url", "signaling_url", "turn_control_url"})
    public Call(
            Integer id,
            String meetingId,
            String audioHostUrl,
            String audioFallbackUrl,
            String screenDataUrl,
            String screenSharingUrl,
            String screenViewingUrl,
            String signalingUrl,
            String turnControlUrl
    ) {
        this.id = id;
        this.meetingId = meetingId;
        this.audioHostUrl = audioHostUrl;
        this.audioFallbackUrl = audioFallbackUrl;
        this.screenDataUrl = screenDataUrl;
        this.screenSharingUrl = screenSharingUrl;
        this.screenViewingUrl = screenViewingUrl;
        this.signalingUrl = signalingUrl;
        this.turnControlUrl = turnControlUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
