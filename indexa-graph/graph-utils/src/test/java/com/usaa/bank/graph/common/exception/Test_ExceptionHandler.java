package com.usaa.bank.graph.common.exception;

import com.usaa.bank.graph.common.logging.LoggerRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Test_ExceptionHandler {
    final Throwable throwable = new Throwable();
    final ExceptionHandler exceptionHandler = new ExceptionHandler();
    List messages = new ArrayList<String>();
    List severity;
    DeMaRuntimeException deMaRuntimeException;

    @Rule
    public final LoggerRule loggerRule = new LoggerRule();

    @Before
    public void setup(){
        severity = new ArrayList<String>();
        messages.add("DeMaException with correlationId:null");
        deMaRuntimeException = new DeMaRuntimeException("test");
    }

    @Test(expected = Throwable.class)
    public void handleThrowsExceptionGivenThrow() throws Throwable {
            exceptionHandler.handle(throwable);
    }

    @Test(expected = NullPointerException.class)
    public void handleThrowsExceptionGivenNull() throws Throwable {
        exceptionHandler.handle(null);
    }

    @Test
    public void logGivenDeMaExceptionAndSeverityLevelERRORLogsCorrectly(){
        severity.add(Severity.ERROR.toString());
        deMaRuntimeException.setSeverity(Severity.ERROR);
        exceptionHandler.log(deMaRuntimeException);
        assertEquals(loggerRule.getFormattedMessages(),messages);
        assertEquals(loggerRule.getLevels(), severity);
    }

    @Test
    public void logGivenDeMaExceptionAndSeverityLevelWARNLogsCorrectly(){
        severity.add(Severity.ERROR.toString());
        deMaRuntimeException.setSeverity(Severity.WARN);
        exceptionHandler.log(deMaRuntimeException);
        assertEquals(loggerRule.getFormattedMessages(),messages);
        assertEquals(loggerRule.getLevels(), severity);
    }

    @Test
    public void getCorrelationIdReturnsStringOfExceptionHandler(){
        assertEquals(exceptionHandler.getCorrelationId(), "");
    }

}
