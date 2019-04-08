package net.landzero.xlog.redis;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;
import redis.clients.util.JedisURIHelper;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;

/**
 * 此类目的：将JedisFactory改为XLogJedisFactory
 *
 * @author chenkeguang 2018年10月19日
 */
public class XLogJedisPool extends JedisPool {
    public XLogJedisPool() {
        this(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host) {
        this(poolConfig, host, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null);
    }

    public XLogJedisPool(String host, int port) {
        this(new GenericObjectPoolConfig(), host, port, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE,
                null);
    }

    public XLogJedisPool(final String host) {
        URI uri = URI.create(host);
        if (JedisURIHelper.isValid(uri)) {
            String h = uri.getHost();
            int port = uri.getPort();
            String password = JedisURIHelper.getPassword(uri);
            int database = JedisURIHelper.getDBIndex(uri);
            boolean ssl = uri.getScheme().equals("rediss");
            this.internalPool = new GenericObjectPool<Jedis>(new XLogJedisFactory(h, port, Protocol.DEFAULT_TIMEOUT,
                    Protocol.DEFAULT_TIMEOUT, password, database, null, ssl, null, null, null),
                    new GenericObjectPoolConfig());
        } else {
            this.internalPool = new GenericObjectPool<Jedis>(
                    new XLogJedisFactory(host, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT,
                            null, Protocol.DEFAULT_DATABASE, null, false, null, null, null),
                    new GenericObjectPoolConfig());
        }
    }

    public XLogJedisPool(final String host, final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                         final HostnameVerifier hostnameVerifier) {
        URI uri = URI.create(host);
        if (JedisURIHelper.isValid(uri)) {
            String h = uri.getHost();
            int port = uri.getPort();
            String password = JedisURIHelper.getPassword(uri);
            int database = JedisURIHelper.getDBIndex(uri);
            boolean ssl = uri.getScheme().equals("rediss");
            this.internalPool = new GenericObjectPool<Jedis>(
                    new XLogJedisFactory(h, port, Protocol.DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT, password, database,
                            null, ssl, sslSocketFactory, sslParameters, hostnameVerifier),
                    new GenericObjectPoolConfig());
        } else {
            this.internalPool = new GenericObjectPool<Jedis>(
                    new XLogJedisFactory(host, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT,
                            null, Protocol.DEFAULT_DATABASE, null, false, null, null, null),
                    new GenericObjectPoolConfig());
        }
    }

    public XLogJedisPool(final URI uri) {
        this(new GenericObjectPoolConfig(), uri, Protocol.DEFAULT_TIMEOUT);
    }

    public XLogJedisPool(final URI uri, final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                         final HostnameVerifier hostnameVerifier) {
        this(new GenericObjectPoolConfig(), uri, Protocol.DEFAULT_TIMEOUT, sslSocketFactory, sslParameters,
                hostnameVerifier);
    }

    public XLogJedisPool(final URI uri, final int timeout) {
        this(new GenericObjectPoolConfig(), uri, timeout);
    }

    public XLogJedisPool(final URI uri, final int timeout, final SSLSocketFactory sslSocketFactory,
                         final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        this(new GenericObjectPoolConfig(), uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port, int timeout,
                         final String password) {
        this(poolConfig, host, port, timeout, password, Protocol.DEFAULT_DATABASE, null);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port, int timeout,
                         final String password, final boolean ssl) {
        this(poolConfig, host, port, timeout, password, Protocol.DEFAULT_DATABASE, null, ssl);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port, int timeout,
                         final String password, final boolean ssl, final SSLSocketFactory sslSocketFactory,
                         final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, password, Protocol.DEFAULT_DATABASE, null, ssl, sslSocketFactory,
                sslParameters, hostnameVerifier);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port) {
        this(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port, final boolean ssl) {
        this(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null, ssl);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port, final boolean ssl,
                         final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                         final HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null, ssl,
                sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port, final int timeout) {
        this(poolConfig, host, port, timeout, null, Protocol.DEFAULT_DATABASE, null);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port, final int timeout,
                         final boolean ssl) {
        this(poolConfig, host, port, timeout, null, Protocol.DEFAULT_DATABASE, null, ssl);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port, final int timeout,
                         final boolean ssl, final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                         final HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, null, Protocol.DEFAULT_DATABASE, null, ssl, sslSocketFactory,
                sslParameters, hostnameVerifier);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port, int timeout,
                         final String password, final int database) {
        this(poolConfig, host, port, timeout, password, database, null);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port, int timeout,
                         final String password, final int database, final boolean ssl) {
        this(poolConfig, host, port, timeout, password, database, null, ssl);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port, int timeout,
                         final String password, final int database, final boolean ssl, final SSLSocketFactory sslSocketFactory,
                         final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, password, database, null, ssl, sslSocketFactory, sslParameters,
                hostnameVerifier);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port, int timeout,
                         final String password, final int database, final String clientName) {
        this(poolConfig, host, port, timeout, timeout, password, database, clientName, false, null, null, null);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port, int timeout,
                         final String password, final int database, final String clientName, final boolean ssl) {
        this(poolConfig, host, port, timeout, timeout, password, database, clientName, ssl, null, null, null);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port, int timeout,
                         final String password, final int database, final String clientName, final boolean ssl,
                         final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                         final HostnameVerifier hostnameVerifier) {
        this(poolConfig, host, port, timeout, timeout, password, database, clientName, ssl, sslSocketFactory,
                sslParameters, hostnameVerifier);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri) {
        this(poolConfig, uri, Protocol.DEFAULT_TIMEOUT);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri,
                         final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                         final HostnameVerifier hostnameVerifier) {
        this(poolConfig, uri, Protocol.DEFAULT_TIMEOUT, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri, final int timeout) {
        this(poolConfig, uri, timeout, timeout);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri, final int timeout,
                         final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                         final HostnameVerifier hostnameVerifier) {
        this(poolConfig, uri, timeout, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                         final int connectionTimeout, final int soTimeout, final String password, final int database,
                         final String clientName, final boolean ssl, final SSLSocketFactory sslSocketFactory,
                         final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
        initPool(poolConfig, new XLogJedisFactory(host, port, connectionTimeout, soTimeout, password, database,
                clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier));
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri, final int connectionTimeout,
                         final int soTimeout) {
        initPool(poolConfig, new XLogJedisFactory(uri, connectionTimeout, soTimeout, null, false, null, null, null));
    }

    public XLogJedisPool(final GenericObjectPoolConfig poolConfig, final URI uri, final int connectionTimeout,
                         final int soTimeout, final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                         final HostnameVerifier hostnameVerifier) {
        initPool(poolConfig,
                new XLogJedisFactory(uri, connectionTimeout, soTimeout, null,
                        (uri.getScheme() != null && uri.getScheme().equals("rediss")), sslSocketFactory, sslParameters,
                        hostnameVerifier));
    }
}
