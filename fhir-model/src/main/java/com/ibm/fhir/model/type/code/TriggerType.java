/*
 * (C) Copyright IBM Corp. 2019, 2022
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

@System("http://hl7.org/fhir/trigger-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class TriggerType extends Code {
    /**
     * Named Event
     * 
     * <p>The trigger occurs in response to a specific named event, and no other information about the trigger is specified. 
     * Named events are completely pre-coordinated, and the formal semantics of the trigger are not provided.
     */
    public static final TriggerType NAMED_EVENT = TriggerType.builder().value(Value.NAMED_EVENT).build();

    /**
     * Periodic
     * 
     * <p>The trigger occurs at a specific time or periodically as described by a timing or schedule. A periodic event cannot 
     * have any data elements, but may have a name assigned as a shorthand for the event.
     */
    public static final TriggerType PERIODIC = TriggerType.builder().value(Value.PERIODIC).build();

    /**
     * Data Changed
     * 
     * <p>The trigger occurs whenever data of a particular type is changed in any way, either added, modified, or removed.
     */
    public static final TriggerType DATA_CHANGED = TriggerType.builder().value(Value.DATA_CHANGED).build();

    /**
     * Data Added
     * 
     * <p>The trigger occurs whenever data of a particular type is added.
     */
    public static final TriggerType DATA_ADDED = TriggerType.builder().value(Value.DATA_ADDED).build();

    /**
     * Data Updated
     * 
     * <p>The trigger occurs whenever data of a particular type is modified.
     */
    public static final TriggerType DATA_MODIFIED = TriggerType.builder().value(Value.DATA_MODIFIED).build();

    /**
     * Data Removed
     * 
     * <p>The trigger occurs whenever data of a particular type is removed.
     */
    public static final TriggerType DATA_REMOVED = TriggerType.builder().value(Value.DATA_REMOVED).build();

    /**
     * Data Accessed
     * 
     * <p>The trigger occurs whenever data of a particular type is accessed.
     */
    public static final TriggerType DATA_ACCESSED = TriggerType.builder().value(Value.DATA_ACCESSED).build();

    /**
     * Data Access Ended
     * 
     * <p>The trigger occurs whenever access to data of a particular type is completed.
     */
    public static final TriggerType DATA_ACCESS_ENDED = TriggerType.builder().value(Value.DATA_ACCESS_ENDED).build();

    private volatile int hashCode;

    private TriggerType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this TriggerType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating TriggerType objects from a passed enum value.
     */
    public static TriggerType of(Value value) {
        switch (value) {
        case NAMED_EVENT:
            return NAMED_EVENT;
        case PERIODIC:
            return PERIODIC;
        case DATA_CHANGED:
            return DATA_CHANGED;
        case DATA_ADDED:
            return DATA_ADDED;
        case DATA_MODIFIED:
            return DATA_MODIFIED;
        case DATA_REMOVED:
            return DATA_REMOVED;
        case DATA_ACCESSED:
            return DATA_ACCESSED;
        case DATA_ACCESS_ENDED:
            return DATA_ACCESS_ENDED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating TriggerType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static TriggerType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating TriggerType objects from a passed string value.
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
     * Inherited factory method for creating TriggerType objects from a passed string value.
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
        TriggerType other = (TriggerType) obj;
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
         *     An enum constant for TriggerType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public TriggerType build() {
            TriggerType triggerType = new TriggerType(this);
            if (validating) {
                validate(triggerType);
            }
            return triggerType;
        }

        protected void validate(TriggerType triggerType) {
            super.validate(triggerType);
        }

        protected Builder from(TriggerType triggerType) {
            super.from(triggerType);
            return this;
        }
    }

    public enum Value {
        /**
         * Named Event
         * 
         * <p>The trigger occurs in response to a specific named event, and no other information about the trigger is specified. 
         * Named events are completely pre-coordinated, and the formal semantics of the trigger are not provided.
         */
        NAMED_EVENT("named-event"),

        /**
         * Periodic
         * 
         * <p>The trigger occurs at a specific time or periodically as described by a timing or schedule. A periodic event cannot 
         * have any data elements, but may have a name assigned as a shorthand for the event.
         */
        PERIODIC("periodic"),

        /**
         * Data Changed
         * 
         * <p>The trigger occurs whenever data of a particular type is changed in any way, either added, modified, or removed.
         */
        DATA_CHANGED("data-changed"),

        /**
         * Data Added
         * 
         * <p>The trigger occurs whenever data of a particular type is added.
         */
        DATA_ADDED("data-added"),

        /**
         * Data Updated
         * 
         * <p>The trigger occurs whenever data of a particular type is modified.
         */
        DATA_MODIFIED("data-modified"),

        /**
         * Data Removed
         * 
         * <p>The trigger occurs whenever data of a particular type is removed.
         */
        DATA_REMOVED("data-removed"),

        /**
         * Data Accessed
         * 
         * <p>The trigger occurs whenever data of a particular type is accessed.
         */
        DATA_ACCESSED("data-accessed"),

        /**
         * Data Access Ended
         * 
         * <p>The trigger occurs whenever access to data of a particular type is completed.
         */
        DATA_ACCESS_ENDED("data-access-ended");

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
         * Factory method for creating TriggerType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding TriggerType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "named-event":
                return NAMED_EVENT;
            case "periodic":
                return PERIODIC;
            case "data-changed":
                return DATA_CHANGED;
            case "data-added":
                return DATA_ADDED;
            case "data-modified":
                return DATA_MODIFIED;
            case "data-removed":
                return DATA_REMOVED;
            case "data-accessed":
                return DATA_ACCESSED;
            case "data-access-ended":
                return DATA_ACCESS_ENDED;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
