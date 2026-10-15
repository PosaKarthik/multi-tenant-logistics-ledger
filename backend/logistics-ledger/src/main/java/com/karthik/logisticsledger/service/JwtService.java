package com.karthik.logisticsledger.service;


import com.karthik.logisticsledger.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret}") String secret){
        this.secretKey= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user){
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("tenantId",user.getTenant().getId())
                .claim("role",user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60*60))
                .signWith(secretKey)
                .compact();
    }


    public Long extractUserId(String token){
        return Long.valueOf(
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseEncryptedClaims(token)
                        .getPayload()
                        .getSubject()
        );
    }

    public Long extractTenantId(String token){
        return Long.valueOf(
           Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseEncryptedClaims(token)
                   .getPayload()
                   .get("tenantId", Long.class)
        );
    }

    public String extractRole(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseEncryptedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
