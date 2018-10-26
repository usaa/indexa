package com.usaa.bank.graph.common.identity;

/**
 * Interface that helps specification for basic interactions available with GUID.
 */
public interface Identifiable {
    /**
     * Get the associated GUID value.
     *
     * @return - GUID object.
     */
    GUID getGUID();
}