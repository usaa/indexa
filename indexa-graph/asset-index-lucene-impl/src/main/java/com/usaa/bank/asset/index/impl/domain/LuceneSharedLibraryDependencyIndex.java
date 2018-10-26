package com.usaa.bank.asset.index.impl.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.shared.library.ISharedLibraryDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.shared.library.SharedLibraryDependency;
import org.apache.lucene.document.Document;

import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * This class is an implementation to modify the lucene-indexes associated with the shared-library dependency.<br>
 * This implements the interface ISharedLibraryDependencyIndex which outlines the specs.
 */
public class LuceneSharedLibraryDependencyIndex extends RealtimeLuceneIndex implements ISharedLibraryDependencyIndex {

    public static final String DEFAULT_INDEX_FILE_PATH = ".sharedlibrary.dependency.index";
    private static final String KEY_GUID = "guid";
    private static final String KEY_SHARED_LIBRARY_GUID = "sharedLibraryGUID";
    private static final String KEY_ARTIFACT_PATH = "artifactPath";

    public LuceneSharedLibraryDependencyIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }

    @Override
    public boolean addDependency(GUID sharedLibraryGUID, String artifactPath) {

        BasicValidator.validateNotNull("sharedLibraryGUID", sharedLibraryGUID);
        BasicValidator.validateNotNull("artifactPath", artifactPath);
        GUID guid = SharedLibraryDependency.createGUID(sharedLibraryGUID, artifactPath);
        Map<String, Set<String>> fields = new HashMap<>();
        fields.put(KEY_SHARED_LIBRARY_GUID, IndexUtil.createSingleValueSet(sharedLibraryGUID.getStringValue()));
        fields.put(KEY_ARTIFACT_PATH, IndexUtil.createSingleValueSet(artifactPath));
        return this.getLuceneIndexDAO().saveDocument(KEY_GUID, guid.getStringValue(), DocumentBuilder.createDocument(guid.getStringValue(), fields));

    }

    @Override
    public void removeDependency(GUID sharedLibraryGUID, String artifactPath) {

        BasicValidator.validateNotNull("sharedLibraryGUID", sharedLibraryGUID);
        BasicValidator.validateNotNull("artifactPath", artifactPath);
        GUID guid = SharedLibraryDependency.createGUID(sharedLibraryGUID, artifactPath);
        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_GUID, guid.getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public void removeDependencies(GUID sharedLibraryGUID) {

        BasicValidator.validateNotNull("sharedLibraryGUID", sharedLibraryGUID);
        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_SHARED_LIBRARY_GUID, sharedLibraryGUID.getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);

    }

    @Override
    public Set<GUID> findLibraries(String artifactPath) {
        BasicValidator.validateNotNull("artifactPath", artifactPath);
        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_ARTIFACT_PATH, artifactPath);
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        Set<GUID> libraries = buildGUIDSet(documents, KEY_SHARED_LIBRARY_GUID);
        return libraries;
    }

    @Override
    public Set<String> findArtifactPaths(GUID sharedLibraryGUID) {

        BasicValidator.validateNotNull("sharedLibraryGUID", sharedLibraryGUID);
        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_SHARED_LIBRARY_GUID, sharedLibraryGUID.getStringValue());
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        Set<String> paths = buildStringSet(documents, KEY_ARTIFACT_PATH);
        return paths;
    }

    @Override
    public Set<SharedLibraryDependency> findDependencies() {
        return LuceneSharedLibraryDependencyIndex.createSharedLibraryDependencySet(this.getLuceneIndexDAO().findDocuments(null));
    }

    @Override
    public Set<SharedLibraryDependency> findDependencies(GUID sharedLibraryGUID) {

        BasicValidator.validateNotNull("sharedLibraryGUID", sharedLibraryGUID);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_SHARED_LIBRARY_GUID, sharedLibraryGUID.getStringValue());
        return LuceneSharedLibraryDependencyIndex.createSharedLibraryDependencySet(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<SharedLibraryDependency> findDependencies(String artifactPath) {

        BasicValidator.validateNotNull("artifactPath", artifactPath);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_ARTIFACT_PATH, artifactPath);
        return LuceneSharedLibraryDependencyIndex.createSharedLibraryDependencySet(this.getLuceneIndexDAO().findDocuments(fields));
    }


    // ===========================================================
    // UTILITY METHODS
    // ===========================================================

    /**
     * Grabs the set of GUID for the documents. FieldName is used to create a GUID.
     *
     * @param documents - Lucene-documents
     * @param fieldname - Field of an lucene-index
     * @return
     */
    private Set<GUID> buildGUIDSet(Set<Document> documents, String fieldname) {
        Set<GUID> results = new HashSet<>();
        if (documents != null) {
            for (Document document : documents) {
                results.add(GUIDFactory.passGUID(document.get(fieldname)));
            }
        }
        return results;
    }

    /**
     * Grabs all the data linked with the 'fieldname' from all of the lucene-indexes. The collected field is added to a collection.
     *
     * @param documents - Lucene documents
     * @param fieldname - Field of an lucene-index
     * @return - Collection of field-value
     */
    private Set<String> buildStringSet(Set<Document> documents, String fieldname) {
        Set<String> results = new HashSet<>();
        if (documents != null) {
            for (Document document : documents) {
                results.add(document.get(fieldname));
            }
        }
        return results;
    }

    /**
     * Generates a collection of all dependencies associated with shared-library(s).
     *
     * @param documents - Lucene Documents
     * @return - Collection of dependencies
     */
    private static Set<SharedLibraryDependency> createSharedLibraryDependencySet(Set<Document> documents) {
        Set<SharedLibraryDependency> sharedLibraryDependencySet = new HashSet<SharedLibraryDependency>();
        if (documents != null) {
            for (Document document : documents) {
                sharedLibraryDependencySet.add(
                        new SharedLibraryDependency(
                                GUIDFactory.passGUID(document.get(KEY_GUID)),
                                GUIDFactory.passGUID(document.get(KEY_SHARED_LIBRARY_GUID)),
                                document.get(KEY_ARTIFACT_PATH)));
            }
        }
        return sharedLibraryDependencySet;
    }
}
