package io.github.logtube.event;

import io.github.logtube.IEvent;
import io.github.logtube.IEventCommitter;
import io.github.logtube.IEventLogger;
import io.github.logtube.IEventOutput;
import io.github.logtube.utils.StringUtil;
import io.github.logtube.utils.TopicAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.github.logtube.IEvent.EMPTY_TOPIC;

public abstract class BaseEventLogger extends TopicAware implements IEventLogger, IEventOutput {

    private List<IEventOutput> outputs = new ArrayList<>();

    public void setOutputs(@NotNull List<IEventOutput> outputs) {
        this.outputs = outputs;
    }

    public void addOutput(@NotNull IEventOutput output) {
        outputs.add(output);
    }

    @Override
    public @NotNull IEventCommitter topic(@Nullable String topic) {
        topic = StringUtil.safeString(topic, EMPTY_TOPIC);
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
    public abstract String getEnv();

    private class EventCommitter extends BaseEventCommitter {

        EventCommitter(@NotNull String topic) {
            super(getHostname(), getProject(), getEnv());
            topic(topic);
            timestamp(System.currentTimeMillis());
        }

        @Override
        public void commit() {
            appendEvent(build());
        }

    }

}
