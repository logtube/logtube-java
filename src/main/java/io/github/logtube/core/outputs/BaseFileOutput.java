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
import java.util.Map;

public abstract class BaseFileOutput extends BaseEventOutput {

    private static final char[] NEW_LINE = new char[]{'\r', '\n'};

    private final String dir;

    private final SignalChecker signalChecker;

    private final IntervalChecker signalIntervalChecker = new IntervalChecker(30 * 1000);

    private final HashMap<String, FileWriter> writers = new HashMap<>();

    public BaseFileOutput(@NotNull String dir, @NotNull String signal) {
        this.dir = dir;
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
            // make parent directories
            //noinspection ResultOfMethodCallIgnored
            Paths.get(this.dir, e.getEnv(), e.getTopic()).toFile().mkdirs();
            // calculate full file path
            Path path = Paths.get(this.dir, e.getEnv(), e.getTopic(), e.getProject() + ".log");
            // create file writer
            w = new FileWriter(path.toAbsolutePath().toString(), true);
            // cache file writer
            this.writers.put(e.getTopic(), w);
        }
        return w;
    }

    @Override
    public void doAppendEvent(@NotNull IEvent e) {
        try {
            FileWriter w = getWriter(e);
            synchronized (w) {
                serializeLine(e, w);
                w.write(NEW_LINE);
                w.flush();
            }
        } catch (IOException ignored) {
        }
    }


    @Override
    public void doStart() {
        super.doStart();
    }

    @Override
    public void doStop() {
        try {
            closeWriters();
        } catch (IOException ignored) {
        }
        super.doStop();
    }

    abstract void serializeLine(@NotNull IEvent e, @NotNull Writer w) throws IOException;

}
