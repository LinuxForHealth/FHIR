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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AppointmentStatus;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ParticipantRequired;
import com.ibm.fhir.model.type.code.ParticipationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific 
 * date/time. This may result in one or more Encounter(s).
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "app-1",
    level = "Rule",
    location = "Appointment.participant",
    description = "Either the type or actor on the participant SHALL be specified",
    expression = "type.exists() or actor.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Appointment"
)
@Constraint(
    id = "app-2",
    level = "Rule",
    location = "(base)",
    description = "Either start and end are specified, or neither",
    expression = "start.exists() = end.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Appointment"
)
@Constraint(
    id = "app-3",
    level = "Rule",
    location = "(base)",
    description = "Only proposed or cancelled appointments can be missing start/end dates",
    expression = "(start.exists() and end.exists()) or (status in ('proposed' | 'cancelled' | 'waitlist'))",
    source = "http://hl7.org/fhir/StructureDefinition/Appointment"
)
@Constraint(
    id = "app-4",
    level = "Rule",
    location = "(base)",
    description = "Cancelation reason is only used for appointments that have been cancelled, or no-show",
    expression = "Appointment.cancelationReason.exists() implies (Appointment.status='no-show' or Appointment.status='cancelled')",
    source = "http://hl7.org/fhir/StructureDefinition/Appointment"
)
@Constraint(
    id = "appointment-5",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/c80-practice-codes",
    expression = "specialty.exists() implies (specialty.all(memberOf('http://hl7.org/fhir/ValueSet/c80-practice-codes', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/Appointment",
    generated = true
)
@Constraint(
    id = "appointment-6",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://terminology.hl7.org/ValueSet/v2-0276",
    expression = "appointmentType.exists() implies (appointmentType.memberOf('http://terminology.hl7.org/ValueSet/v2-0276', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/Appointment",
    generated = true
)
@Constraint(
    id = "appointment-7",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/encounter-reason",
    expression = "reasonCode.exists() implies (reasonCode.all(memberOf('http://hl7.org/fhir/ValueSet/encounter-reason', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/Appointment",
    generated = true
)
@Constraint(
    id = "appointment-8",
    level = "Warning",
    location = "participant.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/encounter-participant-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/encounter-participant-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Appointment",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Appointment extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "AppointmentStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The free/busy status of an appointment.",
        valueSet = "http://hl7.org/fhir/ValueSet/appointmentstatus|4.3.0"
    )
    @Required
    private final AppointmentStatus status;
    @Summary
    @Binding(
        bindingName = "cancelation-reason",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/appointment-cancellation-reason"
    )
    private final CodeableConcept cancelationReason;
    @Summary
    @Binding(
        bindingName = "service-category",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/service-category"
    )
    private final List<CodeableConcept> serviceCategory;
    @Summary
    @Binding(
        bindingName = "service-type",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/service-type"
    )
    private final List<CodeableConcept> serviceType;
    @Summary
    @Binding(
        bindingName = "specialty",
        strength = BindingStrength.Value.PREFERRED,
        valueSet = "http://hl7.org/fhir/ValueSet/c80-practice-codes"
    )
    private final List<CodeableConcept> specialty;
    @Summary
    @Binding(
        bindingName = "appointment-type",
        strength = BindingStrength.Value.PREFERRED,
        valueSet = "http://terminology.hl7.org/ValueSet/v2-0276"
    )
    private final CodeableConcept appointmentType;
    @Summary
    @Binding(
        bindingName = "ApptReason",
        strength = BindingStrength.Value.PREFERRED,
        description = "The Reason for the appointment to take place.",
        valueSet = "http://hl7.org/fhir/ValueSet/encounter-reason"
    )
    private final List<CodeableConcept> reasonCode;
    @ReferenceTarget({ "Condition", "Procedure", "Observation", "ImmunizationRecommendation" })
    private final List<Reference> reasonReference;
    private final UnsignedInt priority;
    private final String description;
    private final List<Reference> supportingInformation;
    @Summary
    private final Instant start;
    @Summary
    private final Instant end;
    private final PositiveInt minutesDuration;
    @ReferenceTarget({ "Slot" })
    private final List<Reference> slot;
    private final DateTime created;
    private final String comment;
    private final String patientInstruction;
    @ReferenceTarget({ "ServiceRequest" })
    private final List<Reference> basedOn;
    @Required
    private final List<Participant> participant;
    private final List<Period> requestedPeriod;

    private Appointment(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        cancelationReason = builder.cancelationReason;
        serviceCategory = Collections.unmodifiableList(builder.serviceCategory);
        serviceType = Collections.unmodifiableList(builder.serviceType);
        specialty = Collections.unmodifiableList(builder.specialty);
        appointmentType = builder.appointmentType;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        priority = builder.priority;
        description = builder.description;
        supportingInformation = Collections.unmodifiableList(builder.supportingInformation);
        start = builder.start;
        end = builder.end;
        minutesDuration = builder.minutesDuration;
        slot = Collections.unmodifiableList(builder.slot);
        created = builder.created;
        comment = builder.comment;
        patientInstruction = builder.patientInstruction;
        basedOn = Collections.unmodifiableList(builder.basedOn);
        participant = Collections.unmodifiableList(builder.participant);
        requestedPeriod = Collections.unmodifiableList(builder.requestedPeriod);
    }

    /**
     * This records identifiers associated with this appointment concern that are defined by business processes and/or used 
     * to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in 
     * written / printed documentation).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The overall status of the Appointment. Each of the participants has their own participation status which indicates 
     * their involvement in the process, however this status indicates the shared status.
     * 
     * @return
     *     An immutable object of type {@link AppointmentStatus} that is non-null.
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * The coded reason for the appointment being cancelled. This is often used in reporting/billing/futher processing to 
     * determine if further actions are required, or specific fees apply.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCancelationReason() {
        return cancelationReason;
    }

    /**
     * A broad categorization of the service that is to be performed during this appointment.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getServiceCategory() {
        return serviceCategory;
    }

    /**
     * The specific service that is to be performed during this appointment.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getServiceType() {
        return serviceType;
    }

    /**
     * The specialty of a practitioner that would be required to perform the service requested in this appointment.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getSpecialty() {
        return specialty;
    }

    /**
     * The style of appointment or patient that has been booked in the slot (not service type).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getAppointmentType() {
        return appointmentType;
    }

    /**
     * The coded reason that this appointment is being scheduled. This is more clinical than administrative.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Reason the appointment has been scheduled to take place, as specified using information from another resource. When 
     * the patient arrives and the encounter begins it may be used as the admission diagnosis. The indication will typically 
     * be a Condition (with other resources referenced in the evidence.detail), or a Procedure.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The 
     * iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority).
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt} that may be null.
     */
    public UnsignedInt getPriority() {
        return priority;
    }

    /**
     * The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment 
     * list. Detailed or expanded information should be put in the comment field.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Additional information to support the appointment provided when making the appointment.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSupportingInformation() {
        return supportingInformation;
    }

    /**
     * Date/Time that the appointment is to take place.
     * 
     * @return
     *     An immutable object of type {@link Instant} that may be null.
     */
    public Instant getStart() {
        return start;
    }

    /**
     * Date/Time that the appointment is to conclude.
     * 
     * @return
     *     An immutable object of type {@link Instant} that may be null.
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * Number of minutes that the appointment is to take. This can be less than the duration between the start and end times. 
     * For example, where the actual time of appointment is only an estimate or if a 30 minute appointment is being 
     * requested, but any time would work. Also, if there is, for example, a planned 15 minute break in the middle of a long 
     * appointment, the duration may be 15 minutes less than the difference between the start and end.
     * 
     * @return
     *     An immutable object of type {@link PositiveInt} that may be null.
     */
    public PositiveInt getMinutesDuration() {
        return minutesDuration;
    }

    /**
     * The slots from the participants' schedules that will be filled by the appointment.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSlot() {
        return slot;
    }

    /**
     * The date that this appointment was initially created. This could be different to the meta.lastModified value on the 
     * initial entry, as this could have been before the resource was created on the FHIR server, and should remain unchanged 
     * over the lifespan of the appointment.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * Additional comments about the appointment.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getComment() {
        return comment;
    }

    /**
     * While Appointment.comment contains information for internal use, Appointment.patientInstructions is used to capture 
     * patient facing information about the Appointment (e.g. please bring your referral or fast from 8pm night before).
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getPatientInstruction() {
        return patientInstruction;
    }

    /**
     * The service request this appointment is allocated to assess (e.g. incoming referral or procedure request).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * List of participants involved in the appointment.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Participant} that is non-empty.
     */
    public List<Participant> getParticipant() {
        return participant;
    }

    /**
     * A set of date ranges (potentially including times) that the appointment is preferred to be scheduled within.
     * 
     * <p>The duration (usually in minutes) could also be provided to indicate the length of the appointment to fill and 
     * populate the start/end times for the actual allocated time. However, in other situations the duration may be 
     * calculated by the scheduling system.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Period} that may be empty.
     */
    public List<Period> getRequestedPeriod() {
        return requestedPeriod;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (cancelationReason != null) || 
            !serviceCategory.isEmpty() || 
            !serviceType.isEmpty() || 
            !specialty.isEmpty() || 
            (appointmentType != null) || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            (priority != null) || 
            (description != null) || 
            !supportingInformation.isEmpty() || 
            (start != null) || 
            (end != null) || 
            (minutesDuration != null) || 
            !slot.isEmpty() || 
            (created != null) || 
            (comment != null) || 
            (patientInstruction != null) || 
            !basedOn.isEmpty() || 
            !participant.isEmpty() || 
            !requestedPeriod.isEmpty();
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
                accept(cancelationReason, "cancelationReason", visitor);
                accept(serviceCategory, "serviceCategory", visitor, CodeableConcept.class);
                accept(serviceType, "serviceType", visitor, CodeableConcept.class);
                accept(specialty, "specialty", visitor, CodeableConcept.class);
                accept(appointmentType, "appointmentType", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(priority, "priority", visitor);
                accept(description, "description", visitor);
                accept(supportingInformation, "supportingInformation", visitor, Reference.class);
                accept(start, "start", visitor);
                accept(end, "end", visitor);
                accept(minutesDuration, "minutesDuration", visitor);
                accept(slot, "slot", visitor, Reference.class);
                accept(created, "created", visitor);
                accept(comment, "comment", visitor);
                accept(patientInstruction, "patientInstruction", visitor);
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(participant, "participant", visitor, Participant.class);
                accept(requestedPeriod, "requestedPeriod", visitor, Period.class);
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
        Appointment other = (Appointment) obj;
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
            Objects.equals(cancelationReason, other.cancelationReason) && 
            Objects.equals(serviceCategory, other.serviceCategory) && 
            Objects.equals(serviceType, other.serviceType) && 
            Objects.equals(specialty, other.specialty) && 
            Objects.equals(appointmentType, other.appointmentType) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(description, other.description) && 
            Objects.equals(supportingInformation, other.supportingInformation) && 
            Objects.equals(start, other.start) && 
            Objects.equals(end, other.end) && 
            Objects.equals(minutesDuration, other.minutesDuration) && 
            Objects.equals(slot, other.slot) && 
            Objects.equals(created, other.created) && 
            Objects.equals(comment, other.comment) && 
            Objects.equals(patientInstruction, other.patientInstruction) && 
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(participant, other.participant) && 
            Objects.equals(requestedPeriod, other.requestedPeriod);
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
                cancelationReason, 
                serviceCategory, 
                serviceType, 
                specialty, 
                appointmentType, 
                reasonCode, 
                reasonReference, 
                priority, 
                description, 
                supportingInformation, 
                start, 
                end, 
                minutesDuration, 
                slot, 
                created, 
                comment, 
                patientInstruction, 
                basedOn, 
                participant, 
                requestedPeriod);
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
        private AppointmentStatus status;
        private CodeableConcept cancelationReason;
        private List<CodeableConcept> serviceCategory = new ArrayList<>();
        private List<CodeableConcept> serviceType = new ArrayList<>();
        private List<CodeableConcept> specialty = new ArrayList<>();
        private CodeableConcept appointmentType;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private UnsignedInt priority;
        private String description;
        private List<Reference> supportingInformation = new ArrayList<>();
        private Instant start;
        private Instant end;
        private PositiveInt minutesDuration;
        private List<Reference> slot = new ArrayList<>();
        private DateTime created;
        private String comment;
        private String patientInstruction;
        private List<Reference> basedOn = new ArrayList<>();
        private List<Participant> participant = new ArrayList<>();
        private List<Period> requestedPeriod = new ArrayList<>();

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
         * This records identifiers associated with this appointment concern that are defined by business processes and/or used 
         * to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in 
         * written / printed documentation).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External Ids for this item
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
         * This records identifiers associated with this appointment concern that are defined by business processes and/or used 
         * to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in 
         * written / printed documentation).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External Ids for this item
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
         * The overall status of the Appointment. Each of the participants has their own participation status which indicates 
         * their involvement in the process, however this status indicates the shared status.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     proposed | pending | booked | arrived | fulfilled | cancelled | noshow | entered-in-error | checked-in | waitlist
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(AppointmentStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The coded reason for the appointment being cancelled. This is often used in reporting/billing/futher processing to 
         * determine if further actions are required, or specific fees apply.
         * 
         * @param cancelationReason
         *     The coded reason for the appointment being cancelled
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder cancelationReason(CodeableConcept cancelationReason) {
            this.cancelationReason = cancelationReason;
            return this;
        }

        /**
         * A broad categorization of the service that is to be performed during this appointment.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param serviceCategory
         *     A broad categorization of the service that is to be performed during this appointment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviceCategory(CodeableConcept... serviceCategory) {
            for (CodeableConcept value : serviceCategory) {
                this.serviceCategory.add(value);
            }
            return this;
        }

        /**
         * A broad categorization of the service that is to be performed during this appointment.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param serviceCategory
         *     A broad categorization of the service that is to be performed during this appointment
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder serviceCategory(Collection<CodeableConcept> serviceCategory) {
            this.serviceCategory = new ArrayList<>(serviceCategory);
            return this;
        }

        /**
         * The specific service that is to be performed during this appointment.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param serviceType
         *     The specific service that is to be performed during this appointment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviceType(CodeableConcept... serviceType) {
            for (CodeableConcept value : serviceType) {
                this.serviceType.add(value);
            }
            return this;
        }

        /**
         * The specific service that is to be performed during this appointment.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param serviceType
         *     The specific service that is to be performed during this appointment
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder serviceType(Collection<CodeableConcept> serviceType) {
            this.serviceType = new ArrayList<>(serviceType);
            return this;
        }

        /**
         * The specialty of a practitioner that would be required to perform the service requested in this appointment.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialty
         *     The specialty of a practitioner that would be required to perform the service requested in this appointment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specialty(CodeableConcept... specialty) {
            for (CodeableConcept value : specialty) {
                this.specialty.add(value);
            }
            return this;
        }

        /**
         * The specialty of a practitioner that would be required to perform the service requested in this appointment.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialty
         *     The specialty of a practitioner that would be required to perform the service requested in this appointment
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder specialty(Collection<CodeableConcept> specialty) {
            this.specialty = new ArrayList<>(specialty);
            return this;
        }

        /**
         * The style of appointment or patient that has been booked in the slot (not service type).
         * 
         * @param appointmentType
         *     The style of appointment or patient that has been booked in the slot (not service type)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder appointmentType(CodeableConcept appointmentType) {
            this.appointmentType = appointmentType;
            return this;
        }

        /**
         * The coded reason that this appointment is being scheduled. This is more clinical than administrative.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Coded reason this appointment is scheduled
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
         * The coded reason that this appointment is being scheduled. This is more clinical than administrative.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Coded reason this appointment is scheduled
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
         * Reason the appointment has been scheduled to take place, as specified using information from another resource. When 
         * the patient arrives and the encounter begins it may be used as the admission diagnosis. The indication will typically 
         * be a Condition (with other resources referenced in the evidence.detail), or a Procedure.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Observation}</li>
         * <li>{@link ImmunizationRecommendation}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Reason the appointment is to take place (resource)
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
         * Reason the appointment has been scheduled to take place, as specified using information from another resource. When 
         * the patient arrives and the encounter begins it may be used as the admission diagnosis. The indication will typically 
         * be a Condition (with other resources referenced in the evidence.detail), or a Procedure.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Observation}</li>
         * <li>{@link ImmunizationRecommendation}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Reason the appointment is to take place (resource)
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
         * The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The 
         * iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority).
         * 
         * @param priority
         *     Used to make informed decisions if needing to re-prioritize
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priority(UnsignedInt priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Convenience method for setting {@code description}.
         * 
         * @param description
         *     Shown on a subject line in a meeting request, or appointment list
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
         * The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment 
         * list. Detailed or expanded information should be put in the comment field.
         * 
         * @param description
         *     Shown on a subject line in a meeting request, or appointment list
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Additional information to support the appointment provided when making the appointment.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInformation
         *     Additional information to support the appointment
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
         * Additional information to support the appointment provided when making the appointment.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInformation
         *     Additional information to support the appointment
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
         * Convenience method for setting {@code start}.
         * 
         * @param start
         *     When appointment is to take place
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #start(com.ibm.fhir.model.type.Instant)
         */
        public Builder start(java.time.ZonedDateTime start) {
            this.start = (start == null) ? null : Instant.of(start);
            return this;
        }

        /**
         * Date/Time that the appointment is to take place.
         * 
         * @param start
         *     When appointment is to take place
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder start(Instant start) {
            this.start = start;
            return this;
        }

        /**
         * Convenience method for setting {@code end}.
         * 
         * @param end
         *     When appointment is to conclude
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #end(com.ibm.fhir.model.type.Instant)
         */
        public Builder end(java.time.ZonedDateTime end) {
            this.end = (end == null) ? null : Instant.of(end);
            return this;
        }

        /**
         * Date/Time that the appointment is to conclude.
         * 
         * @param end
         *     When appointment is to conclude
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder end(Instant end) {
            this.end = end;
            return this;
        }

        /**
         * Number of minutes that the appointment is to take. This can be less than the duration between the start and end times. 
         * For example, where the actual time of appointment is only an estimate or if a 30 minute appointment is being 
         * requested, but any time would work. Also, if there is, for example, a planned 15 minute break in the middle of a long 
         * appointment, the duration may be 15 minutes less than the difference between the start and end.
         * 
         * @param minutesDuration
         *     Can be less than start/end (e.g. estimate)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder minutesDuration(PositiveInt minutesDuration) {
            this.minutesDuration = minutesDuration;
            return this;
        }

        /**
         * The slots from the participants' schedules that will be filled by the appointment.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Slot}</li>
         * </ul>
         * 
         * @param slot
         *     The slots that this appointment is filling
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder slot(Reference... slot) {
            for (Reference value : slot) {
                this.slot.add(value);
            }
            return this;
        }

        /**
         * The slots from the participants' schedules that will be filled by the appointment.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Slot}</li>
         * </ul>
         * 
         * @param slot
         *     The slots that this appointment is filling
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder slot(Collection<Reference> slot) {
            this.slot = new ArrayList<>(slot);
            return this;
        }

        /**
         * The date that this appointment was initially created. This could be different to the meta.lastModified value on the 
         * initial entry, as this could have been before the resource was created on the FHIR server, and should remain unchanged 
         * over the lifespan of the appointment.
         * 
         * @param created
         *     The date that this appointment was initially created
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        /**
         * Convenience method for setting {@code comment}.
         * 
         * @param comment
         *     Additional comments
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #comment(com.ibm.fhir.model.type.String)
         */
        public Builder comment(java.lang.String comment) {
            this.comment = (comment == null) ? null : String.of(comment);
            return this;
        }

        /**
         * Additional comments about the appointment.
         * 
         * @param comment
         *     Additional comments
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Convenience method for setting {@code patientInstruction}.
         * 
         * @param patientInstruction
         *     Detailed information and instructions for the patient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #patientInstruction(com.ibm.fhir.model.type.String)
         */
        public Builder patientInstruction(java.lang.String patientInstruction) {
            this.patientInstruction = (patientInstruction == null) ? null : String.of(patientInstruction);
            return this;
        }

        /**
         * While Appointment.comment contains information for internal use, Appointment.patientInstructions is used to capture 
         * patient facing information about the Appointment (e.g. please bring your referral or fast from 8pm night before).
         * 
         * @param patientInstruction
         *     Detailed information and instructions for the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patientInstruction(String patientInstruction) {
            this.patientInstruction = patientInstruction;
            return this;
        }

        /**
         * The service request this appointment is allocated to assess (e.g. incoming referral or procedure request).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     The service request this appointment is allocated to assess
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
         * The service request this appointment is allocated to assess (e.g. incoming referral or procedure request).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     The service request this appointment is allocated to assess
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
         * List of participants involved in the appointment.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param participant
         *     Participants involved in appointment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder participant(Participant... participant) {
            for (Participant value : participant) {
                this.participant.add(value);
            }
            return this;
        }

        /**
         * List of participants involved in the appointment.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param participant
         *     Participants involved in appointment
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder participant(Collection<Participant> participant) {
            this.participant = new ArrayList<>(participant);
            return this;
        }

        /**
         * A set of date ranges (potentially including times) that the appointment is preferred to be scheduled within.
         * 
         * <p>The duration (usually in minutes) could also be provided to indicate the length of the appointment to fill and 
         * populate the start/end times for the actual allocated time. However, in other situations the duration may be 
         * calculated by the scheduling system.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param requestedPeriod
         *     Potential date/time interval(s) requested to allocate the appointment within
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder requestedPeriod(Period... requestedPeriod) {
            for (Period value : requestedPeriod) {
                this.requestedPeriod.add(value);
            }
            return this;
        }

        /**
         * A set of date ranges (potentially including times) that the appointment is preferred to be scheduled within.
         * 
         * <p>The duration (usually in minutes) could also be provided to indicate the length of the appointment to fill and 
         * populate the start/end times for the actual allocated time. However, in other situations the duration may be 
         * calculated by the scheduling system.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param requestedPeriod
         *     Potential date/time interval(s) requested to allocate the appointment within
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder requestedPeriod(Collection<Period> requestedPeriod) {
            this.requestedPeriod = new ArrayList<>(requestedPeriod);
            return this;
        }

        /**
         * Build the {@link Appointment}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>participant</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Appointment}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Appointment per the base specification
         */
        @Override
        public Appointment build() {
            Appointment appointment = new Appointment(this);
            if (validating) {
                validate(appointment);
            }
            return appointment;
        }

        protected void validate(Appointment appointment) {
            super.validate(appointment);
            ValidationSupport.checkList(appointment.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(appointment.status, "status");
            ValidationSupport.checkList(appointment.serviceCategory, "serviceCategory", CodeableConcept.class);
            ValidationSupport.checkList(appointment.serviceType, "serviceType", CodeableConcept.class);
            ValidationSupport.checkList(appointment.specialty, "specialty", CodeableConcept.class);
            ValidationSupport.checkList(appointment.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.checkList(appointment.reasonReference, "reasonReference", Reference.class);
            ValidationSupport.checkList(appointment.supportingInformation, "supportingInformation", Reference.class);
            ValidationSupport.checkList(appointment.slot, "slot", Reference.class);
            ValidationSupport.checkList(appointment.basedOn, "basedOn", Reference.class);
            ValidationSupport.checkNonEmptyList(appointment.participant, "participant", Participant.class);
            ValidationSupport.checkList(appointment.requestedPeriod, "requestedPeriod", Period.class);
            ValidationSupport.checkReferenceType(appointment.reasonReference, "reasonReference", "Condition", "Procedure", "Observation", "ImmunizationRecommendation");
            ValidationSupport.checkReferenceType(appointment.slot, "slot", "Slot");
            ValidationSupport.checkReferenceType(appointment.basedOn, "basedOn", "ServiceRequest");
        }

        protected Builder from(Appointment appointment) {
            super.from(appointment);
            identifier.addAll(appointment.identifier);
            status = appointment.status;
            cancelationReason = appointment.cancelationReason;
            serviceCategory.addAll(appointment.serviceCategory);
            serviceType.addAll(appointment.serviceType);
            specialty.addAll(appointment.specialty);
            appointmentType = appointment.appointmentType;
            reasonCode.addAll(appointment.reasonCode);
            reasonReference.addAll(appointment.reasonReference);
            priority = appointment.priority;
            description = appointment.description;
            supportingInformation.addAll(appointment.supportingInformation);
            start = appointment.start;
            end = appointment.end;
            minutesDuration = appointment.minutesDuration;
            slot.addAll(appointment.slot);
            created = appointment.created;
            comment = appointment.comment;
            patientInstruction = appointment.patientInstruction;
            basedOn.addAll(appointment.basedOn);
            participant.addAll(appointment.participant);
            requestedPeriod.addAll(appointment.requestedPeriod);
            return this;
        }
    }

    /**
     * List of participants involved in the appointment.
     */
    public static class Participant extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "ParticipantType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Role of participant in encounter.",
            valueSet = "http://hl7.org/fhir/ValueSet/encounter-participant-type"
        )
        private final List<CodeableConcept> type;
        @Summary
        @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Device", "HealthcareService", "Location" })
        private final Reference actor;
        @Summary
        @Binding(
            bindingName = "ParticipantRequired",
            strength = BindingStrength.Value.REQUIRED,
            description = "Is the Participant required to attend the appointment.",
            valueSet = "http://hl7.org/fhir/ValueSet/participantrequired|4.3.0"
        )
        private final ParticipantRequired required;
        @Summary
        @Binding(
            bindingName = "ParticipationStatus",
            strength = BindingStrength.Value.REQUIRED,
            description = "The Participation status of an appointment.",
            valueSet = "http://hl7.org/fhir/ValueSet/participationstatus|4.3.0"
        )
        @Required
        private final ParticipationStatus status;
        private final Period period;

        private Participant(Builder builder) {
            super(builder);
            type = Collections.unmodifiableList(builder.type);
            actor = builder.actor;
            required = builder.required;
            status = builder.status;
            period = builder.period;
        }

        /**
         * Role of participant in the appointment.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getType() {
            return type;
        }

        /**
         * A Person, Location/HealthcareService or Device that is participating in the appointment.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getActor() {
            return actor;
        }

        /**
         * Whether this participant is required to be present at the meeting. This covers a use-case where two doctors need to 
         * meet to discuss the results for a specific patient, and the patient is not required to be present.
         * 
         * @return
         *     An immutable object of type {@link ParticipantRequired} that may be null.
         */
        public ParticipantRequired getRequired() {
            return required;
        }

        /**
         * Participation status of the actor.
         * 
         * @return
         *     An immutable object of type {@link ParticipationStatus} that is non-null.
         */
        public ParticipationStatus getStatus() {
            return status;
        }

        /**
         * Participation period of the actor.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !type.isEmpty() || 
                (actor != null) || 
                (required != null) || 
                (status != null) || 
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
                    accept(type, "type", visitor, CodeableConcept.class);
                    accept(actor, "actor", visitor);
                    accept(required, "required", visitor);
                    accept(status, "status", visitor);
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
            Participant other = (Participant) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(actor, other.actor) && 
                Objects.equals(required, other.required) && 
                Objects.equals(status, other.status) && 
                Objects.equals(period, other.period);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    actor, 
                    required, 
                    status, 
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
            private List<CodeableConcept> type = new ArrayList<>();
            private Reference actor;
            private ParticipantRequired required;
            private ParticipationStatus status;
            private Period period;

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
             * Role of participant in the appointment.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param type
             *     Role of participant in the appointment
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
             * Role of participant in the appointment.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param type
             *     Role of participant in the appointment
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
             * A Person, Location/HealthcareService or Device that is participating in the appointment.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Patient}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Device}</li>
             * <li>{@link HealthcareService}</li>
             * <li>{@link Location}</li>
             * </ul>
             * 
             * @param actor
             *     Person, Location/HealthcareService or Device
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder actor(Reference actor) {
                this.actor = actor;
                return this;
            }

            /**
             * Whether this participant is required to be present at the meeting. This covers a use-case where two doctors need to 
             * meet to discuss the results for a specific patient, and the patient is not required to be present.
             * 
             * @param required
             *     required | optional | information-only
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder required(ParticipantRequired required) {
                this.required = required;
                return this;
            }

            /**
             * Participation status of the actor.
             * 
             * <p>This element is required.
             * 
             * @param status
             *     accepted | declined | tentative | needs-action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder status(ParticipationStatus status) {
                this.status = status;
                return this;
            }

            /**
             * Participation period of the actor.
             * 
             * @param period
             *     Participation period of the actor
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * Build the {@link Participant}
             * 
             * <p>Required elements:
             * <ul>
             * <li>status</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Participant}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Participant per the base specification
             */
            @Override
            public Participant build() {
                Participant participant = new Participant(this);
                if (validating) {
                    validate(participant);
                }
                return participant;
            }

            protected void validate(Participant participant) {
                super.validate(participant);
                ValidationSupport.checkList(participant.type, "type", CodeableConcept.class);
                ValidationSupport.requireNonNull(participant.status, "status");
                ValidationSupport.checkReferenceType(participant.actor, "actor", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Device", "HealthcareService", "Location");
                ValidationSupport.requireValueOrChildren(participant);
            }

            protected Builder from(Participant participant) {
                super.from(participant);
                type.addAll(participant.type);
                actor = participant.actor;
                required = participant.required;
                status = participant.status;
                period = participant.period;
                return this;
            }
        }
    }
}
