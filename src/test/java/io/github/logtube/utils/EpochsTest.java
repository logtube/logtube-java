package io.github.logtube.utils;

import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class EpochsTest {


    @Test
    public void beginningOfTheDay() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        assertEquals(1553702400000L, Epochs.beginningOfTheDay(1553754429477L));
    }

    @Test
    public void endOfTheDay() {

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        assertEquals(1553788799000L, Epochs.endOfTheDay(1553754429477L));
    }
}