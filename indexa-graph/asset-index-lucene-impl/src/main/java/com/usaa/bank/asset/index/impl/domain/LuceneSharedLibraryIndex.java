package com.usaa.bank.asset.index.impl.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.shared.library.ISharedLibrary;
import com.usaa.bank.asset.index.api.domain.asset.shared.library.ISharedLibraryIndex;
import com.usaa.bank.asset.index.api.domain.asset.shared.library.SharedLibrary;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;

import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.asset.index.impl.util.SharedLibraryBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import com.usaa.bank.graph.common.validate.BasicValidator;

/**
 * This class is an implementation to modify the lucene-indexes associated with the shared-library.<br>
 * This implements the interface ISharedLibraryIndex which outlines the specs.
 */
public class LuceneSharedLibraryIndex extends RealtimeLuceneIndex implements ISharedLibraryIndex {
    private static final String DEFAULT_INDEX_FILE_PATH = ".sharedLib.index";
    private static final String[] keys = {SharedLibrary.KEY_GUID, SharedLibrary.KEY_ID, SharedLibrary.KEY_NAME};

    public LuceneSharedLibraryIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }

    @Override
    public ISharedLibrary create(String id, String name) {

        BasicValidator.validateNotNull("id", id);
        BasicValidator.validateNotNull("name", name);
        String guid = this.getGuid(id, name).toString();
        Document doc = this.createSharedLibraryDoc(guid, id, name);
        this.getLuceneIndexDAO().saveDocument(SharedLibrary.KEY_GUID, guid, doc);
        return SharedLibraryBuilder.createSharedLibrary(doc);
    }

    @Override
    public boolean remove(String id, String name) {

        BasicValidator.validateNotNull("id", id);
        BasicValidator.validateNotNull("name", name);
        Map<String, String> fields = new HashMap<>();
        fields.put(SharedLibrary.KEY_GUID, SharedLibrary.createGUID(id, name).toString());
        fields.put(SharedLibrary.KEY_ID, id);
        fields.put(SharedLibrary.KEY_NAME, name);
        return this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public ISharedLibrary create(Set<String> tags, Map<String, Set<String>> fields) {

        BasicValidator.validateNotNull("fields", fields);
        try {
            Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);
            String guid = SharedLibrary.createGUID(fields).toString();
            Document doc = DocumentBuilder.createDocument(guid, tags, fields);
            this.getLuceneIndexDAO().saveDocument(SharedLibrary.KEY_GUID, guid, doc);
            return SharedLibraryBuilder.createSharedLibrary(doc);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public ISharedLibrary get(String id, String name) {

        BasicValidator.validateNotNull("id", id);
        BasicValidator.validateNotNull("name", name);
        ISharedLibrary result = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(SharedLibrary.KEY_GUID, SharedLibrary.createGUID(id, name).toString());
        fields.put(SharedLibrary.KEY_ID, id);
        fields.put(SharedLibrary.KEY_NAME, name);
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            result = SharedLibraryBuilder.createSharedLibrary(new ArrayList<>(resultList).get(0));
        }
        return result;
    }

    @Override
    public boolean save(ISharedLibrary digitalAsset) {

        BasicValidator.validateNotNull("digitalAsset", digitalAsset);
        String id = digitalAsset.getId();
        String name = digitalAsset.getName();
        String guid = this.getGuid(id, name).toString();
        return this.getLuceneIndexDAO().saveDocument(SharedLibrary.KEY_GUID, guid, this.createSharedLibraryDoc(guid, id, name));
    }

    @Override
    public Set<ISharedLibrary> find(String name) {
        if (StringUtils.isBlank(name)) {
            return SharedLibraryBuilder.createSharedLibrarys(this.getLuceneIndexDAO().findDocuments(null));
        }
        Map<String, String> fields = new HashMap<>();
        fields.put(SharedLibrary.KEY_NAME, name);
        return SharedLibraryBuilder.createSharedLibrarys(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public boolean remove(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        Map<String, String> fields = new HashMap<>();
        try {
            fields.put(SharedLibrary.KEY_GUID, guid.getStringValue());
            return this.getLuceneIndexDAO().deleteDocuments(fields);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public ISharedLibrary get(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        Map<String, String> fields = new HashMap<>();
        ISharedLibrary result = null;
        try {
            fields.put(SharedLibrary.KEY_GUID, guid.getStringValue());
            Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
            if (!resultList.isEmpty()) {
                result = SharedLibraryBuilder.createSharedLibrary(new ArrayList<>(resultList).get(0));
            }
            return result;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public Set<ISharedLibrary> find(String fieldKey, String fieldValue) {
        BasicValidator.validateNotNull("key", fieldKey);
        BasicValidator.validateNotNull("value", fieldValue);
        Map<String, String> fields = new HashMap<>();
        fields.put(fieldKey, fieldValue);
        return SharedLibraryBuilder.createSharedLibrarys(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<ISharedLibrary> find(Map<String, String> fields) {
        BasicValidator.validateNotNull("fields", fields);
        return SharedLibraryBuilder.createSharedLibrarys(this.getLuceneIndexDAO().findDocuments(fields));
    }

    public GUID getGuid(String id, String name) {
        return GUIDFactory.createGUID(id + SharedLibrary.GUID_DELIMITER + name);
    }

    private Document createSharedLibraryDoc(String guid, String id, String name) {
        Map<String, Set<String>> fields = new HashMap<>();
        Set<String> value = null;
        value = new HashSet<>();
        value.add(id);
        fields.put(SharedLibrary.KEY_ID, value);
        value = new HashSet<>();
        value.add(name);
        fields.put(SharedLibrary.KEY_NAME, value);
        return DocumentBuilder.createDocument(guid, null, fields);
    }

    @Override
    public Set<ISharedLibrary> find() {
        return SharedLibraryBuilder.createSharedLibrarys(this.getLuceneIndexDAO().findDocuments(null));
    }

    @Override
    public int getIndexSize() {
        return this.getLuceneIndexDAO().getDocumentCount();
    }

}