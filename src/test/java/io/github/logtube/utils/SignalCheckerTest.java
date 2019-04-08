package io.github.logtube.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SignalCheckerTest {

    static final private String TEST_FILE = "/tmp/test.xlog.reopen.txt";

    @Test
    public void check() throws InterruptedException, IOException {
        new File(TEST_FILE).delete();
        SignalChecker checker = new SignalChecker(TEST_FILE);
        assertFalse(checker.check());
        new File(TEST_FILE).createNewFile();
        assertTrue(checker.check());
        assertFalse(checker.check());
        assertFalse(checker.check());
        Thread.sleep(2000);
        new File(TEST_FILE).setLastModified(System.currentTimeMillis());
        assertTrue(checker.check());
        assertFalse(checker.check());
        assertFalse(checker.check());
    }
}