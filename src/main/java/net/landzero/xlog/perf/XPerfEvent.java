package net.landzero.xlog.perf;

import com.google.gson.annotations.SerializedName;
import net.landzero.xlog.XLogEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class XPerfEvent extends XLogEvent {

    @Override
    public @NotNull
    String topic() {
        return "x-perf";
    }

    @SerializedName("action")
    private String action = null;

    @SerializedName("arguments")
    private List<String> arguments = null;

    @SerializedName("class_name")
    private String className = null;

    @SerializedName("method_name")
    private String methodName = null;

    @SerializedName("duration")
    private long duration = 0;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
