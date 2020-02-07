/*
 * (C) Copyright IBM Corp. 2019, 2020
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
public class Status extends Code {
    /**
     * Attested
     */
    public static final Status ATTESTED = Status.builder().value(ValueSet.ATTESTED).build();

    /**
     * Validated
     */
    public static final Status VALIDATED = Status.builder().value(ValueSet.VALIDATED).build();

    /**
     * In process
     */
    public static final Status IN_PROCESS = Status.builder().value(ValueSet.IN_PROCESS).build();

    /**
     * Requires revalidation
     */
    public static final Status REQ_REVALID = Status.builder().value(ValueSet.REQ_REVALID).build();

    /**
     * Validation failed
     */
    public static final Status VAL_FAIL = Status.builder().value(ValueSet.VAL_FAIL).build();

    /**
     * Re-Validation failed
     */
    public static final Status REVAL_FAIL = Status.builder().value(ValueSet.REVAL_FAIL).build();

    private volatile int hashCode;

    private Status(Builder builder) {
        super(builder);
    }

    public static Status of(ValueSet value) {
        switch (value) {
        case ATTESTED:
            return ATTESTED;
        case VALIDATED:
            return VALIDATED;
        case IN_PROCESS:
            return IN_PROCESS;
        case REQ_REVALID:
            return REQ_REVALID;
        case VAL_FAIL:
            return VAL_FAIL;
        case REVAL_FAIL:
            return REVAL_FAIL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static Status of(java.lang.String value) {
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
        Status other = (Status) obj;
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
        public Status build() {
            return new Status(this);
        }
    }

    public enum ValueSet {
        /**
         * Attested
         */
        ATTESTED("attested"),

        /**
         * Validated
         */
        VALIDATED("validated"),

        /**
         * In process
         */
        IN_PROCESS("in-process"),

        /**
         * Requires revalidation
         */
        REQ_REVALID("req-revalid"),

        /**
         * Validation failed
         */
        VAL_FAIL("val-fail"),

        /**
         * Re-Validation failed
         */
        REVAL_FAIL("reval-fail");

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
