package com.usaa.bank.graph.lucene;


import com.usaa.bank.graph.common.exception.DeMaRuntimeException;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class Test_LuceneIndexDAOLocalImpl {

    static LuceneIndexDAOLocalImpl luceneIndexDAOLocal;
    static Document document;
    static final String termName = "termName";
    static final String termValue = "termValue";
    static final Set<String> fieldsSet = new HashSet<>();
    static HashMap fields = new HashMap<String, Set<String>>();
    static HashMap searchFields = new HashMap<String, String>();



    @Before
    public void setup(){
        tryCloseSession();
        fieldsSet.add(termValue);
        fields.put(termName, fieldsSet);
        searchFields.put(termName, termValue);
        luceneIndexDAOLocal = new LuceneIndexDAOLocalImpl(new RAMDirectory());
        document = DocumentBuilder.createDocument("testGUID", null, fields);
    }

    @Test(expected = DeMaRuntimeException.class)
    public void openSessionGivenIndexToIndexThrowsExceptionDueToAlreadyOpenIndexSession(){
        luceneIndexDAOLocal.openSession();
        luceneIndexDAOLocal.openSession();
        luceneIndexDAOLocal.closeSession();
    }

    @Test(expected = DeMaRuntimeException.class)
    public void openReadOnlySessionGivenIndexThrowsExceptionDueToAlreadyOpenIndexSession(){
        luceneIndexDAOLocal.openReadOnlySession();
        luceneIndexDAOLocal.closeSession();
    }

    @Test
    public void openReadOnlySessionGivenIndex(){
        luceneIndexDAOLocal.openSession();
        luceneIndexDAOLocal.saveDocument(termName, termValue, document);
        luceneIndexDAOLocal.closeSession();
        luceneIndexDAOLocal.openReadOnlySession();
        luceneIndexDAOLocal.closeSession();
    }

    @Test(expected = DeMaRuntimeException.class)
    public void closeSessionGivenIndexToIndexThrowsExceptionDueToAlreadyClosedIndexSession(){
        luceneIndexDAOLocal.closeSession();
    }

    @Test
    public void closeSessionGivenIndex(){
        luceneIndexDAOLocal.openSession();
        luceneIndexDAOLocal.closeSession();
    }

    @Test(expected = NullPointerException.class)
    public void flushSessionGivenIndexToIndexThrowsExceptionDueToNoOpenIndexSession(){
        luceneIndexDAOLocal.flushSession();
        luceneIndexDAOLocal.closeSession();
    }

    @Test
    public void saveDocumentGivenDocumentAndStringValues(){
        luceneIndexDAOLocal.openSession();
        assertTrue(luceneIndexDAOLocal.saveDocument(termName, termValue, document));
        luceneIndexDAOLocal.closeSession();
    }

    @Test
    public void findDocumentsGivenStringMaps(){
        luceneIndexDAOLocal.openSession();
        luceneIndexDAOLocal.saveDocument(termName, termValue, document);
        luceneIndexDAOLocal.flushSession();
        assertEquals(luceneIndexDAOLocal.findDocuments(searchFields).size(), 1);
        luceneIndexDAOLocal.closeSession();
    }

    @Test
    public void findDocumentsGivenStringMapAndOffsetAndResultsWanted(){
        luceneIndexDAOLocal.openSession();
        luceneIndexDAOLocal.saveDocument(termName, termValue, document);
        luceneIndexDAOLocal.flushSession();
        assertEquals(luceneIndexDAOLocal.findDocuments(searchFields,0,5).size(), 1);
        luceneIndexDAOLocal.closeSession();
    }



    @Test
    public void getDocumentCountGivenIndexReturnsCount(){
        luceneIndexDAOLocal.openSession();
        luceneIndexDAOLocal.saveDocument(termName, termValue, document);
        luceneIndexDAOLocal.flushSession();
        luceneIndexDAOLocal.getIndexSearcher().getIndexReader().maxDoc();
        assertEquals(luceneIndexDAOLocal.getDocumentCount(), 1);
        luceneIndexDAOLocal.closeSession();

    }

    @Test
    public void getIndexWriterReturnsTheCurrentIndexerWriter(){
        luceneIndexDAOLocal.openSession();
        assertNotNull(luceneIndexDAOLocal.getIndexWriter());
        luceneIndexDAOLocal.closeSession();
    }

    @Test
    public void getIndexSearcherReturnsTheCurrentIndexerSearcher(){
        luceneIndexDAOLocal.openSession();
        luceneIndexDAOLocal.saveDocument(termName, termValue, document);
        luceneIndexDAOLocal.flushSession();
        assertNotNull(luceneIndexDAOLocal.getIndexSearcher());
        luceneIndexDAOLocal.closeSession();
    }

    @Test
    public void deleteDocumentsReturnsTrueWhenGivenFields(){
        luceneIndexDAOLocal.openSession();
        luceneIndexDAOLocal.saveDocument(termName, termValue, document);
        luceneIndexDAOLocal.flushSession();
        assertTrue(luceneIndexDAOLocal.deleteDocuments(searchFields));
        luceneIndexDAOLocal.closeSession();
    }

    private static void tryCloseSession(){
        try {
            luceneIndexDAOLocal.closeSession();
        }catch(Exception e){
        }
    }


}
