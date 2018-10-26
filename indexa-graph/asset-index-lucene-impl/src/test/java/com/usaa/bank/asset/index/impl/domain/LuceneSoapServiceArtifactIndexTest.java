package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.web.services.soap.services.ISoapServiceArtifact;
import com.usaa.bank.asset.index.api.domain.web.services.soap.services.SoapServiceArtifact;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LuceneSoapServiceArtifactIndexTest {
    private LuceneSoapServiceArtifactIndex luceneSoapServiceArtifactIndex;
    private static GUID guid;
    private static ISoapServiceArtifact soapServiceArtifact;
    private static final String archiveName = "testArchiveName";
    private static final String contextRoot = "testContextRoot";
    private static final String serviceDefinitionType = "testServiceDefintionType";
    private static final String serviceName = "testServiceName";
    private static final String implementationClass = "testImplementationClass";
    private static final String interfaceClass = "testInterfaceClass";
    private static final String operationName = "testOperationName";
    private static final String artifactPath = "testArtifactPath";
    private Map<String, String> mappingData = new HashMap<String, String>();
    private Map<String, String> jvmComponentMapping = new HashMap<String, String>();

    @Before
    public void setUp() throws Exception {
        this.luceneSoapServiceArtifactIndex = new LuceneSoapServiceArtifactIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        mappingData.put("ear", "earName");
        mappingData.put("war", "warName");

        jvmComponentMapping.put("jvm1", "comp1");
        jvmComponentMapping.put("jvm2", "comp2");
        soapServiceArtifact = new SoapServiceArtifact(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        guid = SoapServiceArtifact.createGUID(archiveName, serviceName, operationName);
        luceneSoapServiceArtifactIndex.openSession();
    }

    @After
    public void tearDown() throws Exception {
        luceneSoapServiceArtifactIndex.closeSession();
    }

    @Test
    public void createSoapServiceArtifact() throws Exception {
        ISoapServiceArtifact soapServiceArtifactIndexed = luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        assertEquals(soapServiceArtifact, soapServiceArtifactIndexed);

    }

    @Test
    public void createSoapServiceArtifactAndPopulateInIndex() throws Exception {
        luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        assertEquals(1, luceneSoapServiceArtifactIndex.find(archiveName).size());
    }

    @Test
    public void removeSoapServiceArtifactGivenArchiveNameAndImplementationClass() throws Exception {
        luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        luceneSoapServiceArtifactIndex.remove(guid);
        luceneSoapServiceArtifactIndex.flushSession();
        assertEquals(0, luceneSoapServiceArtifactIndex.find().size());
    }

    @Test
    public void getSoapServiceArtifactGivenArchiveNameAndImplementationClass() throws Exception {
        luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        ISoapServiceArtifact soapServiceArtifactResult = luceneSoapServiceArtifactIndex.get(archiveName, serviceName, operationName);
        assertEquals(soapServiceArtifactResult, soapServiceArtifact);
    }

    @Test
    public void findSoapServiceArtifactsGivenArchiveName() throws Exception {
        luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        Set<ISoapServiceArtifact> soapServiceArtifactSet = luceneSoapServiceArtifactIndex.find(archiveName);
        assertEquals(1, soapServiceArtifactSet.size());
    }

    @Test
    public void saveSoapServiceArtifactInIndexGivenSoapServiceArtifact() throws Exception {
        luceneSoapServiceArtifactIndex.save(soapServiceArtifact);
        luceneSoapServiceArtifactIndex.flushSession();
        ISoapServiceArtifact soapServiceArtifactIndexed = luceneSoapServiceArtifactIndex.get(guid);
        assertEquals(soapServiceArtifactIndexed, soapServiceArtifact);
    }

    @Test
    public void removeSoapServiceArtifactFromIndexGivenGUID() throws Exception {
        luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        boolean removed = luceneSoapServiceArtifactIndex.remove(guid);
        assertEquals(removed, true);
    }

    @Test

    public void getSoapServiceArtifactFromIndexGivenGUID() throws Exception {
        ISoapServiceArtifact soapServiceArtifact = luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        ISoapServiceArtifact soapServiceArtifactIndexed = luceneSoapServiceArtifactIndex.get(guid);
        assertEquals(soapServiceArtifactIndexed.getGUID(), soapServiceArtifact.getGUID());
    }

    @Test
    public void findAllSoapServiceArtifactsFromIndex() throws Exception {
        luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        luceneSoapServiceArtifactIndex.create("test2archiveName", contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        Set<ISoapServiceArtifact> soapServiceArtifactsSet = luceneSoapServiceArtifactIndex.find();
        assertEquals(2, soapServiceArtifactsSet.size());

    }

    @Test
    public void findSoapServiceArtifactGivenFieldKeyAndFieldValue() throws Exception {
        luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName,artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        Set<ISoapServiceArtifact> soapServiceArtifactSet = luceneSoapServiceArtifactIndex.find("guid", guid.getStringValue());
        assertEquals(1, soapServiceArtifactSet.size());
    }

    @Test
    public void getSizeOfSoapServiceArtifactIndex() throws Exception {
        luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName,artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        int size = luceneSoapServiceArtifactIndex.getIndexSize();
        assertEquals(size, 1);
    }

    @Test
    public void checkingEqualsMethodByAddingTwoDocsAndOnlyReturningOne() throws Exception {
        luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName,artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.create(archiveName, contextRoot, serviceDefinitionType + "ot", serviceName, implementationClass, interfaceClass + "o", "\"" + operationName,artifactPath, mappingData, jvmComponentMapping);
        luceneSoapServiceArtifactIndex.flushSession();
        int size = luceneSoapServiceArtifactIndex.getIndexSize();
        assertEquals(size, 2);
        assertEquals(luceneSoapServiceArtifactIndex.find().size(), 1);
    }
}