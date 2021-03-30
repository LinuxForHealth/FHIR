/*
 * (C) Copyright IBM Corp. 2019, 2021
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

@System("http://hl7.org/fhir/resource-aggregation-mode")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AggregationMode extends Code {
    /**
     * Contained
     * 
     * <p>The reference is a local reference to a contained resource.
     */
    public static final AggregationMode CONTAINED = AggregationMode.builder().value(ValueSet.CONTAINED).build();

    /**
     * Referenced
     * 
     * <p>The reference to a resource that has to be resolved externally to the resource that includes the reference.
     */
    public static final AggregationMode REFERENCED = AggregationMode.builder().value(ValueSet.REFERENCED).build();

    /**
     * Bundled
     * 
     * <p>The resource the reference points to will be found in the same bundle as the resource that includes the reference.
     */
    public static final AggregationMode BUNDLED = AggregationMode.builder().value(ValueSet.BUNDLED).build();

    private volatile int hashCode;

    private AggregationMode(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating AggregationMode objects from a passed enum value.
     */
    public static AggregationMode of(ValueSet value) {
        switch (value) {
        case CONTAINED:
            return CONTAINED;
        case REFERENCED:
            return REFERENCED;
        case BUNDLED:
            return BUNDLED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AggregationMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AggregationMode of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating AggregationMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating AggregationMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
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
        AggregationMode other = (AggregationMode) obj;
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
        public AggregationMode build() {
            return new AggregationMode(this);
        }
    }

    public enum ValueSet {
        /**
         * Contained
         * 
         * <p>The reference is a local reference to a contained resource.
         */
        CONTAINED("contained"),

        /**
         * Referenced
         * 
         * <p>The reference to a resource that has to be resolved externally to the resource that includes the reference.
         */
        REFERENCED("referenced"),

        /**
         * Bundled
         * 
         * <p>The resource the reference points to will be found in the same bundle as the resource that includes the reference.
         */
        BUNDLED("bundled");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating AggregationMode.ValueSet values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
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
