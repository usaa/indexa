package com.usaa.bank.asset.index.api.domain.ui.component.wicket;

import com.usaa.bank.asset.index.api.domain.asset.IAssetIndex;

import java.util.Map;
import java.util.Set;

public interface IWicketApplicationArtifactIndex extends IAssetIndex<IWicketApplicationArtifact> {

    IWicketApplicationArtifact create(String wicketApplicationName, String wicketInfrastructureClass, String wicketDisplayName, String description, String wicketApplicationClass, String wicketApplicationArchiveName, Map<String, String> mappingData, Map<String, String> jvmComponentMapping);

    boolean remove(String wicketApplicationName, String wicketApplicationClass);

    IWicketApplicationArtifact get(String wicketApplicationName, String wicketApplicationClass);

    Set<IWicketApplicationArtifact> find(String wicketApplicationName);

}
