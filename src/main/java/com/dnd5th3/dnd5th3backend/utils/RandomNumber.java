package com.dnd5th3.dnd5th3backend.utils;

import java.security.SecureRandom;

public class RandomNumber {

    private static final char[] word = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final int PASSWORD_LENGTH = 8;

    public static int startFromZeroTo(int limit) {
        return (int) (Math.random() * limit);
    }

    public static String generatePassword(){

        StringBuilder sb = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i<PASSWORD_LENGTH; i++) {
            int random  = secureRandom.nextInt(word.length);
            sb.append(word[random]);
        }

        return sb.toString();
    }
}
