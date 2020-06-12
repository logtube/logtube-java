package io.github.logtube.utils;

import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class RotationThreadTest {

    @Test
    public void testCollectFiles() {
        Set<String> out = new HashSet<>();
        RotationThread.collectFiles(out, new File("logs"));
        for (String s : out) {
            System.out.println(s);
        }
    }

}