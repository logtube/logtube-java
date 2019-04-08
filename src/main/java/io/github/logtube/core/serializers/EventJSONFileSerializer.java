package io.github.logtube.core.serializers;

import io.github.logtube.core.IEvent;
import io.github.logtube.utils.JSONWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class EventJSONFileSerializer extends BaseEventFileSerializer {

    @Override
    public void serialize(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        super.serialize(e, w);

        JSONWriter j = new JSONWriter(w);

        j.beginObject();
        j.name("c").value(e.getCrid());
        if (e.getMessage() != null) {
            j.name("m").value(e.getMessage());
        }
        if (e.getKeyword() != null) {
            j.name("k").value(e.getKeyword());
        }
        if (e.getExtra() != null) {
            j.name("x").beginObject();
            for (Map.Entry<String, Object> entry : e.getExtra().entrySet()) {
                j.name(entry.getKey()).value(entry.getValue());
            }
            j.endObject();
        }
        j.endObject();
    }

}
