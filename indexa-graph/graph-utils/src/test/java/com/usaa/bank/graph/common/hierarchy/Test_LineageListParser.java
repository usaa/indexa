package com.usaa.bank.graph.common.hierarchy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class Test_LineageListParser {
    final String testString = "test/String";
    final String testStringCustomDelimiter= "test.String";
    final String delimiter = ".";
    LineageList lineageList;

    @Test
    public void parseReturnsLineageListGivenCustomDelimiterAndStringThatContainsCustomDelimiter(){
        lineageList = LineageListParser.parse(testStringCustomDelimiter,delimiter,false);
        assertEquals(lineageList.dequeueChild(), "test");
    }

    @Test
    public void parseReturnsLineageListGivenNoDelimiterAndStringThatContainsCustomDelimiter(){
        lineageList = LineageListParser.parse(testStringCustomDelimiter,null,false);
        assertEquals(lineageList.dequeueChild(), "test.String");
    }

    @Test
    public void parseReturnsLineageListGivenNoDelimiterAndStringThatContainsNoCustomDelimiter(){
        lineageList = LineageListParser.parse(testString,null,false);
        assertEquals(lineageList.dequeueChild(), "test/String");
    }

    @Test
    public void parseReturnsLineageListGivenDelimiterAndStringThatContainsNoCustomDelimiter(){
        lineageList = LineageListParser.parse(testString,delimiter,false);
        assertEquals(lineageList.dequeueChild(), testString);
    }
    @Test
    public void toStringReturnsStringFromGivenLineageListGivenNoCustomDelimiter(){
        lineageList = LineageListParser.parse(testString,null,false);
        assertEquals(LineageListParser.toString(lineageList, null,true), testString);

    }
    @Test
    public void toStringReturnsStringFromGivenLineageListGivenCustomDelimiter(){
        lineageList = LineageListParser.parse(testStringCustomDelimiter,delimiter,true);
        assertEquals(LineageListParser.toString(lineageList, delimiter,true), testStringCustomDelimiter);
    }




}
