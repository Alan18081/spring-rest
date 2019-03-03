package com.alex.springrest.shared;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final int ITERATIONS = 1000;
    private final int KEY_LENGTH = 256;

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    public String generateAddressId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder string = new StringBuilder(length);

        for(int i = 0; i < length; i++) {
            string.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(string);
    }

    public String generateEmailVerificationToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
    }

    public String generateResetPasswordToken(String userId, String email) {
        return Jwts.builder()
                .setSubject(userId)
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
    }

    public static boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.getTokenSecret())
                .parseClaimsJws(token).getBody();

        Date tokenExpirationDate = claims.getExpiration();
        Date todayDate = new Date();

        return tokenExpirationDate.before(todayDate);
    }

}
