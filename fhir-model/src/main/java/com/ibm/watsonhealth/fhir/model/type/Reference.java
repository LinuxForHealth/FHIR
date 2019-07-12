/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A reference from one resource to another.
 * </p>
 */
@Constraint(
    id = "ref-1",
    level = "Rule",
    location = "(base)",
    description = "SHALL have a contained resource if a local reference is provided",
    expression = "reference.startsWith('#').not() or (reference.substring(1).trace('url') in %resource.contained.id.trace('ids'))"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Reference extends Element {
    private final String reference;
    private final Uri type;
    private final Identifier identifier;
    private final String display;

    private volatile int hashCode;

    private Reference(Builder builder) {
        super(builder);
        reference = builder.reference;
        type = builder.type;
        identifier = builder.identifier;
        display = builder.display;
        ValidationSupport.requireValueOrChildren(this);
    }

    /**
     * <p>
     * A reference to a location at which the other resource is found. The reference may be a relative reference, in which 
     * case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is 
     * found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should 
     * be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getReference() {
        return reference;
    }

    /**
     * <p>
     * The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and 
     * Reference.reference is a FHIR URL, both SHALL be consistent.
     * </p>
     * <p>
     * The type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs 
     * that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. "Patient" is a reference to http://hl7.
     * org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in 
     * references in logical models, not resources).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getType() {
        return type;
    }

    /**
     * <p>
     * An identifier for the target resource. This is used when there is no way to reference the other resource directly, 
     * either because the entity it represents is not available through a FHIR server, or because there is no way for the 
     * author of the resource to convert a known identifier to an actual location. There is no requirement that a Reference.
     * identifier point to something that is actually exposed as a FHIR instance, but it SHALL point to a business concept 
     * that would be expected to be exposed as a FHIR instance, and that instance would need to be of a FHIR resource type 
     * allowed by the reference.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * Plain text narrative that identifies the resource in addition to the resource reference.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
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
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(reference, "reference", visitor);
                accept(type, "type", visitor);
                accept(identifier, "identifier", visitor);
                accept(display, "display", visitor);
            }
            visitor.visitEnd(elementName, this);
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
        // optional
        private String reference;
        private Uri type;
        private Identifier identifier;
        private String display;

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
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * A reference to a location at which the other resource is found. The reference may be a relative reference, in which 
         * case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is 
         * found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should 
         * be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.
         * </p>
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
         * <p>
         * The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and 
         * Reference.reference is a FHIR URL, both SHALL be consistent.
         * </p>
         * <p>
         * The type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs 
         * that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. "Patient" is a reference to http://hl7.
         * org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in 
         * references in logical models, not resources).
         * </p>
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
         * <p>
         * An identifier for the target resource. This is used when there is no way to reference the other resource directly, 
         * either because the entity it represents is not available through a FHIR server, or because there is no way for the 
         * author of the resource to convert a known identifier to an actual location. There is no requirement that a Reference.
         * identifier point to something that is actually exposed as a FHIR instance, but it SHALL point to a business concept 
         * that would be expected to be exposed as a FHIR instance, and that instance would need to be of a FHIR resource type 
         * allowed by the reference.
         * </p>
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
         * <p>
         * Plain text narrative that identifies the resource in addition to the resource reference.
         * </p>
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

        @Override
        public Reference build() {
            return new Reference(this);
        }

        private Builder from(Reference reference) {
            id = reference.id;
            extension.addAll(reference.extension);
            this.reference = reference.reference;
            type = reference.type;
            identifier = reference.identifier;
            display = reference.display;
            return this;
        }
    }
}
