package com.usaa.bank.graph.lucene;

import com.usaa.bank.graph.common.validate.ValidationException;
import org.apache.lucene.document.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class Test_DocumentBuilder {
    static Document document;
    static Document document2;
    static Set tags = new HashSet();
    static HashMap fields = new HashMap<String, Set<String>>();
    static final String tag1 = "test1";
    static final String tag2 = "test2";
    static final String field1 = "field1";
    static final String field2 = "field1";
    static final String guidValue = "testGUIDValue";
    static final String guidKey = "testGUIDkey";
    static final String tagsKey = "testTagsKey";


    @BeforeClass
    public static void setup(){
        tags.add(tag1);
        tags.add(tag2);
        fields.put(field1, tags);
    }


    @Test
    public void createDocumentGivenGUIDAndSetofTagsAndMapOfFieldsReturnsStoredDocument(){
        document = DocumentBuilder.createDocument(guidValue,tags,fields);
        assertEquals(document.toString(), "Document<stored,indexed,omitNorms,indexOptions=DOCS<guid:testGUIDValue> stored,indexed,omitNorms,indexOptions=DOCS<tags:test2> stored,indexed,omitNorms,indexOptions=DOCS<tags:test1> stored,indexed,omitNorms,indexOptions=DOCS<field1:test2> stored,indexed,omitNorms,indexOptions=DOCS<field1:test1>>");
    }

    @Test
    public void createDocumentGivenGUIDAndNullsReturnsStoredDocumentWithJustGUIDSaved(){
        document = DocumentBuilder.createDocument(guidValue,new HashSet(), new HashMap<String, Set<String>>());
        assertEquals(document.toString(), "Document<stored,indexed,omitNorms,indexOptions=DOCS<guid:testGUIDValue>>");
    }

    @Test
    public void createDocumentGivenNoGUIDAndNullsReturnsStoredDocument(){
        document = DocumentBuilder.createDocument(null,new HashSet(),new HashMap<String, Set<String>>());
        assertEquals(document.toString(), "Document<>");
    }

    @Test
    public void createDocumentGivenGUIDkeyAndGUIDValueAndTagsKeyAndSetOfTagsAndMapOfFieldsReturnsStoredDocument(){
        document = DocumentBuilder.createDocument(guidKey,guidValue,tagsKey,tags,fields);
        assertEquals(document.toString(), "Document<stored,indexed,omitNorms,indexOptions=DOCS<testGUIDkey:testGUIDValue> stored,indexed,omitNorms,indexOptions=DOCS<testTagsKey:test2> stored,indexed,omitNorms,indexOptions=DOCS<testTagsKey:test1> stored,indexed,omitNorms,indexOptions=DOCS<field1:test2> stored,indexed,omitNorms,indexOptions=DOCS<field1:test1>>");
    }
    @Test
    public void createDocumentGivenNullsReturnsStoredDocument(){
        document = DocumentBuilder.createDocument(null, null,null, null, null);
        assertEquals(document.toString(), "Document<>");
    }
    @Test
    public void createDocumentGivenGUIDStringAndMapOfFieldsReturnsStoredDocument(){
        document = DocumentBuilder.createDocument(guidValue, fields);
        assertEquals(document.toString(), "Document<stored,indexed,omitNorms,indexOptions=DOCS<guid:testGUIDValue> stored,indexed,omitNorms,indexOptions=DOCS<field1:test2> stored,indexed,omitNorms,indexOptions=DOCS<field1:test1>>");
    }
    @Test
    public void copyDocumentGivenNonNullDocument(){
        document = DocumentBuilder.createDocument(guidValue, fields);
         document2 = DocumentBuilder.copyDocument(document);
        assertEquals(document, document2);
    }

    @Test(expected = ValidationException.class)
    public void copyDocumentGivenNullDocumentThrowsValidationException(){
        document2 = DocumentBuilder.copyDocument(null);
    }

    @Test
    public void addFieldGivenFieldNameAndFieldValueAndDocument(){
        document = DocumentBuilder.addField(field1, field2, document);
        assertEquals(document.getField(field1).toString(),"stored,indexed,omitNorms,indexOptions=DOCS<field1:test2>");

    }





}
