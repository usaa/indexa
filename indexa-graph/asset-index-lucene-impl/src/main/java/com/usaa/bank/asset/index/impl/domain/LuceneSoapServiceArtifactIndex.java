package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.web.services.soap.services.ISoapServiceArtifact;
import com.usaa.bank.asset.index.api.domain.web.services.soap.services.ISoapServiceArtifactIndex;
import com.usaa.bank.asset.index.api.domain.web.services.soap.services.SoapServiceArtifact;
import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.common.validate.BasicValidator;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LuceneSoapServiceArtifactIndex extends RealtimeLuceneIndex implements ISoapServiceArtifactIndex {
    private static final String KEY_GUID = "guid";
    private static final String[] keys = {SoapServiceArtifact.archiveName, SoapServiceArtifact.contextRoot, SoapServiceArtifact.serviceDefinitionType, SoapServiceArtifact.serviceName, SoapServiceArtifact.implementationClass, SoapServiceArtifact.interfaceClass, SoapServiceArtifact.operationName};

    public LuceneSoapServiceArtifactIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }


    @Override
    public ISoapServiceArtifact create(String archiveName, String contextRoot, String serviceDefinitionType, String serviceName, String implementationClass, String interfaceClass, String operationName, String artifactPath, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        BasicValidator.validateNotNull("archiveName", archiveName);
        BasicValidator.validateNotNull("serviceName", serviceName);
        BasicValidator.validateNotNull("operationName", operationName);
        ISoapServiceArtifact soapServiceArtifact = new SoapServiceArtifact(archiveName, contextRoot, serviceDefinitionType, serviceName, implementationClass, interfaceClass, operationName, artifactPath, mappingData, jvmComponentMapping);
        if (this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, soapServiceArtifact.getGUID().getStringValue(), DocumentBuilder.createDocument(soapServiceArtifact))) {
            return soapServiceArtifact;
        }
        return null;
    }

    @Override
    public boolean remove(String archiveName, String serviceName, String operationName) {
        BasicValidator.validateNotNull("archiveName", archiveName);
        BasicValidator.validateNotNull("serviceName", serviceName);
        BasicValidator.validateNotNull("operationName", operationName);
        Map<String, String> fields = new HashMap<>();
        fields.put(SoapServiceArtifact.archiveName, archiveName);
        fields.put(SoapServiceArtifact.serviceName, serviceName);
        fields.put(SoapServiceArtifact.operationName, operationName);
        this.getLuceneIndexDAO().deleteDocuments(fields);
        return true;
    }

    @Override
    public ISoapServiceArtifact get(String archiveName, String serviceName, String operationName) {
        BasicValidator.validateNotNull("archiveName", archiveName);
        BasicValidator.validateNotNull("serviceName", serviceName);
        BasicValidator.validateNotNull("operationName", operationName);
        ISoapServiceArtifact result = null;
        Map<String, String> fields = new HashMap<>();
        GUID guid = SoapServiceArtifact.createGUID(archiveName, serviceName, operationName);
        fields.put(KEY_GUID, guid.toString());
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            result = LuceneSoapServiceArtifactIndex.createSoapServiceArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    @Override
    public Set<ISoapServiceArtifact> find(String archiveName) {
        BasicValidator.validateNotNull("archiveName", archiveName);
        Set<ISoapServiceArtifact> results = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(SoapServiceArtifact.archiveName, archiveName);
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            results = LuceneSoapServiceArtifactIndex.createSoapServiceArtifactSet(resultList);
        }
        return results;

    }

    @Override
    public ISoapServiceArtifact create(Set<String> tags, Map<String, Set<String>> fields) {
        BasicValidator.validateNotNull("fields", fields);
        try {
            Set<Document> docs = null;
            Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);
            String guid = GUIDFactory.createGUID(DigitalAsset.getFirstValueString(SoapServiceArtifact.KEY_GUID, fields)).toString();
            docs.add(DocumentBuilder.createDocument(guid, tags, fields));
            this.getLuceneIndexDAO().saveDocument(SoapServiceArtifact.KEY_GUID, guid, docs.iterator().next());
            return LuceneSoapServiceArtifactIndex.createSoapServiceArtifactSet(docs).iterator().next();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public boolean save(ISoapServiceArtifact digitalAsset) {
        BasicValidator.validateNotNull("digitalAsset", digitalAsset);
        BasicValidator.validateNotNull("guid", digitalAsset.getGUID());
        return this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, digitalAsset.getGUID().getStringValue(), DocumentBuilder.createDocument(digitalAsset));
    }

    @Override
    public boolean remove(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        Map<String, String> fields = new HashMap<>();
        fields.put(DigitalAsset.KEY_GUID, guid.getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);
        return true;
    }

    @Override
    public ISoapServiceArtifact get(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        ISoapServiceArtifact result = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(LuceneSoapServiceArtifactIndex.KEY_GUID, guid.getStringValue());
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            result = LuceneSoapServiceArtifactIndex.createSoapServiceArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    @Override
    public Set<ISoapServiceArtifact> find() {
        return LuceneSoapServiceArtifactIndex.createSoapServiceArtifactSet((this.getLuceneIndexDAO().findDocuments(null)));
    }

    @Override
    public Set<ISoapServiceArtifact> find(String fieldKey, String fieldValue) {
        BasicValidator.validateNotNull("fieldValue", fieldValue);
        Map<String, String> fields = new HashMap<>();
        fields.put(fieldKey, fieldValue);
        return this.find(fields);
    }

    @Override
    public Set<ISoapServiceArtifact> find(Map<String, String> fields) {
        BasicValidator.validateNotNull("fields", fields);
        Set<ISoapServiceArtifact> results = null;
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        if (documents != null) {
            results = LuceneSoapServiceArtifactIndex.createSoapServiceArtifactSet(documents);
        }
        return results;
    }

    @Override
    public int getIndexSize() {
        return getLuceneIndexDAO().findDocuments(null).size();

    }

    private static Set<ISoapServiceArtifact> createSoapServiceArtifactSet(Set<Document> documents) {
        Set<ISoapServiceArtifact> soapServiceArtifactSet = new HashSet<>();
        if (documents != null) {
            for (Document document : documents) {

                Map<String, String> attributes = new HashMap<String, String>();
                // Create a MAP for the fields that are not present in the PresentationServicePageArtifact as CONSTANT field
                for (IndexableField field : document.getFields()) {
                    if (notKey(field.name())) {
                        attributes.put(field.name(), document.get(field.name()));
                    }
                }

                soapServiceArtifactSet.add(
                        new SoapServiceArtifact(
                                GUIDFactory.passGUID(document.get(KEY_GUID)),
                                document.get(SoapServiceArtifact.archiveName),
                                document.get(SoapServiceArtifact.contextRoot),
                                document.get(SoapServiceArtifact.serviceDefinitionType),
                                document.get(SoapServiceArtifact.serviceName),
                                document.get(SoapServiceArtifact.implementationClass),
                                document.get(SoapServiceArtifact.interfaceClass),
                                document.get(SoapServiceArtifact.operationName),
                                document.get(SoapServiceArtifact.SOAP_ARTIFACT_PATH), attributes, null
                        ));
            }
        }
        return soapServiceArtifactSet;
    }

    public static boolean notKey(String name) {
        boolean isKey = true;

        switch (name) {
            case SoapServiceArtifact.archiveName:
                isKey = false;
            case SoapServiceArtifact.contextRoot:
                isKey = false;
            case SoapServiceArtifact.implementationClass:
                isKey = false;
            case SoapServiceArtifact.interfaceClass:
                isKey = false;
            case SoapServiceArtifact.operationName:
                isKey = false;
            case SoapServiceArtifact.serviceDefinitionType:
                isKey = false;
            case SoapServiceArtifact.serviceName:
                isKey = false;
        }

        return isKey;
    }

//        public static void main(String[] args){
//        Set<Document> docs = new HashSet<>();
//        SoapServiceArtifact soapServiceArtifact1 = new SoapServiceArtifact("a","b","w","d","e","f","\"e\"",null,null);
//        SoapServiceArtifact soapServiceArtifact2 = new SoapServiceArtifact("a","b","c","d","e","f","e",null,null);
//        docs.add(DocumentBuilder.createDocument(soapServiceArtifact1));
//        docs.add(DocumentBuilder.createDocument(soapServiceArtifact2));
//        System.out.println(docs.size());
//        System.out.println(createSoapServiceArtifactSet(docs).size());
//
//    }
}

