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
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.ProdCharacteristic;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The manufactured item as contained in the packaged medicinal product.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MedicinalProductManufactured extends DomainResource {
    private final CodeableConcept manufacturedDoseForm;
    private final CodeableConcept unitOfPresentation;
    private final Quantity quantity;
    private final List<Reference> manufacturer;
    private final List<Reference> ingredient;
    private final ProdCharacteristic physicalCharacteristics;
    private final List<CodeableConcept> otherCharacteristics;

    private volatile int hashCode;

    private MedicinalProductManufactured(Builder builder) {
        super(builder);
        manufacturedDoseForm = ValidationSupport.requireNonNull(builder.manufacturedDoseForm, "manufacturedDoseForm");
        unitOfPresentation = builder.unitOfPresentation;
        quantity = ValidationSupport.requireNonNull(builder.quantity, "quantity");
        manufacturer = Collections.unmodifiableList(builder.manufacturer);
        ingredient = Collections.unmodifiableList(builder.ingredient);
        physicalCharacteristics = builder.physicalCharacteristics;
        otherCharacteristics = Collections.unmodifiableList(builder.otherCharacteristics);
    }

    /**
     * <p>
     * Dose form as manufactured and before any transformation into the pharmaceutical product.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getManufacturedDoseForm() {
        return manufacturedDoseForm;
    }

    /**
     * <p>
     * The “real world” units in which the quantity of the manufactured item is described.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getUnitOfPresentation() {
        return unitOfPresentation;
    }

    /**
     * <p>
     * The quantity or "count number" of the manufactured item.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * <p>
     * Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues).
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
     * Ingredient.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getIngredient() {
        return ingredient;
    }

    /**
     * <p>
     * Dimensions, color etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ProdCharacteristic}.
     */
    public ProdCharacteristic getPhysicalCharacteristics() {
        return physicalCharacteristics;
    }

    /**
     * <p>
     * Other codeable characteristics.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getOtherCharacteristics() {
        return otherCharacteristics;
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
                accept(manufacturedDoseForm, "manufacturedDoseForm", visitor);
                accept(unitOfPresentation, "unitOfPresentation", visitor);
                accept(quantity, "quantity", visitor);
                accept(manufacturer, "manufacturer", visitor, Reference.class);
                accept(ingredient, "ingredient", visitor, Reference.class);
                accept(physicalCharacteristics, "physicalCharacteristics", visitor);
                accept(otherCharacteristics, "otherCharacteristics", visitor, CodeableConcept.class);
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
        MedicinalProductManufactured other = (MedicinalProductManufactured) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(manufacturedDoseForm, other.manufacturedDoseForm) && 
            Objects.equals(unitOfPresentation, other.unitOfPresentation) && 
            Objects.equals(quantity, other.quantity) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(ingredient, other.ingredient) && 
            Objects.equals(physicalCharacteristics, other.physicalCharacteristics) && 
            Objects.equals(otherCharacteristics, other.otherCharacteristics);
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
                manufacturedDoseForm, 
                unitOfPresentation, 
                quantity, 
                manufacturer, 
                ingredient, 
                physicalCharacteristics, 
                otherCharacteristics);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(manufacturedDoseForm, quantity).from(this);
    }

    public Builder toBuilder(CodeableConcept manufacturedDoseForm, Quantity quantity) {
        return new Builder(manufacturedDoseForm, quantity).from(this);
    }

    public static Builder builder(CodeableConcept manufacturedDoseForm, Quantity quantity) {
        return new Builder(manufacturedDoseForm, quantity);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final CodeableConcept manufacturedDoseForm;
        private final Quantity quantity;

        // optional
        private CodeableConcept unitOfPresentation;
        private List<Reference> manufacturer = new ArrayList<>();
        private List<Reference> ingredient = new ArrayList<>();
        private ProdCharacteristic physicalCharacteristics;
        private List<CodeableConcept> otherCharacteristics = new ArrayList<>();

        private Builder(CodeableConcept manufacturedDoseForm, Quantity quantity) {
            super();
            this.manufacturedDoseForm = manufacturedDoseForm;
            this.quantity = quantity;
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
         * The “real world” units in which the quantity of the manufactured item is described.
         * </p>
         * 
         * @param unitOfPresentation
         *     The “real world” units in which the quantity of the manufactured item is described
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder unitOfPresentation(CodeableConcept unitOfPresentation) {
            this.unitOfPresentation = unitOfPresentation;
            return this;
        }

        /**
         * <p>
         * Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues).
         * </p>
         * 
         * @param manufacturer
         *     Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues)
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
         * Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues).
         * </p>
         * 
         * @param manufacturer
         *     Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues)
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
         * Ingredient.
         * </p>
         * 
         * @param ingredient
         *     Ingredient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder ingredient(Reference... ingredient) {
            for (Reference value : ingredient) {
                this.ingredient.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Ingredient.
         * </p>
         * 
         * @param ingredient
         *     Ingredient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder ingredient(Collection<Reference> ingredient) {
            this.ingredient.addAll(ingredient);
            return this;
        }

        /**
         * <p>
         * Dimensions, color etc.
         * </p>
         * 
         * @param physicalCharacteristics
         *     Dimensions, color etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder physicalCharacteristics(ProdCharacteristic physicalCharacteristics) {
            this.physicalCharacteristics = physicalCharacteristics;
            return this;
        }

        /**
         * <p>
         * Other codeable characteristics.
         * </p>
         * 
         * @param otherCharacteristics
         *     Other codeable characteristics
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder otherCharacteristics(CodeableConcept... otherCharacteristics) {
            for (CodeableConcept value : otherCharacteristics) {
                this.otherCharacteristics.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Other codeable characteristics.
         * </p>
         * 
         * @param otherCharacteristics
         *     Other codeable characteristics
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder otherCharacteristics(Collection<CodeableConcept> otherCharacteristics) {
            this.otherCharacteristics.addAll(otherCharacteristics);
            return this;
        }

        @Override
        public MedicinalProductManufactured build() {
            return new MedicinalProductManufactured(this);
        }

        private Builder from(MedicinalProductManufactured medicinalProductManufactured) {
            id = medicinalProductManufactured.id;
            meta = medicinalProductManufactured.meta;
            implicitRules = medicinalProductManufactured.implicitRules;
            language = medicinalProductManufactured.language;
            text = medicinalProductManufactured.text;
            contained.addAll(medicinalProductManufactured.contained);
            extension.addAll(medicinalProductManufactured.extension);
            modifierExtension.addAll(medicinalProductManufactured.modifierExtension);
            unitOfPresentation = medicinalProductManufactured.unitOfPresentation;
            manufacturer.addAll(medicinalProductManufactured.manufacturer);
            ingredient.addAll(medicinalProductManufactured.ingredient);
            physicalCharacteristics = medicinalProductManufactured.physicalCharacteristics;
            otherCharacteristics.addAll(medicinalProductManufactured.otherCharacteristics);
            return this;
        }
    }
}
