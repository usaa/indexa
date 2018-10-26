package com.usaa.bank.asset.index.api.domain.asset;

import java.util.Map;
import java.util.Set;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.IStatefulIndexerSession;

/**
 * This interface provides the basic capabilities of an asset's lucene-indexes.
 *
 * @param <T> - This describes the my type parameter
 */
public interface IAssetIndex<T extends IDigitalAsset> extends IStatefulIndexerSession {
    /**
     * Creates an asset from the given inputs.
     *
     * @param tags   - tasgs associated with the asset
     * @param fields - Fields associated with the asset
     * @return - Created Asset Object
     */
    T create(Set<String> tags, Map<String, Set<String>> fields);

    /**
     * Saves the given digitalAsset to the lucene-indexes. This fucntion will create new artifact from the given data. Finally the artifact is stored in the lucene-document.
     *
     * @param digitalAsset - Asset to be stored
     * @return - True if successful storage
     */
    boolean save(T digitalAsset);

    /**
     * Removes the given GUID from the lucene-documents. A field is just a Map with GUID as it's entry with the corresponding predefined key. <br>
     * Finally the "field" created will be then deleted from the lucene-index and the document will be updated.
     *
     * @param guid - guid to be removed
     * @return - True if the deletion was successful.
     */
    boolean remove(GUID guid);

    /**
     * Searches for GUID using the artifactPath from the lucene-indexes associated with the luceneIndexerDao. A field is just a Map with GUID as its entry with its corresponding predefined key. <br>
     * Finally the "field" created will be searched from all the lucene-index. Out of the matching documents only the first one will be parsed to an IArtifact object.
     *
     * @param guid - ArtifactPath added to search query
     * @return - an artifact matched
     */
    T get(GUID guid);

    /**
     * Obtains all the lucene-indexes present.
     *
     * @return - Set of artifact(s).
     */
    Set<T> find();

    /**
     * Finds  for certain  artifacts from the lucene-indexes associated with the luceneIndexerDao. A field is created, it is a Map with fieldKey as key and fieldValue as value. <br>
     * Finally the "field" created will be searched from all the lucene-index, matching documents will be then parsed to IArtifact objects.
     *
     * @param fieldKey   - Key to be used for search-query
     * @param fieldValue - value to be used for search-query
     * @return - Set of artifacts matched
     */
    Set<T> find(String fieldKey, String fieldValue);

    /**
     * Finds  for certain  artifacts from the lucene-indexes associated with the luceneIndexerDao.
     * Finally the "field" provided will be searched from all the lucene-index, matching documents will be then parsed to IArtifact objects.
     *
     * @param fields - Map used in search-query
     * @return - Set of artifacts matched
     */
    Set<T> find(Map<String, String> fields);

    /**
     * Get the number of lucene-documents associated with its luceneIndexerDao. Indexes are stored as documents in lucene-indexer.
     *
     * @return - Number of indexes
     */
    int getIndexSize();
}
