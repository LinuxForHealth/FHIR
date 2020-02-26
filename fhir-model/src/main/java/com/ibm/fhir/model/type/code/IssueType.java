/*
 * (C) Copyright IBM Corp. 2019, 2020
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
     */
    public static final IssueType INVALID = IssueType.builder().value(ValueSet.INVALID).build();

    /**
     * Structural Issue
     */
    public static final IssueType STRUCTURE = IssueType.builder().value(ValueSet.STRUCTURE).build();

    /**
     * Required element missing
     */
    public static final IssueType REQUIRED = IssueType.builder().value(ValueSet.REQUIRED).build();

    /**
     * Element value invalid
     */
    public static final IssueType VALUE = IssueType.builder().value(ValueSet.VALUE).build();

    /**
     * Validation rule failed
     */
    public static final IssueType INVARIANT = IssueType.builder().value(ValueSet.INVARIANT).build();

    /**
     * Security Problem
     */
    public static final IssueType SECURITY = IssueType.builder().value(ValueSet.SECURITY).build();

    /**
     * Login Required
     */
    public static final IssueType LOGIN = IssueType.builder().value(ValueSet.LOGIN).build();

    /**
     * Unknown User
     */
    public static final IssueType UNKNOWN = IssueType.builder().value(ValueSet.UNKNOWN).build();

    /**
     * Session Expired
     */
    public static final IssueType EXPIRED = IssueType.builder().value(ValueSet.EXPIRED).build();

    /**
     * Forbidden
     */
    public static final IssueType FORBIDDEN = IssueType.builder().value(ValueSet.FORBIDDEN).build();

    /**
     * Information  Suppressed
     */
    public static final IssueType SUPPRESSED = IssueType.builder().value(ValueSet.SUPPRESSED).build();

    /**
     * Processing Failure
     */
    public static final IssueType PROCESSING = IssueType.builder().value(ValueSet.PROCESSING).build();

    /**
     * Content not supported
     */
    public static final IssueType NOT_SUPPORTED = IssueType.builder().value(ValueSet.NOT_SUPPORTED).build();

    /**
     * Duplicate
     */
    public static final IssueType DUPLICATE = IssueType.builder().value(ValueSet.DUPLICATE).build();

    /**
     * Multiple Matches
     */
    public static final IssueType MULTIPLE_MATCHES = IssueType.builder().value(ValueSet.MULTIPLE_MATCHES).build();

    /**
     * Not Found
     */
    public static final IssueType NOT_FOUND = IssueType.builder().value(ValueSet.NOT_FOUND).build();

    /**
     * Deleted
     */
    public static final IssueType DELETED = IssueType.builder().value(ValueSet.DELETED).build();

    /**
     * Content Too Long
     */
    public static final IssueType TOO_LONG = IssueType.builder().value(ValueSet.TOO_LONG).build();

    /**
     * Invalid Code
     */
    public static final IssueType CODE_INVALID = IssueType.builder().value(ValueSet.CODE_INVALID).build();

    /**
     * Unacceptable Extension
     */
    public static final IssueType EXTENSION = IssueType.builder().value(ValueSet.EXTENSION).build();

    /**
     * Operation Too Costly
     */
    public static final IssueType TOO_COSTLY = IssueType.builder().value(ValueSet.TOO_COSTLY).build();

    /**
     * Business Rule Violation
     */
    public static final IssueType BUSINESS_RULE = IssueType.builder().value(ValueSet.BUSINESS_RULE).build();

    /**
     * Edit Version Conflict
     */
    public static final IssueType CONFLICT = IssueType.builder().value(ValueSet.CONFLICT).build();

    /**
     * Transient Issue
     */
    public static final IssueType TRANSIENT = IssueType.builder().value(ValueSet.TRANSIENT).build();

    /**
     * Lock Error
     */
    public static final IssueType LOCK_ERROR = IssueType.builder().value(ValueSet.LOCK_ERROR).build();

    /**
     * No Store Available
     */
    public static final IssueType NO_STORE = IssueType.builder().value(ValueSet.NO_STORE).build();

    /**
     * Exception
     */
    public static final IssueType EXCEPTION = IssueType.builder().value(ValueSet.EXCEPTION).build();

    /**
     * Timeout
     */
    public static final IssueType TIMEOUT = IssueType.builder().value(ValueSet.TIMEOUT).build();

    /**
     * Incomplete Results
     */
    public static final IssueType INCOMPLETE = IssueType.builder().value(ValueSet.INCOMPLETE).build();

    /**
     * Throttled
     */
    public static final IssueType THROTTLED = IssueType.builder().value(ValueSet.THROTTLED).build();

    /**
     * Informational Note
     */
    public static final IssueType INFORMATIONAL = IssueType.builder().value(ValueSet.INFORMATIONAL).build();

    private volatile int hashCode;

    private IssueType(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    public static IssueType of(ValueSet value) {
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

    public static IssueType of(java.lang.String value) {
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
        public IssueType build() {
            return new IssueType(this);
        }
    }

    public enum ValueSet {
        /**
         * Invalid Content
         */
        INVALID("invalid"),

        /**
         * Structural Issue
         */
        STRUCTURE("structure"),

        /**
         * Required element missing
         */
        REQUIRED("required"),

        /**
         * Element value invalid
         */
        VALUE("value"),

        /**
         * Validation rule failed
         */
        INVARIANT("invariant"),

        /**
         * Security Problem
         */
        SECURITY("security"),

        /**
         * Login Required
         */
        LOGIN("login"),

        /**
         * Unknown User
         */
        UNKNOWN("unknown"),

        /**
         * Session Expired
         */
        EXPIRED("expired"),

        /**
         * Forbidden
         */
        FORBIDDEN("forbidden"),

        /**
         * Information  Suppressed
         */
        SUPPRESSED("suppressed"),

        /**
         * Processing Failure
         */
        PROCESSING("processing"),

        /**
         * Content not supported
         */
        NOT_SUPPORTED("not-supported"),

        /**
         * Duplicate
         */
        DUPLICATE("duplicate"),

        /**
         * Multiple Matches
         */
        MULTIPLE_MATCHES("multiple-matches"),

        /**
         * Not Found
         */
        NOT_FOUND("not-found"),

        /**
         * Deleted
         */
        DELETED("deleted"),

        /**
         * Content Too Long
         */
        TOO_LONG("too-long"),

        /**
         * Invalid Code
         */
        CODE_INVALID("code-invalid"),

        /**
         * Unacceptable Extension
         */
        EXTENSION("extension"),

        /**
         * Operation Too Costly
         */
        TOO_COSTLY("too-costly"),

        /**
         * Business Rule Violation
         */
        BUSINESS_RULE("business-rule"),

        /**
         * Edit Version Conflict
         */
        CONFLICT("conflict"),

        /**
         * Transient Issue
         */
        TRANSIENT("transient"),

        /**
         * Lock Error
         */
        LOCK_ERROR("lock-error"),

        /**
         * No Store Available
         */
        NO_STORE("no-store"),

        /**
         * Exception
         */
        EXCEPTION("exception"),

        /**
         * Timeout
         */
        TIMEOUT("timeout"),

        /**
         * Incomplete Results
         */
        INCOMPLETE("incomplete"),

        /**
         * Throttled
         */
        THROTTLED("throttled"),

        /**
         * Informational Note
         */
        INFORMATIONAL("informational");

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
