package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.artifact.Artifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.deployment.DeploymentConfig;
import com.usaa.bank.asset.index.api.domain.asset.deployment.IDeploymentConfig;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LuceneDeploymentConfigindexTest {
    private LuceneDeploymentConfigIndex deploymentIndex = null;
    private static final String id = "config-id";
    private static final String name = "config-name";


    @Before
    public void runOnceBeforeClass() {
        this.deploymentIndex = new LuceneDeploymentConfigIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
    }

    @Test
    public void EmptyLuceneCreationTest() {
        this.deploymentIndex.openSession();
        IDeploymentConfig config = createDeploymentIndex();

        Assert.assertEquals(id, config.getId());

        this.deploymentIndex.closeSession();
    }

    @Test
    public void removeByGUID() {
        this.deploymentIndex.openSession();
        createDeploymentIndex();

        boolean success = this.deploymentIndex.remove(GUIDFactory.createGUID(id + ":" + name));
        this.deploymentIndex.closeSession();
        assertTrue(success);
    }

    @Test
    public void removeByIdName() {
        this.deploymentIndex.openSession();
        createDeploymentIndex();

        boolean success = this.deploymentIndex.remove(id, name);
        this.deploymentIndex.closeSession();
        assertTrue(success);
    }

    @Test
    public void findAllDocuments() {
        this.deploymentIndex.openSession();
        createDeploymentIndex();
        this.deploymentIndex.flushSession();
        Assert.assertEquals(1, this.deploymentIndex.find().size());
        this.deploymentIndex.closeSession();
    }

    @Test
    public void saveAndDeleteArtifact() {
        this.deploymentIndex.openSession();
        IDeploymentConfig config = new DeploymentConfig(id, name);
        boolean isSaved = this.deploymentIndex.save(config);

        Assert.assertEquals(true, isSaved);
        this.deploymentIndex.remove(config.getGUID());
        this.deploymentIndex.flushSession();
        this.deploymentIndex.closeSession();
    }


    @Test
    public void findTypeOne() {
        this.deploymentIndex.openSession();
        createDeploymentIndex();
        this.deploymentIndex.flushSession();
        Assert.assertEquals(1, this.deploymentIndex.find(name).size());
        this.deploymentIndex.closeSession();
    }

    @Test
    public void getArtifactByGUID() {
        this.deploymentIndex.openSession();
        IDeploymentConfig config = createDeploymentIndex();
        this.deploymentIndex.flushSession();

        IDeploymentConfig retrievedConfig = this.deploymentIndex.get(config.getGUID());
        Assert.assertEquals(DeploymentConfig.createGUID(id, name), retrievedConfig.getGUID());

    }

    @Test
    public void getArtifactBykeyValue() {
        this.deploymentIndex.openSession();
        createDeploymentIndex();
        this.deploymentIndex.flushSession();

        Set<IDeploymentConfig> artifactSet = this.deploymentIndex.find(DeploymentConfig.KEY_ID, id);
        Assert.assertEquals(1, artifactSet.size());
    }


    /*
     * Utility Methods
     *
     */
    public IDeploymentConfig createDeploymentIndex() {
        return this.deploymentIndex.create(id, name);
    }


}