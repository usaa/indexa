package com.usaa.bank.asset.index.api.domain.ui.component.js;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Map;

public class JSApplicationArtifact extends DigitalAsset implements IJSApplicationArtifact, Serializable {
    private static final String GUID_DELIMITER = ":";
    public static final String JS_APP_ID = "jsAppId";
    public static final String JS_APP_NAME = "jsAppName";
    public static final String JS_APP_DESCRIPTION = "jsAppDescription";
    public static final String JS_APP_REV = "jsAppRev";
    public static final String JS_APP_AUTHOR = "jsAppAuthor";
    public static final String JS_APP_VERSION = "jsAppVersion";
    public static final String JS_APP_DEPENDENCIES = "jsAppDependencies";

    public JSApplicationArtifact(GUID guid, String appId, String appName, String appDescription, String appRev, String appAuthor, String version, Map<String, String> fields) {
        super(guid);
        this.setJsAppId(appId);
        this.setJsbAppName(appName);
        this.setJsAppDescription(appDescription);
        this.setJsAppRev(appRev);
        this.setJsAppAuthor(appAuthor);
        this.setJsAppVersion(version);
        this.setJsAppFields(fields);
    }

    public JSApplicationArtifact(String appId, String appName, String appDescription, String appRev, String appAuthor, String version, Map<String, String> fields) {
        super(JSApplicationArtifact.createGUID(appId, version));
        this.setJsAppId(appId);
        this.setJsbAppName(appName);
        this.setJsAppDescription(appDescription);
        this.setJsAppRev(appRev);
        this.setJsAppAuthor(appAuthor);
        this.setJsAppVersion(version);
        this.setJsAppFields(fields);
    }

    public static GUID createGUID(String appId, String appName) {
        GUID guid = null;
        if (StringUtils.isNotEmpty(appId) && StringUtils.isNotEmpty(appName)) {
            guid = GUIDFactory.createGUID(appId + GUID_DELIMITER + appName);
        }
        return guid;
    }

    private void setJsAppId(String appId) {
        if (StringUtils.isNotEmpty(appId)) {
            this.addField(JSApplicationArtifact.JS_APP_ID, appId);
        }
    }

    private void setJsbAppName(String appName) {
        if (StringUtils.isNotEmpty(appName)) {
            this.addField(JSApplicationArtifact.JS_APP_NAME, appName);
        }
    }

    private void setJsAppDescription(String appDescription) {
        if (StringUtils.isNotEmpty(appDescription)) {
            this.addField(JSApplicationArtifact.JS_APP_DESCRIPTION, appDescription);
        }
    }

    private void setJsAppRev(String appRev) {
        if (StringUtils.isNotEmpty(appRev)) {
            this.addField(JSApplicationArtifact.JS_APP_REV, appRev);
        }
    }

    private void setJsAppAuthor(String appAuthor) {
        if (StringUtils.isNotEmpty(appAuthor)) {
            this.addField(JSApplicationArtifact.JS_APP_AUTHOR, appAuthor);
        }
    }

    private void setJsAppVersion(String version) {
        if (StringUtils.isNotEmpty(version)) {
            this.addField(JSApplicationArtifact.JS_APP_VERSION, version);
        }
    }

    private void setJsAppFields(Map<String, String> fields) {
        if (!fields.isEmpty()) {
            for (String key : fields.keySet()) {
                this.addField(key, fields.get(key));
            }

        }
    }

    @Override
    public String getJsAppId() {
        return this.getFieldValue(JSApplicationArtifact.JS_APP_ID);
    }

    @Override
    public String getJsAppRev() {
        return this.getFieldValue(JSApplicationArtifact.JS_APP_REV);
    }

    @Override
    public String getJsAppName() {
        return this.getFieldValue(JSApplicationArtifact.JS_APP_NAME);
    }

    @Override
    public String getJsAppDescription() {
        return this.getFieldValue(JSApplicationArtifact.JS_APP_DESCRIPTION);
    }

    @Override
    public String getJsAppAuthor() {
        return this.getFieldValue(JSApplicationArtifact.JS_APP_AUTHOR);
    }

    @Override
    public String getJsVersion() {
        return this.getFieldValue(JSApplicationArtifact.JS_APP_VERSION);
    }

    @Override
    public String getJsVersions() {
        return this.getFieldValue(JSApplicationArtifact.JS_APP_DEPENDENCIES);
    }

    public boolean equals(Object o) {
        IJSApplicationArtifact object = null;

        if (!(o instanceof IJSApplicationArtifact)) {
            return false;
        } else {
            object = (IJSApplicationArtifact) o;
        }

        if (!StringUtils.equals(this.getJsAppAuthor(), object.getJsAppAuthor())) {
            return false;
        }

        if (!StringUtils.equals(this.getJsAppDescription(), object.getJsAppDescription())) {
            return false;
        }

        if (!StringUtils.equals(this.getJsAppId(), object.getJsAppId())) {
            return false;
        }

        if (!StringUtils.equals(this.getJsAppName(), object.getJsAppName())) {
            return false;
        }

        if (!StringUtils.equals(this.getJsAppRev(), object.getJsAppRev())) {
            return false;
        }

        if (!StringUtils.equals(this.getJsVersion(), object.getJsVersion())) {
            return false;
        }

        return true;
    }

}

