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

@System("http://hl7.org/fhir/codesystem-content-mode")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class CodeSystemContentMode extends Code {
    /**
     * Not Present
     * 
     * <p>None of the concepts defined by the code system are included in the code system resource.
     */
    public static final CodeSystemContentMode NOT_PRESENT = CodeSystemContentMode.builder().value(Value.NOT_PRESENT).build();

    /**
     * Example
     * 
     * <p>A few representative concepts are included in the code system resource. There is no useful intent in the subset 
     * choice and there's no process to make it workable: it's not intended to be workable.
     */
    public static final CodeSystemContentMode EXAMPLE = CodeSystemContentMode.builder().value(Value.EXAMPLE).build();

    /**
     * Fragment
     * 
     * <p>A subset of the code system concepts are included in the code system resource. This is a curated subset released 
     * for a specific purpose under the governance of the code system steward, and that the intent, bounds and consequences 
     * of the fragmentation are clearly defined in the fragment or the code system documentation. Fragments are also known as 
     * partitions.
     */
    public static final CodeSystemContentMode FRAGMENT = CodeSystemContentMode.builder().value(Value.FRAGMENT).build();

    /**
     * Complete
     * 
     * <p>All the concepts defined by the code system are included in the code system resource.
     */
    public static final CodeSystemContentMode COMPLETE = CodeSystemContentMode.builder().value(Value.COMPLETE).build();

    /**
     * Supplement
     * 
     * <p>The resource doesn't define any new concepts; it just provides additional designations and properties to another 
     * code system.
     */
    public static final CodeSystemContentMode SUPPLEMENT = CodeSystemContentMode.builder().value(Value.SUPPLEMENT).build();

    private volatile int hashCode;

    private CodeSystemContentMode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this CodeSystemContentMode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating CodeSystemContentMode objects from a passed enum value.
     */
    public static CodeSystemContentMode of(Value value) {
        switch (value) {
        case NOT_PRESENT:
            return NOT_PRESENT;
        case EXAMPLE:
            return EXAMPLE;
        case FRAGMENT:
            return FRAGMENT;
        case COMPLETE:
            return COMPLETE;
        case SUPPLEMENT:
            return SUPPLEMENT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating CodeSystemContentMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static CodeSystemContentMode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating CodeSystemContentMode objects from a passed string value.
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
     * Inherited factory method for creating CodeSystemContentMode objects from a passed string value.
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
        CodeSystemContentMode other = (CodeSystemContentMode) obj;
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
         *     An enum constant for CodeSystemContentMode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public CodeSystemContentMode build() {
            CodeSystemContentMode codeSystemContentMode = new CodeSystemContentMode(this);
            if (validating) {
                validate(codeSystemContentMode);
            }
            return codeSystemContentMode;
        }

        protected void validate(CodeSystemContentMode codeSystemContentMode) {
            super.validate(codeSystemContentMode);
        }

        protected Builder from(CodeSystemContentMode codeSystemContentMode) {
            super.from(codeSystemContentMode);
            return this;
        }
    }

    public enum Value {
        /**
         * Not Present
         * 
         * <p>None of the concepts defined by the code system are included in the code system resource.
         */
        NOT_PRESENT("not-present"),

        /**
         * Example
         * 
         * <p>A few representative concepts are included in the code system resource. There is no useful intent in the subset 
         * choice and there's no process to make it workable: it's not intended to be workable.
         */
        EXAMPLE("example"),

        /**
         * Fragment
         * 
         * <p>A subset of the code system concepts are included in the code system resource. This is a curated subset released 
         * for a specific purpose under the governance of the code system steward, and that the intent, bounds and consequences 
         * of the fragmentation are clearly defined in the fragment or the code system documentation. Fragments are also known as 
         * partitions.
         */
        FRAGMENT("fragment"),

        /**
         * Complete
         * 
         * <p>All the concepts defined by the code system are included in the code system resource.
         */
        COMPLETE("complete"),

        /**
         * Supplement
         * 
         * <p>The resource doesn't define any new concepts; it just provides additional designations and properties to another 
         * code system.
         */
        SUPPLEMENT("supplement");

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
         * Factory method for creating CodeSystemContentMode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding CodeSystemContentMode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "not-present":
                return NOT_PRESENT;
            case "example":
                return EXAMPLE;
            case "fragment":
                return FRAGMENT;
            case "complete":
                return COMPLETE;
            case "supplement":
                return SUPPLEMENT;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
