/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class TaskIntent extends Code {
    /**
     * Unknown
     */
    public static final TaskIntent UNKNOWN = TaskIntent.of(ValueSet.UNKNOWN);

    public static final TaskIntent PROPOSAL = TaskIntent.of(ValueSet.PROPOSAL);

    public static final TaskIntent PLAN = TaskIntent.of(ValueSet.PLAN);

    public static final TaskIntent ORDER = TaskIntent.of(ValueSet.ORDER);

    public static final TaskIntent ORIGINAL_ORDER = TaskIntent.of(ValueSet.ORIGINAL_ORDER);

    public static final TaskIntent REFLEX_ORDER = TaskIntent.of(ValueSet.REFLEX_ORDER);

    public static final TaskIntent FILLER_ORDER = TaskIntent.of(ValueSet.FILLER_ORDER);

    public static final TaskIntent INSTANCE_ORDER = TaskIntent.of(ValueSet.INSTANCE_ORDER);

    public static final TaskIntent OPTION = TaskIntent.of(ValueSet.OPTION);

    private volatile int hashCode;

    private TaskIntent(Builder builder) {
        super(builder);
    }

    public static TaskIntent of(java.lang.String value) {
        return TaskIntent.builder().value(value).build();
    }

    public static TaskIntent of(ValueSet value) {
        return TaskIntent.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return TaskIntent.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return TaskIntent.builder().value(value).build();
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
        TaskIntent other = (TaskIntent) obj;
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
        public TaskIntent build() {
            return new TaskIntent(this);
        }
    }

    public enum ValueSet {
        /**
         * Unknown
         */
        UNKNOWN("unknown"),

        PROPOSAL("proposal"),

        PLAN("plan"),

        ORDER("order"),

        ORIGINAL_ORDER("original-order"),

        REFLEX_ORDER("reflex-order"),

        FILLER_ORDER("filler-order"),

        INSTANCE_ORDER("instance-order"),

        OPTION("option");

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
