package com.usaa.bank.asset.index.impl.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.java.classes.HierarchicalPackage;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClass;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.JavaClass;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.lucene.document.Document;

import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.asset.index.impl.util.JavaClassBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * This class is an implementation to modify the lucene-indexes associated with the java-class.<br>
 * This implements the interface IJavaClassIndex which outlines the specs.
 */
public class LuceneJavaClassIndex extends RealtimeLuceneIndex implements IJavaClassIndex {
    public static final String DEFAULT_INDEX_FILE_PATH = ".class.index";
    private static final String[] keys = {JavaClass.KEY_GUID, JavaClass.KEY_ARTIFACT_HASH, JavaClass.KEY_PACKAGE, JavaClass.KEY_CLASS_NAME,
            JavaClass.KEY_CREATED_DATE, JavaClass.KEY_MODIFIED_DATE, JavaClass.KEY_MAJOR_VERSION, JavaClass.KEY_MINOR_VERSION};

    /**
     * Creates a LuceneJavaClassIndex instance from the given lucene-indexer. Lucene indexer comprise of lucene documents for this JavaClass index.
     *
     * @param luceneIndexDAO Lucene indexer corresponding to the JavaClass Index
     */
    public LuceneJavaClassIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }

    @Override
    public IJavaClass create(Set<String> tags, Map<String, Set<String>> fields) {

        BasicValidator.validateNotNull("fields", fields);
        Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);
        String guid = this.createGuid(fields);
        Document doc = DocumentBuilder.createDocument(guid, tags, fields);
        this.getLuceneIndexDAO().saveDocument(JavaClass.KEY_GUID, guid, doc);
        return JavaClassBuilder.createJavaClass(doc);
    }

    @Override
    public boolean save(IJavaClass javaClass) {

        BasicValidator.validateNotNull("JavaClass", javaClass);
        String guid = this.createGuid(javaClass.getArtifactHash(), javaClass.getPackage(), javaClass.getClassName());
        return this.getLuceneIndexDAO().saveDocument(
                JavaClass.KEY_GUID, guid,
                this.createJavaClassDoc(
                        guid, javaClass.getArtifactHash(), javaClass.getPackage(), javaClass.getClassName(), javaClass.getCreatedDate(),
                        javaClass.getModifiedDate(), javaClass.getMajorVersion(), javaClass.getMinorVersion()
                )
        );
    }

    @Override
    public boolean remove(GUID guid) {

        BasicValidator.validateNotNull("guid", guid);
        Map<String, String> fields = new HashMap<>();
        try {
            fields.put(JavaClass.KEY_GUID, guid.getStringValue());
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public IJavaClass get(GUID guid) {

        BasicValidator.validateNotNull("guid", guid);
        IJavaClass result = null;
        Map<String, String> fields = new HashMap<>();
        try {
            fields.put(JavaClass.KEY_GUID, guid.getStringValue());
            Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
            if (!resultList.isEmpty()) {
                result = JavaClassBuilder.createJavaClass(new ArrayList<>(resultList).get(0));
            }
        } catch (IndexOutOfBoundsException e) {
            return result;
        }
        return result;
    }

    @Override
    public Set<IJavaClass> find(String fieldKey, String fieldValue) {
        BasicValidator.validateNotNull("key", fieldKey);
        BasicValidator.validateNotNull("value", fieldValue);
        Map<String, String> map = new HashMap<>();
        map.put(fieldKey, fieldValue);
        return this.find(map);
    }

    @Override
    public Set<IJavaClass> find(Map<String, String> fields) {

        BasicValidator.validateNotNull("fields", fields);
        Map<String, String> cleanFields = new HashMap<>();
        for (String key : keys) {
            if (fields.containsKey(key)) {
                String value = fields.get(key);
                if (value != null) {
                    cleanFields.put(key, value);
                }
            }
        }
        return JavaClassBuilder.createJavaClasses(this.getLuceneIndexDAO().findDocuments(cleanFields));
    }

    @Override
    public IJavaClass create(String artifactHash, HierarchicalPackage hierarchicalPackage, String className,
                             long createdDate, long modifiedDate, int majorVersion, int minorVersion) {

        BasicValidator.validateNotNull("hash", artifactHash);
        BasicValidator.validateNotNull("package", hierarchicalPackage);
        BasicValidator.validateNotNull("className", className);
        BasicValidator.validateNotNull("createdDate", createdDate);
        BasicValidator.validateNotNull("modifiedDate", modifiedDate);
        BasicValidator.validateNotNull("majorVersion", majorVersion);
        BasicValidator.validateNotNull("miniorVersion", minorVersion);
        String guid = this.createGuid(artifactHash, hierarchicalPackage, className);
        Document doc = this.createJavaClassDoc(guid, artifactHash, hierarchicalPackage, className,
                createdDate, modifiedDate, majorVersion, minorVersion);
        this.getLuceneIndexDAO().saveDocument(JavaClass.KEY_GUID, guid, doc);
        return JavaClassBuilder.createJavaClass(doc);
    }

    @Override
    public boolean remove(String artifactHash, HierarchicalPackage hierarchicalPackage, String className) {

        BasicValidator.validateNotNull("hash", artifactHash);
        BasicValidator.validateNotNull("package", hierarchicalPackage);
        BasicValidator.validateNotNull("className", className);
        Map<String, String> fields = new HashMap<>();
        fields.put(JavaClass.KEY_ARTIFACT_HASH, artifactHash);
        fields.put(JavaClass.KEY_PACKAGE, hierarchicalPackage.toString());
        fields.put(JavaClass.KEY_CLASS_NAME, className);
        return this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public IJavaClass get(String artifactHash, HierarchicalPackage hierarchicalPackage, String className) {

        BasicValidator.validateNotNull("hash", artifactHash);
        BasicValidator.validateNotNull("package", hierarchicalPackage);
        BasicValidator.validateNotNull("className", className);
        IJavaClass result = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(JavaClass.KEY_ARTIFACT_HASH, artifactHash);
        fields.put(JavaClass.KEY_PACKAGE, hierarchicalPackage.toString());
        fields.put(JavaClass.KEY_CLASS_NAME, className);
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            result = JavaClassBuilder.createJavaClass(new ArrayList<>(resultList).get(0));
        }
        return result;
    }

    @Override
    public Set<IJavaClass> find(String artifactHash) {

        BasicValidator.validateNotNull("hash", artifactHash);
        Map<String, String> fields = new HashMap<>();
        fields.put(JavaClass.KEY_ARTIFACT_HASH, artifactHash);
        return JavaClassBuilder.createJavaClasses(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<IJavaClass> find(HierarchicalPackage hierarchicalPackage) {

        BasicValidator.validateNotNull("package", hierarchicalPackage);
        Map<String, String> fields = new HashMap<>();
        fields.put(JavaClass.KEY_PACKAGE, hierarchicalPackage.toString());
        return JavaClassBuilder.createJavaClasses(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<IJavaClass> find(HierarchicalPackage hierarchicalPackage, String className) {

        BasicValidator.validateNotNull("package", hierarchicalPackage);
        BasicValidator.validateNotNull("className", className);
        Map<String, String> fields = new HashMap<>();
        fields.put(JavaClass.KEY_PACKAGE, hierarchicalPackage.toString());
        fields.put(JavaClass.KEY_CLASS_NAME, className);
        return JavaClassBuilder.createJavaClasses(this.getLuceneIndexDAO().findDocuments(fields));
    }

    /**
     * Creates a GUID using the given fields. <br>
     *
     * @param fields - Map
     * @return - Guid created
     */
    private String createGuid(Map<String, Set<String>> fields) {
        BasicValidator.validateNotNull("fields", fields);
        try {
            String artifactHash = new ArrayList<>(fields.get(JavaClass.KEY_ARTIFACT_HASH)).get(0);
            String hierarchicalPackage = new ArrayList<>(fields.get(JavaClass.KEY_PACKAGE)).get(0);
            String className = new ArrayList<>(fields.get(JavaClass.KEY_CLASS_NAME)).get(0);
            StringBuilder sb = new StringBuilder();
            sb.append(artifactHash).append(JavaClass.GUID_DELIMITER).append(hierarchicalPackage.toString())
                    .append(JavaClass.GUID_DELIMITER).append(className);
            String guid = GUIDFactory.createGUID(sb.toString()).toString();
            return guid;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Create a lucene-document from the given JavaClass.
     *
     * @param guid                - Guid of javaclass
     * @param artifactHash        - Hash of artifact the JavaClass is present in
     * @param hierarchicalPackage - Package name of JavaClass
     * @param className           - name of the class
     * @param createdDate         - created date of class
     * @param modifiedDate        - last date modified of class
     * @param majorVersion        - Major gav of class
     * @param minorVersion        - Minor gav of class
     * @return - A lucene-document containing  metadata from JavaClass object sent.
     */
    private Document createJavaClassDoc(String guid, String artifactHash, HierarchicalPackage hierarchicalPackage,
                                        String className, long createdDate, long modifiedDate, int majorVersion, int minorVersion) {
        Map<String, Set<String>> fields = new HashMap<>();
        Set<String> value = new HashSet<>();
        value.add(artifactHash);
        fields.put(JavaClass.KEY_ARTIFACT_HASH, value);
        value = new HashSet<>();
        value.add(hierarchicalPackage.toString());
        fields.put(JavaClass.KEY_PACKAGE, value);
        value = new HashSet<>();
        value.add(className);
        fields.put(JavaClass.KEY_CLASS_NAME, value);
        value = new HashSet<>();
        value.add(String.valueOf(createdDate));
        fields.put(JavaClass.KEY_CREATED_DATE, value);
        value = new HashSet<>();
        value.add(String.valueOf(modifiedDate));
        fields.put(JavaClass.KEY_MODIFIED_DATE, value);
        value = new HashSet<>();
        value.add(String.valueOf(majorVersion));
        fields.put(JavaClass.KEY_MAJOR_VERSION, value);
        value = new HashSet<>();
        value.add(String.valueOf(minorVersion));
        fields.put(JavaClass.KEY_MINOR_VERSION, value);
        return DocumentBuilder.createDocument(guid, null, fields);
    }

    /**
     * Creates a GUID using the given artifactHash, package-name and className. <br>
     * A field is created, it is a Map with artifactHash, hierarchicalPackage and className as its entry with its corresponding predefined key.
     *
     * @param artifactHash
     * @param hierarchicalPackage
     * @param className
     * @return
     */
    private String createGuid(String artifactHash, HierarchicalPackage hierarchicalPackage, String className) {
        Map<String, Set<String>> fields = new HashMap<>();
        Set<String> value = new HashSet<>();
        value.add(artifactHash);
        fields.put(JavaClass.KEY_ARTIFACT_HASH, value);
        value = new HashSet<>();
        value.add(hierarchicalPackage.toString());
        fields.put(JavaClass.KEY_PACKAGE, value);
        value = new HashSet<>();
        value.add(className);
        fields.put(JavaClass.KEY_CLASS_NAME, value);
        return this.createGuid(fields);
    }

    @Override
    public Set<IJavaClass> find() {
        return JavaClassBuilder.createJavaClasses(this.getLuceneIndexDAO().findDocuments(null));
    }

    @Override
    public int getIndexSize() {
        return this.getLuceneIndexDAO().getDocumentCount();
    }

}