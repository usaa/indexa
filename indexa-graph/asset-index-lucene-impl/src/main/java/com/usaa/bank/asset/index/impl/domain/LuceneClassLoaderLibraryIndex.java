package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.classloader.ClassLoaderLibrary;
import com.usaa.bank.asset.index.api.domain.asset.classloader.IClassLoaderLibraryIndex;
import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import com.usaa.bank.graph.common.validate.BasicValidator;
import org.apache.lucene.document.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * * This class is an implementation to modify the lucene-indexes associated with the class-loader-library.<br>
 * This implements the interface IClassLoaderLibraryIndex which outlines the specs.
 */
public class LuceneClassLoaderLibraryIndex extends RealtimeLuceneIndex implements IClassLoaderLibraryIndex {
    public static final String DEFAULT_INDEX_FILE_PATH = ".deployment.config.classloader.index";
    private static final String KEY_GUID = "guid";
    private static final String KEY_CLASSLOADER_GUID = "classLoaderGUID";
    private static final String KEY_NAME = "sharedLibraryName";

    /**
     * Creates a LuceneClassLoaderLibraryIndex instance from the given lucene-index. Lucene-indexer will contain the lucene indexes present in a directory.
     *
     * @param luceneIndexDAO - lucene-index-dao which has the lucene-indexer in it.
     */
    public LuceneClassLoaderLibraryIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }


    @Override
    public boolean addLibrary(GUID classLoaderGUID, String sharedLibraryName) {
        BasicValidator.validateNotNull("classLoaderGUID", classLoaderGUID);
        BasicValidator.validateNotNull("sharedLibraryName", sharedLibraryName);
        GUID guid = ClassLoaderLibrary.createGUID(classLoaderGUID, sharedLibraryName);
        Map<String, Set<String>> fields = new HashMap<>();
        fields.put(KEY_CLASSLOADER_GUID, IndexUtil.createSingleValueSet(classLoaderGUID.getStringValue()));
        fields.put(KEY_NAME, IndexUtil.createSingleValueSet(sharedLibraryName));
        return this.getLuceneIndexDAO().saveDocument(KEY_GUID, guid.getStringValue(), DocumentBuilder.createDocument(guid.getStringValue(), fields));

    }

    @Override
    public void removeLibrary(GUID classLoaderGUID, String sharedLibraryName) {

        BasicValidator.validateNotNull("classLoaderGUID", classLoaderGUID);
        BasicValidator.validateNotNull("sharedLibraryName", sharedLibraryName);
        GUID guid = ClassLoaderLibrary.createGUID(classLoaderGUID, sharedLibraryName);
        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_GUID, guid.getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public void removeLibraries(GUID classLoaderGUID) {

        BasicValidator.validateNotNull("classLoaderGUID", classLoaderGUID);
        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_CLASSLOADER_GUID, classLoaderGUID.getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);

    }

    @Override
    public Set<ClassLoaderLibrary> findLibraries() {

        Map<String, String> fields = new HashMap<String, String>();
        return LuceneClassLoaderLibraryIndex.createClassLoaderLibrarySet(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<ClassLoaderLibrary> findLibraries(GUID classLoaderGUID) {

        BasicValidator.validateNotNull("classLoaderGUID", classLoaderGUID);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_CLASSLOADER_GUID, classLoaderGUID.getStringValue());
        return LuceneClassLoaderLibraryIndex.createClassLoaderLibrarySet(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<GUID> findClassLoaders(String sharedLibraryName) {
        BasicValidator.validateNotNull("sharedLibraryName", sharedLibraryName);
        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_NAME, sharedLibraryName);
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        Set<GUID> classLoaders = buildGUIDSet(documents, KEY_CLASSLOADER_GUID);
        return classLoaders;
    }

    @Override
    public Set<ClassLoaderLibrary> findLibraries(String sharedLibraryName) {

        BasicValidator.validateNotNull("sharedLibraryName", sharedLibraryName);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_CLASSLOADER_GUID, sharedLibraryName);
        return LuceneClassLoaderLibraryIndex.createClassLoaderLibrarySet(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public ClassLoaderLibrary getLibrary(GUID classLoaderGUID, String sharedLibraryName) {
        BasicValidator.validateNotNull("classLoaderGUID", classLoaderGUID);
        BasicValidator.validateNotNull("sharedLibraryName", sharedLibraryName);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_CLASSLOADER_GUID, classLoaderGUID.getStringValue());
        fields.put(KEY_NAME, sharedLibraryName);
        return LuceneClassLoaderLibraryIndex.createClassLoaderLibrarySet(this.getLuceneIndexDAO().findDocuments(fields)).iterator().next();
    }
    // ===========================================================
    // UTILITY METHODSF
    // ===========================================================

    private Set<GUID> buildGUIDSet(Set<Document> documents, String fieldname) {
        Set<GUID> results = new HashSet<>();
        if (documents != null) {
            for (Document document : documents) {
                results.add(GUIDFactory.passGUID(document.get(fieldname)));
            }
        }
        return results;
    }

    private Set<String> buildStringSet(Set<Document> documents, String fieldname) {
        Set<String> results = new HashSet<>();
        if (documents != null) {
            for (Document document : documents) {
                results.add(document.get(fieldname));
            }
        }
        return results;
    }

    private static Set<ClassLoaderLibrary> createClassLoaderLibrarySet(Set<Document> documents) {
        Set<ClassLoaderLibrary> classLoaderClassLoaderLibrarySet = new HashSet<ClassLoaderLibrary>();
        if (documents != null) {
            for (Document document : documents) {
                classLoaderClassLoaderLibrarySet.add(
                        new ClassLoaderLibrary(
                                GUIDFactory.passGUID(document.get(KEY_GUID)),
                                GUIDFactory.passGUID(document.get(KEY_CLASSLOADER_GUID)),
                                document.get(KEY_NAME)));
            }
        }
        return classLoaderClassLoaderLibrarySet;
    }
}
