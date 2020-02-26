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

@System("http://hl7.org/fhir/chargeitem-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ChargeItemStatus extends Code {
    /**
     * Planned
     */
    public static final ChargeItemStatus PLANNED = ChargeItemStatus.builder().value(ValueSet.PLANNED).build();

    /**
     * Billable
     */
    public static final ChargeItemStatus BILLABLE = ChargeItemStatus.builder().value(ValueSet.BILLABLE).build();

    /**
     * Not billable
     */
    public static final ChargeItemStatus NOT_BILLABLE = ChargeItemStatus.builder().value(ValueSet.NOT_BILLABLE).build();

    /**
     * Aborted
     */
    public static final ChargeItemStatus ABORTED = ChargeItemStatus.builder().value(ValueSet.ABORTED).build();

    /**
     * Billed
     */
    public static final ChargeItemStatus BILLED = ChargeItemStatus.builder().value(ValueSet.BILLED).build();

    /**
     * Entered in Error
     */
    public static final ChargeItemStatus ENTERED_IN_ERROR = ChargeItemStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     */
    public static final ChargeItemStatus UNKNOWN = ChargeItemStatus.builder().value(ValueSet.UNKNOWN).build();

    private volatile int hashCode;

    private ChargeItemStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    public static ChargeItemStatus of(ValueSet value) {
        switch (value) {
        case PLANNED:
            return PLANNED;
        case BILLABLE:
            return BILLABLE;
        case NOT_BILLABLE:
            return NOT_BILLABLE;
        case ABORTED:
            return ABORTED;
        case BILLED:
            return BILLED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static ChargeItemStatus of(java.lang.String value) {
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
        ChargeItemStatus other = (ChargeItemStatus) obj;
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
        public ChargeItemStatus build() {
            return new ChargeItemStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Planned
         */
        PLANNED("planned"),

        /**
         * Billable
         */
        BILLABLE("billable"),

        /**
         * Not billable
         */
        NOT_BILLABLE("not-billable"),

        /**
         * Aborted
         */
        ABORTED("aborted"),

        /**
         * Billed
         */
        BILLED("billed"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Unknown
         */
        UNKNOWN("unknown");

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
