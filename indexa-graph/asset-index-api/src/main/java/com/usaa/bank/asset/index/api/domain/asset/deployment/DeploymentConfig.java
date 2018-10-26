package com.usaa.bank.asset.index.api.domain.asset.deployment;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * DeploymentConfig is an model that represent the DeploymentConfig asset and also contains the metadata associated with it.
 */
public class DeploymentConfig extends DigitalAsset implements IDeploymentConfig, Serializable {

    public static final String GUID_DELIMITER = ":";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";

    /**
     * Create a DeploymentConfig instance with tags and field. Tags implies the various tags associated with this deployment-config.
     * The map of fields contains all the fields present in the deployment-config. Guid will be created from fields of map for this instance.
     *
     * @param tags   - tags of the DeploymentConfig
     * @param fields - Fields of DeploymentConfig
     */
    public DeploymentConfig(Set<String> tags, Map<String, Set<String>> fields) {
        super(DeploymentConfig.createGUID(
                DigitalAsset.getFirstValueString(DeploymentConfig.KEY_ID, fields),
                DigitalAsset.getFirstValueString(DeploymentConfig.KEY_NAME, fields)));
        this.addTags(tags);
        this.addFields(fields);
    }

    /**
     * Create a DeploymentConfig instance with id and name of deployment-config. Guid will be created from id and name for this instance.
     *
     * @param id   - id of deployment-config
     * @param name - name of deployment-config
     */
    public DeploymentConfig(String id, String name) {
        super(DeploymentConfig.createGUID(id, name));
        this.setId(id);
        this.setName(name);
    }

    /**
     * Create a DeploymentConfig instance with guid, id and name of deployment-config .
     *
     * @param guid - Guid of deployment-config
     * @param id   - id of deployment-config
     * @param name - name of deployment-conf
     */
    protected DeploymentConfig(GUID guid, String id, String name) {
        super(guid);
        this.setId(id);
        this.setName(name);
    }

    /**
     * Obtain the id associated with a deployment config.
     *
     * @return Id of deployment-config.
     */
    public String getId() {
        return this.getFieldValue(DeploymentConfig.KEY_ID);
    }

    /**
     * Get the id linked with the deployment-config.
     *
     * @return - String value of id. Empty string will be returned if empty id.
     */
    public String getIdString() {
        if (this.getId() != null) {
            return this.getId();
        }
        return "";
    }

    /**
     * Assign a id to be associated with deployment-config.
     *
     * @param id - id to be set
     */
    private void setId(String id) {
        this.addField(DeploymentConfig.KEY_ID, id);
    }

    public String getName() {
        return this.getFieldValue(DeploymentConfig.KEY_NAME);
    }

    /**
     * Retrieves the name associated with deployment-config
     *
     * @return name of deployment-config
     */
    public String getNameString() {
        if (this.getName() != null) {
            return this.getName();
        }
        return "";
    }

    /**
     * Assign a name for a particular deployment-config
     *
     * @param name - name to be set
     */
    private void setName(String name) {
        this.addField(DeploymentConfig.KEY_NAME, name);
    }

    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================

    /**
     * Crates a GUID for deployment-config from the given id and name
     *
     * @param id   - id of deployment-config
     * @param name - name of deployment-config
     * @return - guid created for deployment-config
     */
    public static GUID createGUID(String id, String name) {
        GUID guid = null;
        if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(name)) {
            guid = GUIDFactory.createGUID(id + DeploymentConfig.GUID_DELIMITER + name);
        }
        return guid;
    }

    /**
     * Crates a GUID for deployment-config from various values of the fields.
     *
     * @param fields - Fields of DeploymentConfig
     * @return - guid created for deployment-config
     */
    public static GUID createGUID(Map<String, Set<String>> fields) {
        try {
            if (fields.containsKey(DeploymentConfig.KEY_GUID)) {
                return GUIDFactory.passGUID(new ArrayList<>(fields.get(DeploymentConfig.KEY_GUID)).get(0));
            } else {
                return createGUID(new ArrayList<>(fields.get(DeploymentConfig.KEY_ID)).get(0), new ArrayList<>(fields.get(DeploymentConfig.KEY_NAME)).get(0));
            }
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
