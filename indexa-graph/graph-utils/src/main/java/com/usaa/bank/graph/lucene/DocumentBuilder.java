package com.usaa.bank.graph.lucene;

import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexableField;

import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * Document Builder is used for building Lucene documents for given data.
 */
public class DocumentBuilder {
    /**
     * Default key name for GUID.
     */
    public static final String KEY_GUID = "guid";

    /**
     * * Default key name for TAGS.
     */
    public static final String KEY_TAGS = "tags";

    /**
     * Create a Lucene Document with GUID, tags and fields data. Default keys will be used for each data.
     *
     * @param guidValue - Value of guid.
     * @param tags      - Value of tags.
     * @param fields    - Fields data.
     * @return -  Newly created Lucene document.
     */
    public static Document createDocument(String guidValue, Set<String> tags, Map<String, Set<String>> fields) {
        return DocumentBuilder.createDocument(KEY_GUID, guidValue, KEY_TAGS, tags, fields);
    }

    /**
     * Create a Lucene Document with GUID, tags and fields data. Users can specify the key as well for their data.
     *
     * @param guidKey   - Key for the GUID.
     * @param guidValue - Value of GUID.
     * @param tagsKey   - Key of Tags.
     * @param tags      - Value of Tags.
     * @param fields    - Fields data.
     * @return -  Newly created Lucene document.
     */
    public static Document createDocument(String guidKey, String guidValue, String tagsKey, Set<String> tags, Map<String, Set<String>> fields) {
        Document document = new Document();
        if (guidValue != null) {
            document.add(new StringField(guidKey, guidValue, Field.Store.YES));
        }

        if (tags != null) {
            for (String tag : tags) {
                document.add(new StringField(tagsKey, tag, Field.Store.YES));
            }
        }

        if (fields != null) {
            Set<String> fieldValues = null;
            for (String key : fields.keySet()) {
                fieldValues = fields.get(key);
                if (fieldValues != null) {
                    for (String value : fieldValues) {
                        document.add(new StringField(key, value, Field.Store.YES));
                    }
                }
            }
        }
        return document;
    }

    /**
     * Create a Lucene Document with GUID and Fields data.
     *
     * @param guid   - GUID value of the GAV.
     * @param fields - Fields data.
     * @return -  Newly created Lucene document.
     */
    public static Document createDocument(String guid, Map<String, Set<String>> fields) {
        return DocumentBuilder.createDocument(guid, null, fields);
    }

    /**
     * Copies the document's data into a new document.
     *
     * @param document - Document to be copied.
     * @return - Copied Document.
     */
    public static Document copyDocument(Document document) {
        BasicValidator.validateNotNull("document", document);
        Document copy = new Document();
        for (IndexableField field : document.getFields()) {
            String fieldName = field.name();
            for (String value : document.getValues(fieldName)) {
                copy.add(new StringField(fieldName, value, Field.Store.YES));
            }
        }
        return document;
    }

    /**
     * Add a field to the existing Lucene Document provided.
     *
     * @param fieldName  - Name of the field.
     * @param fieldValue - Value of the field.
     * @param document   - Document to be appended to.
     * @return - Document with appended data.
     */
    public static Document addField(String fieldName, String fieldValue, Document document) {
        BasicValidator.validateNotNull("fieldName", fieldName);
        BasicValidator.validateNotNull("document", document);
        document.add(new StringField(fieldName, fieldValue, Field.Store.YES));
        return document;
    }

}