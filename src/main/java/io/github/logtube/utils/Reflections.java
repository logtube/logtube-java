package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Reflections {

    public static @Nullable StackTraceElement getStackTraceElement(@NotNull Class boundaryClass) {
        boolean found = false;

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        for (StackTraceElement element : stackTraceElements) {
            if (element.getClassName().equals(boundaryClass.getName())) {
                found = true;
            } else {
                if (found) {
                    return element;
                }
            }
        }
        return null;
    }

    public static int getLineNumber(@NotNull Class boundaryClass) {
        int result = -1;
        StackTraceElement element = getStackTraceElement(boundaryClass);
        if (element != null) {
            result = element.getLineNumber();
        }
        return result;
    }

}
