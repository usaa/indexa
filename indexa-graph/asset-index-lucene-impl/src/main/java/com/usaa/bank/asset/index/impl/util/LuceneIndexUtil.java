package com.usaa.bank.asset.index.impl.util;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.validate.BasicValidator;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import org.apache.lucene.document.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class LuceneIndexUtil {
    public static boolean remove(ILuceneIndexDAO luceneIndexDAO, GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(DigitalAsset.KEY_GUID, guid.getStringValue());
        luceneIndexDAO.deleteDocuments(fields);
        return true;
    }

    public static IArtifact get(ILuceneIndexDAO luceneIndexDAO, GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        IArtifact digitalAsset = null;
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(DigitalAsset.KEY_GUID, guid.getStringValue());
        Set<Document> documents = luceneIndexDAO.findDocuments(fields);
        if (documents != null) {
            for (Document document : documents) {
                digitalAsset = ArtifactBuilder.createArtifact(document);
                break;
            }
        }
        return digitalAsset;
    }


    public static Set<IArtifact> find(ILuceneIndexDAO luceneIndexDAO, String fieldKey, String fieldValue) {
        BasicValidator.validateNotNull("fieldKey", fieldKey);
        BasicValidator.validateNotNull("fieldValue", fieldValue);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put(fieldKey, fieldValue);
        return find(luceneIndexDAO, fields);
    }


    public static Set<IArtifact> find(ILuceneIndexDAO luceneIndexDAO, Map<String, String> fields) {
        BasicValidator.validateNotNull("fields", fields);
        Set<IArtifact> digitalAssets = null;
        Set<Document> documents = luceneIndexDAO.findDocuments(fields);
        if (documents != null) {
            digitalAssets = new HashSet<IArtifact>();
            for (Document document : documents) {
                digitalAssets.add(ArtifactBuilder.createArtifact(document));
            }
        }
        return digitalAssets;
    }
}
