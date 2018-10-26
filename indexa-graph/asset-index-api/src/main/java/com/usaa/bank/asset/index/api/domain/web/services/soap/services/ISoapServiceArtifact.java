package com.usaa.bank.asset.index.api.domain.web.services.soap.services;

import com.usaa.bank.asset.index.api.domain.asset.IComputeAsset;

/**
 * Implementing this interface allows the implemented class to be considered as an variation of IArtifact.
 */
public interface ISoapServiceArtifact extends IComputeAsset {

    /**
     * Get the archive Name
     *
     * @return archive Name
     */
    String getArchiveName();

    /**
     * Get the context Root
     *
     * @return context Root
     */
    String getContextRoot();

    /**
     * Get the Service Definition Type
     *
     * @return Service Definition Type
     */
    String getServiceDefinitionType();

    /**
     * Get the Service Name
     *
     * @return Service Name
     */
    String getServiceName();

    /**
     * Get the Interface Class
     *
     * @return Interface Class
     */
    String getInterfaceClass();

    /**
     * Get the Operation Name
     *
     * @return Operation Name
     */
    String getOperationName();

    /**
     * Get the Implementation Class
     *
     * @return Implementation Class
     */
    String getImplementationClass();

}