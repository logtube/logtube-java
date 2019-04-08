package net.landzero.xlog;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XLogTest {

    @Test
    public void keyword() {
        assertEquals(XLog.keyword("he,ll,o[]]", 124, null, true), " K[he_ll_o_,124,null,true]");
    }
}