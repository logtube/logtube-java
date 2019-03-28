package io.github.logtube.outputs;

import io.github.logtube.IEvent;
import io.github.logtube.IEventOutput;
import io.github.logtube.IEventSerializer;
import io.github.logtube.serializers.EventFileJSONSerializer;
import io.github.logtube.serializers.EventFilePlainSerializer;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class EventFileOutput implements IEventOutput, Closeable {

    private static final IEventSerializer JSON_SERIALIZER = new EventFileJSONSerializer();

    private static final IEventSerializer PLAIN_SERIALIZER = new EventFilePlainSerializer();

    private static final char[] NEW_LINE = new char[]{'\r', '\n'};

    private final String dir;

    private final AtomicLong age = new AtomicLong();

    private final HashMap<String, FileWriter> writers = new HashMap<>();

    private final HashMap<String, Long> ages = new HashMap<>();

    private Set<String> jsonTopics = new HashSet<>();

    private void closeUnusedFiles() {
        long age = this.age.incrementAndGet();

        if (age % 100 == 0) {
            return;
        }

        // TODO: implements
    }

    public EventFileOutput(@NotNull String dir) {
        this.dir = dir;
    }

    public void setJSONTopics(@NotNull Set<String> jsonTopics) {
        this.jsonTopics = jsonTopics;
    }

    public void enableJSONTopic(@NotNull String topic) {
        this.jsonTopics.add(topic);
    }

    public void disableJSONTopic(@NotNull String topic) {
        this.jsonTopics.remove(topic);
    }

    public boolean isJSONTopic(@NotNull String topic) {
        return this.jsonTopics.contains(topic);
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
    public synchronized void appendEvent(@NotNull IEvent e) {
        try {
            FileWriter fw = getWriter(e);
            if (isJSONTopic(e.getTopic())) {
                JSON_SERIALIZER.serialize(e, fw);
            } else {
                PLAIN_SERIALIZER.serialize(e, fw);
            }
            fw.write(NEW_LINE);
            fw.flush();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void close() throws IOException {
    }

}
