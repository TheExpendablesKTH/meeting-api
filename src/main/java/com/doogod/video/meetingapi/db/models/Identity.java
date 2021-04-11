package com.doogod.video.meetingapi.db.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.json.JSONPropertyIgnore;
import org.json.JSONPropertyName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Value
@Builder
public class Identity implements UserDetails {
    private Integer id;
    private String type;
    private Integer residencyId;
    private String username;
    private String password;
    private Integer adminId;
    private Integer deviceId;

    @JsonCreator
    public Identity(Integer id, String type, Integer residencyId, String username, String password, Integer adminId, Integer deviceId) {
        super();
        this.id = id;
        this.type = type;
        this.residencyId = residencyId;
        this.username = requireNonNull(username);
        this.password = password;
        this.adminId = adminId;
        this.deviceId = deviceId;
    }

    @JSONPropertyIgnore
    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    @JsonProperty("residency_id")
    @JSONPropertyName("residency_id")
    public Integer getResidencyId() {
        return residencyId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JSONPropertyIgnore
    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonProperty("identity_id")
    @JSONPropertyName("identity_id")
    public Integer getIdentityId() {
        return id;
    }

    @JSONPropertyIgnore
    @JsonIgnore
    public Integer getAdminId() {
        return adminId;
    }

    @JSONPropertyIgnore
    @JsonIgnore
    public Integer getDeviceId() {
        return deviceId;
    }

    @JSONPropertyIgnore
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONPropertyIgnore
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONPropertyIgnore
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JSONPropertyIgnore
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}
