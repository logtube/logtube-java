package net.landzero.xlog.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * nested object flatten util
 * <p>
 * 嵌套对象抹平序列化工具
 */
public class Flatten {

    private static final Gson gson = new GsonBuilder().create();

    /**
     * deserialize a JSON string into a #{java.util.Map}, iterate all key value within nested objects and arrays, generate a ArrayList with format "key=value"
     * <p>
     * 将一个 JSON 字符串反序列化为一个 #{java.util.Map}，遍历任意深度的所有键值，最终生成一维字符串数组，格式为 "key=value"
     * <p>
     * 嵌套的对象会在键值中使用 "." 作为分隔符
     * <p>
     * 比如:
     * <pre>
     *     {
     *         "hello": {
     *             "hello": "world"
     *         }
     *     }
     * </pre>
     * 会被抹平为
     * <pre>
     *     [ "hello.hello=world" ]
     * </pre>
     * 数组使用索引作为键值
     * 比如:
     * <pre>
     *     {
     *         "hello": [
     *              "world1",
     *              "world2
     *         ]
     *     }
     * </pre>
     * 会被抹平为
     * <pre>
     *     [ "hello.0=world1", "hello.1=world2" ]
     * </pre>
     * 上述操作可以无限嵌套
     *
     * @param json json string
     * @return flattened ArrayList
     */
    @Nullable
    public static ArrayList<String> flattenJSON(@Nullable String json) {
        if (json == null)
            return null;
        ArrayList<String> out = new ArrayList<>();
        try {
            Map map = gson.fromJson(json, Map.class);
            if (map == null)
                return null;
            mergeMap(map, "", out);
        } catch (Exception e) {
            return null;
        }
        return out;
    }

    /**
     * same as #{flattenJSON}, except array is regarded as a single value if it has only one element
     * <p>
     * 和 #{flattenJSON} 类似，只是当数组只有一个元素的时候，看做是一个普通的值
     *
     * @param parameters parameters
     * @return flattened ArrayList
     */
    @Nullable
    public static ArrayList<String> flattenParameters(@Nullable Map<String, String[]> parameters) {
        if (parameters == null) {
            return null;
        }
        ArrayList<String> out = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            if (entry.getValue() == null)
                continue;
            if (entry.getValue().length == 0)
                continue;
            if (entry.getValue().length == 1)
                out.add(entry.getKey() + "=" + entry.getValue()[0]);
            else
                mergeList(Arrays.asList(entry.getValue()), entry.getKey() + ".", out);
        }
        return out;
    }

    @NotNull
    public static byte[] objectToByteArray(@NotNull Object o) {
        return Flatten.gson.toJson(o).getBytes();
    }

    @SuppressWarnings("unchecked")
    private static void mergeMap(Map input, String prefix, ArrayList<String> output) {
        if (prefix == null)
            prefix = "";
        Set<Map.Entry> entries = input.entrySet();
        for (Map.Entry entry : entries) {
            if (entry.getKey() == null || entry.getValue() == null)
                continue;
            if (entry.getValue() instanceof Map)
                mergeMap((Map) entry.getValue(), prefix + entry.getKey().toString() + ".", output);
            else if (entry.getValue() instanceof List)
                mergeList((List) entry.getValue(), prefix + entry.getKey().toString() + ".", output);
            else
                output.add(prefix + entry.getKey().toString() + "=" + entry.getValue().toString());
        }
    }

    @SuppressWarnings("unchecked")
    private static void mergeList(List input, String prefix, ArrayList<String> output) {
        if (prefix == null)
            prefix = "";
        int index = -1;
        for (Object value : input) {
            index++;
            if (value == null) {
                continue;
            }
            if (value instanceof Map)
                mergeMap((Map) value, prefix + index + ".", output);
            else if (value instanceof List)
                mergeList((List) value, prefix + index + ".", output);
            else
                output.add(prefix + index + "=" + value.toString());
        }
    }
}
