package io.github.logtube.core.outputs;

import org.junit.Test;

import java.util.HashMap;

public class EventFileOutputTest {

    @Test
    public void testDeadlock() {
        EventFileOutput efo = new EventFileOutput("logs", new HashMap<>(), "signal");
        EventFileOutput efo1 = new EventFileOutput("logs1", new HashMap<>(), "signal");
        efo.start();
        efo1.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        efo.stop();
        efo1.stop();
    }

}