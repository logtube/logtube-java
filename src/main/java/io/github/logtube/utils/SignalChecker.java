package io.github.logtube.utils;

import java.io.File;

public class SignalChecker {

    private final File file;

    private long cachedLastModified;

    public long getCachedLastModified() {
        return cachedLastModified;
    }

    public void setCachedLastModified(long cachedLastModified) {
        this.cachedLastModified = cachedLastModified;
    }

    public SignalChecker(String filename) {
        this.file = new File(filename);
    }

    public boolean check() {
        // check lastModified
        long lastModified = this.file.lastModified();
        if (lastModified == getCachedLastModified()) {
            return false;
        }
        setCachedLastModified(lastModified);
        return true;
    }

}
