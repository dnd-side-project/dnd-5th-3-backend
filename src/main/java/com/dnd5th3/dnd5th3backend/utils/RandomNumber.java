package com.dnd5th3.dnd5th3backend.utils;

public class RandomNumber {
    public static int startFromZeroTo(int limit) {
        return (int) (Math.random() * limit);
    }
}
