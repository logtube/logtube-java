package io.github.logtube;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogtubeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogtubeTest.class);

    @Test
    public void topic() {
        Logtube.topic("info").message("hello").commit();
        LOGGER.error("TEST ERROR");
    }

}