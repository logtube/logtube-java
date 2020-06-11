package io.github.logtube.http;

import io.github.logtube.LogtubeComponentConfigurator;
import io.github.logtube.LogtubeOptions;
import org.jetbrains.annotations.NotNull;

public class LogtubeHttpConfigurator implements LogtubeComponentConfigurator {

    @Override
    public void configure(@NotNull LogtubeOptions options) {
        LogtubeHttpFilter.GLOBAL_HTTP_IGNORES = options.getHttpIgnores();
    }

}
