package com.usaa.bank.asset.index.api.domain.asset.classpath;

import java.util.Set;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.IStatefulIndexerSession;

/**
 * This interface provides the basic capabilities of an classpath classpath's lucene-indexes.
 */
public interface IManifestClasspathIndex extends IStatefulIndexerSession {

    /**
     * Adds a classPathEntry to the LuceneManifestClasspathIndex's lucene documents. A field is created, it is a Map with artifactGUID, classpathEntry as its entries with its corresponding predefined key.<br>
     * <p>
     * Creates a GUID from the given artifactGUID and classpathEntry. Using the field and GUID, updates to lucene-documents are done and saved.<br>
     *
     * @param artifactGUID   - Guid of artifact containing the class-path-entry
     * @param classpathEntry - Name of the class-path-entry
     * @return - True if addition was successful
     */
    boolean addClasspathEntry(GUID artifactGUID, String classpathEntry);

    /**
     * Removes the class-path-entry from the given dependent-artifact.
     * A field is created, it is a Map with dependentArtifactGUID, classpathEntry as its entries with its corresponding predefined key.<br>
     * Using the field this function will delete the entry matching in the indexes.
     *
     * @param dependentArtifactGUID - GUID of dependent-artifact for the class-path-entry
     * @param classpathEntry        - Name of the class-path-entry
     */
    void removeClasspathEntry(GUID dependentArtifactGUID, String classpathEntry);

    /**
     * Removes all lucene-indexes with the matching class-path-entry.
     * A field is created, it is a Map with classpathEntry as its entry with its corresponding predefined key.<br>
     * Using the field this function will delete the entry matching in the indexes.
     *
     * @param classpathEntry - Name of the class-path-entry
     */
    void removeClasspathEntries(String classpathEntry);

    /**
     * Finds all indexes with the given artifactGUID
     * A field is created, it is a Map with artifactGUID as its entry with its corresponding predefined key.<br>
     * Using the field this function will gather all the entry matching in the indexes.
     *
     * @param artifactGUID - Guid of artifact containing the class-path-entry
     * @return - Set of artifacts matched
     */
    Set<String> findClasspathEntries(GUID artifactGUID);

    /**
     * Removes all the artifact that has the given dependent-artifact.
     * A field is created, it is a Map with dependentArtifactGUID as its entry with its corresponding predefined key.<br>
     * Using the field this function will delete the entry matching in the indexes.
     *
     * @param dependentArtifactGUID - GUID of dependent-artifact for the class-path-entry
     */
    void removeArtifact(GUID dependentArtifactGUID);

    /**
     * Finds all the artifacts that has the classpathEntry.
     *
     * @param classpathEntry - Name of the class-path-entry
     * @return - Set of Artifacts matched
     */
    Set<GUID> findArtifacts(String classpathEntry);
}