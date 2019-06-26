/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class ResearchStudyStatus extends Code {
    /**
     * Active
     */
    public static final ResearchStudyStatus ACTIVE = ResearchStudyStatus.of(ValueSet.ACTIVE);

    /**
     * Administratively Completed
     */
    public static final ResearchStudyStatus ADMINISTRATIVELY_COMPLETED = ResearchStudyStatus.of(ValueSet.ADMINISTRATIVELY_COMPLETED);

    /**
     * Approved
     */
    public static final ResearchStudyStatus APPROVED = ResearchStudyStatus.of(ValueSet.APPROVED);

    /**
     * Closed to Accrual
     */
    public static final ResearchStudyStatus CLOSED_TO_ACCRUAL = ResearchStudyStatus.of(ValueSet.CLOSED_TO_ACCRUAL);

    /**
     * Closed to Accrual and Intervention
     */
    public static final ResearchStudyStatus CLOSED_TO_ACCRUAL_AND_INTERVENTION = ResearchStudyStatus.of(ValueSet.CLOSED_TO_ACCRUAL_AND_INTERVENTION);

    /**
     * Completed
     */
    public static final ResearchStudyStatus COMPLETED = ResearchStudyStatus.of(ValueSet.COMPLETED);

    /**
     * Disapproved
     */
    public static final ResearchStudyStatus DISAPPROVED = ResearchStudyStatus.of(ValueSet.DISAPPROVED);

    /**
     * In Review
     */
    public static final ResearchStudyStatus IN_REVIEW = ResearchStudyStatus.of(ValueSet.IN_REVIEW);

    /**
     * Temporarily Closed to Accrual
     */
    public static final ResearchStudyStatus TEMPORARILY_CLOSED_TO_ACCRUAL = ResearchStudyStatus.of(ValueSet.TEMPORARILY_CLOSED_TO_ACCRUAL);

    /**
     * Temporarily Closed to Accrual and Intervention
     */
    public static final ResearchStudyStatus TEMPORARILY_CLOSED_TO_ACCRUAL_AND_INTERVENTION = ResearchStudyStatus.of(ValueSet.TEMPORARILY_CLOSED_TO_ACCRUAL_AND_INTERVENTION);

    /**
     * Withdrawn
     */
    public static final ResearchStudyStatus WITHDRAWN = ResearchStudyStatus.of(ValueSet.WITHDRAWN);

    private ResearchStudyStatus(Builder builder) {
        super(builder);
    }

    public static ResearchStudyStatus of(java.lang.String value) {
        return ResearchStudyStatus.builder().value(value).build();
    }

    public static ResearchStudyStatus of(ValueSet value) {
        return ResearchStudyStatus.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
