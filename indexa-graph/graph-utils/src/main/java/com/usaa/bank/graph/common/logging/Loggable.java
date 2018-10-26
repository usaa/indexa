package com.usaa.bank.graph.common.logging;

/**
 * Interface to specify the feature of logging.
 */
public interface Loggable {
    /**
     * Sets logging to be supported by default.
     */
    void setLogged();

    /**
     * Gives information whether logging supported or not.
     *
     * @return - True if logging is supported.
     */
    boolean isLogged();
}
