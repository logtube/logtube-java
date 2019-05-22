package io.github.logtube.utils;

import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.*;

public class StringsTest {

    @Test
    public void safeString() {
        assertEquals("a_b_c", Strings.sanitize("a,b.c", "ab"));
        assertEquals("ab", Strings.sanitize(null, "ab"));
        assertEquals("a_b_c", Strings.sanitize("a,B.c", "ab"));
    }

    @Test
    public void formatTimestampLinePrefix() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        assertEquals("[2019-03-27 16:04:24.388 +0800]", Dates.formatLineTimestamp(1553673864388L));
    }

    @Test
    public void isEmpty() {
        assertTrue(io.github.logtube.utils.Strings.isEmpty(null));
        assertTrue(io.github.logtube.utils.Strings.isEmpty(""));
        assertTrue(io.github.logtube.utils.Strings.isEmpty(" "));
        assertTrue(io.github.logtube.utils.Strings.isEmpty(" \n"));
        assertTrue(io.github.logtube.utils.Strings.isEmpty(" \t"));
        assertFalse(io.github.logtube.utils.Strings.isEmpty("  a"));
    }

    @Test
    public void normalize() {
        assertTrue(io.github.logtube.utils.Strings.normalize(null) == null);
        assertTrue(io.github.logtube.utils.Strings.normalize("  ") == null);
        assertTrue(io.github.logtube.utils.Strings.normalize("\n") == null);
        assertFalse(io.github.logtube.utils.Strings.normalize("a\n") == null);
        assertEquals(io.github.logtube.utils.Strings.normalize("a\n \t"), "a");
    }

    @Test
    public void normalizeKeyword() {
        assertEquals(io.github.logtube.utils.Strings.safeNormalizeKeyword(",]]]  "), "_");
        assertEquals(Strings.safeNormalizeKeyword("a,b,c,]]dd "), "a_b_c_dd");
    }

    @Test
    public void evaluateEnvironmentVariables() {
        assertEquals("__START__" + System.getenv("HOME") + "__END____START__" + System.getenv("HOME"), Strings.evaluateEnvironmentVariables("__START__${HOME}__END____START__${HOME}"));
    }
}