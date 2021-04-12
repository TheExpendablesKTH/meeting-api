package com.doogod.video.meetingapi.security.token;

import com.doogod.video.meetingapi.db.models.Identity;
import com.doogod.video.meetingapi.security.authentication.Identifiable;
import com.doogod.video.meetingapi.security.permissions.Permissions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import lombok.experimental.FieldDefaults;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public final class TokenService implements Clock {
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    String issuer;
    int expirationSec;
    int clockSkewSec;
    String secretKey;

    TokenService(@Value("${jwt.issuer}") final String issuer,
                 @Value("${jwt.expiration-sec}") final int expirationSec,
                 @Value("${jwt.clock-skew-sec}") final int clockSkewSec,
                 @Value("${jwt.secret}") final String secret) {
        super();
        this.issuer = requireNonNull(issuer);
        this.expirationSec = requireNonNull(expirationSec);
        this.clockSkewSec = requireNonNull(clockSkewSec);
        this.secretKey = BASE64.encode(requireNonNull(secret));
    }

    public String permanent(Identifiable identifiable) {
        Identity identity = identifiable.createIdentity();
        Permissions permissions = identifiable.getPermissions();

        Map<String, String> attributes = ImmutableMap.of(
                "username",
                identity.getUsername(),
                "permissions",
                permissions.toString()
        );

        return newToken(attributes, 0);
    }

    public String expiring(Identifiable identifiable) {
        Identity identity = identifiable.createIdentity();
        Permissions permissions = identifiable.getPermissions();

        Map<String, String> attributes = ImmutableMap.of(
                "username",
                identity.getUsername(),
                "permissions",
                permissions.toString()
        );

        return newToken(attributes, expirationSec);
    }

    private String newToken(final Map<String, String> attributes, final int expiresInSec) {
        final DateTime now = DateTime.now();
        final Claims claims = Jwts
                .claims()
                .setIssuer(issuer)
                .setIssuedAt(now.toDate());

        if (expiresInSec > 0) {
            final DateTime expiresAt = now.plusSeconds(expiresInSec);
            claims.setExpiration(expiresAt.toDate());
        }
        claims.putAll(attributes);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }

    public Map<String, String> verify(final String token) {
        final JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey);
        return parseClaims(() -> parser.parseClaimsJws(token).getBody());
    }

    private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
        try {
            final Claims claims = toClaims.get();
            final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            for (final Map.Entry<String, Object> e: claims.entrySet()) {
                builder.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return builder.build();
        } catch (final IllegalArgumentException | JwtException e) {
            return ImmutableMap.of();
        }
    }

    @Override
    public Date now() {
        final DateTime now = DateTime.now();
        return now.toDate();
    }
}
