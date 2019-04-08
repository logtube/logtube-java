package net.landzero.xlog.utils;

public class IntervalChecker {

    private final long interval;

    private long checked = 0;

    public long getInterval() {
        return interval;
    }

    public long getChecked() {
        return checked;
    }

    public void setChecked(long checked) {
        this.checked = checked;
    }

    public IntervalChecker(long interval) {
        this.interval = interval;
    }

    public boolean check() {
        long now = System.currentTimeMillis();
        if (now - getChecked() < getInterval()) {
            return false;
        }
        setChecked(now);
        return true;
    }

}
