package com.usaa.bank.asset.index.api.domain.asset.gav;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.usaa.bank.graph.common.identity.ImmutableIdentifier;

/**
 * GAV is an model that represent the GAV part of an Artifact. Contains information such as groupId, artifactId, gav and artifactType etc.
 */

//An Example GAV for lucene-core 7.3.0
//https://mvnrepository.com/artifact/org.apache.lucene/lucene-core/7.3.0

//Group: 'org.apache.lucene', ArtifactID: 'lucene-core', Version: '7.3.0'


public class GAV implements ImmutableIdentifier, Serializable {
    // ========================================================================
    // Everything at the level of a GAV has some kind of business/IT purpose.
    // An example could be a web application or a library
    // This purpose should be the same for all files under this GAV. There
    // can be multiple. For example a GAV could contain a jar and jar with source.
    // These both still have a business purpose of a re-usable library,
    // however their consumers will use them differently. This is resolved
    // with "classifiers". Classifiers are at the Artifact level. This level
    // holds the IT purpose associated with multiple "classified" artifacts
    // ========================================================================
    private static final long serialVersionUID = 1L;
    private static final String DELIMITER = ":";

    private GroupId groupId;
    private ArtifactId artifactId;
    private Version version;

    private ArtifactType artifactType;


    /**
     * Create a GAV instance with the given groupId, artifactId, gav and artifactType.
     *
     * @param groupId      - GroupId part of gav
     * @param artifactId   - ArtifactId part of gav
     * @param version      - Version part of gav
     * @param artifactType - ArtifactType Enum part of gav
     */
    public GAV(GroupId groupId, ArtifactId artifactId, Version version, ArtifactType artifactType) {
        //this();

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.artifactType = artifactType;
    }

    /**
     * Create a GAV instance with the given groupId, artifactId, gav and artifactType.
     *
     * @param groupId      - GroupId value
     * @param artifactId   - Artifact ID value
     * @param version      - Version value
     * @param artifactType - ArtifactType Enum
     */
    public GAV(String groupId, String artifactId, String version, ArtifactType artifactType) {
        this(new GroupId(groupId), new ArtifactId(artifactId), new Version(version), artifactType);
    }

    /**
     * Get groupId part fo the GAV.
     *
     * @return - GroupId object
     */
    public GroupId getGroupId() {
        return this.groupId;
    }

    /**
     * Get the artifactId part of the GAV
     *
     * @return - ARtifactID object
     */
    public ArtifactId getArtifactId() {
        return this.artifactId;
    }

    /**
     * Get the Version part of the GAV
     *
     * @return - Version object
     */
    public Version getVersion() {
        return this.version;
    }

    /**
     * Get groupId part fo the GAV.
     *
     * @return - GroupId value
     */
    public String getGroupIdString() {
        if (this.groupId != null) {
            return this.groupId.toString();
        }
        return "";
    }

    /**
     * Get the artifactId part of the GAV
     *
     * @return - ArtifactId value
     */
    public String getArtifactIdString() {
        if (this.artifactId != null) {
            return this.artifactId.getStringValue();
        }
        return "";
    }

    /**
     * Get the Version part of the GAV
     *
     * @return - Version value
     */
    public String getVersionString() {
        if (this.version != null) {
            return this.version.toString();
        }
        return "";
    }

    /**
     * Get the ArtifactType part of the GAV
     *
     * @return - ArtifactType object
     */
    public ArtifactType getArtifactType() {
        return artifactType;
    }

    /**
     * Get the ArtifactType part of the GAV
     *
     * @return - ArtifactType value
     */
    public String getArtifactTypeString() {
        if (this.artifactType != null) {
            return this.artifactType.toString();
        }
        return "";
    }

    @Override
    public String getStringValue() {
        StringBuffer sb = new StringBuffer(this.getGroupIdString());
        sb.append(GAV.DELIMITER);
        sb.append(this.getArtifactIdString());
        sb.append(GAV.DELIMITER);
        sb.append(this.getVersionString());
        sb.append(GAV.DELIMITER);
        sb.append(this.getArtifactType());
        return sb.toString();
    }


    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        builder.append(this.getGroupId());
        builder.append(this.getArtifactId());
        builder.append(this.getVersion());
        builder.append(this.getArtifactType());
//	    builder.append(this.getClassifier());
        return builder.toHashCode();
    }

    /*public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GAV)) {
            return false;
        }
        GAV that = (GAV) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getGroupId(), that.getGroupId());
        builder.append(this.getArtifactId(), that.getArtifactId());
        builder.append(this.getVersion(), that.getVersion());
        builder.append(this.getArtifactType(), that.getArtifactType());

        return builder.isEquals();
    }*/

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GAV)) {
            return false;
        }
        GAV that = (GAV) obj;
        EqualsBuilder builder = new EqualsBuilder();

        if (!StringUtils.equals(this.getGroupIdString(), that.getGroupIdString())) {
            return false;
        }

        if (!StringUtils.equals(this.getArtifactIdString(), that.getArtifactIdString())) {
            return false;
        }

        if (!StringUtils.equals(this.getVersionString(), that.getVersionString())) {
            return false;
        }
        if (!StringUtils.equals(this.getArtifactTypeString(), that.getArtifactTypeString())) {
            return false;
        }


        return true;
    }

    public String toString() {
        return this.getStringValue();
    }


}