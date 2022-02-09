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

@System("http://hl7.org/fhir/codesystem-hierarchy-meaning")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CodeSystemHierarchyMeaning extends Code {
    /**
     * Grouped By
     * 
     * <p>No particular relationship between the concepts can be assumed, except what can be determined by inspection of the 
     * definitions of the elements (possible reasons to use this: importing from a source where this is not defined, or where 
     * various parts of the hierarchy have different meanings).
     */
    public static final CodeSystemHierarchyMeaning GROUPED_BY = CodeSystemHierarchyMeaning.builder().value(Value.GROUPED_BY).build();

    /**
     * Is-A
     * 
     * <p>A hierarchy where the child concepts have an IS-A relationship with the parents - that is, all the properties of 
     * the parent are also true for its child concepts. Not that is-a is a property of the concepts, so additional 
     * subsumption relationships may be defined using properties or the [subsumes](extension-codesystem-subsumes.html) 
     * extension.
     */
    public static final CodeSystemHierarchyMeaning IS_A = CodeSystemHierarchyMeaning.builder().value(Value.IS_A).build();

    /**
     * Part Of
     * 
     * <p>Child elements list the individual parts of a composite whole (e.g. body site).
     */
    public static final CodeSystemHierarchyMeaning PART_OF = CodeSystemHierarchyMeaning.builder().value(Value.PART_OF).build();

    /**
     * Classified With
     * 
     * <p>Child concepts in the hierarchy may have only one parent, and there is a presumption that the code system is a 
     * "closed world" meaning all things must be in the hierarchy. This results in concepts such as "not otherwise 
     * classified.".
     */
    public static final CodeSystemHierarchyMeaning CLASSIFIED_WITH = CodeSystemHierarchyMeaning.builder().value(Value.CLASSIFIED_WITH).build();

    private volatile int hashCode;

    private CodeSystemHierarchyMeaning(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this CodeSystemHierarchyMeaning as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating CodeSystemHierarchyMeaning objects from a passed enum value.
     */
    public static CodeSystemHierarchyMeaning of(Value value) {
        switch (value) {
        case GROUPED_BY:
            return GROUPED_BY;
        case IS_A:
            return IS_A;
        case PART_OF:
            return PART_OF;
        case CLASSIFIED_WITH:
            return CLASSIFIED_WITH;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating CodeSystemHierarchyMeaning objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static CodeSystemHierarchyMeaning of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating CodeSystemHierarchyMeaning objects from a passed string value.
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
     * Inherited factory method for creating CodeSystemHierarchyMeaning objects from a passed string value.
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
        CodeSystemHierarchyMeaning other = (CodeSystemHierarchyMeaning) obj;
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
         *     An enum constant for CodeSystemHierarchyMeaning
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public CodeSystemHierarchyMeaning build() {
            CodeSystemHierarchyMeaning codeSystemHierarchyMeaning = new CodeSystemHierarchyMeaning(this);
            if (validating) {
                validate(codeSystemHierarchyMeaning);
            }
            return codeSystemHierarchyMeaning;
        }

        protected void validate(CodeSystemHierarchyMeaning codeSystemHierarchyMeaning) {
            super.validate(codeSystemHierarchyMeaning);
        }

        protected Builder from(CodeSystemHierarchyMeaning codeSystemHierarchyMeaning) {
            super.from(codeSystemHierarchyMeaning);
            return this;
        }
    }

    public enum Value {
        /**
         * Grouped By
         * 
         * <p>No particular relationship between the concepts can be assumed, except what can be determined by inspection of the 
         * definitions of the elements (possible reasons to use this: importing from a source where this is not defined, or where 
         * various parts of the hierarchy have different meanings).
         */
        GROUPED_BY("grouped-by"),

        /**
         * Is-A
         * 
         * <p>A hierarchy where the child concepts have an IS-A relationship with the parents - that is, all the properties of 
         * the parent are also true for its child concepts. Not that is-a is a property of the concepts, so additional 
         * subsumption relationships may be defined using properties or the [subsumes](extension-codesystem-subsumes.html) 
         * extension.
         */
        IS_A("is-a"),

        /**
         * Part Of
         * 
         * <p>Child elements list the individual parts of a composite whole (e.g. body site).
         */
        PART_OF("part-of"),

        /**
         * Classified With
         * 
         * <p>Child concepts in the hierarchy may have only one parent, and there is a presumption that the code system is a 
         * "closed world" meaning all things must be in the hierarchy. This results in concepts such as "not otherwise 
         * classified.".
         */
        CLASSIFIED_WITH("classified-with");

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
         * Factory method for creating CodeSystemHierarchyMeaning.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding CodeSystemHierarchyMeaning.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "grouped-by":
                return GROUPED_BY;
            case "is-a":
                return IS_A;
            case "part-of":
                return PART_OF;
            case "classified-with":
                return CLASSIFIED_WITH;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
