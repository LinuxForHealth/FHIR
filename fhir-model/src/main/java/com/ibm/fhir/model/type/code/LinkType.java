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

@System("http://hl7.org/fhir/link-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class LinkType extends Code {
    /**
     * Replaced-by
     */
    public static final LinkType REPLACED_BY = LinkType.builder().value(ValueSet.REPLACED_BY).build();

    /**
     * Replaces
     */
    public static final LinkType REPLACES = LinkType.builder().value(ValueSet.REPLACES).build();

    /**
     * Refer
     */
    public static final LinkType REFER = LinkType.builder().value(ValueSet.REFER).build();

    /**
     * See also
     */
    public static final LinkType SEEALSO = LinkType.builder().value(ValueSet.SEEALSO).build();

    private volatile int hashCode;

    private LinkType(Builder builder) {
        super(builder);
    }

    public static LinkType of(ValueSet value) {
        switch (value) {
        case REPLACED_BY:
            return REPLACED_BY;
        case REPLACES:
            return REPLACES;
        case REFER:
            return REFER;
        case SEEALSO:
            return SEEALSO;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static LinkType of(java.lang.String value) {
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
        LinkType other = (LinkType) obj;
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
        public LinkType build() {
            return new LinkType(this);
        }
    }

    public enum ValueSet {
        /**
         * Replaced-by
         */
        REPLACED_BY("replaced-by"),

        /**
         * Replaces
         */
        REPLACES("replaces"),

        /**
         * Refer
         */
        REFER("refer"),

        /**
         * See also
         */
        SEEALSO("seealso");

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
