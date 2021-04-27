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

@System("http://hl7.org/fhir/invoice-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class InvoiceStatus extends Code {
    /**
     * draft
     * 
     * <p>the invoice has been prepared but not yet finalized.
     */
    public static final InvoiceStatus DRAFT = InvoiceStatus.builder().value(Value.DRAFT).build();

    /**
     * issued
     * 
     * <p>the invoice has been finalized and sent to the recipient.
     */
    public static final InvoiceStatus ISSUED = InvoiceStatus.builder().value(Value.ISSUED).build();

    /**
     * balanced
     * 
     * <p>the invoice has been balaced / completely paid.
     */
    public static final InvoiceStatus BALANCED = InvoiceStatus.builder().value(Value.BALANCED).build();

    /**
     * cancelled
     * 
     * <p>the invoice was cancelled.
     */
    public static final InvoiceStatus CANCELLED = InvoiceStatus.builder().value(Value.CANCELLED).build();

    /**
     * entered in error
     * 
     * <p>the invoice was determined as entered in error before it was issued.
     */
    public static final InvoiceStatus ENTERED_IN_ERROR = InvoiceStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private InvoiceStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this InvoiceStatus as an enum constant.
     * @deprecated replaced by {@link #getValueAsEnum()}
     */
    @Deprecated
    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Get the value of this InvoiceStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating InvoiceStatus objects from a passed enum value.
     * @deprecated replaced by {@link #of(Value)}
     */
    @Deprecated
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

    /**
     * Factory method for creating InvoiceStatus objects from a passed enum value.
     */
    public static InvoiceStatus of(Value value) {
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

    /**
     * Factory method for creating InvoiceStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static InvoiceStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating InvoiceStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating InvoiceStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(Value.from(value));
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
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * @deprecated replaced by  {@link #value(Value)}
         */
        @Deprecated
        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for InvoiceStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public InvoiceStatus build() {
            return new InvoiceStatus(this);
        }
    }

    @Deprecated
    public enum ValueSet {
        /**
         * draft
         * 
         * <p>the invoice has been prepared but not yet finalized.
         */
        DRAFT("draft"),

        /**
         * issued
         * 
         * <p>the invoice has been finalized and sent to the recipient.
         */
        ISSUED("issued"),

        /**
         * balanced
         * 
         * <p>the invoice has been balaced / completely paid.
         */
        BALANCED("balanced"),

        /**
         * cancelled
         * 
         * <p>the invoice was cancelled.
         */
        CANCELLED("cancelled"),

        /**
         * entered in error
         * 
         * <p>the invoice was determined as entered in error before it was issued.
         */
        ENTERED_IN_ERROR("entered-in-error");

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
         * Factory method for creating InvoiceStatus.Value values from a passed string value.
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

    public enum Value {
        /**
         * draft
         * 
         * <p>the invoice has been prepared but not yet finalized.
         */
        DRAFT("draft"),

        /**
         * issued
         * 
         * <p>the invoice has been finalized and sent to the recipient.
         */
        ISSUED("issued"),

        /**
         * balanced
         * 
         * <p>the invoice has been balaced / completely paid.
         */
        BALANCED("balanced"),

        /**
         * cancelled
         * 
         * <p>the invoice was cancelled.
         */
        CANCELLED("cancelled"),

        /**
         * entered in error
         * 
         * <p>the invoice was determined as entered in error before it was issued.
         */
        ENTERED_IN_ERROR("entered-in-error");

        private final java.lang.String value;

        Value(java.lang.String value) {
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
         * Factory method for creating InvoiceStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            for (Value c : Value.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
