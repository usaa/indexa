package com.usaa.mapper.processors.mappers;

import com.usaa.bank.asset.index.api.domain.asset.artifact.Artifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactId;
import com.usaa.bank.asset.index.api.domain.asset.gav.GAV;
import com.usaa.bank.asset.index.api.domain.asset.gav.GroupId;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.impl.domain.LuceneArtifactIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneDependencyIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneJavaClassDependencyIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneJavaClassIndex;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.zip.ZipInputStream;

public class ClassMapper_Test {

    private LuceneArtifactIndex artifactIndex;
    private LuceneDependencyIndex artifactDependencyIndex;
    private LuceneJavaClassIndex javaClassIndex;
    private LuceneJavaClassDependencyIndex javaClassDependencyIndex;
    private final String CLASS_FILE_EXTENSION = ".class";
    private final String JAR_FILE_EXTENSION = ".jar";
    private final String WAR_FILE_EXTENSION = ".war";
    private IArtifact artifact;

    @Before
    public void setup() throws Exception {
        this.artifactIndex = new LuceneArtifactIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.artifactDependencyIndex = new LuceneDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.javaClassIndex = new LuceneJavaClassIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.javaClassDependencyIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.artifactIndex.openSession();
        this.artifactDependencyIndex.openSession();
        this.javaClassIndex.openSession();
        this.javaClassDependencyIndex.openSession();
        this.artifact = new Artifact("test", "hub/testPath", new GAV(new GroupId("testGroupId"), new ArtifactId("testArtifactId"), new Version("testVersion"), ArtifactType.COMPONENT), "JAR");
    }

    @After
    public void tearDown() throws Exception {
        this.artifactIndex.closeSession();
        this.artifactDependencyIndex.closeSession();
        this.javaClassIndex.closeSession();
        this.javaClassDependencyIndex.closeSession();
    }

    @Test
    public void createIndexCausesExceptionConstantError() {
        ClassMapper ClassMapper = new ClassMapper();

        try {
            URI uri = new URI(ClassMapper_Test.class.getClassLoader().getResource("hub/com/bar/loggerB.jar").toString());
            InputStream is = new FileInputStream(uri.getPath());
            ClassMapper.indexJavaInternals(artifact, artifactIndex, javaClassIndex, javaClassDependencyIndex, new ZipInputStream(is));
            javaClassIndex.flushSession();
        } catch (Exception e) {
            Assert.assertEquals("invalid constant type: 60", e.getMessage());
        }
    }

    @Test
    public void indexJavaInternals_shouldIndexContentsOfJavaArchiveWhenArtifactContainsClassFiles() throws Exception {
        ClassMapper ClassMapper = new ClassMapper();
        URI uri = new URI(ClassMapper_Test.class.getClassLoader().getResource("hub/com/bar/c.jar").toString());
        InputStream is = new FileInputStream(uri.getPath());
        Assert.assertEquals(CLASS_FILE_EXTENSION, ClassMapper.indexJavaInternals(artifact, artifactIndex, javaClassIndex, javaClassDependencyIndex, new ZipInputStream(is)));
    }


    @Test
    public void indexJavaInternals_shouldIndexContentsOfJavaArchiveWhenArtifactContainsAJarFile() throws Exception {
        ClassMapper ClassMapper = new ClassMapper();
        URI uri = new URI(ClassMapper_Test.class.getClassLoader().getResource("hub/com/bar/c.jar").toString());
        InputStream is = new FileInputStream(uri.getPath());
        ClassMapper.indexJavaInternals(artifact, artifactIndex, javaClassIndex, javaClassDependencyIndex, new ZipInputStream(is));
        javaClassIndex.flushSession();
        Assert.assertTrue(0 < javaClassIndex.find().size());
    }

    @Test
    public void indexJavaInternals_shouldNotIndexContentsOfJavaArchiveWhenAnyContentsAreNotIndexable() throws Exception {
        ClassMapper ClassMapper = new ClassMapper();
        URI uri = new URI(ClassMapper_Test.class.getClassLoader().getResource("empty.xml").toString());
        InputStream is = new FileInputStream(uri.getPath());
        Assert.assertNull(ClassMapper.indexJavaInternals(artifact, artifactIndex, javaClassIndex, javaClassDependencyIndex, new ZipInputStream(is)));
    }

    @Test
    public void indexArchiveArtifact_shouldIndexArchiveArtifactWhenArtifactIsAJar() throws Exception {
        ClassMapper ClassMapper = new ClassMapper();
        URI uri = new URI(ClassMapper_Test.class.getClassLoader().getResource("hub/com/bar/c.jar").toString());
        InputStream is = new FileInputStream(uri.getPath());
        Assert.assertNotNull(ClassMapper.indexArchiveArtifact(uri.getPath().toString(), "test", new ZipInputStream(is), artifactIndex, javaClassIndex, javaClassDependencyIndex));
    }

    @Test
    public void indexArchiveArtifact_shouldIndexArchiveArtifactWhenArtifactIsAnEar() throws Exception {
        ClassMapper ClassMapper = new ClassMapper();
        URI uri = new URI(ClassMapper_Test.class.getClassLoader().getResource("hub/com/bar/earTest.ear").toString());
        InputStream is = new FileInputStream(uri.getPath());
        Assert.assertNotNull(ClassMapper.indexArchiveArtifact(uri.getPath().toString(), "test", new ZipInputStream(is), artifactIndex, javaClassIndex, javaClassDependencyIndex));
    }

    @Test
    public void indexArchiveArtifact_shouldIndexArchiveArtifactWhenArtifactIsAWar() throws Exception {
        ClassMapper ClassMapper = new ClassMapper();
        URI uri = new URI(ClassMapper_Test.class.getClassLoader().getResource("hub/com/bar/warTest.war").toString());
        InputStream is = new FileInputStream(uri.getPath());
        String path = uri.getPath().toString();
        Assert.assertNotNull(ClassMapper.indexArchiveArtifact(path, "test", new ZipInputStream(is), artifactIndex, javaClassIndex, javaClassDependencyIndex));
    }

    @Test
    public void indexArchiveArtifact_shouldNotIndexArchiveArtifactWhenAnyOfTheProvidedParametersAreNull() throws Exception {
        ClassMapper ClassMapper = new ClassMapper();
        Assert.assertNull(ClassMapper.indexArchiveArtifact("test", "test", null, artifactIndex, null,null));
    }

}

   