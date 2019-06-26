/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class TestScriptRequestMethodCode extends Code {
    /**
     * DELETE
     */
    public static final TestScriptRequestMethodCode DELETE = TestScriptRequestMethodCode.of(ValueSet.DELETE);

    /**
     * GET
     */
    public static final TestScriptRequestMethodCode GET = TestScriptRequestMethodCode.of(ValueSet.GET);

    /**
     * OPTIONS
     */
    public static final TestScriptRequestMethodCode OPTIONS = TestScriptRequestMethodCode.of(ValueSet.OPTIONS);

    /**
     * PATCH
     */
    public static final TestScriptRequestMethodCode PATCH = TestScriptRequestMethodCode.of(ValueSet.PATCH);

    /**
     * POST
     */
    public static final TestScriptRequestMethodCode POST = TestScriptRequestMethodCode.of(ValueSet.POST);

    /**
     * PUT
     */
    public static final TestScriptRequestMethodCode PUT = TestScriptRequestMethodCode.of(ValueSet.PUT);

    /**
     * HEAD
     */
    public static final TestScriptRequestMethodCode HEAD = TestScriptRequestMethodCode.of(ValueSet.HEAD);

    private TestScriptRequestMethodCode(Builder builder) {
        super(builder);
    }

    public static TestScriptRequestMethodCode of(java.lang.String value) {
        return TestScriptRequestMethodCode.builder().value(value).build();
    }

    public static TestScriptRequestMethodCode of(ValueSet value) {
        return TestScriptRequestMethodCode.builder().value(value).build();
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
        public TestScriptRequestMethodCode build() {
            return new TestScriptRequestMethodCode(this);
        }
    }

    public enum ValueSet {
        /**
         * DELETE
         */
        DELETE("delete"),

        /**
         * GET
         */
        GET("get"),

        /**
         * OPTIONS
         */
        OPTIONS("options"),

        /**
         * PATCH
         */
        PATCH("patch"),

        /**
         * POST
         */
        POST("post"),

        /**
         * PUT
         */
        PUT("put"),

        /**
         * HEAD
         */
        HEAD("head");

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
