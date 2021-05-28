package io.github.logtube.job;

import io.github.logtube.core.IEventLogger;
import org.jetbrains.annotations.NotNull;

public class XJob {

    public static XJobCommitter create(IEventLogger logger, @NotNull String jobName) {
        return new XJobCommitter(logger.topic("job")).setJobName(jobName);
    }

    public static XJobCommitter create(IEventLogger logger, @NotNull String jobName, @NotNull String jobId) {
        return new XJobCommitter(logger.topic("job")).setJobName(jobName).setJobId(jobId);
    }

}
