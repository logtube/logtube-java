package io.github.logtube.redis;

import io.github.logtube.Logtube;
import io.github.logtube.core.IMutableEvent;
import io.github.logtube.utils.Flatten;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Protocol.Command;

public class RedisTrackEventCommitter {

    static private int minDuration = 0;

    static private int minResultSize = 0;

    public static void setMinDuration(int minDuration) {
        RedisTrackEventCommitter.minDuration = minDuration;
    }

    public static void setMinResultSize(int minResultSize) {
        RedisTrackEventCommitter.minResultSize = minResultSize;
    }

    private final IMutableEvent event = Logtube.getLogger(RedisTrackEventCommitter.class).topic("x-redis-track");

    private final long startTime = System.currentTimeMillis();

    public void commit(Object res) {
        final long duration = System.currentTimeMillis() - this.startTime;
        final int resultSize = Flatten.objectToByteArray(res).length;

        /*
         *  只输出
         *  耗时大于等于logtube.filter.redis-min-duration-time的redis日志
         *  返回值大于等于logtube.filter.redis-min-result-size的redis日志
         */
        if (duration >= minDuration || resultSize >= minResultSize) {
            this.event.xDuration(duration)
                    .extra("result_size", resultSize)
                    .commit();
        }
    }

    public void setCmdAndArgs(@NotNull Command cmd, byte[][] args) {
        event.extra("cmd", cmd.name());

        long paramValueSize = 0;

        // args的第1个为key。当args长度大于零时，才需要设置key及param等参数
        if (args != null && args.length > 0) {
            event.extra("key", new String(args[0]));

            // 从参数的size相同及记录
            for (int i = 1; i < args.length; i++) {
                paramValueSize += args[i].length;
            }

            // 仅当参数存在时设置paramValue、paramValueSize
            event.extra("param_value_size", paramValueSize);
        }

    }
}
