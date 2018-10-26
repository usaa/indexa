package com.usaa.bank.asset.index.api.domain.asset.classloader;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * ClassLoader is an model that represent the ClassLoader asset and also contains the metadata associated with it.
 */
public class ClassLoader implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DELIMITER = ":";

    private GUID guid;
    private GUID deploymentConfigGUID;
    private String id;

    /**
     * Instantiates a ClassLoader with id and deploymentConfigGuid information. ID are set for th classloader identification purposes while the deployment-config-guid
     * is used for the deployment configuration set for this particular class-loader. <br>
     * <p>
     * Guid will be created for ClassLoader from the deploymentConfigGUID and id.
     *
     * @param deploymentConfigGUID - Deployment configuration to be used for this ClassLoader
     * @param id                   - Id to set for the ClassLoader
     */
    public ClassLoader(GUID deploymentConfigGUID, String id) {
        super();
        this.guid = ClassLoader.createGUID(deploymentConfigGUID, id);
        this.deploymentConfigGUID = deploymentConfigGUID;
        this.id = id;
    }

    /**
     * Instantiates a ClassLoader with guid, id and deploymentConfigGuid information. Guid and ID are set for th classloader identification purposes while the deployment-config-guid
     * is used for the deployment configuration set for this particular class-loader
     *
     * @param guid                 - Guid to be linked with ClassLoader
     * @param deploymentConfigGUID - Deployment configuration to be used for this ClassLoader
     * @param id                   - Id to set for the ClassLoader
     */
    public ClassLoader(GUID guid, GUID deploymentConfigGUID, String id) {
        super();
        this.guid = guid;
        this.deploymentConfigGUID = deploymentConfigGUID;
        this.id = id;
    }

    /**
     * Gets the GUID set for a ClassLoader.
     *
     * @return - Guid object
     */
    public GUID getGUID() {
        return guid;
    }

    /**
     * Set the GUID to be linked with a ClassLoader
     *
     * @param guid - Guid to be set
     */
    protected void setGUID(GUID guid) {
        this.guid = guid;
    }

    /**
     * Get the deploymentConfig assocaited with the ClassLoader.
     *
     * @return - Guid of deployment-config
     */
    public GUID getDeploymentConfigGUID() {
        return deploymentConfigGUID;
    }

    /**
     * Sets a deploymentConfig of a particular ClassLoader.
     *
     * @param deploymentConfigGUID - guid of deployment-config
     */
    protected void setDeploymentConfigGUID(GUID deploymentConfigGUID) {
        this.deploymentConfigGUID = deploymentConfigGUID;
    }

    /**
     * Gets the Id to be associated with a ClassLoader
     *
     * @return - Id of a ClassLoader
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the Id to be associated with a ClassLoader
     *
     * @param id - Id of ClassLoader
     */
    protected void setId(String id) {
        this.id = id;
    }

    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================
    public static GUID createGUID(GUID deploymentConfigGUID, String id) {
        if (deploymentConfigGUID != null && StringUtils.isNotEmpty(id)) {
            StringBuffer sb = new StringBuffer();
            return GUIDFactory.createGUID(sb.append(deploymentConfigGUID.getStringValue()).append(DELIMITER).append(id).toString());
        }
        return null;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        builder.append(this.getDeploymentConfigGUID());
        builder.append(this.getId());
        return builder.toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ClassLoader that = (ClassLoader) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getDeploymentConfigGUID(), that.getDeploymentConfigGUID());
        builder.append(this.getId(), that.getId());
        return builder.isEquals();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

}