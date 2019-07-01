/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class TestReportParticipantType extends Code {
    /**
     * Test Engine
     */
    public static final TestReportParticipantType TEST_ENGINE = TestReportParticipantType.of(ValueSet.TEST_ENGINE);

    /**
     * Client
     */
    public static final TestReportParticipantType CLIENT = TestReportParticipantType.of(ValueSet.CLIENT);

    /**
     * Server
     */
    public static final TestReportParticipantType SERVER = TestReportParticipantType.of(ValueSet.SERVER);

    private volatile int hashCode;

    private TestReportParticipantType(Builder builder) {
        super(builder);
    }

    public static TestReportParticipantType of(java.lang.String value) {
        return TestReportParticipantType.builder().value(value).build();
    }

    public static TestReportParticipantType of(ValueSet value) {
        return TestReportParticipantType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return TestReportParticipantType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return TestReportParticipantType.builder().value(value).build();
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
        TestReportParticipantType other = (TestReportParticipantType) obj;
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
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
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
        public TestReportParticipantType build() {
            return new TestReportParticipantType(this);
        }
    }

    public enum ValueSet {
        /**
         * Test Engine
         */
        TEST_ENGINE("test-engine"),

        /**
         * Client
         */
        CLIENT("client"),

        /**
         * Server
         */
        SERVER("server");

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
