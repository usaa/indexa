package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.ui.component.wicket.IWicketApplicationArtifact;
import com.usaa.bank.asset.index.api.domain.ui.component.wicket.WicketApplicationArtifact;
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

public class LuceneWicketApplicationArtifactIndexTest {
    private LuceneWicketApplicationArtifactIndex luceneWicketApplicationArtifactIndex;
    private static GUID guid;
    private static IWicketApplicationArtifact wicketApplicationArtifact;
    private static final String wicketApplicationName = "testwicketApplicationName";
    private static final String wicketInfrastructureClass = "testwicketInfrastructureClass ";
    private static final String wicketDisplayName = "testwicketDisplayName";
    private static final String description = "testdescription";
    private static final String wicketApplicationClass = "testwicketApplicationClass";
    private static final String wicketApplicationArchiveName = "testwicketApplicationArchiveName ";
    private Map<String, String> mappingData = new HashMap<String, String>();
    private Map<String, String> jvmComponentMapping = new HashMap<String, String>();

    @Before
    public void setUp() throws Exception {
        this.luceneWicketApplicationArtifactIndex = new LuceneWicketApplicationArtifactIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        mappingData.put("ear", "earName");
        mappingData.put("war", "warName");

        jvmComponentMapping.put("jvm1", "comp1");
        jvmComponentMapping.put("jvm2", "comp2");
        wicketApplicationArtifact = new WicketApplicationArtifact(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        guid = WicketApplicationArtifact.createGUID(wicketApplicationName, wicketApplicationClass);
        luceneWicketApplicationArtifactIndex.openSession();
    }

    @After
    public void tearDown() throws Exception {
        luceneWicketApplicationArtifactIndex.closeSession();
    }

    @Test
    public void createWicketApplicationArtifact() throws Exception {
        IWicketApplicationArtifact WicketApplicationArtifactIndexed = luceneWicketApplicationArtifactIndex.create(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        assertEquals(wicketApplicationArtifact, WicketApplicationArtifactIndexed);
    }

    @Test
    public void createWicketApplicationArtifactAndPopulateInIndex() throws Exception {
        luceneWicketApplicationArtifactIndex.create(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        luceneWicketApplicationArtifactIndex.flushSession();
        assertEquals(1, luceneWicketApplicationArtifactIndex.find(wicketApplicationName).size());
    }

    @Test
    public void removeWicketApplicationArtifactGivenArchiveNameAndImplementationClass() throws Exception {
        luceneWicketApplicationArtifactIndex.create(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        luceneWicketApplicationArtifactIndex.flushSession();
        luceneWicketApplicationArtifactIndex.remove(guid);
        luceneWicketApplicationArtifactIndex.flushSession();
        assertEquals(0, luceneWicketApplicationArtifactIndex.find().size());
    }

    @Test
    public void getWicketApplicationArtifactGivenArchiveNameAndImplementationClass() throws Exception {
        luceneWicketApplicationArtifactIndex.create(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        luceneWicketApplicationArtifactIndex.flushSession();
        IWicketApplicationArtifact WicketApplicationArtifactResult = luceneWicketApplicationArtifactIndex.get(wicketApplicationName, wicketApplicationClass);
        assertEquals(WicketApplicationArtifactResult, wicketApplicationArtifact);
    }

    @Test
    public void findWicketApplicationArtifactsGivenArchiveName() throws Exception {
        luceneWicketApplicationArtifactIndex.create(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        luceneWicketApplicationArtifactIndex.flushSession();
        Set<IWicketApplicationArtifact> WicketApplicationArtifactSet = luceneWicketApplicationArtifactIndex.find(wicketApplicationName);
        assertEquals(1, WicketApplicationArtifactSet.size());
    }

    @Test
    public void saveWicketApplicationArtifactInIndexGivenWicketApplicationArtifact() throws Exception {
        luceneWicketApplicationArtifactIndex.save(wicketApplicationArtifact);
        luceneWicketApplicationArtifactIndex.flushSession();
        IWicketApplicationArtifact WicketApplicationArtifactIndexed = luceneWicketApplicationArtifactIndex.get(guid);
        assertEquals(WicketApplicationArtifactIndexed, wicketApplicationArtifact);
    }

    @Test
    public void removeWicketApplicationArtifactFromIndexGivenGUID() throws Exception {
        luceneWicketApplicationArtifactIndex.create(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        luceneWicketApplicationArtifactIndex.flushSession();
        boolean removed = luceneWicketApplicationArtifactIndex.remove(guid);
        assertEquals(removed, true);
    }

    @Test

    public void getWicketApplicationArtifactFromIndexGivenGUID() throws Exception {
        IWicketApplicationArtifact WicketApplicationArtifact = luceneWicketApplicationArtifactIndex.create(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        luceneWicketApplicationArtifactIndex.flushSession();
        IWicketApplicationArtifact WicketApplicationArtifactIndexed = luceneWicketApplicationArtifactIndex.get(guid);
        assertEquals(WicketApplicationArtifactIndexed.getGUID(), WicketApplicationArtifact.getGUID());
    }

    @Test
    public void findAllWicketApplicationArtifactsFromIndex() throws Exception {
        luceneWicketApplicationArtifactIndex.create(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        luceneWicketApplicationArtifactIndex.flushSession();
        luceneWicketApplicationArtifactIndex.create("test2wicketapplicationname", wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        luceneWicketApplicationArtifactIndex.flushSession();
        Set<IWicketApplicationArtifact> WicketApplicationArtifactsSet = luceneWicketApplicationArtifactIndex.find();
        assertEquals(2, WicketApplicationArtifactsSet.size());

    }

    @Test
    public void findWicketApplicationArtifactGivenFieldKeyAndFieldValue() throws Exception {
        luceneWicketApplicationArtifactIndex.create(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        luceneWicketApplicationArtifactIndex.flushSession();
        Set<IWicketApplicationArtifact> WicketApplicationArtifactSet = luceneWicketApplicationArtifactIndex.find("guid", guid.getStringValue());
        assertEquals(1, WicketApplicationArtifactSet.size());
    }

    @Test
    public void getSizeOfWicketApplicationArtifactIndex() throws Exception {
        luceneWicketApplicationArtifactIndex.create(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        luceneWicketApplicationArtifactIndex.flushSession();
        int size = luceneWicketApplicationArtifactIndex.getIndexSize();
        assertEquals(size, 1);
    }
}