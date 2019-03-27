package io.github.logtube.classic;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class SLF4JBindingTest {

    @Test
    public void binding() {
        Logger logger = LoggerFactory.getLogger("demo");
        assertEquals(logger.getClass().getCanonicalName(), ClassicLogger.class.getCanonicalName());
    }

}