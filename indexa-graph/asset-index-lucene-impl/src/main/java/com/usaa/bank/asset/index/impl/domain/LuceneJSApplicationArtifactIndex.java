package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;

import com.usaa.bank.asset.index.api.domain.ui.component.js.IJSApplicationArtifact;
import com.usaa.bank.asset.index.api.domain.ui.component.js.IJSApplicationArtifactIndex;
import com.usaa.bank.asset.index.api.domain.ui.component.js.JSApplicationArtifact;
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
public class LuceneJSApplicationArtifactIndex extends RealtimeLuceneIndex implements IJSApplicationArtifactIndex {

    private static final String KEY_GUID = "guid";
    private static final String[] keys = {};

    /**
     * Creates a LuceneJSApplicationArtifactIndex instance with
     *
     * @param luceneIndexDAO lucene-index-dao which has the lucene-indexer in it.
     */
    public LuceneJSApplicationArtifactIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }

    @Override
    public IJSApplicationArtifact create(String appId, String appName, String appDescription, String appRev, String appAuthor, String version, Map<String, String> fields) {
        BasicValidator.validateNotNull("js application ID", appId);
        BasicValidator.validateNotNull("presentationServiceContextRoot", appName);

        // create a new artifact model , this will be used to write to the lucene document location
        IJSApplicationArtifact jsApplicationArtifact = new JSApplicationArtifact(appId, appName, appDescription, appRev, appAuthor, version, fields);

        if (this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, jsApplicationArtifact.getGUID().getStringValue(), DocumentBuilder.createDocument(jsApplicationArtifact))) {
            return jsApplicationArtifact;
        }
        return null;
    }

    @Override
    public boolean remove(String jsAppId, String version) {
        BasicValidator.validateNotNull("jsAppId", jsAppId);
        BasicValidator.validateNotNull("JS_APP_VERSION", version);

        Map<String, String> fields = new HashMap<>();

        fields.put(JSApplicationArtifact.JS_APP_ID, jsAppId);
        fields.put(JSApplicationArtifact.JS_APP_VERSION, version);

        this.getLuceneIndexDAO().deleteDocuments(fields);
        return true;
    }

    @Override
    public IJSApplicationArtifact get(String jsAppId, String version) {
        BasicValidator.validateNotNull("jsAppId", jsAppId);
        BasicValidator.validateNotNull("JS_APP_VERSION", version);
        Map<String, String> fields = new HashMap<>();
        IJSApplicationArtifact result = null;

        fields.put(JSApplicationArtifact.JS_APP_ID, jsAppId);
        fields.put(JSApplicationArtifact.JS_APP_VERSION, version);

        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);

        if (!resultList.isEmpty()) {
            result = LuceneJSApplicationArtifactIndex.createPresentationServiceApplicationArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    @Override
    public Set<IJSApplicationArtifact> find(String jsAppId) {
        BasicValidator.validateNotNull("jsAppId", jsAppId);

        Set<IJSApplicationArtifact> results = null;

        Map<String, String> fields = new HashMap<>();

        fields.put(JSApplicationArtifact.JS_APP_ID, jsAppId);

        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);

        if (!resultList.isEmpty()) {
            results = LuceneJSApplicationArtifactIndex.createPresentationServiceApplicationArtifactSet(resultList);
        }

        return results;
    }

    @Override
    public IJSApplicationArtifact create(Set<String> tags, Map<String, Set<String>> fields) {
        BasicValidator.validateNotNull("fields", fields);
        try {
            Set<Document> docs = null;

            Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);

            String guid = GUIDFactory.createGUID(DigitalAsset.getFirstValueString(KEY_GUID, fields)).toString();

            docs.add(DocumentBuilder.createDocument(guid, tags, fields));

            this.getLuceneIndexDAO().saveDocument(KEY_GUID, guid, docs.iterator().next());

            return LuceneJSApplicationArtifactIndex.createPresentationServiceApplicationArtifactSet(docs).iterator().next();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public boolean save(IJSApplicationArtifact digitalAsset) {
        BasicValidator.validateNotNull("digitalAsset", digitalAsset);
        BasicValidator.validateNotNull("guid", digitalAsset.getGUID());
        return this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, digitalAsset.getGUID().getStringValue(), DocumentBuilder.createDocument(digitalAsset));
    }

    @Override
    public boolean remove(GUID guid) {
        return LuceneIndexUtil.remove(getLuceneIndexDAO(),guid);
    }

    @Override
    public IJSApplicationArtifact get(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        IJSApplicationArtifact result = null;

        Map<String, String> fields = new HashMap<>();

        fields.put(LuceneJSApplicationArtifactIndex.KEY_GUID, guid.getStringValue());

        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);

        if (!resultList.isEmpty()) {
            result = LuceneJSApplicationArtifactIndex.createPresentationServiceApplicationArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    @Override
    public Set<IJSApplicationArtifact> find() {
        return LuceneJSApplicationArtifactIndex.createPresentationServiceApplicationArtifactSet((this.getLuceneIndexDAO().findDocuments(null)));

    }

    @Override
    public Set<IJSApplicationArtifact> find(String fieldKey, String fieldValue) {
        BasicValidator.validateNotNull("fieldValue", fieldValue);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(fieldKey, fieldValue);
        return this.find(fields);
    }

    @Override
    public Set<IJSApplicationArtifact> find(Map<String, String> fields) {
        BasicValidator.validateNotNull("fields", fields);
        Set<IJSApplicationArtifact> results = null;
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        if (documents != null) {
            results = LuceneJSApplicationArtifactIndex.createPresentationServiceApplicationArtifactSet(documents);
        }
        return results;
    }

    @Override
    public int getIndexSize() {
        return this.getLuceneIndexDAO().findDocuments(null).size();
    }

    private static Set<IJSApplicationArtifact> createPresentationServiceApplicationArtifactSet(Set<Document> documents) {
        Set<IJSApplicationArtifact> presentationServicesArtifactSet = new HashSet<>();

        if (documents != null) {
            for (Document document : documents) {

                Map<String, String> attributes = new HashMap<String, String>();
                for (IndexableField field : document.getFields()) {
                    if (notKey(field.name())) {
                        attributes.put(field.name(), document.get(field.name()));
                    }
                }
                presentationServicesArtifactSet.add(
                        new JSApplicationArtifact(
                                GUIDFactory.passGUID(document.get(KEY_GUID)),
                                document.get(JSApplicationArtifact.JS_APP_ID),
                                document.get(JSApplicationArtifact.JS_APP_NAME),
                                document.get(JSApplicationArtifact.JS_APP_DESCRIPTION),
                                document.get(JSApplicationArtifact.JS_APP_REV),
                                document.get(JSApplicationArtifact.JS_APP_AUTHOR),
                                document.get(JSApplicationArtifact.JS_APP_VERSION),
                                attributes
                        ));
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
            case JSApplicationArtifact.JS_APP_ID:
                isKey = false;
            case JSApplicationArtifact.JS_APP_NAME:
                isKey = false;
            case JSApplicationArtifact.JS_APP_DESCRIPTION:
                isKey = false;
            case JSApplicationArtifact.JS_APP_REV:
                isKey = false;
            case JSApplicationArtifact.JS_APP_AUTHOR:
                isKey = false;
            case JSApplicationArtifact.JS_APP_VERSION:
                isKey = false;
        }

        return isKey;
    }
}
