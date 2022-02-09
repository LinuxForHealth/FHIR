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

@System("http://hl7.org/fhir/contract-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ContractStatus extends Code {
    /**
     * Amended
     * 
     * <p>Contract is augmented with additional information to correct errors in a predecessor or to updated values in a 
     * predecessor. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status 
     * codes: revised; replaced.
     */
    public static final ContractStatus AMENDED = ContractStatus.builder().value(Value.AMENDED).build();

    /**
     * Appended
     * 
     * <p>Contract is augmented with additional information that was missing from a predecessor Contract. Usage: Contract 
     * altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: updated, replaced.
     */
    public static final ContractStatus APPENDED = ContractStatus.builder().value(Value.APPENDED).build();

    /**
     * Cancelled
     * 
     * <p>Contract is terminated due to failure of the Grantor and/or the Grantee to fulfil one or more contract provisions. 
     * Usage: Abnormal contract termination. Precedence Order = 10. Comparable FHIR and v.3 status codes: stopped; failed; 
     * aborted.
     */
    public static final ContractStatus CANCELLED = ContractStatus.builder().value(Value.CANCELLED).build();

    /**
     * Disputed
     * 
     * <p>Contract is pended to rectify failure of the Grantor or the Grantee to fulfil contract provision(s). E.g., Grantee 
     * complaint about Grantor's failure to comply with contract provisions. Usage: Contract pended. Precedence Order = 7. 
     * Comparable FHIR and v.3 status codes: on hold; pended; suspended.
     */
    public static final ContractStatus DISPUTED = ContractStatus.builder().value(Value.DISPUTED).build();

    /**
     * Entered in Error
     * 
     * <p>Contract was created in error. No Precedence Order. Status may be applied to a Contract with any status.
     */
    public static final ContractStatus ENTERED_IN_ERROR = ContractStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Executable
     * 
     * <p>Contract execution pending; may be executed when either the Grantor or the Grantee accepts the contract provisions 
     * by signing. I.e., where either the Grantor or the Grantee has signed, but not both. E.g., when an insurance applicant 
     * signs the insurers application, which references the policy. Usage: Optional first step of contract execution 
     * activity. May be skipped and contracting activity moves directly to executed state. Precedence Order = 3. Comparable 
     * FHIR and v.3 status codes: draft; preliminary; planned; intended; active.
     */
    public static final ContractStatus EXECUTABLE = ContractStatus.builder().value(Value.EXECUTABLE).build();

    /**
     * Executed
     * 
     * <p>Contract is activated for period stipulated when both the Grantor and Grantee have signed it. Usage: Required state 
     * for normal completion of contracting activity. Precedence Order = 6. Comparable FHIR and v.3 status codes: accepted; 
     * completed.
     */
    public static final ContractStatus EXECUTED = ContractStatus.builder().value(Value.EXECUTED).build();

    /**
     * Negotiable
     * 
     * <p>Contract execution is suspended while either or both the Grantor and Grantee propose and consider new or revised 
     * contract provisions. I.e., where the party which has not signed proposes changes to the terms. E .g., a life insurer 
     * declines to agree to the signed application because the life insurer has evidence that the applicant, who asserted to 
     * being younger or a non-smoker to get a lower premium rate - but offers instead to agree to a higher premium based on 
     * the applicants actual age or smoking status. Usage: Optional contract activity between executable and executed state. 
     * Precedence Order = 4. Comparable FHIR and v.3 status codes: in progress; review; held.
     */
    public static final ContractStatus NEGOTIABLE = ContractStatus.builder().value(Value.NEGOTIABLE).build();

    /**
     * Offered
     * 
     * <p>Contract is a proposal by either the Grantor or the Grantee. Aka - A Contract hard copy or electronic 'template', 
     * 'form' or 'application'. E.g., health insurance application; consent directive form. Usage: Beginning of contract 
     * negotiation, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 2. 
     * Comparable FHIR and v.3 status codes: requested; new.
     */
    public static final ContractStatus OFFERED = ContractStatus.builder().value(Value.OFFERED).build();

    /**
     * Policy
     * 
     * <p>Contract template is available as the basis for an application or offer by the Grantor or Grantee. E.g., health 
     * insurance policy; consent directive policy. Usage: Required initial contract activity, which may have been completed 
     * as a precondition because used for 0..* contracts. Precedence Order = 1. Comparable FHIR and v.3 status codes: 
     * proposed; intended.
     */
    public static final ContractStatus POLICY = ContractStatus.builder().value(Value.POLICY).build();

    /**
     * Rejected
     * 
     * <p> Execution of the Contract is not completed because either or both the Grantor and Grantee decline to accept some 
     * or all of the contract provisions. Usage: Optional contract activity between executable and abnormal termination. 
     * Precedence Order = 5. Comparable FHIR and v.3 status codes: stopped; cancelled.
     */
    public static final ContractStatus REJECTED = ContractStatus.builder().value(Value.REJECTED).build();

    /**
     * Renewed
     * 
     * <p>Beginning of a successor Contract at the termination of predecessor Contract lifecycle. Usage: Follows termination 
     * of a preceding Contract that has reached its expiry date. Precedence Order = 13. Comparable FHIR and v.3 status codes: 
     * superseded.
     */
    public static final ContractStatus RENEWED = ContractStatus.builder().value(Value.RENEWED).build();

    /**
     * Revoked
     * 
     * <p>A Contract that is rescinded. May be required prior to replacing with an updated Contract. Comparable FHIR and v.3 
     * status codes: nullified.
     */
    public static final ContractStatus REVOKED = ContractStatus.builder().value(Value.REVOKED).build();

    /**
     * Resolved
     * 
     * <p>Contract is reactivated after being pended because of faulty execution. *E.g., competency of the signer(s), or 
     * where the policy is substantially different from and did not accompany the application/form so that the applicant 
     * could not compare them. Aka - ''reactivated''. Usage: Optional stage where a pended contract is reactivated. 
     * Precedence Order = 8. Comparable FHIR and v.3 status codes: reactivated.
     */
    public static final ContractStatus RESOLVED = ContractStatus.builder().value(Value.RESOLVED).build();

    /**
     * Terminated
     * 
     * <p>Contract reaches its expiry date. It might or might not be renewed or renegotiated. Usage: Normal end of contract 
     * period. Precedence Order = 12. Comparable FHIR and v.3 status codes: Obsoleted.
     */
    public static final ContractStatus TERMINATED = ContractStatus.builder().value(Value.TERMINATED).build();

    private volatile int hashCode;

    private ContractStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ContractStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ContractStatus objects from a passed enum value.
     */
    public static ContractStatus of(Value value) {
        switch (value) {
        case AMENDED:
            return AMENDED;
        case APPENDED:
            return APPENDED;
        case CANCELLED:
            return CANCELLED;
        case DISPUTED:
            return DISPUTED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case EXECUTABLE:
            return EXECUTABLE;
        case EXECUTED:
            return EXECUTED;
        case NEGOTIABLE:
            return NEGOTIABLE;
        case OFFERED:
            return OFFERED;
        case POLICY:
            return POLICY;
        case REJECTED:
            return REJECTED;
        case RENEWED:
            return RENEWED;
        case REVOKED:
            return REVOKED;
        case RESOLVED:
            return RESOLVED;
        case TERMINATED:
            return TERMINATED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ContractStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ContractStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ContractStatus objects from a passed string value.
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
     * Inherited factory method for creating ContractStatus objects from a passed string value.
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
         *     An enum constant for ContractStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ContractStatus build() {
            ContractStatus contractStatus = new ContractStatus(this);
            if (validating) {
                validate(contractStatus);
            }
            return contractStatus;
        }

        protected void validate(ContractStatus contractStatus) {
            super.validate(contractStatus);
        }

        protected Builder from(ContractStatus contractStatus) {
            super.from(contractStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Amended
         * 
         * <p>Contract is augmented with additional information to correct errors in a predecessor or to updated values in a 
         * predecessor. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status 
         * codes: revised; replaced.
         */
        AMENDED("amended"),

        /**
         * Appended
         * 
         * <p>Contract is augmented with additional information that was missing from a predecessor Contract. Usage: Contract 
         * altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: updated, replaced.
         */
        APPENDED("appended"),

        /**
         * Cancelled
         * 
         * <p>Contract is terminated due to failure of the Grantor and/or the Grantee to fulfil one or more contract provisions. 
         * Usage: Abnormal contract termination. Precedence Order = 10. Comparable FHIR and v.3 status codes: stopped; failed; 
         * aborted.
         */
        CANCELLED("cancelled"),

        /**
         * Disputed
         * 
         * <p>Contract is pended to rectify failure of the Grantor or the Grantee to fulfil contract provision(s). E.g., Grantee 
         * complaint about Grantor's failure to comply with contract provisions. Usage: Contract pended. Precedence Order = 7. 
         * Comparable FHIR and v.3 status codes: on hold; pended; suspended.
         */
        DISPUTED("disputed"),

        /**
         * Entered in Error
         * 
         * <p>Contract was created in error. No Precedence Order. Status may be applied to a Contract with any status.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Executable
         * 
         * <p>Contract execution pending; may be executed when either the Grantor or the Grantee accepts the contract provisions 
         * by signing. I.e., where either the Grantor or the Grantee has signed, but not both. E.g., when an insurance applicant 
         * signs the insurers application, which references the policy. Usage: Optional first step of contract execution 
         * activity. May be skipped and contracting activity moves directly to executed state. Precedence Order = 3. Comparable 
         * FHIR and v.3 status codes: draft; preliminary; planned; intended; active.
         */
        EXECUTABLE("executable"),

        /**
         * Executed
         * 
         * <p>Contract is activated for period stipulated when both the Grantor and Grantee have signed it. Usage: Required state 
         * for normal completion of contracting activity. Precedence Order = 6. Comparable FHIR and v.3 status codes: accepted; 
         * completed.
         */
        EXECUTED("executed"),

        /**
         * Negotiable
         * 
         * <p>Contract execution is suspended while either or both the Grantor and Grantee propose and consider new or revised 
         * contract provisions. I.e., where the party which has not signed proposes changes to the terms. E .g., a life insurer 
         * declines to agree to the signed application because the life insurer has evidence that the applicant, who asserted to 
         * being younger or a non-smoker to get a lower premium rate - but offers instead to agree to a higher premium based on 
         * the applicants actual age or smoking status. Usage: Optional contract activity between executable and executed state. 
         * Precedence Order = 4. Comparable FHIR and v.3 status codes: in progress; review; held.
         */
        NEGOTIABLE("negotiable"),

        /**
         * Offered
         * 
         * <p>Contract is a proposal by either the Grantor or the Grantee. Aka - A Contract hard copy or electronic 'template', 
         * 'form' or 'application'. E.g., health insurance application; consent directive form. Usage: Beginning of contract 
         * negotiation, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 2. 
         * Comparable FHIR and v.3 status codes: requested; new.
         */
        OFFERED("offered"),

        /**
         * Policy
         * 
         * <p>Contract template is available as the basis for an application or offer by the Grantor or Grantee. E.g., health 
         * insurance policy; consent directive policy. Usage: Required initial contract activity, which may have been completed 
         * as a precondition because used for 0..* contracts. Precedence Order = 1. Comparable FHIR and v.3 status codes: 
         * proposed; intended.
         */
        POLICY("policy"),

        /**
         * Rejected
         * 
         * <p> Execution of the Contract is not completed because either or both the Grantor and Grantee decline to accept some 
         * or all of the contract provisions. Usage: Optional contract activity between executable and abnormal termination. 
         * Precedence Order = 5. Comparable FHIR and v.3 status codes: stopped; cancelled.
         */
        REJECTED("rejected"),

        /**
         * Renewed
         * 
         * <p>Beginning of a successor Contract at the termination of predecessor Contract lifecycle. Usage: Follows termination 
         * of a preceding Contract that has reached its expiry date. Precedence Order = 13. Comparable FHIR and v.3 status codes: 
         * superseded.
         */
        RENEWED("renewed"),

        /**
         * Revoked
         * 
         * <p>A Contract that is rescinded. May be required prior to replacing with an updated Contract. Comparable FHIR and v.3 
         * status codes: nullified.
         */
        REVOKED("revoked"),

        /**
         * Resolved
         * 
         * <p>Contract is reactivated after being pended because of faulty execution. *E.g., competency of the signer(s), or 
         * where the policy is substantially different from and did not accompany the application/form so that the applicant 
         * could not compare them. Aka - ''reactivated''. Usage: Optional stage where a pended contract is reactivated. 
         * Precedence Order = 8. Comparable FHIR and v.3 status codes: reactivated.
         */
        RESOLVED("resolved"),

        /**
         * Terminated
         * 
         * <p>Contract reaches its expiry date. It might or might not be renewed or renegotiated. Usage: Normal end of contract 
         * period. Precedence Order = 12. Comparable FHIR and v.3 status codes: Obsoleted.
         */
        TERMINATED("terminated");

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
         * Factory method for creating ContractStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ContractStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "amended":
                return AMENDED;
            case "appended":
                return APPENDED;
            case "cancelled":
                return CANCELLED;
            case "disputed":
                return DISPUTED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "executable":
                return EXECUTABLE;
            case "executed":
                return EXECUTED;
            case "negotiable":
                return NEGOTIABLE;
            case "offered":
                return OFFERED;
            case "policy":
                return POLICY;
            case "rejected":
                return REJECTED;
            case "renewed":
                return RENEWED;
            case "revoked":
                return REVOKED;
            case "resolved":
                return RESOLVED;
            case "terminated":
                return TERMINATED;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
