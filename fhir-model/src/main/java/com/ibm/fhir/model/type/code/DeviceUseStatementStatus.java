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
public class DeviceUseStatementStatus extends Code {
    /**
     * Active
     */
    public static final DeviceUseStatementStatus ACTIVE = DeviceUseStatementStatus.of(ValueSet.ACTIVE);

    /**
     * Completed
     */
    public static final DeviceUseStatementStatus COMPLETED = DeviceUseStatementStatus.of(ValueSet.COMPLETED);

    /**
     * Entered in Error
     */
    public static final DeviceUseStatementStatus ENTERED_IN_ERROR = DeviceUseStatementStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Intended
     */
    public static final DeviceUseStatementStatus INTENDED = DeviceUseStatementStatus.of(ValueSet.INTENDED);

    /**
     * Stopped
     */
    public static final DeviceUseStatementStatus STOPPED = DeviceUseStatementStatus.of(ValueSet.STOPPED);

    /**
     * On Hold
     */
    public static final DeviceUseStatementStatus ON_HOLD = DeviceUseStatementStatus.of(ValueSet.ON_HOLD);

    private volatile int hashCode;

    private DeviceUseStatementStatus(Builder builder) {
        super(builder);
    }

    public static DeviceUseStatementStatus of(java.lang.String value) {
        return DeviceUseStatementStatus.builder().value(value).build();
    }

    public static DeviceUseStatementStatus of(ValueSet value) {
        return DeviceUseStatementStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return DeviceUseStatementStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return DeviceUseStatementStatus.builder().value(value).build();
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
        DeviceUseStatementStatus other = (DeviceUseStatementStatus) obj;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public DeviceUseStatementStatus build() {
            return new DeviceUseStatementStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Completed
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Intended
         */
        INTENDED("intended"),

        /**
         * Stopped
         */
        STOPPED("stopped"),

        /**
         * On Hold
         */
        ON_HOLD("on-hold");

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
