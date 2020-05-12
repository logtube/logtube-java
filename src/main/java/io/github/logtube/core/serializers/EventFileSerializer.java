package io.github.logtube.core.serializers;

import io.github.logtube.core.IEvent;
import io.github.logtube.utils.ExtraJsonWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * 构造 Logtube v2.1 日志格式
 * <p>
 * [2018-09-10 17:24:22.120 +0800] [{"c":"xxxxxxx"}] this is a message
 * <p>
 * 同时支持纯文本 和 结构化日志
 */

public class EventFileSerializer extends BaseEventFileSerializer {

    @Override
    public void serialize(@NotNull IEvent e, @NotNull Writer w) throws IOException {
        super.serialize(e, w);

        ExtraJsonWriter j = new ExtraJsonWriter(w);

        j.beginArray();
        j.beginObject();
        j.name("c").value(e.getCrid());
        if (e.getKeyword() != null) {
            j.name("k").value(e.getKeyword());
        }
        if (e.getExtra() != null) {
            j.name("x").beginObject();
            for (Map.Entry<String, Object> entry : e.getExtra().entrySet()) {
                j.name(entry.getKey());
                j.value(entry.getValue());
            }
            j.endObject();
        }
        j.endObject();
        j.endArray();
        w.write(' ');

        if (e.getMessage() != null) {
            w.write(e.getMessage());
        }
    }

}
