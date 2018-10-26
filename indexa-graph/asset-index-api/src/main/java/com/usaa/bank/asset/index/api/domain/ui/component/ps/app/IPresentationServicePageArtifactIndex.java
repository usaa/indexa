package com.usaa.bank.asset.index.api.domain.ui.component.ps.app;

import com.usaa.bank.asset.index.api.domain.asset.IAssetIndex;
import com.usaa.bank.asset.index.api.domain.ui.component.ps.app.IPresentationServicesPageArtifact;

import java.util.Map;
import java.util.Set;

/**
 * This interface outlines the basic capabilities of an ps-application artifact's indexer.
 */
public interface IPresentationServicePageArtifactIndex extends IAssetIndex<IPresentationServicesPageArtifact> {

    /**
     * Creates a ps-application artifact instance. The instance will be initialized with the given parameter.
     *
     * @param applicationName     Name of PS applccation
     * @param contextRoot         Context root of the application
     * @param pageId              Id of a ps-page
     * @param pageName            Name of a ps-page
     * @param pageHeading         Heading name of a ps-page
     * @param pageTitle           Title name of a ps-page
     * @param nodeAttributes      Other attributes of a ps-page
     * @param mappingData         Mapping data between  WAS and EAR
     * @param jvmComponentMapping JVM and component map data
     * @return PS-artifact created
     */
    IPresentationServicesPageArtifact create(String applicationName, String contextRoot, String pageId, String pageName, String pageHeading, String pageTitle, Map<String, String> nodeAttributes, Map<String, String> mappingData, Map<String, String> jvmComponentMapping);

    /**
     * @param applicationName Name of PS applccation
     * @param contextRoot     Context root of the application
     * @param pageId          Id of a ps-page
     * @param pageName        Name of a ps-page
     * @return - True if the deletion was successful.
     */
    boolean remove(String applicationName, String contextRoot, String pageId, String pageName);

    /**
     * Searches for a ps-application artifacts using the applicationName, contextRoot, pageId and pageName from the lucene-index. <br>
     *
     * @param applicationName Name of PS applccation
     * @param contextRoot     Context root of the application
     * @param pageId          Id of a ps-page
     * @param pageName        Name of a ps-page
     * @return - a ps-application artifact matched
     */
    IPresentationServicesPageArtifact get(String applicationName, String contextRoot, String pageId, String pageName);


    /**
     * Searches for a ps-application artifacts using the applicationName and contextRoot from the lucene-index. <br>
     *
     * @param applicationName Name of PS applccation
     * @param contextRoot     Context root of the application
     * @return - a ps-application artifact matched
     */
    IPresentationServicesPageArtifact get(String applicationName, String contextRoot);

    /**
     * Finds all ps-application artifacts using the given applicationName from the lucene-index. <br>
     *
     * @param applicationName Name of PS applccation
     * @return - Set of ps-application artifacts matched
     */
    Set<IPresentationServicesPageArtifact> find(String applicationName);
}
