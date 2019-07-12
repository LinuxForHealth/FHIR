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

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.NutritionOrderIntent;
import com.ibm.watsonhealth.fhir.model.type.NutritionOrderStatus;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.
 * </p>
 */
@Constraint(
    id = "nor-1",
    level = "Warning",
    location = "(base)",
    description = "Nutrition Order SHALL contain either Oral Diet , Supplement, or Enteral Formula class",
    expression = "oralDiet.exists() or supplement.exists() or enteralFormula.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class NutritionOrder extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Canonical> instantiatesCanonical;
    private final List<Uri> instantiatesUri;
    private final List<Uri> instantiates;
    private final NutritionOrderStatus status;
    private final NutritionOrderIntent intent;
    private final Reference patient;
    private final Reference encounter;
    private final DateTime dateTime;
    private final Reference orderer;
    private final List<Reference> allergyIntolerance;
    private final List<CodeableConcept> foodPreferenceModifier;
    private final List<CodeableConcept> excludeFoodModifier;
    private final OralDiet oralDiet;
    private final List<Supplement> supplement;
    private final EnteralFormula enteralFormula;
    private final List<Annotation> note;

    private volatile int hashCode;

    private NutritionOrder(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
        instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
        instantiates = Collections.unmodifiableList(builder.instantiates);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        intent = ValidationSupport.requireNonNull(builder.intent, "intent");
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        encounter = builder.encounter;
        dateTime = ValidationSupport.requireNonNull(builder.dateTime, "dateTime");
        orderer = builder.orderer;
        allergyIntolerance = Collections.unmodifiableList(builder.allergyIntolerance);
        foodPreferenceModifier = Collections.unmodifiableList(builder.foodPreferenceModifier);
        excludeFoodModifier = Collections.unmodifiableList(builder.excludeFoodModifier);
        oralDiet = builder.oralDiet;
        supplement = Collections.unmodifiableList(builder.supplement);
        enteralFormula = builder.enteralFormula;
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * <p>
     * Identifiers assigned to this order by the order sender or by the order receiver.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
     * part by this NutritionOrder.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * <p>
     * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this NutritionOrder.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * <p>
     * The URL pointing to a protocol, guideline, orderset or other definition that is adhered to in whole or in part by this 
     * NutritionOrder.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getInstantiates() {
        return instantiates;
    }

    /**
     * <p>
     * The workflow status of the nutrition order/request.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link NutritionOrderStatus}.
     */
    public NutritionOrderStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Indicates the level of authority/intentionality associated with the NutrionOrder and where the request fits into the 
     * workflow chain.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link NutritionOrderIntent}.
     */
    public NutritionOrderIntent getIntent() {
        return intent;
    }

    /**
     * <p>
     * The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula 
     * feeding.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * <p>
     * An encounter that provides additional information about the healthcare context in which this request is made.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * <p>
     * The date and time that this nutrition order was requested.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDateTime() {
        return dateTime;
    }

    /**
     * <p>
     * The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOrderer() {
        return orderer;
    }

    /**
     * <p>
     * A link to a record of allergies or intolerances which should be included in the nutrition order.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAllergyIntolerance() {
        return allergyIntolerance;
    }

    /**
     * <p>
     * This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be 
     * derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to 
     * the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getFoodPreferenceModifier() {
        return foodPreferenceModifier;
    }

    /**
     * <p>
     * This modifier is used to convey Order-specific modifier about the type of oral food or oral fluids that should not be 
     * given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No 
     * Wheat or Gluten-Free. While it should not be necessary to repeat allergy or intolerance information captured in the 
     * referenced AllergyIntolerance resource in the excludeFoodModifier, this element may be used to convey additional 
     * specificity related to foods that should be eliminated from the patient’s diet for any reason. This modifier applies 
     * to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getExcludeFoodModifier() {
        return excludeFoodModifier;
    }

    /**
     * <p>
     * Diet given orally in contrast to enteral (tube) feeding.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link OralDiet}.
     */
    public OralDiet getOralDiet() {
        return oralDiet;
    }

    /**
     * <p>
     * Oral nutritional products given in order to add further nutritional value to the patient's diet.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Supplement}.
     */
    public List<Supplement> getSupplement() {
        return supplement;
    }

    /**
     * <p>
     * Feeding provided through the gastrointestinal tract via a tube, catheter, or stoma that delivers nutrition distal to 
     * the oral cavity.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link EnteralFormula}.
     */
    public EnteralFormula getEnteralFormula() {
        return enteralFormula;
    }

    /**
     * <p>
     * Comments made about the {{title}} by the requester, performer, subject or other participants.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
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
                accept(instantiatesCanonical, "instantiatesCanonical", visitor, Canonical.class);
                accept(instantiatesUri, "instantiatesUri", visitor, Uri.class);
                accept(instantiates, "instantiates", visitor, Uri.class);
                accept(status, "status", visitor);
                accept(intent, "intent", visitor);
                accept(patient, "patient", visitor);
                accept(encounter, "encounter", visitor);
                accept(dateTime, "dateTime", visitor);
                accept(orderer, "orderer", visitor);
                accept(allergyIntolerance, "allergyIntolerance", visitor, Reference.class);
                accept(foodPreferenceModifier, "foodPreferenceModifier", visitor, CodeableConcept.class);
                accept(excludeFoodModifier, "excludeFoodModifier", visitor, CodeableConcept.class);
                accept(oralDiet, "oralDiet", visitor);
                accept(supplement, "supplement", visitor, Supplement.class);
                accept(enteralFormula, "enteralFormula", visitor);
                accept(note, "note", visitor, Annotation.class);
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
        NutritionOrder other = (NutritionOrder) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(instantiatesCanonical, other.instantiatesCanonical) && 
            Objects.equals(instantiatesUri, other.instantiatesUri) && 
            Objects.equals(instantiates, other.instantiates) && 
            Objects.equals(status, other.status) && 
            Objects.equals(intent, other.intent) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(dateTime, other.dateTime) && 
            Objects.equals(orderer, other.orderer) && 
            Objects.equals(allergyIntolerance, other.allergyIntolerance) && 
            Objects.equals(foodPreferenceModifier, other.foodPreferenceModifier) && 
            Objects.equals(excludeFoodModifier, other.excludeFoodModifier) && 
            Objects.equals(oralDiet, other.oralDiet) && 
            Objects.equals(supplement, other.supplement) && 
            Objects.equals(enteralFormula, other.enteralFormula) && 
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
                identifier, 
                instantiatesCanonical, 
                instantiatesUri, 
                instantiates, 
                status, 
                intent, 
                patient, 
                encounter, 
                dateTime, 
                orderer, 
                allergyIntolerance, 
                foodPreferenceModifier, 
                excludeFoodModifier, 
                oralDiet, 
                supplement, 
                enteralFormula, 
                note);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status, intent, patient, dateTime).from(this);
    }

    public Builder toBuilder(NutritionOrderStatus status, NutritionOrderIntent intent, Reference patient, DateTime dateTime) {
        return new Builder(status, intent, patient, dateTime).from(this);
    }

    public static Builder builder(NutritionOrderStatus status, NutritionOrderIntent intent, Reference patient, DateTime dateTime) {
        return new Builder(status, intent, patient, dateTime);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final NutritionOrderStatus status;
        private final NutritionOrderIntent intent;
        private final Reference patient;
        private final DateTime dateTime;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Canonical> instantiatesCanonical = new ArrayList<>();
        private List<Uri> instantiatesUri = new ArrayList<>();
        private List<Uri> instantiates = new ArrayList<>();
        private Reference encounter;
        private Reference orderer;
        private List<Reference> allergyIntolerance = new ArrayList<>();
        private List<CodeableConcept> foodPreferenceModifier = new ArrayList<>();
        private List<CodeableConcept> excludeFoodModifier = new ArrayList<>();
        private OralDiet oralDiet;
        private List<Supplement> supplement = new ArrayList<>();
        private EnteralFormula enteralFormula;
        private List<Annotation> note = new ArrayList<>();

        private Builder(NutritionOrderStatus status, NutritionOrderIntent intent, Reference patient, DateTime dateTime) {
            super();
            this.status = status;
            this.intent = intent;
            this.patient = patient;
            this.dateTime = dateTime;
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to the existing list
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * Identifiers assigned to this order by the order sender or by the order receiver.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param identifier
         *     Identifiers assigned to this order
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
         * <p>
         * Identifiers assigned to this order by the order sender or by the order receiver.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Identifiers assigned to this order
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * <p>
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this NutritionOrder.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesCanonical(Canonical... instantiatesCanonical) {
            for (Canonical value : instantiatesCanonical) {
                this.instantiatesCanonical.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this NutritionOrder.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical = new ArrayList<>(instantiatesCanonical);
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this NutritionOrder.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesUri(Uri... instantiatesUri) {
            for (Uri value : instantiatesUri) {
                this.instantiatesUri.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this NutritionOrder.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri = new ArrayList<>(instantiatesUri);
            return this;
        }

        /**
         * <p>
         * The URL pointing to a protocol, guideline, orderset or other definition that is adhered to in whole or in part by this 
         * NutritionOrder.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param instantiates
         *     Instantiates protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiates(Uri... instantiates) {
            for (Uri value : instantiates) {
                this.instantiates.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL pointing to a protocol, guideline, orderset or other definition that is adhered to in whole or in part by this 
         * NutritionOrder.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param instantiates
         *     Instantiates protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiates(Collection<Uri> instantiates) {
            this.instantiates = new ArrayList<>(instantiates);
            return this;
        }

        /**
         * <p>
         * An encounter that provides additional information about the healthcare context in which this request is made.
         * </p>
         * 
         * @param encounter
         *     The encounter associated with this nutrition order
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * <p>
         * The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.
         * </p>
         * 
         * @param orderer
         *     Who ordered the diet, formula or nutritional supplement
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder orderer(Reference orderer) {
            this.orderer = orderer;
            return this;
        }

        /**
         * <p>
         * A link to a record of allergies or intolerances which should be included in the nutrition order.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param allergyIntolerance
         *     List of the patient's food and nutrition-related allergies and intolerances
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder allergyIntolerance(Reference... allergyIntolerance) {
            for (Reference value : allergyIntolerance) {
                this.allergyIntolerance.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A link to a record of allergies or intolerances which should be included in the nutrition order.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param allergyIntolerance
         *     List of the patient's food and nutrition-related allergies and intolerances
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder allergyIntolerance(Collection<Reference> allergyIntolerance) {
            this.allergyIntolerance = new ArrayList<>(allergyIntolerance);
            return this;
        }

        /**
         * <p>
         * This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be 
         * derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to 
         * the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param foodPreferenceModifier
         *     Order-specific modifier about the type of food that should be given
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder foodPreferenceModifier(CodeableConcept... foodPreferenceModifier) {
            for (CodeableConcept value : foodPreferenceModifier) {
                this.foodPreferenceModifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be 
         * derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to 
         * the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param foodPreferenceModifier
         *     Order-specific modifier about the type of food that should be given
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder foodPreferenceModifier(Collection<CodeableConcept> foodPreferenceModifier) {
            this.foodPreferenceModifier = new ArrayList<>(foodPreferenceModifier);
            return this;
        }

        /**
         * <p>
         * This modifier is used to convey Order-specific modifier about the type of oral food or oral fluids that should not be 
         * given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No 
         * Wheat or Gluten-Free. While it should not be necessary to repeat allergy or intolerance information captured in the 
         * referenced AllergyIntolerance resource in the excludeFoodModifier, this element may be used to convey additional 
         * specificity related to foods that should be eliminated from the patient’s diet for any reason. This modifier applies 
         * to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param excludeFoodModifier
         *     Order-specific modifier about the type of food that should not be given
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder excludeFoodModifier(CodeableConcept... excludeFoodModifier) {
            for (CodeableConcept value : excludeFoodModifier) {
                this.excludeFoodModifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * This modifier is used to convey Order-specific modifier about the type of oral food or oral fluids that should not be 
         * given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No 
         * Wheat or Gluten-Free. While it should not be necessary to repeat allergy or intolerance information captured in the 
         * referenced AllergyIntolerance resource in the excludeFoodModifier, this element may be used to convey additional 
         * specificity related to foods that should be eliminated from the patient’s diet for any reason. This modifier applies 
         * to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param excludeFoodModifier
         *     Order-specific modifier about the type of food that should not be given
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder excludeFoodModifier(Collection<CodeableConcept> excludeFoodModifier) {
            this.excludeFoodModifier = new ArrayList<>(excludeFoodModifier);
            return this;
        }

        /**
         * <p>
         * Diet given orally in contrast to enteral (tube) feeding.
         * </p>
         * 
         * @param oralDiet
         *     Oral diet components
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder oralDiet(OralDiet oralDiet) {
            this.oralDiet = oralDiet;
            return this;
        }

        /**
         * <p>
         * Oral nutritional products given in order to add further nutritional value to the patient's diet.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param supplement
         *     Supplement components
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supplement(Supplement... supplement) {
            for (Supplement value : supplement) {
                this.supplement.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Oral nutritional products given in order to add further nutritional value to the patient's diet.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param supplement
         *     Supplement components
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supplement(Collection<Supplement> supplement) {
            this.supplement = new ArrayList<>(supplement);
            return this;
        }

        /**
         * <p>
         * Feeding provided through the gastrointestinal tract via a tube, catheter, or stoma that delivers nutrition distal to 
         * the oral cavity.
         * </p>
         * 
         * @param enteralFormula
         *     Enteral formula components
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder enteralFormula(EnteralFormula enteralFormula) {
            this.enteralFormula = enteralFormula;
            return this;
        }

        /**
         * <p>
         * Comments made about the {{title}} by the requester, performer, subject or other participants.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param note
         *     Comments
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
         * <p>
         * Comments made about the {{title}} by the requester, performer, subject or other participants.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Comments
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        @Override
        public NutritionOrder build() {
            return new NutritionOrder(this);
        }

        private Builder from(NutritionOrder nutritionOrder) {
            id = nutritionOrder.id;
            meta = nutritionOrder.meta;
            implicitRules = nutritionOrder.implicitRules;
            language = nutritionOrder.language;
            text = nutritionOrder.text;
            contained.addAll(nutritionOrder.contained);
            extension.addAll(nutritionOrder.extension);
            modifierExtension.addAll(nutritionOrder.modifierExtension);
            identifier.addAll(nutritionOrder.identifier);
            instantiatesCanonical.addAll(nutritionOrder.instantiatesCanonical);
            instantiatesUri.addAll(nutritionOrder.instantiatesUri);
            instantiates.addAll(nutritionOrder.instantiates);
            encounter = nutritionOrder.encounter;
            orderer = nutritionOrder.orderer;
            allergyIntolerance.addAll(nutritionOrder.allergyIntolerance);
            foodPreferenceModifier.addAll(nutritionOrder.foodPreferenceModifier);
            excludeFoodModifier.addAll(nutritionOrder.excludeFoodModifier);
            oralDiet = nutritionOrder.oralDiet;
            supplement.addAll(nutritionOrder.supplement);
            enteralFormula = nutritionOrder.enteralFormula;
            note.addAll(nutritionOrder.note);
            return this;
        }
    }

    /**
     * <p>
     * Diet given orally in contrast to enteral (tube) feeding.
     * </p>
     */
    public static class OralDiet extends BackboneElement {
        private final List<CodeableConcept> type;
        private final List<Timing> schedule;
        private final List<Nutrient> nutrient;
        private final List<Texture> texture;
        private final List<CodeableConcept> fluidConsistencyType;
        private final String instruction;

        private volatile int hashCode;

        private OralDiet(Builder builder) {
            super(builder);
            type = Collections.unmodifiableList(builder.type);
            schedule = Collections.unmodifiableList(builder.schedule);
            nutrient = Collections.unmodifiableList(builder.nutrient);
            texture = Collections.unmodifiableList(builder.texture);
            fluidConsistencyType = Collections.unmodifiableList(builder.fluidConsistencyType);
            instruction = builder.instruction;
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * The kind of diet or dietary restriction such as fiber restricted diet or diabetic diet.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getType() {
            return type;
        }

        /**
         * <p>
         * The time period and frequency at which the diet should be given. The diet should be given for the combination of all 
         * schedules if more than one schedule is present.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Timing}.
         */
        public List<Timing> getSchedule() {
            return schedule;
        }

        /**
         * <p>
         * Class that defines the quantity and type of nutrient modifications (for example carbohydrate, fiber or sodium) 
         * required for the oral diet.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Nutrient}.
         */
        public List<Nutrient> getNutrient() {
            return nutrient;
        }

        /**
         * <p>
         * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Texture}.
         */
        public List<Texture> getTexture() {
            return texture;
        }

        /**
         * <p>
         * The required consistency (e.g. honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getFluidConsistencyType() {
            return fluidConsistencyType;
        }

        /**
         * <p>
         * Free text or additional instructions or information pertaining to the oral diet.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getInstruction() {
            return instruction;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                !type.isEmpty() || 
                !schedule.isEmpty() || 
                !nutrient.isEmpty() || 
                !texture.isEmpty() || 
                !fluidConsistencyType.isEmpty() || 
                (instruction != null);
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
                    accept(type, "type", visitor, CodeableConcept.class);
                    accept(schedule, "schedule", visitor, Timing.class);
                    accept(nutrient, "nutrient", visitor, Nutrient.class);
                    accept(texture, "texture", visitor, Texture.class);
                    accept(fluidConsistencyType, "fluidConsistencyType", visitor, CodeableConcept.class);
                    accept(instruction, "instruction", visitor);
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
            OralDiet other = (OralDiet) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(schedule, other.schedule) && 
                Objects.equals(nutrient, other.nutrient) && 
                Objects.equals(texture, other.texture) && 
                Objects.equals(fluidConsistencyType, other.fluidConsistencyType) && 
                Objects.equals(instruction, other.instruction);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    schedule, 
                    nutrient, 
                    texture, 
                    fluidConsistencyType, 
                    instruction);
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
            // optional
            private List<CodeableConcept> type = new ArrayList<>();
            private List<Timing> schedule = new ArrayList<>();
            private List<Nutrient> nutrient = new ArrayList<>();
            private List<Texture> texture = new ArrayList<>();
            private List<CodeableConcept> fluidConsistencyType = new ArrayList<>();
            private String instruction;

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
             * Adds new element(s) to the existing list
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
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * The kind of diet or dietary restriction such as fiber restricted diet or diabetic diet.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param type
             *     Type of oral diet or diet restrictions that describe what can be consumed orally
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept... type) {
                for (CodeableConcept value : type) {
                    this.type.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The kind of diet or dietary restriction such as fiber restricted diet or diabetic diet.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param type
             *     Type of oral diet or diet restrictions that describe what can be consumed orally
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(Collection<CodeableConcept> type) {
                this.type = new ArrayList<>(type);
                return this;
            }

            /**
             * <p>
             * The time period and frequency at which the diet should be given. The diet should be given for the combination of all 
             * schedules if more than one schedule is present.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param schedule
             *     Scheduled frequency of diet
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder schedule(Timing... schedule) {
                for (Timing value : schedule) {
                    this.schedule.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The time period and frequency at which the diet should be given. The diet should be given for the combination of all 
             * schedules if more than one schedule is present.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param schedule
             *     Scheduled frequency of diet
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder schedule(Collection<Timing> schedule) {
                this.schedule = new ArrayList<>(schedule);
                return this;
            }

            /**
             * <p>
             * Class that defines the quantity and type of nutrient modifications (for example carbohydrate, fiber or sodium) 
             * required for the oral diet.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param nutrient
             *     Required nutrient modifications
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
             * <p>
             * Class that defines the quantity and type of nutrient modifications (for example carbohydrate, fiber or sodium) 
             * required for the oral diet.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param nutrient
             *     Required nutrient modifications
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder nutrient(Collection<Nutrient> nutrient) {
                this.nutrient = new ArrayList<>(nutrient);
                return this;
            }

            /**
             * <p>
             * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param texture
             *     Required texture modifications
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder texture(Texture... texture) {
                for (Texture value : texture) {
                    this.texture.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param texture
             *     Required texture modifications
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder texture(Collection<Texture> texture) {
                this.texture = new ArrayList<>(texture);
                return this;
            }

            /**
             * <p>
             * The required consistency (e.g. honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param fluidConsistencyType
             *     The required consistency of fluids and liquids provided to the patient
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder fluidConsistencyType(CodeableConcept... fluidConsistencyType) {
                for (CodeableConcept value : fluidConsistencyType) {
                    this.fluidConsistencyType.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The required consistency (e.g. honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param fluidConsistencyType
             *     The required consistency of fluids and liquids provided to the patient
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder fluidConsistencyType(Collection<CodeableConcept> fluidConsistencyType) {
                this.fluidConsistencyType = new ArrayList<>(fluidConsistencyType);
                return this;
            }

            /**
             * <p>
             * Free text or additional instructions or information pertaining to the oral diet.
             * </p>
             * 
             * @param instruction
             *     Instructions or additional information about the oral diet
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder instruction(String instruction) {
                this.instruction = instruction;
                return this;
            }

            @Override
            public OralDiet build() {
                return new OralDiet(this);
            }

            private Builder from(OralDiet oralDiet) {
                id = oralDiet.id;
                extension.addAll(oralDiet.extension);
                modifierExtension.addAll(oralDiet.modifierExtension);
                type.addAll(oralDiet.type);
                schedule.addAll(oralDiet.schedule);
                nutrient.addAll(oralDiet.nutrient);
                texture.addAll(oralDiet.texture);
                fluidConsistencyType.addAll(oralDiet.fluidConsistencyType);
                instruction = oralDiet.instruction;
                return this;
            }
        }

        /**
         * <p>
         * Class that defines the quantity and type of nutrient modifications (for example carbohydrate, fiber or sodium) 
         * required for the oral diet.
         * </p>
         */
        public static class Nutrient extends BackboneElement {
            private final CodeableConcept modifier;
            private final Quantity amount;

            private volatile int hashCode;

            private Nutrient(Builder builder) {
                super(builder);
                modifier = builder.modifier;
                amount = builder.amount;
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * The nutrient that is being modified such as carbohydrate or sodium.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getModifier() {
                return modifier;
            }

            /**
             * <p>
             * The quantity of the specified nutrient to include in diet.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Quantity}.
             */
            public Quantity getAmount() {
                return amount;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (modifier != null) || 
                    (amount != null);
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
                        accept(modifier, "modifier", visitor);
                        accept(amount, "amount", visitor);
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
                Nutrient other = (Nutrient) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(modifier, other.modifier) && 
                    Objects.equals(amount, other.amount);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        modifier, 
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
                // optional
                private CodeableConcept modifier;
                private Quantity amount;

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
                 * Adds new element(s) to the existing list
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
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * <p>
                 * The nutrient that is being modified such as carbohydrate or sodium.
                 * </p>
                 * 
                 * @param modifier
                 *     Type of nutrient that is being modified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder modifier(CodeableConcept modifier) {
                    this.modifier = modifier;
                    return this;
                }

                /**
                 * <p>
                 * The quantity of the specified nutrient to include in diet.
                 * </p>
                 * 
                 * @param amount
                 *     Quantity of the specified nutrient
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder amount(Quantity amount) {
                    this.amount = amount;
                    return this;
                }

                @Override
                public Nutrient build() {
                    return new Nutrient(this);
                }

                private Builder from(Nutrient nutrient) {
                    id = nutrient.id;
                    extension.addAll(nutrient.extension);
                    modifierExtension.addAll(nutrient.modifierExtension);
                    modifier = nutrient.modifier;
                    amount = nutrient.amount;
                    return this;
                }
            }
        }

        /**
         * <p>
         * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
         * </p>
         */
        public static class Texture extends BackboneElement {
            private final CodeableConcept modifier;
            private final CodeableConcept foodType;

            private volatile int hashCode;

            private Texture(Builder builder) {
                super(builder);
                modifier = builder.modifier;
                foodType = builder.foodType;
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * Any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getModifier() {
                return modifier;
            }

            /**
             * <p>
             * The food type(s) (e.g. meats, all foods) that the texture modification applies to. This could be all foods types.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getFoodType() {
                return foodType;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (modifier != null) || 
                    (foodType != null);
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
                        accept(modifier, "modifier", visitor);
                        accept(foodType, "foodType", visitor);
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
                Texture other = (Texture) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(modifier, other.modifier) && 
                    Objects.equals(foodType, other.foodType);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        modifier, 
                        foodType);
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
                // optional
                private CodeableConcept modifier;
                private CodeableConcept foodType;

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
                 * Adds new element(s) to the existing list
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
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * <p>
                 * Any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.
                 * </p>
                 * 
                 * @param modifier
                 *     Code to indicate how to alter the texture of the foods, e.g. pureed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder modifier(CodeableConcept modifier) {
                    this.modifier = modifier;
                    return this;
                }

                /**
                 * <p>
                 * The food type(s) (e.g. meats, all foods) that the texture modification applies to. This could be all foods types.
                 * </p>
                 * 
                 * @param foodType
                 *     Concepts that are used to identify an entity that is ingested for nutritional purposes
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder foodType(CodeableConcept foodType) {
                    this.foodType = foodType;
                    return this;
                }

                @Override
                public Texture build() {
                    return new Texture(this);
                }

                private Builder from(Texture texture) {
                    id = texture.id;
                    extension.addAll(texture.extension);
                    modifierExtension.addAll(texture.modifierExtension);
                    modifier = texture.modifier;
                    foodType = texture.foodType;
                    return this;
                }
            }
        }
    }

    /**
     * <p>
     * Oral nutritional products given in order to add further nutritional value to the patient's diet.
     * </p>
     */
    public static class Supplement extends BackboneElement {
        private final CodeableConcept type;
        private final String productName;
        private final List<Timing> schedule;
        private final Quantity quantity;
        private final String instruction;

        private volatile int hashCode;

        private Supplement(Builder builder) {
            super(builder);
            type = builder.type;
            productName = builder.productName;
            schedule = Collections.unmodifiableList(builder.schedule);
            quantity = builder.quantity;
            instruction = builder.instruction;
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * The kind of nutritional supplement product required such as a high protein or pediatric clear liquid supplement.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * <p>
         * The product or brand name of the nutritional supplement such as "Acme Protein Shake".
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getProductName() {
            return productName;
        }

        /**
         * <p>
         * The time period and frequency at which the supplement(s) should be given. The supplement should be given for the 
         * combination of all schedules if more than one schedule is present.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Timing}.
         */
        public List<Timing> getSchedule() {
            return schedule;
        }

        /**
         * <p>
         * The amount of the nutritional supplement to be given.
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
         * Free text or additional instructions or information pertaining to the oral supplement.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getInstruction() {
            return instruction;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (productName != null) || 
                !schedule.isEmpty() || 
                (quantity != null) || 
                (instruction != null);
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
                    accept(type, "type", visitor);
                    accept(productName, "productName", visitor);
                    accept(schedule, "schedule", visitor, Timing.class);
                    accept(quantity, "quantity", visitor);
                    accept(instruction, "instruction", visitor);
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
            Supplement other = (Supplement) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(productName, other.productName) && 
                Objects.equals(schedule, other.schedule) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(instruction, other.instruction);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    productName, 
                    schedule, 
                    quantity, 
                    instruction);
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
            // optional
            private CodeableConcept type;
            private String productName;
            private List<Timing> schedule = new ArrayList<>();
            private Quantity quantity;
            private String instruction;

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
             * Adds new element(s) to the existing list
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
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * The kind of nutritional supplement product required such as a high protein or pediatric clear liquid supplement.
             * </p>
             * 
             * @param type
             *     Type of supplement product requested
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * <p>
             * The product or brand name of the nutritional supplement such as "Acme Protein Shake".
             * </p>
             * 
             * @param productName
             *     Product or brand name of the nutritional supplement
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder productName(String productName) {
                this.productName = productName;
                return this;
            }

            /**
             * <p>
             * The time period and frequency at which the supplement(s) should be given. The supplement should be given for the 
             * combination of all schedules if more than one schedule is present.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param schedule
             *     Scheduled frequency of supplement
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder schedule(Timing... schedule) {
                for (Timing value : schedule) {
                    this.schedule.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The time period and frequency at which the supplement(s) should be given. The supplement should be given for the 
             * combination of all schedules if more than one schedule is present.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param schedule
             *     Scheduled frequency of supplement
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder schedule(Collection<Timing> schedule) {
                this.schedule = new ArrayList<>(schedule);
                return this;
            }

            /**
             * <p>
             * The amount of the nutritional supplement to be given.
             * </p>
             * 
             * @param quantity
             *     Amount of the nutritional supplement
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(Quantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * <p>
             * Free text or additional instructions or information pertaining to the oral supplement.
             * </p>
             * 
             * @param instruction
             *     Instructions or additional information about the oral supplement
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder instruction(String instruction) {
                this.instruction = instruction;
                return this;
            }

            @Override
            public Supplement build() {
                return new Supplement(this);
            }

            private Builder from(Supplement supplement) {
                id = supplement.id;
                extension.addAll(supplement.extension);
                modifierExtension.addAll(supplement.modifierExtension);
                type = supplement.type;
                productName = supplement.productName;
                schedule.addAll(supplement.schedule);
                quantity = supplement.quantity;
                instruction = supplement.instruction;
                return this;
            }
        }
    }

    /**
     * <p>
     * Feeding provided through the gastrointestinal tract via a tube, catheter, or stoma that delivers nutrition distal to 
     * the oral cavity.
     * </p>
     */
    public static class EnteralFormula extends BackboneElement {
        private final CodeableConcept baseFormulaType;
        private final String baseFormulaProductName;
        private final CodeableConcept additiveType;
        private final String additiveProductName;
        private final Quantity caloricDensity;
        private final CodeableConcept routeofAdministration;
        private final List<Administration> administration;
        private final Quantity maxVolumeToDeliver;
        private final String administrationInstruction;

        private volatile int hashCode;

        private EnteralFormula(Builder builder) {
            super(builder);
            baseFormulaType = builder.baseFormulaType;
            baseFormulaProductName = builder.baseFormulaProductName;
            additiveType = builder.additiveType;
            additiveProductName = builder.additiveProductName;
            caloricDensity = builder.caloricDensity;
            routeofAdministration = builder.routeofAdministration;
            administration = Collections.unmodifiableList(builder.administration);
            maxVolumeToDeliver = builder.maxVolumeToDeliver;
            administrationInstruction = builder.administrationInstruction;
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * The type of enteral or infant formula such as an adult standard formula with fiber or a soy-based infant formula.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getBaseFormulaType() {
            return baseFormulaType;
        }

        /**
         * <p>
         * The product or brand name of the enteral or infant formula product such as "ACME Adult Standard Formula".
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getBaseFormulaProductName() {
            return baseFormulaProductName;
        }

        /**
         * <p>
         * Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or 
         * mixed with the base formula.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getAdditiveType() {
            return additiveType;
        }

        /**
         * <p>
         * The product or brand name of the type of modular component to be added to the formula.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getAdditiveProductName() {
            return additiveProductName;
        }

        /**
         * <p>
         * The amount of energy (calories) that the formula should provide per specified volume, typically per mL or fluid oz. 
         * For example, an infant may require a formula that provides 24 calories per fluid ounce or an adult may require an 
         * enteral formula that provides 1.5 calorie/mL.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getCaloricDensity() {
            return caloricDensity;
        }

        /**
         * <p>
         * The route or physiological path of administration into the patient's gastrointestinal tract for purposes of providing 
         * the formula feeding, e.g. nasogastric tube.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getRouteofAdministration() {
            return routeofAdministration;
        }

        /**
         * <p>
         * Formula administration instructions as structured data. This repeating structure allows for changing the 
         * administration rate or volume over time for both bolus and continuous feeding. An example of this would be an 
         * instruction to increase the rate of continuous feeding every 2 hours.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Administration}.
         */
        public List<Administration> getAdministration() {
            return administration;
        }

        /**
         * <p>
         * The maximum total quantity of formula that may be administered to a subject over the period of time, e.g. 1440 mL over 
         * 24 hours.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getMaxVolumeToDeliver() {
            return maxVolumeToDeliver;
        }

        /**
         * <p>
         * Free text formula administration, feeding instructions or additional instructions or information.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getAdministrationInstruction() {
            return administrationInstruction;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (baseFormulaType != null) || 
                (baseFormulaProductName != null) || 
                (additiveType != null) || 
                (additiveProductName != null) || 
                (caloricDensity != null) || 
                (routeofAdministration != null) || 
                !administration.isEmpty() || 
                (maxVolumeToDeliver != null) || 
                (administrationInstruction != null);
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
                    accept(baseFormulaType, "baseFormulaType", visitor);
                    accept(baseFormulaProductName, "baseFormulaProductName", visitor);
                    accept(additiveType, "additiveType", visitor);
                    accept(additiveProductName, "additiveProductName", visitor);
                    accept(caloricDensity, "caloricDensity", visitor);
                    accept(routeofAdministration, "routeofAdministration", visitor);
                    accept(administration, "administration", visitor, Administration.class);
                    accept(maxVolumeToDeliver, "maxVolumeToDeliver", visitor);
                    accept(administrationInstruction, "administrationInstruction", visitor);
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
            EnteralFormula other = (EnteralFormula) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(baseFormulaType, other.baseFormulaType) && 
                Objects.equals(baseFormulaProductName, other.baseFormulaProductName) && 
                Objects.equals(additiveType, other.additiveType) && 
                Objects.equals(additiveProductName, other.additiveProductName) && 
                Objects.equals(caloricDensity, other.caloricDensity) && 
                Objects.equals(routeofAdministration, other.routeofAdministration) && 
                Objects.equals(administration, other.administration) && 
                Objects.equals(maxVolumeToDeliver, other.maxVolumeToDeliver) && 
                Objects.equals(administrationInstruction, other.administrationInstruction);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    baseFormulaType, 
                    baseFormulaProductName, 
                    additiveType, 
                    additiveProductName, 
                    caloricDensity, 
                    routeofAdministration, 
                    administration, 
                    maxVolumeToDeliver, 
                    administrationInstruction);
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
            // optional
            private CodeableConcept baseFormulaType;
            private String baseFormulaProductName;
            private CodeableConcept additiveType;
            private String additiveProductName;
            private Quantity caloricDensity;
            private CodeableConcept routeofAdministration;
            private List<Administration> administration = new ArrayList<>();
            private Quantity maxVolumeToDeliver;
            private String administrationInstruction;

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
             * Adds new element(s) to the existing list
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
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * The type of enteral or infant formula such as an adult standard formula with fiber or a soy-based infant formula.
             * </p>
             * 
             * @param baseFormulaType
             *     Type of enteral or infant formula
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder baseFormulaType(CodeableConcept baseFormulaType) {
                this.baseFormulaType = baseFormulaType;
                return this;
            }

            /**
             * <p>
             * The product or brand name of the enteral or infant formula product such as "ACME Adult Standard Formula".
             * </p>
             * 
             * @param baseFormulaProductName
             *     Product or brand name of the enteral or infant formula
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder baseFormulaProductName(String baseFormulaProductName) {
                this.baseFormulaProductName = baseFormulaProductName;
                return this;
            }

            /**
             * <p>
             * Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or 
             * mixed with the base formula.
             * </p>
             * 
             * @param additiveType
             *     Type of modular component to add to the feeding
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder additiveType(CodeableConcept additiveType) {
                this.additiveType = additiveType;
                return this;
            }

            /**
             * <p>
             * The product or brand name of the type of modular component to be added to the formula.
             * </p>
             * 
             * @param additiveProductName
             *     Product or brand name of the modular additive
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder additiveProductName(String additiveProductName) {
                this.additiveProductName = additiveProductName;
                return this;
            }

            /**
             * <p>
             * The amount of energy (calories) that the formula should provide per specified volume, typically per mL or fluid oz. 
             * For example, an infant may require a formula that provides 24 calories per fluid ounce or an adult may require an 
             * enteral formula that provides 1.5 calorie/mL.
             * </p>
             * 
             * @param caloricDensity
             *     Amount of energy per specified volume that is required
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder caloricDensity(Quantity caloricDensity) {
                this.caloricDensity = caloricDensity;
                return this;
            }

            /**
             * <p>
             * The route or physiological path of administration into the patient's gastrointestinal tract for purposes of providing 
             * the formula feeding, e.g. nasogastric tube.
             * </p>
             * 
             * @param routeofAdministration
             *     How the formula should enter the patient's gastrointestinal tract
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder routeofAdministration(CodeableConcept routeofAdministration) {
                this.routeofAdministration = routeofAdministration;
                return this;
            }

            /**
             * <p>
             * Formula administration instructions as structured data. This repeating structure allows for changing the 
             * administration rate or volume over time for both bolus and continuous feeding. An example of this would be an 
             * instruction to increase the rate of continuous feeding every 2 hours.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param administration
             *     Formula feeding instruction as structured data
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder administration(Administration... administration) {
                for (Administration value : administration) {
                    this.administration.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Formula administration instructions as structured data. This repeating structure allows for changing the 
             * administration rate or volume over time for both bolus and continuous feeding. An example of this would be an 
             * instruction to increase the rate of continuous feeding every 2 hours.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param administration
             *     Formula feeding instruction as structured data
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder administration(Collection<Administration> administration) {
                this.administration = new ArrayList<>(administration);
                return this;
            }

            /**
             * <p>
             * The maximum total quantity of formula that may be administered to a subject over the period of time, e.g. 1440 mL over 
             * 24 hours.
             * </p>
             * 
             * @param maxVolumeToDeliver
             *     Upper limit on formula volume per unit of time
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder maxVolumeToDeliver(Quantity maxVolumeToDeliver) {
                this.maxVolumeToDeliver = maxVolumeToDeliver;
                return this;
            }

            /**
             * <p>
             * Free text formula administration, feeding instructions or additional instructions or information.
             * </p>
             * 
             * @param administrationInstruction
             *     Formula feeding instructions expressed as text
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder administrationInstruction(String administrationInstruction) {
                this.administrationInstruction = administrationInstruction;
                return this;
            }

            @Override
            public EnteralFormula build() {
                return new EnteralFormula(this);
            }

            private Builder from(EnteralFormula enteralFormula) {
                id = enteralFormula.id;
                extension.addAll(enteralFormula.extension);
                modifierExtension.addAll(enteralFormula.modifierExtension);
                baseFormulaType = enteralFormula.baseFormulaType;
                baseFormulaProductName = enteralFormula.baseFormulaProductName;
                additiveType = enteralFormula.additiveType;
                additiveProductName = enteralFormula.additiveProductName;
                caloricDensity = enteralFormula.caloricDensity;
                routeofAdministration = enteralFormula.routeofAdministration;
                administration.addAll(enteralFormula.administration);
                maxVolumeToDeliver = enteralFormula.maxVolumeToDeliver;
                administrationInstruction = enteralFormula.administrationInstruction;
                return this;
            }
        }

        /**
         * <p>
         * Formula administration instructions as structured data. This repeating structure allows for changing the 
         * administration rate or volume over time for both bolus and continuous feeding. An example of this would be an 
         * instruction to increase the rate of continuous feeding every 2 hours.
         * </p>
         */
        public static class Administration extends BackboneElement {
            private final Timing schedule;
            private final Quantity quantity;
            private final Element rate;

            private volatile int hashCode;

            private Administration(Builder builder) {
                super(builder);
                schedule = builder.schedule;
                quantity = builder.quantity;
                rate = ValidationSupport.choiceElement(builder.rate, "rate", Quantity.class, Ratio.class);
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * The time period and frequency at which the enteral formula should be delivered to the patient.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Timing}.
             */
            public Timing getSchedule() {
                return schedule;
            }

            /**
             * <p>
             * The volume of formula to provide to the patient per the specified administration schedule.
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
             * The rate of administration of formula via a feeding pump, e.g. 60 mL per hour, according to the specified schedule.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getRate() {
                return rate;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (schedule != null) || 
                    (quantity != null) || 
                    (rate != null);
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
                        accept(schedule, "schedule", visitor);
                        accept(quantity, "quantity", visitor);
                        accept(rate, "rate", visitor);
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
                Administration other = (Administration) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(schedule, other.schedule) && 
                    Objects.equals(quantity, other.quantity) && 
                    Objects.equals(rate, other.rate);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        schedule, 
                        quantity, 
                        rate);
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
                // optional
                private Timing schedule;
                private Quantity quantity;
                private Element rate;

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
                 * Adds new element(s) to the existing list
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
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * <p>
                 * The time period and frequency at which the enteral formula should be delivered to the patient.
                 * </p>
                 * 
                 * @param schedule
                 *     Scheduled frequency of enteral feeding
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder schedule(Timing schedule) {
                    this.schedule = schedule;
                    return this;
                }

                /**
                 * <p>
                 * The volume of formula to provide to the patient per the specified administration schedule.
                 * </p>
                 * 
                 * @param quantity
                 *     The volume of formula to provide
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder quantity(Quantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * <p>
                 * The rate of administration of formula via a feeding pump, e.g. 60 mL per hour, according to the specified schedule.
                 * </p>
                 * 
                 * @param rate
                 *     Speed with which the formula is provided per period of time
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder rate(Element rate) {
                    this.rate = rate;
                    return this;
                }

                @Override
                public Administration build() {
                    return new Administration(this);
                }

                private Builder from(Administration administration) {
                    id = administration.id;
                    extension.addAll(administration.extension);
                    modifierExtension.addAll(administration.modifierExtension);
                    schedule = administration.schedule;
                    quantity = administration.quantity;
                    rate = administration.rate;
                    return this;
                }
            }
        }
    }
}
