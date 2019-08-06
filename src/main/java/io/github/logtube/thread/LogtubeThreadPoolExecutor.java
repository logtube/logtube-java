package io.github.logtube.thread;

import io.github.logtube.Logtube;
import io.github.logtube.core.IEventContext;

import java.util.concurrent.*;

public class LogtubeThreadPoolExecutor extends ThreadPoolExecutor {

    private final ConcurrentHashMap<Runnable, IEventContext> eventContexts = new ConcurrentHashMap<>();

    public LogtubeThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public LogtubeThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public LogtubeThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public LogtubeThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        // Child Thread:
        // restore the EventContext, i.e. crid and path
        IEventContext context = eventContexts.get(r);
        if (context != null) {
            context.restore();
        }
    }

    @Override
    public void execute(Runnable command) {
        // Main Thread:
        // save the EventContext and relate to the Runnable
        eventContexts.put(command, Logtube.captureContext());
        super.execute(command);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        // ChildThread:
        // clear Logtube's ThreadLocal variables
        Logtube.getProcessor().clearContext();
        // clear the EventContext related to the Runnable
        eventContexts.remove(r);
        super.afterExecute(r, t);
    }

}
