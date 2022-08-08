/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/goal-status")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class GoalLifecycleStatus extends Code {
    /**
     * Proposed
     * 
     * <p>A goal is proposed for this patient.
     */
    public static final GoalLifecycleStatus PROPOSED = GoalLifecycleStatus.builder().value(Value.PROPOSED).build();

    /**
     * Planned
     * 
     * <p>A goal is planned for this patient.
     */
    public static final GoalLifecycleStatus PLANNED = GoalLifecycleStatus.builder().value(Value.PLANNED).build();

    /**
     * Accepted
     * 
     * <p>A proposed goal was accepted or acknowledged.
     */
    public static final GoalLifecycleStatus ACCEPTED = GoalLifecycleStatus.builder().value(Value.ACCEPTED).build();

    /**
     * Active
     * 
     * <p>The goal is being sought actively.
     */
    public static final GoalLifecycleStatus ACTIVE = GoalLifecycleStatus.builder().value(Value.ACTIVE).build();

    /**
     * On Hold
     * 
     * <p>The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.
     */
    public static final GoalLifecycleStatus ON_HOLD = GoalLifecycleStatus.builder().value(Value.ON_HOLD).build();

    /**
     * Completed
     * 
     * <p>The goal is no longer being sought.
     */
    public static final GoalLifecycleStatus COMPLETED = GoalLifecycleStatus.builder().value(Value.COMPLETED).build();

    /**
     * Cancelled
     * 
     * <p>The goal has been abandoned.
     */
    public static final GoalLifecycleStatus CANCELLED = GoalLifecycleStatus.builder().value(Value.CANCELLED).build();

    /**
     * Entered in Error
     * 
     * <p>The goal was entered in error and voided.
     */
    public static final GoalLifecycleStatus ENTERED_IN_ERROR = GoalLifecycleStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Rejected
     * 
     * <p>A proposed goal was rejected.
     */
    public static final GoalLifecycleStatus REJECTED = GoalLifecycleStatus.builder().value(Value.REJECTED).build();

    private volatile int hashCode;

    private GoalLifecycleStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this GoalLifecycleStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating GoalLifecycleStatus objects from a passed enum value.
     */
    public static GoalLifecycleStatus of(Value value) {
        switch (value) {
        case PROPOSED:
            return PROPOSED;
        case PLANNED:
            return PLANNED;
        case ACCEPTED:
            return ACCEPTED;
        case ACTIVE:
            return ACTIVE;
        case ON_HOLD:
            return ON_HOLD;
        case COMPLETED:
            return COMPLETED;
        case CANCELLED:
            return CANCELLED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case REJECTED:
            return REJECTED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating GoalLifecycleStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static GoalLifecycleStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating GoalLifecycleStatus objects from a passed string value.
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
     * Inherited factory method for creating GoalLifecycleStatus objects from a passed string value.
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
        GoalLifecycleStatus other = (GoalLifecycleStatus) obj;
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
        return new Builder().from(this);
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
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for GoalLifecycleStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public GoalLifecycleStatus build() {
            GoalLifecycleStatus goalLifecycleStatus = new GoalLifecycleStatus(this);
            if (validating) {
                validate(goalLifecycleStatus);
            }
            return goalLifecycleStatus;
        }

        protected void validate(GoalLifecycleStatus goalLifecycleStatus) {
            super.validate(goalLifecycleStatus);
        }

        protected Builder from(GoalLifecycleStatus goalLifecycleStatus) {
            super.from(goalLifecycleStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Proposed
         * 
         * <p>A goal is proposed for this patient.
         */
        PROPOSED("proposed"),

        /**
         * Planned
         * 
         * <p>A goal is planned for this patient.
         */
        PLANNED("planned"),

        /**
         * Accepted
         * 
         * <p>A proposed goal was accepted or acknowledged.
         */
        ACCEPTED("accepted"),

        /**
         * Active
         * 
         * <p>The goal is being sought actively.
         */
        ACTIVE("active"),

        /**
         * On Hold
         * 
         * <p>The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.
         */
        ON_HOLD("on-hold"),

        /**
         * Completed
         * 
         * <p>The goal is no longer being sought.
         */
        COMPLETED("completed"),

        /**
         * Cancelled
         * 
         * <p>The goal has been abandoned.
         */
        CANCELLED("cancelled"),

        /**
         * Entered in Error
         * 
         * <p>The goal was entered in error and voided.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Rejected
         * 
         * <p>A proposed goal was rejected.
         */
        REJECTED("rejected");

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
         * Factory method for creating GoalLifecycleStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding GoalLifecycleStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "proposed":
                return PROPOSED;
            case "planned":
                return PLANNED;
            case "accepted":
                return ACCEPTED;
            case "active":
                return ACTIVE;
            case "on-hold":
                return ON_HOLD;
            case "completed":
                return COMPLETED;
            case "cancelled":
                return CANCELLED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "rejected":
                return REJECTED;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
