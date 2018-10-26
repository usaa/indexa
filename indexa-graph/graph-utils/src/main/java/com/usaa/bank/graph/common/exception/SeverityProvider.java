package com.usaa.bank.graph.common.exception;

/**
 * Interface which specifies the extent of severity.
 */
public interface SeverityProvider {
    /**
     * Get severity associated.
     *
     * @return - Severity Enum.
     */
    Severity getSeverity();
}
