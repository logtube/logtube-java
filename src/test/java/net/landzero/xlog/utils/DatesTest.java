package net.landzero.xlog.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class DatesTest {

    @Test
    public void yesterday() {
        Date now = new Date();
        Date yesterday = Dates.yesterday();
        long diff = now.getTime() - yesterday.getTime();
        long devi = Math.abs(diff - 60 * 60 * 24 * 1000);
        assertTrue(devi < 500);
    }

    @Test
    public void yesterday_yyyyMMdd() {
    }
}