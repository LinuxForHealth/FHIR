/*
 * (C) Copyright IBM Corp. 2019, 2021
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

@System("http://hl7.org/fhir/encounter-location-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class EncounterLocationStatus extends Code {
    /**
     * Planned
     * 
     * <p>The patient is planned to be moved to this location at some point in the future.
     */
    public static final EncounterLocationStatus PLANNED = EncounterLocationStatus.builder().value(Value.PLANNED).build();

    /**
     * Active
     * 
     * <p>The patient is currently at this location, or was between the period specified.A system may update these records 
     * when the patient leaves the location to either reserved, or completed.
     */
    public static final EncounterLocationStatus ACTIVE = EncounterLocationStatus.builder().value(Value.ACTIVE).build();

    /**
     * Reserved
     * 
     * <p>This location is held empty for this patient.
     */
    public static final EncounterLocationStatus RESERVED = EncounterLocationStatus.builder().value(Value.RESERVED).build();

    /**
     * Completed
     * 
     * <p>The patient was at this location during the period specified.Not to be used when the patient is currently at the 
     * location.
     */
    public static final EncounterLocationStatus COMPLETED = EncounterLocationStatus.builder().value(Value.COMPLETED).build();

    private volatile int hashCode;

    private EncounterLocationStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this EncounterLocationStatus as an enum constant.
     * @deprecated replaced by {@link #getValueConstant()}
     */
    @Deprecated
    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Get the value of this EncounterLocationStatus as an enum constant.
     */
    public Value getValueConstant() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating EncounterLocationStatus objects from a passed enum value.
     * @deprecated replaced by {@link #of(Value)}
     */
    @Deprecated
    public static EncounterLocationStatus of(ValueSet value) {
        switch (value) {
        case PLANNED:
            return PLANNED;
        case ACTIVE:
            return ACTIVE;
        case RESERVED:
            return RESERVED;
        case COMPLETED:
            return COMPLETED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating EncounterLocationStatus objects from a passed enum value.
     */
    public static EncounterLocationStatus of(Value value) {
        switch (value) {
        case PLANNED:
            return PLANNED;
        case ACTIVE:
            return ACTIVE;
        case RESERVED:
            return RESERVED;
        case COMPLETED:
            return COMPLETED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating EncounterLocationStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static EncounterLocationStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating EncounterLocationStatus objects from a passed string value.
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
     * Inherited factory method for creating EncounterLocationStatus objects from a passed string value.
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
        EncounterLocationStatus other = (EncounterLocationStatus) obj;
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
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * @deprecated replaced by  {@link #value(Value)}
         */
        @Deprecated
        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for EncounterLocationStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public EncounterLocationStatus build() {
            return new EncounterLocationStatus(this);
        }
    }

    @Deprecated
    public enum ValueSet {
        /**
         * Planned
         * 
         * <p>The patient is planned to be moved to this location at some point in the future.
         */
        PLANNED("planned"),

        /**
         * Active
         * 
         * <p>The patient is currently at this location, or was between the period specified.A system may update these records 
         * when the patient leaves the location to either reserved, or completed.
         */
        ACTIVE("active"),

        /**
         * Reserved
         * 
         * <p>This location is held empty for this patient.
         */
        RESERVED("reserved"),

        /**
         * Completed
         * 
         * <p>The patient was at this location during the period specified.Not to be used when the patient is currently at the 
         * location.
         */
        COMPLETED("completed");

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
         * Factory method for creating EncounterLocationStatus.Value values from a passed string value.
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

    public enum Value {
        /**
         * Planned
         * 
         * <p>The patient is planned to be moved to this location at some point in the future.
         */
        PLANNED("planned"),

        /**
         * Active
         * 
         * <p>The patient is currently at this location, or was between the period specified.A system may update these records 
         * when the patient leaves the location to either reserved, or completed.
         */
        ACTIVE("active"),

        /**
         * Reserved
         * 
         * <p>This location is held empty for this patient.
         */
        RESERVED("reserved"),

        /**
         * Completed
         * 
         * <p>The patient was at this location during the period specified.Not to be used when the patient is currently at the 
         * location.
         */
        COMPLETED("completed");

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
         * Factory method for creating EncounterLocationStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            for (Value c : Value.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
