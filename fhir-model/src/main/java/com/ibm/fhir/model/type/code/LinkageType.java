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
public class LinkageType extends Code {
    /**
     * Source of Truth
     */
    public static final LinkageType SOURCE = LinkageType.of(ValueSet.SOURCE);

    /**
     * Alternate Record
     */
    public static final LinkageType ALTERNATE = LinkageType.of(ValueSet.ALTERNATE);

    /**
     * Historical/Obsolete Record
     */
    public static final LinkageType HISTORICAL = LinkageType.of(ValueSet.HISTORICAL);

    private volatile int hashCode;

    private LinkageType(Builder builder) {
        super(builder);
    }

    public static LinkageType of(java.lang.String value) {
        return LinkageType.builder().value(value).build();
    }

    public static LinkageType of(ValueSet value) {
        return LinkageType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return LinkageType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return LinkageType.builder().value(value).build();
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public LinkageType build() {
            return new LinkageType(this);
        }
    }

    public enum ValueSet {
        /**
         * Source of Truth
         */
        SOURCE("source"),

        /**
         * Alternate Record
         */
        ALTERNATE("alternate"),

        /**
         * Historical/Obsolete Record
         */
        HISTORICAL("historical");

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
