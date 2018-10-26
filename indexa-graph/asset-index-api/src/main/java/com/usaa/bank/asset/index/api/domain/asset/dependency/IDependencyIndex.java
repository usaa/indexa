package com.usaa.bank.asset.index.api.domain.asset.dependency;

import java.util.Set;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.IStatefulIndexerSession;

/**
 * This interface provides the basic capabilities of an dependency's lucene-indexes.
 */
public interface IDependencyIndex extends IStatefulIndexerSession {
    //"dependents" are children of a "dependency"

    /**
     * Adds a dependency to the lucene-index. Lucene-indexes are associated with its luceneIndexerDao.
     * A new GUID is generated for ClassLoader using the dependent-guid and dependency-guid. <br>
     * A field is created, it is a Map with dependent, dependency, dependencyScope, sharedLibrary as its entry with its corresponding predefined key. Field and GUID are stored in the lucene-document.<br>
     *
     * @param dependent       - Guid of dependent
     * @param dependency      - Guid of dependency
     * @param dependencyScope - Scope of the dependency
     * @param sharedLibrary   - Guid of Shared library
     * @return - True if addition was a success
     */
    boolean addDependency(GUID dependent, GUID dependency, DependencyScope dependencyScope, GUID sharedLibrary);

    /**
     * Removes a dependency from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao.
     * A field is created, it is a Map with dependent, dependency, dependencyScope, sharedLibrary as its entries with its corresponding predefined key. <br>
     * Using the field this function will delete the entry of the dependency found. Only the first found dependency will be deleted. <br>
     *
     * @param dependent       - Guid of dependent
     * @param dependency      - Guid of dependency
     * @param dependencyScope - Scope of the dependency
     * @param sharedLibrary   - Guid of Shared library
     */
    void removeDependency(GUID dependent, GUID dependency, DependencyScope dependencyScope, GUID sharedLibrary);

    /**
     * Removes a dependency from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao.
     * A field is created, it is a Map with dependent, dependencyScope, sharedLibrary as its entries with its corresponding predefined key. <br>
     * Using the field this function will delete the entry of the dependency found. All the found dependency will be deleted. <br>
     *
     * @param dependent       - Guid of dependent
     * @param dependencyScope - Scope of the dependency
     * @param sharedLibrary   - Guid of Shared library
     */
    void removeDependencies(GUID dependent, DependencyScope dependencyScope, GUID sharedLibrary);

    /**
     * Removes a dependent from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao.
     * A field is created, it is a Map with dependency, dependencyScope, sharedLibrary as its entries with its corresponding predefined key. <br>
     * Using the field this function will delete the entry of the dependency found. All the found dependent will be deleted. <br>
     *
     * @param dependency      - Guid of dependency
     * @param dependencyScope - Scope of the dependency
     * @param sharedLibrary   - Guid of Shared library
     */
    void removeDependents(GUID dependency, DependencyScope dependencyScope, GUID sharedLibrary);

    /**
     * Find all the dependencies for artifacts from the given dependent, dependencyScope, sharedLibrary.
     *
     * @param dependent       - Guid of dependent
     * @param dependencyScope - Scope of the dependency
     * @param sharedLibrary   - Guid of Shared library
     * @return - Set of dependencies
     */
    Set<GUID> findDependencies(GUID dependent, DependencyScope dependencyScope, GUID sharedLibrary);

    /**
     * Find all the dependents for artifacts from the given dependent, dependencyScope, sharedLibrary.
     *
     * @param dependency      - Guid of dependency
     * @param dependencyScope - Scope of the dependency
     * @param sharedLibrary   - Guid of Shared library
     * @return - Set of dependents
     */
    Set<GUID> findDependents(GUID dependency, DependencyScope dependencyScope, GUID sharedLibrary);

   /* *//**
     * Get a dependency graph for the given GUID of dependency.
     *
     * @param guid - GUID of dependency
     * @return - Dependency graph
     *//*
    DependencyGraph getDependencyGraph(GUID guid);*/

 /*   *//**
     * Get a dependency graph for the given GUID of dependency and the depth of the graph from root.
     *
     * @param guid  - GUID of dependency
     * @param depth - Depth of the graph
     * @return - Dependency graph
     *//*
    DependencyGraph getDependencyGraph(GUID guid, int depth);*/
}