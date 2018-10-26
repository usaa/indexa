package com.usaa.bank.graph.common.exception;

import com.usaa.bank.graph.common.identity.Correlatable;
import com.usaa.bank.graph.common.logging.Loggable;

/**
 * Throw an exception that are checked at compile time. This is an extension of Exception.<br>
 * <p>
 * DeMaCheckedException implements the SeverityProvider, Correlatable and Loggable which provide severity, correlationId and logging for the exception
 */
public class DeMaCheckedException extends Exception implements Loggable, Correlatable, SeverityProvider {
    private static final long serialVersionUID = 1L;
    private static final Severity DEFAULT_SEVERITY = Severity.ERROR;

    private boolean logged;
    private String correlationId;
    private Severity severity;

    /**
     * Constructs with specific message and severity.
     *
     * @param msg      - The detailed message
     * @param severity -  Enum stating the severity
     */
    public DeMaCheckedException(String msg, Severity severity) {
        super(msg);
        this.setSeverity(severity);
    }

    /**
     * Constructs with specific cause and severity
     *
     * @param t        - The cause
     * @param severity -  Enum stating the severity
     */
    public DeMaCheckedException(Throwable t, Severity severity) {
        super(t);
        this.setSeverity(severity);
    }

    /**
     * Constructs with specific message , cause and severity.
     *
     * @param msg      - The detailed message
     * @param t        - The cause
     * @param severity -  Enum stating the severity
     */
    public DeMaCheckedException(String msg, Throwable t, Severity severity) {
        super(msg, t);
        this.setSeverity(severity);
    }

    /**
     * Constructs with specific message.
     *
     * @param msg - the detailed message
     */
    public DeMaCheckedException(String msg) {
        super(msg);
        this.setSeverity(DEFAULT_SEVERITY);
    }

    /**
     * Constructs with the cause.
     *
     * @param t - The cause
     */
    public DeMaCheckedException(Throwable t) {
        super(t);
        this.setSeverity(DEFAULT_SEVERITY);
    }

    /**
     * Constructs with specific message and the cause.
     *
     * @param msg - The detailed message
     * @param t   - The cause
     */
    public DeMaCheckedException(String msg, Throwable t) {
        super(msg, t);
        this.setSeverity(DEFAULT_SEVERITY);
    }


    public boolean isLogged() {
        return logged;
    }

    public void setLogged() {
        this.logged = true;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationId = correlationID;
    }

    public String getCorrelationID() {
        return this.correlationId;
    }

    public Severity getSeverity() {
        return this.severity;
    }

    /**
     * Set severity for the exception.
     *
     * @param severity -  Enum stating the severity
     */
    protected void setSeverity(Severity severity) {
        this.severity = severity;
    }

}
