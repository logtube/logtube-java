package io.github.logtube.serializers;

import io.github.logtube.Event;
import io.github.logtube.EventSerializer;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;

public class EventConsoleSerializer implements EventSerializer {

    @Override
    public @NotNull String serialize(@NotNull Event e) {
        StringBuilder s = new StringBuilder();
        s.append(Strings.formatLineTimestampPrefix(e.getTimestamp()));
        s.append(' ');
        s.append(e.getEnv());
        s.append('/');
        s.append(e.getProject());
        s.append('/');
        s.append(e.getTopic());
        s.append(" (");
        s.append(e.getCrid());
        s.append(")");
        if (e.getKeyword() != null) {
            s.append(" [");
            s.append(e.getKeyword());
            s.append("] ");
        }
        if (e.getMessage() != null) {
            s.append(e.getMessage());
        }
        if (e.getExtra() != null) {
            s.append(" {");
            e.getExtra().forEach((k, v) -> {
                s.append(k);
                s.append('=');
                s.append(v.toString());
                s.append(' ');
            });
            s.append('}');
        }
        return s.toString();
    }

}
