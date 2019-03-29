package io.github.logtube.event;

import io.github.logtube.IEventLogger;
import io.github.logtube.outputs.EventConsoleOutput;
import org.junit.Test;

import java.util.HashSet;

public class EventLoggerTest {

    @Test
    public void testConsole() {
        HashSet<String> topics = new HashSet<>();
        topics.add(IEventLogger.CLASSIC_TOPIC_DEBUG);
        EventLogger logger = new EventLogger("localhost", "test-proj", "test-env");
        logger.addOutput(new EventConsoleOutput());
        logger.setTopics(topics);
        logger.debug().keyword("hello", "world").message("hello, this cruel world").commit();
    }

}