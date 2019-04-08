package net.landzero.xlog.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Collections;
import java.util.Set;

/**
 * 继承JedisCluster类，目的为了更改connectionHandler的引用类，改为引用XLogJedisSlotBasedConnectionHandler
 *
 * @author chenkeguang 2018年10月19日
 */
public class XLogJedisCluster extends JedisCluster {
    public XLogJedisCluster(HostAndPort node) {
        this(Collections.singleton(node), DEFAULT_TIMEOUT);
    }

    public XLogJedisCluster(HostAndPort node, int timeout) {
        this(Collections.singleton(node), timeout, DEFAULT_MAX_REDIRECTIONS);
    }

    public XLogJedisCluster(HostAndPort node, int timeout, int maxAttempts) {
        this(Collections.singleton(node), timeout, maxAttempts, new GenericObjectPoolConfig());
    }

    public XLogJedisCluster(HostAndPort node, final GenericObjectPoolConfig poolConfig) {
        this(Collections.singleton(node), DEFAULT_TIMEOUT, DEFAULT_MAX_REDIRECTIONS, poolConfig);
    }

    public XLogJedisCluster(HostAndPort node, int timeout, final GenericObjectPoolConfig poolConfig) {
        this(Collections.singleton(node), timeout, DEFAULT_MAX_REDIRECTIONS, poolConfig);
    }

    public XLogJedisCluster(HostAndPort node, int timeout, int maxAttempts, final GenericObjectPoolConfig poolConfig) {
        this(Collections.singleton(node), timeout, maxAttempts, poolConfig);
    }

    public XLogJedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts,
                            final GenericObjectPoolConfig poolConfig) {
        super(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, poolConfig);
        this.connectionHandler = new XLogJedisSlotBasedConnectionHandler(Collections.singleton(node), poolConfig,
                connectionTimeout, soTimeout);
    }

    public XLogJedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password,
                            final GenericObjectPoolConfig poolConfig) {
        super(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
        this.connectionHandler = new XLogJedisSlotBasedConnectionHandler(Collections.singleton(node), poolConfig,
                connectionTimeout, soTimeout, password);
    }

    public XLogJedisCluster(Set<HostAndPort> nodes) {
        this(nodes, DEFAULT_TIMEOUT);
    }

    public XLogJedisCluster(Set<HostAndPort> nodes, int timeout) {
        this(nodes, timeout, DEFAULT_MAX_REDIRECTIONS);
    }

    public XLogJedisCluster(Set<HostAndPort> nodes, int timeout, int maxAttempts) {
        this(nodes, timeout, maxAttempts, new GenericObjectPoolConfig());
    }

    public XLogJedisCluster(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig) {
        this(nodes, DEFAULT_TIMEOUT, DEFAULT_MAX_REDIRECTIONS, poolConfig);
    }

    public XLogJedisCluster(Set<HostAndPort> nodes, int timeout, final GenericObjectPoolConfig poolConfig) {
        this(nodes, timeout, DEFAULT_MAX_REDIRECTIONS, poolConfig);
    }

    public XLogJedisCluster(Set<HostAndPort> jedisClusterNode, int timeout, int maxAttempts,
                            final GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, timeout, maxAttempts, poolConfig);
        this.connectionHandler = new XLogJedisSlotBasedConnectionHandler(jedisClusterNode, poolConfig, timeout);
    }

    public XLogJedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts,
                            final GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, poolConfig);
        this.connectionHandler = new XLogJedisSlotBasedConnectionHandler(jedisClusterNode, poolConfig, connectionTimeout,
                soTimeout);
    }

    public XLogJedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts,
                            String password, final GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
        this.connectionHandler = new XLogJedisSlotBasedConnectionHandler(jedisClusterNode, poolConfig, connectionTimeout,
                soTimeout, password);
    }
}
