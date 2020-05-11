package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;

public interface ITopicAware {

    boolean isTopicEnabled(@NotNull String topic);

    boolean isTraceEnabled();

    boolean isDebugEnabled();

    boolean isInfoEnabled();

    boolean isWarnEnabled();

    boolean isErrorEnabled();

    boolean isFatalEnabled();

}
