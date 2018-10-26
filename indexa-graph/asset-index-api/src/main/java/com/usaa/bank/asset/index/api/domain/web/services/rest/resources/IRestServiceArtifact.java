package com.usaa.bank.asset.index.api.domain.web.services.rest.resources;

import com.usaa.bank.asset.index.api.domain.asset.IComputeAsset;

/**
 * Implementing this interface allows the implemented class to be considered as an variation of IArtifact.
 */
public interface IRestServiceArtifact extends IComputeAsset {

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
     * Get the http Method
     *
     * @return http Method
     */
    String gethttpMethod();

    /**
     * Get the Rest Service Path
     *
     * @return Rest Service Path
     */
    String getRestServicePath();

    /**
     * Get the rest Operation Path
     *
     * @return rest Operation Path
     */
    String getRestOperationPath();

    /**
     * Get the rest Operation method
     *
     * @return rest Operation method
     */
    String getRestOperationMethod();


    /**
     * Get the roles-allowed for the rest Operation
     *
     * @return roles-allowed for the rest operation
     */
    String getRolesAllowed();

    /**
     * Get the artifact-path for the rest Operation
     *
     * @return artifact resource path for the rest operation
     */
    String getArtifactPath();

}