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
public class CommunicationPriority extends Code {
    /**
     * Routine
     */
    public static final CommunicationPriority ROUTINE = CommunicationPriority.builder().value(ValueSet.ROUTINE).build();

    /**
     * Urgent
     */
    public static final CommunicationPriority URGENT = CommunicationPriority.builder().value(ValueSet.URGENT).build();

    /**
     * ASAP
     */
    public static final CommunicationPriority ASAP = CommunicationPriority.builder().value(ValueSet.ASAP).build();

    /**
     * STAT
     */
    public static final CommunicationPriority STAT = CommunicationPriority.builder().value(ValueSet.STAT).build();

    private volatile int hashCode;

    private CommunicationPriority(Builder builder) {
        super(builder);
    }

    public static CommunicationPriority of(ValueSet value) {
        switch (value) {
        case ROUTINE:
            return ROUTINE;
        case URGENT:
            return URGENT;
        case ASAP:
            return ASAP;
        case STAT:
            return STAT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static CommunicationPriority of(java.lang.String value) {
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
        CommunicationPriority other = (CommunicationPriority) obj;
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
        public CommunicationPriority build() {
            return new CommunicationPriority(this);
        }
    }

    public enum ValueSet {
        /**
         * Routine
         */
        ROUTINE("routine"),

        /**
         * Urgent
         */
        URGENT("urgent"),

        /**
         * ASAP
         */
        ASAP("asap"),

        /**
         * STAT
         */
        STAT("stat");

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
