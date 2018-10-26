package com.usaa.bank.asset.index.api.domain.asset.gav;

import com.usaa.bank.graph.common.identity.SimpleImmutableIdentifier;

/**
 * Purpose of this class is to create and access an artifact's Id.
 */
public class ArtifactId extends SimpleImmutableIdentifier {
    private static final long serialVersionUID = 1L;

    /**
     * Create a ArtifactId instance for the given identifier. Given identifier will be used for creating the artifact's Id.
     *
     * @param identifier - identifier of the artifact
     */
    public ArtifactId(String identifier) {
        super(identifier);
    }
}
