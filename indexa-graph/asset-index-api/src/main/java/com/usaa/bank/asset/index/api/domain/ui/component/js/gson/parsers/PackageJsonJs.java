package com.usaa.bank.asset.index.api.domain.ui.component.js.gson.parsers;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import com.usaa.bank.asset.index.api.domain.ui.component.js.gson.parsers.DistTags;

import java.util.HashMap;
import java.util.Map;

public class PackageJsonJs {

    @SerializedName("_id")
    private String _id = "";
    @SerializedName("_rev")
    private String _rev = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("description")
    private String description = "";
    @SerializedName("dist-tags")
    private DistTags disttags = new DistTags();

    private String authorInfo = "";
    private String versionInfo = "";
    private Map<String, String> fields = new HashMap<String, String>();
    private HashMap<String, LinkedTreeMap> versionTreeMap;


    public HashMap<String, LinkedTreeMap> getVersionTreeMap() {
        return versionTreeMap;
    }

    public void setVersionTreeMap(HashMap<String, LinkedTreeMap> versionTreeMap) {
        this.versionTreeMap = versionTreeMap;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DistTags getDisttags() {
        return disttags;
    }

    public void setDisttags(DistTags disttags) {
        this.disttags = disttags;
    }

    public String getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public String get_rev() {
        return _rev;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
