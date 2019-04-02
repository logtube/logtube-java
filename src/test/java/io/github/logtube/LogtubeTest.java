package io.github.logtube;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogtubeTest {


    @Test
    public void binding() {
        Logtube.getRootLogger().setCrid(null);
        Logger logger = LoggerFactory.getLogger(LogtubeTest.class);
        logger.info("hello world");
    }

}
