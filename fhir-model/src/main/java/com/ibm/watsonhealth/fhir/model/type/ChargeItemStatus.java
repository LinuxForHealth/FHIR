/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class ChargeItemStatus extends Code {
    /**
     * Planned
     */
    public static final ChargeItemStatus PLANNED = ChargeItemStatus.of(ValueSet.PLANNED);

    /**
     * Billable
     */
    public static final ChargeItemStatus BILLABLE = ChargeItemStatus.of(ValueSet.BILLABLE);

    /**
     * Not billable
     */
    public static final ChargeItemStatus NOT_BILLABLE = ChargeItemStatus.of(ValueSet.NOT_BILLABLE);

    /**
     * Aborted
     */
    public static final ChargeItemStatus ABORTED = ChargeItemStatus.of(ValueSet.ABORTED);

    /**
     * Billed
     */
    public static final ChargeItemStatus BILLED = ChargeItemStatus.of(ValueSet.BILLED);

    /**
     * Entered in Error
     */
    public static final ChargeItemStatus ENTERED_IN_ERROR = ChargeItemStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Unknown
     */
    public static final ChargeItemStatus UNKNOWN = ChargeItemStatus.of(ValueSet.UNKNOWN);

    private volatile int hashCode;

    private ChargeItemStatus(Builder builder) {
        super(builder);
    }

    public static ChargeItemStatus of(java.lang.String value) {
        return ChargeItemStatus.builder().value(value).build();
    }

    public static ChargeItemStatus of(ValueSet value) {
        return ChargeItemStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ChargeItemStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ChargeItemStatus.builder().value(value).build();
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
