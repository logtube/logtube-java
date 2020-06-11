package io.github.logtube.redis;

import io.github.logtube.LogtubeComponentConfigurator;
import io.github.logtube.LogtubeOptions;
import org.jetbrains.annotations.NotNull;

public class LogtubeJedisConfigurator implements LogtubeComponentConfigurator {

    @Override
    public void configure(@NotNull LogtubeOptions options) {
        RedisTrackEventCommitter.setMinDuration(options.getRedisMinDuration());
        RedisTrackEventCommitter.setMinResultSize(options.getRedisMinResultSize());
    }

}
