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
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A reference from one resource to another.
 */
@Constraint(
    id = "ref-1",
    level = "Rule",
    location = "(base)",
    description = "SHALL have a contained resource if a local reference is provided",
    expression = "reference.startsWith('#').not() or (reference.substring(1).trace('url') in %rootResource.contained.id.trace('ids'))",
    source = "http://hl7.org/fhir/StructureDefinition/Reference"
)
@Constraint(
    id = "reference-2",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/resource-types",
    expression = "type.exists() implies (type.memberOf('http://hl7.org/fhir/ValueSet/resource-types', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Reference",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Reference extends Element {
    @Summary
    private final String reference;
    @Summary
    @Binding(
        bindingName = "FHIRResourceTypeExt",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Aa resource (or, for logical models, the URI of the logical model).",
        valueSet = "http://hl7.org/fhir/ValueSet/resource-types"
    )
    private final Uri type;
    @Summary
    private final Identifier identifier;
    @Summary
    private final String display;

    private Reference(Builder builder) {
        super(builder);
        reference = builder.reference;
        type = builder.type;
        identifier = builder.identifier;
        display = builder.display;
    }

    /**
     * A reference to a location at which the other resource is found. The reference may be a relative reference, in which 
     * case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is 
     * found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should 
     * be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getReference() {
        return reference;
    }

    /**
     * The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and 
     * Reference.reference is a FHIR URL, both SHALL be consistent.
     * 
     * <p>The type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs 
     * that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. "Patient" is a reference to http://hl7.
     * org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in 
     * references in logical models, not resources).
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getType() {
        return type;
    }

    /**
     * An identifier for the target resource. This is used when there is no way to reference the other resource directly, 
     * either because the entity it represents is not available through a FHIR server, or because there is no way for the 
     * author of the resource to convert a known identifier to an actual location. There is no requirement that a Reference.
     * identifier point to something that is actually exposed as a FHIR instance, but it SHALL point to a business concept 
     * that would be expected to be exposed as a FHIR instance, and that instance would need to be of a FHIR resource type 
     * allowed by the reference.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * Plain text narrative that identifies the resource in addition to the resource reference.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDisplay() {
        return display;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (reference != null) || 
            (type != null) || 
            (identifier != null) || 
            (display != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(reference, "reference", visitor);
                accept(type, "type", visitor);
                accept(identifier, "identifier", visitor);
                accept(display, "display", visitor);
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
        Reference other = (Reference) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(reference, other.reference) && 
            Objects.equals(type, other.type) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(display, other.display);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                reference, 
                type, 
                identifier, 
                display);
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
        private String reference;
        private Uri type;
        private Identifier identifier;
        private String display;

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
         * Convenience method for setting {@code reference}.
         * 
         * @param reference
         *     Literal reference, Relative, internal or absolute URL
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #reference(com.ibm.fhir.model.type.String)
         */
        public Builder reference(java.lang.String reference) {
            this.reference = (reference == null) ? null : String.of(reference);
            return this;
        }

        /**
         * A reference to a location at which the other resource is found. The reference may be a relative reference, in which 
         * case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is 
         * found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should 
         * be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.
         * 
         * @param reference
         *     Literal reference, Relative, internal or absolute URL
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        /**
         * The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and 
         * Reference.reference is a FHIR URL, both SHALL be consistent.
         * 
         * <p>The type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs 
         * that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. "Patient" is a reference to http://hl7.
         * org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in 
         * references in logical models, not resources).
         * 
         * @param type
         *     Type the reference refers to (e.g. "Patient")
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(Uri type) {
            this.type = type;
            return this;
        }

        /**
         * An identifier for the target resource. This is used when there is no way to reference the other resource directly, 
         * either because the entity it represents is not available through a FHIR server, or because there is no way for the 
         * author of the resource to convert a known identifier to an actual location. There is no requirement that a Reference.
         * identifier point to something that is actually exposed as a FHIR instance, but it SHALL point to a business concept 
         * that would be expected to be exposed as a FHIR instance, and that instance would need to be of a FHIR resource type 
         * allowed by the reference.
         * 
         * @param identifier
         *     Logical reference, when literal reference is not known
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * Convenience method for setting {@code display}.
         * 
         * @param display
         *     Text alternative for the resource
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #display(com.ibm.fhir.model.type.String)
         */
        public Builder display(java.lang.String display) {
            this.display = (display == null) ? null : String.of(display);
            return this;
        }

        /**
         * Plain text narrative that identifies the resource in addition to the resource reference.
         * 
         * @param display
         *     Text alternative for the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder display(String display) {
            this.display = display;
            return this;
        }

        /**
         * Build the {@link Reference}
         * 
         * @return
         *     An immutable object of type {@link Reference}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Reference per the base specification
         */
        @Override
        public Reference build() {
            Reference reference = new Reference(this);
            if (validating) {
                validate(reference);
            }
            return reference;
        }

        protected void validate(Reference reference) {
            super.validate(reference);
            ValidationSupport.requireValueOrChildren(reference);
        }

        protected Builder from(Reference reference) {
            super.from(reference);
            this.reference = reference.reference;
            type = reference.type;
            identifier = reference.identifier;
            display = reference.display;
            return this;
        }
    }
}
