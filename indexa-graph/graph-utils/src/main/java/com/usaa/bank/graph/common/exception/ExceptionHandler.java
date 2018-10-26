package com.usaa.bank.graph.common.exception;

import java.util.UUID;

import com.usaa.bank.graph.common.logging.CommonLogger;

/**
 * Utility class to handle for the exception thrown.
 */
public final class ExceptionHandler {
    private static final CommonLogger Logger = new CommonLogger(ExceptionHandler.class);
    private static final String EXCEPTION_MESSAGE = "DeMaException with correlationId:";
    private static ThreadLocal<String> correlationIdLocal = new ThreadLocal<String>();


    /**
     * Handles the thrown cause of an excpetion.
     *
     * @param thrown - The cause
     * @throws Throwable -
     */
    public void handle(Throwable thrown) throws Throwable {
        //correlate, log, propagate
        if (DeMaCheckedException.class.isAssignableFrom(thrown.getClass())) {

            DeMaRuntimeException deMaRuntimeException = DeMaRuntimeException.class.cast(thrown);
            deMaRuntimeException.setCorrelationID(ExceptionHandler.getCorrelationId());

            if (!deMaRuntimeException.isLogged()) {
                ExceptionHandler.log(deMaRuntimeException);
                deMaRuntimeException.setLogged();
            }
        }

        throw thrown;
    }


    /**
     * Logs the exception caused based on the severity designated to each exception
     *
     * @param deMaRuntimeException - DeMaRuntimeException to be logged
     * @return - Same DeMaRuntimeException obtained in method signature
     */
    public static DeMaRuntimeException log(DeMaRuntimeException deMaRuntimeException) {
        if (deMaRuntimeException != null) {
            switch (deMaRuntimeException.getSeverity()) {
                case ERROR: {
                    Logger.error(ExceptionHandler.EXCEPTION_MESSAGE + deMaRuntimeException.getCorrelationID(), deMaRuntimeException);
                    break;
                }
                case WARN: {
                    Logger.error(ExceptionHandler.EXCEPTION_MESSAGE + deMaRuntimeException.getCorrelationID(), deMaRuntimeException);
                    break;
                }
                default:
                    Logger.error(ExceptionHandler.EXCEPTION_MESSAGE + deMaRuntimeException.getCorrelationID(), deMaRuntimeException);
            }
        }
        return deMaRuntimeException;
    }

    /**
     * Get CorrelationId of the Exception-Handler. Id generated will be using the Java Api UUID.
     *
     * @return - Generated Correlation Id
     */
    public static String getCorrelationId() {
        if (ExceptionHandler.correlationIdLocal == null) {
            ExceptionHandler.correlationIdLocal = new ThreadLocal<String>();
            String correlationId = UUID.randomUUID().toString();
            correlationIdLocal.set(correlationId);
            return correlationId;
        }
        return "";
    }
}
