package net.landzero.xlog.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 仅供http包使用
 *
 * @author chenkeguang 2018年11月12日
 */
class XLogServletOutputStream extends ServletOutputStream {
    private ByteArrayOutputStream stream;

    public XLogServletOutputStream(ByteArrayOutputStream outputStream) {
        this.stream = outputStream;
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

}