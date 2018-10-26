package com.usaa.bank.graph.common.logging;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Test_CommonLogger {
    CommonLogger commonLogger = new CommonLogger(Test_CommonLogger.class);
    List messages = new ArrayList<String>();
    List levels;
    final String output = "testOUTPUT";
    final String error = "ERROR";
    final String warn = "WARN";
    final String debug = "DEBUG";
    final String info = "INFO";
    final String trace = "TRACE";
    final String [] arguments = {"test"};


    @Rule
    public final LoggerRule loggerRule = new LoggerRule();

    @Before
    public void setup(){
        levels = new ArrayList<String>();
        messages.add(output);
    }

    @Test
    public void errorGivenStringReturnsErrorString(){
        commonLogger.error(output);
        levels.add(error);
        assertEquals(loggerRule.getFormattedMessages(),messages);
        assertEquals(loggerRule.getLevels(), levels);
    }

    @Test
    public void errorGivenStringAndObjectArrayReturnsErrorStringAndObjectArray(){
        levels.add(error);
        commonLogger.error(output,arguments);
        assertEquals(loggerRule.getFormattedMessages(),messages);
        assertEquals(loggerRule.getLevels(), levels);
    }

    @Test
    public void warnGivenStringReturnsWarnString(){
        levels.add(warn);
        commonLogger.warning(output);
        assertEquals(loggerRule.getFormattedMessages(),messages);
    }

    @Test
    public void warnGivenStringAndObjectArrayReturnsWarnString(){
        levels.add(warn);
        commonLogger.warning(output,arguments);
        assertEquals(loggerRule.getFormattedMessages(),messages);
        assertEquals(loggerRule.getLevels(), levels);
    }

    @Test
    public void infoGivenStringReturnInfoString(){
        levels.add(info);
        commonLogger.info(output);
        assertEquals(loggerRule.getFormattedMessages(),messages);
    }

    @Test
    public void infoGivenStringAndObjectArrayReturnsInfoString(){
        levels.add(info);
        commonLogger.info(output,arguments);
        assertEquals(loggerRule.getFormattedMessages(),messages);
        assertEquals(loggerRule.getLevels(), levels);
    }

    @Test
    public void debugGivenStringReturnDebugString(){
        levels.add(debug);
        commonLogger.debug(output);
        assertEquals(loggerRule.getFormattedMessages(),messages);
    }

    @Test
    public void debugGivenStringAndObjectArrayReturnsDebugString(){
        levels.add(debug);
        commonLogger.debug(output,arguments);
        assertEquals(loggerRule.getFormattedMessages(),messages);
        assertEquals(loggerRule.getLevels(), levels);
    }

    @Test
    public void traceGivenStringReturnTraceString(){
        levels.add(trace);
        commonLogger.trace(output);
        assertEquals(loggerRule.getFormattedMessages(),messages);
    }

    @Test
    public void traceGivenStringAndObjectArrayReturnsTraceString(){
        levels.add(trace);
        commonLogger.trace(output,arguments);
        assertEquals(loggerRule.getFormattedMessages(),messages);
        assertEquals(loggerRule.getLevels(), levels);
    }

}