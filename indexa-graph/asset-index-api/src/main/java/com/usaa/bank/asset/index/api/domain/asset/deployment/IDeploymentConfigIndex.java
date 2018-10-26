package com.usaa.bank.asset.index.api.domain.asset.deployment;

import com.usaa.bank.asset.index.api.domain.asset.IAssetIndex;

import java.util.Set;

/**
 * This interface provides the basic capabilities of an deployment configuration's lucene-indexes.
 */
public interface IDeploymentConfigIndex extends IAssetIndex<IDeploymentConfig> {

    /**
     * Creates a new DeploymentConfig and adds it to the lucene-index at the end. A new GUID is generated for ClassLoader using the deploymentConfigGUID and name.<br>
     * A field is created, it is a Map with deploymentConfigGUID as its entry with its corresponding predefined key. Field and GUID are stored in the lucene-document.<br>
     *
     * @param id   - Id of the deployment-config
     * @param name - Name of the deployment-config
     * @return - new DeploymentConfig instance
     */
    IDeploymentConfig create(String id, String name);

    /**
     * Removes a DeploymentConfig from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao. A field is created, it is a Map with id, name as its entries with its corresponding predefined key.<br>
     * Using the field this function will delete the entry of the DeploymentConfig found. Only the first found DeploymentConfig will be deleted. <br>
     *
     * @param id   - Id of the deployment-config
     * @param name - Name of the deployment-config
     * @return - True if deleted successfully
     */
    boolean remove(String id, String name);

    /**
     * Get a DeploymentConfigs that matches with the given id and name. Returns only one of the matching DeploymentConfigs.
     *
     * @param id   - Id of the deployment-config
     * @param name - Name of the deployment-config
     * @return - A DeploymentConfig
     */
    IDeploymentConfig get(String id, String name);


    /**
     * Get all the DeploymentConfig that matches with the given name.
     *
     * @param name - Name of the deployment-config
     * @return - Set of DeploymentConfig
     */
    Set<IDeploymentConfig> find(String name);
}
