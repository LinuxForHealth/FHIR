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

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.CoverageStatus;
import com.ibm.watsonhealth.fhir.model.type.Element;
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
 * Financial instrument which may be used to reimburse or pay for health care products and services. Includes both 
 * insurance and self-payment.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Coverage extends DomainResource {
    private final List<Identifier> identifier;
    private final CoverageStatus status;
    private final CodeableConcept type;
    private final Reference policyHolder;
    private final Reference subscriber;
    private final String subscriberId;
    private final Reference beneficiary;
    private final String dependent;
    private final CodeableConcept relationship;
    private final Period period;
    private final List<Reference> payor;
    private final List<Class> clazz;
    private final PositiveInt order;
    private final String network;
    private final List<CostToBeneficiary> costToBeneficiary;
    private final Boolean subrogation;
    private final List<Reference> contract;

    private Coverage(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        type = builder.type;
        policyHolder = builder.policyHolder;
        subscriber = builder.subscriber;
        subscriberId = builder.subscriberId;
        beneficiary = ValidationSupport.requireNonNull(builder.beneficiary, "beneficiary");
        dependent = builder.dependent;
        relationship = builder.relationship;
        period = builder.period;
        payor = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.payor, "payor"));
        clazz = Collections.unmodifiableList(builder.clazz);
        order = builder.order;
        network = builder.network;
        costToBeneficiary = Collections.unmodifiableList(builder.costToBeneficiary);
        subrogation = builder.subrogation;
        contract = Collections.unmodifiableList(builder.contract);
    }

    /**
     * <p>
     * A unique identifier assigned to this coverage.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
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
     *     An immutable object of type {@link CoverageStatus}.
     */
    public CoverageStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health or 
     * payment by an individual or organization.
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
     * The party who 'owns' the insurance policy.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPolicyHolder() {
        return policyHolder;
    }

    /**
     * <p>
     * The party who has signed-up for or 'owns' the contractual relationship to the policy or to whom the benefit of the 
     * policy for services rendered to them or their family is due.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSubscriber() {
        return subscriber;
    }

    /**
     * <p>
     * The insurer assigned ID for the Subscriber.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getSubscriberId() {
        return subscriberId;
    }

    /**
     * <p>
     * The party who benefits from the insurance coverage; the patient when products and/or services are provided.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getBeneficiary() {
        return beneficiary;
    }

    /**
     * <p>
     * A unique identifier for a dependent under the coverage.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDependent() {
        return dependent;
    }

    /**
     * <p>
     * The relationship of beneficiary (patient) to the subscriber.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getRelationship() {
        return relationship;
    }

    /**
     * <p>
     * Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a 
     * missing end date means the coverage is continuing to be in force.
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
     * The program or plan underwriter or payor including both insurance and non-insurance agreements, such as patient-pay 
     * agreements.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPayor() {
        return payor;
    }

    /**
     * <p>
     * A suite of underwriter specific classifiers.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Class}.
     */
    public List<Class> getClazz() {
        return clazz;
    }

    /**
     * <p>
     * The order of applicability of this coverage relative to other coverages which are currently in force. Note, there may 
     * be gaps in the numbering and this does not imply primary, secondary etc. as the specific positioning of coverages 
     * depends upon the episode of care.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PositiveInt}.
     */
    public PositiveInt getOrder() {
        return order;
    }

    /**
     * <p>
     * The insurer-specific identifier for the insurer-defined network of providers to which the beneficiary may seek 
     * treatment which will be covered at the 'in-network' rate, otherwise 'out of network' terms and conditions apply.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getNetwork() {
        return network;
    }

    /**
     * <p>
     * A suite of codes indicating the cost category and associated amount which have been detailed in the policy and may 
     * have been included on the health card.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CostToBeneficiary}.
     */
    public List<CostToBeneficiary> getCostToBeneficiary() {
        return costToBeneficiary;
    }

    /**
     * <p>
     * When 'subrogation=true' this insurance instance has been included not for adjudication but to provide insurers with 
     * the details to recover costs.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getSubrogation() {
        return subrogation;
    }

    /**
     * <p>
     * The policy(s) which constitute this insurance coverage.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getContract() {
        return contract;
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
                accept(policyHolder, "policyHolder", visitor);
                accept(subscriber, "subscriber", visitor);
                accept(subscriberId, "subscriberId", visitor);
                accept(beneficiary, "beneficiary", visitor);
                accept(dependent, "dependent", visitor);
                accept(relationship, "relationship", visitor);
                accept(period, "period", visitor);
                accept(payor, "payor", visitor, Reference.class);
                accept(clazz, "class", visitor, Class.class);
                accept(order, "order", visitor);
                accept(network, "network", visitor);
                accept(costToBeneficiary, "costToBeneficiary", visitor, CostToBeneficiary.class);
                accept(subrogation, "subrogation", visitor);
                accept(contract, "contract", visitor, Reference.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, beneficiary, payor);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.type = type;
        builder.policyHolder = policyHolder;
        builder.subscriber = subscriber;
        builder.subscriberId = subscriberId;
        builder.dependent = dependent;
        builder.relationship = relationship;
        builder.period = period;
        builder.clazz.addAll(clazz);
        builder.order = order;
        builder.network = network;
        builder.costToBeneficiary.addAll(costToBeneficiary);
        builder.subrogation = subrogation;
        builder.contract.addAll(contract);
        return builder;
    }

    public static Builder builder(CoverageStatus status, Reference beneficiary, List<Reference> payor) {
        return new Builder(status, beneficiary, payor);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final CoverageStatus status;
        private final Reference beneficiary;
        private final List<Reference> payor;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept type;
        private Reference policyHolder;
        private Reference subscriber;
        private String subscriberId;
        private String dependent;
        private CodeableConcept relationship;
        private Period period;
        private List<Class> clazz = new ArrayList<>();
        private PositiveInt order;
        private String network;
        private List<CostToBeneficiary> costToBeneficiary = new ArrayList<>();
        private Boolean subrogation;
        private List<Reference> contract = new ArrayList<>();

        private Builder(CoverageStatus status, Reference beneficiary, List<Reference> payor) {
            super();
            this.status = status;
            this.beneficiary = beneficiary;
            this.payor = payor;
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * A unique identifier assigned to this coverage.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for the coverage
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A unique identifier assigned to this coverage.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for the coverage
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier.addAll(identifier);
            return this;
        }

        /**
         * <p>
         * The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health or 
         * payment by an individual or organization.
         * </p>
         * 
         * @param type
         *     Coverage category such as medical or accident
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * <p>
         * The party who 'owns' the insurance policy.
         * </p>
         * 
         * @param policyHolder
         *     Owner of the policy
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder policyHolder(Reference policyHolder) {
            this.policyHolder = policyHolder;
            return this;
        }

        /**
         * <p>
         * The party who has signed-up for or 'owns' the contractual relationship to the policy or to whom the benefit of the 
         * policy for services rendered to them or their family is due.
         * </p>
         * 
         * @param subscriber
         *     Subscriber to the policy
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subscriber(Reference subscriber) {
            this.subscriber = subscriber;
            return this;
        }

        /**
         * <p>
         * The insurer assigned ID for the Subscriber.
         * </p>
         * 
         * @param subscriberId
         *     ID assigned to the subscriber
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subscriberId(String subscriberId) {
            this.subscriberId = subscriberId;
            return this;
        }

        /**
         * <p>
         * A unique identifier for a dependent under the coverage.
         * </p>
         * 
         * @param dependent
         *     Dependent number
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dependent(String dependent) {
            this.dependent = dependent;
            return this;
        }

        /**
         * <p>
         * The relationship of beneficiary (patient) to the subscriber.
         * </p>
         * 
         * @param relationship
         *     Beneficiary relationship to the subscriber
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relationship(CodeableConcept relationship) {
            this.relationship = relationship;
            return this;
        }

        /**
         * <p>
         * Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a 
         * missing end date means the coverage is continuing to be in force.
         * </p>
         * 
         * @param period
         *     Coverage start and end dates
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * <p>
         * The program or plan underwriter or payor including both insurance and non-insurance agreements, such as patient-pay 
         * agreements.
         * </p>
         * 
         * @param payor
         *     Issuer of the policy
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder payor(Reference... payor) {
            for (Reference value : payor) {
                this.payor.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The program or plan underwriter or payor including both insurance and non-insurance agreements, such as patient-pay 
         * agreements.
         * </p>
         * 
         * @param payor
         *     Issuer of the policy
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder payor(Collection<Reference> payor) {
            this.payor.addAll(payor);
            return this;
        }

        /**
         * <p>
         * A suite of underwriter specific classifiers.
         * </p>
         * 
         * @param clazz
         *     Additional coverage classifications
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder clazz(Class... clazz) {
            for (Class value : clazz) {
                this.clazz.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A suite of underwriter specific classifiers.
         * </p>
         * 
         * @param clazz
         *     Additional coverage classifications
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder clazz(Collection<Class> clazz) {
            this.clazz.addAll(clazz);
            return this;
        }

        /**
         * <p>
         * The order of applicability of this coverage relative to other coverages which are currently in force. Note, there may 
         * be gaps in the numbering and this does not imply primary, secondary etc. as the specific positioning of coverages 
         * depends upon the episode of care.
         * </p>
         * 
         * @param order
         *     Relative order of the coverage
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder order(PositiveInt order) {
            this.order = order;
            return this;
        }

        /**
         * <p>
         * The insurer-specific identifier for the insurer-defined network of providers to which the beneficiary may seek 
         * treatment which will be covered at the 'in-network' rate, otherwise 'out of network' terms and conditions apply.
         * </p>
         * 
         * @param network
         *     Insurer network
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder network(String network) {
            this.network = network;
            return this;
        }

        /**
         * <p>
         * A suite of codes indicating the cost category and associated amount which have been detailed in the policy and may 
         * have been included on the health card.
         * </p>
         * 
         * @param costToBeneficiary
         *     Patient payments for services/products
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder costToBeneficiary(CostToBeneficiary... costToBeneficiary) {
            for (CostToBeneficiary value : costToBeneficiary) {
                this.costToBeneficiary.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A suite of codes indicating the cost category and associated amount which have been detailed in the policy and may 
         * have been included on the health card.
         * </p>
         * 
         * @param costToBeneficiary
         *     Patient payments for services/products
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder costToBeneficiary(Collection<CostToBeneficiary> costToBeneficiary) {
            this.costToBeneficiary.addAll(costToBeneficiary);
            return this;
        }

        /**
         * <p>
         * When 'subrogation=true' this insurance instance has been included not for adjudication but to provide insurers with 
         * the details to recover costs.
         * </p>
         * 
         * @param subrogation
         *     Reimbursement to insurer
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subrogation(Boolean subrogation) {
            this.subrogation = subrogation;
            return this;
        }

        /**
         * <p>
         * The policy(s) which constitute this insurance coverage.
         * </p>
         * 
         * @param contract
         *     Contract details
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contract(Reference... contract) {
            for (Reference value : contract) {
                this.contract.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The policy(s) which constitute this insurance coverage.
         * </p>
         * 
         * @param contract
         *     Contract details
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contract(Collection<Reference> contract) {
            this.contract.addAll(contract);
            return this;
        }

        @Override
        public Coverage build() {
            return new Coverage(this);
        }
    }

    /**
     * <p>
     * A suite of underwriter specific classifiers.
     * </p>
     */
    public static class Class extends BackboneElement {
        private final CodeableConcept type;
        private final String value;
        private final String name;

        private Class(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            value = ValidationSupport.requireNonNull(builder.value, "value");
            name = builder.name;
        }

        /**
         * <p>
         * The type of classification for which an insurer-specific class label or number and optional name is provided, for 
         * example may be used to identify a class of coverage or employer group, Policy, Plan.
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
         * The alphanumeric string value associated with the insurer issued label.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getValue() {
            return value;
        }

        /**
         * <p>
         * A short description for the class.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getName() {
            return name;
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
                    accept(value, "value", visitor);
                    accept(name, "name", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept type, String value) {
            return new Builder(type, value);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;
            private final String value;

            // optional
            private String name;

            private Builder(CodeableConcept type, String value) {
                super();
                this.type = type;
                this.value = value;
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
             *     A reference to this Builder instance.
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
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * A short description for the class.
             * </p>
             * 
             * @param name
             *     Human readable description of the type and value
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            @Override
            public Class build() {
                return new Class(this);
            }

            private static Builder from(Class _class) {
                Builder builder = new Builder(_class.type, _class.value);
                builder.name = _class.name;
                return builder;
            }
        }
    }

    /**
     * <p>
     * A suite of codes indicating the cost category and associated amount which have been detailed in the policy and may 
     * have been included on the health card.
     * </p>
     */
    public static class CostToBeneficiary extends BackboneElement {
        private final CodeableConcept type;
        private final Element value;
        private final List<Exception> exception;

        private CostToBeneficiary(Builder builder) {
            super(builder);
            type = builder.type;
            value = ValidationSupport.requireChoiceElement(builder.value, "value", Quantity.class, Money.class);
            exception = Collections.unmodifiableList(builder.exception);
        }

        /**
         * <p>
         * The category of patient centric costs associated with treatment.
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
         * The amount due from the patient for the cost category.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getValue() {
            return value;
        }

        /**
         * <p>
         * A suite of codes indicating exceptions or reductions to patient costs and their effective periods.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Exception}.
         */
        public List<Exception> getException() {
            return exception;
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
                    accept(value, "value", visitor, true);
                    accept(exception, "exception", visitor, Exception.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Element value) {
            return new Builder(value);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Element value;

            // optional
            private CodeableConcept type;
            private List<Exception> exception = new ArrayList<>();

            private Builder(Element value) {
                super();
                this.value = value;
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
             *     A reference to this Builder instance.
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
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * The category of patient centric costs associated with treatment.
             * </p>
             * 
             * @param type
             *     Cost category
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * <p>
             * A suite of codes indicating exceptions or reductions to patient costs and their effective periods.
             * </p>
             * 
             * @param exception
             *     Exceptions for patient payments
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder exception(Exception... exception) {
                for (Exception value : exception) {
                    this.exception.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A suite of codes indicating exceptions or reductions to patient costs and their effective periods.
             * </p>
             * 
             * @param exception
             *     Exceptions for patient payments
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder exception(Collection<Exception> exception) {
                this.exception.addAll(exception);
                return this;
            }

            @Override
            public CostToBeneficiary build() {
                return new CostToBeneficiary(this);
            }

            private static Builder from(CostToBeneficiary costToBeneficiary) {
                Builder builder = new Builder(costToBeneficiary.value);
                builder.type = costToBeneficiary.type;
                builder.exception.addAll(costToBeneficiary.exception);
                return builder;
            }
        }

        /**
         * <p>
         * A suite of codes indicating exceptions or reductions to patient costs and their effective periods.
         * </p>
         */
        public static class Exception extends BackboneElement {
            private final CodeableConcept type;
            private final Period period;

            private Exception(Builder builder) {
                super(builder);
                type = ValidationSupport.requireNonNull(builder.type, "type");
                period = builder.period;
            }

            /**
             * <p>
             * The code for the specific exception.
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
             * The timeframe during when the exception is in force.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Period}.
             */
            public Period getPeriod() {
                return period;
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
                        accept(period, "period", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(CodeableConcept type) {
                return new Builder(type);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept type;

                // optional
                private Period period;

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
                 *     A reference to this Builder instance.
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
                 * 
                 * @param extension
                 *     Additional content defined by implementations
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param extension
                 *     Additional content defined by implementations
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                @Override
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * <p>
                 * The timeframe during when the exception is in force.
                 * </p>
                 * 
                 * @param period
                 *     The effective period of the exception
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder period(Period period) {
                    this.period = period;
                    return this;
                }

                @Override
                public Exception build() {
                    return new Exception(this);
                }

                private static Builder from(Exception exception) {
                    Builder builder = new Builder(exception.type);
                    builder.period = exception.period;
                    return builder;
                }
            }
        }
    }
}
