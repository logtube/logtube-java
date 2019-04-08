package net.landzero.xlog.utils;

import java.io.File;

public class SignalFileChecker {

    private final File file;

    private long cachedLastModified;

    public long getCachedLastModified() {
        return cachedLastModified;
    }

    public void setCachedLastModified(long cachedLastModified) {
        this.cachedLastModified = cachedLastModified;
    }

    public SignalFileChecker(String filename) {
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
