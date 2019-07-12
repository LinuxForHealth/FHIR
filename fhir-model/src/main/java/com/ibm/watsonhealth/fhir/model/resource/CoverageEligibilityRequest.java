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

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.EligibilityRequestPurpose;
import com.ibm.watsonhealth.fhir.model.type.EligibilityRequestStatus;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, 
 * in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-
 * force and optionally to provide the insurance details of the policy.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class CoverageEligibilityRequest extends DomainResource {
    private final List<Identifier> identifier;
    private final EligibilityRequestStatus status;
    private final CodeableConcept priority;
    private final List<EligibilityRequestPurpose> purpose;
    private final Reference patient;
    private final Element serviced;
    private final DateTime created;
    private final Reference enterer;
    private final Reference provider;
    private final Reference insurer;
    private final Reference facility;
    private final List<SupportingInfo> supportingInfo;
    private final List<Insurance> insurance;
    private final List<Item> item;

    private volatile int hashCode;

    private CoverageEligibilityRequest(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        priority = builder.priority;
        purpose = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.purpose, "purpose"));
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        serviced = ValidationSupport.choiceElement(builder.serviced, "serviced", Date.class, Period.class);
        created = ValidationSupport.requireNonNull(builder.created, "created");
        enterer = builder.enterer;
        provider = builder.provider;
        insurer = ValidationSupport.requireNonNull(builder.insurer, "insurer");
        facility = builder.facility;
        supportingInfo = Collections.unmodifiableList(builder.supportingInfo);
        insurance = Collections.unmodifiableList(builder.insurance);
        item = Collections.unmodifiableList(builder.item);
    }

    /**
     * <p>
     * A unique identifier assigned to this coverage eligiblity request.
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
     * The status of the resource instance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link EligibilityRequestStatus}.
     */
    public EligibilityRequestStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * When the requestor expects the processor to complete processing.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getPriority() {
        return priority;
    }

    /**
     * <p>
     * Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; 
     * benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation 
     * that the specified coverage is in-force at the date/period specified or 'now' if not specified.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link EligibilityRequestPurpose}.
     */
    public List<EligibilityRequestPurpose> getPurpose() {
        return purpose;
    }

    /**
     * <p>
     * The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * <p>
     * The date or dates when the enclosed suite of services were performed or completed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getServiced() {
        return serviced;
    }

    /**
     * <p>
     * The date when this resource was created.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * <p>
     * Person who created the request.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getEnterer() {
        return enterer;
    }

    /**
     * <p>
     * The provider which is responsible for the request.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getProvider() {
        return provider;
    }

    /**
     * <p>
     * The Insurer who issued the coverage in question and is the recipient of the request.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getInsurer() {
        return insurer;
    }

    /**
     * <p>
     * Facility where the services are intended to be provided.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getFacility() {
        return facility;
    }

    /**
     * <p>
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
     * concurrent issues.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link SupportingInfo}.
     */
    public List<SupportingInfo> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * <p>
     * Financial instruments for reimbursement for the health care products and services.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Insurance}.
     */
    public List<Insurance> getInsurance() {
        return insurance;
    }

    /**
     * <p>
     * Service categories or billable services for which benefit details and/or an authorization prior to service delivery 
     * may be required by the payor.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Item}.
     */
    public List<Item> getItem() {
        return item;
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
                accept(priority, "priority", visitor);
                accept(purpose, "purpose", visitor, EligibilityRequestPurpose.class);
                accept(patient, "patient", visitor);
                accept(serviced, "serviced", visitor);
                accept(created, "created", visitor);
                accept(enterer, "enterer", visitor);
                accept(provider, "provider", visitor);
                accept(insurer, "insurer", visitor);
                accept(facility, "facility", visitor);
                accept(supportingInfo, "supportingInfo", visitor, SupportingInfo.class);
                accept(insurance, "insurance", visitor, Insurance.class);
                accept(item, "item", visitor, Item.class);
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
        CoverageEligibilityRequest other = (CoverageEligibilityRequest) obj;
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
            Objects.equals(priority, other.priority) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(serviced, other.serviced) && 
            Objects.equals(created, other.created) && 
            Objects.equals(enterer, other.enterer) && 
            Objects.equals(provider, other.provider) && 
            Objects.equals(insurer, other.insurer) && 
            Objects.equals(facility, other.facility) && 
            Objects.equals(supportingInfo, other.supportingInfo) && 
            Objects.equals(insurance, other.insurance) && 
            Objects.equals(item, other.item);
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
                priority, 
                purpose, 
                patient, 
                serviced, 
                created, 
                enterer, 
                provider, 
                insurer, 
                facility, 
                supportingInfo, 
                insurance, 
                item);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status, purpose, patient, created, insurer).from(this);
    }

    public Builder toBuilder(EligibilityRequestStatus status, List<EligibilityRequestPurpose> purpose, Reference patient, DateTime created, Reference insurer) {
        return new Builder(status, purpose, patient, created, insurer).from(this);
    }

    public static Builder builder(EligibilityRequestStatus status, List<EligibilityRequestPurpose> purpose, Reference patient, DateTime created, Reference insurer) {
        return new Builder(status, purpose, patient, created, insurer);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final EligibilityRequestStatus status;
        private final List<EligibilityRequestPurpose> purpose;
        private final Reference patient;
        private final DateTime created;
        private final Reference insurer;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept priority;
        private Element serviced;
        private Reference enterer;
        private Reference provider;
        private Reference facility;
        private List<SupportingInfo> supportingInfo = new ArrayList<>();
        private List<Insurance> insurance = new ArrayList<>();
        private List<Item> item = new ArrayList<>();

        private Builder(EligibilityRequestStatus status, List<EligibilityRequestPurpose> purpose, Reference patient, DateTime created, Reference insurer) {
            super();
            this.status = status;
            this.purpose = purpose;
            this.patient = patient;
            this.created = created;
            this.insurer = insurer;
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
         * A unique identifier assigned to this coverage eligiblity request.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param identifier
         *     Business Identifier for coverage eligiblity request
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
         * A unique identifier assigned to this coverage eligiblity request.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Business Identifier for coverage eligiblity request
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
         * When the requestor expects the processor to complete processing.
         * </p>
         * 
         * @param priority
         *     Desired processing priority
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priority(CodeableConcept priority) {
            this.priority = priority;
            return this;
        }

        /**
         * <p>
         * The date or dates when the enclosed suite of services were performed or completed.
         * </p>
         * 
         * @param serviced
         *     Estimated date or dates of service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviced(Element serviced) {
            this.serviced = serviced;
            return this;
        }

        /**
         * <p>
         * Person who created the request.
         * </p>
         * 
         * @param enterer
         *     Author
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder enterer(Reference enterer) {
            this.enterer = enterer;
            return this;
        }

        /**
         * <p>
         * The provider which is responsible for the request.
         * </p>
         * 
         * @param provider
         *     Party responsible for the request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder provider(Reference provider) {
            this.provider = provider;
            return this;
        }

        /**
         * <p>
         * Facility where the services are intended to be provided.
         * </p>
         * 
         * @param facility
         *     Servicing facility
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder facility(Reference facility) {
            this.facility = facility;
            return this;
        }

        /**
         * <p>
         * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
         * concurrent issues.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param supportingInfo
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInfo(SupportingInfo... supportingInfo) {
            for (SupportingInfo value : supportingInfo) {
                this.supportingInfo.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
         * concurrent issues.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param supportingInfo
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInfo(Collection<SupportingInfo> supportingInfo) {
            this.supportingInfo = new ArrayList<>(supportingInfo);
            return this;
        }

        /**
         * <p>
         * Financial instruments for reimbursement for the health care products and services.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Insurance... insurance) {
            for (Insurance value : insurance) {
                this.insurance.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Financial instruments for reimbursement for the health care products and services.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Collection<Insurance> insurance) {
            this.insurance = new ArrayList<>(insurance);
            return this;
        }

        /**
         * <p>
         * Service categories or billable services for which benefit details and/or an authorization prior to service delivery 
         * may be required by the payor.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param item
         *     Item to be evaluated for eligibiity
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder item(Item... item) {
            for (Item value : item) {
                this.item.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Service categories or billable services for which benefit details and/or an authorization prior to service delivery 
         * may be required by the payor.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param item
         *     Item to be evaluated for eligibiity
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder item(Collection<Item> item) {
            this.item = new ArrayList<>(item);
            return this;
        }

        @Override
        public CoverageEligibilityRequest build() {
            return new CoverageEligibilityRequest(this);
        }

        private Builder from(CoverageEligibilityRequest coverageEligibilityRequest) {
            id = coverageEligibilityRequest.id;
            meta = coverageEligibilityRequest.meta;
            implicitRules = coverageEligibilityRequest.implicitRules;
            language = coverageEligibilityRequest.language;
            text = coverageEligibilityRequest.text;
            contained.addAll(coverageEligibilityRequest.contained);
            extension.addAll(coverageEligibilityRequest.extension);
            modifierExtension.addAll(coverageEligibilityRequest.modifierExtension);
            identifier.addAll(coverageEligibilityRequest.identifier);
            priority = coverageEligibilityRequest.priority;
            serviced = coverageEligibilityRequest.serviced;
            enterer = coverageEligibilityRequest.enterer;
            provider = coverageEligibilityRequest.provider;
            facility = coverageEligibilityRequest.facility;
            supportingInfo.addAll(coverageEligibilityRequest.supportingInfo);
            insurance.addAll(coverageEligibilityRequest.insurance);
            item.addAll(coverageEligibilityRequest.item);
            return this;
        }
    }

    /**
     * <p>
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
     * concurrent issues.
     * </p>
     */
    public static class SupportingInfo extends BackboneElement {
        private final PositiveInt sequence;
        private final Reference information;
        private final Boolean appliesToAll;

        private volatile int hashCode;

        private SupportingInfo(Builder builder) {
            super(builder);
            sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            information = ValidationSupport.requireNonNull(builder.information, "information");
            appliesToAll = builder.appliesToAll;
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * A number to uniquely identify supporting information entries.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * <p>
         * Additional data or information such as resources, documents, images etc. including references to the data or the 
         * actual inclusion of the data.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getInformation() {
            return information;
        }

        /**
         * <p>
         * The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getAppliesToAll() {
            return appliesToAll;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (sequence != null) || 
                (information != null) || 
                (appliesToAll != null);
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
                    accept(sequence, "sequence", visitor);
                    accept(information, "information", visitor);
                    accept(appliesToAll, "appliesToAll", visitor);
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
            SupportingInfo other = (SupportingInfo) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(information, other.information) && 
                Objects.equals(appliesToAll, other.appliesToAll);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    sequence, 
                    information, 
                    appliesToAll);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(sequence, information).from(this);
        }

        public Builder toBuilder(PositiveInt sequence, Reference information) {
            return new Builder(sequence, information).from(this);
        }

        public static Builder builder(PositiveInt sequence, Reference information) {
            return new Builder(sequence, information);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final PositiveInt sequence;
            private final Reference information;

            // optional
            private Boolean appliesToAll;

            private Builder(PositiveInt sequence, Reference information) {
                super();
                this.sequence = sequence;
                this.information = information;
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
             * The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.
             * </p>
             * 
             * @param appliesToAll
             *     Applies to all items
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder appliesToAll(Boolean appliesToAll) {
                this.appliesToAll = appliesToAll;
                return this;
            }

            @Override
            public SupportingInfo build() {
                return new SupportingInfo(this);
            }

            private Builder from(SupportingInfo supportingInfo) {
                id = supportingInfo.id;
                extension.addAll(supportingInfo.extension);
                modifierExtension.addAll(supportingInfo.modifierExtension);
                appliesToAll = supportingInfo.appliesToAll;
                return this;
            }
        }
    }

    /**
     * <p>
     * Financial instruments for reimbursement for the health care products and services.
     * </p>
     */
    public static class Insurance extends BackboneElement {
        private final Boolean focal;
        private final Reference coverage;
        private final String businessArrangement;

        private volatile int hashCode;

        private Insurance(Builder builder) {
            super(builder);
            focal = builder.focal;
            coverage = ValidationSupport.requireNonNull(builder.coverage, "coverage");
            businessArrangement = builder.businessArrangement;
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getFocal() {
            return focal;
        }

        /**
         * <p>
         * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer 
         * will use these details to locate the patient's actual coverage within the insurer's information system.
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
         * A business agreement number established between the provider and the insurer for special business processing purposes.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getBusinessArrangement() {
            return businessArrangement;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (focal != null) || 
                (coverage != null) || 
                (businessArrangement != null);
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
                    accept(focal, "focal", visitor);
                    accept(coverage, "coverage", visitor);
                    accept(businessArrangement, "businessArrangement", visitor);
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
            Insurance other = (Insurance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(focal, other.focal) && 
                Objects.equals(coverage, other.coverage) && 
                Objects.equals(businessArrangement, other.businessArrangement);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    focal, 
                    coverage, 
                    businessArrangement);
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
            private Boolean focal;
            private String businessArrangement;

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
             * A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.
             * </p>
             * 
             * @param focal
             *     Applicable coverage
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder focal(Boolean focal) {
                this.focal = focal;
                return this;
            }

            /**
             * <p>
             * A business agreement number established between the provider and the insurer for special business processing purposes.
             * </p>
             * 
             * @param businessArrangement
             *     Additional provider contract number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder businessArrangement(String businessArrangement) {
                this.businessArrangement = businessArrangement;
                return this;
            }

            @Override
            public Insurance build() {
                return new Insurance(this);
            }

            private Builder from(Insurance insurance) {
                id = insurance.id;
                extension.addAll(insurance.extension);
                modifierExtension.addAll(insurance.modifierExtension);
                focal = insurance.focal;
                businessArrangement = insurance.businessArrangement;
                return this;
            }
        }
    }

    /**
     * <p>
     * Service categories or billable services for which benefit details and/or an authorization prior to service delivery 
     * may be required by the payor.
     * </p>
     */
    public static class Item extends BackboneElement {
        private final List<PositiveInt> supportingInfoSequence;
        private final CodeableConcept category;
        private final CodeableConcept productOrService;
        private final List<CodeableConcept> modifier;
        private final Reference provider;
        private final Quantity quantity;
        private final Money unitPrice;
        private final Reference facility;
        private final List<Diagnosis> diagnosis;
        private final List<Reference> detail;

        private volatile int hashCode;

        private Item(Builder builder) {
            super(builder);
            supportingInfoSequence = Collections.unmodifiableList(builder.supportingInfoSequence);
            category = builder.category;
            productOrService = builder.productOrService;
            modifier = Collections.unmodifiableList(builder.modifier);
            provider = builder.provider;
            quantity = builder.quantity;
            unitPrice = builder.unitPrice;
            facility = builder.facility;
            diagnosis = Collections.unmodifiableList(builder.diagnosis);
            detail = Collections.unmodifiableList(builder.detail);
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Exceptions, special conditions and supporting information applicable for this service or product line.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getSupportingInfoSequence() {
            return supportingInfoSequence;
        }

        /**
         * <p>
         * Code to identify the general type of benefits under which products and services are provided.
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
         * This contains the product, service, drug or other billing code for the item.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getProductOrService() {
            return productOrService;
        }

        /**
         * <p>
         * Item typification or modifiers codes to convey additional context for the product or service.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getModifier() {
            return modifier;
        }

        /**
         * <p>
         * The practitioner who is responsible for the product or service to be rendered to the patient.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getProvider() {
            return provider;
        }

        /**
         * <p>
         * The number of repetitions of a service or product.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getQuantity() {
            return quantity;
        }

        /**
         * <p>
         * The amount charged to the patient by the provider for a single unit.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getUnitPrice() {
            return unitPrice;
        }

        /**
         * <p>
         * Facility where the services will be provided.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getFacility() {
            return facility;
        }

        /**
         * <p>
         * Patient diagnosis for which care is sought.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Diagnosis}.
         */
        public List<Diagnosis> getDiagnosis() {
            return diagnosis;
        }

        /**
         * <p>
         * The plan/proposal/order describing the proposed service in detail.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getDetail() {
            return detail;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                !supportingInfoSequence.isEmpty() || 
                (category != null) || 
                (productOrService != null) || 
                !modifier.isEmpty() || 
                (provider != null) || 
                (quantity != null) || 
                (unitPrice != null) || 
                (facility != null) || 
                !diagnosis.isEmpty() || 
                !detail.isEmpty();
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
                    accept(supportingInfoSequence, "supportingInfoSequence", visitor, PositiveInt.class);
                    accept(category, "category", visitor);
                    accept(productOrService, "productOrService", visitor);
                    accept(modifier, "modifier", visitor, CodeableConcept.class);
                    accept(provider, "provider", visitor);
                    accept(quantity, "quantity", visitor);
                    accept(unitPrice, "unitPrice", visitor);
                    accept(facility, "facility", visitor);
                    accept(diagnosis, "diagnosis", visitor, Diagnosis.class);
                    accept(detail, "detail", visitor, Reference.class);
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
            Item other = (Item) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(supportingInfoSequence, other.supportingInfoSequence) && 
                Objects.equals(category, other.category) && 
                Objects.equals(productOrService, other.productOrService) && 
                Objects.equals(modifier, other.modifier) && 
                Objects.equals(provider, other.provider) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(unitPrice, other.unitPrice) && 
                Objects.equals(facility, other.facility) && 
                Objects.equals(diagnosis, other.diagnosis) && 
                Objects.equals(detail, other.detail);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    supportingInfoSequence, 
                    category, 
                    productOrService, 
                    modifier, 
                    provider, 
                    quantity, 
                    unitPrice, 
                    facility, 
                    diagnosis, 
                    detail);
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
            private List<PositiveInt> supportingInfoSequence = new ArrayList<>();
            private CodeableConcept category;
            private CodeableConcept productOrService;
            private List<CodeableConcept> modifier = new ArrayList<>();
            private Reference provider;
            private Quantity quantity;
            private Money unitPrice;
            private Reference facility;
            private List<Diagnosis> diagnosis = new ArrayList<>();
            private List<Reference> detail = new ArrayList<>();

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
             * Exceptions, special conditions and supporting information applicable for this service or product line.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param supportingInfoSequence
             *     Applicable exception or supporting information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder supportingInfoSequence(PositiveInt... supportingInfoSequence) {
                for (PositiveInt value : supportingInfoSequence) {
                    this.supportingInfoSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Exceptions, special conditions and supporting information applicable for this service or product line.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param supportingInfoSequence
             *     Applicable exception or supporting information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder supportingInfoSequence(Collection<PositiveInt> supportingInfoSequence) {
                this.supportingInfoSequence = new ArrayList<>(supportingInfoSequence);
                return this;
            }

            /**
             * <p>
             * Code to identify the general type of benefits under which products and services are provided.
             * </p>
             * 
             * @param category
             *     Benefit classification
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder category(CodeableConcept category) {
                this.category = category;
                return this;
            }

            /**
             * <p>
             * This contains the product, service, drug or other billing code for the item.
             * </p>
             * 
             * @param productOrService
             *     Billing, service, product, or drug code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder productOrService(CodeableConcept productOrService) {
                this.productOrService = productOrService;
                return this;
            }

            /**
             * <p>
             * Item typification or modifiers codes to convey additional context for the product or service.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param modifier
             *     Product or service billing modifiers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder modifier(CodeableConcept... modifier) {
                for (CodeableConcept value : modifier) {
                    this.modifier.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Item typification or modifiers codes to convey additional context for the product or service.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifier
             *     Product or service billing modifiers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder modifier(Collection<CodeableConcept> modifier) {
                this.modifier = new ArrayList<>(modifier);
                return this;
            }

            /**
             * <p>
             * The practitioner who is responsible for the product or service to be rendered to the patient.
             * </p>
             * 
             * @param provider
             *     Perfoming practitioner
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder provider(Reference provider) {
                this.provider = provider;
                return this;
            }

            /**
             * <p>
             * The number of repetitions of a service or product.
             * </p>
             * 
             * @param quantity
             *     Count of products or services
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(Quantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * <p>
             * The amount charged to the patient by the provider for a single unit.
             * </p>
             * 
             * @param unitPrice
             *     Fee, charge or cost per item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder unitPrice(Money unitPrice) {
                this.unitPrice = unitPrice;
                return this;
            }

            /**
             * <p>
             * Facility where the services will be provided.
             * </p>
             * 
             * @param facility
             *     Servicing facility
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder facility(Reference facility) {
                this.facility = facility;
                return this;
            }

            /**
             * <p>
             * Patient diagnosis for which care is sought.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param diagnosis
             *     Applicable diagnosis
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diagnosis(Diagnosis... diagnosis) {
                for (Diagnosis value : diagnosis) {
                    this.diagnosis.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Patient diagnosis for which care is sought.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param diagnosis
             *     Applicable diagnosis
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diagnosis(Collection<Diagnosis> diagnosis) {
                this.diagnosis = new ArrayList<>(diagnosis);
                return this;
            }

            /**
             * <p>
             * The plan/proposal/order describing the proposed service in detail.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param detail
             *     Product or service details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Reference... detail) {
                for (Reference value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The plan/proposal/order describing the proposed service in detail.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param detail
             *     Product or service details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Collection<Reference> detail) {
                this.detail = new ArrayList<>(detail);
                return this;
            }

            @Override
            public Item build() {
                return new Item(this);
            }

            private Builder from(Item item) {
                id = item.id;
                extension.addAll(item.extension);
                modifierExtension.addAll(item.modifierExtension);
                supportingInfoSequence.addAll(item.supportingInfoSequence);
                category = item.category;
                productOrService = item.productOrService;
                modifier.addAll(item.modifier);
                provider = item.provider;
                quantity = item.quantity;
                unitPrice = item.unitPrice;
                facility = item.facility;
                diagnosis.addAll(item.diagnosis);
                detail.addAll(item.detail);
                return this;
            }
        }

        /**
         * <p>
         * Patient diagnosis for which care is sought.
         * </p>
         */
        public static class Diagnosis extends BackboneElement {
            private final Element diagnosis;

            private volatile int hashCode;

            private Diagnosis(Builder builder) {
                super(builder);
                diagnosis = ValidationSupport.choiceElement(builder.diagnosis, "diagnosis", CodeableConcept.class, Reference.class);
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * The nature of illness or problem in a coded form or as a reference to an external defined Condition.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getDiagnosis() {
                return diagnosis;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (diagnosis != null);
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
                        accept(diagnosis, "diagnosis", visitor);
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
                Diagnosis other = (Diagnosis) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(diagnosis, other.diagnosis);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        diagnosis);
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
                private Element diagnosis;

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
                 * The nature of illness or problem in a coded form or as a reference to an external defined Condition.
                 * </p>
                 * 
                 * @param diagnosis
                 *     Nature of illness or problem
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder diagnosis(Element diagnosis) {
                    this.diagnosis = diagnosis;
                    return this;
                }

                @Override
                public Diagnosis build() {
                    return new Diagnosis(this);
                }

                private Builder from(Diagnosis diagnosis) {
                    id = diagnosis.id;
                    extension.addAll(diagnosis.extension);
                    modifierExtension.addAll(diagnosis.modifierExtension);
                    this.diagnosis = diagnosis.diagnosis;
                    return this;
                }
            }
        }
    }
}
