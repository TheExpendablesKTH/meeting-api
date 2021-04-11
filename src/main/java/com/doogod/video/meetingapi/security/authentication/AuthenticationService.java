package com.doogod.video.meetingapi.security.authentication;

import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.models.Identity;
import com.doogod.video.meetingapi.db.services.IdentityService;
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
    BCryptPasswordEncoder bcrypt;

    public AuthenticationService(TokenService tokens, IdentityService identityService, BCryptPasswordEncoder bcrypt) {
        this.tokenService = tokens;
        this.identityService = identityService;
        this.bcrypt = bcrypt;
    }

    public String login(Identity identity) throws IdentityNotFoundException {
        Identity fromDB = identityService.findByUsername(identity.getUsername());
        if (bcrypt.matches(identity.getPassword(), fromDB.getPassword())) {
            return tokenService.permanent(ImmutableMap.of("username", fromDB.getUsername()));
        }

        // we shouldn't distinct between a password failure and identity not found,
        // to protect users privacy and safety from brute forcing
        throw new IdentityNotFoundException();
    }

    public Identity findByToken(String token) throws IdentityNotFoundException {
        if (token.contains("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        Map<String, String> claims = tokenService.verify(token);
        String username = claims.get("username");
        return identityService.findByUsername(username);
    }

    public void logout(final Identity identity) {
        // TODO: add support for invalidating tokens
    }
}
