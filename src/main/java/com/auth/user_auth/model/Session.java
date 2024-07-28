package com.auth.user_auth.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "session")
@Setter
@Getter
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long accountId;
    private Role role;
    private Instant expiredAt;

    public Session(long accountId, Role role, Instant expiredAt) {
        this.accountId = accountId;
        this.role = role;
        this.expiredAt = expiredAt;
    }

    public String genAccessToken(String secret) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("accountId", getAccountId());
        claims.put("role", getRole());
        claims.put("session", getId());
        //way to create access token
        String token = Jwts.builder()
                .setId(String.valueOf(getId()))
                .setExpiration(Date.from(getExpiredAt()))
                .setIssuedAt(new Date())
                .claim("claims", claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;
    }

    public static Map<String, Object> decodeAccessToken(String accessToken, String secret) {
        Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(accessToken).getBody();
        Date expireDate = body.getExpiration();
        if(expireDate.compareTo(Date.from(Instant.now())) < 0) {
            throw new Error("Session has expired");
        }
        Map<String, Object> claims = body.get("claims", Map.class);
        return claims;
    }

    public boolean isExpired() {
        return this.expiredAt.compareTo(Instant.now()) >= 0;
    }
}
