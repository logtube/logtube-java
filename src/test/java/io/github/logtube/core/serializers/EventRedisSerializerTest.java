package io.github.logtube.core.serializers;

import io.github.logtube.core.events.Event;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventRedisSerializerTest {

    @Test
    public void serialize() {
        EventRedisSerializer serializer = new EventRedisSerializer();
        Event event = new Event();
        event.setTimestamp(1000);
        event.setHostname("test.localhost");
        event.setCrid("123456");
        event.setEnv("test");
        event.setProject("hello");
        event.setTopic("debug");
        event.setKeyword("world");
        event.setMessage("hello world");
        event.extra("hello", "world");

        assertEquals("{\"beat\":{\"hostname\":\"test.localhost\"},\"message\":\"[1970-01-01 08:00:01.000 +0800] {\\\"c\\\":\\\"123456\\\",\\\"m\\\":\\\"hello world\\\",\\\"k\\\":\\\"world\\\",\\\"x\\\":{\\\"hello\\\":\\\"world\\\"}}\",\"source\":\"/var/log/test/debug/hello.log\"}", serializer.toString(event));
    }
}