/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.IdentifierUse;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * An identifier - identifies some entity uniquely and unambiguously. Typically this is used for business identifiers.
 */
@Constraint(
    id = "identifier-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/identifier-type",
    expression = "type.exists() implies (type.memberOf('http://hl7.org/fhir/ValueSet/identifier-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Identifier",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Identifier extends Element {
    @Summary
    @Binding(
        bindingName = "IdentifierUse",
        strength = BindingStrength.Value.REQUIRED,
        valueSet = "http://hl7.org/fhir/ValueSet/identifier-use|4.3.0-CIBUILD"
    )
    private final IdentifierUse use;
    @Summary
    @Binding(
        bindingName = "IdentifierType",
        strength = BindingStrength.Value.EXTENSIBLE,
        valueSet = "http://hl7.org/fhir/ValueSet/identifier-type"
    )
    private final CodeableConcept type;
    @Summary
    private final Uri system;
    @Summary
    private final String value;
    @Summary
    private final Period period;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference assigner;

    private Identifier(Builder builder) {
        super(builder);
        use = builder.use;
        type = builder.type;
        system = builder.system;
        value = builder.value;
        period = builder.period;
        assigner = builder.assigner;
    }

    /**
     * The purpose of this identifier.
     * 
     * @return
     *     An immutable object of type {@link IdentifierUse} that may be null.
     */
    public IdentifierUse getUse() {
        return use;
    }

    /**
     * A coded type for the identifier that can be used to determine which identifier to use for a specific purpose.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * Establishes the namespace for the value - that is, a URL that describes a set values that are unique.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getSystem() {
        return system;
    }

    /**
     * The portion of the identifier typically relevant to the user and which is unique within the context of the system.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getValue() {
        return value;
    }

    /**
     * Time period during which identifier is/was valid for use.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Organization that issued/manages the identifier.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAssigner() {
        return assigner;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (use != null) || 
            (type != null) || 
            (system != null) || 
            (value != null) || 
            (period != null) || 
            (assigner != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(use, "use", visitor);
                accept(type, "type", visitor);
                accept(system, "system", visitor);
                accept(value, "value", visitor);
                accept(period, "period", visitor);
                accept(assigner, "assigner", visitor);
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
        Identifier other = (Identifier) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(use, other.use) && 
            Objects.equals(type, other.type) && 
            Objects.equals(system, other.system) && 
            Objects.equals(value, other.value) && 
            Objects.equals(period, other.period) && 
            Objects.equals(assigner, other.assigner);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                use, 
                type, 
                system, 
                value, 
                period, 
                assigner);
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
        private IdentifierUse use;
        private CodeableConcept type;
        private Uri system;
        private String value;
        private Period period;
        private Reference assigner;

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
         * The purpose of this identifier.
         * 
         * @param use
         *     usual | official | temp | secondary | old (If known)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder use(IdentifierUse use) {
            this.use = use;
            return this;
        }

        /**
         * A coded type for the identifier that can be used to determine which identifier to use for a specific purpose.
         * 
         * @param type
         *     Description of identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * Establishes the namespace for the value - that is, a URL that describes a set values that are unique.
         * 
         * @param system
         *     The namespace for the identifier value
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder system(Uri system) {
            this.system = system;
            return this;
        }

        /**
         * Convenience method for setting {@code value}.
         * 
         * @param value
         *     The value that is unique
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #value(com.ibm.fhir.model.type.String)
         */
        public Builder value(java.lang.String value) {
            this.value = (value == null) ? null : String.of(value);
            return this;
        }

        /**
         * The portion of the identifier typically relevant to the user and which is unique within the context of the system.
         * 
         * @param value
         *     The value that is unique
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(String value) {
            this.value = value;
            return this;
        }

        /**
         * Time period during which identifier is/was valid for use.
         * 
         * @param period
         *     Time period when id is/was valid for use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * Organization that issued/manages the identifier.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param assigner
         *     Organization that issued id (may be just text)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder assigner(Reference assigner) {
            this.assigner = assigner;
            return this;
        }

        /**
         * Build the {@link Identifier}
         * 
         * @return
         *     An immutable object of type {@link Identifier}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Identifier per the base specification
         */
        @Override
        public Identifier build() {
            Identifier identifier = new Identifier(this);
            if (validating) {
                validate(identifier);
            }
            return identifier;
        }

        protected void validate(Identifier identifier) {
            super.validate(identifier);
            ValidationSupport.checkReferenceType(identifier.assigner, "assigner", "Organization");
            ValidationSupport.requireValueOrChildren(identifier);
        }

        protected Builder from(Identifier identifier) {
            super.from(identifier);
            use = identifier.use;
            type = identifier.type;
            system = identifier.system;
            value = identifier.value;
            period = identifier.period;
            assigner = identifier.assigner;
            return this;
        }
    }
}
