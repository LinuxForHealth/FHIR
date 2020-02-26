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

@System("http://hl7.org/fhir/guidance-response-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class GuidanceResponseStatus extends Code {
    /**
     * Success
     */
    public static final GuidanceResponseStatus SUCCESS = GuidanceResponseStatus.builder().value(ValueSet.SUCCESS).build();

    /**
     * Data Requested
     */
    public static final GuidanceResponseStatus DATA_REQUESTED = GuidanceResponseStatus.builder().value(ValueSet.DATA_REQUESTED).build();

    /**
     * Data Required
     */
    public static final GuidanceResponseStatus DATA_REQUIRED = GuidanceResponseStatus.builder().value(ValueSet.DATA_REQUIRED).build();

    /**
     * In Progress
     */
    public static final GuidanceResponseStatus IN_PROGRESS = GuidanceResponseStatus.builder().value(ValueSet.IN_PROGRESS).build();

    /**
     * Failure
     */
    public static final GuidanceResponseStatus FAILURE = GuidanceResponseStatus.builder().value(ValueSet.FAILURE).build();

    /**
     * Entered In Error
     */
    public static final GuidanceResponseStatus ENTERED_IN_ERROR = GuidanceResponseStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private GuidanceResponseStatus(Builder builder) {
        super(builder);
    }

    public static GuidanceResponseStatus of(ValueSet value) {
        switch (value) {
        case SUCCESS:
            return SUCCESS;
        case DATA_REQUESTED:
            return DATA_REQUESTED;
        case DATA_REQUIRED:
            return DATA_REQUIRED;
        case IN_PROGRESS:
            return IN_PROGRESS;
        case FAILURE:
            return FAILURE;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static GuidanceResponseStatus of(java.lang.String value) {
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
        GuidanceResponseStatus other = (GuidanceResponseStatus) obj;
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
        public GuidanceResponseStatus build() {
            return new GuidanceResponseStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Success
         */
        SUCCESS("success"),

        /**
         * Data Requested
         */
        DATA_REQUESTED("data-requested"),

        /**
         * Data Required
         */
        DATA_REQUIRED("data-required"),

        /**
         * In Progress
         */
        IN_PROGRESS("in-progress"),

        /**
         * Failure
         */
        FAILURE("failure"),

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
