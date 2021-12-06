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
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ExplanationOfBenefitStatus;
import com.ibm.fhir.model.type.code.NoteType;
import com.ibm.fhir.model.type.code.RemittanceOutcome;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.Use;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account 
 * balance information, for informing the subscriber of the benefits provided.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "explanationOfBenefit-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/claim-type",
    expression = "type.exists() and type.memberOf('http://hl7.org/fhir/ValueSet/claim-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/ExplanationOfBenefit",
    generated = true
)
@Constraint(
    id = "explanationOfBenefit-1",
    level = "Warning",
    location = "accident.type",
    description = "SHALL, if possible, contain a code from value set http://terminology.hl7.org/ValueSet/v3-ActIncidentCode",
    expression = "$this.memberOf('http://terminology.hl7.org/ValueSet/v3-ActIncidentCode', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/ExplanationOfBenefit",
    generated = true
)
@Constraint(
    id = "explanationOfBenefit-2",
    level = "Warning",
    location = "processNote.language",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/languages",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/ExplanationOfBenefit",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ExplanationOfBenefit extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "ExplanationOfBenefitStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A code specifying the state of the resource instance.",
        valueSet = "http://hl7.org/fhir/ValueSet/explanationofbenefit-status|4.1.0"
    )
    @Required
    private final ExplanationOfBenefitStatus status;
    @Summary
    @Binding(
        bindingName = "ClaimType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The type or discipline-style of the claim.",
        valueSet = "http://hl7.org/fhir/ValueSet/claim-type"
    )
    @Required
    private final CodeableConcept type;
    @Binding(
        bindingName = "ClaimSubType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A more granular claim typecode.",
        valueSet = "http://hl7.org/fhir/ValueSet/claim-subtype"
    )
    private final CodeableConcept subType;
    @Summary
    @Binding(
        bindingName = "Use",
        strength = BindingStrength.Value.REQUIRED,
        description = "Complete, proposed, exploratory, other.",
        valueSet = "http://hl7.org/fhir/ValueSet/claim-use|4.1.0"
    )
    @Required
    private final Use use;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference patient;
    @Summary
    private final Period billablePeriod;
    @Summary
    @Required
    private final DateTime created;
    @ReferenceTarget({ "Practitioner", "PractitionerRole" })
    private final Reference enterer;
    @Summary
    @ReferenceTarget({ "Organization" })
    @Required
    private final Reference insurer;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    @Required
    private final Reference provider;
    @Binding(
        bindingName = "ProcessPriority",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The timeliness with which processing is required: stat, normal, deferred.",
        valueSet = "http://terminology.hl7.org/CodeSystem/processpriority"
    )
    private final CodeableConcept priority;
    @Binding(
        bindingName = "FundsReserve",
        strength = BindingStrength.Value.EXAMPLE,
        description = "For whom funds are to be reserved: (Patient, Provider, None).",
        valueSet = "http://hl7.org/fhir/ValueSet/fundsreserve"
    )
    private final CodeableConcept fundsReserveRequested;
    @Binding(
        bindingName = "FundsReserve",
        strength = BindingStrength.Value.EXAMPLE,
        description = "For whom funds are to be reserved: (Patient, Provider, None).",
        valueSet = "http://hl7.org/fhir/ValueSet/fundsreserve"
    )
    private final CodeableConcept fundsReserve;
    private final List<Related> related;
    @ReferenceTarget({ "MedicationRequest", "VisionPrescription" })
    private final Reference prescription;
    @ReferenceTarget({ "MedicationRequest" })
    private final Reference originalPrescription;
    private final Payee payee;
    @ReferenceTarget({ "ServiceRequest" })
    private final Reference referral;
    @ReferenceTarget({ "Location" })
    private final Reference facility;
    @ReferenceTarget({ "Claim" })
    private final Reference claim;
    @ReferenceTarget({ "ClaimResponse" })
    private final Reference claimResponse;
    @Summary
    @Binding(
        bindingName = "RemittanceOutcome",
        strength = BindingStrength.Value.REQUIRED,
        description = "The result of the claim processing.",
        valueSet = "http://hl7.org/fhir/ValueSet/remittance-outcome|4.1.0"
    )
    @Required
    private final RemittanceOutcome outcome;
    private final String disposition;
    private final List<String> preAuthRef;
    private final List<Period> preAuthRefPeriod;
    private final List<CareTeam> careTeam;
    private final List<SupportingInfo> supportingInfo;
    private final List<Diagnosis> diagnosis;
    private final List<Procedure> procedure;
    private final PositiveInt precedence;
    @Summary
    @Required
    private final List<Insurance> insurance;
    private final Accident accident;
    private final List<Item> item;
    private final List<AddItem> addItem;
    private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;
    @Summary
    private final List<Total> total;
    private final Payment payment;
    @Binding(
        bindingName = "Forms",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The forms codes.",
        valueSet = "http://hl7.org/fhir/ValueSet/forms"
    )
    private final CodeableConcept formCode;
    private final Attachment form;
    private final List<ProcessNote> processNote;
    private final Period benefitPeriod;
    private final List<BenefitBalance> benefitBalance;

    private ExplanationOfBenefit(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        type = builder.type;
        subType = builder.subType;
        use = builder.use;
        patient = builder.patient;
        billablePeriod = builder.billablePeriod;
        created = builder.created;
        enterer = builder.enterer;
        insurer = builder.insurer;
        provider = builder.provider;
        priority = builder.priority;
        fundsReserveRequested = builder.fundsReserveRequested;
        fundsReserve = builder.fundsReserve;
        related = Collections.unmodifiableList(builder.related);
        prescription = builder.prescription;
        originalPrescription = builder.originalPrescription;
        payee = builder.payee;
        referral = builder.referral;
        facility = builder.facility;
        claim = builder.claim;
        claimResponse = builder.claimResponse;
        outcome = builder.outcome;
        disposition = builder.disposition;
        preAuthRef = Collections.unmodifiableList(builder.preAuthRef);
        preAuthRefPeriod = Collections.unmodifiableList(builder.preAuthRefPeriod);
        careTeam = Collections.unmodifiableList(builder.careTeam);
        supportingInfo = Collections.unmodifiableList(builder.supportingInfo);
        diagnosis = Collections.unmodifiableList(builder.diagnosis);
        procedure = Collections.unmodifiableList(builder.procedure);
        precedence = builder.precedence;
        insurance = Collections.unmodifiableList(builder.insurance);
        accident = builder.accident;
        item = Collections.unmodifiableList(builder.item);
        addItem = Collections.unmodifiableList(builder.addItem);
        adjudication = Collections.unmodifiableList(builder.adjudication);
        total = Collections.unmodifiableList(builder.total);
        payment = builder.payment;
        formCode = builder.formCode;
        form = builder.form;
        processNote = Collections.unmodifiableList(builder.processNote);
        benefitPeriod = builder.benefitPeriod;
        benefitBalance = Collections.unmodifiableList(builder.benefitBalance);
    }

    /**
     * A unique identifier assigned to this explanation of benefit.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The status of the resource instance.
     * 
     * @return
     *     An immutable object of type {@link ExplanationOfBenefitStatus} that is non-null.
     */
    public ExplanationOfBenefitStatus getStatus() {
        return status;
    }

    /**
     * The category of claim, e.g. oral, pharmacy, vision, institutional, professional.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient 
     * and/or a specialty service.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSubType() {
        return subType;
    }

    /**
     * A code to indicate whether the nature of the request is: to request adjudication of products and services previously 
     * rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding 
     * adjudication of the listed products and services which could be provided in the future.
     * 
     * @return
     *     An immutable object of type {@link Use} that is non-null.
     */
    public Use getUse() {
        return use;
    }

    /**
     * The party to whom the professional services and/or products have been supplied or are being considered and for whom 
     * actual for forecast reimbursement is sought.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * The period for which charges are being submitted.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getBillablePeriod() {
        return billablePeriod;
    }

    /**
     * The date this resource was created.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * Individual who created the claim, predetermination or preauthorization.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEnterer() {
        return enterer;
    }

    /**
     * The party responsible for authorization, adjudication and reimbursement.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getInsurer() {
        return insurer;
    }

    /**
     * The provider which is responsible for the claim, predetermination or preauthorization.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getProvider() {
        return provider;
    }

    /**
     * The provider-required urgency of processing the request. Typical values include: stat, routine deferred.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPriority() {
        return priority;
    }

    /**
     * A code to indicate whether and for whom funds are to be reserved for future claims.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getFundsReserveRequested() {
        return fundsReserveRequested;
    }

    /**
     * A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and 
     * for whom.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getFundsReserve() {
        return fundsReserve;
    }

    /**
     * Other claims which are related to this claim such as prior submissions or claims for related services or for the same 
     * event.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Related} that may be empty.
     */
    public List<Related> getRelated() {
        return related;
    }

    /**
     * Prescription to support the dispensing of pharmacy, device or vision products.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPrescription() {
        return prescription;
    }

    /**
     * Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, 
     * medications or products.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getOriginalPrescription() {
        return originalPrescription;
    }

    /**
     * The party to be reimbursed for cost of the products and services according to the terms of the policy.
     * 
     * @return
     *     An immutable object of type {@link Payee} that may be null.
     */
    public Payee getPayee() {
        return payee;
    }

    /**
     * A reference to a referral resource.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getReferral() {
        return referral;
    }

    /**
     * Facility where the services were provided.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getFacility() {
        return facility;
    }

    /**
     * The business identifier for the instance of the adjudication request: claim predetermination or preauthorization.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getClaim() {
        return claim;
    }

    /**
     * The business identifier for the instance of the adjudication response: claim, predetermination or preauthorization 
     * response.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getClaimResponse() {
        return claimResponse;
    }

    /**
     * The outcome of the claim, predetermination, or preauthorization processing.
     * 
     * @return
     *     An immutable object of type {@link RemittanceOutcome} that is non-null.
     */
    public RemittanceOutcome getOutcome() {
        return outcome;
    }

    /**
     * A human readable description of the status of the adjudication.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * Reference from the Insurer which is used in later communications which refers to this adjudication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getPreAuthRef() {
        return preAuthRef;
    }

    /**
     * The timeframe during which the supplied preauthorization reference may be quoted on claims to obtain the adjudication 
     * as provided.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Period} that may be empty.
     */
    public List<Period> getPreAuthRefPeriod() {
        return preAuthRefPeriod;
    }

    /**
     * The members of the team who provided the products and services.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CareTeam} that may be empty.
     */
    public List<CareTeam> getCareTeam() {
        return careTeam;
    }

    /**
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
     * concurrent issues.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link SupportingInfo} that may be empty.
     */
    public List<SupportingInfo> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * Information about diagnoses relevant to the claim items.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Diagnosis} that may be empty.
     */
    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }

    /**
     * Procedures performed on the patient relevant to the billing items with the claim.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Procedure} that may be empty.
     */
    public List<Procedure> getProcedure() {
        return procedure;
    }

    /**
     * This indicates the relative order of a series of EOBs related to different coverages for the same suite of services.
     * 
     * @return
     *     An immutable object of type {@link PositiveInt} that may be null.
     */
    public PositiveInt getPrecedence() {
        return precedence;
    }

    /**
     * Financial instruments for reimbursement for the health care products and services specified on the claim.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Insurance} that is non-empty.
     */
    public List<Insurance> getInsurance() {
        return insurance;
    }

    /**
     * Details of a accident which resulted in injuries which required the products and services listed in the claim.
     * 
     * @return
     *     An immutable object of type {@link Accident} that may be null.
     */
    public Accident getAccident() {
        return accident;
    }

    /**
     * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
     * groups of sub-details.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Item} that may be empty.
     */
    public List<Item> getItem() {
        return item;
    }

    /**
     * The first-tier service adjudications for payor added product or service lines.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link AddItem} that may be empty.
     */
    public List<AddItem> getAddItem() {
        return addItem;
    }

    /**
     * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Adjudication} that may be empty.
     */
    public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
        return adjudication;
    }

    /**
     * Categorized monetary totals for the adjudication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Total} that may be empty.
     */
    public List<Total> getTotal() {
        return total;
    }

    /**
     * Payment details for the adjudication of the claim.
     * 
     * @return
     *     An immutable object of type {@link Payment} that may be null.
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * A code for the form to be used for printing the content.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getFormCode() {
        return formCode;
    }

    /**
     * The actual form, by reference or inclusion, for printing the content or an EOB.
     * 
     * @return
     *     An immutable object of type {@link Attachment} that may be null.
     */
    public Attachment getForm() {
        return form;
    }

    /**
     * A note that describes or explains adjudication results in a human readable form.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ProcessNote} that may be empty.
     */
    public List<ProcessNote> getProcessNote() {
        return processNote;
    }

    /**
     * The term of the benefits documented in this response.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getBenefitPeriod() {
        return benefitPeriod;
    }

    /**
     * Balance by Benefit Category.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link BenefitBalance} that may be empty.
     */
    public List<BenefitBalance> getBenefitBalance() {
        return benefitBalance;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (type != null) || 
            (subType != null) || 
            (use != null) || 
            (patient != null) || 
            (billablePeriod != null) || 
            (created != null) || 
            (enterer != null) || 
            (insurer != null) || 
            (provider != null) || 
            (priority != null) || 
            (fundsReserveRequested != null) || 
            (fundsReserve != null) || 
            !related.isEmpty() || 
            (prescription != null) || 
            (originalPrescription != null) || 
            (payee != null) || 
            (referral != null) || 
            (facility != null) || 
            (claim != null) || 
            (claimResponse != null) || 
            (outcome != null) || 
            (disposition != null) || 
            !preAuthRef.isEmpty() || 
            !preAuthRefPeriod.isEmpty() || 
            !careTeam.isEmpty() || 
            !supportingInfo.isEmpty() || 
            !diagnosis.isEmpty() || 
            !procedure.isEmpty() || 
            (precedence != null) || 
            !insurance.isEmpty() || 
            (accident != null) || 
            !item.isEmpty() || 
            !addItem.isEmpty() || 
            !adjudication.isEmpty() || 
            !total.isEmpty() || 
            (payment != null) || 
            (formCode != null) || 
            (form != null) || 
            !processNote.isEmpty() || 
            (benefitPeriod != null) || 
            !benefitBalance.isEmpty();
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
        ExplanationOfBenefit other = (ExplanationOfBenefit) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(type, other.type) && 
            Objects.equals(subType, other.subType) && 
            Objects.equals(use, other.use) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(billablePeriod, other.billablePeriod) && 
            Objects.equals(created, other.created) && 
            Objects.equals(enterer, other.enterer) && 
            Objects.equals(insurer, other.insurer) && 
            Objects.equals(provider, other.provider) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(fundsReserveRequested, other.fundsReserveRequested) && 
            Objects.equals(fundsReserve, other.fundsReserve) && 
            Objects.equals(related, other.related) && 
            Objects.equals(prescription, other.prescription) && 
            Objects.equals(originalPrescription, other.originalPrescription) && 
            Objects.equals(payee, other.payee) && 
            Objects.equals(referral, other.referral) && 
            Objects.equals(facility, other.facility) && 
            Objects.equals(claim, other.claim) && 
            Objects.equals(claimResponse, other.claimResponse) && 
            Objects.equals(outcome, other.outcome) && 
            Objects.equals(disposition, other.disposition) && 
            Objects.equals(preAuthRef, other.preAuthRef) && 
            Objects.equals(preAuthRefPeriod, other.preAuthRefPeriod) && 
            Objects.equals(careTeam, other.careTeam) && 
            Objects.equals(supportingInfo, other.supportingInfo) && 
            Objects.equals(diagnosis, other.diagnosis) && 
            Objects.equals(procedure, other.procedure) && 
            Objects.equals(precedence, other.precedence) && 
            Objects.equals(insurance, other.insurance) && 
            Objects.equals(accident, other.accident) && 
            Objects.equals(item, other.item) && 
            Objects.equals(addItem, other.addItem) && 
            Objects.equals(adjudication, other.adjudication) && 
            Objects.equals(total, other.total) && 
            Objects.equals(payment, other.payment) && 
            Objects.equals(formCode, other.formCode) && 
            Objects.equals(form, other.form) && 
            Objects.equals(processNote, other.processNote) && 
            Objects.equals(benefitPeriod, other.benefitPeriod) && 
            Objects.equals(benefitBalance, other.benefitBalance);
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
                status, 
                type, 
                subType, 
                use, 
                patient, 
                billablePeriod, 
                created, 
                enterer, 
                insurer, 
                provider, 
                priority, 
                fundsReserveRequested, 
                fundsReserve, 
                related, 
                prescription, 
                originalPrescription, 
                payee, 
                referral, 
                facility, 
                claim, 
                claimResponse, 
                outcome, 
                disposition, 
                preAuthRef, 
                preAuthRefPeriod, 
                careTeam, 
                supportingInfo, 
                diagnosis, 
                procedure, 
                precedence, 
                insurance, 
                accident, 
                item, 
                addItem, 
                adjudication, 
                total, 
                payment, 
                formCode, 
                form, 
                processNote, 
                benefitPeriod, 
                benefitBalance);
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
        private ExplanationOfBenefitStatus status;
        private CodeableConcept type;
        private CodeableConcept subType;
        private Use use;
        private Reference patient;
        private Period billablePeriod;
        private DateTime created;
        private Reference enterer;
        private Reference insurer;
        private Reference provider;
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
        private RemittanceOutcome outcome;
        private String disposition;
        private List<String> preAuthRef = new ArrayList<>();
        private List<Period> preAuthRefPeriod = new ArrayList<>();
        private List<CareTeam> careTeam = new ArrayList<>();
        private List<SupportingInfo> supportingInfo = new ArrayList<>();
        private List<Diagnosis> diagnosis = new ArrayList<>();
        private List<Procedure> procedure = new ArrayList<>();
        private PositiveInt precedence;
        private List<Insurance> insurance = new ArrayList<>();
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
         * A unique identifier assigned to this explanation of benefit.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for the resource
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
         * A unique identifier assigned to this explanation of benefit.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for the resource
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
         * The status of the resource instance.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | cancelled | draft | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ExplanationOfBenefitStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The category of claim, e.g. oral, pharmacy, vision, institutional, professional.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     Category or discipline
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient 
         * and/or a specialty service.
         * 
         * @param subType
         *     More granular claim type
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subType(CodeableConcept subType) {
            this.subType = subType;
            return this;
        }

        /**
         * A code to indicate whether the nature of the request is: to request adjudication of products and services previously 
         * rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding 
         * adjudication of the listed products and services which could be provided in the future.
         * 
         * <p>This element is required.
         * 
         * @param use
         *     claim | preauthorization | predetermination
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder use(Use use) {
            this.use = use;
            return this;
        }

        /**
         * The party to whom the professional services and/or products have been supplied or are being considered and for whom 
         * actual for forecast reimbursement is sought.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     The recipient of the products and services
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * The period for which charges are being submitted.
         * 
         * @param billablePeriod
         *     Relevant time frame for the claim
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder billablePeriod(Period billablePeriod) {
            this.billablePeriod = billablePeriod;
            return this;
        }

        /**
         * The date this resource was created.
         * 
         * <p>This element is required.
         * 
         * @param created
         *     Response creation date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        /**
         * Individual who created the claim, predetermination or preauthorization.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param enterer
         *     Author of the claim
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder enterer(Reference enterer) {
            this.enterer = enterer;
            return this;
        }

        /**
         * The party responsible for authorization, adjudication and reimbursement.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param insurer
         *     Party responsible for reimbursement
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurer(Reference insurer) {
            this.insurer = insurer;
            return this;
        }

        /**
         * The provider which is responsible for the claim, predetermination or preauthorization.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param provider
         *     Party responsible for the claim
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder provider(Reference provider) {
            this.provider = provider;
            return this;
        }

        /**
         * The provider-required urgency of processing the request. Typical values include: stat, routine deferred.
         * 
         * @param priority
         *     Desired processing urgency
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priority(CodeableConcept priority) {
            this.priority = priority;
            return this;
        }

        /**
         * A code to indicate whether and for whom funds are to be reserved for future claims.
         * 
         * @param fundsReserveRequested
         *     For whom to reserve funds
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder fundsReserveRequested(CodeableConcept fundsReserveRequested) {
            this.fundsReserveRequested = fundsReserveRequested;
            return this;
        }

        /**
         * A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and 
         * for whom.
         * 
         * @param fundsReserve
         *     Funds reserved status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder fundsReserve(CodeableConcept fundsReserve) {
            this.fundsReserve = fundsReserve;
            return this;
        }

        /**
         * Other claims which are related to this claim such as prior submissions or claims for related services or for the same 
         * event.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param related
         *     Prior or corollary claims
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder related(Related... related) {
            for (Related value : related) {
                this.related.add(value);
            }
            return this;
        }

        /**
         * Other claims which are related to this claim such as prior submissions or claims for related services or for the same 
         * event.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param related
         *     Prior or corollary claims
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder related(Collection<Related> related) {
            this.related = new ArrayList<>(related);
            return this;
        }

        /**
         * Prescription to support the dispensing of pharmacy, device or vision products.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link MedicationRequest}</li>
         * <li>{@link VisionPrescription}</li>
         * </ul>
         * 
         * @param prescription
         *     Prescription authorizing services or products
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder prescription(Reference prescription) {
            this.prescription = prescription;
            return this;
        }

        /**
         * Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, 
         * medications or products.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link MedicationRequest}</li>
         * </ul>
         * 
         * @param originalPrescription
         *     Original prescription if superceded by fulfiller
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder originalPrescription(Reference originalPrescription) {
            this.originalPrescription = originalPrescription;
            return this;
        }

        /**
         * The party to be reimbursed for cost of the products and services according to the terms of the policy.
         * 
         * @param payee
         *     Recipient of benefits payable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payee(Payee payee) {
            this.payee = payee;
            return this;
        }

        /**
         * A reference to a referral resource.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param referral
         *     Treatment Referral
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referral(Reference referral) {
            this.referral = referral;
            return this;
        }

        /**
         * Facility where the services were provided.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param facility
         *     Servicing Facility
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder facility(Reference facility) {
            this.facility = facility;
            return this;
        }

        /**
         * The business identifier for the instance of the adjudication request: claim predetermination or preauthorization.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Claim}</li>
         * </ul>
         * 
         * @param claim
         *     Claim reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder claim(Reference claim) {
            this.claim = claim;
            return this;
        }

        /**
         * The business identifier for the instance of the adjudication response: claim, predetermination or preauthorization 
         * response.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link ClaimResponse}</li>
         * </ul>
         * 
         * @param claimResponse
         *     Claim response reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder claimResponse(Reference claimResponse) {
            this.claimResponse = claimResponse;
            return this;
        }

        /**
         * The outcome of the claim, predetermination, or preauthorization processing.
         * 
         * <p>This element is required.
         * 
         * @param outcome
         *     complete | error | partial
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder outcome(RemittanceOutcome outcome) {
            this.outcome = outcome;
            return this;
        }

        /**
         * Convenience method for setting {@code disposition}.
         * 
         * @param disposition
         *     Disposition Message
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #disposition(com.ibm.fhir.model.type.String)
         */
        public Builder disposition(java.lang.String disposition) {
            this.disposition = (disposition == null) ? null : String.of(disposition);
            return this;
        }

        /**
         * A human readable description of the status of the adjudication.
         * 
         * @param disposition
         *     Disposition Message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder disposition(String disposition) {
            this.disposition = disposition;
            return this;
        }

        /**
         * Convenience method for setting {@code preAuthRef}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param preAuthRef
         *     Preauthorization reference
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #preAuthRef(com.ibm.fhir.model.type.String)
         */
        public Builder preAuthRef(java.lang.String... preAuthRef) {
            for (java.lang.String value : preAuthRef) {
                this.preAuthRef.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * Reference from the Insurer which is used in later communications which refers to this adjudication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param preAuthRef
         *     Preauthorization reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder preAuthRef(String... preAuthRef) {
            for (String value : preAuthRef) {
                this.preAuthRef.add(value);
            }
            return this;
        }

        /**
         * Reference from the Insurer which is used in later communications which refers to this adjudication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param preAuthRef
         *     Preauthorization reference
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder preAuthRef(Collection<String> preAuthRef) {
            this.preAuthRef = new ArrayList<>(preAuthRef);
            return this;
        }

        /**
         * The timeframe during which the supplied preauthorization reference may be quoted on claims to obtain the adjudication 
         * as provided.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param preAuthRefPeriod
         *     Preauthorization in-effect period
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder preAuthRefPeriod(Period... preAuthRefPeriod) {
            for (Period value : preAuthRefPeriod) {
                this.preAuthRefPeriod.add(value);
            }
            return this;
        }

        /**
         * The timeframe during which the supplied preauthorization reference may be quoted on claims to obtain the adjudication 
         * as provided.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param preAuthRefPeriod
         *     Preauthorization in-effect period
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder preAuthRefPeriod(Collection<Period> preAuthRefPeriod) {
            this.preAuthRefPeriod = new ArrayList<>(preAuthRefPeriod);
            return this;
        }

        /**
         * The members of the team who provided the products and services.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param careTeam
         *     Care Team members
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder careTeam(CareTeam... careTeam) {
            for (CareTeam value : careTeam) {
                this.careTeam.add(value);
            }
            return this;
        }

        /**
         * The members of the team who provided the products and services.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param careTeam
         *     Care Team members
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder careTeam(Collection<CareTeam> careTeam) {
            this.careTeam = new ArrayList<>(careTeam);
            return this;
        }

        /**
         * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
         * concurrent issues.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInfo
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInfo(SupportingInfo... supportingInfo) {
            for (SupportingInfo value : supportingInfo) {
                this.supportingInfo.add(value);
            }
            return this;
        }

        /**
         * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
         * concurrent issues.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInfo
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder supportingInfo(Collection<SupportingInfo> supportingInfo) {
            this.supportingInfo = new ArrayList<>(supportingInfo);
            return this;
        }

        /**
         * Information about diagnoses relevant to the claim items.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param diagnosis
         *     Pertinent diagnosis information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder diagnosis(Diagnosis... diagnosis) {
            for (Diagnosis value : diagnosis) {
                this.diagnosis.add(value);
            }
            return this;
        }

        /**
         * Information about diagnoses relevant to the claim items.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param diagnosis
         *     Pertinent diagnosis information
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder diagnosis(Collection<Diagnosis> diagnosis) {
            this.diagnosis = new ArrayList<>(diagnosis);
            return this;
        }

        /**
         * Procedures performed on the patient relevant to the billing items with the claim.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param procedure
         *     Clinical procedures performed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder procedure(Procedure... procedure) {
            for (Procedure value : procedure) {
                this.procedure.add(value);
            }
            return this;
        }

        /**
         * Procedures performed on the patient relevant to the billing items with the claim.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param procedure
         *     Clinical procedures performed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder procedure(Collection<Procedure> procedure) {
            this.procedure = new ArrayList<>(procedure);
            return this;
        }

        /**
         * This indicates the relative order of a series of EOBs related to different coverages for the same suite of services.
         * 
         * @param precedence
         *     Precedence (primary, secondary, etc.)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder precedence(PositiveInt precedence) {
            this.precedence = precedence;
            return this;
        }

        /**
         * Financial instruments for reimbursement for the health care products and services specified on the claim.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Insurance... insurance) {
            for (Insurance value : insurance) {
                this.insurance.add(value);
            }
            return this;
        }

        /**
         * Financial instruments for reimbursement for the health care products and services specified on the claim.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder insurance(Collection<Insurance> insurance) {
            this.insurance = new ArrayList<>(insurance);
            return this;
        }

        /**
         * Details of a accident which resulted in injuries which required the products and services listed in the claim.
         * 
         * @param accident
         *     Details of the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder accident(Accident accident) {
            this.accident = accident;
            return this;
        }

        /**
         * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
         * groups of sub-details.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param item
         *     Product or service provided
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder item(Item... item) {
            for (Item value : item) {
                this.item.add(value);
            }
            return this;
        }

        /**
         * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
         * groups of sub-details.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param item
         *     Product or service provided
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder item(Collection<Item> item) {
            this.item = new ArrayList<>(item);
            return this;
        }

        /**
         * The first-tier service adjudications for payor added product or service lines.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param addItem
         *     Insurer added line items
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder addItem(AddItem... addItem) {
            for (AddItem value : addItem) {
                this.addItem.add(value);
            }
            return this;
        }

        /**
         * The first-tier service adjudications for payor added product or service lines.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param addItem
         *     Insurer added line items
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder addItem(Collection<AddItem> addItem) {
            this.addItem = new ArrayList<>(addItem);
            return this;
        }

        /**
         * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param adjudication
         *     Header-level adjudication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
            for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                this.adjudication.add(value);
            }
            return this;
        }

        /**
         * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param adjudication
         *     Header-level adjudication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
            this.adjudication = new ArrayList<>(adjudication);
            return this;
        }

        /**
         * Categorized monetary totals for the adjudication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param total
         *     Adjudication totals
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder total(Total... total) {
            for (Total value : total) {
                this.total.add(value);
            }
            return this;
        }

        /**
         * Categorized monetary totals for the adjudication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param total
         *     Adjudication totals
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder total(Collection<Total> total) {
            this.total = new ArrayList<>(total);
            return this;
        }

        /**
         * Payment details for the adjudication of the claim.
         * 
         * @param payment
         *     Payment Details
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payment(Payment payment) {
            this.payment = payment;
            return this;
        }

        /**
         * A code for the form to be used for printing the content.
         * 
         * @param formCode
         *     Printed form identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder formCode(CodeableConcept formCode) {
            this.formCode = formCode;
            return this;
        }

        /**
         * The actual form, by reference or inclusion, for printing the content or an EOB.
         * 
         * @param form
         *     Printed reference or actual form
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder form(Attachment form) {
            this.form = form;
            return this;
        }

        /**
         * A note that describes or explains adjudication results in a human readable form.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param processNote
         *     Note concerning adjudication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder processNote(ProcessNote... processNote) {
            for (ProcessNote value : processNote) {
                this.processNote.add(value);
            }
            return this;
        }

        /**
         * A note that describes or explains adjudication results in a human readable form.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param processNote
         *     Note concerning adjudication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder processNote(Collection<ProcessNote> processNote) {
            this.processNote = new ArrayList<>(processNote);
            return this;
        }

        /**
         * The term of the benefits documented in this response.
         * 
         * @param benefitPeriod
         *     When the benefits are applicable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder benefitPeriod(Period benefitPeriod) {
            this.benefitPeriod = benefitPeriod;
            return this;
        }

        /**
         * Balance by Benefit Category.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param benefitBalance
         *     Balance by Benefit Category
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder benefitBalance(BenefitBalance... benefitBalance) {
            for (BenefitBalance value : benefitBalance) {
                this.benefitBalance.add(value);
            }
            return this;
        }

        /**
         * Balance by Benefit Category.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param benefitBalance
         *     Balance by Benefit Category
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder benefitBalance(Collection<BenefitBalance> benefitBalance) {
            this.benefitBalance = new ArrayList<>(benefitBalance);
            return this;
        }

        /**
         * Build the {@link ExplanationOfBenefit}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>type</li>
         * <li>use</li>
         * <li>patient</li>
         * <li>created</li>
         * <li>insurer</li>
         * <li>provider</li>
         * <li>outcome</li>
         * <li>insurance</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ExplanationOfBenefit}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ExplanationOfBenefit per the base specification
         */
        @Override
        public ExplanationOfBenefit build() {
            ExplanationOfBenefit explanationOfBenefit = new ExplanationOfBenefit(this);
            if (validating) {
                validate(explanationOfBenefit);
            }
            return explanationOfBenefit;
        }

        protected void validate(ExplanationOfBenefit explanationOfBenefit) {
            super.validate(explanationOfBenefit);
            ValidationSupport.checkList(explanationOfBenefit.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(explanationOfBenefit.status, "status");
            ValidationSupport.requireNonNull(explanationOfBenefit.type, "type");
            ValidationSupport.requireNonNull(explanationOfBenefit.use, "use");
            ValidationSupport.requireNonNull(explanationOfBenefit.patient, "patient");
            ValidationSupport.requireNonNull(explanationOfBenefit.created, "created");
            ValidationSupport.requireNonNull(explanationOfBenefit.insurer, "insurer");
            ValidationSupport.requireNonNull(explanationOfBenefit.provider, "provider");
            ValidationSupport.checkList(explanationOfBenefit.related, "related", Related.class);
            ValidationSupport.requireNonNull(explanationOfBenefit.outcome, "outcome");
            ValidationSupport.checkList(explanationOfBenefit.preAuthRef, "preAuthRef", String.class);
            ValidationSupport.checkList(explanationOfBenefit.preAuthRefPeriod, "preAuthRefPeriod", Period.class);
            ValidationSupport.checkList(explanationOfBenefit.careTeam, "careTeam", CareTeam.class);
            ValidationSupport.checkList(explanationOfBenefit.supportingInfo, "supportingInfo", SupportingInfo.class);
            ValidationSupport.checkList(explanationOfBenefit.diagnosis, "diagnosis", Diagnosis.class);
            ValidationSupport.checkList(explanationOfBenefit.procedure, "procedure", Procedure.class);
            ValidationSupport.checkNonEmptyList(explanationOfBenefit.insurance, "insurance", Insurance.class);
            ValidationSupport.checkList(explanationOfBenefit.item, "item", Item.class);
            ValidationSupport.checkList(explanationOfBenefit.addItem, "addItem", AddItem.class);
            ValidationSupport.checkList(explanationOfBenefit.adjudication, "adjudication", ExplanationOfBenefit.Item.Adjudication.class);
            ValidationSupport.checkList(explanationOfBenefit.total, "total", Total.class);
            ValidationSupport.checkList(explanationOfBenefit.processNote, "processNote", ProcessNote.class);
            ValidationSupport.checkList(explanationOfBenefit.benefitBalance, "benefitBalance", BenefitBalance.class);
            ValidationSupport.checkReferenceType(explanationOfBenefit.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(explanationOfBenefit.enterer, "enterer", "Practitioner", "PractitionerRole");
            ValidationSupport.checkReferenceType(explanationOfBenefit.insurer, "insurer", "Organization");
            ValidationSupport.checkReferenceType(explanationOfBenefit.provider, "provider", "Practitioner", "PractitionerRole", "Organization");
            ValidationSupport.checkReferenceType(explanationOfBenefit.prescription, "prescription", "MedicationRequest", "VisionPrescription");
            ValidationSupport.checkReferenceType(explanationOfBenefit.originalPrescription, "originalPrescription", "MedicationRequest");
            ValidationSupport.checkReferenceType(explanationOfBenefit.referral, "referral", "ServiceRequest");
            ValidationSupport.checkReferenceType(explanationOfBenefit.facility, "facility", "Location");
            ValidationSupport.checkReferenceType(explanationOfBenefit.claim, "claim", "Claim");
            ValidationSupport.checkReferenceType(explanationOfBenefit.claimResponse, "claimResponse", "ClaimResponse");
        }

        protected Builder from(ExplanationOfBenefit explanationOfBenefit) {
            super.from(explanationOfBenefit);
            identifier.addAll(explanationOfBenefit.identifier);
            status = explanationOfBenefit.status;
            type = explanationOfBenefit.type;
            subType = explanationOfBenefit.subType;
            use = explanationOfBenefit.use;
            patient = explanationOfBenefit.patient;
            billablePeriod = explanationOfBenefit.billablePeriod;
            created = explanationOfBenefit.created;
            enterer = explanationOfBenefit.enterer;
            insurer = explanationOfBenefit.insurer;
            provider = explanationOfBenefit.provider;
            priority = explanationOfBenefit.priority;
            fundsReserveRequested = explanationOfBenefit.fundsReserveRequested;
            fundsReserve = explanationOfBenefit.fundsReserve;
            related.addAll(explanationOfBenefit.related);
            prescription = explanationOfBenefit.prescription;
            originalPrescription = explanationOfBenefit.originalPrescription;
            payee = explanationOfBenefit.payee;
            referral = explanationOfBenefit.referral;
            facility = explanationOfBenefit.facility;
            claim = explanationOfBenefit.claim;
            claimResponse = explanationOfBenefit.claimResponse;
            outcome = explanationOfBenefit.outcome;
            disposition = explanationOfBenefit.disposition;
            preAuthRef.addAll(explanationOfBenefit.preAuthRef);
            preAuthRefPeriod.addAll(explanationOfBenefit.preAuthRefPeriod);
            careTeam.addAll(explanationOfBenefit.careTeam);
            supportingInfo.addAll(explanationOfBenefit.supportingInfo);
            diagnosis.addAll(explanationOfBenefit.diagnosis);
            procedure.addAll(explanationOfBenefit.procedure);
            precedence = explanationOfBenefit.precedence;
            insurance.addAll(explanationOfBenefit.insurance);
            accident = explanationOfBenefit.accident;
            item.addAll(explanationOfBenefit.item);
            addItem.addAll(explanationOfBenefit.addItem);
            adjudication.addAll(explanationOfBenefit.adjudication);
            total.addAll(explanationOfBenefit.total);
            payment = explanationOfBenefit.payment;
            formCode = explanationOfBenefit.formCode;
            form = explanationOfBenefit.form;
            processNote.addAll(explanationOfBenefit.processNote);
            benefitPeriod = explanationOfBenefit.benefitPeriod;
            benefitBalance.addAll(explanationOfBenefit.benefitBalance);
            return this;
        }
    }

    /**
     * Other claims which are related to this claim such as prior submissions or claims for related services or for the same 
     * event.
     */
    public static class Related extends BackboneElement {
        @ReferenceTarget({ "Claim" })
        private final Reference claim;
        @Binding(
            bindingName = "RelatedClaimRelationship",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Relationship of this claim to a related Claim.",
            valueSet = "http://hl7.org/fhir/ValueSet/related-claim-relationship"
        )
        private final CodeableConcept relationship;
        private final Identifier reference;

        private Related(Builder builder) {
            super(builder);
            claim = builder.claim;
            relationship = builder.relationship;
            reference = builder.reference;
        }

        /**
         * Reference to a related claim.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getClaim() {
            return claim;
        }

        /**
         * A code to convey how the claims are related.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRelationship() {
            return relationship;
        }

        /**
         * An alternate organizational reference to the case or file to which this particular claim pertains.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getReference() {
            return reference;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (claim != null) || 
                (relationship != null) || 
                (reference != null);
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
                    accept(claim, "claim", visitor);
                    accept(relationship, "relationship", visitor);
                    accept(reference, "reference", visitor);
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
            Related other = (Related) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(claim, other.claim) && 
                Objects.equals(relationship, other.relationship) && 
                Objects.equals(reference, other.reference);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    claim, 
                    relationship, 
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
            private Reference claim;
            private CodeableConcept relationship;
            private Identifier reference;

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
             * Reference to a related claim.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Claim}</li>
             * </ul>
             * 
             * @param claim
             *     Reference to the related claim
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder claim(Reference claim) {
                this.claim = claim;
                return this;
            }

            /**
             * A code to convey how the claims are related.
             * 
             * @param relationship
             *     How the reference claim is related
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder relationship(CodeableConcept relationship) {
                this.relationship = relationship;
                return this;
            }

            /**
             * An alternate organizational reference to the case or file to which this particular claim pertains.
             * 
             * @param reference
             *     File or case reference
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reference(Identifier reference) {
                this.reference = reference;
                return this;
            }

            /**
             * Build the {@link Related}
             * 
             * @return
             *     An immutable object of type {@link Related}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Related per the base specification
             */
            @Override
            public Related build() {
                Related related = new Related(this);
                if (validating) {
                    validate(related);
                }
                return related;
            }

            protected void validate(Related related) {
                super.validate(related);
                ValidationSupport.checkReferenceType(related.claim, "claim", "Claim");
                ValidationSupport.requireValueOrChildren(related);
            }

            protected Builder from(Related related) {
                super.from(related);
                claim = related.claim;
                relationship = related.relationship;
                reference = related.reference;
                return this;
            }
        }
    }

    /**
     * The party to be reimbursed for cost of the products and services according to the terms of the policy.
     */
    public static class Payee extends BackboneElement {
        @Binding(
            bindingName = "PayeeType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A code for the party to be reimbursed.",
            valueSet = "http://hl7.org/fhir/ValueSet/payeetype"
        )
        private final CodeableConcept type;
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "Patient", "RelatedPerson" })
        private final Reference party;

        private Payee(Builder builder) {
            super(builder);
            type = builder.type;
            party = builder.party;
        }

        /**
         * Type of Party to be reimbursed: Subscriber, provider, other.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Reference to the individual or organization to whom any payment will be made.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getParty() {
            return party;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (party != null);
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
                    accept(party, "party", visitor);
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
            Payee other = (Payee) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(party, other.party);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    party);
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
            private Reference party;

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
             * Type of Party to be reimbursed: Subscriber, provider, other.
             * 
             * @param type
             *     Category of recipient
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Reference to the individual or organization to whom any payment will be made.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * <li>{@link Patient}</li>
             * <li>{@link RelatedPerson}</li>
             * </ul>
             * 
             * @param party
             *     Recipient reference
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder party(Reference party) {
                this.party = party;
                return this;
            }

            /**
             * Build the {@link Payee}
             * 
             * @return
             *     An immutable object of type {@link Payee}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Payee per the base specification
             */
            @Override
            public Payee build() {
                Payee payee = new Payee(this);
                if (validating) {
                    validate(payee);
                }
                return payee;
            }

            protected void validate(Payee payee) {
                super.validate(payee);
                ValidationSupport.checkReferenceType(payee.party, "party", "Practitioner", "PractitionerRole", "Organization", "Patient", "RelatedPerson");
                ValidationSupport.requireValueOrChildren(payee);
            }

            protected Builder from(Payee payee) {
                super.from(payee);
                type = payee.type;
                party = payee.party;
                return this;
            }
        }
    }

    /**
     * The members of the team who provided the products and services.
     */
    public static class CareTeam extends BackboneElement {
        @Required
        private final PositiveInt sequence;
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
        @Required
        private final Reference provider;
        private final Boolean responsible;
        @Binding(
            bindingName = "CareTeamRole",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The role codes for the care team members.",
            valueSet = "http://hl7.org/fhir/ValueSet/claim-careteamrole"
        )
        private final CodeableConcept role;
        @Binding(
            bindingName = "ProviderQualification",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Provider professional qualifications.",
            valueSet = "http://hl7.org/fhir/ValueSet/provider-qualification"
        )
        private final CodeableConcept qualification;

        private CareTeam(Builder builder) {
            super(builder);
            sequence = builder.sequence;
            provider = builder.provider;
            responsible = builder.responsible;
            role = builder.role;
            qualification = builder.qualification;
        }

        /**
         * A number to uniquely identify care team entries.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that is non-null.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * Member of the team who provided the product or service.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getProvider() {
            return provider;
        }

        /**
         * The party who is billing and/or responsible for the claimed products or services.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getResponsible() {
            return responsible;
        }

        /**
         * The lead, assisting or supervising practitioner and their discipline if a multidisciplinary team.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRole() {
            return role;
        }

        /**
         * The qualification of the practitioner which is applicable for this service.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getQualification() {
            return qualification;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (sequence != null) || 
                (provider != null) || 
                (responsible != null) || 
                (role != null) || 
                (qualification != null);
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
                    accept(sequence, "sequence", visitor);
                    accept(provider, "provider", visitor);
                    accept(responsible, "responsible", visitor);
                    accept(role, "role", visitor);
                    accept(qualification, "qualification", visitor);
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
            CareTeam other = (CareTeam) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(provider, other.provider) && 
                Objects.equals(responsible, other.responsible) && 
                Objects.equals(role, other.role) && 
                Objects.equals(qualification, other.qualification);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    sequence, 
                    provider, 
                    responsible, 
                    role, 
                    qualification);
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
            private PositiveInt sequence;
            private Reference provider;
            private Boolean responsible;
            private CodeableConcept role;
            private CodeableConcept qualification;

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
             * A number to uniquely identify care team entries.
             * 
             * <p>This element is required.
             * 
             * @param sequence
             *     Order of care team
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequence(PositiveInt sequence) {
                this.sequence = sequence;
                return this;
            }

            /**
             * Member of the team who provided the product or service.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param provider
             *     Practitioner or organization
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder provider(Reference provider) {
                this.provider = provider;
                return this;
            }

            /**
             * Convenience method for setting {@code responsible}.
             * 
             * @param responsible
             *     Indicator of the lead practitioner
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #responsible(com.ibm.fhir.model.type.Boolean)
             */
            public Builder responsible(java.lang.Boolean responsible) {
                this.responsible = (responsible == null) ? null : Boolean.of(responsible);
                return this;
            }

            /**
             * The party who is billing and/or responsible for the claimed products or services.
             * 
             * @param responsible
             *     Indicator of the lead practitioner
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder responsible(Boolean responsible) {
                this.responsible = responsible;
                return this;
            }

            /**
             * The lead, assisting or supervising practitioner and their discipline if a multidisciplinary team.
             * 
             * @param role
             *     Function within the team
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder role(CodeableConcept role) {
                this.role = role;
                return this;
            }

            /**
             * The qualification of the practitioner which is applicable for this service.
             * 
             * @param qualification
             *     Practitioner credential or specialization
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder qualification(CodeableConcept qualification) {
                this.qualification = qualification;
                return this;
            }

            /**
             * Build the {@link CareTeam}
             * 
             * <p>Required elements:
             * <ul>
             * <li>sequence</li>
             * <li>provider</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link CareTeam}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid CareTeam per the base specification
             */
            @Override
            public CareTeam build() {
                CareTeam careTeam = new CareTeam(this);
                if (validating) {
                    validate(careTeam);
                }
                return careTeam;
            }

            protected void validate(CareTeam careTeam) {
                super.validate(careTeam);
                ValidationSupport.requireNonNull(careTeam.sequence, "sequence");
                ValidationSupport.requireNonNull(careTeam.provider, "provider");
                ValidationSupport.checkReferenceType(careTeam.provider, "provider", "Practitioner", "PractitionerRole", "Organization");
                ValidationSupport.requireValueOrChildren(careTeam);
            }

            protected Builder from(CareTeam careTeam) {
                super.from(careTeam);
                sequence = careTeam.sequence;
                provider = careTeam.provider;
                responsible = careTeam.responsible;
                role = careTeam.role;
                qualification = careTeam.qualification;
                return this;
            }
        }
    }

    /**
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
     * concurrent issues.
     */
    public static class SupportingInfo extends BackboneElement {
        @Required
        private final PositiveInt sequence;
        @Binding(
            bindingName = "InformationCategory",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The valuset used for additional information category codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/claim-informationcategory"
        )
        @Required
        private final CodeableConcept category;
        @Binding(
            bindingName = "InformationCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The valuset used for additional information codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/claim-exception"
        )
        private final CodeableConcept code;
        @Choice({ Date.class, Period.class })
        private final Element timing;
        @Choice({ Boolean.class, String.class, Quantity.class, Attachment.class, Reference.class })
        private final Element value;
        @Binding(
            bindingName = "MissingReason",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Reason codes for the missing teeth.",
            valueSet = "http://hl7.org/fhir/ValueSet/missing-tooth-reason"
        )
        private final Coding reason;

        private SupportingInfo(Builder builder) {
            super(builder);
            sequence = builder.sequence;
            category = builder.category;
            code = builder.code;
            timing = builder.timing;
            value = builder.value;
            reason = builder.reason;
        }

        /**
         * A number to uniquely identify supporting information entries.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that is non-null.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * The general class of the information supplied: information; exception; accident, employment; onset, etc.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * System and code pertaining to the specific information regarding special conditions relating to the setting, treatment 
         * or patient for which care is sought.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * The date when or period to which this information refers.
         * 
         * @return
         *     An immutable object of type {@link Date} or {@link Period} that may be null.
         */
        public Element getTiming() {
            return timing;
        }

        /**
         * Additional data or information such as resources, documents, images etc. including references to the data or the 
         * actual inclusion of the data.
         * 
         * @return
         *     An immutable object of type {@link Boolean}, {@link String}, {@link Quantity}, {@link Attachment} or {@link Reference} 
         *     that may be null.
         */
        public Element getValue() {
            return value;
        }

        /**
         * Provides the reason in the situation where a reason code is required in addition to the content.
         * 
         * @return
         *     An immutable object of type {@link Coding} that may be null.
         */
        public Coding getReason() {
            return reason;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (sequence != null) || 
                (category != null) || 
                (code != null) || 
                (timing != null) || 
                (value != null) || 
                (reason != null);
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
                    accept(sequence, "sequence", visitor);
                    accept(category, "category", visitor);
                    accept(code, "code", visitor);
                    accept(timing, "timing", visitor);
                    accept(value, "value", visitor);
                    accept(reason, "reason", visitor);
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
            SupportingInfo other = (SupportingInfo) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(category, other.category) && 
                Objects.equals(code, other.code) && 
                Objects.equals(timing, other.timing) && 
                Objects.equals(value, other.value) && 
                Objects.equals(reason, other.reason);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    sequence, 
                    category, 
                    code, 
                    timing, 
                    value, 
                    reason);
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
            private PositiveInt sequence;
            private CodeableConcept category;
            private CodeableConcept code;
            private Element timing;
            private Element value;
            private Coding reason;

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
             * A number to uniquely identify supporting information entries.
             * 
             * <p>This element is required.
             * 
             * @param sequence
             *     Information instance identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequence(PositiveInt sequence) {
                this.sequence = sequence;
                return this;
            }

            /**
             * The general class of the information supplied: information; exception; accident, employment; onset, etc.
             * 
             * <p>This element is required.
             * 
             * @param category
             *     Classification of the supplied information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder category(CodeableConcept category) {
                this.category = category;
                return this;
            }

            /**
             * System and code pertaining to the specific information regarding special conditions relating to the setting, treatment 
             * or patient for which care is sought.
             * 
             * @param code
             *     Type of information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Convenience method for setting {@code timing} with choice type Date.
             * 
             * @param timing
             *     When it occurred
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #timing(Element)
             */
            public Builder timing(java.time.LocalDate timing) {
                this.timing = (timing == null) ? null : Date.of(timing);
                return this;
            }

            /**
             * The date when or period to which this information refers.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Date}</li>
             * <li>{@link Period}</li>
             * </ul>
             * 
             * @param timing
             *     When it occurred
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder timing(Element timing) {
                this.timing = timing;
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Boolean.
             * 
             * @param value
             *     Data to be provided
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
             * Convenience method for setting {@code value} with choice type String.
             * 
             * @param value
             *     Data to be provided
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
             * Additional data or information such as resources, documents, images etc. including references to the data or the 
             * actual inclusion of the data.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Boolean}</li>
             * <li>{@link String}</li>
             * <li>{@link Quantity}</li>
             * <li>{@link Attachment}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * @param value
             *     Data to be provided
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Provides the reason in the situation where a reason code is required in addition to the content.
             * 
             * @param reason
             *     Explanation for the information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reason(Coding reason) {
                this.reason = reason;
                return this;
            }

            /**
             * Build the {@link SupportingInfo}
             * 
             * <p>Required elements:
             * <ul>
             * <li>sequence</li>
             * <li>category</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link SupportingInfo}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid SupportingInfo per the base specification
             */
            @Override
            public SupportingInfo build() {
                SupportingInfo supportingInfo = new SupportingInfo(this);
                if (validating) {
                    validate(supportingInfo);
                }
                return supportingInfo;
            }

            protected void validate(SupportingInfo supportingInfo) {
                super.validate(supportingInfo);
                ValidationSupport.requireNonNull(supportingInfo.sequence, "sequence");
                ValidationSupport.requireNonNull(supportingInfo.category, "category");
                ValidationSupport.choiceElement(supportingInfo.timing, "timing", Date.class, Period.class);
                ValidationSupport.choiceElement(supportingInfo.value, "value", Boolean.class, String.class, Quantity.class, Attachment.class, Reference.class);
                ValidationSupport.requireValueOrChildren(supportingInfo);
            }

            protected Builder from(SupportingInfo supportingInfo) {
                super.from(supportingInfo);
                sequence = supportingInfo.sequence;
                category = supportingInfo.category;
                code = supportingInfo.code;
                timing = supportingInfo.timing;
                value = supportingInfo.value;
                reason = supportingInfo.reason;
                return this;
            }
        }
    }

    /**
     * Information about diagnoses relevant to the claim items.
     */
    public static class Diagnosis extends BackboneElement {
        @Required
        private final PositiveInt sequence;
        @ReferenceTarget({ "Condition" })
        @Choice({ CodeableConcept.class, Reference.class })
        @Binding(
            bindingName = "ICD10",
            strength = BindingStrength.Value.EXAMPLE,
            description = "ICD10 Diagnostic codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/icd-10"
        )
        @Required
        private final Element diagnosis;
        @Binding(
            bindingName = "DiagnosisType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The type of the diagnosis: admitting, principal, discharge.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-diagnosistype"
        )
        private final List<CodeableConcept> type;
        @Binding(
            bindingName = "DiagnosisOnAdmission",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Present on admission.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-diagnosis-on-admission"
        )
        private final CodeableConcept onAdmission;
        @Binding(
            bindingName = "DiagnosisRelatedGroup",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The DRG codes associated with the diagnosis.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-diagnosisrelatedgroup"
        )
        private final CodeableConcept packageCode;

        private Diagnosis(Builder builder) {
            super(builder);
            sequence = builder.sequence;
            diagnosis = builder.diagnosis;
            type = Collections.unmodifiableList(builder.type);
            onAdmission = builder.onAdmission;
            packageCode = builder.packageCode;
        }

        /**
         * A number to uniquely identify diagnosis entries.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that is non-null.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * The nature of illness or problem in a coded form or as a reference to an external defined Condition.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} or {@link Reference} that is non-null.
         */
        public Element getDiagnosis() {
            return diagnosis;
        }

        /**
         * When the condition was observed or the relative ranking.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getType() {
            return type;
        }

        /**
         * Indication of whether the diagnosis was present on admission to a facility.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOnAdmission() {
            return onAdmission;
        }

        /**
         * A package billing code or bundle code used to group products and services to a particular health condition (such as 
         * heart attack) which is based on a predetermined grouping code system.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getPackageCode() {
            return packageCode;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (sequence != null) || 
                (diagnosis != null) || 
                !type.isEmpty() || 
                (onAdmission != null) || 
                (packageCode != null);
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
                    accept(sequence, "sequence", visitor);
                    accept(diagnosis, "diagnosis", visitor);
                    accept(type, "type", visitor, CodeableConcept.class);
                    accept(onAdmission, "onAdmission", visitor);
                    accept(packageCode, "packageCode", visitor);
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
            Diagnosis other = (Diagnosis) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(diagnosis, other.diagnosis) && 
                Objects.equals(type, other.type) && 
                Objects.equals(onAdmission, other.onAdmission) && 
                Objects.equals(packageCode, other.packageCode);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    sequence, 
                    diagnosis, 
                    type, 
                    onAdmission, 
                    packageCode);
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
            private PositiveInt sequence;
            private Element diagnosis;
            private List<CodeableConcept> type = new ArrayList<>();
            private CodeableConcept onAdmission;
            private CodeableConcept packageCode;

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
             * A number to uniquely identify diagnosis entries.
             * 
             * <p>This element is required.
             * 
             * @param sequence
             *     Diagnosis instance identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequence(PositiveInt sequence) {
                this.sequence = sequence;
                return this;
            }

            /**
             * The nature of illness or problem in a coded form or as a reference to an external defined Condition.
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
             * <li>{@link Condition}</li>
             * </ul>
             * 
             * @param diagnosis
             *     Nature of illness or problem
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diagnosis(Element diagnosis) {
                this.diagnosis = diagnosis;
                return this;
            }

            /**
             * When the condition was observed or the relative ranking.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param type
             *     Timing or nature of the diagnosis
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
             * When the condition was observed or the relative ranking.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param type
             *     Timing or nature of the diagnosis
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder type(Collection<CodeableConcept> type) {
                this.type = new ArrayList<>(type);
                return this;
            }

            /**
             * Indication of whether the diagnosis was present on admission to a facility.
             * 
             * @param onAdmission
             *     Present on admission
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder onAdmission(CodeableConcept onAdmission) {
                this.onAdmission = onAdmission;
                return this;
            }

            /**
             * A package billing code or bundle code used to group products and services to a particular health condition (such as 
             * heart attack) which is based on a predetermined grouping code system.
             * 
             * @param packageCode
             *     Package billing code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder packageCode(CodeableConcept packageCode) {
                this.packageCode = packageCode;
                return this;
            }

            /**
             * Build the {@link Diagnosis}
             * 
             * <p>Required elements:
             * <ul>
             * <li>sequence</li>
             * <li>diagnosis</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Diagnosis}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Diagnosis per the base specification
             */
            @Override
            public Diagnosis build() {
                Diagnosis diagnosis = new Diagnosis(this);
                if (validating) {
                    validate(diagnosis);
                }
                return diagnosis;
            }

            protected void validate(Diagnosis diagnosis) {
                super.validate(diagnosis);
                ValidationSupport.requireNonNull(diagnosis.sequence, "sequence");
                ValidationSupport.requireChoiceElement(diagnosis.diagnosis, "diagnosis", CodeableConcept.class, Reference.class);
                ValidationSupport.checkList(diagnosis.type, "type", CodeableConcept.class);
                ValidationSupport.checkReferenceType(diagnosis.diagnosis, "diagnosis", "Condition");
                ValidationSupport.requireValueOrChildren(diagnosis);
            }

            protected Builder from(Diagnosis diagnosis) {
                super.from(diagnosis);
                sequence = diagnosis.sequence;
                this.diagnosis = diagnosis.diagnosis;
                type.addAll(diagnosis.type);
                onAdmission = diagnosis.onAdmission;
                packageCode = diagnosis.packageCode;
                return this;
            }
        }
    }

    /**
     * Procedures performed on the patient relevant to the billing items with the claim.
     */
    public static class Procedure extends BackboneElement {
        @Required
        private final PositiveInt sequence;
        @Binding(
            bindingName = "ProcedureType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Example procedure type codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-procedure-type"
        )
        private final List<CodeableConcept> type;
        private final DateTime date;
        @ReferenceTarget({ "Procedure" })
        @Choice({ CodeableConcept.class, Reference.class })
        @Binding(
            bindingName = "ICD10_Procedures",
            strength = BindingStrength.Value.EXAMPLE,
            description = "ICD10 Procedure codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/icd-10-procedures"
        )
        @Required
        private final Element procedure;
        @ReferenceTarget({ "Device" })
        private final List<Reference> udi;

        private Procedure(Builder builder) {
            super(builder);
            sequence = builder.sequence;
            type = Collections.unmodifiableList(builder.type);
            date = builder.date;
            procedure = builder.procedure;
            udi = Collections.unmodifiableList(builder.udi);
        }

        /**
         * A number to uniquely identify procedure entries.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that is non-null.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * When the condition was observed or the relative ranking.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getType() {
            return type;
        }

        /**
         * Date and optionally time the procedure was performed.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getDate() {
            return date;
        }

        /**
         * The code or reference to a Procedure resource which identifies the clinical intervention performed.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} or {@link Reference} that is non-null.
         */
        public Element getProcedure() {
            return procedure;
        }

        /**
         * Unique Device Identifiers associated with this line item.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getUdi() {
            return udi;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (sequence != null) || 
                !type.isEmpty() || 
                (date != null) || 
                (procedure != null) || 
                !udi.isEmpty();
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
                    accept(sequence, "sequence", visitor);
                    accept(type, "type", visitor, CodeableConcept.class);
                    accept(date, "date", visitor);
                    accept(procedure, "procedure", visitor);
                    accept(udi, "udi", visitor, Reference.class);
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
            Procedure other = (Procedure) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(type, other.type) && 
                Objects.equals(date, other.date) && 
                Objects.equals(procedure, other.procedure) && 
                Objects.equals(udi, other.udi);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    sequence, 
                    type, 
                    date, 
                    procedure, 
                    udi);
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
            private PositiveInt sequence;
            private List<CodeableConcept> type = new ArrayList<>();
            private DateTime date;
            private Element procedure;
            private List<Reference> udi = new ArrayList<>();

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
             * A number to uniquely identify procedure entries.
             * 
             * <p>This element is required.
             * 
             * @param sequence
             *     Procedure instance identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequence(PositiveInt sequence) {
                this.sequence = sequence;
                return this;
            }

            /**
             * When the condition was observed or the relative ranking.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param type
             *     Category of Procedure
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
             * When the condition was observed or the relative ranking.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param type
             *     Category of Procedure
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder type(Collection<CodeableConcept> type) {
                this.type = new ArrayList<>(type);
                return this;
            }

            /**
             * Date and optionally time the procedure was performed.
             * 
             * @param date
             *     When the procedure was performed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(DateTime date) {
                this.date = date;
                return this;
            }

            /**
             * The code or reference to a Procedure resource which identifies the clinical intervention performed.
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
             * <li>{@link Procedure}</li>
             * </ul>
             * 
             * @param procedure
             *     Specific clinical procedure
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder procedure(Element procedure) {
                this.procedure = procedure;
                return this;
            }

            /**
             * Unique Device Identifiers associated with this line item.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Device}</li>
             * </ul>
             * 
             * @param udi
             *     Unique device identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder udi(Reference... udi) {
                for (Reference value : udi) {
                    this.udi.add(value);
                }
                return this;
            }

            /**
             * Unique Device Identifiers associated with this line item.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Device}</li>
             * </ul>
             * 
             * @param udi
             *     Unique device identifier
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder udi(Collection<Reference> udi) {
                this.udi = new ArrayList<>(udi);
                return this;
            }

            /**
             * Build the {@link Procedure}
             * 
             * <p>Required elements:
             * <ul>
             * <li>sequence</li>
             * <li>procedure</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Procedure}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Procedure per the base specification
             */
            @Override
            public Procedure build() {
                Procedure procedure = new Procedure(this);
                if (validating) {
                    validate(procedure);
                }
                return procedure;
            }

            protected void validate(Procedure procedure) {
                super.validate(procedure);
                ValidationSupport.requireNonNull(procedure.sequence, "sequence");
                ValidationSupport.checkList(procedure.type, "type", CodeableConcept.class);
                ValidationSupport.requireChoiceElement(procedure.procedure, "procedure", CodeableConcept.class, Reference.class);
                ValidationSupport.checkList(procedure.udi, "udi", Reference.class);
                ValidationSupport.checkReferenceType(procedure.procedure, "procedure", "Procedure");
                ValidationSupport.checkReferenceType(procedure.udi, "udi", "Device");
                ValidationSupport.requireValueOrChildren(procedure);
            }

            protected Builder from(Procedure procedure) {
                super.from(procedure);
                sequence = procedure.sequence;
                type.addAll(procedure.type);
                date = procedure.date;
                this.procedure = procedure.procedure;
                udi.addAll(procedure.udi);
                return this;
            }
        }
    }

    /**
     * Financial instruments for reimbursement for the health care products and services specified on the claim.
     */
    public static class Insurance extends BackboneElement {
        @Summary
        @Required
        private final Boolean focal;
        @Summary
        @ReferenceTarget({ "Coverage" })
        @Required
        private final Reference coverage;
        private final List<String> preAuthRef;

        private Insurance(Builder builder) {
            super(builder);
            focal = builder.focal;
            coverage = builder.coverage;
            preAuthRef = Collections.unmodifiableList(builder.preAuthRef);
        }

        /**
         * A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that is non-null.
         */
        public Boolean getFocal() {
            return focal;
        }

        /**
         * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer 
         * will use these details to locate the patient's actual coverage within the insurer's information system.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getCoverage() {
            return coverage;
        }

        /**
         * Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing 
         * services or products related to the prior authorization.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
         */
        public List<String> getPreAuthRef() {
            return preAuthRef;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (focal != null) || 
                (coverage != null) || 
                !preAuthRef.isEmpty();
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
                    accept(focal, "focal", visitor);
                    accept(coverage, "coverage", visitor);
                    accept(preAuthRef, "preAuthRef", visitor, String.class);
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
            Insurance other = (Insurance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(focal, other.focal) && 
                Objects.equals(coverage, other.coverage) && 
                Objects.equals(preAuthRef, other.preAuthRef);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    focal, 
                    coverage, 
                    preAuthRef);
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
            private Boolean focal;
            private Reference coverage;
            private List<String> preAuthRef = new ArrayList<>();

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
             * Convenience method for setting {@code focal}.
             * 
             * <p>This element is required.
             * 
             * @param focal
             *     Coverage to be used for adjudication
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #focal(com.ibm.fhir.model.type.Boolean)
             */
            public Builder focal(java.lang.Boolean focal) {
                this.focal = (focal == null) ? null : Boolean.of(focal);
                return this;
            }

            /**
             * A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.
             * 
             * <p>This element is required.
             * 
             * @param focal
             *     Coverage to be used for adjudication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder focal(Boolean focal) {
                this.focal = focal;
                return this;
            }

            /**
             * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer 
             * will use these details to locate the patient's actual coverage within the insurer's information system.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Coverage}</li>
             * </ul>
             * 
             * @param coverage
             *     Insurance information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder coverage(Reference coverage) {
                this.coverage = coverage;
                return this;
            }

            /**
             * Convenience method for setting {@code preAuthRef}.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param preAuthRef
             *     Prior authorization reference number
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #preAuthRef(com.ibm.fhir.model.type.String)
             */
            public Builder preAuthRef(java.lang.String... preAuthRef) {
                for (java.lang.String value : preAuthRef) {
                    this.preAuthRef.add((value == null) ? null : String.of(value));
                }
                return this;
            }

            /**
             * Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing 
             * services or products related to the prior authorization.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param preAuthRef
             *     Prior authorization reference number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder preAuthRef(String... preAuthRef) {
                for (String value : preAuthRef) {
                    this.preAuthRef.add(value);
                }
                return this;
            }

            /**
             * Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing 
             * services or products related to the prior authorization.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param preAuthRef
             *     Prior authorization reference number
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder preAuthRef(Collection<String> preAuthRef) {
                this.preAuthRef = new ArrayList<>(preAuthRef);
                return this;
            }

            /**
             * Build the {@link Insurance}
             * 
             * <p>Required elements:
             * <ul>
             * <li>focal</li>
             * <li>coverage</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Insurance}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Insurance per the base specification
             */
            @Override
            public Insurance build() {
                Insurance insurance = new Insurance(this);
                if (validating) {
                    validate(insurance);
                }
                return insurance;
            }

            protected void validate(Insurance insurance) {
                super.validate(insurance);
                ValidationSupport.requireNonNull(insurance.focal, "focal");
                ValidationSupport.requireNonNull(insurance.coverage, "coverage");
                ValidationSupport.checkList(insurance.preAuthRef, "preAuthRef", String.class);
                ValidationSupport.checkReferenceType(insurance.coverage, "coverage", "Coverage");
                ValidationSupport.requireValueOrChildren(insurance);
            }

            protected Builder from(Insurance insurance) {
                super.from(insurance);
                focal = insurance.focal;
                coverage = insurance.coverage;
                preAuthRef.addAll(insurance.preAuthRef);
                return this;
            }
        }
    }

    /**
     * Details of a accident which resulted in injuries which required the products and services listed in the claim.
     */
    public static class Accident extends BackboneElement {
        private final Date date;
        @Binding(
            bindingName = "AccidentType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Type of accident: work place, auto, etc.",
            valueSet = "http://terminology.hl7.org/ValueSet/v3-ActIncidentCode"
        )
        private final CodeableConcept type;
        @ReferenceTarget({ "Location" })
        @Choice({ Address.class, Reference.class })
        private final Element location;

        private Accident(Builder builder) {
            super(builder);
            date = builder.date;
            type = builder.type;
            location = builder.location;
        }

        /**
         * Date of an accident event related to the products and services contained in the claim.
         * 
         * @return
         *     An immutable object of type {@link Date} that may be null.
         */
        public Date getDate() {
            return date;
        }

        /**
         * The type or context of the accident event for the purposes of selection of potential insurance coverages and 
         * determination of coordination between insurers.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The physical location of the accident event.
         * 
         * @return
         *     An immutable object of type {@link Address} or {@link Reference} that may be null.
         */
        public Element getLocation() {
            return location;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (date != null) || 
                (type != null) || 
                (location != null);
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
                    accept(date, "date", visitor);
                    accept(type, "type", visitor);
                    accept(location, "location", visitor);
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
            Accident other = (Accident) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(date, other.date) && 
                Objects.equals(type, other.type) && 
                Objects.equals(location, other.location);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    date, 
                    type, 
                    location);
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
            private Date date;
            private CodeableConcept type;
            private Element location;

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
             * Convenience method for setting {@code date}.
             * 
             * @param date
             *     When the incident occurred
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #date(com.ibm.fhir.model.type.Date)
             */
            public Builder date(java.time.LocalDate date) {
                this.date = (date == null) ? null : Date.of(date);
                return this;
            }

            /**
             * Date of an accident event related to the products and services contained in the claim.
             * 
             * @param date
             *     When the incident occurred
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(Date date) {
                this.date = date;
                return this;
            }

            /**
             * The type or context of the accident event for the purposes of selection of potential insurance coverages and 
             * determination of coordination between insurers.
             * 
             * @param type
             *     The nature of the accident
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * The physical location of the accident event.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Address}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link Location}</li>
             * </ul>
             * 
             * @param location
             *     Where the event occurred
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder location(Element location) {
                this.location = location;
                return this;
            }

            /**
             * Build the {@link Accident}
             * 
             * @return
             *     An immutable object of type {@link Accident}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Accident per the base specification
             */
            @Override
            public Accident build() {
                Accident accident = new Accident(this);
                if (validating) {
                    validate(accident);
                }
                return accident;
            }

            protected void validate(Accident accident) {
                super.validate(accident);
                ValidationSupport.choiceElement(accident.location, "location", Address.class, Reference.class);
                ValidationSupport.checkReferenceType(accident.location, "location", "Location");
                ValidationSupport.requireValueOrChildren(accident);
            }

            protected Builder from(Accident accident) {
                super.from(accident);
                date = accident.date;
                type = accident.type;
                location = accident.location;
                return this;
            }
        }
    }

    /**
     * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
     * groups of sub-details.
     */
    public static class Item extends BackboneElement {
        @Required
        private final PositiveInt sequence;
        private final List<PositiveInt> careTeamSequence;
        private final List<PositiveInt> diagnosisSequence;
        private final List<PositiveInt> procedureSequence;
        private final List<PositiveInt> informationSequence;
        @Binding(
            bindingName = "RevenueCenter",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes for the revenue or cost centers supplying the service and/or products.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-revenue-center"
        )
        private final CodeableConcept revenue;
        @Binding(
            bindingName = "BenefitCategory",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Benefit categories such as: oral, medical, vision, oral-basic etc.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-benefitcategory"
        )
        private final CodeableConcept category;
        @Binding(
            bindingName = "ServiceProduct",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Allowable service and product codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/service-uscls"
        )
        @Required
        private final CodeableConcept productOrService;
        @Binding(
            bindingName = "Modifiers",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Item type or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.",
            valueSet = "http://hl7.org/fhir/ValueSet/claim-modifiers"
        )
        private final List<CodeableConcept> modifier;
        @Binding(
            bindingName = "ProgramCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Program specific reason codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-program-code"
        )
        private final List<CodeableConcept> programCode;
        @Choice({ Date.class, Period.class })
        private final Element serviced;
        @ReferenceTarget({ "Location" })
        @Choice({ CodeableConcept.class, Address.class, Reference.class })
        @Binding(
            bindingName = "ServicePlace",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Place where the service is rendered.",
            valueSet = "http://hl7.org/fhir/ValueSet/service-place"
        )
        private final Element location;
        private final SimpleQuantity quantity;
        private final Money unitPrice;
        private final Decimal factor;
        private final Money net;
        @ReferenceTarget({ "Device" })
        private final List<Reference> udi;
        @Binding(
            bindingName = "OralSites",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The code for the teeth, quadrant, sextant and arch.",
            valueSet = "http://hl7.org/fhir/ValueSet/tooth"
        )
        private final CodeableConcept bodySite;
        @Binding(
            bindingName = "Surface",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The code for the tooth surface and surface combinations.",
            valueSet = "http://hl7.org/fhir/ValueSet/surface"
        )
        private final List<CodeableConcept> subSite;
        @ReferenceTarget({ "Encounter" })
        private final List<Reference> encounter;
        private final List<PositiveInt> noteNumber;
        private final List<Adjudication> adjudication;
        private final List<Detail> detail;

        private Item(Builder builder) {
            super(builder);
            sequence = builder.sequence;
            careTeamSequence = Collections.unmodifiableList(builder.careTeamSequence);
            diagnosisSequence = Collections.unmodifiableList(builder.diagnosisSequence);
            procedureSequence = Collections.unmodifiableList(builder.procedureSequence);
            informationSequence = Collections.unmodifiableList(builder.informationSequence);
            revenue = builder.revenue;
            category = builder.category;
            productOrService = builder.productOrService;
            modifier = Collections.unmodifiableList(builder.modifier);
            programCode = Collections.unmodifiableList(builder.programCode);
            serviced = builder.serviced;
            location = builder.location;
            quantity = builder.quantity;
            unitPrice = builder.unitPrice;
            factor = builder.factor;
            net = builder.net;
            udi = Collections.unmodifiableList(builder.udi);
            bodySite = builder.bodySite;
            subSite = Collections.unmodifiableList(builder.subSite);
            encounter = Collections.unmodifiableList(builder.encounter);
            noteNumber = Collections.unmodifiableList(builder.noteNumber);
            adjudication = Collections.unmodifiableList(builder.adjudication);
            detail = Collections.unmodifiableList(builder.detail);
        }

        /**
         * A number to uniquely identify item entries.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that is non-null.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * Care team members related to this service or product.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
         */
        public List<PositiveInt> getCareTeamSequence() {
            return careTeamSequence;
        }

        /**
         * Diagnoses applicable for this service or product.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
         */
        public List<PositiveInt> getDiagnosisSequence() {
            return diagnosisSequence;
        }

        /**
         * Procedures applicable for this service or product.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
         */
        public List<PositiveInt> getProcedureSequence() {
            return procedureSequence;
        }

        /**
         * Exceptions, special conditions and supporting information applicable for this service or product.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
         */
        public List<PositiveInt> getInformationSequence() {
            return informationSequence;
        }

        /**
         * The type of revenue or cost center providing the product and/or service.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRevenue() {
            return revenue;
        }

        /**
         * Code to identify the general type of benefits under which products and services are provided.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
         * product, service, drug or other billing code for the item.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getProductOrService() {
            return productOrService;
        }

        /**
         * Item typification or modifiers codes to convey additional context for the product or service.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getModifier() {
            return modifier;
        }

        /**
         * Identifies the program under which this may be recovered.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getProgramCode() {
            return programCode;
        }

        /**
         * The date or dates when the service or product was supplied, performed or completed.
         * 
         * @return
         *     An immutable object of type {@link Date} or {@link Period} that may be null.
         */
        public Element getServiced() {
            return serviced;
        }

        /**
         * Where the product or service was provided.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}, {@link Address} or {@link Reference} that may be null.
         */
        public Element getLocation() {
            return location;
        }

        /**
         * The number of repetitions of a service or product.
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getQuantity() {
            return quantity;
        }

        /**
         * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
         * for the details of the group.
         * 
         * @return
         *     An immutable object of type {@link Money} that may be null.
         */
        public Money getUnitPrice() {
            return unitPrice;
        }

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
         * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getFactor() {
            return factor;
        }

        /**
         * The quantity times the unit price for an additional service or product or charge.
         * 
         * @return
         *     An immutable object of type {@link Money} that may be null.
         */
        public Money getNet() {
            return net;
        }

        /**
         * Unique Device Identifiers associated with this line item.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getUdi() {
            return udi;
        }

        /**
         * Physical service site on the patient (limb, tooth, etc.).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getBodySite() {
            return bodySite;
        }

        /**
         * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getSubSite() {
            return subSite;
        }

        /**
         * A billed item may include goods or services provided in multiple encounters.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getEncounter() {
            return encounter;
        }

        /**
         * The numbers associated with notes below which apply to the adjudication of this item.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
         */
        public List<PositiveInt> getNoteNumber() {
            return noteNumber;
        }

        /**
         * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a 
         * simple product or service then this is the result of the adjudication of this item.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Adjudication} that may be empty.
         */
        public List<Adjudication> getAdjudication() {
            return adjudication;
        }

        /**
         * Second-tier of goods and services.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Detail} that may be empty.
         */
        public List<Detail> getDetail() {
            return detail;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (sequence != null) || 
                !careTeamSequence.isEmpty() || 
                !diagnosisSequence.isEmpty() || 
                !procedureSequence.isEmpty() || 
                !informationSequence.isEmpty() || 
                (revenue != null) || 
                (category != null) || 
                (productOrService != null) || 
                !modifier.isEmpty() || 
                !programCode.isEmpty() || 
                (serviced != null) || 
                (location != null) || 
                (quantity != null) || 
                (unitPrice != null) || 
                (factor != null) || 
                (net != null) || 
                !udi.isEmpty() || 
                (bodySite != null) || 
                !subSite.isEmpty() || 
                !encounter.isEmpty() || 
                !noteNumber.isEmpty() || 
                !adjudication.isEmpty() || 
                !detail.isEmpty();
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
                    accept(serviced, "serviced", visitor);
                    accept(location, "location", visitor);
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
            Item other = (Item) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(careTeamSequence, other.careTeamSequence) && 
                Objects.equals(diagnosisSequence, other.diagnosisSequence) && 
                Objects.equals(procedureSequence, other.procedureSequence) && 
                Objects.equals(informationSequence, other.informationSequence) && 
                Objects.equals(revenue, other.revenue) && 
                Objects.equals(category, other.category) && 
                Objects.equals(productOrService, other.productOrService) && 
                Objects.equals(modifier, other.modifier) && 
                Objects.equals(programCode, other.programCode) && 
                Objects.equals(serviced, other.serviced) && 
                Objects.equals(location, other.location) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(unitPrice, other.unitPrice) && 
                Objects.equals(factor, other.factor) && 
                Objects.equals(net, other.net) && 
                Objects.equals(udi, other.udi) && 
                Objects.equals(bodySite, other.bodySite) && 
                Objects.equals(subSite, other.subSite) && 
                Objects.equals(encounter, other.encounter) && 
                Objects.equals(noteNumber, other.noteNumber) && 
                Objects.equals(adjudication, other.adjudication) && 
                Objects.equals(detail, other.detail);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    sequence, 
                    careTeamSequence, 
                    diagnosisSequence, 
                    procedureSequence, 
                    informationSequence, 
                    revenue, 
                    category, 
                    productOrService, 
                    modifier, 
                    programCode, 
                    serviced, 
                    location, 
                    quantity, 
                    unitPrice, 
                    factor, 
                    net, 
                    udi, 
                    bodySite, 
                    subSite, 
                    encounter, 
                    noteNumber, 
                    adjudication, 
                    detail);
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
            private PositiveInt sequence;
            private List<PositiveInt> careTeamSequence = new ArrayList<>();
            private List<PositiveInt> diagnosisSequence = new ArrayList<>();
            private List<PositiveInt> procedureSequence = new ArrayList<>();
            private List<PositiveInt> informationSequence = new ArrayList<>();
            private CodeableConcept revenue;
            private CodeableConcept category;
            private CodeableConcept productOrService;
            private List<CodeableConcept> modifier = new ArrayList<>();
            private List<CodeableConcept> programCode = new ArrayList<>();
            private Element serviced;
            private Element location;
            private SimpleQuantity quantity;
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
             * A number to uniquely identify item entries.
             * 
             * <p>This element is required.
             * 
             * @param sequence
             *     Item instance identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequence(PositiveInt sequence) {
                this.sequence = sequence;
                return this;
            }

            /**
             * Care team members related to this service or product.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param careTeamSequence
             *     Applicable care team members
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder careTeamSequence(PositiveInt... careTeamSequence) {
                for (PositiveInt value : careTeamSequence) {
                    this.careTeamSequence.add(value);
                }
                return this;
            }

            /**
             * Care team members related to this service or product.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param careTeamSequence
             *     Applicable care team members
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder careTeamSequence(Collection<PositiveInt> careTeamSequence) {
                this.careTeamSequence = new ArrayList<>(careTeamSequence);
                return this;
            }

            /**
             * Diagnoses applicable for this service or product.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param diagnosisSequence
             *     Applicable diagnoses
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diagnosisSequence(PositiveInt... diagnosisSequence) {
                for (PositiveInt value : diagnosisSequence) {
                    this.diagnosisSequence.add(value);
                }
                return this;
            }

            /**
             * Diagnoses applicable for this service or product.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param diagnosisSequence
             *     Applicable diagnoses
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder diagnosisSequence(Collection<PositiveInt> diagnosisSequence) {
                this.diagnosisSequence = new ArrayList<>(diagnosisSequence);
                return this;
            }

            /**
             * Procedures applicable for this service or product.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param procedureSequence
             *     Applicable procedures
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder procedureSequence(PositiveInt... procedureSequence) {
                for (PositiveInt value : procedureSequence) {
                    this.procedureSequence.add(value);
                }
                return this;
            }

            /**
             * Procedures applicable for this service or product.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param procedureSequence
             *     Applicable procedures
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder procedureSequence(Collection<PositiveInt> procedureSequence) {
                this.procedureSequence = new ArrayList<>(procedureSequence);
                return this;
            }

            /**
             * Exceptions, special conditions and supporting information applicable for this service or product.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param informationSequence
             *     Applicable exception and supporting information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder informationSequence(PositiveInt... informationSequence) {
                for (PositiveInt value : informationSequence) {
                    this.informationSequence.add(value);
                }
                return this;
            }

            /**
             * Exceptions, special conditions and supporting information applicable for this service or product.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param informationSequence
             *     Applicable exception and supporting information
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder informationSequence(Collection<PositiveInt> informationSequence) {
                this.informationSequence = new ArrayList<>(informationSequence);
                return this;
            }

            /**
             * The type of revenue or cost center providing the product and/or service.
             * 
             * @param revenue
             *     Revenue or cost center code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder revenue(CodeableConcept revenue) {
                this.revenue = revenue;
                return this;
            }

            /**
             * Code to identify the general type of benefits under which products and services are provided.
             * 
             * @param category
             *     Benefit classification
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder category(CodeableConcept category) {
                this.category = category;
                return this;
            }

            /**
             * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
             * product, service, drug or other billing code for the item.
             * 
             * <p>This element is required.
             * 
             * @param productOrService
             *     Billing, service, product, or drug code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder productOrService(CodeableConcept productOrService) {
                this.productOrService = productOrService;
                return this;
            }

            /**
             * Item typification or modifiers codes to convey additional context for the product or service.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifier
             *     Product or service billing modifiers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder modifier(CodeableConcept... modifier) {
                for (CodeableConcept value : modifier) {
                    this.modifier.add(value);
                }
                return this;
            }

            /**
             * Item typification or modifiers codes to convey additional context for the product or service.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifier
             *     Product or service billing modifiers
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder modifier(Collection<CodeableConcept> modifier) {
                this.modifier = new ArrayList<>(modifier);
                return this;
            }

            /**
             * Identifies the program under which this may be recovered.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param programCode
             *     Program the product or service is provided under
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder programCode(CodeableConcept... programCode) {
                for (CodeableConcept value : programCode) {
                    this.programCode.add(value);
                }
                return this;
            }

            /**
             * Identifies the program under which this may be recovered.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param programCode
             *     Program the product or service is provided under
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder programCode(Collection<CodeableConcept> programCode) {
                this.programCode = new ArrayList<>(programCode);
                return this;
            }

            /**
             * Convenience method for setting {@code serviced} with choice type Date.
             * 
             * @param serviced
             *     Date or dates of service or product delivery
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #serviced(Element)
             */
            public Builder serviced(java.time.LocalDate serviced) {
                this.serviced = (serviced == null) ? null : Date.of(serviced);
                return this;
            }

            /**
             * The date or dates when the service or product was supplied, performed or completed.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Date}</li>
             * <li>{@link Period}</li>
             * </ul>
             * 
             * @param serviced
             *     Date or dates of service or product delivery
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder serviced(Element serviced) {
                this.serviced = serviced;
                return this;
            }

            /**
             * Where the product or service was provided.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Address}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link Location}</li>
             * </ul>
             * 
             * @param location
             *     Place of service or where product was supplied
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder location(Element location) {
                this.location = location;
                return this;
            }

            /**
             * The number of repetitions of a service or product.
             * 
             * @param quantity
             *     Count of products or services
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(SimpleQuantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
             * for the details of the group.
             * 
             * @param unitPrice
             *     Fee, charge or cost per item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder unitPrice(Money unitPrice) {
                this.unitPrice = unitPrice;
                return this;
            }

            /**
             * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
             * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
             * 
             * @param factor
             *     Price scaling factor
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder factor(Decimal factor) {
                this.factor = factor;
                return this;
            }

            /**
             * The quantity times the unit price for an additional service or product or charge.
             * 
             * @param net
             *     Total item cost
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder net(Money net) {
                this.net = net;
                return this;
            }

            /**
             * Unique Device Identifiers associated with this line item.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Device}</li>
             * </ul>
             * 
             * @param udi
             *     Unique device identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder udi(Reference... udi) {
                for (Reference value : udi) {
                    this.udi.add(value);
                }
                return this;
            }

            /**
             * Unique Device Identifiers associated with this line item.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Device}</li>
             * </ul>
             * 
             * @param udi
             *     Unique device identifier
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder udi(Collection<Reference> udi) {
                this.udi = new ArrayList<>(udi);
                return this;
            }

            /**
             * Physical service site on the patient (limb, tooth, etc.).
             * 
             * @param bodySite
             *     Anatomical location
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder bodySite(CodeableConcept bodySite) {
                this.bodySite = bodySite;
                return this;
            }

            /**
             * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param subSite
             *     Anatomical sub-location
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subSite(CodeableConcept... subSite) {
                for (CodeableConcept value : subSite) {
                    this.subSite.add(value);
                }
                return this;
            }

            /**
             * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param subSite
             *     Anatomical sub-location
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder subSite(Collection<CodeableConcept> subSite) {
                this.subSite = new ArrayList<>(subSite);
                return this;
            }

            /**
             * A billed item may include goods or services provided in multiple encounters.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Encounter}</li>
             * </ul>
             * 
             * @param encounter
             *     Encounters related to this billed item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder encounter(Reference... encounter) {
                for (Reference value : encounter) {
                    this.encounter.add(value);
                }
                return this;
            }

            /**
             * A billed item may include goods or services provided in multiple encounters.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Encounter}</li>
             * </ul>
             * 
             * @param encounter
             *     Encounters related to this billed item
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder encounter(Collection<Reference> encounter) {
                this.encounter = new ArrayList<>(encounter);
                return this;
            }

            /**
             * The numbers associated with notes below which apply to the adjudication of this item.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder noteNumber(PositiveInt... noteNumber) {
                for (PositiveInt value : noteNumber) {
                    this.noteNumber.add(value);
                }
                return this;
            }

            /**
             * The numbers associated with notes below which apply to the adjudication of this item.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                this.noteNumber = new ArrayList<>(noteNumber);
                return this;
            }

            /**
             * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a 
             * simple product or service then this is the result of the adjudication of this item.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param adjudication
             *     Adjudication details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder adjudication(Adjudication... adjudication) {
                for (Adjudication value : adjudication) {
                    this.adjudication.add(value);
                }
                return this;
            }

            /**
             * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a 
             * simple product or service then this is the result of the adjudication of this item.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param adjudication
             *     Adjudication details
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder adjudication(Collection<Adjudication> adjudication) {
                this.adjudication = new ArrayList<>(adjudication);
                return this;
            }

            /**
             * Second-tier of goods and services.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param detail
             *     Additional items
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Detail... detail) {
                for (Detail value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * Second-tier of goods and services.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param detail
             *     Additional items
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder detail(Collection<Detail> detail) {
                this.detail = new ArrayList<>(detail);
                return this;
            }

            /**
             * Build the {@link Item}
             * 
             * <p>Required elements:
             * <ul>
             * <li>sequence</li>
             * <li>productOrService</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Item}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Item per the base specification
             */
            @Override
            public Item build() {
                Item item = new Item(this);
                if (validating) {
                    validate(item);
                }
                return item;
            }

            protected void validate(Item item) {
                super.validate(item);
                ValidationSupport.requireNonNull(item.sequence, "sequence");
                ValidationSupport.checkList(item.careTeamSequence, "careTeamSequence", PositiveInt.class);
                ValidationSupport.checkList(item.diagnosisSequence, "diagnosisSequence", PositiveInt.class);
                ValidationSupport.checkList(item.procedureSequence, "procedureSequence", PositiveInt.class);
                ValidationSupport.checkList(item.informationSequence, "informationSequence", PositiveInt.class);
                ValidationSupport.requireNonNull(item.productOrService, "productOrService");
                ValidationSupport.checkList(item.modifier, "modifier", CodeableConcept.class);
                ValidationSupport.checkList(item.programCode, "programCode", CodeableConcept.class);
                ValidationSupport.choiceElement(item.serviced, "serviced", Date.class, Period.class);
                ValidationSupport.choiceElement(item.location, "location", CodeableConcept.class, Address.class, Reference.class);
                ValidationSupport.checkList(item.udi, "udi", Reference.class);
                ValidationSupport.checkList(item.subSite, "subSite", CodeableConcept.class);
                ValidationSupport.checkList(item.encounter, "encounter", Reference.class);
                ValidationSupport.checkList(item.noteNumber, "noteNumber", PositiveInt.class);
                ValidationSupport.checkList(item.adjudication, "adjudication", Adjudication.class);
                ValidationSupport.checkList(item.detail, "detail", Detail.class);
                ValidationSupport.checkReferenceType(item.location, "location", "Location");
                ValidationSupport.checkReferenceType(item.udi, "udi", "Device");
                ValidationSupport.checkReferenceType(item.encounter, "encounter", "Encounter");
                ValidationSupport.requireValueOrChildren(item);
            }

            protected Builder from(Item item) {
                super.from(item);
                sequence = item.sequence;
                careTeamSequence.addAll(item.careTeamSequence);
                diagnosisSequence.addAll(item.diagnosisSequence);
                procedureSequence.addAll(item.procedureSequence);
                informationSequence.addAll(item.informationSequence);
                revenue = item.revenue;
                category = item.category;
                productOrService = item.productOrService;
                modifier.addAll(item.modifier);
                programCode.addAll(item.programCode);
                serviced = item.serviced;
                location = item.location;
                quantity = item.quantity;
                unitPrice = item.unitPrice;
                factor = item.factor;
                net = item.net;
                udi.addAll(item.udi);
                bodySite = item.bodySite;
                subSite.addAll(item.subSite);
                encounter.addAll(item.encounter);
                noteNumber.addAll(item.noteNumber);
                adjudication.addAll(item.adjudication);
                detail.addAll(item.detail);
                return this;
            }
        }

        /**
         * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a 
         * simple product or service then this is the result of the adjudication of this item.
         */
        public static class Adjudication extends BackboneElement {
            @Binding(
                bindingName = "Adjudication",
                strength = BindingStrength.Value.EXAMPLE,
                description = "The adjudication codes.",
                valueSet = "http://hl7.org/fhir/ValueSet/adjudication"
            )
            @Required
            private final CodeableConcept category;
            @Binding(
                bindingName = "AdjudicationReason",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Adjudication reason codes.",
                valueSet = "http://hl7.org/fhir/ValueSet/adjudication-reason"
            )
            private final CodeableConcept reason;
            private final Money amount;
            private final Decimal value;

            private Adjudication(Builder builder) {
                super(builder);
                category = builder.category;
                reason = builder.reason;
                amount = builder.amount;
                value = builder.value;
            }

            /**
             * A code to indicate the information type of this adjudication record. Information types may include: the value 
             * submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible 
             * for in-aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getCategory() {
                return category;
            }

            /**
             * A code supporting the understanding of the adjudication result and explaining variance from expected amount.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getReason() {
                return reason;
            }

            /**
             * Monetary amount associated with the category.
             * 
             * @return
             *     An immutable object of type {@link Money} that may be null.
             */
            public Money getAmount() {
                return amount;
            }

            /**
             * A non-monetary value associated with the category. Mutually exclusive to the amount element above.
             * 
             * @return
             *     An immutable object of type {@link Decimal} that may be null.
             */
            public Decimal getValue() {
                return value;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (category != null) || 
                    (reason != null) || 
                    (amount != null) || 
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
                        accept(category, "category", visitor);
                        accept(reason, "reason", visitor);
                        accept(amount, "amount", visitor);
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
                Adjudication other = (Adjudication) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(category, other.category) && 
                    Objects.equals(reason, other.reason) && 
                    Objects.equals(amount, other.amount) && 
                    Objects.equals(value, other.value);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        category, 
                        reason, 
                        amount, 
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
                private CodeableConcept category;
                private CodeableConcept reason;
                private Money amount;
                private Decimal value;

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
                 * A code to indicate the information type of this adjudication record. Information types may include: the value 
                 * submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible 
                 * for in-aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.
                 * 
                 * <p>This element is required.
                 * 
                 * @param category
                 *     Type of adjudication information
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder category(CodeableConcept category) {
                    this.category = category;
                    return this;
                }

                /**
                 * A code supporting the understanding of the adjudication result and explaining variance from expected amount.
                 * 
                 * @param reason
                 *     Explanation of adjudication outcome
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reason(CodeableConcept reason) {
                    this.reason = reason;
                    return this;
                }

                /**
                 * Monetary amount associated with the category.
                 * 
                 * @param amount
                 *     Monetary amount
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder amount(Money amount) {
                    this.amount = amount;
                    return this;
                }

                /**
                 * A non-monetary value associated with the category. Mutually exclusive to the amount element above.
                 * 
                 * @param value
                 *     Non-monitary value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Decimal value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Build the {@link Adjudication}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>category</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Adjudication}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Adjudication per the base specification
                 */
                @Override
                public Adjudication build() {
                    Adjudication adjudication = new Adjudication(this);
                    if (validating) {
                        validate(adjudication);
                    }
                    return adjudication;
                }

                protected void validate(Adjudication adjudication) {
                    super.validate(adjudication);
                    ValidationSupport.requireNonNull(adjudication.category, "category");
                    ValidationSupport.requireValueOrChildren(adjudication);
                }

                protected Builder from(Adjudication adjudication) {
                    super.from(adjudication);
                    category = adjudication.category;
                    reason = adjudication.reason;
                    amount = adjudication.amount;
                    value = adjudication.value;
                    return this;
                }
            }
        }

        /**
         * Second-tier of goods and services.
         */
        public static class Detail extends BackboneElement {
            @Required
            private final PositiveInt sequence;
            @Binding(
                bindingName = "RevenueCenter",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Codes for the revenue or cost centers supplying the service and/or products.",
                valueSet = "http://hl7.org/fhir/ValueSet/ex-revenue-center"
            )
            private final CodeableConcept revenue;
            @Binding(
                bindingName = "BenefitCategory",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Benefit categories such as: oral, medical, vision, oral-basic etc.",
                valueSet = "http://hl7.org/fhir/ValueSet/ex-benefitcategory"
            )
            private final CodeableConcept category;
            @Binding(
                bindingName = "ServiceProduct",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Allowable service and product codes.",
                valueSet = "http://hl7.org/fhir/ValueSet/service-uscls"
            )
            @Required
            private final CodeableConcept productOrService;
            @Binding(
                bindingName = "Modifiers",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Item type or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.",
                valueSet = "http://hl7.org/fhir/ValueSet/claim-modifiers"
            )
            private final List<CodeableConcept> modifier;
            @Binding(
                bindingName = "ProgramCode",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Program specific reason codes.",
                valueSet = "http://hl7.org/fhir/ValueSet/ex-program-code"
            )
            private final List<CodeableConcept> programCode;
            private final SimpleQuantity quantity;
            private final Money unitPrice;
            private final Decimal factor;
            private final Money net;
            @ReferenceTarget({ "Device" })
            private final List<Reference> udi;
            private final List<PositiveInt> noteNumber;
            private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;
            private final List<SubDetail> subDetail;

            private Detail(Builder builder) {
                super(builder);
                sequence = builder.sequence;
                revenue = builder.revenue;
                category = builder.category;
                productOrService = builder.productOrService;
                modifier = Collections.unmodifiableList(builder.modifier);
                programCode = Collections.unmodifiableList(builder.programCode);
                quantity = builder.quantity;
                unitPrice = builder.unitPrice;
                factor = builder.factor;
                net = builder.net;
                udi = Collections.unmodifiableList(builder.udi);
                noteNumber = Collections.unmodifiableList(builder.noteNumber);
                adjudication = Collections.unmodifiableList(builder.adjudication);
                subDetail = Collections.unmodifiableList(builder.subDetail);
            }

            /**
             * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
             * 
             * @return
             *     An immutable object of type {@link PositiveInt} that is non-null.
             */
            public PositiveInt getSequence() {
                return sequence;
            }

            /**
             * The type of revenue or cost center providing the product and/or service.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getRevenue() {
                return revenue;
            }

            /**
             * Code to identify the general type of benefits under which products and services are provided.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getCategory() {
                return category;
            }

            /**
             * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
             * product, service, drug or other billing code for the item.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getProductOrService() {
                return productOrService;
            }

            /**
             * Item typification or modifiers codes to convey additional context for the product or service.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getModifier() {
                return modifier;
            }

            /**
             * Identifies the program under which this may be recovered.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getProgramCode() {
                return programCode;
            }

            /**
             * The number of repetitions of a service or product.
             * 
             * @return
             *     An immutable object of type {@link SimpleQuantity} that may be null.
             */
            public SimpleQuantity getQuantity() {
                return quantity;
            }

            /**
             * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
             * for the details of the group.
             * 
             * @return
             *     An immutable object of type {@link Money} that may be null.
             */
            public Money getUnitPrice() {
                return unitPrice;
            }

            /**
             * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
             * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
             * 
             * @return
             *     An immutable object of type {@link Decimal} that may be null.
             */
            public Decimal getFactor() {
                return factor;
            }

            /**
             * The quantity times the unit price for an additional service or product or charge.
             * 
             * @return
             *     An immutable object of type {@link Money} that may be null.
             */
            public Money getNet() {
                return net;
            }

            /**
             * Unique Device Identifiers associated with this line item.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
             */
            public List<Reference> getUdi() {
                return udi;
            }

            /**
             * The numbers associated with notes below which apply to the adjudication of this item.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
             */
            public List<PositiveInt> getNoteNumber() {
                return noteNumber;
            }

            /**
             * The adjudication results.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Adjudication} that may be empty.
             */
            public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
                return adjudication;
            }

            /**
             * Third-tier of goods and services.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link SubDetail} that may be empty.
             */
            public List<SubDetail> getSubDetail() {
                return subDetail;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (sequence != null) || 
                    (revenue != null) || 
                    (category != null) || 
                    (productOrService != null) || 
                    !modifier.isEmpty() || 
                    !programCode.isEmpty() || 
                    (quantity != null) || 
                    (unitPrice != null) || 
                    (factor != null) || 
                    (net != null) || 
                    !udi.isEmpty() || 
                    !noteNumber.isEmpty() || 
                    !adjudication.isEmpty() || 
                    !subDetail.isEmpty();
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
                Detail other = (Detail) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(sequence, other.sequence) && 
                    Objects.equals(revenue, other.revenue) && 
                    Objects.equals(category, other.category) && 
                    Objects.equals(productOrService, other.productOrService) && 
                    Objects.equals(modifier, other.modifier) && 
                    Objects.equals(programCode, other.programCode) && 
                    Objects.equals(quantity, other.quantity) && 
                    Objects.equals(unitPrice, other.unitPrice) && 
                    Objects.equals(factor, other.factor) && 
                    Objects.equals(net, other.net) && 
                    Objects.equals(udi, other.udi) && 
                    Objects.equals(noteNumber, other.noteNumber) && 
                    Objects.equals(adjudication, other.adjudication) && 
                    Objects.equals(subDetail, other.subDetail);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        sequence, 
                        revenue, 
                        category, 
                        productOrService, 
                        modifier, 
                        programCode, 
                        quantity, 
                        unitPrice, 
                        factor, 
                        net, 
                        udi, 
                        noteNumber, 
                        adjudication, 
                        subDetail);
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
                private PositiveInt sequence;
                private CodeableConcept revenue;
                private CodeableConcept category;
                private CodeableConcept productOrService;
                private List<CodeableConcept> modifier = new ArrayList<>();
                private List<CodeableConcept> programCode = new ArrayList<>();
                private SimpleQuantity quantity;
                private Money unitPrice;
                private Decimal factor;
                private Money net;
                private List<Reference> udi = new ArrayList<>();
                private List<PositiveInt> noteNumber = new ArrayList<>();
                private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();
                private List<SubDetail> subDetail = new ArrayList<>();

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
                 * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
                 * 
                 * <p>This element is required.
                 * 
                 * @param sequence
                 *     Product or service provided
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder sequence(PositiveInt sequence) {
                    this.sequence = sequence;
                    return this;
                }

                /**
                 * The type of revenue or cost center providing the product and/or service.
                 * 
                 * @param revenue
                 *     Revenue or cost center code
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder revenue(CodeableConcept revenue) {
                    this.revenue = revenue;
                    return this;
                }

                /**
                 * Code to identify the general type of benefits under which products and services are provided.
                 * 
                 * @param category
                 *     Benefit classification
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder category(CodeableConcept category) {
                    this.category = category;
                    return this;
                }

                /**
                 * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
                 * product, service, drug or other billing code for the item.
                 * 
                 * <p>This element is required.
                 * 
                 * @param productOrService
                 *     Billing, service, product, or drug code
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder productOrService(CodeableConcept productOrService) {
                    this.productOrService = productOrService;
                    return this;
                }

                /**
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifier
                 *     Service/Product billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder modifier(CodeableConcept... modifier) {
                    for (CodeableConcept value : modifier) {
                        this.modifier.add(value);
                    }
                    return this;
                }

                /**
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifier
                 *     Service/Product billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder modifier(Collection<CodeableConcept> modifier) {
                    this.modifier = new ArrayList<>(modifier);
                    return this;
                }

                /**
                 * Identifies the program under which this may be recovered.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param programCode
                 *     Program the product or service is provided under
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder programCode(CodeableConcept... programCode) {
                    for (CodeableConcept value : programCode) {
                        this.programCode.add(value);
                    }
                    return this;
                }

                /**
                 * Identifies the program under which this may be recovered.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param programCode
                 *     Program the product or service is provided under
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder programCode(Collection<CodeableConcept> programCode) {
                    this.programCode = new ArrayList<>(programCode);
                    return this;
                }

                /**
                 * The number of repetitions of a service or product.
                 * 
                 * @param quantity
                 *     Count of products or services
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder quantity(SimpleQuantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                 * for the details of the group.
                 * 
                 * @param unitPrice
                 *     Fee, charge or cost per item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder unitPrice(Money unitPrice) {
                    this.unitPrice = unitPrice;
                    return this;
                }

                /**
                 * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                 * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                 * 
                 * @param factor
                 *     Price scaling factor
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder factor(Decimal factor) {
                    this.factor = factor;
                    return this;
                }

                /**
                 * The quantity times the unit price for an additional service or product or charge.
                 * 
                 * @param net
                 *     Total item cost
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder net(Money net) {
                    this.net = net;
                    return this;
                }

                /**
                 * Unique Device Identifiers associated with this line item.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * <p>Allowed resource types for the references:
                 * <ul>
                 * <li>{@link Device}</li>
                 * </ul>
                 * 
                 * @param udi
                 *     Unique device identifier
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder udi(Reference... udi) {
                    for (Reference value : udi) {
                        this.udi.add(value);
                    }
                    return this;
                }

                /**
                 * Unique Device Identifiers associated with this line item.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * <p>Allowed resource types for the references:
                 * <ul>
                 * <li>{@link Device}</li>
                 * </ul>
                 * 
                 * @param udi
                 *     Unique device identifier
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder udi(Collection<Reference> udi) {
                    this.udi = new ArrayList<>(udi);
                    return this;
                }

                /**
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder noteNumber(PositiveInt... noteNumber) {
                    for (PositiveInt value : noteNumber) {
                        this.noteNumber.add(value);
                    }
                    return this;
                }

                /**
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                    this.noteNumber = new ArrayList<>(noteNumber);
                    return this;
                }

                /**
                 * The adjudication results.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param adjudication
                 *     Detail level adjudication details
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
                    for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                        this.adjudication.add(value);
                    }
                    return this;
                }

                /**
                 * The adjudication results.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param adjudication
                 *     Detail level adjudication details
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
                    this.adjudication = new ArrayList<>(adjudication);
                    return this;
                }

                /**
                 * Third-tier of goods and services.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param subDetail
                 *     Additional items
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder subDetail(SubDetail... subDetail) {
                    for (SubDetail value : subDetail) {
                        this.subDetail.add(value);
                    }
                    return this;
                }

                /**
                 * Third-tier of goods and services.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param subDetail
                 *     Additional items
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder subDetail(Collection<SubDetail> subDetail) {
                    this.subDetail = new ArrayList<>(subDetail);
                    return this;
                }

                /**
                 * Build the {@link Detail}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>sequence</li>
                 * <li>productOrService</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Detail}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Detail per the base specification
                 */
                @Override
                public Detail build() {
                    Detail detail = new Detail(this);
                    if (validating) {
                        validate(detail);
                    }
                    return detail;
                }

                protected void validate(Detail detail) {
                    super.validate(detail);
                    ValidationSupport.requireNonNull(detail.sequence, "sequence");
                    ValidationSupport.requireNonNull(detail.productOrService, "productOrService");
                    ValidationSupport.checkList(detail.modifier, "modifier", CodeableConcept.class);
                    ValidationSupport.checkList(detail.programCode, "programCode", CodeableConcept.class);
                    ValidationSupport.checkList(detail.udi, "udi", Reference.class);
                    ValidationSupport.checkList(detail.noteNumber, "noteNumber", PositiveInt.class);
                    ValidationSupport.checkList(detail.adjudication, "adjudication", ExplanationOfBenefit.Item.Adjudication.class);
                    ValidationSupport.checkList(detail.subDetail, "subDetail", SubDetail.class);
                    ValidationSupport.checkReferenceType(detail.udi, "udi", "Device");
                    ValidationSupport.requireValueOrChildren(detail);
                }

                protected Builder from(Detail detail) {
                    super.from(detail);
                    sequence = detail.sequence;
                    revenue = detail.revenue;
                    category = detail.category;
                    productOrService = detail.productOrService;
                    modifier.addAll(detail.modifier);
                    programCode.addAll(detail.programCode);
                    quantity = detail.quantity;
                    unitPrice = detail.unitPrice;
                    factor = detail.factor;
                    net = detail.net;
                    udi.addAll(detail.udi);
                    noteNumber.addAll(detail.noteNumber);
                    adjudication.addAll(detail.adjudication);
                    subDetail.addAll(detail.subDetail);
                    return this;
                }
            }

            /**
             * Third-tier of goods and services.
             */
            public static class SubDetail extends BackboneElement {
                @Required
                private final PositiveInt sequence;
                @Binding(
                    bindingName = "RevenueCenter",
                    strength = BindingStrength.Value.EXAMPLE,
                    description = "Codes for the revenue or cost centers supplying the service and/or products.",
                    valueSet = "http://hl7.org/fhir/ValueSet/ex-revenue-center"
                )
                private final CodeableConcept revenue;
                @Binding(
                    bindingName = "BenefitCategory",
                    strength = BindingStrength.Value.EXAMPLE,
                    description = "Benefit categories such as: oral, medical, vision, oral-basic etc.",
                    valueSet = "http://hl7.org/fhir/ValueSet/ex-benefitcategory"
                )
                private final CodeableConcept category;
                @Binding(
                    bindingName = "ServiceProduct",
                    strength = BindingStrength.Value.EXAMPLE,
                    description = "Allowable service and product codes.",
                    valueSet = "http://hl7.org/fhir/ValueSet/service-uscls"
                )
                @Required
                private final CodeableConcept productOrService;
                @Binding(
                    bindingName = "Modifiers",
                    strength = BindingStrength.Value.EXAMPLE,
                    description = "Item type or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.",
                    valueSet = "http://hl7.org/fhir/ValueSet/claim-modifiers"
                )
                private final List<CodeableConcept> modifier;
                @Binding(
                    bindingName = "ProgramCode",
                    strength = BindingStrength.Value.EXAMPLE,
                    description = "Program specific reason codes.",
                    valueSet = "http://hl7.org/fhir/ValueSet/ex-program-code"
                )
                private final List<CodeableConcept> programCode;
                private final SimpleQuantity quantity;
                private final Money unitPrice;
                private final Decimal factor;
                private final Money net;
                @ReferenceTarget({ "Device" })
                private final List<Reference> udi;
                private final List<PositiveInt> noteNumber;
                private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;

                private SubDetail(Builder builder) {
                    super(builder);
                    sequence = builder.sequence;
                    revenue = builder.revenue;
                    category = builder.category;
                    productOrService = builder.productOrService;
                    modifier = Collections.unmodifiableList(builder.modifier);
                    programCode = Collections.unmodifiableList(builder.programCode);
                    quantity = builder.quantity;
                    unitPrice = builder.unitPrice;
                    factor = builder.factor;
                    net = builder.net;
                    udi = Collections.unmodifiableList(builder.udi);
                    noteNumber = Collections.unmodifiableList(builder.noteNumber);
                    adjudication = Collections.unmodifiableList(builder.adjudication);
                }

                /**
                 * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
                 * 
                 * @return
                 *     An immutable object of type {@link PositiveInt} that is non-null.
                 */
                public PositiveInt getSequence() {
                    return sequence;
                }

                /**
                 * The type of revenue or cost center providing the product and/or service.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that may be null.
                 */
                public CodeableConcept getRevenue() {
                    return revenue;
                }

                /**
                 * Code to identify the general type of benefits under which products and services are provided.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that may be null.
                 */
                public CodeableConcept getCategory() {
                    return category;
                }

                /**
                 * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
                 * product, service, drug or other billing code for the item.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that is non-null.
                 */
                public CodeableConcept getProductOrService() {
                    return productOrService;
                }

                /**
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
                 */
                public List<CodeableConcept> getModifier() {
                    return modifier;
                }

                /**
                 * Identifies the program under which this may be recovered.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
                 */
                public List<CodeableConcept> getProgramCode() {
                    return programCode;
                }

                /**
                 * The number of repetitions of a service or product.
                 * 
                 * @return
                 *     An immutable object of type {@link SimpleQuantity} that may be null.
                 */
                public SimpleQuantity getQuantity() {
                    return quantity;
                }

                /**
                 * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                 * for the details of the group.
                 * 
                 * @return
                 *     An immutable object of type {@link Money} that may be null.
                 */
                public Money getUnitPrice() {
                    return unitPrice;
                }

                /**
                 * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                 * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                 * 
                 * @return
                 *     An immutable object of type {@link Decimal} that may be null.
                 */
                public Decimal getFactor() {
                    return factor;
                }

                /**
                 * The quantity times the unit price for an additional service or product or charge.
                 * 
                 * @return
                 *     An immutable object of type {@link Money} that may be null.
                 */
                public Money getNet() {
                    return net;
                }

                /**
                 * Unique Device Identifiers associated with this line item.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
                 */
                public List<Reference> getUdi() {
                    return udi;
                }

                /**
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
                 */
                public List<PositiveInt> getNoteNumber() {
                    return noteNumber;
                }

                /**
                 * The adjudication results.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Adjudication} that may be empty.
                 */
                public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
                    return adjudication;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (sequence != null) || 
                        (revenue != null) || 
                        (category != null) || 
                        (productOrService != null) || 
                        !modifier.isEmpty() || 
                        !programCode.isEmpty() || 
                        (quantity != null) || 
                        (unitPrice != null) || 
                        (factor != null) || 
                        (net != null) || 
                        !udi.isEmpty() || 
                        !noteNumber.isEmpty() || 
                        !adjudication.isEmpty();
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
                    SubDetail other = (SubDetail) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(sequence, other.sequence) && 
                        Objects.equals(revenue, other.revenue) && 
                        Objects.equals(category, other.category) && 
                        Objects.equals(productOrService, other.productOrService) && 
                        Objects.equals(modifier, other.modifier) && 
                        Objects.equals(programCode, other.programCode) && 
                        Objects.equals(quantity, other.quantity) && 
                        Objects.equals(unitPrice, other.unitPrice) && 
                        Objects.equals(factor, other.factor) && 
                        Objects.equals(net, other.net) && 
                        Objects.equals(udi, other.udi) && 
                        Objects.equals(noteNumber, other.noteNumber) && 
                        Objects.equals(adjudication, other.adjudication);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            sequence, 
                            revenue, 
                            category, 
                            productOrService, 
                            modifier, 
                            programCode, 
                            quantity, 
                            unitPrice, 
                            factor, 
                            net, 
                            udi, 
                            noteNumber, 
                            adjudication);
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
                    private PositiveInt sequence;
                    private CodeableConcept revenue;
                    private CodeableConcept category;
                    private CodeableConcept productOrService;
                    private List<CodeableConcept> modifier = new ArrayList<>();
                    private List<CodeableConcept> programCode = new ArrayList<>();
                    private SimpleQuantity quantity;
                    private Money unitPrice;
                    private Decimal factor;
                    private Money net;
                    private List<Reference> udi = new ArrayList<>();
                    private List<PositiveInt> noteNumber = new ArrayList<>();
                    private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();

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
                     * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
                     * 
                     * <p>This element is required.
                     * 
                     * @param sequence
                     *     Product or service provided
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder sequence(PositiveInt sequence) {
                        this.sequence = sequence;
                        return this;
                    }

                    /**
                     * The type of revenue or cost center providing the product and/or service.
                     * 
                     * @param revenue
                     *     Revenue or cost center code
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder revenue(CodeableConcept revenue) {
                        this.revenue = revenue;
                        return this;
                    }

                    /**
                     * Code to identify the general type of benefits under which products and services are provided.
                     * 
                     * @param category
                     *     Benefit classification
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder category(CodeableConcept category) {
                        this.category = category;
                        return this;
                    }

                    /**
                     * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
                     * product, service, drug or other billing code for the item.
                     * 
                     * <p>This element is required.
                     * 
                     * @param productOrService
                     *     Billing, service, product, or drug code
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder productOrService(CodeableConcept productOrService) {
                        this.productOrService = productOrService;
                        return this;
                    }

                    /**
                     * Item typification or modifiers codes to convey additional context for the product or service.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param modifier
                     *     Service/Product billing modifiers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder modifier(CodeableConcept... modifier) {
                        for (CodeableConcept value : modifier) {
                            this.modifier.add(value);
                        }
                        return this;
                    }

                    /**
                     * Item typification or modifiers codes to convey additional context for the product or service.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param modifier
                     *     Service/Product billing modifiers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder modifier(Collection<CodeableConcept> modifier) {
                        this.modifier = new ArrayList<>(modifier);
                        return this;
                    }

                    /**
                     * Identifies the program under which this may be recovered.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param programCode
                     *     Program the product or service is provided under
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder programCode(CodeableConcept... programCode) {
                        for (CodeableConcept value : programCode) {
                            this.programCode.add(value);
                        }
                        return this;
                    }

                    /**
                     * Identifies the program under which this may be recovered.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param programCode
                     *     Program the product or service is provided under
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder programCode(Collection<CodeableConcept> programCode) {
                        this.programCode = new ArrayList<>(programCode);
                        return this;
                    }

                    /**
                     * The number of repetitions of a service or product.
                     * 
                     * @param quantity
                     *     Count of products or services
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder quantity(SimpleQuantity quantity) {
                        this.quantity = quantity;
                        return this;
                    }

                    /**
                     * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                     * for the details of the group.
                     * 
                     * @param unitPrice
                     *     Fee, charge or cost per item
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder unitPrice(Money unitPrice) {
                        this.unitPrice = unitPrice;
                        return this;
                    }

                    /**
                     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                     * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                     * 
                     * @param factor
                     *     Price scaling factor
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder factor(Decimal factor) {
                        this.factor = factor;
                        return this;
                    }

                    /**
                     * The quantity times the unit price for an additional service or product or charge.
                     * 
                     * @param net
                     *     Total item cost
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder net(Money net) {
                        this.net = net;
                        return this;
                    }

                    /**
                     * Unique Device Identifiers associated with this line item.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * <p>Allowed resource types for the references:
                     * <ul>
                     * <li>{@link Device}</li>
                     * </ul>
                     * 
                     * @param udi
                     *     Unique device identifier
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder udi(Reference... udi) {
                        for (Reference value : udi) {
                            this.udi.add(value);
                        }
                        return this;
                    }

                    /**
                     * Unique Device Identifiers associated with this line item.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * <p>Allowed resource types for the references:
                     * <ul>
                     * <li>{@link Device}</li>
                     * </ul>
                     * 
                     * @param udi
                     *     Unique device identifier
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder udi(Collection<Reference> udi) {
                        this.udi = new ArrayList<>(udi);
                        return this;
                    }

                    /**
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder noteNumber(PositiveInt... noteNumber) {
                        for (PositiveInt value : noteNumber) {
                            this.noteNumber.add(value);
                        }
                        return this;
                    }

                    /**
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                        this.noteNumber = new ArrayList<>(noteNumber);
                        return this;
                    }

                    /**
                     * The adjudication results.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param adjudication
                     *     Subdetail level adjudication details
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
                        for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                            this.adjudication.add(value);
                        }
                        return this;
                    }

                    /**
                     * The adjudication results.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param adjudication
                     *     Subdetail level adjudication details
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
                        this.adjudication = new ArrayList<>(adjudication);
                        return this;
                    }

                    /**
                     * Build the {@link SubDetail}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>sequence</li>
                     * <li>productOrService</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link SubDetail}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid SubDetail per the base specification
                     */
                    @Override
                    public SubDetail build() {
                        SubDetail subDetail = new SubDetail(this);
                        if (validating) {
                            validate(subDetail);
                        }
                        return subDetail;
                    }

                    protected void validate(SubDetail subDetail) {
                        super.validate(subDetail);
                        ValidationSupport.requireNonNull(subDetail.sequence, "sequence");
                        ValidationSupport.requireNonNull(subDetail.productOrService, "productOrService");
                        ValidationSupport.checkList(subDetail.modifier, "modifier", CodeableConcept.class);
                        ValidationSupport.checkList(subDetail.programCode, "programCode", CodeableConcept.class);
                        ValidationSupport.checkList(subDetail.udi, "udi", Reference.class);
                        ValidationSupport.checkList(subDetail.noteNumber, "noteNumber", PositiveInt.class);
                        ValidationSupport.checkList(subDetail.adjudication, "adjudication", ExplanationOfBenefit.Item.Adjudication.class);
                        ValidationSupport.checkReferenceType(subDetail.udi, "udi", "Device");
                        ValidationSupport.requireValueOrChildren(subDetail);
                    }

                    protected Builder from(SubDetail subDetail) {
                        super.from(subDetail);
                        sequence = subDetail.sequence;
                        revenue = subDetail.revenue;
                        category = subDetail.category;
                        productOrService = subDetail.productOrService;
                        modifier.addAll(subDetail.modifier);
                        programCode.addAll(subDetail.programCode);
                        quantity = subDetail.quantity;
                        unitPrice = subDetail.unitPrice;
                        factor = subDetail.factor;
                        net = subDetail.net;
                        udi.addAll(subDetail.udi);
                        noteNumber.addAll(subDetail.noteNumber);
                        adjudication.addAll(subDetail.adjudication);
                        return this;
                    }
                }
            }
        }
    }

    /**
     * The first-tier service adjudications for payor added product or service lines.
     */
    public static class AddItem extends BackboneElement {
        private final List<PositiveInt> itemSequence;
        private final List<PositiveInt> detailSequence;
        private final List<PositiveInt> subDetailSequence;
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
        private final List<Reference> provider;
        @Binding(
            bindingName = "ServiceProduct",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Allowable service and product codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/service-uscls"
        )
        @Required
        private final CodeableConcept productOrService;
        @Binding(
            bindingName = "Modifiers",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Item type or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.",
            valueSet = "http://hl7.org/fhir/ValueSet/claim-modifiers"
        )
        private final List<CodeableConcept> modifier;
        @Binding(
            bindingName = "ProgramCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Program specific reason codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-program-code"
        )
        private final List<CodeableConcept> programCode;
        @Choice({ Date.class, Period.class })
        private final Element serviced;
        @ReferenceTarget({ "Location" })
        @Choice({ CodeableConcept.class, Address.class, Reference.class })
        @Binding(
            bindingName = "ServicePlace",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Place where the service is rendered.",
            valueSet = "http://hl7.org/fhir/ValueSet/service-place"
        )
        private final Element location;
        private final SimpleQuantity quantity;
        private final Money unitPrice;
        private final Decimal factor;
        private final Money net;
        @Binding(
            bindingName = "OralSites",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The code for the teeth, quadrant, sextant and arch.",
            valueSet = "http://hl7.org/fhir/ValueSet/tooth"
        )
        private final CodeableConcept bodySite;
        @Binding(
            bindingName = "Surface",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The code for the tooth surface and surface combinations.",
            valueSet = "http://hl7.org/fhir/ValueSet/surface"
        )
        private final List<CodeableConcept> subSite;
        private final List<PositiveInt> noteNumber;
        private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;
        private final List<Detail> detail;

        private AddItem(Builder builder) {
            super(builder);
            itemSequence = Collections.unmodifiableList(builder.itemSequence);
            detailSequence = Collections.unmodifiableList(builder.detailSequence);
            subDetailSequence = Collections.unmodifiableList(builder.subDetailSequence);
            provider = Collections.unmodifiableList(builder.provider);
            productOrService = builder.productOrService;
            modifier = Collections.unmodifiableList(builder.modifier);
            programCode = Collections.unmodifiableList(builder.programCode);
            serviced = builder.serviced;
            location = builder.location;
            quantity = builder.quantity;
            unitPrice = builder.unitPrice;
            factor = builder.factor;
            net = builder.net;
            bodySite = builder.bodySite;
            subSite = Collections.unmodifiableList(builder.subSite);
            noteNumber = Collections.unmodifiableList(builder.noteNumber);
            adjudication = Collections.unmodifiableList(builder.adjudication);
            detail = Collections.unmodifiableList(builder.detail);
        }

        /**
         * Claim items which this service line is intended to replace.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
         */
        public List<PositiveInt> getItemSequence() {
            return itemSequence;
        }

        /**
         * The sequence number of the details within the claim item which this line is intended to replace.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
         */
        public List<PositiveInt> getDetailSequence() {
            return detailSequence;
        }

        /**
         * The sequence number of the sub-details woithin the details within the claim item which this line is intended to 
         * replace.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
         */
        public List<PositiveInt> getSubDetailSequence() {
            return subDetailSequence;
        }

        /**
         * The providers who are authorized for the services rendered to the patient.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getProvider() {
            return provider;
        }

        /**
         * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
         * product, service, drug or other billing code for the item.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getProductOrService() {
            return productOrService;
        }

        /**
         * Item typification or modifiers codes to convey additional context for the product or service.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getModifier() {
            return modifier;
        }

        /**
         * Identifies the program under which this may be recovered.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getProgramCode() {
            return programCode;
        }

        /**
         * The date or dates when the service or product was supplied, performed or completed.
         * 
         * @return
         *     An immutable object of type {@link Date} or {@link Period} that may be null.
         */
        public Element getServiced() {
            return serviced;
        }

        /**
         * Where the product or service was provided.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}, {@link Address} or {@link Reference} that may be null.
         */
        public Element getLocation() {
            return location;
        }

        /**
         * The number of repetitions of a service or product.
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getQuantity() {
            return quantity;
        }

        /**
         * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
         * for the details of the group.
         * 
         * @return
         *     An immutable object of type {@link Money} that may be null.
         */
        public Money getUnitPrice() {
            return unitPrice;
        }

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
         * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getFactor() {
            return factor;
        }

        /**
         * The quantity times the unit price for an additional service or product or charge.
         * 
         * @return
         *     An immutable object of type {@link Money} that may be null.
         */
        public Money getNet() {
            return net;
        }

        /**
         * Physical service site on the patient (limb, tooth, etc.).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getBodySite() {
            return bodySite;
        }

        /**
         * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getSubSite() {
            return subSite;
        }

        /**
         * The numbers associated with notes below which apply to the adjudication of this item.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
         */
        public List<PositiveInt> getNoteNumber() {
            return noteNumber;
        }

        /**
         * The adjudication results.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Adjudication} that may be empty.
         */
        public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
            return adjudication;
        }

        /**
         * The second-tier service adjudications for payor added services.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Detail} that may be empty.
         */
        public List<Detail> getDetail() {
            return detail;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !itemSequence.isEmpty() || 
                !detailSequence.isEmpty() || 
                !subDetailSequence.isEmpty() || 
                !provider.isEmpty() || 
                (productOrService != null) || 
                !modifier.isEmpty() || 
                !programCode.isEmpty() || 
                (serviced != null) || 
                (location != null) || 
                (quantity != null) || 
                (unitPrice != null) || 
                (factor != null) || 
                (net != null) || 
                (bodySite != null) || 
                !subSite.isEmpty() || 
                !noteNumber.isEmpty() || 
                !adjudication.isEmpty() || 
                !detail.isEmpty();
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
                    accept(itemSequence, "itemSequence", visitor, PositiveInt.class);
                    accept(detailSequence, "detailSequence", visitor, PositiveInt.class);
                    accept(subDetailSequence, "subDetailSequence", visitor, PositiveInt.class);
                    accept(provider, "provider", visitor, Reference.class);
                    accept(productOrService, "productOrService", visitor);
                    accept(modifier, "modifier", visitor, CodeableConcept.class);
                    accept(programCode, "programCode", visitor, CodeableConcept.class);
                    accept(serviced, "serviced", visitor);
                    accept(location, "location", visitor);
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
            AddItem other = (AddItem) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(itemSequence, other.itemSequence) && 
                Objects.equals(detailSequence, other.detailSequence) && 
                Objects.equals(subDetailSequence, other.subDetailSequence) && 
                Objects.equals(provider, other.provider) && 
                Objects.equals(productOrService, other.productOrService) && 
                Objects.equals(modifier, other.modifier) && 
                Objects.equals(programCode, other.programCode) && 
                Objects.equals(serviced, other.serviced) && 
                Objects.equals(location, other.location) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(unitPrice, other.unitPrice) && 
                Objects.equals(factor, other.factor) && 
                Objects.equals(net, other.net) && 
                Objects.equals(bodySite, other.bodySite) && 
                Objects.equals(subSite, other.subSite) && 
                Objects.equals(noteNumber, other.noteNumber) && 
                Objects.equals(adjudication, other.adjudication) && 
                Objects.equals(detail, other.detail);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    itemSequence, 
                    detailSequence, 
                    subDetailSequence, 
                    provider, 
                    productOrService, 
                    modifier, 
                    programCode, 
                    serviced, 
                    location, 
                    quantity, 
                    unitPrice, 
                    factor, 
                    net, 
                    bodySite, 
                    subSite, 
                    noteNumber, 
                    adjudication, 
                    detail);
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
            private List<PositiveInt> itemSequence = new ArrayList<>();
            private List<PositiveInt> detailSequence = new ArrayList<>();
            private List<PositiveInt> subDetailSequence = new ArrayList<>();
            private List<Reference> provider = new ArrayList<>();
            private CodeableConcept productOrService;
            private List<CodeableConcept> modifier = new ArrayList<>();
            private List<CodeableConcept> programCode = new ArrayList<>();
            private Element serviced;
            private Element location;
            private SimpleQuantity quantity;
            private Money unitPrice;
            private Decimal factor;
            private Money net;
            private CodeableConcept bodySite;
            private List<CodeableConcept> subSite = new ArrayList<>();
            private List<PositiveInt> noteNumber = new ArrayList<>();
            private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();
            private List<Detail> detail = new ArrayList<>();

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
             * Claim items which this service line is intended to replace.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param itemSequence
             *     Item sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder itemSequence(PositiveInt... itemSequence) {
                for (PositiveInt value : itemSequence) {
                    this.itemSequence.add(value);
                }
                return this;
            }

            /**
             * Claim items which this service line is intended to replace.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param itemSequence
             *     Item sequence number
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder itemSequence(Collection<PositiveInt> itemSequence) {
                this.itemSequence = new ArrayList<>(itemSequence);
                return this;
            }

            /**
             * The sequence number of the details within the claim item which this line is intended to replace.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param detailSequence
             *     Detail sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detailSequence(PositiveInt... detailSequence) {
                for (PositiveInt value : detailSequence) {
                    this.detailSequence.add(value);
                }
                return this;
            }

            /**
             * The sequence number of the details within the claim item which this line is intended to replace.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param detailSequence
             *     Detail sequence number
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder detailSequence(Collection<PositiveInt> detailSequence) {
                this.detailSequence = new ArrayList<>(detailSequence);
                return this;
            }

            /**
             * The sequence number of the sub-details woithin the details within the claim item which this line is intended to 
             * replace.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param subDetailSequence
             *     Subdetail sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subDetailSequence(PositiveInt... subDetailSequence) {
                for (PositiveInt value : subDetailSequence) {
                    this.subDetailSequence.add(value);
                }
                return this;
            }

            /**
             * The sequence number of the sub-details woithin the details within the claim item which this line is intended to 
             * replace.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param subDetailSequence
             *     Subdetail sequence number
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder subDetailSequence(Collection<PositiveInt> subDetailSequence) {
                this.subDetailSequence = new ArrayList<>(subDetailSequence);
                return this;
            }

            /**
             * The providers who are authorized for the services rendered to the patient.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param provider
             *     Authorized providers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder provider(Reference... provider) {
                for (Reference value : provider) {
                    this.provider.add(value);
                }
                return this;
            }

            /**
             * The providers who are authorized for the services rendered to the patient.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param provider
             *     Authorized providers
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder provider(Collection<Reference> provider) {
                this.provider = new ArrayList<>(provider);
                return this;
            }

            /**
             * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
             * product, service, drug or other billing code for the item.
             * 
             * <p>This element is required.
             * 
             * @param productOrService
             *     Billing, service, product, or drug code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder productOrService(CodeableConcept productOrService) {
                this.productOrService = productOrService;
                return this;
            }

            /**
             * Item typification or modifiers codes to convey additional context for the product or service.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifier
             *     Service/Product billing modifiers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder modifier(CodeableConcept... modifier) {
                for (CodeableConcept value : modifier) {
                    this.modifier.add(value);
                }
                return this;
            }

            /**
             * Item typification or modifiers codes to convey additional context for the product or service.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifier
             *     Service/Product billing modifiers
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder modifier(Collection<CodeableConcept> modifier) {
                this.modifier = new ArrayList<>(modifier);
                return this;
            }

            /**
             * Identifies the program under which this may be recovered.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param programCode
             *     Program the product or service is provided under
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder programCode(CodeableConcept... programCode) {
                for (CodeableConcept value : programCode) {
                    this.programCode.add(value);
                }
                return this;
            }

            /**
             * Identifies the program under which this may be recovered.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param programCode
             *     Program the product or service is provided under
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder programCode(Collection<CodeableConcept> programCode) {
                this.programCode = new ArrayList<>(programCode);
                return this;
            }

            /**
             * Convenience method for setting {@code serviced} with choice type Date.
             * 
             * @param serviced
             *     Date or dates of service or product delivery
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #serviced(Element)
             */
            public Builder serviced(java.time.LocalDate serviced) {
                this.serviced = (serviced == null) ? null : Date.of(serviced);
                return this;
            }

            /**
             * The date or dates when the service or product was supplied, performed or completed.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Date}</li>
             * <li>{@link Period}</li>
             * </ul>
             * 
             * @param serviced
             *     Date or dates of service or product delivery
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder serviced(Element serviced) {
                this.serviced = serviced;
                return this;
            }

            /**
             * Where the product or service was provided.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Address}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link Location}</li>
             * </ul>
             * 
             * @param location
             *     Place of service or where product was supplied
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder location(Element location) {
                this.location = location;
                return this;
            }

            /**
             * The number of repetitions of a service or product.
             * 
             * @param quantity
             *     Count of products or services
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(SimpleQuantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
             * for the details of the group.
             * 
             * @param unitPrice
             *     Fee, charge or cost per item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder unitPrice(Money unitPrice) {
                this.unitPrice = unitPrice;
                return this;
            }

            /**
             * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
             * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
             * 
             * @param factor
             *     Price scaling factor
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder factor(Decimal factor) {
                this.factor = factor;
                return this;
            }

            /**
             * The quantity times the unit price for an additional service or product or charge.
             * 
             * @param net
             *     Total item cost
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder net(Money net) {
                this.net = net;
                return this;
            }

            /**
             * Physical service site on the patient (limb, tooth, etc.).
             * 
             * @param bodySite
             *     Anatomical location
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder bodySite(CodeableConcept bodySite) {
                this.bodySite = bodySite;
                return this;
            }

            /**
             * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param subSite
             *     Anatomical sub-location
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subSite(CodeableConcept... subSite) {
                for (CodeableConcept value : subSite) {
                    this.subSite.add(value);
                }
                return this;
            }

            /**
             * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param subSite
             *     Anatomical sub-location
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder subSite(Collection<CodeableConcept> subSite) {
                this.subSite = new ArrayList<>(subSite);
                return this;
            }

            /**
             * The numbers associated with notes below which apply to the adjudication of this item.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder noteNumber(PositiveInt... noteNumber) {
                for (PositiveInt value : noteNumber) {
                    this.noteNumber.add(value);
                }
                return this;
            }

            /**
             * The numbers associated with notes below which apply to the adjudication of this item.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                this.noteNumber = new ArrayList<>(noteNumber);
                return this;
            }

            /**
             * The adjudication results.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param adjudication
             *     Added items adjudication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
                for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                    this.adjudication.add(value);
                }
                return this;
            }

            /**
             * The adjudication results.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param adjudication
             *     Added items adjudication
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
                this.adjudication = new ArrayList<>(adjudication);
                return this;
            }

            /**
             * The second-tier service adjudications for payor added services.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param detail
             *     Insurer added line items
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Detail... detail) {
                for (Detail value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * The second-tier service adjudications for payor added services.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param detail
             *     Insurer added line items
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder detail(Collection<Detail> detail) {
                this.detail = new ArrayList<>(detail);
                return this;
            }

            /**
             * Build the {@link AddItem}
             * 
             * <p>Required elements:
             * <ul>
             * <li>productOrService</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link AddItem}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid AddItem per the base specification
             */
            @Override
            public AddItem build() {
                AddItem addItem = new AddItem(this);
                if (validating) {
                    validate(addItem);
                }
                return addItem;
            }

            protected void validate(AddItem addItem) {
                super.validate(addItem);
                ValidationSupport.checkList(addItem.itemSequence, "itemSequence", PositiveInt.class);
                ValidationSupport.checkList(addItem.detailSequence, "detailSequence", PositiveInt.class);
                ValidationSupport.checkList(addItem.subDetailSequence, "subDetailSequence", PositiveInt.class);
                ValidationSupport.checkList(addItem.provider, "provider", Reference.class);
                ValidationSupport.requireNonNull(addItem.productOrService, "productOrService");
                ValidationSupport.checkList(addItem.modifier, "modifier", CodeableConcept.class);
                ValidationSupport.checkList(addItem.programCode, "programCode", CodeableConcept.class);
                ValidationSupport.choiceElement(addItem.serviced, "serviced", Date.class, Period.class);
                ValidationSupport.choiceElement(addItem.location, "location", CodeableConcept.class, Address.class, Reference.class);
                ValidationSupport.checkList(addItem.subSite, "subSite", CodeableConcept.class);
                ValidationSupport.checkList(addItem.noteNumber, "noteNumber", PositiveInt.class);
                ValidationSupport.checkList(addItem.adjudication, "adjudication", ExplanationOfBenefit.Item.Adjudication.class);
                ValidationSupport.checkList(addItem.detail, "detail", Detail.class);
                ValidationSupport.checkReferenceType(addItem.provider, "provider", "Practitioner", "PractitionerRole", "Organization");
                ValidationSupport.checkReferenceType(addItem.location, "location", "Location");
                ValidationSupport.requireValueOrChildren(addItem);
            }

            protected Builder from(AddItem addItem) {
                super.from(addItem);
                itemSequence.addAll(addItem.itemSequence);
                detailSequence.addAll(addItem.detailSequence);
                subDetailSequence.addAll(addItem.subDetailSequence);
                provider.addAll(addItem.provider);
                productOrService = addItem.productOrService;
                modifier.addAll(addItem.modifier);
                programCode.addAll(addItem.programCode);
                serviced = addItem.serviced;
                location = addItem.location;
                quantity = addItem.quantity;
                unitPrice = addItem.unitPrice;
                factor = addItem.factor;
                net = addItem.net;
                bodySite = addItem.bodySite;
                subSite.addAll(addItem.subSite);
                noteNumber.addAll(addItem.noteNumber);
                adjudication.addAll(addItem.adjudication);
                detail.addAll(addItem.detail);
                return this;
            }
        }

        /**
         * The second-tier service adjudications for payor added services.
         */
        public static class Detail extends BackboneElement {
            @Binding(
                bindingName = "ServiceProduct",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Allowable service and product codes.",
                valueSet = "http://hl7.org/fhir/ValueSet/service-uscls"
            )
            @Required
            private final CodeableConcept productOrService;
            @Binding(
                bindingName = "Modifiers",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Item type or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.",
                valueSet = "http://hl7.org/fhir/ValueSet/claim-modifiers"
            )
            private final List<CodeableConcept> modifier;
            private final SimpleQuantity quantity;
            private final Money unitPrice;
            private final Decimal factor;
            private final Money net;
            private final List<PositiveInt> noteNumber;
            private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;
            private final List<SubDetail> subDetail;

            private Detail(Builder builder) {
                super(builder);
                productOrService = builder.productOrService;
                modifier = Collections.unmodifiableList(builder.modifier);
                quantity = builder.quantity;
                unitPrice = builder.unitPrice;
                factor = builder.factor;
                net = builder.net;
                noteNumber = Collections.unmodifiableList(builder.noteNumber);
                adjudication = Collections.unmodifiableList(builder.adjudication);
                subDetail = Collections.unmodifiableList(builder.subDetail);
            }

            /**
             * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
             * product, service, drug or other billing code for the item.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getProductOrService() {
                return productOrService;
            }

            /**
             * Item typification or modifiers codes to convey additional context for the product or service.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getModifier() {
                return modifier;
            }

            /**
             * The number of repetitions of a service or product.
             * 
             * @return
             *     An immutable object of type {@link SimpleQuantity} that may be null.
             */
            public SimpleQuantity getQuantity() {
                return quantity;
            }

            /**
             * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
             * for the details of the group.
             * 
             * @return
             *     An immutable object of type {@link Money} that may be null.
             */
            public Money getUnitPrice() {
                return unitPrice;
            }

            /**
             * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
             * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
             * 
             * @return
             *     An immutable object of type {@link Decimal} that may be null.
             */
            public Decimal getFactor() {
                return factor;
            }

            /**
             * The quantity times the unit price for an additional service or product or charge.
             * 
             * @return
             *     An immutable object of type {@link Money} that may be null.
             */
            public Money getNet() {
                return net;
            }

            /**
             * The numbers associated with notes below which apply to the adjudication of this item.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
             */
            public List<PositiveInt> getNoteNumber() {
                return noteNumber;
            }

            /**
             * The adjudication results.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Adjudication} that may be empty.
             */
            public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
                return adjudication;
            }

            /**
             * The third-tier service adjudications for payor added services.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link SubDetail} that may be empty.
             */
            public List<SubDetail> getSubDetail() {
                return subDetail;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (productOrService != null) || 
                    !modifier.isEmpty() || 
                    (quantity != null) || 
                    (unitPrice != null) || 
                    (factor != null) || 
                    (net != null) || 
                    !noteNumber.isEmpty() || 
                    !adjudication.isEmpty() || 
                    !subDetail.isEmpty();
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
                Detail other = (Detail) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(productOrService, other.productOrService) && 
                    Objects.equals(modifier, other.modifier) && 
                    Objects.equals(quantity, other.quantity) && 
                    Objects.equals(unitPrice, other.unitPrice) && 
                    Objects.equals(factor, other.factor) && 
                    Objects.equals(net, other.net) && 
                    Objects.equals(noteNumber, other.noteNumber) && 
                    Objects.equals(adjudication, other.adjudication) && 
                    Objects.equals(subDetail, other.subDetail);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        productOrService, 
                        modifier, 
                        quantity, 
                        unitPrice, 
                        factor, 
                        net, 
                        noteNumber, 
                        adjudication, 
                        subDetail);
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
                private CodeableConcept productOrService;
                private List<CodeableConcept> modifier = new ArrayList<>();
                private SimpleQuantity quantity;
                private Money unitPrice;
                private Decimal factor;
                private Money net;
                private List<PositiveInt> noteNumber = new ArrayList<>();
                private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();
                private List<SubDetail> subDetail = new ArrayList<>();

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
                 * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
                 * product, service, drug or other billing code for the item.
                 * 
                 * <p>This element is required.
                 * 
                 * @param productOrService
                 *     Billing, service, product, or drug code
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder productOrService(CodeableConcept productOrService) {
                    this.productOrService = productOrService;
                    return this;
                }

                /**
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifier
                 *     Service/Product billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder modifier(CodeableConcept... modifier) {
                    for (CodeableConcept value : modifier) {
                        this.modifier.add(value);
                    }
                    return this;
                }

                /**
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifier
                 *     Service/Product billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder modifier(Collection<CodeableConcept> modifier) {
                    this.modifier = new ArrayList<>(modifier);
                    return this;
                }

                /**
                 * The number of repetitions of a service or product.
                 * 
                 * @param quantity
                 *     Count of products or services
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder quantity(SimpleQuantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                 * for the details of the group.
                 * 
                 * @param unitPrice
                 *     Fee, charge or cost per item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder unitPrice(Money unitPrice) {
                    this.unitPrice = unitPrice;
                    return this;
                }

                /**
                 * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                 * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                 * 
                 * @param factor
                 *     Price scaling factor
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder factor(Decimal factor) {
                    this.factor = factor;
                    return this;
                }

                /**
                 * The quantity times the unit price for an additional service or product or charge.
                 * 
                 * @param net
                 *     Total item cost
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder net(Money net) {
                    this.net = net;
                    return this;
                }

                /**
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder noteNumber(PositiveInt... noteNumber) {
                    for (PositiveInt value : noteNumber) {
                        this.noteNumber.add(value);
                    }
                    return this;
                }

                /**
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                    this.noteNumber = new ArrayList<>(noteNumber);
                    return this;
                }

                /**
                 * The adjudication results.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param adjudication
                 *     Added items adjudication
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
                    for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                        this.adjudication.add(value);
                    }
                    return this;
                }

                /**
                 * The adjudication results.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param adjudication
                 *     Added items adjudication
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
                    this.adjudication = new ArrayList<>(adjudication);
                    return this;
                }

                /**
                 * The third-tier service adjudications for payor added services.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param subDetail
                 *     Insurer added line items
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder subDetail(SubDetail... subDetail) {
                    for (SubDetail value : subDetail) {
                        this.subDetail.add(value);
                    }
                    return this;
                }

                /**
                 * The third-tier service adjudications for payor added services.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param subDetail
                 *     Insurer added line items
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder subDetail(Collection<SubDetail> subDetail) {
                    this.subDetail = new ArrayList<>(subDetail);
                    return this;
                }

                /**
                 * Build the {@link Detail}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>productOrService</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Detail}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Detail per the base specification
                 */
                @Override
                public Detail build() {
                    Detail detail = new Detail(this);
                    if (validating) {
                        validate(detail);
                    }
                    return detail;
                }

                protected void validate(Detail detail) {
                    super.validate(detail);
                    ValidationSupport.requireNonNull(detail.productOrService, "productOrService");
                    ValidationSupport.checkList(detail.modifier, "modifier", CodeableConcept.class);
                    ValidationSupport.checkList(detail.noteNumber, "noteNumber", PositiveInt.class);
                    ValidationSupport.checkList(detail.adjudication, "adjudication", ExplanationOfBenefit.Item.Adjudication.class);
                    ValidationSupport.checkList(detail.subDetail, "subDetail", SubDetail.class);
                    ValidationSupport.requireValueOrChildren(detail);
                }

                protected Builder from(Detail detail) {
                    super.from(detail);
                    productOrService = detail.productOrService;
                    modifier.addAll(detail.modifier);
                    quantity = detail.quantity;
                    unitPrice = detail.unitPrice;
                    factor = detail.factor;
                    net = detail.net;
                    noteNumber.addAll(detail.noteNumber);
                    adjudication.addAll(detail.adjudication);
                    subDetail.addAll(detail.subDetail);
                    return this;
                }
            }

            /**
             * The third-tier service adjudications for payor added services.
             */
            public static class SubDetail extends BackboneElement {
                @Binding(
                    bindingName = "ServiceProduct",
                    strength = BindingStrength.Value.EXAMPLE,
                    description = "Allowable service and product codes.",
                    valueSet = "http://hl7.org/fhir/ValueSet/service-uscls"
                )
                @Required
                private final CodeableConcept productOrService;
                @Binding(
                    bindingName = "Modifiers",
                    strength = BindingStrength.Value.EXAMPLE,
                    description = "Item type or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.",
                    valueSet = "http://hl7.org/fhir/ValueSet/claim-modifiers"
                )
                private final List<CodeableConcept> modifier;
                private final SimpleQuantity quantity;
                private final Money unitPrice;
                private final Decimal factor;
                private final Money net;
                private final List<PositiveInt> noteNumber;
                private final List<ExplanationOfBenefit.Item.Adjudication> adjudication;

                private SubDetail(Builder builder) {
                    super(builder);
                    productOrService = builder.productOrService;
                    modifier = Collections.unmodifiableList(builder.modifier);
                    quantity = builder.quantity;
                    unitPrice = builder.unitPrice;
                    factor = builder.factor;
                    net = builder.net;
                    noteNumber = Collections.unmodifiableList(builder.noteNumber);
                    adjudication = Collections.unmodifiableList(builder.adjudication);
                }

                /**
                 * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
                 * product, service, drug or other billing code for the item.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that is non-null.
                 */
                public CodeableConcept getProductOrService() {
                    return productOrService;
                }

                /**
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
                 */
                public List<CodeableConcept> getModifier() {
                    return modifier;
                }

                /**
                 * The number of repetitions of a service or product.
                 * 
                 * @return
                 *     An immutable object of type {@link SimpleQuantity} that may be null.
                 */
                public SimpleQuantity getQuantity() {
                    return quantity;
                }

                /**
                 * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                 * for the details of the group.
                 * 
                 * @return
                 *     An immutable object of type {@link Money} that may be null.
                 */
                public Money getUnitPrice() {
                    return unitPrice;
                }

                /**
                 * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                 * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                 * 
                 * @return
                 *     An immutable object of type {@link Decimal} that may be null.
                 */
                public Decimal getFactor() {
                    return factor;
                }

                /**
                 * The quantity times the unit price for an additional service or product or charge.
                 * 
                 * @return
                 *     An immutable object of type {@link Money} that may be null.
                 */
                public Money getNet() {
                    return net;
                }

                /**
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
                 */
                public List<PositiveInt> getNoteNumber() {
                    return noteNumber;
                }

                /**
                 * The adjudication results.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Adjudication} that may be empty.
                 */
                public List<ExplanationOfBenefit.Item.Adjudication> getAdjudication() {
                    return adjudication;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (productOrService != null) || 
                        !modifier.isEmpty() || 
                        (quantity != null) || 
                        (unitPrice != null) || 
                        (factor != null) || 
                        (net != null) || 
                        !noteNumber.isEmpty() || 
                        !adjudication.isEmpty();
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
                            accept(productOrService, "productOrService", visitor);
                            accept(modifier, "modifier", visitor, CodeableConcept.class);
                            accept(quantity, "quantity", visitor);
                            accept(unitPrice, "unitPrice", visitor);
                            accept(factor, "factor", visitor);
                            accept(net, "net", visitor);
                            accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                            accept(adjudication, "adjudication", visitor, ExplanationOfBenefit.Item.Adjudication.class);
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
                    SubDetail other = (SubDetail) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(productOrService, other.productOrService) && 
                        Objects.equals(modifier, other.modifier) && 
                        Objects.equals(quantity, other.quantity) && 
                        Objects.equals(unitPrice, other.unitPrice) && 
                        Objects.equals(factor, other.factor) && 
                        Objects.equals(net, other.net) && 
                        Objects.equals(noteNumber, other.noteNumber) && 
                        Objects.equals(adjudication, other.adjudication);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            productOrService, 
                            modifier, 
                            quantity, 
                            unitPrice, 
                            factor, 
                            net, 
                            noteNumber, 
                            adjudication);
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
                    private CodeableConcept productOrService;
                    private List<CodeableConcept> modifier = new ArrayList<>();
                    private SimpleQuantity quantity;
                    private Money unitPrice;
                    private Decimal factor;
                    private Money net;
                    private List<PositiveInt> noteNumber = new ArrayList<>();
                    private List<ExplanationOfBenefit.Item.Adjudication> adjudication = new ArrayList<>();

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
                     * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
                     * product, service, drug or other billing code for the item.
                     * 
                     * <p>This element is required.
                     * 
                     * @param productOrService
                     *     Billing, service, product, or drug code
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder productOrService(CodeableConcept productOrService) {
                        this.productOrService = productOrService;
                        return this;
                    }

                    /**
                     * Item typification or modifiers codes to convey additional context for the product or service.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param modifier
                     *     Service/Product billing modifiers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder modifier(CodeableConcept... modifier) {
                        for (CodeableConcept value : modifier) {
                            this.modifier.add(value);
                        }
                        return this;
                    }

                    /**
                     * Item typification or modifiers codes to convey additional context for the product or service.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param modifier
                     *     Service/Product billing modifiers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder modifier(Collection<CodeableConcept> modifier) {
                        this.modifier = new ArrayList<>(modifier);
                        return this;
                    }

                    /**
                     * The number of repetitions of a service or product.
                     * 
                     * @param quantity
                     *     Count of products or services
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder quantity(SimpleQuantity quantity) {
                        this.quantity = quantity;
                        return this;
                    }

                    /**
                     * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                     * for the details of the group.
                     * 
                     * @param unitPrice
                     *     Fee, charge or cost per item
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder unitPrice(Money unitPrice) {
                        this.unitPrice = unitPrice;
                        return this;
                    }

                    /**
                     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                     * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                     * 
                     * @param factor
                     *     Price scaling factor
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder factor(Decimal factor) {
                        this.factor = factor;
                        return this;
                    }

                    /**
                     * The quantity times the unit price for an additional service or product or charge.
                     * 
                     * @param net
                     *     Total item cost
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder net(Money net) {
                        this.net = net;
                        return this;
                    }

                    /**
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder noteNumber(PositiveInt... noteNumber) {
                        for (PositiveInt value : noteNumber) {
                            this.noteNumber.add(value);
                        }
                        return this;
                    }

                    /**
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                        this.noteNumber = new ArrayList<>(noteNumber);
                        return this;
                    }

                    /**
                     * The adjudication results.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param adjudication
                     *     Added items adjudication
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder adjudication(ExplanationOfBenefit.Item.Adjudication... adjudication) {
                        for (ExplanationOfBenefit.Item.Adjudication value : adjudication) {
                            this.adjudication.add(value);
                        }
                        return this;
                    }

                    /**
                     * The adjudication results.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param adjudication
                     *     Added items adjudication
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder adjudication(Collection<ExplanationOfBenefit.Item.Adjudication> adjudication) {
                        this.adjudication = new ArrayList<>(adjudication);
                        return this;
                    }

                    /**
                     * Build the {@link SubDetail}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>productOrService</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link SubDetail}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid SubDetail per the base specification
                     */
                    @Override
                    public SubDetail build() {
                        SubDetail subDetail = new SubDetail(this);
                        if (validating) {
                            validate(subDetail);
                        }
                        return subDetail;
                    }

                    protected void validate(SubDetail subDetail) {
                        super.validate(subDetail);
                        ValidationSupport.requireNonNull(subDetail.productOrService, "productOrService");
                        ValidationSupport.checkList(subDetail.modifier, "modifier", CodeableConcept.class);
                        ValidationSupport.checkList(subDetail.noteNumber, "noteNumber", PositiveInt.class);
                        ValidationSupport.checkList(subDetail.adjudication, "adjudication", ExplanationOfBenefit.Item.Adjudication.class);
                        ValidationSupport.requireValueOrChildren(subDetail);
                    }

                    protected Builder from(SubDetail subDetail) {
                        super.from(subDetail);
                        productOrService = subDetail.productOrService;
                        modifier.addAll(subDetail.modifier);
                        quantity = subDetail.quantity;
                        unitPrice = subDetail.unitPrice;
                        factor = subDetail.factor;
                        net = subDetail.net;
                        noteNumber.addAll(subDetail.noteNumber);
                        adjudication.addAll(subDetail.adjudication);
                        return this;
                    }
                }
            }
        }
    }

    /**
     * Categorized monetary totals for the adjudication.
     */
    public static class Total extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "Adjudication",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The adjudication codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/adjudication"
        )
        @Required
        private final CodeableConcept category;
        @Summary
        @Required
        private final Money amount;

        private Total(Builder builder) {
            super(builder);
            category = builder.category;
            amount = builder.amount;
        }

        /**
         * A code to indicate the information type of this adjudication record. Information types may include: the value 
         * submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible 
         * for in aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * Monetary total amount associated with the category.
         * 
         * @return
         *     An immutable object of type {@link Money} that is non-null.
         */
        public Money getAmount() {
            return amount;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (category != null) || 
                (amount != null);
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
                    accept(category, "category", visitor);
                    accept(amount, "amount", visitor);
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
            Total other = (Total) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(category, other.category) && 
                Objects.equals(amount, other.amount);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    category, 
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
            private CodeableConcept category;
            private Money amount;

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
             * A code to indicate the information type of this adjudication record. Information types may include: the value 
             * submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible 
             * for in aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.
             * 
             * <p>This element is required.
             * 
             * @param category
             *     Type of adjudication information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder category(CodeableConcept category) {
                this.category = category;
                return this;
            }

            /**
             * Monetary total amount associated with the category.
             * 
             * <p>This element is required.
             * 
             * @param amount
             *     Financial total for the category
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Money amount) {
                this.amount = amount;
                return this;
            }

            /**
             * Build the {@link Total}
             * 
             * <p>Required elements:
             * <ul>
             * <li>category</li>
             * <li>amount</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Total}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Total per the base specification
             */
            @Override
            public Total build() {
                Total total = new Total(this);
                if (validating) {
                    validate(total);
                }
                return total;
            }

            protected void validate(Total total) {
                super.validate(total);
                ValidationSupport.requireNonNull(total.category, "category");
                ValidationSupport.requireNonNull(total.amount, "amount");
                ValidationSupport.requireValueOrChildren(total);
            }

            protected Builder from(Total total) {
                super.from(total);
                category = total.category;
                amount = total.amount;
                return this;
            }
        }
    }

    /**
     * Payment details for the adjudication of the claim.
     */
    public static class Payment extends BackboneElement {
        @Binding(
            bindingName = "PaymentType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The type (partial, complete) of the payment.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-paymenttype"
        )
        private final CodeableConcept type;
        private final Money adjustment;
        @Binding(
            bindingName = "PaymentAdjustmentReason",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Payment Adjustment reason codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/payment-adjustment-reason"
        )
        private final CodeableConcept adjustmentReason;
        private final Date date;
        private final Money amount;
        private final Identifier identifier;

        private Payment(Builder builder) {
            super(builder);
            type = builder.type;
            adjustment = builder.adjustment;
            adjustmentReason = builder.adjustmentReason;
            date = builder.date;
            amount = builder.amount;
            identifier = builder.identifier;
        }

        /**
         * Whether this represents partial or complete payment of the benefits payable.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Total amount of all adjustments to this payment included in this transaction which are not related to this claim's 
         * adjudication.
         * 
         * @return
         *     An immutable object of type {@link Money} that may be null.
         */
        public Money getAdjustment() {
            return adjustment;
        }

        /**
         * Reason for the payment adjustment.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getAdjustmentReason() {
            return adjustmentReason;
        }

        /**
         * Estimated date the payment will be issued or the actual issue date of payment.
         * 
         * @return
         *     An immutable object of type {@link Date} that may be null.
         */
        public Date getDate() {
            return date;
        }

        /**
         * Benefits payable less any payment adjustment.
         * 
         * @return
         *     An immutable object of type {@link Money} that may be null.
         */
        public Money getAmount() {
            return amount;
        }

        /**
         * Issuer's unique identifier for the payment instrument.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (adjustment != null) || 
                (adjustmentReason != null) || 
                (date != null) || 
                (amount != null) || 
                (identifier != null);
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
                    accept(adjustment, "adjustment", visitor);
                    accept(adjustmentReason, "adjustmentReason", visitor);
                    accept(date, "date", visitor);
                    accept(amount, "amount", visitor);
                    accept(identifier, "identifier", visitor);
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
            Payment other = (Payment) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(adjustment, other.adjustment) && 
                Objects.equals(adjustmentReason, other.adjustmentReason) && 
                Objects.equals(date, other.date) && 
                Objects.equals(amount, other.amount) && 
                Objects.equals(identifier, other.identifier);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    adjustment, 
                    adjustmentReason, 
                    date, 
                    amount, 
                    identifier);
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
            private Money adjustment;
            private CodeableConcept adjustmentReason;
            private Date date;
            private Money amount;
            private Identifier identifier;

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
             * Whether this represents partial or complete payment of the benefits payable.
             * 
             * @param type
             *     Partial or complete payment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Total amount of all adjustments to this payment included in this transaction which are not related to this claim's 
             * adjudication.
             * 
             * @param adjustment
             *     Payment adjustment for non-claim issues
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder adjustment(Money adjustment) {
                this.adjustment = adjustment;
                return this;
            }

            /**
             * Reason for the payment adjustment.
             * 
             * @param adjustmentReason
             *     Explanation for the variance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder adjustmentReason(CodeableConcept adjustmentReason) {
                this.adjustmentReason = adjustmentReason;
                return this;
            }

            /**
             * Convenience method for setting {@code date}.
             * 
             * @param date
             *     Expected date of payment
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #date(com.ibm.fhir.model.type.Date)
             */
            public Builder date(java.time.LocalDate date) {
                this.date = (date == null) ? null : Date.of(date);
                return this;
            }

            /**
             * Estimated date the payment will be issued or the actual issue date of payment.
             * 
             * @param date
             *     Expected date of payment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(Date date) {
                this.date = date;
                return this;
            }

            /**
             * Benefits payable less any payment adjustment.
             * 
             * @param amount
             *     Payable amount after adjustment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Money amount) {
                this.amount = amount;
                return this;
            }

            /**
             * Issuer's unique identifier for the payment instrument.
             * 
             * @param identifier
             *     Business identifier for the payment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * Build the {@link Payment}
             * 
             * @return
             *     An immutable object of type {@link Payment}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Payment per the base specification
             */
            @Override
            public Payment build() {
                Payment payment = new Payment(this);
                if (validating) {
                    validate(payment);
                }
                return payment;
            }

            protected void validate(Payment payment) {
                super.validate(payment);
                ValidationSupport.requireValueOrChildren(payment);
            }

            protected Builder from(Payment payment) {
                super.from(payment);
                type = payment.type;
                adjustment = payment.adjustment;
                adjustmentReason = payment.adjustmentReason;
                date = payment.date;
                amount = payment.amount;
                identifier = payment.identifier;
                return this;
            }
        }
    }

    /**
     * A note that describes or explains adjudication results in a human readable form.
     */
    public static class ProcessNote extends BackboneElement {
        private final PositiveInt number;
        @Binding(
            bindingName = "NoteType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The presentation types of notes.",
            valueSet = "http://hl7.org/fhir/ValueSet/note-type|4.1.0"
        )
        private final NoteType type;
        private final String text;
        @Binding(
            bindingName = "Language",
            strength = BindingStrength.Value.PREFERRED,
            description = "A human language.",
            valueSet = "http://hl7.org/fhir/ValueSet/languages",
            maxValueSet = "http://hl7.org/fhir/ValueSet/all-languages"
        )
        private final CodeableConcept language;

        private ProcessNote(Builder builder) {
            super(builder);
            number = builder.number;
            type = builder.type;
            text = builder.text;
            language = builder.language;
        }

        /**
         * A number to uniquely identify a note entry.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that may be null.
         */
        public PositiveInt getNumber() {
            return number;
        }

        /**
         * The business purpose of the note text.
         * 
         * @return
         *     An immutable object of type {@link NoteType} that may be null.
         */
        public NoteType getType() {
            return type;
        }

        /**
         * The explanation or description associated with the processing.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getText() {
            return text;
        }

        /**
         * A code to define the language used in the text of the note.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getLanguage() {
            return language;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (number != null) || 
                (type != null) || 
                (text != null) || 
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
                    accept(number, "number", visitor);
                    accept(type, "type", visitor);
                    accept(text, "text", visitor);
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
            ProcessNote other = (ProcessNote) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(number, other.number) && 
                Objects.equals(type, other.type) && 
                Objects.equals(text, other.text) && 
                Objects.equals(language, other.language);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    number, 
                    type, 
                    text, 
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
            private PositiveInt number;
            private NoteType type;
            private String text;
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
             * A number to uniquely identify a note entry.
             * 
             * @param number
             *     Note instance identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder number(PositiveInt number) {
                this.number = number;
                return this;
            }

            /**
             * The business purpose of the note text.
             * 
             * @param type
             *     display | print | printoper
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(NoteType type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code text}.
             * 
             * @param text
             *     Note explanatory text
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #text(com.ibm.fhir.model.type.String)
             */
            public Builder text(java.lang.String text) {
                this.text = (text == null) ? null : String.of(text);
                return this;
            }

            /**
             * The explanation or description associated with the processing.
             * 
             * @param text
             *     Note explanatory text
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * A code to define the language used in the text of the note.
             * 
             * @param language
             *     Language of the text
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder language(CodeableConcept language) {
                this.language = language;
                return this;
            }

            /**
             * Build the {@link ProcessNote}
             * 
             * @return
             *     An immutable object of type {@link ProcessNote}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ProcessNote per the base specification
             */
            @Override
            public ProcessNote build() {
                ProcessNote processNote = new ProcessNote(this);
                if (validating) {
                    validate(processNote);
                }
                return processNote;
            }

            protected void validate(ProcessNote processNote) {
                super.validate(processNote);
                ValidationSupport.checkValueSetBinding(processNote.language, "language", "http://hl7.org/fhir/ValueSet/all-languages", "urn:ietf:bcp:47");
                ValidationSupport.requireValueOrChildren(processNote);
            }

            protected Builder from(ProcessNote processNote) {
                super.from(processNote);
                number = processNote.number;
                type = processNote.type;
                text = processNote.text;
                language = processNote.language;
                return this;
            }
        }
    }

    /**
     * Balance by Benefit Category.
     */
    public static class BenefitBalance extends BackboneElement {
        @Binding(
            bindingName = "BenefitCategory",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Benefit categories such as: oral, medical, vision, oral-basic etc.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-benefitcategory"
        )
        @Required
        private final CodeableConcept category;
        private final Boolean excluded;
        private final String name;
        private final String description;
        @Binding(
            bindingName = "BenefitNetwork",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Code to classify in or out of network services.",
            valueSet = "http://hl7.org/fhir/ValueSet/benefit-network"
        )
        private final CodeableConcept network;
        @Binding(
            bindingName = "BenefitUnit",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Unit covered/serviced - individual or family.",
            valueSet = "http://hl7.org/fhir/ValueSet/benefit-unit"
        )
        private final CodeableConcept unit;
        @Binding(
            bindingName = "BenefitTerm",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Coverage unit - annual, lifetime.",
            valueSet = "http://hl7.org/fhir/ValueSet/benefit-term"
        )
        private final CodeableConcept term;
        private final List<Financial> financial;

        private BenefitBalance(Builder builder) {
            super(builder);
            category = builder.category;
            excluded = builder.excluded;
            name = builder.name;
            description = builder.description;
            network = builder.network;
            unit = builder.unit;
            term = builder.term;
            financial = Collections.unmodifiableList(builder.financial);
        }

        /**
         * Code to identify the general type of benefits under which products and services are provided.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * True if the indicated class of service is excluded from the plan, missing or False indicates the product or service is 
         * included in the coverage.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getExcluded() {
            return excluded;
        }

        /**
         * A short name or tag for the benefit.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * A richer description of the benefit or services covered.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Is a flag to indicate whether the benefits refer to in-network providers or out-of-network providers.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getNetwork() {
            return network;
        }

        /**
         * Indicates if the benefits apply to an individual or to the family.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getUnit() {
            return unit;
        }

        /**
         * The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual visits'.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getTerm() {
            return term;
        }

        /**
         * Benefits Used to date.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Financial} that may be empty.
         */
        public List<Financial> getFinancial() {
            return financial;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (category != null) || 
                (excluded != null) || 
                (name != null) || 
                (description != null) || 
                (network != null) || 
                (unit != null) || 
                (term != null) || 
                !financial.isEmpty();
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
                    accept(category, "category", visitor);
                    accept(excluded, "excluded", visitor);
                    accept(name, "name", visitor);
                    accept(description, "description", visitor);
                    accept(network, "network", visitor);
                    accept(unit, "unit", visitor);
                    accept(term, "term", visitor);
                    accept(financial, "financial", visitor, Financial.class);
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
            BenefitBalance other = (BenefitBalance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(category, other.category) && 
                Objects.equals(excluded, other.excluded) && 
                Objects.equals(name, other.name) && 
                Objects.equals(description, other.description) && 
                Objects.equals(network, other.network) && 
                Objects.equals(unit, other.unit) && 
                Objects.equals(term, other.term) && 
                Objects.equals(financial, other.financial);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    category, 
                    excluded, 
                    name, 
                    description, 
                    network, 
                    unit, 
                    term, 
                    financial);
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
            private CodeableConcept category;
            private Boolean excluded;
            private String name;
            private String description;
            private CodeableConcept network;
            private CodeableConcept unit;
            private CodeableConcept term;
            private List<Financial> financial = new ArrayList<>();

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
             * Code to identify the general type of benefits under which products and services are provided.
             * 
             * <p>This element is required.
             * 
             * @param category
             *     Benefit classification
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder category(CodeableConcept category) {
                this.category = category;
                return this;
            }

            /**
             * Convenience method for setting {@code excluded}.
             * 
             * @param excluded
             *     Excluded from the plan
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #excluded(com.ibm.fhir.model.type.Boolean)
             */
            public Builder excluded(java.lang.Boolean excluded) {
                this.excluded = (excluded == null) ? null : Boolean.of(excluded);
                return this;
            }

            /**
             * True if the indicated class of service is excluded from the plan, missing or False indicates the product or service is 
             * included in the coverage.
             * 
             * @param excluded
             *     Excluded from the plan
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder excluded(Boolean excluded) {
                this.excluded = excluded;
                return this;
            }

            /**
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     Short name for the benefit
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
             * A short name or tag for the benefit.
             * 
             * @param name
             *     Short name for the benefit
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Description of the benefit or services covered
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #description(com.ibm.fhir.model.type.String)
             */
            public Builder description(java.lang.String description) {
                this.description = (description == null) ? null : String.of(description);
                return this;
            }

            /**
             * A richer description of the benefit or services covered.
             * 
             * @param description
             *     Description of the benefit or services covered
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Is a flag to indicate whether the benefits refer to in-network providers or out-of-network providers.
             * 
             * @param network
             *     In or out of network
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder network(CodeableConcept network) {
                this.network = network;
                return this;
            }

            /**
             * Indicates if the benefits apply to an individual or to the family.
             * 
             * @param unit
             *     Individual or family
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder unit(CodeableConcept unit) {
                this.unit = unit;
                return this;
            }

            /**
             * The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual visits'.
             * 
             * @param term
             *     Annual or lifetime
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder term(CodeableConcept term) {
                this.term = term;
                return this;
            }

            /**
             * Benefits Used to date.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param financial
             *     Benefit Summary
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder financial(Financial... financial) {
                for (Financial value : financial) {
                    this.financial.add(value);
                }
                return this;
            }

            /**
             * Benefits Used to date.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param financial
             *     Benefit Summary
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder financial(Collection<Financial> financial) {
                this.financial = new ArrayList<>(financial);
                return this;
            }

            /**
             * Build the {@link BenefitBalance}
             * 
             * <p>Required elements:
             * <ul>
             * <li>category</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link BenefitBalance}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid BenefitBalance per the base specification
             */
            @Override
            public BenefitBalance build() {
                BenefitBalance benefitBalance = new BenefitBalance(this);
                if (validating) {
                    validate(benefitBalance);
                }
                return benefitBalance;
            }

            protected void validate(BenefitBalance benefitBalance) {
                super.validate(benefitBalance);
                ValidationSupport.requireNonNull(benefitBalance.category, "category");
                ValidationSupport.checkList(benefitBalance.financial, "financial", Financial.class);
                ValidationSupport.requireValueOrChildren(benefitBalance);
            }

            protected Builder from(BenefitBalance benefitBalance) {
                super.from(benefitBalance);
                category = benefitBalance.category;
                excluded = benefitBalance.excluded;
                name = benefitBalance.name;
                description = benefitBalance.description;
                network = benefitBalance.network;
                unit = benefitBalance.unit;
                term = benefitBalance.term;
                financial.addAll(benefitBalance.financial);
                return this;
            }
        }

        /**
         * Benefits Used to date.
         */
        public static class Financial extends BackboneElement {
            @Binding(
                bindingName = "BenefitType",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Deductable, visits, co-pay, etc.",
                valueSet = "http://hl7.org/fhir/ValueSet/benefit-type"
            )
            @Required
            private final CodeableConcept type;
            @Choice({ UnsignedInt.class, String.class, Money.class })
            private final Element allowed;
            @Choice({ UnsignedInt.class, Money.class })
            private final Element used;

            private Financial(Builder builder) {
                super(builder);
                type = builder.type;
                allowed = builder.allowed;
                used = builder.used;
            }

            /**
             * Classification of benefit being provided.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * The quantity of the benefit which is permitted under the coverage.
             * 
             * @return
             *     An immutable object of type {@link UnsignedInt}, {@link String} or {@link Money} that may be null.
             */
            public Element getAllowed() {
                return allowed;
            }

            /**
             * The quantity of the benefit which have been consumed to date.
             * 
             * @return
             *     An immutable object of type {@link UnsignedInt} or {@link Money} that may be null.
             */
            public Element getUsed() {
                return used;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (allowed != null) || 
                    (used != null);
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
                        accept(used, "used", visitor);
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
                Financial other = (Financial) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(allowed, other.allowed) && 
                    Objects.equals(used, other.used);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        allowed, 
                        used);
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
                private Element allowed;
                private Element used;

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
                 * Classification of benefit being provided.
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     Benefit classification
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Convenience method for setting {@code allowed} with choice type String.
                 * 
                 * @param allowed
                 *     Benefits allowed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #allowed(Element)
                 */
                public Builder allowed(java.lang.String allowed) {
                    this.allowed = (allowed == null) ? null : String.of(allowed);
                    return this;
                }

                /**
                 * The quantity of the benefit which is permitted under the coverage.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link UnsignedInt}</li>
                 * <li>{@link String}</li>
                 * <li>{@link Money}</li>
                 * </ul>
                 * 
                 * @param allowed
                 *     Benefits allowed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder allowed(Element allowed) {
                    this.allowed = allowed;
                    return this;
                }

                /**
                 * The quantity of the benefit which have been consumed to date.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link UnsignedInt}</li>
                 * <li>{@link Money}</li>
                 * </ul>
                 * 
                 * @param used
                 *     Benefits used
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder used(Element used) {
                    this.used = used;
                    return this;
                }

                /**
                 * Build the {@link Financial}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Financial}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Financial per the base specification
                 */
                @Override
                public Financial build() {
                    Financial financial = new Financial(this);
                    if (validating) {
                        validate(financial);
                    }
                    return financial;
                }

                protected void validate(Financial financial) {
                    super.validate(financial);
                    ValidationSupport.requireNonNull(financial.type, "type");
                    ValidationSupport.choiceElement(financial.allowed, "allowed", UnsignedInt.class, String.class, Money.class);
                    ValidationSupport.choiceElement(financial.used, "used", UnsignedInt.class, Money.class);
                    ValidationSupport.requireValueOrChildren(financial);
                }

                protected Builder from(Financial financial) {
                    super.from(financial);
                    type = financial.type;
                    allowed = financial.allowed;
                    used = financial.used;
                    return this;
                }
            }
        }
    }
}
