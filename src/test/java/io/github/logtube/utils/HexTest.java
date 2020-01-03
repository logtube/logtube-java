package io.github.logtube.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HexTest {

    @Test
    public void toHex() {
        assertEquals(Hex.toHex((byte) 0x2d), "2d");
        assertEquals(Hex.toHex((byte) 0x0a), "0a");
        assertEquals(Hex.toHex(0x00000000000d0002), "00000000000d0002");
    }

    @Test
    public void md5() {
        assertEquals(Hex.md5("aaa"), "47bce5c74f589f4867dbd57e9ca9f808");
        assertEquals(Hex.md5("bbc"), "99be496ab9ad1cd2b9910cecf142235a");
        assertEquals(Hex.md5("ccc"), "9df62e693988eb4e1e1444ece0578579");
    }
}