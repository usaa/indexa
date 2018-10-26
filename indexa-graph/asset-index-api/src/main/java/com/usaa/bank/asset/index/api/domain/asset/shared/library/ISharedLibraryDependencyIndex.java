package com.usaa.bank.asset.index.api.domain.asset.shared.library;

import java.util.Set;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.IStatefulIndexerSession;

public interface ISharedLibraryDependencyIndex extends IStatefulIndexerSession{

    /**
     * Adds a dependency (artifactPath) to a shared-library. A new GUID is created using the sharedLibraryGUID and artifactPath.
     * A field is created, it is a Map with sharedLibraryGUID, artifactPath as its entries with its corresponding predefined key.<br>
     * Using the field map this function will update the lucene-indexes associated and save.
     *
     * @param sharedLibraryGUID Guid of shared-Config
     * @param artifactPath      Path of an artifact
     * @return True for successful addition
     */
    boolean addDependency(GUID sharedLibraryGUID, String artifactPath);

    /**
     * Removes a dependency from a shared-library. A new GUID is created using the sharedLibraryGUID and libraryName.
     * A field is created, it is a Map with GUID as its entry with its corresponding predefined key.<br>
     * Using the field map this function will delete the lucene-indexes associated. Only one dependency linked to GUID will be deleted.
     *
     * @param sharedLibraryGUID Guid of shared-Config
     * @param artifactPath      Path of an artifact
     */
    void removeDependency(GUID sharedLibraryGUID, String artifactPath);

    /**
     * Removes all the dependencies associated with a shared-library.
     * A field is created, it is a Map with sharedLibraryGUID as its entry with its corresponding predefined key.<br>
     * Using the field map this function will delete the lucene-indexes.
     *
     * @param sharedLibraryGUID Guid of shared-Config
     */
    void removeDependencies(GUID sharedLibraryGUID);

    /**
     * Finds all the libraries associated with the serverConfig.
     *
     * @return Collection of SharedLibraryDependency
     */
    Set<SharedLibraryDependency> findDependencies();

    /**
     * Get all the dependencies corresponding to shared-library(s). ArtifactPath is used to identify the shared-library from the lucene-indexes. <br>
     * A field is created, it is a Map with artifactPath as its entry with its corresponding predefined key.
     * Using the field map this function will identify the shared-library and grab all the dependencies from each index.
     *
     * @param artifactPath Path of an artifact
     * @return Collection of dependencies for all shared-library having artifactPath
     */
    Set<SharedLibraryDependency> findDependencies(String artifactPath);

    /**
     * Get all the dependencies corresponding to shared-library(s). SharedLibraryGUID is used to identify the shared-library from the lucene-indexes. <br>
     * A field is created, it is a Map with sharedLibraryGUID as its entry with its corresponding predefined key.
     * Using the field map this function will identify the shared-library and grab all the dependencies from that particular index.
     *
     * @param sharedLibraryGUID Guid of shared-Config
     * @return Collection of dependencies for a particular shared-library.
     */
    Set<SharedLibraryDependency> findDependencies(GUID sharedLibraryGUID);

    /**
     * Get all the libraries corresponding to an shared-library. ArtifactPath is used to identify the shared-library from the lucene-indexes. <br>
     * A field is created, it is a Map with artifactPath as its entry with its corresponding predefined key.
     * Using the field map this function will identify the shared-library and grab all the libraries from that particular index.
     *
     * @param artifactPath Path of an artifact
     * @return Collection of shared-libraries that contains the artifactPath.
     */
    Set<GUID> findLibraries(String artifactPath);

    /**
     * Get all the artifactPath corresponding to shared-library(s). SharedLibraryGUID is used to identify the shared-library from the lucene-indexes. <br>
     * A field is created, it is a Map with sharedLibraryGUID as its entry with its corresponding predefined key.
     * Using the field map this function will identify the shared-library and grab all the artifactsPaths from that particular index.
     *
     * @param sharedLibraryGUID Guid of a shared-Config
     * @return Collection of artifact-path for an shared-library
     */
    Set<String> findArtifactPaths(GUID sharedLibraryGUID);
}
