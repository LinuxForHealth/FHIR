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
public class ContractStatus extends Code {
    /**
     * Amended
     */
    public static final ContractStatus AMENDED = ContractStatus.of(ValueSet.AMENDED);

    /**
     * Appended
     */
    public static final ContractStatus APPENDED = ContractStatus.of(ValueSet.APPENDED);

    /**
     * Cancelled
     */
    public static final ContractStatus CANCELLED = ContractStatus.of(ValueSet.CANCELLED);

    /**
     * Disputed
     */
    public static final ContractStatus DISPUTED = ContractStatus.of(ValueSet.DISPUTED);

    /**
     * Entered in Error
     */
    public static final ContractStatus ENTERED_IN_ERROR = ContractStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Executable
     */
    public static final ContractStatus EXECUTABLE = ContractStatus.of(ValueSet.EXECUTABLE);

    /**
     * Executed
     */
    public static final ContractStatus EXECUTED = ContractStatus.of(ValueSet.EXECUTED);

    /**
     * Negotiable
     */
    public static final ContractStatus NEGOTIABLE = ContractStatus.of(ValueSet.NEGOTIABLE);

    /**
     * Offered
     */
    public static final ContractStatus OFFERED = ContractStatus.of(ValueSet.OFFERED);

    /**
     * Policy
     */
    public static final ContractStatus POLICY = ContractStatus.of(ValueSet.POLICY);

    /**
     * Rejected
     */
    public static final ContractStatus REJECTED = ContractStatus.of(ValueSet.REJECTED);

    /**
     * Renewed
     */
    public static final ContractStatus RENEWED = ContractStatus.of(ValueSet.RENEWED);

    /**
     * Revoked
     */
    public static final ContractStatus REVOKED = ContractStatus.of(ValueSet.REVOKED);

    /**
     * Resolved
     */
    public static final ContractStatus RESOLVED = ContractStatus.of(ValueSet.RESOLVED);

    /**
     * Terminated
     */
    public static final ContractStatus TERMINATED = ContractStatus.of(ValueSet.TERMINATED);

    private volatile int hashCode;

    private ContractStatus(Builder builder) {
        super(builder);
    }

    public static ContractStatus of(java.lang.String value) {
        return ContractStatus.builder().value(value).build();
    }

    public static ContractStatus of(ValueSet value) {
        return ContractStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ContractStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ContractStatus.builder().value(value).build();
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
        ContractStatus other = (ContractStatus) obj;
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
        public ContractStatus build() {
            return new ContractStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Amended
         */
        AMENDED("amended"),

        /**
         * Appended
         */
        APPENDED("appended"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

        /**
         * Disputed
         */
        DISPUTED("disputed"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Executable
         */
        EXECUTABLE("executable"),

        /**
         * Executed
         */
        EXECUTED("executed"),

        /**
         * Negotiable
         */
        NEGOTIABLE("negotiable"),

        /**
         * Offered
         */
        OFFERED("offered"),

        /**
         * Policy
         */
        POLICY("policy"),

        /**
         * Rejected
         */
        REJECTED("rejected"),

        /**
         * Renewed
         */
        RENEWED("renewed"),

        /**
         * Revoked
         */
        REVOKED("revoked"),

        /**
         * Resolved
         */
        RESOLVED("resolved"),

        /**
         * Terminated
         */
        TERMINATED("terminated");

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
