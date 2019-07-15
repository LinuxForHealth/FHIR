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
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.ParticipantStatus;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
 * </p>
 */
@Constraint(
    id = "apr-1",
    level = "Rule",
    location = "(base)",
    description = "Either the participantType or actor must be specified",
    expression = "participantType.exists() or actor.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class AppointmentResponse extends DomainResource {
    private final List<Identifier> identifier;
    private final Reference appointment;
    private final Instant start;
    private final Instant end;
    private final List<CodeableConcept> participantType;
    private final Reference actor;
    private final ParticipantStatus participantStatus;
    private final String comment;

    private volatile int hashCode;

    private AppointmentResponse(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        appointment = ValidationSupport.requireNonNull(builder.appointment, "appointment");
        start = builder.start;
        end = builder.end;
        participantType = Collections.unmodifiableList(builder.participantType);
        actor = builder.actor;
        participantStatus = ValidationSupport.requireNonNull(builder.participantStatus, "participantStatus");
        comment = builder.comment;
    }

    /**
     * <p>
     * This records identifiers associated with this appointment response concern that are defined by business processes and/ 
     * or used to refer to it when a direct URL reference to the resource itself is not appropriate.
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
     * Appointment that this response is replying to.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAppointment() {
        return appointment;
    }

    /**
     * <p>
     * Date/Time that the appointment is to take place, or requested new start time.
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
     * This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new 
     * time to request a re-negotiation of the end time.
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
     * Role of participant in the appointment.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getParticipantType() {
        return participantType;
    }

    /**
     * <p>
     * A Person, Location, HealthcareService, or Device that is participating in the appointment.
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
     * Participation status of the participant. When the status is declined or tentative if the start/end times are different 
     * to the appointment, then these times should be interpreted as a requested time change. When the status is accepted, 
     * the times can either be the time of the appointment (as a confirmation of the time) or can be empty.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ParticipantStatus}.
     */
    public ParticipantStatus getParticipantStatus() {
        return participantStatus;
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
                accept(appointment, "appointment", visitor);
                accept(start, "start", visitor);
                accept(end, "end", visitor);
                accept(participantType, "participantType", visitor, CodeableConcept.class);
                accept(actor, "actor", visitor);
                accept(participantStatus, "participantStatus", visitor);
                accept(comment, "comment", visitor);
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
        AppointmentResponse other = (AppointmentResponse) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(appointment, other.appointment) && 
            Objects.equals(start, other.start) && 
            Objects.equals(end, other.end) && 
            Objects.equals(participantType, other.participantType) && 
            Objects.equals(actor, other.actor) && 
            Objects.equals(participantStatus, other.participantStatus) && 
            Objects.equals(comment, other.comment);
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
                appointment, 
                start, 
                end, 
                participantType, 
                actor, 
                participantStatus, 
                comment);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(appointment, participantStatus).from(this);
    }

    public Builder toBuilder(Reference appointment, ParticipantStatus participantStatus) {
        return new Builder(appointment, participantStatus).from(this);
    }

    public static Builder builder(Reference appointment, ParticipantStatus participantStatus) {
        return new Builder(appointment, participantStatus);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Reference appointment;
        private final ParticipantStatus participantStatus;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Instant start;
        private Instant end;
        private List<CodeableConcept> participantType = new ArrayList<>();
        private Reference actor;
        private String comment;

        private Builder(Reference appointment, ParticipantStatus participantStatus) {
            super();
            this.appointment = appointment;
            this.participantStatus = participantStatus;
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
         * This records identifiers associated with this appointment response concern that are defined by business processes and/ 
         * or used to refer to it when a direct URL reference to the resource itself is not appropriate.
         * </p>
         * <p>
         * Adds new element(s) to existing list
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
         * This records identifiers associated with this appointment response concern that are defined by business processes and/ 
         * or used to refer to it when a direct URL reference to the resource itself is not appropriate.
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
         * Date/Time that the appointment is to take place, or requested new start time.
         * </p>
         * 
         * @param start
         *     Time from appointment, or requested new start time
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
         * This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new 
         * time to request a re-negotiation of the end time.
         * </p>
         * 
         * @param end
         *     Time from appointment, or requested new end time
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
         * Role of participant in the appointment.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param participantType
         *     Role of participant in the appointment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder participantType(CodeableConcept... participantType) {
            for (CodeableConcept value : participantType) {
                this.participantType.add(value);
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
         * @param participantType
         *     Role of participant in the appointment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder participantType(Collection<CodeableConcept> participantType) {
            this.participantType = new ArrayList<>(participantType);
            return this;
        }

        /**
         * <p>
         * A Person, Location, HealthcareService, or Device that is participating in the appointment.
         * </p>
         * 
         * @param actor
         *     Person, Location, HealthcareService, or Device
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

        @Override
        public AppointmentResponse build() {
            return new AppointmentResponse(this);
        }

        private Builder from(AppointmentResponse appointmentResponse) {
            id = appointmentResponse.id;
            meta = appointmentResponse.meta;
            implicitRules = appointmentResponse.implicitRules;
            language = appointmentResponse.language;
            text = appointmentResponse.text;
            contained.addAll(appointmentResponse.contained);
            extension.addAll(appointmentResponse.extension);
            modifierExtension.addAll(appointmentResponse.modifierExtension);
            identifier.addAll(appointmentResponse.identifier);
            start = appointmentResponse.start;
            end = appointmentResponse.end;
            participantType.addAll(appointmentResponse.participantType);
            actor = appointmentResponse.actor;
            comment = appointmentResponse.comment;
            return this;
        }
    }
}
