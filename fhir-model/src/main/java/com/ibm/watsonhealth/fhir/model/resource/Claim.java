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

import com.ibm.watsonhealth.fhir.model.type.Address;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.ClaimStatus;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Use;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A provider issued list of professional services and products which have been provided, or are to be provided, to a 
 * patient which is sent to an insurer for reimbursement.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Claim extends DomainResource {
    private final List<Identifier> identifier;
    private final ClaimStatus status;
    private final CodeableConcept type;
    private final CodeableConcept subType;
    private final Use use;
    private final Reference patient;
    private final Period billablePeriod;
    private final DateTime created;
    private final Reference enterer;
    private final Reference insurer;
    private final Reference provider;
    private final CodeableConcept priority;
    private final CodeableConcept fundsReserve;
    private final List<Related> related;
    private final Reference prescription;
    private final Reference originalPrescription;
    private final Payee payee;
    private final Reference referral;
    private final Reference facility;
    private final List<CareTeam> careTeam;
    private final List<SupportingInfo> supportingInfo;
    private final List<Diagnosis> diagnosis;
    private final List<Procedure> procedure;
    private final List<Insurance> insurance;
    private final Accident accident;
    private final List<Item> item;
    private final Money total;

    private Claim(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        type = ValidationSupport.requireNonNull(builder.type, "type");
        subType = builder.subType;
        use = ValidationSupport.requireNonNull(builder.use, "use");
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        billablePeriod = builder.billablePeriod;
        created = ValidationSupport.requireNonNull(builder.created, "created");
        enterer = builder.enterer;
        insurer = builder.insurer;
        provider = ValidationSupport.requireNonNull(builder.provider, "provider");
        priority = ValidationSupport.requireNonNull(builder.priority, "priority");
        fundsReserve = builder.fundsReserve;
        related = Collections.unmodifiableList(builder.related);
        prescription = builder.prescription;
        originalPrescription = builder.originalPrescription;
        payee = builder.payee;
        referral = builder.referral;
        facility = builder.facility;
        careTeam = Collections.unmodifiableList(builder.careTeam);
        supportingInfo = Collections.unmodifiableList(builder.supportingInfo);
        diagnosis = Collections.unmodifiableList(builder.diagnosis);
        procedure = Collections.unmodifiableList(builder.procedure);
        insurance = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.insurance, "insurance"));
        accident = builder.accident;
        item = Collections.unmodifiableList(builder.item);
        total = builder.total;
    }

    /**
     * <p>
     * A unique identifier assigned to this claim.
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
     * The status of the resource instance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ClaimStatus}.
     */
    public ClaimStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The category of claim, e.g. oral, pharmacy, vision, institutional, professional.
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
     * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient 
     * and/or a specialty service.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getSubType() {
        return subType;
    }

    /**
     * <p>
     * A code to indicate whether the nature of the request is: to request adjudication of products and services previously 
     * rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding 
     * adjudication of the listed products and services which could be provided in the future.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Use}.
     */
    public Use getUse() {
        return use;
    }

    /**
     * <p>
     * The party to whom the professional services and/or products have been supplied or are being considered and for whom 
     * actual or forecast reimbursement is sought.
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
     * The period for which charges are being submitted.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getBillablePeriod() {
        return billablePeriod;
    }

    /**
     * <p>
     * The date this resource was created.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * <p>
     * Individual who created the claim, predetermination or preauthorization.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getEnterer() {
        return enterer;
    }

    /**
     * <p>
     * The Insurer who is target of the request.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getInsurer() {
        return insurer;
    }

    /**
     * <p>
     * The provider which is responsible for the claim, predetermination or preauthorization.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getProvider() {
        return provider;
    }

    /**
     * <p>
     * The provider-required urgency of processing the request. Typical values include: stat, routine deferred.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getPriority() {
        return priority;
    }

    /**
     * <p>
     * A code to indicate whether and for whom funds are to be reserved for future claims.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getFundsReserve() {
        return fundsReserve;
    }

    /**
     * <p>
     * Other claims which are related to this claim such as prior submissions or claims for related services or for the same 
     * event.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Related}.
     */
    public List<Related> getRelated() {
        return related;
    }

    /**
     * <p>
     * Prescription to support the dispensing of pharmacy, device or vision products.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPrescription() {
        return prescription;
    }

    /**
     * <p>
     * Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, 
     * medications or products.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOriginalPrescription() {
        return originalPrescription;
    }

    /**
     * <p>
     * The party to be reimbursed for cost of the products and services according to the terms of the policy.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Payee}.
     */
    public Payee getPayee() {
        return payee;
    }

    /**
     * <p>
     * A reference to a referral resource.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getReferral() {
        return referral;
    }

    /**
     * <p>
     * Facility where the services were provided.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getFacility() {
        return facility;
    }

    /**
     * <p>
     * The members of the team who provided the products and services.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CareTeam}.
     */
    public List<CareTeam> getCareTeam() {
        return careTeam;
    }

    /**
     * <p>
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
     * concurrent issues.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link SupportingInfo}.
     */
    public List<SupportingInfo> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * <p>
     * Information about diagnoses relevant to the claim items.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Diagnosis}.
     */
    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }

    /**
     * <p>
     * Procedures performed on the patient relevant to the billing items with the claim.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Procedure}.
     */
    public List<Procedure> getProcedure() {
        return procedure;
    }

    /**
     * <p>
     * Financial instruments for reimbursement for the health care products and services specified on the claim.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Insurance}.
     */
    public List<Insurance> getInsurance() {
        return insurance;
    }

    /**
     * <p>
     * Details of an accident which resulted in injuries which required the products and services listed in the claim.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Accident}.
     */
    public Accident getAccident() {
        return accident;
    }

    /**
     * <p>
     * A claim line. Either a simple product or service or a 'group' of details which can each be a simple items or groups of 
     * sub-details.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Item}.
     */
    public List<Item> getItem() {
        return item;
    }

    /**
     * <p>
     * The total value of the all the items in the claim.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Money}.
     */
    public Money getTotal() {
        return total;
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
                accept(status, "status", visitor);
                accept(type, "type", visitor);
                accept(subType, "subType", visitor);
                accept(use, "use", visitor);
                accept(patient, "patient", visitor);
                accept(billablePeriod, "billablePeriod", visitor);
                accept(created, "created", visitor);
                accept(enterer, "enterer", visitor);
                accept(insurer, "insurer", visitor);
                accept(provider, "provider", visitor);
                accept(priority, "priority", visitor);
                accept(fundsReserve, "fundsReserve", visitor);
                accept(related, "related", visitor, Related.class);
                accept(prescription, "prescription", visitor);
                accept(originalPrescription, "originalPrescription", visitor);
                accept(payee, "payee", visitor);
                accept(referral, "referral", visitor);
                accept(facility, "facility", visitor);
                accept(careTeam, "careTeam", visitor, CareTeam.class);
                accept(supportingInfo, "supportingInfo", visitor, SupportingInfo.class);
                accept(diagnosis, "diagnosis", visitor, Diagnosis.class);
                accept(procedure, "procedure", visitor, Procedure.class);
                accept(insurance, "insurance", visitor, Insurance.class);
                accept(accident, "accident", visitor);
                accept(item, "item", visitor, Item.class);
                accept(total, "total", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, type, use, patient, created, provider, priority, insurance);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.subType = subType;
        builder.billablePeriod = billablePeriod;
        builder.enterer = enterer;
        builder.insurer = insurer;
        builder.fundsReserve = fundsReserve;
        builder.related.addAll(related);
        builder.prescription = prescription;
        builder.originalPrescription = originalPrescription;
        builder.payee = payee;
        builder.referral = referral;
        builder.facility = facility;
        builder.careTeam.addAll(careTeam);
        builder.supportingInfo.addAll(supportingInfo);
        builder.diagnosis.addAll(diagnosis);
        builder.procedure.addAll(procedure);
        builder.accident = accident;
        builder.item.addAll(item);
        builder.total = total;
        return builder;
    }

    public static Builder builder(ClaimStatus status, CodeableConcept type, Use use, Reference patient, DateTime created, Reference provider, CodeableConcept priority, List<Insurance> insurance) {
        return new Builder(status, type, use, patient, created, provider, priority, insurance);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ClaimStatus status;
        private final CodeableConcept type;
        private final Use use;
        private final Reference patient;
        private final DateTime created;
        private final Reference provider;
        private final CodeableConcept priority;
        private final List<Insurance> insurance;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept subType;
        private Period billablePeriod;
        private Reference enterer;
        private Reference insurer;
        private CodeableConcept fundsReserve;
        private List<Related> related = new ArrayList<>();
        private Reference prescription;
        private Reference originalPrescription;
        private Payee payee;
        private Reference referral;
        private Reference facility;
        private List<CareTeam> careTeam = new ArrayList<>();
        private List<SupportingInfo> supportingInfo = new ArrayList<>();
        private List<Diagnosis> diagnosis = new ArrayList<>();
        private List<Procedure> procedure = new ArrayList<>();
        private Accident accident;
        private List<Item> item = new ArrayList<>();
        private Money total;

        private Builder(ClaimStatus status, CodeableConcept type, Use use, Reference patient, DateTime created, Reference provider, CodeableConcept priority, List<Insurance> insurance) {
            super();
            this.status = status;
            this.type = type;
            this.use = use;
            this.patient = patient;
            this.created = created;
            this.provider = provider;
            this.priority = priority;
            this.insurance = insurance;
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
         * A unique identifier assigned to this claim.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for claim
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
         * A unique identifier assigned to this claim.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for claim
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
         * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient 
         * and/or a specialty service.
         * </p>
         * 
         * @param subType
         *     More granular claim type
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subType(CodeableConcept subType) {
            this.subType = subType;
            return this;
        }

        /**
         * <p>
         * The period for which charges are being submitted.
         * </p>
         * 
         * @param billablePeriod
         *     Relevant time frame for the claim
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder billablePeriod(Period billablePeriod) {
            this.billablePeriod = billablePeriod;
            return this;
        }

        /**
         * <p>
         * Individual who created the claim, predetermination or preauthorization.
         * </p>
         * 
         * @param enterer
         *     Author of the claim
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder enterer(Reference enterer) {
            this.enterer = enterer;
            return this;
        }

        /**
         * <p>
         * The Insurer who is target of the request.
         * </p>
         * 
         * @param insurer
         *     Target
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder insurer(Reference insurer) {
            this.insurer = insurer;
            return this;
        }

        /**
         * <p>
         * A code to indicate whether and for whom funds are to be reserved for future claims.
         * </p>
         * 
         * @param fundsReserve
         *     For whom to reserve funds
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder fundsReserve(CodeableConcept fundsReserve) {
            this.fundsReserve = fundsReserve;
            return this;
        }

        /**
         * <p>
         * Other claims which are related to this claim such as prior submissions or claims for related services or for the same 
         * event.
         * </p>
         * 
         * @param related
         *     Prior or corollary claims
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder related(Related... related) {
            for (Related value : related) {
                this.related.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Other claims which are related to this claim such as prior submissions or claims for related services or for the same 
         * event.
         * </p>
         * 
         * @param related
         *     Prior or corollary claims
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder related(Collection<Related> related) {
            this.related.addAll(related);
            return this;
        }

        /**
         * <p>
         * Prescription to support the dispensing of pharmacy, device or vision products.
         * </p>
         * 
         * @param prescription
         *     Prescription authorizing services and products
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder prescription(Reference prescription) {
            this.prescription = prescription;
            return this;
        }

        /**
         * <p>
         * Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, 
         * medications or products.
         * </p>
         * 
         * @param originalPrescription
         *     Original prescription if superseded by fulfiller
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder originalPrescription(Reference originalPrescription) {
            this.originalPrescription = originalPrescription;
            return this;
        }

        /**
         * <p>
         * The party to be reimbursed for cost of the products and services according to the terms of the policy.
         * </p>
         * 
         * @param payee
         *     Recipient of benefits payable
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder payee(Payee payee) {
            this.payee = payee;
            return this;
        }

        /**
         * <p>
         * A reference to a referral resource.
         * </p>
         * 
         * @param referral
         *     Treatment referral
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder referral(Reference referral) {
            this.referral = referral;
            return this;
        }

        /**
         * <p>
         * Facility where the services were provided.
         * </p>
         * 
         * @param facility
         *     Servicing facility
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder facility(Reference facility) {
            this.facility = facility;
            return this;
        }

        /**
         * <p>
         * The members of the team who provided the products and services.
         * </p>
         * 
         * @param careTeam
         *     Members of the care team
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder careTeam(CareTeam... careTeam) {
            for (CareTeam value : careTeam) {
                this.careTeam.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The members of the team who provided the products and services.
         * </p>
         * 
         * @param careTeam
         *     Members of the care team
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder careTeam(Collection<CareTeam> careTeam) {
            this.careTeam.addAll(careTeam);
            return this;
        }

        /**
         * <p>
         * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
         * concurrent issues.
         * </p>
         * 
         * @param supportingInfo
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInfo(SupportingInfo... supportingInfo) {
            for (SupportingInfo value : supportingInfo) {
                this.supportingInfo.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
         * concurrent issues.
         * </p>
         * 
         * @param supportingInfo
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInfo(Collection<SupportingInfo> supportingInfo) {
            this.supportingInfo.addAll(supportingInfo);
            return this;
        }

        /**
         * <p>
         * Information about diagnoses relevant to the claim items.
         * </p>
         * 
         * @param diagnosis
         *     Pertinent diagnosis information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder diagnosis(Diagnosis... diagnosis) {
            for (Diagnosis value : diagnosis) {
                this.diagnosis.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Information about diagnoses relevant to the claim items.
         * </p>
         * 
         * @param diagnosis
         *     Pertinent diagnosis information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder diagnosis(Collection<Diagnosis> diagnosis) {
            this.diagnosis.addAll(diagnosis);
            return this;
        }

        /**
         * <p>
         * Procedures performed on the patient relevant to the billing items with the claim.
         * </p>
         * 
         * @param procedure
         *     Clinical procedures performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder procedure(Procedure... procedure) {
            for (Procedure value : procedure) {
                this.procedure.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Procedures performed on the patient relevant to the billing items with the claim.
         * </p>
         * 
         * @param procedure
         *     Clinical procedures performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder procedure(Collection<Procedure> procedure) {
            this.procedure.addAll(procedure);
            return this;
        }

        /**
         * <p>
         * Financial instruments for reimbursement for the health care products and services specified on the claim.
         * </p>
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder insurance(Insurance... insurance) {
            for (Insurance value : insurance) {
                this.insurance.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Financial instruments for reimbursement for the health care products and services specified on the claim.
         * </p>
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder insurance(Collection<Insurance> insurance) {
            this.insurance.addAll(insurance);
            return this;
        }

        /**
         * <p>
         * Details of an accident which resulted in injuries which required the products and services listed in the claim.
         * </p>
         * 
         * @param accident
         *     Details of the event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder accident(Accident accident) {
            this.accident = accident;
            return this;
        }

        /**
         * <p>
         * A claim line. Either a simple product or service or a 'group' of details which can each be a simple items or groups of 
         * sub-details.
         * </p>
         * 
         * @param item
         *     Product or service provided
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder item(Item... item) {
            for (Item value : item) {
                this.item.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A claim line. Either a simple product or service or a 'group' of details which can each be a simple items or groups of 
         * sub-details.
         * </p>
         * 
         * @param item
         *     Product or service provided
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder item(Collection<Item> item) {
            this.item.addAll(item);
            return this;
        }

        /**
         * <p>
         * The total value of the all the items in the claim.
         * </p>
         * 
         * @param total
         *     Total claim cost
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder total(Money total) {
            this.total = total;
            return this;
        }

        @Override
        public Claim build() {
            return new Claim(this);
        }
    }

    /**
     * <p>
     * Other claims which are related to this claim such as prior submissions or claims for related services or for the same 
     * event.
     * </p>
     */
    public static class Related extends BackboneElement {
        private final Reference claim;
        private final CodeableConcept relationship;
        private final Identifier reference;

        private Related(Builder builder) {
            super(builder);
            claim = builder.claim;
            relationship = builder.relationship;
            reference = builder.reference;
        }

        /**
         * <p>
         * Reference to a related claim.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getClaim() {
            return claim;
        }

        /**
         * <p>
         * A code to convey how the claims are related.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getRelationship() {
            return relationship;
        }

        /**
         * <p>
         * An alternate organizational reference to the case or file to which this particular claim pertains.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getReference() {
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
                    accept(claim, "claim", visitor);
                    accept(relationship, "relationship", visitor);
                    accept(reference, "reference", visitor);
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
            private Reference claim;
            private CodeableConcept relationship;
            private Identifier reference;

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
             * Reference to a related claim.
             * </p>
             * 
             * @param claim
             *     Reference to the related claim
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder claim(Reference claim) {
                this.claim = claim;
                return this;
            }

            /**
             * <p>
             * A code to convey how the claims are related.
             * </p>
             * 
             * @param relationship
             *     How the reference claim is related
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder relationship(CodeableConcept relationship) {
                this.relationship = relationship;
                return this;
            }

            /**
             * <p>
             * An alternate organizational reference to the case or file to which this particular claim pertains.
             * </p>
             * 
             * @param reference
             *     File or case reference
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder reference(Identifier reference) {
                this.reference = reference;
                return this;
            }

            @Override
            public Related build() {
                return new Related(this);
            }

            private static Builder from(Related related) {
                Builder builder = new Builder();
                builder.claim = related.claim;
                builder.relationship = related.relationship;
                builder.reference = related.reference;
                return builder;
            }
        }
    }

    /**
     * <p>
     * The party to be reimbursed for cost of the products and services according to the terms of the policy.
     * </p>
     */
    public static class Payee extends BackboneElement {
        private final CodeableConcept type;
        private final Reference party;

        private Payee(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            party = builder.party;
        }

        /**
         * <p>
         * Type of Party to be reimbursed: subscriber, provider, other.
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
         * Reference to the individual or organization to whom any payment will be made.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getParty() {
            return party;
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
                    accept(party, "party", visitor);
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
            private Reference party;

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
             * Reference to the individual or organization to whom any payment will be made.
             * </p>
             * 
             * @param party
             *     Recipient reference
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder party(Reference party) {
                this.party = party;
                return this;
            }

            @Override
            public Payee build() {
                return new Payee(this);
            }

            private static Builder from(Payee payee) {
                Builder builder = new Builder(payee.type);
                builder.party = payee.party;
                return builder;
            }
        }
    }

    /**
     * <p>
     * The members of the team who provided the products and services.
     * </p>
     */
    public static class CareTeam extends BackboneElement {
        private final PositiveInt sequence;
        private final Reference provider;
        private final Boolean responsible;
        private final CodeableConcept role;
        private final CodeableConcept qualification;

        private CareTeam(Builder builder) {
            super(builder);
            sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            provider = ValidationSupport.requireNonNull(builder.provider, "provider");
            responsible = builder.responsible;
            role = builder.role;
            qualification = builder.qualification;
        }

        /**
         * <p>
         * A number to uniquely identify care team entries.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * <p>
         * Member of the team who provided the product or service.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getProvider() {
            return provider;
        }

        /**
         * <p>
         * The party who is billing and/or responsible for the claimed products or services.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getResponsible() {
            return responsible;
        }

        /**
         * <p>
         * The lead, assisting or supervising practitioner and their discipline if a multidisciplinary team.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getRole() {
            return role;
        }

        /**
         * <p>
         * The qualification of the practitioner which is applicable for this service.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getQualification() {
            return qualification;
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
                    accept(sequence, "sequence", visitor);
                    accept(provider, "provider", visitor);
                    accept(responsible, "responsible", visitor);
                    accept(role, "role", visitor);
                    accept(qualification, "qualification", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(PositiveInt sequence, Reference provider) {
            return new Builder(sequence, provider);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final PositiveInt sequence;
            private final Reference provider;

            // optional
            private Boolean responsible;
            private CodeableConcept role;
            private CodeableConcept qualification;

            private Builder(PositiveInt sequence, Reference provider) {
                super();
                this.sequence = sequence;
                this.provider = provider;
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
             * The party who is billing and/or responsible for the claimed products or services.
             * </p>
             * 
             * @param responsible
             *     Indicator of the lead practitioner
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder responsible(Boolean responsible) {
                this.responsible = responsible;
                return this;
            }

            /**
             * <p>
             * The lead, assisting or supervising practitioner and their discipline if a multidisciplinary team.
             * </p>
             * 
             * @param role
             *     Function within the team
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder role(CodeableConcept role) {
                this.role = role;
                return this;
            }

            /**
             * <p>
             * The qualification of the practitioner which is applicable for this service.
             * </p>
             * 
             * @param qualification
             *     Practitioner credential or specialization
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder qualification(CodeableConcept qualification) {
                this.qualification = qualification;
                return this;
            }

            @Override
            public CareTeam build() {
                return new CareTeam(this);
            }

            private static Builder from(CareTeam careTeam) {
                Builder builder = new Builder(careTeam.sequence, careTeam.provider);
                builder.responsible = careTeam.responsible;
                builder.role = careTeam.role;
                builder.qualification = careTeam.qualification;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
     * concurrent issues.
     * </p>
     */
    public static class SupportingInfo extends BackboneElement {
        private final PositiveInt sequence;
        private final CodeableConcept category;
        private final CodeableConcept code;
        private final Element timing;
        private final Element value;
        private final CodeableConcept reason;

        private SupportingInfo(Builder builder) {
            super(builder);
            sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            category = ValidationSupport.requireNonNull(builder.category, "category");
            code = builder.code;
            timing = ValidationSupport.choiceElement(builder.timing, "timing", Date.class, Period.class);
            value = ValidationSupport.choiceElement(builder.value, "value", Boolean.class, String.class, Quantity.class, Attachment.class, Reference.class);
            reason = builder.reason;
        }

        /**
         * <p>
         * A number to uniquely identify supporting information entries.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * <p>
         * The general class of the information supplied: information; exception; accident, employment; onset, etc.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * <p>
         * System and code pertaining to the specific information regarding special conditions relating to the setting, treatment 
         * or patient for which care is sought.
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
         * The date when or period to which this information refers.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getTiming() {
            return timing;
        }

        /**
         * <p>
         * Additional data or information such as resources, documents, images etc. including references to the data or the 
         * actual inclusion of the data.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getValue() {
            return value;
        }

        /**
         * <p>
         * Provides the reason in the situation where a reason code is required in addition to the content.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getReason() {
            return reason;
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
                    accept(sequence, "sequence", visitor);
                    accept(category, "category", visitor);
                    accept(code, "code", visitor);
                    accept(timing, "timing", visitor, true);
                    accept(value, "value", visitor, true);
                    accept(reason, "reason", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(PositiveInt sequence, CodeableConcept category) {
            return new Builder(sequence, category);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final PositiveInt sequence;
            private final CodeableConcept category;

            // optional
            private CodeableConcept code;
            private Element timing;
            private Element value;
            private CodeableConcept reason;

            private Builder(PositiveInt sequence, CodeableConcept category) {
                super();
                this.sequence = sequence;
                this.category = category;
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
             * System and code pertaining to the specific information regarding special conditions relating to the setting, treatment 
             * or patient for which care is sought.
             * </p>
             * 
             * @param code
             *     Type of information
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
             * The date when or period to which this information refers.
             * </p>
             * 
             * @param timing
             *     When it occurred
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder timing(Element timing) {
                this.timing = timing;
                return this;
            }

            /**
             * <p>
             * Additional data or information such as resources, documents, images etc. including references to the data or the 
             * actual inclusion of the data.
             * </p>
             * 
             * @param value
             *     Data to be provided
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * <p>
             * Provides the reason in the situation where a reason code is required in addition to the content.
             * </p>
             * 
             * @param reason
             *     Explanation for the information
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder reason(CodeableConcept reason) {
                this.reason = reason;
                return this;
            }

            @Override
            public SupportingInfo build() {
                return new SupportingInfo(this);
            }

            private static Builder from(SupportingInfo supportingInfo) {
                Builder builder = new Builder(supportingInfo.sequence, supportingInfo.category);
                builder.code = supportingInfo.code;
                builder.timing = supportingInfo.timing;
                builder.value = supportingInfo.value;
                builder.reason = supportingInfo.reason;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Information about diagnoses relevant to the claim items.
     * </p>
     */
    public static class Diagnosis extends BackboneElement {
        private final PositiveInt sequence;
        private final Element diagnosis;
        private final List<CodeableConcept> type;
        private final CodeableConcept onAdmission;
        private final CodeableConcept packageCode;

        private Diagnosis(Builder builder) {
            super(builder);
            sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            diagnosis = ValidationSupport.requireChoiceElement(builder.diagnosis, "diagnosis", CodeableConcept.class, Reference.class);
            type = Collections.unmodifiableList(builder.type);
            onAdmission = builder.onAdmission;
            packageCode = builder.packageCode;
        }

        /**
         * <p>
         * A number to uniquely identify diagnosis entries.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * <p>
         * The nature of illness or problem in a coded form or as a reference to an external defined Condition.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getDiagnosis() {
            return diagnosis;
        }

        /**
         * <p>
         * When the condition was observed or the relative ranking.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getType() {
            return type;
        }

        /**
         * <p>
         * Indication of whether the diagnosis was present on admission to a facility.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getOnAdmission() {
            return onAdmission;
        }

        /**
         * <p>
         * A package billing code or bundle code used to group products and services to a particular health condition (such as 
         * heart attack) which is based on a predetermined grouping code system.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getPackageCode() {
            return packageCode;
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
                    accept(sequence, "sequence", visitor);
                    accept(diagnosis, "diagnosis", visitor, true);
                    accept(type, "type", visitor, CodeableConcept.class);
                    accept(onAdmission, "onAdmission", visitor);
                    accept(packageCode, "packageCode", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(PositiveInt sequence, Element diagnosis) {
            return new Builder(sequence, diagnosis);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final PositiveInt sequence;
            private final Element diagnosis;

            // optional
            private List<CodeableConcept> type = new ArrayList<>();
            private CodeableConcept onAdmission;
            private CodeableConcept packageCode;

            private Builder(PositiveInt sequence, Element diagnosis) {
                super();
                this.sequence = sequence;
                this.diagnosis = diagnosis;
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
             * When the condition was observed or the relative ranking.
             * </p>
             * 
             * @param type
             *     Timing or nature of the diagnosis
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(CodeableConcept... type) {
                for (CodeableConcept value : type) {
                    this.type.add(value);
                }
                return this;
            }

            /**
             * <p>
             * When the condition was observed or the relative ranking.
             * </p>
             * 
             * @param type
             *     Timing or nature of the diagnosis
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(Collection<CodeableConcept> type) {
                this.type.addAll(type);
                return this;
            }

            /**
             * <p>
             * Indication of whether the diagnosis was present on admission to a facility.
             * </p>
             * 
             * @param onAdmission
             *     Present on admission
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder onAdmission(CodeableConcept onAdmission) {
                this.onAdmission = onAdmission;
                return this;
            }

            /**
             * <p>
             * A package billing code or bundle code used to group products and services to a particular health condition (such as 
             * heart attack) which is based on a predetermined grouping code system.
             * </p>
             * 
             * @param packageCode
             *     Package billing code
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder packageCode(CodeableConcept packageCode) {
                this.packageCode = packageCode;
                return this;
            }

            @Override
            public Diagnosis build() {
                return new Diagnosis(this);
            }

            private static Builder from(Diagnosis diagnosis) {
                Builder builder = new Builder(diagnosis.sequence, diagnosis.diagnosis);
                builder.type.addAll(diagnosis.type);
                builder.onAdmission = diagnosis.onAdmission;
                builder.packageCode = diagnosis.packageCode;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Procedures performed on the patient relevant to the billing items with the claim.
     * </p>
     */
    public static class Procedure extends BackboneElement {
        private final PositiveInt sequence;
        private final List<CodeableConcept> type;
        private final DateTime date;
        private final Element procedure;
        private final List<Reference> udi;

        private Procedure(Builder builder) {
            super(builder);
            sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            type = Collections.unmodifiableList(builder.type);
            date = builder.date;
            procedure = ValidationSupport.requireChoiceElement(builder.procedure, "procedure", CodeableConcept.class, Reference.class);
            udi = Collections.unmodifiableList(builder.udi);
        }

        /**
         * <p>
         * A number to uniquely identify procedure entries.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * <p>
         * When the condition was observed or the relative ranking.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getType() {
            return type;
        }

        /**
         * <p>
         * Date and optionally time the procedure was performed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getDate() {
            return date;
        }

        /**
         * <p>
         * The code or reference to a Procedure resource which identifies the clinical intervention performed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getProcedure() {
            return procedure;
        }

        /**
         * <p>
         * Unique Device Identifiers associated with this line item.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getUdi() {
            return udi;
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
                    accept(sequence, "sequence", visitor);
                    accept(type, "type", visitor, CodeableConcept.class);
                    accept(date, "date", visitor);
                    accept(procedure, "procedure", visitor, true);
                    accept(udi, "udi", visitor, Reference.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(PositiveInt sequence, Element procedure) {
            return new Builder(sequence, procedure);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final PositiveInt sequence;
            private final Element procedure;

            // optional
            private List<CodeableConcept> type = new ArrayList<>();
            private DateTime date;
            private List<Reference> udi = new ArrayList<>();

            private Builder(PositiveInt sequence, Element procedure) {
                super();
                this.sequence = sequence;
                this.procedure = procedure;
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
             * When the condition was observed or the relative ranking.
             * </p>
             * 
             * @param type
             *     Category of Procedure
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(CodeableConcept... type) {
                for (CodeableConcept value : type) {
                    this.type.add(value);
                }
                return this;
            }

            /**
             * <p>
             * When the condition was observed or the relative ranking.
             * </p>
             * 
             * @param type
             *     Category of Procedure
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(Collection<CodeableConcept> type) {
                this.type.addAll(type);
                return this;
            }

            /**
             * <p>
             * Date and optionally time the procedure was performed.
             * </p>
             * 
             * @param date
             *     When the procedure was performed
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder date(DateTime date) {
                this.date = date;
                return this;
            }

            /**
             * <p>
             * Unique Device Identifiers associated with this line item.
             * </p>
             * 
             * @param udi
             *     Unique device identifier
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder udi(Reference... udi) {
                for (Reference value : udi) {
                    this.udi.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Unique Device Identifiers associated with this line item.
             * </p>
             * 
             * @param udi
             *     Unique device identifier
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder udi(Collection<Reference> udi) {
                this.udi.addAll(udi);
                return this;
            }

            @Override
            public Procedure build() {
                return new Procedure(this);
            }

            private static Builder from(Procedure procedure) {
                Builder builder = new Builder(procedure.sequence, procedure.procedure);
                builder.type.addAll(procedure.type);
                builder.date = procedure.date;
                builder.udi.addAll(procedure.udi);
                return builder;
            }
        }
    }

    /**
     * <p>
     * Financial instruments for reimbursement for the health care products and services specified on the claim.
     * </p>
     */
    public static class Insurance extends BackboneElement {
        private final PositiveInt sequence;
        private final Boolean focal;
        private final Identifier identifier;
        private final Reference coverage;
        private final String businessArrangement;
        private final List<String> preAuthRef;
        private final Reference claimResponse;

        private Insurance(Builder builder) {
            super(builder);
            sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            focal = ValidationSupport.requireNonNull(builder.focal, "focal");
            identifier = builder.identifier;
            coverage = ValidationSupport.requireNonNull(builder.coverage, "coverage");
            businessArrangement = builder.businessArrangement;
            preAuthRef = Collections.unmodifiableList(builder.preAuthRef);
            claimResponse = builder.claimResponse;
        }

        /**
         * <p>
         * A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit 
         * order.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * <p>
         * A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getFocal() {
            return focal;
        }

        /**
         * <p>
         * The business identifier to be used when the claim is sent for adjudication against this insurance policy.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * <p>
         * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer 
         * will use these details to locate the patient's actual coverage within the insurer's information system.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getCoverage() {
            return coverage;
        }

        /**
         * <p>
         * A business agreement number established between the provider and the insurer for special business processing purposes.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getBusinessArrangement() {
            return businessArrangement;
        }

        /**
         * <p>
         * Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing 
         * services or products related to the prior authorization.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link String}.
         */
        public List<String> getPreAuthRef() {
            return preAuthRef;
        }

        /**
         * <p>
         * The result of the adjudication of the line items for the Coverage specified in this insurance.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getClaimResponse() {
            return claimResponse;
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
                    accept(sequence, "sequence", visitor);
                    accept(focal, "focal", visitor);
                    accept(identifier, "identifier", visitor);
                    accept(coverage, "coverage", visitor);
                    accept(businessArrangement, "businessArrangement", visitor);
                    accept(preAuthRef, "preAuthRef", visitor, String.class);
                    accept(claimResponse, "claimResponse", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(PositiveInt sequence, Boolean focal, Reference coverage) {
            return new Builder(sequence, focal, coverage);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final PositiveInt sequence;
            private final Boolean focal;
            private final Reference coverage;

            // optional
            private Identifier identifier;
            private String businessArrangement;
            private List<String> preAuthRef = new ArrayList<>();
            private Reference claimResponse;

            private Builder(PositiveInt sequence, Boolean focal, Reference coverage) {
                super();
                this.sequence = sequence;
                this.focal = focal;
                this.coverage = coverage;
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
             * The business identifier to be used when the claim is sent for adjudication against this insurance policy.
             * </p>
             * 
             * @param identifier
             *     Pre-assigned Claim number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * <p>
             * A business agreement number established between the provider and the insurer for special business processing purposes.
             * </p>
             * 
             * @param businessArrangement
             *     Additional provider contract number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder businessArrangement(String businessArrangement) {
                this.businessArrangement = businessArrangement;
                return this;
            }

            /**
             * <p>
             * Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing 
             * services or products related to the prior authorization.
             * </p>
             * 
             * @param preAuthRef
             *     Prior authorization reference number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder preAuthRef(String... preAuthRef) {
                for (String value : preAuthRef) {
                    this.preAuthRef.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing 
             * services or products related to the prior authorization.
             * </p>
             * 
             * @param preAuthRef
             *     Prior authorization reference number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder preAuthRef(Collection<String> preAuthRef) {
                this.preAuthRef.addAll(preAuthRef);
                return this;
            }

            /**
             * <p>
             * The result of the adjudication of the line items for the Coverage specified in this insurance.
             * </p>
             * 
             * @param claimResponse
             *     Adjudication results
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder claimResponse(Reference claimResponse) {
                this.claimResponse = claimResponse;
                return this;
            }

            @Override
            public Insurance build() {
                return new Insurance(this);
            }

            private static Builder from(Insurance insurance) {
                Builder builder = new Builder(insurance.sequence, insurance.focal, insurance.coverage);
                builder.identifier = insurance.identifier;
                builder.businessArrangement = insurance.businessArrangement;
                builder.preAuthRef.addAll(insurance.preAuthRef);
                builder.claimResponse = insurance.claimResponse;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Details of an accident which resulted in injuries which required the products and services listed in the claim.
     * </p>
     */
    public static class Accident extends BackboneElement {
        private final Date date;
        private final CodeableConcept type;
        private final Element location;

        private Accident(Builder builder) {
            super(builder);
            date = ValidationSupport.requireNonNull(builder.date, "date");
            type = builder.type;
            location = ValidationSupport.choiceElement(builder.location, "location", Address.class, Reference.class);
        }

        /**
         * <p>
         * Date of an accident event related to the products and services contained in the claim.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Date}.
         */
        public Date getDate() {
            return date;
        }

        /**
         * <p>
         * The type or context of the accident event for the purposes of selection of potential insurance coverages and 
         * determination of coordination between insurers.
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
         * The physical location of the accident event.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getLocation() {
            return location;
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
                    accept(date, "date", visitor);
                    accept(type, "type", visitor);
                    accept(location, "location", visitor, true);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Date date) {
            return new Builder(date);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Date date;

            // optional
            private CodeableConcept type;
            private Element location;

            private Builder(Date date) {
                super();
                this.date = date;
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
             * The type or context of the accident event for the purposes of selection of potential insurance coverages and 
             * determination of coordination between insurers.
             * </p>
             * 
             * @param type
             *     The nature of the accident
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
             * The physical location of the accident event.
             * </p>
             * 
             * @param location
             *     Where the event occurred
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder location(Element location) {
                this.location = location;
                return this;
            }

            @Override
            public Accident build() {
                return new Accident(this);
            }

            private static Builder from(Accident accident) {
                Builder builder = new Builder(accident.date);
                builder.type = accident.type;
                builder.location = accident.location;
                return builder;
            }
        }
    }

    /**
     * <p>
     * A claim line. Either a simple product or service or a 'group' of details which can each be a simple items or groups of 
     * sub-details.
     * </p>
     */
    public static class Item extends BackboneElement {
        private final PositiveInt sequence;
        private final List<PositiveInt> careTeamSequence;
        private final List<PositiveInt> diagnosisSequence;
        private final List<PositiveInt> procedureSequence;
        private final List<PositiveInt> informationSequence;
        private final CodeableConcept revenue;
        private final CodeableConcept category;
        private final CodeableConcept productOrService;
        private final List<CodeableConcept> modifier;
        private final List<CodeableConcept> programCode;
        private final Element serviced;
        private final Element location;
        private final Quantity quantity;
        private final Money unitPrice;
        private final Decimal factor;
        private final Money net;
        private final List<Reference> udi;
        private final CodeableConcept bodySite;
        private final List<CodeableConcept> subSite;
        private final List<Reference> encounter;
        private final List<Detail> detail;

        private Item(Builder builder) {
            super(builder);
            sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            careTeamSequence = Collections.unmodifiableList(builder.careTeamSequence);
            diagnosisSequence = Collections.unmodifiableList(builder.diagnosisSequence);
            procedureSequence = Collections.unmodifiableList(builder.procedureSequence);
            informationSequence = Collections.unmodifiableList(builder.informationSequence);
            revenue = builder.revenue;
            category = builder.category;
            productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
            modifier = Collections.unmodifiableList(builder.modifier);
            programCode = Collections.unmodifiableList(builder.programCode);
            serviced = ValidationSupport.choiceElement(builder.serviced, "serviced", Date.class, Period.class);
            location = ValidationSupport.choiceElement(builder.location, "location", CodeableConcept.class, Address.class, Reference.class);
            quantity = builder.quantity;
            unitPrice = builder.unitPrice;
            factor = builder.factor;
            net = builder.net;
            udi = Collections.unmodifiableList(builder.udi);
            bodySite = builder.bodySite;
            subSite = Collections.unmodifiableList(builder.subSite);
            encounter = Collections.unmodifiableList(builder.encounter);
            detail = Collections.unmodifiableList(builder.detail);
        }

        /**
         * <p>
         * A number to uniquely identify item entries.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * <p>
         * CareTeam members related to this service or product.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getCareTeamSequence() {
            return careTeamSequence;
        }

        /**
         * <p>
         * Diagnosis applicable for this service or product.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getDiagnosisSequence() {
            return diagnosisSequence;
        }

        /**
         * <p>
         * Procedures applicable for this service or product.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getProcedureSequence() {
            return procedureSequence;
        }

        /**
         * <p>
         * Exceptions, special conditions and supporting information applicable for this service or product.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getInformationSequence() {
            return informationSequence;
        }

        /**
         * <p>
         * The type of revenue or cost center providing the product and/or service.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getRevenue() {
            return revenue;
        }

        /**
         * <p>
         * Code to identify the general type of benefits under which products and services are provided.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * <p>
         * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
         * product, service, drug or other billing code for the item.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getProductOrService() {
            return productOrService;
        }

        /**
         * <p>
         * Item typification or modifiers codes to convey additional context for the product or service.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getModifier() {
            return modifier;
        }

        /**
         * <p>
         * Identifies the program under which this may be recovered.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getProgramCode() {
            return programCode;
        }

        /**
         * <p>
         * The date or dates when the service or product was supplied, performed or completed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getServiced() {
            return serviced;
        }

        /**
         * <p>
         * Where the product or service was provided.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getLocation() {
            return location;
        }

        /**
         * <p>
         * The number of repetitions of a service or product.
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
         * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
         * for the details of the group.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getUnitPrice() {
            return unitPrice;
        }

        /**
         * <p>
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
         * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getFactor() {
            return factor;
        }

        /**
         * <p>
         * The quantity times the unit price for an additional service or product or charge.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getNet() {
            return net;
        }

        /**
         * <p>
         * Unique Device Identifiers associated with this line item.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getUdi() {
            return udi;
        }

        /**
         * <p>
         * Physical service site on the patient (limb, tooth, etc.).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getBodySite() {
            return bodySite;
        }

        /**
         * <p>
         * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getSubSite() {
            return subSite;
        }

        /**
         * <p>
         * The Encounters during which this Claim was created or to which the creation of this record is tightly associated.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getEncounter() {
            return encounter;
        }

        /**
         * <p>
         * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Detail}.
         */
        public List<Detail> getDetail() {
            return detail;
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
                    accept(sequence, "sequence", visitor);
                    accept(careTeamSequence, "careTeamSequence", visitor, PositiveInt.class);
                    accept(diagnosisSequence, "diagnosisSequence", visitor, PositiveInt.class);
                    accept(procedureSequence, "procedureSequence", visitor, PositiveInt.class);
                    accept(informationSequence, "informationSequence", visitor, PositiveInt.class);
                    accept(revenue, "revenue", visitor);
                    accept(category, "category", visitor);
                    accept(productOrService, "productOrService", visitor);
                    accept(modifier, "modifier", visitor, CodeableConcept.class);
                    accept(programCode, "programCode", visitor, CodeableConcept.class);
                    accept(serviced, "serviced", visitor, true);
                    accept(location, "location", visitor, true);
                    accept(quantity, "quantity", visitor);
                    accept(unitPrice, "unitPrice", visitor);
                    accept(factor, "factor", visitor);
                    accept(net, "net", visitor);
                    accept(udi, "udi", visitor, Reference.class);
                    accept(bodySite, "bodySite", visitor);
                    accept(subSite, "subSite", visitor, CodeableConcept.class);
                    accept(encounter, "encounter", visitor, Reference.class);
                    accept(detail, "detail", visitor, Detail.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(PositiveInt sequence, CodeableConcept productOrService) {
            return new Builder(sequence, productOrService);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final PositiveInt sequence;
            private final CodeableConcept productOrService;

            // optional
            private List<PositiveInt> careTeamSequence = new ArrayList<>();
            private List<PositiveInt> diagnosisSequence = new ArrayList<>();
            private List<PositiveInt> procedureSequence = new ArrayList<>();
            private List<PositiveInt> informationSequence = new ArrayList<>();
            private CodeableConcept revenue;
            private CodeableConcept category;
            private List<CodeableConcept> modifier = new ArrayList<>();
            private List<CodeableConcept> programCode = new ArrayList<>();
            private Element serviced;
            private Element location;
            private Quantity quantity;
            private Money unitPrice;
            private Decimal factor;
            private Money net;
            private List<Reference> udi = new ArrayList<>();
            private CodeableConcept bodySite;
            private List<CodeableConcept> subSite = new ArrayList<>();
            private List<Reference> encounter = new ArrayList<>();
            private List<Detail> detail = new ArrayList<>();

            private Builder(PositiveInt sequence, CodeableConcept productOrService) {
                super();
                this.sequence = sequence;
                this.productOrService = productOrService;
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
             * CareTeam members related to this service or product.
             * </p>
             * 
             * @param careTeamSequence
             *     Applicable careTeam members
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder careTeamSequence(PositiveInt... careTeamSequence) {
                for (PositiveInt value : careTeamSequence) {
                    this.careTeamSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * CareTeam members related to this service or product.
             * </p>
             * 
             * @param careTeamSequence
             *     Applicable careTeam members
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder careTeamSequence(Collection<PositiveInt> careTeamSequence) {
                this.careTeamSequence.addAll(careTeamSequence);
                return this;
            }

            /**
             * <p>
             * Diagnosis applicable for this service or product.
             * </p>
             * 
             * @param diagnosisSequence
             *     Applicable diagnoses
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder diagnosisSequence(PositiveInt... diagnosisSequence) {
                for (PositiveInt value : diagnosisSequence) {
                    this.diagnosisSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Diagnosis applicable for this service or product.
             * </p>
             * 
             * @param diagnosisSequence
             *     Applicable diagnoses
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder diagnosisSequence(Collection<PositiveInt> diagnosisSequence) {
                this.diagnosisSequence.addAll(diagnosisSequence);
                return this;
            }

            /**
             * <p>
             * Procedures applicable for this service or product.
             * </p>
             * 
             * @param procedureSequence
             *     Applicable procedures
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder procedureSequence(PositiveInt... procedureSequence) {
                for (PositiveInt value : procedureSequence) {
                    this.procedureSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Procedures applicable for this service or product.
             * </p>
             * 
             * @param procedureSequence
             *     Applicable procedures
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder procedureSequence(Collection<PositiveInt> procedureSequence) {
                this.procedureSequence.addAll(procedureSequence);
                return this;
            }

            /**
             * <p>
             * Exceptions, special conditions and supporting information applicable for this service or product.
             * </p>
             * 
             * @param informationSequence
             *     Applicable exception and supporting information
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder informationSequence(PositiveInt... informationSequence) {
                for (PositiveInt value : informationSequence) {
                    this.informationSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Exceptions, special conditions and supporting information applicable for this service or product.
             * </p>
             * 
             * @param informationSequence
             *     Applicable exception and supporting information
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder informationSequence(Collection<PositiveInt> informationSequence) {
                this.informationSequence.addAll(informationSequence);
                return this;
            }

            /**
             * <p>
             * The type of revenue or cost center providing the product and/or service.
             * </p>
             * 
             * @param revenue
             *     Revenue or cost center code
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder revenue(CodeableConcept revenue) {
                this.revenue = revenue;
                return this;
            }

            /**
             * <p>
             * Code to identify the general type of benefits under which products and services are provided.
             * </p>
             * 
             * @param category
             *     Benefit classification
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder category(CodeableConcept category) {
                this.category = category;
                return this;
            }

            /**
             * <p>
             * Item typification or modifiers codes to convey additional context for the product or service.
             * </p>
             * 
             * @param modifier
             *     Product or service billing modifiers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder modifier(CodeableConcept... modifier) {
                for (CodeableConcept value : modifier) {
                    this.modifier.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Item typification or modifiers codes to convey additional context for the product or service.
             * </p>
             * 
             * @param modifier
             *     Product or service billing modifiers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder modifier(Collection<CodeableConcept> modifier) {
                this.modifier.addAll(modifier);
                return this;
            }

            /**
             * <p>
             * Identifies the program under which this may be recovered.
             * </p>
             * 
             * @param programCode
             *     Program the product or service is provided under
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder programCode(CodeableConcept... programCode) {
                for (CodeableConcept value : programCode) {
                    this.programCode.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Identifies the program under which this may be recovered.
             * </p>
             * 
             * @param programCode
             *     Program the product or service is provided under
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder programCode(Collection<CodeableConcept> programCode) {
                this.programCode.addAll(programCode);
                return this;
            }

            /**
             * <p>
             * The date or dates when the service or product was supplied, performed or completed.
             * </p>
             * 
             * @param serviced
             *     Date or dates of service or product delivery
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder serviced(Element serviced) {
                this.serviced = serviced;
                return this;
            }

            /**
             * <p>
             * Where the product or service was provided.
             * </p>
             * 
             * @param location
             *     Place of service or where product was supplied
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder location(Element location) {
                this.location = location;
                return this;
            }

            /**
             * <p>
             * The number of repetitions of a service or product.
             * </p>
             * 
             * @param quantity
             *     Count of products or services
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder quantity(Quantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * <p>
             * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
             * for the details of the group.
             * </p>
             * 
             * @param unitPrice
             *     Fee, charge or cost per item
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder unitPrice(Money unitPrice) {
                this.unitPrice = unitPrice;
                return this;
            }

            /**
             * <p>
             * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
             * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
             * </p>
             * 
             * @param factor
             *     Price scaling factor
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder factor(Decimal factor) {
                this.factor = factor;
                return this;
            }

            /**
             * <p>
             * The quantity times the unit price for an additional service or product or charge.
             * </p>
             * 
             * @param net
             *     Total item cost
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder net(Money net) {
                this.net = net;
                return this;
            }

            /**
             * <p>
             * Unique Device Identifiers associated with this line item.
             * </p>
             * 
             * @param udi
             *     Unique device identifier
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder udi(Reference... udi) {
                for (Reference value : udi) {
                    this.udi.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Unique Device Identifiers associated with this line item.
             * </p>
             * 
             * @param udi
             *     Unique device identifier
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder udi(Collection<Reference> udi) {
                this.udi.addAll(udi);
                return this;
            }

            /**
             * <p>
             * Physical service site on the patient (limb, tooth, etc.).
             * </p>
             * 
             * @param bodySite
             *     Anatomical location
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder bodySite(CodeableConcept bodySite) {
                this.bodySite = bodySite;
                return this;
            }

            /**
             * <p>
             * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
             * </p>
             * 
             * @param subSite
             *     Anatomical sub-location
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder subSite(CodeableConcept... subSite) {
                for (CodeableConcept value : subSite) {
                    this.subSite.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
             * </p>
             * 
             * @param subSite
             *     Anatomical sub-location
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder subSite(Collection<CodeableConcept> subSite) {
                this.subSite.addAll(subSite);
                return this;
            }

            /**
             * <p>
             * The Encounters during which this Claim was created or to which the creation of this record is tightly associated.
             * </p>
             * 
             * @param encounter
             *     Encounters related to this billed item
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder encounter(Reference... encounter) {
                for (Reference value : encounter) {
                    this.encounter.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The Encounters during which this Claim was created or to which the creation of this record is tightly associated.
             * </p>
             * 
             * @param encounter
             *     Encounters related to this billed item
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder encounter(Collection<Reference> encounter) {
                this.encounter.addAll(encounter);
                return this;
            }

            /**
             * <p>
             * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
             * </p>
             * 
             * @param detail
             *     Product or service provided
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Detail... detail) {
                for (Detail value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
             * </p>
             * 
             * @param detail
             *     Product or service provided
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Collection<Detail> detail) {
                this.detail.addAll(detail);
                return this;
            }

            @Override
            public Item build() {
                return new Item(this);
            }

            private static Builder from(Item item) {
                Builder builder = new Builder(item.sequence, item.productOrService);
                builder.careTeamSequence.addAll(item.careTeamSequence);
                builder.diagnosisSequence.addAll(item.diagnosisSequence);
                builder.procedureSequence.addAll(item.procedureSequence);
                builder.informationSequence.addAll(item.informationSequence);
                builder.revenue = item.revenue;
                builder.category = item.category;
                builder.modifier.addAll(item.modifier);
                builder.programCode.addAll(item.programCode);
                builder.serviced = item.serviced;
                builder.location = item.location;
                builder.quantity = item.quantity;
                builder.unitPrice = item.unitPrice;
                builder.factor = item.factor;
                builder.net = item.net;
                builder.udi.addAll(item.udi);
                builder.bodySite = item.bodySite;
                builder.subSite.addAll(item.subSite);
                builder.encounter.addAll(item.encounter);
                builder.detail.addAll(item.detail);
                return builder;
            }
        }

        /**
         * <p>
         * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
         * </p>
         */
        public static class Detail extends BackboneElement {
            private final PositiveInt sequence;
            private final CodeableConcept revenue;
            private final CodeableConcept category;
            private final CodeableConcept productOrService;
            private final List<CodeableConcept> modifier;
            private final List<CodeableConcept> programCode;
            private final Quantity quantity;
            private final Money unitPrice;
            private final Decimal factor;
            private final Money net;
            private final List<Reference> udi;
            private final List<SubDetail> subDetail;

            private Detail(Builder builder) {
                super(builder);
                sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
                revenue = builder.revenue;
                category = builder.category;
                productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
                modifier = Collections.unmodifiableList(builder.modifier);
                programCode = Collections.unmodifiableList(builder.programCode);
                quantity = builder.quantity;
                unitPrice = builder.unitPrice;
                factor = builder.factor;
                net = builder.net;
                udi = Collections.unmodifiableList(builder.udi);
                subDetail = Collections.unmodifiableList(builder.subDetail);
            }

            /**
             * <p>
             * A number to uniquely identify item entries.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link PositiveInt}.
             */
            public PositiveInt getSequence() {
                return sequence;
            }

            /**
             * <p>
             * The type of revenue or cost center providing the product and/or service.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getRevenue() {
                return revenue;
            }

            /**
             * <p>
             * Code to identify the general type of benefits under which products and services are provided.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getCategory() {
                return category;
            }

            /**
             * <p>
             * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
             * product, service, drug or other billing code for the item.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getProductOrService() {
                return productOrService;
            }

            /**
             * <p>
             * Item typification or modifiers codes to convey additional context for the product or service.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getModifier() {
                return modifier;
            }

            /**
             * <p>
             * Identifies the program under which this may be recovered.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getProgramCode() {
                return programCode;
            }

            /**
             * <p>
             * The number of repetitions of a service or product.
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
             * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
             * for the details of the group.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Money}.
             */
            public Money getUnitPrice() {
                return unitPrice;
            }

            /**
             * <p>
             * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
             * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Decimal}.
             */
            public Decimal getFactor() {
                return factor;
            }

            /**
             * <p>
             * The quantity times the unit price for an additional service or product or charge.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Money}.
             */
            public Money getNet() {
                return net;
            }

            /**
             * <p>
             * Unique Device Identifiers associated with this line item.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Reference}.
             */
            public List<Reference> getUdi() {
                return udi;
            }

            /**
             * <p>
             * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link SubDetail}.
             */
            public List<SubDetail> getSubDetail() {
                return subDetail;
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
                        accept(sequence, "sequence", visitor);
                        accept(revenue, "revenue", visitor);
                        accept(category, "category", visitor);
                        accept(productOrService, "productOrService", visitor);
                        accept(modifier, "modifier", visitor, CodeableConcept.class);
                        accept(programCode, "programCode", visitor, CodeableConcept.class);
                        accept(quantity, "quantity", visitor);
                        accept(unitPrice, "unitPrice", visitor);
                        accept(factor, "factor", visitor);
                        accept(net, "net", visitor);
                        accept(udi, "udi", visitor, Reference.class);
                        accept(subDetail, "subDetail", visitor, SubDetail.class);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(PositiveInt sequence, CodeableConcept productOrService) {
                return new Builder(sequence, productOrService);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final PositiveInt sequence;
                private final CodeableConcept productOrService;

                // optional
                private CodeableConcept revenue;
                private CodeableConcept category;
                private List<CodeableConcept> modifier = new ArrayList<>();
                private List<CodeableConcept> programCode = new ArrayList<>();
                private Quantity quantity;
                private Money unitPrice;
                private Decimal factor;
                private Money net;
                private List<Reference> udi = new ArrayList<>();
                private List<SubDetail> subDetail = new ArrayList<>();

                private Builder(PositiveInt sequence, CodeableConcept productOrService) {
                    super();
                    this.sequence = sequence;
                    this.productOrService = productOrService;
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
                 * The type of revenue or cost center providing the product and/or service.
                 * </p>
                 * 
                 * @param revenue
                 *     Revenue or cost center code
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder revenue(CodeableConcept revenue) {
                    this.revenue = revenue;
                    return this;
                }

                /**
                 * <p>
                 * Code to identify the general type of benefits under which products and services are provided.
                 * </p>
                 * 
                 * @param category
                 *     Benefit classification
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder category(CodeableConcept category) {
                    this.category = category;
                    return this;
                }

                /**
                 * <p>
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * </p>
                 * 
                 * @param modifier
                 *     Service/Product billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder modifier(CodeableConcept... modifier) {
                    for (CodeableConcept value : modifier) {
                        this.modifier.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * </p>
                 * 
                 * @param modifier
                 *     Service/Product billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder modifier(Collection<CodeableConcept> modifier) {
                    this.modifier.addAll(modifier);
                    return this;
                }

                /**
                 * <p>
                 * Identifies the program under which this may be recovered.
                 * </p>
                 * 
                 * @param programCode
                 *     Program the product or service is provided under
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder programCode(CodeableConcept... programCode) {
                    for (CodeableConcept value : programCode) {
                        this.programCode.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Identifies the program under which this may be recovered.
                 * </p>
                 * 
                 * @param programCode
                 *     Program the product or service is provided under
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder programCode(Collection<CodeableConcept> programCode) {
                    this.programCode.addAll(programCode);
                    return this;
                }

                /**
                 * <p>
                 * The number of repetitions of a service or product.
                 * </p>
                 * 
                 * @param quantity
                 *     Count of products or services
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder quantity(Quantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * <p>
                 * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                 * for the details of the group.
                 * </p>
                 * 
                 * @param unitPrice
                 *     Fee, charge or cost per item
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder unitPrice(Money unitPrice) {
                    this.unitPrice = unitPrice;
                    return this;
                }

                /**
                 * <p>
                 * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                 * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                 * </p>
                 * 
                 * @param factor
                 *     Price scaling factor
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder factor(Decimal factor) {
                    this.factor = factor;
                    return this;
                }

                /**
                 * <p>
                 * The quantity times the unit price for an additional service or product or charge.
                 * </p>
                 * 
                 * @param net
                 *     Total item cost
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder net(Money net) {
                    this.net = net;
                    return this;
                }

                /**
                 * <p>
                 * Unique Device Identifiers associated with this line item.
                 * </p>
                 * 
                 * @param udi
                 *     Unique device identifier
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder udi(Reference... udi) {
                    for (Reference value : udi) {
                        this.udi.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Unique Device Identifiers associated with this line item.
                 * </p>
                 * 
                 * @param udi
                 *     Unique device identifier
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder udi(Collection<Reference> udi) {
                    this.udi.addAll(udi);
                    return this;
                }

                /**
                 * <p>
                 * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
                 * </p>
                 * 
                 * @param subDetail
                 *     Product or service provided
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder subDetail(SubDetail... subDetail) {
                    for (SubDetail value : subDetail) {
                        this.subDetail.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
                 * </p>
                 * 
                 * @param subDetail
                 *     Product or service provided
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder subDetail(Collection<SubDetail> subDetail) {
                    this.subDetail.addAll(subDetail);
                    return this;
                }

                @Override
                public Detail build() {
                    return new Detail(this);
                }

                private static Builder from(Detail detail) {
                    Builder builder = new Builder(detail.sequence, detail.productOrService);
                    builder.revenue = detail.revenue;
                    builder.category = detail.category;
                    builder.modifier.addAll(detail.modifier);
                    builder.programCode.addAll(detail.programCode);
                    builder.quantity = detail.quantity;
                    builder.unitPrice = detail.unitPrice;
                    builder.factor = detail.factor;
                    builder.net = detail.net;
                    builder.udi.addAll(detail.udi);
                    builder.subDetail.addAll(detail.subDetail);
                    return builder;
                }
            }

            /**
             * <p>
             * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
             * </p>
             */
            public static class SubDetail extends BackboneElement {
                private final PositiveInt sequence;
                private final CodeableConcept revenue;
                private final CodeableConcept category;
                private final CodeableConcept productOrService;
                private final List<CodeableConcept> modifier;
                private final List<CodeableConcept> programCode;
                private final Quantity quantity;
                private final Money unitPrice;
                private final Decimal factor;
                private final Money net;
                private final List<Reference> udi;

                private SubDetail(Builder builder) {
                    super(builder);
                    sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
                    revenue = builder.revenue;
                    category = builder.category;
                    productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
                    modifier = Collections.unmodifiableList(builder.modifier);
                    programCode = Collections.unmodifiableList(builder.programCode);
                    quantity = builder.quantity;
                    unitPrice = builder.unitPrice;
                    factor = builder.factor;
                    net = builder.net;
                    udi = Collections.unmodifiableList(builder.udi);
                }

                /**
                 * <p>
                 * A number to uniquely identify item entries.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link PositiveInt}.
                 */
                public PositiveInt getSequence() {
                    return sequence;
                }

                /**
                 * <p>
                 * The type of revenue or cost center providing the product and/or service.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getRevenue() {
                    return revenue;
                }

                /**
                 * <p>
                 * Code to identify the general type of benefits under which products and services are provided.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getCategory() {
                    return category;
                }

                /**
                 * <p>
                 * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
                 * product, service, drug or other billing code for the item.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getProductOrService() {
                    return productOrService;
                }

                /**
                 * <p>
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link CodeableConcept}.
                 */
                public List<CodeableConcept> getModifier() {
                    return modifier;
                }

                /**
                 * <p>
                 * Identifies the program under which this may be recovered.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link CodeableConcept}.
                 */
                public List<CodeableConcept> getProgramCode() {
                    return programCode;
                }

                /**
                 * <p>
                 * The number of repetitions of a service or product.
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
                 * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                 * for the details of the group.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Money}.
                 */
                public Money getUnitPrice() {
                    return unitPrice;
                }

                /**
                 * <p>
                 * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                 * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Decimal}.
                 */
                public Decimal getFactor() {
                    return factor;
                }

                /**
                 * <p>
                 * The quantity times the unit price for an additional service or product or charge.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Money}.
                 */
                public Money getNet() {
                    return net;
                }

                /**
                 * <p>
                 * Unique Device Identifiers associated with this line item.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link Reference}.
                 */
                public List<Reference> getUdi() {
                    return udi;
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
                            accept(sequence, "sequence", visitor);
                            accept(revenue, "revenue", visitor);
                            accept(category, "category", visitor);
                            accept(productOrService, "productOrService", visitor);
                            accept(modifier, "modifier", visitor, CodeableConcept.class);
                            accept(programCode, "programCode", visitor, CodeableConcept.class);
                            accept(quantity, "quantity", visitor);
                            accept(unitPrice, "unitPrice", visitor);
                            accept(factor, "factor", visitor);
                            accept(net, "net", visitor);
                            accept(udi, "udi", visitor, Reference.class);
                        }
                        visitor.visitEnd(elementName, this);
                        visitor.postVisit(this);
                    }
                }

                @Override
                public Builder toBuilder() {
                    return Builder.from(this);
                }

                public static Builder builder(PositiveInt sequence, CodeableConcept productOrService) {
                    return new Builder(sequence, productOrService);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final PositiveInt sequence;
                    private final CodeableConcept productOrService;

                    // optional
                    private CodeableConcept revenue;
                    private CodeableConcept category;
                    private List<CodeableConcept> modifier = new ArrayList<>();
                    private List<CodeableConcept> programCode = new ArrayList<>();
                    private Quantity quantity;
                    private Money unitPrice;
                    private Decimal factor;
                    private Money net;
                    private List<Reference> udi = new ArrayList<>();

                    private Builder(PositiveInt sequence, CodeableConcept productOrService) {
                        super();
                        this.sequence = sequence;
                        this.productOrService = productOrService;
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
                     * The type of revenue or cost center providing the product and/or service.
                     * </p>
                     * 
                     * @param revenue
                     *     Revenue or cost center code
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder revenue(CodeableConcept revenue) {
                        this.revenue = revenue;
                        return this;
                    }

                    /**
                     * <p>
                     * Code to identify the general type of benefits under which products and services are provided.
                     * </p>
                     * 
                     * @param category
                     *     Benefit classification
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder category(CodeableConcept category) {
                        this.category = category;
                        return this;
                    }

                    /**
                     * <p>
                     * Item typification or modifiers codes to convey additional context for the product or service.
                     * </p>
                     * 
                     * @param modifier
                     *     Service/Product billing modifiers
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder modifier(CodeableConcept... modifier) {
                        for (CodeableConcept value : modifier) {
                            this.modifier.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * Item typification or modifiers codes to convey additional context for the product or service.
                     * </p>
                     * 
                     * @param modifier
                     *     Service/Product billing modifiers
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder modifier(Collection<CodeableConcept> modifier) {
                        this.modifier.addAll(modifier);
                        return this;
                    }

                    /**
                     * <p>
                     * Identifies the program under which this may be recovered.
                     * </p>
                     * 
                     * @param programCode
                     *     Program the product or service is provided under
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder programCode(CodeableConcept... programCode) {
                        for (CodeableConcept value : programCode) {
                            this.programCode.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * Identifies the program under which this may be recovered.
                     * </p>
                     * 
                     * @param programCode
                     *     Program the product or service is provided under
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder programCode(Collection<CodeableConcept> programCode) {
                        this.programCode.addAll(programCode);
                        return this;
                    }

                    /**
                     * <p>
                     * The number of repetitions of a service or product.
                     * </p>
                     * 
                     * @param quantity
                     *     Count of products or services
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder quantity(Quantity quantity) {
                        this.quantity = quantity;
                        return this;
                    }

                    /**
                     * <p>
                     * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                     * for the details of the group.
                     * </p>
                     * 
                     * @param unitPrice
                     *     Fee, charge or cost per item
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder unitPrice(Money unitPrice) {
                        this.unitPrice = unitPrice;
                        return this;
                    }

                    /**
                     * <p>
                     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                     * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                     * </p>
                     * 
                     * @param factor
                     *     Price scaling factor
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder factor(Decimal factor) {
                        this.factor = factor;
                        return this;
                    }

                    /**
                     * <p>
                     * The quantity times the unit price for an additional service or product or charge.
                     * </p>
                     * 
                     * @param net
                     *     Total item cost
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder net(Money net) {
                        this.net = net;
                        return this;
                    }

                    /**
                     * <p>
                     * Unique Device Identifiers associated with this line item.
                     * </p>
                     * 
                     * @param udi
                     *     Unique device identifier
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder udi(Reference... udi) {
                        for (Reference value : udi) {
                            this.udi.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * Unique Device Identifiers associated with this line item.
                     * </p>
                     * 
                     * @param udi
                     *     Unique device identifier
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder udi(Collection<Reference> udi) {
                        this.udi.addAll(udi);
                        return this;
                    }

                    @Override
                    public SubDetail build() {
                        return new SubDetail(this);
                    }

                    private static Builder from(SubDetail subDetail) {
                        Builder builder = new Builder(subDetail.sequence, subDetail.productOrService);
                        builder.revenue = subDetail.revenue;
                        builder.category = subDetail.category;
                        builder.modifier.addAll(subDetail.modifier);
                        builder.programCode.addAll(subDetail.programCode);
                        builder.quantity = subDetail.quantity;
                        builder.unitPrice = subDetail.unitPrice;
                        builder.factor = subDetail.factor;
                        builder.net = subDetail.net;
                        builder.udi.addAll(subDetail.udi);
                        return builder;
                    }
                }
            }
        }
    }
}
