/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * An ingredient of a manufactured item or pharmaceutical product.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicinalProductIngredient extends DomainResource {
    @Summary
    private final Identifier identifier;
    @Summary
    @Required
    private final CodeableConcept role;
    @Summary
    private final Boolean allergenicIndicator;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final List<Reference> manufacturer;
    @Summary
    private final List<SpecifiedSubstance> specifiedSubstance;
    @Summary
    private final Substance substance;

    private MedicinalProductIngredient(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        role = builder.role;
        allergenicIndicator = builder.allergenicIndicator;
        manufacturer = Collections.unmodifiableList(builder.manufacturer);
        specifiedSubstance = Collections.unmodifiableList(builder.specifiedSubstance);
        substance = builder.substance;
    }

    /**
     * The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct 
     * URL reference to the resource itself is not appropriate.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * Ingredient role e.g. Active ingredient, excipient.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getRole() {
        return role;
    }

    /**
     * If the ingredient is a known or suspected allergen.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getAllergenicIndicator() {
        return allergenicIndicator;
    }

    /**
     * Manufacturer of this Ingredient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getManufacturer() {
        return manufacturer;
    }

    /**
     * A specified substance that comprises this ingredient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link SpecifiedSubstance} that may be empty.
     */
    public List<SpecifiedSubstance> getSpecifiedSubstance() {
        return specifiedSubstance;
    }

    /**
     * The ingredient substance.
     * 
     * @return
     *     An immutable object of type {@link Substance} that may be null.
     */
    public Substance getSubstance() {
        return substance;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (identifier != null) || 
            (role != null) || 
            (allergenicIndicator != null) || 
            !manufacturer.isEmpty() || 
            !specifiedSubstance.isEmpty() || 
            (substance != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(meta, "meta", visitor);
                accept(implicitRules, "implicitRules", visitor);
                accept(language, "language", visitor);
                accept(text, "text", visitor);
                accept(contained, "contained", visitor, Resource.class);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(identifier, "identifier", visitor);
                accept(role, "role", visitor);
                accept(allergenicIndicator, "allergenicIndicator", visitor);
                accept(manufacturer, "manufacturer", visitor, Reference.class);
                accept(specifiedSubstance, "specifiedSubstance", visitor, SpecifiedSubstance.class);
                accept(substance, "substance", visitor);
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
        MedicinalProductIngredient other = (MedicinalProductIngredient) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(role, other.role) && 
            Objects.equals(allergenicIndicator, other.allergenicIndicator) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(specifiedSubstance, other.specifiedSubstance) && 
            Objects.equals(substance, other.substance);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                text, 
                contained, 
                extension, 
                modifierExtension, 
                identifier, 
                role, 
                allergenicIndicator, 
                manufacturer, 
                specifiedSubstance, 
                substance);
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

    public static class Builder extends DomainResource.Builder {
        private Identifier identifier;
        private CodeableConcept role;
        private Boolean allergenicIndicator;
        private List<Reference> manufacturer = new ArrayList<>();
        private List<SpecifiedSubstance> specifiedSubstance = new ArrayList<>();
        private Substance substance;

        private Builder() {
            super();
        }

        /**
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * The base language in which the resource is written.
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct 
         * URL reference to the resource itself is not appropriate.
         * 
         * @param identifier
         *     Identifier for the ingredient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * Ingredient role e.g. Active ingredient, excipient.
         * 
         * <p>This element is required.
         * 
         * @param role
         *     Ingredient role e.g. Active ingredient, excipient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder role(CodeableConcept role) {
            this.role = role;
            return this;
        }

        /**
         * Convenience method for setting {@code allergenicIndicator}.
         * 
         * @param allergenicIndicator
         *     If the ingredient is a known or suspected allergen
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #allergenicIndicator(com.ibm.fhir.model.type.Boolean)
         */
        public Builder allergenicIndicator(java.lang.Boolean allergenicIndicator) {
            this.allergenicIndicator = (allergenicIndicator == null) ? null : Boolean.of(allergenicIndicator);
            return this;
        }

        /**
         * If the ingredient is a known or suspected allergen.
         * 
         * @param allergenicIndicator
         *     If the ingredient is a known or suspected allergen
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder allergenicIndicator(Boolean allergenicIndicator) {
            this.allergenicIndicator = allergenicIndicator;
            return this;
        }

        /**
         * Manufacturer of this Ingredient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param manufacturer
         *     Manufacturer of this Ingredient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufacturer(Reference... manufacturer) {
            for (Reference value : manufacturer) {
                this.manufacturer.add(value);
            }
            return this;
        }

        /**
         * Manufacturer of this Ingredient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param manufacturer
         *     Manufacturer of this Ingredient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder manufacturer(Collection<Reference> manufacturer) {
            this.manufacturer = new ArrayList<>(manufacturer);
            return this;
        }

        /**
         * A specified substance that comprises this ingredient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specifiedSubstance
         *     A specified substance that comprises this ingredient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specifiedSubstance(SpecifiedSubstance... specifiedSubstance) {
            for (SpecifiedSubstance value : specifiedSubstance) {
                this.specifiedSubstance.add(value);
            }
            return this;
        }

        /**
         * A specified substance that comprises this ingredient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specifiedSubstance
         *     A specified substance that comprises this ingredient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder specifiedSubstance(Collection<SpecifiedSubstance> specifiedSubstance) {
            this.specifiedSubstance = new ArrayList<>(specifiedSubstance);
            return this;
        }

        /**
         * The ingredient substance.
         * 
         * @param substance
         *     The ingredient substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder substance(Substance substance) {
            this.substance = substance;
            return this;
        }

        /**
         * Build the {@link MedicinalProductIngredient}
         * 
         * <p>Required elements:
         * <ul>
         * <li>role</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MedicinalProductIngredient}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicinalProductIngredient per the base specification
         */
        @Override
        public MedicinalProductIngredient build() {
            MedicinalProductIngredient medicinalProductIngredient = new MedicinalProductIngredient(this);
            if (validating) {
                validate(medicinalProductIngredient);
            }
            return medicinalProductIngredient;
        }

        protected void validate(MedicinalProductIngredient medicinalProductIngredient) {
            super.validate(medicinalProductIngredient);
            ValidationSupport.requireNonNull(medicinalProductIngredient.role, "role");
            ValidationSupport.checkList(medicinalProductIngredient.manufacturer, "manufacturer", Reference.class);
            ValidationSupport.checkList(medicinalProductIngredient.specifiedSubstance, "specifiedSubstance", SpecifiedSubstance.class);
            ValidationSupport.checkReferenceType(medicinalProductIngredient.manufacturer, "manufacturer", "Organization");
        }

        protected Builder from(MedicinalProductIngredient medicinalProductIngredient) {
            super.from(medicinalProductIngredient);
            identifier = medicinalProductIngredient.identifier;
            role = medicinalProductIngredient.role;
            allergenicIndicator = medicinalProductIngredient.allergenicIndicator;
            manufacturer.addAll(medicinalProductIngredient.manufacturer);
            specifiedSubstance.addAll(medicinalProductIngredient.specifiedSubstance);
            substance = medicinalProductIngredient.substance;
            return this;
        }
    }

    /**
     * A specified substance that comprises this ingredient.
     */
    public static class SpecifiedSubstance extends BackboneElement {
        @Summary
        @Required
        private final CodeableConcept code;
        @Summary
        @Required
        private final CodeableConcept group;
        @Summary
        private final CodeableConcept confidentiality;
        @Summary
        private final List<Strength> strength;

        private SpecifiedSubstance(Builder builder) {
            super(builder);
            code = builder.code;
            group = builder.group;
            confidentiality = builder.confidentiality;
            strength = Collections.unmodifiableList(builder.strength);
        }

        /**
         * The specified substance.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * The group of specified substance, e.g. group 1 to 4.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getGroup() {
            return group;
        }

        /**
         * Confidentiality level of the specified substance as the ingredient.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getConfidentiality() {
            return confidentiality;
        }

        /**
         * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Strength} that may be empty.
         */
        public List<Strength> getStrength() {
            return strength;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (group != null) || 
                (confidentiality != null) || 
                !strength.isEmpty();
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
                    accept(group, "group", visitor);
                    accept(confidentiality, "confidentiality", visitor);
                    accept(strength, "strength", visitor, Strength.class);
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
            SpecifiedSubstance other = (SpecifiedSubstance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(group, other.group) && 
                Objects.equals(confidentiality, other.confidentiality) && 
                Objects.equals(strength, other.strength);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    group, 
                    confidentiality, 
                    strength);
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

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept code;
            private CodeableConcept group;
            private CodeableConcept confidentiality;
            private List<Strength> strength = new ArrayList<>();

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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * The specified substance.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     The specified substance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * The group of specified substance, e.g. group 1 to 4.
             * 
             * <p>This element is required.
             * 
             * @param group
             *     The group of specified substance, e.g. group 1 to 4
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder group(CodeableConcept group) {
                this.group = group;
                return this;
            }

            /**
             * Confidentiality level of the specified substance as the ingredient.
             * 
             * @param confidentiality
             *     Confidentiality level of the specified substance as the ingredient
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder confidentiality(CodeableConcept confidentiality) {
                this.confidentiality = confidentiality;
                return this;
            }

            /**
             * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param strength
             *     Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder strength(Strength... strength) {
                for (Strength value : strength) {
                    this.strength.add(value);
                }
                return this;
            }

            /**
             * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param strength
             *     Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder strength(Collection<Strength> strength) {
                this.strength = new ArrayList<>(strength);
                return this;
            }

            /**
             * Build the {@link SpecifiedSubstance}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * <li>group</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link SpecifiedSubstance}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid SpecifiedSubstance per the base specification
             */
            @Override
            public SpecifiedSubstance build() {
                SpecifiedSubstance specifiedSubstance = new SpecifiedSubstance(this);
                if (validating) {
                    validate(specifiedSubstance);
                }
                return specifiedSubstance;
            }

            protected void validate(SpecifiedSubstance specifiedSubstance) {
                super.validate(specifiedSubstance);
                ValidationSupport.requireNonNull(specifiedSubstance.code, "code");
                ValidationSupport.requireNonNull(specifiedSubstance.group, "group");
                ValidationSupport.checkList(specifiedSubstance.strength, "strength", Strength.class);
                ValidationSupport.requireValueOrChildren(specifiedSubstance);
            }

            protected Builder from(SpecifiedSubstance specifiedSubstance) {
                super.from(specifiedSubstance);
                code = specifiedSubstance.code;
                group = specifiedSubstance.group;
                confidentiality = specifiedSubstance.confidentiality;
                strength.addAll(specifiedSubstance.strength);
                return this;
            }
        }

        /**
         * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
         */
        public static class Strength extends BackboneElement {
            @Summary
            @Required
            private final Ratio presentation;
            @Summary
            private final Ratio presentationLowLimit;
            @Summary
            private final Ratio concentration;
            @Summary
            private final Ratio concentrationLowLimit;
            @Summary
            private final String measurementPoint;
            @Summary
            private final List<CodeableConcept> country;
            @Summary
            private final List<ReferenceStrength> referenceStrength;

            private Strength(Builder builder) {
                super(builder);
                presentation = builder.presentation;
                presentationLowLimit = builder.presentationLowLimit;
                concentration = builder.concentration;
                concentrationLowLimit = builder.concentrationLowLimit;
                measurementPoint = builder.measurementPoint;
                country = Collections.unmodifiableList(builder.country);
                referenceStrength = Collections.unmodifiableList(builder.referenceStrength);
            }

            /**
             * The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product 
             * or manufactured item.
             * 
             * @return
             *     An immutable object of type {@link Ratio} that is non-null.
             */
            public Ratio getPresentation() {
                return presentation;
            }

            /**
             * A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, 
             * this is the lower limit, with the presentation attribute becoming the upper limit.
             * 
             * @return
             *     An immutable object of type {@link Ratio} that may be null.
             */
            public Ratio getPresentationLowLimit() {
                return presentationLowLimit;
            }

            /**
             * The strength per unitary volume (or mass).
             * 
             * @return
             *     An immutable object of type {@link Ratio} that may be null.
             */
            public Ratio getConcentration() {
                return concentration;
            }

            /**
             * A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute 
             * then becomes the upper limit.
             * 
             * @return
             *     An immutable object of type {@link Ratio} that may be null.
             */
            public Ratio getConcentrationLowLimit() {
                return concentrationLowLimit;
            }

            /**
             * For when strength is measured at a particular point or distance.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getMeasurementPoint() {
                return measurementPoint;
            }

            /**
             * The country or countries for which the strength range applies.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getCountry() {
                return country;
            }

            /**
             * Strength expressed in terms of a reference substance.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link ReferenceStrength} that may be empty.
             */
            public List<ReferenceStrength> getReferenceStrength() {
                return referenceStrength;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (presentation != null) || 
                    (presentationLowLimit != null) || 
                    (concentration != null) || 
                    (concentrationLowLimit != null) || 
                    (measurementPoint != null) || 
                    !country.isEmpty() || 
                    !referenceStrength.isEmpty();
            }

            @Override
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(presentation, "presentation", visitor);
                        accept(presentationLowLimit, "presentationLowLimit", visitor);
                        accept(concentration, "concentration", visitor);
                        accept(concentrationLowLimit, "concentrationLowLimit", visitor);
                        accept(measurementPoint, "measurementPoint", visitor);
                        accept(country, "country", visitor, CodeableConcept.class);
                        accept(referenceStrength, "referenceStrength", visitor, ReferenceStrength.class);
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
                Strength other = (Strength) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(presentation, other.presentation) && 
                    Objects.equals(presentationLowLimit, other.presentationLowLimit) && 
                    Objects.equals(concentration, other.concentration) && 
                    Objects.equals(concentrationLowLimit, other.concentrationLowLimit) && 
                    Objects.equals(measurementPoint, other.measurementPoint) && 
                    Objects.equals(country, other.country) && 
                    Objects.equals(referenceStrength, other.referenceStrength);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        presentation, 
                        presentationLowLimit, 
                        concentration, 
                        concentrationLowLimit, 
                        measurementPoint, 
                        country, 
                        referenceStrength);
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

            public static class Builder extends BackboneElement.Builder {
                private Ratio presentation;
                private Ratio presentationLowLimit;
                private Ratio concentration;
                private Ratio concentrationLowLimit;
                private String measurementPoint;
                private List<CodeableConcept> country = new ArrayList<>();
                private List<ReferenceStrength> referenceStrength = new ArrayList<>();

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
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * 
                 * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder modifierExtension(Extension... modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * 
                 * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                @Override
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product 
                 * or manufactured item.
                 * 
                 * <p>This element is required.
                 * 
                 * @param presentation
                 *     The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product 
                 *     or manufactured item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder presentation(Ratio presentation) {
                    this.presentation = presentation;
                    return this;
                }

                /**
                 * A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, 
                 * this is the lower limit, with the presentation attribute becoming the upper limit.
                 * 
                 * @param presentationLowLimit
                 *     A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, 
                 *     this is the lower limit, with the presentation attribute becoming the upper limit
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder presentationLowLimit(Ratio presentationLowLimit) {
                    this.presentationLowLimit = presentationLowLimit;
                    return this;
                }

                /**
                 * The strength per unitary volume (or mass).
                 * 
                 * @param concentration
                 *     The strength per unitary volume (or mass)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder concentration(Ratio concentration) {
                    this.concentration = concentration;
                    return this;
                }

                /**
                 * A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute 
                 * then becomes the upper limit.
                 * 
                 * @param concentrationLowLimit
                 *     A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute 
                 *     then becomes the upper limit
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder concentrationLowLimit(Ratio concentrationLowLimit) {
                    this.concentrationLowLimit = concentrationLowLimit;
                    return this;
                }

                /**
                 * Convenience method for setting {@code measurementPoint}.
                 * 
                 * @param measurementPoint
                 *     For when strength is measured at a particular point or distance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #measurementPoint(com.ibm.fhir.model.type.String)
                 */
                public Builder measurementPoint(java.lang.String measurementPoint) {
                    this.measurementPoint = (measurementPoint == null) ? null : String.of(measurementPoint);
                    return this;
                }

                /**
                 * For when strength is measured at a particular point or distance.
                 * 
                 * @param measurementPoint
                 *     For when strength is measured at a particular point or distance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder measurementPoint(String measurementPoint) {
                    this.measurementPoint = measurementPoint;
                    return this;
                }

                /**
                 * The country or countries for which the strength range applies.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param country
                 *     The country or countries for which the strength range applies
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder country(CodeableConcept... country) {
                    for (CodeableConcept value : country) {
                        this.country.add(value);
                    }
                    return this;
                }

                /**
                 * The country or countries for which the strength range applies.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param country
                 *     The country or countries for which the strength range applies
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder country(Collection<CodeableConcept> country) {
                    this.country = new ArrayList<>(country);
                    return this;
                }

                /**
                 * Strength expressed in terms of a reference substance.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param referenceStrength
                 *     Strength expressed in terms of a reference substance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder referenceStrength(ReferenceStrength... referenceStrength) {
                    for (ReferenceStrength value : referenceStrength) {
                        this.referenceStrength.add(value);
                    }
                    return this;
                }

                /**
                 * Strength expressed in terms of a reference substance.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param referenceStrength
                 *     Strength expressed in terms of a reference substance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder referenceStrength(Collection<ReferenceStrength> referenceStrength) {
                    this.referenceStrength = new ArrayList<>(referenceStrength);
                    return this;
                }

                /**
                 * Build the {@link Strength}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>presentation</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Strength}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Strength per the base specification
                 */
                @Override
                public Strength build() {
                    Strength strength = new Strength(this);
                    if (validating) {
                        validate(strength);
                    }
                    return strength;
                }

                protected void validate(Strength strength) {
                    super.validate(strength);
                    ValidationSupport.requireNonNull(strength.presentation, "presentation");
                    ValidationSupport.checkList(strength.country, "country", CodeableConcept.class);
                    ValidationSupport.checkList(strength.referenceStrength, "referenceStrength", ReferenceStrength.class);
                    ValidationSupport.requireValueOrChildren(strength);
                }

                protected Builder from(Strength strength) {
                    super.from(strength);
                    presentation = strength.presentation;
                    presentationLowLimit = strength.presentationLowLimit;
                    concentration = strength.concentration;
                    concentrationLowLimit = strength.concentrationLowLimit;
                    measurementPoint = strength.measurementPoint;
                    country.addAll(strength.country);
                    referenceStrength.addAll(strength.referenceStrength);
                    return this;
                }
            }

            /**
             * Strength expressed in terms of a reference substance.
             */
            public static class ReferenceStrength extends BackboneElement {
                @Summary
                private final CodeableConcept substance;
                @Summary
                @Required
                private final Ratio strength;
                @Summary
                private final Ratio strengthLowLimit;
                @Summary
                private final String measurementPoint;
                @Summary
                private final List<CodeableConcept> country;

                private ReferenceStrength(Builder builder) {
                    super(builder);
                    substance = builder.substance;
                    strength = builder.strength;
                    strengthLowLimit = builder.strengthLowLimit;
                    measurementPoint = builder.measurementPoint;
                    country = Collections.unmodifiableList(builder.country);
                }

                /**
                 * Relevant reference substance.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that may be null.
                 */
                public CodeableConcept getSubstance() {
                    return substance;
                }

                /**
                 * Strength expressed in terms of a reference substance.
                 * 
                 * @return
                 *     An immutable object of type {@link Ratio} that is non-null.
                 */
                public Ratio getStrength() {
                    return strength;
                }

                /**
                 * Strength expressed in terms of a reference substance.
                 * 
                 * @return
                 *     An immutable object of type {@link Ratio} that may be null.
                 */
                public Ratio getStrengthLowLimit() {
                    return strengthLowLimit;
                }

                /**
                 * For when strength is measured at a particular point or distance.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getMeasurementPoint() {
                    return measurementPoint;
                }

                /**
                 * The country or countries for which the strength range applies.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
                 */
                public List<CodeableConcept> getCountry() {
                    return country;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (substance != null) || 
                        (strength != null) || 
                        (strengthLowLimit != null) || 
                        (measurementPoint != null) || 
                        !country.isEmpty();
                }

                @Override
                public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                    if (visitor.preVisit(this)) {
                        visitor.visitStart(elementName, elementIndex, this);
                        if (visitor.visit(elementName, elementIndex, this)) {
                            // visit children
                            accept(id, "id", visitor);
                            accept(extension, "extension", visitor, Extension.class);
                            accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                            accept(substance, "substance", visitor);
                            accept(strength, "strength", visitor);
                            accept(strengthLowLimit, "strengthLowLimit", visitor);
                            accept(measurementPoint, "measurementPoint", visitor);
                            accept(country, "country", visitor, CodeableConcept.class);
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
                    ReferenceStrength other = (ReferenceStrength) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(substance, other.substance) && 
                        Objects.equals(strength, other.strength) && 
                        Objects.equals(strengthLowLimit, other.strengthLowLimit) && 
                        Objects.equals(measurementPoint, other.measurementPoint) && 
                        Objects.equals(country, other.country);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            substance, 
                            strength, 
                            strengthLowLimit, 
                            measurementPoint, 
                            country);
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

                public static class Builder extends BackboneElement.Builder {
                    private CodeableConcept substance;
                    private Ratio strength;
                    private Ratio strengthLowLimit;
                    private String measurementPoint;
                    private List<CodeableConcept> country = new ArrayList<>();

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
                     * May be used to represent additional information that is not part of the basic definition of the element and that 
                     * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                     * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                     * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                     * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                     * extension. Applications processing a resource are required to check for modifier extensions.
                     * 
                     * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                     * change the meaning of modifierExtension itself).
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    @Override
                    public Builder modifierExtension(Extension... modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    /**
                     * May be used to represent additional information that is not part of the basic definition of the element and that 
                     * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                     * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                     * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                     * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                     * extension. Applications processing a resource are required to check for modifier extensions.
                     * 
                     * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                     * change the meaning of modifierExtension itself).
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    @Override
                    public Builder modifierExtension(Collection<Extension> modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    /**
                     * Relevant reference substance.
                     * 
                     * @param substance
                     *     Relevant reference substance
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder substance(CodeableConcept substance) {
                        this.substance = substance;
                        return this;
                    }

                    /**
                     * Strength expressed in terms of a reference substance.
                     * 
                     * <p>This element is required.
                     * 
                     * @param strength
                     *     Strength expressed in terms of a reference substance
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder strength(Ratio strength) {
                        this.strength = strength;
                        return this;
                    }

                    /**
                     * Strength expressed in terms of a reference substance.
                     * 
                     * @param strengthLowLimit
                     *     Strength expressed in terms of a reference substance
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder strengthLowLimit(Ratio strengthLowLimit) {
                        this.strengthLowLimit = strengthLowLimit;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code measurementPoint}.
                     * 
                     * @param measurementPoint
                     *     For when strength is measured at a particular point or distance
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #measurementPoint(com.ibm.fhir.model.type.String)
                     */
                    public Builder measurementPoint(java.lang.String measurementPoint) {
                        this.measurementPoint = (measurementPoint == null) ? null : String.of(measurementPoint);
                        return this;
                    }

                    /**
                     * For when strength is measured at a particular point or distance.
                     * 
                     * @param measurementPoint
                     *     For when strength is measured at a particular point or distance
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder measurementPoint(String measurementPoint) {
                        this.measurementPoint = measurementPoint;
                        return this;
                    }

                    /**
                     * The country or countries for which the strength range applies.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param country
                     *     The country or countries for which the strength range applies
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder country(CodeableConcept... country) {
                        for (CodeableConcept value : country) {
                            this.country.add(value);
                        }
                        return this;
                    }

                    /**
                     * The country or countries for which the strength range applies.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param country
                     *     The country or countries for which the strength range applies
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder country(Collection<CodeableConcept> country) {
                        this.country = new ArrayList<>(country);
                        return this;
                    }

                    /**
                     * Build the {@link ReferenceStrength}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>strength</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link ReferenceStrength}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid ReferenceStrength per the base specification
                     */
                    @Override
                    public ReferenceStrength build() {
                        ReferenceStrength referenceStrength = new ReferenceStrength(this);
                        if (validating) {
                            validate(referenceStrength);
                        }
                        return referenceStrength;
                    }

                    protected void validate(ReferenceStrength referenceStrength) {
                        super.validate(referenceStrength);
                        ValidationSupport.requireNonNull(referenceStrength.strength, "strength");
                        ValidationSupport.checkList(referenceStrength.country, "country", CodeableConcept.class);
                        ValidationSupport.requireValueOrChildren(referenceStrength);
                    }

                    protected Builder from(ReferenceStrength referenceStrength) {
                        super.from(referenceStrength);
                        substance = referenceStrength.substance;
                        strength = referenceStrength.strength;
                        strengthLowLimit = referenceStrength.strengthLowLimit;
                        measurementPoint = referenceStrength.measurementPoint;
                        country.addAll(referenceStrength.country);
                        return this;
                    }
                }
            }
        }
    }

    /**
     * The ingredient substance.
     */
    public static class Substance extends BackboneElement {
        @Summary
        @Required
        private final CodeableConcept code;
        @Summary
        private final List<MedicinalProductIngredient.SpecifiedSubstance.Strength> strength;

        private Substance(Builder builder) {
            super(builder);
            code = builder.code;
            strength = Collections.unmodifiableList(builder.strength);
        }

        /**
         * The ingredient substance.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Strength} that may be empty.
         */
        public List<MedicinalProductIngredient.SpecifiedSubstance.Strength> getStrength() {
            return strength;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                !strength.isEmpty();
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
                    accept(strength, "strength", visitor, MedicinalProductIngredient.SpecifiedSubstance.Strength.class);
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
            Substance other = (Substance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(strength, other.strength);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    strength);
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

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept code;
            private List<MedicinalProductIngredient.SpecifiedSubstance.Strength> strength = new ArrayList<>();

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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * The ingredient substance.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     The ingredient substance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param strength
             *     Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder strength(MedicinalProductIngredient.SpecifiedSubstance.Strength... strength) {
                for (MedicinalProductIngredient.SpecifiedSubstance.Strength value : strength) {
                    this.strength.add(value);
                }
                return this;
            }

            /**
             * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param strength
             *     Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder strength(Collection<MedicinalProductIngredient.SpecifiedSubstance.Strength> strength) {
                this.strength = new ArrayList<>(strength);
                return this;
            }

            /**
             * Build the {@link Substance}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Substance}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Substance per the base specification
             */
            @Override
            public Substance build() {
                Substance substance = new Substance(this);
                if (validating) {
                    validate(substance);
                }
                return substance;
            }

            protected void validate(Substance substance) {
                super.validate(substance);
                ValidationSupport.requireNonNull(substance.code, "code");
                ValidationSupport.checkList(substance.strength, "strength", MedicinalProductIngredient.SpecifiedSubstance.Strength.class);
                ValidationSupport.requireValueOrChildren(substance);
            }

            protected Builder from(Substance substance) {
                super.from(substance);
                code = substance.code;
                strength.addAll(substance.strength);
                return this;
            }
        }
    }
}
