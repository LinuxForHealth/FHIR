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
public class ConsentState extends Code {
    /**
     * Pending
     */
    public static final ConsentState DRAFT = ConsentState.builder().value(ValueSet.DRAFT).build();

    /**
     * Proposed
     */
    public static final ConsentState PROPOSED = ConsentState.builder().value(ValueSet.PROPOSED).build();

    /**
     * Active
     */
    public static final ConsentState ACTIVE = ConsentState.builder().value(ValueSet.ACTIVE).build();

    /**
     * Rejected
     */
    public static final ConsentState REJECTED = ConsentState.builder().value(ValueSet.REJECTED).build();

    /**
     * Inactive
     */
    public static final ConsentState INACTIVE = ConsentState.builder().value(ValueSet.INACTIVE).build();

    /**
     * Entered in Error
     */
    public static final ConsentState ENTERED_IN_ERROR = ConsentState.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private ConsentState(Builder builder) {
        super(builder);
    }

    public static ConsentState of(ValueSet value) {
        switch (value) {
        case DRAFT:
            return DRAFT;
        case PROPOSED:
            return PROPOSED;
        case ACTIVE:
            return ACTIVE;
        case REJECTED:
            return REJECTED;
        case INACTIVE:
            return INACTIVE;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static ConsentState of(java.lang.String value) {
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
        ConsentState other = (ConsentState) obj;
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
        public ConsentState build() {
            return new ConsentState(this);
        }
    }

    public enum ValueSet {
        /**
         * Pending
         */
        DRAFT("draft"),

        /**
         * Proposed
         */
        PROPOSED("proposed"),

        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Rejected
         */
        REJECTED("rejected"),

        /**
         * Inactive
         */
        INACTIVE("inactive"),

        /**
         * Entered in Error
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
