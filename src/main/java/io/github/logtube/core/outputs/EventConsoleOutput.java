package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventSerializer;
import io.github.logtube.core.serializers.EventConsoleSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class EventConsoleOutput extends BaseEventOutput {

    private static final char[] NEW_LINE = new char[]{'\r', '\n'};

    private final IEventSerializer serializer = new EventConsoleSerializer();

    private final Writer writer = new PrintWriter(System.out);

    @Override
    public synchronized void doAppendEvent(@NotNull IEvent e) {
        try {
            serializer.serialize(e, writer);
            writer.write(NEW_LINE);
            writer.flush();
        } catch (IOException ignored) {
        }
    }

}
