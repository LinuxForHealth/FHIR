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
public class EncounterStatus extends Code {
    /**
     * Planned
     */
    public static final EncounterStatus PLANNED = EncounterStatus.builder().value(ValueSet.PLANNED).build();

    /**
     * Arrived
     */
    public static final EncounterStatus ARRIVED = EncounterStatus.builder().value(ValueSet.ARRIVED).build();

    /**
     * Triaged
     */
    public static final EncounterStatus TRIAGED = EncounterStatus.builder().value(ValueSet.TRIAGED).build();

    /**
     * In Progress
     */
    public static final EncounterStatus IN_PROGRESS = EncounterStatus.builder().value(ValueSet.IN_PROGRESS).build();

    /**
     * On Leave
     */
    public static final EncounterStatus ONLEAVE = EncounterStatus.builder().value(ValueSet.ONLEAVE).build();

    /**
     * Finished
     */
    public static final EncounterStatus FINISHED = EncounterStatus.builder().value(ValueSet.FINISHED).build();

    /**
     * Cancelled
     */
    public static final EncounterStatus CANCELLED = EncounterStatus.builder().value(ValueSet.CANCELLED).build();

    /**
     * Entered in Error
     */
    public static final EncounterStatus ENTERED_IN_ERROR = EncounterStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     */
    public static final EncounterStatus UNKNOWN = EncounterStatus.builder().value(ValueSet.UNKNOWN).build();

    private volatile int hashCode;

    private EncounterStatus(Builder builder) {
        super(builder);
    }

    public static EncounterStatus of(ValueSet value) {
        switch (value) {
        case PLANNED:
            return PLANNED;
        case ARRIVED:
            return ARRIVED;
        case TRIAGED:
            return TRIAGED;
        case IN_PROGRESS:
            return IN_PROGRESS;
        case ONLEAVE:
            return ONLEAVE;
        case FINISHED:
            return FINISHED;
        case CANCELLED:
            return CANCELLED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalArgumentException(value.name());
        }
    }

    public static EncounterStatus of(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.valueOf(value));
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
        EncounterStatus other = (EncounterStatus) obj;
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
        public EncounterStatus build() {
            return new EncounterStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Planned
         */
        PLANNED("planned"),

        /**
         * Arrived
         */
        ARRIVED("arrived"),

        /**
         * Triaged
         */
        TRIAGED("triaged"),

        /**
         * In Progress
         */
        IN_PROGRESS("in-progress"),

        /**
         * On Leave
         */
        ONLEAVE("onleave"),

        /**
         * Finished
         */
        FINISHED("finished"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Unknown
         */
        UNKNOWN("unknown");

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
