package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.metrics.IMetrics;
import com.usaa.bank.asset.index.api.domain.metrics.IMetricsIndex;
import com.usaa.bank.asset.index.api.domain.metrics.Metrics;
import com.usaa.bank.asset.index.api.domain.web.services.soap.services.SoapServiceArtifact;
import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.asset.index.impl.util.LuceneIndexUtil;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.common.validate.BasicValidator;
import com.usaa.bank.graph.lucene.ILuceneIndexDAO;
import com.usaa.bank.graph.lucene.IndexUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LuceneMetricsIndex extends RealtimeLuceneIndex implements IMetricsIndex {
    private static final String KEY_GUID = "guid";
    private static final String[] keys = {Metrics.REST_RESOURCE_THIRD_PARTY, Metrics.REST_RESOURCE_DeMa, Metrics.SOAP_SERVICE_THIRD_PARTY, Metrics.SOAP_SERVICE_DeMa, Metrics.TOTAL_REST_RESOURCE, Metrics.TOTAL_SOAP_SERVICES};

    public LuceneMetricsIndex(ILuceneIndexDAO luceneIndexDAO) {
        super(luceneIndexDAO);
    }


    @Override
    public IMetrics create(String thirdPartyRestResource, String usaaRestResource, String thirdPartySoapService, String usaaSoapService, String totalRestService, String totalSoapService) {

        IMetrics metric = new Metrics(thirdPartyRestResource, usaaRestResource, thirdPartySoapService, usaaSoapService, totalRestService, totalSoapService);
        if (this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, metric.getGUID().getStringValue(), DocumentBuilder.createDocument(metric))) {
            return metric;
        }
        return null;
    }

    @Override
    public IMetrics get(String archiveName) {
        BasicValidator.validateNotNull("archiveName", archiveName);
        Document returnDoc = new Document();
        IMetrics result = null;

        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(null);
        if (archiveName.equalsIgnoreCase("soapResource")) {
            for (Document d : resultList) {
                for (IndexableField field : d.getFields()) {
                    if (field.name().equalsIgnoreCase(Metrics.TOTAL_SOAP_SERVICES)) {
                        returnDoc.add(field);
                    }

                    if (field.name().equalsIgnoreCase(Metrics.SOAP_SERVICE_DeMa)) {
                        returnDoc.add(field);
                    }

                    if (field.name().equalsIgnoreCase(Metrics.SOAP_SERVICE_THIRD_PARTY)) {
                        returnDoc.add(field);
                    }
                }
                result = LuceneMetricsIndex.createMetricsSet(returnDoc);
            }
        }

        if (archiveName.equalsIgnoreCase("restResource")) {
            for (Document d : resultList) {
                for (IndexableField field : d.getFields()) {
                    if (field.name().equalsIgnoreCase(Metrics.TOTAL_REST_RESOURCE)) {
                        returnDoc.add(field);
                    }

                    if (field.name().equalsIgnoreCase(Metrics.REST_RESOURCE_DeMa)) {
                        returnDoc.add(field);
                    }

                    if (field.name().equalsIgnoreCase(Metrics.REST_RESOURCE_THIRD_PARTY)) {
                        returnDoc.add(field);
                    }
                }
                result = LuceneMetricsIndex.createMetricsSet(returnDoc);
            }
        }

        return result;
    }


    @Override
    public IMetrics create(Set<String> tags, Map<String, Set<String>> fields) {
        BasicValidator.validateNotNull("fields", fields);
        try {
            Set<Document> docs = null;
            Map<String, Set<String>> cleanFields = IndexUtil.cleanFields(keys, fields);
            String guid = GUIDFactory.createGUID(DigitalAsset.getFirstValueString(SoapServiceArtifact.KEY_GUID, fields)).toString();
            docs.add(DocumentBuilder.createDocument(guid, tags, fields));
            this.getLuceneIndexDAO().saveDocument(SoapServiceArtifact.KEY_GUID, guid, docs.iterator().next());
            return LuceneMetricsIndex.createMetricsSet(docs).iterator().next();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public boolean save(IMetrics digitalAsset) {
        BasicValidator.validateNotNull("digitalAsset", digitalAsset);
        BasicValidator.validateNotNull("guid", digitalAsset.getGUID());
        return this.getLuceneIndexDAO().saveDocument(DigitalAsset.KEY_GUID, digitalAsset.getGUID().getStringValue(), DocumentBuilder.createDocument(digitalAsset));
    }

    @Override
    public boolean remove(GUID guid) {
        return LuceneIndexUtil.remove(getLuceneIndexDAO(),guid);
    }

    @Override
    public IMetrics get(GUID guid) {
        BasicValidator.validateNotNull("guid", guid);
        IMetrics result = null;
        Map<String, String> fields = new HashMap<>();
        fields.put(LuceneMetricsIndex.KEY_GUID, guid.getStringValue());
        Set<Document> resultList = this.getLuceneIndexDAO().findDocuments(fields);

        if (!resultList.isEmpty()) {
            result = LuceneMetricsIndex.createMetricsSet(resultList).iterator().next();
        }

        return result;
    }

    @Override
    public Set<IMetrics> find() {
        return LuceneMetricsIndex.createMetricsSet((this.getLuceneIndexDAO().findDocuments(null)));
    }

    @Override
    public Set<IMetrics> find(String fieldKey, String fieldValue) {
        BasicValidator.validateNotNull("fieldValue", fieldValue);
        Map<String, String> fields = new HashMap<>();
        fields.put(fieldKey, fieldValue);
        return this.find(fields);
    }

    @Override
    public Set<IMetrics> find(Map<String, String> fields) {
        BasicValidator.validateNotNull("fields", fields);
        Set<IMetrics> results = null;
        Set<Document> documents = this.getLuceneIndexDAO().findDocuments(fields);
        if (documents != null) {
            results = LuceneMetricsIndex.createMetricsSet(documents);
        }
        return results;
    }

    @Override
    public int getIndexSize() {
        return getLuceneIndexDAO().findDocuments(null).size();

    }

    private static Set<IMetrics> createMetricsSet(Set<Document> documents) {
        Set<IMetrics> metricsArtifacts = new HashSet<>();
        if (documents != null) {
            for (Document document : documents) {

                metricsArtifacts.add(
                        new Metrics(
                                GUIDFactory.passGUID(document.get(KEY_GUID)),
                                document.get(Metrics.REST_RESOURCE_THIRD_PARTY),
                                document.get(Metrics.REST_RESOURCE_DeMa),
                                document.get(Metrics.SOAP_SERVICE_THIRD_PARTY),
                                document.get(Metrics.SOAP_SERVICE_DeMa),
                                document.get(Metrics.TOTAL_REST_RESOURCE),
                                document.get(Metrics.TOTAL_SOAP_SERVICES)
                        ));
            }
        }
        return metricsArtifacts;
    }

    private static IMetrics createMetricsSet(Document document) {
        if (document != null) {
            return new Metrics(
                    GUIDFactory.passGUID(document.get(KEY_GUID)),
                    document.get(Metrics.REST_RESOURCE_THIRD_PARTY),
                    document.get(Metrics.REST_RESOURCE_DeMa),
                    document.get(Metrics.SOAP_SERVICE_THIRD_PARTY),
                    document.get(Metrics.SOAP_SERVICE_DeMa),
                    document.get(Metrics.TOTAL_REST_RESOURCE),
                    document.get(Metrics.TOTAL_SOAP_SERVICES)
            );

        }
        return null;
    }


}


