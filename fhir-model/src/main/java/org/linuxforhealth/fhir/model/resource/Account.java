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
import org.linuxforhealth.fhir.model.annotation.Maturity;
import org.linuxforhealth.fhir.model.annotation.ReferenceTarget;
import org.linuxforhealth.fhir.model.annotation.Required;
import org.linuxforhealth.fhir.model.annotation.Summary;
import org.linuxforhealth.fhir.model.type.BackboneElement;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Period;
import org.linuxforhealth.fhir.model.type.PositiveInt;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.String;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.AccountStatus;
import org.linuxforhealth.fhir.model.type.code.BindingStrength;
import org.linuxforhealth.fhir.model.type.code.StandardsStatus;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * A financial tool for tracking value accrued for a particular purpose. In the healthcare field, used to track charges 
 * for a patient, cost centers, etc.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class Account extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "AccountStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Indicates whether the account is available to be used.",
        valueSet = "http://hl7.org/fhir/ValueSet/account-status|4.3.0"
    )
    @Required
    private final AccountStatus status;
    @Summary
    @Binding(
        bindingName = "AccountType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The usage type of this account, permits categorization of accounts.",
        valueSet = "http://hl7.org/fhir/ValueSet/account-type"
    )
    private final CodeableConcept type;
    @Summary
    private final String name;
    @Summary
    @ReferenceTarget({ "Patient", "Device", "Practitioner", "PractitionerRole", "Location", "HealthcareService", "Organization" })
    private final List<Reference> subject;
    @Summary
    private final Period servicePeriod;
    @Summary
    private final List<Coverage> coverage;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference owner;
    @Summary
    private final String description;
    private final List<Guarantor> guarantor;
    @ReferenceTarget({ "Account" })
    private final Reference partOf;

    private Account(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        type = builder.type;
        name = builder.name;
        subject = Collections.unmodifiableList(builder.subject);
        servicePeriod = builder.servicePeriod;
        coverage = Collections.unmodifiableList(builder.coverage);
        owner = builder.owner;
        description = builder.description;
        guarantor = Collections.unmodifiableList(builder.guarantor);
        partOf = builder.partOf;
    }

    /**
     * Unique identifier used to reference the account. Might or might not be intended for human use (e.g. credit card 
     * number).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Indicates whether the account is presently used/usable or not.
     * 
     * @return
     *     An immutable object of type {@link AccountStatus} that is non-null.
     */
    public AccountStatus getStatus() {
        return status;
    }

    /**
     * Categorizes the account for reporting and searching purposes.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * Name used for the account when displaying it to humans in reports, etc.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * Identifies the entity which incurs the expenses. While the immediate recipients of services or goods might be entities 
     * related to the subject, the expenses were ultimately incurred by the subject of the Account.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSubject() {
        return subject;
    }

    /**
     * The date range of services associated with this account.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getServicePeriod() {
        return servicePeriod;
    }

    /**
     * The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
     * the account.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coverage} that may be empty.
     */
    public List<Coverage> getCoverage() {
        return coverage;
    }

    /**
     * Indicates the service area, hospital, department, etc. with responsibility for managing the Account.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getOwner() {
        return owner;
    }

    /**
     * Provides additional information about what the account tracks and how it is used.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The parties responsible for balancing the account if other payment options fall short.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Guarantor} that may be empty.
     */
    public List<Guarantor> getGuarantor() {
        return guarantor;
    }

    /**
     * Reference to a parent Account.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPartOf() {
        return partOf;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (type != null) || 
            (name != null) || 
            !subject.isEmpty() || 
            (servicePeriod != null) || 
            !coverage.isEmpty() || 
            (owner != null) || 
            (description != null) || 
            !guarantor.isEmpty() || 
            (partOf != null);
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
                accept(type, "type", visitor);
                accept(name, "name", visitor);
                accept(subject, "subject", visitor, Reference.class);
                accept(servicePeriod, "servicePeriod", visitor);
                accept(coverage, "coverage", visitor, Coverage.class);
                accept(owner, "owner", visitor);
                accept(description, "description", visitor);
                accept(guarantor, "guarantor", visitor, Guarantor.class);
                accept(partOf, "partOf", visitor);
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
        Account other = (Account) obj;
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
            Objects.equals(subject, other.subject) && 
            Objects.equals(servicePeriod, other.servicePeriod) && 
            Objects.equals(coverage, other.coverage) && 
            Objects.equals(owner, other.owner) && 
            Objects.equals(description, other.description) && 
            Objects.equals(guarantor, other.guarantor) && 
            Objects.equals(partOf, other.partOf);
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
                subject, 
                servicePeriod, 
                coverage, 
                owner, 
                description, 
                guarantor, 
                partOf);
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
        private AccountStatus status;
        private CodeableConcept type;
        private String name;
        private List<Reference> subject = new ArrayList<>();
        private Period servicePeriod;
        private List<Coverage> coverage = new ArrayList<>();
        private Reference owner;
        private String description;
        private List<Guarantor> guarantor = new ArrayList<>();
        private Reference partOf;

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
         * Unique identifier used to reference the account. Might or might not be intended for human use (e.g. credit card 
         * number).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Account number
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
         * Unique identifier used to reference the account. Might or might not be intended for human use (e.g. credit card 
         * number).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Account number
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
         * Indicates whether the account is presently used/usable or not.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | inactive | entered-in-error | on-hold | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(AccountStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Categorizes the account for reporting and searching purposes.
         * 
         * @param type
         *     E.g. patient, expense, depreciation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     Human-readable label
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
         * Name used for the account when displaying it to humans in reports, etc.
         * 
         * @param name
         *     Human-readable label
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Identifies the entity which incurs the expenses. While the immediate recipients of services or goods might be entities 
         * related to the subject, the expenses were ultimately incurred by the subject of the Account.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Device}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Location}</li>
         * <li>{@link HealthcareService}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param subject
         *     The entity that caused the expenses
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference... subject) {
            for (Reference value : subject) {
                this.subject.add(value);
            }
            return this;
        }

        /**
         * Identifies the entity which incurs the expenses. While the immediate recipients of services or goods might be entities 
         * related to the subject, the expenses were ultimately incurred by the subject of the Account.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Device}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Location}</li>
         * <li>{@link HealthcareService}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param subject
         *     The entity that caused the expenses
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder subject(Collection<Reference> subject) {
            this.subject = new ArrayList<>(subject);
            return this;
        }

        /**
         * The date range of services associated with this account.
         * 
         * @param servicePeriod
         *     Transaction window
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder servicePeriod(Period servicePeriod) {
            this.servicePeriod = servicePeriod;
            return this;
        }

        /**
         * The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
         * the account.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param coverage
         *     The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
         *     the account
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
         * The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
         * the account.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param coverage
         *     The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
         *     the account
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
         * Indicates the service area, hospital, department, etc. with responsibility for managing the Account.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param owner
         *     Entity managing the Account
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder owner(Reference owner) {
            this.owner = owner;
            return this;
        }

        /**
         * Convenience method for setting {@code description}.
         * 
         * @param description
         *     Explanation of purpose/use
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #description(org.linuxforhealth.fhir.model.type.String)
         */
        public Builder description(java.lang.String description) {
            this.description = (description == null) ? null : String.of(description);
            return this;
        }

        /**
         * Provides additional information about what the account tracks and how it is used.
         * 
         * @param description
         *     Explanation of purpose/use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * The parties responsible for balancing the account if other payment options fall short.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param guarantor
         *     The parties ultimately responsible for balancing the Account
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder guarantor(Guarantor... guarantor) {
            for (Guarantor value : guarantor) {
                this.guarantor.add(value);
            }
            return this;
        }

        /**
         * The parties responsible for balancing the account if other payment options fall short.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param guarantor
         *     The parties ultimately responsible for balancing the Account
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder guarantor(Collection<Guarantor> guarantor) {
            this.guarantor = new ArrayList<>(guarantor);
            return this;
        }

        /**
         * Reference to a parent Account.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Account}</li>
         * </ul>
         * 
         * @param partOf
         *     Reference to a parent Account
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference partOf) {
            this.partOf = partOf;
            return this;
        }

        /**
         * Build the {@link Account}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Account}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Account per the base specification
         */
        @Override
        public Account build() {
            Account account = new Account(this);
            if (validating) {
                validate(account);
            }
            return account;
        }

        protected void validate(Account account) {
            super.validate(account);
            ValidationSupport.checkList(account.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(account.status, "status");
            ValidationSupport.checkList(account.subject, "subject", Reference.class);
            ValidationSupport.checkList(account.coverage, "coverage", Coverage.class);
            ValidationSupport.checkList(account.guarantor, "guarantor", Guarantor.class);
            ValidationSupport.checkReferenceType(account.subject, "subject", "Patient", "Device", "Practitioner", "PractitionerRole", "Location", "HealthcareService", "Organization");
            ValidationSupport.checkReferenceType(account.owner, "owner", "Organization");
            ValidationSupport.checkReferenceType(account.partOf, "partOf", "Account");
        }

        protected Builder from(Account account) {
            super.from(account);
            identifier.addAll(account.identifier);
            status = account.status;
            type = account.type;
            name = account.name;
            subject.addAll(account.subject);
            servicePeriod = account.servicePeriod;
            coverage.addAll(account.coverage);
            owner = account.owner;
            description = account.description;
            guarantor.addAll(account.guarantor);
            partOf = account.partOf;
            return this;
        }
    }

    /**
     * The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
     * the account.
     */
    public static class Coverage extends BackboneElement {
        @Summary
        @ReferenceTarget({ "Coverage" })
        @Required
        private final Reference coverage;
        @Summary
        private final PositiveInt priority;

        private Coverage(Builder builder) {
            super(builder);
            coverage = builder.coverage;
            priority = builder.priority;
        }

        /**
         * The party(s) that contribute to payment (or part of) of the charges applied to this account (including self-pay).
         * 
         * <p>A coverage may only be responsible for specific types of charges, and the sequence of the coverages in the account 
         * could be important when processing billing.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getCoverage() {
            return coverage;
        }

        /**
         * The priority of the coverage in the context of this account.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that may be null.
         */
        public PositiveInt getPriority() {
            return priority;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (coverage != null) || 
                (priority != null);
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
                    accept(coverage, "coverage", visitor);
                    accept(priority, "priority", visitor);
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
                Objects.equals(coverage, other.coverage) && 
                Objects.equals(priority, other.priority);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    coverage, 
                    priority);
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
            private Reference coverage;
            private PositiveInt priority;

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
             * The party(s) that contribute to payment (or part of) of the charges applied to this account (including self-pay).
             * 
             * <p>A coverage may only be responsible for specific types of charges, and the sequence of the coverages in the account 
             * could be important when processing billing.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Coverage}</li>
             * </ul>
             * 
             * @param coverage
             *     The party(s), such as insurances, that may contribute to the payment of this account
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder coverage(Reference coverage) {
                this.coverage = coverage;
                return this;
            }

            /**
             * The priority of the coverage in the context of this account.
             * 
             * @param priority
             *     The priority of the coverage in the context of this account
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder priority(PositiveInt priority) {
                this.priority = priority;
                return this;
            }

            /**
             * Build the {@link Coverage}
             * 
             * <p>Required elements:
             * <ul>
             * <li>coverage</li>
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
                ValidationSupport.requireNonNull(coverage.coverage, "coverage");
                ValidationSupport.checkReferenceType(coverage.coverage, "coverage", "Coverage");
                ValidationSupport.requireValueOrChildren(coverage);
            }

            protected Builder from(Coverage coverage) {
                super.from(coverage);
                this.coverage = coverage.coverage;
                priority = coverage.priority;
                return this;
            }
        }
    }

    /**
     * The parties responsible for balancing the account if other payment options fall short.
     */
    public static class Guarantor extends BackboneElement {
        @ReferenceTarget({ "Patient", "RelatedPerson", "Organization" })
        @Required
        private final Reference party;
        private final Boolean onHold;
        private final Period period;

        private Guarantor(Builder builder) {
            super(builder);
            party = builder.party;
            onHold = builder.onHold;
            period = builder.period;
        }

        /**
         * The entity who is responsible.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getParty() {
            return party;
        }

        /**
         * A guarantor may be placed on credit hold or otherwise have their role temporarily suspended.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getOnHold() {
            return onHold;
        }

        /**
         * The timeframe during which the guarantor accepts responsibility for the account.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (party != null) || 
                (onHold != null) || 
                (period != null);
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
                    accept(party, "party", visitor);
                    accept(onHold, "onHold", visitor);
                    accept(period, "period", visitor);
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
            Guarantor other = (Guarantor) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(party, other.party) && 
                Objects.equals(onHold, other.onHold) && 
                Objects.equals(period, other.period);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    party, 
                    onHold, 
                    period);
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
            private Reference party;
            private Boolean onHold;
            private Period period;

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
             * The entity who is responsible.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Patient}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param party
             *     Responsible entity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder party(Reference party) {
                this.party = party;
                return this;
            }

            /**
             * Convenience method for setting {@code onHold}.
             * 
             * @param onHold
             *     Credit or other hold applied
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #onHold(org.linuxforhealth.fhir.model.type.Boolean)
             */
            public Builder onHold(java.lang.Boolean onHold) {
                this.onHold = (onHold == null) ? null : Boolean.of(onHold);
                return this;
            }

            /**
             * A guarantor may be placed on credit hold or otherwise have their role temporarily suspended.
             * 
             * @param onHold
             *     Credit or other hold applied
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder onHold(Boolean onHold) {
                this.onHold = onHold;
                return this;
            }

            /**
             * The timeframe during which the guarantor accepts responsibility for the account.
             * 
             * @param period
             *     Guarantee account during
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * Build the {@link Guarantor}
             * 
             * <p>Required elements:
             * <ul>
             * <li>party</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Guarantor}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Guarantor per the base specification
             */
            @Override
            public Guarantor build() {
                Guarantor guarantor = new Guarantor(this);
                if (validating) {
                    validate(guarantor);
                }
                return guarantor;
            }

            protected void validate(Guarantor guarantor) {
                super.validate(guarantor);
                ValidationSupport.requireNonNull(guarantor.party, "party");
                ValidationSupport.checkReferenceType(guarantor.party, "party", "Patient", "RelatedPerson", "Organization");
                ValidationSupport.requireValueOrChildren(guarantor);
            }

            protected Builder from(Guarantor guarantor) {
                super.from(guarantor);
                party = guarantor.party;
                onHold = guarantor.onHold;
                period = guarantor.period;
                return this;
            }
        }
    }
}
