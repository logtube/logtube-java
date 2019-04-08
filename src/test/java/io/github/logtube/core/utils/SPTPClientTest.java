package io.github.logtube.core.utils;

import org.junit.Test;

import java.io.IOException;
import java.security.SecureRandom;

public class SPTPClientTest {

    @Test
    public void send() throws IOException {
        byte[] bytes = new byte[102400];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);
        SPTPClient client = new SPTPClient(new String[]{"localhost:9988"});
        client.send(bytes);
    }
}