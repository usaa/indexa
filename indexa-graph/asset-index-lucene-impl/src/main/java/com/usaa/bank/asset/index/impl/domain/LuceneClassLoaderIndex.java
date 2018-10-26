package com.usaa.bank.asset.index.impl.domain;


import com.usaa.bank.asset.index.api.domain.asset.classloader.IClassLoaderIndex;
import com.usaa.bank.asset.index.api.domain.asset.classloader.ClassLoader;
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
 * This class is an implementation to modify the lucene-indexes associated with the class-loader.<br>
 * This implements the interface IClassLoaderIndex which outlines the specs.
 */
public class LuceneClassLoaderIndex extends RealtimeLuceneIndex implements IClassLoaderIndex {
    public static final String DEFAULT_INDEX_FILE_PATH = ".deployment.config.classloader.index";
    private static final String KEY_GUID = "guid";
    private static final String KEY_DEPLOYMENT_CONFIG_GUID = "deploymentConfigGUID";
    private static final String KEY_NAME = "id";

    /**
     * Creates a LuceneClassLoaderIndex instance from the given lucene-index. Lucene-indexer will contain the lucene indexes present in a directory.
     *
     * @param luceneIndexDAO - lucene-index-dao which has the lucene-indexer in it.
     */
    public LuceneClassLoaderIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }


    @Override
    public boolean addClassLoader(GUID deploymentConfigGUID, String id) {

        BasicValidator.validateNotNull("deploymentConfigGUID", deploymentConfigGUID);
        BasicValidator.validateNotNull("id", id);
        GUID guid = ClassLoader.createGUID(deploymentConfigGUID, id);
        Map<String, Set<String>> fields = new HashMap<>();
        fields.put(KEY_DEPLOYMENT_CONFIG_GUID, IndexUtil.createSingleValueSet(deploymentConfigGUID.getStringValue()));
        fields.put(KEY_NAME, IndexUtil.createSingleValueSet(id));
        return this.getLuceneIndexDAO().saveDocument(KEY_GUID, guid.getStringValue(), DocumentBuilder.createDocument(guid.getStringValue(), fields));

    }

    @Override
    public void removeClassLoader(GUID deploymentConfigGUID, String id) {

        BasicValidator.validateNotNull("deploymentConfigGUID", deploymentConfigGUID);
        BasicValidator.validateNotNull("id", id);
        GUID guid = ClassLoader.createGUID(deploymentConfigGUID, id);
        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_GUID, guid.getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public void removeClassLoaders(GUID deploymentConfigGUID) {

        BasicValidator.validateNotNull("deploymentConfigGUID", deploymentConfigGUID);
        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_DEPLOYMENT_CONFIG_GUID, deploymentConfigGUID.getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);

    }

    @Override
    public Set<ClassLoader> findClassLoaders() {

        Map<String, String> fields = new HashMap<String, String>();
        return LuceneClassLoaderIndex.createClassLoaderSet(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<ClassLoader> findClassLoaders(GUID deploymentConfigGUID) {

        BasicValidator.validateNotNull("deploymentConfigGUID", deploymentConfigGUID);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_DEPLOYMENT_CONFIG_GUID, deploymentConfigGUID.getStringValue());
        return LuceneClassLoaderIndex.createClassLoaderSet(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<GUID> findDeploymentConfigs(String id) {
        BasicValidator.validateNotNull("classloaderName", id);
        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_NAME,id);
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        Set<GUID> deploymentConfigs = buildGUIDSet(documents, KEY_NAME);
        return deploymentConfigs;
    }

    @Override
    public ClassLoader getClassLoader(GUID deploymentConfigGUID, String id) {
        BasicValidator.validateNotNull("deploymentConfigGUID", deploymentConfigGUID);
        BasicValidator.validateNotNull("id", id);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_DEPLOYMENT_CONFIG_GUID, deploymentConfigGUID.getStringValue());
        fields.put(KEY_NAME, id);
        return LuceneClassLoaderIndex.createClassLoaderSet(this.getLuceneIndexDAO().findDocuments(fields)).iterator().next();
    }

    @Override
    public Set<ClassLoader> findClassLoaders(String id) {

        BasicValidator.validateNotNull("id", id);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_NAME, id);
        return LuceneClassLoaderIndex.createClassLoaderSet(this.getLuceneIndexDAO().findDocuments(fields));
    }


    // ===========================================================
    // UTILITY METHODS
    // ===========================================================

    private Set<GUID> buildGUIDSet(Set<Document> documents, String fieldid) {
        Set<GUID> results = new HashSet<>();
        if (documents != null) {
            for (Document document : documents) {
                results.add(GUIDFactory.passGUID(document.get(fieldid)));
            }
        }
        return results;
    }

    private Set<String> buildStringSet(Set<Document> documents, String fieldid) {
        Set<String> results = new HashSet<>();
        if (documents != null) {
            for (Document document : documents) {
                results.add(document.get(fieldid));
            }
        }
        return results;
    }

    private static Set<ClassLoader> createClassLoaderSet(Set<Document> documents) {
        Set<ClassLoader> deploymentConfigClassLoaderSet = new HashSet<ClassLoader>();
        if (documents != null) {
            for (Document document : documents) {
                deploymentConfigClassLoaderSet.add(
                        new ClassLoader(
                                GUIDFactory.passGUID(document.get(KEY_GUID)),
                                GUIDFactory.passGUID(document.get(KEY_DEPLOYMENT_CONFIG_GUID)),
                                document.get(KEY_NAME)));
            }
        }
        return deploymentConfigClassLoaderSet;
    }
}
