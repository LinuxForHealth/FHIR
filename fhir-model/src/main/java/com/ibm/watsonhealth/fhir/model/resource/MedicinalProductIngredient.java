/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * An ingredient of a manufactured item or pharmaceutical product.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MedicinalProductIngredient extends DomainResource {
    private final Identifier identifier;
    private final CodeableConcept role;
    private final Boolean allergenicIndicator;
    private final List<Reference> manufacturer;
    private final List<SpecifiedSubstance> specifiedSubstance;
    private final Substance substance;

    private MedicinalProductIngredient(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        role = ValidationSupport.requireNonNull(builder.role, "role");
        allergenicIndicator = builder.allergenicIndicator;
        manufacturer = Collections.unmodifiableList(builder.manufacturer);
        specifiedSubstance = Collections.unmodifiableList(builder.specifiedSubstance);
        substance = builder.substance;
    }

    /**
     * <p>
     * The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct 
     * URL reference to the resource itself is not appropriate.
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
     * Ingredient role e.g. Active ingredient, excipient.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getRole() {
        return role;
    }

    /**
     * <p>
     * If the ingredient is a known or suspected allergen.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getAllergenicIndicator() {
        return allergenicIndicator;
    }

    /**
     * <p>
     * Manufacturer of this Ingredient.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getManufacturer() {
        return manufacturer;
    }

    /**
     * <p>
     * A specified substance that comprises this ingredient.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link SpecifiedSubstance}.
     */
    public List<SpecifiedSubstance> getSpecifiedSubstance() {
        return specifiedSubstance;
    }

    /**
     * <p>
     * The ingredient substance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Substance}.
     */
    public Substance getSubstance() {
        return substance;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        return new Builder(role).from(this);
    }

    public Builder toBuilder(CodeableConcept role) {
        return new Builder(role).from(this);
    }

    public static Builder builder(CodeableConcept role) {
        return new Builder(role);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final CodeableConcept role;

        // optional
        private Identifier identifier;
        private Boolean allergenicIndicator;
        private List<Reference> manufacturer = new ArrayList<>();
        private List<SpecifiedSubstance> specifiedSubstance = new ArrayList<>();
        private Substance substance;

        private Builder(CodeableConcept role) {
            super();
            this.role = role;
        }

        /**
         * <p>
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * </p>
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder id(Id id) {
            return (Builder) super.id(id);
        }

        /**
         * <p>
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * </p>
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * <p>
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * </p>
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * <p>
         * The base language in which the resource is written.
         * </p>
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * <p>
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * </p>
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct 
         * URL reference to the resource itself is not appropriate.
         * </p>
         * 
         * @param identifier
         *     Identifier for the ingredient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * <p>
         * If the ingredient is a known or suspected allergen.
         * </p>
         * 
         * @param allergenicIndicator
         *     If the ingredient is a known or suspected allergen
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder allergenicIndicator(Boolean allergenicIndicator) {
            this.allergenicIndicator = allergenicIndicator;
            return this;
        }

        /**
         * <p>
         * Manufacturer of this Ingredient.
         * </p>
         * 
         * @param manufacturer
         *     Manufacturer of this Ingredient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder manufacturer(Reference... manufacturer) {
            for (Reference value : manufacturer) {
                this.manufacturer.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Manufacturer of this Ingredient.
         * </p>
         * 
         * @param manufacturer
         *     Manufacturer of this Ingredient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder manufacturer(Collection<Reference> manufacturer) {
            this.manufacturer.addAll(manufacturer);
            return this;
        }

        /**
         * <p>
         * A specified substance that comprises this ingredient.
         * </p>
         * 
         * @param specifiedSubstance
         *     A specified substance that comprises this ingredient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder specifiedSubstance(SpecifiedSubstance... specifiedSubstance) {
            for (SpecifiedSubstance value : specifiedSubstance) {
                this.specifiedSubstance.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A specified substance that comprises this ingredient.
         * </p>
         * 
         * @param specifiedSubstance
         *     A specified substance that comprises this ingredient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder specifiedSubstance(Collection<SpecifiedSubstance> specifiedSubstance) {
            this.specifiedSubstance.addAll(specifiedSubstance);
            return this;
        }

        /**
         * <p>
         * The ingredient substance.
         * </p>
         * 
         * @param substance
         *     The ingredient substance
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder substance(Substance substance) {
            this.substance = substance;
            return this;
        }

        @Override
        public MedicinalProductIngredient build() {
            return new MedicinalProductIngredient(this);
        }

        private Builder from(MedicinalProductIngredient medicinalProductIngredient) {
            id = medicinalProductIngredient.id;
            meta = medicinalProductIngredient.meta;
            implicitRules = medicinalProductIngredient.implicitRules;
            language = medicinalProductIngredient.language;
            text = medicinalProductIngredient.text;
            contained.addAll(medicinalProductIngredient.contained);
            extension.addAll(medicinalProductIngredient.extension);
            modifierExtension.addAll(medicinalProductIngredient.modifierExtension);
            identifier = medicinalProductIngredient.identifier;
            allergenicIndicator = medicinalProductIngredient.allergenicIndicator;
            manufacturer.addAll(medicinalProductIngredient.manufacturer);
            specifiedSubstance.addAll(medicinalProductIngredient.specifiedSubstance);
            substance = medicinalProductIngredient.substance;
            return this;
        }
    }

    /**
     * <p>
     * A specified substance that comprises this ingredient.
     * </p>
     */
    public static class SpecifiedSubstance extends BackboneElement {
        private final CodeableConcept code;
        private final CodeableConcept group;
        private final CodeableConcept confidentiality;
        private final List<Strength> strength;

        private SpecifiedSubstance(Builder builder) {
            super(builder);
            code = ValidationSupport.requireNonNull(builder.code, "code");
            group = ValidationSupport.requireNonNull(builder.group, "group");
            confidentiality = builder.confidentiality;
            strength = Collections.unmodifiableList(builder.strength);
        }

        /**
         * <p>
         * The specified substance.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * <p>
         * The group of specified substance, e.g. group 1 to 4.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getGroup() {
            return group;
        }

        /**
         * <p>
         * Confidentiality level of the specified substance as the ingredient.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getConfidentiality() {
            return confidentiality;
        }

        /**
         * <p>
         * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Strength}.
         */
        public List<Strength> getStrength() {
            return strength;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
                    accept(group, "group", visitor);
                    accept(confidentiality, "confidentiality", visitor);
                    accept(strength, "strength", visitor, Strength.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(code, group).from(this);
        }

        public Builder toBuilder(CodeableConcept code, CodeableConcept group) {
            return new Builder(code, group).from(this);
        }

        public static Builder builder(CodeableConcept code, CodeableConcept group) {
            return new Builder(code, group);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept code;
            private final CodeableConcept group;

            // optional
            private CodeableConcept confidentiality;
            private List<Strength> strength = new ArrayList<>();

            private Builder(CodeableConcept code, CodeableConcept group) {
                super();
                this.code = code;
                this.group = group;
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Confidentiality level of the specified substance as the ingredient.
             * </p>
             * 
             * @param confidentiality
             *     Confidentiality level of the specified substance as the ingredient
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder confidentiality(CodeableConcept confidentiality) {
                this.confidentiality = confidentiality;
                return this;
            }

            /**
             * <p>
             * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
             * </p>
             * 
             * @param strength
             *     Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder strength(Strength... strength) {
                for (Strength value : strength) {
                    this.strength.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
             * </p>
             * 
             * @param strength
             *     Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder strength(Collection<Strength> strength) {
                this.strength.addAll(strength);
                return this;
            }

            @Override
            public SpecifiedSubstance build() {
                return new SpecifiedSubstance(this);
            }

            private Builder from(SpecifiedSubstance specifiedSubstance) {
                id = specifiedSubstance.id;
                extension.addAll(specifiedSubstance.extension);
                modifierExtension.addAll(specifiedSubstance.modifierExtension);
                confidentiality = specifiedSubstance.confidentiality;
                strength.addAll(specifiedSubstance.strength);
                return this;
            }
        }

        /**
         * <p>
         * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
         * </p>
         */
        public static class Strength extends BackboneElement {
            private final Ratio presentation;
            private final Ratio presentationLowLimit;
            private final Ratio concentration;
            private final Ratio concentrationLowLimit;
            private final String measurementPoint;
            private final List<CodeableConcept> country;
            private final List<ReferenceStrength> referenceStrength;

            private Strength(Builder builder) {
                super(builder);
                presentation = ValidationSupport.requireNonNull(builder.presentation, "presentation");
                presentationLowLimit = builder.presentationLowLimit;
                concentration = builder.concentration;
                concentrationLowLimit = builder.concentrationLowLimit;
                measurementPoint = builder.measurementPoint;
                country = Collections.unmodifiableList(builder.country);
                referenceStrength = Collections.unmodifiableList(builder.referenceStrength);
            }

            /**
             * <p>
             * The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product 
             * or manufactured item.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Ratio}.
             */
            public Ratio getPresentation() {
                return presentation;
            }

            /**
             * <p>
             * A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, 
             * this is the lower limit, with the presentation attribute becoming the upper limit.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Ratio}.
             */
            public Ratio getPresentationLowLimit() {
                return presentationLowLimit;
            }

            /**
             * <p>
             * The strength per unitary volume (or mass).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Ratio}.
             */
            public Ratio getConcentration() {
                return concentration;
            }

            /**
             * <p>
             * A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute 
             * then becomes the upper limit.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Ratio}.
             */
            public Ratio getConcentrationLowLimit() {
                return concentrationLowLimit;
            }

            /**
             * <p>
             * For when strength is measured at a particular point or distance.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getMeasurementPoint() {
                return measurementPoint;
            }

            /**
             * <p>
             * The country or countries for which the strength range applies.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getCountry() {
                return country;
            }

            /**
             * <p>
             * Strength expressed in terms of a reference substance.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link ReferenceStrength}.
             */
            public List<ReferenceStrength> getReferenceStrength() {
                return referenceStrength;
            }

            @Override
            public void accept(java.lang.String elementName, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, this);
                    if (visitor.visit(elementName, this)) {
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
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return new Builder(presentation).from(this);
            }

            public Builder toBuilder(Ratio presentation) {
                return new Builder(presentation).from(this);
            }

            public static Builder builder(Ratio presentation) {
                return new Builder(presentation);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Ratio presentation;

                // optional
                private Ratio presentationLowLimit;
                private Ratio concentration;
                private Ratio concentrationLowLimit;
                private String measurementPoint;
                private List<CodeableConcept> country = new ArrayList<>();
                private List<ReferenceStrength> referenceStrength = new ArrayList<>();

                private Builder(Ratio presentation) {
                    super();
                    this.presentation = presentation;
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
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * </p>
                 * <p>
                 * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * </p>
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                @Override
                public Builder modifierExtension(Extension... modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * <p>
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * </p>
                 * <p>
                 * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * </p>
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                @Override
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * <p>
                 * A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, 
                 * this is the lower limit, with the presentation attribute becoming the upper limit.
                 * </p>
                 * 
                 * @param presentationLowLimit
                 *     A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, 
                 *     this is the lower limit, with the presentation attribute becoming the upper limit
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder presentationLowLimit(Ratio presentationLowLimit) {
                    this.presentationLowLimit = presentationLowLimit;
                    return this;
                }

                /**
                 * <p>
                 * The strength per unitary volume (or mass).
                 * </p>
                 * 
                 * @param concentration
                 *     The strength per unitary volume (or mass)
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder concentration(Ratio concentration) {
                    this.concentration = concentration;
                    return this;
                }

                /**
                 * <p>
                 * A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute 
                 * then becomes the upper limit.
                 * </p>
                 * 
                 * @param concentrationLowLimit
                 *     A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute 
                 *     then becomes the upper limit
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder concentrationLowLimit(Ratio concentrationLowLimit) {
                    this.concentrationLowLimit = concentrationLowLimit;
                    return this;
                }

                /**
                 * <p>
                 * For when strength is measured at a particular point or distance.
                 * </p>
                 * 
                 * @param measurementPoint
                 *     For when strength is measured at a particular point or distance
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder measurementPoint(String measurementPoint) {
                    this.measurementPoint = measurementPoint;
                    return this;
                }

                /**
                 * <p>
                 * The country or countries for which the strength range applies.
                 * </p>
                 * 
                 * @param country
                 *     The country or countries for which the strength range applies
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder country(CodeableConcept... country) {
                    for (CodeableConcept value : country) {
                        this.country.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The country or countries for which the strength range applies.
                 * </p>
                 * 
                 * @param country
                 *     The country or countries for which the strength range applies
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder country(Collection<CodeableConcept> country) {
                    this.country.addAll(country);
                    return this;
                }

                /**
                 * <p>
                 * Strength expressed in terms of a reference substance.
                 * </p>
                 * 
                 * @param referenceStrength
                 *     Strength expressed in terms of a reference substance
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder referenceStrength(ReferenceStrength... referenceStrength) {
                    for (ReferenceStrength value : referenceStrength) {
                        this.referenceStrength.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Strength expressed in terms of a reference substance.
                 * </p>
                 * 
                 * @param referenceStrength
                 *     Strength expressed in terms of a reference substance
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder referenceStrength(Collection<ReferenceStrength> referenceStrength) {
                    this.referenceStrength.addAll(referenceStrength);
                    return this;
                }

                @Override
                public Strength build() {
                    return new Strength(this);
                }

                private Builder from(Strength strength) {
                    id = strength.id;
                    extension.addAll(strength.extension);
                    modifierExtension.addAll(strength.modifierExtension);
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
             * <p>
             * Strength expressed in terms of a reference substance.
             * </p>
             */
            public static class ReferenceStrength extends BackboneElement {
                private final CodeableConcept substance;
                private final Ratio strength;
                private final Ratio strengthLowLimit;
                private final String measurementPoint;
                private final List<CodeableConcept> country;

                private ReferenceStrength(Builder builder) {
                    super(builder);
                    substance = builder.substance;
                    strength = ValidationSupport.requireNonNull(builder.strength, "strength");
                    strengthLowLimit = builder.strengthLowLimit;
                    measurementPoint = builder.measurementPoint;
                    country = Collections.unmodifiableList(builder.country);
                }

                /**
                 * <p>
                 * Relevant reference substance.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getSubstance() {
                    return substance;
                }

                /**
                 * <p>
                 * Strength expressed in terms of a reference substance.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Ratio}.
                 */
                public Ratio getStrength() {
                    return strength;
                }

                /**
                 * <p>
                 * Strength expressed in terms of a reference substance.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Ratio}.
                 */
                public Ratio getStrengthLowLimit() {
                    return strengthLowLimit;
                }

                /**
                 * <p>
                 * For when strength is measured at a particular point or distance.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getMeasurementPoint() {
                    return measurementPoint;
                }

                /**
                 * <p>
                 * The country or countries for which the strength range applies.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link CodeableConcept}.
                 */
                public List<CodeableConcept> getCountry() {
                    return country;
                }

                @Override
                public void accept(java.lang.String elementName, Visitor visitor) {
                    if (visitor.preVisit(this)) {
                        visitor.visitStart(elementName, this);
                        if (visitor.visit(elementName, this)) {
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
                        visitor.visitEnd(elementName, this);
                        visitor.postVisit(this);
                    }
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(strength).from(this);
                }

                public Builder toBuilder(Ratio strength) {
                    return new Builder(strength).from(this);
                }

                public static Builder builder(Ratio strength) {
                    return new Builder(strength);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final Ratio strength;

                    // optional
                    private CodeableConcept substance;
                    private Ratio strengthLowLimit;
                    private String measurementPoint;
                    private List<CodeableConcept> country = new ArrayList<>();

                    private Builder(Ratio strength) {
                        super();
                        this.strength = strength;
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
                     * May be used to represent additional information that is not part of the basic definition of the element and that 
                     * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                     * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                     * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                     * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                     * extension. Applications processing a resource are required to check for modifier extensions.
                     * </p>
                     * <p>
                     * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                     * change the meaning of modifierExtension itself).
                     * </p>
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    @Override
                    public Builder modifierExtension(Extension... modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    /**
                     * <p>
                     * May be used to represent additional information that is not part of the basic definition of the element and that 
                     * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                     * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                     * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                     * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                     * extension. Applications processing a resource are required to check for modifier extensions.
                     * </p>
                     * <p>
                     * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                     * change the meaning of modifierExtension itself).
                     * </p>
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    @Override
                    public Builder modifierExtension(Collection<Extension> modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    /**
                     * <p>
                     * Relevant reference substance.
                     * </p>
                     * 
                     * @param substance
                     *     Relevant reference substance
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder substance(CodeableConcept substance) {
                        this.substance = substance;
                        return this;
                    }

                    /**
                     * <p>
                     * Strength expressed in terms of a reference substance.
                     * </p>
                     * 
                     * @param strengthLowLimit
                     *     Strength expressed in terms of a reference substance
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder strengthLowLimit(Ratio strengthLowLimit) {
                        this.strengthLowLimit = strengthLowLimit;
                        return this;
                    }

                    /**
                     * <p>
                     * For when strength is measured at a particular point or distance.
                     * </p>
                     * 
                     * @param measurementPoint
                     *     For when strength is measured at a particular point or distance
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder measurementPoint(String measurementPoint) {
                        this.measurementPoint = measurementPoint;
                        return this;
                    }

                    /**
                     * <p>
                     * The country or countries for which the strength range applies.
                     * </p>
                     * 
                     * @param country
                     *     The country or countries for which the strength range applies
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder country(CodeableConcept... country) {
                        for (CodeableConcept value : country) {
                            this.country.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * The country or countries for which the strength range applies.
                     * </p>
                     * 
                     * @param country
                     *     The country or countries for which the strength range applies
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder country(Collection<CodeableConcept> country) {
                        this.country.addAll(country);
                        return this;
                    }

                    @Override
                    public ReferenceStrength build() {
                        return new ReferenceStrength(this);
                    }

                    private Builder from(ReferenceStrength referenceStrength) {
                        id = referenceStrength.id;
                        extension.addAll(referenceStrength.extension);
                        modifierExtension.addAll(referenceStrength.modifierExtension);
                        substance = referenceStrength.substance;
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
     * <p>
     * The ingredient substance.
     * </p>
     */
    public static class Substance extends BackboneElement {
        private final CodeableConcept code;
        private final List<MedicinalProductIngredient.SpecifiedSubstance.Strength> strength;

        private Substance(Builder builder) {
            super(builder);
            code = ValidationSupport.requireNonNull(builder.code, "code");
            strength = Collections.unmodifiableList(builder.strength);
        }

        /**
         * <p>
         * The ingredient substance.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * <p>
         * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Strength}.
         */
        public List<MedicinalProductIngredient.SpecifiedSubstance.Strength> getStrength() {
            return strength;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
                    accept(strength, "strength", visitor, MedicinalProductIngredient.SpecifiedSubstance.Strength.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(code).from(this);
        }

        public Builder toBuilder(CodeableConcept code) {
            return new Builder(code).from(this);
        }

        public static Builder builder(CodeableConcept code) {
            return new Builder(code);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept code;

            // optional
            private List<MedicinalProductIngredient.SpecifiedSubstance.Strength> strength = new ArrayList<>();

            private Builder(CodeableConcept code) {
                super();
                this.code = code;
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
             * </p>
             * 
             * @param strength
             *     Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder strength(MedicinalProductIngredient.SpecifiedSubstance.Strength... strength) {
                for (MedicinalProductIngredient.SpecifiedSubstance.Strength value : strength) {
                    this.strength.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
             * </p>
             * 
             * @param strength
             *     Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder strength(Collection<MedicinalProductIngredient.SpecifiedSubstance.Strength> strength) {
                this.strength.addAll(strength);
                return this;
            }

            @Override
            public Substance build() {
                return new Substance(this);
            }

            private Builder from(Substance substance) {
                id = substance.id;
                extension.addAll(substance.extension);
                modifierExtension.addAll(substance.modifierExtension);
                strength.addAll(substance.strength);
                return this;
            }
        }
    }
}
