package com.usaa.bank.graph.lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;


public class Test_IndexUtil {
    static HashMap fieldMap = new HashMap<>();
    static Set fieldSet = new HashSet<>();
    static String [] keysArray;
    static File file;
    static Directory directory;
    static SearcherManager searcherManager;
    static IndexWriter indexWriter;
    static final String fieldName= "fieldName";
    static final String fieldValue= "fieldValue";
    static final String fieldName1= "fieldName1";
    static final String fieldValue1= "fieldValue1";


    @Before
    public void setup(){
        fieldSet.add(fieldValue);
        fieldMap.put(fieldName,fieldSet);
        keysArray = new String [] {fieldName};
        file = new File("src/test/resources/testIndex");
    }


    @Test
    public void getFirstStringValueGivenFieldAndFieldMapReturnsFieldStringFound(){
        String firstValue = IndexUtil.getFirstValueString(fieldName,fieldMap);
        assertEquals(firstValue, fieldValue);
    }

    @Test
    public void getFirstStringValueGivenNotFoundFieldAndFieldMapReturnsEmptyString(){
        String firstValue = IndexUtil.getFirstValueString(fieldValue,fieldMap);
        assertEquals(firstValue, "");
    }

    @Test
    public void getFirstStringValueGivenNullMapReturnsEmptyString(){
        String firstValue = IndexUtil.getFirstValueString(fieldValue,null);
        assertEquals(firstValue, "");
    }

    @Test
    public void cleanFieldsGivenArrayOfKeysAndMapOfFieldsReturnsMapWithOnlyFieldsFromKeysArray(){
        Map returnMap = IndexUtil.cleanFields(keysArray,fieldMap);
        assertTrue(returnMap.containsKey(fieldName));
    }

    @Test
    public void cleanFieldsGivenArrayOfKeysAndMapOfFieldsReturnsMapWithOnlyFieldsFromKeysArrayNegativeTest(){
        Map returnMap = IndexUtil.cleanFields(keysArray,fieldMap);
        assertFalse(returnMap.containsKey(fieldName1));
    }

    @Test
    public void createSingleValueSetGivenStringValueReturnsSetContainingJustThatString(){
        Set singleValue = IndexUtil.createSingleValueSet(fieldValue);
        assertEquals(singleValue.size(), 1);
    }

    @Test
    public void createSingleValueSetGivenStringValueReturnsSetContainingJustThatStringNegativeTest(){
        Set singleValue = IndexUtil.createSingleValueSet(null);
        assertEquals(singleValue, null);
    }

    @Test
    public void openDirectoryGivenIndexDirectoryReturnsIndexWriter(){
        directory = IndexUtil.openDirectory(file);
        assertNotNull(directory);
    }

    @Test
    public void openDirectoryGivenNullIndexDirectoryReturnsNull(){
        directory = IndexUtil.openDirectory(null);
        assertNull(directory);
    }

    @Test
    public void openIndexWriterGivenIndexDirectoryReturnsIndexWriter(){
        directory = IndexUtil.openDirectory(file);
        indexWriter = IndexUtil.openIndexWriter(directory);
        assertNotNull(indexWriter);
    }

    @Test(expected = NullPointerException.class)
    public void openIndexWriterGivenNullIndexDirectoryThrowsNullPointer(){
        directory = IndexUtil.openDirectory(null);
        indexWriter = IndexUtil.openIndexWriter(directory);
        assertNull(indexWriter);
    }

    @Test
    public void openSearcherManagerGivenIndexDirectoryReturnsSearcherManager(){
        directory = IndexUtil.openDirectory(file);
        searcherManager= IndexUtil.openSearcherManager(directory);
        assertNotNull(searcherManager);
    }

    @Test
    public void openSearcherManagerGivenReturnsNull(){
        directory = null;
        searcherManager= IndexUtil.openSearcherManager(directory);
        assertNull(searcherManager);
    }

    @Test
    public void openSearcherManagerGivenIndexWriterReturnsSearcherManager(){
        directory = IndexUtil.openDirectory(file);
        searcherManager= IndexUtil.openSearcherManager(directory);
        assertNotNull(searcherManager);
    }

    @Test(expected = NullPointerException.class)
    public void openSearcherManagerGiveNullIndexWriterReturnsNull(){
        indexWriter = null;
        searcherManager= IndexUtil.openSearcherManager(indexWriter);
        assertNull(searcherManager);
    }

}
