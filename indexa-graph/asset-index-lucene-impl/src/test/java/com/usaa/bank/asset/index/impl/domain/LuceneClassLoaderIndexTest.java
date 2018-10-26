package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.deployment.DeploymentConfig;
import com.usaa.bank.asset.index.api.domain.asset.deployment.IDeploymentConfig;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LuceneClassLoaderIndexTest {
    private LuceneClassLoaderIndex classLoaderIndex = null;

    private static final GUID deploymentConfigGUID = DeploymentConfig.createGUID("config-id", "config-name");
    private static final String id = "class-loader-id";

    @Before
    public void runOnceBeforeClass() {
        this.classLoaderIndex = new LuceneClassLoaderIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
    }

    @Test
    public void EmptyLuceneCreationTest() {
        this.classLoaderIndex.openSession();
        boolean isCreated = createClassLoaderIndex();

        Assert.assertTrue(isCreated);

        this.classLoaderIndex.closeSession();
    }

    @Test
    public void removeByGUID() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();

        this.classLoaderIndex.removeClassLoaders(deploymentConfigGUID);
        this.classLoaderIndex.flushSession();
        assertEquals(0, this.classLoaderIndex.findClassLoaders(id).size());
        this.classLoaderIndex.closeSession();
    }

    @Test
    public void removeByGuidId() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();

        this.classLoaderIndex.removeClassLoader(deploymentConfigGUID, id);
        this.classLoaderIndex.flushSession();
        assertEquals(0, this.classLoaderIndex.findClassLoaders(id).size());
        this.classLoaderIndex.closeSession();

    }

    @Test
    public void findAllDocuments() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();
        this.classLoaderIndex.flushSession();

        // 1 is the expected from the size
        Assert.assertEquals(1, this.classLoaderIndex.findClassLoaders().size());
        this.classLoaderIndex.closeSession();
    }

    @Test
    public void findByGuid() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();
        this.classLoaderIndex.flushSession();

        // 1 is the expected from the size
        Assert.assertEquals(1, this.classLoaderIndex.findClassLoaders(deploymentConfigGUID).size());
        this.classLoaderIndex.closeSession();
    }

    @Test
    public void getByGuidAndId() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();
        this.classLoaderIndex.flushSession();
        Assert.assertEquals(id, this.classLoaderIndex.getClassLoader(deploymentConfigGUID, id).getId());
        this.classLoaderIndex.closeSession();
    }

    @Test
    public void findById() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();
        this.classLoaderIndex.flushSession();
        Assert.assertEquals(1, this.classLoaderIndex.findClassLoaders(id).size());
        this.classLoaderIndex.closeSession();
    }

    @Test
    public void findNumberOfDependencyGUID() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();
        this.classLoaderIndex.flushSession();
        this.classLoaderIndex.closeSession();
        this.classLoaderIndex.openSession();
        Assert.assertEquals(1, this.classLoaderIndex.findDeploymentConfigs(id).size());
        this.classLoaderIndex.closeSession();
    }


    /*
     * Utility Methods
     *
     */
    public boolean createClassLoaderIndex() {
        return this.classLoaderIndex.addClassLoader(deploymentConfigGUID, id);
    }


}