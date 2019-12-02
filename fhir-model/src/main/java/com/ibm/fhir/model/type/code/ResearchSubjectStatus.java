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
public class ResearchSubjectStatus extends Code {
    /**
     * Candidate
     */
    public static final ResearchSubjectStatus CANDIDATE = ResearchSubjectStatus.builder().value(ValueSet.CANDIDATE).build();

    /**
     * Eligible
     */
    public static final ResearchSubjectStatus ELIGIBLE = ResearchSubjectStatus.builder().value(ValueSet.ELIGIBLE).build();

    /**
     * Follow-up
     */
    public static final ResearchSubjectStatus FOLLOW_UP = ResearchSubjectStatus.builder().value(ValueSet.FOLLOW_UP).build();

    /**
     * Ineligible
     */
    public static final ResearchSubjectStatus INELIGIBLE = ResearchSubjectStatus.builder().value(ValueSet.INELIGIBLE).build();

    /**
     * Not Registered
     */
    public static final ResearchSubjectStatus NOT_REGISTERED = ResearchSubjectStatus.builder().value(ValueSet.NOT_REGISTERED).build();

    /**
     * Off-study
     */
    public static final ResearchSubjectStatus OFF_STUDY = ResearchSubjectStatus.builder().value(ValueSet.OFF_STUDY).build();

    /**
     * On-study
     */
    public static final ResearchSubjectStatus ON_STUDY = ResearchSubjectStatus.builder().value(ValueSet.ON_STUDY).build();

    /**
     * On-study-intervention
     */
    public static final ResearchSubjectStatus ON_STUDY_INTERVENTION = ResearchSubjectStatus.builder().value(ValueSet.ON_STUDY_INTERVENTION).build();

    /**
     * On-study-observation
     */
    public static final ResearchSubjectStatus ON_STUDY_OBSERVATION = ResearchSubjectStatus.builder().value(ValueSet.ON_STUDY_OBSERVATION).build();

    /**
     * Pending on-study
     */
    public static final ResearchSubjectStatus PENDING_ON_STUDY = ResearchSubjectStatus.builder().value(ValueSet.PENDING_ON_STUDY).build();

    /**
     * Potential Candidate
     */
    public static final ResearchSubjectStatus POTENTIAL_CANDIDATE = ResearchSubjectStatus.builder().value(ValueSet.POTENTIAL_CANDIDATE).build();

    /**
     * Screening
     */
    public static final ResearchSubjectStatus SCREENING = ResearchSubjectStatus.builder().value(ValueSet.SCREENING).build();

    /**
     * Withdrawn
     */
    public static final ResearchSubjectStatus WITHDRAWN = ResearchSubjectStatus.builder().value(ValueSet.WITHDRAWN).build();

    private volatile int hashCode;

    private ResearchSubjectStatus(Builder builder) {
        super(builder);
    }

    public static ResearchSubjectStatus of(ValueSet value) {
        switch (value) {
        case CANDIDATE:
            return CANDIDATE;
        case ELIGIBLE:
            return ELIGIBLE;
        case FOLLOW_UP:
            return FOLLOW_UP;
        case INELIGIBLE:
            return INELIGIBLE;
        case NOT_REGISTERED:
            return NOT_REGISTERED;
        case OFF_STUDY:
            return OFF_STUDY;
        case ON_STUDY:
            return ON_STUDY;
        case ON_STUDY_INTERVENTION:
            return ON_STUDY_INTERVENTION;
        case ON_STUDY_OBSERVATION:
            return ON_STUDY_OBSERVATION;
        case PENDING_ON_STUDY:
            return PENDING_ON_STUDY;
        case POTENTIAL_CANDIDATE:
            return POTENTIAL_CANDIDATE;
        case SCREENING:
            return SCREENING;
        case WITHDRAWN:
            return WITHDRAWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static ResearchSubjectStatus of(java.lang.String value) {
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
        ResearchSubjectStatus other = (ResearchSubjectStatus) obj;
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
        public ResearchSubjectStatus build() {
            return new ResearchSubjectStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Candidate
         */
        CANDIDATE("candidate"),

        /**
         * Eligible
         */
        ELIGIBLE("eligible"),

        /**
         * Follow-up
         */
        FOLLOW_UP("follow-up"),

        /**
         * Ineligible
         */
        INELIGIBLE("ineligible"),

        /**
         * Not Registered
         */
        NOT_REGISTERED("not-registered"),

        /**
         * Off-study
         */
        OFF_STUDY("off-study"),

        /**
         * On-study
         */
        ON_STUDY("on-study"),

        /**
         * On-study-intervention
         */
        ON_STUDY_INTERVENTION("on-study-intervention"),

        /**
         * On-study-observation
         */
        ON_STUDY_OBSERVATION("on-study-observation"),

        /**
         * Pending on-study
         */
        PENDING_ON_STUDY("pending-on-study"),

        /**
         * Potential Candidate
         */
        POTENTIAL_CANDIDATE("potential-candidate"),

        /**
         * Screening
         */
        SCREENING("screening"),

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
