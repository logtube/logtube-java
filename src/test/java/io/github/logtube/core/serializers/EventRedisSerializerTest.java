package io.github.logtube.core.serializers;

import io.github.logtube.core.events.Event;
import org.junit.Test;

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

        System.out.println(serializer.toString(event));
    }
}