package io.github.logtube.classic;

import io.github.logtube.Logtube;
import org.slf4j.helpers.MarkerIgnoringBase;

import static io.github.logtube.ILogger.*;

public class ClassicLogger extends MarkerIgnoringBase {

    private final String name;

    public ClassicLogger(String name) {
        this.name = name;
    }

    @Override
    public boolean isTraceEnabled() {
        return Logtube.isTopicEnabled(CLASSIC_TOPIC_TRACE);
    }

    @Override
    public void trace(String s) {
    }

    @Override
    public void trace(String s, Object o) {
    }

    @Override
    public void trace(String s, Object o, Object o1) {
    }

    @Override
    public void trace(String s, Object... objects) {
    }

    @Override
    public void trace(String s, Throwable throwable) {
    }

    @Override
    public boolean isDebugEnabled() {
        return Logtube.isTopicEnabled(CLASSIC_TOPIC_DEBUG);
    }

    @Override
    public void debug(String s) {
    }

    @Override
    public void debug(String s, Object o) {

    }

    @Override
    public void debug(String s, Object o, Object o1) {

    }

    @Override
    public void debug(String s, Object... objects) {

    }

    @Override
    public void debug(String s, Throwable throwable) {

    }

    @Override
    public boolean isInfoEnabled() {
        return Logtube.isTopicEnabled(CLASSIC_TOPIC_INFO);
    }

    @Override
    public void info(String s) {

    }

    @Override
    public void info(String s, Object o) {

    }

    @Override
    public void info(String s, Object o, Object o1) {

    }

    @Override
    public void info(String s, Object... objects) {

    }

    @Override
    public void info(String s, Throwable throwable) {

    }

    @Override
    public boolean isWarnEnabled() {
        return Logtube.isTopicEnabled(CLASSIC_TOPIC_WARN);
    }

    @Override
    public void warn(String s) {

    }

    @Override
    public void warn(String s, Object o) {

    }

    @Override
    public void warn(String s, Object... objects) {

    }

    @Override
    public void warn(String s, Object o, Object o1) {

    }

    @Override
    public void warn(String s, Throwable throwable) {
    }

    @Override
    public boolean isErrorEnabled() {
        return Logtube.isTopicEnabled(CLASSIC_TOPIC_ERROR);
    }

    @Override
    public void error(String s) {
    }

    @Override
    public void error(String s, Object o) {
    }

    @Override
    public void error(String s, Object o, Object o1) {
    }

    @Override
    public void error(String s, Object... objects) {
    }

    @Override
    public void error(String s, Throwable throwable) {
    }

}
