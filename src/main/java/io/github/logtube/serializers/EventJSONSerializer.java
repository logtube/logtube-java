package io.github.logtube.serializers;

import io.github.logtube.Event;
import io.github.logtube.EventSerializer;
import io.github.logtube.utils.JSONWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class EventJSONSerializer implements EventSerializer {

    @Override
    @NotNull
    public String serialize(@NotNull Event e) {
        // serialize manually
        StringWriter s = new StringWriter();
        JSONWriter w = new JSONWriter(s);
        try {
            w.beginObject();
            w.name("t").value(e.getTimestamp());
            w.name("h").value(e.getHostname());
            w.name("e").value(e.getEnv());
            w.name("p").value(e.getProject());
            w.name("o").value(e.getTopic());
            w.name("c").value(e.getCrid());
            if (e.getMessage() != null) {
                w.name("m").value(e.getMessage());
            }
            if (e.getKeyword() != null) {
                w.name("k").value(e.getKeyword());
            }
            if (e.getExtra() != null) {
                w.name("x").beginObject();
                for (Map.Entry<String, Object> entry : e.getExtra().entrySet()) {
                    w.name(entry.getKey()).value(entry.getValue());
                }
                w.endObject();
            }
            w.endObject();
            w.close();
        } catch (IOException ignored) {
        }
        return s.toString();
    }

}
