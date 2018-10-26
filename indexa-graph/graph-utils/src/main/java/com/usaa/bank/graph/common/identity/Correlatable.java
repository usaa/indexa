package com.usaa.bank.graph.common.identity;

/**
 * Interface that provides specification to identify mutual or reciprocal relationships.
 */
public interface Correlatable {
    /**
     * Set correlation ID.
     *
     * @param correlationID - Correlation ID value.
     */
    void setCorrelationID(String correlationID);

    /**
     * Get correlation ID.
     *
     * @return - String value of correlation ID.
     */
    String getCorrelationID();
}
