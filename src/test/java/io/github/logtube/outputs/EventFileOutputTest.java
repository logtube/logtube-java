package io.github.logtube.outputs;

import io.github.logtube.event.EventBaseCommitter;
import org.junit.Test;

public class EventFileOutputTest {

    @Test
    public void appendEvent() {
        EventBaseFileOutput o = new EventJSONFileOutput("/tmp/logtube-test");
        o.enableTopic("info");
        o.appendEvent(
                new EventBaseCommitter("example.com", "test-proj", "test-env")
                        .timestamp(System.currentTimeMillis())
                        .topic("info")
                        .message("hello, world")
                        .build()
        );
        o = new EventPlainFileOutput("/tmp/logtube-test");
        o.appendEvent(
                new EventBaseCommitter("example.com", "test-proj", "test-env")
                        .timestamp(System.currentTimeMillis())
                        .topic("access")
                        .message("hello, world")
                        .build()
        );
    }
}