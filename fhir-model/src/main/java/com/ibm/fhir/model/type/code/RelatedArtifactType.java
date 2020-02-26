/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.annotation.System;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/related-artifact-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class RelatedArtifactType extends Code {
    /**
     * Documentation
     */
    public static final RelatedArtifactType DOCUMENTATION = RelatedArtifactType.builder().value(ValueSet.DOCUMENTATION).build();

    /**
     * Justification
     */
    public static final RelatedArtifactType JUSTIFICATION = RelatedArtifactType.builder().value(ValueSet.JUSTIFICATION).build();

    /**
     * Citation
     */
    public static final RelatedArtifactType CITATION = RelatedArtifactType.builder().value(ValueSet.CITATION).build();

    /**
     * Predecessor
     */
    public static final RelatedArtifactType PREDECESSOR = RelatedArtifactType.builder().value(ValueSet.PREDECESSOR).build();

    /**
     * Successor
     */
    public static final RelatedArtifactType SUCCESSOR = RelatedArtifactType.builder().value(ValueSet.SUCCESSOR).build();

    /**
     * Derived From
     */
    public static final RelatedArtifactType DERIVED_FROM = RelatedArtifactType.builder().value(ValueSet.DERIVED_FROM).build();

    /**
     * Depends On
     */
    public static final RelatedArtifactType DEPENDS_ON = RelatedArtifactType.builder().value(ValueSet.DEPENDS_ON).build();

    /**
     * Composed Of
     */
    public static final RelatedArtifactType COMPOSED_OF = RelatedArtifactType.builder().value(ValueSet.COMPOSED_OF).build();

    private volatile int hashCode;

    private RelatedArtifactType(Builder builder) {
        super(builder);
    }

    public static RelatedArtifactType of(ValueSet value) {
        switch (value) {
        case DOCUMENTATION:
            return DOCUMENTATION;
        case JUSTIFICATION:
            return JUSTIFICATION;
        case CITATION:
            return CITATION;
        case PREDECESSOR:
            return PREDECESSOR;
        case SUCCESSOR:
            return SUCCESSOR;
        case DERIVED_FROM:
            return DERIVED_FROM;
        case DEPENDS_ON:
            return DEPENDS_ON;
        case COMPOSED_OF:
            return COMPOSED_OF;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static RelatedArtifactType of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
        RelatedArtifactType other = (RelatedArtifactType) obj;
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
        public RelatedArtifactType build() {
            return new RelatedArtifactType(this);
        }
    }

    public enum ValueSet {
        /**
         * Documentation
         */
        DOCUMENTATION("documentation"),

        /**
         * Justification
         */
        JUSTIFICATION("justification"),

        /**
         * Citation
         */
        CITATION("citation"),

        /**
         * Predecessor
         */
        PREDECESSOR("predecessor"),

        /**
         * Successor
         */
        SUCCESSOR("successor"),

        /**
         * Derived From
         */
        DERIVED_FROM("derived-from"),

        /**
         * Depends On
         */
        DEPENDS_ON("depends-on"),

        /**
         * Composed Of
         */
        COMPOSED_OF("composed-of");

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
