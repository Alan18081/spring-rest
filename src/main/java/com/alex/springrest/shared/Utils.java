package com.alex.springrest.shared;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
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

    private String generateRandomString(int length) {
        StringBuilder string = new StringBuilder(length);

        for(int i = 0; i < length; i++) {
            string.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(string);
    }

}
