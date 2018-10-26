package com.usaa.bank.graph.common.identity;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * Base class from which IDs are derived. Because they are identifiers, these objects must be immutable.
 */
public class SimpleImmutableIdentifier implements ImmutableIdentifier, Serializable {
    private static final long serialVersionUID = 1L;
    private String identifier;

    /**
     * Sets the immutable identifier to be used while creating the hashcode.
     *
     * @param identifier - Immutable string.
     */
    public SimpleImmutableIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getStringValue() {
        return this.identifier;
    }

    //  ================================================================
    //  UTILITY METHODS
    //  ================================================================
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        builder.append(this.getStringValue());
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
        SimpleImmutableIdentifier that = (SimpleImmutableIdentifier) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getStringValue(), that.getStringValue());
        return builder.isEquals();
    }

    /**
     * Get string value of the object.
     *
     * @return A String representing the identifier used in hashcode.
     */
    public String toString() {
        return this.getStringValue();
    }
}
