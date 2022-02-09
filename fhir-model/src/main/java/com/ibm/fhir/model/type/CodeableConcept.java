/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CodeableConcept extends Element {
    @Summary
    private final List<Coding> coding;
    @Summary
    private final String text;

    private CodeableConcept(Builder builder) {
        super(builder);
        coding = Collections.unmodifiableList(builder.coding);
        text = builder.text;
    }

    /**
     * A reference to a code defined by a terminology system.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
     */
    public List<Coding> getCoding() {
        return coding;
    }

    /**
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which 
     * represents the intended meaning of the user.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getText() {
        return text;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !coding.isEmpty() || 
            (text != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(coding, "coding", visitor, Coding.class);
                accept(text, "text", visitor);
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
        CodeableConcept other = (CodeableConcept) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(coding, other.coding) && 
            Objects.equals(text, other.text);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                coding, 
                text);
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
        private List<Coding> coding = new ArrayList<>();
        private String text;

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
         * A reference to a code defined by a terminology system.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param coding
         *     Code defined by a terminology system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder coding(Coding... coding) {
            for (Coding value : coding) {
                this.coding.add(value);
            }
            return this;
        }

        /**
         * A reference to a code defined by a terminology system.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param coding
         *     Code defined by a terminology system
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder coding(Collection<Coding> coding) {
            this.coding = new ArrayList<>(coding);
            return this;
        }

        /**
         * Convenience method for setting {@code text}.
         * 
         * @param text
         *     Plain text representation of the concept
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #text(com.ibm.fhir.model.type.String)
         */
        public Builder text(java.lang.String text) {
            this.text = (text == null) ? null : String.of(text);
            return this;
        }

        /**
         * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which 
         * represents the intended meaning of the user.
         * 
         * @param text
         *     Plain text representation of the concept
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * Build the {@link CodeableConcept}
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid CodeableConcept per the base specification
         */
        @Override
        public CodeableConcept build() {
            CodeableConcept codeableConcept = new CodeableConcept(this);
            if (validating) {
                validate(codeableConcept);
            }
            return codeableConcept;
        }

        protected void validate(CodeableConcept codeableConcept) {
            super.validate(codeableConcept);
            ValidationSupport.checkList(codeableConcept.coding, "coding", Coding.class);
            ValidationSupport.requireValueOrChildren(codeableConcept);
        }

        protected Builder from(CodeableConcept codeableConcept) {
            super.from(codeableConcept);
            coding.addAll(codeableConcept.coding);
            text = codeableConcept.text;
            return this;
        }
    }
}
