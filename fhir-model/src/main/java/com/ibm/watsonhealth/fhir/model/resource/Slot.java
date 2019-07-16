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

import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.SlotStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A slot of time on a schedule that may be available for booking appointments.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Slot extends DomainResource {
    private final List<Identifier> identifier;
    private final List<CodeableConcept> serviceCategory;
    private final List<CodeableConcept> serviceType;
    private final List<CodeableConcept> specialty;
    private final CodeableConcept appointmentType;
    private final Reference schedule;
    private final SlotStatus status;
    private final Instant start;
    private final Instant end;
    private final Boolean overbooked;
    private final String comment;

    private volatile int hashCode;

    private Slot(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        serviceCategory = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.serviceCategory, "serviceCategory"));
        serviceType = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.serviceType, "serviceType"));
        specialty = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.specialty, "specialty"));
        appointmentType = builder.appointmentType;
        schedule = ValidationSupport.requireNonNull(builder.schedule, "schedule");
        status = ValidationSupport.requireNonNull(builder.status, "status");
        start = ValidationSupport.requireNonNull(builder.start, "start");
        end = ValidationSupport.requireNonNull(builder.end, "end");
        overbooked = builder.overbooked;
        comment = builder.comment;
    }

    /**
     * <p>
     * External Ids for this item.
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
     * The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is 
     * at a location, rather than the location itself). If provided then this overrides the value provided on the 
     * availability resource.
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
     * The style of appointment or patient that may be booked in the slot (not service type).
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
     * The schedule resource that this slot defines an interval of status information.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSchedule() {
        return schedule;
    }

    /**
     * <p>
     * busy | free | busy-unavailable | busy-tentative | entered-in-error.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link SlotStatus}.
     */
    public SlotStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Date/Time that the slot is to begin.
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
     * Date/Time that the slot is to conclude.
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
     * This slot has already been overbooked, appointments are unlikely to be accepted for this time.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getOverbooked() {
        return overbooked;
    }

    /**
     * <p>
     * Comments on the slot to describe any extended information. Such as custom constraints on the slot.
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
                accept(serviceCategory, "serviceCategory", visitor, CodeableConcept.class);
                accept(serviceType, "serviceType", visitor, CodeableConcept.class);
                accept(specialty, "specialty", visitor, CodeableConcept.class);
                accept(appointmentType, "appointmentType", visitor);
                accept(schedule, "schedule", visitor);
                accept(status, "status", visitor);
                accept(start, "start", visitor);
                accept(end, "end", visitor);
                accept(overbooked, "overbooked", visitor);
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
        Slot other = (Slot) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(serviceCategory, other.serviceCategory) && 
            Objects.equals(serviceType, other.serviceType) && 
            Objects.equals(specialty, other.specialty) && 
            Objects.equals(appointmentType, other.appointmentType) && 
            Objects.equals(schedule, other.schedule) && 
            Objects.equals(status, other.status) && 
            Objects.equals(start, other.start) && 
            Objects.equals(end, other.end) && 
            Objects.equals(overbooked, other.overbooked) && 
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
                serviceCategory, 
                serviceType, 
                specialty, 
                appointmentType, 
                schedule, 
                status, 
                start, 
                end, 
                overbooked, 
                comment);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(schedule, status, start, end).from(this);
    }

    public Builder toBuilder(Reference schedule, SlotStatus status, Instant start, Instant end) {
        return new Builder(schedule, status, start, end).from(this);
    }

    public static Builder builder(Reference schedule, SlotStatus status, Instant start, Instant end) {
        return new Builder(schedule, status, start, end);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Reference schedule;
        private final SlotStatus status;
        private final Instant start;
        private final Instant end;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<CodeableConcept> serviceCategory = new ArrayList<>();
        private List<CodeableConcept> serviceType = new ArrayList<>();
        private List<CodeableConcept> specialty = new ArrayList<>();
        private CodeableConcept appointmentType;
        private Boolean overbooked;
        private String comment;

        private Builder(Reference schedule, SlotStatus status, Instant start, Instant end) {
            super();
            this.schedule = schedule;
            this.status = status;
            this.start = start;
            this.end = end;
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
         * External Ids for this item.
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
         * External Ids for this item.
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
         * A broad categorization of the service that is to be performed during this appointment.
         * </p>
         * <p>
         * Adds new element(s) to existing list
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
         * The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is 
         * at a location, rather than the location itself). If provided then this overrides the value provided on the 
         * availability resource.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param serviceType
         *     The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is 
         *     at a location, rather than the location itself). If provided then this overrides the value provided on the 
         *     availability resource
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
         * The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is 
         * at a location, rather than the location itself). If provided then this overrides the value provided on the 
         * availability resource.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param serviceType
         *     The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is 
         *     at a location, rather than the location itself). If provided then this overrides the value provided on the 
         *     availability resource
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
         * Adds new element(s) to existing list
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
         * The style of appointment or patient that may be booked in the slot (not service type).
         * </p>
         * 
         * @param appointmentType
         *     The style of appointment or patient that may be booked in the slot (not service type)
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
         * This slot has already been overbooked, appointments are unlikely to be accepted for this time.
         * </p>
         * 
         * @param overbooked
         *     This slot has already been overbooked, appointments are unlikely to be accepted for this time
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder overbooked(Boolean overbooked) {
            this.overbooked = overbooked;
            return this;
        }

        /**
         * <p>
         * Comments on the slot to describe any extended information. Such as custom constraints on the slot.
         * </p>
         * 
         * @param comment
         *     Comments on the slot to describe any extended information. Such as custom constraints on the slot
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        @Override
        public Slot build() {
            return new Slot(this);
        }

        private Builder from(Slot slot) {
            id = slot.id;
            meta = slot.meta;
            implicitRules = slot.implicitRules;
            language = slot.language;
            text = slot.text;
            contained.addAll(slot.contained);
            extension.addAll(slot.extension);
            modifierExtension.addAll(slot.modifierExtension);
            identifier.addAll(slot.identifier);
            serviceCategory.addAll(slot.serviceCategory);
            serviceType.addAll(slot.serviceType);
            specialty.addAll(slot.specialty);
            appointmentType = slot.appointmentType;
            overbooked = slot.overbooked;
            comment = slot.comment;
            return this;
        }
    }
}
