package com.usaa.bank.asset.index.impl.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.java.classes.HierarchicalPackage;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.JavaClassDependency;
import org.apache.lucene.document.Document;

import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * This class is an implementation to modify the lucene-indexes associated with the java-class dependency.<br>
 * This implements the interface IJavaClassDependencyIndex which outlines the specs.
 *
 */
public class LuceneJavaClassDependencyIndex extends RealtimeLuceneIndex implements IJavaClassDependencyIndex {
    public static final String DEFAULT_INDEX_FILE_PATH = ".class.dependency.index";
    private static final String GUID_DELIMITER = ":";
    private static final String KEY_GUID = "guid";
    private static final String KEY_DEPENDENT_CLASS_GUID = "dependentClassGUID";
    private static final String KEY_DEPENDENCY_PACKAGE = "dependencyPackage";
    private static final String KEY_DEPENDENCY_CLASS_NAME = "dependencyClassName";

    public LuceneJavaClassDependencyIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }

    @Override
    public void addDependency(GUID dependentClassGUID, HierarchicalPackage dependencyPackage, String dependencyClassName) {
        BasicValidator.validateNotNull("dependentClassGUID", dependentClassGUID);
        BasicValidator.validateNotNull("dependencyPackage", dependencyPackage);
        BasicValidator.validateNotNull("dependencyClassName", dependencyClassName);
        Map<String, Set<String>> fields = new HashMap<String, Set<String>>();
        fields.put(KEY_DEPENDENT_CLASS_GUID, IndexUtil.createSingleValueSet(dependentClassGUID.getStringValue()));
        fields.put(KEY_DEPENDENCY_PACKAGE, IndexUtil.createSingleValueSet(dependencyPackage.toString()));
        fields.put(KEY_DEPENDENCY_CLASS_NAME, IndexUtil.createSingleValueSet(dependencyClassName));
        GUID guid = LuceneJavaClassDependencyIndex.createGUID(dependentClassGUID, dependencyPackage, dependencyClassName);
        this.getLuceneIndexDAO().saveDocument(KEY_GUID, guid.getStringValue(), DocumentBuilder.createDocument(guid.getStringValue(), fields));

    }

    @Override
    public void removeDependency(GUID dependentClassGUID, HierarchicalPackage dependencyPackage, String dependencyClassName) {
        BasicValidator.validateNotNull("dependentClassGUID", dependentClassGUID);
        BasicValidator.validateNotNull("dependencyPackage", dependencyPackage);
        BasicValidator.validateNotNull("dependencyClassName", dependencyClassName);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_GUID, LuceneJavaClassDependencyIndex.createGUID(dependentClassGUID, dependencyPackage, dependencyClassName).getStringValue());
        this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public void removeDependencies(HierarchicalPackage dependencyPackage, String dependencyClassName) {
        BasicValidator.validateNotNull("dependencyPackage", dependencyPackage);
        BasicValidator.validateNotNull("dependencyClassName", dependencyClassName);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_DEPENDENCY_PACKAGE, dependencyPackage.toString());
        fields.put(KEY_DEPENDENCY_CLASS_NAME, dependencyClassName);
        this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public Set<GUID> findDependents(HierarchicalPackage dependencyPackage, String dependencyClassName) {
        BasicValidator.validateNotNull("dependencyPackage", dependencyPackage);
        BasicValidator.validateNotNull("dependencyClassName", dependencyClassName);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_DEPENDENCY_PACKAGE, dependencyPackage.toString());
        fields.put(KEY_DEPENDENCY_CLASS_NAME, dependencyClassName);
        return LuceneJavaClassDependencyIndex.createGUIDSet(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<JavaClassDependency> findDependencies(int offset, int numberOfResults) {
        BasicValidator.validateNotNegative("offset", offset);
        BasicValidator.validateNotNegative("numberOfResults", numberOfResults);
        Map<String, String> fields = new HashMap<String, String>();
        return LuceneJavaClassDependencyIndex.createJavaClassDependencySet(this.getLuceneIndexDAO().findDocuments(fields, offset, numberOfResults));
    }

    @Override
    public Set<JavaClassDependency> findDependencies(GUID dependentClassGUID) {
        BasicValidator.validateNotNull("dependentClassGUID", dependentClassGUID);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(KEY_DEPENDENT_CLASS_GUID, dependentClassGUID.getStringValue());
        return LuceneJavaClassDependencyIndex.createJavaClassDependencySet(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<JavaClassDependency> findDependencies() {
        return LuceneJavaClassDependencyIndex.createJavaClassDependencySet(this.getLuceneIndexDAO().findDocuments(null));
    }

    // ==========================================================
    // UTILITY METHODS
    // ==========================================================
    public static GUID createGUID(GUID dependentClassGUID, HierarchicalPackage dependencyPackage, String dependencyClassName) {
        BasicValidator.validateNotNull("dependentClassGUID", dependentClassGUID);
        BasicValidator.validateNotNull("dependencyPackage", dependencyPackage);
        BasicValidator.validateNotNull("dependencyClassName", dependencyClassName);
        StringBuffer sb = new StringBuffer();
        return GUIDFactory.createGUID(sb.append(dependentClassGUID.getStringValue()).append(GUID_DELIMITER).append(dependencyPackage.toString()).append(GUID_DELIMITER).append(dependencyClassName).toString());
    }

    private static Set<GUID> createGUIDSet(Set<Document> documents) {
        Set<GUID> guidSet = new HashSet<GUID>();
        if (documents != null) {
            for (Document document : documents) {
                guidSet.add(GUIDFactory.passGUID(document.get(LuceneJavaClassDependencyIndex.KEY_DEPENDENT_CLASS_GUID)));
            }
        }
        return guidSet;
    }

    private static Set<JavaClassDependency> createJavaClassDependencySet(Set<Document> documents) {
        Set<JavaClassDependency> dependencyClassSet = new HashSet<JavaClassDependency>();
        if (documents != null) {
            for (Document document : documents) {
                dependencyClassSet.add(
                        new JavaClassDependency(
                                GUIDFactory.passGUID(document.get(KEY_GUID)),
                                GUIDFactory.passGUID(document.get(KEY_DEPENDENT_CLASS_GUID)),
                                new HierarchicalPackage(document.get(KEY_DEPENDENCY_PACKAGE)),
                                document.get(KEY_DEPENDENCY_CLASS_NAME)));
            }
        }
        return dependencyClassSet;
    }

}