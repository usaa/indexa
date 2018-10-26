package com.usaa.bank.asset.index.impl.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.dependency.DependencyScope;
import com.usaa.bank.asset.index.api.domain.asset.dependency.IDependencyIndex;
import org.apache.lucene.document.Document;

import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * This class is an implementation to modify the lucene-indexes associated with the dependency.<br>
 * This implements the interface IDependencyIndex which outlines the specs.
 */
public class LuceneDependencyIndex extends RealtimeLuceneIndex implements IDependencyIndex {

    public static final String DEFAULT_INDEX_FILE_PATH = ".dependency.index";
    private static final String DEPENDENCY = "dependencyGUID";
    private static final String DEPENDENT = "dependentGUID";
    private static final String DEPENDENCYSCOPE = "dependencyScope";
    private static final String SHAREDLIBRARY = "sharedLibraryGUID";
    private static final String GUID = "guid";
    private static final String DEPTH = "depth";
    private static final String DELIMITER = ":";
    private static final int MAX_RESULTS = 1000;

    public LuceneDependencyIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }


    @Override
    public boolean addDependency(GUID dependent, GUID dependency, DependencyScope dependencyScope, GUID sharedLibrary) {

        BasicValidator.validateNotNull(DEPENDENT, dependent);
        BasicValidator.validateNotNull(DEPENDENCY, dependency);
        BasicValidator.validateNotNull(DEPENDENCYSCOPE, dependencyScope);

        StringBuffer sb = new StringBuffer();

        sb.append(dependent.getStringValue()).append(DELIMITER).append(dependency.getStringValue()).append(DELIMITER).append(dependencyScope.name());

        Map<String, Set<String>> fields = new HashMap<String, Set<String>>();
        fields.put(DEPENDENT, IndexUtil.createSingleValueSet(dependent.getStringValue()));
        fields.put(DEPENDENCY, IndexUtil.createSingleValueSet(dependency.getStringValue()));
        fields.put(DEPENDENCYSCOPE, IndexUtil.createSingleValueSet(dependencyScope.name()));
        if (sharedLibrary != null) {
            sb.append(sharedLibrary.getStringValue());
            fields.put(SHAREDLIBRARY, IndexUtil.createSingleValueSet(sharedLibrary.getStringValue()));
        }
        GUID guid = GUIDFactory.createGUID(sb.toString());
        return this.getLuceneIndexDAO().saveDocument(GUID, guid.getStringValue(), DocumentBuilder.createDocument(guid.getStringValue(), fields));

    }

    @Override
    public void removeDependency(GUID dependent, GUID dependency, DependencyScope dependencyScope, GUID sharedLibrary) {

        BasicValidator.validateNotNull(DEPENDENT, dependent);
        BasicValidator.validateNotNull(DEPENDENCY, dependency);
        BasicValidator.validateNotNull(DEPENDENCYSCOPE, dependencyScope);

        Map<String, String> fields = new HashMap<>();
        fields.put(DEPENDENT, dependent.getStringValue());
        fields.put(DEPENDENCY, dependency.getStringValue());
        fields.put(DEPENDENCYSCOPE, dependencyScope.name());
        if (sharedLibrary != null) {
            fields.put(SHAREDLIBRARY, sharedLibrary.getStringValue());
        }
        this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public void removeDependencies(GUID dependent, DependencyScope dependencyScope, GUID sharedLibrary) {

        BasicValidator.validateNotNull(DEPENDENT, dependent);
        BasicValidator.validateNotNull(DEPENDENCYSCOPE, dependencyScope);
        removeFromIndex(DEPENDENT, dependent, dependencyScope, sharedLibrary);
    }

    @Override
    public void removeDependents(GUID dependency, DependencyScope dependencyScope, GUID sharedLibrary) {

        BasicValidator.validateNotNull(DEPENDENCY, dependency);
        BasicValidator.validateNotNull(DEPENDENCYSCOPE, dependencyScope);
        removeFromIndex(DEPENDENCY, dependency, dependencyScope, sharedLibrary);
    }

    @Override
    public Set<GUID> findDependencies(GUID dependent, DependencyScope dependencyScope, GUID sharedLibrary) {

        BasicValidator.validateNotNull(DEPENDENT, dependent);
        Set<Document> documents = findGUIDs(DEPENDENT, dependent, dependencyScope, sharedLibrary);
        return buildGUIDSet(documents, DEPENDENCY);
    }

    @Override
    public Set<GUID> findDependents(GUID dependency, DependencyScope dependencyScope, GUID sharedLibrary) {

        BasicValidator.validateNotNull(DEPENDENCY, dependency);
        Set<Document> documents = findGUIDs(DEPENDENCY, dependency, dependencyScope, sharedLibrary);
        return buildGUIDSet(documents, DEPENDENT);
    }


    /***********************************************
     A DEPTH OF 0 RETURNS THE ENTIRE GRAPH
     ************************************************/
    /**
     * Removes the documents that matches the indexes with type of guid used, dependency scope and shared-library guid.
     *
     * @param type            - Type of guid
     * @param guid            - Guid of dependency
     * @param dependencyScope - Scope of dependency
     * @param sharedLibrary   - Guid of shared-library
     */
    private void removeFromIndex(String type, GUID guid, DependencyScope dependencyScope, GUID sharedLibrary) {
        Map<String, String> fields = new HashMap<>();
        if (sharedLibrary != null) {
            fields.put(SHAREDLIBRARY, sharedLibrary.getStringValue());
        }
        fields.put(DEPENDENCYSCOPE, dependencyScope.name());
        fields.put(type, guid.getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    /**
     * Finds all the GUID from its lucene-index using the type of guid used, dependency scope and shared-library guid.
     *
     * @param type            - Type of guid
     * @param guid            - Guid of dependency
     * @param dependencyScope - Scope of dependency
     * @param sharedLibrary   - Guid of shared-library
     * @return - Set of lucene-documents
     */
    private Set<Document> findGUIDs(String type, GUID guid, DependencyScope dependencyScope, GUID sharedLibrary) {
        Map<String, String> fields = new HashMap<>();

        fields.put(type, guid.getStringValue());

        if (dependencyScope != null) {
            fields.put(DEPENDENCYSCOPE, dependencyScope.name());
        }
        if (sharedLibrary != null) {
            fields.put(SHAREDLIBRARY, sharedLibrary.getStringValue());
        }
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        return documents;
    }


    /**
     * Builds a Set of GUID fromt he given documents. The documents added to the set will need to have the fieldname provided.
     *
     * @param documents - Set of lucene documents
     * @param fieldname - Field name to match
     * @return - Set of GUID that match
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

}
