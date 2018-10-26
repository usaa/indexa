package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.deployment.IDeploymentConfig;
import com.usaa.bank.asset.index.api.domain.asset.deployment.IDeploymentConfigIndex;
import com.usaa.bank.asset.index.api.domain.asset.deployment.DeploymentConfig;
import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.asset.index.impl.util.DeploymentConfigBuilder;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import com.usaa.bank.graph.common.validate.BasicValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * This class is an implementation to modify the lucene-indexes associated with the deployment-configuration.<br>
 * This implements the interface IDeploymentConfigIndex which outlines the specs.
 */
public class LuceneDeploymentConfigIndex extends RealtimeLuceneIndex implements IDeploymentConfigIndex {
    private static final String DEFAULT_INDEX_FILE_PATH = ".deployment.config.index";
    private static final String[] keys = {DeploymentConfig.KEY_GUID, DeploymentConfig.KEY_ID, DeploymentConfig.KEY_NAME};

    public LuceneDeploymentConfigIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }


    @Override
    public IDeploymentConfig create(String id, String name) {

        BasicValidator.validateNotNull("id", id);
        BasicValidator.validateNotNull("name", name);
        String guid = this.getGuid(id, name).toString();
        Document doc = this.createDeploymentConfigDoc(guid, id, name);
        this.getLuceneIndexDAO().saveDocument(DeploymentConfig.KEY_GUID, guid, doc);
        return DeploymentConfigBuilder.createDeploymentConfig(doc);
    }

    @Override
    public boolean remove(String id, String name) {

        BasicValidator.validateNotNull("id", id);
        BasicValidator.validateNotNull("name", name);
        Map<String, String> fields = new HashMap<>();
        fields.put(DeploymentConfig.KEY_GUID, DeploymentConfig.createGUID(id, name).toString());
        fields.put(DeploymentConfig.KEY_ID, id);
        fields.put(DeploymentConfig.KEY_NAME, name);
        return this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public IDeploymentConfig create(Set<String> tags, Map<String, Set<String>> fields) {

        BasicValidator.validateNotNull("fields", fields);
        try {
            Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);
            String guid = DeploymentConfig.createGUID(fields).toString();
            Document doc = DocumentBuilder.createDocument(guid, tags, fields);
            this.getLuceneIndexDAO().saveDocument(DeploymentConfig.KEY_GUID, guid, doc);
            return DeploymentConfigBuilder.createDeploymentConfig(doc);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public IDeploymentConfig get(String id, String name) {

        BasicValidator.validateNotNull("id", id);
        BasicValidator.validateNotNull("name", name);
        IDeploymentConfig result = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(DeploymentConfig.KEY_GUID, DeploymentConfig.createGUID(id, name).toString());
        fields.put(DeploymentConfig.KEY_ID, id);
        fields.put(DeploymentConfig.KEY_NAME, name);
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            result = DeploymentConfigBuilder.createDeploymentConfig(new ArrayList<>(resultList).get(0));
        }
        return result;
    }

    @Override
    public boolean save(IDeploymentConfig digitalAsset) {

        BasicValidator.validateNotNull("digitalAsset", digitalAsset);
        String id = digitalAsset.getId();
        String name = digitalAsset.getName();
        String guid = this.getGuid(id, name).toString();
        return this.getLuceneIndexDAO().saveDocument(DeploymentConfig.KEY_GUID, guid, this.createDeploymentConfigDoc(guid, id, name));
    }

    @Override
    public Set<IDeploymentConfig> find(String name) {
        if (StringUtils.isBlank(name)) {
            return DeploymentConfigBuilder.createDeploymentConfigs(this.getLuceneIndexDAO().findDocuments(null));
        }
        Map<String, String> fields = new HashMap<>();
        fields.put(DeploymentConfig.KEY_NAME, name);
        return DeploymentConfigBuilder.createDeploymentConfigs(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public boolean remove(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        Map<String, String> fields = new HashMap<>();
        fields.put(DeploymentConfig.KEY_GUID, guid.toString());
        return this.getLuceneIndexDAO().deleteDocuments(fields);
    }

    @Override
    public IDeploymentConfig get(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        IDeploymentConfig result = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(DeploymentConfig.KEY_GUID, guid.toString());
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);
        if (!resultList.isEmpty()) {
            result = DeploymentConfigBuilder.createDeploymentConfig(new ArrayList<>(resultList).get(0));
        }
        return result;
    }

    @Override
    public Set<IDeploymentConfig> find() {
        return DeploymentConfigBuilder.createDeploymentConfigs(this.getLuceneIndexDAO().findDocuments(null));
    }

    @Override
    public Set<IDeploymentConfig> find(String fieldKey, String fieldValue) {

        BasicValidator.validateNotNull("key", fieldKey);
        BasicValidator.validateNotNull("value", fieldValue);
        Map<String, String> fields = new HashMap<>();
        fields.put(fieldKey, fieldValue);
        return DeploymentConfigBuilder.createDeploymentConfigs(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public Set<IDeploymentConfig> find(Map<String, String> fields) {

        BasicValidator.validateNotNull("fields", fields);
        return DeploymentConfigBuilder.createDeploymentConfigs(this.getLuceneIndexDAO().findDocuments(fields));
    }

    @Override
    public int getIndexSize() {
        return this.getLuceneIndexDAO().getDocumentCount();
    }

    /**
     * Creates a new GUID using the id and name of the deployment config.
     *
     * @param id   - Id of deployment-config
     * @param name - Name of deployment-config
     * @return - newly GUID created
     */
    public GUID getGuid(String id, String name) {
        return GUIDFactory.createGUID(id + DeploymentConfig.GUID_DELIMITER + name);
    }

    /**
     * Creates a deployment-config document from the given guid,id and name.
     *
     * @param guid - Guid of deployment-config
     * @param id   - Id of deployment-config
     * @param name - Name of deployment-config
     * @return - A lucene document
     */
    private Document createDeploymentConfigDoc(String guid, String id, String name) {
        Map<String, Set<String>> fields = new HashMap<>();
        Set<String> value = null;
        value = new HashSet<>();
        value.add(id);
        fields.put(DeploymentConfig.KEY_ID, value);
        value = new HashSet<>();
        value.add(name);
        fields.put(DeploymentConfig.KEY_NAME, value);
        return DocumentBuilder.createDocument(guid, null, fields);
    }
}