package io.github.logtube;

import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;

public class EventPlainLineSerializer implements EventSerializer {

    @Override
    public @NotNull String serialize(@NotNull Event e) {
        StringBuilder sb = new StringBuilder(Strings.formatLineTimestampPrefix(e.getTimestamp()));
        sb.append(" CRID[");
        sb.append(e.getCrid());
        sb.append("] ");
        if (e.getKeyword() != null) {
            sb.append("KEYWORD[");
            sb.append(e.getKeyword());
            sb.append("] ");
        }
        if (e.getMessage() != null) {
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

}
