package com.usaa.bank.graph.common.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Commmon Logger utility. Internally uses slf4j logger.
 */
public class CommonLogger {
    private Logger logger;

    public CommonLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /*
     * Log message resulting from irreconcilable object state. i.e: If the object
     * 		can no longer be used because of the error condition
     */
    public void error(String msg) {
        this.logger.error(msg);
    }

    public void error(String msg, Object[] arguments) {
        this.logger.error(msg, arguments);
    }

    /*
     * Log error message resulting from irreconcilable object state. i.e: If the object
     * 		can no longer be used because of the error condition
     */
    public void error(String msg, Throwable t) {
        this.logger.error(msg, t);
    }

    /*
     * Log non-fatal error message that could/should result in a system admin taking some kind of action.
     * 		Ex: initialization failure, connection failure, and high importance validation failure
     */
    public void warning(String msg) {
        this.logger.warn(msg);
    }

    public void warning(String msg, Object[] arguments) {
        this.logger.warn(msg, arguments);
    }

    /*
     * Log non-fatal error message with exception that could/should result in a system admin taking some kind of action.
     * 		Ex: initialization failure, connection failure, and high importance validation failure
     */
    public void warning(String msg, Throwable t) {
        this.logger.warn(msg, t);
    }

    /*
     * Log major executed system tasks. Ex: successful connections, successful initialization, unsuccessful initialization that
     * does NOT  result in overall system error.
     */
    public void info(String msg) {
        this.logger.info(msg);
    }

    public void info(String msg, Object[] arguments) {
        this.logger.info(msg, arguments);
    }

    /*
     * Log outgoing and incoming messages to external systems or modules
     */
    public void debug(String msg) {
        this.logger.debug(msg);
    }

    public void debug(String msg, Object[] arguments) {
        this.logger.debug(msg, arguments);
    }

    /*
     * Log messages to step through application code
     */
    public void trace(String msg) {
        this.logger.trace(msg);
    }

    public void trace(String msg, Object[] arguments) {
        this.logger.trace(msg, arguments);
    }

}