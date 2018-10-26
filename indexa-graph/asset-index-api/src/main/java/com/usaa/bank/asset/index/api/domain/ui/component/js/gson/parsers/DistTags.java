package com.usaa.bank.asset.index.api.domain.ui.component.js.gson.parsers;

import com.google.gson.annotations.SerializedName;


public class DistTags {

    @SerializedName("latest")
    private String latest = "";

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getLatest() {
        return latest;
    }
}

