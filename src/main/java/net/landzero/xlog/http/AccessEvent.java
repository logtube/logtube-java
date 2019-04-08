package net.landzero.xlog.http;

import com.google.gson.annotations.SerializedName;
import net.landzero.xlog.XLogEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AccessEvent extends XLogEvent {

    @NotNull
    @Override
    public String topic() {
        return "x-access";
    }

    @Nullable
    @SerializedName("host")
    private String host = null;

    @Nullable
    @SerializedName("method")
    private String method = null;

    @SerializedName("status")
    private int status = 0;

    @SerializedName("duration")
    private long duration = 0;

    @Nullable
    @SerializedName("query")
    private String query = null;

    @Nullable
    @SerializedName("params")
    private ArrayList<String> params = null;

    @Nullable
    @SerializedName("header_app_info")
    private ArrayList<String> headerAppInfo = null;

    @Nullable
    @SerializedName("header_ver_info")
    private ArrayList<String> headerVerInfo = null;

    @Nullable
    @SerializedName("header_user_token")
    private String headerUserToken = null;

    @SerializedName("response_size")
    private long responseSize = 0;

    @Nullable
    public String getHost() {
        return host;
    }

    public void setHost(@Nullable String host) {
        this.host = host;
    }

    @Nullable
    public String getMethod() {
        return method;
    }

    public void setMethod(@Nullable String method) {
        this.method = method;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Nullable
    public String getQuery() {
        return query;
    }

    public void setQuery(@Nullable String query) {
        this.query = query;
    }

    @Nullable
    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams(@Nullable ArrayList<String> params) {
        this.params = params;
    }

    @Nullable
    public ArrayList<String> getHeaderAppInfo() {
        return headerAppInfo;
    }

    public void setHeaderAppInfo(@Nullable ArrayList<String> headerAppInfo) {
        this.headerAppInfo = headerAppInfo;
    }

    @Nullable
    public ArrayList<String> getHeaderVerInfo() {
        return headerVerInfo;
    }

    public void setHeaderVerInfo(@Nullable ArrayList<String> headerVerInfo) {
        this.headerVerInfo = headerVerInfo;
    }

    @Nullable
    public String getHeaderUserToken() {
        return headerUserToken;
    }

    public void setHeaderUserToken(@Nullable String headerUserToken) {
        this.headerUserToken = headerUserToken;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(long responseSize) {
        this.responseSize = responseSize;
    }

}
