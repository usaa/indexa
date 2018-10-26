package com.usaa.bank.asset.index.api.domain.asset.java.classes;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;

/**
 * JavaClass is an model that represent the Class asset and also contains the metadata associated with it.
 */
public class JavaClass extends DigitalAsset implements IJavaClass, Serializable {
    private static final long serialVersionUID = 1L;
    public static final String GUID_DELIMITER = ":";
    private static final String PACKAGE_DELIMITER = ".";
    public static final String KEY_ARTIFACT_HASH = "artifactHash";
    public static final String KEY_PACKAGE = "package";
    public static final String KEY_CLASS_NAME = "className";
    public static final String KEY_MODIFIED_DATE = "modifiedDate";
    public static final String KEY_CREATED_DATE = "createdDate";
    public static final String KEY_MAJOR_VERSION = "majorVersion";
    public static final String KEY_MINOR_VERSION = "minorVersion";


    /**
     * Create a JavaClass instance from the given tags and fields.
     *
     * @param tags   - Tags for the class
     * @param fields - Fields for the class
     */
    public JavaClass(Set<String> tags, Map<String, Set<String>> fields) {
        super(JavaClass.createGUID(
                DigitalAsset.getFirstValueString(JavaClass.KEY_ARTIFACT_HASH, fields),
                new HierarchicalPackage(DigitalAsset.getFirstValueString(JavaClass.KEY_PACKAGE, fields)),
                DigitalAsset.getFirstValueString(JavaClass.KEY_CLASS_NAME, fields)));
        this.addTags(tags);
        this.addFields(fields);
    }

    /**
     * Create a JavaClass instance from the given artifactHash, hierarchicalPackage and className .
     *
     * @param artifactHash        - hash of the class
     * @param hierarchicalPackage - hierarchical name of Package
     * @param className           - Name of the java class
     */
    public JavaClass(String artifactHash, HierarchicalPackage hierarchicalPackage, String className) {
        super(JavaClass.createGUID(artifactHash, hierarchicalPackage, className));
        this.setArtifactHash(artifactHash);
        this.setHierarchicalPackage(hierarchicalPackage);
        this.setClassName(className);
    }

    /**
     * Create a JavaClass instance from the given inputs.
     *
     * @param artifactHash        - hash of the class
     * @param hierarchicalPackage - hierarchical name of Package
     * @param className           - Name of the java class
     * @param createdDate         - Date of creation
     * @param modifiedDate        - Last modified date
     * @param majorVersion        - Major gav
     * @param minorVersion        - Minor gav
     */

    public JavaClass(String artifactHash, HierarchicalPackage hierarchicalPackage, String className, long createdDate, long modifiedDate, int majorVersion, int minorVersion) {
        super(JavaClass.createGUID(artifactHash, hierarchicalPackage, className));
        this.setArtifactHash(artifactHash);
        this.setHierarchicalPackage(hierarchicalPackage);
        this.setClassName(className);
        this.setCreatedDate(createdDate);
        this.setModifiedDate(modifiedDate);
        this.setMajorVersion(majorVersion);
        this.setMinorVersion(minorVersion);
    }


    /**
     * Create a JavaClass instance from the given inputs.
     *
     * @param guid                GUID of the java class
     * @param artifactHash        hash of the class
     * @param directoryContext    Directory service interface (Not implemented)
     * @param hierarchicalPackage hierarchical name of Package
     * @param className           Name of the java class
     * @param createdDate         Date of creation
     * @param modifiedDate        Last modified date
     * @param majorVersion        Major gav
     * @param minorVersion        Minor gav
     */
    protected JavaClass(GUID guid, String artifactHash, String directoryContext, HierarchicalPackage hierarchicalPackage, String className, long createdDate, long modifiedDate, int majorVersion, int minorVersion) {
        super(guid);
        this.setArtifactHash(artifactHash);
        this.setHierarchicalPackage(hierarchicalPackage);
        this.setClassName(className);
        this.setCreatedDate(createdDate);
        this.setModifiedDate(modifiedDate);
        this.setMajorVersion(majorVersion);
        this.setMinorVersion(minorVersion);
    }


    /**
     * Get the hierarchical structure for the package name. The data is obtained from the fieldMap which contains all the fields of javaClass.
     *
     * @return - HierarchicalPackage data
     */
    public HierarchicalPackage getHierarchicalPackage() {
        return new HierarchicalPackage(this.getFieldValue(JavaClass.KEY_PACKAGE));
    }

    /**
     * Set the hierarchical structure for the JavaClass
     *
     * @param hierarchicalPackage hierarchical name of Package
     */
    private void setHierarchicalPackage(HierarchicalPackage hierarchicalPackage) {
        if (hierarchicalPackage != null) {
            this.addField(JavaClass.KEY_PACKAGE, hierarchicalPackage.toString());
        }
    }

    /**
     * Get the class name associated with a JavaClass. The data is obtained from the fieldMap which contains all the fields of javaClass.
     *
     * @return Name of the class
     */
    public String getClassName() {
        return this.getFieldValue(JavaClass.KEY_CLASS_NAME);
    }

    /**
     * Get the class name associated with a JavaClass. The data is obtained from the fieldMap which contains all the fields of javaClass.
     *
     * @return - class name
     */
    public String getClassNameString() {
        if (this.getClassName() != null) {
            return this.getClassName();
        }
        return "";
    }

    /**
     * Sets the name of the java class.
     *
     * @param className - ClassName to be set
     */
    private void setClassName(String className) {
        this.addField(JavaClass.KEY_CLASS_NAME, className);
    }

    /**
     * Get the last modified date metadata from JavaClass
     *
     * @return - unix timestamp
     */
    public long getModifiedDate() {
        return Long.valueOf(this.getFieldValue(JavaClass.KEY_MODIFIED_DATE));
    }

    /**
     * Sets last modified date for the JavaClass.
     *
     * @param modifiedDate - Unix time-stamp to be set
     */
    private void setModifiedDate(long modifiedDate) {
        this.addField(JavaClass.KEY_MODIFIED_DATE, String.valueOf(modifiedDate));
    }

    /**
     * Get the creation date metadata from JavaClass
     *
     * @return - unix timestamp
     */
    public long getCreatedDate() {
        return Long.valueOf(this.getFieldValue(JavaClass.KEY_CREATED_DATE));
    }

    /**
     * Sets last creation date for the JavaClass.
     *
     * @param createdDate - Unix time-stamp to be set
     */
    private void setCreatedDate(long createdDate) {
        this.addField(JavaClass.KEY_CREATED_DATE, String.valueOf(createdDate));
    }

    public int getMajorVersion() {
        return Integer.valueOf(this.getFieldValue(JavaClass.KEY_MAJOR_VERSION));
    }

    /**
     * Sets major gav associated with the class.
     *
     * @param majorVersion - major gav to set
     */
    private void setMajorVersion(int majorVersion) {
        this.addField(JavaClass.KEY_MAJOR_VERSION, String.valueOf(majorVersion));
    }


    public int getMinorVersion() {
        return Integer.valueOf(this.getFieldValue(JavaClass.KEY_MINOR_VERSION));
    }

    /**
     * Sets minor gav associated with the class.
     *
     * @param minorVersion - minor gav to set
     */
    private void setMinorVersion(int minorVersion) {
        this.addField(JavaClass.KEY_MINOR_VERSION, String.valueOf(minorVersion));
    }

    public HierarchicalPackage getPackage() {
        return new HierarchicalPackage(this.getFieldValue(JavaClass.KEY_PACKAGE));
    }

    /**
     * Gets package name of the class.
     *
     * @return - pacakgeName value
     */
    public String getPackageString() {
        if (this.getPackage() != null) {
            return this.getPackage().toString();
        }
        return "";
    }

    public String getArtifactHash() {
        return this.getFieldValue(JavaClass.KEY_ARTIFACT_HASH);
    }

    /**
     * Sets  the artifact's hash value associated with this JavaClass. Each class has an associated artifact's hash value.
     *
     * @param artifactHash - hash of an artifact
     */
    private void setArtifactHash(String artifactHash) {
        if (StringUtils.isNotEmpty(artifactHash)) {
            this.addField(JavaClass.KEY_ARTIFACT_HASH, artifactHash);
        }
    }

    @Override
    public String getFullName() {
        return this.getPackageString() + JavaClass.PACKAGE_DELIMITER + this.getClassNameString();
    }

    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================

    /**
     * Create GUID for the class using the various informations such as hash, package and classname.
     *
     * @param artifactHash        - hash of the class
     * @param hierarchicalPackage - hierarchical name of Package
     * @param className           - class name
     * @return - GUID object
     */
    public static GUID createGUID(String artifactHash, HierarchicalPackage hierarchicalPackage, String className) {
        GUID guid = null;
        if (StringUtils.isNotEmpty(artifactHash) && hierarchicalPackage != null && StringUtils.isNotEmpty(className)) {
            guid = GUIDFactory.createGUID(artifactHash + GUID_DELIMITER + hierarchicalPackage.toString() + GUID_DELIMITER + className);
        }
        return guid;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

    public boolean equals(Object o) {
        IJavaClass object = null;

        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof IJavaClass)) {
            return false;
        } else {
            object = (IJavaClass) o;
        }

        if (!StringUtils.equals(this.getClassName(), object.getClassName())) {
            return false;
        }

        if (!(this.getMinorVersion() == object.getMinorVersion())) {
            return false;
        }

        if (!(this.getMajorVersion() == object.getMajorVersion())) {
            return false;
        }

        if (!(this.getModifiedDate() == object.getModifiedDate())) {
            return false;
        }

        if (!(this.getCreatedDate() == object.getCreatedDate())) {
            return false;
        }
        return true;
    }


}