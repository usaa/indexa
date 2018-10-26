package com.usaa.bank.asset.index.api.domain.asset.java.classes;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.usaa.bank.graph.common.hierarchy.LineageList;
import com.usaa.bank.graph.common.hierarchy.LineageListParser;

/**
 * Utility class to create a linage-list hierarchical structure for a package.
 */
public class HierarchicalPackage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DELIMITER = ".";

    private LineageList<String> lineageList = null;

    public HierarchicalPackage() {
        super();
        this.lineageList = new LineageList<String>();
    }

    /**
     * Create HierarchicalPackage instance from the given packageName. Parser will create a linage-list structure for the given package-name.
     *
     * @param packageName - Package to be structured
     */
    public HierarchicalPackage(String packageName) {
        this.lineageList = LineageListParser.parse(packageName, HierarchicalPackage.DELIMITER, true);
    }

    /**
     * Get the iterator for the hierarchically structured package.
     *
     * @return - Iterator
     */
    public Iterator<String> getParentsFirstIterator() {
        return this.lineageList.getParentsFirstIterator();
    }


    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        builder.append(this.toString());
        return builder.toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        HierarchicalPackage that = (HierarchicalPackage) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.toString(), that.toString());
        return builder.isEquals();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this.lineageList != null) {
            sb.append(this.lineageList.toString());
        }
        return sb.toString();
    }

}
