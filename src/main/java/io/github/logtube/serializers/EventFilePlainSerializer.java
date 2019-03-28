package io.github.logtube.serializers;

import io.github.logtube.IEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class EventFilePlainSerializer extends EventFileBaseSerializer {

    @Override
    public void serialize(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        super.serialize(e, w);

        w.write("CRID[");
        w.write(e.getCrid());
        w.write("] ");
        if (e.getKeyword() != null) {
            w.write("KEYWORD[");
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
