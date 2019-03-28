package io.github.logtube.serializers;

import io.github.logtube.IEventSerializer;
import io.github.logtube.event.Event;
import io.github.logtube.event.EventBaseCommitter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventConsoleSerializerTest {

    @Test
    public void serialize() {
        EventBaseCommitter eb = new EventBaseCommitter("test.example.com", "test-proj", "test");
        Event e = eb.timestamp(1553678195987L).crid("aa").message("hello, world").keyword("hello", "world").extra("duration", 1, "e", true).build();
        IEventSerializer s = new EventConsoleSerializer();
        assertEquals(s.toString(e), "[2019-03-27 17:16:35.987 +0800] test/test-proj/info (aa) [hello,world] hello, world {duration=1 e=true }");
    }
}