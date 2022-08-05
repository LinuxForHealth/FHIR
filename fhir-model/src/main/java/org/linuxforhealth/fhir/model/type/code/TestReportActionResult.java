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

@System("http://hl7.org/fhir/report-action-result-codes")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class TestReportActionResult extends Code {
    /**
     * Pass
     * 
     * <p>The action was successful.
     */
    public static final TestReportActionResult PASS = TestReportActionResult.builder().value(Value.PASS).build();

    /**
     * Skip
     * 
     * <p>The action was skipped.
     */
    public static final TestReportActionResult SKIP = TestReportActionResult.builder().value(Value.SKIP).build();

    /**
     * Fail
     * 
     * <p>The action failed.
     */
    public static final TestReportActionResult FAIL = TestReportActionResult.builder().value(Value.FAIL).build();

    /**
     * Warning
     * 
     * <p>The action passed but with warnings.
     */
    public static final TestReportActionResult WARNING = TestReportActionResult.builder().value(Value.WARNING).build();

    /**
     * Error
     * 
     * <p>The action encountered a fatal error and the engine was unable to process.
     */
    public static final TestReportActionResult ERROR = TestReportActionResult.builder().value(Value.ERROR).build();

    private volatile int hashCode;

    private TestReportActionResult(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this TestReportActionResult as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating TestReportActionResult objects from a passed enum value.
     */
    public static TestReportActionResult of(Value value) {
        switch (value) {
        case PASS:
            return PASS;
        case SKIP:
            return SKIP;
        case FAIL:
            return FAIL;
        case WARNING:
            return WARNING;
        case ERROR:
            return ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating TestReportActionResult objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static TestReportActionResult of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating TestReportActionResult objects from a passed string value.
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
     * Inherited factory method for creating TestReportActionResult objects from a passed string value.
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
        TestReportActionResult other = (TestReportActionResult) obj;
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
         *     An enum constant for TestReportActionResult
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public TestReportActionResult build() {
            TestReportActionResult testReportActionResult = new TestReportActionResult(this);
            if (validating) {
                validate(testReportActionResult);
            }
            return testReportActionResult;
        }

        protected void validate(TestReportActionResult testReportActionResult) {
            super.validate(testReportActionResult);
        }

        protected Builder from(TestReportActionResult testReportActionResult) {
            super.from(testReportActionResult);
            return this;
        }
    }

    public enum Value {
        /**
         * Pass
         * 
         * <p>The action was successful.
         */
        PASS("pass"),

        /**
         * Skip
         * 
         * <p>The action was skipped.
         */
        SKIP("skip"),

        /**
         * Fail
         * 
         * <p>The action failed.
         */
        FAIL("fail"),

        /**
         * Warning
         * 
         * <p>The action passed but with warnings.
         */
        WARNING("warning"),

        /**
         * Error
         * 
         * <p>The action encountered a fatal error and the engine was unable to process.
         */
        ERROR("error");

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
         * Factory method for creating TestReportActionResult.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding TestReportActionResult.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "pass":
                return PASS;
            case "skip":
                return SKIP;
            case "fail":
                return FAIL;
            case "warning":
                return WARNING;
            case "error":
                return ERROR;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
