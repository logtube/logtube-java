package io.github.logtube.core.serializers;

import io.github.logtube.core.IEvent;
import io.github.logtube.core.IEventSerializer;
import io.github.logtube.utils.Dates;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class BaseEventFileSerializer implements IEventSerializer {

    @Override
    public void serialize(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        w.write(Dates.formatLineTimestamp(e.getTimestamp()));
        w.write(' ');
    }

}
