package io.github.logtube.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.exceptions.JedisNoReachableClusterNodeException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 将内部Jedis改为XLogJedis
 *
 * @author chenkeguang 2018年10月19日
 */
public class LogtubeJedisSlotBasedConnectionHandler extends JedisSlotBasedConnectionHandler {

    protected final LogtubeJedisClusterInfoCache jedisClusterInfoCache;

    public LogtubeJedisSlotBasedConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
                                                  int timeout) {
        this(nodes, poolConfig, timeout, timeout);
    }

    public LogtubeJedisSlotBasedConnectionHandler(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig,
                                                  int connectionTimeout, int soTimeout) {
        this(nodes, poolConfig, connectionTimeout, soTimeout, null);
    }

    public LogtubeJedisSlotBasedConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig,
                                                  int connectionTimeout, int soTimeout, String password) {
        super(nodes, poolConfig, connectionTimeout, soTimeout, password);

        // 清空原先的cache
        cache.reset();
        jedisClusterInfoCache = new LogtubeJedisClusterInfoCache(poolConfig, connectionTimeout, soTimeout, password);
        initializeSlotsCache(nodes, poolConfig, password);
    }

    private void initializeSlotsCache(Set<HostAndPort> startNodes, GenericObjectPoolConfig poolConfig, String password) {
        for (HostAndPort hostAndPort : startNodes) {
            Jedis jedis = new LogtubeJedis(hostAndPort.getHost(), hostAndPort.getPort());
            if (password != null) {
                jedis.auth(password);
            }
            try {
                jedisClusterInfoCache.discoverClusterNodesAndSlots(jedis);
                break;
            } catch (JedisConnectionException e) {
                // try next nodes
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see redis.clients.jedis.JedisClusterConnectionHandler#getConnectionFromNode(redis.clients.jedis.HostAndPort)
     */
    @Override
    public Jedis getConnectionFromNode(HostAndPort node) {
        return jedisClusterInfoCache.setupNodeIfNotExist(node).getResource();
    }

    /* (non-Javadoc)
     * @see redis.clients.jedis.JedisClusterConnectionHandler#getNodes()
     */
    @Override
    public Map<String, JedisPool> getNodes() {
        return jedisClusterInfoCache.getNodes();
    }

    /* (non-Javadoc)
     * @see redis.clients.jedis.JedisSlotBasedConnectionHandler#getConnection()
     */
    @Override
    public Jedis getConnection() {
        // In antirez's redis-rb-cluster implementation,
        // getRandomConnection always return valid connection (able to
        // ping-pong)
        // or exception if all connections are invalid

        List<JedisPool> pools = jedisClusterInfoCache.getShuffledNodesPool();

        for (JedisPool pool : pools) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();

                if (jedis == null) {
                    continue;
                }

                String result = jedis.ping();

                if (result.equalsIgnoreCase("pong")) return jedis;

                jedis.close();
            } catch (JedisException ex) {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }

        throw new JedisNoReachableClusterNodeException("No reachable node in cluster");
    }

    /* (non-Javadoc)
     * @see redis.clients.jedis.JedisSlotBasedConnectionHandler#getConnectionFromSlot(int)
     */
    @Override
    public Jedis getConnectionFromSlot(int slot) {
        JedisPool connectionPool = jedisClusterInfoCache.getSlotPool(slot);
        if (connectionPool != null) {
            // It can't guaranteed to get valid connection because of node
            // assignment
            return connectionPool.getResource();
        } else {
            renewSlotCache(); //It's abnormal situation for cluster mode, that we have just nothing for slot, try to rediscover state
            connectionPool = jedisClusterInfoCache.getSlotPool(slot);
            if (connectionPool != null) {
                return connectionPool.getResource();
            } else {
                //no choice, fallback to new connection to random node
                return getConnection();
            }
        }
    }

    /* (non-Javadoc)
     * @see redis.clients.jedis.JedisClusterConnectionHandler#renewSlotCache()
     */
    @Override
    public void renewSlotCache() {
        jedisClusterInfoCache.renewClusterSlots(null);
    }

    /* (non-Javadoc)
     * @see redis.clients.jedis.JedisClusterConnectionHandler#renewSlotCache(redis.clients.jedis.Jedis)
     */
    @Override
    public void renewSlotCache(Jedis jedis) {
        jedisClusterInfoCache.renewClusterSlots(jedis);
    }

    /* (non-Javadoc)
     * @see redis.clients.jedis.JedisClusterConnectionHandler#close()
     */
    @Override
    public void close() {
        jedisClusterInfoCache.reset();
    }
}
