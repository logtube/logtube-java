package io.github.logtube.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertTrue;

public class FlattenTest {

    @Test
    public void flattenJSON() {
        String json = "{\n" +
                "  \"key1\": \"value1\",\n" +
                "  \"key2\": [\n" +
                "    \"value20\", \"value21\", {\n" +
                "      \"key21\": \"value22\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"key3\": {\n" +
                "    \"key31\": {\n" +
                "      \"key311\": \"value311\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        ArrayList<String> output = Flatten.flattenJSON(json);
        HashSet<String> set = new HashSet<>(output);
        assertTrue(set.contains("key1=value1"));
        assertTrue(set.contains("key2.0=value20"));
        assertTrue(set.contains("key2.1=value21"));
        assertTrue(set.contains("key2.2.key21=value22"));
        assertTrue(set.contains("key3.key31.key311=value311"));
    }

    @Test
    public void flattenParameters() {
        HashMap<String, String[]> map = new HashMap<>();
        map.put("key1", new String[]{"val1"});
        map.put("key2", new String[]{"val2", "val3"});
        map.put("key3", new String[]{"val2", "val3", "val4"});
        ArrayList<String> output = Flatten.flattenParameters(map);
        assertTrue(output.contains("key1=val1"));
        assertTrue(output.contains("key2.0=val2"));
        assertTrue(output.contains("key2.1=val3"));
        assertTrue(output.contains("key3.0=val2"));
        assertTrue(output.contains("key3.1=val3"));
        assertTrue(output.contains("key3.2=val4"));
    }

    @Test
    public void objectToByteArray() {
    }
}