/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class ResearchSubjectStatus extends Code {
    /**
     * Candidate
     */
    public static final ResearchSubjectStatus CANDIDATE = ResearchSubjectStatus.of(ValueSet.CANDIDATE);

    /**
     * Eligible
     */
    public static final ResearchSubjectStatus ELIGIBLE = ResearchSubjectStatus.of(ValueSet.ELIGIBLE);

    /**
     * Follow-up
     */
    public static final ResearchSubjectStatus FOLLOW_UP = ResearchSubjectStatus.of(ValueSet.FOLLOW_UP);

    /**
     * Ineligible
     */
    public static final ResearchSubjectStatus INELIGIBLE = ResearchSubjectStatus.of(ValueSet.INELIGIBLE);

    /**
     * Not Registered
     */
    public static final ResearchSubjectStatus NOT_REGISTERED = ResearchSubjectStatus.of(ValueSet.NOT_REGISTERED);

    /**
     * Off-study
     */
    public static final ResearchSubjectStatus OFF_STUDY = ResearchSubjectStatus.of(ValueSet.OFF_STUDY);

    /**
     * On-study
     */
    public static final ResearchSubjectStatus ON_STUDY = ResearchSubjectStatus.of(ValueSet.ON_STUDY);

    /**
     * On-study-intervention
     */
    public static final ResearchSubjectStatus ON_STUDY_INTERVENTION = ResearchSubjectStatus.of(ValueSet.ON_STUDY_INTERVENTION);

    /**
     * On-study-observation
     */
    public static final ResearchSubjectStatus ON_STUDY_OBSERVATION = ResearchSubjectStatus.of(ValueSet.ON_STUDY_OBSERVATION);

    /**
     * Pending on-study
     */
    public static final ResearchSubjectStatus PENDING_ON_STUDY = ResearchSubjectStatus.of(ValueSet.PENDING_ON_STUDY);

    /**
     * Potential Candidate
     */
    public static final ResearchSubjectStatus POTENTIAL_CANDIDATE = ResearchSubjectStatus.of(ValueSet.POTENTIAL_CANDIDATE);

    /**
     * Screening
     */
    public static final ResearchSubjectStatus SCREENING = ResearchSubjectStatus.of(ValueSet.SCREENING);

    /**
     * Withdrawn
     */
    public static final ResearchSubjectStatus WITHDRAWN = ResearchSubjectStatus.of(ValueSet.WITHDRAWN);

    private volatile int hashCode;

    private ResearchSubjectStatus(Builder builder) {
        super(builder);
    }

    public static ResearchSubjectStatus of(java.lang.String value) {
        return ResearchSubjectStatus.builder().value(value).build();
    }

    public static ResearchSubjectStatus of(ValueSet value) {
        return ResearchSubjectStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ResearchSubjectStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ResearchSubjectStatus.builder().value(value).build();
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
