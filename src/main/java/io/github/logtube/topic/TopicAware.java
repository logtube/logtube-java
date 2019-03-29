package io.github.logtube.topic;

import io.github.logtube.ITopicMutableAware;
import io.github.logtube.utils.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class TopicAware implements ITopicMutableAware {

    private Set<String> topics = null;

    private boolean isBlacklistTopics = false;

    @Override
    public void setTopics(@Nullable Set<String> topics) {
        // null for "*"
        if (topics == null) {
            this.topics = null;
            this.isBlacklistTopics = false;
            return;
        }
        HashSet<String> result = new HashSet<>();
        if (topics.contains("*")) {
            // blacklist mode
            topics.remove("*");
            topics.forEach((e) -> {
                if (e.startsWith("-")) {
                    String topic = StringUtil.safeString(e.substring(1), null);
                    if (topic != null) {
                        result.add(topic);
                    }
                }
            });
            this.isBlacklistTopics = true;
        } else {
            // whitelist mode
            topics.forEach((e) -> {
                String topic = StringUtil.safeString(e, null);
                if (topic != null) {
                    result.add(topic);
                }
            });
            this.isBlacklistTopics = false;
        }

        this.topics = result;
    }

    @Override
    public boolean isTopicEnabled(@NotNull String topic) {
        if (this.topics == null) {
            return true;
        }
        if (this.isBlacklistTopics) {
            return !this.topics.contains(topic);
        }
        return this.topics.contains(topic);
    }

}
