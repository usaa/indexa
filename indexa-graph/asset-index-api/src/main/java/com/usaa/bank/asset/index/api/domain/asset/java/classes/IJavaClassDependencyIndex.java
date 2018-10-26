package com.usaa.bank.asset.index.api.domain.asset.java.classes;

import java.util.Set;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.IStatefulIndexerSession;

/**
 * This interface provides the basic capabilities of an java-class dependency's lucene-indexes.
 */
public interface IJavaClassDependencyIndex extends IStatefulIndexerSession {
    //"dependents" are children of a "dependency"

    /**
     * Creates a new Dependency and adds it to the lucene-index at the end. A new GUID is generated for ClassLoader using the dependentClassGUID, dependencyPackage and dependencyClassName.<br>
     * A field is created, it is a Map with dependentClassGUID, dependencyPackage, dependencyClassName as its entries with its corresponding predefined key. Field and GUID are stored in the lucene-document.<br>
     *
     * @param dependentClassGUID  - Guid of dependent-class
     * @param dependencyPackage   - Package name of dependency class
     * @param dependencyClassName - name of dependency class
     */
    void addDependency(GUID dependentClassGUID, HierarchicalPackage dependencyPackage, String dependencyClassName);

    /**
     * Removes a Dependency from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao. A field is created, it is a Map with dependentClassGUID, dependencyPackage, dependencyClassName as its entries with its corresponding predefined key.<br>
     * Using the field this function will delete the entry of the Dependency found. Only the first found Dependency will be deleted. <br>
     *
     * @param dependentClassGUID  - Guid of dependent-class
     * @param dependencyPackage   - Package name of dependency class
     * @param dependencyClassName - name of dependency class
     */
    void removeDependency(GUID dependentClassGUID, HierarchicalPackage dependencyPackage, String dependencyClassName);

    /**
     * Removes a Dependency from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao. A field is created, it is a Map with dependentClassGUID, dependencyPackage, dependencyClassName as its entries with its corresponding predefined key.<br>
     * Using the field this function will delete the entry of the Dependency found. All the Dependencies found will be deleted. <br>
     *
     * @param dependencyPackage   - Package name of dependency class
     * @param dependencyClassName - name of dependency class
     */
    void removeDependencies(HierarchicalPackage dependencyPackage, String dependencyClassName);

    /**
     * Get all the dependents of a JavaClass's lucene-index. It matches the indexes of dependents having user specified dependencyPackage and dependencyClassName. <br>
     * Dependents of a class are classes that rely upon our class to compile.
     *
     * @param dependencyPackage   - Package name of dependency class
     * @param dependencyClassName - name of dependency class
     * @return -Set of dependents
     */
    Set<GUID> findDependents(HierarchicalPackage dependencyPackage, String dependencyClassName);

    /**
     * Gathers all the dependencies from the JavaClass's lucene-index matching the guid of dependent class.
     *
     * @param dependentClassGUID - Guid of dependent-class
     * @return -Set of dependencies
     */
    Set<JavaClassDependency> findDependencies(GUID dependentClassGUID);

    /**
     * Gathers all the dependencies from the JavaClass's lucene-index
     *
     * @return - Set of dependencies of a JavaClass
     */
    Set<JavaClassDependency> findDependencies();

    /**
     * Gathers all the dependencies from the JavaClass's lucene-index. User specified offset position to start gathering the data and also the numberOfResults from the offset.
     *
     * @param offset          - Starting position to gather data from
     * @param numberOfResults - Number of results requested.
     * @return - Set of dependencies of a JavaClass
     */
    Set<JavaClassDependency> findDependencies(int offset, int numberOfResults);
}