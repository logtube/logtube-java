package io.github.logtube.core.serializers;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventSerializer;
import io.github.logtube.utils.Dates;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class EventConsoleSerializer implements IEventSerializer {

    @Override
    public void serialize(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        w.write(Dates.formatLineTimestamp(e.getTimestamp()));
        w.write(" [");
        w.write(e.getTopic());
        w.write("] (");
        w.write(e.getCrsrc());
        w.write("/");
        w.write(e.getCrid());
        w.write(") ");
        if (e.getKeyword() != null) {
            w.write("[");
            w.write(e.getKeyword());
            w.write("] ");
        }
        Map<String, Object> extra = e.getExtra();
        if (extra != null && !extra.isEmpty()) {
            w.write('{');
            for (Map.Entry<String, Object> entry : extra.entrySet()) {
                w.write(entry.getKey());
                w.write('=');
                w.write(entry.getValue().toString());
                w.write(',');
            }
            w.write("} ");
        }
        if (e.getMessage() != null) {
            w.write(e.getMessage());
        }
    }
}
