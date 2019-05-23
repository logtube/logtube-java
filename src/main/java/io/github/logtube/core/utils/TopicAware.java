package io.github.logtube.core.utils;

import io.github.logtube.core.ITopicMutableAware;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * 将 topic 筛选逻辑封装为一个基类，提供给 日志器 和 日志输出使用
 */
public class TopicAware implements ITopicMutableAware {

    private Set<String> topics = null;

    private boolean isTopicsBlacklist = false;

    @Override
    public void setTopics(@Nullable Set<String> topics) {
        // null for "*"
        if (topics == null) {
            this.topics = null;
            this.isTopicsBlacklist = false;
            return;
        }
        if (topics.size() == 0) {
            this.topics = null;
            this.isTopicsBlacklist = true;
            return;
        }
        if (topics.contains("ALL") || topics.contains("*")) {
            // blacklist mode
            HashSet<String> result = new HashSet<>();
            topics.forEach((e) -> {
                if (e.equals("ALL") || e.equals("*")) {
                    return;
                }
                if (e.startsWith("-")) {
                    String topic = Strings.sanitize(e.substring(1), null);
                    if (topic != null) {
                        result.add(topic);
                    }
                }
            });
            if (result.size() == 0) {
                this.topics = null;
                this.isTopicsBlacklist = false;
            } else {
                this.topics = result;
                this.isTopicsBlacklist = true;
            }
        } else if (topics.contains("NONE")) {
            // NONE mode
            this.topics = null;
            this.isTopicsBlacklist = true;
        } else {
            // whitelist mode
            HashSet<String> result = new HashSet<>();
            topics.forEach((e) -> {
                String topic = Strings.sanitize(e, null);
                if (topic != null) {
                    result.add(topic);
                }
            });
            if (result.size() == 0) {
                this.topics = null;
                this.isTopicsBlacklist = true;
            } else {
                this.topics = result;
                this.isTopicsBlacklist = false;
            }
        }
    }

    @Override
    public boolean isTopicEnabled(@NotNull String topic) {
        if (this.topics == null) {
            return !this.isTopicsBlacklist;
        }
        if (this.isTopicsBlacklist) {
            return !this.topics.contains(topic);
        }
        return this.topics.contains(topic);
    }

}
