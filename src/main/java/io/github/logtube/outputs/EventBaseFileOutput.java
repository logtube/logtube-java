package io.github.logtube.outputs;

import io.github.logtube.IEvent;
import io.github.logtube.IEventOutput;
import io.github.logtube.utils.Strings;
import io.github.logtube.utils.TopicAware;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public abstract class EventBaseFileOutput extends TopicAware implements IEventOutput, Closeable {

    private static final char[] NEW_LINE = new char[]{'\r', '\n'};

    private final String dir;

    private final HashMap<String, FileWriter> writers = new HashMap<>();

    private void closeUnusedFiles() {
        // TODO: implements
    }

    public EventBaseFileOutput(@NotNull String dir) {
        this.dir = dir;
    }

    private FileWriter getWriter(@NotNull IEvent e) throws IOException {
        // close unused files
        closeUnusedFiles();
        // calculate full file path
        Path path = Paths.get(
                this.dir,
                e.getEnvironment(),
                e.getTopic(),
                e.getProject() + "." + Strings.formatPathSuffix(e.getTimestamp()) + ".log");
        String absolutePath = path.toAbsolutePath().toString();
        FileWriter fw = this.writers.get(absolutePath);
        if (fw == null) {
            // make parent directories
            Paths.get(this.dir, e.getEnvironment(), e.getTopic()).toFile().mkdirs();
            // create file writer
            fw = new FileWriter(path.toAbsolutePath().toString(), true);
            // cache file writer
            this.writers.put(absolutePath, fw);
        }
        return fw;
    }

    @Override
    public void appendEvent(@NotNull IEvent e) {
        if (!isTopicEnabled(e.getTopic())) {
            return;
        }
        synchronized (this) {
            try {
                FileWriter w = getWriter(e);
                serializeLine(e, w);
                w.write(NEW_LINE);
                w.flush();
            } catch (IOException ignored) {
            }
        }
    }

    abstract void serializeLine(@NotNull IEvent e, @NotNull Writer w) throws IOException;

    @Override
    public void close() throws IOException {
    }

}
