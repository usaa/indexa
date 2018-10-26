package com.usaa.bank.asset.index.api.domain.web.services.rest.resources;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class RestServiceArtifact extends DigitalAsset implements IRestServiceArtifact, Serializable {
    private static final String GUID_DELIMITER = ":";
    public static final String archiveName = "archiveName";
    public static final String contextRoot = "contextRoot";
    public static final String httpMethod = "httpMethod";
    public static final String restServicePath = "restServicePath";
    public static final String restOperationPath = "restOperationPath";
    public static final String restOperationMethod = "restOperationMethod";
    public static final String restRolesAllowed = "restRolesAllowed";
    public static final String restArtifactPath = "restArtifactPath";


    public RestServiceArtifact(Set<String> tags, Map<String, Set<String>> fields) {
        super(RestServiceArtifact.createGUID(
                DigitalAsset.getFirstValueString(RestServiceArtifact.archiveName, fields),
                DigitalAsset.getFirstValueString(RestServiceArtifact.restServicePath, fields) + "/" +
                        DigitalAsset.getFirstValueString(RestServiceArtifact.restOperationPath, fields),
                DigitalAsset.getFirstValueString(RestServiceArtifact.httpMethod, fields)));
        this.addTags(tags);
        this.addFields(fields);

    }

    public RestServiceArtifact(GUID guid) {
        super(guid);
    }

    public RestServiceArtifact(String archiveName, String contextRoot, String httpMethod, String restServicePath, String restOperationPath, String restOperationMethod,
                               String artifactPath, String rolesAllowed, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        super(RestServiceArtifact.createGUID(archiveName, restServicePath + "/" + restOperationPath, httpMethod));
        this.setArchiveName(archiveName);
        this.setContextRoot(contextRoot);
        this.sethttpMethod(httpMethod);
        this.setRestServicePath(restServicePath);
        this.setRestOperationPath(restOperationPath);
        this.setRestOperationMethod(restOperationMethod);
        this.setArtifactPath(artifactPath);
        this.setRestRolesAllowed(rolesAllowed);
        this.setMappingData(mappingData);
        this.setJvmComponentMappingData(jvmComponentMapping);
    }

    public RestServiceArtifact(GUID guid, String archiveName, String contextRoot, String httpMethod, String restServicePath, String restOperationPath, String restOperationMethod,
                               String artifactPath, String rolesAllowed, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        super(guid);
        this.setArchiveName(archiveName);
        this.setContextRoot(contextRoot);
        this.sethttpMethod(httpMethod);
        this.setRestServicePath(restServicePath);
        this.setRestOperationPath(restOperationPath);
        this.setRestOperationMethod(restOperationMethod);
        this.setArtifactPath(artifactPath);
        this.setRestRolesAllowed(rolesAllowed);
        this.setMappingData(mappingData);
        this.setJvmComponentMappingData(jvmComponentMapping);
    }

    public String getArchiveName() {
        return this.getFieldValue(RestServiceArtifact.archiveName);
    }


    private void setArchiveName(String archiveName) {
        if (StringUtils.isNotEmpty(archiveName)) {
            this.addField(RestServiceArtifact.archiveName, archiveName);
        }
    }

    public String getContextRoot() {
        return this.getFieldValue(RestServiceArtifact.contextRoot);
    }

    private void setContextRoot(String contextRoot) {
        if (StringUtils.isNotEmpty(contextRoot)) {
            this.addField(RestServiceArtifact.contextRoot, contextRoot);
        }
    }

    public String gethttpMethod() {
        return this.getFieldValue(RestServiceArtifact.httpMethod);
    }

    private void sethttpMethod(String httpMethod) {
        if (StringUtils.isNotEmpty(httpMethod)) {
            this.addField(RestServiceArtifact.httpMethod, httpMethod);
        }
    }

    public String getRestServicePath() {
        return this.getFieldValue(RestServiceArtifact.restServicePath);
    }


    private void setRestServicePath(String restServicePath) {
        if (StringUtils.isNotEmpty(restServicePath)) {
            this.addField(RestServiceArtifact.restServicePath, restServicePath);
        }
    }

    public String getRestOperationPath() {
        return this.getFieldValue(RestServiceArtifact.restOperationPath);
    }


    private void setRestOperationPath(String restOperationPath) {
        if (StringUtils.isNotEmpty(restOperationPath)) {
            this.addField(RestServiceArtifact.restOperationPath, restOperationPath);
        }
    }

    public String getRestOperationMethod() {

        return this.getFieldValue(RestServiceArtifact.restOperationMethod);
    }

    public String getRolesAllowed() {
        return this.getFieldValue(RestServiceArtifact.restRolesAllowed);
    }

    @Override
    public String getArtifactPath() {
        return this.getFieldValue(RestServiceArtifact.restArtifactPath);
    }

    private void setRestOperationMethod(String restOperationMethod) {
        if (StringUtils.isNotEmpty(restOperationMethod)) {
            this.addField(RestServiceArtifact.restOperationMethod, restOperationMethod);
        }
    }

    private void setRestRolesAllowed(String restRolesAllowed) {
        if (StringUtils.isNotEmpty(restRolesAllowed)) {
            this.addField(RestServiceArtifact.restRolesAllowed, restRolesAllowed);
        }
    }


    private void setArtifactPath(String artifactPath) {
        if (StringUtils.isNotEmpty(artifactPath)) {
            this.addField(RestServiceArtifact.restArtifactPath, artifactPath);
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

    public static GUID createGUID(String archiveName, String restServiceOperationPath, String httpMethod) {
        GUID guid = null;
        if (StringUtils.isNotEmpty(archiveName) && StringUtils.isNotEmpty(restServiceOperationPath) && StringUtils.isNotEmpty(httpMethod)) {
            guid = GUIDFactory.createGUID(archiveName + GUID_DELIMITER + restServiceOperationPath + GUID_DELIMITER + httpMethod);
        }
        return guid;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

    public boolean equals(Object o) {
        IRestServiceArtifact object = null;

        if (!(o instanceof IRestServiceArtifact)) {
            return false;
        } else {
            object = (IRestServiceArtifact) o;
        }

        if (!StringUtils.equals(this.getArchiveName(), object.getArchiveName())) {
            return false;
        }

        if (!StringUtils.equals(this.getContextRoot(), object.getContextRoot())) {
            return false;
        }

        if (!StringUtils.equals(this.gethttpMethod(), object.gethttpMethod())) {
            return false;
        }

        if (!StringUtils.equals(this.getRestOperationMethod(), object.getRestOperationMethod())) {
            return false;
        }

        if (!StringUtils.equals(this.getRestOperationPath(), object.getRestOperationPath())) {
            return false;
        }

        if (!StringUtils.equals(this.getRestServicePath(), object.getRestServicePath())) {
            return false;
        }

        return true;
    }

}

