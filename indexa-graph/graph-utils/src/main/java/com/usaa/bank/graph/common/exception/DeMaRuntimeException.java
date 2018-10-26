package com.usaa.bank.graph.common.exception;

import com.usaa.bank.graph.common.identity.Correlatable;
import com.usaa.bank.graph.common.logging.Loggable;

/**
 * Thrown to indicate exception occurring at run time. This is an extension of RuntimeException.<br>
 * <p>
 * DeMaRuntimeException implements the SeverityProvider, Correlatable and Loggable which provide severity, correlationId and logging for the exception
 */
public class DeMaRuntimeException extends RuntimeException implements Loggable, Correlatable, SeverityProvider {
    private static final long serialVersionUID = 1L;
    private static final Severity DEFAULT_SEVERITY = Severity.ERROR;

    private boolean logged;
    private String correlationId;
    private Severity severity;

    /**
     * Constructs with specific message and severity of exception.
     *
     * @param msg      - The detailed message
     * @param severity - Enum stating the Severity
     */
    public DeMaRuntimeException(String msg, Severity severity) {
        super(msg);
        this.setSeverity(severity);
    }

    /**
     * Constructs with the cause and severity of exception.
     *
     * @param t        - The cause
     * @param severity - Enum stating the Severity
     */
    public DeMaRuntimeException(Throwable t, Severity severity) {
        super(t);
        this.setSeverity(severity);
    }

    /**
     * Constructs with specific message, the cause and severity of exception.
     *
     * @param msg      - The detailed message
     * @param t        -the cause
     * @param severity - Enum stating the Severity
     */
    public DeMaRuntimeException(String msg, Throwable t, Severity severity) {
        super(msg, t);
        this.setSeverity(severity);
    }

    /**
     * Constructs with specific message of exception.
     *
     * @param msg - The detailed message
     */
    public DeMaRuntimeException(String msg) {
        super(msg);
        this.setSeverity(DEFAULT_SEVERITY);
    }

    /**
     * Constructs with the cause of exception.
     *
     * @param t - The cause
     */
    public DeMaRuntimeException(Throwable t) {
        super(t);
        this.setSeverity(DEFAULT_SEVERITY);
    }

    /**
     * Constructs with specific message and  the cause of exception.
     *
     * @param msg - The detailed message
     * @param t   - The cause
     */
    public DeMaRuntimeException(String msg, Throwable t) {
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
     * Sets the severity of the excpetion.
     *
     * @param severity - Enum stating the Severity
     */
    protected void setSeverity(Severity severity) {
        this.severity = severity;
    }

}
