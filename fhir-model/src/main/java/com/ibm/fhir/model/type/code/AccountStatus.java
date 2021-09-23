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

@System("http://hl7.org/fhir/account-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AccountStatus extends Code {
    /**
     * Active
     * 
     * <p>This account is active and may be used.
     */
    public static final AccountStatus ACTIVE = AccountStatus.builder().value(Value.ACTIVE).build();

    /**
     * Inactive
     * 
     * <p>This account is inactive and should not be used to track financial information.
     */
    public static final AccountStatus INACTIVE = AccountStatus.builder().value(Value.INACTIVE).build();

    /**
     * Entered in error
     * 
     * <p>This instance should not have been part of this patient's medical record.
     */
    public static final AccountStatus ENTERED_IN_ERROR = AccountStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * On Hold
     * 
     * <p>This account is on hold.
     */
    public static final AccountStatus ON_HOLD = AccountStatus.builder().value(Value.ON_HOLD).build();

    /**
     * Unknown
     * 
     * <p>The account status is unknown.
     */
    public static final AccountStatus UNKNOWN = AccountStatus.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private AccountStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this AccountStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating AccountStatus objects from a passed enum value.
     */
    public static AccountStatus of(Value value) {
        switch (value) {
        case ACTIVE:
            return ACTIVE;
        case INACTIVE:
            return INACTIVE;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case ON_HOLD:
            return ON_HOLD;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AccountStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AccountStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating AccountStatus objects from a passed string value.
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
     * Inherited factory method for creating AccountStatus objects from a passed string value.
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
        AccountStatus other = (AccountStatus) obj;
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
         *     An enum constant for AccountStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public AccountStatus build() {
            AccountStatus accountStatus = new AccountStatus(this);
            if (validating) {
                validate(accountStatus);
            }
            return accountStatus;
        }

        protected void validate(AccountStatus accountStatus) {
            super.validate(accountStatus);
        }

        protected Builder from(AccountStatus accountStatus) {
            super.from(accountStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Active
         * 
         * <p>This account is active and may be used.
         */
        ACTIVE("active"),

        /**
         * Inactive
         * 
         * <p>This account is inactive and should not be used to track financial information.
         */
        INACTIVE("inactive"),

        /**
         * Entered in error
         * 
         * <p>This instance should not have been part of this patient's medical record.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * On Hold
         * 
         * <p>This account is on hold.
         */
        ON_HOLD("on-hold"),

        /**
         * Unknown
         * 
         * <p>The account status is unknown.
         */
        UNKNOWN("unknown");

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
         * Factory method for creating AccountStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding AccountStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "active":
                return ACTIVE;
            case "inactive":
                return INACTIVE;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "on-hold":
                return ON_HOLD;
            case "unknown":
                return UNKNOWN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
