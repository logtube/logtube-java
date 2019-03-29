package io.github.logtube.outputs;

import io.github.logtube.IEventLogger;
import io.github.logtube.event.BaseEventCommitter;
import org.junit.Test;

import java.util.HashSet;

public class EventFileOutputTest {

    @Test
    public void appendEvent() {
        HashSet<String> topics = new HashSet<>();
        topics.add(IEventLogger.CLASSIC_TOPIC_INFO);
        BaseEventFileOutput o = new EventJSONFileOutput("/tmp/logtube-test", "/tmp/signal.txt");
        o.setTopics(topics);
        o.appendEvent(
                new BaseEventCommitter("example.com", "test-proj", "test-env")
                        .timestamp(System.currentTimeMillis())
                        .topic("info")
                        .message("hello, world")
                        .build()
        );
        o = new EventPlainFileOutput("/tmp/logtube-test", "/tmp/signal.txt");
        o.appendEvent(
                new BaseEventCommitter("example.com", "test-proj", "test-env")
                        .timestamp(System.currentTimeMillis())
                        .topic("access")
                        .message("hello, world")
                        .build()
        );
    }
}