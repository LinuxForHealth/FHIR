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

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.CodeableReference;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.MarketingStatus;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use, 
 * drug catalogs).
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "medicinalProductDefinition-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/publication-status",
    expression = "status.exists() implies (status.memberOf('http://hl7.org/fhir/ValueSet/publication-status', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/MedicinalProductDefinition",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicinalProductDefinition extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "MedicinalProductType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Overall defining type of this medicinal product.",
        valueSet = "http://hl7.org/fhir/ValueSet/medicinal-product-type"
    )
    private final CodeableConcept type;
    @Summary
    @Binding(
        bindingName = "MedicinalProductType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Applicable domain for this product (e.g. human, veterinary).",
        valueSet = "http://hl7.org/fhir/ValueSet/medicinal-product-domain"
    )
    private final CodeableConcept domain;
    @Summary
    private final String version;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.PREFERRED,
        description = "Identifies the level of importance to be assigned to actioning the request.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status"
    )
    private final CodeableConcept status;
    @Summary
    private final DateTime statusDate;
    @Summary
    private final Markdown description;
    @Summary
    @Binding(
        bindingName = "CombinedDoseForm",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Dose forms for a product as a whole, considering all individual parts, but before any mixing",
        valueSet = "http://hl7.org/fhir/ValueSet/combined-dose-form"
    )
    private final CodeableConcept combinedPharmaceuticalDoseForm;
    @Summary
    @Binding(
        bindingName = "SNOMEDCTRouteCodes",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient's body.",
        valueSet = "http://hl7.org/fhir/ValueSet/route-codes"
    )
    private final List<CodeableConcept> route;
    @Summary
    private final Markdown indication;
    @Summary
    @Binding(
        bindingName = "LegalStatusOfSupply",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The prescription supply types appropriate to a medicinal product",
        valueSet = "http://hl7.org/fhir/ValueSet/legal-status-of-supply"
    )
    private final CodeableConcept legalStatusOfSupply;
    @Summary
    private final CodeableConcept additionalMonitoringIndicator;
    @Summary
    private final List<CodeableConcept> specialMeasures;
    @Summary
    private final CodeableConcept pediatricUseIndicator;
    @Summary
    private final List<CodeableConcept> classification;
    @Summary
    private final List<MarketingStatus> marketingStatus;
    @Summary
    @Binding(
        bindingName = "MedicinalProductPackageType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Types of medicinal product packs",
        valueSet = "http://hl7.org/fhir/ValueSet/medicinal-product-package-type"
    )
    private final List<CodeableConcept> packagedMedicinalProduct;
    @Summary
    private final List<CodeableConcept> ingredient;
    @Summary
    private final List<CodeableReference> impurity;
    @Summary
    @ReferenceTarget({ "DocumentReference" })
    private final List<Reference> attachedDocument;
    @Summary
    @ReferenceTarget({ "DocumentReference" })
    private final List<Reference> masterFile;
    @Summary
    private final List<Contact> contact;
    @Summary
    @ReferenceTarget({ "ResearchStudy" })
    private final List<Reference> clinicalTrial;
    @Summary
    @Binding(
        bindingName = "MedicationFormalRepresentation",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept that defines the type of a medication.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-codes"
    )
    private final List<Coding> code;
    @Summary
    @Required
    private final List<Name> name;
    @Summary
    private final List<CrossReference> crossReference;
    @Summary
    private final List<Operation> operation;
    @Summary
    private final List<Characteristic> characteristic;

    private MedicinalProductDefinition(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        type = builder.type;
        domain = builder.domain;
        version = builder.version;
        status = builder.status;
        statusDate = builder.statusDate;
        description = builder.description;
        combinedPharmaceuticalDoseForm = builder.combinedPharmaceuticalDoseForm;
        route = Collections.unmodifiableList(builder.route);
        indication = builder.indication;
        legalStatusOfSupply = builder.legalStatusOfSupply;
        additionalMonitoringIndicator = builder.additionalMonitoringIndicator;
        specialMeasures = Collections.unmodifiableList(builder.specialMeasures);
        pediatricUseIndicator = builder.pediatricUseIndicator;
        classification = Collections.unmodifiableList(builder.classification);
        marketingStatus = Collections.unmodifiableList(builder.marketingStatus);
        packagedMedicinalProduct = Collections.unmodifiableList(builder.packagedMedicinalProduct);
        ingredient = Collections.unmodifiableList(builder.ingredient);
        impurity = Collections.unmodifiableList(builder.impurity);
        attachedDocument = Collections.unmodifiableList(builder.attachedDocument);
        masterFile = Collections.unmodifiableList(builder.masterFile);
        contact = Collections.unmodifiableList(builder.contact);
        clinicalTrial = Collections.unmodifiableList(builder.clinicalTrial);
        code = Collections.unmodifiableList(builder.code);
        name = Collections.unmodifiableList(builder.name);
        crossReference = Collections.unmodifiableList(builder.crossReference);
        operation = Collections.unmodifiableList(builder.operation);
        characteristic = Collections.unmodifiableList(builder.characteristic);
    }

    /**
     * Business identifier for this product. Could be an MPID.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Regulatory type, e.g. Investigational or Authorized.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * If this medicine applies to human or veterinary uses.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getDomain() {
        return domain;
    }

    /**
     * A business identifier relating to a specific version of the product, this is commonly used to support revisions to an 
     * existing product.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * The status within the lifecycle of this product record. A high-level status, this is not intended to duplicate details 
     * carried elsewhere such as legal status, or authorization status.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatus() {
        return status;
    }

    /**
     * The date at which the given status became applicable.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getStatusDate() {
        return statusDate;
    }

    /**
     * General description of this product.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * The dose form for a single part product, or combined form of a multiple part product.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCombinedPharmaceuticalDoseForm() {
        return combinedPharmaceuticalDoseForm;
    }

    /**
     * The path by which the product is taken into or makes contact with the body. In some regions this is referred to as the 
     * licenced or approved route. See also AdministrableProductDefinition resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getRoute() {
        return route;
    }

    /**
     * Description of indication(s) for this product, used when structured indications are not required. In cases where 
     * structured indications are required, they are captured using the ClinicalUseDefinition resource. An indication is a 
     * medical situation for which using the product is appropriate.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getIndication() {
        return indication;
    }

    /**
     * The legal status of supply of the medicinal product as classified by the regulator.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getLegalStatusOfSupply() {
        return legalStatusOfSupply;
    }

    /**
     * Whether the Medicinal Product is subject to additional monitoring for regulatory reasons.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getAdditionalMonitoringIndicator() {
        return additionalMonitoringIndicator;
    }

    /**
     * Whether the Medicinal Product is subject to special measures for regulatory reasons.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getSpecialMeasures() {
        return specialMeasures;
    }

    /**
     * If authorised for use in children.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPediatricUseIndicator() {
        return pediatricUseIndicator;
    }

    /**
     * Allows the product to be classified by various systems.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getClassification() {
        return classification;
    }

    /**
     * Marketing status of the medicinal product, in contrast to marketing authorization.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MarketingStatus} that may be empty.
     */
    public List<MarketingStatus> getMarketingStatus() {
        return marketingStatus;
    }

    /**
     * Package representation for the product. See also the PackagedProductDefinition resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getPackagedMedicinalProduct() {
        return packagedMedicinalProduct;
    }

    /**
     * The ingredients of this medicinal product - when not detailed in other resources. This is only needed if the 
     * ingredients are not specified by incoming references from the Ingredient resource, or indirectly via incoming 
     * AdministrableProductDefinition, PackagedProductDefinition or ManufacturedItemDefinition references. In cases where 
     * those levels of detail are not used, the ingredients may be specified directly here as codes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getIngredient() {
        return ingredient;
    }

    /**
     * Any component of the drug product which is not the chemical entity defined as the drug substance or an excipient in 
     * the drug product. This includes process-related impurities and contaminants, product-related impurities including 
     * degradation products.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableReference} that may be empty.
     */
    public List<CodeableReference> getImpurity() {
        return impurity;
    }

    /**
     * Additional information or supporting documentation about the medicinal product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAttachedDocument() {
        return attachedDocument;
    }

    /**
     * A master file for the medicinal product (e.g. Pharmacovigilance System Master File). Drug master files (DMFs) are 
     * documents submitted to regulatory agencies to provide confidential detailed information about facilities, processes or 
     * articles used in the manufacturing, processing, packaging and storing of drug products.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getMasterFile() {
        return masterFile;
    }

    /**
     * A product specific contact, person (in a role), or an organization.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Contact} that may be empty.
     */
    public List<Contact> getContact() {
        return contact;
    }

    /**
     * Clinical trials or studies that this product is involved in.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getClinicalTrial() {
        return clinicalTrial;
    }

    /**
     * A code that this product is known by, usually within some formal terminology. Products (types of medications) tend to 
     * be known by identifiers during development and within regulatory process. However when they are prescribed they tend 
     * to be identified by codes. The same product may be have multiple codes, applied to it by multiple organizations.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
     */
    public List<Coding> getCode() {
        return code;
    }

    /**
     * The product's name, including full name and possibly coded parts.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Name} that is non-empty.
     */
    public List<Name> getName() {
        return name;
    }

    /**
     * Reference to another product, e.g. for linking authorised to investigational product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CrossReference} that may be empty.
     */
    public List<CrossReference> getCrossReference() {
        return crossReference;
    }

    /**
     * A manufacturing or administrative process or step associated with (or performed on) the medicinal product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Operation} that may be empty.
     */
    public List<Operation> getOperation() {
        return operation;
    }

    /**
     * Allows the key product features to be recorded, such as "sugar free", "modified release", "parallel import".
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Characteristic} that may be empty.
     */
    public List<Characteristic> getCharacteristic() {
        return characteristic;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (type != null) || 
            (domain != null) || 
            (version != null) || 
            (status != null) || 
            (statusDate != null) || 
            (description != null) || 
            (combinedPharmaceuticalDoseForm != null) || 
            !route.isEmpty() || 
            (indication != null) || 
            (legalStatusOfSupply != null) || 
            (additionalMonitoringIndicator != null) || 
            !specialMeasures.isEmpty() || 
            (pediatricUseIndicator != null) || 
            !classification.isEmpty() || 
            !marketingStatus.isEmpty() || 
            !packagedMedicinalProduct.isEmpty() || 
            !ingredient.isEmpty() || 
            !impurity.isEmpty() || 
            !attachedDocument.isEmpty() || 
            !masterFile.isEmpty() || 
            !contact.isEmpty() || 
            !clinicalTrial.isEmpty() || 
            !code.isEmpty() || 
            !name.isEmpty() || 
            !crossReference.isEmpty() || 
            !operation.isEmpty() || 
            !characteristic.isEmpty();
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
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(type, "type", visitor);
                accept(domain, "domain", visitor);
                accept(version, "version", visitor);
                accept(status, "status", visitor);
                accept(statusDate, "statusDate", visitor);
                accept(description, "description", visitor);
                accept(combinedPharmaceuticalDoseForm, "combinedPharmaceuticalDoseForm", visitor);
                accept(route, "route", visitor, CodeableConcept.class);
                accept(indication, "indication", visitor);
                accept(legalStatusOfSupply, "legalStatusOfSupply", visitor);
                accept(additionalMonitoringIndicator, "additionalMonitoringIndicator", visitor);
                accept(specialMeasures, "specialMeasures", visitor, CodeableConcept.class);
                accept(pediatricUseIndicator, "pediatricUseIndicator", visitor);
                accept(classification, "classification", visitor, CodeableConcept.class);
                accept(marketingStatus, "marketingStatus", visitor, MarketingStatus.class);
                accept(packagedMedicinalProduct, "packagedMedicinalProduct", visitor, CodeableConcept.class);
                accept(ingredient, "ingredient", visitor, CodeableConcept.class);
                accept(impurity, "impurity", visitor, CodeableReference.class);
                accept(attachedDocument, "attachedDocument", visitor, Reference.class);
                accept(masterFile, "masterFile", visitor, Reference.class);
                accept(contact, "contact", visitor, Contact.class);
                accept(clinicalTrial, "clinicalTrial", visitor, Reference.class);
                accept(code, "code", visitor, Coding.class);
                accept(name, "name", visitor, Name.class);
                accept(crossReference, "crossReference", visitor, CrossReference.class);
                accept(operation, "operation", visitor, Operation.class);
                accept(characteristic, "characteristic", visitor, Characteristic.class);
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
        MedicinalProductDefinition other = (MedicinalProductDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(type, other.type) && 
            Objects.equals(domain, other.domain) && 
            Objects.equals(version, other.version) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusDate, other.statusDate) && 
            Objects.equals(description, other.description) && 
            Objects.equals(combinedPharmaceuticalDoseForm, other.combinedPharmaceuticalDoseForm) && 
            Objects.equals(route, other.route) && 
            Objects.equals(indication, other.indication) && 
            Objects.equals(legalStatusOfSupply, other.legalStatusOfSupply) && 
            Objects.equals(additionalMonitoringIndicator, other.additionalMonitoringIndicator) && 
            Objects.equals(specialMeasures, other.specialMeasures) && 
            Objects.equals(pediatricUseIndicator, other.pediatricUseIndicator) && 
            Objects.equals(classification, other.classification) && 
            Objects.equals(marketingStatus, other.marketingStatus) && 
            Objects.equals(packagedMedicinalProduct, other.packagedMedicinalProduct) && 
            Objects.equals(ingredient, other.ingredient) && 
            Objects.equals(impurity, other.impurity) && 
            Objects.equals(attachedDocument, other.attachedDocument) && 
            Objects.equals(masterFile, other.masterFile) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(clinicalTrial, other.clinicalTrial) && 
            Objects.equals(code, other.code) && 
            Objects.equals(name, other.name) && 
            Objects.equals(crossReference, other.crossReference) && 
            Objects.equals(operation, other.operation) && 
            Objects.equals(characteristic, other.characteristic);
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
                type, 
                domain, 
                version, 
                status, 
                statusDate, 
                description, 
                combinedPharmaceuticalDoseForm, 
                route, 
                indication, 
                legalStatusOfSupply, 
                additionalMonitoringIndicator, 
                specialMeasures, 
                pediatricUseIndicator, 
                classification, 
                marketingStatus, 
                packagedMedicinalProduct, 
                ingredient, 
                impurity, 
                attachedDocument, 
                masterFile, 
                contact, 
                clinicalTrial, 
                code, 
                name, 
                crossReference, 
                operation, 
                characteristic);
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
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept type;
        private CodeableConcept domain;
        private String version;
        private CodeableConcept status;
        private DateTime statusDate;
        private Markdown description;
        private CodeableConcept combinedPharmaceuticalDoseForm;
        private List<CodeableConcept> route = new ArrayList<>();
        private Markdown indication;
        private CodeableConcept legalStatusOfSupply;
        private CodeableConcept additionalMonitoringIndicator;
        private List<CodeableConcept> specialMeasures = new ArrayList<>();
        private CodeableConcept pediatricUseIndicator;
        private List<CodeableConcept> classification = new ArrayList<>();
        private List<MarketingStatus> marketingStatus = new ArrayList<>();
        private List<CodeableConcept> packagedMedicinalProduct = new ArrayList<>();
        private List<CodeableConcept> ingredient = new ArrayList<>();
        private List<CodeableReference> impurity = new ArrayList<>();
        private List<Reference> attachedDocument = new ArrayList<>();
        private List<Reference> masterFile = new ArrayList<>();
        private List<Contact> contact = new ArrayList<>();
        private List<Reference> clinicalTrial = new ArrayList<>();
        private List<Coding> code = new ArrayList<>();
        private List<Name> name = new ArrayList<>();
        private List<CrossReference> crossReference = new ArrayList<>();
        private List<Operation> operation = new ArrayList<>();
        private List<Characteristic> characteristic = new ArrayList<>();

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
         * Business identifier for this product. Could be an MPID.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier for this product. Could be an MPID
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
         * Business identifier for this product. Could be an MPID.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier for this product. Could be an MPID
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
         * Regulatory type, e.g. Investigational or Authorized.
         * 
         * @param type
         *     Regulatory type, e.g. Investigational or Authorized
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * If this medicine applies to human or veterinary uses.
         * 
         * @param domain
         *     If this medicine applies to human or veterinary uses
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder domain(CodeableConcept domain) {
            this.domain = domain;
            return this;
        }

        /**
         * Convenience method for setting {@code version}.
         * 
         * @param version
         *     A business identifier relating to a specific version of the product, this is commonly used to support revisions to an 
         *     existing product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #version(com.ibm.fhir.model.type.String)
         */
        public Builder version(java.lang.String version) {
            this.version = (version == null) ? null : String.of(version);
            return this;
        }

        /**
         * A business identifier relating to a specific version of the product, this is commonly used to support revisions to an 
         * existing product.
         * 
         * @param version
         *     A business identifier relating to a specific version of the product, this is commonly used to support revisions to an 
         *     existing product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * The status within the lifecycle of this product record. A high-level status, this is not intended to duplicate details 
         * carried elsewhere such as legal status, or authorization status.
         * 
         * @param status
         *     The status within the lifecycle of this product record. A high-level status, this is not intended to duplicate details 
         *     carried elsewhere such as legal status, or authorization status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CodeableConcept status) {
            this.status = status;
            return this;
        }

        /**
         * The date at which the given status became applicable.
         * 
         * @param statusDate
         *     The date at which the given status became applicable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusDate(DateTime statusDate) {
            this.statusDate = statusDate;
            return this;
        }

        /**
         * General description of this product.
         * 
         * @param description
         *     General description of this product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * The dose form for a single part product, or combined form of a multiple part product.
         * 
         * @param combinedPharmaceuticalDoseForm
         *     The dose form for a single part product, or combined form of a multiple part product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder combinedPharmaceuticalDoseForm(CodeableConcept combinedPharmaceuticalDoseForm) {
            this.combinedPharmaceuticalDoseForm = combinedPharmaceuticalDoseForm;
            return this;
        }

        /**
         * The path by which the product is taken into or makes contact with the body. In some regions this is referred to as the 
         * licenced or approved route. See also AdministrableProductDefinition resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param route
         *     The path by which the product is taken into or makes contact with the body. In some regions this is referred to as the 
         *     licenced or approved route. See also AdministrableProductDefinition resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder route(CodeableConcept... route) {
            for (CodeableConcept value : route) {
                this.route.add(value);
            }
            return this;
        }

        /**
         * The path by which the product is taken into or makes contact with the body. In some regions this is referred to as the 
         * licenced or approved route. See also AdministrableProductDefinition resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param route
         *     The path by which the product is taken into or makes contact with the body. In some regions this is referred to as the 
         *     licenced or approved route. See also AdministrableProductDefinition resource
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder route(Collection<CodeableConcept> route) {
            this.route = new ArrayList<>(route);
            return this;
        }

        /**
         * Description of indication(s) for this product, used when structured indications are not required. In cases where 
         * structured indications are required, they are captured using the ClinicalUseDefinition resource. An indication is a 
         * medical situation for which using the product is appropriate.
         * 
         * @param indication
         *     Description of indication(s) for this product, used when structured indications are not required. In cases where 
         *     structured indications are required, they are captured using the ClinicalUseDefinition resource. An indication is a 
         *     medical situation for which using the product is appropriate
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder indication(Markdown indication) {
            this.indication = indication;
            return this;
        }

        /**
         * The legal status of supply of the medicinal product as classified by the regulator.
         * 
         * @param legalStatusOfSupply
         *     The legal status of supply of the medicinal product as classified by the regulator
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder legalStatusOfSupply(CodeableConcept legalStatusOfSupply) {
            this.legalStatusOfSupply = legalStatusOfSupply;
            return this;
        }

        /**
         * Whether the Medicinal Product is subject to additional monitoring for regulatory reasons.
         * 
         * @param additionalMonitoringIndicator
         *     Whether the Medicinal Product is subject to additional monitoring for regulatory reasons
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder additionalMonitoringIndicator(CodeableConcept additionalMonitoringIndicator) {
            this.additionalMonitoringIndicator = additionalMonitoringIndicator;
            return this;
        }

        /**
         * Whether the Medicinal Product is subject to special measures for regulatory reasons.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialMeasures
         *     Whether the Medicinal Product is subject to special measures for regulatory reasons
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specialMeasures(CodeableConcept... specialMeasures) {
            for (CodeableConcept value : specialMeasures) {
                this.specialMeasures.add(value);
            }
            return this;
        }

        /**
         * Whether the Medicinal Product is subject to special measures for regulatory reasons.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialMeasures
         *     Whether the Medicinal Product is subject to special measures for regulatory reasons
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder specialMeasures(Collection<CodeableConcept> specialMeasures) {
            this.specialMeasures = new ArrayList<>(specialMeasures);
            return this;
        }

        /**
         * If authorised for use in children.
         * 
         * @param pediatricUseIndicator
         *     If authorised for use in children
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder pediatricUseIndicator(CodeableConcept pediatricUseIndicator) {
            this.pediatricUseIndicator = pediatricUseIndicator;
            return this;
        }

        /**
         * Allows the product to be classified by various systems.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param classification
         *     Allows the product to be classified by various systems
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
         * Allows the product to be classified by various systems.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param classification
         *     Allows the product to be classified by various systems
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
         * Marketing status of the medicinal product, in contrast to marketing authorization.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param marketingStatus
         *     Marketing status of the medicinal product, in contrast to marketing authorization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder marketingStatus(MarketingStatus... marketingStatus) {
            for (MarketingStatus value : marketingStatus) {
                this.marketingStatus.add(value);
            }
            return this;
        }

        /**
         * Marketing status of the medicinal product, in contrast to marketing authorization.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param marketingStatus
         *     Marketing status of the medicinal product, in contrast to marketing authorization
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder marketingStatus(Collection<MarketingStatus> marketingStatus) {
            this.marketingStatus = new ArrayList<>(marketingStatus);
            return this;
        }

        /**
         * Package representation for the product. See also the PackagedProductDefinition resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param packagedMedicinalProduct
         *     Package representation for the product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder packagedMedicinalProduct(CodeableConcept... packagedMedicinalProduct) {
            for (CodeableConcept value : packagedMedicinalProduct) {
                this.packagedMedicinalProduct.add(value);
            }
            return this;
        }

        /**
         * Package representation for the product. See also the PackagedProductDefinition resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param packagedMedicinalProduct
         *     Package representation for the product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder packagedMedicinalProduct(Collection<CodeableConcept> packagedMedicinalProduct) {
            this.packagedMedicinalProduct = new ArrayList<>(packagedMedicinalProduct);
            return this;
        }

        /**
         * The ingredients of this medicinal product - when not detailed in other resources. This is only needed if the 
         * ingredients are not specified by incoming references from the Ingredient resource, or indirectly via incoming 
         * AdministrableProductDefinition, PackagedProductDefinition or ManufacturedItemDefinition references. In cases where 
         * those levels of detail are not used, the ingredients may be specified directly here as codes.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param ingredient
         *     The ingredients of this medicinal product - when not detailed in other resources. This is only needed if the 
         *     ingredients are not specified by incoming references from the Ingredient resource, or indirectly via incoming 
         *     AdministrableProductDefinition, PackagedProductDefinition or ManufacturedItemDefinition references. In cases where 
         *     those levels of detail are not used, the ingredients may be specified directly here as codes
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder ingredient(CodeableConcept... ingredient) {
            for (CodeableConcept value : ingredient) {
                this.ingredient.add(value);
            }
            return this;
        }

        /**
         * The ingredients of this medicinal product - when not detailed in other resources. This is only needed if the 
         * ingredients are not specified by incoming references from the Ingredient resource, or indirectly via incoming 
         * AdministrableProductDefinition, PackagedProductDefinition or ManufacturedItemDefinition references. In cases where 
         * those levels of detail are not used, the ingredients may be specified directly here as codes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param ingredient
         *     The ingredients of this medicinal product - when not detailed in other resources. This is only needed if the 
         *     ingredients are not specified by incoming references from the Ingredient resource, or indirectly via incoming 
         *     AdministrableProductDefinition, PackagedProductDefinition or ManufacturedItemDefinition references. In cases where 
         *     those levels of detail are not used, the ingredients may be specified directly here as codes
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder ingredient(Collection<CodeableConcept> ingredient) {
            this.ingredient = new ArrayList<>(ingredient);
            return this;
        }

        /**
         * Any component of the drug product which is not the chemical entity defined as the drug substance or an excipient in 
         * the drug product. This includes process-related impurities and contaminants, product-related impurities including 
         * degradation products.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param impurity
         *     Any component of the drug product which is not the chemical entity defined as the drug substance or an excipient in 
         *     the drug product. This includes process-related impurities and contaminants, product-related impurities including 
         *     degradation products
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder impurity(CodeableReference... impurity) {
            for (CodeableReference value : impurity) {
                this.impurity.add(value);
            }
            return this;
        }

        /**
         * Any component of the drug product which is not the chemical entity defined as the drug substance or an excipient in 
         * the drug product. This includes process-related impurities and contaminants, product-related impurities including 
         * degradation products.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param impurity
         *     Any component of the drug product which is not the chemical entity defined as the drug substance or an excipient in 
         *     the drug product. This includes process-related impurities and contaminants, product-related impurities including 
         *     degradation products
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder impurity(Collection<CodeableReference> impurity) {
            this.impurity = new ArrayList<>(impurity);
            return this;
        }

        /**
         * Additional information or supporting documentation about the medicinal product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param attachedDocument
         *     Additional information or supporting documentation about the medicinal product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder attachedDocument(Reference... attachedDocument) {
            for (Reference value : attachedDocument) {
                this.attachedDocument.add(value);
            }
            return this;
        }

        /**
         * Additional information or supporting documentation about the medicinal product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param attachedDocument
         *     Additional information or supporting documentation about the medicinal product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder attachedDocument(Collection<Reference> attachedDocument) {
            this.attachedDocument = new ArrayList<>(attachedDocument);
            return this;
        }

        /**
         * A master file for the medicinal product (e.g. Pharmacovigilance System Master File). Drug master files (DMFs) are 
         * documents submitted to regulatory agencies to provide confidential detailed information about facilities, processes or 
         * articles used in the manufacturing, processing, packaging and storing of drug products.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param masterFile
         *     A master file for the medicinal product (e.g. Pharmacovigilance System Master File)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder masterFile(Reference... masterFile) {
            for (Reference value : masterFile) {
                this.masterFile.add(value);
            }
            return this;
        }

        /**
         * A master file for the medicinal product (e.g. Pharmacovigilance System Master File). Drug master files (DMFs) are 
         * documents submitted to regulatory agencies to provide confidential detailed information about facilities, processes or 
         * articles used in the manufacturing, processing, packaging and storing of drug products.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param masterFile
         *     A master file for the medicinal product (e.g. Pharmacovigilance System Master File)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder masterFile(Collection<Reference> masterFile) {
            this.masterFile = new ArrayList<>(masterFile);
            return this;
        }

        /**
         * A product specific contact, person (in a role), or an organization.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     A product specific contact, person (in a role), or an organization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Contact... contact) {
            for (Contact value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * A product specific contact, person (in a role), or an organization.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     A product specific contact, person (in a role), or an organization
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<Contact> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * Clinical trials or studies that this product is involved in.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ResearchStudy}</li>
         * </ul>
         * 
         * @param clinicalTrial
         *     Clinical trials or studies that this product is involved in
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder clinicalTrial(Reference... clinicalTrial) {
            for (Reference value : clinicalTrial) {
                this.clinicalTrial.add(value);
            }
            return this;
        }

        /**
         * Clinical trials or studies that this product is involved in.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ResearchStudy}</li>
         * </ul>
         * 
         * @param clinicalTrial
         *     Clinical trials or studies that this product is involved in
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder clinicalTrial(Collection<Reference> clinicalTrial) {
            this.clinicalTrial = new ArrayList<>(clinicalTrial);
            return this;
        }

        /**
         * A code that this product is known by, usually within some formal terminology. Products (types of medications) tend to 
         * be known by identifiers during development and within regulatory process. However when they are prescribed they tend 
         * to be identified by codes. The same product may be have multiple codes, applied to it by multiple organizations.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param code
         *     A code that this product is known by, within some formal terminology
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Coding... code) {
            for (Coding value : code) {
                this.code.add(value);
            }
            return this;
        }

        /**
         * A code that this product is known by, usually within some formal terminology. Products (types of medications) tend to 
         * be known by identifiers during development and within regulatory process. However when they are prescribed they tend 
         * to be identified by codes. The same product may be have multiple codes, applied to it by multiple organizations.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param code
         *     A code that this product is known by, within some formal terminology
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder code(Collection<Coding> code) {
            this.code = new ArrayList<>(code);
            return this;
        }

        /**
         * The product's name, including full name and possibly coded parts.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     The product's name, including full name and possibly coded parts
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(Name... name) {
            for (Name value : name) {
                this.name.add(value);
            }
            return this;
        }

        /**
         * The product's name, including full name and possibly coded parts.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     The product's name, including full name and possibly coded parts
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder name(Collection<Name> name) {
            this.name = new ArrayList<>(name);
            return this;
        }

        /**
         * Reference to another product, e.g. for linking authorised to investigational product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param crossReference
         *     Reference to another product, e.g. for linking authorised to investigational product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder crossReference(CrossReference... crossReference) {
            for (CrossReference value : crossReference) {
                this.crossReference.add(value);
            }
            return this;
        }

        /**
         * Reference to another product, e.g. for linking authorised to investigational product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param crossReference
         *     Reference to another product, e.g. for linking authorised to investigational product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder crossReference(Collection<CrossReference> crossReference) {
            this.crossReference = new ArrayList<>(crossReference);
            return this;
        }

        /**
         * A manufacturing or administrative process or step associated with (or performed on) the medicinal product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param operation
         *     A manufacturing or administrative process or step associated with (or performed on) the medicinal product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder operation(Operation... operation) {
            for (Operation value : operation) {
                this.operation.add(value);
            }
            return this;
        }

        /**
         * A manufacturing or administrative process or step associated with (or performed on) the medicinal product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param operation
         *     A manufacturing or administrative process or step associated with (or performed on) the medicinal product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder operation(Collection<Operation> operation) {
            this.operation = new ArrayList<>(operation);
            return this;
        }

        /**
         * Allows the key product features to be recorded, such as "sugar free", "modified release", "parallel import".
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristic
         *     Allows the key product features to be recorded, such as "sugar free", "modified release", "parallel import"
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder characteristic(Characteristic... characteristic) {
            for (Characteristic value : characteristic) {
                this.characteristic.add(value);
            }
            return this;
        }

        /**
         * Allows the key product features to be recorded, such as "sugar free", "modified release", "parallel import".
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristic
         *     Allows the key product features to be recorded, such as "sugar free", "modified release", "parallel import"
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder characteristic(Collection<Characteristic> characteristic) {
            this.characteristic = new ArrayList<>(characteristic);
            return this;
        }

        /**
         * Build the {@link MedicinalProductDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>name</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MedicinalProductDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicinalProductDefinition per the base specification
         */
        @Override
        public MedicinalProductDefinition build() {
            MedicinalProductDefinition medicinalProductDefinition = new MedicinalProductDefinition(this);
            if (validating) {
                validate(medicinalProductDefinition);
            }
            return medicinalProductDefinition;
        }

        protected void validate(MedicinalProductDefinition medicinalProductDefinition) {
            super.validate(medicinalProductDefinition);
            ValidationSupport.checkList(medicinalProductDefinition.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(medicinalProductDefinition.route, "route", CodeableConcept.class);
            ValidationSupport.checkList(medicinalProductDefinition.specialMeasures, "specialMeasures", CodeableConcept.class);
            ValidationSupport.checkList(medicinalProductDefinition.classification, "classification", CodeableConcept.class);
            ValidationSupport.checkList(medicinalProductDefinition.marketingStatus, "marketingStatus", MarketingStatus.class);
            ValidationSupport.checkList(medicinalProductDefinition.packagedMedicinalProduct, "packagedMedicinalProduct", CodeableConcept.class);
            ValidationSupport.checkList(medicinalProductDefinition.ingredient, "ingredient", CodeableConcept.class);
            ValidationSupport.checkList(medicinalProductDefinition.impurity, "impurity", CodeableReference.class);
            ValidationSupport.checkList(medicinalProductDefinition.attachedDocument, "attachedDocument", Reference.class);
            ValidationSupport.checkList(medicinalProductDefinition.masterFile, "masterFile", Reference.class);
            ValidationSupport.checkList(medicinalProductDefinition.contact, "contact", Contact.class);
            ValidationSupport.checkList(medicinalProductDefinition.clinicalTrial, "clinicalTrial", Reference.class);
            ValidationSupport.checkList(medicinalProductDefinition.code, "code", Coding.class);
            ValidationSupport.checkNonEmptyList(medicinalProductDefinition.name, "name", Name.class);
            ValidationSupport.checkList(medicinalProductDefinition.crossReference, "crossReference", CrossReference.class);
            ValidationSupport.checkList(medicinalProductDefinition.operation, "operation", Operation.class);
            ValidationSupport.checkList(medicinalProductDefinition.characteristic, "characteristic", Characteristic.class);
            ValidationSupport.checkReferenceType(medicinalProductDefinition.attachedDocument, "attachedDocument", "DocumentReference");
            ValidationSupport.checkReferenceType(medicinalProductDefinition.masterFile, "masterFile", "DocumentReference");
            ValidationSupport.checkReferenceType(medicinalProductDefinition.clinicalTrial, "clinicalTrial", "ResearchStudy");
        }

        protected Builder from(MedicinalProductDefinition medicinalProductDefinition) {
            super.from(medicinalProductDefinition);
            identifier.addAll(medicinalProductDefinition.identifier);
            type = medicinalProductDefinition.type;
            domain = medicinalProductDefinition.domain;
            version = medicinalProductDefinition.version;
            status = medicinalProductDefinition.status;
            statusDate = medicinalProductDefinition.statusDate;
            description = medicinalProductDefinition.description;
            combinedPharmaceuticalDoseForm = medicinalProductDefinition.combinedPharmaceuticalDoseForm;
            route.addAll(medicinalProductDefinition.route);
            indication = medicinalProductDefinition.indication;
            legalStatusOfSupply = medicinalProductDefinition.legalStatusOfSupply;
            additionalMonitoringIndicator = medicinalProductDefinition.additionalMonitoringIndicator;
            specialMeasures.addAll(medicinalProductDefinition.specialMeasures);
            pediatricUseIndicator = medicinalProductDefinition.pediatricUseIndicator;
            classification.addAll(medicinalProductDefinition.classification);
            marketingStatus.addAll(medicinalProductDefinition.marketingStatus);
            packagedMedicinalProduct.addAll(medicinalProductDefinition.packagedMedicinalProduct);
            ingredient.addAll(medicinalProductDefinition.ingredient);
            impurity.addAll(medicinalProductDefinition.impurity);
            attachedDocument.addAll(medicinalProductDefinition.attachedDocument);
            masterFile.addAll(medicinalProductDefinition.masterFile);
            contact.addAll(medicinalProductDefinition.contact);
            clinicalTrial.addAll(medicinalProductDefinition.clinicalTrial);
            code.addAll(medicinalProductDefinition.code);
            name.addAll(medicinalProductDefinition.name);
            crossReference.addAll(medicinalProductDefinition.crossReference);
            operation.addAll(medicinalProductDefinition.operation);
            characteristic.addAll(medicinalProductDefinition.characteristic);
            return this;
        }
    }

    /**
     * A product specific contact, person (in a role), or an organization.
     */
    public static class Contact extends BackboneElement {
        @Summary
        private final CodeableConcept type;
        @Summary
        @ReferenceTarget({ "Organization", "PractitionerRole" })
        @Required
        private final Reference contact;

        private Contact(Builder builder) {
            super(builder);
            type = builder.type;
            contact = builder.contact;
        }

        /**
         * Allows the contact to be classified, for example QPPV, Pharmacovigilance Enquiry Information.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * A product specific contact, person (in a role), or an organization.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getContact() {
            return contact;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (contact != null);
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
                    accept(contact, "contact", visitor);
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
            Contact other = (Contact) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(contact, other.contact);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    contact);
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
            private Reference contact;

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
             * Allows the contact to be classified, for example QPPV, Pharmacovigilance Enquiry Information.
             * 
             * @param type
             *     Allows the contact to be classified, for example QPPV, Pharmacovigilance Enquiry Information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * A product specific contact, person (in a role), or an organization.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * <li>{@link PractitionerRole}</li>
             * </ul>
             * 
             * @param contact
             *     A product specific contact, person (in a role), or an organization
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder contact(Reference contact) {
                this.contact = contact;
                return this;
            }

            /**
             * Build the {@link Contact}
             * 
             * <p>Required elements:
             * <ul>
             * <li>contact</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Contact}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Contact per the base specification
             */
            @Override
            public Contact build() {
                Contact contact = new Contact(this);
                if (validating) {
                    validate(contact);
                }
                return contact;
            }

            protected void validate(Contact contact) {
                super.validate(contact);
                ValidationSupport.requireNonNull(contact.contact, "contact");
                ValidationSupport.checkReferenceType(contact.contact, "contact", "Organization", "PractitionerRole");
                ValidationSupport.requireValueOrChildren(contact);
            }

            protected Builder from(Contact contact) {
                super.from(contact);
                type = contact.type;
                this.contact = contact.contact;
                return this;
            }
        }
    }

    /**
     * The product's name, including full name and possibly coded parts.
     */
    public static class Name extends BackboneElement {
        @Summary
        @Required
        private final String productName;
        @Summary
        private final CodeableConcept type;
        @Summary
        private final List<NamePart> namePart;
        @Summary
        private final List<CountryLanguage> countryLanguage;

        private Name(Builder builder) {
            super(builder);
            productName = builder.productName;
            type = builder.type;
            namePart = Collections.unmodifiableList(builder.namePart);
            countryLanguage = Collections.unmodifiableList(builder.countryLanguage);
        }

        /**
         * The full product name.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getProductName() {
            return productName;
        }

        /**
         * Type of product name, such as rINN, BAN, Proprietary, Non-Proprietary.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Coding words or phrases of the name.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link NamePart} that may be empty.
         */
        public List<NamePart> getNamePart() {
            return namePart;
        }

        /**
         * Country where the name applies.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CountryLanguage} that may be empty.
         */
        public List<CountryLanguage> getCountryLanguage() {
            return countryLanguage;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (productName != null) || 
                (type != null) || 
                !namePart.isEmpty() || 
                !countryLanguage.isEmpty();
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
                    accept(productName, "productName", visitor);
                    accept(type, "type", visitor);
                    accept(namePart, "namePart", visitor, NamePart.class);
                    accept(countryLanguage, "countryLanguage", visitor, CountryLanguage.class);
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
            Name other = (Name) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(productName, other.productName) && 
                Objects.equals(type, other.type) && 
                Objects.equals(namePart, other.namePart) && 
                Objects.equals(countryLanguage, other.countryLanguage);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    productName, 
                    type, 
                    namePart, 
                    countryLanguage);
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
            private String productName;
            private CodeableConcept type;
            private List<NamePart> namePart = new ArrayList<>();
            private List<CountryLanguage> countryLanguage = new ArrayList<>();

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
             * Convenience method for setting {@code productName}.
             * 
             * <p>This element is required.
             * 
             * @param productName
             *     The full product name
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #productName(com.ibm.fhir.model.type.String)
             */
            public Builder productName(java.lang.String productName) {
                this.productName = (productName == null) ? null : String.of(productName);
                return this;
            }

            /**
             * The full product name.
             * 
             * <p>This element is required.
             * 
             * @param productName
             *     The full product name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder productName(String productName) {
                this.productName = productName;
                return this;
            }

            /**
             * Type of product name, such as rINN, BAN, Proprietary, Non-Proprietary.
             * 
             * @param type
             *     Type of product name, such as rINN, BAN, Proprietary, Non-Proprietary
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Coding words or phrases of the name.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param namePart
             *     Coding words or phrases of the name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder namePart(NamePart... namePart) {
                for (NamePart value : namePart) {
                    this.namePart.add(value);
                }
                return this;
            }

            /**
             * Coding words or phrases of the name.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param namePart
             *     Coding words or phrases of the name
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder namePart(Collection<NamePart> namePart) {
                this.namePart = new ArrayList<>(namePart);
                return this;
            }

            /**
             * Country where the name applies.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param countryLanguage
             *     Country where the name applies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder countryLanguage(CountryLanguage... countryLanguage) {
                for (CountryLanguage value : countryLanguage) {
                    this.countryLanguage.add(value);
                }
                return this;
            }

            /**
             * Country where the name applies.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param countryLanguage
             *     Country where the name applies
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder countryLanguage(Collection<CountryLanguage> countryLanguage) {
                this.countryLanguage = new ArrayList<>(countryLanguage);
                return this;
            }

            /**
             * Build the {@link Name}
             * 
             * <p>Required elements:
             * <ul>
             * <li>productName</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Name}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Name per the base specification
             */
            @Override
            public Name build() {
                Name name = new Name(this);
                if (validating) {
                    validate(name);
                }
                return name;
            }

            protected void validate(Name name) {
                super.validate(name);
                ValidationSupport.requireNonNull(name.productName, "productName");
                ValidationSupport.checkList(name.namePart, "namePart", NamePart.class);
                ValidationSupport.checkList(name.countryLanguage, "countryLanguage", CountryLanguage.class);
                ValidationSupport.requireValueOrChildren(name);
            }

            protected Builder from(Name name) {
                super.from(name);
                productName = name.productName;
                type = name.type;
                namePart.addAll(name.namePart);
                countryLanguage.addAll(name.countryLanguage);
                return this;
            }
        }

        /**
         * Coding words or phrases of the name.
         */
        public static class NamePart extends BackboneElement {
            @Summary
            @Required
            private final String part;
            @Summary
            @Required
            private final CodeableConcept type;

            private NamePart(Builder builder) {
                super(builder);
                part = builder.part;
                type = builder.type;
            }

            /**
             * A fragment of a product name.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getPart() {
                return part;
            }

            /**
             * Identifying type for this part of the name (e.g. strength part).
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getType() {
                return type;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (part != null) || 
                    (type != null);
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
                        accept(part, "part", visitor);
                        accept(type, "type", visitor);
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
                NamePart other = (NamePart) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(part, other.part) && 
                    Objects.equals(type, other.type);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        part, 
                        type);
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
                private String part;
                private CodeableConcept type;

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
                 * Convenience method for setting {@code part}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param part
                 *     A fragment of a product name
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #part(com.ibm.fhir.model.type.String)
                 */
                public Builder part(java.lang.String part) {
                    this.part = (part == null) ? null : String.of(part);
                    return this;
                }

                /**
                 * A fragment of a product name.
                 * 
                 * <p>This element is required.
                 * 
                 * @param part
                 *     A fragment of a product name
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder part(String part) {
                    this.part = part;
                    return this;
                }

                /**
                 * Identifying type for this part of the name (e.g. strength part).
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     Identifying type for this part of the name (e.g. strength part)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Build the {@link NamePart}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>part</li>
                 * <li>type</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link NamePart}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid NamePart per the base specification
                 */
                @Override
                public NamePart build() {
                    NamePart namePart = new NamePart(this);
                    if (validating) {
                        validate(namePart);
                    }
                    return namePart;
                }

                protected void validate(NamePart namePart) {
                    super.validate(namePart);
                    ValidationSupport.requireNonNull(namePart.part, "part");
                    ValidationSupport.requireNonNull(namePart.type, "type");
                    ValidationSupport.requireValueOrChildren(namePart);
                }

                protected Builder from(NamePart namePart) {
                    super.from(namePart);
                    part = namePart.part;
                    type = namePart.type;
                    return this;
                }
            }
        }

        /**
         * Country where the name applies.
         */
        public static class CountryLanguage extends BackboneElement {
            @Summary
            @Required
            private final CodeableConcept country;
            @Summary
            private final CodeableConcept jurisdiction;
            @Summary
            @Required
            private final CodeableConcept language;

            private CountryLanguage(Builder builder) {
                super(builder);
                country = builder.country;
                jurisdiction = builder.jurisdiction;
                language = builder.language;
            }

            /**
             * Country code for where this name applies.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getCountry() {
                return country;
            }

            /**
             * Jurisdiction code for where this name applies.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getJurisdiction() {
                return jurisdiction;
            }

            /**
             * Language code for this name.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getLanguage() {
                return language;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (country != null) || 
                    (jurisdiction != null) || 
                    (language != null);
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
                        accept(country, "country", visitor);
                        accept(jurisdiction, "jurisdiction", visitor);
                        accept(language, "language", visitor);
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
                CountryLanguage other = (CountryLanguage) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(country, other.country) && 
                    Objects.equals(jurisdiction, other.jurisdiction) && 
                    Objects.equals(language, other.language);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        country, 
                        jurisdiction, 
                        language);
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
                private CodeableConcept country;
                private CodeableConcept jurisdiction;
                private CodeableConcept language;

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
                 * Country code for where this name applies.
                 * 
                 * <p>This element is required.
                 * 
                 * @param country
                 *     Country code for where this name applies
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder country(CodeableConcept country) {
                    this.country = country;
                    return this;
                }

                /**
                 * Jurisdiction code for where this name applies.
                 * 
                 * @param jurisdiction
                 *     Jurisdiction code for where this name applies
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder jurisdiction(CodeableConcept jurisdiction) {
                    this.jurisdiction = jurisdiction;
                    return this;
                }

                /**
                 * Language code for this name.
                 * 
                 * <p>This element is required.
                 * 
                 * @param language
                 *     Language code for this name
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder language(CodeableConcept language) {
                    this.language = language;
                    return this;
                }

                /**
                 * Build the {@link CountryLanguage}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>country</li>
                 * <li>language</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link CountryLanguage}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid CountryLanguage per the base specification
                 */
                @Override
                public CountryLanguage build() {
                    CountryLanguage countryLanguage = new CountryLanguage(this);
                    if (validating) {
                        validate(countryLanguage);
                    }
                    return countryLanguage;
                }

                protected void validate(CountryLanguage countryLanguage) {
                    super.validate(countryLanguage);
                    ValidationSupport.requireNonNull(countryLanguage.country, "country");
                    ValidationSupport.requireNonNull(countryLanguage.language, "language");
                    ValidationSupport.requireValueOrChildren(countryLanguage);
                }

                protected Builder from(CountryLanguage countryLanguage) {
                    super.from(countryLanguage);
                    country = countryLanguage.country;
                    jurisdiction = countryLanguage.jurisdiction;
                    language = countryLanguage.language;
                    return this;
                }
            }
        }
    }

    /**
     * Reference to another product, e.g. for linking authorised to investigational product.
     */
    public static class CrossReference extends BackboneElement {
        @Summary
        @Required
        private final CodeableReference product;
        @Summary
        private final CodeableConcept type;

        private CrossReference(Builder builder) {
            super(builder);
            product = builder.product;
            type = builder.type;
        }

        /**
         * Reference to another product, e.g. for linking authorised to investigational product.
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that is non-null.
         */
        public CodeableReference getProduct() {
            return product;
        }

        /**
         * The type of relationship, for instance branded to generic, product to development product (investigational), parallel 
         * import version.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (product != null) || 
                (type != null);
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
                    accept(product, "product", visitor);
                    accept(type, "type", visitor);
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
            CrossReference other = (CrossReference) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(product, other.product) && 
                Objects.equals(type, other.type);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    product, 
                    type);
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
            private CodeableReference product;
            private CodeableConcept type;

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
             * Reference to another product, e.g. for linking authorised to investigational product.
             * 
             * <p>This element is required.
             * 
             * @param product
             *     Reference to another product, e.g. for linking authorised to investigational product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder product(CodeableReference product) {
                this.product = product;
                return this;
            }

            /**
             * The type of relationship, for instance branded to generic, product to development product (investigational), parallel 
             * import version.
             * 
             * @param type
             *     The type of relationship, for instance branded to generic, product to development product (investigational), parallel 
             *     import version
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Build the {@link CrossReference}
             * 
             * <p>Required elements:
             * <ul>
             * <li>product</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link CrossReference}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid CrossReference per the base specification
             */
            @Override
            public CrossReference build() {
                CrossReference crossReference = new CrossReference(this);
                if (validating) {
                    validate(crossReference);
                }
                return crossReference;
            }

            protected void validate(CrossReference crossReference) {
                super.validate(crossReference);
                ValidationSupport.requireNonNull(crossReference.product, "product");
                ValidationSupport.requireValueOrChildren(crossReference);
            }

            protected Builder from(CrossReference crossReference) {
                super.from(crossReference);
                product = crossReference.product;
                type = crossReference.type;
                return this;
            }
        }
    }

    /**
     * A manufacturing or administrative process or step associated with (or performed on) the medicinal product.
     */
    public static class Operation extends BackboneElement {
        @Summary
        private final CodeableReference type;
        @Summary
        private final Period effectiveDate;
        @Summary
        @ReferenceTarget({ "Organization" })
        private final List<Reference> organization;
        @Summary
        private final CodeableConcept confidentialityIndicator;

        private Operation(Builder builder) {
            super(builder);
            type = builder.type;
            effectiveDate = builder.effectiveDate;
            organization = Collections.unmodifiableList(builder.organization);
            confidentialityIndicator = builder.confidentialityIndicator;
        }

        /**
         * The type of manufacturing operation e.g. manufacturing itself, re-packaging. For the authorization of this, a 
         * RegulatedAuthorization would point to the same plan or activity referenced here.
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that may be null.
         */
        public CodeableReference getType() {
            return type;
        }

        /**
         * Date range of applicability.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getEffectiveDate() {
            return effectiveDate;
        }

        /**
         * The organization or establishment responsible for (or associated with) the particular process or step, examples 
         * include the manufacturer, importer, agent.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getOrganization() {
            return organization;
        }

        /**
         * Specifies whether this particular business or manufacturing process is considered proprietary or confidential.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getConfidentialityIndicator() {
            return confidentialityIndicator;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (effectiveDate != null) || 
                !organization.isEmpty() || 
                (confidentialityIndicator != null);
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
                    accept(effectiveDate, "effectiveDate", visitor);
                    accept(organization, "organization", visitor, Reference.class);
                    accept(confidentialityIndicator, "confidentialityIndicator", visitor);
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
            Operation other = (Operation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(effectiveDate, other.effectiveDate) && 
                Objects.equals(organization, other.organization) && 
                Objects.equals(confidentialityIndicator, other.confidentialityIndicator);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    effectiveDate, 
                    organization, 
                    confidentialityIndicator);
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
            private CodeableReference type;
            private Period effectiveDate;
            private List<Reference> organization = new ArrayList<>();
            private CodeableConcept confidentialityIndicator;

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
             * The type of manufacturing operation e.g. manufacturing itself, re-packaging. For the authorization of this, a 
             * RegulatedAuthorization would point to the same plan or activity referenced here.
             * 
             * @param type
             *     The type of manufacturing operation e.g. manufacturing itself, re-packaging. For the authorization of this, a 
             *     RegulatedAuthorization would point to the same plan or activity referenced here
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableReference type) {
                this.type = type;
                return this;
            }

            /**
             * Date range of applicability.
             * 
             * @param effectiveDate
             *     Date range of applicability
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder effectiveDate(Period effectiveDate) {
                this.effectiveDate = effectiveDate;
                return this;
            }

            /**
             * The organization or establishment responsible for (or associated with) the particular process or step, examples 
             * include the manufacturer, importer, agent.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param organization
             *     The organization or establishment responsible for (or associated with) the particular process or step, examples 
             *     include the manufacturer, importer, agent
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder organization(Reference... organization) {
                for (Reference value : organization) {
                    this.organization.add(value);
                }
                return this;
            }

            /**
             * The organization or establishment responsible for (or associated with) the particular process or step, examples 
             * include the manufacturer, importer, agent.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param organization
             *     The organization or establishment responsible for (or associated with) the particular process or step, examples 
             *     include the manufacturer, importer, agent
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder organization(Collection<Reference> organization) {
                this.organization = new ArrayList<>(organization);
                return this;
            }

            /**
             * Specifies whether this particular business or manufacturing process is considered proprietary or confidential.
             * 
             * @param confidentialityIndicator
             *     Specifies whether this particular business or manufacturing process is considered proprietary or confidential
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder confidentialityIndicator(CodeableConcept confidentialityIndicator) {
                this.confidentialityIndicator = confidentialityIndicator;
                return this;
            }

            /**
             * Build the {@link Operation}
             * 
             * @return
             *     An immutable object of type {@link Operation}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Operation per the base specification
             */
            @Override
            public Operation build() {
                Operation operation = new Operation(this);
                if (validating) {
                    validate(operation);
                }
                return operation;
            }

            protected void validate(Operation operation) {
                super.validate(operation);
                ValidationSupport.checkList(operation.organization, "organization", Reference.class);
                ValidationSupport.checkReferenceType(operation.organization, "organization", "Organization");
                ValidationSupport.requireValueOrChildren(operation);
            }

            protected Builder from(Operation operation) {
                super.from(operation);
                type = operation.type;
                effectiveDate = operation.effectiveDate;
                organization.addAll(operation.organization);
                confidentialityIndicator = operation.confidentialityIndicator;
                return this;
            }
        }
    }

    /**
     * Allows the key product features to be recorded, such as "sugar free", "modified release", "parallel import".
     */
    public static class Characteristic extends BackboneElement {
        @Summary
        @Required
        private final CodeableConcept type;
        @Summary
        @Choice({ CodeableConcept.class, Quantity.class, Date.class, Boolean.class, Attachment.class })
        private final Element value;

        private Characteristic(Builder builder) {
            super(builder);
            type = builder.type;
            value = builder.value;
        }

        /**
         * A code expressing the type of characteristic.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * A value for the characteristic.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}, {@link Quantity}, {@link Date}, {@link Boolean} or {@link 
         *     Attachment} that may be null.
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
            Characteristic other = (Characteristic) obj;
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
             * A code expressing the type of characteristic.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     A code expressing the type of characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Date.
             * 
             * @param value
             *     A value for the characteristic
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.LocalDate value) {
                this.value = (value == null) ? null : Date.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Boolean.
             * 
             * @param value
             *     A value for the characteristic
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
             * A value for the characteristic.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Quantity}</li>
             * <li>{@link Date}</li>
             * <li>{@link Boolean}</li>
             * <li>{@link Attachment}</li>
             * </ul>
             * 
             * @param value
             *     A value for the characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Build the {@link Characteristic}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Characteristic}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Characteristic per the base specification
             */
            @Override
            public Characteristic build() {
                Characteristic characteristic = new Characteristic(this);
                if (validating) {
                    validate(characteristic);
                }
                return characteristic;
            }

            protected void validate(Characteristic characteristic) {
                super.validate(characteristic);
                ValidationSupport.requireNonNull(characteristic.type, "type");
                ValidationSupport.choiceElement(characteristic.value, "value", CodeableConcept.class, Quantity.class, Date.class, Boolean.class, Attachment.class);
                ValidationSupport.requireValueOrChildren(characteristic);
            }

            protected Builder from(Characteristic characteristic) {
                super.from(characteristic);
                type = characteristic.type;
                value = characteristic.value;
                return this;
            }
        }
    }
}
