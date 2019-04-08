package net.landzero.xlog.perf;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XPerfTest {

    @Test
    public void create() throws InterruptedException {
        XPerfEventBuilder eb = (XPerfEventBuilder) XPerf.create("test", 1, 2, null, true);
        Thread.sleep(1000);
        XPerfEvent event = eb.build();
        assertTrue(event.getDuration() > 1000);
        assertEquals(event.getClassName(), XPerfTest.class.getCanonicalName());
        assertEquals(event.getMethodName(), "create");
        assertEquals(event.getArguments().get(0), "1");
        assertEquals(event.getArguments().get(1), "2");
        assertEquals(event.getArguments().get(2), "null");
        assertEquals(event.getArguments().get(3), "true");
    }
}