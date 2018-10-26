package com.usaa.bank.asset.index.api.domain.asset.shared.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;

/**
 * SharedLibrary  is a model that represent the shared-library asset type and contains meta-data associated with each shared library.
 */
public class SharedLibrary extends DigitalAsset implements ISharedLibrary, Serializable {
    private static final long serialVersionUID = 1L;
    public static final String GUID_DELIMITER = ":";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";

    /**
     * Creates a SharedLibrary instance from the given tags and fields.
     *
     * @param tags   - Tags of the shared library
     * @param fields - Fields of the shared library
     */
    public SharedLibrary(Set<String> tags, Map<String, Set<String>> fields) {
        super(SharedLibrary.createGUID(
                DigitalAsset.getFirstValueString(SharedLibrary.KEY_ID, fields),
                DigitalAsset.getFirstValueString(SharedLibrary.KEY_NAME, fields)));
        this.addTags(tags);
        this.addFields(fields);
    }

    /**
     * Creates a SharedLibrary instance from the given id and name.
     *
     * @param id   - Id of shared library
     * @param name - name of shared library
     */
    public SharedLibrary(String id, String name) {
        super(SharedLibrary.createGUID(id, name));
        this.setId(id);
        this.setName(name);
    }

    /**
     * @param guid - Guid of shared library
     * @param id   - Id of shared library
     * @param name - name of shared library
     */
    protected SharedLibrary(GUID guid, String id, String name) {
        super(guid);
        this.setId(id);
        this.setName(name);
    }

    /**
     * Get the id of the shared library.
     *
     * @return - Id value
     */
    public String getId() {
        return this.getFieldValue(SharedLibrary.KEY_ID);
    }

    /**
     * Get the id of the shared library.
     *
     * @return - Id value
     */
    public String getIdString() {
        if (this.getId() != null) {
            return this.getId();
        }
        return "";
    }

    /**
     * Sets the Id for an shared library
     *
     * @param id - Id to be set
     */
    private void setId(String id) {
        this.addField(SharedLibrary.KEY_ID, id);
    }

    /**
     * Get the name of the shared library
     *
     * @return - name of shared library
     */
    public String getName() {
        return this.getFieldValue(SharedLibrary.KEY_NAME);
    }

    /**
     * Get the name of the shared library. Returns empty string in-case no name is present.
     *
     * @return - name of shared library
     */
    public String getNameString() {
        if (this.getName() != null) {
            return this.getName();
        }
        return "";
    }

    /**
     * Set name of the shared library
     *
     * @param name - Name of the shared library
     */
    private void setName(String name) {
        this.addField(SharedLibrary.KEY_NAME, name);
    }

    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================

    /**
     * Create a GUID for the shared library using id and name.
     *
     * @param id   - Id of shared library
     * @param name - Name of shared library
     * @return - Guid object
     */
    public static GUID createGUID(String id, String name) {
        GUID guid = null;
        if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(name)) {
            guid = GUIDFactory.createGUID(id + SharedLibrary.GUID_DELIMITER + name);
        }
        return guid;
    }

    /**
     * Create a GUID for the shared library using fields data.
     *
     * @param fields - Various fields present in the shared library
     * @return - Guid object
     */
    public static GUID createGUID(Map<String, Set<String>> fields) {
        try {
            if (fields.containsKey(SharedLibrary.KEY_GUID)) {
                return GUIDFactory.createGUID(new ArrayList<>(fields.get(SharedLibrary.KEY_GUID)).get(0));
            } else {
                return createGUID(new ArrayList<>(fields.get(SharedLibrary.KEY_ID)).get(0), new ArrayList<>(fields.get(SharedLibrary.KEY_NAME)).get(0));
            }
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}