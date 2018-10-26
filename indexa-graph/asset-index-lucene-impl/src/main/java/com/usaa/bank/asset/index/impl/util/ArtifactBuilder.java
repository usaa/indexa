package com.usaa.bank.asset.index.impl.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import com.usaa.bank.asset.index.api.domain.asset.artifact.Artifact;
import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * Utility class to facilitate the construction of Artifact related objects using lucene-document.
 */
public class ArtifactBuilder {

    /**
     * Create artifact from the given lucene-document. This method will parse the lucene document to IArtifact object.
     *
     * @param document - Lucene document
     * @return - Artifact containing the parsed content.
     */
    public static IArtifact createArtifact(Document document) {
        BasicValidator.validateNotNull("document", document);
        Map<String, Set<String>> fieldMap = ArtifactBuilder.getFieldMap(document);
        IArtifact artifact =
                new Artifact(
                        ArtifactBuilder.getFirstValueString(Artifact.KEY_HASH, fieldMap),
                        ArtifactBuilder.getFirstValueString(Artifact.KEY_ARTIFACT_PATH, fieldMap));
        artifact.addTags(fieldMap.get(DigitalAsset.KEY_TAGS));
        fieldMap.remove(DigitalAsset.KEY_TAGS);
        artifact.addFields(fieldMap);
        return artifact;
    }

    /**
     * Create artifacts for the given set of lucene objects. This method will parse all the given set of lucene documents to of IArtifact object
     *
     * @param documents - Set of lucene documents
     * @return - Set of artifacts for given documents
     */
    public static Set<IArtifact> createArtifacts(Set<Document> documents) {
        BasicValidator.validateNotNull("documents", documents);
        Set<IArtifact> artifacts = new HashSet<IArtifact>();

        for (Document document : documents) {
            artifacts.add(ArtifactBuilder.createArtifact(document));
        }
        return artifacts;
    }


    /**
     * Generate the Field HashMap data from the given document. <br>
     * Gather all the fields from the document and put into the hashmap, for multiple entries for a key we use Set collection for insertion to eliminate duplication.
     *
     * @param document - Lucene document
     * @return - Field Map data collected from the document.
     */
    public static Map<String, Set<String>> getFieldMap(Document document) {
        Map<String, Set<String>> map = null;

        if (document != null) {
            map = new HashMap<String, Set<String>>();

            for (IndexableField field : document.getFields()) {

                if (!map.containsKey(field.name())) {
                    map.put(field.name(), new HashSet<String>());
                }
                map.get(field.name()).add(field.stringValue());
            }

        }
        return map;
    }

    /**
     * Get the first value of the given "key" from the fieldMap data. FileMap has Set collection as value for each key. We return the first value present in the Set collection.
     *
     * @param key      - Key to be searched
     * @param fieldMap - Map data to be searched in
     * @return - First entry in the Set for the given "key"
     */
    public static String getFirstValueString(String key, Map<String, Set<String>> fieldMap) {
        String value = "";
        if (fieldMap != null) {
            if (fieldMap.get(key) != null && fieldMap.get(key).size() > 0) {
                Iterator<String> it = fieldMap.get(key).iterator();
                value = it.next();
            }
        }
        return value;
    }

}