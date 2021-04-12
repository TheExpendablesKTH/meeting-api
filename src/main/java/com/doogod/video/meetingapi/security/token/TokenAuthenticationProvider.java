package com.doogod.video.meetingapi.security.token;

import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.security.authentication.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
public final class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    AuthenticationService auth;

    @Override
    protected void additionalAuthenticationChecks(final UserDetails d, final UsernamePasswordAuthenticationToken auth) {
        // Nothing to do
    }

    @Override
    protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication) {
        final Object token = authentication.getCredentials();
        try {
            return auth.findByToken(token.toString());
        } catch (IdentityNotFoundException e) {
            throw new UsernameNotFoundException("Cannot find user with authentication " + token);
        }
    }
}
