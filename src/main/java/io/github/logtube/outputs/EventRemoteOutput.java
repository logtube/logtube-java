package io.github.logtube.outputs;

import io.github.logtube.IEvent;
import io.github.logtube.IEventOutput;
import io.github.logtube.IEventSerializer;
import io.github.logtube.serializers.EventJSONSerializer;
import io.github.logtube.utils.SPTPClient;
import io.github.logtube.utils.TopicAware;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EventRemoteOutput extends TopicAware implements IEventOutput {

    private static final IEventSerializer SERIALIZER = new EventJSONSerializer();

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    private final OutputStreamWriter bufferWriter = new OutputStreamWriter(buffer);

    private final SPTPClient client;

    public EventRemoteOutput(String[] hosts) {
        this.client = new SPTPClient(hosts);
    }

    @Override
    public void appendEvent(@NotNull IEvent e) {
        if (!isTopicEnabled(e.getTopic())) {
            return;
        }
        synchronized (this) {
            buffer.reset();
            try {
                SERIALIZER.serialize(e, bufferWriter);
                bufferWriter.flush();
                client.send(buffer.toByteArray());
            } catch (IOException ignored) {
            }
        }
    }

}
