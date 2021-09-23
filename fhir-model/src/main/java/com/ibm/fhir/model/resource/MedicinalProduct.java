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

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.MarketingStatus;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicinalProduct extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final CodeableConcept type;
    @Summary
    private final Coding domain;
    @Summary
    private final CodeableConcept combinedPharmaceuticalDoseForm;
    @Summary
    private final CodeableConcept legalStatusOfSupply;
    @Summary
    private final CodeableConcept additionalMonitoringIndicator;
    @Summary
    private final List<String> specialMeasures;
    @Summary
    private final CodeableConcept paediatricUseIndicator;
    @Summary
    private final List<CodeableConcept> productClassification;
    @Summary
    private final List<MarketingStatus> marketingStatus;
    @Summary
    @ReferenceTarget({ "MedicinalProductPharmaceutical" })
    private final List<Reference> pharmaceuticalProduct;
    @Summary
    @ReferenceTarget({ "MedicinalProductPackaged" })
    private final List<Reference> packagedMedicinalProduct;
    @Summary
    @ReferenceTarget({ "DocumentReference" })
    private final List<Reference> attachedDocument;
    @Summary
    @ReferenceTarget({ "DocumentReference" })
    private final List<Reference> masterFile;
    @Summary
    @ReferenceTarget({ "Organization", "PractitionerRole" })
    private final List<Reference> contact;
    @Summary
    @ReferenceTarget({ "ResearchStudy" })
    private final List<Reference> clinicalTrial;
    @Summary
    @Required
    private final List<Name> name;
    @Summary
    private final List<Identifier> crossReference;
    @Summary
    private final List<ManufacturingBusinessOperation> manufacturingBusinessOperation;
    @Summary
    private final List<SpecialDesignation> specialDesignation;

    private MedicinalProduct(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        type = builder.type;
        domain = builder.domain;
        combinedPharmaceuticalDoseForm = builder.combinedPharmaceuticalDoseForm;
        legalStatusOfSupply = builder.legalStatusOfSupply;
        additionalMonitoringIndicator = builder.additionalMonitoringIndicator;
        specialMeasures = Collections.unmodifiableList(builder.specialMeasures);
        paediatricUseIndicator = builder.paediatricUseIndicator;
        productClassification = Collections.unmodifiableList(builder.productClassification);
        marketingStatus = Collections.unmodifiableList(builder.marketingStatus);
        pharmaceuticalProduct = Collections.unmodifiableList(builder.pharmaceuticalProduct);
        packagedMedicinalProduct = Collections.unmodifiableList(builder.packagedMedicinalProduct);
        attachedDocument = Collections.unmodifiableList(builder.attachedDocument);
        masterFile = Collections.unmodifiableList(builder.masterFile);
        contact = Collections.unmodifiableList(builder.contact);
        clinicalTrial = Collections.unmodifiableList(builder.clinicalTrial);
        name = Collections.unmodifiableList(builder.name);
        crossReference = Collections.unmodifiableList(builder.crossReference);
        manufacturingBusinessOperation = Collections.unmodifiableList(builder.manufacturingBusinessOperation);
        specialDesignation = Collections.unmodifiableList(builder.specialDesignation);
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
     *     An immutable object of type {@link Coding} that may be null.
     */
    public Coding getDomain() {
        return domain;
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
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getSpecialMeasures() {
        return specialMeasures;
    }

    /**
     * If authorised for use in children.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPaediatricUseIndicator() {
        return paediatricUseIndicator;
    }

    /**
     * Allows the product to be classified by various systems.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getProductClassification() {
        return productClassification;
    }

    /**
     * Marketing status of the medicinal product, in contrast to marketing authorizaton.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MarketingStatus} that may be empty.
     */
    public List<MarketingStatus> getMarketingStatus() {
        return marketingStatus;
    }

    /**
     * Pharmaceutical aspects of product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPharmaceuticalProduct() {
        return pharmaceuticalProduct;
    }

    /**
     * Package representation for the product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPackagedMedicinalProduct() {
        return packagedMedicinalProduct;
    }

    /**
     * Supporting documentation, typically for regulatory submission.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAttachedDocument() {
        return attachedDocument;
    }

    /**
     * A master file for to the medicinal product (e.g. Pharmacovigilance System Master File).
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
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getContact() {
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
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getCrossReference() {
        return crossReference;
    }

    /**
     * An operation applied to the product, for manufacturing or adminsitrative purpose.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ManufacturingBusinessOperation} that may be empty.
     */
    public List<ManufacturingBusinessOperation> getManufacturingBusinessOperation() {
        return manufacturingBusinessOperation;
    }

    /**
     * Indicates if the medicinal product has an orphan designation for the treatment of a rare disease.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link SpecialDesignation} that may be empty.
     */
    public List<SpecialDesignation> getSpecialDesignation() {
        return specialDesignation;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (type != null) || 
            (domain != null) || 
            (combinedPharmaceuticalDoseForm != null) || 
            (legalStatusOfSupply != null) || 
            (additionalMonitoringIndicator != null) || 
            !specialMeasures.isEmpty() || 
            (paediatricUseIndicator != null) || 
            !productClassification.isEmpty() || 
            !marketingStatus.isEmpty() || 
            !pharmaceuticalProduct.isEmpty() || 
            !packagedMedicinalProduct.isEmpty() || 
            !attachedDocument.isEmpty() || 
            !masterFile.isEmpty() || 
            !contact.isEmpty() || 
            !clinicalTrial.isEmpty() || 
            !name.isEmpty() || 
            !crossReference.isEmpty() || 
            !manufacturingBusinessOperation.isEmpty() || 
            !specialDesignation.isEmpty();
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
                accept(combinedPharmaceuticalDoseForm, "combinedPharmaceuticalDoseForm", visitor);
                accept(legalStatusOfSupply, "legalStatusOfSupply", visitor);
                accept(additionalMonitoringIndicator, "additionalMonitoringIndicator", visitor);
                accept(specialMeasures, "specialMeasures", visitor, String.class);
                accept(paediatricUseIndicator, "paediatricUseIndicator", visitor);
                accept(productClassification, "productClassification", visitor, CodeableConcept.class);
                accept(marketingStatus, "marketingStatus", visitor, MarketingStatus.class);
                accept(pharmaceuticalProduct, "pharmaceuticalProduct", visitor, Reference.class);
                accept(packagedMedicinalProduct, "packagedMedicinalProduct", visitor, Reference.class);
                accept(attachedDocument, "attachedDocument", visitor, Reference.class);
                accept(masterFile, "masterFile", visitor, Reference.class);
                accept(contact, "contact", visitor, Reference.class);
                accept(clinicalTrial, "clinicalTrial", visitor, Reference.class);
                accept(name, "name", visitor, Name.class);
                accept(crossReference, "crossReference", visitor, Identifier.class);
                accept(manufacturingBusinessOperation, "manufacturingBusinessOperation", visitor, ManufacturingBusinessOperation.class);
                accept(specialDesignation, "specialDesignation", visitor, SpecialDesignation.class);
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
        MedicinalProduct other = (MedicinalProduct) obj;
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
            Objects.equals(combinedPharmaceuticalDoseForm, other.combinedPharmaceuticalDoseForm) && 
            Objects.equals(legalStatusOfSupply, other.legalStatusOfSupply) && 
            Objects.equals(additionalMonitoringIndicator, other.additionalMonitoringIndicator) && 
            Objects.equals(specialMeasures, other.specialMeasures) && 
            Objects.equals(paediatricUseIndicator, other.paediatricUseIndicator) && 
            Objects.equals(productClassification, other.productClassification) && 
            Objects.equals(marketingStatus, other.marketingStatus) && 
            Objects.equals(pharmaceuticalProduct, other.pharmaceuticalProduct) && 
            Objects.equals(packagedMedicinalProduct, other.packagedMedicinalProduct) && 
            Objects.equals(attachedDocument, other.attachedDocument) && 
            Objects.equals(masterFile, other.masterFile) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(clinicalTrial, other.clinicalTrial) && 
            Objects.equals(name, other.name) && 
            Objects.equals(crossReference, other.crossReference) && 
            Objects.equals(manufacturingBusinessOperation, other.manufacturingBusinessOperation) && 
            Objects.equals(specialDesignation, other.specialDesignation);
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
                combinedPharmaceuticalDoseForm, 
                legalStatusOfSupply, 
                additionalMonitoringIndicator, 
                specialMeasures, 
                paediatricUseIndicator, 
                productClassification, 
                marketingStatus, 
                pharmaceuticalProduct, 
                packagedMedicinalProduct, 
                attachedDocument, 
                masterFile, 
                contact, 
                clinicalTrial, 
                name, 
                crossReference, 
                manufacturingBusinessOperation, 
                specialDesignation);
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
        private Coding domain;
        private CodeableConcept combinedPharmaceuticalDoseForm;
        private CodeableConcept legalStatusOfSupply;
        private CodeableConcept additionalMonitoringIndicator;
        private List<String> specialMeasures = new ArrayList<>();
        private CodeableConcept paediatricUseIndicator;
        private List<CodeableConcept> productClassification = new ArrayList<>();
        private List<MarketingStatus> marketingStatus = new ArrayList<>();
        private List<Reference> pharmaceuticalProduct = new ArrayList<>();
        private List<Reference> packagedMedicinalProduct = new ArrayList<>();
        private List<Reference> attachedDocument = new ArrayList<>();
        private List<Reference> masterFile = new ArrayList<>();
        private List<Reference> contact = new ArrayList<>();
        private List<Reference> clinicalTrial = new ArrayList<>();
        private List<Name> name = new ArrayList<>();
        private List<Identifier> crossReference = new ArrayList<>();
        private List<ManufacturingBusinessOperation> manufacturingBusinessOperation = new ArrayList<>();
        private List<SpecialDesignation> specialDesignation = new ArrayList<>();

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
        public Builder domain(Coding domain) {
            this.domain = domain;
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
         * Convenience method for setting {@code specialMeasures}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialMeasures
         *     Whether the Medicinal Product is subject to special measures for regulatory reasons
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #specialMeasures(com.ibm.fhir.model.type.String)
         */
        public Builder specialMeasures(java.lang.String... specialMeasures) {
            for (java.lang.String value : specialMeasures) {
                this.specialMeasures.add((value == null) ? null : String.of(value));
            }
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
        public Builder specialMeasures(String... specialMeasures) {
            for (String value : specialMeasures) {
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
        public Builder specialMeasures(Collection<String> specialMeasures) {
            this.specialMeasures = new ArrayList<>(specialMeasures);
            return this;
        }

        /**
         * If authorised for use in children.
         * 
         * @param paediatricUseIndicator
         *     If authorised for use in children
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder paediatricUseIndicator(CodeableConcept paediatricUseIndicator) {
            this.paediatricUseIndicator = paediatricUseIndicator;
            return this;
        }

        /**
         * Allows the product to be classified by various systems.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param productClassification
         *     Allows the product to be classified by various systems
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder productClassification(CodeableConcept... productClassification) {
            for (CodeableConcept value : productClassification) {
                this.productClassification.add(value);
            }
            return this;
        }

        /**
         * Allows the product to be classified by various systems.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param productClassification
         *     Allows the product to be classified by various systems
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder productClassification(Collection<CodeableConcept> productClassification) {
            this.productClassification = new ArrayList<>(productClassification);
            return this;
        }

        /**
         * Marketing status of the medicinal product, in contrast to marketing authorizaton.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param marketingStatus
         *     Marketing status of the medicinal product, in contrast to marketing authorizaton
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
         * Marketing status of the medicinal product, in contrast to marketing authorizaton.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param marketingStatus
         *     Marketing status of the medicinal product, in contrast to marketing authorizaton
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
         * Pharmaceutical aspects of product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductPharmaceutical}</li>
         * </ul>
         * 
         * @param pharmaceuticalProduct
         *     Pharmaceutical aspects of product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder pharmaceuticalProduct(Reference... pharmaceuticalProduct) {
            for (Reference value : pharmaceuticalProduct) {
                this.pharmaceuticalProduct.add(value);
            }
            return this;
        }

        /**
         * Pharmaceutical aspects of product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductPharmaceutical}</li>
         * </ul>
         * 
         * @param pharmaceuticalProduct
         *     Pharmaceutical aspects of product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder pharmaceuticalProduct(Collection<Reference> pharmaceuticalProduct) {
            this.pharmaceuticalProduct = new ArrayList<>(pharmaceuticalProduct);
            return this;
        }

        /**
         * Package representation for the product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductPackaged}</li>
         * </ul>
         * 
         * @param packagedMedicinalProduct
         *     Package representation for the product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder packagedMedicinalProduct(Reference... packagedMedicinalProduct) {
            for (Reference value : packagedMedicinalProduct) {
                this.packagedMedicinalProduct.add(value);
            }
            return this;
        }

        /**
         * Package representation for the product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductPackaged}</li>
         * </ul>
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
        public Builder packagedMedicinalProduct(Collection<Reference> packagedMedicinalProduct) {
            this.packagedMedicinalProduct = new ArrayList<>(packagedMedicinalProduct);
            return this;
        }

        /**
         * Supporting documentation, typically for regulatory submission.
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
         *     Supporting documentation, typically for regulatory submission
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
         * Supporting documentation, typically for regulatory submission.
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
         *     Supporting documentation, typically for regulatory submission
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
         * A master file for to the medicinal product (e.g. Pharmacovigilance System Master File).
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
         *     A master file for to the medicinal product (e.g. Pharmacovigilance System Master File)
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
         * A master file for to the medicinal product (e.g. Pharmacovigilance System Master File).
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
         *     A master file for to the medicinal product (e.g. Pharmacovigilance System Master File)
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
         * <p>Allowed resource types for the references:
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
        public Builder contact(Reference... contact) {
            for (Reference value : contact) {
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
         * <p>Allowed resource types for the references:
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
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<Reference> contact) {
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
        public Builder crossReference(Identifier... crossReference) {
            for (Identifier value : crossReference) {
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
        public Builder crossReference(Collection<Identifier> crossReference) {
            this.crossReference = new ArrayList<>(crossReference);
            return this;
        }

        /**
         * An operation applied to the product, for manufacturing or adminsitrative purpose.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param manufacturingBusinessOperation
         *     An operation applied to the product, for manufacturing or adminsitrative purpose
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufacturingBusinessOperation(ManufacturingBusinessOperation... manufacturingBusinessOperation) {
            for (ManufacturingBusinessOperation value : manufacturingBusinessOperation) {
                this.manufacturingBusinessOperation.add(value);
            }
            return this;
        }

        /**
         * An operation applied to the product, for manufacturing or adminsitrative purpose.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param manufacturingBusinessOperation
         *     An operation applied to the product, for manufacturing or adminsitrative purpose
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder manufacturingBusinessOperation(Collection<ManufacturingBusinessOperation> manufacturingBusinessOperation) {
            this.manufacturingBusinessOperation = new ArrayList<>(manufacturingBusinessOperation);
            return this;
        }

        /**
         * Indicates if the medicinal product has an orphan designation for the treatment of a rare disease.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialDesignation
         *     Indicates if the medicinal product has an orphan designation for the treatment of a rare disease
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specialDesignation(SpecialDesignation... specialDesignation) {
            for (SpecialDesignation value : specialDesignation) {
                this.specialDesignation.add(value);
            }
            return this;
        }

        /**
         * Indicates if the medicinal product has an orphan designation for the treatment of a rare disease.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialDesignation
         *     Indicates if the medicinal product has an orphan designation for the treatment of a rare disease
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder specialDesignation(Collection<SpecialDesignation> specialDesignation) {
            this.specialDesignation = new ArrayList<>(specialDesignation);
            return this;
        }

        /**
         * Build the {@link MedicinalProduct}
         * 
         * <p>Required elements:
         * <ul>
         * <li>name</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MedicinalProduct}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicinalProduct per the base specification
         */
        @Override
        public MedicinalProduct build() {
            MedicinalProduct medicinalProduct = new MedicinalProduct(this);
            if (validating) {
                validate(medicinalProduct);
            }
            return medicinalProduct;
        }

        protected void validate(MedicinalProduct medicinalProduct) {
            super.validate(medicinalProduct);
            ValidationSupport.checkList(medicinalProduct.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(medicinalProduct.specialMeasures, "specialMeasures", String.class);
            ValidationSupport.checkList(medicinalProduct.productClassification, "productClassification", CodeableConcept.class);
            ValidationSupport.checkList(medicinalProduct.marketingStatus, "marketingStatus", MarketingStatus.class);
            ValidationSupport.checkList(medicinalProduct.pharmaceuticalProduct, "pharmaceuticalProduct", Reference.class);
            ValidationSupport.checkList(medicinalProduct.packagedMedicinalProduct, "packagedMedicinalProduct", Reference.class);
            ValidationSupport.checkList(medicinalProduct.attachedDocument, "attachedDocument", Reference.class);
            ValidationSupport.checkList(medicinalProduct.masterFile, "masterFile", Reference.class);
            ValidationSupport.checkList(medicinalProduct.contact, "contact", Reference.class);
            ValidationSupport.checkList(medicinalProduct.clinicalTrial, "clinicalTrial", Reference.class);
            ValidationSupport.checkNonEmptyList(medicinalProduct.name, "name", Name.class);
            ValidationSupport.checkList(medicinalProduct.crossReference, "crossReference", Identifier.class);
            ValidationSupport.checkList(medicinalProduct.manufacturingBusinessOperation, "manufacturingBusinessOperation", ManufacturingBusinessOperation.class);
            ValidationSupport.checkList(medicinalProduct.specialDesignation, "specialDesignation", SpecialDesignation.class);
            ValidationSupport.checkReferenceType(medicinalProduct.pharmaceuticalProduct, "pharmaceuticalProduct", "MedicinalProductPharmaceutical");
            ValidationSupport.checkReferenceType(medicinalProduct.packagedMedicinalProduct, "packagedMedicinalProduct", "MedicinalProductPackaged");
            ValidationSupport.checkReferenceType(medicinalProduct.attachedDocument, "attachedDocument", "DocumentReference");
            ValidationSupport.checkReferenceType(medicinalProduct.masterFile, "masterFile", "DocumentReference");
            ValidationSupport.checkReferenceType(medicinalProduct.contact, "contact", "Organization", "PractitionerRole");
            ValidationSupport.checkReferenceType(medicinalProduct.clinicalTrial, "clinicalTrial", "ResearchStudy");
        }

        protected Builder from(MedicinalProduct medicinalProduct) {
            super.from(medicinalProduct);
            identifier.addAll(medicinalProduct.identifier);
            type = medicinalProduct.type;
            domain = medicinalProduct.domain;
            combinedPharmaceuticalDoseForm = medicinalProduct.combinedPharmaceuticalDoseForm;
            legalStatusOfSupply = medicinalProduct.legalStatusOfSupply;
            additionalMonitoringIndicator = medicinalProduct.additionalMonitoringIndicator;
            specialMeasures.addAll(medicinalProduct.specialMeasures);
            paediatricUseIndicator = medicinalProduct.paediatricUseIndicator;
            productClassification.addAll(medicinalProduct.productClassification);
            marketingStatus.addAll(medicinalProduct.marketingStatus);
            pharmaceuticalProduct.addAll(medicinalProduct.pharmaceuticalProduct);
            packagedMedicinalProduct.addAll(medicinalProduct.packagedMedicinalProduct);
            attachedDocument.addAll(medicinalProduct.attachedDocument);
            masterFile.addAll(medicinalProduct.masterFile);
            contact.addAll(medicinalProduct.contact);
            clinicalTrial.addAll(medicinalProduct.clinicalTrial);
            name.addAll(medicinalProduct.name);
            crossReference.addAll(medicinalProduct.crossReference);
            manufacturingBusinessOperation.addAll(medicinalProduct.manufacturingBusinessOperation);
            specialDesignation.addAll(medicinalProduct.specialDesignation);
            return this;
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
        private final List<NamePart> namePart;
        @Summary
        private final List<CountryLanguage> countryLanguage;

        private Name(Builder builder) {
            super(builder);
            productName = builder.productName;
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
            private final Coding type;

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
             * Idenifying type for this part of the name (e.g. strength part).
             * 
             * @return
             *     An immutable object of type {@link Coding} that is non-null.
             */
            public Coding getType() {
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
                private Coding type;

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
                 * Idenifying type for this part of the name (e.g. strength part).
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     Idenifying type for this part of the name (e.g. strength part)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(Coding type) {
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
     * An operation applied to the product, for manufacturing or adminsitrative purpose.
     */
    public static class ManufacturingBusinessOperation extends BackboneElement {
        @Summary
        private final CodeableConcept operationType;
        @Summary
        private final Identifier authorisationReferenceNumber;
        @Summary
        private final DateTime effectiveDate;
        @Summary
        private final CodeableConcept confidentialityIndicator;
        @Summary
        @ReferenceTarget({ "Organization" })
        private final List<Reference> manufacturer;
        @Summary
        @ReferenceTarget({ "Organization" })
        private final Reference regulator;

        private ManufacturingBusinessOperation(Builder builder) {
            super(builder);
            operationType = builder.operationType;
            authorisationReferenceNumber = builder.authorisationReferenceNumber;
            effectiveDate = builder.effectiveDate;
            confidentialityIndicator = builder.confidentialityIndicator;
            manufacturer = Collections.unmodifiableList(builder.manufacturer);
            regulator = builder.regulator;
        }

        /**
         * The type of manufacturing operation.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOperationType() {
            return operationType;
        }

        /**
         * Regulatory authorization reference number.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getAuthorisationReferenceNumber() {
            return authorisationReferenceNumber;
        }

        /**
         * Regulatory authorization date.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getEffectiveDate() {
            return effectiveDate;
        }

        /**
         * To indicate if this proces is commercially confidential.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getConfidentialityIndicator() {
            return confidentialityIndicator;
        }

        /**
         * The manufacturer or establishment associated with the process.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getManufacturer() {
            return manufacturer;
        }

        /**
         * A regulator which oversees the operation.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getRegulator() {
            return regulator;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (operationType != null) || 
                (authorisationReferenceNumber != null) || 
                (effectiveDate != null) || 
                (confidentialityIndicator != null) || 
                !manufacturer.isEmpty() || 
                (regulator != null);
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
                    accept(operationType, "operationType", visitor);
                    accept(authorisationReferenceNumber, "authorisationReferenceNumber", visitor);
                    accept(effectiveDate, "effectiveDate", visitor);
                    accept(confidentialityIndicator, "confidentialityIndicator", visitor);
                    accept(manufacturer, "manufacturer", visitor, Reference.class);
                    accept(regulator, "regulator", visitor);
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
            ManufacturingBusinessOperation other = (ManufacturingBusinessOperation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(operationType, other.operationType) && 
                Objects.equals(authorisationReferenceNumber, other.authorisationReferenceNumber) && 
                Objects.equals(effectiveDate, other.effectiveDate) && 
                Objects.equals(confidentialityIndicator, other.confidentialityIndicator) && 
                Objects.equals(manufacturer, other.manufacturer) && 
                Objects.equals(regulator, other.regulator);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    operationType, 
                    authorisationReferenceNumber, 
                    effectiveDate, 
                    confidentialityIndicator, 
                    manufacturer, 
                    regulator);
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
            private CodeableConcept operationType;
            private Identifier authorisationReferenceNumber;
            private DateTime effectiveDate;
            private CodeableConcept confidentialityIndicator;
            private List<Reference> manufacturer = new ArrayList<>();
            private Reference regulator;

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
             * The type of manufacturing operation.
             * 
             * @param operationType
             *     The type of manufacturing operation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder operationType(CodeableConcept operationType) {
                this.operationType = operationType;
                return this;
            }

            /**
             * Regulatory authorization reference number.
             * 
             * @param authorisationReferenceNumber
             *     Regulatory authorization reference number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder authorisationReferenceNumber(Identifier authorisationReferenceNumber) {
                this.authorisationReferenceNumber = authorisationReferenceNumber;
                return this;
            }

            /**
             * Regulatory authorization date.
             * 
             * @param effectiveDate
             *     Regulatory authorization date
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder effectiveDate(DateTime effectiveDate) {
                this.effectiveDate = effectiveDate;
                return this;
            }

            /**
             * To indicate if this proces is commercially confidential.
             * 
             * @param confidentialityIndicator
             *     To indicate if this proces is commercially confidential
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder confidentialityIndicator(CodeableConcept confidentialityIndicator) {
                this.confidentialityIndicator = confidentialityIndicator;
                return this;
            }

            /**
             * The manufacturer or establishment associated with the process.
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
             *     The manufacturer or establishment associated with the process
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
             * The manufacturer or establishment associated with the process.
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
             *     The manufacturer or establishment associated with the process
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
             * A regulator which oversees the operation.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param regulator
             *     A regulator which oversees the operation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder regulator(Reference regulator) {
                this.regulator = regulator;
                return this;
            }

            /**
             * Build the {@link ManufacturingBusinessOperation}
             * 
             * @return
             *     An immutable object of type {@link ManufacturingBusinessOperation}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ManufacturingBusinessOperation per the base specification
             */
            @Override
            public ManufacturingBusinessOperation build() {
                ManufacturingBusinessOperation manufacturingBusinessOperation = new ManufacturingBusinessOperation(this);
                if (validating) {
                    validate(manufacturingBusinessOperation);
                }
                return manufacturingBusinessOperation;
            }

            protected void validate(ManufacturingBusinessOperation manufacturingBusinessOperation) {
                super.validate(manufacturingBusinessOperation);
                ValidationSupport.checkList(manufacturingBusinessOperation.manufacturer, "manufacturer", Reference.class);
                ValidationSupport.checkReferenceType(manufacturingBusinessOperation.manufacturer, "manufacturer", "Organization");
                ValidationSupport.checkReferenceType(manufacturingBusinessOperation.regulator, "regulator", "Organization");
                ValidationSupport.requireValueOrChildren(manufacturingBusinessOperation);
            }

            protected Builder from(ManufacturingBusinessOperation manufacturingBusinessOperation) {
                super.from(manufacturingBusinessOperation);
                operationType = manufacturingBusinessOperation.operationType;
                authorisationReferenceNumber = manufacturingBusinessOperation.authorisationReferenceNumber;
                effectiveDate = manufacturingBusinessOperation.effectiveDate;
                confidentialityIndicator = manufacturingBusinessOperation.confidentialityIndicator;
                manufacturer.addAll(manufacturingBusinessOperation.manufacturer);
                regulator = manufacturingBusinessOperation.regulator;
                return this;
            }
        }
    }

    /**
     * Indicates if the medicinal product has an orphan designation for the treatment of a rare disease.
     */
    public static class SpecialDesignation extends BackboneElement {
        @Summary
        private final List<Identifier> identifier;
        @Summary
        private final CodeableConcept type;
        @Summary
        private final CodeableConcept intendedUse;
        @Summary
        @ReferenceTarget({ "MedicinalProductIndication" })
        @Choice({ CodeableConcept.class, Reference.class })
        private final Element indication;
        @Summary
        private final CodeableConcept status;
        @Summary
        private final DateTime date;
        @Summary
        private final CodeableConcept species;

        private SpecialDesignation(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(builder.identifier);
            type = builder.type;
            intendedUse = builder.intendedUse;
            indication = builder.indication;
            status = builder.status;
            date = builder.date;
            species = builder.species;
        }

        /**
         * Identifier for the designation, or procedure number.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
         */
        public List<Identifier> getIdentifier() {
            return identifier;
        }

        /**
         * The type of special designation, e.g. orphan drug, minor use.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The intended use of the product, e.g. prevention, treatment.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getIntendedUse() {
            return intendedUse;
        }

        /**
         * Condition for which the medicinal use applies.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} or {@link Reference} that may be null.
         */
        public Element getIndication() {
            return indication;
        }

        /**
         * For example granted, pending, expired or withdrawn.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStatus() {
            return status;
        }

        /**
         * Date when the designation was granted.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getDate() {
            return date;
        }

        /**
         * Animal species for which this applies.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getSpecies() {
            return species;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !identifier.isEmpty() || 
                (type != null) || 
                (intendedUse != null) || 
                (indication != null) || 
                (status != null) || 
                (date != null) || 
                (species != null);
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
                    accept(identifier, "identifier", visitor, Identifier.class);
                    accept(type, "type", visitor);
                    accept(intendedUse, "intendedUse", visitor);
                    accept(indication, "indication", visitor);
                    accept(status, "status", visitor);
                    accept(date, "date", visitor);
                    accept(species, "species", visitor);
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
            SpecialDesignation other = (SpecialDesignation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(type, other.type) && 
                Objects.equals(intendedUse, other.intendedUse) && 
                Objects.equals(indication, other.indication) && 
                Objects.equals(status, other.status) && 
                Objects.equals(date, other.date) && 
                Objects.equals(species, other.species);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    type, 
                    intendedUse, 
                    indication, 
                    status, 
                    date, 
                    species);
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
            private List<Identifier> identifier = new ArrayList<>();
            private CodeableConcept type;
            private CodeableConcept intendedUse;
            private Element indication;
            private CodeableConcept status;
            private DateTime date;
            private CodeableConcept species;

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
             * Identifier for the designation, or procedure number.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     Identifier for the designation, or procedure number
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
             * Identifier for the designation, or procedure number.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     Identifier for the designation, or procedure number
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
             * The type of special designation, e.g. orphan drug, minor use.
             * 
             * @param type
             *     The type of special designation, e.g. orphan drug, minor use
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * The intended use of the product, e.g. prevention, treatment.
             * 
             * @param intendedUse
             *     The intended use of the product, e.g. prevention, treatment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder intendedUse(CodeableConcept intendedUse) {
                this.intendedUse = intendedUse;
                return this;
            }

            /**
             * Condition for which the medicinal use applies.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link MedicinalProductIndication}</li>
             * </ul>
             * 
             * @param indication
             *     Condition for which the medicinal use applies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder indication(Element indication) {
                this.indication = indication;
                return this;
            }

            /**
             * For example granted, pending, expired or withdrawn.
             * 
             * @param status
             *     For example granted, pending, expired or withdrawn
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder status(CodeableConcept status) {
                this.status = status;
                return this;
            }

            /**
             * Date when the designation was granted.
             * 
             * @param date
             *     Date when the designation was granted
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(DateTime date) {
                this.date = date;
                return this;
            }

            /**
             * Animal species for which this applies.
             * 
             * @param species
             *     Animal species for which this applies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder species(CodeableConcept species) {
                this.species = species;
                return this;
            }

            /**
             * Build the {@link SpecialDesignation}
             * 
             * @return
             *     An immutable object of type {@link SpecialDesignation}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid SpecialDesignation per the base specification
             */
            @Override
            public SpecialDesignation build() {
                SpecialDesignation specialDesignation = new SpecialDesignation(this);
                if (validating) {
                    validate(specialDesignation);
                }
                return specialDesignation;
            }

            protected void validate(SpecialDesignation specialDesignation) {
                super.validate(specialDesignation);
                ValidationSupport.checkList(specialDesignation.identifier, "identifier", Identifier.class);
                ValidationSupport.choiceElement(specialDesignation.indication, "indication", CodeableConcept.class, Reference.class);
                ValidationSupport.checkReferenceType(specialDesignation.indication, "indication", "MedicinalProductIndication");
                ValidationSupport.requireValueOrChildren(specialDesignation);
            }

            protected Builder from(SpecialDesignation specialDesignation) {
                super.from(specialDesignation);
                identifier.addAll(specialDesignation.identifier);
                type = specialDesignation.type;
                intendedUse = specialDesignation.intendedUse;
                indication = specialDesignation.indication;
                status = specialDesignation.status;
                date = specialDesignation.date;
                species = specialDesignation.species;
                return this;
            }
        }
    }
}
