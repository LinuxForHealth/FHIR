/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class TaskStatus extends Code {
    /**
     * Draft
     */
    public static final TaskStatus DRAFT = TaskStatus.of(ValueSet.DRAFT);

    /**
     * Requested
     */
    public static final TaskStatus REQUESTED = TaskStatus.of(ValueSet.REQUESTED);

    /**
     * Received
     */
    public static final TaskStatus RECEIVED = TaskStatus.of(ValueSet.RECEIVED);

    /**
     * Accepted
     */
    public static final TaskStatus ACCEPTED = TaskStatus.of(ValueSet.ACCEPTED);

    /**
     * Rejected
     */
    public static final TaskStatus REJECTED = TaskStatus.of(ValueSet.REJECTED);

    /**
     * Ready
     */
    public static final TaskStatus READY = TaskStatus.of(ValueSet.READY);

    /**
     * Cancelled
     */
    public static final TaskStatus CANCELLED = TaskStatus.of(ValueSet.CANCELLED);

    /**
     * In Progress
     */
    public static final TaskStatus IN_PROGRESS = TaskStatus.of(ValueSet.IN_PROGRESS);

    /**
     * On Hold
     */
    public static final TaskStatus ON_HOLD = TaskStatus.of(ValueSet.ON_HOLD);

    /**
     * Failed
     */
    public static final TaskStatus FAILED = TaskStatus.of(ValueSet.FAILED);

    /**
     * Completed
     */
    public static final TaskStatus COMPLETED = TaskStatus.of(ValueSet.COMPLETED);

    /**
     * Entered in Error
     */
    public static final TaskStatus ENTERED_IN_ERROR = TaskStatus.of(ValueSet.ENTERED_IN_ERROR);

    private volatile int hashCode;

    private TaskStatus(Builder builder) {
        super(builder);
    }

    public static TaskStatus of(java.lang.String value) {
        return TaskStatus.builder().value(value).build();
    }

    public static TaskStatus of(ValueSet value) {
        return TaskStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return TaskStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return TaskStatus.builder().value(value).build();
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
        TaskStatus other = (TaskStatus) obj;
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
        public TaskStatus build() {
            return new TaskStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Draft
         */
        DRAFT("draft"),

        /**
         * Requested
         */
        REQUESTED("requested"),

        /**
         * Received
         */
        RECEIVED("received"),

        /**
         * Accepted
         */
        ACCEPTED("accepted"),

        /**
         * Rejected
         */
        REJECTED("rejected"),

        /**
         * Ready
         */
        READY("ready"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

        /**
         * In Progress
         */
        IN_PROGRESS("in-progress"),

        /**
         * On Hold
         */
        ON_HOLD("on-hold"),

        /**
         * Failed
         */
        FAILED("failed"),

        /**
         * Completed
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error");

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
