package com.usaa.bank.asset.index.api.domain.asset.java.classes;

import com.usaa.bank.asset.index.api.domain.asset.IComputeAsset;

/**
 * Implementing this interface allows the implemented class to be considered as an variation of IJavaClass.
 */
public interface IJavaClass extends IComputeAsset {
    // Unique key = (artifactHash, package, class)

    /**
     * Gets the artifact has for a java class.
     *
     * @return Hash value associated with the JavaClass
     */
    String getArtifactHash();

    /**
     * Gets a hierarchically structured package name for the java class.
     *
     * @return Hierarchical package name of JavaClass
     */
    HierarchicalPackage getPackage();

    /**
     * Gets class name of the java class
     *
     * @return Class Name linked with the JavaClass
     */
    String getClassName();

    /**
     * Gets fully qualified name of the class. Full name includes the package name as well.
     *
     * @return Full name of the JavaClass
     */
    String getFullName();

    /**
     * Gets the Minor gav of the java class.
     *
     * @return - Integer representing Minor gav
     */
    int getMinorVersion();

    /**
     * Gets the Minor gav of the java class.
     *
     * @return Integer representing Major gav
     */
    int getMajorVersion();

    /**
     * Gets the last date modified for the java class.
     *
     * @return Date modified
     */
    long getModifiedDate();

    /**
     * Gets the date  of creation for the java class.
     *
     * @return Date created
     */
    long getCreatedDate();
}