package net.landzero.xlog.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntervalCheckerTest {

    @Test
    public void check() throws InterruptedException {
        IntervalChecker checker = new IntervalChecker(100);
        assertTrue(checker.check());
        assertFalse(checker.check());
        assertFalse(checker.check());
        Thread.sleep(150);
        assertTrue(checker.check());
        assertFalse(checker.check());
        assertFalse(checker.check());
    }
}