package io.github.logtube.utils;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

public class RotationThread extends Thread {

    private static final String ROT = "ROT";

    private enum Mode {
        None,
        Daily,
        Size
    }

    private Mode mode = Mode.None;

    private long size = 256 * 1024 * 1024;

    private int keep = 0;

    private Set<String> dirs;

    private Set<String> signalFiles;

    public void setup(@NotNull String mode, int keep, @NotNull Set<String> dirs, @NotNull Set<String> signalFiles) {
        synchronized (this) {
            if (mode.equalsIgnoreCase("daily")) {
                this.mode = Mode.Daily;
                this.size = 0;
            } else if (mode.toLowerCase().endsWith("mb")) {
                this.mode = Mode.Size;
                this.size = Long.parseLong(mode.substring(0, mode.length() - 2));
            } else {
                this.mode = Mode.None;
                this.size = 0;
            }
            this.keep = keep;
            this.dirs = dirs;
            this.signalFiles = signalFiles;
        }
    }

    private boolean rotateDirs() {
        // TODO:
        return false;
    }

    private void touchSignals() {
        this.signalFiles.forEach((f) -> new File(f).setLastModified(System.currentTimeMillis()));
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException ignored) {
                return;
            }

            synchronized (this) {
                if (this.mode == Mode.None) {
                    continue;
                }
                if (rotateDirs()) {
                    touchSignals();
                }
            }
        }
    }

}
