package io.github.logtube.serializers;

import io.github.logtube.IEventSerializer;
import io.github.logtube.event.Event;
import io.github.logtube.event.EventBaseCommitter;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class EventFileSerializerTest {

    @Test
    public void serializeJSON() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        EventBaseCommitter eb = new EventBaseCommitter("test.example.com", "test-proj", "test");
        Event e = eb.timestamp(1553678195987L).message("hello, world").keyword("hello", "world").extra("duration", 1, "e", true).build();
        IEventSerializer s = new EventFileJSONSerializer();
        assertEquals(s.toString(e), "[2019-03-27 17:16:35.987 +0800] {\"c\":\"-\",\"m\":\"hello, world\",\"k\":\"hello,world\",\"x\":{\"duration\":1,\"e\":true}}");
    }

    @Test
    public void serializeNonJSON() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        EventBaseCommitter eb = new EventBaseCommitter("test.example.com", "test-proj", "test");
        Event e = eb.timestamp(1553678195987L).crid("aa").message("hello, world").keyword("hello", "world").extra("duration", 1, "e", true).build();
        IEventSerializer s = new EventFilePlainSerializer();
        assertEquals(s.toString(e), "[2019-03-27 17:16:35.987 +0800] CRID[aa] KEYWORD[hello,world] hello, world {duration=1 e=true }");
    }
}