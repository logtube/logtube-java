package io.github.logtube.core;

import io.github.logtube.Logtube;
import io.github.logtube.perf.XPerf;
import io.github.logtube.perf.XPerfCommitter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogtubeTest {


    @Test
    public void binding() throws InterruptedException {
        Logtube.getProcessor().setCrid(null);
        Logger logger = LoggerFactory.getLogger(LogtubeTest.class);
        logger.info("hello world");
        logger.warn("warn test");
        logger.trace("hello world {}", "222");
        XPerfCommitter committer = XPerf.create("test", 1, 2, 3, 4);
        Thread.sleep(1000);
        committer.commit();
    }


    @Test
    public void multiThread() throws InterruptedException {
        Logtube.getProcessor().setCrid(null);
        Logger logger = LoggerFactory.getLogger(LogtubeTest.class);
        logger.info("hello world");
        Thread thread = new Thread(() -> logger.info("hello world from child thread"));
        thread.start();
    }

}
