package io.github.logtube.utils;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TopicAwareTest {

    public Set<String> easySet(String... val) {
        HashSet<String> set = new HashSet<>();
        for (String v : val) {
            set.add(v);
        }
        return set;
    }

    @Test
    public void isTopicEnabled() {
        TopicAware topicAware = new TopicAware();
        topicAware.setTopics(null);
        assertEquals(true, topicAware.isTopicEnabled("topic1"));
        assertEquals(true, topicAware.isTopicEnabled("topic2"));
        assertEquals(true, topicAware.isTopicEnabled("topic3"));
        topicAware.setTopics(easySet());
        assertEquals(false, topicAware.isTopicEnabled("topic1"));
        assertEquals(false, topicAware.isTopicEnabled("topic2"));
        assertEquals(false, topicAware.isTopicEnabled("topic3"));
        topicAware.setTopics(easySet("NONE", "topic1"));
        assertEquals(false, topicAware.isTopicEnabled("topic1"));
        assertEquals(false, topicAware.isTopicEnabled("topic2"));
        assertEquals(false, topicAware.isTopicEnabled("topic3"));
        topicAware.setTopics(easySet("NONE"));
        assertEquals(false, topicAware.isTopicEnabled("topic1"));
        assertEquals(false, topicAware.isTopicEnabled("topic2"));
        assertEquals(false, topicAware.isTopicEnabled("topic3"));
        topicAware.setTopics(easySet("ALL", "-topic1"));
        assertEquals(false, topicAware.isTopicEnabled("topic1"));
        assertEquals(true, topicAware.isTopicEnabled("topic2"));
        assertEquals(true, topicAware.isTopicEnabled("topic3"));
        topicAware.setTopics(easySet("topic1", "topic2"));
        assertEquals(true, topicAware.isTopicEnabled("topic1"));
        assertEquals(true, topicAware.isTopicEnabled("topic2"));
        assertEquals(false, topicAware.isTopicEnabled("topic3"));
    }
}