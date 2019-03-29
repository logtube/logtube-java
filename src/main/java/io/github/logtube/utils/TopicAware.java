package io.github.logtube.utils;

import io.github.logtube.IEventLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class TopicAware {

    private Set<String> topics = null;

    private boolean blacklist = false;

    public void setTopics(@Nullable Set<String> topics) {
        // null for "*"
        if (topics == null) {
            this.topics = null;
            this.blacklist = false;
            return;
        }
        HashSet<String> result = new HashSet<>();
        if (topics.contains("*")) {
            // blacklist mode
            topics.remove("*");
            topics.forEach((e) -> {
                if (e.startsWith("-")) {
                    result.add(StringUtil.safeString(e.substring(1), IEventLogger.CLASSIC_TOPIC_INFO));
                }
            });
            this.blacklist = true;
        } else {
            // whitelist mode
            topics.forEach((e) -> result.add(StringUtil.safeString(e, IEventLogger.CLASSIC_TOPIC_INFO)));
            this.blacklist = false;
        }

        this.topics = result;
    }

    public boolean isTopicEnabled(@NotNull String topic) {
        if (this.topics == null) {
            return true;
        }
        if (this.blacklist) {
            return !this.topics.contains(topic);
        }
        return this.topics.contains(topic);
    }

}
