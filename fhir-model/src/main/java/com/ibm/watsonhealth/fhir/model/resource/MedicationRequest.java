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

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Dosage;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.MedicationRequestIntent;
import com.ibm.watsonhealth.fhir.model.type.MedicationRequestPriority;
import com.ibm.watsonhealth.fhir.model.type.MedicationRequestStatus;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * An order or request for both supply of the medication and the instructions for administration of the medication to a 
 * patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to 
 * generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with 
 * workflow patterns.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MedicationRequest extends DomainResource {
    private final List<Identifier> identifier;
    private final MedicationRequestStatus status;
    private final CodeableConcept statusReason;
    private final MedicationRequestIntent intent;
    private final List<CodeableConcept> category;
    private final MedicationRequestPriority priority;
    private final Boolean doNotPerform;
    private final Element reported;
    private final Element medication;
    private final Reference subject;
    private final Reference encounter;
    private final List<Reference> supportingInformation;
    private final DateTime authoredOn;
    private final Reference requester;
    private final Reference performer;
    private final CodeableConcept performerType;
    private final Reference recorder;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<Canonical> instantiatesCanonical;
    private final List<Uri> instantiatesUri;
    private final List<Reference> basedOn;
    private final Identifier groupIdentifier;
    private final CodeableConcept courseOfTherapyType;
    private final List<Reference> insurance;
    private final List<Annotation> note;
    private final List<Dosage> dosageInstruction;
    private final DispenseRequest dispenseRequest;
    private final Substitution substitution;
    private final Reference priorPrescription;
    private final List<Reference> detectedIssue;
    private final List<Reference> eventHistory;

    private volatile int hashCode;

    private MedicationRequest(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        statusReason = builder.statusReason;
        intent = ValidationSupport.requireNonNull(builder.intent, "intent");
        category = Collections.unmodifiableList(builder.category);
        priority = builder.priority;
        doNotPerform = builder.doNotPerform;
        reported = ValidationSupport.choiceElement(builder.reported, "reported", Boolean.class, Reference.class);
        medication = ValidationSupport.requireChoiceElement(builder.medication, "medication", CodeableConcept.class, Reference.class);
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
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
     * <p>
     * Identifiers associated with this medication request that are defined by business processes and/or used to refer to it 
     * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
     * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
     * to server.
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
     * A code specifying the current state of the order. Generally, this will be active or completed state.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MedicationRequestStatus}.
     */
    public MedicationRequestStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Captures the reason for the current state of the MedicationRequest.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getStatusReason() {
        return statusReason;
    }

    /**
     * <p>
     * Whether the request is a proposal, plan, or an original order.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MedicationRequestIntent}.
     */
    public MedicationRequestIntent getIntent() {
        return intent;
    }

    /**
     * <p>
     * Indicates the type of medication request (for example, where the medication is expected to be consumed or administered 
     * (i.e. inpatient or outpatient)).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * Indicates how quickly the Medication Request should be addressed with respect to other requests.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MedicationRequestPriority}.
     */
    public MedicationRequestPriority getPriority() {
        return priority;
    }

    /**
     * <p>
     * If true indicates that the provider is asking for the medication request not to occur.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getDoNotPerform() {
        return doNotPerform;
    }

    /**
     * <p>
     * Indicates if this record was captured as a secondary 'reported' record rather than as an original primary source-of-
     * truth record. It may also indicate the source of the report.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getReported() {
        return reported;
    }

    /**
     * <p>
     * Identifies the medication being requested. This is a link to a resource that represents the medication which may be 
     * the details of the medication or simply an attribute carrying a code that identifies the medication from a known list 
     * of medications.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getMedication() {
        return medication;
    }

    /**
     * <p>
     * A link to a resource representing the person or set of individuals to whom the medication will be given.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * <p>
     * The Encounter during which this [x] was created or to which the creation of this record is tightly associated.
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
     * Include additional information (for example, patient height and weight) that supports the ordering of the medication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSupportingInformation() {
        return supportingInformation;
    }

    /**
     * <p>
     * The date (and perhaps time) when the prescription was initially written or authored on.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getAuthoredOn() {
        return authoredOn;
    }

    /**
     * <p>
     * The individual, organization, or device that initiated the request and has responsibility for its activation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRequester() {
        return requester;
    }

    /**
     * <p>
     * The specified desired performer of the medication treatment (e.g. the performer of the medication administration).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPerformer() {
        return performer;
    }

    /**
     * <p>
     * Indicates the type of performer of the administration of the medication.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getPerformerType() {
        return performerType;
    }

    /**
     * <p>
     * The person who entered the order on behalf of another individual for example in the case of a verbal or a telephone 
     * order.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRecorder() {
        return recorder;
    }

    /**
     * <p>
     * The reason or the indication for ordering or not ordering the medication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * <p>
     * Condition or observation that supports why the medication was ordered.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * <p>
     * The URL pointing to a protocol, guideline, orderset, or other definition that is adhered to in whole or in part by 
     * this MedicationRequest.
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
     * whole or in part by this MedicationRequest.
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
     * A plan or request that is fulfilled in whole or in part by this medication request.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * <p>
     * A shared identifier common to all requests that were authorized more or less simultaneously by a single author, 
     * representing the identifier of the requisition or prescription.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getGroupIdentifier() {
        return groupIdentifier;
    }

    /**
     * <p>
     * The description of the overall patte3rn of the administration of the medication to the patient.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getCourseOfTherapyType() {
        return courseOfTherapyType;
    }

    /**
     * <p>
     * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be required for delivering 
     * the requested service.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getInsurance() {
        return insurance;
    }

    /**
     * <p>
     * Extra information about the prescription that could not be conveyed by the other attributes.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * <p>
     * Indicates how the medication is to be used by the patient.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Dosage}.
     */
    public List<Dosage> getDosageInstruction() {
        return dosageInstruction;
    }

    /**
     * <p>
     * Indicates the specific details for the dispense or medication supply part of a medication request (also known as a 
     * Medication Prescription or Medication Order). Note that this information is not always sent with the order. There may 
     * be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the 
     * pharmacy department.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DispenseRequest}.
     */
    public DispenseRequest getDispenseRequest() {
        return dispenseRequest;
    }

    /**
     * <p>
     * Indicates whether or not substitution can or should be part of the dispense. In some cases, substitution must happen, 
     * in other cases substitution must not happen. This block explains the prescriber's intent. If nothing is specified 
     * substitution may be done.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Substitution}.
     */
    public Substitution getSubstitution() {
        return substitution;
    }

    /**
     * <p>
     * A link to a resource representing an earlier order related order or prescription.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPriorPrescription() {
        return priorPrescription;
    }

    /**
     * <p>
     * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
     * patient; e.g. Drug-drug interaction, duplicate therapy, dosage alert etc.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getDetectedIssue() {
        return detectedIssue;
    }

    /**
     * <p>
     * Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify 
     * key state transitions or updates that are likely to be relevant to a user looking at the current version of the 
     * resource.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getEventHistory() {
        return eventHistory;
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
        return new Builder(status, intent, medication, subject).from(this);
    }

    public Builder toBuilder(MedicationRequestStatus status, MedicationRequestIntent intent, Element medication, Reference subject) {
        return new Builder(status, intent, medication, subject).from(this);
    }

    public static Builder builder(MedicationRequestStatus status, MedicationRequestIntent intent, Element medication, Reference subject) {
        return new Builder(status, intent, medication, subject);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final MedicationRequestStatus status;
        private final MedicationRequestIntent intent;
        private final Element medication;
        private final Reference subject;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept statusReason;
        private List<CodeableConcept> category = new ArrayList<>();
        private MedicationRequestPriority priority;
        private Boolean doNotPerform;
        private Element reported;
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

        private Builder(MedicationRequestStatus status, MedicationRequestIntent intent, Element medication, Reference subject) {
            super();
            this.status = status;
            this.intent = intent;
            this.medication = medication;
            this.subject = subject;
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
         * Identifiers associated with this medication request that are defined by business processes and/or used to refer to it 
         * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
         * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
         * to server.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Identifiers associated with this medication request that are defined by business processes and/or used to refer to it 
         * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
         * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
         * to server.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     External ids for this request
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
         * Captures the reason for the current state of the MedicationRequest.
         * </p>
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
         * <p>
         * Indicates the type of medication request (for example, where the medication is expected to be consumed or administered 
         * (i.e. inpatient or outpatient)).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Indicates the type of medication request (for example, where the medication is expected to be consumed or administered 
         * (i.e. inpatient or outpatient)).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param category
         *     Type of medication usage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * <p>
         * Indicates how quickly the Medication Request should be addressed with respect to other requests.
         * </p>
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
         * <p>
         * If true indicates that the provider is asking for the medication request not to occur.
         * </p>
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
         * <p>
         * Indicates if this record was captured as a secondary 'reported' record rather than as an original primary source-of-
         * truth record. It may also indicate the source of the report.
         * </p>
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
         * <p>
         * The Encounter during which this [x] was created or to which the creation of this record is tightly associated.
         * </p>
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
         * <p>
         * Include additional information (for example, patient height and weight) that supports the ordering of the medication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Include additional information (for example, patient height and weight) that supports the ordering of the medication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param supportingInformation
         *     Information to support ordering of the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInformation(Collection<Reference> supportingInformation) {
            this.supportingInformation = new ArrayList<>(supportingInformation);
            return this;
        }

        /**
         * <p>
         * The date (and perhaps time) when the prescription was initially written or authored on.
         * </p>
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
         * <p>
         * The individual, organization, or device that initiated the request and has responsibility for its activation.
         * </p>
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
         * <p>
         * The specified desired performer of the medication treatment (e.g. the performer of the medication administration).
         * </p>
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
         * <p>
         * Indicates the type of performer of the administration of the medication.
         * </p>
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
         * <p>
         * The person who entered the order on behalf of another individual for example in the case of a verbal or a telephone 
         * order.
         * </p>
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
         * <p>
         * The reason or the indication for ordering or not ordering the medication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The reason or the indication for ordering or not ordering the medication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonCode
         *     Reason or indication for ordering or not ordering the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * <p>
         * Condition or observation that supports why the medication was ordered.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Condition or observation that supports why the medication was ordered.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonReference
         *     Condition or observation that supports why the prescription is being written
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * <p>
         * The URL pointing to a protocol, guideline, orderset, or other definition that is adhered to in whole or in part by 
         * this MedicationRequest.
         * </p>
         * <p>
         * Adds new element(s) to existing list
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
         * The URL pointing to a protocol, guideline, orderset, or other definition that is adhered to in whole or in part by 
         * this MedicationRequest.
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
         * whole or in part by this MedicationRequest.
         * </p>
         * <p>
         * Adds new element(s) to existing list
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
         * whole or in part by this MedicationRequest.
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
         * A plan or request that is fulfilled in whole or in part by this medication request.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A plan or request that is fulfilled in whole or in part by this medication request.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param basedOn
         *     What request fulfills
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * <p>
         * A shared identifier common to all requests that were authorized more or less simultaneously by a single author, 
         * representing the identifier of the requisition or prescription.
         * </p>
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
         * <p>
         * The description of the overall patte3rn of the administration of the medication to the patient.
         * </p>
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
         * <p>
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be required for delivering 
         * the requested service.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be required for delivering 
         * the requested service.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param insurance
         *     Associated insurance coverage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Collection<Reference> insurance) {
            this.insurance = new ArrayList<>(insurance);
            return this;
        }

        /**
         * <p>
         * Extra information about the prescription that could not be conveyed by the other attributes.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Extra information about the prescription that could not be conveyed by the other attributes.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Information about the prescription
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * <p>
         * Indicates how the medication is to be used by the patient.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Indicates how the medication is to be used by the patient.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param dosageInstruction
         *     How the medication should be taken
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dosageInstruction(Collection<Dosage> dosageInstruction) {
            this.dosageInstruction = new ArrayList<>(dosageInstruction);
            return this;
        }

        /**
         * <p>
         * Indicates the specific details for the dispense or medication supply part of a medication request (also known as a 
         * Medication Prescription or Medication Order). Note that this information is not always sent with the order. There may 
         * be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the 
         * pharmacy department.
         * </p>
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
         * <p>
         * Indicates whether or not substitution can or should be part of the dispense. In some cases, substitution must happen, 
         * in other cases substitution must not happen. This block explains the prescriber's intent. If nothing is specified 
         * substitution may be done.
         * </p>
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
         * <p>
         * A link to a resource representing an earlier order related order or prescription.
         * </p>
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
         * <p>
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
         * patient; e.g. Drug-drug interaction, duplicate therapy, dosage alert etc.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
         * patient; e.g. Drug-drug interaction, duplicate therapy, dosage alert etc.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param detectedIssue
         *     Clinical Issue with action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder detectedIssue(Collection<Reference> detectedIssue) {
            this.detectedIssue = new ArrayList<>(detectedIssue);
            return this;
        }

        /**
         * <p>
         * Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify 
         * key state transitions or updates that are likely to be relevant to a user looking at the current version of the 
         * resource.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify 
         * key state transitions or updates that are likely to be relevant to a user looking at the current version of the 
         * resource.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param eventHistory
         *     A list of events of interest in the lifecycle
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder eventHistory(Collection<Reference> eventHistory) {
            this.eventHistory = new ArrayList<>(eventHistory);
            return this;
        }

        @Override
        public MedicationRequest build() {
            return new MedicationRequest(this);
        }

        private Builder from(MedicationRequest medicationRequest) {
            id = medicationRequest.id;
            meta = medicationRequest.meta;
            implicitRules = medicationRequest.implicitRules;
            language = medicationRequest.language;
            text = medicationRequest.text;
            contained.addAll(medicationRequest.contained);
            extension.addAll(medicationRequest.extension);
            modifierExtension.addAll(medicationRequest.modifierExtension);
            identifier.addAll(medicationRequest.identifier);
            statusReason = medicationRequest.statusReason;
            category.addAll(medicationRequest.category);
            priority = medicationRequest.priority;
            doNotPerform = medicationRequest.doNotPerform;
            reported = medicationRequest.reported;
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
     * <p>
     * Indicates the specific details for the dispense or medication supply part of a medication request (also known as a 
     * Medication Prescription or Medication Order). Note that this information is not always sent with the order. There may 
     * be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the 
     * pharmacy department.
     * </p>
     */
    public static class DispenseRequest extends BackboneElement {
        private final InitialFill initialFill;
        private final Duration dispenseInterval;
        private final Period validityPeriod;
        private final UnsignedInt numberOfRepeatsAllowed;
        private final Quantity quantity;
        private final Duration expectedSupplyDuration;
        private final Reference performer;

        private volatile int hashCode;

        private DispenseRequest(Builder builder) {
            super(builder);
            initialFill = builder.initialFill;
            dispenseInterval = builder.dispenseInterval;
            validityPeriod = builder.validityPeriod;
            numberOfRepeatsAllowed = builder.numberOfRepeatsAllowed;
            quantity = builder.quantity;
            expectedSupplyDuration = builder.expectedSupplyDuration;
            performer = builder.performer;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Indicates the quantity or duration for the first dispense of the medication.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link InitialFill}.
         */
        public InitialFill getInitialFill() {
            return initialFill;
        }

        /**
         * <p>
         * The minimum period of time that must occur between dispenses of the medication.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Duration}.
         */
        public Duration getDispenseInterval() {
            return dispenseInterval;
        }

        /**
         * <p>
         * This indicates the validity period of a prescription (stale dating the Prescription).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getValidityPeriod() {
            return validityPeriod;
        }

        /**
         * <p>
         * An integer indicating the number of times, in addition to the original dispense, (aka refills or repeats) that the 
         * patient can receive the prescribed medication. Usage Notes: This integer does not include the original order dispense. 
         * This means that if an order indicates dispense 30 tablets plus "3 repeats", then the order can be dispensed a total of 
         * 4 times and the patient can receive a total of 120 tablets. A prescriber may explicitly say that zero refills are 
         * permitted after the initial dispense.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt}.
         */
        public UnsignedInt getNumberOfRepeatsAllowed() {
            return numberOfRepeatsAllowed;
        }

        /**
         * <p>
         * The amount that is to be dispensed for one fill.
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
         * Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense 
         * is expected to last.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Duration}.
         */
        public Duration getExpectedSupplyDuration() {
            return expectedSupplyDuration;
        }

        /**
         * <p>
         * Indicates the intended dispensing Organization specified by the prescriber.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
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
            // optional
            private InitialFill initialFill;
            private Duration dispenseInterval;
            private Period validityPeriod;
            private UnsignedInt numberOfRepeatsAllowed;
            private Quantity quantity;
            private Duration expectedSupplyDuration;
            private Reference performer;

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
             * Indicates the quantity or duration for the first dispense of the medication.
             * </p>
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
             * <p>
             * The minimum period of time that must occur between dispenses of the medication.
             * </p>
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
             * <p>
             * This indicates the validity period of a prescription (stale dating the Prescription).
             * </p>
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
             * <p>
             * An integer indicating the number of times, in addition to the original dispense, (aka refills or repeats) that the 
             * patient can receive the prescribed medication. Usage Notes: This integer does not include the original order dispense. 
             * This means that if an order indicates dispense 30 tablets plus "3 repeats", then the order can be dispensed a total of 
             * 4 times and the patient can receive a total of 120 tablets. A prescriber may explicitly say that zero refills are 
             * permitted after the initial dispense.
             * </p>
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
             * <p>
             * The amount that is to be dispensed for one fill.
             * </p>
             * 
             * @param quantity
             *     Amount of medication to supply per dispense
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
             * Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense 
             * is expected to last.
             * </p>
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
             * <p>
             * Indicates the intended dispensing Organization specified by the prescriber.
             * </p>
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

            @Override
            public DispenseRequest build() {
                return new DispenseRequest(this);
            }

            private Builder from(DispenseRequest dispenseRequest) {
                id = dispenseRequest.id;
                extension.addAll(dispenseRequest.extension);
                modifierExtension.addAll(dispenseRequest.modifierExtension);
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
         * <p>
         * Indicates the quantity or duration for the first dispense of the medication.
         * </p>
         */
        public static class InitialFill extends BackboneElement {
            private final Quantity quantity;
            private final Duration duration;

            private volatile int hashCode;

            private InitialFill(Builder builder) {
                super(builder);
                quantity = builder.quantity;
                duration = builder.duration;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * The amount or quantity to provide as part of the first dispense.
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
             * The length of time that the first dispense is expected to last.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Duration}.
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
            public void accept(java.lang.String elementName, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, this);
                    if (visitor.visit(elementName, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(quantity, "quantity", visitor);
                        accept(duration, "duration", visitor);
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
                // optional
                private Quantity quantity;
                private Duration duration;

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
                 * The amount or quantity to provide as part of the first dispense.
                 * </p>
                 * 
                 * @param quantity
                 *     First fill quantity
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
                 * The length of time that the first dispense is expected to last.
                 * </p>
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

                @Override
                public InitialFill build() {
                    return new InitialFill(this);
                }

                private Builder from(InitialFill initialFill) {
                    id = initialFill.id;
                    extension.addAll(initialFill.extension);
                    modifierExtension.addAll(initialFill.modifierExtension);
                    quantity = initialFill.quantity;
                    duration = initialFill.duration;
                    return this;
                }
            }
        }
    }

    /**
     * <p>
     * Indicates whether or not substitution can or should be part of the dispense. In some cases, substitution must happen, 
     * in other cases substitution must not happen. This block explains the prescriber's intent. If nothing is specified 
     * substitution may be done.
     * </p>
     */
    public static class Substitution extends BackboneElement {
        private final Element allowed;
        private final CodeableConcept reason;

        private volatile int hashCode;

        private Substitution(Builder builder) {
            super(builder);
            allowed = ValidationSupport.requireChoiceElement(builder.allowed, "allowed", Boolean.class, CodeableConcept.class);
            reason = builder.reason;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * True if the prescriber allows a different drug to be dispensed from what was prescribed.
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
         * Indicates the reason for the substitution, or why substitution must or must not be performed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(allowed, "allowed", visitor);
                    accept(reason, "reason", visitor);
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
            return new Builder(allowed).from(this);
        }

        public Builder toBuilder(Element allowed) {
            return new Builder(allowed).from(this);
        }

        public static Builder builder(Element allowed) {
            return new Builder(allowed);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Element allowed;

            // optional
            private CodeableConcept reason;

            private Builder(Element allowed) {
                super();
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
             * Indicates the reason for the substitution, or why substitution must or must not be performed.
             * </p>
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

            @Override
            public Substitution build() {
                return new Substitution(this);
            }

            private Builder from(Substitution substitution) {
                id = substitution.id;
                extension.addAll(substitution.extension);
                modifierExtension.addAll(substitution.modifierExtension);
                reason = substitution.reason;
                return this;
            }
        }
    }
}
