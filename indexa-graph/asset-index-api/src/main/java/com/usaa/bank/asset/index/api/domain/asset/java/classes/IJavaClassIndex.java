package com.usaa.bank.asset.index.api.domain.asset.java.classes;

import com.usaa.bank.asset.index.api.domain.asset.IAssetIndex;

import java.util.Set;

/**
 * This interface provides the basic capabilities of an java-class's lucene-indexes.
 */
public interface IJavaClassIndex extends IAssetIndex<IJavaClass> {
    /**
     * Creates a JavaClass inherited instance. Creates a new GUID using the
     *
     * @param artifactHash        - Hash of artifact the JavaClass is present in
     * @param hierarchicalPackage - Package name of JavaClass
     * @param className           - name of the class
     * @param createdDate         - created date of class
     * @param modifiedDate        - last date modified of class
     * @param majorVersion        - Major gav of class
     * @param minorVersion        - Minor gav of class
     * @return - JavaClass instance
     */
    IJavaClass create(String artifactHash, HierarchicalPackage hierarchicalPackage, String className, long createdDate, long modifiedDate, int majorVersion, int minorVersion);

    /**
     * Removes a JavaClass from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao.
     * A field is created, it is a Map with artifactHash,hierarchicalPackage and className as its entries with its corresponding predefined key.<br>
     * Using the field this function will delete the entry of the JavaClass found. Only the first found JavaClass will be deleted.
     *
     * @param artifactHash        - Hash of artifact the JavaClass is present in
     * @param hierarchicalPackage - Package name of JavaClass
     * @param className           - name of the class
     * @return - True if deletion was a success
     */
    boolean remove(String artifactHash, HierarchicalPackage hierarchicalPackage, String className);

    /**
     * Searches for JavaClass from the lucene-indexes.
     * A field is just a Map with artifactHash,hierarchicalPackage and className as its entries with its corresponding predefined key. <br>
     * Using the "field" search is performed on the lucene-index. Out of the matching documents only the first one will be reconstructed to an JavaClass object.
     *
     * @param artifactHash        - Hash of artifact the JavaClass is present in
     * @param hierarchicalPackage - Package name of JavaClass
     * @param className           - name of the class
     * @return - A JavaClass object
     */
    IJavaClass get(String artifactHash, HierarchicalPackage hierarchicalPackage, String className);

    /**
     * Searches for JavaClass from the lucene-indexes.
     * A field is just a Map with artifactHash as its entry with its corresponding predefined key. <br>
     * Using the "field" search is performed on the lucene-index. All of the matching documents will be parsed.
     *
     * @param artifactHash - Hash of artifact the JavaClass is present in
     * @return - Set of matching JavaClass
     */
    Set<IJavaClass> find(String artifactHash);

    /**
     * Searches for JavaClass from the lucene-indexes.
     * A field is just a Map with hierarchicalPackage as its entry with its corresponding predefined key. <br>
     * Using the "field" search is performed on the lucene-index. All of the matching documents will be parsed.
     *
     * @param hierarchicalPackage - Package name of JavaClass
     * @return - Set of matching JavaClass
     */
    Set<IJavaClass> find(HierarchicalPackage hierarchicalPackage);

    /**
     * Searches for JavaClass from the lucene-indexes.
     * A field is just a Map with hierarchicalPackage and className as its entries with its corresponding predefined key. <br>
     * Using the "field" search is performed on the lucene-index. All of the matching documents will be parsed.
     *
     * @param hierarchicalPackage - Package name of JavaClass
     * @param className           - name of the class
     * @return - Set of matching JavaClass
     */
    Set<IJavaClass> find(HierarchicalPackage hierarchicalPackage, String className);
}