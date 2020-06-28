package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Reflections {

    private static final String LOGTUBE_BOUNDARY_PREFIX = "io.github.logtube.";

    private static boolean isInternalClass(@NotNull String className) {
        return className.startsWith(LOGTUBE_BOUNDARY_PREFIX) && !className.contains("Test");
    }

    public static @Nullable StackTraceElement getStackTraceElement() {
        boolean found = false;

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        for (StackTraceElement element : stackTraceElements) {
            if (isInternalClass(element.getClassName())) {
                found = true;
            } else {
                if (found) {
                    return element;
                }
            }
        }
        return null;
    }

    public static @Nullable StackTraceElement[] getStackTraceElements() {
        ArrayList<StackTraceElement> result = new ArrayList<>();
        boolean found = false;

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        for (StackTraceElement element : stackTraceElements) {
            if (isInternalClass(element.getClassName())) {
                found = true;
            } else {
                if (found) {
                    result.add(element);
                }
            }
        }
        return result.toArray(new StackTraceElement[0]);
    }

}
