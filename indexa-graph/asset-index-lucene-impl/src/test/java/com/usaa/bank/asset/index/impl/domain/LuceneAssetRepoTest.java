package com.usaa.bank.asset.index.impl.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.java.classes.HierarchicalPackage;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactId;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.dependency.DependencyScope;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.JavaClass;
import com.usaa.bank.asset.index.api.domain.asset.gav.GroupId;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;

public class LuceneAssetRepoTest {
    private LuceneArtifactIndex artifactIndex = null;
    private LuceneDependencyIndex artifactDependencyIndex = null;
    private LuceneManifestClasspathIndex manifestClasspathIndex = null;
    private LuceneJavaClassIndex javaClassIndex = null;
    private LuceneJavaClassDependencyIndex javaClassDependencyIndex = null;
    private LuceneSharedLibraryIndex sharedLibraryIndex = null;
    private LuceneSharedLibraryDependencyIndex sharedLibraryDependencyIndex = null;
    private LuceneAssetRepository assetRepository = null;
    private static final GroupId groupIdTwo = new GroupId("groupId2");
    private static final ArtifactId artifactIdTwo = new ArtifactId("artifactId2");
    private static final Version versionTwo = new Version("version2");
    private static final String id = "LibId";
    private static final String name = "LibName";
    private static final String hash = "hash1234";
    private static final String artifactPath = "www/bank/1234/";
    private static final GroupId groupId = new GroupId("groupId1234");
    private static final ArtifactId artifactId = new ArtifactId("artId1234");
    private static final Version version = new Version("version1234");
    private static final String classifier = "class1234";
    private static final ArtifactType artifactType = ArtifactType.COMPONENT;
    private static final Set<String> tags = new HashSet<String>();
    private static final Map<String, Set<String>> fields = new HashMap<String, Set<String>>();
    private HierarchicalPackage classPackage = new HierarchicalPackage(artifactPath);
    private String className = "class1234";
    private int major = 1;
    private int minor = 2;
    private long date = 1234567890;

    @Before
    public void runOnceBeforeClass() {
        this.artifactIndex = new LuceneArtifactIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.artifactDependencyIndex = new LuceneDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.manifestClasspathIndex = new LuceneManifestClasspathIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.javaClassIndex = new LuceneJavaClassIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.javaClassDependencyIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.sharedLibraryIndex = new LuceneSharedLibraryIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.sharedLibraryDependencyIndex = new LuceneSharedLibraryDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
        this.assetRepository = new LuceneAssetRepository(this.artifactIndex, this.artifactDependencyIndex, this.manifestClasspathIndex, this.javaClassIndex, this.javaClassDependencyIndex, this.sharedLibraryIndex, this.sharedLibraryDependencyIndex);
        testSetup();
    }

    @Test
    public void GetArtifactGivenGroupIdAndArtifactIdAndVersionAndClassifer() {
        assertNotNull(assetRepository.findArtifacts(groupId, artifactId, version, classifier));
    }

    @Test
    public void GetArtifactGivenHash() {
        assertNotNull(assetRepository.findArtifactsByHash(hash));
    }

    @Test
    public void FindArtifactsGivenArtifactType() {
        assertEquals(2, assetRepository.findArtifactsByType(artifactType).size());
    }

    @Test
    public void FindArtifactsGivenSharedLibraryId() {
        assertEquals(1, this.assetRepository.findArtifactsBySharedLibrary(name).size());
    }

    @Test
    public void FindArtifactsGivenHierarchicalPackageAndClassName() {
        assertEquals(1, this.assetRepository.findArtifactsByClass(classPackage, className).size());
    }

    @Test
    public void FindArtifactDependenciesGivenGroupIdAndArtifactIdAndVersion() {
        assertEquals(1, this.assetRepository.findArtifactDependencies(groupId, artifactId, version).size());
    }

    @Test
    public void FindArtifactDependenciesGivenHash() {
        assertEquals(1, this.assetRepository.findArtifactDependencies(hash).size());
    }

    @Test
    public void FindArtifactDependentsGivenGroupIdAndArtifactIdAndVersion() {
        assertEquals(1, this.assetRepository.findArtifactDependents(groupIdTwo, artifactIdTwo, versionTwo).size());
    }

    @Test
    public void FindArtifactDependentsGivenHash() {
        assertEquals(1, this.assetRepository.findArtifactDependents(hash + 2).size());
    }

    @Test
    public void GetJavaClassGivenGroupIdAndArtifactIdAndVersionAndHierarchicalPackageAndClassName() {
        assertNotNull(this.assetRepository.getJavaClass(groupId, artifactId, version, classPackage, className));
    }

    @Test
    public void GetJavaClassGivenHashAndHierarchicalPackageAndClassName() {
        assertNotNull(this.assetRepository.getJavaClass(hash, classPackage, className));
    }

    @Test
    public void FindJavaClassesGivenGroupIdAndArtifactIdAndVersionAndClassifer() {
        assertEquals(1, this.assetRepository.findJavaClasses(groupId, artifactId, version, classifier).size());
    }

    @Test
    public void FindJavaClassesGivenHash() {
        assertEquals(1, this.assetRepository.findJavaClasses(hash).size());
    }

    @Test
    public void FindJavaClassDependenciesGivenGroupIdAndArtifactIdAndVersionAndHierarchicalPackageAndClassName() {
        assertEquals(1, this.assetRepository.findJavaClassDependencies(groupId, artifactId, version, classPackage, className).size());

    }

    @Test
    public void FindJavaClassDependenciesGivenHashAndHierarchicalPackageAndClassName() {
        assertEquals(1, this.assetRepository.findJavaClassDependencies(hash, classPackage, className).size());
    }

    @Test
    public void FindSharedLibraries() {
        assertEquals(2, this.assetRepository.findSharedLibraries().size());
    }

    private void testSetup() {
        this.artifactIndex.openSession();
        this.artifactIndex.create(hash, artifactPath, groupId, artifactId, version, artifactType, classifier, tags, fields);
        this.artifactIndex.create(hash + 2, artifactPath + 2, groupIdTwo, artifactIdTwo, versionTwo, artifactType, classifier + 2, tags, fields);
        this.artifactIndex.closeSession();
        this.sharedLibraryIndex.openSession();
        this.sharedLibraryIndex.create(id, name);
        this.sharedLibraryIndex.create(id + 2, name + 2);
        this.sharedLibraryIndex.closeSession();
        this.sharedLibraryDependencyIndex.openSession();
        this.sharedLibraryDependencyIndex.addDependency(sharedLibraryIndex.getGuid(id, name), artifactPath);
        this.sharedLibraryDependencyIndex.closeSession();
        this.javaClassDependencyIndex.openSession();
        this.javaClassDependencyIndex.addDependency(GUIDFactory.createGUID(new StringBuilder(hash).append(JavaClass.GUID_DELIMITER)
                .append(classPackage.toString()).append(JavaClass.GUID_DELIMITER).append(className).toString()), classPackage, className);
        this.javaClassDependencyIndex.closeSession();
        this.javaClassIndex.openSession();
        this.javaClassIndex.create(hash, classPackage, className, date, date, major, minor);
        this.javaClassIndex.closeSession();
        this.artifactDependencyIndex.openSession();
        this.artifactDependencyIndex.addDependency(GUIDFactory.createGUID(hash + ":" + artifactPath), GUIDFactory.createGUID(hash + 2 + ":" + artifactPath + 2), DependencyScope.BUILD, null);
        this.artifactDependencyIndex.closeSession();
    }


}
