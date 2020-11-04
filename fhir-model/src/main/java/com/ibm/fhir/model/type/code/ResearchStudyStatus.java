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

@System("http://hl7.org/fhir/research-study-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ResearchStudyStatus extends Code {
    /**
     * Active
     * 
     * <p>Study is opened for accrual.
     */
    public static final ResearchStudyStatus ACTIVE = ResearchStudyStatus.builder().value(ValueSet.ACTIVE).build();

    /**
     * Administratively Completed
     * 
     * <p>Study is completed prematurely and will not resume; patients are no longer examined nor treated.
     */
    public static final ResearchStudyStatus ADMINISTRATIVELY_COMPLETED = ResearchStudyStatus.builder().value(ValueSet.ADMINISTRATIVELY_COMPLETED).build();

    /**
     * Approved
     * 
     * <p>Protocol is approved by the review board.
     */
    public static final ResearchStudyStatus APPROVED = ResearchStudyStatus.builder().value(ValueSet.APPROVED).build();

    /**
     * Closed to Accrual
     * 
     * <p>Study is closed for accrual; patients can be examined and treated.
     */
    public static final ResearchStudyStatus CLOSED_TO_ACCRUAL = ResearchStudyStatus.builder().value(ValueSet.CLOSED_TO_ACCRUAL).build();

    /**
     * Closed to Accrual and Intervention
     * 
     * <p>Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have 
     * completed treatment or intervention but are still being followed according to the primary objective of the study.
     */
    public static final ResearchStudyStatus CLOSED_TO_ACCRUAL_AND_INTERVENTION = ResearchStudyStatus.builder().value(ValueSet.CLOSED_TO_ACCRUAL_AND_INTERVENTION).build();

    /**
     * Completed
     * 
     * <p>Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have 
     * completed treatment
     * <p>or intervention but are still being followed according to the primary objective of the study.
     */
    public static final ResearchStudyStatus COMPLETED = ResearchStudyStatus.builder().value(ValueSet.COMPLETED).build();

    /**
     * Disapproved
     * 
     * <p>Protocol was disapproved by the review board.
     */
    public static final ResearchStudyStatus DISAPPROVED = ResearchStudyStatus.builder().value(ValueSet.DISAPPROVED).build();

    /**
     * In Review
     * 
     * <p>Protocol is submitted to the review board for approval.
     */
    public static final ResearchStudyStatus IN_REVIEW = ResearchStudyStatus.builder().value(ValueSet.IN_REVIEW).build();

    /**
     * Temporarily Closed to Accrual
     * 
     * <p>Study is temporarily closed for accrual; can be potentially resumed in the future; patients can be examined and 
     * treated.
     */
    public static final ResearchStudyStatus TEMPORARILY_CLOSED_TO_ACCRUAL = ResearchStudyStatus.builder().value(ValueSet.TEMPORARILY_CLOSED_TO_ACCRUAL).build();

    /**
     * Temporarily Closed to Accrual and Intervention
     * 
     * <p>Study is temporarily closed for accrual and intervention and potentially can be resumed in the future.
     */
    public static final ResearchStudyStatus TEMPORARILY_CLOSED_TO_ACCRUAL_AND_INTERVENTION = ResearchStudyStatus.builder().value(ValueSet.TEMPORARILY_CLOSED_TO_ACCRUAL_AND_INTERVENTION).build();

    /**
     * Withdrawn
     * 
     * <p>Protocol was withdrawn by the lead organization.
     */
    public static final ResearchStudyStatus WITHDRAWN = ResearchStudyStatus.builder().value(ValueSet.WITHDRAWN).build();

    private volatile int hashCode;

    private ResearchStudyStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating ResearchStudyStatus objects from a passed enum value.
     */
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
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ResearchStudyStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ResearchStudyStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating ResearchStudyStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating ResearchStudyStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
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
         * 
         * <p>Study is opened for accrual.
         */
        ACTIVE("active"),

        /**
         * Administratively Completed
         * 
         * <p>Study is completed prematurely and will not resume; patients are no longer examined nor treated.
         */
        ADMINISTRATIVELY_COMPLETED("administratively-completed"),

        /**
         * Approved
         * 
         * <p>Protocol is approved by the review board.
         */
        APPROVED("approved"),

        /**
         * Closed to Accrual
         * 
         * <p>Study is closed for accrual; patients can be examined and treated.
         */
        CLOSED_TO_ACCRUAL("closed-to-accrual"),

        /**
         * Closed to Accrual and Intervention
         * 
         * <p>Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have 
         * completed treatment or intervention but are still being followed according to the primary objective of the study.
         */
        CLOSED_TO_ACCRUAL_AND_INTERVENTION("closed-to-accrual-and-intervention"),

        /**
         * Completed
         * 
         * <p>Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have 
         * completed treatment
         * <p>or intervention but are still being followed according to the primary objective of the study.
         */
        COMPLETED("completed"),

        /**
         * Disapproved
         * 
         * <p>Protocol was disapproved by the review board.
         */
        DISAPPROVED("disapproved"),

        /**
         * In Review
         * 
         * <p>Protocol is submitted to the review board for approval.
         */
        IN_REVIEW("in-review"),

        /**
         * Temporarily Closed to Accrual
         * 
         * <p>Study is temporarily closed for accrual; can be potentially resumed in the future; patients can be examined and 
         * treated.
         */
        TEMPORARILY_CLOSED_TO_ACCRUAL("temporarily-closed-to-accrual"),

        /**
         * Temporarily Closed to Accrual and Intervention
         * 
         * <p>Study is temporarily closed for accrual and intervention and potentially can be resumed in the future.
         */
        TEMPORARILY_CLOSED_TO_ACCRUAL_AND_INTERVENTION("temporarily-closed-to-accrual-and-intervention"),

        /**
         * Withdrawn
         * 
         * <p>Protocol was withdrawn by the lead organization.
         */
        WITHDRAWN("withdrawn");

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
         * Factory method for creating ResearchStudyStatus.ValueSet values from a passed string value.
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
}
