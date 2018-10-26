package com.usaa.bank.asset.index.api.domain.asset.deployment;

import com.usaa.bank.asset.index.api.domain.asset.IComputeAsset;

/**
 * Implementing this interface allows the implemented class to be considered as an variation of IDeploymentConfig.
 */
public interface IDeploymentConfig extends IComputeAsset {
    /**
     * Gets id of the deployment-config
     *
     * @return - Id
     */
    String getId();

    /**
     * Gets name of the deployment-config
     *
     * @return - Name
     */
    String getName();
}
