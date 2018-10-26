package com.usaa.bank.asset.index.api.domain.ui.component.ps.app;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * PresentationServicePageArtifact is a model that represent the ps-application asset type and contains meta-data associated with each ps-application.
 */
public class PresentationServicePageArtifact extends DigitalAsset implements IPresentationServicesPageArtifact, Serializable {
    private static final String GUID_DELIMITER = ":";
    public static final String PRESENTATION_SERVICE_APPLICATION_NAME = "presentationApplicationName";
    public static final String PRESENTATION_SERVICE_CONTEXT_ROOT = "presentationContextRoot";
    public static final String PRESENTATION_SERVICE_PAGE_ID = "pageId";
    public static final String PRESENTATION_SERVICE_PAGE_NAME = "pageName";
    public static final String PRESENTATION_SERVICE_PAGE_HEADING = "pageHeading";
    public static final String PRESENTATION_SERVICE_PAGE_TITLE = "pageTitle";
    public static final String PRESENTATION_SERVICE_PAGE_ATTRIBUTES = "attributesMetaData";

    /**
     * Creates a instance for the PresentationServicePageArtifact with the following data populated.
     *
     * @param guid GUID of the ps-artifact
     */
    public PresentationServicePageArtifact(GUID guid) {
        super(guid);
    }

    public PresentationServicePageArtifact(Set<String> tags, Map<String, Set<String>> fields) {
    }


    /**
     * Creates a instance for the PresentationServicePageArtifact with the following data populated.
     *
     * @param guid                               GUID of the artifact
     * @param presentationServiceApplicationName Name of PS application
     * @param presentationServiceContextRoot     Context root of the application
     * @param pageId                             Id of a ps-page
     * @param pageName                           Name of a ps-page
     * @param pageHeading                        Heading name of a ps-page
     * @param pageTitle                          Title name of a ps-page
     * @param nodeAttributes                     Other attributes of a ps-page
     * @param mappingData                        Mapping data between  WAS and EAR
     * @param jvmComponentMapping                JVM and component map data
     */
    public PresentationServicePageArtifact(GUID guid, String presentationServiceApplicationName, String presentationServiceContextRoot, String pageId, String pageName, String pageHeading, String pageTitle, Map<String, String> nodeAttributes, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        super(guid);
        this.setPresentationServiceApplicationName(presentationServiceApplicationName);
        this.setPresentationServiceContextRoot(presentationServiceContextRoot);
        this.setPresentationServicePageId(pageId);
        this.setPresentationServicePageName(pageName);
        this.setPresentationServicePageHeading(pageHeading);
        this.setPresentationServicePageTitle(pageTitle);
        this.setPresentationServicePageAttributes(nodeAttributes);
        this.setMappingData(mappingData);
        this.setJvmComponentMappingData(jvmComponentMapping);
    }


    /**
     * Creates a instance for an ps-application artifact. This is initialized with the data provided by the user.
     *
     * @param presentationServiceApplicationName Name of PS application
     * @param presentationServiceContextRoot     Context root of the application
     * @param pageId                             Id of a ps-page
     * @param pageName                           Name of a ps-page
     * @param pageHeading                        Heading name of a ps-page
     * @param pageTitle                          Title name of a ps-page
     * @param nodeAttributes                     Other attributes of a ps-page
     * @param mappingData                        Mapping data between  WAS and EAR
     * @param jvmComponentMapping                JVM and component map data
     */
    public PresentationServicePageArtifact(String presentationServiceApplicationName, String presentationServiceContextRoot, String pageId, String pageName, String pageHeading, String pageTitle, Map<String, String> nodeAttributes, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        super(PresentationServicePageArtifact.createGUID(presentationServiceContextRoot, pageId));
        this.setPresentationServiceApplicationName(presentationServiceApplicationName);
        this.setPresentationServiceContextRoot(presentationServiceContextRoot);
        this.setPresentationServicePageId(pageId);
        this.setPresentationServicePageName(pageName);
        this.setPresentationServicePageHeading(pageHeading);
        this.setPresentationServicePageTitle(pageTitle);
        this.setPresentationServicePageAttributes(nodeAttributes);
        this.setMappingData(mappingData);
        this.setJvmComponentMappingData(jvmComponentMapping);
    }

    /**
     * Creates a GUID for a ps-application with the given context-root and page-id of ps application.
     *
     * @param presentationServiceContextRoot Context root of the application
     * @param pageId                         Id of a ps-page
     * @return Guid created for an ps-application
     */
    public static GUID createGUID(String presentationServiceContextRoot, String pageId) {
        GUID guid = null;
        if (StringUtils.isNotEmpty(presentationServiceContextRoot) && StringUtils.isNotEmpty(pageId)) {
            guid = GUIDFactory.createGUID(presentationServiceContextRoot + GUID_DELIMITER + pageId);
        }
        return guid;
    }

    /**
     * Sets the application name for a ps-application.
     *
     * @param presentationServiceApplicationName Application name to be set
     */
    private void setPresentationServiceApplicationName(String presentationServiceApplicationName) {
        if (StringUtils.isNotEmpty(presentationServiceApplicationName)) {
            this.addField(PresentationServicePageArtifact.PRESENTATION_SERVICE_APPLICATION_NAME, presentationServiceApplicationName);
        }
    }

    /**
     * Sets the context root for a ps-application.
     *
     * @param presentationServiceContextRoot Context root to be set
     */
    private void setPresentationServiceContextRoot(String presentationServiceContextRoot) {
        if (StringUtils.isNotEmpty(presentationServiceContextRoot)) {
            this.addField(PresentationServicePageArtifact.PRESENTATION_SERVICE_CONTEXT_ROOT, presentationServiceContextRoot);
        }
    }

    /**
     * Sets the page id of a ps-application.
     *
     * @param pageId Page id to be set
     */
    private void setPresentationServicePageId(String pageId) {
        if (StringUtils.isNotEmpty(pageId)) {
            this.addField(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_ID, pageId);
        }
    }

    /**
     * Sets the page name of a ps-application.
     *
     * @param pageName Page name to be set
     */
    private void setPresentationServicePageName(String pageName) {
        if (StringUtils.isNotEmpty(pageName)) {
            this.addField(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_NAME, pageName);
        }
    }

    /**
     * Sets the page heading name of a ps-application.
     *
     * @param pageHeading Page heading to be set
     */
    private void setPresentationServicePageHeading(String pageHeading) {
        if (StringUtils.isNotEmpty(pageHeading)) {
            this.addField(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_HEADING, pageHeading);
        }
    }

    /**
     * Sets the page title name of a ps-application.
     *
     * @param pageTitle Page title to be set
     */
    private void setPresentationServicePageTitle(String pageTitle) {
        if (StringUtils.isNotEmpty(pageTitle)) {
            this.addField(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_TITLE, pageTitle);
        }
    }

    /**
     * Sets the attributes of a ps-application.
     *
     * @param pageAttributes Attributes to be set
     */
    private void setPresentationServicePageAttributes(Map<String, String> pageAttributes) {
        if (!pageAttributes.isEmpty()) {
            for (Map.Entry<String, String> e : pageAttributes.entrySet()) {
                this.addField(e.getKey(), e.getValue());
            }
        }
    }

    private void setMappingData(Map<String, String> mappingData) {
        if (mappingData != null && !mappingData.isEmpty()) {
            for (Map.Entry<String, String> e : mappingData.entrySet()) {
                this.addField(e.getKey(), e.getValue());
            }
        }
    }

    private void setJvmComponentMappingData(Map<String, String> jvmComponentMap) {
        if (jvmComponentMap != null && !jvmComponentMap.isEmpty()) {
            for (Map.Entry<String, String> e : jvmComponentMap.entrySet()) {
                this.addField(e.getKey(), e.getValue());
            }

        }
    }

    @Override
    public String getPresentationServiceApplicationName() {
        return this.getFieldValue(PresentationServicePageArtifact.PRESENTATION_SERVICE_APPLICATION_NAME);
    }

    @Override
    public String getPresentationServiceContextRoot() {
        return this.getFieldValue(PresentationServicePageArtifact.PRESENTATION_SERVICE_CONTEXT_ROOT);
    }

    @Override
    public String getPresentationServicePageId() {
        return this.getFieldValue(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_ID);
    }

    @Override
    public String getPresentationServicePageName() {
        return this.getFieldValue(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_NAME);
    }

    @Override
    public String getPresentationServicePageHeading() {
        return this.getFieldValue(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_HEADING);
    }

    @Override
    public String getPresentationServicePageTitle() {
        return this.getFieldValue(PresentationServicePageArtifact.PRESENTATION_SERVICE_PAGE_TITLE);
    }

    public boolean equals(Object o) {
        IPresentationServicesPageArtifact object = null;

        if (!(o instanceof IPresentationServicesPageArtifact)) {
            return false;
        } else {
            object = (IPresentationServicesPageArtifact) o;
        }

        if (!StringUtils.equals(this.getPresentationServiceApplicationName(), object.getPresentationServiceApplicationName())) {
            return false;
        }

        if (!StringUtils.equals(this.getPresentationServiceContextRoot(), object.getPresentationServiceContextRoot())) {
            return false;
        }

        if (!StringUtils.equals(this.getPresentationServicePageHeading(), object.getPresentationServicePageHeading())) {
            return false;
        }

        if (!StringUtils.equals(this.getPresentationServicePageId(), object.getPresentationServicePageId())) {
            return false;
        }

        if (!StringUtils.equals(this.getPresentationServicePageName(), object.getPresentationServicePageName())) {
            return false;
        }

        if (!StringUtils.equals(this.getPresentationServicePageTitle(), object.getPresentationServicePageTitle())) {
            return false;
        }

        return true;
    }
}

