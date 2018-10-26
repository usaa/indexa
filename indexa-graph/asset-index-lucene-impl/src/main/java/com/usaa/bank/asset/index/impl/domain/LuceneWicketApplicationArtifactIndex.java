package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.ui.component.wicket.IWicketApplicationArtifact;
import com.usaa.bank.asset.index.api.domain.ui.component.wicket.IWicketApplicationArtifactIndex;
import com.usaa.bank.asset.index.api.domain.ui.component.wicket.WicketApplicationArtifact;
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
 * This class is an implementation to modify the lucene-indexes associated with the wicket-application of UI component.<br>
 * This implements the interface IWicketApplicationArtifactIndex which outlines the specs.
 */
public class LuceneWicketApplicationArtifactIndex extends RealtimeLuceneIndex implements IWicketApplicationArtifactIndex {

    private static final String KEY_GUID = "guid";
    private static final String[] keys = {WicketApplicationArtifact.wicketApplicationName, WicketApplicationArtifact.wicketInfrastructureClass, WicketApplicationArtifact.wicketDisplayName, WicketApplicationArtifact.description, WicketApplicationArtifact.wicketApplicationClass, WicketApplicationArtifact.wicketApplicationArchiveName};

    /**
     * Creates a instance of the indexer for WicketApplication-lucene-index. Takes in a lucene-indexer-DAO as its parameter which specifies the various operations available with the lucene-documents.
     *
     * @param luceneIndexDAO lucene-index-dao which has the lucene-indexer in it.
     */
    public LuceneWicketApplicationArtifactIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }


    @Override
    public IWicketApplicationArtifact create(String wicketApplicationName, String wicketInfrastructureClass, String wicketDisplayName, String description, String wicketApplicationClass, String wicketApplicationArchiveName, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        BasicValidator.validateNotNull("wicketApplicationName", wicketApplicationName);
        BasicValidator.validateNotNull("wicketApplicationClass", wicketApplicationClass);
        IWicketApplicationArtifact wicketApplicationArtifact = new WicketApplicationArtifact(wicketApplicationName, wicketInfrastructureClass, wicketDisplayName, description, wicketApplicationClass, wicketApplicationArchiveName, mappingData, jvmComponentMapping);
        if (this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, wicketApplicationArtifact.getGUID().getStringValue(), DocumentBuilder.createDocument(wicketApplicationArtifact))) {
            return wicketApplicationArtifact;
        }
        return null;
    }

    @Override
    public boolean remove(String wicketApplicationName, String wicketApplicationClass) {
        BasicValidator.validateNotNull("wicketApplicationName", wicketApplicationName);
        BasicValidator.validateNotNull("wicketApplicationClass", wicketApplicationClass);
        Map<String, String> fields = new HashMap<>();
        fields.put(WicketApplicationArtifact.wicketApplicationName, wicketApplicationName);
        fields.put(WicketApplicationArtifact.wicketApplicationClass, wicketApplicationClass);
        this.getLuceneIndexDAO().deleteDocuments(fields);
        return true;
    }

    @Override
    public IWicketApplicationArtifact get(String wicketApplicationName, String wicketApplicationClass) {
        BasicValidator.validateNotNull("wicketApplicationName", wicketApplicationName);
        BasicValidator.validateNotNull("wicketApplicationClass", wicketApplicationClass);
        Map<String, String> fields = new HashMap<>();
        IWicketApplicationArtifact result = null;
        fields.put(WicketApplicationArtifact.wicketApplicationName, wicketApplicationName);
        fields.put(WicketApplicationArtifact.wicketApplicationClass, wicketApplicationClass);
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            result = LuceneWicketApplicationArtifactIndex.createWicketApplicationArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    @Override
    public Set<IWicketApplicationArtifact> find(String wicketApplicationName) {
        BasicValidator.validateNotNull("wicketApplicationName", wicketApplicationName);
        Set<IWicketApplicationArtifact> results = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(WicketApplicationArtifact.wicketApplicationName, wicketApplicationName);
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            results = LuceneWicketApplicationArtifactIndex.createWicketApplicationArtifactSet(resultList);
        }
        return results;
    }

    @Override
    public IWicketApplicationArtifact create(Set<String> tags, Map<String, Set<String>> fields) {
        BasicValidator.validateNotNull("fields", fields);
        try {
            Set<Document> docs = null;
            Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);
            String guid = GUIDFactory.createGUID(DigitalAsset.getFirstValueString(WicketApplicationArtifact.KEY_GUID, fields)).toString();
            docs.add(DocumentBuilder.createDocument(guid, tags, fields));
            this.getLuceneIndexDAO().saveDocument(WicketApplicationArtifact.KEY_GUID, guid, docs.iterator().next());
            return LuceneWicketApplicationArtifactIndex.createWicketApplicationArtifactSet(docs).iterator().next();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public boolean save(IWicketApplicationArtifact digitalAsset) {
        BasicValidator.validateNotNull("digitalAsset", digitalAsset);
        BasicValidator.validateNotNull("guid", digitalAsset.getGUID());
        return this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, digitalAsset.getGUID().getStringValue(), DocumentBuilder.createDocument(digitalAsset));
    }

    @Override
    public boolean remove(GUID guid) {
        return LuceneIndexUtil.remove(getLuceneIndexDAO(),guid);
    }

    @Override
    public IWicketApplicationArtifact get(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        IWicketApplicationArtifact result = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(LuceneWicketApplicationArtifactIndex.KEY_GUID, guid.getStringValue());
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            result = LuceneWicketApplicationArtifactIndex.createWicketApplicationArtifactSet(resultList).iterator().next();
        }
        return result;
    }

    @Override
    public Set<IWicketApplicationArtifact> find() {
        return LuceneWicketApplicationArtifactIndex.createWicketApplicationArtifactSet((this.getLuceneIndexDAO().findDocuments(null)));
    }

    @Override
    public Set<IWicketApplicationArtifact> find(String fieldKey, String fieldValue) {
        BasicValidator.validateNotNull("fieldValue", fieldValue);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(fieldKey, fieldValue);
        return this.find(fields);
    }

    @Override
    public Set<IWicketApplicationArtifact> find(Map<String, String> fields) {
        BasicValidator.validateNotNull("fields", fields);
        Set<IWicketApplicationArtifact> results = null;
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        if (documents != null) {
            results = LuceneWicketApplicationArtifactIndex.createWicketApplicationArtifactSet(documents);
        }
        return results;

    }

    @Override
    public int getIndexSize() {
        return this.getLuceneIndexDAO().findDocuments(null).size();
    }


    private static Set<IWicketApplicationArtifact> createWicketApplicationArtifactSet(Set<Document> documents) {
        Set<IWicketApplicationArtifact> wicketApplicationArtifactSet = new HashSet<>();
        if (documents != null) {
            for (Document document : documents) {

                Map<String, String> attributes = new HashMap<String, String>();
                // Create a MAP for the fields that are not present in the PresentationServicePageArtifact as CONSTANT field
                for (IndexableField field : document.getFields()) {
                    if (notKey(field.name())) {
                        attributes.put(field.name(), document.get(field.name()));
                    }
                }

                wicketApplicationArtifactSet.add(
                        new WicketApplicationArtifact(
                                GUIDFactory.passGUID(document.get(KEY_GUID)),
                                document.get(WicketApplicationArtifact.wicketApplicationName),
                                document.get(WicketApplicationArtifact.wicketInfrastructureClass),
                                document.get(WicketApplicationArtifact.wicketDisplayName),
                                document.get(WicketApplicationArtifact.description),
                                document.get(WicketApplicationArtifact.wicketApplicationClass),
                                document.get(WicketApplicationArtifact.wicketApplicationArchiveName),
                                attributes,
                                null
                        ));
            }
        }
        return wicketApplicationArtifactSet;
    }

    public static boolean notKey(String name) {
        boolean isKey = true;

        switch (name) {
            case WicketApplicationArtifact.wicketApplicationName:
                isKey = false;
            case WicketApplicationArtifact.wicketInfrastructureClass:
                isKey = false;
            case WicketApplicationArtifact.wicketDisplayName:
                isKey = false;
            case WicketApplicationArtifact.wicketApplicationClass:
                isKey = false;
            case WicketApplicationArtifact.wicketApplicationArchiveName:
                isKey = false;
            case WicketApplicationArtifact.description:
                isKey = false;
        }

        return isKey;
    }
}
