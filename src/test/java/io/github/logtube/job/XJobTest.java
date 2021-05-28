package io.github.logtube.job;

import io.github.logtube.Logtube;
import io.github.logtube.core.IEventLogger;
import org.junit.Test;

public class XJobTest {

    @Test
    public void sleep1s() throws InterruptedException {
        IEventLogger eventLogger = Logtube.getLogger(XJobTest.class);
        XJobCommitter c = XJob.create(eventLogger, "sleep_1s_job", "aaaaabbbbb")
                .addKeyword("time1s", "something else")
                .markStart();
        Thread.sleep(1000);
        c.markEnd().setResult(true, "sleep succeeded").commit();
    }
}
