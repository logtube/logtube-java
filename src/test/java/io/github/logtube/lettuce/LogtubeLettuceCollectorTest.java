package io.github.logtube.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class LogtubeLettuceCollectorTest {

    @Test
    public void testCollector() throws ExecutionException, InterruptedException {
        ClientResources res = DefaultClientResources.builder().commandLatencyCollector(LogtubeLettuceCollector.create()).build();
        RedisClient client = RedisClient.create(res, "redis://172.30.105.229");
        RedisAsyncCommands<String, String> commands = client.connect().async();
        RedisFuture<String> future = commands.get("key");
        String value = future.get();
        System.out.println(value);
    }

}