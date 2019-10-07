/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AssertionResponseTypes extends Code {
    /**
     * okay
     */
    public static final AssertionResponseTypes OKAY = AssertionResponseTypes.of(ValueSet.OKAY);

    /**
     * created
     */
    public static final AssertionResponseTypes CREATED = AssertionResponseTypes.of(ValueSet.CREATED);

    /**
     * noContent
     */
    public static final AssertionResponseTypes NO_CONTENT = AssertionResponseTypes.of(ValueSet.NO_CONTENT);

    /**
     * notModified
     */
    public static final AssertionResponseTypes NOT_MODIFIED = AssertionResponseTypes.of(ValueSet.NOT_MODIFIED);

    /**
     * bad
     */
    public static final AssertionResponseTypes BAD = AssertionResponseTypes.of(ValueSet.BAD);

    /**
     * forbidden
     */
    public static final AssertionResponseTypes FORBIDDEN = AssertionResponseTypes.of(ValueSet.FORBIDDEN);

    /**
     * notFound
     */
    public static final AssertionResponseTypes NOT_FOUND = AssertionResponseTypes.of(ValueSet.NOT_FOUND);

    /**
     * methodNotAllowed
     */
    public static final AssertionResponseTypes METHOD_NOT_ALLOWED = AssertionResponseTypes.of(ValueSet.METHOD_NOT_ALLOWED);

    /**
     * conflict
     */
    public static final AssertionResponseTypes CONFLICT = AssertionResponseTypes.of(ValueSet.CONFLICT);

    /**
     * gone
     */
    public static final AssertionResponseTypes GONE = AssertionResponseTypes.of(ValueSet.GONE);

    /**
     * preconditionFailed
     */
    public static final AssertionResponseTypes PRECONDITION_FAILED = AssertionResponseTypes.of(ValueSet.PRECONDITION_FAILED);

    /**
     * unprocessable
     */
    public static final AssertionResponseTypes UNPROCESSABLE = AssertionResponseTypes.of(ValueSet.UNPROCESSABLE);

    private volatile int hashCode;

    private AssertionResponseTypes(Builder builder) {
        super(builder);
    }

    public static AssertionResponseTypes of(java.lang.String value) {
        return AssertionResponseTypes.builder().value(value).build();
    }

    public static AssertionResponseTypes of(ValueSet value) {
        return AssertionResponseTypes.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return AssertionResponseTypes.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return AssertionResponseTypes.builder().value(value).build();
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
        AssertionResponseTypes other = (AssertionResponseTypes) obj;
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
        public AssertionResponseTypes build() {
            return new AssertionResponseTypes(this);
        }
    }

    public enum ValueSet {
        /**
         * okay
         */
        OKAY("okay"),

        /**
         * created
         */
        CREATED("created"),

        /**
         * noContent
         */
        NO_CONTENT("noContent"),

        /**
         * notModified
         */
        NOT_MODIFIED("notModified"),

        /**
         * bad
         */
        BAD("bad"),

        /**
         * forbidden
         */
        FORBIDDEN("forbidden"),

        /**
         * notFound
         */
        NOT_FOUND("notFound"),

        /**
         * methodNotAllowed
         */
        METHOD_NOT_ALLOWED("methodNotAllowed"),

        /**
         * conflict
         */
        CONFLICT("conflict"),

        /**
         * gone
         */
        GONE("gone"),

        /**
         * preconditionFailed
         */
        PRECONDITION_FAILED("preconditionFailed"),

        /**
         * unprocessable
         */
        UNPROCESSABLE("unprocessable");

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
