package com.usaa.bank.asset.index.api.domain.asset.classloader;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.IStatefulIndexerSession;

import java.util.Set;

/**
 * This interface provides the basic capabilities of an class-loader's lucene-indexes.
 */
public interface IClassLoaderIndex extends IStatefulIndexerSession {

    /**
     * Creates a new ClassLoader and adds it to the lucene-index at the end. A new GUID is generated for ClassLoader using the deploymentConfigGUID and name.<br>
     * A field is created, it is a Map with deploymentConfigGUID as its entry with its corresponding predefined key. Field and GUID are stored in the lucene-document.<br>
     *
     * @param deploymentConfigGUID - Guid of deployment-Config
     * @param name                 - Nname of class loader
     * @return - True if addition was a success
     */
    boolean addClassLoader(GUID deploymentConfigGUID, String name);

    /**
     * Removes a ClassLoader from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao. A field is created, it is a Map with deploymentConfigGUID, name as its entries with its corresponding predefined key.<br>
     * Using the field this function will delete the entry of the classloader found. Only the first found classloader will be deleted. <br>
     *
     * @param deploymentConfigGUID - Guid of deployment-Config
     * @param name                 - Name of class loader
     */
    void removeClassLoader(GUID deploymentConfigGUID, String name);

    /**
     * Removes a ClassLoader from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao. A field is created, it is a Map with deploymentConfigGUID, name as its entries with its corresponding predefined key.<br>
     * Using the field this function will delete the entry of the classloader found. All the found classloader will be deleted. <br>
     *
     * @param deploymentConfigGUID - Guid of deployment-Config
     */
    void removeClassLoaders(GUID deploymentConfigGUID);

    /**
     * Get all the class-loaders present with the class-loader-index.
     *
     * @return - Set of classLoaders
     */
    Set<ClassLoader> findClassLoaders();

    /**
     * Get all the class-loaders that matches with the given name.
     *
     * @param name - Name of the class-loader
     * @return - Set of classloaders
     */
    Set<ClassLoader> findClassLoaders(String name);

    /**
     * Get all the class-loaders that matches with the given deploymentConfigGUID.
     *
     * @param deploymentConfigGUID - Guid of deployment-Config
     * @return - Set of classloaders
     */
    Set<ClassLoader> findClassLoaders(GUID deploymentConfigGUID);

    /**
     * Find all the deployment configurations using the given name of classLoader.
     *
     * @param name - Name of class-loader
     * @return - Set of deployment-configs
     */
    Set<GUID> findDeploymentConfigs(String name);

    /**
     * Get a single class-loaders that matches with the given deploymentConfigGUID.
     *
     * @param DeploymentConfigGUID - Guid of deployment-Config
     * @param name                 - Name of the class-loader
     * @return - a ClassLoader matched
     */
    ClassLoader getClassLoader(GUID DeploymentConfigGUID, String name);


}
