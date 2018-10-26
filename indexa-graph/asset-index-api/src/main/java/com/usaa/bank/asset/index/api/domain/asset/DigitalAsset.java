package com.usaa.bank.asset.index.api.domain.asset;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//============================================================================
// DigitalAssetReference is an artifact which hasnt been fully processed yet
//============================================================================

/**
 * DigitalAsset is the parent model from which the types of artifact indexed model are based from. All extending classes will have these information that classifies it as artifact.
 */
public class DigitalAsset implements IDigitalAsset, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FIELD_VALUE_DELIMITER = ",";
    public static final String KEY_GUID = "guid";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_DEPENDENCIES = "dependencies";
    public static final String KEY_DEPENDENTS = "dependents";
    private GUID guid;
    private Set<String> tagSet = new HashSet<String>();
    private Map<String, Set<String>> fieldMap = new HashMap<String, Set<String>>();
    private Set<GUID> dependencySet = new HashSet<GUID>();
    private Set<GUID> dependentSet = new HashSet<GUID>();

    /**
     * Default constructor is modified to have a GUID created by default.
     */
    public DigitalAsset() {
        super();
        this.guid = GUIDFactory.createGUID();
    }

    /**
     * Create a DigitalAsset instance with the given guid.
     *
     * @param guid - Guid of an asset
     */
    public DigitalAsset(GUID guid) {
        super();
        this.guid = guid;
    }

    @Override
    public GUID getGUID() {
        return this.guid;
    }

    // =============================================
    // Tag Methods
    // =============================================
    @Override
    public Set<String> getTags() {
        return Collections.unmodifiableSet(this.tagSet);
    }

    @Override
    public void addTag(String tag) {
        if (StringUtils.isNotEmpty(tag)) {
            this.tagSet.add(tag);
        }
    }

    @Override
    public void addTags(Set<String> tags) {
        if (tags != null) {
            this.tagSet.addAll(tags);
        }
    }

    @Override
    public void removeTag(String tag) {
        this.tagSet.remove(tag);
    }

    @Override
    public void clearTags() {
        this.tagSet.clear();
    }

    // =============================================
    // Field Methods
    // =============================================
    @Override
    public Map<String, Set<String>> getFields() {
        return Collections.unmodifiableMap(this.fieldMap);
    }

    @Override
    public String getFieldValue(String key) {
        StringBuffer strBuf = new StringBuffer();
        ;
        if (this.fieldMap != null) {
            Set<String> fieldValues = this.fieldMap.get(key);
            if (fieldValues != null && fieldValues.size() > 0) {
                Iterator<String> iterator = fieldValues.iterator();
                strBuf.append(iterator.next());
                while (iterator.hasNext()) {
                    strBuf.append(DigitalAsset.FIELD_VALUE_DELIMITER);
                    strBuf.append(iterator.next());
                }
            }
        }
        return strBuf.toString();
    }

    @Override
    public Set<String> getFieldValues(String key) {
        if (this.fieldMap != null) {
            return this.fieldMap.get(key);
        }
        return null;
    }

    @Override
    public void addField(String key, String value) {
        if (this.fieldMap != null) {
            Set<String> valueSet = this.fieldMap.get(key);
            if (valueSet == null) {
                valueSet = new HashSet<String>();
            }
            valueSet.add(value);
            this.fieldMap.put(key, valueSet);
        }
    }

    @Override
    public void addField(String key, Set<String> values) {
        if (this.fieldMap != null) {
            Set<String> valueSet = this.fieldMap.get(key);
            if (valueSet == null) {
                valueSet = new HashSet<String>();
            }
            valueSet.addAll(values);

            this.fieldMap.put(key, valueSet);
        }
    }

    @Override
    public void addFields(Map<String, Set<String>> fields) {
        if (fields != null && fields.size() > 0) {
            for (String key : fields.keySet()) {
                this.addField(key, fields.get(key));
            }
        }
    }

    @Override
    public void addMapFields(Map<String, String> fields) {
        if (fields != null && fields.size() > 0) {
            for (String key : fields.keySet()) {
                this.addField(key, fields.get(key));
            }
        }
    }


    @Override
    public void removeField(String key) {
        this.fieldMap.remove(key);
    }

    @Override
    public void clearFields() {
        this.fieldMap.clear();
    }

    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================

    /**
     * Utility method that gets the first value for the field from the given fieldMap. A field has a Set collection as its value, we return the first entry present in the Set.
     *
     * @param field    Field to be searched for
     * @param fieldMap A Map containing all the fields present in an asset
     * @return First value associated with field
     */
    public static String getFirstValueString(String field, Map<String, Set<String>> fieldMap) {
        String value = "";
        if (fieldMap != null && fieldMap.size() > 0) {
            for (String fieldValue : fieldMap.get(field)) {
                value = fieldValue;
                break;
            }
        }
        return value;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        builder.append(this.getGUID());
        return builder.toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj.getClass().isAssignableFrom(this.getClass()))) {
            return false;
        }
        DigitalAsset that = (DigitalAsset) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getGUID(), that.getGUID());
        return builder.isEquals();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}