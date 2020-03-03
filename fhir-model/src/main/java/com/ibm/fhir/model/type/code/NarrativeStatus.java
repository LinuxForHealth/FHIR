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

@Generated("com.ibm.fhir.tools.CodeGenerator")
@System("http://hl7.org/fhir/narrative-status")
public class NarrativeStatus extends Code {
    /**
     * Generated
     * 
     * <p>The contents of the narrative are entirely generated from the core elements in the content.
     */
    public static final NarrativeStatus GENERATED = NarrativeStatus.builder().value(ValueSet.GENERATED).build();

    /**
     * Extensions
     * 
     * <p>The contents of the narrative are entirely generated from the core elements in the content and some of the content 
     * is generated from extensions. The narrative SHALL reflect the impact of all modifier extensions.
     */
    public static final NarrativeStatus EXTENSIONS = NarrativeStatus.builder().value(ValueSet.EXTENSIONS).build();

    /**
     * Additional
     * 
     * <p>The contents of the narrative may contain additional information not found in the structured data. Note that there 
     * is no computable way to determine what the extra information is, other than by human inspection.
     */
    public static final NarrativeStatus ADDITIONAL = NarrativeStatus.builder().value(ValueSet.ADDITIONAL).build();

    /**
     * Empty
     * 
     * <p>The contents of the narrative are some equivalent of "No human-readable text provided in this case".
     */
    public static final NarrativeStatus EMPTY = NarrativeStatus.builder().value(ValueSet.EMPTY).build();

    private volatile int hashCode;

    private NarrativeStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    public static NarrativeStatus of(ValueSet value) {
        switch (value) {
        case GENERATED:
            return GENERATED;
        case EXTENSIONS:
            return EXTENSIONS;
        case ADDITIONAL:
            return ADDITIONAL;
        case EMPTY:
            return EMPTY;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static NarrativeStatus of(java.lang.String value) {
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
        NarrativeStatus other = (NarrativeStatus) obj;
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
        public NarrativeStatus build() {
            return new NarrativeStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Generated
         * 
         * <p>The contents of the narrative are entirely generated from the core elements in the content.
         */
        GENERATED("generated"),

        /**
         * Extensions
         * 
         * <p>The contents of the narrative are entirely generated from the core elements in the content and some of the content 
         * is generated from extensions. The narrative SHALL reflect the impact of all modifier extensions.
         */
        EXTENSIONS("extensions"),

        /**
         * Additional
         * 
         * <p>The contents of the narrative may contain additional information not found in the structured data. Note that there 
         * is no computable way to determine what the extra information is, other than by human inspection.
         */
        ADDITIONAL("additional"),

        /**
         * Empty
         * 
         * <p>The contents of the narrative are some equivalent of "No human-readable text provided in this case".
         */
        EMPTY("empty");

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
