package io.github.logtube;

import io.github.logtube.utils.JSONWriter;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class EventJSONLineSerializer implements EventSerializer {

    @Override
    @NotNull
    public String serialize(@NotNull Event e) {
        StringWriter s = new StringWriter();
        s.write(Strings.formatLineTimestampPrefix(e.getTimestamp()));
        s.write(' ');
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
        return s.toString();
    }

}
