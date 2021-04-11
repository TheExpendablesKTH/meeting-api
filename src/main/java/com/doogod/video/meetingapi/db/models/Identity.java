package com.doogod.video.meetingapi.db.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
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
    private String username;
    private String password;
    private Integer adminId;

    @JsonCreator
    public Identity(Integer id, String username, String password, Integer adminId) {
        super();
        this.id = id;
        this.username = requireNonNull(username);
        this.password = requireNonNull(password);
        this.adminId = adminId;
    }

    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonProperty("identity_id")
    public Integer getIdentityId() {
        return id;
    }

    @JsonIgnore
    public Integer getAdminId() {
        return adminId;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}
