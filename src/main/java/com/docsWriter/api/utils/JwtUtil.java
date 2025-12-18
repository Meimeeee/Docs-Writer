package com.docsWriter.api.utils;

import com.docsWriter.api.database.entities.AccountEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class JwtUtil {
    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.access-expiration-minutes}")
    private int accessExpirationMinutes;

    @Value("${app.security.jwt.refresh-expiration-days}")
    private int refreshExpirationDays;

    @Value("${app.security.jwt.issuer}")
    private String issuer;

    private Key key;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        //create key
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        //verify token
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(issuer) //verify iss
                .build();

        log.info("JwtUtil (jjwt) initialized. issuer={}, accessExpMinutes={}, refreshExpDays={}"
                , issuer, accessExpirationMinutes, refreshExpirationDays);
    }

    public String generateAccessToken(AccountEntity account) {
        Instant now = Instant.now();
        Instant expiry = now.plus(accessExpirationMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .setSubject(account.getId().toString())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .claim("email", account.getEmail())
                .claim("username", account.getUsername())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(AccountEntity account) {
        Instant now = Instant.now();
        Instant expiry = now.plus(refreshExpirationDays, ChronoUnit.DAYS);

        return Jwts.builder()
                .setSubject(account.getId().toString())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public UUID verifyAccessTokenAndGetUserId(String token) {
        try {
            Claims claims = jwtParser
                    .parseClaimsJws(token)
                    .getBody();

            String sub = claims.getSubject();
            return UUID.fromString(sub);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid access token: {}", e.getMessage());
            throw new RuntimeException("Invalid access token");
        }
    }

    public UUID verifyRefreshTokenAndGetUserId(String token) {
        try {
            Claims claims = jwtParser
                    .parseClaimsJws(token)
                    .getBody();

            String sub = claims.getSubject();
            return UUID.fromString(sub);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid refresh token: {}", e.getMessage());
            throw new RuntimeException("Invalid refresh token");
        }
    }

}
