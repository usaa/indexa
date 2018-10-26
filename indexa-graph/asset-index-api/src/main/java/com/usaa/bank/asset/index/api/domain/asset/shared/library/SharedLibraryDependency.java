package com.usaa.bank.asset.index.api.domain.asset.shared.library;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;

/**
 * SharedLibraryDependency is a model that represent the dependencies present for a shared library and also containg the meta-data corresponding to the dependencies.
 */
public class SharedLibraryDependency implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DELIMITER = ":";

    private GUID guid;
    private GUID sharedLibraryGUID;
    private String artifactPath;

    /**
     * Creates a SharedLibraryDependency instance from the given sharedLibraryGUID and artifactPath.
     *
     * @param sharedLibraryGUID - Guid of the shared-library
     * @param artifactPath      - Path of the shared library artifact
     */
    public SharedLibraryDependency(GUID sharedLibraryGUID, String artifactPath) {
        super();
        this.guid = SharedLibraryDependency.createGUID(sharedLibraryGUID, artifactPath);
        this.sharedLibraryGUID = sharedLibraryGUID;
        this.artifactPath = artifactPath;
    }

    /**
     * Creates a SharedLibraryDependency instance from the given sharedLibraryGUID and artifactPath.
     *
     * @param guid              - Guid of the
     * @param sharedLibraryGUID - Guid of the shared-library
     * @param artifactPath      - Path of the shared library artifact
     */
    public SharedLibraryDependency(GUID guid, GUID sharedLibraryGUID, String artifactPath) {
        super();
        this.guid = guid;
        this.sharedLibraryGUID = sharedLibraryGUID;
        this.artifactPath = artifactPath;
    }

    /**
     * Gets the guid associated with this Shared-library-dependency.
     *
     * @return - guid
     */
    public GUID getGUID() {
        return guid;
    }

    /**
     * Sets GUID value for an shared-library
     *
     * @param guid - Guid to be set
     */
    protected void setGUID(GUID guid) {
        this.guid = guid;
    }

    /**
     * Get the shared-library-dependency's parent shared-library which contains this dependency entry as one its entries.
     *
     * @return - Guid of dependency's parent shared-library
     */
    public GUID getSharedLibraryGUID() {
        return sharedLibraryGUID;
    }

    /**
     * Sets the shared-library-dependency's parent shared-library which contains this dependency entry as one its entries.
     *
     * @param classGUID - Guid to be set
     */
    protected void setSharedLibraryGUID(GUID classGUID) {
        this.sharedLibraryGUID = classGUID;
    }

    /**
     * Get artifact's path
     *
     * @return - Absolute path for the artifact
     */
    public String getArtifactPath() {
        return artifactPath;
    }

    /**
     * Sets the path where artifact is present in the file system.
     *
     * @param dependencyClassName -
     */
    protected void setArtifactPath(String dependencyClassName) {
        this.artifactPath = dependencyClassName;
    }

    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================

    /**
     * Creates a GUID for the shared-library dependency using the guid of shared-Library and artifact path.
     *
     * @param sharedLibraryGUID - Shared Library guid (This contains the dependency)
     * @param artifactPath      - path of artifact
     * @return - Guid for the dependency
     */
    public static GUID createGUID(GUID sharedLibraryGUID, String artifactPath) {
        if (sharedLibraryGUID != null && StringUtils.isNotEmpty(artifactPath)) {
            StringBuffer sb = new StringBuffer();
            return GUIDFactory.createGUID(sb.append(sharedLibraryGUID.getStringValue()).append(DELIMITER).append(artifactPath).toString());
        }
        return null;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        builder.append(this.getSharedLibraryGUID());
        builder.append(this.getArtifactPath());
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
        SharedLibraryDependency that = (SharedLibraryDependency) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getSharedLibraryGUID(), that.getSharedLibraryGUID());
        builder.append(this.getArtifactPath(), that.getArtifactPath());
        return builder.isEquals();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

}