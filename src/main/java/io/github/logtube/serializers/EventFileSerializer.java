package io.github.logtube.serializers;

import io.github.logtube.Event;
import io.github.logtube.EventSerializer;
import io.github.logtube.utils.JSONWriter;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class EventFileSerializer implements EventSerializer {

    private final boolean isJSON;

    public EventFileSerializer(boolean isJSON) {
        this.isJSON = isJSON;
    }

    @Override
    public @NotNull String serialize(@NotNull Event e) {
        StringWriter s = new StringWriter();
        s.write(Strings.formatLineTimestampPrefix(e.getTimestamp()));
        s.write(' ');
        if (isJSON) {
            JSONWriter w = new JSONWriter(s);
            try {
                w.beginObject();
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
        } else {
            s.write("CRID[");
            s.write(e.getCrid());
            s.write("] ");
            if (e.getKeyword() != null) {
                s.write("KEYWORD[");
                s.write(e.getKeyword());
                s.write("] ");
            }
            if (e.getMessage() != null) {
                s.write(e.getMessage());
            }
            if (e.getExtra() != null) {
                s.write(" {");
                e.getExtra().forEach((k, v) -> {
                    s.write(k);
                    s.write('=');
                    s.write(v.toString());
                    s.write(' ');
                });
                s.write('}');
            }
        }
        return s.toString();
    }

}
