package com.usaa.bank.asset.index.api.domain.asset.gav;

import java.io.Serializable;
import java.util.List;

import com.usaa.bank.graph.common.hierarchy.LineageList;
import com.usaa.bank.graph.common.hierarchy.LineageListParser;

/**
 * Version is a model which represent the gav part an artifact. This is used in creating the gav of an artifact.
 */
public class Version implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG_RELEASE = "-RELEASE";
    private static final String TAG_SNAPSHOT = "-SNAPSHOT";
    private static final String DELIMITER = ".";
    private LineageList<String> versionPartList;
    private boolean stable;
    private boolean tagged;

    public Version() {
        this.tagged = false;
        this.versionPartList = new LineageList<String>();
    }

    /**
     * Create a Version instance for the given tagged data.
     *
     * @param tagged - specifies tagged gav or not
     */
    public Version(boolean tagged) {
        this.tagged = tagged;
        this.versionPartList = new LineageList<String>();
    }

    /**
     * Create a Version instance for the given identifier data.
     *
     * @param identifier - Full Version in a string format
     */
    public Version(String identifier) {
        this.stable = true;
        if (identifier != null && identifier.length() > 0) {
            if (identifier.endsWith(Version.TAG_SNAPSHOT)) {
                this.stable = false;
                this.tagged = true;
                this.versionPartList = LineageListParser.parse(identifier.substring(0, identifier.indexOf(Version.TAG_SNAPSHOT)), Version.DELIMITER, true);
            } else if (identifier.endsWith(Version.TAG_RELEASE)) {
                this.tagged = true;
                this.versionPartList = LineageListParser.parse(identifier.substring(0, identifier.indexOf(Version.TAG_RELEASE)), Version.DELIMITER, true);
            } else {
                this.versionPartList = LineageListParser.parse(identifier, Version.DELIMITER, true);
            }
        } else {
            this.versionPartList = new LineageList<String>();
        }
    }

    /**
     * Get various parts of the Version. Functions returns the various parts in the form of string.
     *
     * @return - List of gav parts
     */
    public List<String> getVersionParts() {
        return this.versionPartList.asList();
    }

    /**
     * Is this gav of artifact a stable gav.
     *
     * @return - True if stable gav
     */
    public boolean isStable() {
        return this.stable;
    }

    /**
     * Is this gav a tagged gav or not
     *
     * @return - Tue if tagged gav.
     */
    public boolean isTagged() {
        return this.tagged;
    }

    public String toString() {

        StringBuffer sb = new StringBuffer();
        if (this.versionPartList != null) {
            sb.append(this.versionPartList.toString());
            if (this.isTagged()) {
                sb.append((this.isStable()) ? Version.TAG_RELEASE : Version.TAG_SNAPSHOT);
            }
        }
        return sb.toString();
    }
}
