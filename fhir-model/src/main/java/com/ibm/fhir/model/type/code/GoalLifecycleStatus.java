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
public class GoalLifecycleStatus extends Code {
    /**
     * Proposed
     */
    public static final GoalLifecycleStatus PROPOSED = GoalLifecycleStatus.of(ValueSet.PROPOSED);

    /**
     * Planned
     */
    public static final GoalLifecycleStatus PLANNED = GoalLifecycleStatus.of(ValueSet.PLANNED);

    /**
     * Accepted
     */
    public static final GoalLifecycleStatus ACCEPTED = GoalLifecycleStatus.of(ValueSet.ACCEPTED);

    /**
     * Active
     */
    public static final GoalLifecycleStatus ACTIVE = GoalLifecycleStatus.of(ValueSet.ACTIVE);

    /**
     * On Hold
     */
    public static final GoalLifecycleStatus ON_HOLD = GoalLifecycleStatus.of(ValueSet.ON_HOLD);

    /**
     * Completed
     */
    public static final GoalLifecycleStatus COMPLETED = GoalLifecycleStatus.of(ValueSet.COMPLETED);

    /**
     * Cancelled
     */
    public static final GoalLifecycleStatus CANCELLED = GoalLifecycleStatus.of(ValueSet.CANCELLED);

    /**
     * Entered in Error
     */
    public static final GoalLifecycleStatus ENTERED_IN_ERROR = GoalLifecycleStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Rejected
     */
    public static final GoalLifecycleStatus REJECTED = GoalLifecycleStatus.of(ValueSet.REJECTED);

    private volatile int hashCode;

    private GoalLifecycleStatus(Builder builder) {
        super(builder);
    }

    public static GoalLifecycleStatus of(java.lang.String value) {
        return GoalLifecycleStatus.builder().value(value).build();
    }

    public static GoalLifecycleStatus of(ValueSet value) {
        return GoalLifecycleStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return GoalLifecycleStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return GoalLifecycleStatus.builder().value(value).build();
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
        public GoalLifecycleStatus build() {
            return new GoalLifecycleStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Proposed
         */
        PROPOSED("proposed"),

        /**
         * Planned
         */
        PLANNED("planned"),

        /**
         * Accepted
         */
        ACCEPTED("accepted"),

        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * On Hold
         */
        ON_HOLD("on-hold"),

        /**
         * Completed
         */
        COMPLETED("completed"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Rejected
         */
        REJECTED("rejected");

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
