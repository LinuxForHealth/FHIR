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
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Dosage;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.MedicationRequestIntent;
import com.ibm.fhir.model.type.code.MedicationRequestPriority;
import com.ibm.fhir.model.type.code.MedicationRequestStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * An order or request for both supply of the medication and the instructions for administration of the medication to a 
 * patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to 
 * generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with 
 * workflow patterns.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationRequest extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "MedicationRequestStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A coded concept specifying the state of the prescribing event. Describes the lifecycle of the prescription.",
        valueSet = "http://hl7.org/fhir/ValueSet/medicationrequest-status|4.3.0"
    )
    @Required
    private final MedicationRequestStatus status;
    @Binding(
        bindingName = "MedicationRequestStatusReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Identifies the reasons for a given status.",
        valueSet = "http://hl7.org/fhir/ValueSet/medicationrequest-status-reason"
    )
    private final CodeableConcept statusReason;
    @Summary
    @Binding(
        bindingName = "MedicationRequestIntent",
        strength = BindingStrength.Value.REQUIRED,
        description = "The kind of medication order.",
        valueSet = "http://hl7.org/fhir/ValueSet/medicationrequest-intent|4.3.0"
    )
    @Required
    private final MedicationRequestIntent intent;
    @Binding(
        bindingName = "MedicationRequestCategory",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept identifying the category of medication request.  For example, where the medication is to be consumed or administered, or the type of medication treatment.",
        valueSet = "http://hl7.org/fhir/ValueSet/medicationrequest-category"
    )
    private final List<CodeableConcept> category;
    @Summary
    @Binding(
        bindingName = "MedicationRequestPriority",
        strength = BindingStrength.Value.REQUIRED,
        description = "Identifies the level of importance to be assigned to actioning the request.",
        valueSet = "http://hl7.org/fhir/ValueSet/request-priority|4.3.0"
    )
    private final MedicationRequestPriority priority;
    @Summary
    private final Boolean doNotPerform;
    @Summary
    @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Organization" })
    @Choice({ Boolean.class, Reference.class })
    private final Element reported;
    @Summary
    @ReferenceTarget({ "Medication" })
    @Choice({ CodeableConcept.class, Reference.class })
    @Binding(
        bindingName = "MedicationCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept identifying substance or product that can be ordered.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-codes"
    )
    @Required
    private final Element medication;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    @Required
    private final Reference subject;
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    private final List<Reference> supportingInformation;
    @Summary
    private final DateTime authoredOn;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "Patient", "RelatedPerson", "Device" })
    private final Reference requester;
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "Patient", "Device", "RelatedPerson", "CareTeam" })
    private final Reference performer;
    @Summary
    @Binding(
        bindingName = "MedicationRequestPerformerType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Identifies the type of individual that is desired to administer the medication.",
        valueSet = "http://hl7.org/fhir/ValueSet/performer-role"
    )
    private final CodeableConcept performerType;
    @ReferenceTarget({ "Practitioner", "PractitionerRole" })
    private final Reference recorder;
    @Binding(
        bindingName = "MedicationRequestReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept indicating why the medication was ordered.",
        valueSet = "http://hl7.org/fhir/ValueSet/condition-code"
    )
    private final List<CodeableConcept> reasonCode;
    @ReferenceTarget({ "Condition", "Observation" })
    private final List<Reference> reasonReference;
    @Summary
    private final List<Canonical> instantiatesCanonical;
    @Summary
    private final List<Uri> instantiatesUri;
    @Summary
    @ReferenceTarget({ "CarePlan", "MedicationRequest", "ServiceRequest", "ImmunizationRecommendation" })
    private final List<Reference> basedOn;
    @Summary
    private final Identifier groupIdentifier;
    @Binding(
        bindingName = "MedicationRequestCourseOfTherapy",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Identifies the overall pattern of medication administratio.",
        valueSet = "http://hl7.org/fhir/ValueSet/medicationrequest-course-of-therapy"
    )
    private final CodeableConcept courseOfTherapyType;
    @ReferenceTarget({ "Coverage", "ClaimResponse" })
    private final List<Reference> insurance;
    private final List<Annotation> note;
    private final List<Dosage> dosageInstruction;
    private final DispenseRequest dispenseRequest;
    private final Substitution substitution;
    @ReferenceTarget({ "MedicationRequest" })
    private final Reference priorPrescription;
    @ReferenceTarget({ "DetectedIssue" })
    private final List<Reference> detectedIssue;
    @ReferenceTarget({ "Provenance" })
    private final List<Reference> eventHistory;

    private MedicationRequest(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        statusReason = builder.statusReason;
        intent = builder.intent;
        category = Collections.unmodifiableList(builder.category);
        priority = builder.priority;
        doNotPerform = builder.doNotPerform;
        reported = builder.reported;
        medication = builder.medication;
        subject = builder.subject;
        encounter = builder.encounter;
        supportingInformation = Collections.unmodifiableList(builder.supportingInformation);
        authoredOn = builder.authoredOn;
        requester = builder.requester;
        performer = builder.performer;
        performerType = builder.performerType;
        recorder = builder.recorder;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
        instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        groupIdentifier = builder.groupIdentifier;
        courseOfTherapyType = builder.courseOfTherapyType;
        insurance = Collections.unmodifiableList(builder.insurance);
        note = Collections.unmodifiableList(builder.note);
        dosageInstruction = Collections.unmodifiableList(builder.dosageInstruction);
        dispenseRequest = builder.dispenseRequest;
        substitution = builder.substitution;
        priorPrescription = builder.priorPrescription;
        detectedIssue = Collections.unmodifiableList(builder.detectedIssue);
        eventHistory = Collections.unmodifiableList(builder.eventHistory);
    }

    /**
     * Identifiers associated with this medication request that are defined by business processes and/or used to refer to it 
     * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
     * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
     * to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A code specifying the current state of the order. Generally, this will be active or completed state.
     * 
     * @return
     *     An immutable object of type {@link MedicationRequestStatus} that is non-null.
     */
    public MedicationRequestStatus getStatus() {
        return status;
    }

    /**
     * Captures the reason for the current state of the MedicationRequest.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatusReason() {
        return statusReason;
    }

    /**
     * Whether the request is a proposal, plan, or an original order.
     * 
     * @return
     *     An immutable object of type {@link MedicationRequestIntent} that is non-null.
     */
    public MedicationRequestIntent getIntent() {
        return intent;
    }

    /**
     * Indicates the type of medication request (for example, where the medication is expected to be consumed or administered 
     * (i.e. inpatient or outpatient)).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * Indicates how quickly the Medication Request should be addressed with respect to other requests.
     * 
     * @return
     *     An immutable object of type {@link MedicationRequestPriority} that may be null.
     */
    public MedicationRequestPriority getPriority() {
        return priority;
    }

    /**
     * If true indicates that the provider is asking for the medication request not to occur.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getDoNotPerform() {
        return doNotPerform;
    }

    /**
     * Indicates if this record was captured as a secondary 'reported' record rather than as an original primary source-of-
     * truth record. It may also indicate the source of the report.
     * 
     * @return
     *     An immutable object of type {@link Boolean} or {@link Reference} that may be null.
     */
    public Element getReported() {
        return reported;
    }

    /**
     * Identifies the medication being requested. This is a link to a resource that represents the medication which may be 
     * the details of the medication or simply an attribute carrying a code that identifies the medication from a known list 
     * of medications.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} or {@link Reference} that is non-null.
     */
    public Element getMedication() {
        return medication;
    }

    /**
     * A link to a resource representing the person or set of individuals to whom the medication will be given.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The Encounter during which this [x] was created or to which the creation of this record is tightly associated.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * Include additional information (for example, patient height and weight) that supports the ordering of the medication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSupportingInformation() {
        return supportingInformation;
    }

    /**
     * The date (and perhaps time) when the prescription was initially written or authored on.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getAuthoredOn() {
        return authoredOn;
    }

    /**
     * The individual, organization, or device that initiated the request and has responsibility for its activation.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRequester() {
        return requester;
    }

    /**
     * The specified desired performer of the medication treatment (e.g. the performer of the medication administration).
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPerformer() {
        return performer;
    }

    /**
     * Indicates the type of performer of the administration of the medication.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPerformerType() {
        return performerType;
    }

    /**
     * The person who entered the order on behalf of another individual for example in the case of a verbal or a telephone 
     * order.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRecorder() {
        return recorder;
    }

    /**
     * The reason or the indication for ordering or not ordering the medication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Condition or observation that supports why the medication was ordered.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * The URL pointing to a protocol, guideline, orderset, or other definition that is adhered to in whole or in part by 
     * this MedicationRequest.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this MedicationRequest.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * A plan or request that is fulfilled in whole or in part by this medication request.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * A shared identifier common to all requests that were authorized more or less simultaneously by a single author, 
     * representing the identifier of the requisition or prescription.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getGroupIdentifier() {
        return groupIdentifier;
    }

    /**
     * The description of the overall patte3rn of the administration of the medication to the patient.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCourseOfTherapyType() {
        return courseOfTherapyType;
    }

    /**
     * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be required for delivering 
     * the requested service.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getInsurance() {
        return insurance;
    }

    /**
     * Extra information about the prescription that could not be conveyed by the other attributes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Indicates how the medication is to be used by the patient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Dosage} that may be empty.
     */
    public List<Dosage> getDosageInstruction() {
        return dosageInstruction;
    }

    /**
     * Indicates the specific details for the dispense or medication supply part of a medication request (also known as a 
     * Medication Prescription or Medication Order). Note that this information is not always sent with the order. There may 
     * be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the 
     * pharmacy department.
     * 
     * @return
     *     An immutable object of type {@link DispenseRequest} that may be null.
     */
    public DispenseRequest getDispenseRequest() {
        return dispenseRequest;
    }

    /**
     * Indicates whether or not substitution can or should be part of the dispense. In some cases, substitution must happen, 
     * in other cases substitution must not happen. This block explains the prescriber's intent. If nothing is specified 
     * substitution may be done.
     * 
     * @return
     *     An immutable object of type {@link Substitution} that may be null.
     */
    public Substitution getSubstitution() {
        return substitution;
    }

    /**
     * A link to a resource representing an earlier order related order or prescription.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPriorPrescription() {
        return priorPrescription;
    }

    /**
     * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
     * patient; e.g. Drug-drug interaction, duplicate therapy, dosage alert etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getDetectedIssue() {
        return detectedIssue;
    }

    /**
     * Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify 
     * key state transitions or updates that are likely to be relevant to a user looking at the current version of the 
     * resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getEventHistory() {
        return eventHistory;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (statusReason != null) || 
            (intent != null) || 
            !category.isEmpty() || 
            (priority != null) || 
            (doNotPerform != null) || 
            (reported != null) || 
            (medication != null) || 
            (subject != null) || 
            (encounter != null) || 
            !supportingInformation.isEmpty() || 
            (authoredOn != null) || 
            (requester != null) || 
            (performer != null) || 
            (performerType != null) || 
            (recorder != null) || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            !instantiatesCanonical.isEmpty() || 
            !instantiatesUri.isEmpty() || 
            !basedOn.isEmpty() || 
            (groupIdentifier != null) || 
            (courseOfTherapyType != null) || 
            !insurance.isEmpty() || 
            !note.isEmpty() || 
            !dosageInstruction.isEmpty() || 
            (dispenseRequest != null) || 
            (substitution != null) || 
            (priorPrescription != null) || 
            !detectedIssue.isEmpty() || 
            !eventHistory.isEmpty();
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
                accept(statusReason, "statusReason", visitor);
                accept(intent, "intent", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(priority, "priority", visitor);
                accept(doNotPerform, "doNotPerform", visitor);
                accept(reported, "reported", visitor);
                accept(medication, "medication", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(supportingInformation, "supportingInformation", visitor, Reference.class);
                accept(authoredOn, "authoredOn", visitor);
                accept(requester, "requester", visitor);
                accept(performer, "performer", visitor);
                accept(performerType, "performerType", visitor);
                accept(recorder, "recorder", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(instantiatesCanonical, "instantiatesCanonical", visitor, Canonical.class);
                accept(instantiatesUri, "instantiatesUri", visitor, Uri.class);
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(groupIdentifier, "groupIdentifier", visitor);
                accept(courseOfTherapyType, "courseOfTherapyType", visitor);
                accept(insurance, "insurance", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(dosageInstruction, "dosageInstruction", visitor, Dosage.class);
                accept(dispenseRequest, "dispenseRequest", visitor);
                accept(substitution, "substitution", visitor);
                accept(priorPrescription, "priorPrescription", visitor);
                accept(detectedIssue, "detectedIssue", visitor, Reference.class);
                accept(eventHistory, "eventHistory", visitor, Reference.class);
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
        MedicationRequest other = (MedicationRequest) obj;
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
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(intent, other.intent) && 
            Objects.equals(category, other.category) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(doNotPerform, other.doNotPerform) && 
            Objects.equals(reported, other.reported) && 
            Objects.equals(medication, other.medication) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(supportingInformation, other.supportingInformation) && 
            Objects.equals(authoredOn, other.authoredOn) && 
            Objects.equals(requester, other.requester) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(performerType, other.performerType) && 
            Objects.equals(recorder, other.recorder) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(instantiatesCanonical, other.instantiatesCanonical) && 
            Objects.equals(instantiatesUri, other.instantiatesUri) && 
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(groupIdentifier, other.groupIdentifier) && 
            Objects.equals(courseOfTherapyType, other.courseOfTherapyType) && 
            Objects.equals(insurance, other.insurance) && 
            Objects.equals(note, other.note) && 
            Objects.equals(dosageInstruction, other.dosageInstruction) && 
            Objects.equals(dispenseRequest, other.dispenseRequest) && 
            Objects.equals(substitution, other.substitution) && 
            Objects.equals(priorPrescription, other.priorPrescription) && 
            Objects.equals(detectedIssue, other.detectedIssue) && 
            Objects.equals(eventHistory, other.eventHistory);
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
                statusReason, 
                intent, 
                category, 
                priority, 
                doNotPerform, 
                reported, 
                medication, 
                subject, 
                encounter, 
                supportingInformation, 
                authoredOn, 
                requester, 
                performer, 
                performerType, 
                recorder, 
                reasonCode, 
                reasonReference, 
                instantiatesCanonical, 
                instantiatesUri, 
                basedOn, 
                groupIdentifier, 
                courseOfTherapyType, 
                insurance, 
                note, 
                dosageInstruction, 
                dispenseRequest, 
                substitution, 
                priorPrescription, 
                detectedIssue, 
                eventHistory);
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
        private MedicationRequestStatus status;
        private CodeableConcept statusReason;
        private MedicationRequestIntent intent;
        private List<CodeableConcept> category = new ArrayList<>();
        private MedicationRequestPriority priority;
        private Boolean doNotPerform;
        private Element reported;
        private Element medication;
        private Reference subject;
        private Reference encounter;
        private List<Reference> supportingInformation = new ArrayList<>();
        private DateTime authoredOn;
        private Reference requester;
        private Reference performer;
        private CodeableConcept performerType;
        private Reference recorder;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Canonical> instantiatesCanonical = new ArrayList<>();
        private List<Uri> instantiatesUri = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private Identifier groupIdentifier;
        private CodeableConcept courseOfTherapyType;
        private List<Reference> insurance = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Dosage> dosageInstruction = new ArrayList<>();
        private DispenseRequest dispenseRequest;
        private Substitution substitution;
        private Reference priorPrescription;
        private List<Reference> detectedIssue = new ArrayList<>();
        private List<Reference> eventHistory = new ArrayList<>();

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
         * Identifiers associated with this medication request that are defined by business processes and/or used to refer to it 
         * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
         * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
         * to server.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External ids for this request
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
         * Identifiers associated with this medication request that are defined by business processes and/or used to refer to it 
         * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
         * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
         * to server.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External ids for this request
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
         * A code specifying the current state of the order. Generally, this will be active or completed state.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | on-hold | cancelled | completed | entered-in-error | stopped | draft | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(MedicationRequestStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Captures the reason for the current state of the MedicationRequest.
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusReason(CodeableConcept statusReason) {
            this.statusReason = statusReason;
            return this;
        }

        /**
         * Whether the request is a proposal, plan, or an original order.
         * 
         * <p>This element is required.
         * 
         * @param intent
         *     proposal | plan | order | original-order | reflex-order | filler-order | instance-order | option
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder intent(MedicationRequestIntent intent) {
            this.intent = intent;
            return this;
        }

        /**
         * Indicates the type of medication request (for example, where the medication is expected to be consumed or administered 
         * (i.e. inpatient or outpatient)).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Type of medication usage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * Indicates the type of medication request (for example, where the medication is expected to be consumed or administered 
         * (i.e. inpatient or outpatient)).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Type of medication usage
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * Indicates how quickly the Medication Request should be addressed with respect to other requests.
         * 
         * @param priority
         *     routine | urgent | asap | stat
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priority(MedicationRequestPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Convenience method for setting {@code doNotPerform}.
         * 
         * @param doNotPerform
         *     True if request is prohibiting action
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #doNotPerform(com.ibm.fhir.model.type.Boolean)
         */
        public Builder doNotPerform(java.lang.Boolean doNotPerform) {
            this.doNotPerform = (doNotPerform == null) ? null : Boolean.of(doNotPerform);
            return this;
        }

        /**
         * If true indicates that the provider is asking for the medication request not to occur.
         * 
         * @param doNotPerform
         *     True if request is prohibiting action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder doNotPerform(Boolean doNotPerform) {
            this.doNotPerform = doNotPerform;
            return this;
        }

        /**
         * Convenience method for setting {@code reported} with choice type Boolean.
         * 
         * @param reported
         *     Reported rather than primary record
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #reported(Element)
         */
        public Builder reported(java.lang.Boolean reported) {
            this.reported = (reported == null) ? null : Boolean.of(reported);
            return this;
        }

        /**
         * Indicates if this record was captured as a secondary 'reported' record rather than as an original primary source-of-
         * truth record. It may also indicate the source of the report.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Boolean}</li>
         * <li>{@link Reference}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param reported
         *     Reported rather than primary record
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reported(Element reported) {
            this.reported = reported;
            return this;
        }

        /**
         * Identifies the medication being requested. This is a link to a resource that represents the medication which may be 
         * the details of the medication or simply an attribute carrying a code that identifies the medication from a known list 
         * of medications.
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
         * <li>{@link Medication}</li>
         * </ul>
         * 
         * @param medication
         *     Medication to be taken
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder medication(Element medication) {
            this.medication = medication;
            return this;
        }

        /**
         * A link to a resource representing the person or set of individuals to whom the medication will be given.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param subject
         *     Who or group medication request is for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The Encounter during which this [x] was created or to which the creation of this record is tightly associated.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param encounter
         *     Encounter created as part of encounter/admission/stay
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * Include additional information (for example, patient height and weight) that supports the ordering of the medication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInformation
         *     Information to support ordering of the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInformation(Reference... supportingInformation) {
            for (Reference value : supportingInformation) {
                this.supportingInformation.add(value);
            }
            return this;
        }

        /**
         * Include additional information (for example, patient height and weight) that supports the ordering of the medication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInformation
         *     Information to support ordering of the medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder supportingInformation(Collection<Reference> supportingInformation) {
            this.supportingInformation = new ArrayList<>(supportingInformation);
            return this;
        }

        /**
         * The date (and perhaps time) when the prescription was initially written or authored on.
         * 
         * @param authoredOn
         *     When request was initially authored
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authoredOn(DateTime authoredOn) {
            this.authoredOn = authoredOn;
            return this;
        }

        /**
         * The individual, organization, or device that initiated the request and has responsibility for its activation.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param requester
         *     Who/What requested the Request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder requester(Reference requester) {
            this.requester = requester;
            return this;
        }

        /**
         * The specified desired performer of the medication treatment (e.g. the performer of the medication administration).
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Device}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link CareTeam}</li>
         * </ul>
         * 
         * @param performer
         *     Intended performer of administration
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Reference performer) {
            this.performer = performer;
            return this;
        }

        /**
         * Indicates the type of performer of the administration of the medication.
         * 
         * @param performerType
         *     Desired kind of performer of the medication administration
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performerType(CodeableConcept performerType) {
            this.performerType = performerType;
            return this;
        }

        /**
         * The person who entered the order on behalf of another individual for example in the case of a verbal or a telephone 
         * order.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param recorder
         *     Person who entered the request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recorder(Reference recorder) {
            this.recorder = recorder;
            return this;
        }

        /**
         * The reason or the indication for ordering or not ordering the medication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Reason or indication for ordering or not ordering the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(CodeableConcept... reasonCode) {
            for (CodeableConcept value : reasonCode) {
                this.reasonCode.add(value);
            }
            return this;
        }

        /**
         * The reason or the indication for ordering or not ordering the medication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Reason or indication for ordering or not ordering the medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * Condition or observation that supports why the medication was ordered.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Condition or observation that supports why the prescription is being written
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Reference... reasonReference) {
            for (Reference value : reasonReference) {
                this.reasonReference.add(value);
            }
            return this;
        }

        /**
         * Condition or observation that supports why the medication was ordered.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Condition or observation that supports why the prescription is being written
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * The URL pointing to a protocol, guideline, orderset, or other definition that is adhered to in whole or in part by 
         * this MedicationRequest.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The URL pointing to a protocol, guideline, orderset, or other definition that is adhered to in whole or in part by 
         * this MedicationRequest.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical = new ArrayList<>(instantiatesCanonical);
            return this;
        }

        /**
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this MedicationRequest.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this MedicationRequest.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri = new ArrayList<>(instantiatesUri);
            return this;
        }

        /**
         * A plan or request that is fulfilled in whole or in part by this medication request.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link CarePlan}</li>
         * <li>{@link MedicationRequest}</li>
         * <li>{@link ServiceRequest}</li>
         * <li>{@link ImmunizationRecommendation}</li>
         * </ul>
         * 
         * @param basedOn
         *     What request fulfills
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * A plan or request that is fulfilled in whole or in part by this medication request.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link CarePlan}</li>
         * <li>{@link MedicationRequest}</li>
         * <li>{@link ServiceRequest}</li>
         * <li>{@link ImmunizationRecommendation}</li>
         * </ul>
         * 
         * @param basedOn
         *     What request fulfills
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * A shared identifier common to all requests that were authorized more or less simultaneously by a single author, 
         * representing the identifier of the requisition or prescription.
         * 
         * @param groupIdentifier
         *     Composite request this is part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder groupIdentifier(Identifier groupIdentifier) {
            this.groupIdentifier = groupIdentifier;
            return this;
        }

        /**
         * The description of the overall patte3rn of the administration of the medication to the patient.
         * 
         * @param courseOfTherapyType
         *     Overall pattern of medication administration
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder courseOfTherapyType(CodeableConcept courseOfTherapyType) {
            this.courseOfTherapyType = courseOfTherapyType;
            return this;
        }

        /**
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be required for delivering 
         * the requested service.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Coverage}</li>
         * <li>{@link ClaimResponse}</li>
         * </ul>
         * 
         * @param insurance
         *     Associated insurance coverage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Reference... insurance) {
            for (Reference value : insurance) {
                this.insurance.add(value);
            }
            return this;
        }

        /**
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be required for delivering 
         * the requested service.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Coverage}</li>
         * <li>{@link ClaimResponse}</li>
         * </ul>
         * 
         * @param insurance
         *     Associated insurance coverage
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder insurance(Collection<Reference> insurance) {
            this.insurance = new ArrayList<>(insurance);
            return this;
        }

        /**
         * Extra information about the prescription that could not be conveyed by the other attributes.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Information about the prescription
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
         * Extra information about the prescription that could not be conveyed by the other attributes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Information about the prescription
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * Indicates how the medication is to be used by the patient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dosageInstruction
         *     How the medication should be taken
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dosageInstruction(Dosage... dosageInstruction) {
            for (Dosage value : dosageInstruction) {
                this.dosageInstruction.add(value);
            }
            return this;
        }

        /**
         * Indicates how the medication is to be used by the patient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dosageInstruction
         *     How the medication should be taken
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder dosageInstruction(Collection<Dosage> dosageInstruction) {
            this.dosageInstruction = new ArrayList<>(dosageInstruction);
            return this;
        }

        /**
         * Indicates the specific details for the dispense or medication supply part of a medication request (also known as a 
         * Medication Prescription or Medication Order). Note that this information is not always sent with the order. There may 
         * be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the 
         * pharmacy department.
         * 
         * @param dispenseRequest
         *     Medication supply authorization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dispenseRequest(DispenseRequest dispenseRequest) {
            this.dispenseRequest = dispenseRequest;
            return this;
        }

        /**
         * Indicates whether or not substitution can or should be part of the dispense. In some cases, substitution must happen, 
         * in other cases substitution must not happen. This block explains the prescriber's intent. If nothing is specified 
         * substitution may be done.
         * 
         * @param substitution
         *     Any restrictions on medication substitution
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder substitution(Substitution substitution) {
            this.substitution = substitution;
            return this;
        }

        /**
         * A link to a resource representing an earlier order related order or prescription.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link MedicationRequest}</li>
         * </ul>
         * 
         * @param priorPrescription
         *     An order/prescription that is being replaced
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priorPrescription(Reference priorPrescription) {
            this.priorPrescription = priorPrescription;
            return this;
        }

        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
         * patient; e.g. Drug-drug interaction, duplicate therapy, dosage alert etc.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DetectedIssue}</li>
         * </ul>
         * 
         * @param detectedIssue
         *     Clinical Issue with action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder detectedIssue(Reference... detectedIssue) {
            for (Reference value : detectedIssue) {
                this.detectedIssue.add(value);
            }
            return this;
        }

        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
         * patient; e.g. Drug-drug interaction, duplicate therapy, dosage alert etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DetectedIssue}</li>
         * </ul>
         * 
         * @param detectedIssue
         *     Clinical Issue with action
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder detectedIssue(Collection<Reference> detectedIssue) {
            this.detectedIssue = new ArrayList<>(detectedIssue);
            return this;
        }

        /**
         * Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify 
         * key state transitions or updates that are likely to be relevant to a user looking at the current version of the 
         * resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Provenance}</li>
         * </ul>
         * 
         * @param eventHistory
         *     A list of events of interest in the lifecycle
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder eventHistory(Reference... eventHistory) {
            for (Reference value : eventHistory) {
                this.eventHistory.add(value);
            }
            return this;
        }

        /**
         * Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify 
         * key state transitions or updates that are likely to be relevant to a user looking at the current version of the 
         * resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Provenance}</li>
         * </ul>
         * 
         * @param eventHistory
         *     A list of events of interest in the lifecycle
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder eventHistory(Collection<Reference> eventHistory) {
            this.eventHistory = new ArrayList<>(eventHistory);
            return this;
        }

        /**
         * Build the {@link MedicationRequest}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>intent</li>
         * <li>medication</li>
         * <li>subject</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MedicationRequest}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicationRequest per the base specification
         */
        @Override
        public MedicationRequest build() {
            MedicationRequest medicationRequest = new MedicationRequest(this);
            if (validating) {
                validate(medicationRequest);
            }
            return medicationRequest;
        }

        protected void validate(MedicationRequest medicationRequest) {
            super.validate(medicationRequest);
            ValidationSupport.checkList(medicationRequest.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(medicationRequest.status, "status");
            ValidationSupport.requireNonNull(medicationRequest.intent, "intent");
            ValidationSupport.checkList(medicationRequest.category, "category", CodeableConcept.class);
            ValidationSupport.choiceElement(medicationRequest.reported, "reported", Boolean.class, Reference.class);
            ValidationSupport.requireChoiceElement(medicationRequest.medication, "medication", CodeableConcept.class, Reference.class);
            ValidationSupport.requireNonNull(medicationRequest.subject, "subject");
            ValidationSupport.checkList(medicationRequest.supportingInformation, "supportingInformation", Reference.class);
            ValidationSupport.checkList(medicationRequest.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.checkList(medicationRequest.reasonReference, "reasonReference", Reference.class);
            ValidationSupport.checkList(medicationRequest.instantiatesCanonical, "instantiatesCanonical", Canonical.class);
            ValidationSupport.checkList(medicationRequest.instantiatesUri, "instantiatesUri", Uri.class);
            ValidationSupport.checkList(medicationRequest.basedOn, "basedOn", Reference.class);
            ValidationSupport.checkList(medicationRequest.insurance, "insurance", Reference.class);
            ValidationSupport.checkList(medicationRequest.note, "note", Annotation.class);
            ValidationSupport.checkList(medicationRequest.dosageInstruction, "dosageInstruction", Dosage.class);
            ValidationSupport.checkList(medicationRequest.detectedIssue, "detectedIssue", Reference.class);
            ValidationSupport.checkList(medicationRequest.eventHistory, "eventHistory", Reference.class);
            ValidationSupport.checkReferenceType(medicationRequest.reported, "reported", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Organization");
            ValidationSupport.checkReferenceType(medicationRequest.medication, "medication", "Medication");
            ValidationSupport.checkReferenceType(medicationRequest.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(medicationRequest.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(medicationRequest.requester, "requester", "Practitioner", "PractitionerRole", "Organization", "Patient", "RelatedPerson", "Device");
            ValidationSupport.checkReferenceType(medicationRequest.performer, "performer", "Practitioner", "PractitionerRole", "Organization", "Patient", "Device", "RelatedPerson", "CareTeam");
            ValidationSupport.checkReferenceType(medicationRequest.recorder, "recorder", "Practitioner", "PractitionerRole");
            ValidationSupport.checkReferenceType(medicationRequest.reasonReference, "reasonReference", "Condition", "Observation");
            ValidationSupport.checkReferenceType(medicationRequest.basedOn, "basedOn", "CarePlan", "MedicationRequest", "ServiceRequest", "ImmunizationRecommendation");
            ValidationSupport.checkReferenceType(medicationRequest.insurance, "insurance", "Coverage", "ClaimResponse");
            ValidationSupport.checkReferenceType(medicationRequest.priorPrescription, "priorPrescription", "MedicationRequest");
            ValidationSupport.checkReferenceType(medicationRequest.detectedIssue, "detectedIssue", "DetectedIssue");
            ValidationSupport.checkReferenceType(medicationRequest.eventHistory, "eventHistory", "Provenance");
        }

        protected Builder from(MedicationRequest medicationRequest) {
            super.from(medicationRequest);
            identifier.addAll(medicationRequest.identifier);
            status = medicationRequest.status;
            statusReason = medicationRequest.statusReason;
            intent = medicationRequest.intent;
            category.addAll(medicationRequest.category);
            priority = medicationRequest.priority;
            doNotPerform = medicationRequest.doNotPerform;
            reported = medicationRequest.reported;
            medication = medicationRequest.medication;
            subject = medicationRequest.subject;
            encounter = medicationRequest.encounter;
            supportingInformation.addAll(medicationRequest.supportingInformation);
            authoredOn = medicationRequest.authoredOn;
            requester = medicationRequest.requester;
            performer = medicationRequest.performer;
            performerType = medicationRequest.performerType;
            recorder = medicationRequest.recorder;
            reasonCode.addAll(medicationRequest.reasonCode);
            reasonReference.addAll(medicationRequest.reasonReference);
            instantiatesCanonical.addAll(medicationRequest.instantiatesCanonical);
            instantiatesUri.addAll(medicationRequest.instantiatesUri);
            basedOn.addAll(medicationRequest.basedOn);
            groupIdentifier = medicationRequest.groupIdentifier;
            courseOfTherapyType = medicationRequest.courseOfTherapyType;
            insurance.addAll(medicationRequest.insurance);
            note.addAll(medicationRequest.note);
            dosageInstruction.addAll(medicationRequest.dosageInstruction);
            dispenseRequest = medicationRequest.dispenseRequest;
            substitution = medicationRequest.substitution;
            priorPrescription = medicationRequest.priorPrescription;
            detectedIssue.addAll(medicationRequest.detectedIssue);
            eventHistory.addAll(medicationRequest.eventHistory);
            return this;
        }
    }

    /**
     * Indicates the specific details for the dispense or medication supply part of a medication request (also known as a 
     * Medication Prescription or Medication Order). Note that this information is not always sent with the order. There may 
     * be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the 
     * pharmacy department.
     */
    public static class DispenseRequest extends BackboneElement {
        private final InitialFill initialFill;
        private final Duration dispenseInterval;
        private final Period validityPeriod;
        private final UnsignedInt numberOfRepeatsAllowed;
        private final SimpleQuantity quantity;
        private final Duration expectedSupplyDuration;
        @ReferenceTarget({ "Organization" })
        private final Reference performer;

        private DispenseRequest(Builder builder) {
            super(builder);
            initialFill = builder.initialFill;
            dispenseInterval = builder.dispenseInterval;
            validityPeriod = builder.validityPeriod;
            numberOfRepeatsAllowed = builder.numberOfRepeatsAllowed;
            quantity = builder.quantity;
            expectedSupplyDuration = builder.expectedSupplyDuration;
            performer = builder.performer;
        }

        /**
         * Indicates the quantity or duration for the first dispense of the medication.
         * 
         * @return
         *     An immutable object of type {@link InitialFill} that may be null.
         */
        public InitialFill getInitialFill() {
            return initialFill;
        }

        /**
         * The minimum period of time that must occur between dispenses of the medication.
         * 
         * @return
         *     An immutable object of type {@link Duration} that may be null.
         */
        public Duration getDispenseInterval() {
            return dispenseInterval;
        }

        /**
         * This indicates the validity period of a prescription (stale dating the Prescription).
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getValidityPeriod() {
            return validityPeriod;
        }

        /**
         * An integer indicating the number of times, in addition to the original dispense, (aka refills or repeats) that the 
         * patient can receive the prescribed medication. Usage Notes: This integer does not include the original order dispense. 
         * This means that if an order indicates dispense 30 tablets plus "3 repeats", then the order can be dispensed a total of 
         * 4 times and the patient can receive a total of 120 tablets. A prescriber may explicitly say that zero refills are 
         * permitted after the initial dispense.
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt} that may be null.
         */
        public UnsignedInt getNumberOfRepeatsAllowed() {
            return numberOfRepeatsAllowed;
        }

        /**
         * The amount that is to be dispensed for one fill.
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getQuantity() {
            return quantity;
        }

        /**
         * Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense 
         * is expected to last.
         * 
         * @return
         *     An immutable object of type {@link Duration} that may be null.
         */
        public Duration getExpectedSupplyDuration() {
            return expectedSupplyDuration;
        }

        /**
         * Indicates the intended dispensing Organization specified by the prescriber.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getPerformer() {
            return performer;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (initialFill != null) || 
                (dispenseInterval != null) || 
                (validityPeriod != null) || 
                (numberOfRepeatsAllowed != null) || 
                (quantity != null) || 
                (expectedSupplyDuration != null) || 
                (performer != null);
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
                    accept(initialFill, "initialFill", visitor);
                    accept(dispenseInterval, "dispenseInterval", visitor);
                    accept(validityPeriod, "validityPeriod", visitor);
                    accept(numberOfRepeatsAllowed, "numberOfRepeatsAllowed", visitor);
                    accept(quantity, "quantity", visitor);
                    accept(expectedSupplyDuration, "expectedSupplyDuration", visitor);
                    accept(performer, "performer", visitor);
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
            DispenseRequest other = (DispenseRequest) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(initialFill, other.initialFill) && 
                Objects.equals(dispenseInterval, other.dispenseInterval) && 
                Objects.equals(validityPeriod, other.validityPeriod) && 
                Objects.equals(numberOfRepeatsAllowed, other.numberOfRepeatsAllowed) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(expectedSupplyDuration, other.expectedSupplyDuration) && 
                Objects.equals(performer, other.performer);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    initialFill, 
                    dispenseInterval, 
                    validityPeriod, 
                    numberOfRepeatsAllowed, 
                    quantity, 
                    expectedSupplyDuration, 
                    performer);
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
            private InitialFill initialFill;
            private Duration dispenseInterval;
            private Period validityPeriod;
            private UnsignedInt numberOfRepeatsAllowed;
            private SimpleQuantity quantity;
            private Duration expectedSupplyDuration;
            private Reference performer;

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
             * Indicates the quantity or duration for the first dispense of the medication.
             * 
             * @param initialFill
             *     First fill details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder initialFill(InitialFill initialFill) {
                this.initialFill = initialFill;
                return this;
            }

            /**
             * The minimum period of time that must occur between dispenses of the medication.
             * 
             * @param dispenseInterval
             *     Minimum period of time between dispenses
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dispenseInterval(Duration dispenseInterval) {
                this.dispenseInterval = dispenseInterval;
                return this;
            }

            /**
             * This indicates the validity period of a prescription (stale dating the Prescription).
             * 
             * @param validityPeriod
             *     Time period supply is authorized for
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder validityPeriod(Period validityPeriod) {
                this.validityPeriod = validityPeriod;
                return this;
            }

            /**
             * An integer indicating the number of times, in addition to the original dispense, (aka refills or repeats) that the 
             * patient can receive the prescribed medication. Usage Notes: This integer does not include the original order dispense. 
             * This means that if an order indicates dispense 30 tablets plus "3 repeats", then the order can be dispensed a total of 
             * 4 times and the patient can receive a total of 120 tablets. A prescriber may explicitly say that zero refills are 
             * permitted after the initial dispense.
             * 
             * @param numberOfRepeatsAllowed
             *     Number of refills authorized
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder numberOfRepeatsAllowed(UnsignedInt numberOfRepeatsAllowed) {
                this.numberOfRepeatsAllowed = numberOfRepeatsAllowed;
                return this;
            }

            /**
             * The amount that is to be dispensed for one fill.
             * 
             * @param quantity
             *     Amount of medication to supply per dispense
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(SimpleQuantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense 
             * is expected to last.
             * 
             * @param expectedSupplyDuration
             *     Number of days supply per dispense
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder expectedSupplyDuration(Duration expectedSupplyDuration) {
                this.expectedSupplyDuration = expectedSupplyDuration;
                return this;
            }

            /**
             * Indicates the intended dispensing Organization specified by the prescriber.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param performer
             *     Intended dispenser
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder performer(Reference performer) {
                this.performer = performer;
                return this;
            }

            /**
             * Build the {@link DispenseRequest}
             * 
             * @return
             *     An immutable object of type {@link DispenseRequest}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid DispenseRequest per the base specification
             */
            @Override
            public DispenseRequest build() {
                DispenseRequest dispenseRequest = new DispenseRequest(this);
                if (validating) {
                    validate(dispenseRequest);
                }
                return dispenseRequest;
            }

            protected void validate(DispenseRequest dispenseRequest) {
                super.validate(dispenseRequest);
                ValidationSupport.checkReferenceType(dispenseRequest.performer, "performer", "Organization");
                ValidationSupport.requireValueOrChildren(dispenseRequest);
            }

            protected Builder from(DispenseRequest dispenseRequest) {
                super.from(dispenseRequest);
                initialFill = dispenseRequest.initialFill;
                dispenseInterval = dispenseRequest.dispenseInterval;
                validityPeriod = dispenseRequest.validityPeriod;
                numberOfRepeatsAllowed = dispenseRequest.numberOfRepeatsAllowed;
                quantity = dispenseRequest.quantity;
                expectedSupplyDuration = dispenseRequest.expectedSupplyDuration;
                performer = dispenseRequest.performer;
                return this;
            }
        }

        /**
         * Indicates the quantity or duration for the first dispense of the medication.
         */
        public static class InitialFill extends BackboneElement {
            private final SimpleQuantity quantity;
            private final Duration duration;

            private InitialFill(Builder builder) {
                super(builder);
                quantity = builder.quantity;
                duration = builder.duration;
            }

            /**
             * The amount or quantity to provide as part of the first dispense.
             * 
             * @return
             *     An immutable object of type {@link SimpleQuantity} that may be null.
             */
            public SimpleQuantity getQuantity() {
                return quantity;
            }

            /**
             * The length of time that the first dispense is expected to last.
             * 
             * @return
             *     An immutable object of type {@link Duration} that may be null.
             */
            public Duration getDuration() {
                return duration;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (quantity != null) || 
                    (duration != null);
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
                        accept(duration, "duration", visitor);
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
                InitialFill other = (InitialFill) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(quantity, other.quantity) && 
                    Objects.equals(duration, other.duration);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        quantity, 
                        duration);
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
                private Duration duration;

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
                 * The amount or quantity to provide as part of the first dispense.
                 * 
                 * @param quantity
                 *     First fill quantity
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder quantity(SimpleQuantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * The length of time that the first dispense is expected to last.
                 * 
                 * @param duration
                 *     First fill duration
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder duration(Duration duration) {
                    this.duration = duration;
                    return this;
                }

                /**
                 * Build the {@link InitialFill}
                 * 
                 * @return
                 *     An immutable object of type {@link InitialFill}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid InitialFill per the base specification
                 */
                @Override
                public InitialFill build() {
                    InitialFill initialFill = new InitialFill(this);
                    if (validating) {
                        validate(initialFill);
                    }
                    return initialFill;
                }

                protected void validate(InitialFill initialFill) {
                    super.validate(initialFill);
                    ValidationSupport.requireValueOrChildren(initialFill);
                }

                protected Builder from(InitialFill initialFill) {
                    super.from(initialFill);
                    quantity = initialFill.quantity;
                    duration = initialFill.duration;
                    return this;
                }
            }
        }
    }

    /**
     * Indicates whether or not substitution can or should be part of the dispense. In some cases, substitution must happen, 
     * in other cases substitution must not happen. This block explains the prescriber's intent. If nothing is specified 
     * substitution may be done.
     */
    public static class Substitution extends BackboneElement {
        @Choice({ Boolean.class, CodeableConcept.class })
        @Binding(
            bindingName = "MedicationRequestSubstitution",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Identifies the type of substitution allowed.",
            valueSet = "http://terminology.hl7.org/ValueSet/v3-ActSubstanceAdminSubstitutionCode"
        )
        @Required
        private final Element allowed;
        @Binding(
            bindingName = "MedicationIntendedSubstitutionReason",
            strength = BindingStrength.Value.EXAMPLE,
            description = "SubstanceAdminSubstitutionReason",
            valueSet = "http://terminology.hl7.org/ValueSet/v3-SubstanceAdminSubstitutionReason"
        )
        private final CodeableConcept reason;

        private Substitution(Builder builder) {
            super(builder);
            allowed = builder.allowed;
            reason = builder.reason;
        }

        /**
         * True if the prescriber allows a different drug to be dispensed from what was prescribed.
         * 
         * @return
         *     An immutable object of type {@link Boolean} or {@link CodeableConcept} that is non-null.
         */
        public Element getAllowed() {
            return allowed;
        }

        /**
         * Indicates the reason for the substitution, or why substitution must or must not be performed.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getReason() {
            return reason;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (allowed != null) || 
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
                    accept(allowed, "allowed", visitor);
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
            Substitution other = (Substitution) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(allowed, other.allowed) && 
                Objects.equals(reason, other.reason);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    allowed, 
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
            private Element allowed;
            private CodeableConcept reason;

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
             * Convenience method for setting {@code allowed} with choice type Boolean.
             * 
             * <p>This element is required.
             * 
             * @param allowed
             *     Whether substitution is allowed or not
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #allowed(Element)
             */
            public Builder allowed(java.lang.Boolean allowed) {
                this.allowed = (allowed == null) ? null : Boolean.of(allowed);
                return this;
            }

            /**
             * True if the prescriber allows a different drug to be dispensed from what was prescribed.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Boolean}</li>
             * <li>{@link CodeableConcept}</li>
             * </ul>
             * 
             * @param allowed
             *     Whether substitution is allowed or not
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder allowed(Element allowed) {
                this.allowed = allowed;
                return this;
            }

            /**
             * Indicates the reason for the substitution, or why substitution must or must not be performed.
             * 
             * @param reason
             *     Why should (not) substitution be made
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reason(CodeableConcept reason) {
                this.reason = reason;
                return this;
            }

            /**
             * Build the {@link Substitution}
             * 
             * <p>Required elements:
             * <ul>
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
                ValidationSupport.requireChoiceElement(substitution.allowed, "allowed", Boolean.class, CodeableConcept.class);
                ValidationSupport.requireValueOrChildren(substitution);
            }

            protected Builder from(Substitution substitution) {
                super.from(substitution);
                allowed = substitution.allowed;
                reason = substitution.reason;
                return this;
            }
        }
    }
}
