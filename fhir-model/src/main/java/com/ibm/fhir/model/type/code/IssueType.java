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

@System("http://hl7.org/fhir/issue-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class IssueType extends Code {
    /**
     * Invalid Content
     * 
     * <p>Content invalid against the specification or a profile.
     */
    public static final IssueType INVALID = IssueType.builder().value(Value.INVALID).build();

    /**
     * Structural Issue
     * 
     * <p>A structural issue in the content such as wrong namespace, unable to parse the content completely, invalid syntax, 
     * etc.
     */
    public static final IssueType STRUCTURE = IssueType.builder().value(Value.STRUCTURE).build();

    /**
     * Required element missing
     * 
     * <p>A required element is missing.
     */
    public static final IssueType REQUIRED = IssueType.builder().value(Value.REQUIRED).build();

    /**
     * Element value invalid
     * 
     * <p>An element or header value is invalid.
     */
    public static final IssueType VALUE = IssueType.builder().value(Value.VALUE).build();

    /**
     * Validation rule failed
     * 
     * <p>A content validation rule failed - e.g. a schematron rule.
     */
    public static final IssueType INVARIANT = IssueType.builder().value(Value.INVARIANT).build();

    /**
     * Security Problem
     * 
     * <p>An authentication/authorization/permissions issue of some kind.
     */
    public static final IssueType SECURITY = IssueType.builder().value(Value.SECURITY).build();

    /**
     * Login Required
     * 
     * <p>The client needs to initiate an authentication process.
     */
    public static final IssueType LOGIN = IssueType.builder().value(Value.LOGIN).build();

    /**
     * Unknown User
     * 
     * <p>The user or system was not able to be authenticated (either there is no process, or the proferred token is 
     * unacceptable).
     */
    public static final IssueType UNKNOWN = IssueType.builder().value(Value.UNKNOWN).build();

    /**
     * Session Expired
     * 
     * <p>User session expired; a login may be required.
     */
    public static final IssueType EXPIRED = IssueType.builder().value(Value.EXPIRED).build();

    /**
     * Forbidden
     * 
     * <p>The user does not have the rights to perform this action.
     */
    public static final IssueType FORBIDDEN = IssueType.builder().value(Value.FORBIDDEN).build();

    /**
     * Information Suppressed
     * 
     * <p>Some information was not or might not have been returned due to business rules, consent or privacy rules, or access 
     * permission constraints. This information may be accessible through alternate processes.
     */
    public static final IssueType SUPPRESSED = IssueType.builder().value(Value.SUPPRESSED).build();

    /**
     * Processing Failure
     * 
     * <p>Processing issues. These are expected to be final e.g. there is no point resubmitting the same content unchanged.
     */
    public static final IssueType PROCESSING = IssueType.builder().value(Value.PROCESSING).build();

    /**
     * Content not supported
     * 
     * <p>The interaction, operation, resource or profile is not supported.
     */
    public static final IssueType NOT_SUPPORTED = IssueType.builder().value(Value.NOT_SUPPORTED).build();

    /**
     * Duplicate
     * 
     * <p>An attempt was made to create a duplicate record.
     */
    public static final IssueType DUPLICATE = IssueType.builder().value(Value.DUPLICATE).build();

    /**
     * Multiple Matches
     * 
     * <p>Multiple matching records were found when the operation required only one match.
     */
    public static final IssueType MULTIPLE_MATCHES = IssueType.builder().value(Value.MULTIPLE_MATCHES).build();

    /**
     * Not Found
     * 
     * <p>The reference provided was not found. In a pure RESTful environment, this would be an HTTP 404 error, but this code 
     * may be used where the content is not found further into the application architecture.
     */
    public static final IssueType NOT_FOUND = IssueType.builder().value(Value.NOT_FOUND).build();

    /**
     * Deleted
     * 
     * <p>The reference pointed to content (usually a resource) that has been deleted.
     */
    public static final IssueType DELETED = IssueType.builder().value(Value.DELETED).build();

    /**
     * Content Too Long
     * 
     * <p>Provided content is too long (typically, this is a denial of service protection type of error).
     */
    public static final IssueType TOO_LONG = IssueType.builder().value(Value.TOO_LONG).build();

    /**
     * Invalid Code
     * 
     * <p>The code or system could not be understood, or it was not valid in the context of a particular ValueSet.code.
     */
    public static final IssueType CODE_INVALID = IssueType.builder().value(Value.CODE_INVALID).build();

    /**
     * Unacceptable Extension
     * 
     * <p>An extension was found that was not acceptable, could not be resolved, or a modifierExtension was not recognized.
     */
    public static final IssueType EXTENSION = IssueType.builder().value(Value.EXTENSION).build();

    /**
     * Operation Too Costly
     * 
     * <p>The operation was stopped to protect server resources; e.g. a request for a value set expansion on all of SNOMED CT.
     */
    public static final IssueType TOO_COSTLY = IssueType.builder().value(Value.TOO_COSTLY).build();

    /**
     * Business Rule Violation
     * 
     * <p>The content/operation failed to pass some business rule and so could not proceed.
     */
    public static final IssueType BUSINESS_RULE = IssueType.builder().value(Value.BUSINESS_RULE).build();

    /**
     * Edit Version Conflict
     * 
     * <p>Content could not be accepted because of an edit conflict (i.e. version aware updates). (In a pure RESTful 
     * environment, this would be an HTTP 409 error, but this code may be used where the conflict is discovered further into 
     * the application architecture.).
     */
    public static final IssueType CONFLICT = IssueType.builder().value(Value.CONFLICT).build();

    /**
     * Transient Issue
     * 
     * <p>Transient processing issues. The system receiving the message may be able to resubmit the same content once an 
     * underlying issue is resolved.
     */
    public static final IssueType TRANSIENT = IssueType.builder().value(Value.TRANSIENT).build();

    /**
     * Lock Error
     * 
     * <p>A resource/record locking failure (usually in an underlying database).
     */
    public static final IssueType LOCK_ERROR = IssueType.builder().value(Value.LOCK_ERROR).build();

    /**
     * No Store Available
     * 
     * <p>The persistent store is unavailable; e.g. the database is down for maintenance or similar action, and the 
     * interaction or operation cannot be processed.
     */
    public static final IssueType NO_STORE = IssueType.builder().value(Value.NO_STORE).build();

    /**
     * Exception
     * 
     * <p>An unexpected internal error has occurred.
     */
    public static final IssueType EXCEPTION = IssueType.builder().value(Value.EXCEPTION).build();

    /**
     * Timeout
     * 
     * <p>An internal timeout has occurred.
     */
    public static final IssueType TIMEOUT = IssueType.builder().value(Value.TIMEOUT).build();

    /**
     * Incomplete Results
     * 
     * <p>Not all data sources typically accessed could be reached or responded in time, so the returned information might 
     * not be complete (applies to search interactions and some operations).
     */
    public static final IssueType INCOMPLETE = IssueType.builder().value(Value.INCOMPLETE).build();

    /**
     * Throttled
     * 
     * <p>The system is not prepared to handle this request due to load management.
     */
    public static final IssueType THROTTLED = IssueType.builder().value(Value.THROTTLED).build();

    /**
     * Informational Note
     * 
     * <p>A message unrelated to the processing success of the completed operation (examples of the latter include things 
     * like reminders of password expiry, system maintenance times, etc.).
     */
    public static final IssueType INFORMATIONAL = IssueType.builder().value(Value.INFORMATIONAL).build();

    private volatile int hashCode;

    private IssueType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this IssueType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating IssueType objects from a passed enum value.
     */
    public static IssueType of(Value value) {
        switch (value) {
        case INVALID:
            return INVALID;
        case STRUCTURE:
            return STRUCTURE;
        case REQUIRED:
            return REQUIRED;
        case VALUE:
            return VALUE;
        case INVARIANT:
            return INVARIANT;
        case SECURITY:
            return SECURITY;
        case LOGIN:
            return LOGIN;
        case UNKNOWN:
            return UNKNOWN;
        case EXPIRED:
            return EXPIRED;
        case FORBIDDEN:
            return FORBIDDEN;
        case SUPPRESSED:
            return SUPPRESSED;
        case PROCESSING:
            return PROCESSING;
        case NOT_SUPPORTED:
            return NOT_SUPPORTED;
        case DUPLICATE:
            return DUPLICATE;
        case MULTIPLE_MATCHES:
            return MULTIPLE_MATCHES;
        case NOT_FOUND:
            return NOT_FOUND;
        case DELETED:
            return DELETED;
        case TOO_LONG:
            return TOO_LONG;
        case CODE_INVALID:
            return CODE_INVALID;
        case EXTENSION:
            return EXTENSION;
        case TOO_COSTLY:
            return TOO_COSTLY;
        case BUSINESS_RULE:
            return BUSINESS_RULE;
        case CONFLICT:
            return CONFLICT;
        case TRANSIENT:
            return TRANSIENT;
        case LOCK_ERROR:
            return LOCK_ERROR;
        case NO_STORE:
            return NO_STORE;
        case EXCEPTION:
            return EXCEPTION;
        case TIMEOUT:
            return TIMEOUT;
        case INCOMPLETE:
            return INCOMPLETE;
        case THROTTLED:
            return THROTTLED;
        case INFORMATIONAL:
            return INFORMATIONAL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating IssueType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static IssueType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating IssueType objects from a passed string value.
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
     * Inherited factory method for creating IssueType objects from a passed string value.
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
        IssueType other = (IssueType) obj;
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
         *     An enum constant for IssueType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public IssueType build() {
            IssueType issueType = new IssueType(this);
            if (validating) {
                validate(issueType);
            }
            return issueType;
        }

        protected void validate(IssueType issueType) {
            super.validate(issueType);
        }

        protected Builder from(IssueType issueType) {
            super.from(issueType);
            return this;
        }
    }

    public enum Value {
        /**
         * Invalid Content
         * 
         * <p>Content invalid against the specification or a profile.
         */
        INVALID("invalid"),

        /**
         * Structural Issue
         * 
         * <p>A structural issue in the content such as wrong namespace, unable to parse the content completely, invalid syntax, 
         * etc.
         */
        STRUCTURE("structure"),

        /**
         * Required element missing
         * 
         * <p>A required element is missing.
         */
        REQUIRED("required"),

        /**
         * Element value invalid
         * 
         * <p>An element or header value is invalid.
         */
        VALUE("value"),

        /**
         * Validation rule failed
         * 
         * <p>A content validation rule failed - e.g. a schematron rule.
         */
        INVARIANT("invariant"),

        /**
         * Security Problem
         * 
         * <p>An authentication/authorization/permissions issue of some kind.
         */
        SECURITY("security"),

        /**
         * Login Required
         * 
         * <p>The client needs to initiate an authentication process.
         */
        LOGIN("login"),

        /**
         * Unknown User
         * 
         * <p>The user or system was not able to be authenticated (either there is no process, or the proferred token is 
         * unacceptable).
         */
        UNKNOWN("unknown"),

        /**
         * Session Expired
         * 
         * <p>User session expired; a login may be required.
         */
        EXPIRED("expired"),

        /**
         * Forbidden
         * 
         * <p>The user does not have the rights to perform this action.
         */
        FORBIDDEN("forbidden"),

        /**
         * Information Suppressed
         * 
         * <p>Some information was not or might not have been returned due to business rules, consent or privacy rules, or access 
         * permission constraints. This information may be accessible through alternate processes.
         */
        SUPPRESSED("suppressed"),

        /**
         * Processing Failure
         * 
         * <p>Processing issues. These are expected to be final e.g. there is no point resubmitting the same content unchanged.
         */
        PROCESSING("processing"),

        /**
         * Content not supported
         * 
         * <p>The interaction, operation, resource or profile is not supported.
         */
        NOT_SUPPORTED("not-supported"),

        /**
         * Duplicate
         * 
         * <p>An attempt was made to create a duplicate record.
         */
        DUPLICATE("duplicate"),

        /**
         * Multiple Matches
         * 
         * <p>Multiple matching records were found when the operation required only one match.
         */
        MULTIPLE_MATCHES("multiple-matches"),

        /**
         * Not Found
         * 
         * <p>The reference provided was not found. In a pure RESTful environment, this would be an HTTP 404 error, but this code 
         * may be used where the content is not found further into the application architecture.
         */
        NOT_FOUND("not-found"),

        /**
         * Deleted
         * 
         * <p>The reference pointed to content (usually a resource) that has been deleted.
         */
        DELETED("deleted"),

        /**
         * Content Too Long
         * 
         * <p>Provided content is too long (typically, this is a denial of service protection type of error).
         */
        TOO_LONG("too-long"),

        /**
         * Invalid Code
         * 
         * <p>The code or system could not be understood, or it was not valid in the context of a particular ValueSet.code.
         */
        CODE_INVALID("code-invalid"),

        /**
         * Unacceptable Extension
         * 
         * <p>An extension was found that was not acceptable, could not be resolved, or a modifierExtension was not recognized.
         */
        EXTENSION("extension"),

        /**
         * Operation Too Costly
         * 
         * <p>The operation was stopped to protect server resources; e.g. a request for a value set expansion on all of SNOMED CT.
         */
        TOO_COSTLY("too-costly"),

        /**
         * Business Rule Violation
         * 
         * <p>The content/operation failed to pass some business rule and so could not proceed.
         */
        BUSINESS_RULE("business-rule"),

        /**
         * Edit Version Conflict
         * 
         * <p>Content could not be accepted because of an edit conflict (i.e. version aware updates). (In a pure RESTful 
         * environment, this would be an HTTP 409 error, but this code may be used where the conflict is discovered further into 
         * the application architecture.).
         */
        CONFLICT("conflict"),

        /**
         * Transient Issue
         * 
         * <p>Transient processing issues. The system receiving the message may be able to resubmit the same content once an 
         * underlying issue is resolved.
         */
        TRANSIENT("transient"),

        /**
         * Lock Error
         * 
         * <p>A resource/record locking failure (usually in an underlying database).
         */
        LOCK_ERROR("lock-error"),

        /**
         * No Store Available
         * 
         * <p>The persistent store is unavailable; e.g. the database is down for maintenance or similar action, and the 
         * interaction or operation cannot be processed.
         */
        NO_STORE("no-store"),

        /**
         * Exception
         * 
         * <p>An unexpected internal error has occurred.
         */
        EXCEPTION("exception"),

        /**
         * Timeout
         * 
         * <p>An internal timeout has occurred.
         */
        TIMEOUT("timeout"),

        /**
         * Incomplete Results
         * 
         * <p>Not all data sources typically accessed could be reached or responded in time, so the returned information might 
         * not be complete (applies to search interactions and some operations).
         */
        INCOMPLETE("incomplete"),

        /**
         * Throttled
         * 
         * <p>The system is not prepared to handle this request due to load management.
         */
        THROTTLED("throttled"),

        /**
         * Informational Note
         * 
         * <p>A message unrelated to the processing success of the completed operation (examples of the latter include things 
         * like reminders of password expiry, system maintenance times, etc.).
         */
        INFORMATIONAL("informational");

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
         * Factory method for creating IssueType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding IssueType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "invalid":
                return INVALID;
            case "structure":
                return STRUCTURE;
            case "required":
                return REQUIRED;
            case "value":
                return VALUE;
            case "invariant":
                return INVARIANT;
            case "security":
                return SECURITY;
            case "login":
                return LOGIN;
            case "unknown":
                return UNKNOWN;
            case "expired":
                return EXPIRED;
            case "forbidden":
                return FORBIDDEN;
            case "suppressed":
                return SUPPRESSED;
            case "processing":
                return PROCESSING;
            case "not-supported":
                return NOT_SUPPORTED;
            case "duplicate":
                return DUPLICATE;
            case "multiple-matches":
                return MULTIPLE_MATCHES;
            case "not-found":
                return NOT_FOUND;
            case "deleted":
                return DELETED;
            case "too-long":
                return TOO_LONG;
            case "code-invalid":
                return CODE_INVALID;
            case "extension":
                return EXTENSION;
            case "too-costly":
                return TOO_COSTLY;
            case "business-rule":
                return BUSINESS_RULE;
            case "conflict":
                return CONFLICT;
            case "transient":
                return TRANSIENT;
            case "lock-error":
                return LOCK_ERROR;
            case "no-store":
                return NO_STORE;
            case "exception":
                return EXCEPTION;
            case "timeout":
                return TIMEOUT;
            case "incomplete":
                return INCOMPLETE;
            case "throttled":
                return THROTTLED;
            case "informational":
                return INFORMATIONAL;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
