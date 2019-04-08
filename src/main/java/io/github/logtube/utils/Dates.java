package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dates {

    private static final SimpleDateFormat LINE_TIMESTAMP_PREFIX_FORMAT = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS ZZZZZ]");

    public static @NotNull Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static @NotNull String yesterday_yyyyMMdd() {
        return new SimpleDateFormat("yyyyMMdd").format(yesterday());
    }

    public static @NotNull String formatLineTimestamp(long epoch) {
        return LINE_TIMESTAMP_PREFIX_FORMAT.format(new Date(epoch));
    }

}
