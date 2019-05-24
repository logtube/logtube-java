package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;

public interface ITopicAware {

    boolean isTopicEnabled(@NotNull String topic);

}
