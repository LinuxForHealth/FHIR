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
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of 
 * relationship/sub-division relationship.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class OrganizationAffiliation extends DomainResource {
    private final List<Identifier> identifier;
    private final Boolean active;
    private final Period period;
    private final Reference organization;
    private final Reference participatingOrganization;
    private final List<Reference> network;
    private final List<CodeableConcept> code;
    private final List<CodeableConcept> specialty;
    private final List<Reference> location;
    private final List<Reference> healthcareService;
    private final List<ContactPoint> telecom;
    private final List<Reference> endpoint;

    private volatile int hashCode;

    private OrganizationAffiliation(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        active = builder.active;
        period = builder.period;
        organization = builder.organization;
        participatingOrganization = builder.participatingOrganization;
        network = Collections.unmodifiableList(builder.network);
        code = Collections.unmodifiableList(builder.code);
        specialty = Collections.unmodifiableList(builder.specialty);
        location = Collections.unmodifiableList(builder.location);
        healthcareService = Collections.unmodifiableList(builder.healthcareService);
        telecom = Collections.unmodifiableList(builder.telecom);
        endpoint = Collections.unmodifiableList(builder.endpoint);
    }

    /**
     * <p>
     * Business identifiers that are specific to this role.
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
     * Whether this organization affiliation record is in active use.
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
     * The period during which the participatingOrganization is affiliated with the primary organization.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * <p>
     * Organization where the role is available (primary organization/has members).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOrganization() {
        return organization;
    }

    /**
     * <p>
     * The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. 
     * providing services or is a member of).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getParticipatingOrganization() {
        return participatingOrganization;
    }

    /**
     * <p>
     * Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at 
     * the indicated locations (if defined).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getNetwork() {
        return network;
    }

    /**
     * <p>
     * Definition of the role the participatingOrganization plays in the association.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCode() {
        return code;
    }

    /**
     * <p>
     * Specific specialty of the participatingOrganization in the context of the role.
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
     * The location(s) at which the role occurs.
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
     * Healthcare services provided through the role.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getHealthcareService() {
        return healthcareService;
    }

    /**
     * <p>
     * Contact details at the participatingOrganization relevant to this Affiliation.
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
     * Technical endpoints providing access to services operated for this role.
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
                accept(period, "period", visitor);
                accept(organization, "organization", visitor);
                accept(participatingOrganization, "participatingOrganization", visitor);
                accept(network, "network", visitor, Reference.class);
                accept(code, "code", visitor, CodeableConcept.class);
                accept(specialty, "specialty", visitor, CodeableConcept.class);
                accept(location, "location", visitor, Reference.class);
                accept(healthcareService, "healthcareService", visitor, Reference.class);
                accept(telecom, "telecom", visitor, ContactPoint.class);
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
        OrganizationAffiliation other = (OrganizationAffiliation) obj;
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
            Objects.equals(period, other.period) && 
            Objects.equals(organization, other.organization) && 
            Objects.equals(participatingOrganization, other.participatingOrganization) && 
            Objects.equals(network, other.network) && 
            Objects.equals(code, other.code) && 
            Objects.equals(specialty, other.specialty) && 
            Objects.equals(location, other.location) && 
            Objects.equals(healthcareService, other.healthcareService) && 
            Objects.equals(telecom, other.telecom) && 
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
                period, 
                organization, 
                participatingOrganization, 
                network, 
                code, 
                specialty, 
                location, 
                healthcareService, 
                telecom, 
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
        private Period period;
        private Reference organization;
        private Reference participatingOrganization;
        private List<Reference> network = new ArrayList<>();
        private List<CodeableConcept> code = new ArrayList<>();
        private List<CodeableConcept> specialty = new ArrayList<>();
        private List<Reference> location = new ArrayList<>();
        private List<Reference> healthcareService = new ArrayList<>();
        private List<ContactPoint> telecom = new ArrayList<>();
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
         * Business identifiers that are specific to this role.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     Business identifiers that are specific to this role
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
         * Business identifiers that are specific to this role.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Business identifiers that are specific to this role
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
         * Whether this organization affiliation record is in active use.
         * </p>
         * 
         * @param active
         *     Whether this organization affiliation record is in active use
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
         * The period during which the participatingOrganization is affiliated with the primary organization.
         * </p>
         * 
         * @param period
         *     The period during which the participatingOrganization is affiliated with the primary organization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * <p>
         * Organization where the role is available (primary organization/has members).
         * </p>
         * 
         * @param organization
         *     Organization where the role is available
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder organization(Reference organization) {
            this.organization = organization;
            return this;
        }

        /**
         * <p>
         * The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. 
         * providing services or is a member of).
         * </p>
         * 
         * @param participatingOrganization
         *     Organization that provides/performs the role (e.g. providing services or is a member of)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder participatingOrganization(Reference participatingOrganization) {
            this.participatingOrganization = participatingOrganization;
            return this;
        }

        /**
         * <p>
         * Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at 
         * the indicated locations (if defined).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param network
         *     Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at 
         *     the indicated locations (if defined)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder network(Reference... network) {
            for (Reference value : network) {
                this.network.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at 
         * the indicated locations (if defined).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param network
         *     Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at 
         *     the indicated locations (if defined)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder network(Collection<Reference> network) {
            this.network = new ArrayList<>(network);
            return this;
        }

        /**
         * <p>
         * Definition of the role the participatingOrganization plays in the association.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param code
         *     Definition of the role the participatingOrganization plays
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept... code) {
            for (CodeableConcept value : code) {
                this.code.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Definition of the role the participatingOrganization plays in the association.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param code
         *     Definition of the role the participatingOrganization plays
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Collection<CodeableConcept> code) {
            this.code = new ArrayList<>(code);
            return this;
        }

        /**
         * <p>
         * Specific specialty of the participatingOrganization in the context of the role.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param specialty
         *     Specific specialty of the participatingOrganization in the context of the role
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
         * Specific specialty of the participatingOrganization in the context of the role.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param specialty
         *     Specific specialty of the participatingOrganization in the context of the role
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
         * The location(s) at which the role occurs.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param location
         *     The location(s) at which the role occurs
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
         * The location(s) at which the role occurs.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param location
         *     The location(s) at which the role occurs
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
         * Healthcare services provided through the role.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param healthcareService
         *     Healthcare services provided through the role
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder healthcareService(Reference... healthcareService) {
            for (Reference value : healthcareService) {
                this.healthcareService.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Healthcare services provided through the role.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param healthcareService
         *     Healthcare services provided through the role
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder healthcareService(Collection<Reference> healthcareService) {
            this.healthcareService = new ArrayList<>(healthcareService);
            return this;
        }

        /**
         * <p>
         * Contact details at the participatingOrganization relevant to this Affiliation.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param telecom
         *     Contact details at the participatingOrganization relevant to this Affiliation
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
         * Contact details at the participatingOrganization relevant to this Affiliation.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param telecom
         *     Contact details at the participatingOrganization relevant to this Affiliation
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
         * Technical endpoints providing access to services operated for this role.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param endpoint
         *     Technical endpoints providing access to services operated for this role
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
         * Technical endpoints providing access to services operated for this role.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param endpoint
         *     Technical endpoints providing access to services operated for this role
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder endpoint(Collection<Reference> endpoint) {
            this.endpoint = new ArrayList<>(endpoint);
            return this;
        }

        @Override
        public OrganizationAffiliation build() {
            return new OrganizationAffiliation(this);
        }

        private Builder from(OrganizationAffiliation organizationAffiliation) {
            id = organizationAffiliation.id;
            meta = organizationAffiliation.meta;
            implicitRules = organizationAffiliation.implicitRules;
            language = organizationAffiliation.language;
            text = organizationAffiliation.text;
            contained.addAll(organizationAffiliation.contained);
            extension.addAll(organizationAffiliation.extension);
            modifierExtension.addAll(organizationAffiliation.modifierExtension);
            identifier.addAll(organizationAffiliation.identifier);
            active = organizationAffiliation.active;
            period = organizationAffiliation.period;
            organization = organizationAffiliation.organization;
            participatingOrganization = organizationAffiliation.participatingOrganization;
            network.addAll(organizationAffiliation.network);
            code.addAll(organizationAffiliation.code);
            specialty.addAll(organizationAffiliation.specialty);
            location.addAll(organizationAffiliation.location);
            healthcareService.addAll(organizationAffiliation.healthcareService);
            telecom.addAll(organizationAffiliation.telecom);
            endpoint.addAll(organizationAffiliation.endpoint);
            return this;
        }
    }
}
