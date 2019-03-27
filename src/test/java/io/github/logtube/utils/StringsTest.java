package io.github.logtube.utils;

import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class StringsTest {

    @org.junit.Test
    public void safeString() {
        assertEquals("a_b_c", Strings.safeString("a,b.c", "ab"));
        assertEquals("ab", Strings.safeString(null, "ab"));
        assertEquals("a_b_c", Strings.safeString("a,B.c", "ab"));
    }

    @Test
    public void formatTimestampLinePrefix() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        assertEquals("[2019-03-27 16:04:24.388 +0800]", Strings.formatLineTimestampPrefix(1553673864388L));
    }
}