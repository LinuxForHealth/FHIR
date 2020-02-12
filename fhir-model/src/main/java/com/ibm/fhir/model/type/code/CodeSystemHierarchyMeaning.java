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

@System("http://hl7.org/fhir/codesystem-hierarchy-meaning")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CodeSystemHierarchyMeaning extends Code {
    /**
     * Grouped By
     */
    public static final CodeSystemHierarchyMeaning GROUPED_BY = CodeSystemHierarchyMeaning.builder().value(ValueSet.GROUPED_BY).build();

    /**
     * Is-A
     */
    public static final CodeSystemHierarchyMeaning IS_A = CodeSystemHierarchyMeaning.builder().value(ValueSet.IS_A).build();

    /**
     * Part Of
     */
    public static final CodeSystemHierarchyMeaning PART_OF = CodeSystemHierarchyMeaning.builder().value(ValueSet.PART_OF).build();

    /**
     * Classified With
     */
    public static final CodeSystemHierarchyMeaning CLASSIFIED_WITH = CodeSystemHierarchyMeaning.builder().value(ValueSet.CLASSIFIED_WITH).build();

    private volatile int hashCode;

    private CodeSystemHierarchyMeaning(Builder builder) {
        super(builder);
    }

    public static CodeSystemHierarchyMeaning of(ValueSet value) {
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

    public static CodeSystemHierarchyMeaning of(java.lang.String value) {
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
        public CodeSystemHierarchyMeaning build() {
            return new CodeSystemHierarchyMeaning(this);
        }
    }

    public enum ValueSet {
        /**
         * Grouped By
         */
        GROUPED_BY("grouped-by"),

        /**
         * Is-A
         */
        IS_A("is-a"),

        /**
         * Part Of
         */
        PART_OF("part-of"),

        /**
         * Classified With
         */
        CLASSIFIED_WITH("classified-with");

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
