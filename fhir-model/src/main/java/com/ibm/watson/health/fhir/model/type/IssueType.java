/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class IssueType extends Code {
    /**
     * Invalid Content
     */
    public static final IssueType INVALID = IssueType.of(ValueSet.INVALID);

    /**
     * Structural Issue
     */
    public static final IssueType STRUCTURE = IssueType.of(ValueSet.STRUCTURE);

    /**
     * Required element missing
     */
    public static final IssueType REQUIRED = IssueType.of(ValueSet.REQUIRED);

    /**
     * Element value invalid
     */
    public static final IssueType VALUE = IssueType.of(ValueSet.VALUE);

    /**
     * Validation rule failed
     */
    public static final IssueType INVARIANT = IssueType.of(ValueSet.INVARIANT);

    /**
     * Security Problem
     */
    public static final IssueType SECURITY = IssueType.of(ValueSet.SECURITY);

    /**
     * Login Required
     */
    public static final IssueType LOGIN = IssueType.of(ValueSet.LOGIN);

    /**
     * Unknown User
     */
    public static final IssueType UNKNOWN = IssueType.of(ValueSet.UNKNOWN);

    /**
     * Session Expired
     */
    public static final IssueType EXPIRED = IssueType.of(ValueSet.EXPIRED);

    /**
     * Forbidden
     */
    public static final IssueType FORBIDDEN = IssueType.of(ValueSet.FORBIDDEN);

    /**
     * Information  Suppressed
     */
    public static final IssueType SUPPRESSED = IssueType.of(ValueSet.SUPPRESSED);

    /**
     * Processing Failure
     */
    public static final IssueType PROCESSING = IssueType.of(ValueSet.PROCESSING);

    /**
     * Content not supported
     */
    public static final IssueType NOT_SUPPORTED = IssueType.of(ValueSet.NOT_SUPPORTED);

    /**
     * Duplicate
     */
    public static final IssueType DUPLICATE = IssueType.of(ValueSet.DUPLICATE);

    /**
     * Multiple Matches
     */
    public static final IssueType MULTIPLE_MATCHES = IssueType.of(ValueSet.MULTIPLE_MATCHES);

    /**
     * Not Found
     */
    public static final IssueType NOT_FOUND = IssueType.of(ValueSet.NOT_FOUND);

    /**
     * Deleted
     */
    public static final IssueType DELETED = IssueType.of(ValueSet.DELETED);

    /**
     * Content Too Long
     */
    public static final IssueType TOO_LONG = IssueType.of(ValueSet.TOO_LONG);

    /**
     * Invalid Code
     */
    public static final IssueType CODE_INVALID = IssueType.of(ValueSet.CODE_INVALID);

    /**
     * Unacceptable Extension
     */
    public static final IssueType EXTENSION = IssueType.of(ValueSet.EXTENSION);

    /**
     * Operation Too Costly
     */
    public static final IssueType TOO_COSTLY = IssueType.of(ValueSet.TOO_COSTLY);

    /**
     * Business Rule Violation
     */
    public static final IssueType BUSINESS_RULE = IssueType.of(ValueSet.BUSINESS_RULE);

    /**
     * Edit Version Conflict
     */
    public static final IssueType CONFLICT = IssueType.of(ValueSet.CONFLICT);

    /**
     * Transient Issue
     */
    public static final IssueType TRANSIENT = IssueType.of(ValueSet.TRANSIENT);

    /**
     * Lock Error
     */
    public static final IssueType LOCK_ERROR = IssueType.of(ValueSet.LOCK_ERROR);

    /**
     * No Store Available
     */
    public static final IssueType NO_STORE = IssueType.of(ValueSet.NO_STORE);

    /**
     * Exception
     */
    public static final IssueType EXCEPTION = IssueType.of(ValueSet.EXCEPTION);

    /**
     * Timeout
     */
    public static final IssueType TIMEOUT = IssueType.of(ValueSet.TIMEOUT);

    /**
     * Incomplete Results
     */
    public static final IssueType INCOMPLETE = IssueType.of(ValueSet.INCOMPLETE);

    /**
     * Throttled
     */
    public static final IssueType THROTTLED = IssueType.of(ValueSet.THROTTLED);

    /**
     * Informational Note
     */
    public static final IssueType INFORMATIONAL = IssueType.of(ValueSet.INFORMATIONAL);

    private volatile int hashCode;

    private IssueType(Builder builder) {
        super(builder);
    }

    public static IssueType of(java.lang.String value) {
        return IssueType.builder().value(value).build();
    }

    public static IssueType of(ValueSet value) {
        return IssueType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return IssueType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return IssueType.builder().value(value).build();
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
