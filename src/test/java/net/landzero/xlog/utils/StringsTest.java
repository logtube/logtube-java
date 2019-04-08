package net.landzero.xlog.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringsTest {

    @Test
    public void isEmpty() {
        assertTrue(Strings.isEmpty(null));
        assertTrue(Strings.isEmpty(""));
        assertTrue(Strings.isEmpty(" "));
        assertTrue(Strings.isEmpty(" \n"));
        assertTrue(Strings.isEmpty(" \t"));
        assertFalse(Strings.isEmpty("  a"));
    }

    @Test
    public void normalize() {
        assertTrue(Strings.normalize(null) == null);
        assertTrue(Strings.normalize("  ") == null);
        assertTrue(Strings.normalize("\n") == null);
        assertFalse(Strings.normalize("a\n") == null);
        assertEquals(Strings.normalize("a\n \t"), "a");
    }

    @Test
    public void normalizeKeyword() {
        assertEquals(Strings.normalizeKeyword(",]]]  "), "_");
        assertEquals(Strings.normalizeKeyword("a,b,c,]]dd "), "a_b_c_dd");
    }
}