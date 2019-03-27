package io.github.logtube.serializers;

import io.github.logtube.Event;
import io.github.logtube.EventBuilder;
import io.github.logtube.EventSerializer;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class EventFileSerializerTest {

    @Test
    public void serializeJSON() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        EventBuilder eb = new EventBuilder("test.example.com", "test-proj", "test");
        Event e = eb.timestamp(1553678195987L).message("hello, world").keyword("hello", "world").extra("duration", 1, "e", true).build();
        EventSerializer s = new EventFileSerializer(true);
        assertEquals(s.serialize(e), "[2019-03-27 17:16:35.987 +0800] {\"c\":\"-\",\"m\":\"hello, world\",\"k\":\"hello,world\",\"x\":{\"duration\":1,\"e\":true}}");
    }

    @Test
    public void serializeNonJSON() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        EventBuilder eb = new EventBuilder("test.example.com", "test-proj", "test");
        Event e = eb.timestamp(1553678195987L).crid("aa").message("hello, world").keyword("hello", "world").extra("duration", 1, "e", true).build();
        EventSerializer s = new EventFileSerializer(false);
        assertEquals(s.serialize(e), "[2019-03-27 17:16:35.987 +0800] CRID[aa] KEYWORD[hello,world] hello, world {duration=1 e=true }");
    }
}