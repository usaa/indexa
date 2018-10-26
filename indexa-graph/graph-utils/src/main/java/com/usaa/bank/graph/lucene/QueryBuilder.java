package com.usaa.bank.graph.lucene;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

/**
 * QueryBuilder is used to create queries.  These are used in querying the Lucene Indexes.
 */
public class QueryBuilder {
    /**
     * Creates a BooleanQuery based on the fields provided. The Map data given will be used to generate the terms for the query.
     *
     * @param fields - Map containing data for the query.
     * @return - A BooleanQuery.
     */
    protected static BooleanQuery createQuery(Map<String, String> fields) {
        BooleanQuery bq = null ;
        if (fields != null && fields.size() > 0) {
            for (String key : fields.keySet()) {
                if (StringUtils.isNotEmpty(fields.get(key))) {
                    bq = new BooleanQuery.Builder().add(new BooleanClause(new TermQuery(new Term(key, fields.get(key))), BooleanClause.Occur.MUST)).build();
                }
            }
        }
        return bq;
    }
}
