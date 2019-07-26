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

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.MedicationKnowledgeStatus;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Information about a medication that is used to support knowledge.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MedicationKnowledge extends DomainResource {
    private final CodeableConcept code;
    private final MedicationKnowledgeStatus status;
    private final Reference manufacturer;
    private final CodeableConcept doseForm;
    private final Quantity amount;
    private final List<String> synonym;
    private final List<RelatedMedicationKnowledge> relatedMedicationKnowledge;
    private final List<Reference> associatedMedication;
    private final List<CodeableConcept> productType;
    private final List<Monograph> monograph;
    private final List<Ingredient> ingredient;
    private final Markdown preparationInstruction;
    private final List<CodeableConcept> intendedRoute;
    private final List<Cost> cost;
    private final List<MonitoringProgram> monitoringProgram;
    private final List<AdministrationGuidelines> administrationGuidelines;
    private final List<MedicineClassification> medicineClassification;
    private final Packaging packaging;
    private final List<DrugCharacteristic> drugCharacteristic;
    private final List<Reference> contraindication;
    private final List<Regulatory> regulatory;
    private final List<Kinetics> kinetics;

    private volatile int hashCode;

    private MedicationKnowledge(Builder builder) {
        super(builder);
        code = builder.code;
        status = builder.status;
        manufacturer = builder.manufacturer;
        doseForm = builder.doseForm;
        amount = builder.amount;
        synonym = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.synonym, "synonym"));
        relatedMedicationKnowledge = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.relatedMedicationKnowledge, "relatedMedicationKnowledge"));
        associatedMedication = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.associatedMedication, "associatedMedication"));
        productType = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.productType, "productType"));
        monograph = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.monograph, "monograph"));
        ingredient = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.ingredient, "ingredient"));
        preparationInstruction = builder.preparationInstruction;
        intendedRoute = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.intendedRoute, "intendedRoute"));
        cost = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.cost, "cost"));
        monitoringProgram = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.monitoringProgram, "monitoringProgram"));
        administrationGuidelines = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.administrationGuidelines, "administrationGuidelines"));
        medicineClassification = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.medicineClassification, "medicineClassification"));
        packaging = builder.packaging;
        drugCharacteristic = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.drugCharacteristic, "drugCharacteristic"));
        contraindication = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.contraindication, "contraindication"));
        regulatory = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.regulatory, "regulatory"));
        kinetics = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.kinetics, "kinetics"));
    }

    /**
     * <p>
     * A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a 
     * standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local 
     * formulary code, optionally with translations to other code systems.
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
     * A code to indicate if the medication is in active use. The status refers to the validity about the information of the 
     * medication and not to its medicinal properties.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MedicationKnowledgeStatus}.
     */
    public MedicationKnowledgeStatus getStatus() {
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
    public CodeableConcept getDoseForm() {
        return doseForm;
    }

    /**
     * <p>
     * Specific amount of the drug in the packaged product. For example, when specifying a product that has the same strength 
     * (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional 
     * clarification of the package amount (For example, 3 mL, 10mL, etc.).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getAmount() {
        return amount;
    }

    /**
     * <p>
     * Additional names for a medication, for example, the name(s) given to a medication in different countries. For example, 
     * acetaminophen and paracetamol or salbutamol and albuterol.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String}.
     */
    public List<String> getSynonym() {
        return synonym;
    }

    /**
     * <p>
     * Associated or related knowledge about a medication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatedMedicationKnowledge}.
     */
    public List<RelatedMedicationKnowledge> getRelatedMedicationKnowledge() {
        return relatedMedicationKnowledge;
    }

    /**
     * <p>
     * Associated or related medications. For example, if the medication is a branded product (e.g. Crestor), this is the 
     * Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a 
     * branded product (e.g. Crestor).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAssociatedMedication() {
        return associatedMedication;
    }

    /**
     * <p>
     * Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, 
     * etc.).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getProductType() {
        return productType;
    }

    /**
     * <p>
     * Associated documentation about the medication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Monograph}.
     */
    public List<Monograph> getMonograph() {
        return monograph;
    }

    /**
     * <p>
     * Identifies a particular constituent of interest in the product.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Ingredient}.
     */
    public List<Ingredient> getIngredient() {
        return ingredient;
    }

    /**
     * <p>
     * The instructions for preparing the medication.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getPreparationInstruction() {
        return preparationInstruction;
    }

    /**
     * <p>
     * The intended or approved route of administration.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getIntendedRoute() {
        return intendedRoute;
    }

    /**
     * <p>
     * The price of the medication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Cost}.
     */
    public List<Cost> getCost() {
        return cost;
    }

    /**
     * <p>
     * The program under which the medication is reviewed.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MonitoringProgram}.
     */
    public List<MonitoringProgram> getMonitoringProgram() {
        return monitoringProgram;
    }

    /**
     * <p>
     * Guidelines for the administration of the medication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link AdministrationGuidelines}.
     */
    public List<AdministrationGuidelines> getAdministrationGuidelines() {
        return administrationGuidelines;
    }

    /**
     * <p>
     * Categorization of the medication within a formulary or classification system.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MedicineClassification}.
     */
    public List<MedicineClassification> getMedicineClassification() {
        return medicineClassification;
    }

    /**
     * <p>
     * Information that only applies to packages (not products).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Packaging}.
     */
    public Packaging getPackaging() {
        return packaging;
    }

    /**
     * <p>
     * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DrugCharacteristic}.
     */
    public List<DrugCharacteristic> getDrugCharacteristic() {
        return drugCharacteristic;
    }

    /**
     * <p>
     * Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease 
     * contraindication, drug-allergy interaction, etc.).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getContraindication() {
        return contraindication;
    }

    /**
     * <p>
     * Regulatory information about a medication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Regulatory}.
     */
    public List<Regulatory> getRegulatory() {
        return regulatory;
    }

    /**
     * <p>
     * The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Kinetics}.
     */
    public List<Kinetics> getKinetics() {
        return kinetics;
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
                accept(code, "code", visitor);
                accept(status, "status", visitor);
                accept(manufacturer, "manufacturer", visitor);
                accept(doseForm, "doseForm", visitor);
                accept(amount, "amount", visitor);
                accept(synonym, "synonym", visitor, String.class);
                accept(relatedMedicationKnowledge, "relatedMedicationKnowledge", visitor, RelatedMedicationKnowledge.class);
                accept(associatedMedication, "associatedMedication", visitor, Reference.class);
                accept(productType, "productType", visitor, CodeableConcept.class);
                accept(monograph, "monograph", visitor, Monograph.class);
                accept(ingredient, "ingredient", visitor, Ingredient.class);
                accept(preparationInstruction, "preparationInstruction", visitor);
                accept(intendedRoute, "intendedRoute", visitor, CodeableConcept.class);
                accept(cost, "cost", visitor, Cost.class);
                accept(monitoringProgram, "monitoringProgram", visitor, MonitoringProgram.class);
                accept(administrationGuidelines, "administrationGuidelines", visitor, AdministrationGuidelines.class);
                accept(medicineClassification, "medicineClassification", visitor, MedicineClassification.class);
                accept(packaging, "packaging", visitor);
                accept(drugCharacteristic, "drugCharacteristic", visitor, DrugCharacteristic.class);
                accept(contraindication, "contraindication", visitor, Reference.class);
                accept(regulatory, "regulatory", visitor, Regulatory.class);
                accept(kinetics, "kinetics", visitor, Kinetics.class);
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
        MedicationKnowledge other = (MedicationKnowledge) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(code, other.code) && 
            Objects.equals(status, other.status) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(doseForm, other.doseForm) && 
            Objects.equals(amount, other.amount) && 
            Objects.equals(synonym, other.synonym) && 
            Objects.equals(relatedMedicationKnowledge, other.relatedMedicationKnowledge) && 
            Objects.equals(associatedMedication, other.associatedMedication) && 
            Objects.equals(productType, other.productType) && 
            Objects.equals(monograph, other.monograph) && 
            Objects.equals(ingredient, other.ingredient) && 
            Objects.equals(preparationInstruction, other.preparationInstruction) && 
            Objects.equals(intendedRoute, other.intendedRoute) && 
            Objects.equals(cost, other.cost) && 
            Objects.equals(monitoringProgram, other.monitoringProgram) && 
            Objects.equals(administrationGuidelines, other.administrationGuidelines) && 
            Objects.equals(medicineClassification, other.medicineClassification) && 
            Objects.equals(packaging, other.packaging) && 
            Objects.equals(drugCharacteristic, other.drugCharacteristic) && 
            Objects.equals(contraindication, other.contraindication) && 
            Objects.equals(regulatory, other.regulatory) && 
            Objects.equals(kinetics, other.kinetics);
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
                code, 
                status, 
                manufacturer, 
                doseForm, 
                amount, 
                synonym, 
                relatedMedicationKnowledge, 
                associatedMedication, 
                productType, 
                monograph, 
                ingredient, 
                preparationInstruction, 
                intendedRoute, 
                cost, 
                monitoringProgram, 
                administrationGuidelines, 
                medicineClassification, 
                packaging, 
                drugCharacteristic, 
                contraindication, 
                regulatory, 
                kinetics);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        Builder builder = new Builder();
        return builder;
    }

    public static class Builder extends DomainResource.Builder {
        private CodeableConcept code;
        private MedicationKnowledgeStatus status;
        private Reference manufacturer;
        private CodeableConcept doseForm;
        private Quantity amount;
        private List<String> synonym = new ArrayList<>();
        private List<RelatedMedicationKnowledge> relatedMedicationKnowledge = new ArrayList<>();
        private List<Reference> associatedMedication = new ArrayList<>();
        private List<CodeableConcept> productType = new ArrayList<>();
        private List<Monograph> monograph = new ArrayList<>();
        private List<Ingredient> ingredient = new ArrayList<>();
        private Markdown preparationInstruction;
        private List<CodeableConcept> intendedRoute = new ArrayList<>();
        private List<Cost> cost = new ArrayList<>();
        private List<MonitoringProgram> monitoringProgram = new ArrayList<>();
        private List<AdministrationGuidelines> administrationGuidelines = new ArrayList<>();
        private List<MedicineClassification> medicineClassification = new ArrayList<>();
        private Packaging packaging;
        private List<DrugCharacteristic> drugCharacteristic = new ArrayList<>();
        private List<Reference> contraindication = new ArrayList<>();
        private List<Regulatory> regulatory = new ArrayList<>();
        private List<Kinetics> kinetics = new ArrayList<>();

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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a 
         * standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local 
         * formulary code, optionally with translations to other code systems.
         * </p>
         * 
         * @param code
         *     Code that identifies this medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * <p>
         * A code to indicate if the medication is in active use. The status refers to the validity about the information of the 
         * medication and not to its medicinal properties.
         * </p>
         * 
         * @param status
         *     active | inactive | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(MedicationKnowledgeStatus status) {
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
         *     A reference to this Builder instance
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
         * @param doseForm
         *     powder | tablets | capsule +
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder doseForm(CodeableConcept doseForm) {
            this.doseForm = doseForm;
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
         *     A reference to this Builder instance
         */
        public Builder amount(Quantity amount) {
            this.amount = amount;
            return this;
        }

        /**
         * <p>
         * Additional names for a medication, for example, the name(s) given to a medication in different countries. For example, 
         * acetaminophen and paracetamol or salbutamol and albuterol.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param synonym
         *     Additional names for a medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder synonym(String... synonym) {
            for (String value : synonym) {
                this.synonym.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Additional names for a medication, for example, the name(s) given to a medication in different countries. For example, 
         * acetaminophen and paracetamol or salbutamol and albuterol.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param synonym
         *     Additional names for a medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder synonym(Collection<String> synonym) {
            this.synonym = new ArrayList<>(synonym);
            return this;
        }

        /**
         * <p>
         * Associated or related knowledge about a medication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param relatedMedicationKnowledge
         *     Associated or related medication information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatedMedicationKnowledge(RelatedMedicationKnowledge... relatedMedicationKnowledge) {
            for (RelatedMedicationKnowledge value : relatedMedicationKnowledge) {
                this.relatedMedicationKnowledge.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Associated or related knowledge about a medication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param relatedMedicationKnowledge
         *     Associated or related medication information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatedMedicationKnowledge(Collection<RelatedMedicationKnowledge> relatedMedicationKnowledge) {
            this.relatedMedicationKnowledge = new ArrayList<>(relatedMedicationKnowledge);
            return this;
        }

        /**
         * <p>
         * Associated or related medications. For example, if the medication is a branded product (e.g. Crestor), this is the 
         * Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a 
         * branded product (e.g. Crestor).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param associatedMedication
         *     A medication resource that is associated with this medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder associatedMedication(Reference... associatedMedication) {
            for (Reference value : associatedMedication) {
                this.associatedMedication.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Associated or related medications. For example, if the medication is a branded product (e.g. Crestor), this is the 
         * Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a 
         * branded product (e.g. Crestor).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param associatedMedication
         *     A medication resource that is associated with this medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder associatedMedication(Collection<Reference> associatedMedication) {
            this.associatedMedication = new ArrayList<>(associatedMedication);
            return this;
        }

        /**
         * <p>
         * Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, 
         * etc.).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param productType
         *     Category of the medication or product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder productType(CodeableConcept... productType) {
            for (CodeableConcept value : productType) {
                this.productType.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, 
         * etc.).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param productType
         *     Category of the medication or product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder productType(Collection<CodeableConcept> productType) {
            this.productType = new ArrayList<>(productType);
            return this;
        }

        /**
         * <p>
         * Associated documentation about the medication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param monograph
         *     Associated documentation about the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder monograph(Monograph... monograph) {
            for (Monograph value : monograph) {
                this.monograph.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Associated documentation about the medication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param monograph
         *     Associated documentation about the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder monograph(Collection<Monograph> monograph) {
            this.monograph = new ArrayList<>(monograph);
            return this;
        }

        /**
         * <p>
         * Identifies a particular constituent of interest in the product.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param ingredient
         *     Active or inactive ingredient
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
         * <p>
         * Identifies a particular constituent of interest in the product.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param ingredient
         *     Active or inactive ingredient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder ingredient(Collection<Ingredient> ingredient) {
            this.ingredient = new ArrayList<>(ingredient);
            return this;
        }

        /**
         * <p>
         * The instructions for preparing the medication.
         * </p>
         * 
         * @param preparationInstruction
         *     The instructions for preparing the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder preparationInstruction(Markdown preparationInstruction) {
            this.preparationInstruction = preparationInstruction;
            return this;
        }

        /**
         * <p>
         * The intended or approved route of administration.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param intendedRoute
         *     The intended or approved route of administration
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder intendedRoute(CodeableConcept... intendedRoute) {
            for (CodeableConcept value : intendedRoute) {
                this.intendedRoute.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The intended or approved route of administration.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param intendedRoute
         *     The intended or approved route of administration
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder intendedRoute(Collection<CodeableConcept> intendedRoute) {
            this.intendedRoute = new ArrayList<>(intendedRoute);
            return this;
        }

        /**
         * <p>
         * The price of the medication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param cost
         *     The pricing of the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder cost(Cost... cost) {
            for (Cost value : cost) {
                this.cost.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The price of the medication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param cost
         *     The pricing of the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder cost(Collection<Cost> cost) {
            this.cost = new ArrayList<>(cost);
            return this;
        }

        /**
         * <p>
         * The program under which the medication is reviewed.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param monitoringProgram
         *     Program under which a medication is reviewed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder monitoringProgram(MonitoringProgram... monitoringProgram) {
            for (MonitoringProgram value : monitoringProgram) {
                this.monitoringProgram.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The program under which the medication is reviewed.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param monitoringProgram
         *     Program under which a medication is reviewed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder monitoringProgram(Collection<MonitoringProgram> monitoringProgram) {
            this.monitoringProgram = new ArrayList<>(monitoringProgram);
            return this;
        }

        /**
         * <p>
         * Guidelines for the administration of the medication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param administrationGuidelines
         *     Guidelines for administration of the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder administrationGuidelines(AdministrationGuidelines... administrationGuidelines) {
            for (AdministrationGuidelines value : administrationGuidelines) {
                this.administrationGuidelines.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Guidelines for the administration of the medication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param administrationGuidelines
         *     Guidelines for administration of the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder administrationGuidelines(Collection<AdministrationGuidelines> administrationGuidelines) {
            this.administrationGuidelines = new ArrayList<>(administrationGuidelines);
            return this;
        }

        /**
         * <p>
         * Categorization of the medication within a formulary or classification system.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param medicineClassification
         *     Categorization of the medication within a formulary or classification system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder medicineClassification(MedicineClassification... medicineClassification) {
            for (MedicineClassification value : medicineClassification) {
                this.medicineClassification.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Categorization of the medication within a formulary or classification system.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param medicineClassification
         *     Categorization of the medication within a formulary or classification system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder medicineClassification(Collection<MedicineClassification> medicineClassification) {
            this.medicineClassification = new ArrayList<>(medicineClassification);
            return this;
        }

        /**
         * <p>
         * Information that only applies to packages (not products).
         * </p>
         * 
         * @param packaging
         *     Details about packaged medications
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder packaging(Packaging packaging) {
            this.packaging = packaging;
            return this;
        }

        /**
         * <p>
         * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param drugCharacteristic
         *     Specifies descriptive properties of the medicine
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder drugCharacteristic(DrugCharacteristic... drugCharacteristic) {
            for (DrugCharacteristic value : drugCharacteristic) {
                this.drugCharacteristic.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param drugCharacteristic
         *     Specifies descriptive properties of the medicine
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder drugCharacteristic(Collection<DrugCharacteristic> drugCharacteristic) {
            this.drugCharacteristic = new ArrayList<>(drugCharacteristic);
            return this;
        }

        /**
         * <p>
         * Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease 
         * contraindication, drug-allergy interaction, etc.).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param contraindication
         *     Potential clinical issue with or between medication(s)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contraindication(Reference... contraindication) {
            for (Reference value : contraindication) {
                this.contraindication.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease 
         * contraindication, drug-allergy interaction, etc.).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contraindication
         *     Potential clinical issue with or between medication(s)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contraindication(Collection<Reference> contraindication) {
            this.contraindication = new ArrayList<>(contraindication);
            return this;
        }

        /**
         * <p>
         * Regulatory information about a medication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param regulatory
         *     Regulatory information about a medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder regulatory(Regulatory... regulatory) {
            for (Regulatory value : regulatory) {
                this.regulatory.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Regulatory information about a medication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param regulatory
         *     Regulatory information about a medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder regulatory(Collection<Regulatory> regulatory) {
            this.regulatory = new ArrayList<>(regulatory);
            return this;
        }

        /**
         * <p>
         * The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param kinetics
         *     The time course of drug absorption, distribution, metabolism and excretion of a medication from the body
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder kinetics(Kinetics... kinetics) {
            for (Kinetics value : kinetics) {
                this.kinetics.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param kinetics
         *     The time course of drug absorption, distribution, metabolism and excretion of a medication from the body
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder kinetics(Collection<Kinetics> kinetics) {
            this.kinetics = new ArrayList<>(kinetics);
            return this;
        }

        @Override
        public MedicationKnowledge build() {
            return new MedicationKnowledge(this);
        }

        protected Builder from(MedicationKnowledge medicationKnowledge) {
            super.from(medicationKnowledge);
            code = medicationKnowledge.code;
            status = medicationKnowledge.status;
            manufacturer = medicationKnowledge.manufacturer;
            doseForm = medicationKnowledge.doseForm;
            amount = medicationKnowledge.amount;
            synonym.addAll(medicationKnowledge.synonym);
            relatedMedicationKnowledge.addAll(medicationKnowledge.relatedMedicationKnowledge);
            associatedMedication.addAll(medicationKnowledge.associatedMedication);
            productType.addAll(medicationKnowledge.productType);
            monograph.addAll(medicationKnowledge.monograph);
            ingredient.addAll(medicationKnowledge.ingredient);
            preparationInstruction = medicationKnowledge.preparationInstruction;
            intendedRoute.addAll(medicationKnowledge.intendedRoute);
            cost.addAll(medicationKnowledge.cost);
            monitoringProgram.addAll(medicationKnowledge.monitoringProgram);
            administrationGuidelines.addAll(medicationKnowledge.administrationGuidelines);
            medicineClassification.addAll(medicationKnowledge.medicineClassification);
            packaging = medicationKnowledge.packaging;
            drugCharacteristic.addAll(medicationKnowledge.drugCharacteristic);
            contraindication.addAll(medicationKnowledge.contraindication);
            regulatory.addAll(medicationKnowledge.regulatory);
            kinetics.addAll(medicationKnowledge.kinetics);
            return this;
        }
    }

    /**
     * <p>
     * Associated or related knowledge about a medication.
     * </p>
     */
    public static class RelatedMedicationKnowledge extends BackboneElement {
        private final CodeableConcept type;
        private final List<Reference> reference;

        private volatile int hashCode;

        private RelatedMedicationKnowledge(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            reference = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.reference, "reference"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The category of the associated medication knowledge reference.
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
         * Associated documentation about the associated medication knowledge.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getReference() {
            return reference;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                !reference.isEmpty();
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
                    accept(reference, "reference", visitor, Reference.class);
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
            RelatedMedicationKnowledge other = (RelatedMedicationKnowledge) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(reference, other.reference);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    reference);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder(CodeableConcept type, Collection<Reference> reference) {
            Builder builder = new Builder();
            builder.type(type);
            builder.reference(reference);
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private List<Reference> reference = new ArrayList<>();

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
             * Adds new element(s) to existing list
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
             * The category of the associated medication knowledge reference.
             * </p>
             * 
             * @param type
             *     Category of medicationKnowledge
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
             * Associated documentation about the associated medication knowledge.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param reference
             *     Associated documentation about the associated medication knowledge
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reference(Reference... reference) {
                for (Reference value : reference) {
                    this.reference.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Associated documentation about the associated medication knowledge.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param reference
             *     Associated documentation about the associated medication knowledge
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reference(Collection<Reference> reference) {
                this.reference = new ArrayList<>(reference);
                return this;
            }

            @Override
            public RelatedMedicationKnowledge build() {
                return new RelatedMedicationKnowledge(this);
            }

            protected Builder from(RelatedMedicationKnowledge relatedMedicationKnowledge) {
                super.from(relatedMedicationKnowledge);
                type = relatedMedicationKnowledge.type;
                reference.addAll(relatedMedicationKnowledge.reference);
                return this;
            }
        }
    }

    /**
     * <p>
     * Associated documentation about the medication.
     * </p>
     */
    public static class Monograph extends BackboneElement {
        private final CodeableConcept type;
        private final Reference source;

        private volatile int hashCode;

        private Monograph(Builder builder) {
            super(builder);
            type = builder.type;
            source = builder.source;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The category of documentation about the medication. (e.g. professional monograph, patient education monograph).
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
         * Associated documentation about the medication.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getSource() {
            return source;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (source != null);
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
                    accept(source, "source", visitor);
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
            Monograph other = (Monograph) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    source);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            Builder builder = new Builder();
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private Reference source;

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
             * Adds new element(s) to existing list
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
             * The category of documentation about the medication. (e.g. professional monograph, patient education monograph).
             * </p>
             * 
             * @param type
             *     The category of medication document
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
             * Associated documentation about the medication.
             * </p>
             * 
             * @param source
             *     Associated documentation about the medication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Reference source) {
                this.source = source;
                return this;
            }

            @Override
            public Monograph build() {
                return new Monograph(this);
            }

            protected Builder from(Monograph monograph) {
                super.from(monograph);
                type = monograph.type;
                source = monograph.source;
                return this;
            }
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

        private volatile int hashCode;

        private Ingredient(Builder builder) {
            super(builder);
            item = ValidationSupport.requireChoiceElement(builder.item, "item", CodeableConcept.class, Reference.class);
            isActive = builder.isActive;
            strength = builder.strength;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The actual ingredient - either a substance (simple ingredient) or another medication.
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
        public boolean hasChildren() {
            return super.hasChildren() || 
                (item != null) || 
                (isActive != null) || 
                (strength != null);
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
                    accept(isActive, "isActive", visitor);
                    accept(strength, "strength", visitor);
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
                Objects.equals(isActive, other.isActive) && 
                Objects.equals(strength, other.strength);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    item, 
                    isActive, 
                    strength);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder(Element item) {
            Builder builder = new Builder();
            builder.item(item);
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private Element item;
            private Boolean isActive;
            private Ratio strength;

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
             * Adds new element(s) to existing list
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
             * The actual ingredient - either a substance (simple ingredient) or another medication.
             * </p>
             * 
             * @param item
             *     Medication(s) or substance(s) contained in the medication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(Element item) {
                this.item = item;
                return this;
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
             *     A reference to this Builder instance
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
             *     A reference to this Builder instance
             */
            public Builder strength(Ratio strength) {
                this.strength = strength;
                return this;
            }

            @Override
            public Ingredient build() {
                return new Ingredient(this);
            }

            protected Builder from(Ingredient ingredient) {
                super.from(ingredient);
                item = ingredient.item;
                isActive = ingredient.isActive;
                strength = ingredient.strength;
                return this;
            }
        }
    }

    /**
     * <p>
     * The price of the medication.
     * </p>
     */
    public static class Cost extends BackboneElement {
        private final CodeableConcept type;
        private final String source;
        private final Money cost;

        private volatile int hashCode;

        private Cost(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            source = builder.source;
            cost = ValidationSupport.requireNonNull(builder.cost, "cost");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The category of the cost information. For example, manufacturers' cost, patient cost, claim reimbursement cost, actual 
         * acquisition cost.
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
         * The source or owner that assigns the price to the medication.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getSource() {
            return source;
        }

        /**
         * <p>
         * The price of the medication.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getCost() {
            return cost;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (source != null) || 
                (cost != null);
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
                    accept(source, "source", visitor);
                    accept(cost, "cost", visitor);
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
            Cost other = (Cost) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(source, other.source) && 
                Objects.equals(cost, other.cost);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    source, 
                    cost);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder(CodeableConcept type, Money cost) {
            Builder builder = new Builder();
            builder.type(type);
            builder.cost(cost);
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private String source;
            private Money cost;

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
             * Adds new element(s) to existing list
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
             * The category of the cost information. For example, manufacturers' cost, patient cost, claim reimbursement cost, actual 
             * acquisition cost.
             * </p>
             * 
             * @param type
             *     The category of the cost information
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
             * The source or owner that assigns the price to the medication.
             * </p>
             * 
             * @param source
             *     The source or owner for the price information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(String source) {
                this.source = source;
                return this;
            }

            /**
             * <p>
             * The price of the medication.
             * </p>
             * 
             * @param cost
             *     The price of the medication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder cost(Money cost) {
                this.cost = cost;
                return this;
            }

            @Override
            public Cost build() {
                return new Cost(this);
            }

            protected Builder from(Cost cost) {
                super.from(cost);
                type = cost.type;
                source = cost.source;
                this.cost = cost.cost;
                return this;
            }
        }
    }

    /**
     * <p>
     * The program under which the medication is reviewed.
     * </p>
     */
    public static class MonitoringProgram extends BackboneElement {
        private final CodeableConcept type;
        private final String name;

        private volatile int hashCode;

        private MonitoringProgram(Builder builder) {
            super(builder);
            type = builder.type;
            name = builder.name;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Type of program under which the medication is monitored.
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
         * Name of the reviewing program.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getName() {
            return name;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (name != null);
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
                    accept(name, "name", visitor);
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
            MonitoringProgram other = (MonitoringProgram) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(name, other.name);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    name);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            Builder builder = new Builder();
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private String name;

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
             * Adds new element(s) to existing list
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
             * Type of program under which the medication is monitored.
             * </p>
             * 
             * @param type
             *     Type of program under which the medication is monitored
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
             * Name of the reviewing program.
             * </p>
             * 
             * @param name
             *     Name of the reviewing program
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            @Override
            public MonitoringProgram build() {
                return new MonitoringProgram(this);
            }

            protected Builder from(MonitoringProgram monitoringProgram) {
                super.from(monitoringProgram);
                type = monitoringProgram.type;
                name = monitoringProgram.name;
                return this;
            }
        }
    }

    /**
     * <p>
     * Guidelines for the administration of the medication.
     * </p>
     */
    public static class AdministrationGuidelines extends BackboneElement {
        private final List<Dosage> dosage;
        private final Element indication;
        private final List<PatientCharacteristics> patientCharacteristics;

        private volatile int hashCode;

        private AdministrationGuidelines(Builder builder) {
            super(builder);
            dosage = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.dosage, "dosage"));
            indication = ValidationSupport.choiceElement(builder.indication, "indication", CodeableConcept.class, Reference.class);
            patientCharacteristics = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.patientCharacteristics, "patientCharacteristics"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Dosage for the medication for the specific guidelines.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Dosage}.
         */
        public List<Dosage> getDosage() {
            return dosage;
        }

        /**
         * <p>
         * Indication for use that apply to the specific administration guidelines.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getIndication() {
            return indication;
        }

        /**
         * <p>
         * Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight, 
         * gender, etc.).
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PatientCharacteristics}.
         */
        public List<PatientCharacteristics> getPatientCharacteristics() {
            return patientCharacteristics;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !dosage.isEmpty() || 
                (indication != null) || 
                !patientCharacteristics.isEmpty();
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
                    accept(dosage, "dosage", visitor, Dosage.class);
                    accept(indication, "indication", visitor);
                    accept(patientCharacteristics, "patientCharacteristics", visitor, PatientCharacteristics.class);
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
            AdministrationGuidelines other = (AdministrationGuidelines) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(dosage, other.dosage) && 
                Objects.equals(indication, other.indication) && 
                Objects.equals(patientCharacteristics, other.patientCharacteristics);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    dosage, 
                    indication, 
                    patientCharacteristics);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            Builder builder = new Builder();
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private List<Dosage> dosage = new ArrayList<>();
            private Element indication;
            private List<PatientCharacteristics> patientCharacteristics = new ArrayList<>();

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
             * Adds new element(s) to existing list
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
             * Dosage for the medication for the specific guidelines.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param dosage
             *     Dosage for the medication for the specific guidelines
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dosage(Dosage... dosage) {
                for (Dosage value : dosage) {
                    this.dosage.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Dosage for the medication for the specific guidelines.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param dosage
             *     Dosage for the medication for the specific guidelines
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dosage(Collection<Dosage> dosage) {
                this.dosage = new ArrayList<>(dosage);
                return this;
            }

            /**
             * <p>
             * Indication for use that apply to the specific administration guidelines.
             * </p>
             * 
             * @param indication
             *     Indication for use that apply to the specific administration guidelines
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder indication(Element indication) {
                this.indication = indication;
                return this;
            }

            /**
             * <p>
             * Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight, 
             * gender, etc.).
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param patientCharacteristics
             *     Characteristics of the patient that are relevant to the administration guidelines
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder patientCharacteristics(PatientCharacteristics... patientCharacteristics) {
                for (PatientCharacteristics value : patientCharacteristics) {
                    this.patientCharacteristics.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight, 
             * gender, etc.).
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param patientCharacteristics
             *     Characteristics of the patient that are relevant to the administration guidelines
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder patientCharacteristics(Collection<PatientCharacteristics> patientCharacteristics) {
                this.patientCharacteristics = new ArrayList<>(patientCharacteristics);
                return this;
            }

            @Override
            public AdministrationGuidelines build() {
                return new AdministrationGuidelines(this);
            }

            protected Builder from(AdministrationGuidelines administrationGuidelines) {
                super.from(administrationGuidelines);
                dosage.addAll(administrationGuidelines.dosage);
                indication = administrationGuidelines.indication;
                patientCharacteristics.addAll(administrationGuidelines.patientCharacteristics);
                return this;
            }
        }

        /**
         * <p>
         * Dosage for the medication for the specific guidelines.
         * </p>
         */
        public static class Dosage extends BackboneElement {
            private final CodeableConcept type;
            private final List<com.ibm.watsonhealth.fhir.model.type.Dosage> dosage;

            private volatile int hashCode;

            private Dosage(Builder builder) {
                super(builder);
                type = ValidationSupport.requireNonNull(builder.type, "type");
                dosage = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.dosage, "dosage"));
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * The type of dosage (for example, prophylaxis, maintenance, therapeutic, etc.).
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
             * Dosage for the medication for the specific guidelines.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Dosage}.
             */
            public List<com.ibm.watsonhealth.fhir.model.type.Dosage> getDosage() {
                return dosage;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    !dosage.isEmpty();
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
                        accept(dosage, "dosage", visitor, com.ibm.watsonhealth.fhir.model.type.Dosage.class);
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
                Dosage other = (Dosage) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(dosage, other.dosage);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        dosage);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder().from(this);
            }

            public static Builder builder(CodeableConcept type, Collection<com.ibm.watsonhealth.fhir.model.type.Dosage> dosage) {
                Builder builder = new Builder();
                builder.type(type);
                builder.dosage(dosage);
                return builder;
            }

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept type;
                private List<com.ibm.watsonhealth.fhir.model.type.Dosage> dosage = new ArrayList<>();

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
                 * Adds new element(s) to existing list
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
                 * The type of dosage (for example, prophylaxis, maintenance, therapeutic, etc.).
                 * </p>
                 * 
                 * @param type
                 *     Type of dosage
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
                 * Dosage for the medication for the specific guidelines.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param dosage
                 *     Dosage for the medication for the specific guidelines
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder dosage(com.ibm.watsonhealth.fhir.model.type.Dosage... dosage) {
                    for (com.ibm.watsonhealth.fhir.model.type.Dosage value : dosage) {
                        this.dosage.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Dosage for the medication for the specific guidelines.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param dosage
                 *     Dosage for the medication for the specific guidelines
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder dosage(Collection<com.ibm.watsonhealth.fhir.model.type.Dosage> dosage) {
                    this.dosage = new ArrayList<>(dosage);
                    return this;
                }

                @Override
                public Dosage build() {
                    return new Dosage(this);
                }

                protected Builder from(Dosage dosage) {
                    super.from(dosage);
                    type = dosage.type;
                    this.dosage.addAll(dosage.dosage);
                    return this;
                }
            }
        }

        /**
         * <p>
         * Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight, 
         * gender, etc.).
         * </p>
         */
        public static class PatientCharacteristics extends BackboneElement {
            private final Element characteristic;
            private final List<String> value;

            private volatile int hashCode;

            private PatientCharacteristics(Builder builder) {
                super(builder);
                characteristic = ValidationSupport.requireChoiceElement(builder.characteristic, "characteristic", CodeableConcept.class, Quantity.class);
                value = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.value, "value"));
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getCharacteristic() {
                return characteristic;
            }

            /**
             * <p>
             * The specific characteristic (e.g. height, weight, gender, etc.).
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String}.
             */
            public List<String> getValue() {
                return value;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (characteristic != null) || 
                    !value.isEmpty();
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
                        accept(characteristic, "characteristic", visitor);
                        accept(value, "value", visitor, String.class);
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
                PatientCharacteristics other = (PatientCharacteristics) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(characteristic, other.characteristic) && 
                    Objects.equals(value, other.value);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        characteristic, 
                        value);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder().from(this);
            }

            public static Builder builder(Element characteristic) {
                Builder builder = new Builder();
                builder.characteristic(characteristic);
                return builder;
            }

            public static class Builder extends BackboneElement.Builder {
                private Element characteristic;
                private List<String> value = new ArrayList<>();

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
                 * Adds new element(s) to existing list
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
                 * Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).
                 * </p>
                 * 
                 * @param characteristic
                 *     Specific characteristic that is relevant to the administration guideline
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder characteristic(Element characteristic) {
                    this.characteristic = characteristic;
                    return this;
                }

                /**
                 * <p>
                 * The specific characteristic (e.g. height, weight, gender, etc.).
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param value
                 *     The specific characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(String... value) {
                    for (String _value : value) {
                        this.value.add(_value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The specific characteristic (e.g. height, weight, gender, etc.).
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param value
                 *     The specific characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Collection<String> value) {
                    this.value = new ArrayList<>(value);
                    return this;
                }

                @Override
                public PatientCharacteristics build() {
                    return new PatientCharacteristics(this);
                }

                protected Builder from(PatientCharacteristics patientCharacteristics) {
                    super.from(patientCharacteristics);
                    characteristic = patientCharacteristics.characteristic;
                    value.addAll(patientCharacteristics.value);
                    return this;
                }
            }
        }
    }

    /**
     * <p>
     * Categorization of the medication within a formulary or classification system.
     * </p>
     */
    public static class MedicineClassification extends BackboneElement {
        private final CodeableConcept type;
        private final List<CodeableConcept> classification;

        private volatile int hashCode;

        private MedicineClassification(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            classification = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.classification, "classification"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification).
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
         * Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc.).
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getClassification() {
            return classification;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                !classification.isEmpty();
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
                    accept(classification, "classification", visitor, CodeableConcept.class);
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
            MedicineClassification other = (MedicineClassification) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(classification, other.classification);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    classification);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder(CodeableConcept type) {
            Builder builder = new Builder();
            builder.type(type);
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private List<CodeableConcept> classification = new ArrayList<>();

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
             * Adds new element(s) to existing list
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
             * The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification).
             * </p>
             * 
             * @param type
             *     The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification)
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
             * Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc.).
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param classification
             *     Specific category assigned to the medication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder classification(CodeableConcept... classification) {
                for (CodeableConcept value : classification) {
                    this.classification.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc.).
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param classification
             *     Specific category assigned to the medication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder classification(Collection<CodeableConcept> classification) {
                this.classification = new ArrayList<>(classification);
                return this;
            }

            @Override
            public MedicineClassification build() {
                return new MedicineClassification(this);
            }

            protected Builder from(MedicineClassification medicineClassification) {
                super.from(medicineClassification);
                type = medicineClassification.type;
                classification.addAll(medicineClassification.classification);
                return this;
            }
        }
    }

    /**
     * <p>
     * Information that only applies to packages (not products).
     * </p>
     */
    public static class Packaging extends BackboneElement {
        private final CodeableConcept type;
        private final Quantity quantity;

        private volatile int hashCode;

        private Packaging(Builder builder) {
            super(builder);
            type = builder.type;
            quantity = builder.quantity;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, 
         * bottle).
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
         * The number of product units the package would contain if fully loaded.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getQuantity() {
            return quantity;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (quantity != null);
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
                    accept(quantity, "quantity", visitor);
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
            Packaging other = (Packaging) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(quantity, other.quantity);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    quantity);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            Builder builder = new Builder();
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private Quantity quantity;

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
             * Adds new element(s) to existing list
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
             * A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, 
             * bottle).
             * </p>
             * 
             * @param type
             *     A code that defines the specific type of packaging that the medication can be found in
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
             * The number of product units the package would contain if fully loaded.
             * </p>
             * 
             * @param quantity
             *     The number of product units the package would contain if fully loaded
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(Quantity quantity) {
                this.quantity = quantity;
                return this;
            }

            @Override
            public Packaging build() {
                return new Packaging(this);
            }

            protected Builder from(Packaging packaging) {
                super.from(packaging);
                type = packaging.type;
                quantity = packaging.quantity;
                return this;
            }
        }
    }

    /**
     * <p>
     * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
     * </p>
     */
    public static class DrugCharacteristic extends BackboneElement {
        private final CodeableConcept type;
        private final Element value;

        private volatile int hashCode;

        private DrugCharacteristic(Builder builder) {
            super(builder);
            type = builder.type;
            value = ValidationSupport.choiceElement(builder.value, "value", CodeableConcept.class, String.class, Quantity.class, Base64Binary.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint).
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
         * Description of the characteristic.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
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
            DrugCharacteristic other = (DrugCharacteristic) obj;
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
            Builder builder = new Builder();
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private Element value;

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
             * Adds new element(s) to existing list
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
             * A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint).
             * </p>
             * 
             * @param type
             *     Code specifying the type of characteristic of medication
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
             * Description of the characteristic.
             * </p>
             * 
             * @param value
             *     Description of the characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            @Override
            public DrugCharacteristic build() {
                return new DrugCharacteristic(this);
            }

            protected Builder from(DrugCharacteristic drugCharacteristic) {
                super.from(drugCharacteristic);
                type = drugCharacteristic.type;
                value = drugCharacteristic.value;
                return this;
            }
        }
    }

    /**
     * <p>
     * Regulatory information about a medication.
     * </p>
     */
    public static class Regulatory extends BackboneElement {
        private final Reference regulatoryAuthority;
        private final List<Substitution> substitution;
        private final List<Schedule> schedule;
        private final MaxDispense maxDispense;

        private volatile int hashCode;

        private Regulatory(Builder builder) {
            super(builder);
            regulatoryAuthority = ValidationSupport.requireNonNull(builder.regulatoryAuthority, "regulatoryAuthority");
            substitution = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.substitution, "substitution"));
            schedule = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.schedule, "schedule"));
            maxDispense = builder.maxDispense;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The authority that is specifying the regulations.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getRegulatoryAuthority() {
            return regulatoryAuthority;
        }

        /**
         * <p>
         * Specifies if changes are allowed when dispensing a medication from a regulatory perspective.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Substitution}.
         */
        public List<Substitution> getSubstitution() {
            return substitution;
        }

        /**
         * <p>
         * Specifies the schedule of a medication in jurisdiction.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Schedule}.
         */
        public List<Schedule> getSchedule() {
            return schedule;
        }

        /**
         * <p>
         * The maximum number of units of the medication that can be dispensed in a period.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link MaxDispense}.
         */
        public MaxDispense getMaxDispense() {
            return maxDispense;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (regulatoryAuthority != null) || 
                !substitution.isEmpty() || 
                !schedule.isEmpty() || 
                (maxDispense != null);
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
                    accept(regulatoryAuthority, "regulatoryAuthority", visitor);
                    accept(substitution, "substitution", visitor, Substitution.class);
                    accept(schedule, "schedule", visitor, Schedule.class);
                    accept(maxDispense, "maxDispense", visitor);
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
            Regulatory other = (Regulatory) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(regulatoryAuthority, other.regulatoryAuthority) && 
                Objects.equals(substitution, other.substitution) && 
                Objects.equals(schedule, other.schedule) && 
                Objects.equals(maxDispense, other.maxDispense);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    regulatoryAuthority, 
                    substitution, 
                    schedule, 
                    maxDispense);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder(Reference regulatoryAuthority) {
            Builder builder = new Builder();
            builder.regulatoryAuthority(regulatoryAuthority);
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private Reference regulatoryAuthority;
            private List<Substitution> substitution = new ArrayList<>();
            private List<Schedule> schedule = new ArrayList<>();
            private MaxDispense maxDispense;

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
             * Adds new element(s) to existing list
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
             * The authority that is specifying the regulations.
             * </p>
             * 
             * @param regulatoryAuthority
             *     Specifies the authority of the regulation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder regulatoryAuthority(Reference regulatoryAuthority) {
                this.regulatoryAuthority = regulatoryAuthority;
                return this;
            }

            /**
             * <p>
             * Specifies if changes are allowed when dispensing a medication from a regulatory perspective.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param substitution
             *     Specifies if changes are allowed when dispensing a medication from a regulatory perspective
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder substitution(Substitution... substitution) {
                for (Substitution value : substitution) {
                    this.substitution.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Specifies if changes are allowed when dispensing a medication from a regulatory perspective.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param substitution
             *     Specifies if changes are allowed when dispensing a medication from a regulatory perspective
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder substitution(Collection<Substitution> substitution) {
                this.substitution = new ArrayList<>(substitution);
                return this;
            }

            /**
             * <p>
             * Specifies the schedule of a medication in jurisdiction.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param schedule
             *     Specifies the schedule of a medication in jurisdiction
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder schedule(Schedule... schedule) {
                for (Schedule value : schedule) {
                    this.schedule.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Specifies the schedule of a medication in jurisdiction.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param schedule
             *     Specifies the schedule of a medication in jurisdiction
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder schedule(Collection<Schedule> schedule) {
                this.schedule = new ArrayList<>(schedule);
                return this;
            }

            /**
             * <p>
             * The maximum number of units of the medication that can be dispensed in a period.
             * </p>
             * 
             * @param maxDispense
             *     The maximum number of units of the medication that can be dispensed in a period
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder maxDispense(MaxDispense maxDispense) {
                this.maxDispense = maxDispense;
                return this;
            }

            @Override
            public Regulatory build() {
                return new Regulatory(this);
            }

            protected Builder from(Regulatory regulatory) {
                super.from(regulatory);
                regulatoryAuthority = regulatory.regulatoryAuthority;
                substitution.addAll(regulatory.substitution);
                schedule.addAll(regulatory.schedule);
                maxDispense = regulatory.maxDispense;
                return this;
            }
        }

        /**
         * <p>
         * Specifies if changes are allowed when dispensing a medication from a regulatory perspective.
         * </p>
         */
        public static class Substitution extends BackboneElement {
            private final CodeableConcept type;
            private final Boolean allowed;

            private volatile int hashCode;

            private Substitution(Builder builder) {
                super(builder);
                type = ValidationSupport.requireNonNull(builder.type, "type");
                allowed = ValidationSupport.requireNonNull(builder.allowed, "allowed");
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Specifies the type of substitution allowed.
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
             * Specifies if regulation allows for changes in the medication when dispensing.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getAllowed() {
                return allowed;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (allowed != null);
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
                        accept(allowed, "allowed", visitor);
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
                Substitution other = (Substitution) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(allowed, other.allowed);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        allowed);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder().from(this);
            }

            public static Builder builder(CodeableConcept type, Boolean allowed) {
                Builder builder = new Builder();
                builder.type(type);
                builder.allowed(allowed);
                return builder;
            }

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept type;
                private Boolean allowed;

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
                 * Adds new element(s) to existing list
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
                 * Specifies the type of substitution allowed.
                 * </p>
                 * 
                 * @param type
                 *     Specifies the type of substitution allowed
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
                 * Specifies if regulation allows for changes in the medication when dispensing.
                 * </p>
                 * 
                 * @param allowed
                 *     Specifies if regulation allows for changes in the medication when dispensing
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder allowed(Boolean allowed) {
                    this.allowed = allowed;
                    return this;
                }

                @Override
                public Substitution build() {
                    return new Substitution(this);
                }

                protected Builder from(Substitution substitution) {
                    super.from(substitution);
                    type = substitution.type;
                    allowed = substitution.allowed;
                    return this;
                }
            }
        }

        /**
         * <p>
         * Specifies the schedule of a medication in jurisdiction.
         * </p>
         */
        public static class Schedule extends BackboneElement {
            private final CodeableConcept schedule;

            private volatile int hashCode;

            private Schedule(Builder builder) {
                super(builder);
                schedule = ValidationSupport.requireNonNull(builder.schedule, "schedule");
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Specifies the specific drug schedule.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getSchedule() {
                return schedule;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (schedule != null);
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
                        accept(schedule, "schedule", visitor);
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
                Schedule other = (Schedule) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(schedule, other.schedule);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        schedule);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder().from(this);
            }

            public static Builder builder(CodeableConcept schedule) {
                Builder builder = new Builder();
                builder.schedule(schedule);
                return builder;
            }

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept schedule;

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
                 * Adds new element(s) to existing list
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
                 * Specifies the specific drug schedule.
                 * </p>
                 * 
                 * @param schedule
                 *     Specifies the specific drug schedule
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder schedule(CodeableConcept schedule) {
                    this.schedule = schedule;
                    return this;
                }

                @Override
                public Schedule build() {
                    return new Schedule(this);
                }

                protected Builder from(Schedule schedule) {
                    super.from(schedule);
                    this.schedule = schedule.schedule;
                    return this;
                }
            }
        }

        /**
         * <p>
         * The maximum number of units of the medication that can be dispensed in a period.
         * </p>
         */
        public static class MaxDispense extends BackboneElement {
            private final Quantity quantity;
            private final Duration period;

            private volatile int hashCode;

            private MaxDispense(Builder builder) {
                super(builder);
                quantity = ValidationSupport.requireNonNull(builder.quantity, "quantity");
                period = builder.period;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * The maximum number of units of the medication that can be dispensed.
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
             * The period that applies to the maximum number of units.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Duration}.
             */
            public Duration getPeriod() {
                return period;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (quantity != null) || 
                    (period != null);
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
                        accept(period, "period", visitor);
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
                MaxDispense other = (MaxDispense) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(quantity, other.quantity) && 
                    Objects.equals(period, other.period);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        quantity, 
                        period);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder().from(this);
            }

            public static Builder builder(Quantity quantity) {
                Builder builder = new Builder();
                builder.quantity(quantity);
                return builder;
            }

            public static class Builder extends BackboneElement.Builder {
                private Quantity quantity;
                private Duration period;

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
                 * Adds new element(s) to existing list
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
                 * The maximum number of units of the medication that can be dispensed.
                 * </p>
                 * 
                 * @param quantity
                 *     The maximum number of units of the medication that can be dispensed
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
                 * The period that applies to the maximum number of units.
                 * </p>
                 * 
                 * @param period
                 *     The period that applies to the maximum number of units
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder period(Duration period) {
                    this.period = period;
                    return this;
                }

                @Override
                public MaxDispense build() {
                    return new MaxDispense(this);
                }

                protected Builder from(MaxDispense maxDispense) {
                    super.from(maxDispense);
                    quantity = maxDispense.quantity;
                    period = maxDispense.period;
                    return this;
                }
            }
        }
    }

    /**
     * <p>
     * The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.
     * </p>
     */
    public static class Kinetics extends BackboneElement {
        private final List<Quantity> areaUnderCurve;
        private final List<Quantity> lethalDose50;
        private final Duration halfLifePeriod;

        private volatile int hashCode;

        private Kinetics(Builder builder) {
            super(builder);
            areaUnderCurve = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.areaUnderCurve, "areaUnderCurve"));
            lethalDose50 = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.lethalDose50, "lethalDose50"));
            halfLifePeriod = builder.halfLifePeriod;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The drug concentration measured at certain discrete points in time.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Quantity}.
         */
        public List<Quantity> getAreaUnderCurve() {
            return areaUnderCurve;
        }

        /**
         * <p>
         * The median lethal dose of a drug.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Quantity}.
         */
        public List<Quantity> getLethalDose50() {
            return lethalDose50;
        }

        /**
         * <p>
         * The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Duration}.
         */
        public Duration getHalfLifePeriod() {
            return halfLifePeriod;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !areaUnderCurve.isEmpty() || 
                !lethalDose50.isEmpty() || 
                (halfLifePeriod != null);
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
                    accept(areaUnderCurve, "areaUnderCurve", visitor, Quantity.class);
                    accept(lethalDose50, "lethalDose50", visitor, Quantity.class);
                    accept(halfLifePeriod, "halfLifePeriod", visitor);
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
            Kinetics other = (Kinetics) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(areaUnderCurve, other.areaUnderCurve) && 
                Objects.equals(lethalDose50, other.lethalDose50) && 
                Objects.equals(halfLifePeriod, other.halfLifePeriod);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    areaUnderCurve, 
                    lethalDose50, 
                    halfLifePeriod);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            Builder builder = new Builder();
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private List<Quantity> areaUnderCurve = new ArrayList<>();
            private List<Quantity> lethalDose50 = new ArrayList<>();
            private Duration halfLifePeriod;

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
             * Adds new element(s) to existing list
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
             * The drug concentration measured at certain discrete points in time.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param areaUnderCurve
             *     The drug concentration measured at certain discrete points in time
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder areaUnderCurve(Quantity... areaUnderCurve) {
                for (Quantity value : areaUnderCurve) {
                    this.areaUnderCurve.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The drug concentration measured at certain discrete points in time.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param areaUnderCurve
             *     The drug concentration measured at certain discrete points in time
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder areaUnderCurve(Collection<Quantity> areaUnderCurve) {
                this.areaUnderCurve = new ArrayList<>(areaUnderCurve);
                return this;
            }

            /**
             * <p>
             * The median lethal dose of a drug.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param lethalDose50
             *     The median lethal dose of a drug
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder lethalDose50(Quantity... lethalDose50) {
                for (Quantity value : lethalDose50) {
                    this.lethalDose50.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The median lethal dose of a drug.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param lethalDose50
             *     The median lethal dose of a drug
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder lethalDose50(Collection<Quantity> lethalDose50) {
                this.lethalDose50 = new ArrayList<>(lethalDose50);
                return this;
            }

            /**
             * <p>
             * The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.
             * </p>
             * 
             * @param halfLifePeriod
             *     Time required for concentration in the body to decrease by half
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder halfLifePeriod(Duration halfLifePeriod) {
                this.halfLifePeriod = halfLifePeriod;
                return this;
            }

            @Override
            public Kinetics build() {
                return new Kinetics(this);
            }

            protected Builder from(Kinetics kinetics) {
                super.from(kinetics);
                areaUnderCurve.addAll(kinetics.areaUnderCurve);
                lethalDose50.addAll(kinetics.lethalDose50);
                halfLifePeriod = kinetics.halfLifePeriod;
                return this;
            }
        }
    }
}
