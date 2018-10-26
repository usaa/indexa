package com.usaa.bank.asset.index.impl.domain;


import com.usaa.bank.asset.index.api.domain.asset.IAssetRepository;
import com.usaa.bank.asset.index.api.domain.asset.classpath.IManifestClasspathIndex;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactId;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifactIndex;
import com.usaa.bank.asset.index.api.domain.asset.dependency.IDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.HierarchicalPackage;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClass;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.JavaClassDependency;
import com.usaa.bank.asset.index.api.domain.asset.shared.library.ISharedLibrary;
import com.usaa.bank.asset.index.api.domain.asset.shared.library.ISharedLibraryDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.shared.library.ISharedLibraryIndex;
import com.usaa.bank.asset.index.api.domain.asset.gav.GroupId;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * This class is an implementation to modify the lucene-indexes associated with the asset-repository.<br>
 * This implements the interface IAssetRepository which outlines the specs.
 */
public class LuceneAssetRepository implements IAssetRepository //, IStatefulIndexerSession
{
    private IArtifactIndex artifactIndex;
    private IDependencyIndex artifactDependencyIndex;
    private IManifestClasspathIndex manifestClasspathIndex;
    private IJavaClassIndex javaClassIndex;
    private IJavaClassDependencyIndex javaClassDependencyIndex;
    private ISharedLibraryIndex sharedLibraryIndex;
    private ISharedLibraryDependencyIndex sharedLibraryDependencyIndex;

    /**
     * Creates a LuceneAssetRepository instance and also initialized the object variables with the given arguments.
     *
     * @param artifactIndex                Artifact-index part of the Asset
     * @param artifactDependencyIndex      Artifact-dependency-index part of the Asset
     * @param manifestClasspathIndex       Manifest-classpath-index part of the Asset
     * @param javaClassIndex               Java-class-index part of the Asset
     * @param javaClassDependencyIndex     Java-class-dependency-index part of the Asset
     * @param sharedLibraryIndex           Shared-library-index part of the Asset
     * @param sharedLibraryDependencyIndex Shared-dependency-dependency-index part of the Asset
     */
    public LuceneAssetRepository(
            IArtifactIndex artifactIndex,
            IDependencyIndex artifactDependencyIndex,
            IManifestClasspathIndex manifestClasspathIndex,
            IJavaClassIndex javaClassIndex,
            IJavaClassDependencyIndex javaClassDependencyIndex,
            ISharedLibraryIndex sharedLibraryIndex,
            ISharedLibraryDependencyIndex sharedLibraryDependencyIndex) {

        super();
        this.artifactIndex = artifactIndex;
        this.artifactDependencyIndex = artifactDependencyIndex;
        this.manifestClasspathIndex = manifestClasspathIndex;
        this.javaClassIndex = javaClassIndex;
        this.javaClassDependencyIndex = javaClassDependencyIndex;
        this.sharedLibraryIndex = sharedLibraryIndex;
        this.sharedLibraryDependencyIndex = sharedLibraryDependencyIndex;
    }

    @Override
    public IArtifact getArtifact(GUID guid) {
        artifactIndex.openReadOnlySession();
        IArtifact artifact = artifactIndex.get(guid);
        artifactIndex.closeSession();
        return artifact;
    }

    @Override
    public IArtifact getArtifactbyGUIDString(String guidString) {
        GUID guid = GUIDFactory.passGUID(guidString);
        return getArtifact(guid);
    }

    @Override
    public IArtifact getArtifact(String artifactHash, String artifactPath) {
        artifactIndex.openReadOnlySession();
        IArtifact artifact = artifactIndex.get(artifactHash, artifactPath);
        artifactIndex.closeSession();
        return artifact;
    }

    @Override
    public IArtifact getArtifact(String artifactHash) {
        artifactIndex.openReadOnlySession();
        IArtifact artifact = artifactIndex.get(artifactHash);
        artifactIndex.closeSession();
        return artifact;
    }

    @Override
    public Set<IArtifact> findArtifacts(GroupId groupId, ArtifactId artifactId, Version version, String classifier) {
        artifactIndex.openReadOnlySession();
        Set<IArtifact> artifacts = artifactIndex.find(groupId, artifactId, version, classifier);
        artifactIndex.closeSession();
        return artifacts;
    }

    public Set<IArtifact> findArtifacts(GroupId groupId) {
        artifactIndex.openReadOnlySession();
        Set<IArtifact> artifacts = artifactIndex.find(groupId);
        artifactIndex.closeSession();
        return artifacts;
    }

    public Set<IArtifact> findArtifacts(GroupId groupId, ArtifactId artifactId) {
        artifactIndex.openReadOnlySession();
        Set<IArtifact> artifacts = artifactIndex.find(groupId, artifactId);
        artifactIndex.closeSession();
        return artifacts;
    }

    public Set<IArtifact> findArtifacts(GroupId groupId, ArtifactId artifactId, Version version) {
        artifactIndex.openReadOnlySession();
        Set<IArtifact> artifacts = artifactIndex.find(groupId, artifactId, version);
        artifactIndex.closeSession();
        return artifacts;
    }

    @Override
    public Set<IArtifact> findArtifactsByHash(String hash) {
        artifactIndex.openReadOnlySession();
        Set<IArtifact> artifacts = artifactIndex.find(hash);
        artifactIndex.closeSession();
        return artifacts;
    }

    @Override
    public Set<IArtifact> findArtifactsByType(ArtifactType artifactType) {
        artifactIndex.openReadOnlySession();
        Set<IArtifact> artifacts = artifactIndex.find(artifactType);
        artifactIndex.closeSession();
        return artifacts;
    }

    @Override
    public Set<IArtifact> findArtifactsBySharedLibrary(String sharedLibraryId) {
        Set<IArtifact> artifacts = new HashSet<>();
        sharedLibraryIndex.openReadOnlySession();
        GUID guid = sharedLibraryIndex.find("name", sharedLibraryId).iterator().next().getGUID();
        sharedLibraryIndex.closeSession();
        sharedLibraryDependencyIndex.openReadOnlySession();
        Set<String> artifactPaths = sharedLibraryDependencyIndex.findArtifactPaths(guid);
        sharedLibraryDependencyIndex.closeSession();
        artifactIndex.openReadOnlySession();

        for (String path : artifactPaths) {
            artifacts.addAll(artifactIndex.find("artifactPath", path));
        }
        artifactIndex.closeSession();
        return artifacts;
    }

    @Override
    public Set<IArtifact> findArtifactsByClass(HierarchicalPackage hierarchicalPackage, String className) {
        Set<IArtifact> artifacts = new HashSet<>();
        javaClassIndex.openReadOnlySession();
        Set<IJavaClass> classes = javaClassIndex.find(hierarchicalPackage, className);
        javaClassIndex.closeSession();
        artifactIndex.openReadOnlySession();
        for (IJavaClass javaClass : classes) artifacts.addAll(artifactIndex.find(javaClass.getArtifactHash()));
        artifactIndex.closeSession();
        return artifacts;
    }

    @Override
    public Set<IArtifact> findArtifactDependencies(GroupId groupId, ArtifactId artifactId, Version version) {
        return findArtifactDependencies(getHashfromGAV(groupId, artifactId, version, artifactIndex));
    }

    @Override
    public Set<IArtifact> findArtifactDependencies(String hash) {
        Set<IArtifact> artifactDependencies = new HashSet<>();
        Set<GUID> guids = new HashSet<>();
        artifactIndex.openReadOnlySession();
        Set<IArtifact> artifacts = new HashSet<>();
        artifacts.addAll(artifactIndex.find(hash));
        if (artifacts.size() > 0) {
            artifactDependencyIndex.openReadOnlySession();
            for (IArtifact artifact : artifacts) {
                guids.addAll(artifactDependencyIndex.findDependencies(artifact.getGUID(), null, null));
            }
            artifactDependencyIndex.closeSession();
            if (guids.size() > 0) {
                for (GUID guid : guids) {
                    artifactDependencies.add(artifactIndex.get(guid));
                }
            }
            artifactIndex.closeSession();
        }
        return artifactDependencies;

    }

    @Override
    public Set<IArtifact> findArtifactDependents(GroupId groupId, ArtifactId artifactId, Version version) {
        return findArtifactDependents(getHashfromGAV(groupId, artifactId, version, artifactIndex));
    }

    @Override
    public Set<IArtifact> findArtifactDependents(String hash) {
        Set<IArtifact> artifactDependents = new HashSet<>();
        Set<GUID> guids = new HashSet<>();
        artifactIndex.openReadOnlySession();
        Set<IArtifact> artifacts = artifactIndex.find(hash);
        artifactDependencyIndex.openReadOnlySession();
        for (IArtifact artifact : artifacts) {
            guids.addAll(artifactDependencyIndex.findDependents(artifact.getGUID(), null, null));
        }
        artifactDependencyIndex.closeSession();
        for (GUID guid : guids) {
            artifactDependents.add(artifactIndex.get(guid));
        }
        artifactIndex.closeSession();
        return artifactDependents;
    }

    @Override
    public IJavaClass getJavaClass(GUID guid) {
        javaClassIndex.openReadOnlySession();
        IJavaClass javaClass = javaClassIndex.get(guid);
        javaClassIndex.closeSession();
        return javaClass;
    }

    @Override
    public IJavaClass getJavaClass(GroupId groupId, ArtifactId artifactId, Version version, HierarchicalPackage hierarchicalPackage, String className) {
        return getJavaClass(getHashfromGAV(groupId, artifactId, version, artifactIndex), hierarchicalPackage, className);

    }

    @Override
    public IJavaClass getJavaClass(String hash, HierarchicalPackage hierarchicalPackage, String className) {
        javaClassIndex.openReadOnlySession();
        IJavaClass javaClass = javaClassIndex.get(hash, hierarchicalPackage, className);
        javaClassIndex.closeSession();
        return javaClass;
    }

    @Override
    public Set<IJavaClass> findJavaClasses(GroupId groupId, ArtifactId artifactId, Version version, String classifier) {
        Set<IJavaClass> classes = new HashSet<>();
        artifactIndex.openReadOnlySession();
        Set<IArtifact> artifacts = artifactIndex.find(groupId, artifactId, version, classifier);
        if (artifacts != null) {
            Iterator<IArtifact> it = artifacts.iterator();
            if (it.hasNext()) {
                IArtifact artifact = it.next();
                javaClassIndex.openReadOnlySession();
                classes.addAll(javaClassIndex.find(artifact.getHash()));
                javaClassIndex.closeSession();
            }
        }
        artifactIndex.closeSession();

        return classes;
    }

    @Override
    public Set<IJavaClass> findJavaClasses(String hash) {
        javaClassIndex.openReadOnlySession();
        Set<IJavaClass> javaClasses = javaClassIndex.find(hash);
        javaClassIndex.closeSession();
        return javaClasses;
    }

    @Override
    public Set<JavaClassDependency> findJavaClassDependencies(GroupId groupId, ArtifactId artifactId, Version version, HierarchicalPackage hierarchicalPackage,
                                                              String className) {
        IJavaClass javaClass = getJavaClass(groupId, artifactId, version, hierarchicalPackage, className);
        javaClassDependencyIndex.openReadOnlySession();
        Set<JavaClassDependency> javaClassDependencies = javaClassDependencyIndex.findDependencies(javaClass.getGUID());
        javaClassDependencyIndex.closeSession();
        return javaClassDependencies;
    }

    @Override
    public Set<JavaClassDependency> findJavaClassDependencies(String hash, HierarchicalPackage hierarchicalPackage, String className) {
        IJavaClass javaClass = getJavaClass(hash, hierarchicalPackage, className);
        javaClassDependencyIndex.openReadOnlySession();
        Set<JavaClassDependency> javaClassDependencies = javaClassDependencyIndex.findDependencies(javaClass.getGUID());
        javaClassDependencyIndex.closeSession();
        return javaClassDependencies;
    }

    @Override
    public Set<ISharedLibrary> findSharedLibraries() {
        sharedLibraryIndex.openReadOnlySession();
        Set<ISharedLibrary> sharedLibraries = sharedLibraryIndex.find("");
        sharedLibraryIndex.closeSession();
        return sharedLibraries;
    }

    private static String getHashfromGAV(GroupId groupId, ArtifactId artifactId, Version version, IArtifactIndex artifactIndex) {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("groupId", groupId.toString());
        fields.put("artifactId", artifactId.toString());
        fields.put("version", version.toString());
        artifactIndex.openReadOnlySession();

        String hash = "";
        if (artifactIndex.find(fields).iterator().hasNext()) {
            hash = artifactIndex.find(fields).iterator().next().getHash();
        }
        artifactIndex.closeSession();
        return hash;
    }
}