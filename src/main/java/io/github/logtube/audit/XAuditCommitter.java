package io.github.logtube.audit;

import io.github.logtube.core.IMutableEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class XAuditCommitter {

    private final IMutableEvent event;

    public XAuditCommitter(@NotNull IMutableEvent event) {
        this.event = event;
    }

    @Contract("_ -> this")
    public XAuditCommitter setIP(String IP) {
        this.event.extra("ip", IP);
        return this;
    }

    @Contract("_ -> this")
    public XAuditCommitter setUserCode(String id) {
        this.event.extra("user_code", id);
        return this;
    }

    @Contract("_ -> this")
    public XAuditCommitter setUserName(String userName) {
        this.event.extra("user_name", userName);
        return this;
    }

    @Contract("_ -> this")
    public XAuditCommitter setAction(String action) {
        this.event.extra("action", action);
        return this;
    }

    @Contract("_ -> this")
    public XAuditCommitter setActionDetail(String actionDetail) {
        this.event.extra("action_detail", actionDetail);
        return this;
    }

    @Contract("_ -> this")
    public XAuditCommitter setURL(String url) {
        this.event.extra("url", url);
        return this;
    }

    public void commit() {
        this.event.commit();
    }

}
