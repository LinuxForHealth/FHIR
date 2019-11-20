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
public class RiskAssessmentStatus extends Code {
    /**
     * Registered
     */
    public static final RiskAssessmentStatus REGISTERED = RiskAssessmentStatus.builder().value(ValueSet.REGISTERED).build();

    /**
     * Preliminary
     */
    public static final RiskAssessmentStatus PRELIMINARY = RiskAssessmentStatus.builder().value(ValueSet.PRELIMINARY).build();

    /**
     * Final
     */
    public static final RiskAssessmentStatus FINAL = RiskAssessmentStatus.builder().value(ValueSet.FINAL).build();

    /**
     * Amended
     */
    public static final RiskAssessmentStatus AMENDED = RiskAssessmentStatus.builder().value(ValueSet.AMENDED).build();

    /**
     * Corrected
     */
    public static final RiskAssessmentStatus CORRECTED = RiskAssessmentStatus.builder().value(ValueSet.CORRECTED).build();

    /**
     * Cancelled
     */
    public static final RiskAssessmentStatus CANCELLED = RiskAssessmentStatus.builder().value(ValueSet.CANCELLED).build();

    /**
     * Entered in Error
     */
    public static final RiskAssessmentStatus ENTERED_IN_ERROR = RiskAssessmentStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     */
    public static final RiskAssessmentStatus UNKNOWN = RiskAssessmentStatus.builder().value(ValueSet.UNKNOWN).build();

    private volatile int hashCode;

    private RiskAssessmentStatus(Builder builder) {
        super(builder);
    }

    public static RiskAssessmentStatus of(ValueSet value) {
        switch (value) {
        case REGISTERED:
            return REGISTERED;
        case PRELIMINARY:
            return PRELIMINARY;
        case FINAL:
            return FINAL;
        case AMENDED:
            return AMENDED;
        case CORRECTED:
            return CORRECTED;
        case CANCELLED:
            return CANCELLED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static RiskAssessmentStatus of(java.lang.String value) {
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
        RiskAssessmentStatus other = (RiskAssessmentStatus) obj;
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
        public RiskAssessmentStatus build() {
            return new RiskAssessmentStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Registered
         */
        REGISTERED("registered"),

        /**
         * Preliminary
         */
        PRELIMINARY("preliminary"),

        /**
         * Final
         */
        FINAL("final"),

        /**
         * Amended
         */
        AMENDED("amended"),

        /**
         * Corrected
         */
        CORRECTED("corrected"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

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
