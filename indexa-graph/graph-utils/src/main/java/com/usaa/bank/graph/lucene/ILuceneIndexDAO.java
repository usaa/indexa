package com.usaa.bank.graph.lucene;

import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;

/**
 * ILuceneIndexDAO provides the interface for specifying various functions with a Lucene document. Several of the possible actions are saving, deleting, finding etc.
 */
public interface ILuceneIndexDAO extends IStatefulIndexerSession {
    // ===================================================================
    // The domain of indexing creates a separation between
    // readers and writers. In lucene, these are searchers
    // and writers. This DAO creates the more familiar DAO-ish interface
    // for developers to use and attempts to hide the details of
    // the searcher and write.
    // Calls are stateless meaning calling a find after a save does
    // not guarantee data will be reflected for the instance of the DAO
    // ===================================================================

    /**
     * Updates a document by first deleting the document(s) containing a term and then adding the new document. A Term is created by termFieldName, termFieldValue.
     *
     * @param termFieldName  - Field name of the index.
     * @param termFieldValue - Field value of the index.
     * @param document       - Lucene document to be updated.
     * @return - True for successful addition.
     */
    boolean saveDocument(String termFieldName, String termFieldValue, Document document);

    /**
     * Generates a BooleanQuery from the given fields Map and uses it to delete the documents matching in the IndexWriter.
     *
     * @param fields - A Map data structure containing a field(Key) and text(value) pair.
     * @return - True for successful deletion.
     */
    boolean deleteDocuments(Map<String, String> fields);

    /**
     * Find the documents in the IndexSearcher matching the Terms generated from fields.
     *
     * @param fields - A Map data structure contains the field(Key) and text(value).
     * @return - Collection of Documents obtained from the search.
     */
    Set<Document> findDocuments(Map<String, String> fields);

    /**
     * Find the documents in the IndexSearcher matching the Terms generated from fields. Offset is the starting position for the search and numberOfResults
     * is the length from offset position.
     *
     * @param fields          - A Map data structure containing a field(Key) and text(value) pair.
     * @param offset          - Starting position of the search.
     * @param numberOfResults - Number of documents to be searched from an offset position.
     * @return - Collection of Documents obtained from the search.
     */
    Set<Document> findDocuments(Map<String, String> fields, int offset, int numberOfResults);

    /**
     * Return the count of the documents present in the IndexSearcher.
     *
     * @return - Integer representing the count of documents.
     */
    int getDocumentCount();

}