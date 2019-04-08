package net.landzero.xlog.logback;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.landzero.xlog.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class XLogRedisAppender extends XLogBaseAppender implements Runnable {

    public static final int QUEUE_CAPACITY = 1024;

    public static final String LIST_KEY = "xlog";

    private static final Gson GSON = new Gson();

    /**
     * public variables
     */

    @NotNull
    private ArrayList<String> hosts = new ArrayList<>();

    @NotNull
    public ArrayList<String> getHosts() {
        return hosts;
    }

    public void addHost(@Nullable String url) {
        url = Strings.normalize(url);
        if (url != null) {
            String[] hosts = url.split(",");
            this.hosts.addAll(Arrays.asList(hosts));
        }
    }

    /**
     * internal variables and methods
     */
    private String source = null;

    private void initSource() {
        if (getEnv() == null || getTopic() == null || getProject() == null) {
            return;
        }
        this.source = String.format("/var/log/%s/%s/%s.log", getEnv(), getTopic(), getProject());
    }

    @NotNull
    private ArrayList<JedisPool> jedisPools = new ArrayList<>();

    @NotNull
    private AtomicInteger index = new AtomicInteger();

    @NotNull
    private String hostname = "localhost";

    @NotNull
    private ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    @Nullable
    private Thread workerThread = null;

    private boolean workerClosing = false;

    private void initHostname() {
        try {
            this.hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            this.hostname = "localhost";
        }
    }

    private void initJedisPools() {
        this.lock.lock();
        unsafeInitJedisPools();
        this.lock.unlock();
    }

    private void closeJedisPools() {
        this.lock.lock();
        unsafeCloseJedisPools();
        this.lock.unlock();
    }

    private void initWorkerThread() {
        this.lock.lock();
        unsafeInitWorkerThread();
        this.lock.unlock();
    }

    private void closeWorkerThread() {
        this.lock.lock();
        unsafeCloseWorkerThread();
        this.lock.unlock();
    }

    private void unsafeInitJedisPools() {
        unsafeCloseJedisPools();
        for (String url : getHosts()) {
            this.jedisPools.add(new JedisPool(url));
        }
    }

    private void unsafeCloseJedisPools() {
        for (JedisPool pool : this.jedisPools) {
            pool.close();
        }
        this.jedisPools = new ArrayList<>();
    }

    private void unsafeInitWorkerThread() {
        unsafeCloseWorkerThread();
        this.workerClosing = false;
        this.workerThread = new Thread(this);
        this.workerThread.start();
    }

    private void unsafeCloseWorkerThread() {
        if (this.workerThread != null) {
            this.workerClosing = true;
            this.workerThread.interrupt();
            try {
                this.workerThread.join();
            } catch (InterruptedException ignored) {
            }
            this.workerThread = null;
        }
    }

    @NotNull
    private Jedis getJedis() {
        return getJedis(this.jedisPools.size());
    }

    @NotNull
    private Jedis getJedis(int retry) {
        // if retry is too small, returns null
        if (retry < 1) {
            throw new JedisConnectionException("failed to find a reachable redis instance");
        }
        // roll the round robin
        int index = this.index.addAndGet(1);
        if (index < 0) {
            index = -index;
        }
        // find a pool and get a jedis
        try {
            return this.jedisPools.get(index % this.jedisPools.size()).getResource();
        } catch (Exception ignored) {
        }
        return getJedis(retry - 1);
    }

    @NotNull
    private String createMessage(@NotNull String message) {
        JsonObject root = new JsonObject();
        JsonObject beat = new JsonObject();
        beat.addProperty("hostname", this.hostname);
        root.add("beat", beat);
        root.addProperty("source", this.source);
        root.addProperty("message", message);
        return GSON.toJson(root);
    }

    @Override
    public void start() {
        if (this.hosts.size() == 0) {
            addError("no 'url' is specified");
            return;
        }
        initSource();
        if (this.source == null) {
            addError("failed to prepare source, check if 'env', 'topic' or 'project' field is missing");
            return;
        }
        initHostname();
        initJedisPools();
        initWorkerThread();
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        closeJedisPools();
        closeWorkerThread();
    }

    @Override
    protected void appendString(@NotNull String string) {
        // offer will only insert when there is capacity
        this.queue.offer(createMessage(string));
    }

    @Override
    public void run() {
        while (!this.workerClosing) {
            // take a message
            String message = null;
            try {
                message = this.queue.take();
            } catch (Exception ignored) {
                continue;
            }
            // push a message
            try (Jedis jedis = getJedis()) { // use try-with-resource
                jedis.rpush(LIST_KEY, message);
            } catch (Exception ignored) {
            }
        }
    }

}
