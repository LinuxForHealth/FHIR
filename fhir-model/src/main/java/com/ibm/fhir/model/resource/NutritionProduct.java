/*
 * (C) Copyright IBM Corp. 2019, 2022
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

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.CodeableReference;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.NutritionProductStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A food or fluid product that is consumed by patients.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class NutritionProduct extends DomainResource {
    @Summary
    @Binding(
        bindingName = "NutritionProductStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes identifying the lifecycle stage of a product.",
        valueSet = "http://hl7.org/fhir/ValueSet/nutritionproduct-status|4.3.0-cibuild"
    )
    @Required
    private final NutritionProductStatus status;
    @Summary
    @Binding(
        bindingName = "NutritionProductCategory",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes identifying classes of nutrition products.",
        valueSet = "http://hl7.org/fhir/ValueSet/nutrition-product-category"
    )
    private final List<CodeableConcept> category;
    @Summary
    @Binding(
        bindingName = "NutritionProductCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes identifying specific types of nutrition products.",
        valueSet = "http://hl7.org/fhir/ValueSet/edible-substance-type"
    )
    private final CodeableConcept code;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final List<Reference> manufacturer;
    @Summary
    private final List<Nutrient> nutrient;
    private final List<Ingredient> ingredient;
    @Binding(
        bindingName = "AllergenClass",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes that identify substances that can be an allergen.",
        valueSet = "http://hl7.org/fhir/ValueSet/allergen-class"
    )
    private final List<CodeableReference> knownAllergen;
    private final List<ProductCharacteristic> productCharacteristic;
    private final Instance instance;
    private final List<Annotation> note;

    private NutritionProduct(Builder builder) {
        super(builder);
        status = builder.status;
        category = Collections.unmodifiableList(builder.category);
        code = builder.code;
        manufacturer = Collections.unmodifiableList(builder.manufacturer);
        nutrient = Collections.unmodifiableList(builder.nutrient);
        ingredient = Collections.unmodifiableList(builder.ingredient);
        knownAllergen = Collections.unmodifiableList(builder.knownAllergen);
        productCharacteristic = Collections.unmodifiableList(builder.productCharacteristic);
        instance = builder.instance;
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * The current state of the product.
     * 
     * @return
     *     An immutable object of type {@link NutritionProductStatus} that is non-null.
     */
    public NutritionProductStatus getStatus() {
        return status;
    }

    /**
     * Nutrition products can have different classifications - according to its nutritional properties, preparation methods, 
     * etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * The code assigned to the product, for example a manufacturer number or other terminology.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * The organisation (manufacturer, representative or legal authorisation holder) that is responsible for the device.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getManufacturer() {
        return manufacturer;
    }

    /**
     * The product's nutritional information expressed by the nutrients.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Nutrient} that may be empty.
     */
    public List<Nutrient> getNutrient() {
        return nutrient;
    }

    /**
     * Ingredients contained in this product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Ingredient} that may be empty.
     */
    public List<Ingredient> getIngredient() {
        return ingredient;
    }

    /**
     * Allergens that are known or suspected to be a part of this nutrition product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableReference} that may be empty.
     */
    public List<CodeableReference> getKnownAllergen() {
        return knownAllergen;
    }

    /**
     * Specifies descriptive properties of the nutrition product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ProductCharacteristic} that may be empty.
     */
    public List<ProductCharacteristic> getProductCharacteristic() {
        return productCharacteristic;
    }

    /**
     * Conveys instance-level information about this product item. One or several physical, countable instances or 
     * occurrences of the product.
     * 
     * @return
     *     An immutable object of type {@link Instance} that may be null.
     */
    public Instance getInstance() {
        return instance;
    }

    /**
     * Comments made about the product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (status != null) || 
            !category.isEmpty() || 
            (code != null) || 
            !manufacturer.isEmpty() || 
            !nutrient.isEmpty() || 
            !ingredient.isEmpty() || 
            !knownAllergen.isEmpty() || 
            !productCharacteristic.isEmpty() || 
            (instance != null) || 
            !note.isEmpty();
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
                accept(status, "status", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(code, "code", visitor);
                accept(manufacturer, "manufacturer", visitor, Reference.class);
                accept(nutrient, "nutrient", visitor, Nutrient.class);
                accept(ingredient, "ingredient", visitor, Ingredient.class);
                accept(knownAllergen, "knownAllergen", visitor, CodeableReference.class);
                accept(productCharacteristic, "productCharacteristic", visitor, ProductCharacteristic.class);
                accept(instance, "instance", visitor);
                accept(note, "note", visitor, Annotation.class);
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
        NutritionProduct other = (NutritionProduct) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(status, other.status) && 
            Objects.equals(category, other.category) && 
            Objects.equals(code, other.code) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(nutrient, other.nutrient) && 
            Objects.equals(ingredient, other.ingredient) && 
            Objects.equals(knownAllergen, other.knownAllergen) && 
            Objects.equals(productCharacteristic, other.productCharacteristic) && 
            Objects.equals(instance, other.instance) && 
            Objects.equals(note, other.note);
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
                status, 
                category, 
                code, 
                manufacturer, 
                nutrient, 
                ingredient, 
                knownAllergen, 
                productCharacteristic, 
                instance, 
                note);
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
        private NutritionProductStatus status;
        private List<CodeableConcept> category = new ArrayList<>();
        private CodeableConcept code;
        private List<Reference> manufacturer = new ArrayList<>();
        private List<Nutrient> nutrient = new ArrayList<>();
        private List<Ingredient> ingredient = new ArrayList<>();
        private List<CodeableReference> knownAllergen = new ArrayList<>();
        private List<ProductCharacteristic> productCharacteristic = new ArrayList<>();
        private Instance instance;
        private List<Annotation> note = new ArrayList<>();

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
         * The current state of the product.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | inactive | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(NutritionProductStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Nutrition products can have different classifications - according to its nutritional properties, preparation methods, 
         * etc.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     A category or class of the nutrition product (halal, kosher, gluten free, vegan, etc)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * Nutrition products can have different classifications - according to its nutritional properties, preparation methods, 
         * etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     A category or class of the nutrition product (halal, kosher, gluten free, vegan, etc)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * The code assigned to the product, for example a manufacturer number or other terminology.
         * 
         * @param code
         *     A code designating a specific type of nutritional product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * The organisation (manufacturer, representative or legal authorisation holder) that is responsible for the device.
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
         *     Manufacturer, representative or officially responsible for the product
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
         * The organisation (manufacturer, representative or legal authorisation holder) that is responsible for the device.
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
         *     Manufacturer, representative or officially responsible for the product
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
         * The product's nutritional information expressed by the nutrients.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param nutrient
         *     The product's nutritional information expressed by the nutrients
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder nutrient(Nutrient... nutrient) {
            for (Nutrient value : nutrient) {
                this.nutrient.add(value);
            }
            return this;
        }

        /**
         * The product's nutritional information expressed by the nutrients.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param nutrient
         *     The product's nutritional information expressed by the nutrients
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder nutrient(Collection<Nutrient> nutrient) {
            this.nutrient = new ArrayList<>(nutrient);
            return this;
        }

        /**
         * Ingredients contained in this product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param ingredient
         *     Ingredients contained in this product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder ingredient(Ingredient... ingredient) {
            for (Ingredient value : ingredient) {
                this.ingredient.add(value);
            }
            return this;
        }

        /**
         * Ingredients contained in this product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param ingredient
         *     Ingredients contained in this product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder ingredient(Collection<Ingredient> ingredient) {
            this.ingredient = new ArrayList<>(ingredient);
            return this;
        }

        /**
         * Allergens that are known or suspected to be a part of this nutrition product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param knownAllergen
         *     Known or suspected allergens that are a part of this product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder knownAllergen(CodeableReference... knownAllergen) {
            for (CodeableReference value : knownAllergen) {
                this.knownAllergen.add(value);
            }
            return this;
        }

        /**
         * Allergens that are known or suspected to be a part of this nutrition product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param knownAllergen
         *     Known or suspected allergens that are a part of this product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder knownAllergen(Collection<CodeableReference> knownAllergen) {
            this.knownAllergen = new ArrayList<>(knownAllergen);
            return this;
        }

        /**
         * Specifies descriptive properties of the nutrition product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param productCharacteristic
         *     Specifies descriptive properties of the nutrition product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder productCharacteristic(ProductCharacteristic... productCharacteristic) {
            for (ProductCharacteristic value : productCharacteristic) {
                this.productCharacteristic.add(value);
            }
            return this;
        }

        /**
         * Specifies descriptive properties of the nutrition product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param productCharacteristic
         *     Specifies descriptive properties of the nutrition product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder productCharacteristic(Collection<ProductCharacteristic> productCharacteristic) {
            this.productCharacteristic = new ArrayList<>(productCharacteristic);
            return this;
        }

        /**
         * Conveys instance-level information about this product item. One or several physical, countable instances or 
         * occurrences of the product.
         * 
         * @param instance
         *     One or several physical instances or occurrences of the nutrition product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instance(Instance instance) {
            this.instance = instance;
            return this;
        }

        /**
         * Comments made about the product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * Comments made about the product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * Build the {@link NutritionProduct}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link NutritionProduct}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid NutritionProduct per the base specification
         */
        @Override
        public NutritionProduct build() {
            NutritionProduct nutritionProduct = new NutritionProduct(this);
            if (validating) {
                validate(nutritionProduct);
            }
            return nutritionProduct;
        }

        protected void validate(NutritionProduct nutritionProduct) {
            super.validate(nutritionProduct);
            ValidationSupport.requireNonNull(nutritionProduct.status, "status");
            ValidationSupport.checkList(nutritionProduct.category, "category", CodeableConcept.class);
            ValidationSupport.checkList(nutritionProduct.manufacturer, "manufacturer", Reference.class);
            ValidationSupport.checkList(nutritionProduct.nutrient, "nutrient", Nutrient.class);
            ValidationSupport.checkList(nutritionProduct.ingredient, "ingredient", Ingredient.class);
            ValidationSupport.checkList(nutritionProduct.knownAllergen, "knownAllergen", CodeableReference.class);
            ValidationSupport.checkList(nutritionProduct.productCharacteristic, "productCharacteristic", ProductCharacteristic.class);
            ValidationSupport.checkList(nutritionProduct.note, "note", Annotation.class);
            ValidationSupport.checkReferenceType(nutritionProduct.manufacturer, "manufacturer", "Organization");
        }

        protected Builder from(NutritionProduct nutritionProduct) {
            super.from(nutritionProduct);
            status = nutritionProduct.status;
            category.addAll(nutritionProduct.category);
            code = nutritionProduct.code;
            manufacturer.addAll(nutritionProduct.manufacturer);
            nutrient.addAll(nutritionProduct.nutrient);
            ingredient.addAll(nutritionProduct.ingredient);
            knownAllergen.addAll(nutritionProduct.knownAllergen);
            productCharacteristic.addAll(nutritionProduct.productCharacteristic);
            instance = nutritionProduct.instance;
            note.addAll(nutritionProduct.note);
            return this;
        }
    }

    /**
     * The product's nutritional information expressed by the nutrients.
     */
    public static class Nutrient extends BackboneElement {
        @Binding(
            bindingName = "NutritionProductNutrient",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes that identify nutrients that could be parts of nutrition products.",
            valueSet = "http://hl7.org/fhir/ValueSet/nutrition-product-nutrient"
        )
        private final CodeableReference item;
        private final List<Ratio> amount;

        private Nutrient(Builder builder) {
            super(builder);
            item = builder.item;
            amount = Collections.unmodifiableList(builder.amount);
        }

        /**
         * The (relevant) nutrients in the product.
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that may be null.
         */
        public CodeableReference getItem() {
            return item;
        }

        /**
         * The amount of nutrient expressed in one or more units: X per pack / per serving / per dose.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Ratio} that may be empty.
         */
        public List<Ratio> getAmount() {
            return amount;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (item != null) || 
                !amount.isEmpty();
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
                    accept(item, "item", visitor);
                    accept(amount, "amount", visitor, Ratio.class);
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
            Nutrient other = (Nutrient) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(item, other.item) && 
                Objects.equals(amount, other.amount);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    item, 
                    amount);
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
            private CodeableReference item;
            private List<Ratio> amount = new ArrayList<>();

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
             * The (relevant) nutrients in the product.
             * 
             * @param item
             *     The (relevant) nutrients in the product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(CodeableReference item) {
                this.item = item;
                return this;
            }

            /**
             * The amount of nutrient expressed in one or more units: X per pack / per serving / per dose.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param amount
             *     The amount of nutrient expressed in one or more units: X per pack / per serving / per dose
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Ratio... amount) {
                for (Ratio value : amount) {
                    this.amount.add(value);
                }
                return this;
            }

            /**
             * The amount of nutrient expressed in one or more units: X per pack / per serving / per dose.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param amount
             *     The amount of nutrient expressed in one or more units: X per pack / per serving / per dose
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder amount(Collection<Ratio> amount) {
                this.amount = new ArrayList<>(amount);
                return this;
            }

            /**
             * Build the {@link Nutrient}
             * 
             * @return
             *     An immutable object of type {@link Nutrient}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Nutrient per the base specification
             */
            @Override
            public Nutrient build() {
                Nutrient nutrient = new Nutrient(this);
                if (validating) {
                    validate(nutrient);
                }
                return nutrient;
            }

            protected void validate(Nutrient nutrient) {
                super.validate(nutrient);
                ValidationSupport.checkList(nutrient.amount, "amount", Ratio.class);
                ValidationSupport.requireValueOrChildren(nutrient);
            }

            protected Builder from(Nutrient nutrient) {
                super.from(nutrient);
                item = nutrient.item;
                amount.addAll(nutrient.amount);
                return this;
            }
        }
    }

    /**
     * Ingredients contained in this product.
     */
    public static class Ingredient extends BackboneElement {
        @Summary
        @Required
        private final CodeableReference item;
        @Summary
        private final List<Ratio> amount;

        private Ingredient(Builder builder) {
            super(builder);
            item = builder.item;
            amount = Collections.unmodifiableList(builder.amount);
        }

        /**
         * The ingredient contained in the product.
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that is non-null.
         */
        public CodeableReference getItem() {
            return item;
        }

        /**
         * The amount of ingredient that is in the product.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Ratio} that may be empty.
         */
        public List<Ratio> getAmount() {
            return amount;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (item != null) || 
                !amount.isEmpty();
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
                    accept(item, "item", visitor);
                    accept(amount, "amount", visitor, Ratio.class);
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
            Ingredient other = (Ingredient) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(item, other.item) && 
                Objects.equals(amount, other.amount);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    item, 
                    amount);
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
            private CodeableReference item;
            private List<Ratio> amount = new ArrayList<>();

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
             * The ingredient contained in the product.
             * 
             * <p>This element is required.
             * 
             * @param item
             *     The ingredient contained in the product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(CodeableReference item) {
                this.item = item;
                return this;
            }

            /**
             * The amount of ingredient that is in the product.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param amount
             *     The amount of ingredient that is in the product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Ratio... amount) {
                for (Ratio value : amount) {
                    this.amount.add(value);
                }
                return this;
            }

            /**
             * The amount of ingredient that is in the product.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param amount
             *     The amount of ingredient that is in the product
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder amount(Collection<Ratio> amount) {
                this.amount = new ArrayList<>(amount);
                return this;
            }

            /**
             * Build the {@link Ingredient}
             * 
             * <p>Required elements:
             * <ul>
             * <li>item</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Ingredient}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Ingredient per the base specification
             */
            @Override
            public Ingredient build() {
                Ingredient ingredient = new Ingredient(this);
                if (validating) {
                    validate(ingredient);
                }
                return ingredient;
            }

            protected void validate(Ingredient ingredient) {
                super.validate(ingredient);
                ValidationSupport.requireNonNull(ingredient.item, "item");
                ValidationSupport.checkList(ingredient.amount, "amount", Ratio.class);
                ValidationSupport.requireValueOrChildren(ingredient);
            }

            protected Builder from(Ingredient ingredient) {
                super.from(ingredient);
                item = ingredient.item;
                amount.addAll(ingredient.amount);
                return this;
            }
        }
    }

    /**
     * Specifies descriptive properties of the nutrition product.
     */
    public static class ProductCharacteristic extends BackboneElement {
        @Binding(
            bindingName = "PropertyCharacteristic",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes that identify properties that can be measured.",
            valueSet = "http://hl7.org/fhir/ValueSet/measurement-property"
        )
        @Required
        private final CodeableConcept type;
        @Choice({ CodeableConcept.class, String.class, SimpleQuantity.class, Base64Binary.class, Attachment.class, Boolean.class })
        @Required
        private final Element value;

        private ProductCharacteristic(Builder builder) {
            super(builder);
            type = builder.type;
            value = builder.value;
        }

        /**
         * A code specifying which characteristic of the product is being described (for example, colour, shape).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The actual characteristic value corresponding to the type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}, {@link String}, {@link SimpleQuantity}, {@link Base64Binary}, 
         *     {@link Attachment} or {@link Boolean} that is non-null.
         */
        public Element getValue() {
            return value;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (value != null);
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
                    accept(type, "type", visitor);
                    accept(value, "value", visitor);
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
            ProductCharacteristic other = (ProductCharacteristic) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    value);
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
            private CodeableConcept type;
            private Element value;

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
             * A code specifying which characteristic of the product is being described (for example, colour, shape).
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Code specifying the type of characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type String.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     The value of the characteristic
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.String value) {
                this.value = (value == null) ? null : String.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Boolean.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     The value of the characteristic
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.Boolean value) {
                this.value = (value == null) ? null : Boolean.of(value);
                return this;
            }

            /**
             * The actual characteristic value corresponding to the type.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link String}</li>
             * <li>{@link SimpleQuantity}</li>
             * <li>{@link Base64Binary}</li>
             * <li>{@link Attachment}</li>
             * <li>{@link Boolean}</li>
             * </ul>
             * 
             * @param value
             *     The value of the characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Build the {@link ProductCharacteristic}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>value</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link ProductCharacteristic}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ProductCharacteristic per the base specification
             */
            @Override
            public ProductCharacteristic build() {
                ProductCharacteristic productCharacteristic = new ProductCharacteristic(this);
                if (validating) {
                    validate(productCharacteristic);
                }
                return productCharacteristic;
            }

            protected void validate(ProductCharacteristic productCharacteristic) {
                super.validate(productCharacteristic);
                ValidationSupport.requireNonNull(productCharacteristic.type, "type");
                ValidationSupport.requireChoiceElement(productCharacteristic.value, "value", CodeableConcept.class, String.class, SimpleQuantity.class, Base64Binary.class, Attachment.class, Boolean.class);
                ValidationSupport.requireValueOrChildren(productCharacteristic);
            }

            protected Builder from(ProductCharacteristic productCharacteristic) {
                super.from(productCharacteristic);
                type = productCharacteristic.type;
                value = productCharacteristic.value;
                return this;
            }
        }
    }

    /**
     * Conveys instance-level information about this product item. One or several physical, countable instances or 
     * occurrences of the product.
     */
    public static class Instance extends BackboneElement {
        private final SimpleQuantity quantity;
        private final List<Identifier> identifier;
        private final String lotNumber;
        private final DateTime expiry;
        private final DateTime useBy;

        private Instance(Builder builder) {
            super(builder);
            quantity = builder.quantity;
            identifier = Collections.unmodifiableList(builder.identifier);
            lotNumber = builder.lotNumber;
            expiry = builder.expiry;
            useBy = builder.useBy;
        }

        /**
         * The amount of items or instances that the resource considers, for instance when referring to 2 identical units 
         * together.
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getQuantity() {
            return quantity;
        }

        /**
         * The identifier for the physical instance, typically a serial number.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
         */
        public List<Identifier> getIdentifier() {
            return identifier;
        }

        /**
         * The identification of the batch or lot of the product.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getLotNumber() {
            return lotNumber;
        }

        /**
         * The time after which the product is no longer expected to be in proper condition, or its use is not advised or not 
         * allowed.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getExpiry() {
            return expiry;
        }

        /**
         * The time after which the product is no longer expected to be in proper condition, or its use is not advised or not 
         * allowed.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getUseBy() {
            return useBy;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (quantity != null) || 
                !identifier.isEmpty() || 
                (lotNumber != null) || 
                (expiry != null) || 
                (useBy != null);
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
                    accept(quantity, "quantity", visitor);
                    accept(identifier, "identifier", visitor, Identifier.class);
                    accept(lotNumber, "lotNumber", visitor);
                    accept(expiry, "expiry", visitor);
                    accept(useBy, "useBy", visitor);
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
            Instance other = (Instance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(lotNumber, other.lotNumber) && 
                Objects.equals(expiry, other.expiry) && 
                Objects.equals(useBy, other.useBy);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    quantity, 
                    identifier, 
                    lotNumber, 
                    expiry, 
                    useBy);
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
            private SimpleQuantity quantity;
            private List<Identifier> identifier = new ArrayList<>();
            private String lotNumber;
            private DateTime expiry;
            private DateTime useBy;

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
             * The amount of items or instances that the resource considers, for instance when referring to 2 identical units 
             * together.
             * 
             * @param quantity
             *     The amount of items or instances
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(SimpleQuantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * The identifier for the physical instance, typically a serial number.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     The identifier for the physical instance, typically a serial number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier... identifier) {
                for (Identifier value : identifier) {
                    this.identifier.add(value);
                }
                return this;
            }

            /**
             * The identifier for the physical instance, typically a serial number.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     The identifier for the physical instance, typically a serial number
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder identifier(Collection<Identifier> identifier) {
                this.identifier = new ArrayList<>(identifier);
                return this;
            }

            /**
             * Convenience method for setting {@code lotNumber}.
             * 
             * @param lotNumber
             *     The identification of the batch or lot of the product
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #lotNumber(com.ibm.fhir.model.type.String)
             */
            public Builder lotNumber(java.lang.String lotNumber) {
                this.lotNumber = (lotNumber == null) ? null : String.of(lotNumber);
                return this;
            }

            /**
             * The identification of the batch or lot of the product.
             * 
             * @param lotNumber
             *     The identification of the batch or lot of the product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder lotNumber(String lotNumber) {
                this.lotNumber = lotNumber;
                return this;
            }

            /**
             * The time after which the product is no longer expected to be in proper condition, or its use is not advised or not 
             * allowed.
             * 
             * @param expiry
             *     The expiry date or date and time for the product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder expiry(DateTime expiry) {
                this.expiry = expiry;
                return this;
            }

            /**
             * The time after which the product is no longer expected to be in proper condition, or its use is not advised or not 
             * allowed.
             * 
             * @param useBy
             *     The date until which the product is expected to be good for consumption
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder useBy(DateTime useBy) {
                this.useBy = useBy;
                return this;
            }

            /**
             * Build the {@link Instance}
             * 
             * @return
             *     An immutable object of type {@link Instance}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Instance per the base specification
             */
            @Override
            public Instance build() {
                Instance instance = new Instance(this);
                if (validating) {
                    validate(instance);
                }
                return instance;
            }

            protected void validate(Instance instance) {
                super.validate(instance);
                ValidationSupport.checkList(instance.identifier, "identifier", Identifier.class);
                ValidationSupport.requireValueOrChildren(instance);
            }

            protected Builder from(Instance instance) {
                super.from(instance);
                quantity = instance.quantity;
                identifier.addAll(instance.identifier);
                lotNumber = instance.lotNumber;
                expiry = instance.expiry;
                useBy = instance.useBy;
                return this;
            }
        }
    }
}
