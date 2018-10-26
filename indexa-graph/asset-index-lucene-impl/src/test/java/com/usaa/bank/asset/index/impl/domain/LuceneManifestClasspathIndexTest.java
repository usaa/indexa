package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LuceneManifestClasspathIndexTest {
    private LuceneManifestClasspathIndex manifestIndex = null;
    private static final GUID ARTIFACTGUID = GUIDFactory.createGUID("artifactGUID");
    private static final String CLASSPATHENTRY = "classpath";

    @Before
    public void runOnceBeforeClass() {
        this.manifestIndex = new LuceneManifestClasspathIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
    }

    @Test
    public void addClasspathEntry() {
        this.manifestIndex.openSession();
        Assert.assertEquals(0, this.manifestIndex.findClasspathEntries(ARTIFACTGUID).size());
        this.manifestIndex.flushSession();
        Assert.assertTrue(this.manifestIndex.addClasspathEntry(ARTIFACTGUID, CLASSPATHENTRY));
        this.manifestIndex.flushSession();
        Assert.assertEquals(1, this.manifestIndex.findClasspathEntries(ARTIFACTGUID).size());
        this.manifestIndex.closeSession();


    }

    @Test
    public void removeClasspathEntry() {
        this.manifestIndex.openSession();
        this.manifestIndex.addClasspathEntry(ARTIFACTGUID, CLASSPATHENTRY);
        this.manifestIndex.flushSession();
        Assert.assertEquals(1, this.manifestIndex.findArtifacts(CLASSPATHENTRY).size());
        this.manifestIndex.flushSession();
        this.manifestIndex.removeClasspathEntry(ARTIFACTGUID, CLASSPATHENTRY);
        this.manifestIndex.flushSession();
        Assert.assertEquals(0, this.manifestIndex.findArtifacts(CLASSPATHENTRY).size());
        this.manifestIndex.closeSession();
    }

    @Test
    public void removeClasspathEntries() {
        this.manifestIndex.openSession();
        this.manifestIndex.addClasspathEntry(ARTIFACTGUID, CLASSPATHENTRY);
        this.manifestIndex.flushSession();
        Assert.assertEquals(1, this.manifestIndex.findArtifacts(CLASSPATHENTRY).size());
        this.manifestIndex.flushSession();
        this.manifestIndex.removeClasspathEntries(CLASSPATHENTRY);
        this.manifestIndex.flushSession();
        Assert.assertEquals(0, this.manifestIndex.findArtifacts(CLASSPATHENTRY).size());
        this.manifestIndex.closeSession();
    }

    @Test
    public void findClasspathEntries() {
        this.manifestIndex.openSession();
        Assert.assertEquals(0, this.manifestIndex.findClasspathEntries(ARTIFACTGUID).size());
        this.manifestIndex.flushSession();
        this.manifestIndex.addClasspathEntry(ARTIFACTGUID, CLASSPATHENTRY);
        this.manifestIndex.flushSession();
        Assert.assertEquals(1, this.manifestIndex.findClasspathEntries(ARTIFACTGUID).size());
        this.manifestIndex.closeSession();
    }

    @Test
    public void removeArtifact() {
        this.manifestIndex.openSession();
        this.manifestIndex.addClasspathEntry(ARTIFACTGUID, CLASSPATHENTRY);
        this.manifestIndex.flushSession();
        Assert.assertEquals(1, this.manifestIndex.findArtifacts(CLASSPATHENTRY).size());
        this.manifestIndex.flushSession();
        this.manifestIndex.removeArtifact(ARTIFACTGUID);
        this.manifestIndex.flushSession();
        Assert.assertEquals(0, this.manifestIndex.findArtifacts(CLASSPATHENTRY).size());
        this.manifestIndex.closeSession();
    }

    @Test
    public void findArtifacts() {
        this.manifestIndex.openSession();
        Assert.assertEquals(0, this.manifestIndex.findArtifacts(CLASSPATHENTRY).size());
        this.manifestIndex.flushSession();
        this.manifestIndex.addClasspathEntry(ARTIFACTGUID, CLASSPATHENTRY);
        this.manifestIndex.flushSession();
        Assert.assertEquals(1, this.manifestIndex.findArtifacts(CLASSPATHENTRY).size());
        this.manifestIndex.closeSession();
    }

}