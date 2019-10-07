/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CoverageStatus extends Code {
    /**
     * Active
     */
    public static final CoverageStatus ACTIVE = CoverageStatus.of(ValueSet.ACTIVE);

    /**
     * Cancelled
     */
    public static final CoverageStatus CANCELLED = CoverageStatus.of(ValueSet.CANCELLED);

    /**
     * Draft
     */
    public static final CoverageStatus DRAFT = CoverageStatus.of(ValueSet.DRAFT);

    /**
     * Entered in Error
     */
    public static final CoverageStatus ENTERED_IN_ERROR = CoverageStatus.of(ValueSet.ENTERED_IN_ERROR);

    private volatile int hashCode;

    private CoverageStatus(Builder builder) {
        super(builder);
    }

    public static CoverageStatus of(java.lang.String value) {
        return CoverageStatus.builder().value(value).build();
    }

    public static CoverageStatus of(ValueSet value) {
        return CoverageStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return CoverageStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return CoverageStatus.builder().value(value).build();
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
        CoverageStatus other = (CoverageStatus) obj;
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
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
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
        public CoverageStatus build() {
            return new CoverageStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

        /**
         * Draft
         */
        DRAFT("draft"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error");

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
