package com.usaa.bank.graph.common.identity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Test_SimpleImmutableIdentifier {

    static final String identifier = "identifier";
    SimpleImmutableIdentifier simpleImmutableIdentifier;

    @Test
    public void createSimpleImmutableIdentifierGivenString(){
        simpleImmutableIdentifier = new SimpleImmutableIdentifier(identifier);
        assertEquals(simpleImmutableIdentifier.getStringValue(), identifier);

    }

    @Test
    public void simpleImmutableIdentifierGivenNullreturnsNonNullSimpleImmutableIdentifier(){
        simpleImmutableIdentifier = new SimpleImmutableIdentifier(null);
        assertEquals(simpleImmutableIdentifier.toString(), null);
    }

}
