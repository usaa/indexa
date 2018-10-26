package com.usaa.bank.asset.index.api.domain.web.services.soap.services;

import com.usaa.bank.asset.index.api.domain.asset.IAssetIndex;

import java.util.Map;
import java.util.Set;

public interface ISoapServiceArtifactIndex extends IAssetIndex<ISoapServiceArtifact> {

    ISoapServiceArtifact create(String archiveName, String contextRoot, String serviceDefinitionType, String serviceName, String implementationClass, String interfaceClass, String operationName, String artifactPath, Map<String, String> mappingData, Map<String, String> jvmComponentMapping);

    boolean remove(String archiveName, String serviceName, String operationName);

    ISoapServiceArtifact get(String archiveName, String serviceName, String operationName);

    Set<ISoapServiceArtifact> find(String archiveName);

}
