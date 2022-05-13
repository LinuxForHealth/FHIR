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
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.SlotStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A slot of time on a schedule that may be available for booking appointments.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "slot-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/c80-practice-codes",
    expression = "specialty.exists() implies (specialty.all(memberOf('http://hl7.org/fhir/ValueSet/c80-practice-codes', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/Slot",
    generated = true
)
@Constraint(
    id = "slot-1",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://terminology.hl7.org/ValueSet/v2-0276",
    expression = "appointmentType.exists() implies (appointmentType.memberOf('http://terminology.hl7.org/ValueSet/v2-0276', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/Slot",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Slot extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
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
        description = "Additional details about where the content was created (e.g. clinical specialty).",
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
    @ReferenceTarget({ "Schedule" })
    @Required
    private final Reference schedule;
    @Summary
    @Binding(
        bindingName = "SlotStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The free/busy status of the slot.",
        valueSet = "http://hl7.org/fhir/ValueSet/slotstatus|4.3.0-cibuild"
    )
    @Required
    private final SlotStatus status;
    @Summary
    @Required
    private final Instant start;
    @Summary
    @Required
    private final Instant end;
    private final Boolean overbooked;
    private final String comment;

    private Slot(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        serviceCategory = Collections.unmodifiableList(builder.serviceCategory);
        serviceType = Collections.unmodifiableList(builder.serviceType);
        specialty = Collections.unmodifiableList(builder.specialty);
        appointmentType = builder.appointmentType;
        schedule = builder.schedule;
        status = builder.status;
        start = builder.start;
        end = builder.end;
        overbooked = builder.overbooked;
        comment = builder.comment;
    }

    /**
     * External Ids for this item.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
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
     * The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is 
     * at a location, rather than the location itself). If provided then this overrides the value provided on the 
     * availability resource.
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
     * The style of appointment or patient that may be booked in the slot (not service type).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getAppointmentType() {
        return appointmentType;
    }

    /**
     * The schedule resource that this slot defines an interval of status information.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSchedule() {
        return schedule;
    }

    /**
     * busy | free | busy-unavailable | busy-tentative | entered-in-error.
     * 
     * @return
     *     An immutable object of type {@link SlotStatus} that is non-null.
     */
    public SlotStatus getStatus() {
        return status;
    }

    /**
     * Date/Time that the slot is to begin.
     * 
     * @return
     *     An immutable object of type {@link Instant} that is non-null.
     */
    public Instant getStart() {
        return start;
    }

    /**
     * Date/Time that the slot is to conclude.
     * 
     * @return
     *     An immutable object of type {@link Instant} that is non-null.
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * This slot has already been overbooked, appointments are unlikely to be accepted for this time.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getOverbooked() {
        return overbooked;
    }

    /**
     * Comments on the slot to describe any extended information. Such as custom constraints on the slot.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getComment() {
        return comment;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !serviceCategory.isEmpty() || 
            !serviceType.isEmpty() || 
            !specialty.isEmpty() || 
            (appointmentType != null) || 
            (schedule != null) || 
            (status != null) || 
            (start != null) || 
            (end != null) || 
            (overbooked != null) || 
            (comment != null);
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
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private List<CodeableConcept> serviceCategory = new ArrayList<>();
        private List<CodeableConcept> serviceType = new ArrayList<>();
        private List<CodeableConcept> specialty = new ArrayList<>();
        private CodeableConcept appointmentType;
        private Reference schedule;
        private SlotStatus status;
        private Instant start;
        private Instant end;
        private Boolean overbooked;
        private String comment;

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
         * External Ids for this item.
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
         * External Ids for this item.
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
         * The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is 
         * at a location, rather than the location itself). If provided then this overrides the value provided on the 
         * availability resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is 
         * at a location, rather than the location itself). If provided then this overrides the value provided on the 
         * availability resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param serviceType
         *     The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is 
         *     at a location, rather than the location itself). If provided then this overrides the value provided on the 
         *     availability resource
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
         * The style of appointment or patient that may be booked in the slot (not service type).
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
         * The schedule resource that this slot defines an interval of status information.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Schedule}</li>
         * </ul>
         * 
         * @param schedule
         *     The schedule resource that this slot defines an interval of status information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder schedule(Reference schedule) {
            this.schedule = schedule;
            return this;
        }

        /**
         * busy | free | busy-unavailable | busy-tentative | entered-in-error.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     busy | free | busy-unavailable | busy-tentative | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(SlotStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Convenience method for setting {@code start}.
         * 
         * <p>This element is required.
         * 
         * @param start
         *     Date/Time that the slot is to begin
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
         * Date/Time that the slot is to begin.
         * 
         * <p>This element is required.
         * 
         * @param start
         *     Date/Time that the slot is to begin
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
         * <p>This element is required.
         * 
         * @param end
         *     Date/Time that the slot is to conclude
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
         * Date/Time that the slot is to conclude.
         * 
         * <p>This element is required.
         * 
         * @param end
         *     Date/Time that the slot is to conclude
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder end(Instant end) {
            this.end = end;
            return this;
        }

        /**
         * Convenience method for setting {@code overbooked}.
         * 
         * @param overbooked
         *     This slot has already been overbooked, appointments are unlikely to be accepted for this time
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #overbooked(com.ibm.fhir.model.type.Boolean)
         */
        public Builder overbooked(java.lang.Boolean overbooked) {
            this.overbooked = (overbooked == null) ? null : Boolean.of(overbooked);
            return this;
        }

        /**
         * This slot has already been overbooked, appointments are unlikely to be accepted for this time.
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
         * Convenience method for setting {@code comment}.
         * 
         * @param comment
         *     Comments on the slot to describe any extended information. Such as custom constraints on the slot
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
         * Comments on the slot to describe any extended information. Such as custom constraints on the slot.
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

        /**
         * Build the {@link Slot}
         * 
         * <p>Required elements:
         * <ul>
         * <li>schedule</li>
         * <li>status</li>
         * <li>start</li>
         * <li>end</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Slot}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Slot per the base specification
         */
        @Override
        public Slot build() {
            Slot slot = new Slot(this);
            if (validating) {
                validate(slot);
            }
            return slot;
        }

        protected void validate(Slot slot) {
            super.validate(slot);
            ValidationSupport.checkList(slot.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(slot.serviceCategory, "serviceCategory", CodeableConcept.class);
            ValidationSupport.checkList(slot.serviceType, "serviceType", CodeableConcept.class);
            ValidationSupport.checkList(slot.specialty, "specialty", CodeableConcept.class);
            ValidationSupport.requireNonNull(slot.schedule, "schedule");
            ValidationSupport.requireNonNull(slot.status, "status");
            ValidationSupport.requireNonNull(slot.start, "start");
            ValidationSupport.requireNonNull(slot.end, "end");
            ValidationSupport.checkReferenceType(slot.schedule, "schedule", "Schedule");
        }

        protected Builder from(Slot slot) {
            super.from(slot);
            identifier.addAll(slot.identifier);
            serviceCategory.addAll(slot.serviceCategory);
            serviceType.addAll(slot.serviceType);
            specialty.addAll(slot.specialty);
            appointmentType = slot.appointmentType;
            schedule = slot.schedule;
            status = slot.status;
            start = slot.start;
            end = slot.end;
            overbooked = slot.overbooked;
            comment = slot.comment;
            return this;
        }
    }
}
