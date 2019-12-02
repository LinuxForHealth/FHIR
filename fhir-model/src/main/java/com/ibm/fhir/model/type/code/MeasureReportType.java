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
public class MeasureReportType extends Code {
    /**
     * Individual
     */
    public static final MeasureReportType INDIVIDUAL = MeasureReportType.builder().value(ValueSet.INDIVIDUAL).build();

    /**
     * Subject List
     */
    public static final MeasureReportType SUBJECT_LIST = MeasureReportType.builder().value(ValueSet.SUBJECT_LIST).build();

    /**
     * Summary
     */
    public static final MeasureReportType SUMMARY = MeasureReportType.builder().value(ValueSet.SUMMARY).build();

    /**
     * Data Collection
     */
    public static final MeasureReportType DATA_COLLECTION = MeasureReportType.builder().value(ValueSet.DATA_COLLECTION).build();

    private volatile int hashCode;

    private MeasureReportType(Builder builder) {
        super(builder);
    }

    public static MeasureReportType of(ValueSet value) {
        switch (value) {
        case INDIVIDUAL:
            return INDIVIDUAL;
        case SUBJECT_LIST:
            return SUBJECT_LIST;
        case SUMMARY:
            return SUMMARY;
        case DATA_COLLECTION:
            return DATA_COLLECTION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static MeasureReportType of(java.lang.String value) {
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
        MeasureReportType other = (MeasureReportType) obj;
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
        public MeasureReportType build() {
            return new MeasureReportType(this);
        }
    }

    public enum ValueSet {
        /**
         * Individual
         */
        INDIVIDUAL("individual"),

        /**
         * Subject List
         */
        SUBJECT_LIST("subject-list"),

        /**
         * Summary
         */
        SUMMARY("summary"),

        /**
         * Data Collection
         */
        DATA_COLLECTION("data-collection");

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
