package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.ui.component.ps.app.IPresentationServicesPageArtifact;
import com.usaa.bank.asset.index.api.domain.ui.component.ps.app.PresentationServicePageArtifact;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LucenePresentationServicePageArtifactIndexTest {

    private LucenePresentationServicePageArtifactIndex psAppIndexer = null;
    private IPresentationServicesPageArtifact presentationServiceApplicationIndex;

    private static GUID guid;

    private static final String applicationName = "test_ps_app_name";
    private static final String contextRoot = "test_ps_app_context_root ";

    private static final String applicationNameTwo = "test_ps_app_name_two";
    private static final String contextRootTwo = "test_ps_app_context_root_two";

    private static final String pageId = "test_ps_app_page_id";
    private static final String pageName = "test_ps_app_page_name";
    private static final String pageHeading = "test_ps_app_page_heading";
    private static final String pageTitle = "test_ps_app_page_title";
    private Map<String, String> nodeAttributes = new HashMap<String, String>();
    private Map<String, String> mappingData = new HashMap<String, String>();
    private Map<String, String> jvmComponentMapping = new HashMap<String, String>();

    @Before
    public void runOnceBeforeClass() {
        this.psAppIndexer = new LucenePresentationServicePageArtifactIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));

        nodeAttributes.put("a", "a1");
        nodeAttributes.put("b", "a2");
        nodeAttributes.put("c", "a3");

        mappingData.put("ear", "earName");
        mappingData.put("war", "warName");

        jvmComponentMapping.put("jvm1", "comp1");
        jvmComponentMapping.put("jvm2", "comp2");
        presentationServiceApplicationIndex = new PresentationServicePageArtifact(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        guid = PresentationServicePageArtifact.createGUID(contextRoot, pageId);


        psAppIndexer.openSession();
    }

    @After
    public void tearDownAfterClass() {
        psAppIndexer.closeSession();
    }

    @Test
    public void createPresentationServicesArtifact() throws Exception {
        IPresentationServicesPageArtifact presentationServiceApplicationResult = psAppIndexer.create(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        assertEquals(presentationServiceApplicationResult, presentationServiceApplicationIndex);
    }

    @Test
    public void createPresentationServicesArtifactAndPopulateInIndex() throws Exception {
        psAppIndexer.create(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        psAppIndexer.flushSession();
        assertEquals(1, psAppIndexer.find(applicationName).size());
    }

    @Test
    public void createAndRemovePresentationServicesArtifact() throws Exception {
        psAppIndexer.create(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        psAppIndexer.flushSession();
        psAppIndexer.remove(guid);
        psAppIndexer.flushSession();
        assertEquals(0, psAppIndexer.find().size());
    }

    @Test
    public void searchPresentationServicesIndexer() throws Exception {
        psAppIndexer.create(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        psAppIndexer.flushSession();
        IPresentationServicesPageArtifact presentationServiceApplicationResult = psAppIndexer.get(applicationName, contextRoot, pageId, pageName);
        assertEquals(presentationServiceApplicationResult, presentationServiceApplicationIndex);
    }

    @Test
    public void findPresentationServicesArtifactsGivenArchiveName() throws Exception {
        psAppIndexer.create(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        psAppIndexer.flushSession();
        Set<IPresentationServicesPageArtifact> presentationServiceApplicationSet = psAppIndexer.find(applicationName);
        assertEquals(1, presentationServiceApplicationSet.size());
    }

    @Test
    public void savePresentationServicesArtifactInIndexGivenArtifact() throws Exception {
        psAppIndexer.save(presentationServiceApplicationIndex);
        psAppIndexer.flushSession();
        IPresentationServicesPageArtifact presentationServiceApplicationIndexed = psAppIndexer.get(guid);
        assertEquals(presentationServiceApplicationIndexed, presentationServiceApplicationIndex);
    }

    @Test
    public void removePresentationServicesArtifactFromIndexGivenGUID() throws Exception {
        psAppIndexer.create(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        psAppIndexer.flushSession();
        boolean removed = psAppIndexer.remove(guid);
        assertEquals(removed, true);
    }

    @Test

    public void getPresentationServicesArtifactFromIndexGivenGUID() throws Exception {
        IPresentationServicesPageArtifact PresentationServicesArtifact = psAppIndexer.create(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        psAppIndexer.flushSession();
        IPresentationServicesPageArtifact presentationServioceArtifactIndexed = psAppIndexer.get(guid);
        assertEquals(presentationServioceArtifactIndexed.getGUID(), PresentationServicesArtifact.getGUID());
    }

    @Test
    public void findAllPresentationServicesArtifactsFromIndex() throws Exception {
        psAppIndexer.create(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        psAppIndexer.flushSession();
        psAppIndexer.create(applicationNameTwo, contextRootTwo, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        psAppIndexer.flushSession();
        Set<IPresentationServicesPageArtifact> presentationServiceArtifactsSet = psAppIndexer.find();
        assertEquals(2, presentationServiceArtifactsSet.size());
    }

    @Test
    public void findPresentationServicesArtifactGivenFieldKeyAndFieldValue() throws Exception {
        psAppIndexer.create(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        psAppIndexer.flushSession();
        Set<IPresentationServicesPageArtifact> presentationServiceArtifactsSet = psAppIndexer.find("guid", guid.getStringValue());
        assertEquals(1, presentationServiceArtifactsSet.size());
    }

    @Test
    public void getSizeOfPresentationServicesArtifactIndex() throws Exception {
        psAppIndexer.create(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);
        psAppIndexer.flushSession();
        int size = psAppIndexer.getIndexSize();
        assertEquals(size, 1);
    }

}
