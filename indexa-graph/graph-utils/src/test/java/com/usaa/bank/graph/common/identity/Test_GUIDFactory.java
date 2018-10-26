package com.usaa.bank.graph.common.identity;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class Test_GUIDFactory {

    static final String guidCheck = "testString";
    static GUID guid;


    @BeforeClass
    public static void setup(){
        guid = new GUID(guidCheck);
    }

    @Test
    public void createGUIDReturnsRandomGUID(){
        assertEquals(GUIDFactory.createGUID().getClass().getTypeName(), "com.usaa.bank.graph.common.identity.GUID");
    }
    @Test
    public void createGUIDGivenStringReturnGUIDFromString(){
        assertEquals(GUIDFactory.createGUID(guidCheck).getStringValue(), UUID.nameUUIDFromBytes(guidCheck.getBytes()).toString() );
    }
    @Test
    public void createGUIDGivenNullReturnsNull(){
        assertEquals(GUIDFactory.createGUID(null), null);
    }
    @Test
    public void passGUIDGivenString(){
        assertEquals(GUIDFactory.passGUID(guidCheck).getStringValue(), guidCheck);
    }
    @Test
    public void passGUIDGivenNull(){
        assertEquals(GUIDFactory.passGUID(null),null);
    }
    @Test
    public void createGUIDGivenGUIDandString(){
        assertEquals(GUIDFactory.createGUID(guid, guidCheck).getStringValue(), guid.getStringValue() +"." +guidCheck);
    }
    @Test
    public void createGUIDGivenNullandString(){
        assertEquals(GUIDFactory.createGUID(null, guidCheck).getStringValue(), UUID.nameUUIDFromBytes((guidCheck).getBytes()).toString());
    }
    @Test
    public void createGUIDGivenNullandNull(){
        assertEquals(GUIDFactory.createGUID(null, null), null);
    }


}
