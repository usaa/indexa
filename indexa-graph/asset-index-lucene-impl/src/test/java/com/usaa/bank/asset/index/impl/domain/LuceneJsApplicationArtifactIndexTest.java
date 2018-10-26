package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.ui.component.js.IJSApplicationArtifact;
import com.usaa.bank.asset.index.api.domain.ui.component.js.JSApplicationArtifact;
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

public class LuceneJsApplicationArtifactIndexTest {
    private LuceneJSApplicationArtifactIndex jsIndexer = null;
    private IJSApplicationArtifact jsApplicationIndex;

    private static GUID guid;

    private static final String appID = "test_new_web_app_id";
    private static final String appName = "test_new_web_app_name";

    private static final String appIDTwo = "test_new_web_app_id_two";
    private static final String appNameTwo = "test_new_web_app_name_two";

    private static final String appDescription = "test_new_web_app_description";
    private static final String appRev = "test_new_web_app_rev";
    private static final String appAuthor = "test_new_web_app_author";
    private static final String version = "test_new_web_app_version";
    private Map<String, String> nodeAttributes = new HashMap<String, String>();


    @Before
    public void runOnceBeforeClass() {
        this.jsIndexer = new LuceneJSApplicationArtifactIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        jsApplicationIndex = new JSApplicationArtifact(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        guid = PresentationServicePageArtifact.createGUID(appID, version);

        nodeAttributes.put("dependency:a", "a1");
        nodeAttributes.put("devDependency:b", "a2");
        nodeAttributes.put("peerDependency:c", "a3");

        jsIndexer.openSession();
    }

    @After
    public void tearDownAfterClass() {
        jsIndexer.closeSession();
    }

    @Test
    public void createJsApplicationArtifact() throws Exception {
        IJSApplicationArtifact presentationServiceApplicationResult = jsIndexer.create(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        assertEquals(presentationServiceApplicationResult, jsApplicationIndex);
    }

    @Test
    public void createJsApplicationArtifactAndPopulateInIndex() throws Exception {
        jsIndexer.create(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        jsIndexer.flushSession();
        assertEquals(1, jsIndexer.find(appID).size());
    }

    @Test
    public void createAndRemoveJsApplicationArtifact() throws Exception {
        jsIndexer.create(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        jsIndexer.flushSession();
        jsIndexer.remove(guid);
        jsIndexer.flushSession();
        assertEquals(0, jsIndexer.find().size());
    }

    @Test
    public void getJsApplicationArtifactGivenAppIdAndVersion() throws Exception {
        jsIndexer.create(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        jsIndexer.flushSession();
        IJSApplicationArtifact presentationServiceApplicationResult = jsIndexer.get(appID, version);
        assertEquals(presentationServiceApplicationResult, jsApplicationIndex);
    }

    @Test
    public void findJsApplicationArtifactsGivenAppName() throws Exception {
        jsIndexer.create(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        jsIndexer.flushSession();
        Set<IJSApplicationArtifact> presentationServiceApplicationSet = jsIndexer.find(appID);
        assertEquals(1, presentationServiceApplicationSet.size());
    }

    @Test
    public void saveJsApplicationArtifactInIndexGivenArtifact() throws Exception {
        jsIndexer.save(jsApplicationIndex);
        jsIndexer.flushSession();
        IJSApplicationArtifact presentationServiceApplicationIndexed = jsIndexer.get(guid);
        assertEquals(presentationServiceApplicationIndexed, jsApplicationIndex);
    }

    @Test
    public void removeJsApplicationArtifactFromIndexGivenGUID() throws Exception {
        jsIndexer.create(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        jsIndexer.flushSession();
        boolean removed = jsIndexer.remove(guid);
        assertEquals(removed, true);
    }

    @Test

    public void getJsApplicationArtifactFromIndexGivenGUID() throws Exception {
        IJSApplicationArtifact jsApplicationArtifact = jsIndexer.create(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        jsIndexer.flushSession();
        IJSApplicationArtifact presentationServioceArtifactIndexed = jsIndexer.get(guid);
        assertEquals(presentationServioceArtifactIndexed.getGUID(), jsApplicationArtifact.getGUID());
    }

    @Test
    public void findAllJsApplicationArtifactsFromIndex() throws Exception {
        jsIndexer.create(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        jsIndexer.flushSession();
        jsIndexer.create(appIDTwo, appNameTwo, appDescription, appRev, appAuthor, version, nodeAttributes);
        jsIndexer.flushSession();
        Set<IJSApplicationArtifact> presentationServiceArtifactsSet = jsIndexer.find();
        assertEquals(2, presentationServiceArtifactsSet.size());

    }

    @Test
    public void findJsApplicationArtifactGivenFieldKeyAndFieldValue() throws Exception {
        jsIndexer.create(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        jsIndexer.flushSession();
        Set<IJSApplicationArtifact> presentationServiceArtifactsSet = jsIndexer.find("guid", guid.getStringValue());
        assertEquals(1, presentationServiceArtifactsSet.size());
    }

    @Test
    public void getSizeOfJsApplicationArtifactIndex() throws Exception {
        jsIndexer.create(appID, appName, appDescription, appRev, appAuthor, version, nodeAttributes);
        jsIndexer.flushSession();
        int size = jsIndexer.getIndexSize();
        assertEquals(size, 1);
    }

}
