package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

/**
 * hex related util, lowercase
 * <p>
 * 16进制相关的工具类，小写字母
 */
public class Hex {

    /**
     * initialize a math random with a initial secure random seed, should be enough
     * <p>
     * 使用一个安全随机数作为种子初始化伪随机数，虽然安全性不足，但是对于 CRID 足够了
     */
    private static final Random RANDOM = new Random((new SecureRandom()).nextLong());

    private final static char[] HEX_CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * create a random 16-hex crid
     * <p>
     * 创建一个16位随机二进制字符串，可以用来做 CRID
     *
     * @return random hex
     */
    @NotNull
    public static String randomHex16() {
        return toHex(RANDOM.nextLong());
    }

    /**
     * create a hex string representation of a long, length is 16
     * <p>
     * 将 long (int64) 转换为16进制字符串，长度为16
     *
     * @param value long
     * @return hex string
     */
    @NotNull
    public static String toHex(long value) {
        char[] chars = new char[16];
        int i, c;

        for (i = 0; i < 16; i++) {
            c = (int) (value & 0xf);
            chars[16 - i - 1] = HEX_CHARS[c];
            value = value >> 4;
        }
        return new String(chars);
    }

    @NotNull
    public static String toHex(byte b) {
        return new String(new char[]{HEX_CHARS[(b & 0xf0) >> 4], HEX_CHARS[(b & 0x0f)]});
    }

    /**
     * create md5 digest of utf8 string
     * <p>
     * 获取 MD5 摘要
     *
     * @param str string to digest
     * @return digest of empty string if failed
     */
    @Nullable
    public static String md5(@Nullable String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] result = md5.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder buffer = new StringBuilder();
            for (byte b : result) {
                buffer.append(HEX_CHARS[(b & 0xf0) >> 4]);
                buffer.append(HEX_CHARS[(b & 0x0f)]);
            }
            return buffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

}
