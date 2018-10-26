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
 * ClassLoaderLibrary is an model that represent the various libraries which makes the classLoader asset and also contains the metadata associated with it. <br>
 * ClassLoaderLibrary is associated with a classLoader.
 */
public class ClassLoaderLibrary implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DELIMITER = ":";

    private GUID guid;
    private GUID classLoaderGUID;
    private String sharedLibraryName;

    /**
     * Creates a ClassLoaderLibrary instance with classLoaderGUID and name. ClassLoaderGUID is the guid of a particular class-loader and also the name represent the shared-library name. <br>
     * A new GUID will be created from classLoaderGUID and name for this instance.
     *
     * @param classLoaderGUID - Guid of class-loader
     * @param name            - Name of shared-library
     */
    public ClassLoaderLibrary(GUID classLoaderGUID, String name) {
        super();
        this.guid = ClassLoaderLibrary.createGUID(classLoaderGUID, name);
        this.classLoaderGUID = classLoaderGUID;
        this.sharedLibraryName = name;
    }

    /**
     * Creates a ClassLoaderLibrary instance with classLoaderGUID and name. "guid"  is the GUID used for this instance of ClassLoaderLibrary ,
     * ClassLoaderGUID is the guid of a particular class-loader and also the name represent the shared-library name. <br>
     *
     * @param guid            - Guid used for ClassLoaderLibrary
     * @param classLoaderGUID - Guid of class-loader
     * @param name            - Name of shared-library
     */
    public ClassLoaderLibrary(GUID guid, GUID classLoaderGUID, String name) {
        super();
        this.guid = guid;
        this.classLoaderGUID = classLoaderGUID;
        this.sharedLibraryName = name;
    }

    /**
     * Gets Guid of the ClassLoaderLibrary/
     *
     * @return - Guid
     */
    public GUID getGUID() {
        return guid;
    }

    /**
     * Assign a GUID for a particular ClassLoaderLibrary
     *
     * @param guid - Guid to be set
     */
    protected void setGUID(GUID guid) {
        this.guid = guid;
    }

    /**
     * Get the guid of ClassLoader associated with the ClassLoaderLibrary
     *
     * @return - Guid of classloader
     */
    public GUID getClassLoaderGUID() {
        return classLoaderGUID;
    }

    /**
     * Assign a class-loader-guid to be associated with a ClassLoaderLibrary.
     *
     * @param classLoaderGUID - Guid of class-loader
     */
    protected void setClassLoaderGUID(GUID classLoaderGUID) {
        this.classLoaderGUID = classLoaderGUID;
    }

    /**
     * Retrives a shared name library linked with a ClassLoaderLibrary.
     *
     * @return - Name of shared library
     */
    public String getSharedLibraryName() {
        return sharedLibraryName;
    }

    /**
     * Assign a shared-library name for a ClassLoaderLibrary.
     *
     * @param sharedLibraryName - Name of shared library
     */
    protected void setSharedLibraryName(String sharedLibraryName) {
        this.sharedLibraryName = sharedLibraryName;
    }

    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================

    /**
     * Creates a GUID for the ClassLoaderLibrary from the given class-loader-guid and name of the shared library this ClassLoaderLibrary is based of.
     *
     * @param classLoaderGUID - Guid of class-loader
     * @param name            - Name of shared-library
     * @return - Guid for ClassLoaderLibrary
     */
    public static GUID createGUID(GUID classLoaderGUID, String name) {
        if (classLoaderGUID != null && StringUtils.isNotEmpty(name)) {
            StringBuffer sb = new StringBuffer();
            return GUIDFactory.createGUID(sb.append(classLoaderGUID.getStringValue()).append(DELIMITER).append(name).toString());
        }
        return null;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        builder.append(this.getClassLoaderGUID());
        builder.append(this.getSharedLibraryName());
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
        ClassLoaderLibrary that = (ClassLoaderLibrary) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getClassLoaderGUID(), that.getClassLoaderGUID());
        builder.append(this.getSharedLibraryName(), that.getSharedLibraryName());
        return builder.isEquals();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

}
