package com.usaa.bank.asset.index.api.domain.asset;

import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.java.classes.HierarchicalPackage;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactId;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClass;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.JavaClassDependency;
import com.usaa.bank.asset.index.api.domain.asset.shared.library.ISharedLibrary;
import com.usaa.bank.asset.index.api.domain.asset.gav.GroupId;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import com.usaa.bank.graph.common.identity.GUID;

/**
 * This interface provides the basic capabilities with asset repository's lucene-indexes.
 */
public interface IAssetRepository {
    /**
     * Searches for artifacts using the guid from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * Out of all artifacts that matches only the first artifact is returned.
     *
     * @param guid - Guid added to search query
     * @return - an artifact matched
     */
    IArtifact getArtifact(GUID guid);

    /**
     * Searches for artifacts using the guid String from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * Out of all artifacts that matches only the first artifact is returned.
     *
     * @param guidString - String added to search query
     * @return - an artifact matched
     */

    IArtifact getArtifactbyGUIDString(String guidString);

    /**
     * Searches for artifacts using the hash, artifactPath from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * Out of all artifacts that matches only the first artifact is returned.
     *
     * @param hash         - Hash used in search-query
     * @param artifactPath - ArtifactPath used in search-query
     * @return - an artifact matched
     */
    IArtifact getArtifact(String hash, String artifactPath);

    /**
     * Searches for artifacts using the artifactPath from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * Out of all artifacts that matches only the first artifact is returned.
     *
     * @param artifactPath - ArtifactPath used in search-query
     * @return - an artifact matched
     */
    IArtifact getArtifact(String artifactPath);

    /**
     * Finds artifacts using the groupId, artifactId, gav and classifier from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * All the matching artifacts will be returned.
     *
     * @param groupId    - GroupId used in search query
     * @param artifactId - ArtifactId used in search query
     * @param version    - Version used in search query
     * @param classifier - Classifier used in search query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> findArtifacts(GroupId groupId, ArtifactId artifactId, Version version, String classifier);


    /**
     * Finds artifacts using the groupId, artifactId and gav from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * All the matching artifacts will be returned.
     *
     * @param groupId    - GroupId used in search query
     * @param artifactId - ArtifactId used in search query
     * @param version    - Version used in search query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> findArtifacts(GroupId groupId, ArtifactId artifactId, Version version);

    /**
     * Finds artifacts using the groupId and artifactId from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * All the matching artifacts will be returned.
     *
     * @param groupId    - GroupId used in search query
     * @param artifactId - ArtifactId used in search query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> findArtifacts(GroupId groupId, ArtifactId artifactId);

    /**
     * Finds artifacts using the groupId from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * All the matching artifacts will be returned.
     *
     * @param groupId - GroupId used in search query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> findArtifacts(GroupId groupId);

    /**
     * Finds artifacts using the hash from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * All the matching artifacts will be returned.
     *
     * @param hash - Hash used in search-query
     * @return - an artifact matched
     */
    Set<IArtifact> findArtifactsByHash(String hash);

    /**
     * Finds artifacts using the artifactType from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * All the matching artifacts will be returned.
     *
     * @param artifactType - Type of artifact used in search-query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> findArtifactsByType(ArtifactType artifactType);

    /**
     * Finds artifacts using the sharedLibraryId from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * All the matching artifacts will be returned.
     *
     * @param sharedLibraryId - sharedLibraryId used in search-query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> findArtifactsBySharedLibrary(String sharedLibraryId);

    /**
     * Finds artifacts using the hierarchicalPackage, className from its artifactsIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * All the matching artifacts will be returned.
     *
     * @param hierarchicalPackage - hierarchicalPackage used in search query
     * @param className           - className used in search query
     * @return - Set of artifacts matched
     */
    Set<IArtifact> findArtifactsByClass(HierarchicalPackage hierarchicalPackage, String className);

    /**
     * Finds artifact's dependencies using the groupId, artifactId, gav from its artifactsIndex.Dependencies are artifacts that our artifact we are searching for depend on. <br>
     * Its opens a readOnly connection with the indexes of artifacts and searches. All the matching artifacts will be returned.
     *
     * @param groupId    - GroupId used in search query
     * @param artifactId - ArtifactId used in search query
     * @param version    - Version used in search query
     * @return - Set of artifacts Dependencies
     */
    Set<IArtifact> findArtifactDependencies(GroupId groupId, ArtifactId artifactId, Version version);

    /**
     * Finds artifact's dependencies using the hash from its artifactsIndex. Dependencies are artifacts that our artifact we are searching for depend on. <br>
     * Its opens a readOnly connection with the indexes of artifacts and searches. All the matching artifacts will be returned.
     *
     * @param hash - Hash used in search-query
     * @return - Set of artifacts Dependencies
     */
    Set<IArtifact> findArtifactDependencies(String hash);

    /**
     * Finds artifact's dependencies using the hash from its artifactsIndex. Dependents are artifacts that depend on our artifact we are searching. <br>
     * Its opens a readOnly connection with the indexes of artifacts and searches. All the matching artifacts will be returned.
     *
     * @param groupId    - GroupId used in search query
     * @param artifactId - ArtifactId used in search query
     * @param version    - Version used in search query
     * @return - Set of artifacts Dependents
     */
    Set<IArtifact> findArtifactDependents(GroupId groupId, ArtifactId artifactId, Version version);

    /**
     * Finds artifact's dependents using the hash from its artifactsIndex. Dependents are artifacts that depend on our artifact we are searching.<br>
     * Its opens a readOnly connection with the indexes of artifacts and searches. All the matching artifacts will be returned.
     *
     * @param hash - Hash used in search-query
     * @return - Set of artifacts Dependents
     */
    Set<IArtifact> findArtifactDependents(String hash);

    /**
     * Finds JavaClass using the guid from its javaClassIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * Out of all JavaClass that matches only the first JavaClass is returned.
     *
     * @param guid - Guid added to search query
     * @return - a java-class matched
     */
    IJavaClass getJavaClass(GUID guid);

    /**
     * Finds JavaClass using the groupId, artifactId, gav, hierarchicalPackage, className  from its javaClassIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * Out of all JavaClass that matches only the first JavaClass is returned.
     *
     * @param groupId             - GroupId used in search query
     * @param artifactId          - ArtifactId used in search query
     * @param version             - Version used in search query
     * @param hierarchicalPackage - hierarchicalPackage used in search query
     * @param className           - className used in search query
     * @return - a java-class matched
     */
    IJavaClass getJavaClass(GroupId groupId, ArtifactId artifactId, Version version, HierarchicalPackage hierarchicalPackage, String className);

    /**
     * Finds JavaClass using the hash, hierarchicalPackage, className from its javaClassIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * Out of all JavaClass that matches only the first JavaClass is returned.
     *
     * @param hash                - Hash used in search-query
     * @param hierarchicalPackage - hierarchicalPackage used in search query
     * @param className           - className used in search query
     * @return - a java-class matched
     */
    IJavaClass getJavaClass(String hash, HierarchicalPackage hierarchicalPackage, String className);

    /**
     * Finds JavaClass using the groupId, artifactId, gav, classifier from its javaClassIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * All the matching JavaClass(s) will be returned.
     *
     * @param groupId    - GroupId used in search query
     * @param artifactId - ArtifactId used in search query
     * @param version    - Version used in search query
     * @param classifier - Classifier used in search query
     * @return - Set of JavaClass matched
     */
    Set<IJavaClass> findJavaClasses(GroupId groupId, ArtifactId artifactId, Version version, String classifier);

    /**
     * Finds JavaClass using the hash from its javaClassIndex. Its opens a readOnly connection with the indexes of artifacts and searches.  <br>
     * All the matching JavaClass(s) will be returned.
     *
     * @param hash - Hash used in search-query
     * @return - Set of JavaClass matched
     */
    Set<IJavaClass> findJavaClasses(String hash);

    /**
     * Finds JavaClass dependencies using the groupId, artifactId, gav, classifier from its javaClassIndex. Dependencies are classes that our class we are searching for depend on.<br>
     * Its opens a readOnly connection with the indexes of artifacts and searches. All the matching JavaClass(s) will be returned.
     *
     * @param groupId             - GroupId used in search query
     * @param artifactId          - ArtifactId used in search query
     * @param version             - Version used in search query
     * @param hierarchicalPackage - HierarchicalPackage used in search query
     * @param className           - ClassName used in search query
     * @return - Set of JavaClass dependencies
     */
    Set<JavaClassDependency> findJavaClassDependencies(GroupId groupId, ArtifactId artifactId, Version version, HierarchicalPackage hierarchicalPackage, String className);

    /**
     * Finds JavaClass dependencies using the groupId, artifactId, gav, classifier from its javaClassIndex. Dependencies are classes that our class we are searching for depend on.<br>
     * Its opens a readOnly connection with the indexes of artifacts and searches. All the matching JavaClass(s) will be returned.
     *
     * @param hash                - Hash used in search-query
     * @param hierarchicalPackage - HierarchicalPackage used in search query
     * @param className           - ClassName used in search query
     * @return - Set of JavaClass dependencies
     */
    Set<JavaClassDependency> findJavaClassDependencies(String hash, HierarchicalPackage hierarchicalPackage, String className);

    /**
     * Find all the shared-libraries found in the sharedLibraryIndex. Returns all the indexes present in the indexer.
     *
     * @return Set of shared-library
     */
    Set<ISharedLibrary> findSharedLibraries();
}