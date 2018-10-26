package com.usaa.mapper.processors;

import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifactIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneArtifactIndex;

import com.usaa.bank.asset.index.impl.domain.LuceneJavaClassDependencyIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneJavaClassIndex;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DirectoryVisitor_Test {

    private Path path;
    private IArtifactIndex artifactIndex;
    private IJavaClassIndex javaClassIndex;
    private IJavaClassDependencyIndex javaClassDependencyIndex;


    @Before
    public void setup() {
        this.artifactIndex = new LuceneArtifactIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.javaClassIndex = new LuceneJavaClassIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.javaClassDependencyIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
    }
    

    @Test
    public void run_shouldRunWhenJarArtifactIsFoundAndPathToItIsValid() throws Exception {
        URI uri = new URI(DirectoryVisitor_Test.class.getClassLoader().getResource("hub/com/bar/c.jar").toString());
        File file = new File(uri);
        path = file.toPath();
        DirectoryVisitor DirectoryVisitor = new DirectoryVisitor(artifactIndex, javaClassIndex, javaClassDependencyIndex);
        openSessions();
        DirectoryVisitor.visitFile(path,null);
        flushSessions();
        Set<IArtifact> artifacts = artifactIndex.find();
        closeSessions();
        assertEquals(artifacts.size(), 1);

    }


    @Test
    public void run_shouldNotRunWhenPathToArtifactIsInvalid() throws Exception {
        path = null;
        DirectoryVisitor DirectoryVisitor = new DirectoryVisitor(artifactIndex,javaClassIndex, javaClassDependencyIndex);
        openSessions();
        DirectoryVisitor.visitFile(path,null);
        flushSessions();
        Set<IArtifact> artifacts = artifactIndex.find();
        closeSessions();
        assertEquals(0, artifacts.size());
    }

    @Test
    public void run_shouldNotRunWhenAnArtifactCannotBeFound() throws Exception {
        DirectoryVisitor DirectoryVisitor = new DirectoryVisitor(artifactIndex, javaClassIndex, javaClassDependencyIndex);
        openSessions();
        DirectoryVisitor.visitFile(null,null);
        flushSessions();
        Set<IArtifact> artifacts = artifactIndex.find();
        closeSessions();
        assertEquals(0, artifacts.size());
    }

    @Test
    public void createDirectoryVisitorNotNull() {
        DirectoryVisitor DirectoryVisitor = new DirectoryVisitor(artifactIndex, javaClassIndex, javaClassDependencyIndex);
        assertNotNull(DirectoryVisitor);

    }
    

    private void openSessions() {
        artifactIndex.openSession();
        javaClassIndex.openSession();
        javaClassDependencyIndex.openSession();
    }

    private void closeSessions() {
        artifactIndex.closeSession();
        javaClassIndex.closeSession();
        javaClassDependencyIndex.closeSession();
    }

    private void flushSessions() {
        artifactIndex.flushSession();
        javaClassIndex.flushSession();
        javaClassDependencyIndex.flushSession();

    }
}
