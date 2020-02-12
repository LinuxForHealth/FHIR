/*
 * (C) Copyright IBM Corp. 2019, 2020
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
public class DiscriminatorType extends Code {
    /**
     * Value
     */
    public static final DiscriminatorType VALUE = DiscriminatorType.builder().value(ValueSet.VALUE).build();

    /**
     * Exists
     */
    public static final DiscriminatorType EXISTS = DiscriminatorType.builder().value(ValueSet.EXISTS).build();

    /**
     * Pattern
     */
    public static final DiscriminatorType PATTERN = DiscriminatorType.builder().value(ValueSet.PATTERN).build();

    /**
     * Type
     */
    public static final DiscriminatorType TYPE = DiscriminatorType.builder().value(ValueSet.TYPE).build();

    /**
     * Profile
     */
    public static final DiscriminatorType PROFILE = DiscriminatorType.builder().value(ValueSet.PROFILE).build();

    private volatile int hashCode;

    private DiscriminatorType(Builder builder) {
        super(builder);
    }

    public static DiscriminatorType of(ValueSet value) {
        switch (value) {
        case VALUE:
            return VALUE;
        case EXISTS:
            return EXISTS;
        case PATTERN:
            return PATTERN;
        case TYPE:
            return TYPE;
        case PROFILE:
            return PROFILE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static DiscriminatorType of(java.lang.String value) {
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
        DiscriminatorType other = (DiscriminatorType) obj;
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
        public DiscriminatorType build() {
            return new DiscriminatorType(this);
        }
    }

    public enum ValueSet {
        /**
         * Value
         */
        VALUE("value"),

        /**
         * Exists
         */
        EXISTS("exists"),

        /**
         * Pattern
         */
        PATTERN("pattern"),

        /**
         * Type
         */
        TYPE("type"),

        /**
         * Profile
         */
        PROFILE("profile");

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
