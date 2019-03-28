package io.github.logtube.logger;

import io.github.logtube.IEvent;
import io.github.logtube.IEventCommitter;
import io.github.logtube.IEventOutput;
import io.github.logtube.ILogger;
import io.github.logtube.event.EventBaseCommitter;
import io.github.logtube.event.NOPEventCommitter;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.logtube.IEvent.EMPTY_TOPIC;

public abstract class BaseLogger implements ILogger, IEventOutput {

    private List<IEventOutput> outputs = new ArrayList<>();

    public void setOutputs(@NotNull List<IEventOutput> outputs) {
        this.outputs = outputs;
    }

    public void addOutput(@NotNull IEventOutput output) {
        outputs.add(output);
    }

    private Set<String> topics = new HashSet<>();

    public void addTopic(@NotNull String topic) {
        this.topics.add(topic);
    }

    public void setTopics(@NotNull Set<String> topics) {
        this.topics = topics;
    }

    public boolean isTopicEnabled(@NotNull String topic) {
        return this.topics.contains(topic);
    }

    @Override
    public @NotNull IEventCommitter topic(@Nullable String topic) {
        topic = Strings.safeString(topic, EMPTY_TOPIC);
        if (isTopicEnabled(topic)) {
            return new EventCommitter(topic);
        }
        return NOPEventCommitter.getSingleton();
    }

    @Override
    public void appendEvent(@NotNull IEvent e) {
        outputs.forEach(o -> o.appendEvent(e));
    }

    @NotNull
    public abstract String getHostname();

    @NotNull
    public abstract String getProject();

    @NotNull
    public abstract String getEnvironment();

    private class EventCommitter extends EventBaseCommitter {

        EventCommitter(@NotNull String topic) {
            super(getHostname(), getProject(), getEnvironment());
            topic(topic);
            timestamp(System.currentTimeMillis());
        }

        @Override
        public void commit() {
            appendEvent(build());
        }

    }

}
