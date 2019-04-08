package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventOutput;
import io.github.logtube.core.IEventSerializer;
import io.github.logtube.core.serializers.EventConsoleSerializer;
import io.github.logtube.core.topic.TopicAware;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class EventConsoleOutput extends TopicAware implements IEventOutput {

    private static final char[] NEW_LINE = new char[]{'\r', '\n'};

    private static final IEventSerializer SERIALIZER = new EventConsoleSerializer();

    private final Writer writer = new PrintWriter(System.out);

    @Override
    public synchronized void appendEvent(@NotNull IEvent e) {
        if (!isTopicEnabled(e.getTopic())) {
            return;
        }
        synchronized (this) {
            try {
                SERIALIZER.serialize(e, writer);
                writer.write(NEW_LINE);
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

}
