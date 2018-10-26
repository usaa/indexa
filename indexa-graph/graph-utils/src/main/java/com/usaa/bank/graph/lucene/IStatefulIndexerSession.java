package com.usaa.bank.graph.lucene;

/**
 * Interface that specifies the various operations associated with Lucene indexer's session.
 */
public interface IStatefulIndexerSession {
    /**
     * Open a session with Lucene indexer.
     */
    void openSession();

    /**
     * Open a read-only session with Lucene indexer.
     */
    void openReadOnlySession();

    /**
     * Flush the session's data for a Lucene indexer.
     */
    void flushSession();

    /**
     * Close a session with Lucene indexer.
     */
    void closeSession();
}