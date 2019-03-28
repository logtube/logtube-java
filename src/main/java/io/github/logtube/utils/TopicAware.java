package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class TopicAware {

    private Set<String> topics = new HashSet<>();

    public void setTopics(@NotNull Set<String> topics) {
        this.topics = topics;
    }

    public void enableTopic(@NotNull String topic) {
        this.topics.add(topic);
    }

    public void disableTopic(@NotNull String topic) {
        this.topics.remove(topic);
    }

    public boolean isTopicEnabled(@NotNull String topic) {
        return this.topics.contains(topic);
    }

}
