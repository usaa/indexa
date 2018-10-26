package com.usaa.bank.asset.index.api.domain.asset.shared.library;

import com.usaa.bank.asset.index.api.domain.asset.IComputeAsset;

/**
 * Implementing this interface allows the implemented class to be considered as an variation of ISharedLibrary.
 */
public interface ISharedLibrary extends IComputeAsset {
    /**
     * Gets id of the shared-library
     *
     * @return -Id
     */
    String getId();

    /**
     * Gets name of the shared-library
     *
     * @return - Name
     */
    String getName();
}