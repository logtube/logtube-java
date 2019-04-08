package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.Random;

public class Bytes {

    /**
     * initialize a math random with a initial secure random seed, should be enough
     * <p>
     * 使用一个安全随机数作为种子初始化伪随机数，虽然安全性不足，但是足够了
     */
    private static final Random RANDOM = new Random((new SecureRandom()).nextLong());

    /**
     * 向一个缓冲区填充随机字节
     *
     * @param bytes  目标缓冲区
     * @param offset 目标位置
     * @param length 目标长度
     */
    public static void randomBytes(@NotNull byte[] bytes, int offset, int length) {
        byte[] rand = new byte[length];
        RANDOM.nextBytes(rand);
        System.arraycopy(rand, 0, bytes, offset, length);
    }

}
