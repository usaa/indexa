package com.usaa.bank.asset.index.impl.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.artifact.Artifact;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactId;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.gav.GroupId;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import com.usaa.bank.graph.common.identity.GUID;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;

public class LuceneArtifactIndexTest {
    private LuceneArtifactIndex artifactIndex = null;
    private static final String hash = "hash1234";
    private static final String artifactPath = "www/bank/1234/";
    private static final GroupId groupId = new GroupId("groupId1234");
    private static final ArtifactId artifactId = new ArtifactId("artId1234");
    private static final Version version = new Version("version1234");
    private static final String classifier = "class1234";
    private static final ArtifactType artifactType = ArtifactType.COMPONENT;
    private static final Set<String> tags = new HashSet<String>();
    private static final Map<String, Set<String>> fields = new HashMap<String, Set<String>>();


    @Before
    public void runOnceBeforeClass() {
        this.artifactIndex = new LuceneArtifactIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
    }

    @Test
    public void EmptyLuceneCreationTest() {
        this.artifactIndex.openSession();
        checkCorrectness(createArtifact());
        this.artifactIndex.closeSession();
    }

    @Test
    public void LuceneCreationWithFieldsTest() {
        this.artifactIndex.openSession();
        checkCorrectness(this.artifactIndex.create(null, createFields()));
        this.artifactIndex.closeSession();
    }

    @Test
    public void FindArtifactByGroupIdArtifactIdVersionArtifactTypeClassifier() {
        this.artifactIndex.openSession();
//		this.artifactIndex.beginTransaction();
        createArtifact();
        this.artifactIndex.flushSession();
//		this.artifactIndex.commitTransaction();
        checkCorrectness(this.artifactIndex.get(hash, artifactPath));
        this.artifactIndex.closeSession();
    }

    @Test
    public void FindArtifactByGroupIdArtifactIdVersion() {
        this.artifactIndex.openSession();
        createArtifact();
        this.artifactIndex.flushSession();
        ArrayList<IArtifact> list = new ArrayList<IArtifact>(this.artifactIndex.find(groupId, artifactId, version));
        checkCorrectness(list.get(0));
        this.artifactIndex.closeSession();
    }

    @Test
    public void FindArtifactByGroupIdArtifactId() {
        this.artifactIndex.openSession();
        createArtifact();
        this.artifactIndex.flushSession();
        ArrayList<IArtifact> list = new ArrayList<IArtifact>(this.artifactIndex.find(groupId, artifactId));
        checkCorrectness(list.get(0));
        this.artifactIndex.closeSession();
    }

    @Test
    public void FindArtifactByGroupId() {
        this.artifactIndex.openSession();
        createArtifact();
        this.artifactIndex.flushSession();
        ArrayList<IArtifact> list = new ArrayList<IArtifact>(this.artifactIndex.find(groupId));
        checkCorrectness(list.get(0));
        this.artifactIndex.closeSession();
    }

    @Test
    public void FindArtifactByArtifactType() {
        this.artifactIndex.openSession();
        createArtifact();
        this.artifactIndex.flushSession();
        ArrayList<IArtifact> list = new ArrayList<IArtifact>(this.artifactIndex.find(artifactType));
        checkCorrectness(list.get(0));
        this.artifactIndex.closeSession();
    }

    @Test
    public void LuceneCreationTestSameArtifact() {
        this.artifactIndex.openSession();
        IArtifact artifact1 = this.artifactIndex.create(hash, artifactPath, groupId, artifactId, version, artifactType, classifier, tags, fields);
        this.artifactIndex.flushSession();
        IArtifact artifact2 = this.artifactIndex.create(hash, artifactPath, groupId, artifactId, version, artifactType, classifier, tags, fields);
        this.artifactIndex.flushSession();
        boolean isEqual = artifact1.equals(artifact2);
        assertTrue(isEqual);
        this.artifactIndex.closeSession();
    }

    @Test
    public void LuceneRemoveArtifactBooleanCheck() {
        this.artifactIndex.openSession();
        this.artifactIndex.create(hash, artifactPath, groupId, artifactId, version, artifactType, classifier, tags, fields);
        boolean success = this.artifactIndex.remove(hash, artifactPath);
        assertTrue(success);
        this.artifactIndex.closeSession();
    }

    @Test
    public void LuceneRemoveArtifactByHashBooleanCheck() {
        this.artifactIndex.openSession();
        this.artifactIndex.create(hash, artifactPath, groupId, artifactId, version, artifactType, classifier, tags, fields);
        this.artifactIndex.closeSession();
        this.artifactIndex.openSession();
        boolean success = this.artifactIndex.remove(hash, artifactPath);
        this.artifactIndex.closeSession();
        assertTrue(success);
    }

    @Test
    public void LuceneRemoveArtifactByHashDeepCheck() {
        IArtifact artifact = null;
        this.artifactIndex.openSession();
        this.artifactIndex.create(hash, artifactPath, groupId, artifactId, version, artifactType, classifier, tags, fields);
        this.artifactIndex.closeSession();

        this.artifactIndex.openSession();
        this.artifactIndex.remove(hash, artifactPath);
        this.artifactIndex.closeSession();

        this.artifactIndex.openSession();
        artifact = this.artifactIndex.get(hash, artifactPath);
        assertNull(artifact);
        this.artifactIndex.closeSession();
    }

    @Test
    public void removeArtifactByGUID() {
        this.artifactIndex.openSession();
        this.artifactIndex.create(hash, artifactPath, groupId, artifactId, version, artifactType, classifier, tags, fields);
        this.artifactIndex.closeSession();
        this.artifactIndex.openSession();
        boolean success = this.artifactIndex.remove(GUIDFactory.createGUID(hash + ":" + artifactPath));
        this.artifactIndex.closeSession();
        assertTrue(success);
    }

    @Test
    public void getArtifactByArtifactPath() {
        this.artifactIndex.openSession();
        createArtifact();
        this.artifactIndex.flushSession();
        checkCorrectness(this.artifactIndex.get(artifactPath));
        this.artifactIndex.closeSession();
    }

    @Test
    public void findAllDocuments() {
        this.artifactIndex.openSession();
        createArtifact();
        this.artifactIndex.flushSession();
        // 1 is the expected from the size
        Assert.assertEquals(1, this.artifactIndex.find().size());
        this.artifactIndex.closeSession();
    }


    // ================================================
    // Utility Methods
    // ================================================
    private Map<String, Set<String>> createFields() {
        Map<String, Set<String>> fields = new HashMap<>();
        Set<String> value = new HashSet<>();
        value.add(hash);
        fields.put(Artifact.KEY_HASH, value);
        value = new HashSet<>();
        value.add(artifactPath);
        fields.put(Artifact.KEY_ARTIFACT_PATH, value);
        value = new HashSet<>();
        value.add(groupId.toString());
        fields.put(Artifact.KEY_GROUP_ID, value);
        value = new HashSet<>();
        value.add(GUIDFactory.createGUID(hash).toString());
        fields.put(Artifact.KEY_GUID, value);
        value = new HashSet<>();
        value.add(artifactId.getStringValue());
        fields.put(Artifact.KEY_ARTIFACT_ID, value);
        value = new HashSet<>();
        value.add(version.toString());
        fields.put(Artifact.KEY_VERSION, value);
        value = new HashSet<>();
        value.add(artifactType.toString());
        fields.put(Artifact.KEY_ARTIFACT_TYPE, value);
        value = new HashSet<>();
        value.add(classifier);
        fields.put(Artifact.KEY_CLASSIFIER, value);
        return fields;
    }

    private IArtifact createArtifact() {
        IArtifact artifact = null;
        artifact = this.artifactIndex.create(hash, artifactPath, groupId, artifactId, version, artifactType, classifier, tags, fields);
        return artifact;
    }

    private void checkCorrectness(IArtifact artifact) {
        assertEquals(
                String.format("%s:%s:%s:%s:%s:%s", hash, artifactPath, groupId.toString(), artifactId.getStringValue(), version.toString(), artifactType.toString()),
                artifact.getHash() + ":" + artifact.getArtifactPath() + ":" + artifact.getGAV().getStringValue());
    }

}