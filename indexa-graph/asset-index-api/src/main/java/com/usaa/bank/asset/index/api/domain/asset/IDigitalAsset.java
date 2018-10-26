package com.usaa.bank.asset.index.api.domain.asset;

import java.util.Map;
import java.util.Set;

/**
 * Implementing this interface allows the implemented class to be considered as an variation of IDigitalAsset.
 */
public interface IDigitalAsset extends IIdentifiableAsset
{


    /**
     * Get the tags for the asset. Tags are represented in a Set dat structure.
     *
     * @return - Tags data
     */
    Set<String> getTags();

    /**
     * Add this "tag" to the existing tags
     *
     * @param tag - tag to be added
     */
    void addTag(String tag);

    /**
     * Add the set of Tags to the Set collection
     *
     * @param tags - Tags to added
     */
    void addTags(Set<String> tags);

    /**
     * Removes a particular Tag from the Set collection
     *
     * @param tag - Tag to be removed
     */
    void removeTag(String tag);

    /**
     * Clears the data present in the Tag Set collection.
     */
    void clearTags();

    /**
     * Get the fields associated with the asset. Fields represent various key:value present in each assets.
     *
     * @return - Fields of an asset
     */
    Map<String, Set<String>> getFields();

    /**
     * Get a particular field value. The field-Map's key has a Set Collection as its value, function returns one of value from set.
     *
     * @param key - key to be searched
     * @return - Set of values associated with the field
     */
    String getFieldValue(String key);

    /**
     * Get a particular field's data-set.
     *
     * @param key - Field key to be searched
     * @return - Value Set for the field
     */
    Set<String> getFieldValues(String key);

    /**
     * Add an entry to the field data. If the key is already present the value will be added to the existing set of values associated.
     *
     * @param key   - key of the field
     * @param value - Value of the field
     */
    void addField(String key, String value);

    /**
     * Add the key and values to the Field Map.
     *
     * @param key    - Field's name
     * @param values - Field's values
     */
    void addField(String key, Set<String> values);

    /**
     * Add the fields to the asset.
     *
     * @param fields - Field data
     */
    void addFields(Map<String, Set<String>> fields);

    // New
    void addMapFields(Map<String, String> fields);

    /**
     * Remove a particular data associated with the Feild Map.
     *
     * @param key - Field to be removed
     */
    void removeField(String key);

    /**
     * Clears the data present in the Field's Map data structure.
     */
    void clearFields();


}