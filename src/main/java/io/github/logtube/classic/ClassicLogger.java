package io.github.logtube.classic;

import org.slf4j.helpers.MarkerIgnoringBase;

public class ClassicLogger extends MarkerIgnoringBase {

    public ClassicLogger(String name) {
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
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
        return false;
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
        return false;
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
        return false;
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
        return false;
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
