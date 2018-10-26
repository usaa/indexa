package com.usaa.bank.asset.index.api.domain.web.services.rest.resources;

import com.usaa.bank.asset.index.api.domain.asset.IAssetIndex;

import java.util.Map;
import java.util.Set;

public interface IRestServiceArtifactIndex extends IAssetIndex<IRestServiceArtifact> {


    /**
     * Creates a rest service artifact from the given data. Each will be created as an entry in the lucene-indexes.
     *
     * @param archiveName         - archive Name of rest service artifact
     * @param contextRoot         - context root of rest service artifact
     * @param httpMethod          - http Method of rest service artifact
     * @param restServicePath     - rest service path of rest service artifact
     * @param restOperationPath   - rest operation path of rest service artifact
     * @param restOperationMethod - rest operation method of rest service artifact
     * @param mappingData         Mapping data between  WAS and EAR
     * @param jvmComponentMapping JVM and component map data
     * @return - The rest service artifact created
     */
    IRestServiceArtifact create(String archiveName, String contextRoot, String httpMethod, String restServicePath, String restOperationPath, String restOperationMethod, String artifactPath, String rolesAllowed, Map<String, String> mappingData, Map<String, String> jvmComponentMapping);

    /**
     * Removes the rest service artifact with the given hash and artifactPath from the lucene-index. <br>
     *
     * @param archiveName     - archiveName to be removed
     * @param restServicePath - restServicePath to be removed
     * @param httpMethod      - httpMethod to be removed
     * @return - True if the deletion was successful.
     */
    boolean remove(String archiveName, String restServicePath, String httpMethod);

    /**
     * Searches for a rest service artifacts using the archive Name, rest service Path, and the http method from the lucene-index. <br>
     *
     * @param archiveName     - archiveName to be removed
     * @param restServicePath - restServicePath to be removed
     * @param httpMethod      - httpMethod to be removed
     * @return - an artifact matched
     */
    IRestServiceArtifact get(String archiveName, String restServicePath, String httpMethod);


    /**
     * Finds rest service artifacts from given archiveName from the lucene-index. <br>
     *
     * @param archiveName - archive Name added to search query
     * @return - Set of rest service artifacts matched
     */
    Set<IRestServiceArtifact> find(String archiveName);

}
