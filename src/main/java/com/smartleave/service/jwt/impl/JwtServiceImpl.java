package com.smartleave.service.jwt.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.smartleave.config.JwtProperties;
import com.smartleave.service.jwt.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    @Override
    public String generateToken(UserDetails userDetails) {
        try {
            JWSSigner signer = new MACSigner(jwtProperties.getSecret().getBytes());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .claim("authorities", userDetails.getAuthorities())
                    .expirationTime(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );

            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(jwtProperties.getSecret().getBytes());

            boolean isValid = jwt.verify(verifier);
            String username = jwt.getJWTClaimsSet().getSubject();
            Date expiration = jwt.getJWTClaimsSet().getExpirationTime();

            return isValid &&
                   username.equals(userDetails.getUsername()) &&
                   expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            return jwt.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Failed to extract username from token", e);
        }
    }
}
