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
public class RelatedArtifactType extends Code {
    /**
     * Documentation
     */
    public static final RelatedArtifactType DOCUMENTATION = RelatedArtifactType.of(ValueSet.DOCUMENTATION);

    /**
     * Justification
     */
    public static final RelatedArtifactType JUSTIFICATION = RelatedArtifactType.of(ValueSet.JUSTIFICATION);

    /**
     * Citation
     */
    public static final RelatedArtifactType CITATION = RelatedArtifactType.of(ValueSet.CITATION);

    /**
     * Predecessor
     */
    public static final RelatedArtifactType PREDECESSOR = RelatedArtifactType.of(ValueSet.PREDECESSOR);

    /**
     * Successor
     */
    public static final RelatedArtifactType SUCCESSOR = RelatedArtifactType.of(ValueSet.SUCCESSOR);

    /**
     * Derived From
     */
    public static final RelatedArtifactType DERIVED_FROM = RelatedArtifactType.of(ValueSet.DERIVED_FROM);

    /**
     * Depends On
     */
    public static final RelatedArtifactType DEPENDS_ON = RelatedArtifactType.of(ValueSet.DEPENDS_ON);

    /**
     * Composed Of
     */
    public static final RelatedArtifactType COMPOSED_OF = RelatedArtifactType.of(ValueSet.COMPOSED_OF);

    private volatile int hashCode;

    private RelatedArtifactType(Builder builder) {
        super(builder);
    }

    public static RelatedArtifactType of(java.lang.String value) {
        return RelatedArtifactType.builder().value(value).build();
    }

    public static RelatedArtifactType of(ValueSet value) {
        return RelatedArtifactType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return RelatedArtifactType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return RelatedArtifactType.builder().value(value).build();
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
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
