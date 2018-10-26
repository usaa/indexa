package com.usaa.bank.asset.index.api.domain.ui.component.js;

import com.usaa.bank.asset.index.api.domain.asset.IComputeAsset;


/**
 * Implementing this interface allows the implemented class to be considered as an variation of IArtifact.
 */
public interface IJSApplicationArtifact extends IComputeAsset {


    String getJsAppId();

    String getJsAppRev();

    String getJsAppName();

    String getJsAppDescription();

    String getJsAppAuthor();

    String getJsVersion();

    String getJsVersions();

}