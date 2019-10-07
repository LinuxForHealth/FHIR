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
public class AuditEventAction extends Code {
    /**
     * Create
     */
    public static final AuditEventAction C = AuditEventAction.of(ValueSet.C);

    /**
     * Read/View/Print
     */
    public static final AuditEventAction R = AuditEventAction.of(ValueSet.R);

    /**
     * Update
     */
    public static final AuditEventAction U = AuditEventAction.of(ValueSet.U);

    /**
     * Delete
     */
    public static final AuditEventAction D = AuditEventAction.of(ValueSet.D);

    /**
     * Execute
     */
    public static final AuditEventAction E = AuditEventAction.of(ValueSet.E);

    private volatile int hashCode;

    private AuditEventAction(Builder builder) {
        super(builder);
    }

    public static AuditEventAction of(java.lang.String value) {
        return AuditEventAction.builder().value(value).build();
    }

    public static AuditEventAction of(ValueSet value) {
        return AuditEventAction.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return AuditEventAction.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return AuditEventAction.builder().value(value).build();
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
        AuditEventAction other = (AuditEventAction) obj;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public AuditEventAction build() {
            return new AuditEventAction(this);
        }
    }

    public enum ValueSet {
        /**
         * Create
         */
        C("C"),

        /**
         * Read/View/Print
         */
        R("R"),

        /**
         * Update
         */
        U("U"),

        /**
         * Delete
         */
        D("D"),

        /**
         * Execute
         */
        E("E");

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
