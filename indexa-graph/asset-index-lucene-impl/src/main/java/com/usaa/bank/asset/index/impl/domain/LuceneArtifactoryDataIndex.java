package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.artifact.Artifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifactoryDataIndex;
import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactId;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.gav.GAV;
import com.usaa.bank.asset.index.api.domain.asset.gav.GroupId;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import com.usaa.bank.asset.index.impl.util.ArtifactBuilder;
import com.usaa.bank.asset.index.impl.util.LuceneIndexUtil;
import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import com.usaa.bank.graph.common.validate.BasicValidator;
import org.apache.lucene.document.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is an implementation to modify the lucene-indexes associated with the artifactory-data.<br>
 * This implements the interface IArtifactoryDataIndex which outlines the specs.
 */
public class LuceneArtifactoryDataIndex extends RealtimeLuceneIndex implements IArtifactoryDataIndex {

    public static final String DEFAULT_INDEX_FILE_PATH = ".artifactory.data.index";
    private static final String[] keys = {Artifact.KEY_GUID, Artifact.KEY_ARTIFACT_ID, Artifact.KEY_HASH,
            Artifact.KEY_ARTIFACT_PATH, Artifact.KEY_GROUP_ID, Artifact.KEY_VERSION, Artifact.KEY_CLASSIFIER};
    private static final int MAX_RESULTS = 10;

    public LuceneArtifactoryDataIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }


    @Override
    public IArtifact create(Set<String> tags, Map<String, Set<String>> fields) {
        BasicValidator.validateNotNull("fields", fields);
        try {
            Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);
            String guid = GUIDFactory.passGUID(DigitalAsset.getFirstValueString(Artifact.KEY_GUID, fields)).toString();
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
            String classifier
    ) {
        BasicValidator.validateNotNull("hash", hash);
        BasicValidator.validateNotNull("artifactPath", artifactPath);
        IArtifact artifact = new Artifact(hash, artifactPath, new GAV(groupId, artifactId, version, ArtifactType.COMPONENT), classifier);

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
    public IArtifact get(String hash) {

        BasicValidator.validateNotNull("hash", hash);
        IArtifact digitalAsset = null;
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(Artifact.KEY_HASH, hash);
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        if (documents != null) {
            for (Document document : documents) {
                digitalAsset = ArtifactBuilder.createArtifact(document);
                break;
            }
        }
        return digitalAsset;
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
    public Set<IArtifact> find() {
        return ArtifactBuilder.createArtifacts(this.getLuceneIndexDAO().findDocuments(null));
    }

    @Override
    public int getIndexSize() {
        return this.getLuceneIndexDAO().getDocumentCount();
    }

}

