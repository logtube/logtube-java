package io.github.logtube;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public interface IEventSerializer {

    void serialize(@NotNull IEvent e, @NotNull Writer w) throws IOException;

    @NotNull
    default String toString(IEvent e) {
        StringWriter s = new StringWriter();
        try {
            serialize(e, s);
        } catch (IOException ignored) {
        }
        return s.toString();
    }

}
