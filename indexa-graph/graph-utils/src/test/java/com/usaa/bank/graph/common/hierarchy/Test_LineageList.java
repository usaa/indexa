package com.usaa.bank.graph.common.hierarchy;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Test_LineageList {

    LineageList lineageList;
    final String delimiter = "....";
    final String testString = "test";

    @Before
    public void setup(){
        lineageList = new LineageList(delimiter);
        lineageList.pushParent(testString);
    }

    @Test
    public void getDelimiterReturnsProperDelimiter(){
        assertEquals(lineageList.getDelimiter(), delimiter);
    }

    @Test
    public void pushParentAddsEntryToLineageList(){
        lineageList.pushParent(testString);
        assertTrue(lineageList.contains(testString));
    }

    @Test
    public void pushParentRemovesTheFirstEntryInTheLineageListAndReturnsIt(){
        lineageList.pushParent(delimiter);
        assertEquals(lineageList.popParent(), delimiter);
    }

    @Test
    public void queueChildAddsObjectAsTheLastEntryInLineageList(){
        lineageList.queueChild(delimiter);
        assertTrue(lineageList.contains(delimiter));
    }
    @Test
    public void dequeueChildRemovesAndReturnsTheLastEntryInLineageList(){
        lineageList.queueChild(delimiter);
        assertEquals(lineageList.dequeueChild(), delimiter);
    }
    @Test
    public void getParentsFirstIteratorIteratesFromFirstToLast(){
        lineageList.queueChild(delimiter);
        Iterator<String> iterator = lineageList.getParentsFirstIterator();
        assertEquals(iterator.next(), testString);
    }
    @Test
    public void getChildrenFirstIteratorIteratesFromFirstToLast(){
        lineageList.pushParent(delimiter);
        Iterator<String> iterator = lineageList.getChildrenFirstIterator();
        assertEquals(iterator.next(), testString);
    }
    @Test
    public void asListReturnsLineageListAsList(){
        List list = lineageList.asList();
        assertEquals(list.size(), 1);
    }
    @Test
    public void containsReturnsTrueWhenLineageListContainsObject(){
        assertTrue(lineageList.contains(testString));
    }



}
