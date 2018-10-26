package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IStatefulIndexerSession;

/**
 * This interface provides the basic capabilities of lucene-indexes at real-time such as open, close etc.
 */
public class RealtimeLuceneIndex implements IStatefulIndexerSession {

    private ILuceneIndexDAO luceneIndexDAO;

    /**
     * Creates a RealtimeLuceneIndex instance with a ILuceneIndexDAO object. ILuceneIndexDAO - specifying various functions with lucene-document.
     *
     * @param luceneIndexDAO - A lucene-index-dao
     */
    public RealtimeLuceneIndex(ILuceneIndexDAO luceneIndexDAO) {
        this.luceneIndexDAO = luceneIndexDAO;
    }

    /**
     * Get the Lucene-Index-Dao associated with a real-time-lucene-indexer. Using the luceneIndexDAO user can perform various tasks with the lucene-documents.
     *
     * @return - A lucene-indexer-dao
     */
    public ILuceneIndexDAO getLuceneIndexDAO() {
        return this.luceneIndexDAO;
    }

    @Override
    public void closeSession() {
        this.luceneIndexDAO.closeSession();
    }

    @Override
    public void flushSession() {
        this.luceneIndexDAO.flushSession();
    }

    @Override
    public void openSession() {
        this.luceneIndexDAO.openSession();
    }

    @Override
    public void openReadOnlySession() {
        this.luceneIndexDAO.openReadOnlySession();
    }


}