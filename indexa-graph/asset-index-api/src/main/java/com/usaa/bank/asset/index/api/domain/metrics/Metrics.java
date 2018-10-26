package com.usaa.bank.asset.index.api.domain.metrics;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class Metrics extends DigitalAsset implements IMetrics, Serializable {
    private static final String GUID_DELIMITER = ":";
    public static final String REST_RESOURCE_DeMa = "internalRestResources";
    public static final String REST_RESOURCE_THIRD_PARTY = "thirdPartyRestResources";
    public static final String SOAP_SERVICE_DeMa = "internalSoapServices";
    public static final String SOAP_SERVICE_THIRD_PARTY = "thirdPartySoapServices";
    public static final String TOTAL_REST_RESOURCE = "totalRestResources";
    public static final String TOTAL_SOAP_SERVICES = "totalSoapServices";


    public Metrics(String thirdPartyRestResource, String companyRestResource, String thirdPartySoapService, String companySoapService, String totalRestService, String totalSoapService) {
        super(Metrics.createGUID(thirdPartyRestResource, companyRestResource, thirdPartySoapService, companySoapService));
        this.setThirdPartyRestResource(thirdPartyRestResource);
        this.setDeMaRestResource(companyRestResource);
        this.setThirdPartySoapService(thirdPartySoapService);
        this.setDeMaSoapService(companySoapService);
        setTotalRestResources(totalRestService);
        setTotalSoapService(totalSoapService);

    }

    public Metrics(GUID guid, String thirdPartyRestResource, String companyRestResource, String thirdPartySoapService, String companySoapService, String totalRestService, String totalSoapService) {
        super(guid);
        this.setThirdPartyRestResource(thirdPartyRestResource);
        this.setDeMaRestResource(companyRestResource);
        this.setThirdPartySoapService(thirdPartySoapService);
        this.setDeMaSoapService(companySoapService);
        setTotalRestResources(totalRestService);
        setTotalSoapService(totalSoapService);
    }


    public static GUID createGUID(String a, String b, String c, String d) {
        GUID guid = null;
        if (StringUtils.isNotEmpty(a) && StringUtils.isNotEmpty(b) && StringUtils.isNotEmpty(c)) {
            guid = GUIDFactory.createGUID(a + GUID_DELIMITER + b + GUID_DELIMITER + c + GUID_DELIMITER + d);
        }
        return guid;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

    private void setThirdPartySoapService(String thirdPartySoapService) {
        if (StringUtils.isNotEmpty(thirdPartySoapService)) {
            this.addField(Metrics.SOAP_SERVICE_THIRD_PARTY, thirdPartySoapService);
        }
    }

    private void setDeMaSoapService(String companySoapService) {
        if (StringUtils.isNotEmpty(companySoapService)) {
            this.addField(Metrics.SOAP_SERVICE_DeMa, companySoapService);
        }
    }

    private void setThirdPartyRestResource(String thirdPartyRestResource) {
        if (StringUtils.isNotEmpty(thirdPartyRestResource)) {
            this.addField(Metrics.REST_RESOURCE_THIRD_PARTY, thirdPartyRestResource);
        }
    }

    private void setDeMaRestResource(String companyRestResource) {
        if (StringUtils.isNotEmpty(companyRestResource)) {
            this.addField(Metrics.REST_RESOURCE_DeMa, companyRestResource);
        }
    }

    private void setTotalRestResources(String totalRestResources) {
        if (StringUtils.isNotEmpty(totalRestResources)) {
            this.addField(Metrics.TOTAL_REST_RESOURCE, totalRestResources);
        }
    }

    private void setTotalSoapService(String totalSoapService) {
        if (StringUtils.isNotEmpty(totalSoapService)) {
            this.addField(Metrics.TOTAL_SOAP_SERVICES, totalSoapService);
        }
    }

    @Override
    public String getThirdPartySoapService() {
        return this.getFieldValue(Metrics.SOAP_SERVICE_THIRD_PARTY);
    }

    @Override
    public String getDeMaSoapService() {
        return this.getFieldValue(Metrics.SOAP_SERVICE_DeMa);
    }

    @Override
    public String getThirdPartyRestResource() {
        return this.getFieldValue(Metrics.REST_RESOURCE_THIRD_PARTY);
    }

    @Override
    public String getDeMaRestResource() {
        return this.getFieldValue(Metrics.REST_RESOURCE_DeMa);
    }

    @Override
    public String getTotalSoapService() {
        return this.getFieldValue(Metrics.TOTAL_SOAP_SERVICES);
    }

    @Override
    public String getTotalRestResources() {
        return this.getFieldValue(Metrics.TOTAL_REST_RESOURCE);
    }
}

