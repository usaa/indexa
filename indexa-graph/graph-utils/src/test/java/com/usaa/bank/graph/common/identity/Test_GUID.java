package com.usaa.bank.graph.common.identity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Test_GUID {
    static final String identifier = "testString";
    static GUID guid;

    @Test
    public void GUIDObjectMadeGivenString(){
        guid = new GUID(identifier);
        assertEquals(guid.getStringValue(), identifier);
    }
    @Test
    public void GUIDObjectMadeGivenNull(){
        guid = new GUID(null);
        assertEquals(guid.getStringValue(), null);
    }

}
