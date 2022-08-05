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

@System("http://hl7.org/fhir/group-type")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class GroupType extends Code {
    /**
     * Person
     * 
     * <p>Group contains "person" Patient resources.
     */
    public static final GroupType PERSON = GroupType.builder().value(Value.PERSON).build();

    /**
     * Animal
     * 
     * <p>Group contains "animal" Patient resources.
     */
    public static final GroupType ANIMAL = GroupType.builder().value(Value.ANIMAL).build();

    /**
     * Practitioner
     * 
     * <p>Group contains healthcare practitioner resources (Practitioner or PractitionerRole).
     */
    public static final GroupType PRACTITIONER = GroupType.builder().value(Value.PRACTITIONER).build();

    /**
     * Device
     * 
     * <p>Group contains Device resources.
     */
    public static final GroupType DEVICE = GroupType.builder().value(Value.DEVICE).build();

    /**
     * Medication
     * 
     * <p>Group contains Medication resources.
     */
    public static final GroupType MEDICATION = GroupType.builder().value(Value.MEDICATION).build();

    /**
     * Substance
     * 
     * <p>Group contains Substance resources.
     */
    public static final GroupType SUBSTANCE = GroupType.builder().value(Value.SUBSTANCE).build();

    private volatile int hashCode;

    private GroupType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this GroupType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating GroupType objects from a passed enum value.
     */
    public static GroupType of(Value value) {
        switch (value) {
        case PERSON:
            return PERSON;
        case ANIMAL:
            return ANIMAL;
        case PRACTITIONER:
            return PRACTITIONER;
        case DEVICE:
            return DEVICE;
        case MEDICATION:
            return MEDICATION;
        case SUBSTANCE:
            return SUBSTANCE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating GroupType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static GroupType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating GroupType objects from a passed string value.
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
     * Inherited factory method for creating GroupType objects from a passed string value.
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
        GroupType other = (GroupType) obj;
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
         *     An enum constant for GroupType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public GroupType build() {
            GroupType groupType = new GroupType(this);
            if (validating) {
                validate(groupType);
            }
            return groupType;
        }

        protected void validate(GroupType groupType) {
            super.validate(groupType);
        }

        protected Builder from(GroupType groupType) {
            super.from(groupType);
            return this;
        }
    }

    public enum Value {
        /**
         * Person
         * 
         * <p>Group contains "person" Patient resources.
         */
        PERSON("person"),

        /**
         * Animal
         * 
         * <p>Group contains "animal" Patient resources.
         */
        ANIMAL("animal"),

        /**
         * Practitioner
         * 
         * <p>Group contains healthcare practitioner resources (Practitioner or PractitionerRole).
         */
        PRACTITIONER("practitioner"),

        /**
         * Device
         * 
         * <p>Group contains Device resources.
         */
        DEVICE("device"),

        /**
         * Medication
         * 
         * <p>Group contains Medication resources.
         */
        MEDICATION("medication"),

        /**
         * Substance
         * 
         * <p>Group contains Substance resources.
         */
        SUBSTANCE("substance");

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
         * Factory method for creating GroupType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding GroupType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "person":
                return PERSON;
            case "animal":
                return ANIMAL;
            case "practitioner":
                return PRACTITIONER;
            case "device":
                return DEVICE;
            case "medication":
                return MEDICATION;
            case "substance":
                return SUBSTANCE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
