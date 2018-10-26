package com.usaa.bank.asset.index.api.domain.ui.component.wicket;

import com.usaa.bank.asset.index.api.domain.asset.IComputeAsset;

/**
 * Implementing this interface allows the implemented class to be considered as an variation of IArtifact.
 */
public interface IWicketApplicationArtifact extends IComputeAsset {

    /**
     * Get the Wicket Application Name
     *
     * @return Wicket Application Name
     */
    String getWicketApplicationName();

    /**
     * Get the Wicket Infrastructure Class
     *
     * @return Wicket Infrastructure Class
     */

    String getWicketInfrastructureClass();

    /**
     * Get the Wicket Display Name
     *
     * @return Wicket Display Name
     */
    String getWicketDisplayName();

    /**
     * Get the description
     *
     * @return description
     */
    String getDescription();

    /**
     * Get the Wicket Application Class
     *
     * @return Wicket Application Class
     */
    String getWicketApplicationClass();

    /**
     * Get the Wicket Application Archive Name
     *
     * @return Wicket Application Archive Name
     */
    String getwicketApplicationArchiveName();


}