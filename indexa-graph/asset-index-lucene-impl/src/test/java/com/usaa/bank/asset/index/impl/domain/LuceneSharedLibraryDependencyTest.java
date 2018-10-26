package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LuceneSharedLibraryDependencyTest {
    private LuceneSharedLibraryDependencyIndex sharedLibraryDependencyIndex = null;
    private static final GUID sharedLibGUID = GUIDFactory.createGUID("sharedLib");
    private static final String dependency = ("C:\\foo\\dependency");


    @Before
    public void runOnceBeforeClass() {
        this.sharedLibraryDependencyIndex = new LuceneSharedLibraryDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
    }

    @Test
    public void AddDependencyToLuceneSharedLibraryDependencyIndex() {
        this.sharedLibraryDependencyIndex.openSession();
        assertEquals(0, this.sharedLibraryDependencyIndex.findArtifactPaths(sharedLibGUID).size());
        this.sharedLibraryDependencyIndex.flushSession();
        assertTrue(this.sharedLibraryDependencyIndex.addDependency(sharedLibGUID, dependency));
        this.sharedLibraryDependencyIndex.flushSession();
        assertEquals(1, this.sharedLibraryDependencyIndex.findArtifactPaths(sharedLibGUID).size());
        this.sharedLibraryDependencyIndex.flushSession();
        this.sharedLibraryDependencyIndex.closeSession();
    }

    @Test
    public void RemoveDependencyGivenSharedLibraryGUIDAndDependencyPath() {
        this.sharedLibraryDependencyIndex.openSession();
        this.sharedLibraryDependencyIndex.addDependency(sharedLibGUID, dependency);
        this.sharedLibraryDependencyIndex.flushSession();
        assertEquals(1, this.sharedLibraryDependencyIndex.findArtifactPaths(sharedLibGUID).size());
        this.sharedLibraryDependencyIndex.flushSession();
        this.sharedLibraryDependencyIndex.removeDependency(sharedLibGUID, dependency);
        this.sharedLibraryDependencyIndex.flushSession();
        assertEquals(0, this.sharedLibraryDependencyIndex.findArtifactPaths(sharedLibGUID).size());
        this.sharedLibraryDependencyIndex.closeSession();
    }

    @Test
    public void RemoveDependenciesGivenSharedLibraryGUID() {
        this.sharedLibraryDependencyIndex.openSession();
        this.sharedLibraryDependencyIndex.addDependency(sharedLibGUID, dependency);
        this.sharedLibraryDependencyIndex.flushSession();
        assertEquals(1, this.sharedLibraryDependencyIndex.findArtifactPaths(sharedLibGUID).size());
        this.sharedLibraryDependencyIndex.flushSession();
        this.sharedLibraryDependencyIndex.removeDependencies(sharedLibGUID);
        this.sharedLibraryDependencyIndex.flushSession();
        assertEquals(0, this.sharedLibraryDependencyIndex.findArtifactPaths(sharedLibGUID).size());
        this.sharedLibraryDependencyIndex.closeSession();
    }

    @Test
    public void FindLibrariesInLuceneSharedLibraryDependencyIndexGivenDependencyPath() {
        this.sharedLibraryDependencyIndex.openSession();
        ArrayList<GUID> guidList = new ArrayList<>(this.sharedLibraryDependencyIndex.findLibraries(dependency));
        assertEquals(0, guidList.size());
        this.sharedLibraryDependencyIndex.flushSession();
        this.sharedLibraryDependencyIndex.addDependency(sharedLibGUID, dependency);
        this.sharedLibraryDependencyIndex.flushSession();
        guidList = new ArrayList<>(this.sharedLibraryDependencyIndex.findLibraries(dependency));
        assertEquals(sharedLibGUID, guidList.get(0));
        this.sharedLibraryDependencyIndex.closeSession();
    }

    @Test
    public void FindDependenciesInLuceneSharedLibraryDependencyIndexGivenSharedLibraryGUID() {
        this.sharedLibraryDependencyIndex.openSession();
        ArrayList<String> paths = new ArrayList<>(this.sharedLibraryDependencyIndex.findArtifactPaths(sharedLibGUID));
        assertEquals(0, paths.size());
        this.sharedLibraryDependencyIndex.flushSession();
        this.sharedLibraryDependencyIndex.addDependency(sharedLibGUID, dependency);
        this.sharedLibraryDependencyIndex.flushSession();
        paths = new ArrayList<>(this.sharedLibraryDependencyIndex.findArtifactPaths(sharedLibGUID));
        assertEquals(dependency, paths.get(0));
        this.sharedLibraryDependencyIndex.closeSession();
    }

}
