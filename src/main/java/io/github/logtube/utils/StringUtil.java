package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

    @NotNull
    public static String safeString(@Nullable String str, @NotNull String defaultValue) {
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

    private static final SimpleDateFormat PATH_SUFFIX_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static class PathSuffixCache {
        long from;
        long to;
        String str;
    }

    private static PathSuffixCache PATH_SUFFIX_CACHE = null;

    public static String formatPathSuffix(long epoch) {
        PathSuffixCache cache = PATH_SUFFIX_CACHE;
        if (cache != null && cache.from <= epoch && cache.to > epoch) {
            return cache.str;
        }
        cache = new PathSuffixCache();
        cache.from = EpochUtil.beginningOfTheDay(epoch);
        cache.to = EpochUtil.endOfTheDay(epoch);
        cache.str = PATH_SUFFIX_FORMAT.format(new Date(epoch));
        PATH_SUFFIX_CACHE = cache;
        return cache.str;
    }

}
