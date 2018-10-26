package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.ui.component.ps.app.IPresentationServicePageArtifactIndex;
import com.usaa.bank.asset.index.api.domain.ui.component.ps.app.IPresentationServicesPageArtifact;
import com.usaa.bank.asset.index.api.domain.ui.component.ps.app.PresentationServicePageArtifact;
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

/**
 * This class is an implementation to modify the lucene-indexes associated with the ps-application of UI component.<br>
 * This implements the interface IPresentationServicePageArtifactIndex which outlines the specs.
 */
public class LucenePresentationServicePageArtifactIndex extends RealtimeLuceneIndex implements IPresentationServicePageArtifactIndex {

    private static final String KEY_GUID = "guid";
    private static final String[] keys = {};

    public LucenePresentationServicePageArtifactIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }

    @Override
    public IPresentationServicesPageArtifact create(String applicationName, String contextRoot, String pageId, String pageName, String pageHeading, String pageTitle, Map<String, String> nodeAttributes, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        BasicValidator.validateNotNull("PRESENTATION_SERVICE_APPLICATION_NAME", applicationName);
        BasicValidator.validateNotNull("presentationServiceContextRoot", contextRoot);

        IPresentationServicesPageArtifact presentationServicesArtifact = new PresentationServicePageArtifact(applicationName, contextRoot, pageId, pageName, pageHeading, pageTitle, nodeAttributes, mappingData, jvmComponentMapping);

        if (this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, presentationServicesArtifact.getGUID().getStringValue(), DocumentBuilder.createDocument(presentationServicesArtifact))) {
            return presentationServicesArtifact;
        }
        return null;
    }

    @Override
    public boolean remove(String applicationName, String contextRoot, String pageId, String pageName) {
        BasicValidator.validateNotNull("PRESENTATION_SERVICE_APPLICATION_NAME", applicationName);
        BasicValidator.validateNotNull("presentationServiceContextRoot", contextRoot);
        BasicValidator.validateNotNull("pageId", pageId);
        BasicValidator.validateNotNull("pageName", pageName);

        Map<String, String> fields = new HashMap<>();

        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_APPLICATION_NAME, applicationName);
        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_CONTEXT_ROOT, contextRoot);
        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_ID, pageId);
        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_NAME, pageName);

        this.getLuceneIndexDAO().deleteDocuments(fields);
        return true;
    }

    @Override
    public IPresentationServicesPageArtifact get(String applicationName, String contextRoot, String pageId, String pageName) {
        BasicValidator.validateNotNull("PRESENTATION_SERVICE_APPLICATION_NAME", applicationName);
        BasicValidator.validateNotNull("presentationServiceContextRoot", contextRoot);

        Map<String, String> fields = new HashMap<>();
        IPresentationServicesPageArtifact result = null;

        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_APPLICATION_NAME, applicationName);
        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_CONTEXT_ROOT, contextRoot);
        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_ID, pageId);
        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_NAME, pageName);

        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);

        if (!resultList.isEmpty()) {
            result = LucenePresentationServicePageArtifactIndex.createPresentationServiceApplicationArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    public IPresentationServicesPageArtifact get(String applicationName, String contextRoot) {
        BasicValidator.validateNotNull("PRESENTATION_SERVICE_APPLICATION_NAME", applicationName);
        BasicValidator.validateNotNull("presentationServiceContextRoot", contextRoot);

        Map<String, String> fields = new HashMap<>();
        IPresentationServicesPageArtifact result = null;

        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_APPLICATION_NAME, applicationName);
        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_CONTEXT_ROOT, contextRoot);

        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);

        if (!resultList.isEmpty()) {
            result = LucenePresentationServicePageArtifactIndex.createPresentationServiceApplicationArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    @Override
    public Set<IPresentationServicesPageArtifact> find(String applicationName) {
        BasicValidator.validateNotNull("applicationName", applicationName);

        Set<IPresentationServicesPageArtifact> results = null;

        Map<String, String> fields = new HashMap<>();

        fields.put(PresentationServicePageArtifact.PRESENTATION_SERVICE_APPLICATION_NAME, applicationName);

        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);

        if (!resultList.isEmpty()) {
            results = LucenePresentationServicePageArtifactIndex.createPresentationServiceApplicationArtifactSet(resultList);
        }

        return results;
    }

    @Override
    public IPresentationServicesPageArtifact create(Set<String> tags, Map<String, Set<String>> fields) {
        BasicValidator.validateNotNull("fields", fields);
        try {
            Set<Document> docs = null;

            Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);

            String guid = GUIDFactory.createGUID(DigitalAsset.getFirstValueString(KEY_GUID, fields)).toString();

            docs.add(DocumentBuilder.createDocument(guid, tags, fields));

            this.getLuceneIndexDAO().saveDocument(KEY_GUID, guid, docs.iterator().next());

            return LucenePresentationServicePageArtifactIndex.createPresentationServiceApplicationArtifactSet(docs).iterator().next();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public boolean save(IPresentationServicesPageArtifact digitalAsset) {
        BasicValidator.validateNotNull("digitalAsset", digitalAsset);
        BasicValidator.validateNotNull("guid", digitalAsset.getGUID());
        return this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, digitalAsset.getGUID().getStringValue(), DocumentBuilder.createDocument(digitalAsset));
    }

    @Override
    public boolean remove(GUID guid) {
        return LuceneIndexUtil.remove(getLuceneIndexDAO(), guid);
    }

    @Override
    public IPresentationServicesPageArtifact get(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        IPresentationServicesPageArtifact result = null;

        Map<String, String> fields = new HashMap<>();

        fields.put(LucenePresentationServicePageArtifactIndex.KEY_GUID, guid.getStringValue());

        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);

        if (!resultList.isEmpty()) {
            result = LucenePresentationServicePageArtifactIndex.createPresentationServiceApplicationArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    @Override
    public Set<IPresentationServicesPageArtifact> find() {
        return LucenePresentationServicePageArtifactIndex.createPresentationServiceApplicationArtifactSet((this.getLuceneIndexDAO().findDocuments(null)));

    }

    @Override
    public Set<IPresentationServicesPageArtifact> find(String fieldKey, String fieldValue) {
        BasicValidator.validateNotNull("fieldValue", fieldValue);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(fieldKey, fieldValue);
        return this.find(fields);
    }

    @Override
    public Set<IPresentationServicesPageArtifact> find(Map<String, String> fields) {
        BasicValidator.validateNotNull("fields", fields);
        Set<IPresentationServicesPageArtifact> results = null;
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        if (documents != null) {
            results = LucenePresentationServicePageArtifactIndex.createPresentationServiceApplicationArtifactSet(documents);
        }
        return results;
    }

    @Override
    public int getIndexSize() {
        return this.getLuceneIndexDAO().findDocuments(null).size();
    }

    private static Set<IPresentationServicesPageArtifact> createPresentationServiceApplicationArtifactSet(Set<Document> documents) {
        Set<IPresentationServicesPageArtifact> presentationServicesArtifactSet = new HashSet<>();

        if (documents != null) {
            for (Document document : documents) {

                Map<String, String> attributes = new HashMap<String, String>();
                for (IndexableField field : document.getFields()) {
                    if (notKey(field.name())) {
                        attributes.put(field.name(), document.get(field.name()));
                    }
                }
                presentationServicesArtifactSet.add(
                        new PresentationServicePageArtifact(
                                GUIDFactory.passGUID(document.get(KEY_GUID)),
                                document.get(PresentationServicePageArtifact.PRESENTATION_SERVICE_APPLICATION_NAME),
                                document.get(PresentationServicePageArtifact.PRESENTATION_SERVICE_CONTEXT_ROOT),
                                document.get(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_ID),
                                document.get(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_NAME),
                                document.get(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_HEADING),
                                document.get(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_TITLE),
                                attributes, null, null));
            }
        }
        return presentationServicesArtifactSet;
    }

    /**
     * Checks if the given name string is present in the constant key specified in the PresentationServicePageArtifact class.
     *
     * @param name String to be compared with name
     * @return True if not a key used for indexing purposes
     */
    public static boolean notKey(String name) {
        boolean isKey = true;

        switch (name) {
            case PresentationServicePageArtifact.PRESENTATION_SERVICE_APPLICATION_NAME:
                isKey = false;
            case PresentationServicePageArtifact.PRESENTATION_SERVICE_CONTEXT_ROOT:
                isKey = false;
            case PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_ID:
                isKey = false;
            case PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_NAME:
                isKey = false;
            case PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_HEADING:
                isKey = false;
            case PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_TITLE:
                isKey = false;
        }

        return isKey;
    }
}
