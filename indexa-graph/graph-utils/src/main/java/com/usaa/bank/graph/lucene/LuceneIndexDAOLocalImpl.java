package com.usaa.bank.graph.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;

import com.usaa.bank.graph.common.exception.DeMaRuntimeException;
import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * Implementation of Lucene Indexer specific to local execution for dependency management.
 */
public class LuceneIndexDAOLocalImpl implements ILuceneIndexDAO {
    private static final int MAX_SEGMENTS_NO_CHANGE = 0;
    private Directory indexDirectory = null;
    private IndexSearcher indexSearcher;
    private IndexWriter indexWriter;
    private SearcherManager searcherManager;
    private boolean flushDeletes;
    private int maxSegments;

    /**
     * Creates a new LuceneIndexDAOLocalImpl instance with indexDirectory argument. The indexDirectory is a directory that contains Lucene documents.
     * This will be used by Index-writer for future tasks related with indexes.
     *
     * @param indexDirectory - Directory which is a flat list of files.
     * @param flushDeletes   - States whether a force merging of all segments that have deleted documents is applicable or not.
     * @param maxSegments    - Maximum number of segments left in the index after merging finishes.
     */
    public LuceneIndexDAOLocalImpl(Directory indexDirectory, boolean flushDeletes, int maxSegments) {
        this.indexDirectory = indexDirectory;
        this.flushDeletes = flushDeletes;
        this.maxSegments = maxSegments;
    }

    /**
     * Creates a new LuceneIndexDAOLocalImpl instance with indexDir argument. The indexDirectory is a directory that contains Lucene documents.
     * This will be used by Index-writer for future tasks related with indexes.
     *
     * @param indexDir     - Indexes directory path.
     * @param flushDeletes - States whether a force merging of all segments that have deleted documents is applicable or not.
     * @param maxSegments  - Maximum number of segments left in the index after merging finishes.
     */
    public LuceneIndexDAOLocalImpl(File indexDir, boolean flushDeletes, int maxSegments) {
        this(IndexUtil.openDirectory(indexDir), flushDeletes, maxSegments);
    }

    /**
     * Creates a new LuceneIndexDAOLocalImpl instance with indexDir argument. The indexDirectory is a directory that contains Lucene documents.
     * This will be used by Index-writer for future tasks related with indexes.
     *
     * @param indexDir - Indexes directory path.
     */
    public LuceneIndexDAOLocalImpl(File indexDir) {
        this(indexDir, false, MAX_SEGMENTS_NO_CHANGE);
    }

    /**
     * Creates a new LuceneIndexDAOLocalImpl instance with indexDir argument.
     *
     * @param indexDirectory - Directory which is a flat list of files.
     */
    public LuceneIndexDAOLocalImpl(Directory indexDirectory) {
        this(indexDirectory, false, MAX_SEGMENTS_NO_CHANGE);
    }


    public void openSession() {
        this.indexWriter = IndexUtil.openIndexWriter(this.indexDirectory);
        this.searcherManager = IndexUtil.openSearcherManager(indexWriter);
        try {
            this.searcherManager.maybeRefresh();
            this.indexSearcher = this.searcherManager.acquire();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void openReadOnlySession() {
        this.searcherManager = IndexUtil.openSearcherManager(this.indexDirectory);
        try {
            this.searcherManager.maybeRefresh();
            this.indexSearcher = this.searcherManager.acquire();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void flushSession() {
        try {
            if (this.indexWriter != null) {
                this.indexWriter.commit();
            }
        } catch (Exception ioe) {
            throw new DeMaRuntimeException(ioe);
        }

        try {
            if (this.indexSearcher != null) {
                this.searcherManager.release(this.indexSearcher);
            }
            this.searcherManager.maybeRefresh();
            this.indexSearcher = this.searcherManager.acquire();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void closeSession() {
        try {
            this.searcherManager.release(this.indexSearcher);
            this.searcherManager.close();
        } catch (Exception ioe) {
            throw new DeMaRuntimeException(ioe);
        }

        try {
            if (this.indexWriter != null) {
                if (flushDeletes) {
                    this.indexWriter.forceMergeDeletes();
                }
                if (this.maxSegments > MAX_SEGMENTS_NO_CHANGE) {
                    this.indexWriter.forceMerge(this.maxSegments);
                }

                this.indexWriter.close();
                this.indexWriter = null;
            }
        } catch (Exception ioe) {
            throw new DeMaRuntimeException(ioe);
        }
    }


    public boolean saveDocument(String termFieldName, String termFieldValue, Document document) {
        BasicValidator.validateNotNull("document", document);
        BasicValidator.validateNotNull("indexWriter", indexWriter);
        BasicValidator.validateNotNull("termFieldName", termFieldName);
        boolean status = false;

        List<Document> temp = new ArrayList<Document>();
        temp.add(document);
        try {
            this.indexWriter.updateDocument(new Term(termFieldName, termFieldValue), document);
            status = this.indexWriter.numDocs() != 0;
        } catch (IOException ioe) {
            throw new DeMaRuntimeException(ioe);
        }
        return status;
    }

    public boolean deleteDocuments(Map<String, String> fields) {
        BasicValidator.validateNotNull("fields", fields);
        return LuceneIndexDAOLocalImpl.executeDelete(QueryBuilder.createQuery(fields), this.indexWriter);
    }


    public Set<Document> findDocuments(Map<String, String> fields) {
        Set<Document> documents = null;
        if (indexSearcher != null) {
            Query query = null;
            if (fields != null && fields.size() > 0) {

                BooleanQuery.Builder bool = new BooleanQuery.Builder();
                for (String key : fields.keySet()) {
                    if (StringUtils.isNotEmpty(fields.get(key))) {
                        bool.add(new TermQuery(new Term(key, fields.get(key))), BooleanClause.Occur.MUST);
                    }
                }
                query = bool.build();
            } else {
                query = new MatchAllDocsQuery();
            }
            documents = LuceneIndexDAOLocalImpl.executeQuery(query, this.indexSearcher);
        }
        return documents;
    }

    public Set<Document> findDocuments(Map<String, String> fields, int offset, int numberOfResults) {
        Set<Document> documents = null;
        if (indexSearcher != null) {
            Query query = null;
            if (fields != null && fields.size() > 0) {


                BooleanQuery.Builder bool = new BooleanQuery.Builder();

                for (String key : fields.keySet()) {
                    if (StringUtils.isNotEmpty(fields.get(key))) {
                        bool.add(new BooleanClause(new TermQuery(new Term(key, fields.get(key))), BooleanClause.Occur.MUST));
                    }
                }
                query = bool.build();
            } else {
                query = new MatchAllDocsQuery();
            }
            documents = LuceneIndexDAOLocalImpl.executeQuery(query, offset, numberOfResults, this.indexSearcher);
        }
        return documents;
    }

    public int getDocumentCount() {
        int count = 0;
        if (this.indexSearcher != null) {
            count = getIndexSearcher().getIndexReader().maxDoc();
        }
        return count;
    }

    // ================================================================
    // UTILITY METHODS
    // ================================================================

    /**
     * Utility method for obtaining the count of query(s) from an indexSearcher of lucene.
     *
     * @param query           - Query to be executed.
     * @param indexerSearcher - Searcher for IndexReader.
     * @return - Integer representing the count of query(s).
     */
    private static int getQueryCount(Query query, IndexSearcher indexerSearcher) {
        int count = 0;
        try {
            TotalHitCountCollector collector = new TotalHitCountCollector();
            indexerSearcher.search(query, collector);
            count = collector.getTotalHits();
        } catch (IOException ioe) {
            throw new DeMaRuntimeException(ioe);
        }
        return count;
    }

    /**
     * Utility to execute a query and collect the data in the form of Lucene Document.
     *
     * @param query           - Query to be executed.
     * @param indexerSearcher - Searcher for IndexReader.
     * @return - Collection of Documents parsed from the indexSearcher.
     */
    private static Set<Document> executeQuery(Query query, IndexSearcher indexerSearcher) {
        Set<Document> returnDocs = new HashSet<Document>();
        try {
            ScoreDoc[] scoreDocs = null;
            TotalHitCountCollector collector = new TotalHitCountCollector();
            indexerSearcher.search(query, collector);
            if (collector.getTotalHits() > 0) {
                scoreDocs = indexerSearcher.search(query, collector.getTotalHits()).scoreDocs;
            }
            if (scoreDocs != null) {
                for (ScoreDoc hit : scoreDocs) {
                    returnDocs.add(indexerSearcher.doc(hit.doc));
                }
            }
        } catch (IOException ioe) {
            throw new DeMaRuntimeException(ioe);
        }
        return returnDocs;
    }

    /**
     * Utility to execute a query and collect only specific number of Lucene Documents. Offset is the starting position to gather data.
     *
     * @param query           - Query to be executed.
     * @param offest          - Offset position to start gathering the document.
     * @param numberOfResults - Number of results from the offset position.
     * @param indexerSearcher - Searcher for a IndexReader.
     * @return - Collection of Documents parsed from the indexSearcher.
     */
    private static Set<Document> executeQuery(Query query, int offest, int numberOfResults, IndexSearcher indexerSearcher) {
        Set<Document> returnDocs = new HashSet<Document>();
        try {
            TotalHitCountCollector thcCollector = new TotalHitCountCollector();
            indexerSearcher.search(query, thcCollector);
            if (thcCollector.getTotalHits() > 0) {

                TopScoreDocCollector tsdCollector = TopScoreDocCollector.create(thcCollector.getTotalHits());
                indexerSearcher.search(query, tsdCollector);

                TopDocs topDocs = tsdCollector.topDocs(offest, numberOfResults);
                if (topDocs != null && topDocs.scoreDocs != null) {
                    int i = 0;
                    for (ScoreDoc hit : topDocs.scoreDocs) {
                        returnDocs.add(indexerSearcher.doc(hit.doc));
                        i++;
                    }
                }
            }
        } catch (IOException ioe) {
            throw new DeMaRuntimeException(ioe);
        }
        return returnDocs;
    }

    /**
     * Deletes the document(s) matching any of the provided queries for the given IndexWriter. <br>
     * A BooleanQuery is a Query that matches documents matching boolean combinations of other queries,  e.g. TermQuerys, PhraseQuerys or other BooleanQuerys (Lucene Api).
     *
     * @param bq          - BooleanQuery to be executed.
     * @param indexWriter - IndexWriter of an index.
     * @return - (Boolean) True if the deletion was a success.
     */
    private static boolean executeDelete(BooleanQuery bq, IndexWriter indexWriter) {
        boolean success = false;
        try {
            indexWriter.deleteDocuments(bq);
            success = indexWriter.numDocs() != 0;
        } catch (IOException ioe) {
            throw new DeMaRuntimeException(ioe);
        }
        return success;
    }

    /**
     * Returns the Lucene IndexWriter.
     *
     * @return - IndexWriter object.
     */
    public IndexWriter getIndexWriter() {
        return this.indexWriter;
    }

    /**
     * Returns the Lucene IndexSearcher for a IndexReader.
     *
     * @return - IndexSearcher object.
     */
    public IndexSearcher getIndexSearcher() {
        return this.indexSearcher;
    }


}