package com.usaa.bank.graph.common.validate;

import com.usaa.bank.graph.common.exception.DeMaRuntimeException;


/**
 * Thrown to indicate that an operation failed basic validation process. This exception extends DeMaRuntimeException.
 */
public class ValidationException extends DeMaRuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with specific message.
     *
     * @param msg - The detailed message.
     */
    public ValidationException(String msg) {
        super(msg);
    }

    /**
     * Constructs with specific message and the cause.
     *
     * @param msg - The detailed message.
     * @param t   - The cause.
     */
    public ValidationException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs with specific cause.
     *
     * @param t - The cause.
     */
    public ValidationException(Throwable t) {
        super(t);
    }
}