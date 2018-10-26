package com.usaa.bank.asset.index.api.domain.asset.gav;

import com.usaa.bank.graph.common.hierarchy.LineageList;
import com.usaa.bank.graph.common.hierarchy.LineageListParser;

import java.io.Serializable;
import java.util.Iterator;

/**
 * GroupId is a model which represent the groupId part an artifact. This is used in creating the gav of an artifact.
 */

public class GroupId implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DELIMITER = ".";

    private LineageList<String> lineageList = null;

    /**
     * Creates a GroupId instance
     */
    public GroupId() {
        super();
        this.lineageList = new LineageList<String>();
    }

    /**
     * Create a GroupId instance from the given groupId. This will create a linageList from the given string input.
     *
     * @param groupId - groupId value
     */
    public GroupId(String groupId) {
        this.lineageList = LineageListParser.parse(groupId, GroupId.DELIMITER, true);
    }

    /**
     * Get an iterator for the groupId created as
     *
     * @return Iterator which start from
     */
    public Iterator<String> getParentsFirstIterator() {
        return this.lineageList.getParentsFirstIterator();
    }

    /**
     * Checks if the given element is persent in the groupId linage-list.
     *
     * @param element - Element to be searched
     * @return - True if found else false.
     */
    public boolean contains(String element) {
        return this.lineageList.contains(element);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this.lineageList != null) {
            sb.append(this.lineageList.toString());
        }
        return sb.toString();
    }

}
