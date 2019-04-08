package net.landzero.xlog.redis;

import net.landzero.xlog.utils.Flatten;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Protocol.Command;

import java.util.StringJoiner;

public class TrackEventBuilder {

    private TrackEvent event = new TrackEvent();

    private long startTime = System.currentTimeMillis();

    public TrackEvent build(Object res) {
        event.setDuration(System.currentTimeMillis() - this.startTime);
        event.setResultSize(Flatten.objectToByteArray(res).length);
        return this.event;
    }

    public void setCmdAndArgs(@NotNull Command cmd, byte[][] args) {
        event.setCmd(cmd.name());

        StringJoiner paramValueJoiner = new StringJoiner(",");
        long paramValueSize = 0;

        // args的第1个为key。当args长度大于零时，才需要设置key及param等参数
        if (args != null && args.length > 0) {
            event.setKey(new String(args[0]));

            // 从参数的size相同及记录
            for (int i = 1; i < args.length; i++) {
                paramValueSize += args[i].length;
                paramValueJoiner.add(new String(args[i]));
            }

            // 仅当参数存在时设置paramValue、paramValueSize
            if (paramValueSize > 0) {
                event.setParamValueSize(paramValueSize);

                // set命令不记录参数值内容
                if (!cmd.equals(Command.SET)) {
                    event.setParamValue(paramValueJoiner.toString());
                }
            }

        }

    }
}
