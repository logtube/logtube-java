package io.github.logtube;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventPlainLineSerializerTest {

    @Test
    public void serialize() {
        EventBuilder eb = new EventBuilder("test.example.com", "test-proj", "test");
        Event e = eb.timestamp(1553678195987L).crid("aa").message("hello, world").keyword("hello", "world").extra("duration", 1, "e", true).build();
        EventSerializer s = new EventPlainLineSerializer();
        assertEquals(s.serialize(e), "[2019-03-27 17:16:35.987 +0800] CRID[aa] KEYWORD[hello,world] hello, world");
    }
}