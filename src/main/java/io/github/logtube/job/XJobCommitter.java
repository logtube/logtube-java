package io.github.logtube.job;

import io.github.logtube.core.IMutableEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class XJobCommitter {

    private final IMutableEvent event;

    private long startedAt = System.currentTimeMillis();

    public XJobCommitter(@NotNull IMutableEvent event) {
        this.event = event;
    }

    @Contract("_->this")
    public XJobCommitter setJobName(@NotNull String jobName) {
        this.event.extra("job_name", jobName);
        return this;
    }

    @Contract("_->this")
    public XJobCommitter setJobId(@NotNull String jobId) {
        this.event.extra("job_id", jobId);
        return this;
    }

    @Contract("_->this")
    public XJobCommitter addKeyword(@NotNull Object... keyword) {
        this.event.keyword(keyword);
        return this;
    }

    @Contract("->this")
    public XJobCommitter markStart() {
        return this.markStart(System.currentTimeMillis());
    }

    @Contract("_->this")
    public XJobCommitter markStart(long epoch) {
        this.startedAt = epoch;
        this.event.extra("started_at", epoch);
        try {
            ((IMutableEvent) this.event.clone()).extra("result", "started").commit();
        } catch (CloneNotSupportedException ignored) {
        }
        return this;
    }

    @Contract("->this")
    public XJobCommitter markEnd() {
        return this.markEnd(System.currentTimeMillis());
    }

    @Contract("_->this")
    public XJobCommitter markEnd(long epoch) {
        this.event.extra("ended_at", epoch)
                .extra("duration", epoch - this.startedAt);
        return this;
    }

    @Contract("_,_->this")
    public XJobCommitter setResult(boolean success, @Nullable String message) {
        if (success) {
            this.event.extra("result", "ok");
        } else {
            this.event.extra("result", "failed");
        }
        this.event.message(message);
        return this;
    }

    public void commit() {
        this.event.commit();
    }

}
