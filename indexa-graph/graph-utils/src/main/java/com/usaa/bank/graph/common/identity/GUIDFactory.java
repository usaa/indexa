package com.usaa.bank.graph.common.identity;

import java.util.UUID;

/**
 * A simple GUID factory.
 */
public class GUIDFactory {

    /**
     * Create GUID. GUID will be a randomly generated UUID from Java Api.
     *
     * @return - GUID created.
     */
    public static GUID createGUID() {
        return new GUID(UUID.randomUUID().toString());
    }

    /**
     * @param identifier - Identifier used in creation process.
     * @return - GUID object.
     */
    public static GUID createGUID(String identifier) {
        if (identifier != null) {
            return new GUID(UUID.nameUUIDFromBytes(identifier.getBytes()).toString());
        }
        return null;
    }
    public static GUID passGUID(String identifier){
        if (identifier != null) {
            return new GUID(identifier);
        }
        return null;
    }

    /**
     * Creates a GUID from the given guid and identifier.
     *
     * @param guid       - GUID used in creation process.
     * @param identifier - Identifier used in creation process.
     * @return - GUID created.
     */
    public static GUID createGUID(GUID guid, String identifier) {
        if (guid != null) {
            return new GUID(guid.getStringValue() + "." + identifier);
        }
        return GUIDFactory.createGUID(identifier);
    }

}