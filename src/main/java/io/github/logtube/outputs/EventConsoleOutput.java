package io.github.logtube.outputs;

import io.github.logtube.IEvent;
import io.github.logtube.IEventOutput;
import io.github.logtube.IEventSerializer;
import io.github.logtube.serializers.EventConsoleSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class EventConsoleOutput implements IEventOutput {

    private static final char[] NEW_LINE = new char[]{'\r', '\n'};

    private final IEventSerializer serializer = new EventConsoleSerializer();

    private final Writer writer = new PrintWriter(System.out);

    @Override
    public synchronized void appendEvent(@NotNull IEvent e) {
        try {
            this.serializer.serialize(e, writer);
            writer.write(NEW_LINE);
            writer.flush();
        } catch (IOException ignored) {
        }
    }

}
