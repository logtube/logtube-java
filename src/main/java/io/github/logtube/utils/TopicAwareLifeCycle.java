package io.github.logtube.utils;

public abstract class TopicAwareLifeCycle extends TopicAware implements ILifeCycle {

    protected boolean isStarted = false;

    @Override
    public synchronized void start() {
        if (this.isStarted) throw new RuntimeException("already started");
        doStart();
        this.isStarted = true;
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
