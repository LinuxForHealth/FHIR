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

@System("http://hl7.org/fhir/action-relationship-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ActionRelationshipType extends Code {
    /**
     * Before Start
     * 
     * <p>The action must be performed before the start of the related action.
     */
    public static final ActionRelationshipType BEFORE_START = ActionRelationshipType.builder().value(Value.BEFORE_START).build();

    /**
     * Before
     * 
     * <p>The action must be performed before the related action.
     */
    public static final ActionRelationshipType BEFORE = ActionRelationshipType.builder().value(Value.BEFORE).build();

    /**
     * Before End
     * 
     * <p>The action must be performed before the end of the related action.
     */
    public static final ActionRelationshipType BEFORE_END = ActionRelationshipType.builder().value(Value.BEFORE_END).build();

    /**
     * Concurrent With Start
     * 
     * <p>The action must be performed concurrent with the start of the related action.
     */
    public static final ActionRelationshipType CONCURRENT_WITH_START = ActionRelationshipType.builder().value(Value.CONCURRENT_WITH_START).build();

    /**
     * Concurrent
     * 
     * <p>The action must be performed concurrent with the related action.
     */
    public static final ActionRelationshipType CONCURRENT = ActionRelationshipType.builder().value(Value.CONCURRENT).build();

    /**
     * Concurrent With End
     * 
     * <p>The action must be performed concurrent with the end of the related action.
     */
    public static final ActionRelationshipType CONCURRENT_WITH_END = ActionRelationshipType.builder().value(Value.CONCURRENT_WITH_END).build();

    /**
     * After Start
     * 
     * <p>The action must be performed after the start of the related action.
     */
    public static final ActionRelationshipType AFTER_START = ActionRelationshipType.builder().value(Value.AFTER_START).build();

    /**
     * After
     * 
     * <p>The action must be performed after the related action.
     */
    public static final ActionRelationshipType AFTER = ActionRelationshipType.builder().value(Value.AFTER).build();

    /**
     * After End
     * 
     * <p>The action must be performed after the end of the related action.
     */
    public static final ActionRelationshipType AFTER_END = ActionRelationshipType.builder().value(Value.AFTER_END).build();

    private volatile int hashCode;

    private ActionRelationshipType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ActionRelationshipType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ActionRelationshipType objects from a passed enum value.
     */
    public static ActionRelationshipType of(Value value) {
        switch (value) {
        case BEFORE_START:
            return BEFORE_START;
        case BEFORE:
            return BEFORE;
        case BEFORE_END:
            return BEFORE_END;
        case CONCURRENT_WITH_START:
            return CONCURRENT_WITH_START;
        case CONCURRENT:
            return CONCURRENT;
        case CONCURRENT_WITH_END:
            return CONCURRENT_WITH_END;
        case AFTER_START:
            return AFTER_START;
        case AFTER:
            return AFTER;
        case AFTER_END:
            return AFTER_END;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ActionRelationshipType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ActionRelationshipType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ActionRelationshipType objects from a passed string value.
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
     * Inherited factory method for creating ActionRelationshipType objects from a passed string value.
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
        ActionRelationshipType other = (ActionRelationshipType) obj;
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
         *     An enum constant for ActionRelationshipType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ActionRelationshipType build() {
            ActionRelationshipType actionRelationshipType = new ActionRelationshipType(this);
            if (validating) {
                validate(actionRelationshipType);
            }
            return actionRelationshipType;
        }

        protected void validate(ActionRelationshipType actionRelationshipType) {
            super.validate(actionRelationshipType);
        }

        protected Builder from(ActionRelationshipType actionRelationshipType) {
            super.from(actionRelationshipType);
            return this;
        }
    }

    public enum Value {
        /**
         * Before Start
         * 
         * <p>The action must be performed before the start of the related action.
         */
        BEFORE_START("before-start"),

        /**
         * Before
         * 
         * <p>The action must be performed before the related action.
         */
        BEFORE("before"),

        /**
         * Before End
         * 
         * <p>The action must be performed before the end of the related action.
         */
        BEFORE_END("before-end"),

        /**
         * Concurrent With Start
         * 
         * <p>The action must be performed concurrent with the start of the related action.
         */
        CONCURRENT_WITH_START("concurrent-with-start"),

        /**
         * Concurrent
         * 
         * <p>The action must be performed concurrent with the related action.
         */
        CONCURRENT("concurrent"),

        /**
         * Concurrent With End
         * 
         * <p>The action must be performed concurrent with the end of the related action.
         */
        CONCURRENT_WITH_END("concurrent-with-end"),

        /**
         * After Start
         * 
         * <p>The action must be performed after the start of the related action.
         */
        AFTER_START("after-start"),

        /**
         * After
         * 
         * <p>The action must be performed after the related action.
         */
        AFTER("after"),

        /**
         * After End
         * 
         * <p>The action must be performed after the end of the related action.
         */
        AFTER_END("after-end");

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
         * Factory method for creating ActionRelationshipType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ActionRelationshipType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "before-start":
                return BEFORE_START;
            case "before":
                return BEFORE;
            case "before-end":
                return BEFORE_END;
            case "concurrent-with-start":
                return CONCURRENT_WITH_START;
            case "concurrent":
                return CONCURRENT;
            case "concurrent-with-end":
                return CONCURRENT_WITH_END;
            case "after-start":
                return AFTER_START;
            case "after":
                return AFTER;
            case "after-end":
                return AFTER_END;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
