package io.github.logtube.core;

import org.jetbrains.annotations.NotNull;

public interface ITopicAware {

    boolean isTopicEnabled(@NotNull String topic);

}
