package net.landzero.xlog.utils;

import org.jetbrains.annotations.NotNull;

public class Reflections {

    @NotNull
    public static CallerInfo getCallerInfo() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Reflections.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                if (callerClassName == null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    CallerInfo info = new CallerInfo();
                    info.setClassName(ste.getClassName());
                    info.setMethodName(ste.getMethodName());
                    return info;
                }
            }
        }
        return new CallerInfo();
    }

}
