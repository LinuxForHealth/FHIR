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

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.AppointmentStatus;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.ParticipantRequired;
import com.ibm.watsonhealth.fhir.model.type.ParticipationStatus;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific 
 * date/time. This may result in one or more Encounter(s).
 * </p>
 */
@Constraint(
    id = "app-1",
    level = "Rule",
    location = "Appointment.participant",
    description = "Either the type or actor on the participant SHALL be specified",
    expression = "type.exists() or actor.exists()"
)
@Constraint(
    id = "app-2",
    level = "Rule",
    location = "(base)",
    description = "Either start and end are specified, or neither",
    expression = "start.exists() = end.exists()"
)
@Constraint(
    id = "app-3",
    level = "Rule",
    location = "(base)",
    description = "Only proposed or cancelled appointments can be missing start/end dates",
    expression = "(start.exists() and end.exists()) or (status in ('proposed' | 'cancelled' | 'waitlist'))"
)
@Constraint(
    id = "app-4",
    level = "Rule",
    location = "(base)",
    description = "Cancelation reason is only used for appointments that have been cancelled, or no-show",
    expression = "Appointment.cancelationReason.exists() implies (Appointment.status='no-show' or Appointment.status='cancelled')"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Appointment extends DomainResource {
    private final List<Identifier> identifier;
    private final AppointmentStatus status;
    private final CodeableConcept cancelationReason;
    private final List<CodeableConcept> serviceCategory;
    private final List<CodeableConcept> serviceType;
    private final List<CodeableConcept> specialty;
    private final CodeableConcept appointmentType;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final UnsignedInt priority;
    private final String description;
    private final List<Reference> supportingInformation;
    private final Instant start;
    private final Instant end;
    private final PositiveInt minutesDuration;
    private final List<Reference> slot;
    private final DateTime created;
    private final String comment;
    private final String patientInstruction;
    private final List<Reference> basedOn;
    private final List<Participant> participant;
    private final List<Period> requestedPeriod;

    private volatile int hashCode;

    private Appointment(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
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
        participant = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.participant, "participant"));
        requestedPeriod = Collections.unmodifiableList(builder.requestedPeriod);
    }

    /**
     * <p>
     * This records identifiers associated with this appointment concern that are defined by business processes and/or used 
     * to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in 
     * written / printed documentation).
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
     * The overall status of the Appointment. Each of the participants has their own participation status which indicates 
     * their involvement in the process, however this status indicates the shared status.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link AppointmentStatus}.
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The coded reason for the appointment being cancelled. This is often used in reporting/billing/futher processing to 
     * determine if further actions are required, or specific fees apply.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getCancelationReason() {
        return cancelationReason;
    }

    /**
     * <p>
     * A broad categorization of the service that is to be performed during this appointment.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getServiceCategory() {
        return serviceCategory;
    }

    /**
     * <p>
     * The specific service that is to be performed during this appointment.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getServiceType() {
        return serviceType;
    }

    /**
     * <p>
     * The specialty of a practitioner that would be required to perform the service requested in this appointment.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getSpecialty() {
        return specialty;
    }

    /**
     * <p>
     * The style of appointment or patient that has been booked in the slot (not service type).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getAppointmentType() {
        return appointmentType;
    }

    /**
     * <p>
     * The coded reason that this appointment is being scheduled. This is more clinical than administrative.
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
     * Reason the appointment has been scheduled to take place, as specified using information from another resource. When 
     * the patient arrives and the encounter begins it may be used as the admission diagnosis. The indication will typically 
     * be a Condition (with other resources referenced in the evidence.detail), or a Procedure.
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
     * The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The 
     * iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt}.
     */
    public UnsignedInt getPriority() {
        return priority;
    }

    /**
     * <p>
     * The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment 
     * list. Detailed or expanded information should be put in the comment field.
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
     * Additional information to support the appointment provided when making the appointment.
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
     * Date/Time that the appointment is to take place.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Instant}.
     */
    public Instant getStart() {
        return start;
    }

    /**
     * <p>
     * Date/Time that the appointment is to conclude.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Instant}.
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * <p>
     * Number of minutes that the appointment is to take. This can be less than the duration between the start and end times. 
     * For example, where the actual time of appointment is only an estimate or if a 30 minute appointment is being 
     * requested, but any time would work. Also, if there is, for example, a planned 15 minute break in the middle of a long 
     * appointment, the duration may be 15 minutes less than the difference between the start and end.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PositiveInt}.
     */
    public PositiveInt getMinutesDuration() {
        return minutesDuration;
    }

    /**
     * <p>
     * The slots from the participants' schedules that will be filled by the appointment.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSlot() {
        return slot;
    }

    /**
     * <p>
     * The date that this appointment was initially created. This could be different to the meta.lastModified value on the 
     * initial entry, as this could have been before the resource was created on the FHIR server, and should remain unchanged 
     * over the lifespan of the appointment.
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
     * Additional comments about the appointment.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getComment() {
        return comment;
    }

    /**
     * <p>
     * While Appointment.comment contains information for internal use, Appointment.patientInstructions is used to capture 
     * patient facing information about the Appointment (e.g. please bring your referral or fast from 8pm night before).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPatientInstruction() {
        return patientInstruction;
    }

    /**
     * <p>
     * The service request this appointment is allocated to assess (e.g. incoming referral or procedure request).
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
     * List of participants involved in the appointment.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Participant}.
     */
    public List<Participant> getParticipant() {
        return participant;
    }

    /**
     * <p>
     * A set of date ranges (potentially including times) that the appointment is preferred to be scheduled within.
     * </p>
     * <p>
     * The duration (usually in minutes) could also be provided to indicate the length of the appointment to fill and 
     * populate the start/end times for the actual allocated time. However, in other situations the duration may be 
     * calculated by the scheduling system.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Period}.
     */
    public List<Period> getRequestedPeriod() {
        return requestedPeriod;
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
        return new Builder(status, participant).from(this);
    }

    public Builder toBuilder(AppointmentStatus status, List<Participant> participant) {
        return new Builder(status, participant).from(this);
    }

    public static Builder builder(AppointmentStatus status, List<Participant> participant) {
        return new Builder(status, participant);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final AppointmentStatus status;
        private final List<Participant> participant;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
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
        private List<Period> requestedPeriod = new ArrayList<>();

        private Builder(AppointmentStatus status, List<Participant> participant) {
            super();
            this.status = status;
            this.participant = participant;
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * This records identifiers associated with this appointment concern that are defined by business processes and/or used 
         * to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in 
         * written / printed documentation).
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * This records identifiers associated with this appointment concern that are defined by business processes and/or used 
         * to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in 
         * written / printed documentation).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     External Ids for this item
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
         * The coded reason for the appointment being cancelled. This is often used in reporting/billing/futher processing to 
         * determine if further actions are required, or specific fees apply.
         * </p>
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
         * <p>
         * A broad categorization of the service that is to be performed during this appointment.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A broad categorization of the service that is to be performed during this appointment.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param serviceCategory
         *     A broad categorization of the service that is to be performed during this appointment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviceCategory(Collection<CodeableConcept> serviceCategory) {
            this.serviceCategory = new ArrayList<>(serviceCategory);
            return this;
        }

        /**
         * <p>
         * The specific service that is to be performed during this appointment.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The specific service that is to be performed during this appointment.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param serviceType
         *     The specific service that is to be performed during this appointment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviceType(Collection<CodeableConcept> serviceType) {
            this.serviceType = new ArrayList<>(serviceType);
            return this;
        }

        /**
         * <p>
         * The specialty of a practitioner that would be required to perform the service requested in this appointment.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The specialty of a practitioner that would be required to perform the service requested in this appointment.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param specialty
         *     The specialty of a practitioner that would be required to perform the service requested in this appointment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specialty(Collection<CodeableConcept> specialty) {
            this.specialty = new ArrayList<>(specialty);
            return this;
        }

        /**
         * <p>
         * The style of appointment or patient that has been booked in the slot (not service type).
         * </p>
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
         * <p>
         * The coded reason that this appointment is being scheduled. This is more clinical than administrative.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The coded reason that this appointment is being scheduled. This is more clinical than administrative.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonCode
         *     Coded reason this appointment is scheduled
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
         * Reason the appointment has been scheduled to take place, as specified using information from another resource. When 
         * the patient arrives and the encounter begins it may be used as the admission diagnosis. The indication will typically 
         * be a Condition (with other resources referenced in the evidence.detail), or a Procedure.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Reason the appointment has been scheduled to take place, as specified using information from another resource. When 
         * the patient arrives and the encounter begins it may be used as the admission diagnosis. The indication will typically 
         * be a Condition (with other resources referenced in the evidence.detail), or a Procedure.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonReference
         *     Reason the appointment is to take place (resource)
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
         * The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The 
         * iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority).
         * </p>
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
         * <p>
         * The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment 
         * list. Detailed or expanded information should be put in the comment field.
         * </p>
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
         * <p>
         * Additional information to support the appointment provided when making the appointment.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Additional information to support the appointment provided when making the appointment.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param supportingInformation
         *     Additional information to support the appointment
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
         * Date/Time that the appointment is to take place.
         * </p>
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
         * <p>
         * Date/Time that the appointment is to conclude.
         * </p>
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
         * <p>
         * Number of minutes that the appointment is to take. This can be less than the duration between the start and end times. 
         * For example, where the actual time of appointment is only an estimate or if a 30 minute appointment is being 
         * requested, but any time would work. Also, if there is, for example, a planned 15 minute break in the middle of a long 
         * appointment, the duration may be 15 minutes less than the difference between the start and end.
         * </p>
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
         * <p>
         * The slots from the participants' schedules that will be filled by the appointment.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The slots from the participants' schedules that will be filled by the appointment.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param slot
         *     The slots that this appointment is filling
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder slot(Collection<Reference> slot) {
            this.slot = new ArrayList<>(slot);
            return this;
        }

        /**
         * <p>
         * The date that this appointment was initially created. This could be different to the meta.lastModified value on the 
         * initial entry, as this could have been before the resource was created on the FHIR server, and should remain unchanged 
         * over the lifespan of the appointment.
         * </p>
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
         * <p>
         * Additional comments about the appointment.
         * </p>
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
         * <p>
         * While Appointment.comment contains information for internal use, Appointment.patientInstructions is used to capture 
         * patient facing information about the Appointment (e.g. please bring your referral or fast from 8pm night before).
         * </p>
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
         * <p>
         * The service request this appointment is allocated to assess (e.g. incoming referral or procedure request).
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The service request this appointment is allocated to assess (e.g. incoming referral or procedure request).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param basedOn
         *     The service request this appointment is allocated to assess
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
         * A set of date ranges (potentially including times) that the appointment is preferred to be scheduled within.
         * </p>
         * <p>
         * The duration (usually in minutes) could also be provided to indicate the length of the appointment to fill and 
         * populate the start/end times for the actual allocated time. However, in other situations the duration may be 
         * calculated by the scheduling system.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A set of date ranges (potentially including times) that the appointment is preferred to be scheduled within.
         * </p>
         * <p>
         * The duration (usually in minutes) could also be provided to indicate the length of the appointment to fill and 
         * populate the start/end times for the actual allocated time. However, in other situations the duration may be 
         * calculated by the scheduling system.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param requestedPeriod
         *     Potential date/time interval(s) requested to allocate the appointment within
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder requestedPeriod(Collection<Period> requestedPeriod) {
            this.requestedPeriod = new ArrayList<>(requestedPeriod);
            return this;
        }

        @Override
        public Appointment build() {
            return new Appointment(this);
        }

        private Builder from(Appointment appointment) {
            id = appointment.id;
            meta = appointment.meta;
            implicitRules = appointment.implicitRules;
            language = appointment.language;
            text = appointment.text;
            contained.addAll(appointment.contained);
            extension.addAll(appointment.extension);
            modifierExtension.addAll(appointment.modifierExtension);
            identifier.addAll(appointment.identifier);
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
            requestedPeriod.addAll(appointment.requestedPeriod);
            return this;
        }
    }

    /**
     * <p>
     * List of participants involved in the appointment.
     * </p>
     */
    public static class Participant extends BackboneElement {
        private final List<CodeableConcept> type;
        private final Reference actor;
        private final ParticipantRequired required;
        private final ParticipationStatus status;
        private final Period period;

        private volatile int hashCode;

        private Participant(Builder builder) {
            super(builder);
            type = Collections.unmodifiableList(builder.type);
            actor = builder.actor;
            required = builder.required;
            status = ValidationSupport.requireNonNull(builder.status, "status");
            period = builder.period;
            if (!hasValue() && !hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Role of participant in the appointment.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getType() {
            return type;
        }

        /**
         * <p>
         * A Person, Location/HealthcareService or Device that is participating in the appointment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getActor() {
            return actor;
        }

        /**
         * <p>
         * Whether this participant is required to be present at the meeting. This covers a use-case where two doctors need to 
         * meet to discuss the results for a specific patient, and the patient is not required to be present.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ParticipantRequired}.
         */
        public ParticipantRequired getRequired() {
            return required;
        }

        /**
         * <p>
         * Participation status of the actor.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ParticipationStatus}.
         */
        public ParticipationStatus getStatus() {
            return status;
        }

        /**
         * <p>
         * Participation period of the actor.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getPeriod() {
            return period;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                !type.isEmpty() || 
                (actor != null) || 
                (required != null) || 
                (status != null) || 
                (period != null);
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
                    accept(type, "type", visitor, CodeableConcept.class);
                    accept(actor, "actor", visitor);
                    accept(required, "required", visitor);
                    accept(status, "status", visitor);
                    accept(period, "period", visitor);
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
            return new Builder(status).from(this);
        }

        public Builder toBuilder(ParticipationStatus status) {
            return new Builder(status).from(this);
        }

        public static Builder builder(ParticipationStatus status) {
            return new Builder(status);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final ParticipationStatus status;

            // optional
            private List<CodeableConcept> type = new ArrayList<>();
            private Reference actor;
            private ParticipantRequired required;
            private Period period;

            private Builder(ParticipationStatus status) {
                super();
                this.status = status;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * Role of participant in the appointment.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Role of participant in the appointment.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param type
             *     Role of participant in the appointment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(Collection<CodeableConcept> type) {
                this.type = new ArrayList<>(type);
                return this;
            }

            /**
             * <p>
             * A Person, Location/HealthcareService or Device that is participating in the appointment.
             * </p>
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
             * <p>
             * Whether this participant is required to be present at the meeting. This covers a use-case where two doctors need to 
             * meet to discuss the results for a specific patient, and the patient is not required to be present.
             * </p>
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
             * <p>
             * Participation period of the actor.
             * </p>
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

            @Override
            public Participant build() {
                return new Participant(this);
            }

            private Builder from(Participant participant) {
                id = participant.id;
                extension.addAll(participant.extension);
                modifierExtension.addAll(participant.modifierExtension);
                type.addAll(participant.type);
                actor = participant.actor;
                required = participant.required;
                period = participant.period;
                return this;
            }
        }
    }
}
