/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/contributor-type")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ContributorType extends Code {
    /**
     * Author
     * 
     * <p>An author of the content of the module.
     */
    public static final ContributorType AUTHOR = ContributorType.builder().value(Value.AUTHOR).build();

    /**
     * Editor
     * 
     * <p>An editor of the content of the module.
     */
    public static final ContributorType EDITOR = ContributorType.builder().value(Value.EDITOR).build();

    /**
     * Reviewer
     * 
     * <p>A reviewer of the content of the module.
     */
    public static final ContributorType REVIEWER = ContributorType.builder().value(Value.REVIEWER).build();

    /**
     * Endorser
     * 
     * <p>An endorser of the content of the module.
     */
    public static final ContributorType ENDORSER = ContributorType.builder().value(Value.ENDORSER).build();

    private volatile int hashCode;

    private ContributorType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ContributorType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ContributorType objects from a passed enum value.
     */
    public static ContributorType of(Value value) {
        switch (value) {
        case AUTHOR:
            return AUTHOR;
        case EDITOR:
            return EDITOR;
        case REVIEWER:
            return REVIEWER;
        case ENDORSER:
            return ENDORSER;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ContributorType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ContributorType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ContributorType objects from a passed string value.
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
     * Inherited factory method for creating ContributorType objects from a passed string value.
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
        ContributorType other = (ContributorType) obj;
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
         *     An enum constant for ContributorType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ContributorType build() {
            ContributorType contributorType = new ContributorType(this);
            if (validating) {
                validate(contributorType);
            }
            return contributorType;
        }

        protected void validate(ContributorType contributorType) {
            super.validate(contributorType);
        }

        protected Builder from(ContributorType contributorType) {
            super.from(contributorType);
            return this;
        }
    }

    public enum Value {
        /**
         * Author
         * 
         * <p>An author of the content of the module.
         */
        AUTHOR("author"),

        /**
         * Editor
         * 
         * <p>An editor of the content of the module.
         */
        EDITOR("editor"),

        /**
         * Reviewer
         * 
         * <p>A reviewer of the content of the module.
         */
        REVIEWER("reviewer"),

        /**
         * Endorser
         * 
         * <p>An endorser of the content of the module.
         */
        ENDORSER("endorser");

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
         * Factory method for creating ContributorType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ContributorType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "author":
                return AUTHOR;
            case "editor":
                return EDITOR;
            case "reviewer":
                return REVIEWER;
            case "endorser":
                return ENDORSER;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
