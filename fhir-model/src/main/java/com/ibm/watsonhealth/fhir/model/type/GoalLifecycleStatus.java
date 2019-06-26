/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

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

    private GoalLifecycleStatus(Builder builder) {
        super(builder);
    }

    public static GoalLifecycleStatus of(java.lang.String value) {
        return GoalLifecycleStatus.builder().value(value).build();
    }

    public static GoalLifecycleStatus of(ValueSet value) {
        return GoalLifecycleStatus.builder().value(value).build();
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
