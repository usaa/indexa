package com.usaa.bank.asset.index.api.domain;

import com.usaa.bank.asset.index.api.domain.metrics.IMetrics;
import com.usaa.bank.asset.index.api.domain.metrics.Metrics;
import com.usaa.bank.asset.index.impl.domain.LuceneMetricsIndex;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LuceneMetricsIndexTest {

    private LuceneMetricsIndex metricsIndexer = null;
    private IMetrics metrics;

    private static GUID guid;

    private static final String soap_third_party = "10";
    private static final String soap_usaa = "5";
    private static final String total_soap = "15";

    private static final String rest_third_party = "3";
    private static final String rest_usaa = "12";
    private static final String total_rest = "15";

    private static final String rest_third_party_data_Set_two = "5";


    @Before
    public void runOnceBeforeClass() {
        this.metricsIndexer = new LuceneMetricsIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));

        metrics = new Metrics(rest_third_party, rest_usaa, soap_third_party, soap_usaa, total_rest, total_soap);
        guid = Metrics.createGUID(rest_third_party, rest_usaa, soap_third_party, soap_usaa);

        metricsIndexer.openSession();
    }

    @After
    public void tearDownAfterClass() {
        metricsIndexer.closeSession();
    }

    @Test
    public void createPresentationServicesArtifact() throws Exception {
        IMetrics metricsResult = metricsIndexer.create(rest_third_party, rest_usaa, soap_third_party, soap_usaa, total_rest, total_soap);
        assertEquals(metricsResult, metrics);
    }


    @Test
    public void createAndRemovePresentationServicesArtifact() throws Exception {
        metricsIndexer.create(rest_third_party, rest_usaa, soap_third_party, soap_usaa, total_rest, total_soap);
        metricsIndexer.flushSession();
        metricsIndexer.remove(guid);
        metricsIndexer.flushSession();
        assertEquals(0, metricsIndexer.find().size());
    }

    @Test
    public void getMetricsByGUID() {
        IMetrics obj = metricsIndexer.create(rest_third_party, rest_usaa, soap_third_party, soap_usaa, total_rest, total_soap);
        metricsIndexer.flushSession();
        IMetrics metricEntry = metricsIndexer.get(obj.getGUID());
        assertEquals(obj.getGUID(), metricEntry.getGUID());
    }

    @Test
    public void saveMetricsAndDelete() {
        IMetrics obj = metricsIndexer.create(rest_third_party_data_Set_two, rest_usaa, soap_third_party, soap_usaa, total_rest, total_soap);
        boolean isSaved = metricsIndexer.save(obj);
        metricsIndexer.flushSession();

        assertEquals(isSaved, true);
        metricsIndexer.remove(obj.getGUID());

    }

    @Test
    public void findMetricsByKeyValue() {
        metricsIndexer.create(rest_third_party, rest_usaa, soap_third_party, soap_usaa, total_rest, total_soap);
        metricsIndexer.flushSession();
        IMetrics metricEntry2 = metricsIndexer.get("soapResource");
        assertEquals("5", metricEntry2.getDeMaSoapService());
    }


    @Test
    public void findBySoapArchive() {
        metricsIndexer.create(rest_third_party, rest_usaa, soap_third_party, soap_usaa, total_rest, total_soap);
        metricsIndexer.flushSession();

        Set<IMetrics> metricsSet = metricsIndexer.find("thirdPartyRestResources", "3");
        assertEquals(1, metricsSet.size());
    }


    @Test
    public void findByRestArchive() {
        metricsIndexer.create(rest_third_party, rest_usaa, soap_third_party, soap_usaa, total_rest, total_soap);
        metricsIndexer.flushSession();
        IMetrics metricEntry = metricsIndexer.get("restResource");
        assertEquals("12", metricEntry.getDeMaRestResource());
    }

}
