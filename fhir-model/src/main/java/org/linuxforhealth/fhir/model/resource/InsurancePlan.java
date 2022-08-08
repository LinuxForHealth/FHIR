/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.annotation.Binding;
import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.annotation.Maturity;
import org.linuxforhealth.fhir.model.annotation.ReferenceTarget;
import org.linuxforhealth.fhir.model.annotation.Required;
import org.linuxforhealth.fhir.model.annotation.Summary;
import org.linuxforhealth.fhir.model.type.Address;
import org.linuxforhealth.fhir.model.type.BackboneElement;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.ContactPoint;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Money;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Period;
import org.linuxforhealth.fhir.model.type.PositiveInt;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.String;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.BindingStrength;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.model.type.code.StandardsStatus;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * Details of a Health Insurance product/plan provided by an organization.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "ipn-1",
    level = "Rule",
    location = "(base)",
    description = "The organization SHALL at least have a name or an identifier, and possibly more than one",
    expression = "(identifier.count() + name.count()) > 0",
    source = "http://hl7.org/fhir/StructureDefinition/InsurancePlan"
)
@Constraint(
    id = "insurancePlan-2",
    level = "Warning",
    location = "contact.purpose",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/contactentity-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/contactentity-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/InsurancePlan",
    generated = true
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class InsurancePlan extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.3.0"
    )
    private final PublicationStatus status;
    @Summary
    @Binding(
        bindingName = "InsurancePlanType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Used to categorize the product/plan.",
        valueSet = "http://hl7.org/fhir/ValueSet/insuranceplan-type"
    )
    private final List<CodeableConcept> type;
    @Summary
    private final String name;
    private final List<String> alias;
    private final Period period;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference ownedBy;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference administeredBy;
    @Summary
    @ReferenceTarget({ "Location" })
    private final List<Reference> coverageArea;
    private final List<Contact> contact;
    @ReferenceTarget({ "Endpoint" })
    private final List<Reference> endpoint;
    @ReferenceTarget({ "Organization" })
    private final List<Reference> network;
    private final List<Coverage> coverage;
    private final List<Plan> plan;

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
     * Business identifiers assigned to this health insurance product which remain constant as the resource is updated and 
     * propagates from server to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The current state of the health insurance product.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that may be null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * The kind of health insurance product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getType() {
        return type;
    }

    /**
     * Official name of the health insurance product as designated by the owner.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A list of alternate names that the product is known as, or was known as in the past.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getAlias() {
        return alias;
    }

    /**
     * The period of time that the health insurance product is available.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * The entity that is providing the health insurance product and underwriting the risk. This is typically an insurance 
     * carriers, other third-party payers, or health plan sponsors comonly referred to as 'payers'.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getOwnedBy() {
        return ownedBy;
    }

    /**
     * An organization which administer other services such as underwriting, customer service and/or claims processing on 
     * behalf of the health insurance product owner.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAdministeredBy() {
        return administeredBy;
    }

    /**
     * The geographic region in which a health insurance product's benefits apply.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getCoverageArea() {
        return coverageArea;
    }

    /**
     * The contact for the health insurance product for a certain purpose.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Contact} that may be empty.
     */
    public List<Contact> getContact() {
        return contact;
    }

    /**
     * The technical endpoints providing access to services operated for the health insurance product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getEndpoint() {
        return endpoint;
    }

    /**
     * Reference to the network included in the health insurance product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getNetwork() {
        return network;
    }

    /**
     * Details about the coverage offered by the insurance product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coverage} that may be empty.
     */
    public List<Coverage> getCoverage() {
        return coverage;
    }

    /**
     * Details about an insurance plan.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Plan} that may be empty.
     */
    public List<Plan> getPlan() {
        return plan;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            !type.isEmpty() || 
            (name != null) || 
            !alias.isEmpty() || 
            (period != null) || 
            (ownedBy != null) || 
            (administeredBy != null) || 
            !coverageArea.isEmpty() || 
            !contact.isEmpty() || 
            !endpoint.isEmpty() || 
            !network.isEmpty() || 
            !coverage.isEmpty() || 
            !plan.isEmpty();
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
         * Business identifiers assigned to this health insurance product which remain constant as the resource is updated and 
         * propagates from server to server.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Business identifiers assigned to this health insurance product which remain constant as the resource is updated and 
         * propagates from server to server.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for Product
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
         * The current state of the health insurance product.
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
         * The kind of health insurance product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The kind of health insurance product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param type
         *     Kind of product
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
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     Official name
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #name(org.linuxforhealth.fhir.model.type.String)
         */
        public Builder name(java.lang.String name) {
            this.name = (name == null) ? null : String.of(name);
            return this;
        }

        /**
         * Official name of the health insurance product as designated by the owner.
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
         * Convenience method for setting {@code alias}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param alias
         *     Alternate names
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #alias(org.linuxforhealth.fhir.model.type.String)
         */
        public Builder alias(java.lang.String... alias) {
            for (java.lang.String value : alias) {
                this.alias.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * A list of alternate names that the product is known as, or was known as in the past.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A list of alternate names that the product is known as, or was known as in the past.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param alias
         *     Alternate names
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder alias(Collection<String> alias) {
            this.alias = new ArrayList<>(alias);
            return this;
        }

        /**
         * The period of time that the health insurance product is available.
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
         * The entity that is providing the health insurance product and underwriting the risk. This is typically an insurance 
         * carriers, other third-party payers, or health plan sponsors comonly referred to as 'payers'.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
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
         * An organization which administer other services such as underwriting, customer service and/or claims processing on 
         * behalf of the health insurance product owner.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
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
         * The geographic region in which a health insurance product's benefits apply.
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
         * The geographic region in which a health insurance product's benefits apply.
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
         *     Where product applies
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
         * The contact for the health insurance product for a certain purpose.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The contact for the health insurance product for a certain purpose.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact for the product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<Contact> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * The technical endpoints providing access to services operated for the health insurance product.
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
         * The technical endpoints providing access to services operated for the health insurance product.
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
         *     Technical endpoint
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
         * Reference to the network included in the health insurance product.
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
         * Reference to the network included in the health insurance product.
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
         *     What networks are Included
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
         * Details about the coverage offered by the insurance product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Details about the coverage offered by the insurance product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param coverage
         *     Coverage details
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder coverage(Collection<Coverage> coverage) {
            this.coverage = new ArrayList<>(coverage);
            return this;
        }

        /**
         * Details about an insurance plan.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Details about an insurance plan.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param plan
         *     Plan details
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder plan(Collection<Plan> plan) {
            this.plan = new ArrayList<>(plan);
            return this;
        }

        /**
         * Build the {@link InsurancePlan}
         * 
         * @return
         *     An immutable object of type {@link InsurancePlan}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid InsurancePlan per the base specification
         */
        @Override
        public InsurancePlan build() {
            InsurancePlan insurancePlan = new InsurancePlan(this);
            if (validating) {
                validate(insurancePlan);
            }
            return insurancePlan;
        }

        protected void validate(InsurancePlan insurancePlan) {
            super.validate(insurancePlan);
            ValidationSupport.checkList(insurancePlan.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(insurancePlan.type, "type", CodeableConcept.class);
            ValidationSupport.checkList(insurancePlan.alias, "alias", String.class);
            ValidationSupport.checkList(insurancePlan.coverageArea, "coverageArea", Reference.class);
            ValidationSupport.checkList(insurancePlan.contact, "contact", Contact.class);
            ValidationSupport.checkList(insurancePlan.endpoint, "endpoint", Reference.class);
            ValidationSupport.checkList(insurancePlan.network, "network", Reference.class);
            ValidationSupport.checkList(insurancePlan.coverage, "coverage", Coverage.class);
            ValidationSupport.checkList(insurancePlan.plan, "plan", Plan.class);
            ValidationSupport.checkReferenceType(insurancePlan.ownedBy, "ownedBy", "Organization");
            ValidationSupport.checkReferenceType(insurancePlan.administeredBy, "administeredBy", "Organization");
            ValidationSupport.checkReferenceType(insurancePlan.coverageArea, "coverageArea", "Location");
            ValidationSupport.checkReferenceType(insurancePlan.endpoint, "endpoint", "Endpoint");
            ValidationSupport.checkReferenceType(insurancePlan.network, "network", "Organization");
        }

        protected Builder from(InsurancePlan insurancePlan) {
            super.from(insurancePlan);
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
     * The contact for the health insurance product for a certain purpose.
     */
    public static class Contact extends BackboneElement {
        @Binding(
            bindingName = "ContactPartyType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The purpose for which you would contact a contact party.",
            valueSet = "http://hl7.org/fhir/ValueSet/contactentity-type"
        )
        private final CodeableConcept purpose;
        private final HumanName name;
        private final List<ContactPoint> telecom;
        private final Address address;

        private Contact(Builder builder) {
            super(builder);
            purpose = builder.purpose;
            name = builder.name;
            telecom = Collections.unmodifiableList(builder.telecom);
            address = builder.address;
        }

        /**
         * Indicates a purpose for which the contact can be reached.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getPurpose() {
            return purpose;
        }

        /**
         * A name associated with the contact.
         * 
         * @return
         *     An immutable object of type {@link HumanName} that may be null.
         */
        public HumanName getName() {
            return name;
        }

        /**
         * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ContactPoint} that may be empty.
         */
        public List<ContactPoint> getTelecom() {
            return telecom;
        }

        /**
         * Visiting or postal addresses for the contact.
         * 
         * @return
         *     An immutable object of type {@link Address} that may be null.
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
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(purpose, "purpose", visitor);
                    accept(name, "name", visitor);
                    accept(telecom, "telecom", visitor, ContactPoint.class);
                    accept(address, "address", visitor);
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
            private CodeableConcept purpose;
            private HumanName name;
            private List<ContactPoint> telecom = new ArrayList<>();
            private Address address;

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
             * Indicates a purpose for which the contact can be reached.
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
             * A name associated with the contact.
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
             * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param telecom
             *     Contact details (telephone, email, etc.) for a contact
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
             * Visiting or postal addresses for the contact.
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

            /**
             * Build the {@link Contact}
             * 
             * @return
             *     An immutable object of type {@link Contact}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Contact per the base specification
             */
            @Override
            public Contact build() {
                Contact contact = new Contact(this);
                if (validating) {
                    validate(contact);
                }
                return contact;
            }

            protected void validate(Contact contact) {
                super.validate(contact);
                ValidationSupport.checkList(contact.telecom, "telecom", ContactPoint.class);
                ValidationSupport.requireValueOrChildren(contact);
            }

            protected Builder from(Contact contact) {
                super.from(contact);
                purpose = contact.purpose;
                name = contact.name;
                telecom.addAll(contact.telecom);
                address = contact.address;
                return this;
            }
        }
    }

    /**
     * Details about the coverage offered by the insurance product.
     */
    public static class Coverage extends BackboneElement {
        @Required
        private final CodeableConcept type;
        @ReferenceTarget({ "Organization" })
        private final List<Reference> network;
        @Required
        private final List<Benefit> benefit;

        private Coverage(Builder builder) {
            super(builder);
            type = builder.type;
            network = Collections.unmodifiableList(builder.network);
            benefit = Collections.unmodifiableList(builder.benefit);
        }

        /**
         * Type of coverage (Medical; Dental; Mental Health; Substance Abuse; Vision; Drug; Short Term; Long Term Care; Hospice; 
         * Home Health).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Reference to the network that providing the type of coverage.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getNetwork() {
            return network;
        }

        /**
         * Specific benefits under this type of coverage.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Benefit} that is non-empty.
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
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(type, "type", visitor);
                    accept(network, "network", visitor, Reference.class);
                    accept(benefit, "benefit", visitor, Benefit.class);
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private List<Reference> network = new ArrayList<>();
            private List<Benefit> benefit = new ArrayList<>();

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
             * Type of coverage (Medical; Dental; Mental Health; Substance Abuse; Vision; Drug; Short Term; Long Term Care; Hospice; 
             * Home Health).
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Type of coverage
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Reference to the network that providing the type of coverage.
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
             * Reference to the network that providing the type of coverage.
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
             *     What networks provide coverage
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
             * Specific benefits under this type of coverage.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param benefit
             *     List of benefits
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
             * Specific benefits under this type of coverage.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param benefit
             *     List of benefits
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder benefit(Collection<Benefit> benefit) {
                this.benefit = new ArrayList<>(benefit);
                return this;
            }

            /**
             * Build the {@link Coverage}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>benefit</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Coverage}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Coverage per the base specification
             */
            @Override
            public Coverage build() {
                Coverage coverage = new Coverage(this);
                if (validating) {
                    validate(coverage);
                }
                return coverage;
            }

            protected void validate(Coverage coverage) {
                super.validate(coverage);
                ValidationSupport.requireNonNull(coverage.type, "type");
                ValidationSupport.checkList(coverage.network, "network", Reference.class);
                ValidationSupport.checkNonEmptyList(coverage.benefit, "benefit", Benefit.class);
                ValidationSupport.checkReferenceType(coverage.network, "network", "Organization");
                ValidationSupport.requireValueOrChildren(coverage);
            }

            protected Builder from(Coverage coverage) {
                super.from(coverage);
                type = coverage.type;
                network.addAll(coverage.network);
                benefit.addAll(coverage.benefit);
                return this;
            }
        }

        /**
         * Specific benefits under this type of coverage.
         */
        public static class Benefit extends BackboneElement {
            @Required
            private final CodeableConcept type;
            private final String requirement;
            private final List<Limit> limit;

            private Benefit(Builder builder) {
                super(builder);
                type = builder.type;
                requirement = builder.requirement;
                limit = Collections.unmodifiableList(builder.limit);
            }

            /**
             * Type of benefit (primary care; speciality care; inpatient; outpatient).
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * The referral requirements to have access/coverage for this benefit.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getRequirement() {
                return requirement;
            }

            /**
             * The specific limits on the benefit.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Limit} that may be empty.
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
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(type, "type", visitor);
                        accept(requirement, "requirement", visitor);
                        accept(limit, "limit", visitor, Limit.class);
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
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept type;
                private String requirement;
                private List<Limit> limit = new ArrayList<>();

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
                 * Type of benefit (primary care; speciality care; inpatient; outpatient).
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     Type of benefit
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Convenience method for setting {@code requirement}.
                 * 
                 * @param requirement
                 *     Referral requirements
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #requirement(org.linuxforhealth.fhir.model.type.String)
                 */
                public Builder requirement(java.lang.String requirement) {
                    this.requirement = (requirement == null) ? null : String.of(requirement);
                    return this;
                }

                /**
                 * The referral requirements to have access/coverage for this benefit.
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
                 * The specific limits on the benefit.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * The specific limits on the benefit.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param limit
                 *     Benefit limits
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder limit(Collection<Limit> limit) {
                    this.limit = new ArrayList<>(limit);
                    return this;
                }

                /**
                 * Build the {@link Benefit}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Benefit}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Benefit per the base specification
                 */
                @Override
                public Benefit build() {
                    Benefit benefit = new Benefit(this);
                    if (validating) {
                        validate(benefit);
                    }
                    return benefit;
                }

                protected void validate(Benefit benefit) {
                    super.validate(benefit);
                    ValidationSupport.requireNonNull(benefit.type, "type");
                    ValidationSupport.checkList(benefit.limit, "limit", Limit.class);
                    ValidationSupport.requireValueOrChildren(benefit);
                }

                protected Builder from(Benefit benefit) {
                    super.from(benefit);
                    type = benefit.type;
                    requirement = benefit.requirement;
                    limit.addAll(benefit.limit);
                    return this;
                }
            }

            /**
             * The specific limits on the benefit.
             */
            public static class Limit extends BackboneElement {
                private final Quantity value;
                private final CodeableConcept code;

                private Limit(Builder builder) {
                    super(builder);
                    value = builder.value;
                    code = builder.code;
                }

                /**
                 * The maximum amount of a service item a plan will pay for a covered benefit. For examples. wellness visits, or 
                 * eyeglasses.
                 * 
                 * @return
                 *     An immutable object of type {@link Quantity} that may be null.
                 */
                public Quantity getValue() {
                    return value;
                }

                /**
                 * The specific limit on the benefit.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that may be null.
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
                public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                    if (visitor.preVisit(this)) {
                        visitor.visitStart(elementName, elementIndex, this);
                        if (visitor.visit(elementName, elementIndex, this)) {
                            // visit children
                            accept(id, "id", visitor);
                            accept(extension, "extension", visitor, Extension.class);
                            accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                            accept(value, "value", visitor);
                            accept(code, "code", visitor);
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
                    private Quantity value;
                    private CodeableConcept code;

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
                     * The maximum amount of a service item a plan will pay for a covered benefit. For examples. wellness visits, or 
                     * eyeglasses.
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
                     * The specific limit on the benefit.
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

                    /**
                     * Build the {@link Limit}
                     * 
                     * @return
                     *     An immutable object of type {@link Limit}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Limit per the base specification
                     */
                    @Override
                    public Limit build() {
                        Limit limit = new Limit(this);
                        if (validating) {
                            validate(limit);
                        }
                        return limit;
                    }

                    protected void validate(Limit limit) {
                        super.validate(limit);
                        ValidationSupport.requireValueOrChildren(limit);
                    }

                    protected Builder from(Limit limit) {
                        super.from(limit);
                        value = limit.value;
                        code = limit.code;
                        return this;
                    }
                }
            }
        }
    }

    /**
     * Details about an insurance plan.
     */
    public static class Plan extends BackboneElement {
        @Summary
        private final List<Identifier> identifier;
        private final CodeableConcept type;
        @Summary
        @ReferenceTarget({ "Location" })
        private final List<Reference> coverageArea;
        @ReferenceTarget({ "Organization" })
        private final List<Reference> network;
        private final List<GeneralCost> generalCost;
        private final List<SpecificCost> specificCost;

        private Plan(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(builder.identifier);
            type = builder.type;
            coverageArea = Collections.unmodifiableList(builder.coverageArea);
            network = Collections.unmodifiableList(builder.network);
            generalCost = Collections.unmodifiableList(builder.generalCost);
            specificCost = Collections.unmodifiableList(builder.specificCost);
        }

        /**
         * Business identifiers assigned to this health insurance plan which remain constant as the resource is updated and 
         * propagates from server to server.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
         */
        public List<Identifier> getIdentifier() {
            return identifier;
        }

        /**
         * Type of plan. For example, "Platinum" or "High Deductable".
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The geographic region in which a health insurance plan's benefits apply.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getCoverageArea() {
            return coverageArea;
        }

        /**
         * Reference to the network that providing the type of coverage.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getNetwork() {
            return network;
        }

        /**
         * Overall costs associated with the plan.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link GeneralCost} that may be empty.
         */
        public List<GeneralCost> getGeneralCost() {
            return generalCost;
        }

        /**
         * Costs associated with the coverage provided by the product.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link SpecificCost} that may be empty.
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
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
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
             * Business identifiers assigned to this health insurance plan which remain constant as the resource is updated and 
             * propagates from server to server.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Business identifiers assigned to this health insurance plan which remain constant as the resource is updated and 
             * propagates from server to server.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     Business Identifier for Product
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
             * Type of plan. For example, "Platinum" or "High Deductable".
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
             * The geographic region in which a health insurance plan's benefits apply.
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
             * The geographic region in which a health insurance plan's benefits apply.
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
             *     Where product applies
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
             * Reference to the network that providing the type of coverage.
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
             * Reference to the network that providing the type of coverage.
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
             *     What networks provide coverage
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
             * Overall costs associated with the plan.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Overall costs associated with the plan.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param generalCost
             *     Overall costs
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder generalCost(Collection<GeneralCost> generalCost) {
                this.generalCost = new ArrayList<>(generalCost);
                return this;
            }

            /**
             * Costs associated with the coverage provided by the product.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Costs associated with the coverage provided by the product.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param specificCost
             *     Specific costs
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder specificCost(Collection<SpecificCost> specificCost) {
                this.specificCost = new ArrayList<>(specificCost);
                return this;
            }

            /**
             * Build the {@link Plan}
             * 
             * @return
             *     An immutable object of type {@link Plan}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Plan per the base specification
             */
            @Override
            public Plan build() {
                Plan plan = new Plan(this);
                if (validating) {
                    validate(plan);
                }
                return plan;
            }

            protected void validate(Plan plan) {
                super.validate(plan);
                ValidationSupport.checkList(plan.identifier, "identifier", Identifier.class);
                ValidationSupport.checkList(plan.coverageArea, "coverageArea", Reference.class);
                ValidationSupport.checkList(plan.network, "network", Reference.class);
                ValidationSupport.checkList(plan.generalCost, "generalCost", GeneralCost.class);
                ValidationSupport.checkList(plan.specificCost, "specificCost", SpecificCost.class);
                ValidationSupport.checkReferenceType(plan.coverageArea, "coverageArea", "Location");
                ValidationSupport.checkReferenceType(plan.network, "network", "Organization");
                ValidationSupport.requireValueOrChildren(plan);
            }

            protected Builder from(Plan plan) {
                super.from(plan);
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
         * Overall costs associated with the plan.
         */
        public static class GeneralCost extends BackboneElement {
            private final CodeableConcept type;
            private final PositiveInt groupSize;
            private final Money cost;
            private final String comment;

            private GeneralCost(Builder builder) {
                super(builder);
                type = builder.type;
                groupSize = builder.groupSize;
                cost = builder.cost;
                comment = builder.comment;
            }

            /**
             * Type of cost.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * Number of participants enrolled in the plan.
             * 
             * @return
             *     An immutable object of type {@link PositiveInt} that may be null.
             */
            public PositiveInt getGroupSize() {
                return groupSize;
            }

            /**
             * Value of the cost.
             * 
             * @return
             *     An immutable object of type {@link Money} that may be null.
             */
            public Money getCost() {
                return cost;
            }

            /**
             * Additional information about the general costs associated with this plan.
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
                    (type != null) || 
                    (groupSize != null) || 
                    (cost != null) || 
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
                        accept(type, "type", visitor);
                        accept(groupSize, "groupSize", visitor);
                        accept(cost, "cost", visitor);
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
                private CodeableConcept type;
                private PositiveInt groupSize;
                private Money cost;
                private String comment;

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
                 * Type of cost.
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
                 * Number of participants enrolled in the plan.
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
                 * Value of the cost.
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
                 * Convenience method for setting {@code comment}.
                 * 
                 * @param comment
                 *     Additional cost information
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #comment(org.linuxforhealth.fhir.model.type.String)
                 */
                public Builder comment(java.lang.String comment) {
                    this.comment = (comment == null) ? null : String.of(comment);
                    return this;
                }

                /**
                 * Additional information about the general costs associated with this plan.
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

                /**
                 * Build the {@link GeneralCost}
                 * 
                 * @return
                 *     An immutable object of type {@link GeneralCost}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid GeneralCost per the base specification
                 */
                @Override
                public GeneralCost build() {
                    GeneralCost generalCost = new GeneralCost(this);
                    if (validating) {
                        validate(generalCost);
                    }
                    return generalCost;
                }

                protected void validate(GeneralCost generalCost) {
                    super.validate(generalCost);
                    ValidationSupport.requireValueOrChildren(generalCost);
                }

                protected Builder from(GeneralCost generalCost) {
                    super.from(generalCost);
                    type = generalCost.type;
                    groupSize = generalCost.groupSize;
                    cost = generalCost.cost;
                    comment = generalCost.comment;
                    return this;
                }
            }
        }

        /**
         * Costs associated with the coverage provided by the product.
         */
        public static class SpecificCost extends BackboneElement {
            @Required
            private final CodeableConcept category;
            private final List<Benefit> benefit;

            private SpecificCost(Builder builder) {
                super(builder);
                category = builder.category;
                benefit = Collections.unmodifiableList(builder.benefit);
            }

            /**
             * General category of benefit (Medical; Dental; Vision; Drug; Mental Health; Substance Abuse; Hospice, Home Health).
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getCategory() {
                return category;
            }

            /**
             * List of the specific benefits under this category of benefit.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Benefit} that may be empty.
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
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(category, "category", visitor);
                        accept(benefit, "benefit", visitor, Benefit.class);
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
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept category;
                private List<Benefit> benefit = new ArrayList<>();

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
                 * General category of benefit (Medical; Dental; Vision; Drug; Mental Health; Substance Abuse; Hospice, Home Health).
                 * 
                 * <p>This element is required.
                 * 
                 * @param category
                 *     General category of benefit
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder category(CodeableConcept category) {
                    this.category = category;
                    return this;
                }

                /**
                 * List of the specific benefits under this category of benefit.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * List of the specific benefits under this category of benefit.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param benefit
                 *     Benefits list
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder benefit(Collection<Benefit> benefit) {
                    this.benefit = new ArrayList<>(benefit);
                    return this;
                }

                /**
                 * Build the {@link SpecificCost}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>category</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link SpecificCost}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid SpecificCost per the base specification
                 */
                @Override
                public SpecificCost build() {
                    SpecificCost specificCost = new SpecificCost(this);
                    if (validating) {
                        validate(specificCost);
                    }
                    return specificCost;
                }

                protected void validate(SpecificCost specificCost) {
                    super.validate(specificCost);
                    ValidationSupport.requireNonNull(specificCost.category, "category");
                    ValidationSupport.checkList(specificCost.benefit, "benefit", Benefit.class);
                    ValidationSupport.requireValueOrChildren(specificCost);
                }

                protected Builder from(SpecificCost specificCost) {
                    super.from(specificCost);
                    category = specificCost.category;
                    benefit.addAll(specificCost.benefit);
                    return this;
                }
            }

            /**
             * List of the specific benefits under this category of benefit.
             */
            public static class Benefit extends BackboneElement {
                @Required
                private final CodeableConcept type;
                private final List<Cost> cost;

                private Benefit(Builder builder) {
                    super(builder);
                    type = builder.type;
                    cost = Collections.unmodifiableList(builder.cost);
                }

                /**
                 * Type of specific benefit (preventative; primary care office visit; speciality office visit; hospitalization; emergency 
                 * room; urgent care).
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that is non-null.
                 */
                public CodeableConcept getType() {
                    return type;
                }

                /**
                 * List of the costs associated with a specific benefit.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Cost} that may be empty.
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
                public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                    if (visitor.preVisit(this)) {
                        visitor.visitStart(elementName, elementIndex, this);
                        if (visitor.visit(elementName, elementIndex, this)) {
                            // visit children
                            accept(id, "id", visitor);
                            accept(extension, "extension", visitor, Extension.class);
                            accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                            accept(type, "type", visitor);
                            accept(cost, "cost", visitor, Cost.class);
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
                    return new Builder().from(this);
                }

                public static Builder builder() {
                    return new Builder();
                }

                public static class Builder extends BackboneElement.Builder {
                    private CodeableConcept type;
                    private List<Cost> cost = new ArrayList<>();

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
                     * Type of specific benefit (preventative; primary care office visit; speciality office visit; hospitalization; emergency 
                     * room; urgent care).
                     * 
                     * <p>This element is required.
                     * 
                     * @param type
                     *     Type of specific benefit
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder type(CodeableConcept type) {
                        this.type = type;
                        return this;
                    }

                    /**
                     * List of the costs associated with a specific benefit.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
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
                     * List of the costs associated with a specific benefit.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param cost
                     *     List of the costs
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder cost(Collection<Cost> cost) {
                        this.cost = new ArrayList<>(cost);
                        return this;
                    }

                    /**
                     * Build the {@link Benefit}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>type</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Benefit}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Benefit per the base specification
                     */
                    @Override
                    public Benefit build() {
                        Benefit benefit = new Benefit(this);
                        if (validating) {
                            validate(benefit);
                        }
                        return benefit;
                    }

                    protected void validate(Benefit benefit) {
                        super.validate(benefit);
                        ValidationSupport.requireNonNull(benefit.type, "type");
                        ValidationSupport.checkList(benefit.cost, "cost", Cost.class);
                        ValidationSupport.requireValueOrChildren(benefit);
                    }

                    protected Builder from(Benefit benefit) {
                        super.from(benefit);
                        type = benefit.type;
                        cost.addAll(benefit.cost);
                        return this;
                    }
                }

                /**
                 * List of the costs associated with a specific benefit.
                 */
                public static class Cost extends BackboneElement {
                    @Required
                    private final CodeableConcept type;
                    @Binding(
                        bindingName = "BenefitCostApplicability",
                        strength = BindingStrength.Value.REQUIRED,
                        description = "Whether the cost applies to in-network or out-of-network providers.",
                        valueSet = "http://hl7.org/fhir/ValueSet/insuranceplan-applicability|4.3.0"
                    )
                    private final CodeableConcept applicability;
                    private final List<CodeableConcept> qualifiers;
                    private final Quantity value;

                    private Cost(Builder builder) {
                        super(builder);
                        type = builder.type;
                        applicability = builder.applicability;
                        qualifiers = Collections.unmodifiableList(builder.qualifiers);
                        value = builder.value;
                    }

                    /**
                     * Type of cost (copay; individual cap; family cap; coinsurance; deductible).
                     * 
                     * @return
                     *     An immutable object of type {@link CodeableConcept} that is non-null.
                     */
                    public CodeableConcept getType() {
                        return type;
                    }

                    /**
                     * Whether the cost applies to in-network or out-of-network providers (in-network; out-of-network; other).
                     * 
                     * @return
                     *     An immutable object of type {@link CodeableConcept} that may be null.
                     */
                    public CodeableConcept getApplicability() {
                        return applicability;
                    }

                    /**
                     * Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).
                     * 
                     * @return
                     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
                     */
                    public List<CodeableConcept> getQualifiers() {
                        return qualifiers;
                    }

                    /**
                     * The actual cost value. (some of the costs may be represented as percentages rather than currency, e.g. 10% 
                     * coinsurance).
                     * 
                     * @return
                     *     An immutable object of type {@link Quantity} that may be null.
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
                    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                        if (visitor.preVisit(this)) {
                            visitor.visitStart(elementName, elementIndex, this);
                            if (visitor.visit(elementName, elementIndex, this)) {
                                // visit children
                                accept(id, "id", visitor);
                                accept(extension, "extension", visitor, Extension.class);
                                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                                accept(type, "type", visitor);
                                accept(applicability, "applicability", visitor);
                                accept(qualifiers, "qualifiers", visitor, CodeableConcept.class);
                                accept(value, "value", visitor);
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
                        return new Builder().from(this);
                    }

                    public static Builder builder() {
                        return new Builder();
                    }

                    public static class Builder extends BackboneElement.Builder {
                        private CodeableConcept type;
                        private CodeableConcept applicability;
                        private List<CodeableConcept> qualifiers = new ArrayList<>();
                        private Quantity value;

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
                         * Type of cost (copay; individual cap; family cap; coinsurance; deductible).
                         * 
                         * <p>This element is required.
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
                         * Whether the cost applies to in-network or out-of-network providers (in-network; out-of-network; other).
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
                         * Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).
                         * 
                         * <p>Adds new element(s) to the existing list.
                         * If any of the elements are null, calling {@link #build()} will fail.
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
                         * Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).
                         * 
                         * <p>Replaces the existing list with a new one containing elements from the Collection.
                         * If any of the elements are null, calling {@link #build()} will fail.
                         * 
                         * @param qualifiers
                         *     Additional information about the cost
                         * 
                         * @return
                         *     A reference to this Builder instance
                         * 
                         * @throws NullPointerException
                         *     If the passed collection is null
                         */
                        public Builder qualifiers(Collection<CodeableConcept> qualifiers) {
                            this.qualifiers = new ArrayList<>(qualifiers);
                            return this;
                        }

                        /**
                         * The actual cost value. (some of the costs may be represented as percentages rather than currency, e.g. 10% 
                         * coinsurance).
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

                        /**
                         * Build the {@link Cost}
                         * 
                         * <p>Required elements:
                         * <ul>
                         * <li>type</li>
                         * </ul>
                         * 
                         * @return
                         *     An immutable object of type {@link Cost}
                         * @throws IllegalStateException
                         *     if the current state cannot be built into a valid Cost per the base specification
                         */
                        @Override
                        public Cost build() {
                            Cost cost = new Cost(this);
                            if (validating) {
                                validate(cost);
                            }
                            return cost;
                        }

                        protected void validate(Cost cost) {
                            super.validate(cost);
                            ValidationSupport.requireNonNull(cost.type, "type");
                            ValidationSupport.checkList(cost.qualifiers, "qualifiers", CodeableConcept.class);
                            ValidationSupport.requireValueOrChildren(cost);
                        }

                        protected Builder from(Cost cost) {
                            super.from(cost);
                            type = cost.type;
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
