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
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.MedicationKnowledgeStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Information about a medication that is used to support knowledge.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationKnowledge extends DomainResource {
    @Summary
    @Binding(
        bindingName = "MedicationFormalRepresentation",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept that defines the type of a medication.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-codes"
    )
    private final CodeableConcept code;
    @Summary
    @Binding(
        bindingName = "MedicationKnowledgeStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A coded concept defining if the medication is in active use.",
        valueSet = "http://hl7.org/fhir/ValueSet/medicationknowledge-status|4.3.0"
    )
    private final MedicationKnowledgeStatus status;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference manufacturer;
    @Binding(
        bindingName = "MedicationForm",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept defining the form of a medication.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-form-codes"
    )
    private final CodeableConcept doseForm;
    @Summary
    private final SimpleQuantity amount;
    @Summary
    private final List<String> synonym;
    private final List<RelatedMedicationKnowledge> relatedMedicationKnowledge;
    @ReferenceTarget({ "Medication" })
    private final List<Reference> associatedMedication;
    private final List<CodeableConcept> productType;
    private final List<Monograph> monograph;
    private final List<Ingredient> ingredient;
    private final Markdown preparationInstruction;
    @Binding(
        bindingName = "MedicationRoute",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept defining the intended route of administration.",
        valueSet = "http://hl7.org/fhir/ValueSet/route-codes"
    )
    private final List<CodeableConcept> intendedRoute;
    private final List<Cost> cost;
    private final List<MonitoringProgram> monitoringProgram;
    private final List<AdministrationGuidelines> administrationGuidelines;
    private final List<MedicineClassification> medicineClassification;
    private final Packaging packaging;
    private final List<DrugCharacteristic> drugCharacteristic;
    @ReferenceTarget({ "DetectedIssue" })
    private final List<Reference> contraindication;
    private final List<Regulatory> regulatory;
    private final List<Kinetics> kinetics;

    private MedicationKnowledge(Builder builder) {
        super(builder);
        code = builder.code;
        status = builder.status;
        manufacturer = builder.manufacturer;
        doseForm = builder.doseForm;
        amount = builder.amount;
        synonym = Collections.unmodifiableList(builder.synonym);
        relatedMedicationKnowledge = Collections.unmodifiableList(builder.relatedMedicationKnowledge);
        associatedMedication = Collections.unmodifiableList(builder.associatedMedication);
        productType = Collections.unmodifiableList(builder.productType);
        monograph = Collections.unmodifiableList(builder.monograph);
        ingredient = Collections.unmodifiableList(builder.ingredient);
        preparationInstruction = builder.preparationInstruction;
        intendedRoute = Collections.unmodifiableList(builder.intendedRoute);
        cost = Collections.unmodifiableList(builder.cost);
        monitoringProgram = Collections.unmodifiableList(builder.monitoringProgram);
        administrationGuidelines = Collections.unmodifiableList(builder.administrationGuidelines);
        medicineClassification = Collections.unmodifiableList(builder.medicineClassification);
        packaging = builder.packaging;
        drugCharacteristic = Collections.unmodifiableList(builder.drugCharacteristic);
        contraindication = Collections.unmodifiableList(builder.contraindication);
        regulatory = Collections.unmodifiableList(builder.regulatory);
        kinetics = Collections.unmodifiableList(builder.kinetics);
    }

    /**
     * A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a 
     * standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local 
     * formulary code, optionally with translations to other code systems.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * A code to indicate if the medication is in active use. The status refers to the validity about the information of the 
     * medication and not to its medicinal properties.
     * 
     * @return
     *     An immutable object of type {@link MedicationKnowledgeStatus} that may be null.
     */
    public MedicationKnowledgeStatus getStatus() {
        return status;
    }

    /**
     * Describes the details of the manufacturer of the medication product. This is not intended to represent the distributor 
     * of a medication product.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getManufacturer() {
        return manufacturer;
    }

    /**
     * Describes the form of the item. Powder; tablets; capsule.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getDoseForm() {
        return doseForm;
    }

    /**
     * Specific amount of the drug in the packaged product. For example, when specifying a product that has the same strength 
     * (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional 
     * clarification of the package amount (For example, 3 mL, 10mL, etc.).
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getAmount() {
        return amount;
    }

    /**
     * Additional names for a medication, for example, the name(s) given to a medication in different countries. For example, 
     * acetaminophen and paracetamol or salbutamol and albuterol.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getSynonym() {
        return synonym;
    }

    /**
     * Associated or related knowledge about a medication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatedMedicationKnowledge} that may be empty.
     */
    public List<RelatedMedicationKnowledge> getRelatedMedicationKnowledge() {
        return relatedMedicationKnowledge;
    }

    /**
     * Associated or related medications. For example, if the medication is a branded product (e.g. Crestor), this is the 
     * Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a 
     * branded product (e.g. Crestor).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAssociatedMedication() {
        return associatedMedication;
    }

    /**
     * Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, 
     * etc.).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getProductType() {
        return productType;
    }

    /**
     * Associated documentation about the medication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Monograph} that may be empty.
     */
    public List<Monograph> getMonograph() {
        return monograph;
    }

    /**
     * Identifies a particular constituent of interest in the product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Ingredient} that may be empty.
     */
    public List<Ingredient> getIngredient() {
        return ingredient;
    }

    /**
     * The instructions for preparing the medication.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPreparationInstruction() {
        return preparationInstruction;
    }

    /**
     * The intended or approved route of administration.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getIntendedRoute() {
        return intendedRoute;
    }

    /**
     * The price of the medication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Cost} that may be empty.
     */
    public List<Cost> getCost() {
        return cost;
    }

    /**
     * The program under which the medication is reviewed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MonitoringProgram} that may be empty.
     */
    public List<MonitoringProgram> getMonitoringProgram() {
        return monitoringProgram;
    }

    /**
     * Guidelines for the administration of the medication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link AdministrationGuidelines} that may be empty.
     */
    public List<AdministrationGuidelines> getAdministrationGuidelines() {
        return administrationGuidelines;
    }

    /**
     * Categorization of the medication within a formulary or classification system.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MedicineClassification} that may be empty.
     */
    public List<MedicineClassification> getMedicineClassification() {
        return medicineClassification;
    }

    /**
     * Information that only applies to packages (not products).
     * 
     * @return
     *     An immutable object of type {@link Packaging} that may be null.
     */
    public Packaging getPackaging() {
        return packaging;
    }

    /**
     * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DrugCharacteristic} that may be empty.
     */
    public List<DrugCharacteristic> getDrugCharacteristic() {
        return drugCharacteristic;
    }

    /**
     * Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease 
     * contraindication, drug-allergy interaction, etc.).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getContraindication() {
        return contraindication;
    }

    /**
     * Regulatory information about a medication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Regulatory} that may be empty.
     */
    public List<Regulatory> getRegulatory() {
        return regulatory;
    }

    /**
     * The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Kinetics} that may be empty.
     */
    public List<Kinetics> getKinetics() {
        return kinetics;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (code != null) || 
            (status != null) || 
            (manufacturer != null) || 
            (doseForm != null) || 
            (amount != null) || 
            !synonym.isEmpty() || 
            !relatedMedicationKnowledge.isEmpty() || 
            !associatedMedication.isEmpty() || 
            !productType.isEmpty() || 
            !monograph.isEmpty() || 
            !ingredient.isEmpty() || 
            (preparationInstruction != null) || 
            !intendedRoute.isEmpty() || 
            !cost.isEmpty() || 
            !monitoringProgram.isEmpty() || 
            !administrationGuidelines.isEmpty() || 
            !medicineClassification.isEmpty() || 
            (packaging != null) || 
            !drugCharacteristic.isEmpty() || 
            !contraindication.isEmpty() || 
            !regulatory.isEmpty() || 
            !kinetics.isEmpty();
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
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private CodeableConcept code;
        private MedicationKnowledgeStatus status;
        private Reference manufacturer;
        private CodeableConcept doseForm;
        private SimpleQuantity amount;
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
         * A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a 
         * standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local 
         * formulary code, optionally with translations to other code systems.
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
         * A code to indicate if the medication is in active use. The status refers to the validity about the information of the 
         * medication and not to its medicinal properties.
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
         * Describes the details of the manufacturer of the medication product. This is not intended to represent the distributor 
         * of a medication product.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
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
         * Describes the form of the item. Powder; tablets; capsule.
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
         * Specific amount of the drug in the packaged product. For example, when specifying a product that has the same strength 
         * (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional 
         * clarification of the package amount (For example, 3 mL, 10mL, etc.).
         * 
         * @param amount
         *     Amount of drug in package
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder amount(SimpleQuantity amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Convenience method for setting {@code synonym}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param synonym
         *     Additional names for a medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #synonym(com.ibm.fhir.model.type.String)
         */
        public Builder synonym(java.lang.String... synonym) {
            for (java.lang.String value : synonym) {
                this.synonym.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * Additional names for a medication, for example, the name(s) given to a medication in different countries. For example, 
         * acetaminophen and paracetamol or salbutamol and albuterol.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Additional names for a medication, for example, the name(s) given to a medication in different countries. For example, 
         * acetaminophen and paracetamol or salbutamol and albuterol.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param synonym
         *     Additional names for a medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder synonym(Collection<String> synonym) {
            this.synonym = new ArrayList<>(synonym);
            return this;
        }

        /**
         * Associated or related knowledge about a medication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Associated or related knowledge about a medication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedMedicationKnowledge
         *     Associated or related medication information
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder relatedMedicationKnowledge(Collection<RelatedMedicationKnowledge> relatedMedicationKnowledge) {
            this.relatedMedicationKnowledge = new ArrayList<>(relatedMedicationKnowledge);
            return this;
        }

        /**
         * Associated or related medications. For example, if the medication is a branded product (e.g. Crestor), this is the 
         * Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a 
         * branded product (e.g. Crestor).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Medication}</li>
         * </ul>
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
         * Associated or related medications. For example, if the medication is a branded product (e.g. Crestor), this is the 
         * Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a 
         * branded product (e.g. Crestor).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Medication}</li>
         * </ul>
         * 
         * @param associatedMedication
         *     A medication resource that is associated with this medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder associatedMedication(Collection<Reference> associatedMedication) {
            this.associatedMedication = new ArrayList<>(associatedMedication);
            return this;
        }

        /**
         * Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, 
         * etc.).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, 
         * etc.).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param productType
         *     Category of the medication or product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder productType(Collection<CodeableConcept> productType) {
            this.productType = new ArrayList<>(productType);
            return this;
        }

        /**
         * Associated documentation about the medication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Associated documentation about the medication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param monograph
         *     Associated documentation about the medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder monograph(Collection<Monograph> monograph) {
            this.monograph = new ArrayList<>(monograph);
            return this;
        }

        /**
         * Identifies a particular constituent of interest in the product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Identifies a particular constituent of interest in the product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param ingredient
         *     Active or inactive ingredient
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
         * The instructions for preparing the medication.
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
         * The intended or approved route of administration.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The intended or approved route of administration.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param intendedRoute
         *     The intended or approved route of administration
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder intendedRoute(Collection<CodeableConcept> intendedRoute) {
            this.intendedRoute = new ArrayList<>(intendedRoute);
            return this;
        }

        /**
         * The price of the medication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The price of the medication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param cost
         *     The pricing of the medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder cost(Collection<Cost> cost) {
            this.cost = new ArrayList<>(cost);
            return this;
        }

        /**
         * The program under which the medication is reviewed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The program under which the medication is reviewed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param monitoringProgram
         *     Program under which a medication is reviewed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder monitoringProgram(Collection<MonitoringProgram> monitoringProgram) {
            this.monitoringProgram = new ArrayList<>(monitoringProgram);
            return this;
        }

        /**
         * Guidelines for the administration of the medication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Guidelines for the administration of the medication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param administrationGuidelines
         *     Guidelines for administration of the medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder administrationGuidelines(Collection<AdministrationGuidelines> administrationGuidelines) {
            this.administrationGuidelines = new ArrayList<>(administrationGuidelines);
            return this;
        }

        /**
         * Categorization of the medication within a formulary or classification system.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Categorization of the medication within a formulary or classification system.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param medicineClassification
         *     Categorization of the medication within a formulary or classification system
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder medicineClassification(Collection<MedicineClassification> medicineClassification) {
            this.medicineClassification = new ArrayList<>(medicineClassification);
            return this;
        }

        /**
         * Information that only applies to packages (not products).
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
         * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param drugCharacteristic
         *     Specifies descriptive properties of the medicine
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder drugCharacteristic(Collection<DrugCharacteristic> drugCharacteristic) {
            this.drugCharacteristic = new ArrayList<>(drugCharacteristic);
            return this;
        }

        /**
         * Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease 
         * contraindication, drug-allergy interaction, etc.).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DetectedIssue}</li>
         * </ul>
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
         * Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease 
         * contraindication, drug-allergy interaction, etc.).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DetectedIssue}</li>
         * </ul>
         * 
         * @param contraindication
         *     Potential clinical issue with or between medication(s)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contraindication(Collection<Reference> contraindication) {
            this.contraindication = new ArrayList<>(contraindication);
            return this;
        }

        /**
         * Regulatory information about a medication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Regulatory information about a medication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param regulatory
         *     Regulatory information about a medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder regulatory(Collection<Regulatory> regulatory) {
            this.regulatory = new ArrayList<>(regulatory);
            return this;
        }

        /**
         * The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param kinetics
         *     The time course of drug absorption, distribution, metabolism and excretion of a medication from the body
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder kinetics(Collection<Kinetics> kinetics) {
            this.kinetics = new ArrayList<>(kinetics);
            return this;
        }

        /**
         * Build the {@link MedicationKnowledge}
         * 
         * @return
         *     An immutable object of type {@link MedicationKnowledge}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicationKnowledge per the base specification
         */
        @Override
        public MedicationKnowledge build() {
            MedicationKnowledge medicationKnowledge = new MedicationKnowledge(this);
            if (validating) {
                validate(medicationKnowledge);
            }
            return medicationKnowledge;
        }

        protected void validate(MedicationKnowledge medicationKnowledge) {
            super.validate(medicationKnowledge);
            ValidationSupport.checkList(medicationKnowledge.synonym, "synonym", String.class);
            ValidationSupport.checkList(medicationKnowledge.relatedMedicationKnowledge, "relatedMedicationKnowledge", RelatedMedicationKnowledge.class);
            ValidationSupport.checkList(medicationKnowledge.associatedMedication, "associatedMedication", Reference.class);
            ValidationSupport.checkList(medicationKnowledge.productType, "productType", CodeableConcept.class);
            ValidationSupport.checkList(medicationKnowledge.monograph, "monograph", Monograph.class);
            ValidationSupport.checkList(medicationKnowledge.ingredient, "ingredient", Ingredient.class);
            ValidationSupport.checkList(medicationKnowledge.intendedRoute, "intendedRoute", CodeableConcept.class);
            ValidationSupport.checkList(medicationKnowledge.cost, "cost", Cost.class);
            ValidationSupport.checkList(medicationKnowledge.monitoringProgram, "monitoringProgram", MonitoringProgram.class);
            ValidationSupport.checkList(medicationKnowledge.administrationGuidelines, "administrationGuidelines", AdministrationGuidelines.class);
            ValidationSupport.checkList(medicationKnowledge.medicineClassification, "medicineClassification", MedicineClassification.class);
            ValidationSupport.checkList(medicationKnowledge.drugCharacteristic, "drugCharacteristic", DrugCharacteristic.class);
            ValidationSupport.checkList(medicationKnowledge.contraindication, "contraindication", Reference.class);
            ValidationSupport.checkList(medicationKnowledge.regulatory, "regulatory", Regulatory.class);
            ValidationSupport.checkList(medicationKnowledge.kinetics, "kinetics", Kinetics.class);
            ValidationSupport.checkReferenceType(medicationKnowledge.manufacturer, "manufacturer", "Organization");
            ValidationSupport.checkReferenceType(medicationKnowledge.associatedMedication, "associatedMedication", "Medication");
            ValidationSupport.checkReferenceType(medicationKnowledge.contraindication, "contraindication", "DetectedIssue");
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
     * Associated or related knowledge about a medication.
     */
    public static class RelatedMedicationKnowledge extends BackboneElement {
        @Required
        private final CodeableConcept type;
        @ReferenceTarget({ "MedicationKnowledge" })
        @Required
        private final List<Reference> reference;

        private RelatedMedicationKnowledge(Builder builder) {
            super(builder);
            type = builder.type;
            reference = Collections.unmodifiableList(builder.reference);
        }

        /**
         * The category of the associated medication knowledge reference.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Associated documentation about the associated medication knowledge.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that is non-empty.
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

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private List<Reference> reference = new ArrayList<>();

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
             * The category of the associated medication knowledge reference.
             * 
             * <p>This element is required.
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
             * Associated documentation about the associated medication knowledge.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link MedicationKnowledge}</li>
             * </ul>
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
             * Associated documentation about the associated medication knowledge.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link MedicationKnowledge}</li>
             * </ul>
             * 
             * @param reference
             *     Associated documentation about the associated medication knowledge
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder reference(Collection<Reference> reference) {
                this.reference = new ArrayList<>(reference);
                return this;
            }

            /**
             * Build the {@link RelatedMedicationKnowledge}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>reference</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link RelatedMedicationKnowledge}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid RelatedMedicationKnowledge per the base specification
             */
            @Override
            public RelatedMedicationKnowledge build() {
                RelatedMedicationKnowledge relatedMedicationKnowledge = new RelatedMedicationKnowledge(this);
                if (validating) {
                    validate(relatedMedicationKnowledge);
                }
                return relatedMedicationKnowledge;
            }

            protected void validate(RelatedMedicationKnowledge relatedMedicationKnowledge) {
                super.validate(relatedMedicationKnowledge);
                ValidationSupport.requireNonNull(relatedMedicationKnowledge.type, "type");
                ValidationSupport.checkNonEmptyList(relatedMedicationKnowledge.reference, "reference", Reference.class);
                ValidationSupport.checkReferenceType(relatedMedicationKnowledge.reference, "reference", "MedicationKnowledge");
                ValidationSupport.requireValueOrChildren(relatedMedicationKnowledge);
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
     * Associated documentation about the medication.
     */
    public static class Monograph extends BackboneElement {
        private final CodeableConcept type;
        @ReferenceTarget({ "DocumentReference", "Media" })
        private final Reference source;

        private Monograph(Builder builder) {
            super(builder);
            type = builder.type;
            source = builder.source;
        }

        /**
         * The category of documentation about the medication. (e.g. professional monograph, patient education monograph).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Associated documentation about the medication.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
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
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private Reference source;

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
             * The category of documentation about the medication. (e.g. professional monograph, patient education monograph).
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
             * Associated documentation about the medication.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * <li>{@link Media}</li>
             * </ul>
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

            /**
             * Build the {@link Monograph}
             * 
             * @return
             *     An immutable object of type {@link Monograph}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Monograph per the base specification
             */
            @Override
            public Monograph build() {
                Monograph monograph = new Monograph(this);
                if (validating) {
                    validate(monograph);
                }
                return monograph;
            }

            protected void validate(Monograph monograph) {
                super.validate(monograph);
                ValidationSupport.checkReferenceType(monograph.source, "source", "DocumentReference", "Media");
                ValidationSupport.requireValueOrChildren(monograph);
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
     * Identifies a particular constituent of interest in the product.
     */
    public static class Ingredient extends BackboneElement {
        @ReferenceTarget({ "Substance" })
        @Choice({ CodeableConcept.class, Reference.class })
        @Required
        private final Element item;
        private final Boolean isActive;
        private final Ratio strength;

        private Ingredient(Builder builder) {
            super(builder);
            item = builder.item;
            isActive = builder.isActive;
            strength = builder.strength;
        }

        /**
         * The actual ingredient - either a substance (simple ingredient) or another medication.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} or {@link Reference} that is non-null.
         */
        public Element getItem() {
            return item;
        }

        /**
         * Indication of whether this ingredient affects the therapeutic action of the drug.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getIsActive() {
            return isActive;
        }

        /**
         * Specifies how many (or how much) of the items there are in this Medication. For example, 250 mg per tablet. This is 
         * expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.
         * 
         * @return
         *     An immutable object of type {@link Ratio} that may be null.
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

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private Element item;
            private Boolean isActive;
            private Ratio strength;

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
             * The actual ingredient - either a substance (simple ingredient) or another medication.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link Substance}</li>
             * </ul>
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
             * Convenience method for setting {@code isActive}.
             * 
             * @param isActive
             *     Active ingredient indicator
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #isActive(com.ibm.fhir.model.type.Boolean)
             */
            public Builder isActive(java.lang.Boolean isActive) {
                this.isActive = (isActive == null) ? null : Boolean.of(isActive);
                return this;
            }

            /**
             * Indication of whether this ingredient affects the therapeutic action of the drug.
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
             * Specifies how many (or how much) of the items there are in this Medication. For example, 250 mg per tablet. This is 
             * expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.
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
                ValidationSupport.requireChoiceElement(ingredient.item, "item", CodeableConcept.class, Reference.class);
                ValidationSupport.checkReferenceType(ingredient.item, "item", "Substance");
                ValidationSupport.requireValueOrChildren(ingredient);
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
     * The price of the medication.
     */
    public static class Cost extends BackboneElement {
        @Required
        private final CodeableConcept type;
        private final String source;
        @Required
        private final Money cost;

        private Cost(Builder builder) {
            super(builder);
            type = builder.type;
            source = builder.source;
            cost = builder.cost;
        }

        /**
         * The category of the cost information. For example, manufacturers' cost, patient cost, claim reimbursement cost, actual 
         * acquisition cost.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The source or owner that assigns the price to the medication.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getSource() {
            return source;
        }

        /**
         * The price of the medication.
         * 
         * @return
         *     An immutable object of type {@link Money} that is non-null.
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

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private String source;
            private Money cost;

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
             * The category of the cost information. For example, manufacturers' cost, patient cost, claim reimbursement cost, actual 
             * acquisition cost.
             * 
             * <p>This element is required.
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
             * Convenience method for setting {@code source}.
             * 
             * @param source
             *     The source or owner for the price information
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #source(com.ibm.fhir.model.type.String)
             */
            public Builder source(java.lang.String source) {
                this.source = (source == null) ? null : String.of(source);
                return this;
            }

            /**
             * The source or owner that assigns the price to the medication.
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
             * The price of the medication.
             * 
             * <p>This element is required.
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

            /**
             * Build the {@link Cost}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>cost</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Cost}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Cost per the base specification
             */
            @Override
            public Cost build() {
                Cost cost = new Cost(this);
                if (validating) {
                    validate(cost);
                }
                return cost;
            }

            protected void validate(Cost cost) {
                super.validate(cost);
                ValidationSupport.requireNonNull(cost.type, "type");
                ValidationSupport.requireNonNull(cost.cost, "cost");
                ValidationSupport.requireValueOrChildren(cost);
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
     * The program under which the medication is reviewed.
     */
    public static class MonitoringProgram extends BackboneElement {
        private final CodeableConcept type;
        private final String name;

        private MonitoringProgram(Builder builder) {
            super(builder);
            type = builder.type;
            name = builder.name;
        }

        /**
         * Type of program under which the medication is monitored.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Name of the reviewing program.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
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
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private String name;

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
             * Type of program under which the medication is monitored.
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
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     Name of the reviewing program
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #name(com.ibm.fhir.model.type.String)
             */
            public Builder name(java.lang.String name) {
                this.name = (name == null) ? null : String.of(name);
                return this;
            }

            /**
             * Name of the reviewing program.
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

            /**
             * Build the {@link MonitoringProgram}
             * 
             * @return
             *     An immutable object of type {@link MonitoringProgram}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid MonitoringProgram per the base specification
             */
            @Override
            public MonitoringProgram build() {
                MonitoringProgram monitoringProgram = new MonitoringProgram(this);
                if (validating) {
                    validate(monitoringProgram);
                }
                return monitoringProgram;
            }

            protected void validate(MonitoringProgram monitoringProgram) {
                super.validate(monitoringProgram);
                ValidationSupport.requireValueOrChildren(monitoringProgram);
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
     * Guidelines for the administration of the medication.
     */
    public static class AdministrationGuidelines extends BackboneElement {
        private final List<Dosage> dosage;
        @ReferenceTarget({ "ObservationDefinition" })
        @Choice({ CodeableConcept.class, Reference.class })
        private final Element indication;
        private final List<PatientCharacteristics> patientCharacteristics;

        private AdministrationGuidelines(Builder builder) {
            super(builder);
            dosage = Collections.unmodifiableList(builder.dosage);
            indication = builder.indication;
            patientCharacteristics = Collections.unmodifiableList(builder.patientCharacteristics);
        }

        /**
         * Dosage for the medication for the specific guidelines.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Dosage} that may be empty.
         */
        public List<Dosage> getDosage() {
            return dosage;
        }

        /**
         * Indication for use that apply to the specific administration guidelines.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} or {@link Reference} that may be null.
         */
        public Element getIndication() {
            return indication;
        }

        /**
         * Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight, 
         * gender, etc.).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PatientCharacteristics} that may be empty.
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
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private List<Dosage> dosage = new ArrayList<>();
            private Element indication;
            private List<PatientCharacteristics> patientCharacteristics = new ArrayList<>();

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
             * Dosage for the medication for the specific guidelines.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Dosage for the medication for the specific guidelines.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param dosage
             *     Dosage for the medication for the specific guidelines
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder dosage(Collection<Dosage> dosage) {
                this.dosage = new ArrayList<>(dosage);
                return this;
            }

            /**
             * Indication for use that apply to the specific administration guidelines.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link ObservationDefinition}</li>
             * </ul>
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
             * Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight, 
             * gender, etc.).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight, 
             * gender, etc.).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param patientCharacteristics
             *     Characteristics of the patient that are relevant to the administration guidelines
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder patientCharacteristics(Collection<PatientCharacteristics> patientCharacteristics) {
                this.patientCharacteristics = new ArrayList<>(patientCharacteristics);
                return this;
            }

            /**
             * Build the {@link AdministrationGuidelines}
             * 
             * @return
             *     An immutable object of type {@link AdministrationGuidelines}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid AdministrationGuidelines per the base specification
             */
            @Override
            public AdministrationGuidelines build() {
                AdministrationGuidelines administrationGuidelines = new AdministrationGuidelines(this);
                if (validating) {
                    validate(administrationGuidelines);
                }
                return administrationGuidelines;
            }

            protected void validate(AdministrationGuidelines administrationGuidelines) {
                super.validate(administrationGuidelines);
                ValidationSupport.checkList(administrationGuidelines.dosage, "dosage", Dosage.class);
                ValidationSupport.choiceElement(administrationGuidelines.indication, "indication", CodeableConcept.class, Reference.class);
                ValidationSupport.checkList(administrationGuidelines.patientCharacteristics, "patientCharacteristics", PatientCharacteristics.class);
                ValidationSupport.checkReferenceType(administrationGuidelines.indication, "indication", "ObservationDefinition");
                ValidationSupport.requireValueOrChildren(administrationGuidelines);
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
         * Dosage for the medication for the specific guidelines.
         */
        public static class Dosage extends BackboneElement {
            @Required
            private final CodeableConcept type;
            @Required
            private final List<com.ibm.fhir.model.type.Dosage> dosage;

            private Dosage(Builder builder) {
                super(builder);
                type = builder.type;
                dosage = Collections.unmodifiableList(builder.dosage);
            }

            /**
             * The type of dosage (for example, prophylaxis, maintenance, therapeutic, etc.).
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * Dosage for the medication for the specific guidelines.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Dosage} that is non-empty.
             */
            public List<com.ibm.fhir.model.type.Dosage> getDosage() {
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
                        accept(dosage, "dosage", visitor, com.ibm.fhir.model.type.Dosage.class);
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

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept type;
                private List<com.ibm.fhir.model.type.Dosage> dosage = new ArrayList<>();

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
                 * The type of dosage (for example, prophylaxis, maintenance, therapeutic, etc.).
                 * 
                 * <p>This element is required.
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
                 * Dosage for the medication for the specific guidelines.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * <p>This element is required.
                 * 
                 * @param dosage
                 *     Dosage for the medication for the specific guidelines
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder dosage(com.ibm.fhir.model.type.Dosage... dosage) {
                    for (com.ibm.fhir.model.type.Dosage value : dosage) {
                        this.dosage.add(value);
                    }
                    return this;
                }

                /**
                 * Dosage for the medication for the specific guidelines.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * <p>This element is required.
                 * 
                 * @param dosage
                 *     Dosage for the medication for the specific guidelines
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder dosage(Collection<com.ibm.fhir.model.type.Dosage> dosage) {
                    this.dosage = new ArrayList<>(dosage);
                    return this;
                }

                /**
                 * Build the {@link Dosage}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * <li>dosage</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Dosage}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Dosage per the base specification
                 */
                @Override
                public Dosage build() {
                    Dosage dosage = new Dosage(this);
                    if (validating) {
                        validate(dosage);
                    }
                    return dosage;
                }

                protected void validate(Dosage dosage) {
                    super.validate(dosage);
                    ValidationSupport.requireNonNull(dosage.type, "type");
                    ValidationSupport.checkNonEmptyList(dosage.dosage, "dosage", com.ibm.fhir.model.type.Dosage.class);
                    ValidationSupport.requireValueOrChildren(dosage);
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
         * Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight, 
         * gender, etc.).
         */
        public static class PatientCharacteristics extends BackboneElement {
            @Choice({ CodeableConcept.class, SimpleQuantity.class })
            @Required
            private final Element characteristic;
            private final List<String> value;

            private PatientCharacteristics(Builder builder) {
                super(builder);
                characteristic = builder.characteristic;
                value = Collections.unmodifiableList(builder.value);
            }

            /**
             * Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} or {@link SimpleQuantity} that is non-null.
             */
            public Element getCharacteristic() {
                return characteristic;
            }

            /**
             * The specific characteristic (e.g. height, weight, gender, etc.).
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
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

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private Element characteristic;
                private List<String> value = new ArrayList<>();

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
                 * Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).
                 * 
                 * <p>This element is required.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link CodeableConcept}</li>
                 * <li>{@link SimpleQuantity}</li>
                 * </ul>
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
                 * Convenience method for setting {@code value}.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param value
                 *     The specific characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(com.ibm.fhir.model.type.String)
                 */
                public Builder value(java.lang.String... value) {
                    for (java.lang.String _value : value) {
                        this.value.add((_value == null) ? null : String.of(_value));
                    }
                    return this;
                }

                /**
                 * The specific characteristic (e.g. height, weight, gender, etc.).
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * The specific characteristic (e.g. height, weight, gender, etc.).
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param value
                 *     The specific characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder value(Collection<String> value) {
                    this.value = new ArrayList<>(value);
                    return this;
                }

                /**
                 * Build the {@link PatientCharacteristics}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>characteristic</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link PatientCharacteristics}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid PatientCharacteristics per the base specification
                 */
                @Override
                public PatientCharacteristics build() {
                    PatientCharacteristics patientCharacteristics = new PatientCharacteristics(this);
                    if (validating) {
                        validate(patientCharacteristics);
                    }
                    return patientCharacteristics;
                }

                protected void validate(PatientCharacteristics patientCharacteristics) {
                    super.validate(patientCharacteristics);
                    ValidationSupport.requireChoiceElement(patientCharacteristics.characteristic, "characteristic", CodeableConcept.class, SimpleQuantity.class);
                    ValidationSupport.checkList(patientCharacteristics.value, "value", String.class);
                    ValidationSupport.requireValueOrChildren(patientCharacteristics);
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
     * Categorization of the medication within a formulary or classification system.
     */
    public static class MedicineClassification extends BackboneElement {
        @Required
        private final CodeableConcept type;
        private final List<CodeableConcept> classification;

        private MedicineClassification(Builder builder) {
            super(builder);
            type = builder.type;
            classification = Collections.unmodifiableList(builder.classification);
        }

        /**
         * The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc.).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
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

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private List<CodeableConcept> classification = new ArrayList<>();

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
             * The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification).
             * 
             * <p>This element is required.
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
             * Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc.).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc.).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param classification
             *     Specific category assigned to the medication
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder classification(Collection<CodeableConcept> classification) {
                this.classification = new ArrayList<>(classification);
                return this;
            }

            /**
             * Build the {@link MedicineClassification}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link MedicineClassification}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid MedicineClassification per the base specification
             */
            @Override
            public MedicineClassification build() {
                MedicineClassification medicineClassification = new MedicineClassification(this);
                if (validating) {
                    validate(medicineClassification);
                }
                return medicineClassification;
            }

            protected void validate(MedicineClassification medicineClassification) {
                super.validate(medicineClassification);
                ValidationSupport.requireNonNull(medicineClassification.type, "type");
                ValidationSupport.checkList(medicineClassification.classification, "classification", CodeableConcept.class);
                ValidationSupport.requireValueOrChildren(medicineClassification);
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
     * Information that only applies to packages (not products).
     */
    public static class Packaging extends BackboneElement {
        @Binding(
            bindingName = "MedicationPackageType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A coded concept defining the type of packaging of a medication.",
            valueSet = "http://hl7.org/fhir/ValueSet/medicationknowledge-package-type"
        )
        private final CodeableConcept type;
        private final SimpleQuantity quantity;

        private Packaging(Builder builder) {
            super(builder);
            type = builder.type;
            quantity = builder.quantity;
        }

        /**
         * A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, 
         * bottle).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The number of product units the package would contain if fully loaded.
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getQuantity() {
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
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private SimpleQuantity quantity;

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
             * A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, 
             * bottle).
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
             * The number of product units the package would contain if fully loaded.
             * 
             * @param quantity
             *     The number of product units the package would contain if fully loaded
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(SimpleQuantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * Build the {@link Packaging}
             * 
             * @return
             *     An immutable object of type {@link Packaging}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Packaging per the base specification
             */
            @Override
            public Packaging build() {
                Packaging packaging = new Packaging(this);
                if (validating) {
                    validate(packaging);
                }
                return packaging;
            }

            protected void validate(Packaging packaging) {
                super.validate(packaging);
                ValidationSupport.requireValueOrChildren(packaging);
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
     * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
     */
    public static class DrugCharacteristic extends BackboneElement {
        @Binding(
            bindingName = "MedicationCharacteristic",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A coded concept defining the characteristic types of a medication.",
            valueSet = "http://hl7.org/fhir/ValueSet/medicationknowledge-characteristic"
        )
        private final CodeableConcept type;
        @Choice({ CodeableConcept.class, String.class, SimpleQuantity.class, Base64Binary.class })
        private final Element value;

        private DrugCharacteristic(Builder builder) {
            super(builder);
            type = builder.type;
            value = builder.value;
        }

        /**
         * A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Description of the characteristic.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}, {@link String}, {@link SimpleQuantity} or {@link Base64Binary} 
         *     that may be null.
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
             * A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint).
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
             * Convenience method for setting {@code value} with choice type String.
             * 
             * @param value
             *     Description of the characteristic
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
             * Description of the characteristic.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link String}</li>
             * <li>{@link SimpleQuantity}</li>
             * <li>{@link Base64Binary}</li>
             * </ul>
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

            /**
             * Build the {@link DrugCharacteristic}
             * 
             * @return
             *     An immutable object of type {@link DrugCharacteristic}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid DrugCharacteristic per the base specification
             */
            @Override
            public DrugCharacteristic build() {
                DrugCharacteristic drugCharacteristic = new DrugCharacteristic(this);
                if (validating) {
                    validate(drugCharacteristic);
                }
                return drugCharacteristic;
            }

            protected void validate(DrugCharacteristic drugCharacteristic) {
                super.validate(drugCharacteristic);
                ValidationSupport.choiceElement(drugCharacteristic.value, "value", CodeableConcept.class, String.class, SimpleQuantity.class, Base64Binary.class);
                ValidationSupport.requireValueOrChildren(drugCharacteristic);
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
     * Regulatory information about a medication.
     */
    public static class Regulatory extends BackboneElement {
        @ReferenceTarget({ "Organization" })
        @Required
        private final Reference regulatoryAuthority;
        private final List<Substitution> substitution;
        private final List<Schedule> schedule;
        private final MaxDispense maxDispense;

        private Regulatory(Builder builder) {
            super(builder);
            regulatoryAuthority = builder.regulatoryAuthority;
            substitution = Collections.unmodifiableList(builder.substitution);
            schedule = Collections.unmodifiableList(builder.schedule);
            maxDispense = builder.maxDispense;
        }

        /**
         * The authority that is specifying the regulations.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getRegulatoryAuthority() {
            return regulatoryAuthority;
        }

        /**
         * Specifies if changes are allowed when dispensing a medication from a regulatory perspective.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Substitution} that may be empty.
         */
        public List<Substitution> getSubstitution() {
            return substitution;
        }

        /**
         * Specifies the schedule of a medication in jurisdiction.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Schedule} that may be empty.
         */
        public List<Schedule> getSchedule() {
            return schedule;
        }

        /**
         * The maximum number of units of the medication that can be dispensed in a period.
         * 
         * @return
         *     An immutable object of type {@link MaxDispense} that may be null.
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

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private Reference regulatoryAuthority;
            private List<Substitution> substitution = new ArrayList<>();
            private List<Schedule> schedule = new ArrayList<>();
            private MaxDispense maxDispense;

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
             * The authority that is specifying the regulations.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
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
             * Specifies if changes are allowed when dispensing a medication from a regulatory perspective.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Specifies if changes are allowed when dispensing a medication from a regulatory perspective.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param substitution
             *     Specifies if changes are allowed when dispensing a medication from a regulatory perspective
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder substitution(Collection<Substitution> substitution) {
                this.substitution = new ArrayList<>(substitution);
                return this;
            }

            /**
             * Specifies the schedule of a medication in jurisdiction.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Specifies the schedule of a medication in jurisdiction.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param schedule
             *     Specifies the schedule of a medication in jurisdiction
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder schedule(Collection<Schedule> schedule) {
                this.schedule = new ArrayList<>(schedule);
                return this;
            }

            /**
             * The maximum number of units of the medication that can be dispensed in a period.
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

            /**
             * Build the {@link Regulatory}
             * 
             * <p>Required elements:
             * <ul>
             * <li>regulatoryAuthority</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Regulatory}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Regulatory per the base specification
             */
            @Override
            public Regulatory build() {
                Regulatory regulatory = new Regulatory(this);
                if (validating) {
                    validate(regulatory);
                }
                return regulatory;
            }

            protected void validate(Regulatory regulatory) {
                super.validate(regulatory);
                ValidationSupport.requireNonNull(regulatory.regulatoryAuthority, "regulatoryAuthority");
                ValidationSupport.checkList(regulatory.substitution, "substitution", Substitution.class);
                ValidationSupport.checkList(regulatory.schedule, "schedule", Schedule.class);
                ValidationSupport.checkReferenceType(regulatory.regulatoryAuthority, "regulatoryAuthority", "Organization");
                ValidationSupport.requireValueOrChildren(regulatory);
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
         * Specifies if changes are allowed when dispensing a medication from a regulatory perspective.
         */
        public static class Substitution extends BackboneElement {
            @Required
            private final CodeableConcept type;
            @Required
            private final Boolean allowed;

            private Substitution(Builder builder) {
                super(builder);
                type = builder.type;
                allowed = builder.allowed;
            }

            /**
             * Specifies the type of substitution allowed.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * Specifies if regulation allows for changes in the medication when dispensing.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that is non-null.
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

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept type;
                private Boolean allowed;

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
                 * Specifies the type of substitution allowed.
                 * 
                 * <p>This element is required.
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
                 * Convenience method for setting {@code allowed}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param allowed
                 *     Specifies if regulation allows for changes in the medication when dispensing
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #allowed(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder allowed(java.lang.Boolean allowed) {
                    this.allowed = (allowed == null) ? null : Boolean.of(allowed);
                    return this;
                }

                /**
                 * Specifies if regulation allows for changes in the medication when dispensing.
                 * 
                 * <p>This element is required.
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

                /**
                 * Build the {@link Substitution}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * <li>allowed</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Substitution}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Substitution per the base specification
                 */
                @Override
                public Substitution build() {
                    Substitution substitution = new Substitution(this);
                    if (validating) {
                        validate(substitution);
                    }
                    return substitution;
                }

                protected void validate(Substitution substitution) {
                    super.validate(substitution);
                    ValidationSupport.requireNonNull(substitution.type, "type");
                    ValidationSupport.requireNonNull(substitution.allowed, "allowed");
                    ValidationSupport.requireValueOrChildren(substitution);
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
         * Specifies the schedule of a medication in jurisdiction.
         */
        public static class Schedule extends BackboneElement {
            @Required
            private final CodeableConcept schedule;

            private Schedule(Builder builder) {
                super(builder);
                schedule = builder.schedule;
            }

            /**
             * Specifies the specific drug schedule.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
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

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept schedule;

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
                 * Specifies the specific drug schedule.
                 * 
                 * <p>This element is required.
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

                /**
                 * Build the {@link Schedule}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>schedule</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Schedule}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Schedule per the base specification
                 */
                @Override
                public Schedule build() {
                    Schedule schedule = new Schedule(this);
                    if (validating) {
                        validate(schedule);
                    }
                    return schedule;
                }

                protected void validate(Schedule schedule) {
                    super.validate(schedule);
                    ValidationSupport.requireNonNull(schedule.schedule, "schedule");
                    ValidationSupport.requireValueOrChildren(schedule);
                }

                protected Builder from(Schedule schedule) {
                    super.from(schedule);
                    this.schedule = schedule.schedule;
                    return this;
                }
            }
        }

        /**
         * The maximum number of units of the medication that can be dispensed in a period.
         */
        public static class MaxDispense extends BackboneElement {
            @Required
            private final SimpleQuantity quantity;
            private final Duration period;

            private MaxDispense(Builder builder) {
                super(builder);
                quantity = builder.quantity;
                period = builder.period;
            }

            /**
             * The maximum number of units of the medication that can be dispensed.
             * 
             * @return
             *     An immutable object of type {@link SimpleQuantity} that is non-null.
             */
            public SimpleQuantity getQuantity() {
                return quantity;
            }

            /**
             * The period that applies to the maximum number of units.
             * 
             * @return
             *     An immutable object of type {@link Duration} that may be null.
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

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private SimpleQuantity quantity;
                private Duration period;

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
                 * The maximum number of units of the medication that can be dispensed.
                 * 
                 * <p>This element is required.
                 * 
                 * @param quantity
                 *     The maximum number of units of the medication that can be dispensed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder quantity(SimpleQuantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * The period that applies to the maximum number of units.
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

                /**
                 * Build the {@link MaxDispense}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>quantity</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link MaxDispense}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid MaxDispense per the base specification
                 */
                @Override
                public MaxDispense build() {
                    MaxDispense maxDispense = new MaxDispense(this);
                    if (validating) {
                        validate(maxDispense);
                    }
                    return maxDispense;
                }

                protected void validate(MaxDispense maxDispense) {
                    super.validate(maxDispense);
                    ValidationSupport.requireNonNull(maxDispense.quantity, "quantity");
                    ValidationSupport.requireValueOrChildren(maxDispense);
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
     * The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.
     */
    public static class Kinetics extends BackboneElement {
        private final List<SimpleQuantity> areaUnderCurve;
        private final List<SimpleQuantity> lethalDose50;
        private final Duration halfLifePeriod;

        private Kinetics(Builder builder) {
            super(builder);
            areaUnderCurve = Collections.unmodifiableList(builder.areaUnderCurve);
            lethalDose50 = Collections.unmodifiableList(builder.lethalDose50);
            halfLifePeriod = builder.halfLifePeriod;
        }

        /**
         * The drug concentration measured at certain discrete points in time.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link SimpleQuantity} that may be empty.
         */
        public List<SimpleQuantity> getAreaUnderCurve() {
            return areaUnderCurve;
        }

        /**
         * The median lethal dose of a drug.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link SimpleQuantity} that may be empty.
         */
        public List<SimpleQuantity> getLethalDose50() {
            return lethalDose50;
        }

        /**
         * The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.
         * 
         * @return
         *     An immutable object of type {@link Duration} that may be null.
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
                    accept(areaUnderCurve, "areaUnderCurve", visitor, SimpleQuantity.class);
                    accept(lethalDose50, "lethalDose50", visitor, SimpleQuantity.class);
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
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private List<SimpleQuantity> areaUnderCurve = new ArrayList<>();
            private List<SimpleQuantity> lethalDose50 = new ArrayList<>();
            private Duration halfLifePeriod;

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
             * The drug concentration measured at certain discrete points in time.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param areaUnderCurve
             *     The drug concentration measured at certain discrete points in time
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder areaUnderCurve(SimpleQuantity... areaUnderCurve) {
                for (SimpleQuantity value : areaUnderCurve) {
                    this.areaUnderCurve.add(value);
                }
                return this;
            }

            /**
             * The drug concentration measured at certain discrete points in time.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param areaUnderCurve
             *     The drug concentration measured at certain discrete points in time
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder areaUnderCurve(Collection<SimpleQuantity> areaUnderCurve) {
                this.areaUnderCurve = new ArrayList<>(areaUnderCurve);
                return this;
            }

            /**
             * The median lethal dose of a drug.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param lethalDose50
             *     The median lethal dose of a drug
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder lethalDose50(SimpleQuantity... lethalDose50) {
                for (SimpleQuantity value : lethalDose50) {
                    this.lethalDose50.add(value);
                }
                return this;
            }

            /**
             * The median lethal dose of a drug.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param lethalDose50
             *     The median lethal dose of a drug
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder lethalDose50(Collection<SimpleQuantity> lethalDose50) {
                this.lethalDose50 = new ArrayList<>(lethalDose50);
                return this;
            }

            /**
             * The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.
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

            /**
             * Build the {@link Kinetics}
             * 
             * @return
             *     An immutable object of type {@link Kinetics}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Kinetics per the base specification
             */
            @Override
            public Kinetics build() {
                Kinetics kinetics = new Kinetics(this);
                if (validating) {
                    validate(kinetics);
                }
                return kinetics;
            }

            protected void validate(Kinetics kinetics) {
                super.validate(kinetics);
                ValidationSupport.checkList(kinetics.areaUnderCurve, "areaUnderCurve", SimpleQuantity.class);
                ValidationSupport.checkList(kinetics.lethalDose50, "lethalDose50", SimpleQuantity.class);
                ValidationSupport.requireValueOrChildren(kinetics);
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
