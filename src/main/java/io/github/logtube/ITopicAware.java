package io.github.logtube;

import org.jetbrains.annotations.NotNull;

public interface ITopicAware {

    boolean isTopicEnabled(@NotNull String topic);

}
