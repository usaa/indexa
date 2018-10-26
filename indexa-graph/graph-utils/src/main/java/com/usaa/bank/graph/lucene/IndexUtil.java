package com.usaa.bank.graph.lucene;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.usaa.bank.graph.common.exception.DeMaRuntimeException;

/**
 * Utility class for accessing attributes of Lucene indexes.
 */
public class IndexUtil {
    /**
     * Gets the value of the field from the Field Map.
     *
     * @param field    - Field to be searched for.
     * @param fieldMap - Map containing fields.
     * @return Field Value from map.
     */
    public static String getFirstValueString(String field, Map<String, Set<String>> fieldMap) {
        String value = "";
        if (fieldMap != null && fieldMap.size() > 0 && fieldMap.containsKey(field)) {
            for (String fieldValue : fieldMap.get(field)) {
                value = fieldValue;
                break;
            }
        }
        return value;
    }

    /**
     * Creates a new hash map with certain data filtered in it. Data in the returing Map will have only "Keys" entries from the fields copied to the new Map.
     *
     * @param keys   - Array of keys.
     * @param fields - Fields Map data.
     * @return - Key entries fom Fields.
     */
    public static Map<String, Set<String>> cleanFields(String[] keys, Map<String, Set<String>> fields) {
        Map<String, Set<String>> cleanFields = new HashMap<>();
        for (String key : keys) {
            if (fields.containsKey(key)) {
                Set<String> values = fields.get(key);
                if (values != null) {
                    cleanFields.put(key, new HashSet<>(values));
                }
            }
        }
        return cleanFields;
    }

    /**
     * Creates a Set collection with only a single entry.
     *
     * @param value - Value to be entered in the Set.
     * @return - Set with a single entry.
     */
    public static Set<String> createSingleValueSet(String value) {
        if (StringUtils.isNotEmpty(value)) {
            Set<String> set = new HashSet<String>();
            set.add(value);
            return set;
        }
        return null;
    }

    /**
     * Creates a new Lucene Directory instance from 'indexDir' as directory-path.
     *
     * @param indexDir - Directory path.
     * @return - Lucene Directory object.
     */
    public static Directory openDirectory(File indexDir) {
        Directory indexDirectory = null;
        if (indexDir != null) {
            try {
                indexDirectory = FSDirectory.open(indexDir.toPath());
            } catch (IOException ioe) {
                throw new DeMaRuntimeException(ioe);
            }
        }
        return indexDirectory;
    }

    /**
     * Opens an IndexWriter in Lucene for the given Lucene directory.
     *
     * @param indexDirectory - Directory of Lucene.
     * @return - Index Writer for the directory.
     */
    public static IndexWriter openIndexWriter(Directory indexDirectory) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = new IndexWriter(indexDirectory, new IndexWriterConfig(new EnglishAnalyzer()));
            indexWriter.commit();
        } catch (IOException ioe) {
            throw new DeMaRuntimeException(ioe);
        }
        return indexWriter;
    }

    /**
     * Opens a SearchManger from the given Lucene directory. SearchManger is a utility class to safely share IndexSearcher instances across multiple threads.
     *
     * @param indexDirectory - Directory of Lucene.
     * @return - A Search Manager for the directory.
     */
    public static SearcherManager openSearcherManager(Directory indexDirectory) {
        SearcherManager searcherManager = null;
        if(indexDirectory != null) {
            try {
                searcherManager = new SearcherManager(indexDirectory, new SearcherFactory());
            } catch (IOException ioe) {
                throw new DeMaRuntimeException(ioe);
            }
        }
        return searcherManager;
    }

    /**
     * Opens a SearchManger from the given index-writer. SearchManger is a utility class to safely share IndexSearcher instances across multiple threads.
     *
     * @param indexWriter - Index writer of Lucene.
     * @return - A Search Manager for the index-writer.
     */
    public static SearcherManager openSearcherManager(IndexWriter indexWriter) {
        SearcherManager searcherManager = null;
        try {
            searcherManager = new SearcherManager(indexWriter, false, false, null);
        } catch (IOException ioe) {
            throw new DeMaRuntimeException(ioe);
        }
        return searcherManager;
    }

}