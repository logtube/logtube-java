/**
 * Http Response封装类
 */
package io.github.logtube.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Http Response封装类
 *
 * @author chenkeguang 2018年11月12日
 */
public class LogtubeHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    private boolean useOutputStream = true;

    public LogtubeHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getWriter()
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        useOutputStream = false;
        return super.getWriter();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getOutputStream()
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {

        return new LogtubeServletOutputStream(bytes);
    }

    public boolean useOutputStream() {
        return useOutputStream;
    }

    public byte[] getContent() {
        return bytes.toByteArray();
    }
}
