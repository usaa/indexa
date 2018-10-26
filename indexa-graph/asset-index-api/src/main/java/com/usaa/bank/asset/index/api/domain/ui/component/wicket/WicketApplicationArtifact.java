package com.usaa.bank.asset.index.api.domain.ui.component.wicket;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class WicketApplicationArtifact extends DigitalAsset implements IWicketApplicationArtifact, Serializable {
    private static final String GUID_DELIMITER = ":";
    public static final String wicketApplicationName = "wicketApplicationName";
    public static final String wicketInfrastructureClass = "wicketInfrastructureClass ";
    public static final String wicketDisplayName = "wicketDisplayName";
    public static final String description = "description";
    public static final String wicketApplicationClass = "wicketApplicationClass";
    public static final String wicketApplicationArchiveName = "wicketApplicationArchiveName ";


    public WicketApplicationArtifact(Set<String> tags, Map<String, Set<String>> fields) {
        super(WicketApplicationArtifact.createGUID(
                DigitalAsset.getFirstValueString(WicketApplicationArtifact.wicketApplicationName, fields),
                DigitalAsset.getFirstValueString(WicketApplicationArtifact.wicketApplicationClass, fields)));
        this.addTags(tags);
        this.addFields(fields);
    }

    public WicketApplicationArtifact(GUID guid) {
        super(guid);
    }

    public WicketApplicationArtifact(String wicketApplicationName, String wicketInfrastructureClass, String wicketDisplayName, String description, String wicketApplicationClass, String wicketApplicationArchiveName, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        super(WicketApplicationArtifact.createGUID(wicketApplicationName, wicketApplicationClass));
        this.setWicketApplicationName(wicketApplicationName);
        this.setWicketInfrastructureClass(wicketInfrastructureClass);
        this.setwicketDisplayName(wicketDisplayName);
        this.setDescription(description);
        this.setWicketApplicationClass(wicketApplicationClass);
        this.setwicketApplicationArchiveName(wicketApplicationArchiveName);
        this.setMappingData(mappingData);
        this.setJvmComponentMappingData(jvmComponentMapping);
    }

    public WicketApplicationArtifact(GUID guid, String wicketApplicationName, String wicketInfrastructureClass, String wicketDisplayName, String description, String wicketApplicationClass, String wicketApplicationArchiveName, Map<String, String> mappingData, Map<String, String> jvmComponentMapping) {
        super(guid);
        this.setWicketApplicationName(wicketApplicationName);
        this.setWicketInfrastructureClass(wicketInfrastructureClass);
        this.setwicketDisplayName(wicketDisplayName);
        this.setDescription(description);
        this.setWicketApplicationClass(wicketApplicationClass);
        this.setwicketApplicationArchiveName(wicketApplicationArchiveName);
        this.setMappingData(mappingData);
        this.setJvmComponentMappingData(jvmComponentMapping);
    }

    public String getWicketApplicationName() {
        return this.getFieldValue(WicketApplicationArtifact.wicketApplicationName);
    }

    private void setWicketApplicationName(String wicketApplicationName) {
        if (StringUtils.isNotEmpty(wicketApplicationName)) {
            this.addField(WicketApplicationArtifact.wicketApplicationName, wicketApplicationName);
        }
    }

    public String getWicketInfrastructureClass() {
        return this.getFieldValue(WicketApplicationArtifact.wicketInfrastructureClass);
    }


    private void setWicketInfrastructureClass(String wicketInfrastructureClass) {
        if (StringUtils.isNotEmpty(wicketInfrastructureClass)) {
            this.addField(WicketApplicationArtifact.wicketInfrastructureClass, wicketInfrastructureClass);
        }
    }

    public String getWicketDisplayName() {
        return this.getFieldValue(WicketApplicationArtifact.wicketDisplayName);
    }


    private void setwicketDisplayName(String wicketDisplayName) {
        if (StringUtils.isNotEmpty(wicketDisplayName)) {
            this.addField(WicketApplicationArtifact.wicketDisplayName, wicketDisplayName);
        }
    }

    public String getDescription() {
        return this.getFieldValue(WicketApplicationArtifact.description);
    }


    private void setDescription(String description) {
        if (StringUtils.isNotEmpty(description)) {
            this.addField(WicketApplicationArtifact.description, description);
        }
    }

    public String getWicketApplicationClass() {
        return this.getFieldValue(WicketApplicationArtifact.wicketApplicationClass);
    }


    private void setWicketApplicationClass(String wicketApplicationClass) {
        if (StringUtils.isNotEmpty(wicketApplicationClass)) {
            this.addField(WicketApplicationArtifact.wicketApplicationClass, wicketApplicationClass);
        }
    }

    public String getwicketApplicationArchiveName() {
        return this.getFieldValue(WicketApplicationArtifact.wicketApplicationArchiveName);
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

    private void setwicketApplicationArchiveName(String wicketApplicationArchiveName) {
        if (StringUtils.isNotEmpty(wicketApplicationArchiveName)) {
            this.addField(WicketApplicationArtifact.wicketApplicationArchiveName, wicketApplicationArchiveName);
        }
    }


    public static GUID createGUID(String wicketApplicationName, String wicketApplicationClass) {
        GUID guid = null;
        if (StringUtils.isNotEmpty(wicketApplicationName) && StringUtils.isNotEmpty(wicketApplicationClass)) {
            guid = GUIDFactory.createGUID(wicketApplicationName + GUID_DELIMITER + wicketApplicationClass);
        }
        return guid;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

    public boolean equals(Object o) {
        IWicketApplicationArtifact object = null;

        if (!(o instanceof IWicketApplicationArtifact)) {
            return false;
        } else {
            object = (IWicketApplicationArtifact) o;
        }

        if (!StringUtils.equals(this.getDescription(), object.getDescription())) {
            return false;
        }

        if (!StringUtils.equals(this.getwicketApplicationArchiveName(), object.getwicketApplicationArchiveName())) {
            return false;
        }

        if (!StringUtils.equals(this.getWicketApplicationClass(), object.getWicketApplicationClass())) {
            return false;
        }

        if (!StringUtils.equals(this.getWicketApplicationName(), object.getWicketApplicationName())) {
            return false;
        }

        if (!StringUtils.equals(this.getWicketDisplayName(), object.getWicketDisplayName())) {
            return false;
        }

        if (!StringUtils.equals(this.getWicketInfrastructureClass(), object.getWicketInfrastructureClass())) {
            return false;
        }

        return true;
    }
}

