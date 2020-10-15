package io.github.logtube.core.outputs;

import io.github.logtube.core.IEvent;
import io.github.logtube.utils.IntervalChecker;
import io.github.logtube.utils.SignalChecker;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class BaseFileOutput extends BaseEventOutput {

    private static final int QUEUE_CAPACITY = 1024;

    private static final String SUBDIR_OTHERS = "others";

    private static final char[] NEW_LINE = new char[]{'\r', '\n'};

    private final String dir;

    private final SignalChecker signalChecker;

    private final IntervalChecker signalIntervalChecker = new IntervalChecker(30 * 1000);

    private final HashMap<String, FileWriter> writers = new HashMap<>();

    private final Map<String, String> subdirMappings;

    @NotNull
    private final ArrayBlockingQueue<IEvent> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    private EventFileOutputWorker worker = null;

    public BaseFileOutput(@NotNull String dir, Map<String, String> subdirMappings, @NotNull String signal) {
        this.dir = dir;
        this.subdirMappings = subdirMappings;
        this.signalChecker = new SignalChecker(signal);
    }

    private void closeWriters() throws IOException {
        for (Map.Entry<String, FileWriter> entry : writers.entrySet()) {
            entry.getValue().close();
        }
        writers.clear();
    }

    private void closeWritersIfNeeded() throws IOException {
        // skip if recently checked
        if (!this.signalIntervalChecker.check()) {
            return;
        }
        // skip if signal file not modified
        if (!this.signalChecker.check()) {
            return;
        }
        // reopen file
        closeWriters();
    }

    private synchronized FileWriter getWriter(@NotNull IEvent e) throws IOException {
        // close unused files
        closeWritersIfNeeded();

        FileWriter w = this.writers.get(e.getTopic());
        if (w == null) {
            // calculate full file path
            String subdir = this.subdirMappings.get(e.getTopic());
            Path path;
            if (subdir == null) {
                String wildcard = this.subdirMappings.get("ALL");
                if (wildcard != null) {
                    path = Paths.get(this.dir, wildcard, e.getEnv() + "." + e.getTopic() + "." + e.getProject() + ".log");
                } else {
                    path = Paths.get(this.dir, SUBDIR_OTHERS, e.getEnv() + "." + e.getTopic() + "." + e.getProject() + ".log");
                }
            } else {
                path = Paths.get(this.dir, subdir, e.getEnv() + "." + e.getTopic() + "." + e.getProject() + ".log");
            }
            // create file writer
            w = new FileWriter(path.toAbsolutePath().toString(), true);
            // cache file writer
            this.writers.put(e.getTopic(), w);
        }
        return w;
    }

    @Override
    public void doAppendEvent(@NotNull IEvent e) {
        this.queue.offer(e);
    }


    @Override
    public void doStart() {
        super.doStart();

        // make parent directories
        //noinspection ResultOfMethodCallIgnored
        Paths.get(this.dir).toFile().mkdirs();
        //noinspection ResultOfMethodCallIgnored
        Paths.get(this.dir, SUBDIR_OTHERS).toFile().mkdirs();

        // make subdirs
        new HashSet<>(this.subdirMappings.values()).forEach(subdir -> {
            //noinspection ResultOfMethodCallIgnored
            Paths.get(this.dir, subdir).toFile().mkdirs();
        });

        // start worker
        this.worker = new EventFileOutputWorker();
        this.worker.start();
    }

    @Override
    public void doStop() {
        // shutdown worker
        this.worker.exit();
        try {
            this.worker.join();
        } catch (InterruptedException ignored) {
        }
        this.worker = null;

        // close all writers
        try {
            closeWriters();
        } catch (IOException ignored) {
        }

        super.doStop();
    }

    private class EventFileOutputWorker extends Thread {

        public EventFileOutputWorker() {
            super("logtube-FileOutputWorker");
            this.setDaemon(true);
        }

        private boolean shouldExit = false;

        public void exit() {
            this.shouldExit = true;
        }

        @Override
        public void run() {
            while (!this.shouldExit) {
                IEvent e = null;
                try {
                    e = BaseFileOutput.this.queue.poll(5, TimeUnit.SECONDS);
                } catch (Exception ignored) {
                    continue;
                }
                if (e == null) {
                    continue;
                }
                try {
                    FileWriter w = getWriter(e);
                    serializeLine(e, w);
                    w.write(NEW_LINE);
                    w.flush();
                } catch (IOException ignored) {
                }
            }
        }
    }

    abstract void serializeLine(@NotNull IEvent e, @NotNull Writer w) throws IOException;

}
