package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventSerializer;
import io.github.logtube.core.serializers.EventJSONFileSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class EventJSONFileOutput extends BaseFileOutput {

    private final IEventSerializer serializer = new EventJSONFileSerializer();

    public EventJSONFileOutput(@NotNull String dir, @NotNull String signal) {
        super(dir, signal);
    }

    @Override
    void serializeLine(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        serializer.serialize(e, w);
    }

}
