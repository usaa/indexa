package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.deployment.DeploymentConfig;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LuceneClassLoaderLibraryIndexTest {
    private LuceneClassLoaderLibraryIndex classLoaderIndex = null;

    private static final GUID classLoaderGUID = DeploymentConfig.createGUID("xyz", "abc");
    private static final String sharedLibraryName = "shared-library-name";

    @Before
    public void runOnceBeforeClass() {
        this.classLoaderIndex = new LuceneClassLoaderLibraryIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
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

        this.classLoaderIndex.removeLibraries(classLoaderGUID);
        this.classLoaderIndex.flushSession();
        assertEquals(0, this.classLoaderIndex.findClassLoaders(sharedLibraryName).size());
        this.classLoaderIndex.closeSession();
    }

    @Test
    public void removeByGuidId() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();

        this.classLoaderIndex.removeLibrary(classLoaderGUID, sharedLibraryName);
        this.classLoaderIndex.flushSession();
        assertEquals(0, this.classLoaderIndex.findClassLoaders(sharedLibraryName).size());
        this.classLoaderIndex.closeSession();

    }

    @Test
    public void findAllDocuments() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();
        this.classLoaderIndex.flushSession();
        Assert.assertEquals(1, this.classLoaderIndex.findLibraries().size());
        this.classLoaderIndex.closeSession();
    }

    @Test
    public void findByGuid() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();
        this.classLoaderIndex.flushSession();
        Assert.assertEquals(1, this.classLoaderIndex.findLibraries(classLoaderGUID).size());
        this.classLoaderIndex.closeSession();
    }

    @Test
    public void getByGuidAndId() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();
        this.classLoaderIndex.flushSession();
        Assert.assertEquals(sharedLibraryName, this.classLoaderIndex.getLibrary(classLoaderGUID, sharedLibraryName).getSharedLibraryName());
        this.classLoaderIndex.closeSession();
    }

    @Test
    public void findById() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();
        this.classLoaderIndex.flushSession();
        Assert.assertEquals(1, this.classLoaderIndex.findClassLoaders(sharedLibraryName).size());
        this.classLoaderIndex.closeSession();
    }

    @Test
    public void findNumberOfDependencyGUID() {
        this.classLoaderIndex.openSession();
        createClassLoaderIndex();
        this.classLoaderIndex.flushSession();
        this.classLoaderIndex.closeSession();
        this.classLoaderIndex.openSession();
        Assert.assertEquals(1, this.classLoaderIndex.findClassLoaders(sharedLibraryName).size());
        this.classLoaderIndex.closeSession();
    }


    /*
     * Utility Methods
     *
     */
    public boolean createClassLoaderIndex() {
        return this.classLoaderIndex.addLibrary(classLoaderGUID, sharedLibraryName);
    }


}