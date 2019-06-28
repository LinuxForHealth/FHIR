/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class TestReportStatus extends Code {
    /**
     * Completed
     */
    public static final TestReportStatus COMPLETED = TestReportStatus.of(ValueSet.COMPLETED);

    /**
     * In Progress
     */
    public static final TestReportStatus IN_PROGRESS = TestReportStatus.of(ValueSet.IN_PROGRESS);

    /**
     * Waiting
     */
    public static final TestReportStatus WAITING = TestReportStatus.of(ValueSet.WAITING);

    /**
     * Stopped
     */
    public static final TestReportStatus STOPPED = TestReportStatus.of(ValueSet.STOPPED);

    /**
     * Entered In Error
     */
    public static final TestReportStatus ENTERED_IN_ERROR = TestReportStatus.of(ValueSet.ENTERED_IN_ERROR);

    private TestReportStatus(Builder builder) {
        super(builder);
    }

    public static TestReportStatus of(java.lang.String value) {
        return TestReportStatus.builder().value(value).build();
    }

    public static TestReportStatus of(ValueSet value) {
        return TestReportStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return TestReportStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return TestReportStatus.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
