package io.github.logtube.lettuce;

import io.github.logtube.Logtube;
import io.github.logtube.core.IMutableEvent;
import io.lettuce.core.metrics.*;
import io.lettuce.core.protocol.ProtocolKeyword;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class LogtubeLettuceCollector implements CommandLatencyCollector {

    private static final Map<CommandLatencyId, CommandMetrics> EMPTY_METRICS = new HashMap<>();

    private final CommandLatencyCollector collector;

    public static CommandLatencyCollector create() {
        return new LogtubeLettuceCollector();
    }

    public static CommandLatencyCollector create(CommandLatencyCollector collector) {
        return new LogtubeLettuceCollector(collector);
    }

    public static CommandLatencyCollector create(DefaultCommandLatencyCollectorOptions options) {
        return new LogtubeLettuceCollector(options);
    }

    private LogtubeLettuceCollector() {
        if (DefaultCommandLatencyCollector.isAvailable()) {
            this.collector = CommandLatencyCollector.create(DefaultCommandLatencyCollectorOptions.create());
        } else {
            this.collector = null;
        }
    }

    private LogtubeLettuceCollector(DefaultCommandLatencyCollectorOptions options) {
        this.collector = CommandLatencyCollector.create(options);
    }

    private LogtubeLettuceCollector(CommandLatencyCollector collector) {
        this.collector = collector;
    }

    @Override
    public void recordCommandLatency(SocketAddress local, SocketAddress remote, ProtocolKeyword commandType, long firstResponseLatency, long completionLatency) {
        if (this.collector != null) {
            this.collector.recordCommandLatency(local, remote, commandType, firstResponseLatency, completionLatency);
        }

        IMutableEvent event = Logtube.getLogger(LogtubeLettuceCollector.class).topic("x-redis-track");
        if (remote instanceof InetSocketAddress) {
            InetSocketAddress address = (InetSocketAddress) remote;
            event.extra("db_host", address.toString());
        }
        event.extra("cmd", commandType.name())
                .xDuration(completionLatency / (1000 * 1000)) // nanosecond
                .commit();
    }

    @Override
    public void shutdown() {
        if (this.collector != null) {
            this.collector.shutdown();
        }
    }

    @Override
    public Map<CommandLatencyId, CommandMetrics> retrieveMetrics() {
        if (this.collector != null) {
            return this.collector.retrieveMetrics();
        }
        return EMPTY_METRICS;
    }

    @Override
    public boolean isEnabled() {
        // always return true
        return true;
    }

}
