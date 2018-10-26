package com.usaa.bank.asset.index.impl.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import com.usaa.bank.asset.index.api.domain.asset.classpath.IManifestClasspathIndex;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;

import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * This class is an implementation to modify the lucene-indexes associated with the artifactory-data.<br>
 * This implements the interface IArtifactoryDataIndex which outlines the specs.
 */
public class LuceneManifestClasspathIndex extends RealtimeLuceneIndex implements IManifestClasspathIndex {
    public static final String DEFAULT_INDEX_FILE_PATH = ".classpath.classpath.index";
    private static final String GUID_DELIMITER = ":";
    private static final String KEY_GUID = "guid";
    private static final String KEY_ARTIFACT_GUID = "artifactGUID";
    private static final String KEY_CLASSPATH_ENTRY = "classpathEntry";

    public LuceneManifestClasspathIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }

    @Override
    public boolean addClasspathEntry(GUID artifactGUID, String classpathEntry) {
        BasicValidator.validateNotNull("artifactGUID", artifactGUID);
        BasicValidator.validateNotNull("classpathEntry", classpathEntry);
        Map<String, Set<String>> fields = new HashMap<String, Set<String>>();
        fields.put(KEY_ARTIFACT_GUID, IndexUtil.createSingleValueSet(artifactGUID.getStringValue()));
        fields.put(KEY_CLASSPATH_ENTRY, IndexUtil.createSingleValueSet(classpathEntry));
        GUID guid = LuceneManifestClasspathIndex.createGUID(artifactGUID, classpathEntry);
        return this.getLuceneIndexDAO().saveDocument(KEY_GUID, guid.getStringValue(), DocumentBuilder.createDocument(guid.getStringValue(), fields));
    }

    @Override
    public void removeClasspathEntry(GUID dependentArtifactGUID, String classpathEntry) {
        BasicValidator.validateNotNull("dependentArtifactGUID", dependentArtifactGUID);
        BasicValidator.validateNotNull("classpathEntry", classpathEntry);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_GUID, LuceneManifestClasspathIndex.createGUID(dependentArtifactGUID, classpathEntry).getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public void removeClasspathEntries(String classpathEntry) {

        BasicValidator.validateNotNull("classpathEntry", classpathEntry);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_CLASSPATH_ENTRY, classpathEntry.toString());
        this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public Set<String> findClasspathEntries(GUID artifactGUID) {

        BasicValidator.validateNotNull("artifactGUID", artifactGUID);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_ARTIFACT_GUID, artifactGUID.getStringValue());
        return LuceneManifestClasspathIndex.createClasspathEntrySet(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public void removeArtifact(GUID dependentArtifactGUID) {

        BasicValidator.validateNotNull("dependentArtifactGUID", dependentArtifactGUID);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_ARTIFACT_GUID, dependentArtifactGUID.getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public Set<GUID> findArtifacts(String classpathEntry) {
        BasicValidator.validateNotNull("classpathEntry", classpathEntry);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_CLASSPATH_ENTRY, classpathEntry);
        return LuceneManifestClasspathIndex.createGUIDSet(this.getLuceneIndexDAO().findDocuments(fields));
    }


    // ===============================================================
    // UTILITY METHODS
    // ===============================================================

    /**
     * Creates a GUID using the given artifactGUID and classpathEntry.
     *
     * @param artifactGUID   - Guid of artifactory where the classPathEntry is present
     * @param classpathEntry - Class path entry name
     * @return - GUID created
     */
    private static GUID createGUID(GUID artifactGUID, String classpathEntry) {
        if (artifactGUID != null && StringUtils.isNotEmpty(classpathEntry)) {
            return GUIDFactory.createGUID(artifactGUID.getStringValue() + GUID_DELIMITER + classpathEntry);
        }
        return null;
    }

    /**
     * Gets the GUID associated with each lucene-documents.
     *
     * @param documents - Lucene-documents
     * @return - Set of GUID created for documents
     */
    private static Set<GUID> createGUIDSet(Set<Document> documents) {
        Set<GUID> guidSet = new HashSet<GUID>();
        if (documents != null) {
            for (Document document : documents) {
                guidSet.add(GUIDFactory.passGUID(document.get(LuceneManifestClasspathIndex.KEY_ARTIFACT_GUID)));
            }
        }
        return guidSet;
    }

    /**
     * Gets the class-path-entry associated with each lucene-documents.
     *
     * @param documents
     * @return
     */
    private static Set<String> createClasspathEntrySet(Set<Document> documents) {
        Set<String> guidSet = new HashSet<String>();
        if (documents != null) {
            for (Document document : documents) {
                guidSet.add(document.get(LuceneManifestClasspathIndex.KEY_CLASSPATH_ENTRY));
            }
        }
        return guidSet;
    }

}