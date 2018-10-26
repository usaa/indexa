package com.usaa.bank.asset.index.api.domain.asset.classloader;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.IStatefulIndexerSession;

import java.util.Set;

/**
 * This interface provides the basic capabilities of an class-loader library's lucene-indexes.
 */
public interface IClassLoaderLibraryIndex extends IStatefulIndexerSession {

    /**
     * Creates a new library and adds it to the lucene-index at the end. A new GUID is generated for ClassLoader using the classLoaderGUID and sharedLibraryName.<br>
     * A field is created, it is a Map with classLoaderGUID,sharedLibraryName as its entries with its corresponding predefined key. Field and GUID are stored in the lucene-document.<br>
     *
     * @param classLoaderGUID   - Guid of class-loader
     * @param sharedLibraryName - Shared library name
     * @return - Library that matched
     */
    boolean addLibrary(GUID classLoaderGUID, String sharedLibraryName);

    /**
     * Removes a Library from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao. A field is created, it is a Map with classLoaderGUID, sharedLibraryName as its entries with its corresponding predefined key.<br>
     * Using the field this function will delete the entry of the library found. Only the first found classloader will be deleted. <br>
     *
     * @param classLoaderGUID   - Guid of class-loader
     * @param sharedLibraryName - Shared library name
     */
    void removeLibrary(GUID classLoaderGUID, String sharedLibraryName);


    /**
     * Removes a Library from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao. A field is created, it is a Map with classLoaderGUID as its entries with its corresponding predefined key.<br>
     * Using the field this function will delete the entry of the Library found. All the found classloader will be deleted. <br>
     *
     * @param classLoaderGUID - Guid of class-loader
     */
    void removeLibraries(GUID classLoaderGUID);

    /**
     * Obtain all the class-loader-libraries associated with the lucene-indexer.
     *
     * @return - Set of class-loader-libraries matched
     */
    Set<ClassLoaderLibrary> findLibraries();

    /**
     * Find all the class-loader-libraries using the given name of sharedLibraryName.
     *
     * @param sharedLibraryName - Name of shared-library
     * @return - Set of class-loader-libraries matched
     */
    Set<ClassLoaderLibrary> findLibraries(String sharedLibraryName);

    /**
     * Find all the class-loader-libraries using the given name of classLoaderGUID.
     *
     * @param classLoaderGUID - Guid of class-loader
     * @return - Set of class-loader-libraries matched
     */
    Set<ClassLoaderLibrary> findLibraries(GUID classLoaderGUID);

    /**
     * Find all the class-loaders using the given name of sharedLibraryName.
     *
     * @param sharedLibraryName - Name of shared-library
     * @return - Set of ClassLoaders matched
     */
    Set<GUID> findClassLoaders(String sharedLibraryName);


    /**
     * Gets a single class-loader-library using the given name of classLoaderGUID and sharedLibraryName.
     *
     * @param classLoaderGUID   - Guid of class-loader
     * @param sharedLibraryName - Name of shared-library
     * @return - A class-loader-library matched
     */
    ClassLoaderLibrary getLibrary(GUID classLoaderGUID, String sharedLibraryName);
}
