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
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.ProdCharacteristic;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The manufactured item as contained in the packaged medicinal product.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicinalProductManufactured extends DomainResource {
    @Summary
    @Required
    private final CodeableConcept manufacturedDoseForm;
    @Summary
    private final CodeableConcept unitOfPresentation;
    @Summary
    @Required
    private final Quantity quantity;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final List<Reference> manufacturer;
    @Summary
    @ReferenceTarget({ "MedicinalProductIngredient" })
    private final List<Reference> ingredient;
    @Summary
    private final ProdCharacteristic physicalCharacteristics;
    @Summary
    private final List<CodeableConcept> otherCharacteristics;

    private MedicinalProductManufactured(Builder builder) {
        super(builder);
        manufacturedDoseForm = builder.manufacturedDoseForm;
        unitOfPresentation = builder.unitOfPresentation;
        quantity = builder.quantity;
        manufacturer = Collections.unmodifiableList(builder.manufacturer);
        ingredient = Collections.unmodifiableList(builder.ingredient);
        physicalCharacteristics = builder.physicalCharacteristics;
        otherCharacteristics = Collections.unmodifiableList(builder.otherCharacteristics);
    }

    /**
     * Dose form as manufactured and before any transformation into the pharmaceutical product.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getManufacturedDoseForm() {
        return manufacturedDoseForm;
    }

    /**
     * The “real world” units in which the quantity of the manufactured item is described.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getUnitOfPresentation() {
        return unitOfPresentation;
    }

    /**
     * The quantity or "count number" of the manufactured item.
     * 
     * @return
     *     An immutable object of type {@link Quantity} that is non-null.
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getManufacturer() {
        return manufacturer;
    }

    /**
     * Ingredient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getIngredient() {
        return ingredient;
    }

    /**
     * Dimensions, color etc.
     * 
     * @return
     *     An immutable object of type {@link ProdCharacteristic} that may be null.
     */
    public ProdCharacteristic getPhysicalCharacteristics() {
        return physicalCharacteristics;
    }

    /**
     * Other codeable characteristics.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getOtherCharacteristics() {
        return otherCharacteristics;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (manufacturedDoseForm != null) || 
            (unitOfPresentation != null) || 
            (quantity != null) || 
            !manufacturer.isEmpty() || 
            !ingredient.isEmpty() || 
            (physicalCharacteristics != null) || 
            !otherCharacteristics.isEmpty();
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
                accept(manufacturedDoseForm, "manufacturedDoseForm", visitor);
                accept(unitOfPresentation, "unitOfPresentation", visitor);
                accept(quantity, "quantity", visitor);
                accept(manufacturer, "manufacturer", visitor, Reference.class);
                accept(ingredient, "ingredient", visitor, Reference.class);
                accept(physicalCharacteristics, "physicalCharacteristics", visitor);
                accept(otherCharacteristics, "otherCharacteristics", visitor, CodeableConcept.class);
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
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private CodeableConcept manufacturedDoseForm;
        private CodeableConcept unitOfPresentation;
        private Quantity quantity;
        private List<Reference> manufacturer = new ArrayList<>();
        private List<Reference> ingredient = new ArrayList<>();
        private ProdCharacteristic physicalCharacteristics;
        private List<CodeableConcept> otherCharacteristics = new ArrayList<>();

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
         * Dose form as manufactured and before any transformation into the pharmaceutical product.
         * 
         * <p>This element is required.
         * 
         * @param manufacturedDoseForm
         *     Dose form as manufactured and before any transformation into the pharmaceutical product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufacturedDoseForm(CodeableConcept manufacturedDoseForm) {
            this.manufacturedDoseForm = manufacturedDoseForm;
            return this;
        }

        /**
         * The “real world” units in which the quantity of the manufactured item is described.
         * 
         * @param unitOfPresentation
         *     The “real world” units in which the quantity of the manufactured item is described
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder unitOfPresentation(CodeableConcept unitOfPresentation) {
            this.unitOfPresentation = unitOfPresentation;
            return this;
        }

        /**
         * The quantity or "count number" of the manufactured item.
         * 
         * <p>This element is required.
         * 
         * @param quantity
         *     The quantity or "count number" of the manufactured item
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quantity(Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues).
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
         *     Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues)
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
         * Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues).
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
         *     Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues)
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
         * Ingredient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductIngredient}</li>
         * </ul>
         * 
         * @param ingredient
         *     Ingredient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder ingredient(Reference... ingredient) {
            for (Reference value : ingredient) {
                this.ingredient.add(value);
            }
            return this;
        }

        /**
         * Ingredient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductIngredient}</li>
         * </ul>
         * 
         * @param ingredient
         *     Ingredient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder ingredient(Collection<Reference> ingredient) {
            this.ingredient = new ArrayList<>(ingredient);
            return this;
        }

        /**
         * Dimensions, color etc.
         * 
         * @param physicalCharacteristics
         *     Dimensions, color etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder physicalCharacteristics(ProdCharacteristic physicalCharacteristics) {
            this.physicalCharacteristics = physicalCharacteristics;
            return this;
        }

        /**
         * Other codeable characteristics.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param otherCharacteristics
         *     Other codeable characteristics
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder otherCharacteristics(CodeableConcept... otherCharacteristics) {
            for (CodeableConcept value : otherCharacteristics) {
                this.otherCharacteristics.add(value);
            }
            return this;
        }

        /**
         * Other codeable characteristics.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param otherCharacteristics
         *     Other codeable characteristics
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder otherCharacteristics(Collection<CodeableConcept> otherCharacteristics) {
            this.otherCharacteristics = new ArrayList<>(otherCharacteristics);
            return this;
        }

        /**
         * Build the {@link MedicinalProductManufactured}
         * 
         * <p>Required elements:
         * <ul>
         * <li>manufacturedDoseForm</li>
         * <li>quantity</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MedicinalProductManufactured}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicinalProductManufactured per the base specification
         */
        @Override
        public MedicinalProductManufactured build() {
            MedicinalProductManufactured medicinalProductManufactured = new MedicinalProductManufactured(this);
            if (validating) {
                validate(medicinalProductManufactured);
            }
            return medicinalProductManufactured;
        }

        protected void validate(MedicinalProductManufactured medicinalProductManufactured) {
            super.validate(medicinalProductManufactured);
            ValidationSupport.requireNonNull(medicinalProductManufactured.manufacturedDoseForm, "manufacturedDoseForm");
            ValidationSupport.requireNonNull(medicinalProductManufactured.quantity, "quantity");
            ValidationSupport.checkList(medicinalProductManufactured.manufacturer, "manufacturer", Reference.class);
            ValidationSupport.checkList(medicinalProductManufactured.ingredient, "ingredient", Reference.class);
            ValidationSupport.checkList(medicinalProductManufactured.otherCharacteristics, "otherCharacteristics", CodeableConcept.class);
            ValidationSupport.checkReferenceType(medicinalProductManufactured.manufacturer, "manufacturer", "Organization");
            ValidationSupport.checkReferenceType(medicinalProductManufactured.ingredient, "ingredient", "MedicinalProductIngredient");
        }

        protected Builder from(MedicinalProductManufactured medicinalProductManufactured) {
            super.from(medicinalProductManufactured);
            manufacturedDoseForm = medicinalProductManufactured.manufacturedDoseForm;
            unitOfPresentation = medicinalProductManufactured.unitOfPresentation;
            quantity = medicinalProductManufactured.quantity;
            manufacturer.addAll(medicinalProductManufactured.manufacturer);
            ingredient.addAll(medicinalProductManufactured.ingredient);
            physicalCharacteristics = medicinalProductManufactured.physicalCharacteristics;
            otherCharacteristics.addAll(medicinalProductManufactured.otherCharacteristics);
            return this;
        }
    }
}
