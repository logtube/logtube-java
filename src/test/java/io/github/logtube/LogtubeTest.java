package io.github.logtube;

import org.junit.Test;

public class LogtubeTest {

    @Test
    public void topic() {
        Logtube.topic("info").message("hello").commit();
    }

}