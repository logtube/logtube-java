package io.github.logtube.utils;

import java.security.SecureRandom;

public class Bytes {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static void copy(byte[] desc, int descOffset, byte[] src, int srcOffset, int length) {
        for (int i = 0; i < length; i++) {
            desc[descOffset + i] = src[srcOffset + i];
        }
    }

    public static void random(byte[] bytes, int offset, int length) {
        byte[] rand = new byte[length];
        SECURE_RANDOM.nextBytes(rand);
        copy(bytes, offset, rand, 0, length);
    }

}
