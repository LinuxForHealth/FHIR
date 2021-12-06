/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A human-readable summary of the resource conveying the essential clinical and business information for the resource.
 */
@Constraint(
    id = "txt-1",
    level = "Rule",
    location = "Narrative.`div`",
    description = "The narrative SHALL contain only the basic html formatting elements and attributes described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, <a> elements (either name or href), images and internally contained style attributes",
    expression = "htmlChecks()",
    source = "http://hl7.org/fhir/StructureDefinition/Narrative"
)
@Constraint(
    id = "txt-2",
    level = "Rule",
    location = "Narrative.`div`",
    description = "The narrative SHALL have some non-whitespace content",
    expression = "htmlChecks()",
    source = "http://hl7.org/fhir/StructureDefinition/Narrative"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Narrative extends Element {
    public static final Narrative EMPTY = builder().status(NarrativeStatus.EMPTY).div(Xhtml.from("Narrative text intentionally left empty")).build();

    @Binding(
        bindingName = "NarrativeStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The status of a resource narrative.",
        valueSet = "http://hl7.org/fhir/ValueSet/narrative-status|4.1.0"
    )
    @Required
    private final NarrativeStatus status;
    @Required
    private final Xhtml div;

    private Narrative(Builder builder) {
        super(builder);
        status = builder.status;
        div = builder.div;
    }

    /**
     * The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or 
     * whether a human authored it and it may contain additional data.
     * 
     * @return
     *     An immutable object of type {@link NarrativeStatus} that is non-null.
     */
    public NarrativeStatus getStatus() {
        return status;
    }

    /**
     * The actual narrative content, a stripped down version of XHTML.
     * 
     * @return
     *     An immutable object of type {@link Xhtml} that is non-null.
     */
    public Xhtml getDiv() {
        return div;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (status != null) || 
            (div != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(status, "status", visitor);
                accept(div, "div", visitor);
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
        Narrative other = (Narrative) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(status, other.status) && 
            Objects.equals(div, other.div);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                status, 
                div);
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
        private NarrativeStatus status;
        private Xhtml div;

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
         * The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or 
         * whether a human authored it and it may contain additional data.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     generated | extensions | additional | empty
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(NarrativeStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The actual narrative content, a stripped down version of XHTML.
         * 
         * <p>This element is required.
         * 
         * @param div
         *     Limited xhtml content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder div(Xhtml div) {
            this.div = div;
            return this;
        }

        /**
         * Build the {@link Narrative}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>div</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Narrative}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Narrative per the base specification
         */
        @Override
        public Narrative build() {
            Narrative narrative = new Narrative(this);
            if (validating) {
                validate(narrative);
            }
            return narrative;
        }

        protected void validate(Narrative narrative) {
            super.validate(narrative);
            ValidationSupport.requireNonNull(narrative.status, "status");
            ValidationSupport.requireNonNull(narrative.div, "div");
            ValidationSupport.requireValueOrChildren(narrative);
        }

        protected Builder from(Narrative narrative) {
            super.from(narrative);
            status = narrative.status;
            div = narrative.div;
            return this;
        }
    }
}
