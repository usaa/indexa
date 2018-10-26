package com.usaa.bank.asset.index.api.domain.ui.component.js;

import com.usaa.bank.asset.index.api.domain.asset.IAssetIndex;

import java.util.Map;
import java.util.Set;

public interface IJSApplicationArtifactIndex extends IAssetIndex<IJSApplicationArtifact> {

    IJSApplicationArtifact create(String appId, String appName, String appDescription, String appRev, String appAuthor, String version, Map<String, String> fields);

    boolean remove(String JsAppId, String version);

    IJSApplicationArtifact get(String JsAppId, String version);

    Set<IJSApplicationArtifact> find(String JsAppId);
}
