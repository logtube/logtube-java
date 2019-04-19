package io.github.logtube.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatesTest {

    @Test
    public void formatLineTimestamp() {
        assertEquals("[2019-04-19 17:45:42.052 +0800]", Dates.formatLineTimestamp(1555667142052L));
    }

}