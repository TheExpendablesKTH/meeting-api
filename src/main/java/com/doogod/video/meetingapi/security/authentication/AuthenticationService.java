package com.doogod.video.meetingapi.security.authentication;

import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.ResidencyNotFoundException;
import com.doogod.video.meetingapi.db.models.Admin;
import com.doogod.video.meetingapi.db.models.Device;
import com.doogod.video.meetingapi.db.models.Identity;
import com.doogod.video.meetingapi.db.services.IdentityService;
import com.doogod.video.meetingapi.db.services.ResidencyService;
import com.doogod.video.meetingapi.security.permissions.Permissions;
import com.doogod.video.meetingapi.security.token.TokenService;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static lombok.AccessLevel.*;

@Service
@FieldDefaults(level = PUBLIC, makeFinal = true)
public final class AuthenticationService {

    @NonNull
    TokenService tokenService;

    @NonNull
    IdentityService identityService;

    @NonNull
    ResidencyService residencyService;

    @NonNull
    BCryptPasswordEncoder bcrypt;

    public AuthenticationService(TokenService tokens, IdentityService identityService, ResidencyService residencyService, BCryptPasswordEncoder bcrypt) {
        this.tokenService = tokens;
        this.identityService = identityService;
        this.residencyService = residencyService;
        this.bcrypt = bcrypt;
    }

    public String login(Admin admin) throws IdentityNotFoundException {
        Identity fromDB = identityService.findByUsername(admin.createIdentity().getUsername());
        if (bcrypt.matches(admin.createIdentity().getPassword(), fromDB.getPassword())) {
            return tokenService.permanent(admin);
        }

        // we shouldn't distinct between a password failure and identity not found,
        // to protect users privacy and safety from brute forcing
        throw new IdentityNotFoundException();
    }

    public String login(Device device) throws IdentityNotFoundException {
        try {
            residencyService.findByDevicePassphrase(device.getPassphrase());
        } catch (ResidencyNotFoundException e) {
            throw new IdentityNotFoundException();
        }

        return tokenService.permanent(device);
    }

    public Identity findByToken(String token) throws IdentityNotFoundException {
        if (token.contains("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        Map<String, String> claims = tokenService.verify(token);
        String username = claims.get("username");
        return identityService.findByUsername(username);
    }

    public Permissions parsePermissions(String token) {
        if (token.contains("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        Map<String, String> claims = tokenService.verify(token);
        String permissions = claims.get("permissions");

        return Permissions.fromString(permissions);
    }

    public void logout(final Identity identity) {
        // TODO: add support for invalidating tokens
    }
}
