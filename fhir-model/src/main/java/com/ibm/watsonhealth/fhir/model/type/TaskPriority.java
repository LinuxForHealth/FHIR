/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class TaskPriority extends Code {
    /**
     * Routine
     */
    public static final TaskPriority ROUTINE = TaskPriority.of(ValueSet.ROUTINE);

    /**
     * Urgent
     */
    public static final TaskPriority URGENT = TaskPriority.of(ValueSet.URGENT);

    /**
     * ASAP
     */
    public static final TaskPriority ASAP = TaskPriority.of(ValueSet.ASAP);

    /**
     * STAT
     */
    public static final TaskPriority STAT = TaskPriority.of(ValueSet.STAT);

    private TaskPriority(Builder builder) {
        super(builder);
    }

    public static TaskPriority of(java.lang.String value) {
        return TaskPriority.builder().value(value).build();
    }

    public static TaskPriority of(ValueSet value) {
        return TaskPriority.builder().value(value).build();
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
        public TaskPriority build() {
            return new TaskPriority(this);
        }
    }

    public enum ValueSet {
        /**
         * Routine
         */
        ROUTINE("routine"),

        /**
         * Urgent
         */
        URGENT("urgent"),

        /**
         * ASAP
         */
        ASAP("asap"),

        /**
         * STAT
         */
        STAT("stat");

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
