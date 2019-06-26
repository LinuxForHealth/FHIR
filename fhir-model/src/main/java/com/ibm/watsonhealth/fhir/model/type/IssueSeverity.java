/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class IssueSeverity extends Code {
    /**
     * Fatal
     */
    public static final IssueSeverity FATAL = IssueSeverity.of(ValueSet.FATAL);

    /**
     * Error
     */
    public static final IssueSeverity ERROR = IssueSeverity.of(ValueSet.ERROR);

    /**
     * Warning
     */
    public static final IssueSeverity WARNING = IssueSeverity.of(ValueSet.WARNING);

    /**
     * Information
     */
    public static final IssueSeverity INFORMATION = IssueSeverity.of(ValueSet.INFORMATION);

    private IssueSeverity(Builder builder) {
        super(builder);
    }

    public static IssueSeverity of(java.lang.String value) {
        return IssueSeverity.builder().value(value).build();
    }

    public static IssueSeverity of(ValueSet value) {
        return IssueSeverity.builder().value(value).build();
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
        public IssueSeverity build() {
            return new IssueSeverity(this);
        }
    }

    public enum ValueSet {
        /**
         * Fatal
         */
        FATAL("fatal"),

        /**
         * Error
         */
        ERROR("error"),

        /**
         * Warning
         */
        WARNING("warning"),

        /**
         * Information
         */
        INFORMATION("information");

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
