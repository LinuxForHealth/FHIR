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
import com.ibm.watsonhealth.fhir.model.type.Address;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Details of a Health Insurance product/plan provided by an organization.
 * </p>
 */
@Constraint(
    id = "ipn-1",
    level = "Rule",
    location = "(base)",
    description = "The organization SHALL at least have a name or an idendtifier, and possibly more than one",
    expression = "(identifier.count() + name.count()) > 0"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class InsurancePlan extends DomainResource {
    private final List<Identifier> identifier;
    private final PublicationStatus status;
    private final List<CodeableConcept> type;
    private final String name;
    private final List<String> alias;
    private final Period period;
    private final Reference ownedBy;
    private final Reference administeredBy;
    private final List<Reference> coverageArea;
    private final List<Contact> contact;
    private final List<Reference> endpoint;
    private final List<Reference> network;
    private final List<Coverage> coverage;
    private final List<Plan> plan;

    private volatile int hashCode;

    private InsurancePlan(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        type = Collections.unmodifiableList(builder.type);
        name = builder.name;
        alias = Collections.unmodifiableList(builder.alias);
        period = builder.period;
        ownedBy = builder.ownedBy;
        administeredBy = builder.administeredBy;
        coverageArea = Collections.unmodifiableList(builder.coverageArea);
        contact = Collections.unmodifiableList(builder.contact);
        endpoint = Collections.unmodifiableList(builder.endpoint);
        network = Collections.unmodifiableList(builder.network);
        coverage = Collections.unmodifiableList(builder.coverage);
        plan = Collections.unmodifiableList(builder.plan);
    }

    /**
     * <p>
     * Business identifiers assigned to this health insurance product which remain constant as the resource is updated and 
     * propagates from server to server.
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
     * The current state of the health insurance product.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus}.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The kind of health insurance product.
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
     * Official name of the health insurance product as designated by the owner.
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
     * A list of alternate names that the product is known as, or was known as in the past.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String}.
     */
    public List<String> getAlias() {
        return alias;
    }

    /**
     * <p>
     * The period of time that the health insurance product is available.
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
     * The entity that is providing the health insurance product and underwriting the risk. This is typically an insurance 
     * carriers, other third-party payers, or health plan sponsors comonly referred to as 'payers'.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOwnedBy() {
        return ownedBy;
    }

    /**
     * <p>
     * An organization which administer other services such as underwriting, customer service and/or claims processing on 
     * behalf of the health insurance product owner.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAdministeredBy() {
        return administeredBy;
    }

    /**
     * <p>
     * The geographic region in which a health insurance product's benefits apply.
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
     * The contact for the health insurance product for a certain purpose.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Contact}.
     */
    public List<Contact> getContact() {
        return contact;
    }

    /**
     * <p>
     * The technical endpoints providing access to services operated for the health insurance product.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getEndpoint() {
        return endpoint;
    }

    /**
     * <p>
     * Reference to the network included in the health insurance product.
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
     * Details about the coverage offered by the insurance product.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coverage}.
     */
    public List<Coverage> getCoverage() {
        return coverage;
    }

    /**
     * <p>
     * Details about an insurance plan.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Plan}.
     */
    public List<Plan> getPlan() {
        return plan;
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
                accept(type, "type", visitor, CodeableConcept.class);
                accept(name, "name", visitor);
                accept(alias, "alias", visitor, String.class);
                accept(period, "period", visitor);
                accept(ownedBy, "ownedBy", visitor);
                accept(administeredBy, "administeredBy", visitor);
                accept(coverageArea, "coverageArea", visitor, Reference.class);
                accept(contact, "contact", visitor, Contact.class);
                accept(endpoint, "endpoint", visitor, Reference.class);
                accept(network, "network", visitor, Reference.class);
                accept(coverage, "coverage", visitor, Coverage.class);
                accept(plan, "plan", visitor, Plan.class);
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
        InsurancePlan other = (InsurancePlan) obj;
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
            Objects.equals(type, other.type) && 
            Objects.equals(name, other.name) && 
            Objects.equals(alias, other.alias) && 
            Objects.equals(period, other.period) && 
            Objects.equals(ownedBy, other.ownedBy) && 
            Objects.equals(administeredBy, other.administeredBy) && 
            Objects.equals(coverageArea, other.coverageArea) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(endpoint, other.endpoint) && 
            Objects.equals(network, other.network) && 
            Objects.equals(coverage, other.coverage) && 
            Objects.equals(plan, other.plan);
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
                type, 
                name, 
                alias, 
                period, 
                ownedBy, 
                administeredBy, 
                coverageArea, 
                contact, 
                endpoint, 
                network, 
                coverage, 
                plan);
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
        private PublicationStatus status;
        private List<CodeableConcept> type = new ArrayList<>();
        private String name;
        private List<String> alias = new ArrayList<>();
        private Period period;
        private Reference ownedBy;
        private Reference administeredBy;
        private List<Reference> coverageArea = new ArrayList<>();
        private List<Contact> contact = new ArrayList<>();
        private List<Reference> endpoint = new ArrayList<>();
        private List<Reference> network = new ArrayList<>();
        private List<Coverage> coverage = new ArrayList<>();
        private List<Plan> plan = new ArrayList<>();

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
         * Business identifiers assigned to this health insurance product which remain constant as the resource is updated and 
         * propagates from server to server.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     Business Identifier for Product
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
         * Business identifiers assigned to this health insurance product which remain constant as the resource is updated and 
         * propagates from server to server.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Business Identifier for Product
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
         * The current state of the health insurance product.
         * </p>
         * 
         * @param status
         *     draft | active | retired | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(PublicationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * The kind of health insurance product.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param type
         *     Kind of product
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
         * The kind of health insurance product.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param type
         *     Kind of product
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
         * Official name of the health insurance product as designated by the owner.
         * </p>
         * 
         * @param name
         *     Official name
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
         * A list of alternate names that the product is known as, or was known as in the past.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param alias
         *     Alternate names
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder alias(String... alias) {
            for (String value : alias) {
                this.alias.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A list of alternate names that the product is known as, or was known as in the past.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param alias
         *     Alternate names
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder alias(Collection<String> alias) {
            this.alias = new ArrayList<>(alias);
            return this;
        }

        /**
         * <p>
         * The period of time that the health insurance product is available.
         * </p>
         * 
         * @param period
         *     When the product is available
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
         * The entity that is providing the health insurance product and underwriting the risk. This is typically an insurance 
         * carriers, other third-party payers, or health plan sponsors comonly referred to as 'payers'.
         * </p>
         * 
         * @param ownedBy
         *     Plan issuer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder ownedBy(Reference ownedBy) {
            this.ownedBy = ownedBy;
            return this;
        }

        /**
         * <p>
         * An organization which administer other services such as underwriting, customer service and/or claims processing on 
         * behalf of the health insurance product owner.
         * </p>
         * 
         * @param administeredBy
         *     Product administrator
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder administeredBy(Reference administeredBy) {
            this.administeredBy = administeredBy;
            return this;
        }

        /**
         * <p>
         * The geographic region in which a health insurance product's benefits apply.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param coverageArea
         *     Where product applies
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
         * The geographic region in which a health insurance product's benefits apply.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param coverageArea
         *     Where product applies
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
         * The contact for the health insurance product for a certain purpose.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param contact
         *     Contact for the product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Contact... contact) {
            for (Contact value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The contact for the health insurance product for a certain purpose.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contact
         *     Contact for the product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Collection<Contact> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * <p>
         * The technical endpoints providing access to services operated for the health insurance product.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param endpoint
         *     Technical endpoint
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
         * The technical endpoints providing access to services operated for the health insurance product.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param endpoint
         *     Technical endpoint
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder endpoint(Collection<Reference> endpoint) {
            this.endpoint = new ArrayList<>(endpoint);
            return this;
        }

        /**
         * <p>
         * Reference to the network included in the health insurance product.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param network
         *     What networks are Included
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
         * Reference to the network included in the health insurance product.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param network
         *     What networks are Included
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
         * Details about the coverage offered by the insurance product.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param coverage
         *     Coverage details
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder coverage(Coverage... coverage) {
            for (Coverage value : coverage) {
                this.coverage.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Details about the coverage offered by the insurance product.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param coverage
         *     Coverage details
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder coverage(Collection<Coverage> coverage) {
            this.coverage = new ArrayList<>(coverage);
            return this;
        }

        /**
         * <p>
         * Details about an insurance plan.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param plan
         *     Plan details
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder plan(Plan... plan) {
            for (Plan value : plan) {
                this.plan.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Details about an insurance plan.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param plan
         *     Plan details
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder plan(Collection<Plan> plan) {
            this.plan = new ArrayList<>(plan);
            return this;
        }

        @Override
        public InsurancePlan build() {
            return new InsurancePlan(this);
        }

        private Builder from(InsurancePlan insurancePlan) {
            id = insurancePlan.id;
            meta = insurancePlan.meta;
            implicitRules = insurancePlan.implicitRules;
            language = insurancePlan.language;
            text = insurancePlan.text;
            contained.addAll(insurancePlan.contained);
            extension.addAll(insurancePlan.extension);
            modifierExtension.addAll(insurancePlan.modifierExtension);
            identifier.addAll(insurancePlan.identifier);
            status = insurancePlan.status;
            type.addAll(insurancePlan.type);
            name = insurancePlan.name;
            alias.addAll(insurancePlan.alias);
            period = insurancePlan.period;
            ownedBy = insurancePlan.ownedBy;
            administeredBy = insurancePlan.administeredBy;
            coverageArea.addAll(insurancePlan.coverageArea);
            contact.addAll(insurancePlan.contact);
            endpoint.addAll(insurancePlan.endpoint);
            network.addAll(insurancePlan.network);
            coverage.addAll(insurancePlan.coverage);
            plan.addAll(insurancePlan.plan);
            return this;
        }
    }

    /**
     * <p>
     * The contact for the health insurance product for a certain purpose.
     * </p>
     */
    public static class Contact extends BackboneElement {
        private final CodeableConcept purpose;
        private final HumanName name;
        private final List<ContactPoint> telecom;
        private final Address address;

        private volatile int hashCode;

        private Contact(Builder builder) {
            super(builder);
            purpose = builder.purpose;
            name = builder.name;
            telecom = Collections.unmodifiableList(builder.telecom);
            address = builder.address;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Indicates a purpose for which the contact can be reached.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getPurpose() {
            return purpose;
        }

        /**
         * <p>
         * A name associated with the contact.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link HumanName}.
         */
        public HumanName getName() {
            return name;
        }

        /**
         * <p>
         * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
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
         * Visiting or postal addresses for the contact.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Address}.
         */
        public Address getAddress() {
            return address;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (purpose != null) || 
                (name != null) || 
                !telecom.isEmpty() || 
                (address != null);
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
                    accept(purpose, "purpose", visitor);
                    accept(name, "name", visitor);
                    accept(telecom, "telecom", visitor, ContactPoint.class);
                    accept(address, "address", visitor);
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
            Contact other = (Contact) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(purpose, other.purpose) && 
                Objects.equals(name, other.name) && 
                Objects.equals(telecom, other.telecom) && 
                Objects.equals(address, other.address);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    purpose, 
                    name, 
                    telecom, 
                    address);
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
            private CodeableConcept purpose;
            private HumanName name;
            private List<ContactPoint> telecom = new ArrayList<>();
            private Address address;

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
             * Indicates a purpose for which the contact can be reached.
             * </p>
             * 
             * @param purpose
             *     The type of contact
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder purpose(CodeableConcept purpose) {
                this.purpose = purpose;
                return this;
            }

            /**
             * <p>
             * A name associated with the contact.
             * </p>
             * 
             * @param name
             *     A name associated with the contact
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(HumanName name) {
                this.name = name;
                return this;
            }

            /**
             * <p>
             * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param telecom
             *     Contact details (telephone, email, etc.) for a contact
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
             * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param telecom
             *     Contact details (telephone, email, etc.) for a contact
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
             * Visiting or postal addresses for the contact.
             * </p>
             * 
             * @param address
             *     Visiting or postal addresses for the contact
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder address(Address address) {
                this.address = address;
                return this;
            }

            @Override
            public Contact build() {
                return new Contact(this);
            }

            private Builder from(Contact contact) {
                id = contact.id;
                extension.addAll(contact.extension);
                modifierExtension.addAll(contact.modifierExtension);
                purpose = contact.purpose;
                name = contact.name;
                telecom.addAll(contact.telecom);
                address = contact.address;
                return this;
            }
        }
    }

    /**
     * <p>
     * Details about the coverage offered by the insurance product.
     * </p>
     */
    public static class Coverage extends BackboneElement {
        private final CodeableConcept type;
        private final List<Reference> network;
        private final List<Benefit> benefit;

        private volatile int hashCode;

        private Coverage(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            network = Collections.unmodifiableList(builder.network);
            benefit = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.benefit, "benefit"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Type of coverage (Medical; Dental; Mental Health; Substance Abuse; Vision; Drug; Short Term; Long Term Care; Hospice; 
         * Home Health).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * <p>
         * Reference to the network that providing the type of coverage.
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
         * Specific benefits under this type of coverage.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Benefit}.
         */
        public List<Benefit> getBenefit() {
            return benefit;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                !network.isEmpty() || 
                !benefit.isEmpty();
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
                    accept(type, "type", visitor);
                    accept(network, "network", visitor, Reference.class);
                    accept(benefit, "benefit", visitor, Benefit.class);
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
            Coverage other = (Coverage) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(network, other.network) && 
                Objects.equals(benefit, other.benefit);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    network, 
                    benefit);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(type, benefit).from(this);
        }

        public Builder toBuilder(CodeableConcept type, List<Benefit> benefit) {
            return new Builder(type, benefit).from(this);
        }

        public static Builder builder(CodeableConcept type, List<Benefit> benefit) {
            return new Builder(type, benefit);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;
            private final List<Benefit> benefit;

            // optional
            private List<Reference> network = new ArrayList<>();

            private Builder(CodeableConcept type, List<Benefit> benefit) {
                super();
                this.type = type;
                this.benefit = new ArrayList<>(benefit);
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
             * Reference to the network that providing the type of coverage.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param network
             *     What networks provide coverage
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
             * Reference to the network that providing the type of coverage.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param network
             *     What networks provide coverage
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder network(Collection<Reference> network) {
                this.network = new ArrayList<>(network);
                return this;
            }

            @Override
            public Coverage build() {
                return new Coverage(this);
            }

            private Builder from(Coverage coverage) {
                id = coverage.id;
                extension.addAll(coverage.extension);
                modifierExtension.addAll(coverage.modifierExtension);
                network.addAll(coverage.network);
                return this;
            }
        }

        /**
         * <p>
         * Specific benefits under this type of coverage.
         * </p>
         */
        public static class Benefit extends BackboneElement {
            private final CodeableConcept type;
            private final String requirement;
            private final List<Limit> limit;

            private volatile int hashCode;

            private Benefit(Builder builder) {
                super(builder);
                type = ValidationSupport.requireNonNull(builder.type, "type");
                requirement = builder.requirement;
                limit = Collections.unmodifiableList(builder.limit);
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Type of benefit (primary care; speciality care; inpatient; outpatient).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * <p>
             * The referral requirements to have access/coverage for this benefit.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getRequirement() {
                return requirement;
            }

            /**
             * <p>
             * The specific limits on the benefit.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Limit}.
             */
            public List<Limit> getLimit() {
                return limit;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (requirement != null) || 
                    !limit.isEmpty();
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
                        accept(type, "type", visitor);
                        accept(requirement, "requirement", visitor);
                        accept(limit, "limit", visitor, Limit.class);
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
                Benefit other = (Benefit) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(requirement, other.requirement) && 
                    Objects.equals(limit, other.limit);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        requirement, 
                        limit);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(type).from(this);
            }

            public Builder toBuilder(CodeableConcept type) {
                return new Builder(type).from(this);
            }

            public static Builder builder(CodeableConcept type) {
                return new Builder(type);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept type;

                // optional
                private String requirement;
                private List<Limit> limit = new ArrayList<>();

                private Builder(CodeableConcept type) {
                    super();
                    this.type = type;
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
                 * The referral requirements to have access/coverage for this benefit.
                 * </p>
                 * 
                 * @param requirement
                 *     Referral requirements
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder requirement(String requirement) {
                    this.requirement = requirement;
                    return this;
                }

                /**
                 * <p>
                 * The specific limits on the benefit.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param limit
                 *     Benefit limits
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder limit(Limit... limit) {
                    for (Limit value : limit) {
                        this.limit.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The specific limits on the benefit.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param limit
                 *     Benefit limits
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder limit(Collection<Limit> limit) {
                    this.limit = new ArrayList<>(limit);
                    return this;
                }

                @Override
                public Benefit build() {
                    return new Benefit(this);
                }

                private Builder from(Benefit benefit) {
                    id = benefit.id;
                    extension.addAll(benefit.extension);
                    modifierExtension.addAll(benefit.modifierExtension);
                    requirement = benefit.requirement;
                    limit.addAll(benefit.limit);
                    return this;
                }
            }

            /**
             * <p>
             * The specific limits on the benefit.
             * </p>
             */
            public static class Limit extends BackboneElement {
                private final Quantity value;
                private final CodeableConcept code;

                private volatile int hashCode;

                private Limit(Builder builder) {
                    super(builder);
                    value = builder.value;
                    code = builder.code;
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * <p>
                 * The maximum amount of a service item a plan will pay for a covered benefit. For examples. wellness visits, or 
                 * eyeglasses.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Quantity}.
                 */
                public Quantity getValue() {
                    return value;
                }

                /**
                 * <p>
                 * The specific limit on the benefit.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getCode() {
                    return code;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (value != null) || 
                        (code != null);
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
                            accept(value, "value", visitor);
                            accept(code, "code", visitor);
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
                    Limit other = (Limit) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(value, other.value) && 
                        Objects.equals(code, other.code);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            value, 
                            code);
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
                    private Quantity value;
                    private CodeableConcept code;

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
                     * The maximum amount of a service item a plan will pay for a covered benefit. For examples. wellness visits, or 
                     * eyeglasses.
                     * </p>
                     * 
                     * @param value
                     *     Maximum value allowed
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder value(Quantity value) {
                        this.value = value;
                        return this;
                    }

                    /**
                     * <p>
                     * The specific limit on the benefit.
                     * </p>
                     * 
                     * @param code
                     *     Benefit limit details
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder code(CodeableConcept code) {
                        this.code = code;
                        return this;
                    }

                    @Override
                    public Limit build() {
                        return new Limit(this);
                    }

                    private Builder from(Limit limit) {
                        id = limit.id;
                        extension.addAll(limit.extension);
                        modifierExtension.addAll(limit.modifierExtension);
                        value = limit.value;
                        code = limit.code;
                        return this;
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Details about an insurance plan.
     * </p>
     */
    public static class Plan extends BackboneElement {
        private final List<Identifier> identifier;
        private final CodeableConcept type;
        private final List<Reference> coverageArea;
        private final List<Reference> network;
        private final List<GeneralCost> generalCost;
        private final List<SpecificCost> specificCost;

        private volatile int hashCode;

        private Plan(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(builder.identifier);
            type = builder.type;
            coverageArea = Collections.unmodifiableList(builder.coverageArea);
            network = Collections.unmodifiableList(builder.network);
            generalCost = Collections.unmodifiableList(builder.generalCost);
            specificCost = Collections.unmodifiableList(builder.specificCost);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Business identifiers assigned to this health insurance plan which remain constant as the resource is updated and 
         * propagates from server to server.
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
         * Type of plan. For example, "Platinum" or "High Deductable".
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * <p>
         * The geographic region in which a health insurance plan's benefits apply.
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
         * Reference to the network that providing the type of coverage.
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
         * Overall costs associated with the plan.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link GeneralCost}.
         */
        public List<GeneralCost> getGeneralCost() {
            return generalCost;
        }

        /**
         * <p>
         * Costs associated with the coverage provided by the product.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link SpecificCost}.
         */
        public List<SpecificCost> getSpecificCost() {
            return specificCost;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !identifier.isEmpty() || 
                (type != null) || 
                !coverageArea.isEmpty() || 
                !network.isEmpty() || 
                !generalCost.isEmpty() || 
                !specificCost.isEmpty();
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
                    accept(identifier, "identifier", visitor, Identifier.class);
                    accept(type, "type", visitor);
                    accept(coverageArea, "coverageArea", visitor, Reference.class);
                    accept(network, "network", visitor, Reference.class);
                    accept(generalCost, "generalCost", visitor, GeneralCost.class);
                    accept(specificCost, "specificCost", visitor, SpecificCost.class);
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
            Plan other = (Plan) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(type, other.type) && 
                Objects.equals(coverageArea, other.coverageArea) && 
                Objects.equals(network, other.network) && 
                Objects.equals(generalCost, other.generalCost) && 
                Objects.equals(specificCost, other.specificCost);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    type, 
                    coverageArea, 
                    network, 
                    generalCost, 
                    specificCost);
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
            private List<Identifier> identifier = new ArrayList<>();
            private CodeableConcept type;
            private List<Reference> coverageArea = new ArrayList<>();
            private List<Reference> network = new ArrayList<>();
            private List<GeneralCost> generalCost = new ArrayList<>();
            private List<SpecificCost> specificCost = new ArrayList<>();

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
             * Business identifiers assigned to this health insurance plan which remain constant as the resource is updated and 
             * propagates from server to server.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param identifier
             *     Business Identifier for Product
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
             * Business identifiers assigned to this health insurance plan which remain constant as the resource is updated and 
             * propagates from server to server.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param identifier
             *     Business Identifier for Product
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
             * Type of plan. For example, "Platinum" or "High Deductable".
             * </p>
             * 
             * @param type
             *     Type of plan
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * <p>
             * The geographic region in which a health insurance plan's benefits apply.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param coverageArea
             *     Where product applies
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
             * The geographic region in which a health insurance plan's benefits apply.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param coverageArea
             *     Where product applies
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
             * Reference to the network that providing the type of coverage.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param network
             *     What networks provide coverage
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
             * Reference to the network that providing the type of coverage.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param network
             *     What networks provide coverage
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
             * Overall costs associated with the plan.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param generalCost
             *     Overall costs
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder generalCost(GeneralCost... generalCost) {
                for (GeneralCost value : generalCost) {
                    this.generalCost.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Overall costs associated with the plan.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param generalCost
             *     Overall costs
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder generalCost(Collection<GeneralCost> generalCost) {
                this.generalCost = new ArrayList<>(generalCost);
                return this;
            }

            /**
             * <p>
             * Costs associated with the coverage provided by the product.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param specificCost
             *     Specific costs
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder specificCost(SpecificCost... specificCost) {
                for (SpecificCost value : specificCost) {
                    this.specificCost.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Costs associated with the coverage provided by the product.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param specificCost
             *     Specific costs
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder specificCost(Collection<SpecificCost> specificCost) {
                this.specificCost = new ArrayList<>(specificCost);
                return this;
            }

            @Override
            public Plan build() {
                return new Plan(this);
            }

            private Builder from(Plan plan) {
                id = plan.id;
                extension.addAll(plan.extension);
                modifierExtension.addAll(plan.modifierExtension);
                identifier.addAll(plan.identifier);
                type = plan.type;
                coverageArea.addAll(plan.coverageArea);
                network.addAll(plan.network);
                generalCost.addAll(plan.generalCost);
                specificCost.addAll(plan.specificCost);
                return this;
            }
        }

        /**
         * <p>
         * Overall costs associated with the plan.
         * </p>
         */
        public static class GeneralCost extends BackboneElement {
            private final CodeableConcept type;
            private final PositiveInt groupSize;
            private final Money cost;
            private final String comment;

            private volatile int hashCode;

            private GeneralCost(Builder builder) {
                super(builder);
                type = builder.type;
                groupSize = builder.groupSize;
                cost = builder.cost;
                comment = builder.comment;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Type of cost.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * <p>
             * Number of participants enrolled in the plan.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link PositiveInt}.
             */
            public PositiveInt getGroupSize() {
                return groupSize;
            }

            /**
             * <p>
             * Value of the cost.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Money}.
             */
            public Money getCost() {
                return cost;
            }

            /**
             * <p>
             * Additional information about the general costs associated with this plan.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getComment() {
                return comment;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (groupSize != null) || 
                    (cost != null) || 
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
                        accept(type, "type", visitor);
                        accept(groupSize, "groupSize", visitor);
                        accept(cost, "cost", visitor);
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
                GeneralCost other = (GeneralCost) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(groupSize, other.groupSize) && 
                    Objects.equals(cost, other.cost) && 
                    Objects.equals(comment, other.comment);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        groupSize, 
                        cost, 
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
                private CodeableConcept type;
                private PositiveInt groupSize;
                private Money cost;
                private String comment;

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
                 * Type of cost.
                 * </p>
                 * 
                 * @param type
                 *     Type of cost
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * <p>
                 * Number of participants enrolled in the plan.
                 * </p>
                 * 
                 * @param groupSize
                 *     Number of enrollees
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder groupSize(PositiveInt groupSize) {
                    this.groupSize = groupSize;
                    return this;
                }

                /**
                 * <p>
                 * Value of the cost.
                 * </p>
                 * 
                 * @param cost
                 *     Cost value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder cost(Money cost) {
                    this.cost = cost;
                    return this;
                }

                /**
                 * <p>
                 * Additional information about the general costs associated with this plan.
                 * </p>
                 * 
                 * @param comment
                 *     Additional cost information
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder comment(String comment) {
                    this.comment = comment;
                    return this;
                }

                @Override
                public GeneralCost build() {
                    return new GeneralCost(this);
                }

                private Builder from(GeneralCost generalCost) {
                    id = generalCost.id;
                    extension.addAll(generalCost.extension);
                    modifierExtension.addAll(generalCost.modifierExtension);
                    type = generalCost.type;
                    groupSize = generalCost.groupSize;
                    cost = generalCost.cost;
                    comment = generalCost.comment;
                    return this;
                }
            }
        }

        /**
         * <p>
         * Costs associated with the coverage provided by the product.
         * </p>
         */
        public static class SpecificCost extends BackboneElement {
            private final CodeableConcept category;
            private final List<Benefit> benefit;

            private volatile int hashCode;

            private SpecificCost(Builder builder) {
                super(builder);
                category = ValidationSupport.requireNonNull(builder.category, "category");
                benefit = Collections.unmodifiableList(builder.benefit);
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * General category of benefit (Medical; Dental; Vision; Drug; Mental Health; Substance Abuse; Hospice, Home Health).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getCategory() {
                return category;
            }

            /**
             * <p>
             * List of the specific benefits under this category of benefit.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Benefit}.
             */
            public List<Benefit> getBenefit() {
                return benefit;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (category != null) || 
                    !benefit.isEmpty();
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
                        accept(category, "category", visitor);
                        accept(benefit, "benefit", visitor, Benefit.class);
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
                SpecificCost other = (SpecificCost) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(category, other.category) && 
                    Objects.equals(benefit, other.benefit);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        category, 
                        benefit);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(category).from(this);
            }

            public Builder toBuilder(CodeableConcept category) {
                return new Builder(category).from(this);
            }

            public static Builder builder(CodeableConcept category) {
                return new Builder(category);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept category;

                // optional
                private List<Benefit> benefit = new ArrayList<>();

                private Builder(CodeableConcept category) {
                    super();
                    this.category = category;
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
                 * List of the specific benefits under this category of benefit.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param benefit
                 *     Benefits list
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder benefit(Benefit... benefit) {
                    for (Benefit value : benefit) {
                        this.benefit.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * List of the specific benefits under this category of benefit.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param benefit
                 *     Benefits list
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder benefit(Collection<Benefit> benefit) {
                    this.benefit = new ArrayList<>(benefit);
                    return this;
                }

                @Override
                public SpecificCost build() {
                    return new SpecificCost(this);
                }

                private Builder from(SpecificCost specificCost) {
                    id = specificCost.id;
                    extension.addAll(specificCost.extension);
                    modifierExtension.addAll(specificCost.modifierExtension);
                    benefit.addAll(specificCost.benefit);
                    return this;
                }
            }

            /**
             * <p>
             * List of the specific benefits under this category of benefit.
             * </p>
             */
            public static class Benefit extends BackboneElement {
                private final CodeableConcept type;
                private final List<Cost> cost;

                private volatile int hashCode;

                private Benefit(Builder builder) {
                    super(builder);
                    type = ValidationSupport.requireNonNull(builder.type, "type");
                    cost = Collections.unmodifiableList(builder.cost);
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * <p>
                 * Type of specific benefit (preventative; primary care office visit; speciality office visit; hospitalization; emergency 
                 * room; urgent care).
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getType() {
                    return type;
                }

                /**
                 * <p>
                 * List of the costs associated with a specific benefit.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Cost}.
                 */
                public List<Cost> getCost() {
                    return cost;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (type != null) || 
                        !cost.isEmpty();
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
                            accept(type, "type", visitor);
                            accept(cost, "cost", visitor, Cost.class);
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
                    Benefit other = (Benefit) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(type, other.type) && 
                        Objects.equals(cost, other.cost);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            type, 
                            cost);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(type).from(this);
                }

                public Builder toBuilder(CodeableConcept type) {
                    return new Builder(type).from(this);
                }

                public static Builder builder(CodeableConcept type) {
                    return new Builder(type);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final CodeableConcept type;

                    // optional
                    private List<Cost> cost = new ArrayList<>();

                    private Builder(CodeableConcept type) {
                        super();
                        this.type = type;
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
                     * List of the costs associated with a specific benefit.
                     * </p>
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
                     * 
                     * @param cost
                     *     List of the costs
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder cost(Cost... cost) {
                        for (Cost value : cost) {
                            this.cost.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * List of the costs associated with a specific benefit.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param cost
                     *     List of the costs
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder cost(Collection<Cost> cost) {
                        this.cost = new ArrayList<>(cost);
                        return this;
                    }

                    @Override
                    public Benefit build() {
                        return new Benefit(this);
                    }

                    private Builder from(Benefit benefit) {
                        id = benefit.id;
                        extension.addAll(benefit.extension);
                        modifierExtension.addAll(benefit.modifierExtension);
                        cost.addAll(benefit.cost);
                        return this;
                    }
                }

                /**
                 * <p>
                 * List of the costs associated with a specific benefit.
                 * </p>
                 */
                public static class Cost extends BackboneElement {
                    private final CodeableConcept type;
                    private final CodeableConcept applicability;
                    private final List<CodeableConcept> qualifiers;
                    private final Quantity value;

                    private volatile int hashCode;

                    private Cost(Builder builder) {
                        super(builder);
                        type = ValidationSupport.requireNonNull(builder.type, "type");
                        applicability = builder.applicability;
                        qualifiers = Collections.unmodifiableList(builder.qualifiers);
                        value = builder.value;
                        ValidationSupport.requireValueOrChildren(this);
                    }

                    /**
                     * <p>
                     * Type of cost (copay; individual cap; family cap; coinsurance; deductible).
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link CodeableConcept}.
                     */
                    public CodeableConcept getType() {
                        return type;
                    }

                    /**
                     * <p>
                     * Whether the cost applies to in-network or out-of-network providers (in-network; out-of-network; other).
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link CodeableConcept}.
                     */
                    public CodeableConcept getApplicability() {
                        return applicability;
                    }

                    /**
                     * <p>
                     * Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).
                     * </p>
                     * 
                     * @return
                     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
                     */
                    public List<CodeableConcept> getQualifiers() {
                        return qualifiers;
                    }

                    /**
                     * <p>
                     * The actual cost value. (some of the costs may be represented as percentages rather than currency, e.g. 10% 
                     * coinsurance).
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link Quantity}.
                     */
                    public Quantity getValue() {
                        return value;
                    }

                    @Override
                    public boolean hasChildren() {
                        return super.hasChildren() || 
                            (type != null) || 
                            (applicability != null) || 
                            !qualifiers.isEmpty() || 
                            (value != null);
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
                                accept(type, "type", visitor);
                                accept(applicability, "applicability", visitor);
                                accept(qualifiers, "qualifiers", visitor, CodeableConcept.class);
                                accept(value, "value", visitor);
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
                        Cost other = (Cost) obj;
                        return Objects.equals(id, other.id) && 
                            Objects.equals(extension, other.extension) && 
                            Objects.equals(modifierExtension, other.modifierExtension) && 
                            Objects.equals(type, other.type) && 
                            Objects.equals(applicability, other.applicability) && 
                            Objects.equals(qualifiers, other.qualifiers) && 
                            Objects.equals(value, other.value);
                    }

                    @Override
                    public int hashCode() {
                        int result = hashCode;
                        if (result == 0) {
                            result = Objects.hash(id, 
                                extension, 
                                modifierExtension, 
                                type, 
                                applicability, 
                                qualifiers, 
                                value);
                            hashCode = result;
                        }
                        return result;
                    }

                    @Override
                    public Builder toBuilder() {
                        return new Builder(type).from(this);
                    }

                    public Builder toBuilder(CodeableConcept type) {
                        return new Builder(type).from(this);
                    }

                    public static Builder builder(CodeableConcept type) {
                        return new Builder(type);
                    }

                    public static class Builder extends BackboneElement.Builder {
                        // required
                        private final CodeableConcept type;

                        // optional
                        private CodeableConcept applicability;
                        private List<CodeableConcept> qualifiers = new ArrayList<>();
                        private Quantity value;

                        private Builder(CodeableConcept type) {
                            super();
                            this.type = type;
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
                         * Whether the cost applies to in-network or out-of-network providers (in-network; out-of-network; other).
                         * </p>
                         * 
                         * @param applicability
                         *     in-network | out-of-network | other
                         * 
                         * @return
                         *     A reference to this Builder instance
                         */
                        public Builder applicability(CodeableConcept applicability) {
                            this.applicability = applicability;
                            return this;
                        }

                        /**
                         * <p>
                         * Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).
                         * </p>
                         * <p>
                         * Adds new element(s) to existing list
                         * </p>
                         * 
                         * @param qualifiers
                         *     Additional information about the cost
                         * 
                         * @return
                         *     A reference to this Builder instance
                         */
                        public Builder qualifiers(CodeableConcept... qualifiers) {
                            for (CodeableConcept value : qualifiers) {
                                this.qualifiers.add(value);
                            }
                            return this;
                        }

                        /**
                         * <p>
                         * Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).
                         * </p>
                         * <p>
                         * Replaces existing list with a new one containing elements from the Collection
                         * </p>
                         * 
                         * @param qualifiers
                         *     Additional information about the cost
                         * 
                         * @return
                         *     A reference to this Builder instance
                         */
                        public Builder qualifiers(Collection<CodeableConcept> qualifiers) {
                            this.qualifiers = new ArrayList<>(qualifiers);
                            return this;
                        }

                        /**
                         * <p>
                         * The actual cost value. (some of the costs may be represented as percentages rather than currency, e.g. 10% 
                         * coinsurance).
                         * </p>
                         * 
                         * @param value
                         *     The actual cost value
                         * 
                         * @return
                         *     A reference to this Builder instance
                         */
                        public Builder value(Quantity value) {
                            this.value = value;
                            return this;
                        }

                        @Override
                        public Cost build() {
                            return new Cost(this);
                        }

                        private Builder from(Cost cost) {
                            id = cost.id;
                            extension.addAll(cost.extension);
                            modifierExtension.addAll(cost.modifierExtension);
                            applicability = cost.applicability;
                            qualifiers.addAll(cost.qualifiers);
                            value = cost.value;
                            return this;
                        }
                    }
                }
            }
        }
    }
}
