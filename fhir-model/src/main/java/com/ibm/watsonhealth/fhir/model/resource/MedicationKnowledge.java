/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    private MedicationKnowledge(Builder builder) {
        super(builder);
        this.code = builder.code;
        this.status = builder.status;
        this.manufacturer = builder.manufacturer;
        this.doseForm = builder.doseForm;
        this.amount = builder.amount;
        this.synonym = builder.synonym;
        this.relatedMedicationKnowledge = builder.relatedMedicationKnowledge;
        this.associatedMedication = builder.associatedMedication;
        this.productType = builder.productType;
        this.monograph = builder.monograph;
        this.ingredient = builder.ingredient;
        this.preparationInstruction = builder.preparationInstruction;
        this.intendedRoute = builder.intendedRoute;
        this.cost = builder.cost;
        this.monitoringProgram = builder.monitoringProgram;
        this.administrationGuidelines = builder.administrationGuidelines;
        this.medicineClassification = builder.medicineClassification;
        this.packaging = builder.packaging;
        this.drugCharacteristic = builder.drugCharacteristic;
        this.contraindication = builder.contraindication;
        this.regulatory = builder.regulatory;
        this.kinetics = builder.kinetics;
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
     *     A list containing immutable objects of type {@link String}.
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
     *     A list containing immutable objects of type {@link RelatedMedicationKnowledge}.
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
     *     A list containing immutable objects of type {@link Reference}.
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
     *     A list containing immutable objects of type {@link CodeableConcept}.
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
     *     A list containing immutable objects of type {@link Monograph}.
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
     *     A list containing immutable objects of type {@link Ingredient}.
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
     *     A list containing immutable objects of type {@link CodeableConcept}.
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
     *     A list containing immutable objects of type {@link Cost}.
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
     *     A list containing immutable objects of type {@link MonitoringProgram}.
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
     *     A list containing immutable objects of type {@link AdministrationGuidelines}.
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
     *     A list containing immutable objects of type {@link MedicineClassification}.
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
     *     A list containing immutable objects of type {@link DrugCharacteristic}.
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
     *     A list containing immutable objects of type {@link Reference}.
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
     *     A list containing immutable objects of type {@link Regulatory}.
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
     *     A list containing immutable objects of type {@link Kinetics}.
     */
    public List<Kinetics> getKinetics() {
        return kinetics;
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.code = code;
        builder.status = status;
        builder.manufacturer = manufacturer;
        builder.doseForm = doseForm;
        builder.amount = amount;
        builder.synonym.addAll(synonym);
        builder.relatedMedicationKnowledge.addAll(relatedMedicationKnowledge);
        builder.associatedMedication.addAll(associatedMedication);
        builder.productType.addAll(productType);
        builder.monograph.addAll(monograph);
        builder.ingredient.addAll(ingredient);
        builder.preparationInstruction = preparationInstruction;
        builder.intendedRoute.addAll(intendedRoute);
        builder.cost.addAll(cost);
        builder.monitoringProgram.addAll(monitoringProgram);
        builder.administrationGuidelines.addAll(administrationGuidelines);
        builder.medicineClassification.addAll(medicineClassification);
        builder.packaging = packaging;
        builder.drugCharacteristic.addAll(drugCharacteristic);
        builder.contraindication.addAll(contraindication);
        builder.regulatory.addAll(regulatory);
        builder.kinetics.addAll(kinetics);
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        // optional
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
         * A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a 
         * standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local 
         * formulary code, optionally with translations to other code systems.
         * </p>
         * 
         * @param code
         *     Code that identifies this medication
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
         * A code to indicate if the medication is in active use. The status refers to the validity about the information of the 
         * medication and not to its medicinal properties.
         * </p>
         * 
         * @param status
         *     active | inactive | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance.
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
         * @param doseForm
         *     powder | tablets | capsule +
         * 
         * @return
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         * 
         * @param synonym
         *     Additional names for a medication
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param synonym
         *     Additional names for a medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder synonym(Collection<String> synonym) {
            this.synonym.addAll(synonym);
            return this;
        }

        /**
         * <p>
         * Associated or related knowledge about a medication.
         * </p>
         * 
         * @param relatedMedicationKnowledge
         *     Associated or related medication information
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param relatedMedicationKnowledge
         *     Associated or related medication information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relatedMedicationKnowledge(Collection<RelatedMedicationKnowledge> relatedMedicationKnowledge) {
            this.relatedMedicationKnowledge.addAll(relatedMedicationKnowledge);
            return this;
        }

        /**
         * <p>
         * Associated or related medications. For example, if the medication is a branded product (e.g. Crestor), this is the 
         * Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a 
         * branded product (e.g. Crestor).
         * </p>
         * 
         * @param associatedMedication
         *     A medication resource that is associated with this medication
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param associatedMedication
         *     A medication resource that is associated with this medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder associatedMedication(Collection<Reference> associatedMedication) {
            this.associatedMedication.addAll(associatedMedication);
            return this;
        }

        /**
         * <p>
         * Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, 
         * etc.).
         * </p>
         * 
         * @param productType
         *     Category of the medication or product
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param productType
         *     Category of the medication or product
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder productType(Collection<CodeableConcept> productType) {
            this.productType.addAll(productType);
            return this;
        }

        /**
         * <p>
         * Associated documentation about the medication.
         * </p>
         * 
         * @param monograph
         *     Associated documentation about the medication
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param monograph
         *     Associated documentation about the medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder monograph(Collection<Monograph> monograph) {
            this.monograph.addAll(monograph);
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
         * The instructions for preparing the medication.
         * </p>
         * 
         * @param preparationInstruction
         *     The instructions for preparing the medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder preparationInstruction(Markdown preparationInstruction) {
            this.preparationInstruction = preparationInstruction;
            return this;
        }

        /**
         * <p>
         * The intended or approved route of administration.
         * </p>
         * 
         * @param intendedRoute
         *     The intended or approved route of administration
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param intendedRoute
         *     The intended or approved route of administration
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder intendedRoute(Collection<CodeableConcept> intendedRoute) {
            this.intendedRoute.addAll(intendedRoute);
            return this;
        }

        /**
         * <p>
         * The price of the medication.
         * </p>
         * 
         * @param cost
         *     The pricing of the medication
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param cost
         *     The pricing of the medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder cost(Collection<Cost> cost) {
            this.cost.addAll(cost);
            return this;
        }

        /**
         * <p>
         * The program under which the medication is reviewed.
         * </p>
         * 
         * @param monitoringProgram
         *     Program under which a medication is reviewed
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param monitoringProgram
         *     Program under which a medication is reviewed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder monitoringProgram(Collection<MonitoringProgram> monitoringProgram) {
            this.monitoringProgram.addAll(monitoringProgram);
            return this;
        }

        /**
         * <p>
         * Guidelines for the administration of the medication.
         * </p>
         * 
         * @param administrationGuidelines
         *     Guidelines for administration of the medication
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param administrationGuidelines
         *     Guidelines for administration of the medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder administrationGuidelines(Collection<AdministrationGuidelines> administrationGuidelines) {
            this.administrationGuidelines.addAll(administrationGuidelines);
            return this;
        }

        /**
         * <p>
         * Categorization of the medication within a formulary or classification system.
         * </p>
         * 
         * @param medicineClassification
         *     Categorization of the medication within a formulary or classification system
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param medicineClassification
         *     Categorization of the medication within a formulary or classification system
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder medicineClassification(Collection<MedicineClassification> medicineClassification) {
            this.medicineClassification.addAll(medicineClassification);
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
         *     A reference to this Builder instance.
         */
        public Builder packaging(Packaging packaging) {
            this.packaging = packaging;
            return this;
        }

        /**
         * <p>
         * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
         * </p>
         * 
         * @param drugCharacteristic
         *     Specifies descriptive properties of the medicine
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param drugCharacteristic
         *     Specifies descriptive properties of the medicine
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder drugCharacteristic(Collection<DrugCharacteristic> drugCharacteristic) {
            this.drugCharacteristic.addAll(drugCharacteristic);
            return this;
        }

        /**
         * <p>
         * Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease 
         * contraindication, drug-allergy interaction, etc.).
         * </p>
         * 
         * @param contraindication
         *     Potential clinical issue with or between medication(s)
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param contraindication
         *     Potential clinical issue with or between medication(s)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contraindication(Collection<Reference> contraindication) {
            this.contraindication.addAll(contraindication);
            return this;
        }

        /**
         * <p>
         * Regulatory information about a medication.
         * </p>
         * 
         * @param regulatory
         *     Regulatory information about a medication
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param regulatory
         *     Regulatory information about a medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder regulatory(Collection<Regulatory> regulatory) {
            this.regulatory.addAll(regulatory);
            return this;
        }

        /**
         * <p>
         * The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.
         * </p>
         * 
         * @param kinetics
         *     The time course of drug absorption, distribution, metabolism and excretion of a medication from the body
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param kinetics
         *     The time course of drug absorption, distribution, metabolism and excretion of a medication from the body
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder kinetics(Collection<Kinetics> kinetics) {
            this.kinetics.addAll(kinetics);
            return this;
        }

        @Override
        public MedicationKnowledge build() {
            return new MedicationKnowledge(this);
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

        private RelatedMedicationKnowledge(Builder builder) {
            super(builder);
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
            this.reference = ValidationSupport.requireNonEmpty(builder.reference, "reference");
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
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getReference() {
            return reference;
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
                    accept(reference, "reference", visitor, Reference.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept type, List<Reference> reference) {
            return new Builder(type, reference);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;
            private final List<Reference> reference;

            private Builder(CodeableConcept type, List<Reference> reference) {
                super();
                this.type = type;
                this.reference = reference;
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

            @Override
            public RelatedMedicationKnowledge build() {
                return new RelatedMedicationKnowledge(this);
            }

            private static Builder from(RelatedMedicationKnowledge relatedMedicationKnowledge) {
                Builder builder = new Builder(relatedMedicationKnowledge.type, relatedMedicationKnowledge.reference);
                builder.id = relatedMedicationKnowledge.id;
                builder.extension.addAll(relatedMedicationKnowledge.extension);
                builder.modifierExtension.addAll(relatedMedicationKnowledge.modifierExtension);
                return builder;
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

        private Monograph(Builder builder) {
            super(builder);
            this.type = builder.type;
            this.source = builder.source;
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(type, "type", visitor);
                    accept(source, "source", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private CodeableConcept type;
            private Reference source;

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
             * The category of documentation about the medication. (e.g. professional monograph, patient education monograph).
             * </p>
             * 
             * @param type
             *     The category of medication document
             * 
             * @return
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder source(Reference source) {
                this.source = source;
                return this;
            }

            @Override
            public Monograph build() {
                return new Monograph(this);
            }

            private static Builder from(Monograph monograph) {
                Builder builder = new Builder();
                builder.id = monograph.id;
                builder.extension.addAll(monograph.extension);
                builder.modifierExtension.addAll(monograph.modifierExtension);
                builder.type = monograph.type;
                builder.source = monograph.source;
                return builder;
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

        private Ingredient(Builder builder) {
            super(builder);
            this.item = ValidationSupport.requireChoiceElement(builder.item, "item", CodeableConcept.class, Reference.class);
            this.isActive = builder.isActive;
            this.strength = builder.strength;
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
            return Builder.from(this);
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

            private static Builder from(Ingredient ingredient) {
                Builder builder = new Builder(ingredient.item);
                builder.id = ingredient.id;
                builder.extension.addAll(ingredient.extension);
                builder.modifierExtension.addAll(ingredient.modifierExtension);
                builder.isActive = ingredient.isActive;
                builder.strength = ingredient.strength;
                return builder;
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

        private Cost(Builder builder) {
            super(builder);
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
            this.source = builder.source;
            this.cost = ValidationSupport.requireNonNull(builder.cost, "cost");
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(type, "type", visitor);
                    accept(source, "source", visitor);
                    accept(cost, "cost", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept type, Money cost) {
            return new Builder(type, cost);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;
            private final Money cost;

            // optional
            private String source;

            private Builder(CodeableConcept type, Money cost) {
                super();
                this.type = type;
                this.cost = cost;
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
             * The source or owner that assigns the price to the medication.
             * </p>
             * 
             * @param source
             *     The source or owner for the price information
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder source(String source) {
                this.source = source;
                return this;
            }

            @Override
            public Cost build() {
                return new Cost(this);
            }

            private static Builder from(Cost cost) {
                Builder builder = new Builder(cost.type, cost.cost);
                builder.id = cost.id;
                builder.extension.addAll(cost.extension);
                builder.modifierExtension.addAll(cost.modifierExtension);
                builder.source = cost.source;
                return builder;
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

        private MonitoringProgram(Builder builder) {
            super(builder);
            this.type = builder.type;
            this.name = builder.name;
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(type, "type", visitor);
                    accept(name, "name", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private CodeableConcept type;
            private String name;

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
             * Type of program under which the medication is monitored.
             * </p>
             * 
             * @param type
             *     Type of program under which the medication is monitored
             * 
             * @return
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            @Override
            public MonitoringProgram build() {
                return new MonitoringProgram(this);
            }

            private static Builder from(MonitoringProgram monitoringProgram) {
                Builder builder = new Builder();
                builder.id = monitoringProgram.id;
                builder.extension.addAll(monitoringProgram.extension);
                builder.modifierExtension.addAll(monitoringProgram.modifierExtension);
                builder.type = monitoringProgram.type;
                builder.name = monitoringProgram.name;
                return builder;
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

        private AdministrationGuidelines(Builder builder) {
            super(builder);
            this.dosage = builder.dosage;
            this.indication = ValidationSupport.choiceElement(builder.indication, "indication", CodeableConcept.class, Reference.class);
            this.patientCharacteristics = builder.patientCharacteristics;
        }

        /**
         * <p>
         * Dosage for the medication for the specific guidelines.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Dosage}.
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
         *     A list containing immutable objects of type {@link PatientCharacteristics}.
         */
        public List<PatientCharacteristics> getPatientCharacteristics() {
            return patientCharacteristics;
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
                    accept(dosage, "dosage", visitor, Dosage.class);
                    accept(indication, "indication", visitor, true);
                    accept(patientCharacteristics, "patientCharacteristics", visitor, PatientCharacteristics.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private List<Dosage> dosage = new ArrayList<>();
            private Element indication;
            private List<PatientCharacteristics> patientCharacteristics = new ArrayList<>();

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
             * Dosage for the medication for the specific guidelines.
             * </p>
             * 
             * @param dosage
             *     Dosage for the medication for the specific guidelines
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param dosage
             *     Dosage for the medication for the specific guidelines
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder dosage(Collection<Dosage> dosage) {
                this.dosage.addAll(dosage);
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
             *     A reference to this Builder instance.
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
             * 
             * @param patientCharacteristics
             *     Characteristics of the patient that are relevant to the administration guidelines
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param patientCharacteristics
             *     Characteristics of the patient that are relevant to the administration guidelines
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder patientCharacteristics(Collection<PatientCharacteristics> patientCharacteristics) {
                this.patientCharacteristics.addAll(patientCharacteristics);
                return this;
            }

            @Override
            public AdministrationGuidelines build() {
                return new AdministrationGuidelines(this);
            }

            private static Builder from(AdministrationGuidelines administrationGuidelines) {
                Builder builder = new Builder();
                builder.id = administrationGuidelines.id;
                builder.extension.addAll(administrationGuidelines.extension);
                builder.modifierExtension.addAll(administrationGuidelines.modifierExtension);
                builder.dosage.addAll(administrationGuidelines.dosage);
                builder.indication = administrationGuidelines.indication;
                builder.patientCharacteristics.addAll(administrationGuidelines.patientCharacteristics);
                return builder;
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

            private Dosage(Builder builder) {
                super(builder);
                this.type = ValidationSupport.requireNonNull(builder.type, "type");
                this.dosage = ValidationSupport.requireNonEmpty(builder.dosage, "dosage");
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
             *     A list containing immutable objects of type {@link Dosage}.
             */
            public List<com.ibm.watsonhealth.fhir.model.type.Dosage> getDosage() {
                return dosage;
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
                        accept(dosage, "dosage", visitor, com.ibm.watsonhealth.fhir.model.type.Dosage.class);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(CodeableConcept type, List<com.ibm.watsonhealth.fhir.model.type.Dosage> dosage) {
                return new Builder(type, dosage);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept type;
                private final List<com.ibm.watsonhealth.fhir.model.type.Dosage> dosage;

                private Builder(CodeableConcept type, List<com.ibm.watsonhealth.fhir.model.type.Dosage> dosage) {
                    super();
                    this.type = type;
                    this.dosage = dosage;
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

                @Override
                public Dosage build() {
                    return new Dosage(this);
                }

                private static Builder from(Dosage dosage) {
                    Builder builder = new Builder(dosage.type, dosage.dosage);
                    builder.id = dosage.id;
                    builder.extension.addAll(dosage.extension);
                    builder.modifierExtension.addAll(dosage.modifierExtension);
                    return builder;
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

            private PatientCharacteristics(Builder builder) {
                super(builder);
                this.characteristic = ValidationSupport.requireChoiceElement(builder.characteristic, "characteristic", CodeableConcept.class, Quantity.class);
                this.value = builder.value;
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
             *     A list containing immutable objects of type {@link String}.
             */
            public List<String> getValue() {
                return value;
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
                        accept(characteristic, "characteristic", visitor, true);
                        accept(value, "value", visitor, String.class);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(Element characteristic) {
                return new Builder(characteristic);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Element characteristic;

                // optional
                private List<String> value = new ArrayList<>();

                private Builder(Element characteristic) {
                    super();
                    this.characteristic = characteristic;
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
                 * The specific characteristic (e.g. height, weight, gender, etc.).
                 * </p>
                 * 
                 * @param value
                 *     The specific characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param value
                 *     The specific characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder value(Collection<String> value) {
                    this.value.addAll(value);
                    return this;
                }

                @Override
                public PatientCharacteristics build() {
                    return new PatientCharacteristics(this);
                }

                private static Builder from(PatientCharacteristics patientCharacteristics) {
                    Builder builder = new Builder(patientCharacteristics.characteristic);
                    builder.id = patientCharacteristics.id;
                    builder.extension.addAll(patientCharacteristics.extension);
                    builder.modifierExtension.addAll(patientCharacteristics.modifierExtension);
                    builder.value.addAll(patientCharacteristics.value);
                    return builder;
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

        private MedicineClassification(Builder builder) {
            super(builder);
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
            this.classification = builder.classification;
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
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getClassification() {
            return classification;
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
                    accept(classification, "classification", visitor, CodeableConcept.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept type) {
            return new Builder(type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;

            // optional
            private List<CodeableConcept> classification = new ArrayList<>();

            private Builder(CodeableConcept type) {
                super();
                this.type = type;
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
             * Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc.).
             * </p>
             * 
             * @param classification
             *     Specific category assigned to the medication
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param classification
             *     Specific category assigned to the medication
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder classification(Collection<CodeableConcept> classification) {
                this.classification.addAll(classification);
                return this;
            }

            @Override
            public MedicineClassification build() {
                return new MedicineClassification(this);
            }

            private static Builder from(MedicineClassification medicineClassification) {
                Builder builder = new Builder(medicineClassification.type);
                builder.id = medicineClassification.id;
                builder.extension.addAll(medicineClassification.extension);
                builder.modifierExtension.addAll(medicineClassification.modifierExtension);
                builder.classification.addAll(medicineClassification.classification);
                return builder;
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

        private Packaging(Builder builder) {
            super(builder);
            this.type = builder.type;
            this.quantity = builder.quantity;
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(type, "type", visitor);
                    accept(quantity, "quantity", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private CodeableConcept type;
            private Quantity quantity;

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
             * A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, 
             * bottle).
             * </p>
             * 
             * @param type
             *     A code that defines the specific type of packaging that the medication can be found in
             * 
             * @return
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder quantity(Quantity quantity) {
                this.quantity = quantity;
                return this;
            }

            @Override
            public Packaging build() {
                return new Packaging(this);
            }

            private static Builder from(Packaging packaging) {
                Builder builder = new Builder();
                builder.id = packaging.id;
                builder.extension.addAll(packaging.extension);
                builder.modifierExtension.addAll(packaging.modifierExtension);
                builder.type = packaging.type;
                builder.quantity = packaging.quantity;
                return builder;
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

        private DrugCharacteristic(Builder builder) {
            super(builder);
            this.type = builder.type;
            this.value = ValidationSupport.choiceElement(builder.value, "value", CodeableConcept.class, String.class, Quantity.class, Base64Binary.class);
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(type, "type", visitor);
                    accept(value, "value", visitor, true);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private CodeableConcept type;
            private Element value;

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
             * A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint).
             * </p>
             * 
             * @param type
             *     Code specifying the type of characteristic of medication
             * 
             * @return
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            @Override
            public DrugCharacteristic build() {
                return new DrugCharacteristic(this);
            }

            private static Builder from(DrugCharacteristic drugCharacteristic) {
                Builder builder = new Builder();
                builder.id = drugCharacteristic.id;
                builder.extension.addAll(drugCharacteristic.extension);
                builder.modifierExtension.addAll(drugCharacteristic.modifierExtension);
                builder.type = drugCharacteristic.type;
                builder.value = drugCharacteristic.value;
                return builder;
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

        private Regulatory(Builder builder) {
            super(builder);
            this.regulatoryAuthority = ValidationSupport.requireNonNull(builder.regulatoryAuthority, "regulatoryAuthority");
            this.substitution = builder.substitution;
            this.schedule = builder.schedule;
            this.maxDispense = builder.maxDispense;
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
         *     A list containing immutable objects of type {@link Substitution}.
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
         *     A list containing immutable objects of type {@link Schedule}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(regulatoryAuthority, "regulatoryAuthority", visitor);
                    accept(substitution, "substitution", visitor, Substitution.class);
                    accept(schedule, "schedule", visitor, Schedule.class);
                    accept(maxDispense, "maxDispense", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Reference regulatoryAuthority) {
            return new Builder(regulatoryAuthority);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference regulatoryAuthority;

            // optional
            private List<Substitution> substitution = new ArrayList<>();
            private List<Schedule> schedule = new ArrayList<>();
            private MaxDispense maxDispense;

            private Builder(Reference regulatoryAuthority) {
                super();
                this.regulatoryAuthority = regulatoryAuthority;
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
             * Specifies if changes are allowed when dispensing a medication from a regulatory perspective.
             * </p>
             * 
             * @param substitution
             *     Specifies if changes are allowed when dispensing a medication from a regulatory perspective
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param substitution
             *     Specifies if changes are allowed when dispensing a medication from a regulatory perspective
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder substitution(Collection<Substitution> substitution) {
                this.substitution.addAll(substitution);
                return this;
            }

            /**
             * <p>
             * Specifies the schedule of a medication in jurisdiction.
             * </p>
             * 
             * @param schedule
             *     Specifies the schedule of a medication in jurisdiction
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param schedule
             *     Specifies the schedule of a medication in jurisdiction
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder schedule(Collection<Schedule> schedule) {
                this.schedule.addAll(schedule);
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
             *     A reference to this Builder instance.
             */
            public Builder maxDispense(MaxDispense maxDispense) {
                this.maxDispense = maxDispense;
                return this;
            }

            @Override
            public Regulatory build() {
                return new Regulatory(this);
            }

            private static Builder from(Regulatory regulatory) {
                Builder builder = new Builder(regulatory.regulatoryAuthority);
                builder.id = regulatory.id;
                builder.extension.addAll(regulatory.extension);
                builder.modifierExtension.addAll(regulatory.modifierExtension);
                builder.substitution.addAll(regulatory.substitution);
                builder.schedule.addAll(regulatory.schedule);
                builder.maxDispense = regulatory.maxDispense;
                return builder;
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

            private Substitution(Builder builder) {
                super(builder);
                this.type = ValidationSupport.requireNonNull(builder.type, "type");
                this.allowed = ValidationSupport.requireNonNull(builder.allowed, "allowed");
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
            public void accept(java.lang.String elementName, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, this);
                    if (visitor.visit(elementName, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(type, "type", visitor);
                        accept(allowed, "allowed", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(CodeableConcept type, Boolean allowed) {
                return new Builder(type, allowed);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept type;
                private final Boolean allowed;

                private Builder(CodeableConcept type, Boolean allowed) {
                    super();
                    this.type = type;
                    this.allowed = allowed;
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

                @Override
                public Substitution build() {
                    return new Substitution(this);
                }

                private static Builder from(Substitution substitution) {
                    Builder builder = new Builder(substitution.type, substitution.allowed);
                    builder.id = substitution.id;
                    builder.extension.addAll(substitution.extension);
                    builder.modifierExtension.addAll(substitution.modifierExtension);
                    return builder;
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

            private Schedule(Builder builder) {
                super(builder);
                this.schedule = ValidationSupport.requireNonNull(builder.schedule, "schedule");
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
            public void accept(java.lang.String elementName, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, this);
                    if (visitor.visit(elementName, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(schedule, "schedule", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(CodeableConcept schedule) {
                return new Builder(schedule);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept schedule;

                private Builder(CodeableConcept schedule) {
                    super();
                    this.schedule = schedule;
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

                @Override
                public Schedule build() {
                    return new Schedule(this);
                }

                private static Builder from(Schedule schedule) {
                    Builder builder = new Builder(schedule.schedule);
                    builder.id = schedule.id;
                    builder.extension.addAll(schedule.extension);
                    builder.modifierExtension.addAll(schedule.modifierExtension);
                    return builder;
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

            private MaxDispense(Builder builder) {
                super(builder);
                this.quantity = ValidationSupport.requireNonNull(builder.quantity, "quantity");
                this.period = builder.period;
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
            public void accept(java.lang.String elementName, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, this);
                    if (visitor.visit(elementName, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(quantity, "quantity", visitor);
                        accept(period, "period", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(Quantity quantity) {
                return new Builder(quantity);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Quantity quantity;

                // optional
                private Duration period;

                private Builder(Quantity quantity) {
                    super();
                    this.quantity = quantity;
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
                 * The period that applies to the maximum number of units.
                 * </p>
                 * 
                 * @param period
                 *     The period that applies to the maximum number of units
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder period(Duration period) {
                    this.period = period;
                    return this;
                }

                @Override
                public MaxDispense build() {
                    return new MaxDispense(this);
                }

                private static Builder from(MaxDispense maxDispense) {
                    Builder builder = new Builder(maxDispense.quantity);
                    builder.id = maxDispense.id;
                    builder.extension.addAll(maxDispense.extension);
                    builder.modifierExtension.addAll(maxDispense.modifierExtension);
                    builder.period = maxDispense.period;
                    return builder;
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

        private Kinetics(Builder builder) {
            super(builder);
            this.areaUnderCurve = builder.areaUnderCurve;
            this.lethalDose50 = builder.lethalDose50;
            this.halfLifePeriod = builder.halfLifePeriod;
        }

        /**
         * <p>
         * The drug concentration measured at certain discrete points in time.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Quantity}.
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
         *     A list containing immutable objects of type {@link Quantity}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(areaUnderCurve, "areaUnderCurve", visitor, Quantity.class);
                    accept(lethalDose50, "lethalDose50", visitor, Quantity.class);
                    accept(halfLifePeriod, "halfLifePeriod", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private List<Quantity> areaUnderCurve = new ArrayList<>();
            private List<Quantity> lethalDose50 = new ArrayList<>();
            private Duration halfLifePeriod;

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
             * The drug concentration measured at certain discrete points in time.
             * </p>
             * 
             * @param areaUnderCurve
             *     The drug concentration measured at certain discrete points in time
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param areaUnderCurve
             *     The drug concentration measured at certain discrete points in time
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder areaUnderCurve(Collection<Quantity> areaUnderCurve) {
                this.areaUnderCurve.addAll(areaUnderCurve);
                return this;
            }

            /**
             * <p>
             * The median lethal dose of a drug.
             * </p>
             * 
             * @param lethalDose50
             *     The median lethal dose of a drug
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param lethalDose50
             *     The median lethal dose of a drug
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder lethalDose50(Collection<Quantity> lethalDose50) {
                this.lethalDose50.addAll(lethalDose50);
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
             *     A reference to this Builder instance.
             */
            public Builder halfLifePeriod(Duration halfLifePeriod) {
                this.halfLifePeriod = halfLifePeriod;
                return this;
            }

            @Override
            public Kinetics build() {
                return new Kinetics(this);
            }

            private static Builder from(Kinetics kinetics) {
                Builder builder = new Builder();
                builder.id = kinetics.id;
                builder.extension.addAll(kinetics.extension);
                builder.modifierExtension.addAll(kinetics.modifierExtension);
                builder.areaUnderCurve.addAll(kinetics.areaUnderCurve);
                builder.lethalDose50.addAll(kinetics.lethalDose50);
                builder.halfLifePeriod = kinetics.halfLifePeriod;
                return builder;
            }
        }
    }
}
