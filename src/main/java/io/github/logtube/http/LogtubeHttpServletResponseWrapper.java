/**
 * Http Response封装类
 */
package io.github.logtube.http;

import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.atomic.LongAdder;

/**
 * Http Response封装类
 *
 * @author chenkeguang 2018年11月12日
 */
public class LogtubeHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final LongAdder responseSize = new LongAdder();

    public LogtubeHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public long getResponseSize() {
        return this.responseSize.longValue();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new WrappedPrintWrapper(super.getWriter());
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new WrappedServletOutputStream(super.getOutputStream());
    }

    private class WrappedServletOutputStream extends ServletOutputStream {

        @NotNull
        private final ServletOutputStream parent;

        WrappedServletOutputStream(@NotNull ServletOutputStream parent) {
            this.parent = parent;
        }

        @Override
        public boolean isReady() {
            return parent.isReady();
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            parent.setWriteListener(writeListener);
        }

        @Override
        public void write(int i) throws IOException {
            parent.write(i);
            responseSize.increment();
        }

    }

    private class WrappedPrintWrapper extends PrintWriter {

        WrappedPrintWrapper(@NotNull PrintWriter parent) {
            super(new WrappedWriter(parent));
        }

    }

    private class WrappedWriter extends Writer {

        @NotNull
        private final Writer parent;

        WrappedWriter(@NotNull Writer parent) {
            super(parent);
            this.parent = parent;
        }

        @Override
        public void write(@NotNull char[] chars, int off, int len) throws IOException {
            this.parent.write(chars, off, len);
            responseSize.add(len);
        }

        @Override
        public void flush() throws IOException {
            this.parent.flush();
        }

        @Override
        public void close() throws IOException {
            this.parent.close();
        }

    }

}
