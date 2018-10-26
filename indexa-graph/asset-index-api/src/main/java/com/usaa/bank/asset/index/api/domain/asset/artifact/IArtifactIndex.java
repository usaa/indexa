package com.usaa.bank.asset.index.api.domain.asset.artifact;

import com.usaa.bank.asset.index.api.domain.asset.IAssetIndex;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactId;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.gav.GroupId;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;

import java.util.Map;
import java.util.Set;

/**
 * This interface provides the possible interaction with the artifact's lucene-indexes.
 */
public interface IArtifactIndex extends IAssetIndex<IArtifact> {

    /**
     * Creates an artifact-asset from the given data. Each will data will be created as an entry in the lucene-indexes.
     *
     * @param hash         - hash of artifact
     * @param artifactPath - artifactPath of artifact
     * @param groupId      - groupId of artifact
     * @param artifactId   - artifactId of artifact
     * @param version      - gav of artifact
     * @param artifactType - artifactType of artifact
     * @param classifier   - classifier of artifact
     * @param tags         - tags of artifact
     * @param fields       - fields of artifact
     * @return - The artifact created
     */
    IArtifact create(
            String hash,
            String artifactPath,
            GroupId groupId,
            ArtifactId artifactId,
            Version version,
            ArtifactType artifactType,
            String classifier,
            Set<String> tags,
            Map<String, Set<String>> fields);

    /**
     * Removes the given hash and artifactPath from the lucene-indexes. A field is just a Map with hash and the artifactPath as its entries with its corresponding predefined key. <br>
     * Finally the "field" created will be then deleted from the lucene-index and the document will be updated.
     *
     * @param hash         - Hash to be removed
     * @param artifactPath - ArtifactPath to be removed
     * @return - True if the deletion was successful.
     */
    boolean remove(String hash, String artifactPath);

    /**
     * Removes the given hash from the lucene-documents. A field is just a Map with hash as it's entry with the corresponding predefined key. <br>
     * Finally the "field" created will be then deleted from the lucene-index and the document will be updated.
     *
     * @param hash - Hash value
     * @return - True if the deletion was successful.
     */
    boolean remove(String hash);

    /**
     * Searches for artifacts using the hash and artifactPath from the lucene-indexes. A field is just a Map with hash and the artifactPath as its entries with its corresponding predefined key. <br>
     * Finally the "field" created will be searched from all the lucene-index. Out of the matching documents only the first one will be parsed to an IArtifact object.
     *
     * @param hash         - hash added to search query
     * @param artifactPath - ArtifactPath added to search query
     * @return - an artifact matched
     */
    //Unfortunately we will not guarantee uniqueness by hash.
    IArtifact get(String hash, String artifactPath);

    /**
     * Searches for artifacts using the artifactPath from the lucene-indexes associated with the luceneIndexerDao. A field is just a Map with artifactPath as its entry with its corresponding predefined key. <br>
     * Finally the "field" created will be searched from all the lucene-index. Out of the matching documents only the first one will be parsed to an IArtifact object.
     *
     * @param artifactPath - ArtifactPath added to search query
     * @return - an artifact matched
     */
    IArtifact get(String artifactPath);

    /**
     * Finds artifacts from given hash from the lucene-indexes associated with the luceneIndexerDao. A field is created, it is a Map with artifactPath as its entry with its corresponding predefined key. <br>
     * Finally the "field" created will be searched from all the lucene-index, matching documents will be then parsed to IArtifact objects.
     *
     * @param hash - hash added to search query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> find(String hash);

    /**
     * Finds artifacts from given groupId from the lucene-indexes associated with the luceneIndexerDao. A field is created, it is a Map with groupId as its entry with its corresponding predefined key. <br>
     * Finally the "field" created will be searched from all the lucene-index, matching documents will be then parsed to IArtifact objects.
     *
     * @param groupId - GroupId added to search query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> find(GroupId groupId);

    /**
     * Finds artifacts from given groupId, artifactId matching from the lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao.
     * A field is created, it is a Map with groupId, artifactId as its entries with its corresponding predefined key. <br>
     * Finally the "field" created will be searched from all the lucene-index, matching documents will be then parsed to IArtifact objects.
     *
     * @param groupId    - GroupId added to search query
     * @param artifactId - ArtifactId added to search query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> find(GroupId groupId, ArtifactId artifactId);

    /**
     * Finds artifacts from given groupId, artifactId, gav matching from the lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao.
     * A field is created, it is a Map with groupId, artifactId, gav as its entries with its corresponding predefined key. <br>
     * Finally the "field" created will be searched from all the lucene-index, matching documents will be then parsed to IArtifact objects.
     *
     * @param groupId    - GroupId added to search query
     * @param artifactId - ArtifactId added to search query
     * @param version    - Version to be searhced
     * @return - Set of artifacts matched
     */
    Set<IArtifact> find(GroupId groupId, ArtifactId artifactId, Version version);

    /**
     * Finds artifacts from given groupId, artifactId, gav and classifier matching from the lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao.
     * A field is created, it is a Map with groupId, artifactId, gav and classifier as its entries with its corresponding predefined key. <br>
     * Finally the "field" created will be searched from all the lucene-index, matching documents will be then parsed to IArtifact objects.
     *
     * @param groupId    - GroupId added to search query
     * @param artifactId - ArtifactId to be searched
     * @param version    - Version to be searhced
     * @param classifier - Classifier added to search query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> find(GroupId groupId, ArtifactId artifactId, Version version, String classifier);

    /**
     * Finds the given artifactType from the lucene-indexes associated with the lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao.
     * A field is created, it is a Map with artifactType as its entry with its corresponding predefined key. <br>
     * Finally the "field" created will be searched from all the lucene-index, matching documents will be then parsed to IArtifact objects.
     *
     * @param artifactType - ArtifactType added to search query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> find(ArtifactType artifactType);


}
