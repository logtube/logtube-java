package net.landzero.xlog.druid;

import com.google.gson.annotations.SerializedName;
import net.landzero.xlog.XLogEvent;
import net.landzero.xlog.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackEvent extends XLogEvent {

    @Override
    @NotNull
    public String topic() {
        return "x-druid-track";
    }

    @SerializedName("duration")
    private long duration;

    @SerializedName("method")
    @Nullable
    private String method = null;

    @Nullable
    @SerializedName("error")
    private String error = null;

    @Nullable
    @SerializedName("sql_digest")
    private String sqlDigest = null;

    @Nullable
    @SerializedName("sql")
    private String sql = null;

    @Nullable
    @SerializedName("db_host")
    private String dbHost;

    @Nullable
    @SerializedName("db_name")
    private String dbName;

    @Nullable
    @SerializedName("affected_rows")
    private long affectedRows;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @NotNull
    public String getMethod() {
        return method;
    }

    @NotNull
    public void setMethod(@NotNull String method) {
        this.method = Strings.normalize(method);
    }

    @Nullable
    public String getError() {
        return error;
    }

    public void setError(@Nullable String error) {
        this.error = Strings.normalize(error);
    }

    @Nullable
    public String getSqlDigest() {
        return sqlDigest;
    }

    public void setSqlDigest(@Nullable String sqlDigest) {
        this.sqlDigest = Strings.normalize(sqlDigest);
    }

    @Nullable
    public String getSql() {
        return sql;
    }

    public void setSql(@Nullable String sql) {
        this.sql = Strings.normalize(sql);
    }

    /**
     * @return the affectedRows
     */
    public long getAffectedRows() {
        return affectedRows;
    }

    /**
     * @param affectedRows the affectedRows to set
     */
    public void setAffectedRows(long affectedRows) {
        this.affectedRows = affectedRows;
    }

    /**
     * @return the dbHost
     */
    public String getDbHost() {
        return dbHost;
    }

    /**
     * @param dbHost the dbHost to set
     */
    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    /**
     * @return the dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

}
