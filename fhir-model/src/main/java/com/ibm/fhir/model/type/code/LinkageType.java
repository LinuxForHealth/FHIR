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

@System("http://hl7.org/fhir/linkage-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class LinkageType extends Code {
    /**
     * Source of Truth
     * 
     * <p>The resource represents the "source of truth" (from the perspective of this Linkage resource) for the underlying 
     * event/condition/etc.
     */
    public static final LinkageType SOURCE = LinkageType.builder().value(ValueSet.SOURCE).build();

    /**
     * Alternate Record
     * 
     * <p>The resource represents an alternative view of the underlying event/condition/etc. The resource may still be 
     * actively maintained, even though it is not considered to be the source of truth.
     */
    public static final LinkageType ALTERNATE = LinkageType.builder().value(ValueSet.ALTERNATE).build();

    /**
     * Historical/Obsolete Record
     * 
     * <p>The resource represents an obsolete record of the underlying event/condition/etc. It is not expected to be actively 
     * maintained.
     */
    public static final LinkageType HISTORICAL = LinkageType.builder().value(ValueSet.HISTORICAL).build();

    private volatile int hashCode;

    private LinkageType(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating LinkageType objects from a passed enum value.
     */
    public static LinkageType of(ValueSet value) {
        switch (value) {
        case SOURCE:
            return SOURCE;
        case ALTERNATE:
            return ALTERNATE;
        case HISTORICAL:
            return HISTORICAL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating LinkageType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static LinkageType of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating LinkageType objects from a passed string value.
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
     * Inherited factory method for creating LinkageType objects from a passed string value.
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
        LinkageType other = (LinkageType) obj;
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
        public LinkageType build() {
            return new LinkageType(this);
        }
    }

    public enum ValueSet {
        /**
         * Source of Truth
         * 
         * <p>The resource represents the "source of truth" (from the perspective of this Linkage resource) for the underlying 
         * event/condition/etc.
         */
        SOURCE("source"),

        /**
         * Alternate Record
         * 
         * <p>The resource represents an alternative view of the underlying event/condition/etc. The resource may still be 
         * actively maintained, even though it is not considered to be the source of truth.
         */
        ALTERNATE("alternate"),

        /**
         * Historical/Obsolete Record
         * 
         * <p>The resource represents an obsolete record of the underlying event/condition/etc. It is not expected to be actively 
         * maintained.
         */
        HISTORICAL("historical");

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
         * Factory method for creating LinkageType.ValueSet values from a passed string value.
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
