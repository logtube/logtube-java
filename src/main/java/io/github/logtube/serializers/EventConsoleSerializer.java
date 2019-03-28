package io.github.logtube.serializers;

import io.github.logtube.IEvent;
import io.github.logtube.IEventSerializer;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class EventConsoleSerializer implements IEventSerializer {

    @Override
    public void serialize(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        w.write(Strings.formatLineTimestamp(e.getTimestamp()));
        w.write(' ');
        w.write(e.getEnvironment());
        w.write('/');
        w.write(e.getProject());
        w.write('/');
        w.write(e.getTopic());
        w.write(" (");
        w.write(e.getCrid());
        w.write(") ");
        if (e.getKeyword() != null) {
            w.write("[");
            w.write(e.getKeyword());
            w.write("] ");
        }
        if (e.getMessage() != null) {
            w.write(e.getMessage());
        }
        if (e.getExtra() != null) {
            w.write(" {");
            for (Map.Entry<String, Object> entry : e.getExtra().entrySet()) {
                w.write(entry.getKey());
                w.write('=');
                w.write(entry.getValue().toString());
                w.write(' ');
            }
            w.write('}');
        }
    }

}
