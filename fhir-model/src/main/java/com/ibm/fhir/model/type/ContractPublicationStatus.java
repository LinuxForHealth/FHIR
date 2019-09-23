/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class ContractPublicationStatus extends Code {
    /**
     * Amended
     */
    public static final ContractPublicationStatus AMENDED = ContractPublicationStatus.of(ValueSet.AMENDED);

    /**
     * Appended
     */
    public static final ContractPublicationStatus APPENDED = ContractPublicationStatus.of(ValueSet.APPENDED);

    /**
     * Cancelled
     */
    public static final ContractPublicationStatus CANCELLED = ContractPublicationStatus.of(ValueSet.CANCELLED);

    /**
     * Disputed
     */
    public static final ContractPublicationStatus DISPUTED = ContractPublicationStatus.of(ValueSet.DISPUTED);

    /**
     * Entered in Error
     */
    public static final ContractPublicationStatus ENTERED_IN_ERROR = ContractPublicationStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Executable
     */
    public static final ContractPublicationStatus EXECUTABLE = ContractPublicationStatus.of(ValueSet.EXECUTABLE);

    /**
     * Executed
     */
    public static final ContractPublicationStatus EXECUTED = ContractPublicationStatus.of(ValueSet.EXECUTED);

    /**
     * Negotiable
     */
    public static final ContractPublicationStatus NEGOTIABLE = ContractPublicationStatus.of(ValueSet.NEGOTIABLE);

    /**
     * Offered
     */
    public static final ContractPublicationStatus OFFERED = ContractPublicationStatus.of(ValueSet.OFFERED);

    /**
     * Policy
     */
    public static final ContractPublicationStatus POLICY = ContractPublicationStatus.of(ValueSet.POLICY);

    /**
     * Rejected
     */
    public static final ContractPublicationStatus REJECTED = ContractPublicationStatus.of(ValueSet.REJECTED);

    /**
     * Renewed
     */
    public static final ContractPublicationStatus RENEWED = ContractPublicationStatus.of(ValueSet.RENEWED);

    /**
     * Revoked
     */
    public static final ContractPublicationStatus REVOKED = ContractPublicationStatus.of(ValueSet.REVOKED);

    /**
     * Resolved
     */
    public static final ContractPublicationStatus RESOLVED = ContractPublicationStatus.of(ValueSet.RESOLVED);

    /**
     * Terminated
     */
    public static final ContractPublicationStatus TERMINATED = ContractPublicationStatus.of(ValueSet.TERMINATED);

    private volatile int hashCode;

    private ContractPublicationStatus(Builder builder) {
        super(builder);
    }

    public static ContractPublicationStatus of(java.lang.String value) {
        return ContractPublicationStatus.builder().value(value).build();
    }

    public static ContractPublicationStatus of(ValueSet value) {
        return ContractPublicationStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ContractPublicationStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ContractPublicationStatus.builder().value(value).build();
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
        ContractPublicationStatus other = (ContractPublicationStatus) obj;
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
        public ContractPublicationStatus build() {
            return new ContractPublicationStatus(this);
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
