package io.github.logtube.utils;

import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;

public class ExtraJsonWriter extends JsonWriter {

    public ExtraJsonWriter(@NotNull Writer out) {
        super(out);
    }

    public void value(@Nullable Object value) throws IOException {
        if (value == null) {
            nullValue();
        } else if (value instanceof Number) {
            value((Number) value);
        } else if (value instanceof String) {
            value((String) value);
        } else if (value instanceof Boolean) {
            value((Boolean) value);
        } else {
            value(value.toString());
        }
    }

}
