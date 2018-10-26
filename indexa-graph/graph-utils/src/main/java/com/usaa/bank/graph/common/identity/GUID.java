package com.usaa.bank.graph.common.identity;


/**
 * A class that specifies the GUID specific to Dependency Management tool. This is a specific to DeMa in terms of implementation.
 */
public class GUID extends SimpleImmutableIdentifier {
    private static final long serialVersionUID = 1L;

    GUID(String identifier) {
        super(identifier);
    }
}
