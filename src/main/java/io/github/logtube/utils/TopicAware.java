package io.github.logtube.utils;

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

    private boolean traceEnabled = false;
    private boolean debugEnabled = false;
    private boolean infoEnabled = false;
    private boolean warnEnabled = false;
    private boolean errorEnabled = false;
    private boolean fatalEnabled = false;

    @Override
    public void setTopics(@Nullable Set<String> topics) {
        // null for "*"
        if (topics == null) {
            this.topics = null;
            this.isTopicsBlacklist = false;
            this.updateKnownTopics();
            return;
        }
        if (topics.size() == 0) {
            this.topics = null;
            this.isTopicsBlacklist = true;
            this.updateKnownTopics();
            return;
        }
        if (topics.contains("ALL") || topics.contains("*")) {
            // blacklist mode
            HashSet<String> result = new HashSet<>();
            topics.forEach((e) -> {
                if (e.equals("ALL") || e.equals("*")) {
                    this.updateKnownTopics();
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
        this.updateKnownTopics();
    }

    private void updateKnownTopics() {
        this.traceEnabled = this.isTopicEnabled("trace");
        this.debugEnabled = this.isTopicEnabled("debug");
        this.infoEnabled = this.isTopicEnabled("info");
        this.warnEnabled = this.isTopicEnabled("warn");
        this.errorEnabled = this.isTopicEnabled("error");
        this.fatalEnabled = this.isTopicEnabled("fatal");
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

    @Override
    public boolean isTraceEnabled() {
        return this.traceEnabled;
    }

    @Override
    public boolean isDebugEnabled() {
        return this.debugEnabled;
    }

    @Override
    public boolean isInfoEnabled() {
        return this.infoEnabled;
    }

    @Override
    public boolean isWarnEnabled() {
        return this.warnEnabled;
    }

    @Override
    public boolean isErrorEnabled() {
        return this.errorEnabled;
    }

    @Override
    public boolean isFatalEnabled() {
        return this.fatalEnabled;
    }

}
