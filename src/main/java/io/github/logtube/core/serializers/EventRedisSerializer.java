package io.github.logtube.core.serializers;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventSerializer;
import io.github.logtube.utils.ExtraJsonWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class EventRedisSerializer implements IEventSerializer {

    private final IEventSerializer jsonFileSerializer = new EventJSONFileSerializer();

    @Override
    public void serialize(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        ExtraJsonWriter j = new ExtraJsonWriter(w);
        j.beginObject();
        j.name("beat").beginObject();
        j.name("hostname").value(e.getHostname());
        j.endObject();
        j.name("message").value(jsonFileSerializer.toString(e));
        j.name("source").value("/var/log/" + e.getEnv() + "/" + e.getTopic() + "/" + e.getProject() + ".log");
        j.endObject();
    }

}
