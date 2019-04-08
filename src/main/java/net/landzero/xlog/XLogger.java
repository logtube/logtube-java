package net.landzero.xlog;

import org.slf4j.Logger;

public interface XLogger {

    Logger withK(Object... keywords);

}
