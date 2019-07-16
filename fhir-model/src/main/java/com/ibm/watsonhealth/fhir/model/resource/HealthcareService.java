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

import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.DaysOfWeek;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The details of a healthcare service available at a location.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class HealthcareService extends DomainResource {
    private final List<Identifier> identifier;
    private final Boolean active;
    private final Reference providedBy;
    private final List<CodeableConcept> category;
    private final List<CodeableConcept> type;
    private final List<CodeableConcept> specialty;
    private final List<Reference> location;
    private final String name;
    private final String comment;
    private final Markdown extraDetails;
    private final Attachment photo;
    private final List<ContactPoint> telecom;
    private final List<Reference> coverageArea;
    private final List<CodeableConcept> serviceProvisionCode;
    private final List<Eligibility> eligibility;
    private final List<CodeableConcept> program;
    private final List<CodeableConcept> characteristic;
    private final List<CodeableConcept> communication;
    private final List<CodeableConcept> referralMethod;
    private final Boolean appointmentRequired;
    private final List<AvailableTime> availableTime;
    private final List<NotAvailable> notAvailable;
    private final String availabilityExceptions;
    private final List<Reference> endpoint;

    private volatile int hashCode;

    private HealthcareService(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        active = builder.active;
        providedBy = builder.providedBy;
        category = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.category, "category"));
        type = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.type, "type"));
        specialty = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.specialty, "specialty"));
        location = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.location, "location"));
        name = builder.name;
        comment = builder.comment;
        extraDetails = builder.extraDetails;
        photo = builder.photo;
        telecom = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.telecom, "telecom"));
        coverageArea = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.coverageArea, "coverageArea"));
        serviceProvisionCode = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.serviceProvisionCode, "serviceProvisionCode"));
        eligibility = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.eligibility, "eligibility"));
        program = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.program, "program"));
        characteristic = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.characteristic, "characteristic"));
        communication = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.communication, "communication"));
        referralMethod = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.referralMethod, "referralMethod"));
        appointmentRequired = builder.appointmentRequired;
        availableTime = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.availableTime, "availableTime"));
        notAvailable = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.notAvailable, "notAvailable"));
        availabilityExceptions = builder.availabilityExceptions;
        endpoint = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.endpoint, "endpoint"));
    }

    /**
     * <p>
     * External identifiers for this item.
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
     * This flag is used to mark the record to not be used. This is not used when a center is closed for maintenance, or for 
     * holidays, the notAvailable period is to be used for this.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * <p>
     * The organization that provides this healthcare service.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getProvidedBy() {
        return providedBy;
    }

    /**
     * <p>
     * Identifies the broad category of service being performed or delivered.
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
     * The specific type of service that may be delivered or performed.
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
     * Collection of specialties handled by the service site. This is more of a medical term.
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
     * The location(s) where this healthcare service may be provided.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getLocation() {
        return location;
    }

    /**
     * <p>
     * Further description of the service as it would be presented to a consumer while searching.
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
     * Any additional description of the service and/or any specific issues not covered by the other attributes, which can be 
     * displayed as further detail under the serviceName.
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
     * Extra details about the service that can't be placed in the other fields.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getExtraDetails() {
        return extraDetails;
    }

    /**
     * <p>
     * If there is a photo/symbol associated with this HealthcareService, it may be included here to facilitate quick 
     * identification of the service in a list.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Attachment}.
     */
    public Attachment getPhoto() {
        return photo;
    }

    /**
     * <p>
     * List of contacts related to this specific healthcare service.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactPoint}.
     */
    public List<ContactPoint> getTelecom() {
        return telecom;
    }

    /**
     * <p>
     * The location(s) that this service is available to (not where the service is provided).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getCoverageArea() {
        return coverageArea;
    }

    /**
     * <p>
     * The code(s) that detail the conditions under which the healthcare service is available/offered.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getServiceProvisionCode() {
        return serviceProvisionCode;
    }

    /**
     * <p>
     * Does this service have specific eligibility requirements that need to be met in order to use the service?
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Eligibility}.
     */
    public List<Eligibility> getEligibility() {
        return eligibility;
    }

    /**
     * <p>
     * Programs that this service is applicable to.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getProgram() {
        return program;
    }

    /**
     * <p>
     * Collection of characteristics (attributes).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCharacteristic() {
        return characteristic;
    }

    /**
     * <p>
     * Some services are specifically made available in multiple languages, this property permits a directory to declare the 
     * languages this is offered in. Typically this is only provided where a service operates in communities with mixed 
     * languages used.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCommunication() {
        return communication;
    }

    /**
     * <p>
     * Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getReferralMethod() {
        return referralMethod;
    }

    /**
     * <p>
     * Indicates whether or not a prospective consumer will require an appointment for a particular service at a site to be 
     * provided by the Organization. Indicates if an appointment is required for access to this service.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getAppointmentRequired() {
        return appointmentRequired;
    }

    /**
     * <p>
     * A collection of times that the Service Site is available.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link AvailableTime}.
     */
    public List<AvailableTime> getAvailableTime() {
        return availableTime;
    }

    /**
     * <p>
     * The HealthcareService is not available during this period of time due to the provided reason.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link NotAvailable}.
     */
    public List<NotAvailable> getNotAvailable() {
        return notAvailable;
    }

    /**
     * <p>
     * A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible 
     * exceptions to normal site availability as details in the available Times and not available Times.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getAvailabilityExceptions() {
        return availabilityExceptions;
    }

    /**
     * <p>
     * Technical endpoints providing access to services operated for the specific healthcare services defined at this 
     * resource.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getEndpoint() {
        return endpoint;
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
        // optional
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
         * External identifiers for this item.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * External identifiers for this item.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     External identifiers for this item
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
         * This flag is used to mark the record to not be used. This is not used when a center is closed for maintenance, or for 
         * holidays, the notAvailable period is to be used for this.
         * </p>
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
         * <p>
         * The organization that provides this healthcare service.
         * </p>
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
         * <p>
         * Identifies the broad category of service being performed or delivered.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Identifies the broad category of service being performed or delivered.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param category
         *     Broad category of service being performed or delivered
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
         * The specific type of service that may be delivered or performed.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The specific type of service that may be delivered or performed.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param type
         *     Type of service that may be delivered or performed
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
         * Collection of specialties handled by the service site. This is more of a medical term.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Collection of specialties handled by the service site. This is more of a medical term.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param specialty
         *     Specialties handled by the HealthcareService
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
         * The location(s) where this healthcare service may be provided.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The location(s) where this healthcare service may be provided.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param location
         *     Location(s) where service may be provided
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(Collection<Reference> location) {
            this.location = new ArrayList<>(location);
            return this;
        }

        /**
         * <p>
         * Further description of the service as it would be presented to a consumer while searching.
         * </p>
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
         * <p>
         * Any additional description of the service and/or any specific issues not covered by the other attributes, which can be 
         * displayed as further detail under the serviceName.
         * </p>
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
         * <p>
         * Extra details about the service that can't be placed in the other fields.
         * </p>
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
         * <p>
         * If there is a photo/symbol associated with this HealthcareService, it may be included here to facilitate quick 
         * identification of the service in a list.
         * </p>
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
         * <p>
         * List of contacts related to this specific healthcare service.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * List of contacts related to this specific healthcare service.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param telecom
         *     Contacts related to the healthcare service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder telecom(Collection<ContactPoint> telecom) {
            this.telecom = new ArrayList<>(telecom);
            return this;
        }

        /**
         * <p>
         * The location(s) that this service is available to (not where the service is provided).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The location(s) that this service is available to (not where the service is provided).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param coverageArea
         *     Location(s) service is intended for/available to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder coverageArea(Collection<Reference> coverageArea) {
            this.coverageArea = new ArrayList<>(coverageArea);
            return this;
        }

        /**
         * <p>
         * The code(s) that detail the conditions under which the healthcare service is available/offered.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The code(s) that detail the conditions under which the healthcare service is available/offered.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param serviceProvisionCode
         *     Conditions under which service is available/offered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviceProvisionCode(Collection<CodeableConcept> serviceProvisionCode) {
            this.serviceProvisionCode = new ArrayList<>(serviceProvisionCode);
            return this;
        }

        /**
         * <p>
         * Does this service have specific eligibility requirements that need to be met in order to use the service?
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Does this service have specific eligibility requirements that need to be met in order to use the service?
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param eligibility
         *     Specific eligibility requirements required to use the service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder eligibility(Collection<Eligibility> eligibility) {
            this.eligibility = new ArrayList<>(eligibility);
            return this;
        }

        /**
         * <p>
         * Programs that this service is applicable to.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Programs that this service is applicable to.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param program
         *     Programs that this service is applicable to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder program(Collection<CodeableConcept> program) {
            this.program = new ArrayList<>(program);
            return this;
        }

        /**
         * <p>
         * Collection of characteristics (attributes).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Collection of characteristics (attributes).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param characteristic
         *     Collection of characteristics (attributes)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder characteristic(Collection<CodeableConcept> characteristic) {
            this.characteristic = new ArrayList<>(characteristic);
            return this;
        }

        /**
         * <p>
         * Some services are specifically made available in multiple languages, this property permits a directory to declare the 
         * languages this is offered in. Typically this is only provided where a service operates in communities with mixed 
         * languages used.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Some services are specifically made available in multiple languages, this property permits a directory to declare the 
         * languages this is offered in. Typically this is only provided where a service operates in communities with mixed 
         * languages used.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param communication
         *     The language that this service is offered in
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder communication(Collection<CodeableConcept> communication) {
            this.communication = new ArrayList<>(communication);
            return this;
        }

        /**
         * <p>
         * Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param referralMethod
         *     Ways that the service accepts referrals
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referralMethod(Collection<CodeableConcept> referralMethod) {
            this.referralMethod = new ArrayList<>(referralMethod);
            return this;
        }

        /**
         * <p>
         * Indicates whether or not a prospective consumer will require an appointment for a particular service at a site to be 
         * provided by the Organization. Indicates if an appointment is required for access to this service.
         * </p>
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
         * <p>
         * A collection of times that the Service Site is available.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A collection of times that the Service Site is available.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param availableTime
         *     Times the Service Site is available
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder availableTime(Collection<AvailableTime> availableTime) {
            this.availableTime = new ArrayList<>(availableTime);
            return this;
        }

        /**
         * <p>
         * The HealthcareService is not available during this period of time due to the provided reason.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The HealthcareService is not available during this period of time due to the provided reason.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param notAvailable
         *     Not available during this time due to provided reason
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder notAvailable(Collection<NotAvailable> notAvailable) {
            this.notAvailable = new ArrayList<>(notAvailable);
            return this;
        }

        /**
         * <p>
         * A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible 
         * exceptions to normal site availability as details in the available Times and not available Times.
         * </p>
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
         * <p>
         * Technical endpoints providing access to services operated for the specific healthcare services defined at this 
         * resource.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Technical endpoints providing access to services operated for the specific healthcare services defined at this 
         * resource.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param endpoint
         *     Technical endpoints providing access to electronic services operated for the healthcare service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder endpoint(Collection<Reference> endpoint) {
            this.endpoint = new ArrayList<>(endpoint);
            return this;
        }

        @Override
        public HealthcareService build() {
            return new HealthcareService(this);
        }

        private Builder from(HealthcareService healthcareService) {
            id = healthcareService.id;
            meta = healthcareService.meta;
            implicitRules = healthcareService.implicitRules;
            language = healthcareService.language;
            text = healthcareService.text;
            contained.addAll(healthcareService.contained);
            extension.addAll(healthcareService.extension);
            modifierExtension.addAll(healthcareService.modifierExtension);
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
     * <p>
     * Does this service have specific eligibility requirements that need to be met in order to use the service?
     * </p>
     */
    public static class Eligibility extends BackboneElement {
        private final CodeableConcept code;
        private final Markdown comment;

        private volatile int hashCode;

        private Eligibility(Builder builder) {
            super(builder);
            code = builder.code;
            comment = builder.comment;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Coded value for the eligibility.
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
         * Describes the eligibility conditions for the service.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Markdown}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
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
            // optional
            private CodeableConcept code;
            private Markdown comment;

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
             * Coded value for the eligibility.
             * </p>
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
             * <p>
             * Describes the eligibility conditions for the service.
             * </p>
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

            @Override
            public Eligibility build() {
                return new Eligibility(this);
            }

            private Builder from(Eligibility eligibility) {
                id = eligibility.id;
                extension.addAll(eligibility.extension);
                modifierExtension.addAll(eligibility.modifierExtension);
                code = eligibility.code;
                comment = eligibility.comment;
                return this;
            }
        }
    }

    /**
     * <p>
     * A collection of times that the Service Site is available.
     * </p>
     */
    public static class AvailableTime extends BackboneElement {
        private final List<DaysOfWeek> daysOfWeek;
        private final Boolean allDay;
        private final Time availableStartTime;
        private final Time availableEndTime;

        private volatile int hashCode;

        private AvailableTime(Builder builder) {
            super(builder);
            daysOfWeek = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.daysOfWeek, "daysOfWeek"));
            allDay = builder.allDay;
            availableStartTime = builder.availableStartTime;
            availableEndTime = builder.availableEndTime;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Indicates which days of the week are available between the start and end Times.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DaysOfWeek}.
         */
        public List<DaysOfWeek> getDaysOfWeek() {
            return daysOfWeek;
        }

        /**
         * <p>
         * Is this always available? (hence times are irrelevant) e.g. 24 hour service.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getAllDay() {
            return allDay;
        }

        /**
         * <p>
         * The opening time of day. Note: If the AllDay flag is set, then this time is ignored.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Time}.
         */
        public Time getAvailableStartTime() {
            return availableStartTime;
        }

        /**
         * <p>
         * The closing time of day. Note: If the AllDay flag is set, then this time is ignored.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Time}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(daysOfWeek, "daysOfWeek", visitor, DaysOfWeek.class);
                    accept(allDay, "allDay", visitor);
                    accept(availableStartTime, "availableStartTime", visitor);
                    accept(availableEndTime, "availableEndTime", visitor);
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
            // optional
            private List<DaysOfWeek> daysOfWeek = new ArrayList<>();
            private Boolean allDay;
            private Time availableStartTime;
            private Time availableEndTime;

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
             * Indicates which days of the week are available between the start and end Times.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Indicates which days of the week are available between the start and end Times.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param daysOfWeek
             *     mon | tue | wed | thu | fri | sat | sun
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder daysOfWeek(Collection<DaysOfWeek> daysOfWeek) {
                this.daysOfWeek = new ArrayList<>(daysOfWeek);
                return this;
            }

            /**
             * <p>
             * Is this always available? (hence times are irrelevant) e.g. 24 hour service.
             * </p>
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
             * <p>
             * The opening time of day. Note: If the AllDay flag is set, then this time is ignored.
             * </p>
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
             * <p>
             * The closing time of day. Note: If the AllDay flag is set, then this time is ignored.
             * </p>
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

            @Override
            public AvailableTime build() {
                return new AvailableTime(this);
            }

            private Builder from(AvailableTime availableTime) {
                id = availableTime.id;
                extension.addAll(availableTime.extension);
                modifierExtension.addAll(availableTime.modifierExtension);
                daysOfWeek.addAll(availableTime.daysOfWeek);
                allDay = availableTime.allDay;
                availableStartTime = availableTime.availableStartTime;
                availableEndTime = availableTime.availableEndTime;
                return this;
            }
        }
    }

    /**
     * <p>
     * The HealthcareService is not available during this period of time due to the provided reason.
     * </p>
     */
    public static class NotAvailable extends BackboneElement {
        private final String description;
        private final Period during;

        private volatile int hashCode;

        private NotAvailable(Builder builder) {
            super(builder);
            description = ValidationSupport.requireNonNull(builder.description, "description");
            during = builder.during;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The reason that can be presented to the user as to why this time is not available.
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
         * Service is not available (seasonally or for a public holiday) from this date.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(description, "description", visitor);
                    accept(during, "during", visitor);
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
            return new Builder(description).from(this);
        }

        public Builder toBuilder(String description) {
            return new Builder(description).from(this);
        }

        public static Builder builder(String description) {
            return new Builder(description);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String description;

            // optional
            private Period during;

            private Builder(String description) {
                super();
                this.description = description;
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
             * Service is not available (seasonally or for a public holiday) from this date.
             * </p>
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

            @Override
            public NotAvailable build() {
                return new NotAvailable(this);
            }

            private Builder from(NotAvailable notAvailable) {
                id = notAvailable.id;
                extension.addAll(notAvailable.extension);
                modifierExtension.addAll(notAvailable.modifierExtension);
                during = notAvailable.during;
                return this;
            }
        }
    }
}
