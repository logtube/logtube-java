package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Strings {

    @NotNull
    public static String safeString(@Nullable String str, @NotNull String dftStr) {
        if (str == null) {
            return dftStr;
        }
        return str.toLowerCase().replaceAll("[^0-9a-zA-Z_-]", "_");
    }

    private static final SimpleDateFormat LINE_TIMESTAMP_PREFIX_FORMAT = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS ZZZZZ]");

    @NotNull
    public static String formatLineTimestampPrefix(long epoch) {
        return LINE_TIMESTAMP_PREFIX_FORMAT.format(new Date(epoch));
    }

}
