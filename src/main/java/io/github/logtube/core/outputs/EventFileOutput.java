package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventSerializer;
import io.github.logtube.core.serializers.EventFileSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class EventFileOutput extends BaseFileOutput {

    private final IEventSerializer serializer = new EventFileSerializer();

    public EventFileOutput(@NotNull String dir, Map<String, String> subdirMappings, @NotNull String signal) {
        super(dir, subdirMappings, signal);
    }

    @Override
    void serializeLine(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        serializer.serialize(e, w);
    }

}