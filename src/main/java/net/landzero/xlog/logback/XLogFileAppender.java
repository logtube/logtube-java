package net.landzero.xlog.logback;

import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.FileUtil;
import net.landzero.xlog.utils.Dates;
import net.landzero.xlog.utils.IntervalChecker;
import net.landzero.xlog.utils.SignalFileChecker;
import net.landzero.xlog.utils.Strings;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * this appender bypass #{OutputStreamAppender}, provides a more direct way to write xlog specified log files
 */
public class XLogFileAppender extends XLogBaseAppender {

    /*
     * constants
     */

    public static final int SIGNAL_FILE_CHECK_INTERVAL = 30000;

    public static final int ROTATION_CHECK_INTERVAL = 300000;

    public static final FileSize BUFFER_SIZE = new FileSize(8192);

    public static final String SIGNAL_FILE = "/tmp/xlog.reopen.txt";

    /*
     * configurable variables
     */

    private String dir = null;

    private int rotate = 0;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = Strings.normalize(dir);
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        if (rotate < 0) rotate = 0;
        this.rotate = rotate;
    }

    /*
     * internal variables and methods
     */

    private String filename = null;

    public String getFilename() {
        return filename;
    }

    private void initFilename() {
        if (this.filename != null) return;
        if (getDir() == null || getEnv() == null || getTopic() == null || getProject() == null) return;
        this.filename = new File(String.join(File.separator, getDir(), getEnv(), getTopic(), getProject() + ".log")).getAbsolutePath();
    }

    private final IntervalChecker rotationIntervalChecker = new IntervalChecker(ROTATION_CHECK_INTERVAL);

    private final IntervalChecker signalFileIntervalChecker = new IntervalChecker(SIGNAL_FILE_CHECK_INTERVAL);

    private final SignalFileChecker signalFileChecker = new SignalFileChecker(SIGNAL_FILE);

    private OutputStream outputStream = null;

    private void initOutputStream() throws IOException {
        this.lock.lock();
        try {
            unsafeInitOutputStream();
        } finally {
            this.lock.unlock();
        }
    }

    private void unsafeInitOutputStream() throws IOException {
        unsafeCloseOutputStream();
        File file = new File(getFilename());
        boolean result = FileUtil.createMissingParentDirectories(file);
        if (!result) {
            addError("failed to create parent directories for [" + file.getAbsolutePath() + "]");
        }
        ResilientFileOutputStream fos = new ResilientFileOutputStream(file, true, BUFFER_SIZE.getSize());
        fos.setContext(getContext());
        this.outputStream = fos;
    }

    private void closeOutputStream() throws IOException {
        this.lock.lock();
        try {
            unsafeCloseOutputStream();
        } finally {
            this.lock.unlock();
        }
    }

    private void unsafeCloseOutputStream() throws IOException {
        if (this.outputStream != null) {
            try {
                this.outputStream.flush();
                this.outputStream.close();
            } finally {
                this.outputStream = null;
            }
        }
    }

    private void unsafeReloadOutputStreamIfNeeded() throws IOException {
        if (unsafeRotateFileIfNeeded()) {
            return;
        }
        if (unsafeReopenFileIfNeeded()) {
            return;
        }
    }

    private boolean unsafeRotateFileIfNeeded() throws IOException {
        // skip if rotation is disabled
        if (getRotate() == 0) return false;
        // skip if recently checked
        if (!this.rotationIntervalChecker.check()) {
            return false;
        }
        // check yesterday already existed
        File yesterdayFile = new File(getFilename() + "-" + Dates.yesterday_yyyyMMdd());
        if (yesterdayFile.exists()) {
            return false;
        }
        // rotate file
        new File(getFilename()).renameTo(yesterdayFile);
        // reopen output stream
        unsafeInitOutputStream();
        // find all rotated files
        File[] files = new File(getFilename()).getParentFile().listFiles(this::isRotatedFile);
        // if existed rotated files exceeded the 'rotate' value
        if (files != null && files.length > getRotate()) {
            // sort files alphabetic
            Arrays.sort(files);
            // delete expired files
            for (int i = 0; i < files.length - getRotate(); i++) {
                files[i].delete();
            }
        }
        return true;
    }

    private boolean isRotatedFile(File file) {
        return file.getAbsolutePath().startsWith(getFilename() + "-") && !file.isDirectory();
    }

    private boolean unsafeReopenFileIfNeeded() throws IOException {
        // skip if recently checked
        if (!this.signalFileIntervalChecker.check()) {
            return false;
        }
        // skip if signal file not modified
        if (!this.signalFileChecker.check()) {
            return false;
        }
        // reopen file
        unsafeInitOutputStream();
        return true;
    }

    /**
     * public methods
     */

    @Override
    public void start() {
        int errors = 0;
        initFilename();
        if (getFilename() == null) {
            if (isJsonMode()) {
                addError("failed to calculate fileName, check if 'dir', 'env' or 'project' field is missing");
            } else {
                addError("failed to calculate fileName, check if 'dir', 'env', 'topic' or 'project' field is missing");
            }
            errors++;
        }
        if (errors == 0) {
            try {
                initOutputStream();
            } catch (IOException e) {
                addError("failed to initialize output stream", e);
                errors++;
            }
        }
        if (errors == 0) {
            super.start();
        }
    }

    @Override
    public void stop() {
        try {
            this.closeOutputStream();
        } catch (IOException e) {
            addWarn("failed to close output stream", e);
        }
        super.stop();
    }

    @Override
    protected void appendString(@NotNull String string) {
        this.lock.lock();
        try {
            unsafeReloadOutputStreamIfNeeded();
            this.outputStream.write(string.getBytes());
            this.outputStream.flush();
        } catch (IOException ignored) {
        } finally {
            this.lock.unlock();
        }
    }

}
