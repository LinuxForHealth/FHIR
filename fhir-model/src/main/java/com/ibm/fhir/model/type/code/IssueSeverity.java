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

@System("http://hl7.org/fhir/issue-severity")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class IssueSeverity extends Code {
    /**
     * Fatal
     * 
     * <p>The issue caused the action to fail and no further checking could be performed.
     */
    public static final IssueSeverity FATAL = IssueSeverity.builder().value(Value.FATAL).build();

    /**
     * Error
     * 
     * <p>The issue is sufficiently important to cause the action to fail.
     */
    public static final IssueSeverity ERROR = IssueSeverity.builder().value(Value.ERROR).build();

    /**
     * Warning
     * 
     * <p>The issue is not important enough to cause the action to fail but may cause it to be performed suboptimally or in a 
     * way that is not as desired.
     */
    public static final IssueSeverity WARNING = IssueSeverity.builder().value(Value.WARNING).build();

    /**
     * Information
     * 
     * <p>The issue has no relation to the degree of success of the action.
     */
    public static final IssueSeverity INFORMATION = IssueSeverity.builder().value(Value.INFORMATION).build();

    private volatile int hashCode;

    private IssueSeverity(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this IssueSeverity as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating IssueSeverity objects from a passed enum value.
     */
    public static IssueSeverity of(Value value) {
        switch (value) {
        case FATAL:
            return FATAL;
        case ERROR:
            return ERROR;
        case WARNING:
            return WARNING;
        case INFORMATION:
            return INFORMATION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating IssueSeverity objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static IssueSeverity of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating IssueSeverity objects from a passed string value.
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
     * Inherited factory method for creating IssueSeverity objects from a passed string value.
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
        IssueSeverity other = (IssueSeverity) obj;
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
         *     An enum constant for IssueSeverity
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public IssueSeverity build() {
            IssueSeverity issueSeverity = new IssueSeverity(this);
            if (validating) {
                validate(issueSeverity);
            }
            return issueSeverity;
        }

        protected void validate(IssueSeverity issueSeverity) {
            super.validate(issueSeverity);
        }

        protected Builder from(IssueSeverity issueSeverity) {
            super.from(issueSeverity);
            return this;
        }
    }

    public enum Value {
        /**
         * Fatal
         * 
         * <p>The issue caused the action to fail and no further checking could be performed.
         */
        FATAL("fatal"),

        /**
         * Error
         * 
         * <p>The issue is sufficiently important to cause the action to fail.
         */
        ERROR("error"),

        /**
         * Warning
         * 
         * <p>The issue is not important enough to cause the action to fail but may cause it to be performed suboptimally or in a 
         * way that is not as desired.
         */
        WARNING("warning"),

        /**
         * Information
         * 
         * <p>The issue has no relation to the degree of success of the action.
         */
        INFORMATION("information");

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
         * Factory method for creating IssueSeverity.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding IssueSeverity.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "fatal":
                return FATAL;
            case "error":
                return ERROR;
            case "warning":
                return WARNING;
            case "information":
                return INFORMATION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
