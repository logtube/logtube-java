package io.github.logtube.outputs;

import io.github.logtube.IEvent;
import io.github.logtube.IEventSerializer;
import io.github.logtube.serializers.EventPlainFileSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class EventPlainFileOutput extends EventBaseFileOutput {

    private static final IEventSerializer SERIALIZER = new EventPlainFileSerializer();

    public EventPlainFileOutput(@NotNull String dir) {
        super(dir);
    }

    @Override
    void serializeLine(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        SERIALIZER.serialize(e, w);
    }

}
