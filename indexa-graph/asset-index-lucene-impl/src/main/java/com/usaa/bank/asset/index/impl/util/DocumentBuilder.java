package com.usaa.bank.asset.index.impl.util;

import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexableField;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.asset.IDigitalAsset;
import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * Utility class that does the construction of data-models containing asset metadata into lucene documents.
 */
public class DocumentBuilder {

    /**
     * Create lucene docuemnt for the given digital-asset. Digital-asset is just a data-model that contains the data such as artifact, dependency,  etc.
     *
     * @param digitalAsset - Asset
     * @return - Lucene document created
     */
    public static Document createDocument(IDigitalAsset digitalAsset) {
        BasicValidator.validateNotNull("digitalAsset", digitalAsset);
        return DocumentBuilder.createDocument(digitalAsset.getGUID().getStringValue(), digitalAsset.getTags(), digitalAsset.getFields());
    }


    /**
     * Create a lucene document from the given user data. Essentially this is just parsing the data from object and creating a lucene document for further processing.<br>
     * Guid represent the unique identification of the document, tags and fields are the various data associated with the asset.
     *
     * @param guid   - Guid of the asset
     * @param tags   - Tags associated with asset
     * @param fields - Fields containing all data for the asset
     * @return - Lucene document
     */
    public static Document createDocument(String guid, Set<String> tags, Map<String, Set<String>> fields) {
        Document document = new Document();
        if (guid != null) {
            document.add(new StringField(DigitalAsset.KEY_GUID, guid, Field.Store.YES));
//			document.add(new Field(DigitalAsset.KEY_GUID, guid, Field.Store.YES, Field.Index.NOT_ANALYZED));
        }

        if (tags != null) {
            for (String tag : tags) {
                document.add(new StringField(DigitalAsset.KEY_TAGS, tag, Field.Store.YES));
//				document.add(new Field(DigitalAsset.KEY_TAGS, tag, Field.Store.YES, Field.Index.NOT_ANALYZED));
            }
        }

        if (fields != null) {
            Set<String> fieldValues = null;
            for (String key : fields.keySet()) {
                fieldValues = fields.get(key);
                if (fieldValues != null) {
                    for (String value : fieldValues) {
                        document.add(new StringField(key, value, Field.Store.YES));
//						document.add(new Field(key, value, Field.Store.YES, Field.Index.NOT_ANALYZED));
                    }
                }
            }
        }
        return document;
    }

    /**
     * Crate a lucene document from the given user data. Essentially this is just parsing the data from object and creating a lucene document for further processing. <br>
     * Guid represent the unique identification of the document and fields are the various data associated with the asset. Tags will be null for the document created.
     *
     * @param guid   - Guid of the asset
     * @param fields - Fields containing all data for the asset
     * @return - Lucene document
     */
    public static Document createDocument(String guid, Map<String, Set<String>> fields) {
        return DocumentBuilder.createDocument(guid, null, fields);
    }

    /**
     * Makes a copy of the given lucene  document and returns a copy of it preserving
     *
     * @param document - lucene document to be copied
     * @return - copy of the document
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
     * Adds a field to the  lucene document provided. The new field added to the document will have the key from 'field-name' and value from 'fieldValue'. <br>
     *
     * @param fieldName  -
     * @param fieldValue -
     * @param document   - lucene document to be updated
     * @return - updated lucene document
     */
    public static Document addField(String fieldName, String fieldValue, Document document) {
        BasicValidator.validateNotNull("fieldName", fieldName);
        BasicValidator.validateNotNull("document", document);
        document.add(new StringField(fieldName, fieldValue, Field.Store.YES));
        return document;
    }
}