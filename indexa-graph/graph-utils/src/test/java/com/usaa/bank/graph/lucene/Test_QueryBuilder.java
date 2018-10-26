package com.usaa.bank.graph.lucene;

import org.apache.lucene.search.BooleanQuery;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class Test_QueryBuilder {

    HashMap map = new HashMap<>();
    BooleanQuery booleanQuery;

    @Test
    public void createQueryReturnsBooleanQueryGivenStringMap(){
        map.put("test", "query");
        booleanQuery = QueryBuilder.createQuery(map);
        assertEquals(booleanQuery.toString(), "+test:query");
    }
    @Test
    public void createQueryReturnsNullGivenEmptyMap(){
        booleanQuery = QueryBuilder.createQuery(map);
        assertEquals(booleanQuery, null);
    }



}
