package io.github.logtube.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Client;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Protocol.Command;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * 重写Client类
 * 重写发送命令的sendCommand方法和接收返回值的readProtocolWithCheckingBroken方法
 * 在命令发送前记录开始时间，解析命令；接收结果后记录结束时间并记录到XLog
 *
 * @author chenkeguang 2018年10月19日
 */
public class LogtubeRedisClient extends Client {

    private RedisTrackEventCommitter builder;

    private static final Logger LOGGER = LoggerFactory.getLogger(LogtubeRedisClient.class);

    /**
     * 日志命令黑名单，执行在此名单中的命令将不记录日志
     */
    private static final List<Command> LOG_BLACK_LIST = new ArrayList<Command>();

    /**
     * 是否需要路过日志记录步骤
     */
    private boolean needSkipLog = true;

    static {
        LOG_BLACK_LIST.add(Command.PING);
        LOG_BLACK_LIST.add(Command.DEL);
    }

    public LogtubeRedisClient() {
        super();
    }

    public LogtubeRedisClient(final String host) {
        super(host);
    }

    public LogtubeRedisClient(final String host, final int port) {
        super(host, port);
    }

    public LogtubeRedisClient(final String host, final int port, final boolean ssl) {
        super(host, port, ssl);
    }

    public LogtubeRedisClient(final String host, final int port, final boolean ssl,
                              final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                              final HostnameVerifier hostnameVerifier) {
        super(host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    @Override
    protected Connection sendCommand(Command cmd, byte[]... args) {
        sendCommandBefore(cmd, args);
        return super.sendCommand(cmd, args);
    }

    @Override
    protected Object readProtocolWithCheckingBroken() {
        Object res = super.readProtocolWithCheckingBroken();
        finished(res);
        return res;
    }

    private void sendCommandBefore(Command cmd, byte[]... args) {
        needSkipLog = LOG_BLACK_LIST.contains(cmd);
        if (needSkipLog) {
            return;
        }
        try {
            builder = new RedisTrackEventCommitter();
            builder.setCmdAndArgs(cmd, args);
        } catch (Throwable e) {
            // 预防出错影响正常流程
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void finished(Object res) {
        if (needSkipLog) {
            return;
        }
        try {
            builder.commit(res);
        } catch (Throwable e) {
            // 预防出错影响正常流程
            LOGGER.error(e.getMessage(), e);
        }
    }

}
