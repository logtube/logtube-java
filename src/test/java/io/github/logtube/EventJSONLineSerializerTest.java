package io.github.logtube;

import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class EventJSONLineSerializerTest {

    @Test
    public void serialize() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        EventBuilder eb = new EventBuilder("test.example.com", "test-proj", "test");
        Event e = eb.timestamp(1553678195987L).message("hello, world").keyword("hello", "world").extra("duration", 1, "e", true).build();
        EventSerializer s = new EventJSONLineSerializer();
        assertEquals(s.serialize(e), "[2019-03-27 17:16:35.987 +0800] {\"c\":\"-\",\"m\":\"hello, world\",\"k\":\"hello,world\",\"x\":{\"duration\":1,\"e\":true}}");
    }

}