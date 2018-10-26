package com.usaa.bank.asset.index.api.domain.asset.java.classes;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;

/**
 * JavaClassDependency is an model that represent the java-class-dependency asset and also contains the metadata associated with it.
 */
public class JavaClassDependency implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DELIMITER = ":";
    private static final String DELIMITER_CLASS = ".";

    private GUID guid;
    private GUID dependentClassGUID;
    private HierarchicalPackage dependencyPackage;
    private String dependencyClassName;

    /**
     * Creates a JavaClassDependency instance from the given use inputs. Dependent-Class-guid is the guid this dependency we are creating depends on.
     *
     * @param dependentClassGUID  - dependentClass-guid is the guid this dependency we are creating depends on.
     * @param dependencyPackage   - Dependency package represented as linage-list
     * @param dependencyClassName - Name of the dependency class
     */
    public JavaClassDependency(GUID dependentClassGUID, HierarchicalPackage dependencyPackage, String dependencyClassName) {
        super();
        this.guid = JavaClassDependency.createGUID(dependentClassGUID, dependencyPackage, dependencyClassName);
        this.dependentClassGUID = dependentClassGUID;
        this.dependencyPackage = dependencyPackage;
        this.dependencyClassName = dependencyClassName;
    }

    /**
     * Creates a JavaClassDependency instance from the given use inputs. Dependent-Class-guid is the guid this dependency we are creating depends on.
     *
     * @param guid                - Guid of the prospective JavaClassDependency
     * @param classGUID           - ClassGUID is the guid of a class this dependency we are creating depends on.
     * @param dependencyPackage   - Dependency package represented as linage-list
     * @param dependencyClassName - Name of the dependency class
     */
    public JavaClassDependency(GUID guid, GUID classGUID, HierarchicalPackage dependencyPackage, String dependencyClassName) {
        super();
        this.guid = guid;
        this.dependentClassGUID = classGUID;
        this.dependencyPackage = dependencyPackage;
        this.dependencyClassName = dependencyClassName;
    }

    /**
     * Gets the guid associated with the java-class-dependency.
     *
     * @return - Guid of dependency
     */
    public GUID getGUID() {
        return guid;
    }

    /**
     * Sets the guid for a java-class-dependency.
     *
     * @param guid - Guid to be set
     */
    protected void setGUID(GUID guid) {
        this.guid = guid;
    }

    /**
     * Obtains the ClassGUID of a class this dependency we created depends on.
     *
     * @return - Guid of dependent class
     */
    public GUID getDependentClassGUID() {
        return dependentClassGUID;
    }

    /**
     * Assigns the ClassGUID of a class this dependency we created depends on.
     *
     * @param classGUID - Guid of dependent class
     */
    protected void setDependentClassGUID(GUID classGUID) {
        this.dependentClassGUID = classGUID;
    }

    /**
     * Get the name of the dependency package
     *
     * @return - Linage-list of packcage-name
     */
    public HierarchicalPackage getDependencyPackage() {
        return dependencyPackage;
    }

    /**
     * Sets the name of the dependency pacakge name associated with java-class-dependency
     *
     * @param dependencyPackage - Pacakge-name in a linage-list form
     */
    protected void setDependencyPackage(HierarchicalPackage dependencyPackage) {
        this.dependencyPackage = dependencyPackage;
    }

    /**
     * Get the dependency class name linked with this java-class-dependency
     *
     * @return - Name of the dependency-class
     */
    public String getDependencyClassName() {
        return dependencyClassName;
    }

    /**
     * Set the name of the class to be associated with java-class-dependency.
     *
     * @param dependencyClassName- name of dependency-class
     */
    protected void setDependencyClassName(String dependencyClassName) {
        this.dependencyClassName = dependencyClassName;
    }

    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================

    /**
     * Creates a guid for the java-class-dependency from the given infomration
     *
     * @param dependentClassGUID  - ClassGUID is the guid of a class this dependency we are creating depends on.
     * @param dependencyPackage   - Dependency package represented as linage-list
     * @param dependencyClassName - Name of the dependency class
     * @return - Guid generated
     */
    public static GUID createGUID(GUID dependentClassGUID, HierarchicalPackage dependencyPackage, String dependencyClassName) {
        if (dependentClassGUID != null && dependencyPackage != null && StringUtils.isNotEmpty(dependencyClassName)) {
            StringBuffer sb = new StringBuffer();
            return GUIDFactory.createGUID(sb.append(dependentClassGUID.getStringValue()).append(DELIMITER).append(dependencyPackage.toString()).append(DELIMITER).append(dependencyClassName).toString());
        }
        return null;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        builder.append(this.getDependentClassGUID());
        builder.append(this.getDependencyPackage());
        builder.append(this.getDependencyClassName());
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
        JavaClassDependency that = (JavaClassDependency) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getDependentClassGUID(), that.getDependentClassGUID());
        builder.append(this.getDependencyPackage(), that.getDependencyPackage());
        builder.append(this.getDependencyClassName(), that.getDependencyClassName());
        return builder.isEquals();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

}