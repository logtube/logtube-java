package io.github.logtube.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 仅供http包使用
 *
 * @author chenkeguang 2018年11月12日
 */
class LogtubeServletOutputStream extends ServletOutputStream {

    private final ByteArrayOutputStream stream;
    private final ServletResponse response;

    public LogtubeServletOutputStream(ByteArrayOutputStream outputStream, ServletResponse response) {
        this.stream = outputStream;
        this.response = response;
    }

    @Override
    public void write(int b) throws IOException {
        stream.write(b);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }

    @Override
    public void close() throws IOException {
        this.response.flushBuffer();
    }

}