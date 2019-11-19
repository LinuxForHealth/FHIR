/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DocumentRelationshipType extends Code {
    /**
     * Replaces
     */
    public static final DocumentRelationshipType REPLACES = DocumentRelationshipType.builder().value(ValueSet.REPLACES).build();

    /**
     * Transforms
     */
    public static final DocumentRelationshipType TRANSFORMS = DocumentRelationshipType.builder().value(ValueSet.TRANSFORMS).build();

    /**
     * Signs
     */
    public static final DocumentRelationshipType SIGNS = DocumentRelationshipType.builder().value(ValueSet.SIGNS).build();

    /**
     * Appends
     */
    public static final DocumentRelationshipType APPENDS = DocumentRelationshipType.builder().value(ValueSet.APPENDS).build();

    private volatile int hashCode;

    private DocumentRelationshipType(Builder builder) {
        super(builder);
    }

    public static DocumentRelationshipType of(ValueSet value) {
        switch (value) {
        case REPLACES:
            return REPLACES;
        case TRANSFORMS:
            return TRANSFORMS;
        case SIGNS:
            return SIGNS;
        case APPENDS:
            return APPENDS;
        default:
            throw new IllegalArgumentException(value.name());
        }
    }

    public static DocumentRelationshipType of(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DocumentRelationshipType other = (DocumentRelationshipType) obj;
        return Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, extension, value);
            hashCode = result;
        }
        return result;
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Code.Builder {
        private Builder() {
            super();
        }

        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder value(java.lang.String value) {
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DocumentRelationshipType build() {
            return new DocumentRelationshipType(this);
        }
    }

    public enum ValueSet {
        /**
         * Replaces
         */
        REPLACES("replaces"),

        /**
         * Transforms
         */
        TRANSFORMS("transforms"),

        /**
         * Signs
         */
        SIGNS("signs"),

        /**
         * Appends
         */
        APPENDS("appends");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
