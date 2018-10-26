package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.web.services.rest.resources.IRestServiceArtifact;
import com.usaa.bank.asset.index.api.domain.web.services.rest.resources.IRestServiceArtifactIndex;
import com.usaa.bank.asset.index.api.domain.web.services.rest.resources.RestServiceArtifact;
import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.asset.index.impl.util.LuceneIndexUtil;
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

public class LuceneRestServiceArtifactIndex extends RealtimeLuceneIndex implements IRestServiceArtifactIndex {
    private static final String KEY_GUID = "guid";
    private static final String[] keys = {RestServiceArtifact.archiveName, RestServiceArtifact.contextRoot, RestServiceArtifact.httpMethod, RestServiceArtifact.restServicePath, RestServiceArtifact.restOperationPath, RestServiceArtifact.restOperationMethod};

    public LuceneRestServiceArtifactIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }

    @Override
    public IRestServiceArtifact create(String archiveName, String contextRoot, String httpMethod, String restServicePath, String restOperationPath, String restOperationMethod,
                                       String artifactPath, String rolesAllowed, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        BasicValidator.validateNotNull("archiveName", archiveName);
        BasicValidator.validateNotNull("restServicePath", restServicePath);
        BasicValidator.validateNotNull("httpMethod", httpMethod);

        IRestServiceArtifact restServiceArtifact = new RestServiceArtifact(archiveName, contextRoot, httpMethod, restServicePath, restOperationPath, restOperationMethod,
                artifactPath, rolesAllowed, mappingData, jvmComponentMapping);

        if (this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, restServiceArtifact.getGUID().getStringValue(), DocumentBuilder.createDocument(restServiceArtifact))) {
            return restServiceArtifact;
        }
        return null;

    }

    @Override
    public boolean remove(String archiveName, String restServicePath, String httpMethod) {
        BasicValidator.validateNotNull("archiveName", archiveName);
        BasicValidator.validateNotNull("restServicePath", restServicePath);
        BasicValidator.validateNotNull("httpMethod", httpMethod);
        Map<String, String> fields = new HashMap<>();
        fields.put(RestServiceArtifact.archiveName, archiveName);
        fields.put(RestServiceArtifact.restServicePath, restServicePath);
        this.getLuceneIndexDAO().deleteDocuments(fields);
        return true;
    }

    @Override
    public IRestServiceArtifact get(String archiveName, String restServicePath, String httpMethod) {
        BasicValidator.validateNotNull("archiveName", archiveName);
        BasicValidator.validateNotNull("restServicePath", restServicePath);
        BasicValidator.validateNotNull("httpMethod", httpMethod);
        IRestServiceArtifact result = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(RestServiceArtifact.archiveName, archiveName);
        fields.put(RestServiceArtifact.restServicePath, restServicePath);
        fields.put(RestServiceArtifact.httpMethod, httpMethod);
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            result = LuceneRestServiceArtifactIndex.createRestServiceArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    @Override
    public Set<IRestServiceArtifact> find(String archiveName) {
        BasicValidator.validateNotNull("archiveName", archiveName);
        Set<IRestServiceArtifact> results = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(RestServiceArtifact.archiveName, archiveName);
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            results = LuceneRestServiceArtifactIndex.createRestServiceArtifactSet(resultList);
        }
        return results;
    }

    @Override
    public IRestServiceArtifact create(Set<String> tags, Map<String, Set<String>> fields) {
        BasicValidator.validateNotNull("fields", fields);
        try {
            Set<Document> docs = null;
            Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);
            String guid = GUIDFactory.createGUID(DigitalAsset.getFirstValueString(RestServiceArtifact.KEY_GUID, fields)).toString();
            docs.add(DocumentBuilder.createDocument(guid, tags, fields));
            this.getLuceneIndexDAO().saveDocument(RestServiceArtifact.KEY_GUID, guid, docs.iterator().next());
            return LuceneRestServiceArtifactIndex.createRestServiceArtifactSet(docs).iterator().next();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }


    @Override
    public boolean save(IRestServiceArtifact digitalAsset) {
        BasicValidator.validateNotNull("digitalAsset", digitalAsset);
        BasicValidator.validateNotNull("guid", digitalAsset.getGUID());
        return this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, digitalAsset.getGUID().getStringValue(), DocumentBuilder.createDocument(digitalAsset));
    }

    @Override
    public boolean remove(GUID guid) {
        return LuceneIndexUtil.remove(getLuceneIndexDAO(), guid);
    }

    @Override
    public IRestServiceArtifact get(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        IRestServiceArtifact result = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(LuceneRestServiceArtifactIndex.KEY_GUID, guid.getStringValue());
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            result = LuceneRestServiceArtifactIndex.createRestServiceArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    @Override
    public Set<IRestServiceArtifact> find() {
        return LuceneRestServiceArtifactIndex.createRestServiceArtifactSet((this.getLuceneIndexDAO().findDocuments(null)));
    }

    @Override
    public Set<IRestServiceArtifact> find(String fieldKey, String fieldValue) {
        BasicValidator.validateNotNull("fieldKey", fieldKey);
        BasicValidator.validateNotNull("fieldValue", fieldValue);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(fieldKey, fieldValue);
        return this.find(fields);
    }

    @Override
    public Set<IRestServiceArtifact> find(Map<String, String> fields) {
        BasicValidator.validateNotNull("fields", fields);
        Set<IRestServiceArtifact> results = null;
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        if (documents != null) {
            results = LuceneRestServiceArtifactIndex.createRestServiceArtifactSet(documents);
        }
        return results;
    }

    @Override
    public int getIndexSize() {
        return this.getLuceneIndexDAO().findDocuments(null).size();
    }


    private static Set<IRestServiceArtifact> createRestServiceArtifactSet(Set<Document> documents) {
        Set<IRestServiceArtifact> restServiceArtifactSet = new HashSet<>();
        if (documents != null) {
            for (Document document : documents) {

                Map<String, String> attributes = new HashMap<String, String>();
                // Create a MAP for the fields that are not present in the PresentationServicePageArtifact as CONSTANT field
                for (IndexableField field : document.getFields()) {
                    if (notKey(field.name())) {
                        attributes.put(field.name(), document.get(field.name()));
                    }
                }

                restServiceArtifactSet.add(
                        new RestServiceArtifact(
                                GUIDFactory.passGUID(document.get(KEY_GUID)),
                                document.get(RestServiceArtifact.archiveName),
                                document.get(RestServiceArtifact.contextRoot),
                                document.get(RestServiceArtifact.httpMethod),
                                document.get(RestServiceArtifact.restServicePath),
                                document.get(RestServiceArtifact.restOperationPath),
                                document.get(RestServiceArtifact.restOperationMethod),
                                document.get(RestServiceArtifact.restArtifactPath),
                                document.get(RestServiceArtifact.restRolesAllowed),
                                attributes,
                                null));
            }
        }
        return restServiceArtifactSet;
    }

    public static boolean notKey(String name) {
        boolean isKey = true;

        switch (name) {
            case RestServiceArtifact.archiveName:
                isKey = false;
            case RestServiceArtifact.contextRoot:
                isKey = false;
            case RestServiceArtifact.httpMethod:
                isKey = false;
            case RestServiceArtifact.restOperationMethod:
                isKey = false;
            case RestServiceArtifact.restOperationPath:
                isKey = false;
            case RestServiceArtifact.restServicePath:
                isKey = false;
        }

        return isKey;
    }
}
