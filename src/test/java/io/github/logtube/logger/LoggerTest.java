package io.github.logtube.logger;

import io.github.logtube.ILogger;
import io.github.logtube.outputs.EventConsoleOutput;
import org.junit.Test;

import java.util.HashSet;

public class LoggerTest {

    @Test
    public void testConsole() {
        HashSet<String> topics = new HashSet<>();
        topics.add(ILogger.CLASSIC_TOPIC_DEBUG);
        Logger logger = new Logger("localhost", "test-proj", "test-env");
        logger.addOutput(new EventConsoleOutput());
        logger.setTopics(topics);
        logger.debug().keyword("hello", "world").message("hello, this cruel world").commit();
    }

}