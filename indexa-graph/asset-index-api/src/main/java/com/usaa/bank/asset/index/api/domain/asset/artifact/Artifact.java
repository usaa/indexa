package com.usaa.bank.asset.index.api.domain.asset.artifact;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactId;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.gav.GAV;
import com.usaa.bank.asset.index.api.domain.asset.gav.GroupId;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Artifact is an model that represent the artifact asset and also contains the metadata associated with it. It could be any type such as COMPONENT, INSTALLABLE, DISTRIBUTION.
 */
public class Artifact extends DigitalAsset implements IArtifact, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String GUID_DELIMITER = ":";
    public static final String KEY_HASH = "hash";
    public static final String KEY_ARTIFACT_PATH = "artifactPath";
    public static final String KEY_GROUP_ID = "groupId";
    public static final String KEY_ARTIFACT_ID = "artifactId";
    public static final String KEY_VERSION = "version";
    public static final String KEY_ARTIFACT_TYPE = "artifactType";
    public static final String KEY_CLASSIFIER = "classifier";
    public static final String CLASSIFIER_DEFAULT = "DEFAULT";


    /**
     * Creates a new Artifact instance from the given tags and fields data.
     *
     * @param tags   Tags for an artifact
     * @param fields Various fields in the artifact
     */
    public Artifact(Set<String> tags, Map<String, Set<String>> fields) {
        super(Artifact.createGUID(
                DigitalAsset.getFirstValueString(Artifact.KEY_HASH, fields),
                DigitalAsset.getFirstValueString(Artifact.KEY_ARTIFACT_PATH, fields)));
        this.addTags(tags);
        this.addFields(fields);

    }

    /**
     * Creates a new Artifact instance from the given guid.
     *
     * @param guid GUID of the artifact
     */
    public Artifact(GUID guid) {
        super(guid);
    }


    /**
     * Creates a new Artifact instance from the given hash and artifactPath respectively.
     *
     * @param hash         Hash of the artifact
     * @param artifactPath Artifact's path location
     */
    public Artifact(String hash, String artifactPath) {
        super(Artifact.createGUID(hash, artifactPath));
        this.setHash(hash);
        this.setArtifactPath(artifactPath);

    }

    /**
     * Creates a new Artifact instance from the given hash, artifactpath , gav and classfier.
     *
     * @param hash         Hash of the artifact
     * @param artifactPath Path of the artifact
     * @param gav          Gav of artifact
     * @param classifier   Classifier to be set for the artifact
     */
    public Artifact(String hash, String artifactPath, GAV gav, String classifier) {
        super(Artifact.createGUID(hash, artifactPath));
        this.setHash(hash);
        this.setArtifactPath(artifactPath);
        this.setGAV(gav);
        this.setClassifier(classifier);
    }

    public String getHash() {
        return this.getFieldValue(Artifact.KEY_HASH);
    }

    private void setHash(String hash) {
        if (StringUtils.isNotEmpty(hash)) {
            this.addField(Artifact.KEY_HASH, hash);
        }
    }

    public String getArtifactPath() {
        return this.getFieldValue(Artifact.KEY_ARTIFACT_PATH);
    }

    private void setArtifactPath(String artifactPath) {
        if (StringUtils.isNotEmpty(artifactPath)) {
            this.addField(Artifact.KEY_ARTIFACT_PATH, artifactPath);
        }
    }

    /**
     * Get groupId for the asset.
     *
     * @return - GroupID value
     */
    public GroupId getGroupId() {
        return new GroupId(this.getFieldValue(Artifact.KEY_GROUP_ID));
    }

    /**
     * Assign gropuID value for an asset with the user specified value.
     *
     * @param groupId - GroupId
     */
    private void setGroupId(GroupId groupId) {
        if (groupId != null) {
            this.addField(Artifact.KEY_GROUP_ID, groupId.toString());
        }
    }

    /**
     * Get artifact's ID
     *
     * @return - Id of the artifact
     */
    public ArtifactId getArtifactId() {
        return new ArtifactId(this.getFieldValue(Artifact.KEY_ARTIFACT_ID));
    }

    /**
     * Set an artifact Id for an asset. This will be added ot the field of the asset ultimately.
     *
     * @param artifactId - Id of the artifact
     */
    private void setArtifactId(ArtifactId artifactId) {
        if (artifactId != null) {
            this.addField(Artifact.KEY_ARTIFACT_ID, artifactId.toString());
        }
    }

    /**
     * Get Version of the asset. Obtains the gav value from the fields associated with the artifact.
     *
     * @return - Version of the asset
     */
    public Version getVersion() {
        return new Version(this.getFieldValue(Artifact.KEY_VERSION));
    }

    /**
     * Sets gav for the artifact. Ultimately adds the gav to the fieldMap.
     *
     * @param version - Version to be set
     */
    private void setVersion(Version version) {
        if (version != null) {
            this.addField(Artifact.KEY_VERSION, version.toString());
        }
    }

    /**
     * Get the type of artifact used in this asset.
     *
     * @return - Artifact's type
     */
    public ArtifactType getArtifactType() {
        return ArtifactType.valueOf(this.getFieldValue(Artifact.KEY_ARTIFACT_TYPE));
    }

    /**
     * Assign a type of artifact assocaited for an asset. ArtifactType is an Enum provided by the user.
     *
     * @param artifactType - Artifact type Enum
     */
    private void setArtifactType(ArtifactType artifactType) {
        if (artifactType != null) {
            this.addField(Artifact.KEY_ARTIFACT_TYPE, artifactType.toString());
        }
    }

    /**
     * Get Gav of an artifact.
     *
     * @return - Gav value
     */
    public GAV getGAV() {
        return new GAV(this.getGroupId(), this.getArtifactId(), this.getVersion(), this.getArtifactType());
    }

    /**
     * Set the GAV for an artifact. GAV is amalgamation of groupId, artifactId, artifactType and gav.
     *
     * @param gav - GAV to be set
     */
    private void setGAV(GAV gav) {
        if (gav != null) {
            this.setGroupId(gav.getGroupId());
            this.setArtifactId(gav.getArtifactId());
            this.setVersion(gav.getVersion());
            this.setArtifactType(gav.getArtifactType());
        }
    }

    /**
     * Get gav  as string for an artifact.
     *
     * @return - Gav represented in string format
     */
    public String getGAVString() {
        GAV gav = this.getGAV();
        if (gav != null) {
            return gav.getStringValue();
        }
        return "";
    }

    /**
     * Get the classfier associated with the artifact.
     *
     * @return - Classifier type
     */
    public String getClassifier() {
        return this.getFieldValue(Artifact.KEY_CLASSIFIER);
    }

    /**
     * Sets the classfier for an artifact
     *
     * @param classifier - Classifier to be set for the artifact
     */
    private void setClassifier(String classifier) {
        if (StringUtils.isNotEmpty(classifier)) {
            this.addField(Artifact.KEY_CLASSIFIER, classifier);
        } else {
            this.addField(Artifact.KEY_CLASSIFIER, Artifact.CLASSIFIER_DEFAULT);
        }
    }


    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================

    /**
     * Create guid for an artifact using the has and artifactPath.
     *
     * @param hash         - Has of an artifact
     * @param artifactPath = Artifact's path in filesystem
     * @return - GUID object created
     */
    private static GUID createGUID(String hash, String artifactPath) {
        GUID guid = null;
        if (StringUtils.isNotEmpty(hash) && StringUtils.isNotEmpty(artifactPath)) {
            guid = GUIDFactory.createGUID(hash + GUID_DELIMITER + artifactPath);
        }
        return guid;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

    public boolean equals(Object o) {
        IArtifact object = null;

        if (!(o instanceof IArtifact)) {
            return false;
        } else {
            object = (IArtifact) o;
        }

        if (!StringUtils.equals(this.getHash(), object.getHash())) {
            return false;
        }

        if (!StringUtils.equals(this.getArtifactPath(), object.getArtifactPath())) {
            return false;
        }

        if (!this.getGAV().equals(object.getGAV())) {
            return false;
        }

        if (!StringUtils.equals(this.getClassifier(), object.getClassifier())) {
            return false;
        }
        return true;
    }
}