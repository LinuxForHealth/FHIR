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
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of 
 * relationship/sub-division relationship.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "organizationAffiliation-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/c80-practice-codes",
    expression = "specialty.exists() implies (specialty.all(memberOf('http://hl7.org/fhir/ValueSet/c80-practice-codes', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/OrganizationAffiliation",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class OrganizationAffiliation extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final Boolean active;
    @Summary
    private final Period period;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference organization;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference participatingOrganization;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final List<Reference> network;
    @Summary
    @Binding(
        bindingName = "OrganizationAffiliation",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The role the participating organization providing services to the primary organization.",
        valueSet = "http://hl7.org/fhir/ValueSet/organization-role"
    )
    private final List<CodeableConcept> code;
    @Summary
    @Binding(
        bindingName = "OrganizationSpecialty",
        strength = BindingStrength.Value.PREFERRED,
        description = "Specific specialty associated with the participating organization.",
        valueSet = "http://hl7.org/fhir/ValueSet/c80-practice-codes"
    )
    private final List<CodeableConcept> specialty;
    @Summary
    @ReferenceTarget({ "Location" })
    private final List<Reference> location;
    @ReferenceTarget({ "HealthcareService" })
    private final List<Reference> healthcareService;
    @Summary
    private final List<ContactPoint> telecom;
    @ReferenceTarget({ "Endpoint" })
    private final List<Reference> endpoint;

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
     * Business identifiers that are specific to this role.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Whether this organization affiliation record is in active use.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * The period during which the participatingOrganization is affiliated with the primary organization.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Organization where the role is available (primary organization/has members).
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getOrganization() {
        return organization;
    }

    /**
     * The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. 
     * providing services or is a member of).
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getParticipatingOrganization() {
        return participatingOrganization;
    }

    /**
     * Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at 
     * the indicated locations (if defined).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getNetwork() {
        return network;
    }

    /**
     * Definition of the role the participatingOrganization plays in the association.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCode() {
        return code;
    }

    /**
     * Specific specialty of the participatingOrganization in the context of the role.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getSpecialty() {
        return specialty;
    }

    /**
     * The location(s) at which the role occurs.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getLocation() {
        return location;
    }

    /**
     * Healthcare services provided through the role.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getHealthcareService() {
        return healthcareService;
    }

    /**
     * Contact details at the participatingOrganization relevant to this Affiliation.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactPoint} that may be empty.
     */
    public List<ContactPoint> getTelecom() {
        return telecom;
    }

    /**
     * Technical endpoints providing access to services operated for this role.
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
            (period != null) || 
            (organization != null) || 
            (participatingOrganization != null) || 
            !network.isEmpty() || 
            !code.isEmpty() || 
            !specialty.isEmpty() || 
            !location.isEmpty() || 
            !healthcareService.isEmpty() || 
            !telecom.isEmpty() || 
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
         * Business identifiers that are specific to this role.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Business identifiers that are specific to this role.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifiers that are specific to this role
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
         *     Whether this organization affiliation record is in active use
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
         * Whether this organization affiliation record is in active use.
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
         * The period during which the participatingOrganization is affiliated with the primary organization.
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
         * Organization where the role is available (primary organization/has members).
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
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
         * The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. 
         * providing services or is a member of).
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
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
         * Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at 
         * the indicated locations (if defined).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
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
         * Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at 
         * the indicated locations (if defined).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param network
         *     Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at 
         *     the indicated locations (if defined)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder network(Collection<Reference> network) {
            this.network = new ArrayList<>(network);
            return this;
        }

        /**
         * Definition of the role the participatingOrganization plays in the association.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Definition of the role the participatingOrganization plays in the association.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param code
         *     Definition of the role the participatingOrganization plays
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder code(Collection<CodeableConcept> code) {
            this.code = new ArrayList<>(code);
            return this;
        }

        /**
         * Specific specialty of the participatingOrganization in the context of the role.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Specific specialty of the participatingOrganization in the context of the role.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialty
         *     Specific specialty of the participatingOrganization in the context of the role
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
         * The location(s) at which the role occurs.
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
         * The location(s) at which the role occurs.
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
         *     The location(s) at which the role occurs
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
         * Healthcare services provided through the role.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link HealthcareService}</li>
         * </ul>
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
         * Healthcare services provided through the role.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link HealthcareService}</li>
         * </ul>
         * 
         * @param healthcareService
         *     Healthcare services provided through the role
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder healthcareService(Collection<Reference> healthcareService) {
            this.healthcareService = new ArrayList<>(healthcareService);
            return this;
        }

        /**
         * Contact details at the participatingOrganization relevant to this Affiliation.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Contact details at the participatingOrganization relevant to this Affiliation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param telecom
         *     Contact details at the participatingOrganization relevant to this Affiliation
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
         * Technical endpoints providing access to services operated for this role.
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
         * Technical endpoints providing access to services operated for this role.
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
         *     Technical endpoints providing access to services operated for this role
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
         * Build the {@link OrganizationAffiliation}
         * 
         * @return
         *     An immutable object of type {@link OrganizationAffiliation}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid OrganizationAffiliation per the base specification
         */
        @Override
        public OrganizationAffiliation build() {
            OrganizationAffiliation organizationAffiliation = new OrganizationAffiliation(this);
            if (validating) {
                validate(organizationAffiliation);
            }
            return organizationAffiliation;
        }

        protected void validate(OrganizationAffiliation organizationAffiliation) {
            super.validate(organizationAffiliation);
            ValidationSupport.checkList(organizationAffiliation.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(organizationAffiliation.network, "network", Reference.class);
            ValidationSupport.checkList(organizationAffiliation.code, "code", CodeableConcept.class);
            ValidationSupport.checkList(organizationAffiliation.specialty, "specialty", CodeableConcept.class);
            ValidationSupport.checkList(organizationAffiliation.location, "location", Reference.class);
            ValidationSupport.checkList(organizationAffiliation.healthcareService, "healthcareService", Reference.class);
            ValidationSupport.checkList(organizationAffiliation.telecom, "telecom", ContactPoint.class);
            ValidationSupport.checkList(organizationAffiliation.endpoint, "endpoint", Reference.class);
            ValidationSupport.checkReferenceType(organizationAffiliation.organization, "organization", "Organization");
            ValidationSupport.checkReferenceType(organizationAffiliation.participatingOrganization, "participatingOrganization", "Organization");
            ValidationSupport.checkReferenceType(organizationAffiliation.network, "network", "Organization");
            ValidationSupport.checkReferenceType(organizationAffiliation.location, "location", "Location");
            ValidationSupport.checkReferenceType(organizationAffiliation.healthcareService, "healthcareService", "HealthcareService");
            ValidationSupport.checkReferenceType(organizationAffiliation.endpoint, "endpoint", "Endpoint");
        }

        protected Builder from(OrganizationAffiliation organizationAffiliation) {
            super.from(organizationAffiliation);
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
