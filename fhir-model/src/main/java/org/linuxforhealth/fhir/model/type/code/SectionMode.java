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

@System("http://hl7.org/fhir/list-mode")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class SectionMode extends Code {
    /**
     * Working List
     * 
     * <p>This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is 
     * tracking changes.
     */
    public static final SectionMode WORKING = SectionMode.builder().value(Value.WORKING).build();

    /**
     * Snapshot List
     * 
     * <p>This list was prepared as a snapshot. It should not be assumed to be current.
     */
    public static final SectionMode SNAPSHOT = SectionMode.builder().value(Value.SNAPSHOT).build();

    /**
     * Change List
     * 
     * <p>A point-in-time list that shows what changes have been made or recommended. E.g. a discharge medication list 
     * showing what was added and removed during an encounter.
     */
    public static final SectionMode CHANGES = SectionMode.builder().value(Value.CHANGES).build();

    private volatile int hashCode;

    private SectionMode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SectionMode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SectionMode objects from a passed enum value.
     */
    public static SectionMode of(Value value) {
        switch (value) {
        case WORKING:
            return WORKING;
        case SNAPSHOT:
            return SNAPSHOT;
        case CHANGES:
            return CHANGES;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SectionMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SectionMode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SectionMode objects from a passed string value.
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
     * Inherited factory method for creating SectionMode objects from a passed string value.
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
        SectionMode other = (SectionMode) obj;
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
         *     An enum constant for SectionMode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SectionMode build() {
            SectionMode sectionMode = new SectionMode(this);
            if (validating) {
                validate(sectionMode);
            }
            return sectionMode;
        }

        protected void validate(SectionMode sectionMode) {
            super.validate(sectionMode);
        }

        protected Builder from(SectionMode sectionMode) {
            super.from(sectionMode);
            return this;
        }
    }

    public enum Value {
        /**
         * Working List
         * 
         * <p>This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is 
         * tracking changes.
         */
        WORKING("working"),

        /**
         * Snapshot List
         * 
         * <p>This list was prepared as a snapshot. It should not be assumed to be current.
         */
        SNAPSHOT("snapshot"),

        /**
         * Change List
         * 
         * <p>A point-in-time list that shows what changes have been made or recommended. E.g. a discharge medication list 
         * showing what was added and removed during an encounter.
         */
        CHANGES("changes");

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
         * Factory method for creating SectionMode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SectionMode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "working":
                return WORKING;
            case "snapshot":
                return SNAPSHOT;
            case "changes":
                return CHANGES;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
