/*
 * (C) Copyright IBM Corp. 2019, 2022
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

@System("http://hl7.org/fhir/audit-event-action")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AuditEventAction extends Code {
    /**
     * Create
     * 
     * <p>Create a new database object, such as placing an order.
     */
    public static final AuditEventAction C = AuditEventAction.builder().value(Value.C).build();

    /**
     * Read/View/Print
     * 
     * <p>Display or print data, such as a doctor census.
     */
    public static final AuditEventAction R = AuditEventAction.builder().value(Value.R).build();

    /**
     * Update
     * 
     * <p>Update data, such as revise patient information.
     */
    public static final AuditEventAction U = AuditEventAction.builder().value(Value.U).build();

    /**
     * Delete
     * 
     * <p>Delete items, such as a doctor master file record.
     */
    public static final AuditEventAction D = AuditEventAction.builder().value(Value.D).build();

    /**
     * Execute
     * 
     * <p>Perform a system or application function such as log-on, program execution or use of an object's method, or perform 
     * a query/search operation.
     */
    public static final AuditEventAction E = AuditEventAction.builder().value(Value.E).build();

    private volatile int hashCode;

    private AuditEventAction(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this AuditEventAction as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating AuditEventAction objects from a passed enum value.
     */
    public static AuditEventAction of(Value value) {
        switch (value) {
        case C:
            return C;
        case R:
            return R;
        case U:
            return U;
        case D:
            return D;
        case E:
            return E;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AuditEventAction objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AuditEventAction of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating AuditEventAction objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating AuditEventAction objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(Value.from(value));
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
        return new Builder().from(this);
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
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for AuditEventAction
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public AuditEventAction build() {
            AuditEventAction auditEventAction = new AuditEventAction(this);
            if (validating) {
                validate(auditEventAction);
            }
            return auditEventAction;
        }

        protected void validate(AuditEventAction auditEventAction) {
            super.validate(auditEventAction);
        }

        protected Builder from(AuditEventAction auditEventAction) {
            super.from(auditEventAction);
            return this;
        }
    }

    public enum Value {
        /**
         * Create
         * 
         * <p>Create a new database object, such as placing an order.
         */
        C("C"),

        /**
         * Read/View/Print
         * 
         * <p>Display or print data, such as a doctor census.
         */
        R("R"),

        /**
         * Update
         * 
         * <p>Update data, such as revise patient information.
         */
        U("U"),

        /**
         * Delete
         * 
         * <p>Delete items, such as a doctor master file record.
         */
        D("D"),

        /**
         * Execute
         * 
         * <p>Perform a system or application function such as log-on, program execution or use of an object's method, or perform 
         * a query/search operation.
         */
        E("E");

        private final java.lang.String value;

        Value(java.lang.String value) {
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
         * Factory method for creating AuditEventAction.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding AuditEventAction.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "C":
                return C;
            case "R":
                return R;
            case "U":
                return U;
            case "D":
                return D;
            case "E":
                return E;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
