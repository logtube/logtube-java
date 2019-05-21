package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventOutput;
import io.github.logtube.core.IEventSerializer;
import io.github.logtube.core.serializers.EventRedisSerializer;
import io.github.logtube.core.topic.TopicAware;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

public class EventRedisOutput extends TopicAware implements IEventOutput, Runnable {

    private final IEventSerializer serializer = new EventRedisSerializer();

    private final String[] hosts;

    private final String key;

    private final AtomicLong cursor = new AtomicLong();

    public EventRedisOutput(String[] hosts, String key) {
        this.hosts = hosts;
        this.key = key;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void appendEvent(@NotNull IEvent e) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void run() {
        // check interrupted
        if (Thread.interrupted()) {
            return;
        }
    }
}
