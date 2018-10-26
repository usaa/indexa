package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.artifact.Artifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactId;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.gav.GAV;
import com.usaa.bank.asset.index.api.domain.asset.gav.GroupId;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LuceneArtifactDataIndexTest {
    private LuceneArtifactoryDataIndex artifactIndex = null;
    private static final String hash = "hash1234";
    private static final String artifactPath = "www/bank/1234/";
    private static final GroupId groupId = new GroupId("groupId1234");
    private static final ArtifactId artifactId = new ArtifactId("artId1234");
    private static final Version version = new Version("version1234");
    private static final ArtifactType artifactType = ArtifactType.COMPONENT;
    private static final Map<String, Set<String>> fields = new HashMap<String, Set<String>>();
    private static final String classifier = "class1234";

    @Before
    public void runOnceBeforeClass() {
        this.artifactIndex = new LuceneArtifactoryDataIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
    }

    @Test
    public void EmptyLuceneCreationTest() {
        this.artifactIndex.openSession();
        checkCorrectness(createArtifactDataIndex());
        this.artifactIndex.closeSession();
    }

    @Test
    public void removeByGUID() {
        this.artifactIndex.openSession();
        createArtifactDataIndex();
        this.artifactIndex.closeSession();

        this.artifactIndex.openSession();
        boolean success = this.artifactIndex.remove(GUIDFactory.createGUID(hash + ":" + artifactPath));
        this.artifactIndex.closeSession();
        assertTrue(success);
    }

    @Test
    public void removeByHash() {
        this.artifactIndex.openSession();
        createArtifactDataIndex();
        this.artifactIndex.closeSession();

        this.artifactIndex.openSession();
        boolean success = this.artifactIndex.remove(hash);
        this.artifactIndex.closeSession();
        assertTrue(success);
    }

    @Test
    public void findAllDocuments() {
        this.artifactIndex.openSession();
        createArtifactDataIndex();
        this.artifactIndex.flushSession();

        // 1 is the expected from the size
        Assert.assertEquals(1, this.artifactIndex.find().size());
        this.artifactIndex.closeSession();
    }

    @Test
    public void saveAndDelteArtifact() {
        this.artifactIndex.openSession();
        IArtifact artifact = new Artifact(hash, artifactPath, new GAV(groupId, artifactId, version, ArtifactType.COMPONENT), classifier);
        boolean isSaved = this.artifactIndex.save(artifact);
        Assert.assertEquals(true, isSaved);
        this.artifactIndex.remove(artifact.getGUID());
        this.artifactIndex.flushSession();
        this.artifactIndex.closeSession();
    }


    @Test
    public void findTypeOne() {
        this.artifactIndex.openSession();
        createArtifactDataIndex();
        this.artifactIndex.flushSession();

        // 1 is the expected from the size
        Assert.assertEquals(1, this.artifactIndex.find(groupId).size());
        this.artifactIndex.closeSession();
    }


    @Test
    public void findTypeTwo() {
        this.artifactIndex.openSession();
        createArtifactDataIndex();
        this.artifactIndex.flushSession();

        // 1 is the expected from the size
        Assert.assertEquals(1, this.artifactIndex.find(groupId, artifactId).size());
        this.artifactIndex.closeSession();
    }

    @Test
    public void findTypeThree() {
        this.artifactIndex.openSession();
        createArtifactDataIndex();
        this.artifactIndex.flushSession();

        // 1 is the expected from the size
        Assert.assertEquals(1, this.artifactIndex.find(groupId, artifactId, version).size());
        this.artifactIndex.closeSession();
    }

    @Test
    public void findTypeFour() {
        this.artifactIndex.openSession();
        createArtifactDataIndex();
        this.artifactIndex.flushSession();

        // 1 is the expected from the size
        Assert.assertEquals(1, this.artifactIndex.find(groupId, artifactId, version, classifier).size());
        this.artifactIndex.closeSession();
    }

    @Test
    public void getArtifactByGUID() {
        this.artifactIndex.openSession();
        IArtifact artifact = createArtifactDataIndex();
        this.artifactIndex.flushSession();

        IArtifact retrievedArtifact = this.artifactIndex.get(artifact.getGUID());
        checkCorrectness(retrievedArtifact);
    }

    @Test
    public void getArtifactByHash() {
        this.artifactIndex.openSession();
        IArtifact artifact = createArtifactDataIndex();
        this.artifactIndex.flushSession();

        IArtifact retrievedArtifact = this.artifactIndex.get(artifact.getHash());

        checkCorrectness(retrievedArtifact);
    }

    @Test
    public void getArtifactBykeyValue() {
        this.artifactIndex.openSession();
        createArtifactDataIndex();
        this.artifactIndex.flushSession();

        Set<IArtifact> artifactSet = this.artifactIndex.find(Artifact.KEY_CLASSIFIER, classifier);
        Assert.assertEquals(1, artifactSet.size());
    }

    /*
     * Utility Methods
     *
     */
    public IArtifact createArtifactDataIndex() {
        return this.artifactIndex.create(hash, artifactPath, groupId, artifactId, version, classifier);
    }

    private void checkCorrectness(IArtifact artifact) {
        assertEquals(
                String.format("%s:%s:%s:%s:%s:%s", hash, artifactPath, groupId.toString(), artifactId.getStringValue(), version.toString(), artifactType.toString()),
                artifact.getHash() + ":" + artifact.getArtifactPath() + ":" + artifact.getGAV().getStringValue());
    }

}