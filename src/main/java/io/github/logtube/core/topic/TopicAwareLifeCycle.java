package io.github.logtube.core.topic;

import io.github.logtube.core.ILifeCycle;

public abstract class TopicAwareLifeCycle extends TopicAware implements ILifeCycle {

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

    public void doStart() {
    }

    public void doStop() {
    }

}
