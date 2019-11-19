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
public class ResearchStudyStatus extends Code {
    /**
     * Active
     */
    public static final ResearchStudyStatus ACTIVE = ResearchStudyStatus.builder().value(ValueSet.ACTIVE).build();

    /**
     * Administratively Completed
     */
    public static final ResearchStudyStatus ADMINISTRATIVELY_COMPLETED = ResearchStudyStatus.builder().value(ValueSet.ADMINISTRATIVELY_COMPLETED).build();

    /**
     * Approved
     */
    public static final ResearchStudyStatus APPROVED = ResearchStudyStatus.builder().value(ValueSet.APPROVED).build();

    /**
     * Closed to Accrual
     */
    public static final ResearchStudyStatus CLOSED_TO_ACCRUAL = ResearchStudyStatus.builder().value(ValueSet.CLOSED_TO_ACCRUAL).build();

    /**
     * Closed to Accrual and Intervention
     */
    public static final ResearchStudyStatus CLOSED_TO_ACCRUAL_AND_INTERVENTION = ResearchStudyStatus.builder().value(ValueSet.CLOSED_TO_ACCRUAL_AND_INTERVENTION).build();

    /**
     * Completed
     */
    public static final ResearchStudyStatus COMPLETED = ResearchStudyStatus.builder().value(ValueSet.COMPLETED).build();

    /**
     * Disapproved
     */
    public static final ResearchStudyStatus DISAPPROVED = ResearchStudyStatus.builder().value(ValueSet.DISAPPROVED).build();

    /**
     * In Review
     */
    public static final ResearchStudyStatus IN_REVIEW = ResearchStudyStatus.builder().value(ValueSet.IN_REVIEW).build();

    /**
     * Temporarily Closed to Accrual
     */
    public static final ResearchStudyStatus TEMPORARILY_CLOSED_TO_ACCRUAL = ResearchStudyStatus.builder().value(ValueSet.TEMPORARILY_CLOSED_TO_ACCRUAL).build();

    /**
     * Temporarily Closed to Accrual and Intervention
     */
    public static final ResearchStudyStatus TEMPORARILY_CLOSED_TO_ACCRUAL_AND_INTERVENTION = ResearchStudyStatus.builder().value(ValueSet.TEMPORARILY_CLOSED_TO_ACCRUAL_AND_INTERVENTION).build();

    /**
     * Withdrawn
     */
    public static final ResearchStudyStatus WITHDRAWN = ResearchStudyStatus.builder().value(ValueSet.WITHDRAWN).build();

    private volatile int hashCode;

    private ResearchStudyStatus(Builder builder) {
        super(builder);
    }

    public static ResearchStudyStatus of(ValueSet value) {
        switch (value) {
        case ACTIVE:
            return ACTIVE;
        case ADMINISTRATIVELY_COMPLETED:
            return ADMINISTRATIVELY_COMPLETED;
        case APPROVED:
            return APPROVED;
        case CLOSED_TO_ACCRUAL:
            return CLOSED_TO_ACCRUAL;
        case CLOSED_TO_ACCRUAL_AND_INTERVENTION:
            return CLOSED_TO_ACCRUAL_AND_INTERVENTION;
        case COMPLETED:
            return COMPLETED;
        case DISAPPROVED:
            return DISAPPROVED;
        case IN_REVIEW:
            return IN_REVIEW;
        case TEMPORARILY_CLOSED_TO_ACCRUAL:
            return TEMPORARILY_CLOSED_TO_ACCRUAL;
        case TEMPORARILY_CLOSED_TO_ACCRUAL_AND_INTERVENTION:
            return TEMPORARILY_CLOSED_TO_ACCRUAL_AND_INTERVENTION;
        case WITHDRAWN:
            return WITHDRAWN;
        default:
            throw new IllegalArgumentException(value.name());
        }
    }

    public static ResearchStudyStatus of(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.valueOf(value));
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
        ResearchStudyStatus other = (ResearchStudyStatus) obj;
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
        public ResearchStudyStatus build() {
            return new ResearchStudyStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Administratively Completed
         */
        ADMINISTRATIVELY_COMPLETED("administratively-completed"),

        /**
         * Approved
         */
        APPROVED("approved"),

        /**
         * Closed to Accrual
         */
        CLOSED_TO_ACCRUAL("closed-to-accrual"),

        /**
         * Closed to Accrual and Intervention
         */
        CLOSED_TO_ACCRUAL_AND_INTERVENTION("closed-to-accrual-and-intervention"),

        /**
         * Completed
         */
        COMPLETED("completed"),

        /**
         * Disapproved
         */
        DISAPPROVED("disapproved"),

        /**
         * In Review
         */
        IN_REVIEW("in-review"),

        /**
         * Temporarily Closed to Accrual
         */
        TEMPORARILY_CLOSED_TO_ACCRUAL("temporarily-closed-to-accrual"),

        /**
         * Temporarily Closed to Accrual and Intervention
         */
        TEMPORARILY_CLOSED_TO_ACCRUAL_AND_INTERVENTION("temporarily-closed-to-accrual-and-intervention"),

        /**
         * Withdrawn
         */
        WITHDRAWN("withdrawn");

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
