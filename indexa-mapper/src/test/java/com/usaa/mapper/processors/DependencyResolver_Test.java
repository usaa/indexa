package com.usaa.mapper.processors;

import com.usaa.bank.asset.index.api.domain.asset.artifact.Artifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifactIndex;
import com.usaa.bank.asset.index.api.domain.asset.dependency.IDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.gav.*;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.HierarchicalPackage;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneArtifactIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneDependencyIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneJavaClassDependencyIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneJavaClassIndex;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DependencyResolver_Test {
    private IArtifactIndex artifactIndex;
    private IDependencyIndex artifactDependencyIndex;
    private IJavaClassIndex javaClassIndex;
    private IJavaClassDependencyIndex javaClassDependencyIndex;

    @Before
    public void setup() {
        artifactIndex = new LuceneArtifactIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        artifactDependencyIndex = new LuceneDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        javaClassIndex = new LuceneJavaClassIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        javaClassDependencyIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        populateIndexesForDependencyResolution();
    }

    @After
    public void tearDown() {
        closeSessions();
    }

    @Test
    public void resolveDependencies_shouldResolveDependenciesWhenProvidedAllPossibleParameters() throws Exception {
        DependencyResolver artifactDependencyResolver = new DependencyResolver();
        artifactDependencyResolver.resolveDependenciesForArtifact(artifactIndex, artifactDependencyIndex, javaClassIndex, javaClassDependencyIndex);
    }


    @Test
    public void performArtifactDependencyResolution_shouldPerformDependencyResolutionForTheArtifact() throws Exception {
        String hash = "67cffaa6e52b1cfa8a6d44d44e74b437cbbf9cba";
        String artifactPath = "/hub/test/location/foobar.jar";
        GAV artifactGAV = new GAV("testGroupId", "foobar", "unknown", ArtifactType.COMPONENT);
        String classifier = "JAR";
        IArtifact artifact = new Artifact(hash, artifactPath, artifactGAV, classifier);
        DependencyResolver.resolveDependenciesForArtifact(artifactIndex, artifactDependencyIndex, javaClassIndex, javaClassDependencyIndex);
        artifactDependencyIndex.flushSession();

        String guidIdentifier1 = "67cffaa6e52b1cfa8a6d44d44e74b437cbbf9cba:/hub/test/location/foobar.jar";
        String guidIdentifier2 = "396058211420f80fdd7c13a78ac1ba1eb9e2d479:/hub/test/location2/foo.jar";
        String guidIdentifier3 = artifactDependencyIndex.findDependencies(GUIDFactory.createGUID(guidIdentifier1), null, null).iterator().next().toString();
        int numberOfDependencies = artifactDependencyIndex.findDependencies(GUIDFactory.createGUID(guidIdentifier1), null, null).size();
        assertEquals(1,numberOfDependencies);
        assertEquals(GUIDFactory.createGUID(guidIdentifier2).getStringValue(), (guidIdentifier3));
    }

    private void populateIndexesForDependencyResolution() {
        openSessions();
        artifactIndex.create("67cffaa6e52b1cfa8a6d44d44e74b437cbbf9cba", "/hub/test/location/foobar.jar", new GroupId("test.groupId"), new ArtifactId("foobar"), new Version("unknown"), ArtifactType.COMPONENT, "JAR", null, null);
        artifactIndex.flushSession();
        artifactIndex.create("e484735c4a22b6be6bd52d36f8eaf1eb421487b8", "/hub/test/location1/bar.jar", new GroupId("test.groupId1"), new ArtifactId("bar"), new Version("unknown"), ArtifactType.COMPONENT, "JAR", null, null);
        artifactIndex.flushSession();
        artifactIndex.create("396058211420f80fdd7c13a78ac1ba1eb9e2d479", "/hub/test/location2/foo.jar", new GroupId("test.groupId2"), new ArtifactId("foo"), new Version("unknown"), ArtifactType.COMPONENT, "JAR", null, null);
        javaClassIndex.create("67cffaa6e52b1cfa8a6d44d44e74b437cbbf9cba", new HierarchicalPackage("org.test.package"), "testClassName$testClass", 0, 0, 0, 0);
        javaClassIndex.flushSession();
        javaClassIndex.create("396058211420f80fdd7c13a78ac1ba1eb9e2d479", new HierarchicalPackage("org.test.package2"), "testDepClassName", 0, 0, 0, 0);
        javaClassDependencyIndex.addDependency(GUIDFactory.createGUID("67cffaa6e52b1cfa8a6d44d44e74b437cbbf9cba:org.test.package:testClassName$testClass"), new HierarchicalPackage("org.test.package2"), "testDepClassName");
        flushSessions();
    }


    private void openSessions() {
        artifactDependencyIndex.openSession();
        artifactIndex.openSession();
        javaClassIndex.openSession();
        javaClassDependencyIndex.openSession();
    }

    private void closeSessions() {
        artifactDependencyIndex.closeSession();
        artifactIndex.closeSession();
        javaClassIndex.closeSession();
        javaClassDependencyIndex.closeSession();
    }

    private void flushSessions() {
        artifactDependencyIndex.flushSession();
        artifactIndex.flushSession();
        javaClassIndex.flushSession();
        javaClassDependencyIndex.flushSession();
    }
}
