package io.github.logtube.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

    @Nullable
    @Contract("_, !null -> !null")
    public static String safeString(@Nullable String str, @Nullable String defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        str = str.trim();
        if (str.length() == 0) {
            return defaultValue;
        }
        return str.toLowerCase().replaceAll("[^0-9a-zA-Z_-]", "_");
    }

    private static final SimpleDateFormat LINE_TIMESTAMP_PREFIX_FORMAT = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS ZZZZZ]");

    @NotNull
    public static String formatLineTimestamp(long epoch) {
        return LINE_TIMESTAMP_PREFIX_FORMAT.format(new Date(epoch));
    }

    private final static char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String randomHex(int bytesLen) {
        try {
            byte[] buf = new byte[bytesLen];
            char[] out = new char[bytesLen * 2];
            new SecureRandom().nextBytes(buf);
            for (int i = 0; i < buf.length; i++) {
                int v = buf[i] & 0xFF;
                out[i * 2] = HEX_ARRAY[v >>> 4];
                out[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
            }
            return new String(out);
        } catch (Exception e) {
            return "";
        }
    }

}
