package io.github.logtube.outputs;

import io.github.logtube.event.EventBaseCommitter;
import org.junit.Test;

public class EventFileOutputTest {

    @Test
    public void appendEvent() {
        EventFileOutput fo = new EventFileOutput("/tmp/logtube-test");
        fo.enableJSONTopic("access");
        fo.appendEvent(
                new EventBaseCommitter("example.com", "test-proj", "test-env")
                        .timestamp(System.currentTimeMillis())
                        .topic("info")
                        .message("hello, world")
                        .build()
        );
        fo.appendEvent(
                new EventBaseCommitter("example.com", "test-proj", "test-env")
                        .timestamp(System.currentTimeMillis())
                        .topic("access")
                        .message("hello, world")
                        .build()
        );
    }
}