package com.usaa.bank.asset.index.api.domain.ui.component.ps.app;

import com.usaa.bank.asset.index.api.domain.asset.IComputeAsset;

/**
 * Implementing this interface allows the implemented class to be considered as an variation of IArtifact.
 */
public interface IPresentationServicesPageArtifact extends IComputeAsset {

    /**
     * Get the application name of the presentation service application.
     *
     * @return Application Name of a ps-App
     */
    String getPresentationServiceApplicationName();

    /**
     * Get the content root of the presentation service application.
     *
     * @return Context root of a ps-App
     */

    String getPresentationServiceContextRoot();

    /**
     * Get the page-id of a presentation service application.
     *
     * @return Page Id of a ps-App
     */
    String getPresentationServicePageId();

    /**
     * Get the page name of a presentation service application.
     *
     * @return Page name of a ps-App
     */
    String getPresentationServicePageName();

    /**
     * Get the page heading name of a presentation service application.
     *
     * @return Page heading of a ps-App
     */
    String getPresentationServicePageHeading();

    /**
     * Get the page title name of a presentation service application.
     *
     * @return Page title of a ps-App
     */
    String getPresentationServicePageTitle();

}