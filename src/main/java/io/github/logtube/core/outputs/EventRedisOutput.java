package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventOutput;
import io.github.logtube.core.topic.TopicAware;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

public class EventRedisOutput extends TopicAware implements IEventOutput {

    private final String[] hosts;

    private final String key;

    private final AtomicLong cursor = new AtomicLong();

    public EventRedisOutput(String[] hosts, String key) {
        this.hosts = hosts;
        this.key = key;
    }

    @Override
    public void appendEvent(@NotNull IEvent e) {
        throw new RuntimeException("not implemented");
    }

}
