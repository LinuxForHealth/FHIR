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
public class SlicingRules extends Code {
    /**
     * Closed
     */
    public static final SlicingRules CLOSED = SlicingRules.builder().value(ValueSet.CLOSED).build();

    /**
     * Open
     */
    public static final SlicingRules OPEN = SlicingRules.builder().value(ValueSet.OPEN).build();

    /**
     * Open at End
     */
    public static final SlicingRules OPEN_AT_END = SlicingRules.builder().value(ValueSet.OPEN_AT_END).build();

    private volatile int hashCode;

    private SlicingRules(Builder builder) {
        super(builder);
    }

    public static SlicingRules of(ValueSet value) {
        switch (value) {
        case CLOSED:
            return CLOSED;
        case OPEN:
            return OPEN;
        case OPEN_AT_END:
            return OPEN_AT_END;
        default:
            throw new IllegalArgumentException(value.name());
        }
    }

    public static SlicingRules of(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.valueOf(value));
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
        SlicingRules other = (SlicingRules) obj;
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
        public SlicingRules build() {
            return new SlicingRules(this);
        }
    }

    public enum ValueSet {
        /**
         * Closed
         */
        CLOSED("closed"),

        /**
         * Open
         */
        OPEN("open"),

        /**
         * Open at End
         */
        OPEN_AT_END("openAtEnd");

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
