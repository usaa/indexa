package com.usaa.bank.asset.index.api.domain.asset.artifact;

import com.usaa.bank.asset.index.api.domain.asset.gav.GAV;
import com.usaa.bank.asset.index.api.domain.asset.IComputeAsset;

/**
 * Implementing this interface allows the implemented class to be considered as an variation of IArtifact.
 */
public interface IArtifact extends IComputeAsset {
    //Unique Key = (hash, artifactHash)

    /**
     * Get the hash
     *
     * @return hash value
     */
    String getHash();

    /**
     * Get the artifact path
     *
     * @return Artifactory path value
     */
    String getArtifactPath();

    /**
     * Get the GAV data
     *
     * @return GAV value
     */
    GAV getGAV();

    /**
     * Get the classifier data. Classifier is the type of data JAR,
     *
     * @return Classifier data
     */
    String getClassifier();
//	Set<String> getManifestEntries();
}