/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class IssueType extends Code {
    /**
     * Invalid Content
     */
    public static final IssueType INVALID = IssueType.of(ValueSet.INVALID);

    /**
     * Security Problem
     */
    public static final IssueType SECURITY = IssueType.of(ValueSet.SECURITY);

    /**
     * Processing Failure
     */
    public static final IssueType PROCESSING = IssueType.of(ValueSet.PROCESSING);

    /**
     * Transient Issue
     */
    public static final IssueType TRANSIENT = IssueType.of(ValueSet.TRANSIENT);

    /**
     * Informational Note
     */
    public static final IssueType INFORMATIONAL = IssueType.of(ValueSet.INFORMATIONAL);

    private IssueType(Builder builder) {
        super(builder);
    }

    public static IssueType of(java.lang.String value) {
        return IssueType.builder().value(value).build();
    }

    public static IssueType of(ValueSet value) {
        return IssueType.builder().value(value).build();
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
        public IssueType build() {
            return new IssueType(this);
        }
    }

    public enum ValueSet {
        /**
         * Invalid Content
         */
        INVALID("invalid"),

        /**
         * Security Problem
         */
        SECURITY("security"),

        /**
         * Processing Failure
         */
        PROCESSING("processing"),

        /**
         * Transient Issue
         */
        TRANSIENT("transient"),

        /**
         * Informational Note
         */
        INFORMATIONAL("informational");

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
