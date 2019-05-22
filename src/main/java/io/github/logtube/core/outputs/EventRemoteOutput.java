package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventSerializer;
import io.github.logtube.core.serializers.EventJSONSerializer;
import io.github.logtube.utils.SPTPClient;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EventRemoteOutput extends BaseEventOutput {

    private final IEventSerializer serializer = new EventJSONSerializer();

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    private final OutputStreamWriter bufferWriter = new OutputStreamWriter(buffer);

    private final String[] hosts;

    private SPTPClient client;

    public EventRemoteOutput(String[] hosts) {
        this.hosts = hosts;
    }

    @Override
    public void doStart() {
        super.doStart();
        this.client = new SPTPClient(this.hosts);
    }

    @Override
    public void doStop() {
        this.client = null;
        super.doStop();
    }

    @Override
    public void doAppendEvent(@NotNull IEvent e) {
        // local ref of client
        SPTPClient client = this.client;
        if (client == null) {
            return;
        }
        // write the payload
        synchronized (this) {
            buffer.reset();
            try {
                serializer.serialize(e, bufferWriter);
                bufferWriter.flush();
                client.send(buffer.toByteArray());
            } catch (IOException ignored) {
            }
        }
    }

}
