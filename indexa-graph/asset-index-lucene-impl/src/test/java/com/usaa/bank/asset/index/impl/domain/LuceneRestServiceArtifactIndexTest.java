package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.web.services.rest.resources.IRestServiceArtifact;
import com.usaa.bank.asset.index.api.domain.web.services.rest.resources.RestServiceArtifact;
import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LuceneRestServiceArtifactIndexTest {
    private LuceneRestServiceArtifactIndex luceneRestServiceArtifactIndex;
    private static GUID guid;
    private static IRestServiceArtifact restServiceArtifact;
    private static final String archiveName = "testarchiveName";
    private static final String contextRoot = "testcontextRoot";
    private static final String httpMethod = "testhttpMethod";
    private static final String restServicePath = "testrestServicePath";
    private static final String restOperationPath = "testrestOperationPath";
    private static final String restOperationMethod = "testestOperationMethod";
    private static final String restRolesAllowed = "testestRolesAllowed";
    private static final String restArtifactPath = "testestArtifactPath";
    private Map<String, String> mappingData = new HashMap<String, String>();
    private Map<String, String> jvmComponentMapping = new HashMap<String, String>();

    @Before
    public void setUp() throws Exception {
        this.luceneRestServiceArtifactIndex = new LuceneRestServiceArtifactIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        mappingData.put("ear", "earName");
        mappingData.put("war", "warName");

        jvmComponentMapping.put("jvm1", "comp1");
        jvmComponentMapping.put("jvm2", "comp2");
        restServiceArtifact = new RestServiceArtifact(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod,
                restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        guid = RestServiceArtifact.createGUID(archiveName, restServicePath + "/" + restOperationPath, httpMethod);
        luceneRestServiceArtifactIndex.openSession();
    }

    @After
    public void tearDown() throws Exception {
        luceneRestServiceArtifactIndex.closeSession();
    }

    @Test
    public void createRestServiceArtifact() throws Exception {
        IRestServiceArtifact RestServiceArtifactIndexed = luceneRestServiceArtifactIndex.create(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        assertEquals(restServiceArtifact, RestServiceArtifactIndexed);

    }

    @Test
    public void createRestServiceArtifactAndPopulateInIndex() throws Exception {
        luceneRestServiceArtifactIndex.create(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        luceneRestServiceArtifactIndex.flushSession();
        assertEquals(1, luceneRestServiceArtifactIndex.find(archiveName).size());
    }

    @Test
    public void removeRestServiceArtifactGivenArchiveNameAndImplementationClass() throws Exception {
        luceneRestServiceArtifactIndex.create(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        luceneRestServiceArtifactIndex.flushSession();
        luceneRestServiceArtifactIndex.remove(guid);
        luceneRestServiceArtifactIndex.flushSession();
        assertEquals(0, luceneRestServiceArtifactIndex.find().size());
    }

    @Test
    public void getRestServiceArtifactGivenArchiveNameAndImplementationClass() throws Exception {
        luceneRestServiceArtifactIndex.create(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        luceneRestServiceArtifactIndex.flushSession();
        IRestServiceArtifact RestServiceArtifactResult = luceneRestServiceArtifactIndex.get(archiveName, restServicePath, httpMethod);
        assertEquals(RestServiceArtifactResult, restServiceArtifact);
    }

    @Test
    public void findRestServiceArtifactsGivenArchiveName() throws Exception {
        luceneRestServiceArtifactIndex.create(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        luceneRestServiceArtifactIndex.flushSession();
        Set<IRestServiceArtifact> RestServiceArtifactSet = luceneRestServiceArtifactIndex.find(archiveName);
        assertEquals(1, RestServiceArtifactSet.size());
    }

    @Test
    public void saveRestServiceArtifactInIndexGivenRestServiceArtifact() throws Exception {
        luceneRestServiceArtifactIndex.save(restServiceArtifact);
        luceneRestServiceArtifactIndex.flushSession();
        IRestServiceArtifact RestServiceArtifactIndexed = luceneRestServiceArtifactIndex.get(guid);
        assertEquals(RestServiceArtifactIndexed, restServiceArtifact);
    }

    @Test
    public void removeRestServiceArtifactFromIndexGivenGUID() throws Exception {
        luceneRestServiceArtifactIndex.create(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        luceneRestServiceArtifactIndex.flushSession();
        boolean removed = luceneRestServiceArtifactIndex.remove(guid);
        assertEquals(removed, true);
    }

    @Test

    public void getRestServiceArtifactFromIndexGivenGUID() throws Exception {
        IRestServiceArtifact RestServiceArtifact = luceneRestServiceArtifactIndex.create(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        luceneRestServiceArtifactIndex.flushSession();
        IRestServiceArtifact RestServiceArtifactIndexed = luceneRestServiceArtifactIndex.get(guid);
        assertEquals(RestServiceArtifactIndexed.getGUID(), RestServiceArtifact.getGUID());
    }

    @Test
    public void findAllRestServiceArtifactsFromIndex() throws Exception {
        luceneRestServiceArtifactIndex.create(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        luceneRestServiceArtifactIndex.flushSession();
        luceneRestServiceArtifactIndex.create("test2archiveName", contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        luceneRestServiceArtifactIndex.flushSession();
        Set<IRestServiceArtifact> RestServiceArtifactsSet = luceneRestServiceArtifactIndex.find();
        assertEquals(2, RestServiceArtifactsSet.size());

    }

    @Test
    public void findRestServiceArtifactGivenFieldKeyAndFieldValue() throws Exception {
        luceneRestServiceArtifactIndex.create(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        luceneRestServiceArtifactIndex.flushSession();
        Set<IRestServiceArtifact> RestServiceArtifactSet = luceneRestServiceArtifactIndex.find("guid", guid.getStringValue());
        assertEquals(1, RestServiceArtifactSet.size());
    }

    @Test
    public void getSizeOfRestServiceArtifactIndex() throws Exception {
        luceneRestServiceArtifactIndex.create(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod, restArtifactPath, restRolesAllowed, mappingData, jvmComponentMapping);
        luceneRestServiceArtifactIndex.flushSession();
        int size = luceneRestServiceArtifactIndex.getIndexSize();
        assertEquals(size, 1);
    }

    @Test
    public void addALuceneDocumentEntry() throws Exception {
        Document doc = DocumentBuilder.createDocument(restServiceArtifact);

        DocumentBuilder.addField("dummy_key", "dummy_value", doc);
        assertEquals("dummy_value", doc.getField("dummy_key").stringValue());
    }

    @Test
    public void copyLuceneDocument() throws Exception {
        Document doc = DocumentBuilder.createDocument(restServiceArtifact);

        Document doc_copy = DocumentBuilder.copyDocument(doc);
        assertEquals(doc.getFields(), doc_copy.getFields());
    }

}