/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A reference to a resource (by instance), or instead, a reference to a concept defined in a terminology or ontology (by 
 * class).
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CodeableReference extends Element {
    @Summary
    private final CodeableConcept concept;
    @Summary
    private final Reference reference;

    private CodeableReference(Builder builder) {
        super(builder);
        concept = builder.concept;
        reference = builder.reference;
    }

    /**
     * A reference to a concept - e.g. the information is identified by its general class to the degree of precision found in 
     * the terminology.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getConcept() {
        return concept;
    }

    /**
     * A reference to a resource the provides exact details about the information being referenced.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getReference() {
        return reference;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (concept != null) || 
            (reference != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(concept, "concept", visitor);
                accept(reference, "reference", visitor);
            }
            visitor.visitEnd(elementName, elementIndex, this);
            visitor.postVisit(this);
        }
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
        CodeableReference other = (CodeableReference) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(concept, other.concept) && 
            Objects.equals(reference, other.reference);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                concept, 
                reference);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        private CodeableConcept concept;
        private Reference reference;

        private Builder() {
            super();
        }

        /**
         * Unique id for the element within a resource (for internal references). This may be any string value that does not 
         * contain spaces.
         * 
         * @param id
         *     Unique id for inter-element referencing
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * A reference to a concept - e.g. the information is identified by its general class to the degree of precision found in 
         * the terminology.
         * 
         * @param concept
         *     Reference to a concept (by class)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder concept(CodeableConcept concept) {
            this.concept = concept;
            return this;
        }

        /**
         * A reference to a resource the provides exact details about the information being referenced.
         * 
         * @param reference
         *     Reference to a resource (by instance)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reference(Reference reference) {
            this.reference = reference;
            return this;
        }

        /**
         * Build the {@link CodeableReference}
         * 
         * @return
         *     An immutable object of type {@link CodeableReference}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid CodeableReference per the base specification
         */
        @Override
        public CodeableReference build() {
            CodeableReference codeableReference = new CodeableReference(this);
            if (validating) {
                validate(codeableReference);
            }
            return codeableReference;
        }

        protected void validate(CodeableReference codeableReference) {
            super.validate(codeableReference);
            ValidationSupport.requireValueOrChildren(codeableReference);
        }

        protected Builder from(CodeableReference codeableReference) {
            super.from(codeableReference);
            concept = codeableReference.concept;
            reference = codeableReference.reference;
            return this;
        }
    }
}
