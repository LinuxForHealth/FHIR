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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DaysOfWeek;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The details of a healthcare service available at a location.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "healthcareService-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/c80-practice-codes",
    expression = "specialty.exists() implies (specialty.all(memberOf('http://hl7.org/fhir/ValueSet/c80-practice-codes', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/HealthcareService",
    generated = true
)
@Constraint(
    id = "healthcareService-1",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/languages",
    expression = "communication.exists() implies (communication.all(memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/HealthcareService",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class HealthcareService extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final Boolean active;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference providedBy;
    @Summary
    @Binding(
        bindingName = "service-category",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A category of the service(s) that could be provided.",
        valueSet = "http://hl7.org/fhir/ValueSet/service-category"
    )
    private final List<CodeableConcept> category;
    @Summary
    @Binding(
        bindingName = "service-type",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Additional details about where the content was created (e.g. clinical specialty).",
        valueSet = "http://hl7.org/fhir/ValueSet/service-type"
    )
    private final List<CodeableConcept> type;
    @Summary
    @Binding(
        bindingName = "service-specialty",
        strength = BindingStrength.Value.PREFERRED,
        description = "A specialty that a healthcare service may provide.",
        valueSet = "http://hl7.org/fhir/ValueSet/c80-practice-codes"
    )
    private final List<CodeableConcept> specialty;
    @Summary
    @ReferenceTarget({ "Location" })
    private final List<Reference> location;
    @Summary
    private final String name;
    @Summary
    private final String comment;
    private final Markdown extraDetails;
    @Summary
    private final Attachment photo;
    private final List<ContactPoint> telecom;
    @ReferenceTarget({ "Location" })
    private final List<Reference> coverageArea;
    @Binding(
        bindingName = "ServiceProvisionConditions",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The code(s) that detail the conditions under which the healthcare service is available/offered.",
        valueSet = "http://hl7.org/fhir/ValueSet/service-provision-conditions"
    )
    private final List<CodeableConcept> serviceProvisionCode;
    private final List<Eligibility> eligibility;
    @Binding(
        bindingName = "Program",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Government or local programs that this service applies to.",
        valueSet = "http://hl7.org/fhir/ValueSet/program"
    )
    private final List<CodeableConcept> program;
    @Binding(
        bindingName = "ServiceCharacteristic",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A custom attribute that could be provided at a service (e.g. Wheelchair accessibiliy)."
    )
    private final List<CodeableConcept> characteristic;
    @Binding(
        bindingName = "Language",
        strength = BindingStrength.Value.PREFERRED,
        description = "A human language.",
        valueSet = "http://hl7.org/fhir/ValueSet/languages",
        maxValueSet = "http://hl7.org/fhir/ValueSet/all-languages"
    )
    private final List<CodeableConcept> communication;
    @Binding(
        bindingName = "ReferralMethod",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The methods of referral can be used when referring to a specific HealthCareService resource.",
        valueSet = "http://hl7.org/fhir/ValueSet/service-referral-method"
    )
    private final List<CodeableConcept> referralMethod;
    private final Boolean appointmentRequired;
    private final List<AvailableTime> availableTime;
    private final List<NotAvailable> notAvailable;
    private final String availabilityExceptions;
    @ReferenceTarget({ "Endpoint" })
    private final List<Reference> endpoint;

    private HealthcareService(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        active = builder.active;
        providedBy = builder.providedBy;
        category = Collections.unmodifiableList(builder.category);
        type = Collections.unmodifiableList(builder.type);
        specialty = Collections.unmodifiableList(builder.specialty);
        location = Collections.unmodifiableList(builder.location);
        name = builder.name;
        comment = builder.comment;
        extraDetails = builder.extraDetails;
        photo = builder.photo;
        telecom = Collections.unmodifiableList(builder.telecom);
        coverageArea = Collections.unmodifiableList(builder.coverageArea);
        serviceProvisionCode = Collections.unmodifiableList(builder.serviceProvisionCode);
        eligibility = Collections.unmodifiableList(builder.eligibility);
        program = Collections.unmodifiableList(builder.program);
        characteristic = Collections.unmodifiableList(builder.characteristic);
        communication = Collections.unmodifiableList(builder.communication);
        referralMethod = Collections.unmodifiableList(builder.referralMethod);
        appointmentRequired = builder.appointmentRequired;
        availableTime = Collections.unmodifiableList(builder.availableTime);
        notAvailable = Collections.unmodifiableList(builder.notAvailable);
        availabilityExceptions = builder.availabilityExceptions;
        endpoint = Collections.unmodifiableList(builder.endpoint);
    }

    /**
     * External identifiers for this item.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * This flag is used to mark the record to not be used. This is not used when a center is closed for maintenance, or for 
     * holidays, the notAvailable period is to be used for this.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * The organization that provides this healthcare service.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getProvidedBy() {
        return providedBy;
    }

    /**
     * Identifies the broad category of service being performed or delivered.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * The specific type of service that may be delivered or performed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getType() {
        return type;
    }

    /**
     * Collection of specialties handled by the service site. This is more of a medical term.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getSpecialty() {
        return specialty;
    }

    /**
     * The location(s) where this healthcare service may be provided.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getLocation() {
        return location;
    }

    /**
     * Further description of the service as it would be presented to a consumer while searching.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * Any additional description of the service and/or any specific issues not covered by the other attributes, which can be 
     * displayed as further detail under the serviceName.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Extra details about the service that can't be placed in the other fields.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getExtraDetails() {
        return extraDetails;
    }

    /**
     * If there is a photo/symbol associated with this HealthcareService, it may be included here to facilitate quick 
     * identification of the service in a list.
     * 
     * @return
     *     An immutable object of type {@link Attachment} that may be null.
     */
    public Attachment getPhoto() {
        return photo;
    }

    /**
     * List of contacts related to this specific healthcare service.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactPoint} that may be empty.
     */
    public List<ContactPoint> getTelecom() {
        return telecom;
    }

    /**
     * The location(s) that this service is available to (not where the service is provided).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getCoverageArea() {
        return coverageArea;
    }

    /**
     * The code(s) that detail the conditions under which the healthcare service is available/offered.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getServiceProvisionCode() {
        return serviceProvisionCode;
    }

    /**
     * Does this service have specific eligibility requirements that need to be met in order to use the service?
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Eligibility} that may be empty.
     */
    public List<Eligibility> getEligibility() {
        return eligibility;
    }

    /**
     * Programs that this service is applicable to.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getProgram() {
        return program;
    }

    /**
     * Collection of characteristics (attributes).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCharacteristic() {
        return characteristic;
    }

    /**
     * Some services are specifically made available in multiple languages, this property permits a directory to declare the 
     * languages this is offered in. Typically this is only provided where a service operates in communities with mixed 
     * languages used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCommunication() {
        return communication;
    }

    /**
     * Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReferralMethod() {
        return referralMethod;
    }

    /**
     * Indicates whether or not a prospective consumer will require an appointment for a particular service at a site to be 
     * provided by the Organization. Indicates if an appointment is required for access to this service.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getAppointmentRequired() {
        return appointmentRequired;
    }

    /**
     * A collection of times that the Service Site is available.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link AvailableTime} that may be empty.
     */
    public List<AvailableTime> getAvailableTime() {
        return availableTime;
    }

    /**
     * The HealthcareService is not available during this period of time due to the provided reason.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link NotAvailable} that may be empty.
     */
    public List<NotAvailable> getNotAvailable() {
        return notAvailable;
    }

    /**
     * A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible 
     * exceptions to normal site availability as details in the available Times and not available Times.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getAvailabilityExceptions() {
        return availabilityExceptions;
    }

    /**
     * Technical endpoints providing access to services operated for the specific healthcare services defined at this 
     * resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getEndpoint() {
        return endpoint;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (active != null) || 
            (providedBy != null) || 
            !category.isEmpty() || 
            !type.isEmpty() || 
            !specialty.isEmpty() || 
            !location.isEmpty() || 
            (name != null) || 
            (comment != null) || 
            (extraDetails != null) || 
            (photo != null) || 
            !telecom.isEmpty() || 
            !coverageArea.isEmpty() || 
            !serviceProvisionCode.isEmpty() || 
            !eligibility.isEmpty() || 
            !program.isEmpty() || 
            !characteristic.isEmpty() || 
            !communication.isEmpty() || 
            !referralMethod.isEmpty() || 
            (appointmentRequired != null) || 
            !availableTime.isEmpty() || 
            !notAvailable.isEmpty() || 
            (availabilityExceptions != null) || 
            !endpoint.isEmpty();
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
                accept(active, "active", visitor);
                accept(providedBy, "providedBy", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(type, "type", visitor, CodeableConcept.class);
                accept(specialty, "specialty", visitor, CodeableConcept.class);
                accept(location, "location", visitor, Reference.class);
                accept(name, "name", visitor);
                accept(comment, "comment", visitor);
                accept(extraDetails, "extraDetails", visitor);
                accept(photo, "photo", visitor);
                accept(telecom, "telecom", visitor, ContactPoint.class);
                accept(coverageArea, "coverageArea", visitor, Reference.class);
                accept(serviceProvisionCode, "serviceProvisionCode", visitor, CodeableConcept.class);
                accept(eligibility, "eligibility", visitor, Eligibility.class);
                accept(program, "program", visitor, CodeableConcept.class);
                accept(characteristic, "characteristic", visitor, CodeableConcept.class);
                accept(communication, "communication", visitor, CodeableConcept.class);
                accept(referralMethod, "referralMethod", visitor, CodeableConcept.class);
                accept(appointmentRequired, "appointmentRequired", visitor);
                accept(availableTime, "availableTime", visitor, AvailableTime.class);
                accept(notAvailable, "notAvailable", visitor, NotAvailable.class);
                accept(availabilityExceptions, "availabilityExceptions", visitor);
                accept(endpoint, "endpoint", visitor, Reference.class);
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
        HealthcareService other = (HealthcareService) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(active, other.active) && 
            Objects.equals(providedBy, other.providedBy) && 
            Objects.equals(category, other.category) && 
            Objects.equals(type, other.type) && 
            Objects.equals(specialty, other.specialty) && 
            Objects.equals(location, other.location) && 
            Objects.equals(name, other.name) && 
            Objects.equals(comment, other.comment) && 
            Objects.equals(extraDetails, other.extraDetails) && 
            Objects.equals(photo, other.photo) && 
            Objects.equals(telecom, other.telecom) && 
            Objects.equals(coverageArea, other.coverageArea) && 
            Objects.equals(serviceProvisionCode, other.serviceProvisionCode) && 
            Objects.equals(eligibility, other.eligibility) && 
            Objects.equals(program, other.program) && 
            Objects.equals(characteristic, other.characteristic) && 
            Objects.equals(communication, other.communication) && 
            Objects.equals(referralMethod, other.referralMethod) && 
            Objects.equals(appointmentRequired, other.appointmentRequired) && 
            Objects.equals(availableTime, other.availableTime) && 
            Objects.equals(notAvailable, other.notAvailable) && 
            Objects.equals(availabilityExceptions, other.availabilityExceptions) && 
            Objects.equals(endpoint, other.endpoint);
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
                active, 
                providedBy, 
                category, 
                type, 
                specialty, 
                location, 
                name, 
                comment, 
                extraDetails, 
                photo, 
                telecom, 
                coverageArea, 
                serviceProvisionCode, 
                eligibility, 
                program, 
                characteristic, 
                communication, 
                referralMethod, 
                appointmentRequired, 
                availableTime, 
                notAvailable, 
                availabilityExceptions, 
                endpoint);
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
        private Boolean active;
        private Reference providedBy;
        private List<CodeableConcept> category = new ArrayList<>();
        private List<CodeableConcept> type = new ArrayList<>();
        private List<CodeableConcept> specialty = new ArrayList<>();
        private List<Reference> location = new ArrayList<>();
        private String name;
        private String comment;
        private Markdown extraDetails;
        private Attachment photo;
        private List<ContactPoint> telecom = new ArrayList<>();
        private List<Reference> coverageArea = new ArrayList<>();
        private List<CodeableConcept> serviceProvisionCode = new ArrayList<>();
        private List<Eligibility> eligibility = new ArrayList<>();
        private List<CodeableConcept> program = new ArrayList<>();
        private List<CodeableConcept> characteristic = new ArrayList<>();
        private List<CodeableConcept> communication = new ArrayList<>();
        private List<CodeableConcept> referralMethod = new ArrayList<>();
        private Boolean appointmentRequired;
        private List<AvailableTime> availableTime = new ArrayList<>();
        private List<NotAvailable> notAvailable = new ArrayList<>();
        private String availabilityExceptions;
        private List<Reference> endpoint = new ArrayList<>();

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
         * External identifiers for this item.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External identifiers for this item
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
         * External identifiers for this item.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External identifiers for this item
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
         * Convenience method for setting {@code active}.
         * 
         * @param active
         *     Whether this HealthcareService record is in active use
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #active(com.ibm.fhir.model.type.Boolean)
         */
        public Builder active(java.lang.Boolean active) {
            this.active = (active == null) ? null : Boolean.of(active);
            return this;
        }

        /**
         * This flag is used to mark the record to not be used. This is not used when a center is closed for maintenance, or for 
         * holidays, the notAvailable period is to be used for this.
         * 
         * @param active
         *     Whether this HealthcareService record is in active use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        /**
         * The organization that provides this healthcare service.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param providedBy
         *     Organization that provides this service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder providedBy(Reference providedBy) {
            this.providedBy = providedBy;
            return this;
        }

        /**
         * Identifies the broad category of service being performed or delivered.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Broad category of service being performed or delivered
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
         * Identifies the broad category of service being performed or delivered.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Broad category of service being performed or delivered
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
         * The specific type of service that may be delivered or performed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param type
         *     Type of service that may be delivered or performed
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
         * The specific type of service that may be delivered or performed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param type
         *     Type of service that may be delivered or performed
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
         * Collection of specialties handled by the service site. This is more of a medical term.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialty
         *     Specialties handled by the HealthcareService
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
         * Collection of specialties handled by the service site. This is more of a medical term.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialty
         *     Specialties handled by the HealthcareService
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
         * The location(s) where this healthcare service may be provided.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param location
         *     Location(s) where service may be provided
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(Reference... location) {
            for (Reference value : location) {
                this.location.add(value);
            }
            return this;
        }

        /**
         * The location(s) where this healthcare service may be provided.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param location
         *     Location(s) where service may be provided
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder location(Collection<Reference> location) {
            this.location = new ArrayList<>(location);
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     Description of service as presented to a consumer while searching
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
         * Further description of the service as it would be presented to a consumer while searching.
         * 
         * @param name
         *     Description of service as presented to a consumer while searching
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Convenience method for setting {@code comment}.
         * 
         * @param comment
         *     Additional description and/or any specific issues not covered elsewhere
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
         * Any additional description of the service and/or any specific issues not covered by the other attributes, which can be 
         * displayed as further detail under the serviceName.
         * 
         * @param comment
         *     Additional description and/or any specific issues not covered elsewhere
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Extra details about the service that can't be placed in the other fields.
         * 
         * @param extraDetails
         *     Extra details about the service that can't be placed in the other fields
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder extraDetails(Markdown extraDetails) {
            this.extraDetails = extraDetails;
            return this;
        }

        /**
         * If there is a photo/symbol associated with this HealthcareService, it may be included here to facilitate quick 
         * identification of the service in a list.
         * 
         * @param photo
         *     Facilitates quick identification of the service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder photo(Attachment photo) {
            this.photo = photo;
            return this;
        }

        /**
         * List of contacts related to this specific healthcare service.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param telecom
         *     Contacts related to the healthcare service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder telecom(ContactPoint... telecom) {
            for (ContactPoint value : telecom) {
                this.telecom.add(value);
            }
            return this;
        }

        /**
         * List of contacts related to this specific healthcare service.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param telecom
         *     Contacts related to the healthcare service
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder telecom(Collection<ContactPoint> telecom) {
            this.telecom = new ArrayList<>(telecom);
            return this;
        }

        /**
         * The location(s) that this service is available to (not where the service is provided).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param coverageArea
         *     Location(s) service is intended for/available to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder coverageArea(Reference... coverageArea) {
            for (Reference value : coverageArea) {
                this.coverageArea.add(value);
            }
            return this;
        }

        /**
         * The location(s) that this service is available to (not where the service is provided).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param coverageArea
         *     Location(s) service is intended for/available to
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder coverageArea(Collection<Reference> coverageArea) {
            this.coverageArea = new ArrayList<>(coverageArea);
            return this;
        }

        /**
         * The code(s) that detail the conditions under which the healthcare service is available/offered.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param serviceProvisionCode
         *     Conditions under which service is available/offered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviceProvisionCode(CodeableConcept... serviceProvisionCode) {
            for (CodeableConcept value : serviceProvisionCode) {
                this.serviceProvisionCode.add(value);
            }
            return this;
        }

        /**
         * The code(s) that detail the conditions under which the healthcare service is available/offered.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param serviceProvisionCode
         *     Conditions under which service is available/offered
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder serviceProvisionCode(Collection<CodeableConcept> serviceProvisionCode) {
            this.serviceProvisionCode = new ArrayList<>(serviceProvisionCode);
            return this;
        }

        /**
         * Does this service have specific eligibility requirements that need to be met in order to use the service?
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param eligibility
         *     Specific eligibility requirements required to use the service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder eligibility(Eligibility... eligibility) {
            for (Eligibility value : eligibility) {
                this.eligibility.add(value);
            }
            return this;
        }

        /**
         * Does this service have specific eligibility requirements that need to be met in order to use the service?
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param eligibility
         *     Specific eligibility requirements required to use the service
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder eligibility(Collection<Eligibility> eligibility) {
            this.eligibility = new ArrayList<>(eligibility);
            return this;
        }

        /**
         * Programs that this service is applicable to.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param program
         *     Programs that this service is applicable to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder program(CodeableConcept... program) {
            for (CodeableConcept value : program) {
                this.program.add(value);
            }
            return this;
        }

        /**
         * Programs that this service is applicable to.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param program
         *     Programs that this service is applicable to
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder program(Collection<CodeableConcept> program) {
            this.program = new ArrayList<>(program);
            return this;
        }

        /**
         * Collection of characteristics (attributes).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristic
         *     Collection of characteristics (attributes)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder characteristic(CodeableConcept... characteristic) {
            for (CodeableConcept value : characteristic) {
                this.characteristic.add(value);
            }
            return this;
        }

        /**
         * Collection of characteristics (attributes).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristic
         *     Collection of characteristics (attributes)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder characteristic(Collection<CodeableConcept> characteristic) {
            this.characteristic = new ArrayList<>(characteristic);
            return this;
        }

        /**
         * Some services are specifically made available in multiple languages, this property permits a directory to declare the 
         * languages this is offered in. Typically this is only provided where a service operates in communities with mixed 
         * languages used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param communication
         *     The language that this service is offered in
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder communication(CodeableConcept... communication) {
            for (CodeableConcept value : communication) {
                this.communication.add(value);
            }
            return this;
        }

        /**
         * Some services are specifically made available in multiple languages, this property permits a directory to declare the 
         * languages this is offered in. Typically this is only provided where a service operates in communities with mixed 
         * languages used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param communication
         *     The language that this service is offered in
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder communication(Collection<CodeableConcept> communication) {
            this.communication = new ArrayList<>(communication);
            return this;
        }

        /**
         * Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param referralMethod
         *     Ways that the service accepts referrals
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referralMethod(CodeableConcept... referralMethod) {
            for (CodeableConcept value : referralMethod) {
                this.referralMethod.add(value);
            }
            return this;
        }

        /**
         * Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param referralMethod
         *     Ways that the service accepts referrals
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder referralMethod(Collection<CodeableConcept> referralMethod) {
            this.referralMethod = new ArrayList<>(referralMethod);
            return this;
        }

        /**
         * Convenience method for setting {@code appointmentRequired}.
         * 
         * @param appointmentRequired
         *     If an appointment is required for access to this service
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #appointmentRequired(com.ibm.fhir.model.type.Boolean)
         */
        public Builder appointmentRequired(java.lang.Boolean appointmentRequired) {
            this.appointmentRequired = (appointmentRequired == null) ? null : Boolean.of(appointmentRequired);
            return this;
        }

        /**
         * Indicates whether or not a prospective consumer will require an appointment for a particular service at a site to be 
         * provided by the Organization. Indicates if an appointment is required for access to this service.
         * 
         * @param appointmentRequired
         *     If an appointment is required for access to this service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder appointmentRequired(Boolean appointmentRequired) {
            this.appointmentRequired = appointmentRequired;
            return this;
        }

        /**
         * A collection of times that the Service Site is available.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param availableTime
         *     Times the Service Site is available
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder availableTime(AvailableTime... availableTime) {
            for (AvailableTime value : availableTime) {
                this.availableTime.add(value);
            }
            return this;
        }

        /**
         * A collection of times that the Service Site is available.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param availableTime
         *     Times the Service Site is available
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder availableTime(Collection<AvailableTime> availableTime) {
            this.availableTime = new ArrayList<>(availableTime);
            return this;
        }

        /**
         * The HealthcareService is not available during this period of time due to the provided reason.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param notAvailable
         *     Not available during this time due to provided reason
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder notAvailable(NotAvailable... notAvailable) {
            for (NotAvailable value : notAvailable) {
                this.notAvailable.add(value);
            }
            return this;
        }

        /**
         * The HealthcareService is not available during this period of time due to the provided reason.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param notAvailable
         *     Not available during this time due to provided reason
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder notAvailable(Collection<NotAvailable> notAvailable) {
            this.notAvailable = new ArrayList<>(notAvailable);
            return this;
        }

        /**
         * Convenience method for setting {@code availabilityExceptions}.
         * 
         * @param availabilityExceptions
         *     Description of availability exceptions
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #availabilityExceptions(com.ibm.fhir.model.type.String)
         */
        public Builder availabilityExceptions(java.lang.String availabilityExceptions) {
            this.availabilityExceptions = (availabilityExceptions == null) ? null : String.of(availabilityExceptions);
            return this;
        }

        /**
         * A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible 
         * exceptions to normal site availability as details in the available Times and not available Times.
         * 
         * @param availabilityExceptions
         *     Description of availability exceptions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder availabilityExceptions(String availabilityExceptions) {
            this.availabilityExceptions = availabilityExceptions;
            return this;
        }

        /**
         * Technical endpoints providing access to services operated for the specific healthcare services defined at this 
         * resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Endpoint}</li>
         * </ul>
         * 
         * @param endpoint
         *     Technical endpoints providing access to electronic services operated for the healthcare service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder endpoint(Reference... endpoint) {
            for (Reference value : endpoint) {
                this.endpoint.add(value);
            }
            return this;
        }

        /**
         * Technical endpoints providing access to services operated for the specific healthcare services defined at this 
         * resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Endpoint}</li>
         * </ul>
         * 
         * @param endpoint
         *     Technical endpoints providing access to electronic services operated for the healthcare service
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder endpoint(Collection<Reference> endpoint) {
            this.endpoint = new ArrayList<>(endpoint);
            return this;
        }

        /**
         * Build the {@link HealthcareService}
         * 
         * @return
         *     An immutable object of type {@link HealthcareService}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid HealthcareService per the base specification
         */
        @Override
        public HealthcareService build() {
            HealthcareService healthcareService = new HealthcareService(this);
            if (validating) {
                validate(healthcareService);
            }
            return healthcareService;
        }

        protected void validate(HealthcareService healthcareService) {
            super.validate(healthcareService);
            ValidationSupport.checkList(healthcareService.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(healthcareService.category, "category", CodeableConcept.class);
            ValidationSupport.checkList(healthcareService.type, "type", CodeableConcept.class);
            ValidationSupport.checkList(healthcareService.specialty, "specialty", CodeableConcept.class);
            ValidationSupport.checkList(healthcareService.location, "location", Reference.class);
            ValidationSupport.checkList(healthcareService.telecom, "telecom", ContactPoint.class);
            ValidationSupport.checkList(healthcareService.coverageArea, "coverageArea", Reference.class);
            ValidationSupport.checkList(healthcareService.serviceProvisionCode, "serviceProvisionCode", CodeableConcept.class);
            ValidationSupport.checkList(healthcareService.eligibility, "eligibility", Eligibility.class);
            ValidationSupport.checkList(healthcareService.program, "program", CodeableConcept.class);
            ValidationSupport.checkList(healthcareService.characteristic, "characteristic", CodeableConcept.class);
            ValidationSupport.checkList(healthcareService.communication, "communication", CodeableConcept.class);
            ValidationSupport.checkList(healthcareService.referralMethod, "referralMethod", CodeableConcept.class);
            ValidationSupport.checkList(healthcareService.availableTime, "availableTime", AvailableTime.class);
            ValidationSupport.checkList(healthcareService.notAvailable, "notAvailable", NotAvailable.class);
            ValidationSupport.checkList(healthcareService.endpoint, "endpoint", Reference.class);
            ValidationSupport.checkValueSetBinding(healthcareService.communication, "communication", "http://hl7.org/fhir/ValueSet/all-languages", "urn:ietf:bcp:47");
            ValidationSupport.checkReferenceType(healthcareService.providedBy, "providedBy", "Organization");
            ValidationSupport.checkReferenceType(healthcareService.location, "location", "Location");
            ValidationSupport.checkReferenceType(healthcareService.coverageArea, "coverageArea", "Location");
            ValidationSupport.checkReferenceType(healthcareService.endpoint, "endpoint", "Endpoint");
        }

        protected Builder from(HealthcareService healthcareService) {
            super.from(healthcareService);
            identifier.addAll(healthcareService.identifier);
            active = healthcareService.active;
            providedBy = healthcareService.providedBy;
            category.addAll(healthcareService.category);
            type.addAll(healthcareService.type);
            specialty.addAll(healthcareService.specialty);
            location.addAll(healthcareService.location);
            name = healthcareService.name;
            comment = healthcareService.comment;
            extraDetails = healthcareService.extraDetails;
            photo = healthcareService.photo;
            telecom.addAll(healthcareService.telecom);
            coverageArea.addAll(healthcareService.coverageArea);
            serviceProvisionCode.addAll(healthcareService.serviceProvisionCode);
            eligibility.addAll(healthcareService.eligibility);
            program.addAll(healthcareService.program);
            characteristic.addAll(healthcareService.characteristic);
            communication.addAll(healthcareService.communication);
            referralMethod.addAll(healthcareService.referralMethod);
            appointmentRequired = healthcareService.appointmentRequired;
            availableTime.addAll(healthcareService.availableTime);
            notAvailable.addAll(healthcareService.notAvailable);
            availabilityExceptions = healthcareService.availabilityExceptions;
            endpoint.addAll(healthcareService.endpoint);
            return this;
        }
    }

    /**
     * Does this service have specific eligibility requirements that need to be met in order to use the service?
     */
    public static class Eligibility extends BackboneElement {
        @Binding(
            bindingName = "ServiceEligibility",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Coded values underwhich a specific service is made available."
        )
        private final CodeableConcept code;
        private final Markdown comment;

        private Eligibility(Builder builder) {
            super(builder);
            code = builder.code;
            comment = builder.comment;
        }

        /**
         * Coded value for the eligibility.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * Describes the eligibility conditions for the service.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getComment() {
            return comment;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (comment != null);
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
                    accept(code, "code", visitor);
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
            Eligibility other = (Eligibility) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(comment, other.comment);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
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

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept code;
            private Markdown comment;

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
             * Coded value for the eligibility.
             * 
             * @param code
             *     Coded value for the eligibility
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Describes the eligibility conditions for the service.
             * 
             * @param comment
             *     Describes the eligibility conditions for the service
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder comment(Markdown comment) {
                this.comment = comment;
                return this;
            }

            /**
             * Build the {@link Eligibility}
             * 
             * @return
             *     An immutable object of type {@link Eligibility}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Eligibility per the base specification
             */
            @Override
            public Eligibility build() {
                Eligibility eligibility = new Eligibility(this);
                if (validating) {
                    validate(eligibility);
                }
                return eligibility;
            }

            protected void validate(Eligibility eligibility) {
                super.validate(eligibility);
                ValidationSupport.requireValueOrChildren(eligibility);
            }

            protected Builder from(Eligibility eligibility) {
                super.from(eligibility);
                code = eligibility.code;
                comment = eligibility.comment;
                return this;
            }
        }
    }

    /**
     * A collection of times that the Service Site is available.
     */
    public static class AvailableTime extends BackboneElement {
        @Binding(
            bindingName = "DaysOfWeek",
            strength = BindingStrength.Value.REQUIRED,
            description = "The days of the week.",
            valueSet = "http://hl7.org/fhir/ValueSet/days-of-week|4.1.0"
        )
        private final List<DaysOfWeek> daysOfWeek;
        private final Boolean allDay;
        private final Time availableStartTime;
        private final Time availableEndTime;

        private AvailableTime(Builder builder) {
            super(builder);
            daysOfWeek = Collections.unmodifiableList(builder.daysOfWeek);
            allDay = builder.allDay;
            availableStartTime = builder.availableStartTime;
            availableEndTime = builder.availableEndTime;
        }

        /**
         * Indicates which days of the week are available between the start and end Times.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DaysOfWeek} that may be empty.
         */
        public List<DaysOfWeek> getDaysOfWeek() {
            return daysOfWeek;
        }

        /**
         * Is this always available? (hence times are irrelevant) e.g. 24 hour service.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getAllDay() {
            return allDay;
        }

        /**
         * The opening time of day. Note: If the AllDay flag is set, then this time is ignored.
         * 
         * @return
         *     An immutable object of type {@link Time} that may be null.
         */
        public Time getAvailableStartTime() {
            return availableStartTime;
        }

        /**
         * The closing time of day. Note: If the AllDay flag is set, then this time is ignored.
         * 
         * @return
         *     An immutable object of type {@link Time} that may be null.
         */
        public Time getAvailableEndTime() {
            return availableEndTime;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !daysOfWeek.isEmpty() || 
                (allDay != null) || 
                (availableStartTime != null) || 
                (availableEndTime != null);
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
                    accept(daysOfWeek, "daysOfWeek", visitor, DaysOfWeek.class);
                    accept(allDay, "allDay", visitor);
                    accept(availableStartTime, "availableStartTime", visitor);
                    accept(availableEndTime, "availableEndTime", visitor);
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
            AvailableTime other = (AvailableTime) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(daysOfWeek, other.daysOfWeek) && 
                Objects.equals(allDay, other.allDay) && 
                Objects.equals(availableStartTime, other.availableStartTime) && 
                Objects.equals(availableEndTime, other.availableEndTime);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    daysOfWeek, 
                    allDay, 
                    availableStartTime, 
                    availableEndTime);
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
            private List<DaysOfWeek> daysOfWeek = new ArrayList<>();
            private Boolean allDay;
            private Time availableStartTime;
            private Time availableEndTime;

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
             * Indicates which days of the week are available between the start and end Times.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param daysOfWeek
             *     mon | tue | wed | thu | fri | sat | sun
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder daysOfWeek(DaysOfWeek... daysOfWeek) {
                for (DaysOfWeek value : daysOfWeek) {
                    this.daysOfWeek.add(value);
                }
                return this;
            }

            /**
             * Indicates which days of the week are available between the start and end Times.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param daysOfWeek
             *     mon | tue | wed | thu | fri | sat | sun
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder daysOfWeek(Collection<DaysOfWeek> daysOfWeek) {
                this.daysOfWeek = new ArrayList<>(daysOfWeek);
                return this;
            }

            /**
             * Convenience method for setting {@code allDay}.
             * 
             * @param allDay
             *     Always available? e.g. 24 hour service
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #allDay(com.ibm.fhir.model.type.Boolean)
             */
            public Builder allDay(java.lang.Boolean allDay) {
                this.allDay = (allDay == null) ? null : Boolean.of(allDay);
                return this;
            }

            /**
             * Is this always available? (hence times are irrelevant) e.g. 24 hour service.
             * 
             * @param allDay
             *     Always available? e.g. 24 hour service
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder allDay(Boolean allDay) {
                this.allDay = allDay;
                return this;
            }

            /**
             * Convenience method for setting {@code availableStartTime}.
             * 
             * @param availableStartTime
             *     Opening time of day (ignored if allDay = true)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #availableStartTime(com.ibm.fhir.model.type.Time)
             */
            public Builder availableStartTime(java.time.LocalTime availableStartTime) {
                this.availableStartTime = (availableStartTime == null) ? null : Time.of(availableStartTime);
                return this;
            }

            /**
             * The opening time of day. Note: If the AllDay flag is set, then this time is ignored.
             * 
             * @param availableStartTime
             *     Opening time of day (ignored if allDay = true)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder availableStartTime(Time availableStartTime) {
                this.availableStartTime = availableStartTime;
                return this;
            }

            /**
             * Convenience method for setting {@code availableEndTime}.
             * 
             * @param availableEndTime
             *     Closing time of day (ignored if allDay = true)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #availableEndTime(com.ibm.fhir.model.type.Time)
             */
            public Builder availableEndTime(java.time.LocalTime availableEndTime) {
                this.availableEndTime = (availableEndTime == null) ? null : Time.of(availableEndTime);
                return this;
            }

            /**
             * The closing time of day. Note: If the AllDay flag is set, then this time is ignored.
             * 
             * @param availableEndTime
             *     Closing time of day (ignored if allDay = true)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder availableEndTime(Time availableEndTime) {
                this.availableEndTime = availableEndTime;
                return this;
            }

            /**
             * Build the {@link AvailableTime}
             * 
             * @return
             *     An immutable object of type {@link AvailableTime}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid AvailableTime per the base specification
             */
            @Override
            public AvailableTime build() {
                AvailableTime availableTime = new AvailableTime(this);
                if (validating) {
                    validate(availableTime);
                }
                return availableTime;
            }

            protected void validate(AvailableTime availableTime) {
                super.validate(availableTime);
                ValidationSupport.checkList(availableTime.daysOfWeek, "daysOfWeek", DaysOfWeek.class);
                ValidationSupport.requireValueOrChildren(availableTime);
            }

            protected Builder from(AvailableTime availableTime) {
                super.from(availableTime);
                daysOfWeek.addAll(availableTime.daysOfWeek);
                allDay = availableTime.allDay;
                availableStartTime = availableTime.availableStartTime;
                availableEndTime = availableTime.availableEndTime;
                return this;
            }
        }
    }

    /**
     * The HealthcareService is not available during this period of time due to the provided reason.
     */
    public static class NotAvailable extends BackboneElement {
        @Required
        private final String description;
        private final Period during;

        private NotAvailable(Builder builder) {
            super(builder);
            description = builder.description;
            during = builder.during;
        }

        /**
         * The reason that can be presented to the user as to why this time is not available.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Service is not available (seasonally or for a public holiday) from this date.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getDuring() {
            return during;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (during != null);
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
                    accept(description, "description", visitor);
                    accept(during, "during", visitor);
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
            NotAvailable other = (NotAvailable) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(during, other.during);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    during);
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
            private String description;
            private Period during;

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
             * Convenience method for setting {@code description}.
             * 
             * <p>This element is required.
             * 
             * @param description
             *     Reason presented to the user explaining why time not available
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
             * The reason that can be presented to the user as to why this time is not available.
             * 
             * <p>This element is required.
             * 
             * @param description
             *     Reason presented to the user explaining why time not available
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Service is not available (seasonally or for a public holiday) from this date.
             * 
             * @param during
             *     Service not available from this date
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder during(Period during) {
                this.during = during;
                return this;
            }

            /**
             * Build the {@link NotAvailable}
             * 
             * <p>Required elements:
             * <ul>
             * <li>description</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link NotAvailable}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid NotAvailable per the base specification
             */
            @Override
            public NotAvailable build() {
                NotAvailable notAvailable = new NotAvailable(this);
                if (validating) {
                    validate(notAvailable);
                }
                return notAvailable;
            }

            protected void validate(NotAvailable notAvailable) {
                super.validate(notAvailable);
                ValidationSupport.requireNonNull(notAvailable.description, "description");
                ValidationSupport.requireValueOrChildren(notAvailable);
            }

            protected Builder from(NotAvailable notAvailable) {
                super.from(notAvailable);
                description = notAvailable.description;
                during = notAvailable.during;
                return this;
            }
        }
    }
}
