/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class ConditionalReadStatus extends Code {
    /**
     * Not Supported
     */
    public static final ConditionalReadStatus NOT_SUPPORTED = ConditionalReadStatus.of(ValueSet.NOT_SUPPORTED);

    /**
     * If-Modified-Since
     */
    public static final ConditionalReadStatus MODIFIED_SINCE = ConditionalReadStatus.of(ValueSet.MODIFIED_SINCE);

    /**
     * If-None-Match
     */
    public static final ConditionalReadStatus NOT_MATCH = ConditionalReadStatus.of(ValueSet.NOT_MATCH);

    /**
     * Full Support
     */
    public static final ConditionalReadStatus FULL_SUPPORT = ConditionalReadStatus.of(ValueSet.FULL_SUPPORT);

    private volatile int hashCode;

    private ConditionalReadStatus(Builder builder) {
        super(builder);
    }

    public static ConditionalReadStatus of(java.lang.String value) {
        return ConditionalReadStatus.builder().value(value).build();
    }

    public static ConditionalReadStatus of(ValueSet value) {
        return ConditionalReadStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ConditionalReadStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ConditionalReadStatus.builder().value(value).build();
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
        ConditionalReadStatus other = (ConditionalReadStatus) obj;
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
        public ConditionalReadStatus build() {
            return new ConditionalReadStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Not Supported
         */
        NOT_SUPPORTED("not-supported"),

        /**
         * If-Modified-Since
         */
        MODIFIED_SINCE("modified-since"),

        /**
         * If-None-Match
         */
        NOT_MATCH("not-match"),

        /**
         * Full Support
         */
        FULL_SUPPORT("full-support");

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
