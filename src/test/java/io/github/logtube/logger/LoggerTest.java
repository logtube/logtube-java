package io.github.logtube.logger;

import io.github.logtube.ILogger;
import io.github.logtube.outputs.EventConsoleOutput;
import org.junit.Test;

public class LoggerTest {

    @Test
    public void testConsole() {
        Logger logger = new Logger("localhost", "test-proj", "test-env");
        logger.addOutput(new EventConsoleOutput());
        logger.enableTopic(ILogger.CLASSIC_TOPIC_DEBUG);
        logger.debug().keyword("hello", "world").message("hello, this cruel world").commit();
    }

}