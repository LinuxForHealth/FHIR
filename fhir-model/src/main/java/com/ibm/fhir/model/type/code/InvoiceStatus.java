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

@System("http://hl7.org/fhir/invoice-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class InvoiceStatus extends Code {
    /**
     * draft
     */
    public static final InvoiceStatus DRAFT = InvoiceStatus.builder().value(ValueSet.DRAFT).build();

    /**
     * issued
     */
    public static final InvoiceStatus ISSUED = InvoiceStatus.builder().value(ValueSet.ISSUED).build();

    /**
     * balanced
     */
    public static final InvoiceStatus BALANCED = InvoiceStatus.builder().value(ValueSet.BALANCED).build();

    /**
     * cancelled
     */
    public static final InvoiceStatus CANCELLED = InvoiceStatus.builder().value(ValueSet.CANCELLED).build();

    /**
     * entered in error
     */
    public static final InvoiceStatus ENTERED_IN_ERROR = InvoiceStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private InvoiceStatus(Builder builder) {
        super(builder);
    }

    public static InvoiceStatus of(ValueSet value) {
        switch (value) {
        case DRAFT:
            return DRAFT;
        case ISSUED:
            return ISSUED;
        case BALANCED:
            return BALANCED;
        case CANCELLED:
            return CANCELLED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static InvoiceStatus of(java.lang.String value) {
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
        InvoiceStatus other = (InvoiceStatus) obj;
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
        public InvoiceStatus build() {
            return new InvoiceStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * draft
         */
        DRAFT("draft"),

        /**
         * issued
         */
        ISSUED("issued"),

        /**
         * balanced
         */
        BALANCED("balanced"),

        /**
         * cancelled
         */
        CANCELLED("cancelled"),

        /**
         * entered in error
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
