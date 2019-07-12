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

import com.ibm.watsonhealth.fhir.model.type.AccountStatus;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A financial tool for tracking value accrued for a particular purpose. In the healthcare field, used to track charges 
 * for a patient, cost centers, etc.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Account extends DomainResource {
    private final List<Identifier> identifier;
    private final AccountStatus status;
    private final CodeableConcept type;
    private final String name;
    private final List<Reference> subject;
    private final Period servicePeriod;
    private final List<Coverage> coverage;
    private final Reference owner;
    private final String description;
    private final List<Guarantor> guarantor;
    private final Reference partOf;

    private volatile int hashCode;

    private Account(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
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
     * <p>
     * Unique identifier used to reference the account. Might or might not be intended for human use (e.g. credit card 
     * number).
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
     * Indicates whether the account is presently used/usable or not.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link AccountStatus}.
     */
    public AccountStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Categorizes the account for reporting and searching purposes.
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
     * Name used for the account when displaying it to humans in reports, etc.
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
     * Identifies the entity which incurs the expenses. While the immediate recipients of services or goods might be entities 
     * related to the subject, the expenses were ultimately incurred by the subject of the Account.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSubject() {
        return subject;
    }

    /**
     * <p>
     * The date range of services associated with this account.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getServicePeriod() {
        return servicePeriod;
    }

    /**
     * <p>
     * The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
     * the account.
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
     * Indicates the service area, hospital, department, etc. with responsibility for managing the Account.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOwner() {
        return owner;
    }

    /**
     * <p>
     * Provides additional information about what the account tracks and how it is used.
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
     * The parties responsible for balancing the account if other payment options fall short.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Guarantor}.
     */
    public List<Guarantor> getGuarantor() {
        return guarantor;
    }

    /**
     * <p>
     * Reference to a parent Account.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPartOf() {
        return partOf;
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
        return new Builder(status).from(this);
    }

    public Builder toBuilder(AccountStatus status) {
        return new Builder(status).from(this);
    }

    public static Builder builder(AccountStatus status) {
        return new Builder(status);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final AccountStatus status;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept type;
        private String name;
        private List<Reference> subject = new ArrayList<>();
        private Period servicePeriod;
        private List<Coverage> coverage = new ArrayList<>();
        private Reference owner;
        private String description;
        private List<Guarantor> guarantor = new ArrayList<>();
        private Reference partOf;

        private Builder(AccountStatus status) {
            super();
            this.status = status;
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Unique identifier used to reference the account. Might or might not be intended for human use (e.g. credit card 
         * number).
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Unique identifier used to reference the account. Might or might not be intended for human use (e.g. credit card 
         * number).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Account number
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
         * Categorizes the account for reporting and searching purposes.
         * </p>
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
         * <p>
         * Name used for the account when displaying it to humans in reports, etc.
         * </p>
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
         * <p>
         * Identifies the entity which incurs the expenses. While the immediate recipients of services or goods might be entities 
         * related to the subject, the expenses were ultimately incurred by the subject of the Account.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Identifies the entity which incurs the expenses. While the immediate recipients of services or goods might be entities 
         * related to the subject, the expenses were ultimately incurred by the subject of the Account.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param subject
         *     The entity that caused the expenses
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Collection<Reference> subject) {
            this.subject = new ArrayList<>(subject);
            return this;
        }

        /**
         * <p>
         * The date range of services associated with this account.
         * </p>
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
         * <p>
         * The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
         * the account.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
         * the account.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param coverage
         *     The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
         *     the account
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
         * Indicates the service area, hospital, department, etc. with responsibility for managing the Account.
         * </p>
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
         * <p>
         * Provides additional information about what the account tracks and how it is used.
         * </p>
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
         * <p>
         * The parties responsible for balancing the account if other payment options fall short.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The parties responsible for balancing the account if other payment options fall short.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param guarantor
         *     The parties ultimately responsible for balancing the Account
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder guarantor(Collection<Guarantor> guarantor) {
            this.guarantor = new ArrayList<>(guarantor);
            return this;
        }

        /**
         * <p>
         * Reference to a parent Account.
         * </p>
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

        @Override
        public Account build() {
            return new Account(this);
        }

        private Builder from(Account account) {
            id = account.id;
            meta = account.meta;
            implicitRules = account.implicitRules;
            language = account.language;
            text = account.text;
            contained.addAll(account.contained);
            extension.addAll(account.extension);
            modifierExtension.addAll(account.modifierExtension);
            identifier.addAll(account.identifier);
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
     * <p>
     * The party(s) that are responsible for covering the payment of this account, and what order should they be applied to 
     * the account.
     * </p>
     */
    public static class Coverage extends BackboneElement {
        private final Reference coverage;
        private final PositiveInt priority;

        private volatile int hashCode;

        private Coverage(Builder builder) {
            super(builder);
            coverage = ValidationSupport.requireNonNull(builder.coverage, "coverage");
            priority = builder.priority;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The party(s) that contribute to payment (or part of) of the charges applied to this account (including self-pay).
         * </p>
         * <p>
         * A coverage may only be responsible for specific types of charges, and the sequence of the coverages in the account 
         * could be important when processing billing.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getCoverage() {
            return coverage;
        }

        /**
         * <p>
         * The priority of the coverage in the context of this account.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(coverage, "coverage", visitor);
                    accept(priority, "priority", visitor);
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
            return new Builder(coverage).from(this);
        }

        public Builder toBuilder(Reference coverage) {
            return new Builder(coverage).from(this);
        }

        public static Builder builder(Reference coverage) {
            return new Builder(coverage);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference coverage;

            // optional
            private PositiveInt priority;

            private Builder(Reference coverage) {
                super();
                this.coverage = coverage;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * The priority of the coverage in the context of this account.
             * </p>
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

            @Override
            public Coverage build() {
                return new Coverage(this);
            }

            private Builder from(Coverage coverage) {
                id = coverage.id;
                extension.addAll(coverage.extension);
                modifierExtension.addAll(coverage.modifierExtension);
                priority = coverage.priority;
                return this;
            }
        }
    }

    /**
     * <p>
     * The parties responsible for balancing the account if other payment options fall short.
     * </p>
     */
    public static class Guarantor extends BackboneElement {
        private final Reference party;
        private final Boolean onHold;
        private final Period period;

        private volatile int hashCode;

        private Guarantor(Builder builder) {
            super(builder);
            party = ValidationSupport.requireNonNull(builder.party, "party");
            onHold = builder.onHold;
            period = builder.period;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The entity who is responsible.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getParty() {
            return party;
        }

        /**
         * <p>
         * A guarantor may be placed on credit hold or otherwise have their role temporarily suspended.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getOnHold() {
            return onHold;
        }

        /**
         * <p>
         * The timeframe during which the guarantor accepts responsibility for the account.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(party, "party", visitor);
                    accept(onHold, "onHold", visitor);
                    accept(period, "period", visitor);
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
            return new Builder(party).from(this);
        }

        public Builder toBuilder(Reference party) {
            return new Builder(party).from(this);
        }

        public static Builder builder(Reference party) {
            return new Builder(party);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference party;

            // optional
            private Boolean onHold;
            private Period period;

            private Builder(Reference party) {
                super();
                this.party = party;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * A guarantor may be placed on credit hold or otherwise have their role temporarily suspended.
             * </p>
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
             * <p>
             * The timeframe during which the guarantor accepts responsibility for the account.
             * </p>
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

            @Override
            public Guarantor build() {
                return new Guarantor(this);
            }

            private Builder from(Guarantor guarantor) {
                id = guarantor.id;
                extension.addAll(guarantor.extension);
                modifierExtension.addAll(guarantor.modifierExtension);
                onHold = guarantor.onHold;
                period = guarantor.period;
                return this;
            }
        }
    }
}
