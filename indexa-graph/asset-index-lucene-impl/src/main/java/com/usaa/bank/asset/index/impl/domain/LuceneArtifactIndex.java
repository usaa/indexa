package com.usaa.bank.asset.index.impl.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.asset.artifact.Artifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifactIndex;

import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactId;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.gav.GAV;
import com.usaa.bank.asset.index.api.domain.asset.gav.GroupId;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import com.usaa.bank.asset.index.impl.util.LuceneIndexUtil;
import org.apache.lucene.document.Document;

import com.usaa.bank.asset.index.impl.util.ArtifactBuilder;
import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * This class is an implementation to modify the lucene-
 * indexes associated with the artifactory-data.<br>
 * This implements the interface IArtifactoryDataIndex which outlines the specs.
 */

public class LuceneArtifactIndex extends RealtimeLuceneIndex implements IArtifactIndex {

    public static final String DEFAULT_INDEX_FILE_PATH = ".artifact.index";
    private static final String[] keys = {Artifact.KEY_GUID, Artifact.KEY_ARTIFACT_ID, Artifact.KEY_HASH,
            Artifact.KEY_ARTIFACT_PATH, Artifact.KEY_GROUP_ID, Artifact.KEY_VERSION, Artifact.KEY_ARTIFACT_TYPE, Artifact.KEY_CLASSIFIER};

    /**
     * Creates a LuceneArtifactIndex instance for the given lucene-index-dao, it contains the lucene-indexes. Using the luceneIndexDAO user can perform various tasks with the lucene-documents.
     *
     * @param luceneIndexDAO - A luceneIndexDAO
     */
    public LuceneArtifactIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }

    @Override
    public IArtifact create(Set<String> tags, Map<String, Set<String>> fields) {
        BasicValidator.validateNotNull("fields", fields);
        try {
            Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);
            String guid = GUIDFactory.createGUID(DigitalAsset.getFirstValueString(Artifact.KEY_GUID, fields)).toString();
            Document doc = DocumentBuilder.createDocument(guid, tags, fields);
            this.getLuceneIndexDAO().saveDocument(Artifact.KEY_GUID, guid, doc);
            return ArtifactBuilder.createArtifact(doc);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public IArtifact create(
            String hash,
            String artifactPath,
            GroupId groupId,
            ArtifactId artifactId,
            Version version,
            ArtifactType artifactType,
            String classifier,
            Set<String> tags,
            Map<String, Set<String>> fields) {
        BasicValidator.validateNotNull("hash", hash);
        BasicValidator.validateNotNull("artifactPath", artifactPath);
        IArtifact artifact = new Artifact(hash, artifactPath, new GAV(groupId, artifactId, version, artifactType), classifier);
        artifact.addTags(tags);
        artifact.addFields(fields);

        if (this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, artifact.getGUID().getStringValue(), DocumentBuilder.createDocument(artifact))) {
            return artifact;
        }
        return null;
    }

    @Override
    public boolean save(IArtifact digitalAsset) {
        BasicValidator.validateNotNull("digitalAsset", digitalAsset);
        BasicValidator.validateNotNull("guid", digitalAsset.getGUID());
        return this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, digitalAsset.getGUID().getStringValue(), DocumentBuilder.createDocument(digitalAsset));
    }

    @Override
    public boolean remove(GUID guid) {
        return LuceneIndexUtil.remove(getLuceneIndexDAO(),guid);
    }

    @Override
    public IArtifact get(GUID guid) {
        return LuceneIndexUtil.get(getLuceneIndexDAO(),guid);
    }

    @Override
    public Set<IArtifact> find(String fieldKey, String fieldValue) {
        return LuceneIndexUtil.find(getLuceneIndexDAO(),fieldKey,fieldValue);
    }

    @Override
    public Set<IArtifact> find(Map<String, String> fields) {
        return LuceneIndexUtil.find(getLuceneIndexDAO(),fields);
    }
    @Override
    public boolean remove(String hash, String artifactPath) {
        BasicValidator.validateNotNull("hash", hash);
        BasicValidator.validateNotNull("artifactPath", artifactPath);

        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_HASH, hash);
        fields.put(Artifact.KEY_ARTIFACT_PATH, artifactPath);

        this.getLuceneIndexDAO().deleteDocuments(fields);
        return true;
    }

    @Override
    public boolean remove(String hash) {
        BasicValidator.validateNotNull("hash", hash);

        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_HASH, hash);

        this.getLuceneIndexDAO().deleteDocuments(fields);
        return true;
    }

    @Override
    public IArtifact get(String hash, String artifactPath) {
        BasicValidator.validateNotNull("hash", hash);
        BasicValidator.validateNotNull("artifactPath", artifactPath);

        IArtifact artifact = null;
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_HASH, hash);
        fields.put(Artifact.KEY_ARTIFACT_PATH, artifactPath);

        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        if (documents != null) {
            for (Document document : documents) {
                artifact = ArtifactBuilder.createArtifact(document);
                break;
            }
        }
        return artifact;
    }

    @Override
    public IArtifact get(String artifactPath) {
        BasicValidator.validateNotNull("artifactPath", artifactPath);

        IArtifact artifact = null;
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_ARTIFACT_PATH, artifactPath);

        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        if (documents != null) {
            for (Document document : documents) {
                artifact = ArtifactBuilder.createArtifact(document);
                break;
            }
        }
        return artifact;
    }

    @Override
    public Set<IArtifact> find(String hash) {
        BasicValidator.validateNotNull("hash", hash);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_HASH, hash);
        return ArtifactBuilder.createArtifacts(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<IArtifact> find(GroupId groupId) {
        BasicValidator.validateNotNull("groupId", groupId);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_GROUP_ID, groupId.toString());
        return ArtifactBuilder.createArtifacts(this.getLuceneIndexDAO().findDocuments(fields));
    }


    @Override
    public Set<IArtifact> find(GroupId groupId, ArtifactId artifactId) {
        BasicValidator.validateNotNull("groupId", groupId);
        BasicValidator.validateNotNull("artifactId", artifactId);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_GROUP_ID, groupId.toString());
        fields.put(Artifact.KEY_ARTIFACT_ID, artifactId.toString());
        return ArtifactBuilder.createArtifacts(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<IArtifact> find(GroupId groupId, ArtifactId artifactId, Version version) {
        BasicValidator.validateNotNull("groupId", groupId);
        BasicValidator.validateNotNull("artifactId", artifactId);
        BasicValidator.validateNotNull("version", version);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_GROUP_ID, groupId.toString());
        fields.put(Artifact.KEY_ARTIFACT_ID, artifactId.toString());
        fields.put(Artifact.KEY_VERSION, version.toString());
        return ArtifactBuilder.createArtifacts(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<IArtifact> find(GroupId groupId, ArtifactId artifactId, Version version, String classifier) {
        BasicValidator.validateNotNull("groupId", groupId);
        BasicValidator.validateNotNull("artifactId", artifactId);
        BasicValidator.validateNotNull("version", version);
        BasicValidator.validateNotNull("classifier", classifier);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_GROUP_ID, groupId.toString());
        fields.put(Artifact.KEY_ARTIFACT_ID, artifactId.toString());
        fields.put(Artifact.KEY_VERSION, version.toString());
        fields.put(Artifact.KEY_CLASSIFIER, classifier);
        return ArtifactBuilder.createArtifacts(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<IArtifact> find(ArtifactType artifactType) {
        BasicValidator.validateNotNull("artifactType", artifactType);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_ARTIFACT_TYPE, artifactType.toString());
        return ArtifactBuilder.createArtifacts(this.getLuceneIndexDAO().findDocuments(fields));
    }


    @Override
    public Set<IArtifact> find() {
        return ArtifactBuilder.createArtifacts(this.getLuceneIndexDAO().findDocuments(null));
    }

    @Override
    public int getIndexSize() {
        return this.getLuceneIndexDAO().getDocumentCount();
    }

}

