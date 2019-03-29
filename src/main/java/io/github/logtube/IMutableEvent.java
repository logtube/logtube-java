package io.github.logtube;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IMutableEvent extends IEvent {

    void setTimestamp(long timestamp);

    void setHostname(@Nullable String hostname);

    void setEnv(@Nullable String env);

    void setProject(@Nullable String project);

    void setTopic(@Nullable String topic);

    void setCrid(@Nullable String crid);

    void setMessage(@Nullable String message);

    void setKeyword(@Nullable String keyword);

    void setExtra(@Nullable Map<String, Object> extra);

}
