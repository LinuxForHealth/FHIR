/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class CodeableConcept extends Element {
    private final List<Coding> coding;
    private final String text;

    private CodeableConcept(Builder builder) {
        super(builder);
        coding = Collections.unmodifiableList(builder.coding);
        text = builder.text;
    }

    /**
     * <p>
     * A reference to a code defined by a terminology system.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Coding}.
     */
    public List<Coding> getCoding() {
        return coding;
    }

    /**
     * <p>
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which 
     * represents the intended meaning of the user.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getText() {
        return text;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(coding, "coding", visitor, Coding.class);
                accept(text, "text", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.coding.addAll(coding);
        builder.text = text;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        // optional
        private List<Coding> coding = new ArrayList<>();
        private String text;

        private Builder() {
            super();
        }

        /**
         * <p>
         * Unique id for the element within a resource (for internal references). This may be any string value that does not 
         * contain spaces.
         * </p>
         * 
         * @param id
         *     Unique id for inter-element referencing
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * A reference to a code defined by a terminology system.
         * </p>
         * 
         * @param coding
         *     Code defined by a terminology system
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder coding(Coding... coding) {
            for (Coding value : coding) {
                this.coding.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A reference to a code defined by a terminology system.
         * </p>
         * 
         * @param coding
         *     Code defined by a terminology system
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder coding(Collection<Coding> coding) {
            this.coding.addAll(coding);
            return this;
        }

        /**
         * <p>
         * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which 
         * represents the intended meaning of the user.
         * </p>
         * 
         * @param text
         *     Plain text representation of the concept
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        @Override
        public CodeableConcept build() {
            return new CodeableConcept(this);
        }
    }
}
