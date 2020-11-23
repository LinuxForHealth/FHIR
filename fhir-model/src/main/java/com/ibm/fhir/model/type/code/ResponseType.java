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

@System("http://hl7.org/fhir/response-code")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ResponseType extends Code {
    /**
     * OK
     * 
     * <p>The message was accepted and processed without error.
     */
    public static final ResponseType OK = ResponseType.builder().value(ValueSet.OK).build();

    /**
     * Transient Error
     * 
     * <p>Some internal unexpected error occurred - wait and try again. Note - this is usually used for things like database 
     * unavailable, which may be expected to resolve, though human intervention may be required.
     */
    public static final ResponseType TRANSIENT_ERROR = ResponseType.builder().value(ValueSet.TRANSIENT_ERROR).build();

    /**
     * Fatal Error
     * 
     * <p>The message was rejected because of a problem with the content. There is no point in re-sending without change. The 
     * response narrative SHALL describe the issue.
     */
    public static final ResponseType FATAL_ERROR = ResponseType.builder().value(ValueSet.FATAL_ERROR).build();

    private volatile int hashCode;

    private ResponseType(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating ResponseType objects from a passed enum value.
     */
    public static ResponseType of(ValueSet value) {
        switch (value) {
        case OK:
            return OK;
        case TRANSIENT_ERROR:
            return TRANSIENT_ERROR;
        case FATAL_ERROR:
            return FATAL_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ResponseType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ResponseType of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating ResponseType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating ResponseType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
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
        ResponseType other = (ResponseType) obj;
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
        public ResponseType build() {
            return new ResponseType(this);
        }
    }

    public enum ValueSet {
        /**
         * OK
         * 
         * <p>The message was accepted and processed without error.
         */
        OK("ok"),

        /**
         * Transient Error
         * 
         * <p>Some internal unexpected error occurred - wait and try again. Note - this is usually used for things like database 
         * unavailable, which may be expected to resolve, though human intervention may be required.
         */
        TRANSIENT_ERROR("transient-error"),

        /**
         * Fatal Error
         * 
         * <p>The message was rejected because of a problem with the content. There is no point in re-sending without change. The 
         * response narrative SHALL describe the issue.
         */
        FATAL_ERROR("fatal-error");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
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
         * Factory method for creating ResponseType.ValueSet values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
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
