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
public class TestScriptRequestMethodCode extends Code {
    /**
     * DELETE
     */
    public static final TestScriptRequestMethodCode DELETE = TestScriptRequestMethodCode.builder().value(ValueSet.DELETE).build();

    /**
     * GET
     */
    public static final TestScriptRequestMethodCode GET = TestScriptRequestMethodCode.builder().value(ValueSet.GET).build();

    /**
     * OPTIONS
     */
    public static final TestScriptRequestMethodCode OPTIONS = TestScriptRequestMethodCode.builder().value(ValueSet.OPTIONS).build();

    /**
     * PATCH
     */
    public static final TestScriptRequestMethodCode PATCH = TestScriptRequestMethodCode.builder().value(ValueSet.PATCH).build();

    /**
     * POST
     */
    public static final TestScriptRequestMethodCode POST = TestScriptRequestMethodCode.builder().value(ValueSet.POST).build();

    /**
     * PUT
     */
    public static final TestScriptRequestMethodCode PUT = TestScriptRequestMethodCode.builder().value(ValueSet.PUT).build();

    /**
     * HEAD
     */
    public static final TestScriptRequestMethodCode HEAD = TestScriptRequestMethodCode.builder().value(ValueSet.HEAD).build();

    private volatile int hashCode;

    private TestScriptRequestMethodCode(Builder builder) {
        super(builder);
    }

    public static TestScriptRequestMethodCode of(ValueSet value) {
        switch (value) {
        case DELETE:
            return DELETE;
        case GET:
            return GET;
        case OPTIONS:
            return OPTIONS;
        case PATCH:
            return PATCH;
        case POST:
            return POST;
        case PUT:
            return PUT;
        case HEAD:
            return HEAD;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static TestScriptRequestMethodCode of(java.lang.String value) {
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
        TestScriptRequestMethodCode other = (TestScriptRequestMethodCode) obj;
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
