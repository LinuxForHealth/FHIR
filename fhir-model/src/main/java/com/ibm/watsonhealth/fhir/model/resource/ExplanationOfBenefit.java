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

import com.ibm.watsonhealth.fhir.model.type.Address;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.ExplanationOfBenefitStatus;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.NoteType;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RemittanceOutcome;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Use;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account 
 * balance information, for informing the subscriber of the benefits provided.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ExplanationOfBenefit extends DomainResource {
    private final List<Identifier> identifier;
    private final ExplanationOfBenefitStatus status;
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
    private final CodeableConcept fundsReserveRequested;
    private final CodeableConcept fundsReserve;
    private final List<Related> related;
    private final Reference prescription;
    private final Reference originalPrescription;
    private final Payee payee;
    private final Reference referral;
    private final Reference facility;
    private final Reference claim;
    private final Reference claimResponse;
    private final RemittanceOutcome outcome;
    private final String disposition;
    private final List<String> preAuthRef;
    private final List<Period> preAuthRefPeriod;
    private final List<CareTeam> careTeam;
    private final List<SupportingInfo> supportingInfo;
    private final List<Diagnosis> diagnosis;
    private final List<Procedure> procedure;
    private final PositiveInt precedence;
    private final List<Insurance> insurance;
    private final Accident accident;
    private final List<Item> item;
    private final List<AddItem> addItem;
    private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;
    private final List<Total> total;
    private final Payment payment;
    private final CodeableConcept formCode;
    private final Attachment form;
    private final List<ProcessNote> processNote;
    private final Period benefitPeriod;
    private final List<BenefitBalance> benefitBalance;

    private ExplanationOfBenefit(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.type = ValidationSupport.requireNonNull(builder.type, "type");
        this.subType = builder.subType;
        this.use = ValidationSupport.requireNonNull(builder.use, "use");
        this.patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        this.billablePeriod = builder.billablePeriod;
        this.created = ValidationSupport.requireNonNull(builder.created, "created");
        this.enterer = builder.enterer;
        this.insurer = ValidationSupport.requireNonNull(builder.insurer, "insurer");
        this.provider = ValidationSupport.requireNonNull(builder.provider, "provider");
        this.priority = builder.priority;
        this.fundsReserveRequested = builder.fundsReserveRequested;
        this.fundsReserve = builder.fundsReserve;
        this.related = builder.related;
        this.prescription = builder.prescription;
        this.originalPrescription = builder.originalPrescription;
        this.payee = builder.payee;
        this.referral = builder.referral;
        this.facility = builder.facility;
        this.claim = builder.claim;
        this.claimResponse = builder.claimResponse;
        this.outcome = ValidationSupport.requireNonNull(builder.outcome, "outcome");
        this.disposition = builder.disposition;
        this.preAuthRef = builder.preAuthRef;
        this.preAuthRefPeriod = builder.preAuthRefPeriod;
        this.careTeam = builder.careTeam;
        this.supportingInfo = builder.supportingInfo;
        this.diagnosis = builder.diagnosis;
        this.procedure = builder.procedure;
        this.precedence = builder.precedence;
        this.insurance = ValidationSupport.requireNonEmpty(builder.insurance, "insurance");
        this.accident = builder.accident;
        this.item = builder.item;
        this.addItem = builder.addItem;
        this.adjudication = builder.adjudication;
        this.total = builder.total;
        this.payment = builder.payment;
        this.formCode = builder.formCode;
        this.form = builder.form;
        this.processNote = builder.processNote;
        this.benefitPeriod = builder.benefitPeriod;
        this.benefitBalance = builder.benefitBalance;
    }

    /**
     * <p>
     * A unique identifier assigned to this explanation of benefit.
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
     *     An immutable object of type {@link ExplanationOfBenefitStatus}.
     */
    public ExplanationOfBenefitStatus getStatus() {
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
     * actual for forecast reimbursement is sought.
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
     * The party responsible for authorization, adjudication and reimbursement.
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
    public CodeableConcept getFundsReserveRequested() {
        return fundsReserveRequested;
    }

    /**
     * <p>
     * A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and 
     * for whom.
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
     * The business identifier for the instance of the adjudication request: claim predetermination or preauthorization.
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
     * The business identifier for the instance of the adjudication response: claim, predetermination or preauthorization 
     * response.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getClaimResponse() {
        return claimResponse;
    }

    /**
     * <p>
     * The outcome of the claim, predetermination, or preauthorization processing.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link RemittanceOutcome}.
     */
    public RemittanceOutcome getOutcome() {
        return outcome;
    }

    /**
     * <p>
     * A human readable description of the status of the adjudication.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * <p>
     * Reference from the Insurer which is used in later communications which refers to this adjudication.
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
     * The timeframe during which the supplied preauthorization reference may be quoted on claims to obtain the adjudication 
     * as provided.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Period}.
     */
    public List<Period> getPreAuthRefPeriod() {
        return preAuthRefPeriod;
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
     * This indicates the relative order of a series of EOBs related to different coverages for the same suite of services.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PositiveInt}.
     */
    public PositiveInt getPrecedence() {
        return precedence;
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
     * Details of a accident which resulted in injuries which required the products and services listed in the claim.
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
     * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
     * groups of sub-details.
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
     * The first-tier service adjudications for payor added product or service lines.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link AddItem}.
     */
    public List<AddItem> getAddItem() {
        return addItem;
    }

    /**
     * <p>
     * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Adjudication}.
     */
    public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
        return adjudication;
    }

    /**
     * <p>
     * Categorized monetary totals for the adjudication.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Total}.
     */
    public List<Total> getTotal() {
        return total;
    }

    /**
     * <p>
     * Payment details for the adjudication of the claim.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Payment}.
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * <p>
     * A code for the form to be used for printing the content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getFormCode() {
        return formCode;
    }

    /**
     * <p>
     * The actual form, by reference or inclusion, for printing the content or an EOB.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Attachment}.
     */
    public Attachment getForm() {
        return form;
    }

    /**
     * <p>
     * A note that describes or explains adjudication results in a human readable form.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ProcessNote}.
     */
    public List<ProcessNote> getProcessNote() {
        return processNote;
    }

    /**
     * <p>
     * The term of the benefits documented in this response.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getBenefitPeriod() {
        return benefitPeriod;
    }

    /**
     * <p>
     * Balance by Benefit Category.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link BenefitBalance}.
     */
    public List<BenefitBalance> getBenefitBalance() {
        return benefitBalance;
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
                accept(fundsReserveRequested, "fundsReserveRequested", visitor);
                accept(fundsReserve, "fundsReserve", visitor);
                accept(related, "related", visitor, Related.class);
                accept(prescription, "prescription", visitor);
                accept(originalPrescription, "originalPrescription", visitor);
                accept(payee, "payee", visitor);
                accept(referral, "referral", visitor);
                accept(facility, "facility", visitor);
                accept(claim, "claim", visitor);
                accept(claimResponse, "claimResponse", visitor);
                accept(outcome, "outcome", visitor);
                accept(disposition, "disposition", visitor);
                accept(preAuthRef, "preAuthRef", visitor, String.class);
                accept(preAuthRefPeriod, "preAuthRefPeriod", visitor, Period.class);
                accept(careTeam, "careTeam", visitor, CareTeam.class);
                accept(supportingInfo, "supportingInfo", visitor, SupportingInfo.class);
                accept(diagnosis, "diagnosis", visitor, Diagnosis.class);
                accept(procedure, "procedure", visitor, Procedure.class);
                accept(precedence, "precedence", visitor);
                accept(insurance, "insurance", visitor, Insurance.class);
                accept(accident, "accident", visitor);
                accept(item, "item", visitor, Item.class);
                accept(addItem, "addItem", visitor, AddItem.class);
                accept(adjudication, "adjudication", visitor, ExplanationOfBenefit.Item.Adjudication.class);
                accept(total, "total", visitor, Total.class);
                accept(payment, "payment", visitor);
                accept(formCode, "formCode", visitor);
                accept(form, "form", visitor);
                accept(processNote, "processNote", visitor, ProcessNote.class);
                accept(benefitPeriod, "benefitPeriod", visitor);
                accept(benefitBalance, "benefitBalance", visitor, BenefitBalance.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, type, use, patient, created, insurer, provider, outcome, insurance);
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
        builder.priority = priority;
        builder.fundsReserveRequested = fundsReserveRequested;
        builder.fundsReserve = fundsReserve;
        builder.related.addAll(related);
        builder.prescription = prescription;
        builder.originalPrescription = originalPrescription;
        builder.payee = payee;
        builder.referral = referral;
        builder.facility = facility;
        builder.claim = claim;
        builder.claimResponse = claimResponse;
        builder.disposition = disposition;
        builder.preAuthRef.addAll(preAuthRef);
        builder.preAuthRefPeriod.addAll(preAuthRefPeriod);
        builder.careTeam.addAll(careTeam);
        builder.supportingInfo.addAll(supportingInfo);
        builder.diagnosis.addAll(diagnosis);
        builder.procedure.addAll(procedure);
        builder.precedence = precedence;
        builder.accident = accident;
        builder.item.addAll(item);
        builder.addItem.addAll(addItem);
        builder.adjudication.addAll(adjudication);
        builder.total.addAll(total);
        builder.payment = payment;
        builder.formCode = formCode;
        builder.form = form;
        builder.processNote.addAll(processNote);
        builder.benefitPeriod = benefitPeriod;
        builder.benefitBalance.addAll(benefitBalance);
        return builder;
    }

    public static Builder builder(ExplanationOfBenefitStatus status, CodeableConcept type, Use use, Reference patient, DateTime created, Reference insurer, Reference provider, RemittanceOutcome outcome, List<Insurance> insurance) {
        return new Builder(status, type, use, patient, created, insurer, provider, outcome, insurance);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ExplanationOfBenefitStatus status;
        private final CodeableConcept type;
        private final Use use;
        private final Reference patient;
        private final DateTime created;
        private final Reference insurer;
        private final Reference provider;
        private final RemittanceOutcome outcome;
        private final List<Insurance> insurance;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept subType;
        private Period billablePeriod;
        private Reference enterer;
        private CodeableConcept priority;
        private CodeableConcept fundsReserveRequested;
        private CodeableConcept fundsReserve;
        private List<Related> related = new ArrayList<>();
        private Reference prescription;
        private Reference originalPrescription;
        private Payee payee;
        private Reference referral;
        private Reference facility;
        private Reference claim;
        private Reference claimResponse;
        private String disposition;
        private List<String> preAuthRef = new ArrayList<>();
        private List<Period> preAuthRefPeriod = new ArrayList<>();
        private List<CareTeam> careTeam = new ArrayList<>();
        private List<SupportingInfo> supportingInfo = new ArrayList<>();
        private List<Diagnosis> diagnosis = new ArrayList<>();
        private List<Procedure> procedure = new ArrayList<>();
        private PositiveInt precedence;
        private Accident accident;
        private List<Item> item = new ArrayList<>();
        private List<AddItem> addItem = new ArrayList<>();
        private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();
        private List<Total> total = new ArrayList<>();
        private Payment payment;
        private CodeableConcept formCode;
        private Attachment form;
        private List<ProcessNote> processNote = new ArrayList<>();
        private Period benefitPeriod;
        private List<BenefitBalance> benefitBalance = new ArrayList<>();

        private Builder(ExplanationOfBenefitStatus status, CodeableConcept type, Use use, Reference patient, DateTime created, Reference insurer, Reference provider, RemittanceOutcome outcome, List<Insurance> insurance) {
            super();
            this.status = status;
            this.type = type;
            this.use = use;
            this.patient = patient;
            this.created = created;
            this.insurer = insurer;
            this.provider = provider;
            this.outcome = outcome;
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
         * A unique identifier assigned to this explanation of benefit.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for the resource
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
         * A unique identifier assigned to this explanation of benefit.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for the resource
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
         * The provider-required urgency of processing the request. Typical values include: stat, routine deferred.
         * </p>
         * 
         * @param priority
         *     Desired processing urgency
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder priority(CodeableConcept priority) {
            this.priority = priority;
            return this;
        }

        /**
         * <p>
         * A code to indicate whether and for whom funds are to be reserved for future claims.
         * </p>
         * 
         * @param fundsReserveRequested
         *     For whom to reserve funds
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder fundsReserveRequested(CodeableConcept fundsReserveRequested) {
            this.fundsReserveRequested = fundsReserveRequested;
            return this;
        }

        /**
         * <p>
         * A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and 
         * for whom.
         * </p>
         * 
         * @param fundsReserve
         *     Funds reserved status
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
         *     Prescription authorizing services or products
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
         *     Original prescription if superceded by fulfiller
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
         *     Treatment Referral
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
         *     Servicing Facility
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
         * The business identifier for the instance of the adjudication request: claim predetermination or preauthorization.
         * </p>
         * 
         * @param claim
         *     Claim reference
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
         * The business identifier for the instance of the adjudication response: claim, predetermination or preauthorization 
         * response.
         * </p>
         * 
         * @param claimResponse
         *     Claim response reference
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder claimResponse(Reference claimResponse) {
            this.claimResponse = claimResponse;
            return this;
        }

        /**
         * <p>
         * A human readable description of the status of the adjudication.
         * </p>
         * 
         * @param disposition
         *     Disposition Message
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder disposition(String disposition) {
            this.disposition = disposition;
            return this;
        }

        /**
         * <p>
         * Reference from the Insurer which is used in later communications which refers to this adjudication.
         * </p>
         * 
         * @param preAuthRef
         *     Preauthorization reference
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
         * Reference from the Insurer which is used in later communications which refers to this adjudication.
         * </p>
         * 
         * @param preAuthRef
         *     Preauthorization reference
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
         * The timeframe during which the supplied preauthorization reference may be quoted on claims to obtain the adjudication 
         * as provided.
         * </p>
         * 
         * @param preAuthRefPeriod
         *     Preauthorization in-effect period
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder preAuthRefPeriod(Period... preAuthRefPeriod) {
            for (Period value : preAuthRefPeriod) {
                this.preAuthRefPeriod.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The timeframe during which the supplied preauthorization reference may be quoted on claims to obtain the adjudication 
         * as provided.
         * </p>
         * 
         * @param preAuthRefPeriod
         *     Preauthorization in-effect period
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder preAuthRefPeriod(Collection<Period> preAuthRefPeriod) {
            this.preAuthRefPeriod.addAll(preAuthRefPeriod);
            return this;
        }

        /**
         * <p>
         * The members of the team who provided the products and services.
         * </p>
         * 
         * @param careTeam
         *     Care Team members
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
         *     Care Team members
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
         * This indicates the relative order of a series of EOBs related to different coverages for the same suite of services.
         * </p>
         * 
         * @param precedence
         *     Precedence (primary, secondary, etc.)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder precedence(PositiveInt precedence) {
            this.precedence = precedence;
            return this;
        }

        /**
         * <p>
         * Details of a accident which resulted in injuries which required the products and services listed in the claim.
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
         * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
         * groups of sub-details.
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
         * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
         * groups of sub-details.
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
         * The first-tier service adjudications for payor added product or service lines.
         * </p>
         * 
         * @param addItem
         *     Insurer added line items
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder addItem(AddItem... addItem) {
            for (AddItem value : addItem) {
                this.addItem.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The first-tier service adjudications for payor added product or service lines.
         * </p>
         * 
         * @param addItem
         *     Insurer added line items
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder addItem(Collection<AddItem> addItem) {
            this.addItem.addAll(addItem);
            return this;
        }

        /**
         * <p>
         * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
         * </p>
         * 
         * @param adjudication
         *     Header-level adjudication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
            for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                this.adjudication.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
         * </p>
         * 
         * @param adjudication
         *     Header-level adjudication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
            this.adjudication.addAll(adjudication);
            return this;
        }

        /**
         * <p>
         * Categorized monetary totals for the adjudication.
         * </p>
         * 
         * @param total
         *     Adjudication totals
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder total(Total... total) {
            for (Total value : total) {
                this.total.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Categorized monetary totals for the adjudication.
         * </p>
         * 
         * @param total
         *     Adjudication totals
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder total(Collection<Total> total) {
            this.total.addAll(total);
            return this;
        }

        /**
         * <p>
         * Payment details for the adjudication of the claim.
         * </p>
         * 
         * @param payment
         *     Payment Details
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder payment(Payment payment) {
            this.payment = payment;
            return this;
        }

        /**
         * <p>
         * A code for the form to be used for printing the content.
         * </p>
         * 
         * @param formCode
         *     Printed form identifier
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder formCode(CodeableConcept formCode) {
            this.formCode = formCode;
            return this;
        }

        /**
         * <p>
         * The actual form, by reference or inclusion, for printing the content or an EOB.
         * </p>
         * 
         * @param form
         *     Printed reference or actual form
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder form(Attachment form) {
            this.form = form;
            return this;
        }

        /**
         * <p>
         * A note that describes or explains adjudication results in a human readable form.
         * </p>
         * 
         * @param processNote
         *     Note concerning adjudication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder processNote(ProcessNote... processNote) {
            for (ProcessNote value : processNote) {
                this.processNote.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A note that describes or explains adjudication results in a human readable form.
         * </p>
         * 
         * @param processNote
         *     Note concerning adjudication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder processNote(Collection<ProcessNote> processNote) {
            this.processNote.addAll(processNote);
            return this;
        }

        /**
         * <p>
         * The term of the benefits documented in this response.
         * </p>
         * 
         * @param benefitPeriod
         *     When the benefits are applicable
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder benefitPeriod(Period benefitPeriod) {
            this.benefitPeriod = benefitPeriod;
            return this;
        }

        /**
         * <p>
         * Balance by Benefit Category.
         * </p>
         * 
         * @param benefitBalance
         *     Balance by Benefit Category
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder benefitBalance(BenefitBalance... benefitBalance) {
            for (BenefitBalance value : benefitBalance) {
                this.benefitBalance.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Balance by Benefit Category.
         * </p>
         * 
         * @param benefitBalance
         *     Balance by Benefit Category
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder benefitBalance(Collection<BenefitBalance> benefitBalance) {
            this.benefitBalance.addAll(benefitBalance);
            return this;
        }

        @Override
        public ExplanationOfBenefit build() {
            return new ExplanationOfBenefit(this);
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
            this.claim = builder.claim;
            this.relationship = builder.relationship;
            this.reference = builder.reference;
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
                builder.id = related.id;
                builder.extension.addAll(related.extension);
                builder.modifierExtension.addAll(related.modifierExtension);
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
            this.type = builder.type;
            this.party = builder.party;
        }

        /**
         * <p>
         * Type of Party to be reimbursed: Subscriber, provider, other.
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

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private CodeableConcept type;
            private Reference party;

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
             * Type of Party to be reimbursed: Subscriber, provider, other.
             * </p>
             * 
             * @param type
             *     Category of recipient
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
                Builder builder = new Builder();
                builder.id = payee.id;
                builder.extension.addAll(payee.extension);
                builder.modifierExtension.addAll(payee.modifierExtension);
                builder.type = payee.type;
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
            this.sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            this.provider = ValidationSupport.requireNonNull(builder.provider, "provider");
            this.responsible = builder.responsible;
            this.role = builder.role;
            this.qualification = builder.qualification;
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
                builder.id = careTeam.id;
                builder.extension.addAll(careTeam.extension);
                builder.modifierExtension.addAll(careTeam.modifierExtension);
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
        private final Coding reason;

        private SupportingInfo(Builder builder) {
            super(builder);
            this.sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            this.category = ValidationSupport.requireNonNull(builder.category, "category");
            this.code = builder.code;
            this.timing = ValidationSupport.choiceElement(builder.timing, "timing", Date.class, Period.class);
            this.value = ValidationSupport.choiceElement(builder.value, "value", Boolean.class, String.class, Quantity.class, Attachment.class, Reference.class);
            this.reason = builder.reason;
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
         *     An immutable object of type {@link Coding}.
         */
        public Coding getReason() {
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
            private Coding reason;

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
            public Builder reason(Coding reason) {
                this.reason = reason;
                return this;
            }

            @Override
            public SupportingInfo build() {
                return new SupportingInfo(this);
            }

            private static Builder from(SupportingInfo supportingInfo) {
                Builder builder = new Builder(supportingInfo.sequence, supportingInfo.category);
                builder.id = supportingInfo.id;
                builder.extension.addAll(supportingInfo.extension);
                builder.modifierExtension.addAll(supportingInfo.modifierExtension);
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
            this.sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            this.diagnosis = ValidationSupport.requireChoiceElement(builder.diagnosis, "diagnosis", CodeableConcept.class, Reference.class);
            this.type = builder.type;
            this.onAdmission = builder.onAdmission;
            this.packageCode = builder.packageCode;
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
                builder.id = diagnosis.id;
                builder.extension.addAll(diagnosis.extension);
                builder.modifierExtension.addAll(diagnosis.modifierExtension);
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
            this.sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            this.type = builder.type;
            this.date = builder.date;
            this.procedure = ValidationSupport.requireChoiceElement(builder.procedure, "procedure", CodeableConcept.class, Reference.class);
            this.udi = builder.udi;
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
                builder.id = procedure.id;
                builder.extension.addAll(procedure.extension);
                builder.modifierExtension.addAll(procedure.modifierExtension);
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
        private final Boolean focal;
        private final Reference coverage;
        private final List<String> preAuthRef;

        private Insurance(Builder builder) {
            super(builder);
            this.focal = ValidationSupport.requireNonNull(builder.focal, "focal");
            this.coverage = ValidationSupport.requireNonNull(builder.coverage, "coverage");
            this.preAuthRef = builder.preAuthRef;
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

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(focal, "focal", visitor);
                    accept(coverage, "coverage", visitor);
                    accept(preAuthRef, "preAuthRef", visitor, String.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Boolean focal, Reference coverage) {
            return new Builder(focal, coverage);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Boolean focal;
            private final Reference coverage;

            // optional
            private List<String> preAuthRef = new ArrayList<>();

            private Builder(Boolean focal, Reference coverage) {
                super();
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

            @Override
            public Insurance build() {
                return new Insurance(this);
            }

            private static Builder from(Insurance insurance) {
                Builder builder = new Builder(insurance.focal, insurance.coverage);
                builder.id = insurance.id;
                builder.extension.addAll(insurance.extension);
                builder.modifierExtension.addAll(insurance.modifierExtension);
                builder.preAuthRef.addAll(insurance.preAuthRef);
                return builder;
            }
        }
    }

    /**
     * <p>
     * Details of a accident which resulted in injuries which required the products and services listed in the claim.
     * </p>
     */
    public static class Accident extends BackboneElement {
        private final Date date;
        private final CodeableConcept type;
        private final Element location;

        private Accident(Builder builder) {
            super(builder);
            this.date = builder.date;
            this.type = builder.type;
            this.location = ValidationSupport.choiceElement(builder.location, "location", Address.class, Reference.class);
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

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private Date date;
            private CodeableConcept type;
            private Element location;

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
             * Date of an accident event related to the products and services contained in the claim.
             * </p>
             * 
             * @param date
             *     When the incident occurred
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder date(Date date) {
                this.date = date;
                return this;
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
                Builder builder = new Builder();
                builder.id = accident.id;
                builder.extension.addAll(accident.extension);
                builder.modifierExtension.addAll(accident.modifierExtension);
                builder.date = accident.date;
                builder.type = accident.type;
                builder.location = accident.location;
                return builder;
            }
        }
    }

    /**
     * <p>
     * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
     * groups of sub-details.
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
        private final List<PositiveInt> noteNumber;
        private final List<Adjudication> adjudication;
        private final List<Detail> detail;

        private Item(Builder builder) {
            super(builder);
            this.sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            this.careTeamSequence = builder.careTeamSequence;
            this.diagnosisSequence = builder.diagnosisSequence;
            this.procedureSequence = builder.procedureSequence;
            this.informationSequence = builder.informationSequence;
            this.revenue = builder.revenue;
            this.category = builder.category;
            this.productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
            this.modifier = builder.modifier;
            this.programCode = builder.programCode;
            this.serviced = ValidationSupport.choiceElement(builder.serviced, "serviced", Date.class, Period.class);
            this.location = ValidationSupport.choiceElement(builder.location, "location", CodeableConcept.class, Address.class, Reference.class);
            this.quantity = builder.quantity;
            this.unitPrice = builder.unitPrice;
            this.factor = builder.factor;
            this.net = builder.net;
            this.udi = builder.udi;
            this.bodySite = builder.bodySite;
            this.subSite = builder.subSite;
            this.encounter = builder.encounter;
            this.noteNumber = builder.noteNumber;
            this.adjudication = builder.adjudication;
            this.detail = builder.detail;
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
         * Care team members related to this service or product.
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
         * Diagnoses applicable for this service or product.
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
         * A billed item may include goods or services provided in multiple encounters.
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
         * The numbers associated with notes below which apply to the adjudication of this item.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getNoteNumber() {
            return noteNumber;
        }

        /**
         * <p>
         * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a 
         * simple product or service then this is the result of the adjudication of this item.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Adjudication}.
         */
        public List<Adjudication> getAdjudication() {
            return adjudication;
        }

        /**
         * <p>
         * Second-tier of goods and services.
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
                    accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                    accept(adjudication, "adjudication", visitor, Adjudication.class);
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
            private List<PositiveInt> noteNumber = new ArrayList<>();
            private List<Adjudication> adjudication = new ArrayList<>();
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
             * Care team members related to this service or product.
             * </p>
             * 
             * @param careTeamSequence
             *     Applicable care team members
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
             * Care team members related to this service or product.
             * </p>
             * 
             * @param careTeamSequence
             *     Applicable care team members
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
             * Diagnoses applicable for this service or product.
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
             * Diagnoses applicable for this service or product.
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
             * A billed item may include goods or services provided in multiple encounters.
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
             * A billed item may include goods or services provided in multiple encounters.
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
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder noteNumber(PositiveInt... noteNumber) {
                for (PositiveInt value : noteNumber) {
                    this.noteNumber.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                this.noteNumber.addAll(noteNumber);
                return this;
            }

            /**
             * <p>
             * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a 
             * simple product or service then this is the result of the adjudication of this item.
             * </p>
             * 
             * @param adjudication
             *     Adjudication details
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder adjudication(Adjudication... adjudication) {
                for (Adjudication value : adjudication) {
                    this.adjudication.add(value);
                }
                return this;
            }

            /**
             * <p>
             * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a 
             * simple product or service then this is the result of the adjudication of this item.
             * </p>
             * 
             * @param adjudication
             *     Adjudication details
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder adjudication(Collection<Adjudication> adjudication) {
                this.adjudication.addAll(adjudication);
                return this;
            }

            /**
             * <p>
             * Second-tier of goods and services.
             * </p>
             * 
             * @param detail
             *     Additional items
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
             * Second-tier of goods and services.
             * </p>
             * 
             * @param detail
             *     Additional items
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
                builder.id = item.id;
                builder.extension.addAll(item.extension);
                builder.modifierExtension.addAll(item.modifierExtension);
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
                builder.noteNumber.addAll(item.noteNumber);
                builder.adjudication.addAll(item.adjudication);
                builder.detail.addAll(item.detail);
                return builder;
            }
        }

        /**
         * <p>
         * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a 
         * simple product or service then this is the result of the adjudication of this item.
         * </p>
         */
        public static class Adjudication extends BackboneElement {
            private final CodeableConcept category;
            private final CodeableConcept reason;
            private final Money amount;
            private final Decimal value;

            private Adjudication(Builder builder) {
                super(builder);
                this.category = ValidationSupport.requireNonNull(builder.category, "category");
                this.reason = builder.reason;
                this.amount = builder.amount;
                this.value = builder.value;
            }

            /**
             * <p>
             * A code to indicate the information type of this adjudication record. Information types may include: the value 
             * submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible 
             * for in-aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.
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
             * A code supporting the understanding of the adjudication result and explaining variance from expected amount.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getReason() {
                return reason;
            }

            /**
             * <p>
             * Monetary amount associated with the category.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Money}.
             */
            public Money getAmount() {
                return amount;
            }

            /**
             * <p>
             * A non-monetary value associated with the category. Mutually exclusive to the amount element above.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Decimal}.
             */
            public Decimal getValue() {
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
                        accept(category, "category", visitor);
                        accept(reason, "reason", visitor);
                        accept(amount, "amount", visitor);
                        accept(value, "value", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(CodeableConcept category) {
                return new Builder(category);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept category;

                // optional
                private CodeableConcept reason;
                private Money amount;
                private Decimal value;

                private Builder(CodeableConcept category) {
                    super();
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
                 * A code supporting the understanding of the adjudication result and explaining variance from expected amount.
                 * </p>
                 * 
                 * @param reason
                 *     Explanation of adjudication outcome
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder reason(CodeableConcept reason) {
                    this.reason = reason;
                    return this;
                }

                /**
                 * <p>
                 * Monetary amount associated with the category.
                 * </p>
                 * 
                 * @param amount
                 *     Monetary amount
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder amount(Money amount) {
                    this.amount = amount;
                    return this;
                }

                /**
                 * <p>
                 * A non-monetary value associated with the category. Mutually exclusive to the amount element above.
                 * </p>
                 * 
                 * @param value
                 *     Non-monitary value
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder value(Decimal value) {
                    this.value = value;
                    return this;
                }

                @Override
                public Adjudication build() {
                    return new Adjudication(this);
                }

                private static Builder from(Adjudication adjudication) {
                    Builder builder = new Builder(adjudication.category);
                    builder.id = adjudication.id;
                    builder.extension.addAll(adjudication.extension);
                    builder.modifierExtension.addAll(adjudication.modifierExtension);
                    builder.reason = adjudication.reason;
                    builder.amount = adjudication.amount;
                    builder.value = adjudication.value;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * Second-tier of goods and services.
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
            private final List<PositiveInt> noteNumber;
            private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;
            private final List<SubDetail> subDetail;

            private Detail(Builder builder) {
                super(builder);
                this.sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
                this.revenue = builder.revenue;
                this.category = builder.category;
                this.productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
                this.modifier = builder.modifier;
                this.programCode = builder.programCode;
                this.quantity = builder.quantity;
                this.unitPrice = builder.unitPrice;
                this.factor = builder.factor;
                this.net = builder.net;
                this.udi = builder.udi;
                this.noteNumber = builder.noteNumber;
                this.adjudication = builder.adjudication;
                this.subDetail = builder.subDetail;
            }

            /**
             * <p>
             * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
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
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link PositiveInt}.
             */
            public List<PositiveInt> getNoteNumber() {
                return noteNumber;
            }

            /**
             * <p>
             * The adjudication results.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Adjudication}.
             */
            public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
                return adjudication;
            }

            /**
             * <p>
             * Third-tier of goods and services.
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
                        accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                        accept(adjudication, "adjudication", visitor, ExplanationOfBenefit.Item.Adjudication.class);
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
                private List<PositiveInt> noteNumber = new ArrayList<>();
                private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();
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
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder noteNumber(PositiveInt... noteNumber) {
                    for (PositiveInt value : noteNumber) {
                        this.noteNumber.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                    this.noteNumber.addAll(noteNumber);
                    return this;
                }

                /**
                 * <p>
                 * The adjudication results.
                 * </p>
                 * 
                 * @param adjudication
                 *     Detail level adjudication details
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
                    for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                        this.adjudication.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The adjudication results.
                 * </p>
                 * 
                 * @param adjudication
                 *     Detail level adjudication details
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
                    this.adjudication.addAll(adjudication);
                    return this;
                }

                /**
                 * <p>
                 * Third-tier of goods and services.
                 * </p>
                 * 
                 * @param subDetail
                 *     Additional items
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
                 * Third-tier of goods and services.
                 * </p>
                 * 
                 * @param subDetail
                 *     Additional items
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
                    builder.id = detail.id;
                    builder.extension.addAll(detail.extension);
                    builder.modifierExtension.addAll(detail.modifierExtension);
                    builder.revenue = detail.revenue;
                    builder.category = detail.category;
                    builder.modifier.addAll(detail.modifier);
                    builder.programCode.addAll(detail.programCode);
                    builder.quantity = detail.quantity;
                    builder.unitPrice = detail.unitPrice;
                    builder.factor = detail.factor;
                    builder.net = detail.net;
                    builder.udi.addAll(detail.udi);
                    builder.noteNumber.addAll(detail.noteNumber);
                    builder.adjudication.addAll(detail.adjudication);
                    builder.subDetail.addAll(detail.subDetail);
                    return builder;
                }
            }

            /**
             * <p>
             * Third-tier of goods and services.
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
                private final List<PositiveInt> noteNumber;
                private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;

                private SubDetail(Builder builder) {
                    super(builder);
                    this.sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
                    this.revenue = builder.revenue;
                    this.category = builder.category;
                    this.productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
                    this.modifier = builder.modifier;
                    this.programCode = builder.programCode;
                    this.quantity = builder.quantity;
                    this.unitPrice = builder.unitPrice;
                    this.factor = builder.factor;
                    this.net = builder.net;
                    this.udi = builder.udi;
                    this.noteNumber = builder.noteNumber;
                    this.adjudication = builder.adjudication;
                }

                /**
                 * <p>
                 * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
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
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link PositiveInt}.
                 */
                public List<PositiveInt> getNoteNumber() {
                    return noteNumber;
                }

                /**
                 * <p>
                 * The adjudication results.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link Adjudication}.
                 */
                public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
                    return adjudication;
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
                            accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                            accept(adjudication, "adjudication", visitor, ExplanationOfBenefit.Item.Adjudication.class);
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
                    private List<PositiveInt> noteNumber = new ArrayList<>();
                    private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();

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
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * </p>
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder noteNumber(PositiveInt... noteNumber) {
                        for (PositiveInt value : noteNumber) {
                            this.noteNumber.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * </p>
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                        this.noteNumber.addAll(noteNumber);
                        return this;
                    }

                    /**
                     * <p>
                     * The adjudication results.
                     * </p>
                     * 
                     * @param adjudication
                     *     Subdetail level adjudication details
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
                        for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                            this.adjudication.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * The adjudication results.
                     * </p>
                     * 
                     * @param adjudication
                     *     Subdetail level adjudication details
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
                        this.adjudication.addAll(adjudication);
                        return this;
                    }

                    @Override
                    public SubDetail build() {
                        return new SubDetail(this);
                    }

                    private static Builder from(SubDetail subDetail) {
                        Builder builder = new Builder(subDetail.sequence, subDetail.productOrService);
                        builder.id = subDetail.id;
                        builder.extension.addAll(subDetail.extension);
                        builder.modifierExtension.addAll(subDetail.modifierExtension);
                        builder.revenue = subDetail.revenue;
                        builder.category = subDetail.category;
                        builder.modifier.addAll(subDetail.modifier);
                        builder.programCode.addAll(subDetail.programCode);
                        builder.quantity = subDetail.quantity;
                        builder.unitPrice = subDetail.unitPrice;
                        builder.factor = subDetail.factor;
                        builder.net = subDetail.net;
                        builder.udi.addAll(subDetail.udi);
                        builder.noteNumber.addAll(subDetail.noteNumber);
                        builder.adjudication.addAll(subDetail.adjudication);
                        return builder;
                    }
                }
            }
        }
    }

    /**
     * <p>
     * The first-tier service adjudications for payor added product or service lines.
     * </p>
     */
    public static class AddItem extends BackboneElement {
        private final List<PositiveInt> itemSequence;
        private final List<PositiveInt> detailSequence;
        private final List<PositiveInt> subDetailSequence;
        private final List<Reference> provider;
        private final CodeableConcept productOrService;
        private final List<CodeableConcept> modifier;
        private final List<CodeableConcept> programCode;
        private final Element serviced;
        private final Element location;
        private final Quantity quantity;
        private final Money unitPrice;
        private final Decimal factor;
        private final Money net;
        private final CodeableConcept bodySite;
        private final List<CodeableConcept> subSite;
        private final List<PositiveInt> noteNumber;
        private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;
        private final List<Detail> detail;

        private AddItem(Builder builder) {
            super(builder);
            this.itemSequence = builder.itemSequence;
            this.detailSequence = builder.detailSequence;
            this.subDetailSequence = builder.subDetailSequence;
            this.provider = builder.provider;
            this.productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
            this.modifier = builder.modifier;
            this.programCode = builder.programCode;
            this.serviced = ValidationSupport.choiceElement(builder.serviced, "serviced", Date.class, Period.class);
            this.location = ValidationSupport.choiceElement(builder.location, "location", CodeableConcept.class, Address.class, Reference.class);
            this.quantity = builder.quantity;
            this.unitPrice = builder.unitPrice;
            this.factor = builder.factor;
            this.net = builder.net;
            this.bodySite = builder.bodySite;
            this.subSite = builder.subSite;
            this.noteNumber = builder.noteNumber;
            this.adjudication = builder.adjudication;
            this.detail = builder.detail;
        }

        /**
         * <p>
         * Claim items which this service line is intended to replace.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getItemSequence() {
            return itemSequence;
        }

        /**
         * <p>
         * The sequence number of the details within the claim item which this line is intended to replace.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getDetailSequence() {
            return detailSequence;
        }

        /**
         * <p>
         * The sequence number of the sub-details woithin the details within the claim item which this line is intended to 
         * replace.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getSubDetailSequence() {
            return subDetailSequence;
        }

        /**
         * <p>
         * The providers who are authorized for the services rendered to the patient.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getProvider() {
            return provider;
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
         * The numbers associated with notes below which apply to the adjudication of this item.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getNoteNumber() {
            return noteNumber;
        }

        /**
         * <p>
         * The adjudication results.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Adjudication}.
         */
        public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
            return adjudication;
        }

        /**
         * <p>
         * The second-tier service adjudications for payor added services.
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
                    accept(itemSequence, "itemSequence", visitor, PositiveInt.class);
                    accept(detailSequence, "detailSequence", visitor, PositiveInt.class);
                    accept(subDetailSequence, "subDetailSequence", visitor, PositiveInt.class);
                    accept(provider, "provider", visitor, Reference.class);
                    accept(productOrService, "productOrService", visitor);
                    accept(modifier, "modifier", visitor, CodeableConcept.class);
                    accept(programCode, "programCode", visitor, CodeableConcept.class);
                    accept(serviced, "serviced", visitor, true);
                    accept(location, "location", visitor, true);
                    accept(quantity, "quantity", visitor);
                    accept(unitPrice, "unitPrice", visitor);
                    accept(factor, "factor", visitor);
                    accept(net, "net", visitor);
                    accept(bodySite, "bodySite", visitor);
                    accept(subSite, "subSite", visitor, CodeableConcept.class);
                    accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                    accept(adjudication, "adjudication", visitor, ExplanationOfBenefit.Item.Adjudication.class);
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

        public static Builder builder(CodeableConcept productOrService) {
            return new Builder(productOrService);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept productOrService;

            // optional
            private List<PositiveInt> itemSequence = new ArrayList<>();
            private List<PositiveInt> detailSequence = new ArrayList<>();
            private List<PositiveInt> subDetailSequence = new ArrayList<>();
            private List<Reference> provider = new ArrayList<>();
            private List<CodeableConcept> modifier = new ArrayList<>();
            private List<CodeableConcept> programCode = new ArrayList<>();
            private Element serviced;
            private Element location;
            private Quantity quantity;
            private Money unitPrice;
            private Decimal factor;
            private Money net;
            private CodeableConcept bodySite;
            private List<CodeableConcept> subSite = new ArrayList<>();
            private List<PositiveInt> noteNumber = new ArrayList<>();
            private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();
            private List<Detail> detail = new ArrayList<>();

            private Builder(CodeableConcept productOrService) {
                super();
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
             * Claim items which this service line is intended to replace.
             * </p>
             * 
             * @param itemSequence
             *     Item sequence number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder itemSequence(PositiveInt... itemSequence) {
                for (PositiveInt value : itemSequence) {
                    this.itemSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Claim items which this service line is intended to replace.
             * </p>
             * 
             * @param itemSequence
             *     Item sequence number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder itemSequence(Collection<PositiveInt> itemSequence) {
                this.itemSequence.addAll(itemSequence);
                return this;
            }

            /**
             * <p>
             * The sequence number of the details within the claim item which this line is intended to replace.
             * </p>
             * 
             * @param detailSequence
             *     Detail sequence number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detailSequence(PositiveInt... detailSequence) {
                for (PositiveInt value : detailSequence) {
                    this.detailSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The sequence number of the details within the claim item which this line is intended to replace.
             * </p>
             * 
             * @param detailSequence
             *     Detail sequence number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detailSequence(Collection<PositiveInt> detailSequence) {
                this.detailSequence.addAll(detailSequence);
                return this;
            }

            /**
             * <p>
             * The sequence number of the sub-details woithin the details within the claim item which this line is intended to 
             * replace.
             * </p>
             * 
             * @param subDetailSequence
             *     Subdetail sequence number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder subDetailSequence(PositiveInt... subDetailSequence) {
                for (PositiveInt value : subDetailSequence) {
                    this.subDetailSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The sequence number of the sub-details woithin the details within the claim item which this line is intended to 
             * replace.
             * </p>
             * 
             * @param subDetailSequence
             *     Subdetail sequence number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder subDetailSequence(Collection<PositiveInt> subDetailSequence) {
                this.subDetailSequence.addAll(subDetailSequence);
                return this;
            }

            /**
             * <p>
             * The providers who are authorized for the services rendered to the patient.
             * </p>
             * 
             * @param provider
             *     Authorized providers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder provider(Reference... provider) {
                for (Reference value : provider) {
                    this.provider.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The providers who are authorized for the services rendered to the patient.
             * </p>
             * 
             * @param provider
             *     Authorized providers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder provider(Collection<Reference> provider) {
                this.provider.addAll(provider);
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
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder noteNumber(PositiveInt... noteNumber) {
                for (PositiveInt value : noteNumber) {
                    this.noteNumber.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                this.noteNumber.addAll(noteNumber);
                return this;
            }

            /**
             * <p>
             * The adjudication results.
             * </p>
             * 
             * @param adjudication
             *     Added items adjudication
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
                for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                    this.adjudication.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The adjudication results.
             * </p>
             * 
             * @param adjudication
             *     Added items adjudication
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
                this.adjudication.addAll(adjudication);
                return this;
            }

            /**
             * <p>
             * The second-tier service adjudications for payor added services.
             * </p>
             * 
             * @param detail
             *     Insurer added line items
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
             * The second-tier service adjudications for payor added services.
             * </p>
             * 
             * @param detail
             *     Insurer added line items
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Collection<Detail> detail) {
                this.detail.addAll(detail);
                return this;
            }

            @Override
            public AddItem build() {
                return new AddItem(this);
            }

            private static Builder from(AddItem addItem) {
                Builder builder = new Builder(addItem.productOrService);
                builder.id = addItem.id;
                builder.extension.addAll(addItem.extension);
                builder.modifierExtension.addAll(addItem.modifierExtension);
                builder.itemSequence.addAll(addItem.itemSequence);
                builder.detailSequence.addAll(addItem.detailSequence);
                builder.subDetailSequence.addAll(addItem.subDetailSequence);
                builder.provider.addAll(addItem.provider);
                builder.modifier.addAll(addItem.modifier);
                builder.programCode.addAll(addItem.programCode);
                builder.serviced = addItem.serviced;
                builder.location = addItem.location;
                builder.quantity = addItem.quantity;
                builder.unitPrice = addItem.unitPrice;
                builder.factor = addItem.factor;
                builder.net = addItem.net;
                builder.bodySite = addItem.bodySite;
                builder.subSite.addAll(addItem.subSite);
                builder.noteNumber.addAll(addItem.noteNumber);
                builder.adjudication.addAll(addItem.adjudication);
                builder.detail.addAll(addItem.detail);
                return builder;
            }
        }

        /**
         * <p>
         * The second-tier service adjudications for payor added services.
         * </p>
         */
        public static class Detail extends BackboneElement {
            private final CodeableConcept productOrService;
            private final List<CodeableConcept> modifier;
            private final Quantity quantity;
            private final Money unitPrice;
            private final Decimal factor;
            private final Money net;
            private final List<PositiveInt> noteNumber;
            private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;
            private final List<SubDetail> subDetail;

            private Detail(Builder builder) {
                super(builder);
                this.productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
                this.modifier = builder.modifier;
                this.quantity = builder.quantity;
                this.unitPrice = builder.unitPrice;
                this.factor = builder.factor;
                this.net = builder.net;
                this.noteNumber = builder.noteNumber;
                this.adjudication = builder.adjudication;
                this.subDetail = builder.subDetail;
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
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link PositiveInt}.
             */
            public List<PositiveInt> getNoteNumber() {
                return noteNumber;
            }

            /**
             * <p>
             * The adjudication results.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Adjudication}.
             */
            public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
                return adjudication;
            }

            /**
             * <p>
             * The third-tier service adjudications for payor added services.
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
                        accept(productOrService, "productOrService", visitor);
                        accept(modifier, "modifier", visitor, CodeableConcept.class);
                        accept(quantity, "quantity", visitor);
                        accept(unitPrice, "unitPrice", visitor);
                        accept(factor, "factor", visitor);
                        accept(net, "net", visitor);
                        accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                        accept(adjudication, "adjudication", visitor, ExplanationOfBenefit.Item.Adjudication.class);
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

            public static Builder builder(CodeableConcept productOrService) {
                return new Builder(productOrService);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept productOrService;

                // optional
                private List<CodeableConcept> modifier = new ArrayList<>();
                private Quantity quantity;
                private Money unitPrice;
                private Decimal factor;
                private Money net;
                private List<PositiveInt> noteNumber = new ArrayList<>();
                private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();
                private List<SubDetail> subDetail = new ArrayList<>();

                private Builder(CodeableConcept productOrService) {
                    super();
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
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder noteNumber(PositiveInt... noteNumber) {
                    for (PositiveInt value : noteNumber) {
                        this.noteNumber.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                    this.noteNumber.addAll(noteNumber);
                    return this;
                }

                /**
                 * <p>
                 * The adjudication results.
                 * </p>
                 * 
                 * @param adjudication
                 *     Added items adjudication
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
                    for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                        this.adjudication.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The adjudication results.
                 * </p>
                 * 
                 * @param adjudication
                 *     Added items adjudication
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
                    this.adjudication.addAll(adjudication);
                    return this;
                }

                /**
                 * <p>
                 * The third-tier service adjudications for payor added services.
                 * </p>
                 * 
                 * @param subDetail
                 *     Insurer added line items
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
                 * The third-tier service adjudications for payor added services.
                 * </p>
                 * 
                 * @param subDetail
                 *     Insurer added line items
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
                    Builder builder = new Builder(detail.productOrService);
                    builder.id = detail.id;
                    builder.extension.addAll(detail.extension);
                    builder.modifierExtension.addAll(detail.modifierExtension);
                    builder.modifier.addAll(detail.modifier);
                    builder.quantity = detail.quantity;
                    builder.unitPrice = detail.unitPrice;
                    builder.factor = detail.factor;
                    builder.net = detail.net;
                    builder.noteNumber.addAll(detail.noteNumber);
                    builder.adjudication.addAll(detail.adjudication);
                    builder.subDetail.addAll(detail.subDetail);
                    return builder;
                }
            }

            /**
             * <p>
             * The third-tier service adjudications for payor added services.
             * </p>
             */
            public static class SubDetail extends BackboneElement {
                private final CodeableConcept productOrService;
                private final List<CodeableConcept> modifier;
                private final Quantity quantity;
                private final Money unitPrice;
                private final Decimal factor;
                private final Money net;
                private final List<PositiveInt> noteNumber;
                private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;

                private SubDetail(Builder builder) {
                    super(builder);
                    this.productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
                    this.modifier = builder.modifier;
                    this.quantity = builder.quantity;
                    this.unitPrice = builder.unitPrice;
                    this.factor = builder.factor;
                    this.net = builder.net;
                    this.noteNumber = builder.noteNumber;
                    this.adjudication = builder.adjudication;
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
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link PositiveInt}.
                 */
                public List<PositiveInt> getNoteNumber() {
                    return noteNumber;
                }

                /**
                 * <p>
                 * The adjudication results.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link Adjudication}.
                 */
                public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
                    return adjudication;
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
                            accept(productOrService, "productOrService", visitor);
                            accept(modifier, "modifier", visitor, CodeableConcept.class);
                            accept(quantity, "quantity", visitor);
                            accept(unitPrice, "unitPrice", visitor);
                            accept(factor, "factor", visitor);
                            accept(net, "net", visitor);
                            accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                            accept(adjudication, "adjudication", visitor, ExplanationOfBenefit.Item.Adjudication.class);
                        }
                        visitor.visitEnd(elementName, this);
                        visitor.postVisit(this);
                    }
                }

                @Override
                public Builder toBuilder() {
                    return Builder.from(this);
                }

                public static Builder builder(CodeableConcept productOrService) {
                    return new Builder(productOrService);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final CodeableConcept productOrService;

                    // optional
                    private List<CodeableConcept> modifier = new ArrayList<>();
                    private Quantity quantity;
                    private Money unitPrice;
                    private Decimal factor;
                    private Money net;
                    private List<PositiveInt> noteNumber = new ArrayList<>();
                    private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();

                    private Builder(CodeableConcept productOrService) {
                        super();
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
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * </p>
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder noteNumber(PositiveInt... noteNumber) {
                        for (PositiveInt value : noteNumber) {
                            this.noteNumber.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * </p>
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                        this.noteNumber.addAll(noteNumber);
                        return this;
                    }

                    /**
                     * <p>
                     * The adjudication results.
                     * </p>
                     * 
                     * @param adjudication
                     *     Added items adjudication
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
                        for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                            this.adjudication.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * The adjudication results.
                     * </p>
                     * 
                     * @param adjudication
                     *     Added items adjudication
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
                        this.adjudication.addAll(adjudication);
                        return this;
                    }

                    @Override
                    public SubDetail build() {
                        return new SubDetail(this);
                    }

                    private static Builder from(SubDetail subDetail) {
                        Builder builder = new Builder(subDetail.productOrService);
                        builder.id = subDetail.id;
                        builder.extension.addAll(subDetail.extension);
                        builder.modifierExtension.addAll(subDetail.modifierExtension);
                        builder.modifier.addAll(subDetail.modifier);
                        builder.quantity = subDetail.quantity;
                        builder.unitPrice = subDetail.unitPrice;
                        builder.factor = subDetail.factor;
                        builder.net = subDetail.net;
                        builder.noteNumber.addAll(subDetail.noteNumber);
                        builder.adjudication.addAll(subDetail.adjudication);
                        return builder;
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Categorized monetary totals for the adjudication.
     * </p>
     */
    public static class Total extends BackboneElement {
        private final CodeableConcept category;
        private final Money amount;

        private Total(Builder builder) {
            super(builder);
            this.category = ValidationSupport.requireNonNull(builder.category, "category");
            this.amount = ValidationSupport.requireNonNull(builder.amount, "amount");
        }

        /**
         * <p>
         * A code to indicate the information type of this adjudication record. Information types may include: the value 
         * submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible 
         * for in aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.
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
         * Monetary total amount associated with the category.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getAmount() {
            return amount;
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
                    accept(category, "category", visitor);
                    accept(amount, "amount", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept category, Money amount) {
            return new Builder(category, amount);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept category;
            private final Money amount;

            private Builder(CodeableConcept category, Money amount) {
                super();
                this.category = category;
                this.amount = amount;
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
            public Total build() {
                return new Total(this);
            }

            private static Builder from(Total total) {
                Builder builder = new Builder(total.category, total.amount);
                builder.id = total.id;
                builder.extension.addAll(total.extension);
                builder.modifierExtension.addAll(total.modifierExtension);
                return builder;
            }
        }
    }

    /**
     * <p>
     * Payment details for the adjudication of the claim.
     * </p>
     */
    public static class Payment extends BackboneElement {
        private final CodeableConcept type;
        private final Money adjustment;
        private final CodeableConcept adjustmentReason;
        private final Date date;
        private final Money amount;
        private final Identifier identifier;

        private Payment(Builder builder) {
            super(builder);
            this.type = builder.type;
            this.adjustment = builder.adjustment;
            this.adjustmentReason = builder.adjustmentReason;
            this.date = builder.date;
            this.amount = builder.amount;
            this.identifier = builder.identifier;
        }

        /**
         * <p>
         * Whether this represents partial or complete payment of the benefits payable.
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
         * Total amount of all adjustments to this payment included in this transaction which are not related to this claim's 
         * adjudication.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getAdjustment() {
            return adjustment;
        }

        /**
         * <p>
         * Reason for the payment adjustment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getAdjustmentReason() {
            return adjustmentReason;
        }

        /**
         * <p>
         * Estimated date the payment will be issued or the actual issue date of payment.
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
         * Benefits payable less any payment adjustment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getAmount() {
            return amount;
        }

        /**
         * <p>
         * Issuer's unique identifier for the payment instrument.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getIdentifier() {
            return identifier;
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
                    accept(adjustment, "adjustment", visitor);
                    accept(adjustmentReason, "adjustmentReason", visitor);
                    accept(date, "date", visitor);
                    accept(amount, "amount", visitor);
                    accept(identifier, "identifier", visitor);
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
            private Money adjustment;
            private CodeableConcept adjustmentReason;
            private Date date;
            private Money amount;
            private Identifier identifier;

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
             * Whether this represents partial or complete payment of the benefits payable.
             * </p>
             * 
             * @param type
             *     Partial or complete payment
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
             * Total amount of all adjustments to this payment included in this transaction which are not related to this claim's 
             * adjudication.
             * </p>
             * 
             * @param adjustment
             *     Payment adjustment for non-claim issues
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder adjustment(Money adjustment) {
                this.adjustment = adjustment;
                return this;
            }

            /**
             * <p>
             * Reason for the payment adjustment.
             * </p>
             * 
             * @param adjustmentReason
             *     Explanation for the variance
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder adjustmentReason(CodeableConcept adjustmentReason) {
                this.adjustmentReason = adjustmentReason;
                return this;
            }

            /**
             * <p>
             * Estimated date the payment will be issued or the actual issue date of payment.
             * </p>
             * 
             * @param date
             *     Expected date of payment
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder date(Date date) {
                this.date = date;
                return this;
            }

            /**
             * <p>
             * Benefits payable less any payment adjustment.
             * </p>
             * 
             * @param amount
             *     Payable amount after adjustment
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder amount(Money amount) {
                this.amount = amount;
                return this;
            }

            /**
             * <p>
             * Issuer's unique identifier for the payment instrument.
             * </p>
             * 
             * @param identifier
             *     Business identifier for the payment
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            @Override
            public Payment build() {
                return new Payment(this);
            }

            private static Builder from(Payment payment) {
                Builder builder = new Builder();
                builder.id = payment.id;
                builder.extension.addAll(payment.extension);
                builder.modifierExtension.addAll(payment.modifierExtension);
                builder.type = payment.type;
                builder.adjustment = payment.adjustment;
                builder.adjustmentReason = payment.adjustmentReason;
                builder.date = payment.date;
                builder.amount = payment.amount;
                builder.identifier = payment.identifier;
                return builder;
            }
        }
    }

    /**
     * <p>
     * A note that describes or explains adjudication results in a human readable form.
     * </p>
     */
    public static class ProcessNote extends BackboneElement {
        private final PositiveInt number;
        private final NoteType type;
        private final String text;
        private final CodeableConcept language;

        private ProcessNote(Builder builder) {
            super(builder);
            this.number = builder.number;
            this.type = builder.type;
            this.text = builder.text;
            this.language = builder.language;
        }

        /**
         * <p>
         * A number to uniquely identify a note entry.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getNumber() {
            return number;
        }

        /**
         * <p>
         * The business purpose of the note text.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link NoteType}.
         */
        public NoteType getType() {
            return type;
        }

        /**
         * <p>
         * The explanation or description associated with the processing.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getText() {
            return text;
        }

        /**
         * <p>
         * A code to define the language used in the text of the note.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getLanguage() {
            return language;
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
                    accept(number, "number", visitor);
                    accept(type, "type", visitor);
                    accept(text, "text", visitor);
                    accept(language, "language", visitor);
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
            private PositiveInt number;
            private NoteType type;
            private String text;
            private CodeableConcept language;

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
             * A number to uniquely identify a note entry.
             * </p>
             * 
             * @param number
             *     Note instance identifier
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder number(PositiveInt number) {
                this.number = number;
                return this;
            }

            /**
             * <p>
             * The business purpose of the note text.
             * </p>
             * 
             * @param type
             *     display | print | printoper
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(NoteType type) {
                this.type = type;
                return this;
            }

            /**
             * <p>
             * The explanation or description associated with the processing.
             * </p>
             * 
             * @param text
             *     Note explanatory text
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * <p>
             * A code to define the language used in the text of the note.
             * </p>
             * 
             * @param language
             *     Language of the text
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder language(CodeableConcept language) {
                this.language = language;
                return this;
            }

            @Override
            public ProcessNote build() {
                return new ProcessNote(this);
            }

            private static Builder from(ProcessNote processNote) {
                Builder builder = new Builder();
                builder.id = processNote.id;
                builder.extension.addAll(processNote.extension);
                builder.modifierExtension.addAll(processNote.modifierExtension);
                builder.number = processNote.number;
                builder.type = processNote.type;
                builder.text = processNote.text;
                builder.language = processNote.language;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Balance by Benefit Category.
     * </p>
     */
    public static class BenefitBalance extends BackboneElement {
        private final CodeableConcept category;
        private final Boolean excluded;
        private final String name;
        private final String description;
        private final CodeableConcept network;
        private final CodeableConcept unit;
        private final CodeableConcept term;
        private final List<Financial> financial;

        private BenefitBalance(Builder builder) {
            super(builder);
            this.category = ValidationSupport.requireNonNull(builder.category, "category");
            this.excluded = builder.excluded;
            this.name = builder.name;
            this.description = builder.description;
            this.network = builder.network;
            this.unit = builder.unit;
            this.term = builder.term;
            this.financial = builder.financial;
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
         * True if the indicated class of service is excluded from the plan, missing or False indicates the product or service is 
         * included in the coverage.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getExcluded() {
            return excluded;
        }

        /**
         * <p>
         * A short name or tag for the benefit.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getName() {
            return name;
        }

        /**
         * <p>
         * A richer description of the benefit or services covered.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDescription() {
            return description;
        }

        /**
         * <p>
         * Is a flag to indicate whether the benefits refer to in-network providers or out-of-network providers.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getNetwork() {
            return network;
        }

        /**
         * <p>
         * Indicates if the benefits apply to an individual or to the family.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getUnit() {
            return unit;
        }

        /**
         * <p>
         * The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual visits'.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getTerm() {
            return term;
        }

        /**
         * <p>
         * Benefits Used to date.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Financial}.
         */
        public List<Financial> getFinancial() {
            return financial;
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
                    accept(category, "category", visitor);
                    accept(excluded, "excluded", visitor);
                    accept(name, "name", visitor);
                    accept(description, "description", visitor);
                    accept(network, "network", visitor);
                    accept(unit, "unit", visitor);
                    accept(term, "term", visitor);
                    accept(financial, "financial", visitor, Financial.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept category) {
            return new Builder(category);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept category;

            // optional
            private Boolean excluded;
            private String name;
            private String description;
            private CodeableConcept network;
            private CodeableConcept unit;
            private CodeableConcept term;
            private List<Financial> financial = new ArrayList<>();

            private Builder(CodeableConcept category) {
                super();
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
             * True if the indicated class of service is excluded from the plan, missing or False indicates the product or service is 
             * included in the coverage.
             * </p>
             * 
             * @param excluded
             *     Excluded from the plan
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder excluded(Boolean excluded) {
                this.excluded = excluded;
                return this;
            }

            /**
             * <p>
             * A short name or tag for the benefit.
             * </p>
             * 
             * @param name
             *     Short name for the benefit
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * <p>
             * A richer description of the benefit or services covered.
             * </p>
             * 
             * @param description
             *     Description of the benefit or services covered
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * <p>
             * Is a flag to indicate whether the benefits refer to in-network providers or out-of-network providers.
             * </p>
             * 
             * @param network
             *     In or out of network
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder network(CodeableConcept network) {
                this.network = network;
                return this;
            }

            /**
             * <p>
             * Indicates if the benefits apply to an individual or to the family.
             * </p>
             * 
             * @param unit
             *     Individual or family
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder unit(CodeableConcept unit) {
                this.unit = unit;
                return this;
            }

            /**
             * <p>
             * The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual visits'.
             * </p>
             * 
             * @param term
             *     Annual or lifetime
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder term(CodeableConcept term) {
                this.term = term;
                return this;
            }

            /**
             * <p>
             * Benefits Used to date.
             * </p>
             * 
             * @param financial
             *     Benefit Summary
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder financial(Financial... financial) {
                for (Financial value : financial) {
                    this.financial.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Benefits Used to date.
             * </p>
             * 
             * @param financial
             *     Benefit Summary
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder financial(Collection<Financial> financial) {
                this.financial.addAll(financial);
                return this;
            }

            @Override
            public BenefitBalance build() {
                return new BenefitBalance(this);
            }

            private static Builder from(BenefitBalance benefitBalance) {
                Builder builder = new Builder(benefitBalance.category);
                builder.id = benefitBalance.id;
                builder.extension.addAll(benefitBalance.extension);
                builder.modifierExtension.addAll(benefitBalance.modifierExtension);
                builder.excluded = benefitBalance.excluded;
                builder.name = benefitBalance.name;
                builder.description = benefitBalance.description;
                builder.network = benefitBalance.network;
                builder.unit = benefitBalance.unit;
                builder.term = benefitBalance.term;
                builder.financial.addAll(benefitBalance.financial);
                return builder;
            }
        }

        /**
         * <p>
         * Benefits Used to date.
         * </p>
         */
        public static class Financial extends BackboneElement {
            private final CodeableConcept type;
            private final Element allowed;
            private final Element used;

            private Financial(Builder builder) {
                super(builder);
                this.type = ValidationSupport.requireNonNull(builder.type, "type");
                this.allowed = ValidationSupport.choiceElement(builder.allowed, "allowed", UnsignedInt.class, String.class, Money.class);
                this.used = ValidationSupport.choiceElement(builder.used, "used", UnsignedInt.class, Money.class);
            }

            /**
             * <p>
             * Classification of benefit being provided.
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
             * The quantity of the benefit which is permitted under the coverage.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getAllowed() {
                return allowed;
            }

            /**
             * <p>
             * The quantity of the benefit which have been consumed to date.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getUsed() {
                return used;
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
                        accept(allowed, "allowed", visitor, true);
                        accept(used, "used", visitor, true);
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
                private Element allowed;
                private Element used;

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
                 * The quantity of the benefit which is permitted under the coverage.
                 * </p>
                 * 
                 * @param allowed
                 *     Benefits allowed
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder allowed(Element allowed) {
                    this.allowed = allowed;
                    return this;
                }

                /**
                 * <p>
                 * The quantity of the benefit which have been consumed to date.
                 * </p>
                 * 
                 * @param used
                 *     Benefits used
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder used(Element used) {
                    this.used = used;
                    return this;
                }

                @Override
                public Financial build() {
                    return new Financial(this);
                }

                private static Builder from(Financial financial) {
                    Builder builder = new Builder(financial.type);
                    builder.id = financial.id;
                    builder.extension.addAll(financial.extension);
                    builder.modifierExtension.addAll(financial.modifierExtension);
                    builder.allowed = financial.allowed;
                    builder.used = financial.used;
                    return builder;
                }
            }
        }
    }
}
