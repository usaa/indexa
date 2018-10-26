package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.dependency.DependencyScope;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LuceneDependencyIndexTest {
    private LuceneDependencyIndex dependencyIndex = null;
    private static final GUID dependent = GUIDFactory.createGUID("dependent");
    private static final GUID dependency = GUIDFactory.createGUID("dependency");
    private static final GUID dependencyLevelTwo = GUIDFactory.createGUID("dependencyLevelTwo");
    private static final GUID dependencyLevelThree = GUIDFactory.createGUID("dependencyLevelThree");
    private static final GUID sharedLibGUID = GUIDFactory.createGUID("sharedLib");
    private static final DependencyScope dependencyScope = DependencyScope.BUILD;

    @Before
    public void runOnceBeforeClass() {
        this.dependencyIndex = new LuceneDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
    }

    @Test
    public void AddDependencyToLuceneDependencyIndex() {
        this.dependencyIndex.openSession();
        assertEquals(0, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.flushSession();
        assertTrue(this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID));
        this.dependencyIndex.flushSession();
        assertEquals(1, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.closeSession();
    }

    @Test
    public void RemoveDependencyGivenDependentGUIDAndDependencyGUIDAndDependencyScope() {
        this.dependencyIndex.openSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        assertEquals(1, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.removeDependency(dependent, dependency, dependencyScope, null);
        this.dependencyIndex.flushSession();
        assertEquals(0, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.closeSession();
    }

    @Test
    public void RemoveDependencyGivenDependentGUIDAndDependencyGUIDAndDependencyScopeAndShareLibraryGUID() {
        this.dependencyIndex.openSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        assertEquals(1, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.removeDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        assertEquals(0, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.closeSession();
    }

    @Test
    public void RemoveDependenciesGivenDependentGUIDAndDependencyScope() {
        this.dependencyIndex.openSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        assertEquals(1, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.removeDependencies(dependent, dependencyScope, null);
        this.dependencyIndex.flushSession();
        assertEquals(0, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.closeSession();
    }

    @Test
    public void RemoveDependenciesGivenDependentGUIDAndDependencyScopeAndShareLibraryGUID() {
        this.dependencyIndex.openSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        assertEquals(1, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.removeDependencies(dependent, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        assertEquals(0, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.closeSession();
    }

    @Test
    public void RemoveDependentsGivenDependencyGUIDAndDependencyScope() {
        this.dependencyIndex.openSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        assertEquals(1, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.removeDependents(dependency, dependencyScope, null);
        this.dependencyIndex.flushSession();
        assertEquals(0, this.dependencyIndex.findDependents(dependency, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.closeSession();
    }

    @Test
    public void RemoveDependentsGivenDependencyGUIDAndDependencyScopeAndSharedLibraryGUID() {
        this.dependencyIndex.openSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        assertEquals(1, this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.removeDependents(dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        assertEquals(0, this.dependencyIndex.findDependents(dependency, dependencyScope, sharedLibGUID).size());
        this.dependencyIndex.closeSession();
    }

    @Test
    public void FindDependenciesInLuceneDependencyIndexGivenDependentGUIDandDependencyScope() {
        this.dependencyIndex.openSession();
        ArrayList<GUID> guidList = new ArrayList<GUID>(this.dependencyIndex.findDependencies(dependent, dependencyScope, null));
        assertEquals(0, guidList.size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        guidList = new ArrayList<GUID>(this.dependencyIndex.findDependencies(dependent, dependencyScope, null));
        assertEquals(dependency, guidList.get(0));
        this.dependencyIndex.closeSession();
    }

    @Test
    public void FindDependenciesInLuceneDependencyIndexGivenDependentGUIDandDependencyScopeAndSharedLibraryGUID() {
        this.dependencyIndex.openSession();
        ArrayList<GUID> guidList = new ArrayList<GUID>(this.dependencyIndex.findDependencies(dependent, dependencyScope, null));
        assertEquals(0, guidList.size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        guidList = new ArrayList<GUID>(this.dependencyIndex.findDependencies(dependent, dependencyScope, sharedLibGUID));
        assertEquals(dependency, guidList.get(0));
        this.dependencyIndex.closeSession();
    }

    @Test
    public void FindDependenciesInLuceneDependencyIndexGivenDependentGUID() {
        this.dependencyIndex.openSession();
        ArrayList<GUID> guidList = new ArrayList<GUID>(this.dependencyIndex.findDependencies(dependent, dependencyScope, null));
        assertEquals(0, guidList.size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        guidList = new ArrayList<GUID>(this.dependencyIndex.findDependencies(dependent, null, null));
        assertEquals(dependency, guidList.get(0));
        this.dependencyIndex.closeSession();
    }

    @Test
    public void FindDependentsInLuceneDependencyIndexGivenDependencyGUIDandDependencyScope() {
        this.dependencyIndex.openSession();
        ArrayList<GUID> guidList = new ArrayList<GUID>(this.dependencyIndex.findDependencies(dependent, dependencyScope, null));
        assertEquals(0, guidList.size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        guidList = new ArrayList<GUID>(this.dependencyIndex.findDependents(dependency, dependencyScope, null));
        assertEquals(dependent, guidList.get(0));
        this.dependencyIndex.closeSession();
    }

    @Test
    public void FindDependentsInLuceneDependencyIndexGivenDependencyGUIDandDependencyScopeAndSharedLibraryGUID() {
        this.dependencyIndex.openSession();
        ArrayList<GUID> guidList = new ArrayList<GUID>(this.dependencyIndex.findDependencies(dependent, dependencyScope, null));
        assertEquals(0, guidList.size());
        this.dependencyIndex.flushSession();
        this.dependencyIndex.addDependency(dependent, dependency, dependencyScope, sharedLibGUID);
        this.dependencyIndex.flushSession();
        guidList = new ArrayList<GUID>(this.dependencyIndex.findDependents(dependency, dependencyScope, sharedLibGUID));
        assertEquals(dependent, guidList.get(0));
        this.dependencyIndex.closeSession();
    }
}