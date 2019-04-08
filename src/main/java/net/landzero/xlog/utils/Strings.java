package net.landzero.xlog.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Strings {

    public static boolean isEmpty(@Nullable String s) {
        if (s == null)
            return true;
        if (s.length() == 0)
            return true;
        return s.trim().length() == 0;
    }

    @Nullable
    public static String normalize(@Nullable String s) {
        if (s == null)
            return null;
        s = s.trim();
        if (s.length() == 0)
            return null;
        return s;
    }

    @Nullable
    public static String normalize(@Nullable Object o) {
        if (o == null) return null;
        return normalize(o.toString());
    }

    @NotNull
    public static String safe(@Nullable String s) {
        return s == null ? "" : s;
    }

    @NotNull
    public static String safeNormalize(@Nullable String s) {
        if (s == null)
            return "";
        return s.trim();
    }

    @NotNull
    public static String safeNormalize(@Nullable Object o) {
        if (o == null) return "null";
        return safeNormalize(o.toString());
    }

    @NotNull
    public static String normalizeKeyword(@Nullable String s) {
        s = normalize(s);
        if (s == null) return "null";
        return s.replaceAll("[\\s,\\[\\]]+", "_");
    }


    @NotNull
    public static String normalizeKeyword(@Nullable Object o) {
        if (o == null) return "null";
        return normalizeKeyword(o.toString());
    }

    @NotNull
    public static String keyword(@Nullable Object... os) {
        // deny if keywords is too large
        if (os == null || os.length > 100) return "";
        return " K[" + Arrays
                .stream(os)
                // normalize
                .map(Strings::normalizeKeyword)
                // join with ','
                .collect(Collectors.joining(","))
                + "]";
    }

}
