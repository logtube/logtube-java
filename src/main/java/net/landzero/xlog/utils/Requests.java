package net.landzero.xlog.utils;

import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Requests {

    private static final String[] METHODS_WITH_BODY = {"POST", "PUT", "PATCH"};

    private static final String[] CONTENT_TYPES_WITH_JSON = {"application/json", "text/json"};

    private static final String CONTENT_TYPES_WITH_URLENCODED = "application/x-www-form-urlencoded";

    public static boolean hasBody(@NotNull HttpServletRequest httpServletRequest) {
        for (String method : METHODS_WITH_BODY) {
            if (method.equalsIgnoreCase(httpServletRequest.getMethod())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasJsonBody(@NotNull HttpServletRequest httpServletRequest) {
        if (!hasBody(httpServletRequest)) {
            return false;
        }
        if (httpServletRequest.getContentType() == null) {
            return false;
        }
        for (String type : CONTENT_TYPES_WITH_JSON) {
            if (httpServletRequest.getContentType().toLowerCase().contains(type.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasFormUrlencodedBody(@NotNull HttpServletRequest httpServletRequest) {
        if (!hasBody(httpServletRequest)) {
            return false;
        }
        if (httpServletRequest.getContentType() == null) {
            return false;
        }
        String contentType = httpServletRequest.getContentType().toLowerCase();
        return contentType.contains(CONTENT_TYPES_WITH_URLENCODED);
    }

    @NotNull
    public static byte[] readBody(@NotNull HttpServletRequest httpServletRequest) throws IOException {
        ServletInputStream inputStream = httpServletRequest.getInputStream();
        if (inputStream == null) {
            return new byte[]{};
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int ch;

        while ((ch = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, ch);
        }
        return outputStream.toByteArray();
    }

}
