/*
 * (C) Copyright IBM Corp. 2019, 2020
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

@System("http://hl7.org/fhir/report-status-codes")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class TestReportStatus extends Code {
    /**
     * Completed
     */
    public static final TestReportStatus COMPLETED = TestReportStatus.builder().value(ValueSet.COMPLETED).build();

    /**
     * In Progress
     */
    public static final TestReportStatus IN_PROGRESS = TestReportStatus.builder().value(ValueSet.IN_PROGRESS).build();

    /**
     * Waiting
     */
    public static final TestReportStatus WAITING = TestReportStatus.builder().value(ValueSet.WAITING).build();

    /**
     * Stopped
     */
    public static final TestReportStatus STOPPED = TestReportStatus.builder().value(ValueSet.STOPPED).build();

    /**
     * Entered In Error
     */
    public static final TestReportStatus ENTERED_IN_ERROR = TestReportStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private TestReportStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    public static TestReportStatus of(ValueSet value) {
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

    public static TestReportStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public TestReportStatus build() {
            return new TestReportStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Completed
         */
        COMPLETED("completed"),

        /**
         * In Progress
         */
        IN_PROGRESS("in-progress"),

        /**
         * Waiting
         */
        WAITING("waiting"),

        /**
         * Stopped
         */
        STOPPED("stopped"),

        /**
         * Entered In Error
         */
        ENTERED_IN_ERROR("entered-in-error");

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
