package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventOutput;
import io.github.logtube.core.IEventSerializer;
import io.github.logtube.core.serializers.EventJSONSerializer;
import io.github.logtube.core.topic.TopicAware;
import io.github.logtube.utils.SPTPClient;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EventRemoteOutput extends TopicAware implements IEventOutput {

    private static final IEventSerializer SERIALIZER = new EventJSONSerializer();

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    private final OutputStreamWriter bufferWriter = new OutputStreamWriter(buffer);

    private final String[] hosts;

    private SPTPClient client;

    public EventRemoteOutput(String[] hosts) {
        this.hosts = hosts;
    }

    @Override
    public void start() {
        this.client = new SPTPClient(this.hosts);
    }

    @Override
    public void stop() {
        this.client = null;
    }

    @Override
    public void appendEvent(@NotNull IEvent e) {
        // check topic enabled
        if (!isTopicEnabled(e.getTopic())) {
            return;
        }
        // local ref of client
        SPTPClient client = this.client;
        if (client == null) {
            return;
        }
        // write the payload
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
