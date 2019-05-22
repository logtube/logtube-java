package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventOutput;
import io.github.logtube.core.ILifeCycle;
import io.github.logtube.core.topic.TopicAware;
import org.jetbrains.annotations.NotNull;

public abstract class BaseEventOutput extends TopicAware implements ILifeCycle, IEventOutput {

    private boolean isStarted = false;

    @Override
    public synchronized void start() {
        if (this.isStarted) throw new RuntimeException("already started");
        this.isStarted = true;
        doStart();
    }

    @Override
    public synchronized void stop() {
        if (!this.isStarted) throw new RuntimeException("not started");
        this.isStarted = false;
        doStop();
    }

    @Override
    public void appendEvent(@NotNull IEvent e) {
        if (isTopicEnabled(e.getTopic())) {
            doAppendEvent(e);
        }
    }

    protected void doStart() {
    }

    protected void doStop() {
    }

    abstract void doAppendEvent(@NotNull IEvent e);

}
