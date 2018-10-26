package com.usaa.bank.asset.index.api.domain.asset.shared.library;

import com.usaa.bank.asset.index.api.domain.asset.IAssetIndex;

import java.util.Set;

/**
 * This interface provides the basic capabilities of an shared library's lucene-indexes.
 */
public interface ISharedLibraryIndex extends IAssetIndex<ISharedLibrary> {
    /**
     * Creates a SharedLibrary implemented instance from the given Id and Name. A new GUID is created for the ServerConfig from the given id and name.
     *
     * @param id   - Guid of shared-library
     * @param name - Name of shared-library
     * @return - A ServerConfig instance
     */
    ISharedLibrary create(String id, String name);

    /**
     * Removes a SharedLibrary from its lucene-indexes. Lucene-indexes are associated with its luceneIndexerDao.
     * A field is created, it is a Map with id,name as its entries with its corresponding predefined key.<br>
     * Using the field this function will delete the entry of the SharedLibrary found. All the matching documents will be deleted.
     *
     * @param id   - Guid of shared-library
     * @param name - Name of shared-library
     * @return - True if deletion is successful
     */
    boolean remove(String id, String name);

    /**
     * Gets SharedLibrary that matches the given id and name using the lucene-indexes.
     * A field is created, it is a Map with id,name as its entries with its corresponding predefined key.<br>
     * Using the field this function will search the indexes of the SharedLibrary. Only the first matching document will be returned.
     *
     * @param id   - Guid of shared-library
     * @param name - Name of shared-library
     * @return - A SharedLibrary matched
     */
    ISharedLibrary get(String id, String name);

    /**
     * Gets SharedLibrary that matches the given name using the lucene-indexes.
     * A field is created, it is a Map with name as its entries with its corresponding predefined key.<br>
     * Using the field this function will search the indexes of the SharedLibrary. All the matching document will be returned.
     *
     * @param name - Name of shared-library
     * @return - Set of SharedLibrary(s) matched
     */
    Set<ISharedLibrary> find(String name);
}