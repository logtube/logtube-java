package io.github.logtube.http;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.github.logtube.utils.Requests;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

public class LogtubeHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogtubeHttpServletResponseWrapper.class);

    private final byte[] body;

    private Map<String, String[]> paramsMap = new HashMap<>();

    private String characterEncoding = Charset.defaultCharset().toString();

    private boolean isUrlencodedBody = false;

    public LogtubeHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.body = Requests.readBody(request);
        isUrlencodedBody = Requests.hasFormUrlencodedBody(request);
        if (request.getCharacterEncoding() != null && request.getCharacterEncoding().trim().length() > 0) {
            characterEncoding = request.getCharacterEncoding();
        }
        initParameters();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new InputStreamWrapper();
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public String getParameter(String name) {
        String[] value = paramsMap.get(name);
        if (value != null && value.length > 0) {
            return value[0];
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return paramsMap;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if (paramsMap.size() <= 0) {
            return super.getParameterNames();
        }
        return Collections.enumeration(paramsMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        if (paramsMap.containsKey(name)) {
            return paramsMap.get(name);
        }
        return super.getParameterValues(name);
    }

    @NotNull
    public String getBody() {
        return new String(body);
    }

    @NotNull
    public ArrayList<String> getParams() {
        ArrayList<String> params = new ArrayList<String>();
        try {
            if (isUrlencodedBody) {
                params.add(URLDecoder.decode(getBody(), characterEncoding));
            } else {
                params.add(getBody());
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.info(e.getMessage(), e);
        }
        return params;
    }

    /**
     * 当前仅处理json和x-www-form-urlencoded格式，其它不变
     */
    private void initParameters() {
        try {
            // 获取请求路径的参数
            paramsMap.putAll(super.getParameterMap());

            // 获取body的请求参数
            String paramStr = new String(body);

            if (Strings.isEmpty(paramStr))
                return;

            if (Requests.hasJsonBody(this)) {
                // 处理Json请求格式的参数
                processJsonBody(paramStr);
            } else if (isUrlencodedBody) {
                // 处理x-www-form-urlencoded请求格式的参数
                processFormUrlencoded(paramStr);
            }
        } catch (Throwable t) {
            // 捕获所有错误，输出info级别日志即可，避免在影响正常业务流程和xlog日志查看
            // 2018-12-13 改为debug 。鉴于此处不太重要，改为 debug
            LOGGER.debug(t.getMessage(), t);
        }

    }

    private void processFormUrlencoded(String paramStr) throws UnsupportedEncodingException {
        String[] paramVals = paramStr.split("&");
        for (String paramVal : paramVals) {
            String[] param = paramVal.split("=");
            if (param != null && param.length == 2) {
                String key = URLDecoder.decode(param[0], characterEncoding);
                String value = URLDecoder.decode(param[1], characterEncoding);
                if (paramsMap.containsKey(key)) {
                    String[] vals = paramsMap.get(key);
                    String[] newVals = new String[vals.length + 1];
                    System.arraycopy(vals, 0, newVals, 0, vals.length);
                    newVals[vals.length] = value;
                    paramsMap.put(key, newVals);
                } else {
                    paramsMap.put(key, new String[]{value});
                }
            }
        }
    }

    /**
     * -只将json的value类型为String 及 java原语类型的值添加到parameterMap
     * -只处理key value类型的json对象
     *
     * @param paramStr
     */
    private void processJsonBody(String paramStr) {
        // TODO: remove dependency on Gson
        // 避免Gson将int 转成了double
        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<TreeMap<String, Object>>() {
        }.getType(), new JsonDeserializer<TreeMap<String, Object>>() {
            @Override
            public TreeMap<String, Object> deserialize(JsonElement json, Type typeOfT,
                                                       JsonDeserializationContext context) throws JsonParseException {

                TreeMap<String, Object> treeMap = new TreeMap<>();
                if (json.isJsonObject()) {
                    JsonObject jsonObject = json.getAsJsonObject();
                    jsonObject.entrySet().stream().forEach(e -> treeMap.put(e.getKey(), e.getValue()));
                }
                return treeMap;
            }
        }).create();
        Map<String, Object> map = gson.fromJson(paramStr, new TypeToken<TreeMap<String, Object>>() {
        }.getType());
        if (map != null)
            map.entrySet().stream().filter(p -> p.getValue() instanceof JsonPrimitive).forEach(e -> {
                paramsMap.put(e.getKey(), new String[]{String.valueOf(e.getValue())});
            });
    }

    public class InputStreamWrapper extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public InputStreamWrapper() {
            this.inputStream = new ByteArrayInputStream(LogtubeHttpServletRequestWrapper.this.body);
        }

        @Override
        public boolean isFinished() {
            return this.inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            try {
                // InputStreamWrapper is always ready
                readListener.onDataAvailable();
            } catch (IOException ignored) {
            }
        }

        @Override
        public int read() {
            return this.inputStream.read();
        }
    }

}
