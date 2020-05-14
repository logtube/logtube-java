package io.github.logtube.audit;

import io.github.logtube.core.IEventLogger;

public class XAudit {

    public static XAuditCommitter create(IEventLogger logger) {
        return new XAuditCommitter(logger.topic("x-audit"));
    }

}
