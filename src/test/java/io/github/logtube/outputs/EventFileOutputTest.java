package io.github.logtube.outputs;

import io.github.logtube.ILogger;
import io.github.logtube.event.EventBaseCommitter;
import org.junit.Test;

import java.util.HashSet;

public class EventFileOutputTest {

    @Test
    public void appendEvent() {
        HashSet<String> topics = new HashSet<>();
        topics.add(ILogger.CLASSIC_TOPIC_INFO);
        EventBaseFileOutput o = new EventJSONFileOutput("/tmp/logtube-test", "/tmp/signal.txt");
        o.setTopics(topics);
        o.appendEvent(
                new EventBaseCommitter("example.com", "test-proj", "test-env")
                        .timestamp(System.currentTimeMillis())
                        .topic("info")
                        .message("hello, world")
                        .build()
        );
        o = new EventPlainFileOutput("/tmp/logtube-test", "/tmp/signal.txt");
        o.appendEvent(
                new EventBaseCommitter("example.com", "test-proj", "test-env")
                        .timestamp(System.currentTimeMillis())
                        .topic("access")
                        .message("hello, world")
                        .build()
        );
    }
}