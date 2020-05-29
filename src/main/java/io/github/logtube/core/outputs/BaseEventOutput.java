package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventOutput;
import io.github.logtube.utils.TopicAwareLifeCycle;
import org.jetbrains.annotations.NotNull;

public abstract class BaseEventOutput extends TopicAwareLifeCycle implements IEventOutput {

    @Override
    public void appendEvent(@NotNull IEvent e) {
        if (!this.isStarted) {
            return;
        }
        if (isTopicEnabled(e.getTopic())) {
            doAppendEvent(e);
        }
    }

    abstract void doAppendEvent(@NotNull IEvent e);

}
