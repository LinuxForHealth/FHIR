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
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.MedicationStatus;
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
 * This resource is primarily used for the identification and definition of a medication for the purposes of prescribing, 
 * dispensing, and administering a medication as well as for making statements about medication use.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Medication extends DomainResource {
    private final List<Identifier> identifier;
    private final CodeableConcept code;
    private final MedicationStatus status;
    private final Reference manufacturer;
    private final CodeableConcept form;
    private final Ratio amount;
    private final List<Ingredient> ingredient;
    private final Batch batch;

    private Medication(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        code = builder.code;
        status = builder.status;
        manufacturer = builder.manufacturer;
        form = builder.form;
        amount = builder.amount;
        ingredient = Collections.unmodifiableList(builder.ingredient);
        batch = builder.batch;
    }

    /**
     * <p>
     * Business identifier for this medication.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: 
     * This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national 
     * or local formulary code, optionally with translations to other code systems.
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
     * A code to indicate if the medication is in active use.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MedicationStatus}.
     */
    public MedicationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Describes the details of the manufacturer of the medication product. This is not intended to represent the distributor 
     * of a medication product.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getManufacturer() {
        return manufacturer;
    }

    /**
     * <p>
     * Describes the form of the item. Powder; tablets; capsule.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getForm() {
        return form;
    }

    /**
     * <p>
     * Specific amount of the drug in the packaged product. For example, when specifying a product that has the same strength 
     * (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional 
     * clarification of the package amount (For example, 3 mL, 10mL, etc.).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Ratio}.
     */
    public Ratio getAmount() {
        return amount;
    }

    /**
     * <p>
     * Identifies a particular constituent of interest in the product.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Ingredient}.
     */
    public List<Ingredient> getIngredient() {
        return ingredient;
    }

    /**
     * <p>
     * Information that only applies to packages (not products).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Batch}.
     */
    public Batch getBatch() {
        return batch;
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
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(code, "code", visitor);
                accept(status, "status", visitor);
                accept(manufacturer, "manufacturer", visitor);
                accept(form, "form", visitor);
                accept(amount, "amount", visitor);
                accept(ingredient, "ingredient", visitor, Ingredient.class);
                accept(batch, "batch", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept code;
        private MedicationStatus status;
        private Reference manufacturer;
        private CodeableConcept form;
        private Ratio amount;
        private List<Ingredient> ingredient = new ArrayList<>();
        private Batch batch;

        private Builder() {
            super();
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
         * Business identifier for this medication.
         * </p>
         * 
         * @param identifier
         *     Business identifier for this medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Business identifier for this medication.
         * </p>
         * 
         * @param identifier
         *     Business identifier for this medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier.addAll(identifier);
            return this;
        }

        /**
         * <p>
         * A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: 
         * This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national 
         * or local formulary code, optionally with translations to other code systems.
         * </p>
         * 
         * @param code
         *     Codes that identify this medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * <p>
         * A code to indicate if the medication is in active use.
         * </p>
         * 
         * @param status
         *     active | inactive | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder status(MedicationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * Describes the details of the manufacturer of the medication product. This is not intended to represent the distributor 
         * of a medication product.
         * </p>
         * 
         * @param manufacturer
         *     Manufacturer of the item
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder manufacturer(Reference manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        /**
         * <p>
         * Describes the form of the item. Powder; tablets; capsule.
         * </p>
         * 
         * @param form
         *     powder | tablets | capsule +
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder form(CodeableConcept form) {
            this.form = form;
            return this;
        }

        /**
         * <p>
         * Specific amount of the drug in the packaged product. For example, when specifying a product that has the same strength 
         * (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional 
         * clarification of the package amount (For example, 3 mL, 10mL, etc.).
         * </p>
         * 
         * @param amount
         *     Amount of drug in package
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder amount(Ratio amount) {
            this.amount = amount;
            return this;
        }

        /**
         * <p>
         * Identifies a particular constituent of interest in the product.
         * </p>
         * 
         * @param ingredient
         *     Active or inactive ingredient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder ingredient(Ingredient... ingredient) {
            for (Ingredient value : ingredient) {
                this.ingredient.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies a particular constituent of interest in the product.
         * </p>
         * 
         * @param ingredient
         *     Active or inactive ingredient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder ingredient(Collection<Ingredient> ingredient) {
            this.ingredient.addAll(ingredient);
            return this;
        }

        /**
         * <p>
         * Information that only applies to packages (not products).
         * </p>
         * 
         * @param batch
         *     Details about packaged medications
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder batch(Batch batch) {
            this.batch = batch;
            return this;
        }

        @Override
        public Medication build() {
            return new Medication(this);
        }

        private Builder from(Medication medication) {
            id = medication.id;
            meta = medication.meta;
            implicitRules = medication.implicitRules;
            language = medication.language;
            text = medication.text;
            contained.addAll(medication.contained);
            extension.addAll(medication.extension);
            modifierExtension.addAll(medication.modifierExtension);
            identifier.addAll(medication.identifier);
            code = medication.code;
            status = medication.status;
            manufacturer = medication.manufacturer;
            form = medication.form;
            amount = medication.amount;
            ingredient.addAll(medication.ingredient);
            batch = medication.batch;
            return this;
        }
    }

    /**
     * <p>
     * Identifies a particular constituent of interest in the product.
     * </p>
     */
    public static class Ingredient extends BackboneElement {
        private final Element item;
        private final Boolean isActive;
        private final Ratio strength;

        private Ingredient(Builder builder) {
            super(builder);
            item = ValidationSupport.requireChoiceElement(builder.item, "item", CodeableConcept.class, Reference.class);
            isActive = builder.isActive;
            strength = builder.strength;
        }

        /**
         * <p>
         * The actual ingredient - either a substance (simple ingredient) or another medication of a medication.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getItem() {
            return item;
        }

        /**
         * <p>
         * Indication of whether this ingredient affects the therapeutic action of the drug.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getIsActive() {
            return isActive;
        }

        /**
         * <p>
         * Specifies how many (or how much) of the items there are in this Medication. For example, 250 mg per tablet. This is 
         * expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Ratio}.
         */
        public Ratio getStrength() {
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
                    accept(item, "item", visitor, true);
                    accept(isActive, "isActive", visitor);
                    accept(strength, "strength", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(item).from(this);
        }

        public Builder toBuilder(Element item) {
            return new Builder(item).from(this);
        }

        public static Builder builder(Element item) {
            return new Builder(item);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Element item;

            // optional
            private Boolean isActive;
            private Ratio strength;

            private Builder(Element item) {
                super();
                this.item = item;
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
             * Indication of whether this ingredient affects the therapeutic action of the drug.
             * </p>
             * 
             * @param isActive
             *     Active ingredient indicator
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder isActive(Boolean isActive) {
                this.isActive = isActive;
                return this;
            }

            /**
             * <p>
             * Specifies how many (or how much) of the items there are in this Medication. For example, 250 mg per tablet. This is 
             * expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.
             * </p>
             * 
             * @param strength
             *     Quantity of ingredient present
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder strength(Ratio strength) {
                this.strength = strength;
                return this;
            }

            @Override
            public Ingredient build() {
                return new Ingredient(this);
            }

            private Builder from(Ingredient ingredient) {
                id = ingredient.id;
                extension.addAll(ingredient.extension);
                modifierExtension.addAll(ingredient.modifierExtension);
                isActive = ingredient.isActive;
                strength = ingredient.strength;
                return this;
            }
        }
    }

    /**
     * <p>
     * Information that only applies to packages (not products).
     * </p>
     */
    public static class Batch extends BackboneElement {
        private final String lotNumber;
        private final DateTime expirationDate;

        private Batch(Builder builder) {
            super(builder);
            lotNumber = builder.lotNumber;
            expirationDate = builder.expirationDate;
        }

        /**
         * <p>
         * The assigned lot number of a batch of the specified product.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getLotNumber() {
            return lotNumber;
        }

        /**
         * <p>
         * When this specific batch of product will expire.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getExpirationDate() {
            return expirationDate;
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
                    accept(lotNumber, "lotNumber", visitor);
                    accept(expirationDate, "expirationDate", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private String lotNumber;
            private DateTime expirationDate;

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
             * The assigned lot number of a batch of the specified product.
             * </p>
             * 
             * @param lotNumber
             *     Identifier assigned to batch
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder lotNumber(String lotNumber) {
                this.lotNumber = lotNumber;
                return this;
            }

            /**
             * <p>
             * When this specific batch of product will expire.
             * </p>
             * 
             * @param expirationDate
             *     When batch will expire
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder expirationDate(DateTime expirationDate) {
                this.expirationDate = expirationDate;
                return this;
            }

            @Override
            public Batch build() {
                return new Batch(this);
            }

            private Builder from(Batch batch) {
                id = batch.id;
                extension.addAll(batch.extension);
                modifierExtension.addAll(batch.modifierExtension);
                lotNumber = batch.lotNumber;
                expirationDate = batch.expirationDate;
                return this;
            }
        }
    }
}
