package com.usaa.bank.asset.index.api.domain.web.services.soap.services;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SoapServiceArtifact extends DigitalAsset implements ISoapServiceArtifact, Serializable {
    private static final String GUID_DELIMITER = ":";
    public static final String archiveName = "archiveName";
    public static final String contextRoot = "contextRoot";
    public static final String serviceDefinitionType = "serviceDefinitionType";
    public static final String serviceName = "serviceName";
    public static final String implementationClass = "implementationClass";
    public static final String interfaceClass = "interfaceClass";
    public static final String operationName = "operationName";
    public static final String SOAP_ARTIFACT_PATH = "artifactPath";
    public static final String WAS_EAR_MAP_EAR_NAME = "wasEarMapEarName";
    public static final String WAS_EAR_MAP_WAR_NAME = "wasEarMapWarName";
    public static final String WAS_EAR_MAP_LOB = "wasEarMapLob";
    public static final String WAS_EAR_MAP_CONTEXT_ROOT = "wasEarMapContextRoot";


    public SoapServiceArtifact(Set<String> tags, Map<String, Set<String>> fields) {
        super(SoapServiceArtifact.createGUID(
                DigitalAsset.getFirstValueString(SoapServiceArtifact.archiveName, fields),
                DigitalAsset.getFirstValueString(SoapServiceArtifact.serviceName, fields),
                DigitalAsset.getFirstValueString(SoapServiceArtifact.operationName, fields)));

        this.addTags(tags);
        this.addFields(fields);

    }

    public SoapServiceArtifact(GUID guid) {
        super(guid);
    }

    public SoapServiceArtifact(String archiveName, String contextRoot, String serviceDefinitionType, String serviceName, String implementationClass, String interfaceClass, String operationName, String artifactPath, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        super(SoapServiceArtifact.createGUID(archiveName, serviceName, operationName));
        this.setArchiveName(archiveName);
        this.setContextRoot(contextRoot);
        this.setServiceDefinitionType(serviceDefinitionType);
        this.setServiceName(serviceName);
        this.setImplementationClass(implementationClass);
        this.setInterfaceClass(interfaceClass);
        this.setOperationName(operationName);
        this.setArtifactPath(artifactPath);
        this.setMappingData(mappingData);
        this.setJvmComponentMappingData(jvmComponentMapping);
    }

    public SoapServiceArtifact(GUID guid, String archiveName, String contextRoot, String serviceDefinitionType, String serviceName, String implementationClass, String interfaceClass, String operationName, String artifactPath, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        super(guid);
        this.setArchiveName(archiveName);
        this.setContextRoot(contextRoot);
        this.setServiceDefinitionType(serviceDefinitionType);
        this.setServiceName(serviceName);
        this.setImplementationClass(implementationClass);
        this.setInterfaceClass(interfaceClass);
        this.setOperationName(operationName);
        this.setArtifactPath(artifactPath);
        this.setMappingData(mappingData);
        this.setJvmComponentMappingData(jvmComponentMapping);
    }

    public String getArchiveName() {
        return this.getFieldValue(SoapServiceArtifact.archiveName);
    }


    private void setArchiveName(String archiveName) {
        if (StringUtils.isNotEmpty(archiveName)) {
            this.addField(SoapServiceArtifact.archiveName, archiveName);
        }
    }

    public String getContextRoot() {
        return this.getFieldValue(SoapServiceArtifact.contextRoot);
    }


    private void setContextRoot(String contextRoot) {
        if (StringUtils.isNotEmpty(contextRoot)) {
            this.addField(SoapServiceArtifact.contextRoot, contextRoot);
        }
    }

    public String getServiceDefinitionType() {
        return this.getFieldValue(SoapServiceArtifact.serviceDefinitionType);
    }


    private void setServiceDefinitionType(String serviceDefinitionType) {
        if (StringUtils.isNotEmpty(serviceDefinitionType)) {
            this.addField(SoapServiceArtifact.serviceDefinitionType, serviceDefinitionType);
        }
    }

    public String getServiceName() {
        return this.getFieldValue(SoapServiceArtifact.serviceName);
    }


    private void setServiceName(String serviceName) {
        if (StringUtils.isNotEmpty(serviceName)) {
            this.addField(SoapServiceArtifact.serviceName, serviceName);
        }
    }

    public String getInterfaceClass() {
        return this.getFieldValue(SoapServiceArtifact.interfaceClass);
    }


    private void setInterfaceClass(String interfaceClass) {
        if (StringUtils.isNotEmpty(interfaceClass)) {
            this.addField(SoapServiceArtifact.interfaceClass, interfaceClass);
        }
    }

    public String getOperationName() {
        return this.getFieldValue(SoapServiceArtifact.operationName);
    }


    private void setOperationName(String operationName) {
        if (StringUtils.isNotEmpty(operationName)) {
            this.addField(SoapServiceArtifact.operationName, operationName);
        }
    }

    private void setArtifactPath(String artifactPath) {
        if (StringUtils.isNotEmpty(artifactPath)) {
            this.addField(SoapServiceArtifact.SOAP_ARTIFACT_PATH, artifactPath);
        }
    }

    public String getImplementationClass() {
        return this.getFieldValue(SoapServiceArtifact.implementationClass);
    }


    private void setImplementationClass(String archiveName) {
        if (StringUtils.isNotEmpty(implementationClass)) {
            this.addField(SoapServiceArtifact.implementationClass, implementationClass);
        }
    }

    private void setMappingData(Map<String, String> mappingData) {
        if (mappingData != null && !mappingData.isEmpty()) {
            for (Map.Entry<String, String> e : mappingData.entrySet()) {
                this.addField(e.getKey(), e.getValue());
            }

        }
    }

    private void setJvmComponentMappingData(Map<String, String> jvmComponentMap) {
        if (jvmComponentMap != null && !jvmComponentMap.isEmpty()) {
            for (Map.Entry<String, String> e : jvmComponentMap.entrySet()) {
                this.addField(e.getKey(), e.getValue());
            }

        }
    }


    public static GUID createGUID(String archiveName, String serviceName, String operationName) {
        GUID guid = null;
        if (StringUtils.isNotEmpty(archiveName) && StringUtils.isNotEmpty(serviceName) && StringUtils.isNotEmpty(operationName)) {
            guid = GUIDFactory.createGUID(archiveName + GUID_DELIMITER + serviceName + GUID_DELIMITER + operationName);
        }
        return guid;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

    @Override
    public boolean equals(Object o) {
        ISoapServiceArtifact object = null;

        if (!(o instanceof ISoapServiceArtifact)) {
            return false;
        } else {
            object = (ISoapServiceArtifact) o;
        }

        if (!StringUtils.equals(this.getContextRoot(), object.getContextRoot())) {
            return false;
        }

        if (!StringUtils.equals(this.getArchiveName(), object.getArchiveName())) {
            return false;
        }

        if (!StringUtils.equals(this.getImplementationClass(), object.getImplementationClass())) {
            return false;
        }


        if (!StringUtils.equals(this.getServiceDefinitionType(), object.getServiceDefinitionType())) {
            if (!StringUtils.equals(this.getInterfaceClass(), object.getInterfaceClass())) {
                if (!StringUtils.equals(this.getOperationName(), object.getOperationName())
                        && !StringUtils.equals(this.getOperationName().toLowerCase().replace("\"", ""), object.getOperationName().toLowerCase().replace("\"", ""))) {
                    return false;
                }
            }
        }

        if (!StringUtils.equals(this.getOperationName(), object.getOperationName())
                && !StringUtils.equals(this.getOperationName().toLowerCase().replace("\"", ""), object.getOperationName().toLowerCase().replace("\"", ""))) {
            return false;
        }

        if (!StringUtils.equals(this.getServiceName(), object.getServiceName())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        String operationName = getOperationName().toLowerCase().replace("\"", "");
        return Objects.hash(getContextRoot(), getArchiveName(), getImplementationClass(), operationName, getServiceName());
    }

//    public static void main(String[] args){
//        SoapServiceArtifact soapServiceArtifact1 = new SoapServiceArtifact("a","b","w","d","e","g","\"e\"",null,null);
//        SoapServiceArtifact soapServiceArtifact2 = new SoapServiceArtifact("a","b","c","d","e","f","e",null,null);
//        System.out.println(soapServiceArtifact1.equals(soapServiceArtifact2));
//        HashSet<ISoapServiceArtifact> hashSet = new HashSet();
//        hashSet.add(soapServiceArtifact1);
//        hashSet.add(soapServiceArtifact2);
//        System.out.println(hashSet.size());
//    }
}
