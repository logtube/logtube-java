package io.github.logtube.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Strings {

    @Contract("_, !null -> !null")
    public static @Nullable String sanitize(@Nullable String str, @Nullable String defaultValue) {
        str = normalize(str);
        if (str == null || str.length() == 0) {
            return defaultValue;
        }
        return str.toLowerCase().replaceAll("[^0-9a-zA-Z_-]", "_");
    }

    public static boolean isEmpty(@Nullable String s) {
        if (s == null) {
            return true;
        }
        if (s.length() == 0) {
            return true;
        }
        return s.trim().length() == 0;
    }

    @Nullable
    public static String normalize(@Nullable String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        if (s.length() == 0) {
            return null;
        }
        return s;
    }

    @NotNull
    public static String safeNormalize(@Nullable String s) {
        if (s == null) {
            return "";
        }
        return s.trim();
    }

    @NotNull
    public static String safeNormalize(@Nullable Object o) {
        if (o == null) {
            return "null";
        }
        return safeNormalize(o.toString());
    }

    @NotNull
    public static String safeNormalizeKeyword(@Nullable String s) {
        s = normalize(s);
        if (s == null) {
            return "null";
        }
        return s.replaceAll("[\\s,\\[\\]]+", "_");
    }

}
