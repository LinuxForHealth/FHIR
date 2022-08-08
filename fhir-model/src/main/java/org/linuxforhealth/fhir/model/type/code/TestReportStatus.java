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

@System("http://hl7.org/fhir/report-status-codes")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class TestReportStatus extends Code {
    /**
     * Completed
     * 
     * <p>All test operations have completed.
     */
    public static final TestReportStatus COMPLETED = TestReportStatus.builder().value(Value.COMPLETED).build();

    /**
     * In Progress
     * 
     * <p>A test operations is currently executing.
     */
    public static final TestReportStatus IN_PROGRESS = TestReportStatus.builder().value(Value.IN_PROGRESS).build();

    /**
     * Waiting
     * 
     * <p>A test operation is waiting for an external client request.
     */
    public static final TestReportStatus WAITING = TestReportStatus.builder().value(Value.WAITING).build();

    /**
     * Stopped
     * 
     * <p>The test script execution was manually stopped.
     */
    public static final TestReportStatus STOPPED = TestReportStatus.builder().value(Value.STOPPED).build();

    /**
     * Entered In Error
     * 
     * <p>This test report was entered or created in error.
     */
    public static final TestReportStatus ENTERED_IN_ERROR = TestReportStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private TestReportStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this TestReportStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating TestReportStatus objects from a passed enum value.
     */
    public static TestReportStatus of(Value value) {
        switch (value) {
        case COMPLETED:
            return COMPLETED;
        case IN_PROGRESS:
            return IN_PROGRESS;
        case WAITING:
            return WAITING;
        case STOPPED:
            return STOPPED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating TestReportStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static TestReportStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating TestReportStatus objects from a passed string value.
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
     * Inherited factory method for creating TestReportStatus objects from a passed string value.
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
        TestReportStatus other = (TestReportStatus) obj;
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
         *     An enum constant for TestReportStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public TestReportStatus build() {
            TestReportStatus testReportStatus = new TestReportStatus(this);
            if (validating) {
                validate(testReportStatus);
            }
            return testReportStatus;
        }

        protected void validate(TestReportStatus testReportStatus) {
            super.validate(testReportStatus);
        }

        protected Builder from(TestReportStatus testReportStatus) {
            super.from(testReportStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Completed
         * 
         * <p>All test operations have completed.
         */
        COMPLETED("completed"),

        /**
         * In Progress
         * 
         * <p>A test operations is currently executing.
         */
        IN_PROGRESS("in-progress"),

        /**
         * Waiting
         * 
         * <p>A test operation is waiting for an external client request.
         */
        WAITING("waiting"),

        /**
         * Stopped
         * 
         * <p>The test script execution was manually stopped.
         */
        STOPPED("stopped"),

        /**
         * Entered In Error
         * 
         * <p>This test report was entered or created in error.
         */
        ENTERED_IN_ERROR("entered-in-error");

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
         * Factory method for creating TestReportStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding TestReportStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "completed":
                return COMPLETED;
            case "in-progress":
                return IN_PROGRESS;
            case "waiting":
                return WAITING;
            case "stopped":
                return STOPPED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
