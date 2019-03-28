package io.github.logtube.serializers;

import io.github.logtube.IEvent;
import io.github.logtube.IEventSerializer;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class EventFileBaseSerializer implements IEventSerializer {

    @Override
    public void serialize(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        w.write(Strings.formatLineTimestampPrefix(e.getTimestamp()));
        w.write(' ');
    }

}
