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

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ContractPublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.ContractStatus;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Signature;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Contract extends DomainResource {
    private final List<Identifier> identifier;
    private final Uri url;
    private final String version;
    private final ContractStatus status;
    private final CodeableConcept legalState;
    private final Reference instantiatesCanonical;
    private final Uri instantiatesUri;
    private final CodeableConcept contentDerivative;
    private final DateTime issued;
    private final Period applies;
    private final CodeableConcept expirationType;
    private final List<Reference> subject;
    private final List<Reference> authority;
    private final List<Reference> domain;
    private final List<Reference> site;
    private final String name;
    private final String title;
    private final String subtitle;
    private final List<String> alias;
    private final Reference author;
    private final CodeableConcept scope;
    private final Element topic;
    private final CodeableConcept type;
    private final List<CodeableConcept> subType;
    private final ContentDefinition contentDefinition;
    private final List<Term> term;
    private final List<Reference> supportingInfo;
    private final List<Reference> relevantHistory;
    private final List<Signer> signer;
    private final List<Friendly> friendly;
    private final List<Legal> legal;
    private final List<Rule> rule;
    private final Element legallyBinding;

    private volatile int hashCode;

    private Contract(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        url = builder.url;
        version = builder.version;
        status = builder.status;
        legalState = builder.legalState;
        instantiatesCanonical = builder.instantiatesCanonical;
        instantiatesUri = builder.instantiatesUri;
        contentDerivative = builder.contentDerivative;
        issued = builder.issued;
        applies = builder.applies;
        expirationType = builder.expirationType;
        subject = Collections.unmodifiableList(builder.subject);
        authority = Collections.unmodifiableList(builder.authority);
        domain = Collections.unmodifiableList(builder.domain);
        site = Collections.unmodifiableList(builder.site);
        name = builder.name;
        title = builder.title;
        subtitle = builder.subtitle;
        alias = Collections.unmodifiableList(builder.alias);
        author = builder.author;
        scope = builder.scope;
        topic = ValidationSupport.choiceElement(builder.topic, "topic", CodeableConcept.class, Reference.class);
        type = builder.type;
        subType = Collections.unmodifiableList(builder.subType);
        contentDefinition = builder.contentDefinition;
        term = Collections.unmodifiableList(builder.term);
        supportingInfo = Collections.unmodifiableList(builder.supportingInfo);
        relevantHistory = Collections.unmodifiableList(builder.relevantHistory);
        signer = Collections.unmodifiableList(builder.signer);
        friendly = Collections.unmodifiableList(builder.friendly);
        legal = Collections.unmodifiableList(builder.legal);
        rule = Collections.unmodifiableList(builder.rule);
        legallyBinding = ValidationSupport.choiceElement(builder.legallyBinding, "legallyBinding", Attachment.class, Reference.class);
    }

    /**
     * <p>
     * Unique identifier for this Contract or a derivative that references a Source Contract.
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
     * Canonical identifier for this contract, represented as a URI (globally unique).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * <p>
     * An edition identifier used for business purposes to label business significant variants.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>
     * The status of the resource instance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ContractStatus}.
     */
    public ContractStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Legal states of the formation of a legal instrument, which is a formally executed written document that can be 
     * formally attributed to its author, records and formally expresses a legally enforceable act, process, or contractual 
     * duty, obligation, or right, and therefore evidences that act, process, or agreement.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getLegalState() {
        return legalState;
    }

    /**
     * <p>
     * The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * <p>
     * The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * <p>
     * The minimal content derived from the basal information source at a specific stage in its lifecycle.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getContentDerivative() {
        return contentDerivative;
    }

    /**
     * <p>
     * When this Contract was issued.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getIssued() {
        return issued;
    }

    /**
     * <p>
     * Relevant time or time-period when this Contract is applicable.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getApplies() {
        return applies;
    }

    /**
     * <p>
     * Event resulting in discontinuation or termination of this Contract instance by one or more parties to the contract.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getExpirationType() {
        return expirationType;
    }

    /**
     * <p>
     * The target entity impacted by or of interest to parties to the agreement.
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
     * A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the 
     * purpose of achieving some form of collective action such as the promulgation, administration and enforcement of 
     * contracts and policies.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAuthority() {
        return authority;
    }

    /**
     * <p>
     * Recognized governance framework or system operating with a circumscribed scope in accordance with specified 
     * principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals 
     * relative to resources.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getDomain() {
        return domain;
    }

    /**
     * <p>
     * Sites in which the contract is complied with, exercised, or in force.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSite() {
        return site;
    }

    /**
     * <p>
     * A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides 
     * additional information about its content. This name should be usable as an identifier for the module by machine 
     * processing applications such as code generation.
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
     * A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t 
     * giving additional information about its content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>
     * An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal 
     * state.t giving additional information about its content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * <p>
     * Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.
     * g., a domain specific contract number related to legislation.
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
     * The individual or organization that authored the Contract definition, derivative, or instance in any legal state.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * <p>
     * A selector of legal concerns for this Contract definition, derivative, or instance in any legal state.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getScope() {
        return scope;
    }

    /**
     * <p>
     * Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getTopic() {
        return topic;
    }

    /**
     * <p>
     * A high-level category for the legal instrument, whether constructed as a Contract definition, derivative, or instance 
     * in any legal state. Provides additional information about its content within the context of the Contract's scope to 
     * distinguish the kinds of systems that would be interested in the contract.
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
     * Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within 
     * the context of the Contract's scope.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getSubType() {
        return subType;
    }

    /**
     * <p>
     * Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be 
     * associated with and transformable into a Contract.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ContentDefinition}.
     */
    public ContentDefinition getContentDefinition() {
        return contentDefinition;
    }

    /**
     * <p>
     * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Term}.
     */
    public List<Term> getTerm() {
        return term;
    }

    /**
     * <p>
     * Information that may be needed by/relevant to the performer in their execution of this term action.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * <p>
     * Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key 
     * state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract. 
     * The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.
     * html#Provenance.entity.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getRelevantHistory() {
        return relevantHistory;
    }

    /**
     * <p>
     * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are 
     * any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the 
     * contract such as a notary or witness.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Signer}.
     */
    public List<Signer> getSigner() {
        return signer;
    }

    /**
     * <p>
     * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the 
     * representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a 
     * layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the 
     * Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Friendly}.
     */
    public List<Friendly> getFriendly() {
        return friendly;
    }

    /**
     * <p>
     * List of Legal expressions or representations of this Contract.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Legal}.
     */
    public List<Legal> getLegal() {
        return legal;
    }

    /**
     * <p>
     * List of Computable Policy Rule Language Representations of this Contract.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Rule}.
     */
    public List<Rule> getRule() {
        return rule;
    }

    /**
     * <p>
     * Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is 
     * considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getLegallyBinding() {
        return legallyBinding;
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
                accept(url, "url", visitor);
                accept(version, "version", visitor);
                accept(status, "status", visitor);
                accept(legalState, "legalState", visitor);
                accept(instantiatesCanonical, "instantiatesCanonical", visitor);
                accept(instantiatesUri, "instantiatesUri", visitor);
                accept(contentDerivative, "contentDerivative", visitor);
                accept(issued, "issued", visitor);
                accept(applies, "applies", visitor);
                accept(expirationType, "expirationType", visitor);
                accept(subject, "subject", visitor, Reference.class);
                accept(authority, "authority", visitor, Reference.class);
                accept(domain, "domain", visitor, Reference.class);
                accept(site, "site", visitor, Reference.class);
                accept(name, "name", visitor);
                accept(title, "title", visitor);
                accept(subtitle, "subtitle", visitor);
                accept(alias, "alias", visitor, String.class);
                accept(author, "author", visitor);
                accept(scope, "scope", visitor);
                accept(topic, "topic", visitor);
                accept(type, "type", visitor);
                accept(subType, "subType", visitor, CodeableConcept.class);
                accept(contentDefinition, "contentDefinition", visitor);
                accept(term, "term", visitor, Term.class);
                accept(supportingInfo, "supportingInfo", visitor, Reference.class);
                accept(relevantHistory, "relevantHistory", visitor, Reference.class);
                accept(signer, "signer", visitor, Signer.class);
                accept(friendly, "friendly", visitor, Friendly.class);
                accept(legal, "legal", visitor, Legal.class);
                accept(rule, "rule", visitor, Rule.class);
                accept(legallyBinding, "legallyBinding", visitor);
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
        Contract other = (Contract) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(url, other.url) && 
            Objects.equals(version, other.version) && 
            Objects.equals(status, other.status) && 
            Objects.equals(legalState, other.legalState) && 
            Objects.equals(instantiatesCanonical, other.instantiatesCanonical) && 
            Objects.equals(instantiatesUri, other.instantiatesUri) && 
            Objects.equals(contentDerivative, other.contentDerivative) && 
            Objects.equals(issued, other.issued) && 
            Objects.equals(applies, other.applies) && 
            Objects.equals(expirationType, other.expirationType) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(authority, other.authority) && 
            Objects.equals(domain, other.domain) && 
            Objects.equals(site, other.site) && 
            Objects.equals(name, other.name) && 
            Objects.equals(title, other.title) && 
            Objects.equals(subtitle, other.subtitle) && 
            Objects.equals(alias, other.alias) && 
            Objects.equals(author, other.author) && 
            Objects.equals(scope, other.scope) && 
            Objects.equals(topic, other.topic) && 
            Objects.equals(type, other.type) && 
            Objects.equals(subType, other.subType) && 
            Objects.equals(contentDefinition, other.contentDefinition) && 
            Objects.equals(term, other.term) && 
            Objects.equals(supportingInfo, other.supportingInfo) && 
            Objects.equals(relevantHistory, other.relevantHistory) && 
            Objects.equals(signer, other.signer) && 
            Objects.equals(friendly, other.friendly) && 
            Objects.equals(legal, other.legal) && 
            Objects.equals(rule, other.rule) && 
            Objects.equals(legallyBinding, other.legallyBinding);
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
                url, 
                version, 
                status, 
                legalState, 
                instantiatesCanonical, 
                instantiatesUri, 
                contentDerivative, 
                issued, 
                applies, 
                expirationType, 
                subject, 
                authority, 
                domain, 
                site, 
                name, 
                title, 
                subtitle, 
                alias, 
                author, 
                scope, 
                topic, 
                type, 
                subType, 
                contentDefinition, 
                term, 
                supportingInfo, 
                relevantHistory, 
                signer, 
                friendly, 
                legal, 
                rule, 
                legallyBinding);
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
        private Uri url;
        private String version;
        private ContractStatus status;
        private CodeableConcept legalState;
        private Reference instantiatesCanonical;
        private Uri instantiatesUri;
        private CodeableConcept contentDerivative;
        private DateTime issued;
        private Period applies;
        private CodeableConcept expirationType;
        private List<Reference> subject = new ArrayList<>();
        private List<Reference> authority = new ArrayList<>();
        private List<Reference> domain = new ArrayList<>();
        private List<Reference> site = new ArrayList<>();
        private String name;
        private String title;
        private String subtitle;
        private List<String> alias = new ArrayList<>();
        private Reference author;
        private CodeableConcept scope;
        private Element topic;
        private CodeableConcept type;
        private List<CodeableConcept> subType = new ArrayList<>();
        private ContentDefinition contentDefinition;
        private List<Term> term = new ArrayList<>();
        private List<Reference> supportingInfo = new ArrayList<>();
        private List<Reference> relevantHistory = new ArrayList<>();
        private List<Signer> signer = new ArrayList<>();
        private List<Friendly> friendly = new ArrayList<>();
        private List<Legal> legal = new ArrayList<>();
        private List<Rule> rule = new ArrayList<>();
        private Element legallyBinding;

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
         * Unique identifier for this Contract or a derivative that references a Source Contract.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param identifier
         *     Contract number
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
         * Unique identifier for this Contract or a derivative that references a Source Contract.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Contract number
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
         * Canonical identifier for this contract, represented as a URI (globally unique).
         * </p>
         * 
         * @param url
         *     Basal definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * <p>
         * An edition identifier used for business purposes to label business significant variants.
         * </p>
         * 
         * @param version
         *     Business edition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * <p>
         * The status of the resource instance.
         * </p>
         * 
         * @param status
         *     draft | active | suspended | cancelled | completed | entered-in-error | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ContractStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * Legal states of the formation of a legal instrument, which is a formally executed written document that can be 
         * formally attributed to its author, records and formally expresses a legally enforceable act, process, or contractual 
         * duty, obligation, or right, and therefore evidences that act, process, or agreement.
         * </p>
         * 
         * @param legalState
         *     Negotiation status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder legalState(CodeableConcept legalState) {
            this.legalState = legalState;
            return this;
        }

        /**
         * <p>
         * The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.
         * </p>
         * 
         * @param instantiatesCanonical
         *     Source Contract Definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesCanonical(Reference instantiatesCanonical) {
            this.instantiatesCanonical = instantiatesCanonical;
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.
         * </p>
         * 
         * @param instantiatesUri
         *     External Contract Definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesUri(Uri instantiatesUri) {
            this.instantiatesUri = instantiatesUri;
            return this;
        }

        /**
         * <p>
         * The minimal content derived from the basal information source at a specific stage in its lifecycle.
         * </p>
         * 
         * @param contentDerivative
         *     Content derived from the basal information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contentDerivative(CodeableConcept contentDerivative) {
            this.contentDerivative = contentDerivative;
            return this;
        }

        /**
         * <p>
         * When this Contract was issued.
         * </p>
         * 
         * @param issued
         *     When this Contract was issued
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder issued(DateTime issued) {
            this.issued = issued;
            return this;
        }

        /**
         * <p>
         * Relevant time or time-period when this Contract is applicable.
         * </p>
         * 
         * @param applies
         *     Effective time
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder applies(Period applies) {
            this.applies = applies;
            return this;
        }

        /**
         * <p>
         * Event resulting in discontinuation or termination of this Contract instance by one or more parties to the contract.
         * </p>
         * 
         * @param expirationType
         *     Contract cessation cause
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder expirationType(CodeableConcept expirationType) {
            this.expirationType = expirationType;
            return this;
        }

        /**
         * <p>
         * The target entity impacted by or of interest to parties to the agreement.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param subject
         *     Contract Target Entity
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
         * The target entity impacted by or of interest to parties to the agreement.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param subject
         *     Contract Target Entity
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
         * A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the 
         * purpose of achieving some form of collective action such as the promulgation, administration and enforcement of 
         * contracts and policies.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param authority
         *     Authority under which this Contract has standing
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authority(Reference... authority) {
            for (Reference value : authority) {
                this.authority.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the 
         * purpose of achieving some form of collective action such as the promulgation, administration and enforcement of 
         * contracts and policies.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param authority
         *     Authority under which this Contract has standing
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authority(Collection<Reference> authority) {
            this.authority = new ArrayList<>(authority);
            return this;
        }

        /**
         * <p>
         * Recognized governance framework or system operating with a circumscribed scope in accordance with specified 
         * principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals 
         * relative to resources.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param domain
         *     A sphere of control governed by an authoritative jurisdiction, organization, or person
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder domain(Reference... domain) {
            for (Reference value : domain) {
                this.domain.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Recognized governance framework or system operating with a circumscribed scope in accordance with specified 
         * principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals 
         * relative to resources.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param domain
         *     A sphere of control governed by an authoritative jurisdiction, organization, or person
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder domain(Collection<Reference> domain) {
            this.domain = new ArrayList<>(domain);
            return this;
        }

        /**
         * <p>
         * Sites in which the contract is complied with, exercised, or in force.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param site
         *     Specific Location
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder site(Reference... site) {
            for (Reference value : site) {
                this.site.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Sites in which the contract is complied with, exercised, or in force.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param site
         *     Specific Location
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder site(Collection<Reference> site) {
            this.site = new ArrayList<>(site);
            return this;
        }

        /**
         * <p>
         * A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides 
         * additional information about its content. This name should be usable as an identifier for the module by machine 
         * processing applications such as code generation.
         * </p>
         * 
         * @param name
         *     Computer friendly designation
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
         * A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t 
         * giving additional information about its content.
         * </p>
         * 
         * @param title
         *     Human Friendly name
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * <p>
         * An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal 
         * state.t giving additional information about its content.
         * </p>
         * 
         * @param subtitle
         *     Subordinate Friendly name
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        /**
         * <p>
         * Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.
         * g., a domain specific contract number related to legislation.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param alias
         *     Acronym or short name
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
         * Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.
         * g., a domain specific contract number related to legislation.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param alias
         *     Acronym or short name
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
         * The individual or organization that authored the Contract definition, derivative, or instance in any legal state.
         * </p>
         * 
         * @param author
         *     Source of Contract
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Reference author) {
            this.author = author;
            return this;
        }

        /**
         * <p>
         * A selector of legal concerns for this Contract definition, derivative, or instance in any legal state.
         * </p>
         * 
         * @param scope
         *     Range of Legal Concerns
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder scope(CodeableConcept scope) {
            this.scope = scope;
            return this;
        }

        /**
         * <p>
         * Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.
         * </p>
         * 
         * @param topic
         *     Focus of contract interest
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder topic(Element topic) {
            this.topic = topic;
            return this;
        }

        /**
         * <p>
         * A high-level category for the legal instrument, whether constructed as a Contract definition, derivative, or instance 
         * in any legal state. Provides additional information about its content within the context of the Contract's scope to 
         * distinguish the kinds of systems that would be interested in the contract.
         * </p>
         * 
         * @param type
         *     Legal instrument category
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
         * Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within 
         * the context of the Contract's scope.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param subType
         *     Subtype within the context of type
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subType(CodeableConcept... subType) {
            for (CodeableConcept value : subType) {
                this.subType.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within 
         * the context of the Contract's scope.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param subType
         *     Subtype within the context of type
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subType(Collection<CodeableConcept> subType) {
            this.subType = new ArrayList<>(subType);
            return this;
        }

        /**
         * <p>
         * Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be 
         * associated with and transformable into a Contract.
         * </p>
         * 
         * @param contentDefinition
         *     Contract precursor content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contentDefinition(ContentDefinition contentDefinition) {
            this.contentDefinition = contentDefinition;
            return this;
        }

        /**
         * <p>
         * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param term
         *     Contract Term List
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder term(Term... term) {
            for (Term value : term) {
                this.term.add(value);
            }
            return this;
        }

        /**
         * <p>
         * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param term
         *     Contract Term List
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder term(Collection<Term> term) {
            this.term = new ArrayList<>(term);
            return this;
        }

        /**
         * <p>
         * Information that may be needed by/relevant to the performer in their execution of this term action.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param supportingInfo
         *     Extra Information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInfo(Reference... supportingInfo) {
            for (Reference value : supportingInfo) {
                this.supportingInfo.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Information that may be needed by/relevant to the performer in their execution of this term action.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param supportingInfo
         *     Extra Information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInfo(Collection<Reference> supportingInfo) {
            this.supportingInfo = new ArrayList<>(supportingInfo);
            return this;
        }

        /**
         * <p>
         * Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key 
         * state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract. 
         * The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.
         * html#Provenance.entity.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param relevantHistory
         *     Key event in Contract History
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relevantHistory(Reference... relevantHistory) {
            for (Reference value : relevantHistory) {
                this.relevantHistory.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key 
         * state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract. 
         * The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.
         * html#Provenance.entity.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param relevantHistory
         *     Key event in Contract History
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relevantHistory(Collection<Reference> relevantHistory) {
            this.relevantHistory = new ArrayList<>(relevantHistory);
            return this;
        }

        /**
         * <p>
         * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are 
         * any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the 
         * contract such as a notary or witness.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param signer
         *     Contract Signatory
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder signer(Signer... signer) {
            for (Signer value : signer) {
                this.signer.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are 
         * any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the 
         * contract such as a notary or witness.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param signer
         *     Contract Signatory
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder signer(Collection<Signer> signer) {
            this.signer = new ArrayList<>(signer);
            return this;
        }

        /**
         * <p>
         * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the 
         * representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a 
         * layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the 
         * Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param friendly
         *     Contract Friendly Language
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder friendly(Friendly... friendly) {
            for (Friendly value : friendly) {
                this.friendly.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the 
         * representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a 
         * layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the 
         * Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param friendly
         *     Contract Friendly Language
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder friendly(Collection<Friendly> friendly) {
            this.friendly = new ArrayList<>(friendly);
            return this;
        }

        /**
         * <p>
         * List of Legal expressions or representations of this Contract.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param legal
         *     Contract Legal Language
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder legal(Legal... legal) {
            for (Legal value : legal) {
                this.legal.add(value);
            }
            return this;
        }

        /**
         * <p>
         * List of Legal expressions or representations of this Contract.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param legal
         *     Contract Legal Language
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder legal(Collection<Legal> legal) {
            this.legal = new ArrayList<>(legal);
            return this;
        }

        /**
         * <p>
         * List of Computable Policy Rule Language Representations of this Contract.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param rule
         *     Computable Contract Language
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder rule(Rule... rule) {
            for (Rule value : rule) {
                this.rule.add(value);
            }
            return this;
        }

        /**
         * <p>
         * List of Computable Policy Rule Language Representations of this Contract.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param rule
         *     Computable Contract Language
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder rule(Collection<Rule> rule) {
            this.rule = new ArrayList<>(rule);
            return this;
        }

        /**
         * <p>
         * Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is 
         * considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.
         * </p>
         * 
         * @param legallyBinding
         *     Binding Contract
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder legallyBinding(Element legallyBinding) {
            this.legallyBinding = legallyBinding;
            return this;
        }

        @Override
        public Contract build() {
            return new Contract(this);
        }

        private Builder from(Contract contract) {
            id = contract.id;
            meta = contract.meta;
            implicitRules = contract.implicitRules;
            language = contract.language;
            text = contract.text;
            contained.addAll(contract.contained);
            extension.addAll(contract.extension);
            modifierExtension.addAll(contract.modifierExtension);
            identifier.addAll(contract.identifier);
            url = contract.url;
            version = contract.version;
            status = contract.status;
            legalState = contract.legalState;
            instantiatesCanonical = contract.instantiatesCanonical;
            instantiatesUri = contract.instantiatesUri;
            contentDerivative = contract.contentDerivative;
            issued = contract.issued;
            applies = contract.applies;
            expirationType = contract.expirationType;
            subject.addAll(contract.subject);
            authority.addAll(contract.authority);
            domain.addAll(contract.domain);
            site.addAll(contract.site);
            name = contract.name;
            title = contract.title;
            subtitle = contract.subtitle;
            alias.addAll(contract.alias);
            author = contract.author;
            scope = contract.scope;
            topic = contract.topic;
            type = contract.type;
            subType.addAll(contract.subType);
            contentDefinition = contract.contentDefinition;
            term.addAll(contract.term);
            supportingInfo.addAll(contract.supportingInfo);
            relevantHistory.addAll(contract.relevantHistory);
            signer.addAll(contract.signer);
            friendly.addAll(contract.friendly);
            legal.addAll(contract.legal);
            rule.addAll(contract.rule);
            legallyBinding = contract.legallyBinding;
            return this;
        }
    }

    /**
     * <p>
     * Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be 
     * associated with and transformable into a Contract.
     * </p>
     */
    public static class ContentDefinition extends BackboneElement {
        private final CodeableConcept type;
        private final CodeableConcept subType;
        private final Reference publisher;
        private final DateTime publicationDate;
        private final ContractPublicationStatus publicationStatus;
        private final Markdown copyright;

        private volatile int hashCode;

        private ContentDefinition(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            subType = builder.subType;
            publisher = builder.publisher;
            publicationDate = builder.publicationDate;
            publicationStatus = ValidationSupport.requireNonNull(builder.publicationStatus, "publicationStatus");
            copyright = builder.copyright;
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Precusory content structure and use, i.e., a boilerplate, template, application for a contract such as an insurance 
         * policy or benefits under a program, e.g., workers compensation.
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
         * Detailed Precusory content type.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getSubType() {
            return subType;
        }

        /**
         * <p>
         * The individual or organization that published the Contract precursor content.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getPublisher() {
            return publisher;
        }

        /**
         * <p>
         * The date (and optionally time) when the contract was published. The date must change when the business version changes 
         * and it must change if the status code changes. In addition, it should change when the substantive content of the 
         * contract changes.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getPublicationDate() {
            return publicationDate;
        }

        /**
         * <p>
         * draft | active | retired | unknown.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ContractPublicationStatus}.
         */
        public ContractPublicationStatus getPublicationStatus() {
            return publicationStatus;
        }

        /**
         * <p>
         * A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on 
         * the use and publishing of the Contract precursor content.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Markdown}.
         */
        public Markdown getCopyright() {
            return copyright;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (subType != null) || 
                (publisher != null) || 
                (publicationDate != null) || 
                (publicationStatus != null) || 
                (copyright != null);
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
                    accept(subType, "subType", visitor);
                    accept(publisher, "publisher", visitor);
                    accept(publicationDate, "publicationDate", visitor);
                    accept(publicationStatus, "publicationStatus", visitor);
                    accept(copyright, "copyright", visitor);
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
            ContentDefinition other = (ContentDefinition) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(subType, other.subType) && 
                Objects.equals(publisher, other.publisher) && 
                Objects.equals(publicationDate, other.publicationDate) && 
                Objects.equals(publicationStatus, other.publicationStatus) && 
                Objects.equals(copyright, other.copyright);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    subType, 
                    publisher, 
                    publicationDate, 
                    publicationStatus, 
                    copyright);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(type, publicationStatus).from(this);
        }

        public Builder toBuilder(CodeableConcept type, ContractPublicationStatus publicationStatus) {
            return new Builder(type, publicationStatus).from(this);
        }

        public static Builder builder(CodeableConcept type, ContractPublicationStatus publicationStatus) {
            return new Builder(type, publicationStatus);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;
            private final ContractPublicationStatus publicationStatus;

            // optional
            private CodeableConcept subType;
            private Reference publisher;
            private DateTime publicationDate;
            private Markdown copyright;

            private Builder(CodeableConcept type, ContractPublicationStatus publicationStatus) {
                super();
                this.type = type;
                this.publicationStatus = publicationStatus;
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
             * Detailed Precusory content type.
             * </p>
             * 
             * @param subType
             *     Detailed Content Type Definition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subType(CodeableConcept subType) {
                this.subType = subType;
                return this;
            }

            /**
             * <p>
             * The individual or organization that published the Contract precursor content.
             * </p>
             * 
             * @param publisher
             *     Publisher Entity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder publisher(Reference publisher) {
                this.publisher = publisher;
                return this;
            }

            /**
             * <p>
             * The date (and optionally time) when the contract was published. The date must change when the business version changes 
             * and it must change if the status code changes. In addition, it should change when the substantive content of the 
             * contract changes.
             * </p>
             * 
             * @param publicationDate
             *     When published
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder publicationDate(DateTime publicationDate) {
                this.publicationDate = publicationDate;
                return this;
            }

            /**
             * <p>
             * A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on 
             * the use and publishing of the Contract precursor content.
             * </p>
             * 
             * @param copyright
             *     Publication Ownership
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder copyright(Markdown copyright) {
                this.copyright = copyright;
                return this;
            }

            @Override
            public ContentDefinition build() {
                return new ContentDefinition(this);
            }

            private Builder from(ContentDefinition contentDefinition) {
                id = contentDefinition.id;
                extension.addAll(contentDefinition.extension);
                modifierExtension.addAll(contentDefinition.modifierExtension);
                subType = contentDefinition.subType;
                publisher = contentDefinition.publisher;
                publicationDate = contentDefinition.publicationDate;
                copyright = contentDefinition.copyright;
                return this;
            }
        }
    }

    /**
     * <p>
     * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
     * </p>
     */
    public static class Term extends BackboneElement {
        private final Identifier identifier;
        private final DateTime issued;
        private final Period applies;
        private final Element topic;
        private final CodeableConcept type;
        private final CodeableConcept subType;
        private final String text;
        private final List<SecurityLabel> securityLabel;
        private final Offer offer;
        private final List<Asset> asset;
        private final List<Action> action;
        private final List<Contract.Term> group;

        private volatile int hashCode;

        private Term(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            issued = builder.issued;
            applies = builder.applies;
            topic = ValidationSupport.choiceElement(builder.topic, "topic", CodeableConcept.class, Reference.class);
            type = builder.type;
            subType = builder.subType;
            text = builder.text;
            securityLabel = Collections.unmodifiableList(builder.securityLabel);
            offer = ValidationSupport.requireNonNull(builder.offer, "offer");
            asset = Collections.unmodifiableList(builder.asset);
            action = Collections.unmodifiableList(builder.action);
            group = Collections.unmodifiableList(builder.group);
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Unique identifier for this particular Contract Provision.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * <p>
         * When this Contract Provision was issued.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getIssued() {
            return issued;
        }

        /**
         * <p>
         * Relevant time or time-period when this Contract Provision is applicable.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getApplies() {
            return applies;
        }

        /**
         * <p>
         * The entity that the term applies to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getTopic() {
            return topic;
        }

        /**
         * <p>
         * A legal clause or condition contained within a contract that requires one or both parties to perform a particular 
         * requirement by some specified time or prevents one or both parties from performing a particular requirement by some 
         * specified time.
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
         * A specialized legal clause or condition based on overarching contract type.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getSubType() {
            return subType;
        }

        /**
         * <p>
         * Statement of a provision in a policy or a contract.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getText() {
            return text;
        }

        /**
         * <p>
         * Security labels that protect the handling of information about the term and its elements, which may be specifically 
         * identified..
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link SecurityLabel}.
         */
        public List<SecurityLabel> getSecurityLabel() {
            return securityLabel;
        }

        /**
         * <p>
         * The matter of concern in the context of this provision of the agrement.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Offer}.
         */
        public Offer getOffer() {
            return offer;
        }

        /**
         * <p>
         * Contract Term Asset List.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Asset}.
         */
        public List<Asset> getAsset() {
            return asset;
        }

        /**
         * <p>
         * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
         * taking place.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action}.
         */
        public List<Action> getAction() {
            return action;
        }

        /**
         * <p>
         * Nested group of Contract Provisions.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Term}.
         */
        public List<Contract.Term> getGroup() {
            return group;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (identifier != null) || 
                (issued != null) || 
                (applies != null) || 
                (topic != null) || 
                (type != null) || 
                (subType != null) || 
                (text != null) || 
                !securityLabel.isEmpty() || 
                (offer != null) || 
                !asset.isEmpty() || 
                !action.isEmpty() || 
                !group.isEmpty();
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
                    accept(identifier, "identifier", visitor);
                    accept(issued, "issued", visitor);
                    accept(applies, "applies", visitor);
                    accept(topic, "topic", visitor);
                    accept(type, "type", visitor);
                    accept(subType, "subType", visitor);
                    accept(text, "text", visitor);
                    accept(securityLabel, "securityLabel", visitor, SecurityLabel.class);
                    accept(offer, "offer", visitor);
                    accept(asset, "asset", visitor, Asset.class);
                    accept(action, "action", visitor, Action.class);
                    accept(group, "group", visitor, Contract.Term.class);
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
            Term other = (Term) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(issued, other.issued) && 
                Objects.equals(applies, other.applies) && 
                Objects.equals(topic, other.topic) && 
                Objects.equals(type, other.type) && 
                Objects.equals(subType, other.subType) && 
                Objects.equals(text, other.text) && 
                Objects.equals(securityLabel, other.securityLabel) && 
                Objects.equals(offer, other.offer) && 
                Objects.equals(asset, other.asset) && 
                Objects.equals(action, other.action) && 
                Objects.equals(group, other.group);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    issued, 
                    applies, 
                    topic, 
                    type, 
                    subType, 
                    text, 
                    securityLabel, 
                    offer, 
                    asset, 
                    action, 
                    group);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(offer).from(this);
        }

        public Builder toBuilder(Offer offer) {
            return new Builder(offer).from(this);
        }

        public static Builder builder(Offer offer) {
            return new Builder(offer);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Offer offer;

            // optional
            private Identifier identifier;
            private DateTime issued;
            private Period applies;
            private Element topic;
            private CodeableConcept type;
            private CodeableConcept subType;
            private String text;
            private List<SecurityLabel> securityLabel = new ArrayList<>();
            private List<Asset> asset = new ArrayList<>();
            private List<Action> action = new ArrayList<>();
            private List<Contract.Term> group = new ArrayList<>();

            private Builder(Offer offer) {
                super();
                this.offer = offer;
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
             * Unique identifier for this particular Contract Provision.
             * </p>
             * 
             * @param identifier
             *     Contract Term Number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * <p>
             * When this Contract Provision was issued.
             * </p>
             * 
             * @param issued
             *     Contract Term Issue Date Time
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder issued(DateTime issued) {
                this.issued = issued;
                return this;
            }

            /**
             * <p>
             * Relevant time or time-period when this Contract Provision is applicable.
             * </p>
             * 
             * @param applies
             *     Contract Term Effective Time
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder applies(Period applies) {
                this.applies = applies;
                return this;
            }

            /**
             * <p>
             * The entity that the term applies to.
             * </p>
             * 
             * @param topic
             *     Term Concern
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder topic(Element topic) {
                this.topic = topic;
                return this;
            }

            /**
             * <p>
             * A legal clause or condition contained within a contract that requires one or both parties to perform a particular 
             * requirement by some specified time or prevents one or both parties from performing a particular requirement by some 
             * specified time.
             * </p>
             * 
             * @param type
             *     Contract Term Type or Form
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
             * A specialized legal clause or condition based on overarching contract type.
             * </p>
             * 
             * @param subType
             *     Contract Term Type specific classification
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subType(CodeableConcept subType) {
                this.subType = subType;
                return this;
            }

            /**
             * <p>
             * Statement of a provision in a policy or a contract.
             * </p>
             * 
             * @param text
             *     Term Statement
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * <p>
             * Security labels that protect the handling of information about the term and its elements, which may be specifically 
             * identified..
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param securityLabel
             *     Protection for the Term
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder securityLabel(SecurityLabel... securityLabel) {
                for (SecurityLabel value : securityLabel) {
                    this.securityLabel.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Security labels that protect the handling of information about the term and its elements, which may be specifically 
             * identified..
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param securityLabel
             *     Protection for the Term
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder securityLabel(Collection<SecurityLabel> securityLabel) {
                this.securityLabel = new ArrayList<>(securityLabel);
                return this;
            }

            /**
             * <p>
             * Contract Term Asset List.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param asset
             *     Contract Term Asset List
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder asset(Asset... asset) {
                for (Asset value : asset) {
                    this.asset.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Contract Term Asset List.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param asset
             *     Contract Term Asset List
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder asset(Collection<Asset> asset) {
                this.asset = new ArrayList<>(asset);
                return this;
            }

            /**
             * <p>
             * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
             * taking place.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param action
             *     Entity being ascribed responsibility
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(Action... action) {
                for (Action value : action) {
                    this.action.add(value);
                }
                return this;
            }

            /**
             * <p>
             * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
             * taking place.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param action
             *     Entity being ascribed responsibility
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(Collection<Action> action) {
                this.action = new ArrayList<>(action);
                return this;
            }

            /**
             * <p>
             * Nested group of Contract Provisions.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param group
             *     Nested Contract Term Group
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder group(Contract.Term... group) {
                for (Contract.Term value : group) {
                    this.group.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Nested group of Contract Provisions.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param group
             *     Nested Contract Term Group
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder group(Collection<Contract.Term> group) {
                this.group = new ArrayList<>(group);
                return this;
            }

            @Override
            public Term build() {
                return new Term(this);
            }

            private Builder from(Term term) {
                id = term.id;
                extension.addAll(term.extension);
                modifierExtension.addAll(term.modifierExtension);
                identifier = term.identifier;
                issued = term.issued;
                applies = term.applies;
                topic = term.topic;
                type = term.type;
                subType = term.subType;
                text = term.text;
                securityLabel.addAll(term.securityLabel);
                asset.addAll(term.asset);
                action.addAll(term.action);
                group.addAll(term.group);
                return this;
            }
        }

        /**
         * <p>
         * Security labels that protect the handling of information about the term and its elements, which may be specifically 
         * identified..
         * </p>
         */
        public static class SecurityLabel extends BackboneElement {
            private final List<UnsignedInt> number;
            private final Coding classification;
            private final List<Coding> category;
            private final List<Coding> control;

            private volatile int hashCode;

            private SecurityLabel(Builder builder) {
                super(builder);
                number = Collections.unmodifiableList(builder.number);
                classification = ValidationSupport.requireNonNull(builder.classification, "classification");
                category = Collections.unmodifiableList(builder.category);
                control = Collections.unmodifiableList(builder.control);
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * Number used to link this term or term element to the applicable Security Label.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link UnsignedInt}.
             */
            public List<UnsignedInt> getNumber() {
                return number;
            }

            /**
             * <p>
             * Security label privacy tag that species the level of confidentiality protection required for this term and/or term 
             * elements.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Coding}.
             */
            public Coding getClassification() {
                return classification;
            }

            /**
             * <p>
             * Security label privacy tag that species the applicable privacy and security policies governing this term and/or term 
             * elements.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Coding}.
             */
            public List<Coding> getCategory() {
                return category;
            }

            /**
             * <p>
             * Security label privacy tag that species the manner in which term and/or term elements are to be protected.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Coding}.
             */
            public List<Coding> getControl() {
                return control;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    !number.isEmpty() || 
                    (classification != null) || 
                    !category.isEmpty() || 
                    !control.isEmpty();
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
                        accept(number, "number", visitor, UnsignedInt.class);
                        accept(classification, "classification", visitor);
                        accept(category, "category", visitor, Coding.class);
                        accept(control, "control", visitor, Coding.class);
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
                SecurityLabel other = (SecurityLabel) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(number, other.number) && 
                    Objects.equals(classification, other.classification) && 
                    Objects.equals(category, other.category) && 
                    Objects.equals(control, other.control);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        number, 
                        classification, 
                        category, 
                        control);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(classification).from(this);
            }

            public Builder toBuilder(Coding classification) {
                return new Builder(classification).from(this);
            }

            public static Builder builder(Coding classification) {
                return new Builder(classification);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Coding classification;

                // optional
                private List<UnsignedInt> number = new ArrayList<>();
                private List<Coding> category = new ArrayList<>();
                private List<Coding> control = new ArrayList<>();

                private Builder(Coding classification) {
                    super();
                    this.classification = classification;
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
                 * Number used to link this term or term element to the applicable Security Label.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param number
                 *     Link to Security Labels
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder number(UnsignedInt... number) {
                    for (UnsignedInt value : number) {
                        this.number.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Number used to link this term or term element to the applicable Security Label.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param number
                 *     Link to Security Labels
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder number(Collection<UnsignedInt> number) {
                    this.number = new ArrayList<>(number);
                    return this;
                }

                /**
                 * <p>
                 * Security label privacy tag that species the applicable privacy and security policies governing this term and/or term 
                 * elements.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param category
                 *     Applicable Policy
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder category(Coding... category) {
                    for (Coding value : category) {
                        this.category.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Security label privacy tag that species the applicable privacy and security policies governing this term and/or term 
                 * elements.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param category
                 *     Applicable Policy
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder category(Collection<Coding> category) {
                    this.category = new ArrayList<>(category);
                    return this;
                }

                /**
                 * <p>
                 * Security label privacy tag that species the manner in which term and/or term elements are to be protected.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param control
                 *     Handling Instructions
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder control(Coding... control) {
                    for (Coding value : control) {
                        this.control.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Security label privacy tag that species the manner in which term and/or term elements are to be protected.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param control
                 *     Handling Instructions
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder control(Collection<Coding> control) {
                    this.control = new ArrayList<>(control);
                    return this;
                }

                @Override
                public SecurityLabel build() {
                    return new SecurityLabel(this);
                }

                private Builder from(SecurityLabel securityLabel) {
                    id = securityLabel.id;
                    extension.addAll(securityLabel.extension);
                    modifierExtension.addAll(securityLabel.modifierExtension);
                    number.addAll(securityLabel.number);
                    category.addAll(securityLabel.category);
                    control.addAll(securityLabel.control);
                    return this;
                }
            }
        }

        /**
         * <p>
         * The matter of concern in the context of this provision of the agrement.
         * </p>
         */
        public static class Offer extends BackboneElement {
            private final List<Identifier> identifier;
            private final List<Party> party;
            private final Reference topic;
            private final CodeableConcept type;
            private final CodeableConcept decision;
            private final List<CodeableConcept> decisionMode;
            private final List<Answer> answer;
            private final String text;
            private final List<String> linkId;
            private final List<UnsignedInt> securityLabelNumber;

            private volatile int hashCode;

            private Offer(Builder builder) {
                super(builder);
                identifier = Collections.unmodifiableList(builder.identifier);
                party = Collections.unmodifiableList(builder.party);
                topic = builder.topic;
                type = builder.type;
                decision = builder.decision;
                decisionMode = Collections.unmodifiableList(builder.decisionMode);
                answer = Collections.unmodifiableList(builder.answer);
                text = builder.text;
                linkId = Collections.unmodifiableList(builder.linkId);
                securityLabelNumber = Collections.unmodifiableList(builder.securityLabelNumber);
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * Unique identifier for this particular Contract Provision.
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
             * Offer Recipient.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Party}.
             */
            public List<Party> getParty() {
                return party;
            }

            /**
             * <p>
             * The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in 
             * any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Reference}.
             */
            public Reference getTopic() {
                return topic;
            }

            /**
             * <p>
             * Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life 
             * time maximum benefit.
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
             * Type of choice made by accepting party with respect to an offer made by an offeror/ grantee.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getDecision() {
                return decision;
            }

            /**
             * <p>
             * How the decision about a Contract was conveyed.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getDecisionMode() {
                return decisionMode;
            }

            /**
             * <p>
             * Response to offer text.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Answer}.
             */
            public List<Answer> getAnswer() {
                return answer;
            }

            /**
             * <p>
             * Human readable form of this Contract Offer.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getText() {
                return text;
            }

            /**
             * <p>
             * The id of the clause or question text of the offer in the referenced questionnaire/response.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String}.
             */
            public List<String> getLinkId() {
                return linkId;
            }

            /**
             * <p>
             * Security labels that protects the offer.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link UnsignedInt}.
             */
            public List<UnsignedInt> getSecurityLabelNumber() {
                return securityLabelNumber;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    !identifier.isEmpty() || 
                    !party.isEmpty() || 
                    (topic != null) || 
                    (type != null) || 
                    (decision != null) || 
                    !decisionMode.isEmpty() || 
                    !answer.isEmpty() || 
                    (text != null) || 
                    !linkId.isEmpty() || 
                    !securityLabelNumber.isEmpty();
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
                        accept(party, "party", visitor, Party.class);
                        accept(topic, "topic", visitor);
                        accept(type, "type", visitor);
                        accept(decision, "decision", visitor);
                        accept(decisionMode, "decisionMode", visitor, CodeableConcept.class);
                        accept(answer, "answer", visitor, Answer.class);
                        accept(text, "text", visitor);
                        accept(linkId, "linkId", visitor, String.class);
                        accept(securityLabelNumber, "securityLabelNumber", visitor, UnsignedInt.class);
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
                Offer other = (Offer) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(identifier, other.identifier) && 
                    Objects.equals(party, other.party) && 
                    Objects.equals(topic, other.topic) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(decision, other.decision) && 
                    Objects.equals(decisionMode, other.decisionMode) && 
                    Objects.equals(answer, other.answer) && 
                    Objects.equals(text, other.text) && 
                    Objects.equals(linkId, other.linkId) && 
                    Objects.equals(securityLabelNumber, other.securityLabelNumber);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        identifier, 
                        party, 
                        topic, 
                        type, 
                        decision, 
                        decisionMode, 
                        answer, 
                        text, 
                        linkId, 
                        securityLabelNumber);
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
                private List<Party> party = new ArrayList<>();
                private Reference topic;
                private CodeableConcept type;
                private CodeableConcept decision;
                private List<CodeableConcept> decisionMode = new ArrayList<>();
                private List<Answer> answer = new ArrayList<>();
                private String text;
                private List<String> linkId = new ArrayList<>();
                private List<UnsignedInt> securityLabelNumber = new ArrayList<>();

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
                 * Unique identifier for this particular Contract Provision.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param identifier
                 *     Offer business ID
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
                 * Unique identifier for this particular Contract Provision.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param identifier
                 *     Offer business ID
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
                 * Offer Recipient.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param party
                 *     Offer Recipient
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder party(Party... party) {
                    for (Party value : party) {
                        this.party.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Offer Recipient.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param party
                 *     Offer Recipient
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder party(Collection<Party> party) {
                    this.party = new ArrayList<>(party);
                    return this;
                }

                /**
                 * <p>
                 * The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in 
                 * any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).
                 * </p>
                 * 
                 * @param topic
                 *     Negotiable offer asset
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder topic(Reference topic) {
                    this.topic = topic;
                    return this;
                }

                /**
                 * <p>
                 * Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life 
                 * time maximum benefit.
                 * </p>
                 * 
                 * @param type
                 *     Contract Offer Type or Form
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
                 * Type of choice made by accepting party with respect to an offer made by an offeror/ grantee.
                 * </p>
                 * 
                 * @param decision
                 *     Accepting party choice
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder decision(CodeableConcept decision) {
                    this.decision = decision;
                    return this;
                }

                /**
                 * <p>
                 * How the decision about a Contract was conveyed.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param decisionMode
                 *     How decision is conveyed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder decisionMode(CodeableConcept... decisionMode) {
                    for (CodeableConcept value : decisionMode) {
                        this.decisionMode.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * How the decision about a Contract was conveyed.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param decisionMode
                 *     How decision is conveyed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder decisionMode(Collection<CodeableConcept> decisionMode) {
                    this.decisionMode = new ArrayList<>(decisionMode);
                    return this;
                }

                /**
                 * <p>
                 * Response to offer text.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param answer
                 *     Response to offer text
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder answer(Answer... answer) {
                    for (Answer value : answer) {
                        this.answer.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Response to offer text.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param answer
                 *     Response to offer text
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder answer(Collection<Answer> answer) {
                    this.answer = new ArrayList<>(answer);
                    return this;
                }

                /**
                 * <p>
                 * Human readable form of this Contract Offer.
                 * </p>
                 * 
                 * @param text
                 *     Human readable offer text
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder text(String text) {
                    this.text = text;
                    return this;
                }

                /**
                 * <p>
                 * The id of the clause or question text of the offer in the referenced questionnaire/response.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param linkId
                 *     Pointer to text
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder linkId(String... linkId) {
                    for (String value : linkId) {
                        this.linkId.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The id of the clause or question text of the offer in the referenced questionnaire/response.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param linkId
                 *     Pointer to text
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder linkId(Collection<String> linkId) {
                    this.linkId = new ArrayList<>(linkId);
                    return this;
                }

                /**
                 * <p>
                 * Security labels that protects the offer.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param securityLabelNumber
                 *     Offer restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder securityLabelNumber(UnsignedInt... securityLabelNumber) {
                    for (UnsignedInt value : securityLabelNumber) {
                        this.securityLabelNumber.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Security labels that protects the offer.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param securityLabelNumber
                 *     Offer restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder securityLabelNumber(Collection<UnsignedInt> securityLabelNumber) {
                    this.securityLabelNumber = new ArrayList<>(securityLabelNumber);
                    return this;
                }

                @Override
                public Offer build() {
                    return new Offer(this);
                }

                private Builder from(Offer offer) {
                    id = offer.id;
                    extension.addAll(offer.extension);
                    modifierExtension.addAll(offer.modifierExtension);
                    identifier.addAll(offer.identifier);
                    party.addAll(offer.party);
                    topic = offer.topic;
                    type = offer.type;
                    decision = offer.decision;
                    decisionMode.addAll(offer.decisionMode);
                    answer.addAll(offer.answer);
                    text = offer.text;
                    linkId.addAll(offer.linkId);
                    securityLabelNumber.addAll(offer.securityLabelNumber);
                    return this;
                }
            }

            /**
             * <p>
             * Offer Recipient.
             * </p>
             */
            public static class Party extends BackboneElement {
                private final List<Reference> reference;
                private final CodeableConcept role;

                private volatile int hashCode;

                private Party(Builder builder) {
                    super(builder);
                    reference = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.reference, "reference"));
                    role = ValidationSupport.requireNonNull(builder.role, "role");
                    if (!hasChildren()) {
                        throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                    }
                }

                /**
                 * <p>
                 * Participant in the offer.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Reference}.
                 */
                public List<Reference> getReference() {
                    return reference;
                }

                /**
                 * <p>
                 * How the party participates in the offer.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getRole() {
                    return role;
                }

                @Override
                protected boolean hasChildren() {
                    return super.hasChildren() || 
                        !reference.isEmpty() || 
                        (role != null);
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
                            accept(reference, "reference", visitor, Reference.class);
                            accept(role, "role", visitor);
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
                    Party other = (Party) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(reference, other.reference) && 
                        Objects.equals(role, other.role);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            reference, 
                            role);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(reference, role).from(this);
                }

                public Builder toBuilder(List<Reference> reference, CodeableConcept role) {
                    return new Builder(reference, role).from(this);
                }

                public static Builder builder(List<Reference> reference, CodeableConcept role) {
                    return new Builder(reference, role);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final List<Reference> reference;
                    private final CodeableConcept role;

                    private Builder(List<Reference> reference, CodeableConcept role) {
                        super();
                        this.reference = reference;
                        this.role = role;
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

                    @Override
                    public Party build() {
                        return new Party(this);
                    }

                    private Builder from(Party party) {
                        id = party.id;
                        extension.addAll(party.extension);
                        modifierExtension.addAll(party.modifierExtension);
                        return this;
                    }
                }
            }

            /**
             * <p>
             * Response to offer text.
             * </p>
             */
            public static class Answer extends BackboneElement {
                private final Element value;

                private volatile int hashCode;

                private Answer(Builder builder) {
                    super(builder);
                    value = ValidationSupport.requireChoiceElement(builder.value, "value", Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Uri.class, Attachment.class, Coding.class, Quantity.class, Reference.class);
                    if (!hasChildren()) {
                        throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                    }
                }

                /**
                 * <p>
                 * Response to an offer clause or question text, which enables selection of values to be agreed to, e.g., the period of 
                 * participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further 
                 * research.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Element}.
                 */
                public Element getValue() {
                    return value;
                }

                @Override
                protected boolean hasChildren() {
                    return super.hasChildren() || 
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
                    Answer other = (Answer) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(value, other.value);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            value);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(value).from(this);
                }

                public Builder toBuilder(Element value) {
                    return new Builder(value).from(this);
                }

                public static Builder builder(Element value) {
                    return new Builder(value);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final Element value;

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

                    @Override
                    public Answer build() {
                        return new Answer(this);
                    }

                    private Builder from(Answer answer) {
                        id = answer.id;
                        extension.addAll(answer.extension);
                        modifierExtension.addAll(answer.modifierExtension);
                        return this;
                    }
                }
            }
        }

        /**
         * <p>
         * Contract Term Asset List.
         * </p>
         */
        public static class Asset extends BackboneElement {
            private final CodeableConcept scope;
            private final List<CodeableConcept> type;
            private final List<Reference> typeReference;
            private final List<CodeableConcept> subtype;
            private final Coding relationship;
            private final List<Context> context;
            private final String condition;
            private final List<CodeableConcept> periodType;
            private final List<Period> period;
            private final List<Period> usePeriod;
            private final String text;
            private final List<String> linkId;
            private final List<Contract.Term.Offer.Answer> answer;
            private final List<UnsignedInt> securityLabelNumber;
            private final List<ValuedItem> valuedItem;

            private volatile int hashCode;

            private Asset(Builder builder) {
                super(builder);
                scope = builder.scope;
                type = Collections.unmodifiableList(builder.type);
                typeReference = Collections.unmodifiableList(builder.typeReference);
                subtype = Collections.unmodifiableList(builder.subtype);
                relationship = builder.relationship;
                context = Collections.unmodifiableList(builder.context);
                condition = builder.condition;
                periodType = Collections.unmodifiableList(builder.periodType);
                period = Collections.unmodifiableList(builder.period);
                usePeriod = Collections.unmodifiableList(builder.usePeriod);
                text = builder.text;
                linkId = Collections.unmodifiableList(builder.linkId);
                answer = Collections.unmodifiableList(builder.answer);
                securityLabelNumber = Collections.unmodifiableList(builder.securityLabelNumber);
                valuedItem = Collections.unmodifiableList(builder.valuedItem);
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * Differentiates the kind of the asset .
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getScope() {
                return scope;
            }

            /**
             * <p>
             * Target entity type about which the term may be concerned.
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
             * Associated entities.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Reference}.
             */
            public List<Reference> getTypeReference() {
                return typeReference;
            }

            /**
             * <p>
             * May be a subtype or part of an offered asset.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getSubtype() {
                return subtype;
            }

            /**
             * <p>
             * Specifies the applicability of the term to an asset resource instance, and instances it refers to orinstances that 
             * refer to it, and/or are owned by the offeree.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Coding}.
             */
            public Coding getRelationship() {
                return relationship;
            }

            /**
             * <p>
             * Circumstance of the asset.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Context}.
             */
            public List<Context> getContext() {
                return context;
            }

            /**
             * <p>
             * Description of the quality and completeness of the asset that imay be a factor in its valuation.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getCondition() {
                return condition;
            }

            /**
             * <p>
             * Type of Asset availability for use or ownership.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getPeriodType() {
                return periodType;
            }

            /**
             * <p>
             * Asset relevant contractual time period.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Period}.
             */
            public List<Period> getPeriod() {
                return period;
            }

            /**
             * <p>
             * Time period of asset use.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Period}.
             */
            public List<Period> getUsePeriod() {
                return usePeriod;
            }

            /**
             * <p>
             * Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in 
             * the formation of the contract.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getText() {
                return text;
            }

            /**
             * <p>
             * Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String}.
             */
            public List<String> getLinkId() {
                return linkId;
            }

            /**
             * <p>
             * Response to assets.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Answer}.
             */
            public List<Contract.Term.Offer.Answer> getAnswer() {
                return answer;
            }

            /**
             * <p>
             * Security labels that protects the asset.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link UnsignedInt}.
             */
            public List<UnsignedInt> getSecurityLabelNumber() {
                return securityLabelNumber;
            }

            /**
             * <p>
             * Contract Valued Item List.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link ValuedItem}.
             */
            public List<ValuedItem> getValuedItem() {
                return valuedItem;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (scope != null) || 
                    !type.isEmpty() || 
                    !typeReference.isEmpty() || 
                    !subtype.isEmpty() || 
                    (relationship != null) || 
                    !context.isEmpty() || 
                    (condition != null) || 
                    !periodType.isEmpty() || 
                    !period.isEmpty() || 
                    !usePeriod.isEmpty() || 
                    (text != null) || 
                    !linkId.isEmpty() || 
                    !answer.isEmpty() || 
                    !securityLabelNumber.isEmpty() || 
                    !valuedItem.isEmpty();
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
                        accept(scope, "scope", visitor);
                        accept(type, "type", visitor, CodeableConcept.class);
                        accept(typeReference, "typeReference", visitor, Reference.class);
                        accept(subtype, "subtype", visitor, CodeableConcept.class);
                        accept(relationship, "relationship", visitor);
                        accept(context, "context", visitor, Context.class);
                        accept(condition, "condition", visitor);
                        accept(periodType, "periodType", visitor, CodeableConcept.class);
                        accept(period, "period", visitor, Period.class);
                        accept(usePeriod, "usePeriod", visitor, Period.class);
                        accept(text, "text", visitor);
                        accept(linkId, "linkId", visitor, String.class);
                        accept(answer, "answer", visitor, Contract.Term.Offer.Answer.class);
                        accept(securityLabelNumber, "securityLabelNumber", visitor, UnsignedInt.class);
                        accept(valuedItem, "valuedItem", visitor, ValuedItem.class);
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
                Asset other = (Asset) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(scope, other.scope) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(typeReference, other.typeReference) && 
                    Objects.equals(subtype, other.subtype) && 
                    Objects.equals(relationship, other.relationship) && 
                    Objects.equals(context, other.context) && 
                    Objects.equals(condition, other.condition) && 
                    Objects.equals(periodType, other.periodType) && 
                    Objects.equals(period, other.period) && 
                    Objects.equals(usePeriod, other.usePeriod) && 
                    Objects.equals(text, other.text) && 
                    Objects.equals(linkId, other.linkId) && 
                    Objects.equals(answer, other.answer) && 
                    Objects.equals(securityLabelNumber, other.securityLabelNumber) && 
                    Objects.equals(valuedItem, other.valuedItem);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        scope, 
                        type, 
                        typeReference, 
                        subtype, 
                        relationship, 
                        context, 
                        condition, 
                        periodType, 
                        period, 
                        usePeriod, 
                        text, 
                        linkId, 
                        answer, 
                        securityLabelNumber, 
                        valuedItem);
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
                private CodeableConcept scope;
                private List<CodeableConcept> type = new ArrayList<>();
                private List<Reference> typeReference = new ArrayList<>();
                private List<CodeableConcept> subtype = new ArrayList<>();
                private Coding relationship;
                private List<Context> context = new ArrayList<>();
                private String condition;
                private List<CodeableConcept> periodType = new ArrayList<>();
                private List<Period> period = new ArrayList<>();
                private List<Period> usePeriod = new ArrayList<>();
                private String text;
                private List<String> linkId = new ArrayList<>();
                private List<Contract.Term.Offer.Answer> answer = new ArrayList<>();
                private List<UnsignedInt> securityLabelNumber = new ArrayList<>();
                private List<ValuedItem> valuedItem = new ArrayList<>();

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
                 * Differentiates the kind of the asset .
                 * </p>
                 * 
                 * @param scope
                 *     Range of asset
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder scope(CodeableConcept scope) {
                    this.scope = scope;
                    return this;
                }

                /**
                 * <p>
                 * Target entity type about which the term may be concerned.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param type
                 *     Asset category
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
                 * Target entity type about which the term may be concerned.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param type
                 *     Asset category
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
                 * Associated entities.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param typeReference
                 *     Associated entities
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder typeReference(Reference... typeReference) {
                    for (Reference value : typeReference) {
                        this.typeReference.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Associated entities.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param typeReference
                 *     Associated entities
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder typeReference(Collection<Reference> typeReference) {
                    this.typeReference = new ArrayList<>(typeReference);
                    return this;
                }

                /**
                 * <p>
                 * May be a subtype or part of an offered asset.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param subtype
                 *     Asset sub-category
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder subtype(CodeableConcept... subtype) {
                    for (CodeableConcept value : subtype) {
                        this.subtype.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * May be a subtype or part of an offered asset.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param subtype
                 *     Asset sub-category
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder subtype(Collection<CodeableConcept> subtype) {
                    this.subtype = new ArrayList<>(subtype);
                    return this;
                }

                /**
                 * <p>
                 * Specifies the applicability of the term to an asset resource instance, and instances it refers to orinstances that 
                 * refer to it, and/or are owned by the offeree.
                 * </p>
                 * 
                 * @param relationship
                 *     Kinship of the asset
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder relationship(Coding relationship) {
                    this.relationship = relationship;
                    return this;
                }

                /**
                 * <p>
                 * Circumstance of the asset.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param context
                 *     Circumstance of the asset
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder context(Context... context) {
                    for (Context value : context) {
                        this.context.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Circumstance of the asset.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param context
                 *     Circumstance of the asset
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder context(Collection<Context> context) {
                    this.context = new ArrayList<>(context);
                    return this;
                }

                /**
                 * <p>
                 * Description of the quality and completeness of the asset that imay be a factor in its valuation.
                 * </p>
                 * 
                 * @param condition
                 *     Quality desctiption of asset
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder condition(String condition) {
                    this.condition = condition;
                    return this;
                }

                /**
                 * <p>
                 * Type of Asset availability for use or ownership.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param periodType
                 *     Asset availability types
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder periodType(CodeableConcept... periodType) {
                    for (CodeableConcept value : periodType) {
                        this.periodType.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Type of Asset availability for use or ownership.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param periodType
                 *     Asset availability types
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder periodType(Collection<CodeableConcept> periodType) {
                    this.periodType = new ArrayList<>(periodType);
                    return this;
                }

                /**
                 * <p>
                 * Asset relevant contractual time period.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param period
                 *     Time period of the asset
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder period(Period... period) {
                    for (Period value : period) {
                        this.period.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Asset relevant contractual time period.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param period
                 *     Time period of the asset
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder period(Collection<Period> period) {
                    this.period = new ArrayList<>(period);
                    return this;
                }

                /**
                 * <p>
                 * Time period of asset use.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param usePeriod
                 *     Time period
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder usePeriod(Period... usePeriod) {
                    for (Period value : usePeriod) {
                        this.usePeriod.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Time period of asset use.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param usePeriod
                 *     Time period
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder usePeriod(Collection<Period> usePeriod) {
                    this.usePeriod = new ArrayList<>(usePeriod);
                    return this;
                }

                /**
                 * <p>
                 * Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in 
                 * the formation of the contract.
                 * </p>
                 * 
                 * @param text
                 *     Asset clause or question text
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder text(String text) {
                    this.text = text;
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param linkId
                 *     Pointer to asset text
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder linkId(String... linkId) {
                    for (String value : linkId) {
                        this.linkId.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param linkId
                 *     Pointer to asset text
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder linkId(Collection<String> linkId) {
                    this.linkId = new ArrayList<>(linkId);
                    return this;
                }

                /**
                 * <p>
                 * Response to assets.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param answer
                 *     Response to assets
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder answer(Contract.Term.Offer.Answer... answer) {
                    for (Contract.Term.Offer.Answer value : answer) {
                        this.answer.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Response to assets.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param answer
                 *     Response to assets
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder answer(Collection<Contract.Term.Offer.Answer> answer) {
                    this.answer = new ArrayList<>(answer);
                    return this;
                }

                /**
                 * <p>
                 * Security labels that protects the asset.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param securityLabelNumber
                 *     Asset restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder securityLabelNumber(UnsignedInt... securityLabelNumber) {
                    for (UnsignedInt value : securityLabelNumber) {
                        this.securityLabelNumber.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Security labels that protects the asset.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param securityLabelNumber
                 *     Asset restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder securityLabelNumber(Collection<UnsignedInt> securityLabelNumber) {
                    this.securityLabelNumber = new ArrayList<>(securityLabelNumber);
                    return this;
                }

                /**
                 * <p>
                 * Contract Valued Item List.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param valuedItem
                 *     Contract Valued Item List
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder valuedItem(ValuedItem... valuedItem) {
                    for (ValuedItem value : valuedItem) {
                        this.valuedItem.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Contract Valued Item List.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param valuedItem
                 *     Contract Valued Item List
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder valuedItem(Collection<ValuedItem> valuedItem) {
                    this.valuedItem = new ArrayList<>(valuedItem);
                    return this;
                }

                @Override
                public Asset build() {
                    return new Asset(this);
                }

                private Builder from(Asset asset) {
                    id = asset.id;
                    extension.addAll(asset.extension);
                    modifierExtension.addAll(asset.modifierExtension);
                    scope = asset.scope;
                    type.addAll(asset.type);
                    typeReference.addAll(asset.typeReference);
                    subtype.addAll(asset.subtype);
                    relationship = asset.relationship;
                    context.addAll(asset.context);
                    condition = asset.condition;
                    periodType.addAll(asset.periodType);
                    period.addAll(asset.period);
                    usePeriod.addAll(asset.usePeriod);
                    text = asset.text;
                    linkId.addAll(asset.linkId);
                    answer.addAll(asset.answer);
                    securityLabelNumber.addAll(asset.securityLabelNumber);
                    valuedItem.addAll(asset.valuedItem);
                    return this;
                }
            }

            /**
             * <p>
             * Circumstance of the asset.
             * </p>
             */
            public static class Context extends BackboneElement {
                private final Reference reference;
                private final List<CodeableConcept> code;
                private final String text;

                private volatile int hashCode;

                private Context(Builder builder) {
                    super(builder);
                    reference = builder.reference;
                    code = Collections.unmodifiableList(builder.code);
                    text = builder.text;
                    if (!hasChildren()) {
                        throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                    }
                }

                /**
                 * <p>
                 * Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository), 
                 * location held, e.g., building, jurisdiction.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Reference}.
                 */
                public Reference getReference() {
                    return reference;
                }

                /**
                 * <p>
                 * Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.
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
                 * Context description.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getText() {
                    return text;
                }

                @Override
                protected boolean hasChildren() {
                    return super.hasChildren() || 
                        (reference != null) || 
                        !code.isEmpty() || 
                        (text != null);
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
                            accept(reference, "reference", visitor);
                            accept(code, "code", visitor, CodeableConcept.class);
                            accept(text, "text", visitor);
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
                    Context other = (Context) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(reference, other.reference) && 
                        Objects.equals(code, other.code) && 
                        Objects.equals(text, other.text);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            reference, 
                            code, 
                            text);
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
                    private Reference reference;
                    private List<CodeableConcept> code = new ArrayList<>();
                    private String text;

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
                     * Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository), 
                     * location held, e.g., building, jurisdiction.
                     * </p>
                     * 
                     * @param reference
                     *     Creator,custodian or owner
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder reference(Reference reference) {
                        this.reference = reference;
                        return this;
                    }

                    /**
                     * <p>
                     * Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.
                     * </p>
                     * <p>
                     * Adds new element(s) to the existing list
                     * </p>
                     * 
                     * @param code
                     *     Codeable asset context
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
                     * Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param code
                     *     Codeable asset context
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
                     * Context description.
                     * </p>
                     * 
                     * @param text
                     *     Context description
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder text(String text) {
                        this.text = text;
                        return this;
                    }

                    @Override
                    public Context build() {
                        return new Context(this);
                    }

                    private Builder from(Context context) {
                        id = context.id;
                        extension.addAll(context.extension);
                        modifierExtension.addAll(context.modifierExtension);
                        reference = context.reference;
                        code.addAll(context.code);
                        text = context.text;
                        return this;
                    }
                }
            }

            /**
             * <p>
             * Contract Valued Item List.
             * </p>
             */
            public static class ValuedItem extends BackboneElement {
                private final Element entity;
                private final Identifier identifier;
                private final DateTime effectiveTime;
                private final Quantity quantity;
                private final Money unitPrice;
                private final Decimal factor;
                private final Decimal points;
                private final Money net;
                private final String payment;
                private final DateTime paymentDate;
                private final Reference responsible;
                private final Reference recipient;
                private final List<String> linkId;
                private final List<UnsignedInt> securityLabelNumber;

                private volatile int hashCode;

                private ValuedItem(Builder builder) {
                    super(builder);
                    entity = ValidationSupport.choiceElement(builder.entity, "entity", CodeableConcept.class, Reference.class);
                    identifier = builder.identifier;
                    effectiveTime = builder.effectiveTime;
                    quantity = builder.quantity;
                    unitPrice = builder.unitPrice;
                    factor = builder.factor;
                    points = builder.points;
                    net = builder.net;
                    payment = builder.payment;
                    paymentDate = builder.paymentDate;
                    responsible = builder.responsible;
                    recipient = builder.recipient;
                    linkId = Collections.unmodifiableList(builder.linkId);
                    securityLabelNumber = Collections.unmodifiableList(builder.securityLabelNumber);
                    if (!hasChildren()) {
                        throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                    }
                }

                /**
                 * <p>
                 * Specific type of Contract Valued Item that may be priced.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Element}.
                 */
                public Element getEntity() {
                    return entity;
                }

                /**
                 * <p>
                 * Identifies a Contract Valued Item instance.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Identifier}.
                 */
                public Identifier getIdentifier() {
                    return identifier;
                }

                /**
                 * <p>
                 * Indicates the time during which this Contract ValuedItem information is effective.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link DateTime}.
                 */
                public DateTime getEffectiveTime() {
                    return effectiveTime;
                }

                /**
                 * <p>
                 * Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or 
                 * measurable Contract Valued Item instances.
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
                 * A Contract Valued Item unit valuation measure.
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
                 * A real number that represents a multiplier used in determining the overall value of the Contract Valued Item 
                 * delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Decimal}.
                 */
                public Decimal getFactor() {
                    return factor;
                }

                /**
                 * <p>
                 * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the 
                 * Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued 
                 * Item, such that a monetary amount can be assigned to each point.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Decimal}.
                 */
                public Decimal getPoints() {
                    return points;
                }

                /**
                 * <p>
                 * Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit 
                 * Quantity * unit Price (Cost per Point) * factor Number * points = net Amount. Quantity, factor and points are assumed 
                 * to be 1 if not supplied.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Money}.
                 */
                public Money getNet() {
                    return net;
                }

                /**
                 * <p>
                 * Terms of valuation.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getPayment() {
                    return payment;
                }

                /**
                 * <p>
                 * When payment is due.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link DateTime}.
                 */
                public DateTime getPaymentDate() {
                    return paymentDate;
                }

                /**
                 * <p>
                 * Who will make payment.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Reference}.
                 */
                public Reference getResponsible() {
                    return responsible;
                }

                /**
                 * <p>
                 * Who will receive payment.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Reference}.
                 */
                public Reference getRecipient() {
                    return recipient;
                }

                /**
                 * <p>
                 * Id of the clause or question text related to the context of this valuedItem in the referenced form or 
                 * QuestionnaireResponse.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link String}.
                 */
                public List<String> getLinkId() {
                    return linkId;
                }

                /**
                 * <p>
                 * A set of security labels that define which terms are controlled by this condition.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link UnsignedInt}.
                 */
                public List<UnsignedInt> getSecurityLabelNumber() {
                    return securityLabelNumber;
                }

                @Override
                protected boolean hasChildren() {
                    return super.hasChildren() || 
                        (entity != null) || 
                        (identifier != null) || 
                        (effectiveTime != null) || 
                        (quantity != null) || 
                        (unitPrice != null) || 
                        (factor != null) || 
                        (points != null) || 
                        (net != null) || 
                        (payment != null) || 
                        (paymentDate != null) || 
                        (responsible != null) || 
                        (recipient != null) || 
                        !linkId.isEmpty() || 
                        !securityLabelNumber.isEmpty();
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
                            accept(entity, "entity", visitor);
                            accept(identifier, "identifier", visitor);
                            accept(effectiveTime, "effectiveTime", visitor);
                            accept(quantity, "quantity", visitor);
                            accept(unitPrice, "unitPrice", visitor);
                            accept(factor, "factor", visitor);
                            accept(points, "points", visitor);
                            accept(net, "net", visitor);
                            accept(payment, "payment", visitor);
                            accept(paymentDate, "paymentDate", visitor);
                            accept(responsible, "responsible", visitor);
                            accept(recipient, "recipient", visitor);
                            accept(linkId, "linkId", visitor, String.class);
                            accept(securityLabelNumber, "securityLabelNumber", visitor, UnsignedInt.class);
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
                    ValuedItem other = (ValuedItem) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(entity, other.entity) && 
                        Objects.equals(identifier, other.identifier) && 
                        Objects.equals(effectiveTime, other.effectiveTime) && 
                        Objects.equals(quantity, other.quantity) && 
                        Objects.equals(unitPrice, other.unitPrice) && 
                        Objects.equals(factor, other.factor) && 
                        Objects.equals(points, other.points) && 
                        Objects.equals(net, other.net) && 
                        Objects.equals(payment, other.payment) && 
                        Objects.equals(paymentDate, other.paymentDate) && 
                        Objects.equals(responsible, other.responsible) && 
                        Objects.equals(recipient, other.recipient) && 
                        Objects.equals(linkId, other.linkId) && 
                        Objects.equals(securityLabelNumber, other.securityLabelNumber);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            entity, 
                            identifier, 
                            effectiveTime, 
                            quantity, 
                            unitPrice, 
                            factor, 
                            points, 
                            net, 
                            payment, 
                            paymentDate, 
                            responsible, 
                            recipient, 
                            linkId, 
                            securityLabelNumber);
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
                    private Element entity;
                    private Identifier identifier;
                    private DateTime effectiveTime;
                    private Quantity quantity;
                    private Money unitPrice;
                    private Decimal factor;
                    private Decimal points;
                    private Money net;
                    private String payment;
                    private DateTime paymentDate;
                    private Reference responsible;
                    private Reference recipient;
                    private List<String> linkId = new ArrayList<>();
                    private List<UnsignedInt> securityLabelNumber = new ArrayList<>();

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
                     * Specific type of Contract Valued Item that may be priced.
                     * </p>
                     * 
                     * @param entity
                     *     Contract Valued Item Type
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder entity(Element entity) {
                        this.entity = entity;
                        return this;
                    }

                    /**
                     * <p>
                     * Identifies a Contract Valued Item instance.
                     * </p>
                     * 
                     * @param identifier
                     *     Contract Valued Item Number
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder identifier(Identifier identifier) {
                        this.identifier = identifier;
                        return this;
                    }

                    /**
                     * <p>
                     * Indicates the time during which this Contract ValuedItem information is effective.
                     * </p>
                     * 
                     * @param effectiveTime
                     *     Contract Valued Item Effective Tiem
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder effectiveTime(DateTime effectiveTime) {
                        this.effectiveTime = effectiveTime;
                        return this;
                    }

                    /**
                     * <p>
                     * Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or 
                     * measurable Contract Valued Item instances.
                     * </p>
                     * 
                     * @param quantity
                     *     Count of Contract Valued Items
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
                     * A Contract Valued Item unit valuation measure.
                     * </p>
                     * 
                     * @param unitPrice
                     *     Contract Valued Item fee, charge, or cost
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
                     * A real number that represents a multiplier used in determining the overall value of the Contract Valued Item 
                     * delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                     * </p>
                     * 
                     * @param factor
                     *     Contract Valued Item Price Scaling Factor
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder factor(Decimal factor) {
                        this.factor = factor;
                        return this;
                    }

                    /**
                     * <p>
                     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the 
                     * Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued 
                     * Item, such that a monetary amount can be assigned to each point.
                     * </p>
                     * 
                     * @param points
                     *     Contract Valued Item Difficulty Scaling Factor
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder points(Decimal points) {
                        this.points = points;
                        return this;
                    }

                    /**
                     * <p>
                     * Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit 
                     * Quantity * unit Price (Cost per Point) * factor Number * points = net Amount. Quantity, factor and points are assumed 
                     * to be 1 if not supplied.
                     * </p>
                     * 
                     * @param net
                     *     Total Contract Valued Item Value
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder net(Money net) {
                        this.net = net;
                        return this;
                    }

                    /**
                     * <p>
                     * Terms of valuation.
                     * </p>
                     * 
                     * @param payment
                     *     Terms of valuation
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder payment(String payment) {
                        this.payment = payment;
                        return this;
                    }

                    /**
                     * <p>
                     * When payment is due.
                     * </p>
                     * 
                     * @param paymentDate
                     *     When payment is due
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder paymentDate(DateTime paymentDate) {
                        this.paymentDate = paymentDate;
                        return this;
                    }

                    /**
                     * <p>
                     * Who will make payment.
                     * </p>
                     * 
                     * @param responsible
                     *     Who will make payment
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder responsible(Reference responsible) {
                        this.responsible = responsible;
                        return this;
                    }

                    /**
                     * <p>
                     * Who will receive payment.
                     * </p>
                     * 
                     * @param recipient
                     *     Who will receive payment
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder recipient(Reference recipient) {
                        this.recipient = recipient;
                        return this;
                    }

                    /**
                     * <p>
                     * Id of the clause or question text related to the context of this valuedItem in the referenced form or 
                     * QuestionnaireResponse.
                     * </p>
                     * <p>
                     * Adds new element(s) to the existing list
                     * </p>
                     * 
                     * @param linkId
                     *     Pointer to specific item
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder linkId(String... linkId) {
                        for (String value : linkId) {
                            this.linkId.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * Id of the clause or question text related to the context of this valuedItem in the referenced form or 
                     * QuestionnaireResponse.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param linkId
                     *     Pointer to specific item
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder linkId(Collection<String> linkId) {
                        this.linkId = new ArrayList<>(linkId);
                        return this;
                    }

                    /**
                     * <p>
                     * A set of security labels that define which terms are controlled by this condition.
                     * </p>
                     * <p>
                     * Adds new element(s) to the existing list
                     * </p>
                     * 
                     * @param securityLabelNumber
                     *     Security Labels that define affected terms
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder securityLabelNumber(UnsignedInt... securityLabelNumber) {
                        for (UnsignedInt value : securityLabelNumber) {
                            this.securityLabelNumber.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * A set of security labels that define which terms are controlled by this condition.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param securityLabelNumber
                     *     Security Labels that define affected terms
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder securityLabelNumber(Collection<UnsignedInt> securityLabelNumber) {
                        this.securityLabelNumber = new ArrayList<>(securityLabelNumber);
                        return this;
                    }

                    @Override
                    public ValuedItem build() {
                        return new ValuedItem(this);
                    }

                    private Builder from(ValuedItem valuedItem) {
                        id = valuedItem.id;
                        extension.addAll(valuedItem.extension);
                        modifierExtension.addAll(valuedItem.modifierExtension);
                        entity = valuedItem.entity;
                        identifier = valuedItem.identifier;
                        effectiveTime = valuedItem.effectiveTime;
                        quantity = valuedItem.quantity;
                        unitPrice = valuedItem.unitPrice;
                        factor = valuedItem.factor;
                        points = valuedItem.points;
                        net = valuedItem.net;
                        payment = valuedItem.payment;
                        paymentDate = valuedItem.paymentDate;
                        responsible = valuedItem.responsible;
                        recipient = valuedItem.recipient;
                        linkId.addAll(valuedItem.linkId);
                        securityLabelNumber.addAll(valuedItem.securityLabelNumber);
                        return this;
                    }
                }
            }
        }

        /**
         * <p>
         * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
         * taking place.
         * </p>
         */
        public static class Action extends BackboneElement {
            private final Boolean doNotPerform;
            private final CodeableConcept type;
            private final List<Subject> subject;
            private final CodeableConcept intent;
            private final List<String> linkId;
            private final CodeableConcept status;
            private final Reference context;
            private final List<String> contextLinkId;
            private final Element occurrence;
            private final List<Reference> requester;
            private final List<String> requesterLinkId;
            private final List<CodeableConcept> performerType;
            private final CodeableConcept performerRole;
            private final Reference performer;
            private final List<String> performerLinkId;
            private final List<CodeableConcept> reasonCode;
            private final List<Reference> reasonReference;
            private final List<String> reason;
            private final List<String> reasonLinkId;
            private final List<Annotation> note;
            private final List<UnsignedInt> securityLabelNumber;

            private volatile int hashCode;

            private Action(Builder builder) {
                super(builder);
                doNotPerform = builder.doNotPerform;
                type = ValidationSupport.requireNonNull(builder.type, "type");
                subject = Collections.unmodifiableList(builder.subject);
                intent = ValidationSupport.requireNonNull(builder.intent, "intent");
                linkId = Collections.unmodifiableList(builder.linkId);
                status = ValidationSupport.requireNonNull(builder.status, "status");
                context = builder.context;
                contextLinkId = Collections.unmodifiableList(builder.contextLinkId);
                occurrence = ValidationSupport.choiceElement(builder.occurrence, "occurrence", DateTime.class, Period.class, Timing.class);
                requester = Collections.unmodifiableList(builder.requester);
                requesterLinkId = Collections.unmodifiableList(builder.requesterLinkId);
                performerType = Collections.unmodifiableList(builder.performerType);
                performerRole = builder.performerRole;
                performer = builder.performer;
                performerLinkId = Collections.unmodifiableList(builder.performerLinkId);
                reasonCode = Collections.unmodifiableList(builder.reasonCode);
                reasonReference = Collections.unmodifiableList(builder.reasonReference);
                reason = Collections.unmodifiableList(builder.reason);
                reasonLinkId = Collections.unmodifiableList(builder.reasonLinkId);
                note = Collections.unmodifiableList(builder.note);
                securityLabelNumber = Collections.unmodifiableList(builder.securityLabelNumber);
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * True if the term prohibits the action.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getDoNotPerform() {
                return doNotPerform;
            }

            /**
             * <p>
             * Activity or service obligation to be done or not done, performed or not performed, effectuated or not by this Contract 
             * term.
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
             * Entity of the action.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Subject}.
             */
            public List<Subject> getSubject() {
                return subject;
            }

            /**
             * <p>
             * Reason or purpose for the action stipulated by this Contract Provision.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getIntent() {
                return intent;
            }

            /**
             * <p>
             * Id [identifier??] of the clause or question text related to this action in the referenced form or 
             * QuestionnaireResponse.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String}.
             */
            public List<String> getLinkId() {
                return linkId;
            }

            /**
             * <p>
             * Current state of the term action.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getStatus() {
                return status;
            }

            /**
             * <p>
             * Encounter or Episode with primary association to specified term activity.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Reference}.
             */
            public Reference getContext() {
                return context;
            }

            /**
             * <p>
             * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
             * QuestionnaireResponse.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String}.
             */
            public List<String> getContextLinkId() {
                return contextLinkId;
            }

            /**
             * <p>
             * When action happens.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getOccurrence() {
                return occurrence;
            }

            /**
             * <p>
             * Who or what initiated the action and has responsibility for its activation.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Reference}.
             */
            public List<Reference> getRequester() {
                return requester;
            }

            /**
             * <p>
             * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
             * QuestionnaireResponse.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String}.
             */
            public List<String> getRequesterLinkId() {
                return requesterLinkId;
            }

            /**
             * <p>
             * The type of individual that is desired or required to perform or not perform the action.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getPerformerType() {
                return performerType;
            }

            /**
             * <p>
             * The type of role or competency of an individual desired or required to perform or not perform the action.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getPerformerRole() {
                return performerRole;
            }

            /**
             * <p>
             * Indicates who or what is being asked to perform (or not perform) the ction.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Reference}.
             */
            public Reference getPerformer() {
                return performer;
            }

            /**
             * <p>
             * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
             * referenced form or QuestionnaireResponse.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String}.
             */
            public List<String> getPerformerLinkId() {
                return performerLinkId;
            }

            /**
             * <p>
             * Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getReasonCode() {
                return reasonCode;
            }

            /**
             * <p>
             * Indicates another resource whose existence justifies permitting or not permitting this action.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Reference}.
             */
            public List<Reference> getReasonReference() {
                return reasonReference;
            }

            /**
             * <p>
             * Describes why the action is to be performed or not performed in textual form.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String}.
             */
            public List<String> getReason() {
                return reason;
            }

            /**
             * <p>
             * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
             * referenced form or QuestionnaireResponse.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String}.
             */
            public List<String> getReasonLinkId() {
                return reasonLinkId;
            }

            /**
             * <p>
             * Comments made about the term action made by the requester, performer, subject or other participants.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Annotation}.
             */
            public List<Annotation> getNote() {
                return note;
            }

            /**
             * <p>
             * Security labels that protects the action.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link UnsignedInt}.
             */
            public List<UnsignedInt> getSecurityLabelNumber() {
                return securityLabelNumber;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (doNotPerform != null) || 
                    (type != null) || 
                    !subject.isEmpty() || 
                    (intent != null) || 
                    !linkId.isEmpty() || 
                    (status != null) || 
                    (context != null) || 
                    !contextLinkId.isEmpty() || 
                    (occurrence != null) || 
                    !requester.isEmpty() || 
                    !requesterLinkId.isEmpty() || 
                    !performerType.isEmpty() || 
                    (performerRole != null) || 
                    (performer != null) || 
                    !performerLinkId.isEmpty() || 
                    !reasonCode.isEmpty() || 
                    !reasonReference.isEmpty() || 
                    !reason.isEmpty() || 
                    !reasonLinkId.isEmpty() || 
                    !note.isEmpty() || 
                    !securityLabelNumber.isEmpty();
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
                        accept(doNotPerform, "doNotPerform", visitor);
                        accept(type, "type", visitor);
                        accept(subject, "subject", visitor, Subject.class);
                        accept(intent, "intent", visitor);
                        accept(linkId, "linkId", visitor, String.class);
                        accept(status, "status", visitor);
                        accept(context, "context", visitor);
                        accept(contextLinkId, "contextLinkId", visitor, String.class);
                        accept(occurrence, "occurrence", visitor);
                        accept(requester, "requester", visitor, Reference.class);
                        accept(requesterLinkId, "requesterLinkId", visitor, String.class);
                        accept(performerType, "performerType", visitor, CodeableConcept.class);
                        accept(performerRole, "performerRole", visitor);
                        accept(performer, "performer", visitor);
                        accept(performerLinkId, "performerLinkId", visitor, String.class);
                        accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                        accept(reasonReference, "reasonReference", visitor, Reference.class);
                        accept(reason, "reason", visitor, String.class);
                        accept(reasonLinkId, "reasonLinkId", visitor, String.class);
                        accept(note, "note", visitor, Annotation.class);
                        accept(securityLabelNumber, "securityLabelNumber", visitor, UnsignedInt.class);
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
                Action other = (Action) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(doNotPerform, other.doNotPerform) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(subject, other.subject) && 
                    Objects.equals(intent, other.intent) && 
                    Objects.equals(linkId, other.linkId) && 
                    Objects.equals(status, other.status) && 
                    Objects.equals(context, other.context) && 
                    Objects.equals(contextLinkId, other.contextLinkId) && 
                    Objects.equals(occurrence, other.occurrence) && 
                    Objects.equals(requester, other.requester) && 
                    Objects.equals(requesterLinkId, other.requesterLinkId) && 
                    Objects.equals(performerType, other.performerType) && 
                    Objects.equals(performerRole, other.performerRole) && 
                    Objects.equals(performer, other.performer) && 
                    Objects.equals(performerLinkId, other.performerLinkId) && 
                    Objects.equals(reasonCode, other.reasonCode) && 
                    Objects.equals(reasonReference, other.reasonReference) && 
                    Objects.equals(reason, other.reason) && 
                    Objects.equals(reasonLinkId, other.reasonLinkId) && 
                    Objects.equals(note, other.note) && 
                    Objects.equals(securityLabelNumber, other.securityLabelNumber);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        doNotPerform, 
                        type, 
                        subject, 
                        intent, 
                        linkId, 
                        status, 
                        context, 
                        contextLinkId, 
                        occurrence, 
                        requester, 
                        requesterLinkId, 
                        performerType, 
                        performerRole, 
                        performer, 
                        performerLinkId, 
                        reasonCode, 
                        reasonReference, 
                        reason, 
                        reasonLinkId, 
                        note, 
                        securityLabelNumber);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(type, intent, status).from(this);
            }

            public Builder toBuilder(CodeableConcept type, CodeableConcept intent, CodeableConcept status) {
                return new Builder(type, intent, status).from(this);
            }

            public static Builder builder(CodeableConcept type, CodeableConcept intent, CodeableConcept status) {
                return new Builder(type, intent, status);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept type;
                private final CodeableConcept intent;
                private final CodeableConcept status;

                // optional
                private Boolean doNotPerform;
                private List<Subject> subject = new ArrayList<>();
                private List<String> linkId = new ArrayList<>();
                private Reference context;
                private List<String> contextLinkId = new ArrayList<>();
                private Element occurrence;
                private List<Reference> requester = new ArrayList<>();
                private List<String> requesterLinkId = new ArrayList<>();
                private List<CodeableConcept> performerType = new ArrayList<>();
                private CodeableConcept performerRole;
                private Reference performer;
                private List<String> performerLinkId = new ArrayList<>();
                private List<CodeableConcept> reasonCode = new ArrayList<>();
                private List<Reference> reasonReference = new ArrayList<>();
                private List<String> reason = new ArrayList<>();
                private List<String> reasonLinkId = new ArrayList<>();
                private List<Annotation> note = new ArrayList<>();
                private List<UnsignedInt> securityLabelNumber = new ArrayList<>();

                private Builder(CodeableConcept type, CodeableConcept intent, CodeableConcept status) {
                    super();
                    this.type = type;
                    this.intent = intent;
                    this.status = status;
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
                 * True if the term prohibits the action.
                 * </p>
                 * 
                 * @param doNotPerform
                 *     True if the term prohibits the action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder doNotPerform(Boolean doNotPerform) {
                    this.doNotPerform = doNotPerform;
                    return this;
                }

                /**
                 * <p>
                 * Entity of the action.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param subject
                 *     Entity of the action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder subject(Subject... subject) {
                    for (Subject value : subject) {
                        this.subject.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Entity of the action.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param subject
                 *     Entity of the action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder subject(Collection<Subject> subject) {
                    this.subject = new ArrayList<>(subject);
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to this action in the referenced form or 
                 * QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param linkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder linkId(String... linkId) {
                    for (String value : linkId) {
                        this.linkId.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to this action in the referenced form or 
                 * QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param linkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder linkId(Collection<String> linkId) {
                    this.linkId = new ArrayList<>(linkId);
                    return this;
                }

                /**
                 * <p>
                 * Encounter or Episode with primary association to specified term activity.
                 * </p>
                 * 
                 * @param context
                 *     Episode associated with action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder context(Reference context) {
                    this.context = context;
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
                 * QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param contextLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder contextLinkId(String... contextLinkId) {
                    for (String value : contextLinkId) {
                        this.contextLinkId.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
                 * QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param contextLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder contextLinkId(Collection<String> contextLinkId) {
                    this.contextLinkId = new ArrayList<>(contextLinkId);
                    return this;
                }

                /**
                 * <p>
                 * When action happens.
                 * </p>
                 * 
                 * @param occurrence
                 *     When action happens
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder occurrence(Element occurrence) {
                    this.occurrence = occurrence;
                    return this;
                }

                /**
                 * <p>
                 * Who or what initiated the action and has responsibility for its activation.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param requester
                 *     Who asked for action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder requester(Reference... requester) {
                    for (Reference value : requester) {
                        this.requester.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Who or what initiated the action and has responsibility for its activation.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param requester
                 *     Who asked for action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder requester(Collection<Reference> requester) {
                    this.requester = new ArrayList<>(requester);
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
                 * QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param requesterLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder requesterLinkId(String... requesterLinkId) {
                    for (String value : requesterLinkId) {
                        this.requesterLinkId.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
                 * QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param requesterLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder requesterLinkId(Collection<String> requesterLinkId) {
                    this.requesterLinkId = new ArrayList<>(requesterLinkId);
                    return this;
                }

                /**
                 * <p>
                 * The type of individual that is desired or required to perform or not perform the action.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param performerType
                 *     Kind of service performer
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder performerType(CodeableConcept... performerType) {
                    for (CodeableConcept value : performerType) {
                        this.performerType.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The type of individual that is desired or required to perform or not perform the action.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param performerType
                 *     Kind of service performer
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder performerType(Collection<CodeableConcept> performerType) {
                    this.performerType = new ArrayList<>(performerType);
                    return this;
                }

                /**
                 * <p>
                 * The type of role or competency of an individual desired or required to perform or not perform the action.
                 * </p>
                 * 
                 * @param performerRole
                 *     Competency of the performer
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder performerRole(CodeableConcept performerRole) {
                    this.performerRole = performerRole;
                    return this;
                }

                /**
                 * <p>
                 * Indicates who or what is being asked to perform (or not perform) the ction.
                 * </p>
                 * 
                 * @param performer
                 *     Actor that wil execute (or not) the action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder performer(Reference performer) {
                    this.performer = performer;
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
                 * referenced form or QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param performerLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder performerLinkId(String... performerLinkId) {
                    for (String value : performerLinkId) {
                        this.performerLinkId.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
                 * referenced form or QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param performerLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder performerLinkId(Collection<String> performerLinkId) {
                    this.performerLinkId = new ArrayList<>(performerLinkId);
                    return this;
                }

                /**
                 * <p>
                 * Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param reasonCode
                 *     Why is action (not) needed?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reasonCode(CodeableConcept... reasonCode) {
                    for (CodeableConcept value : reasonCode) {
                        this.reasonCode.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param reasonCode
                 *     Why is action (not) needed?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
                    this.reasonCode = new ArrayList<>(reasonCode);
                    return this;
                }

                /**
                 * <p>
                 * Indicates another resource whose existence justifies permitting or not permitting this action.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param reasonReference
                 *     Why is action (not) needed?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reasonReference(Reference... reasonReference) {
                    for (Reference value : reasonReference) {
                        this.reasonReference.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Indicates another resource whose existence justifies permitting or not permitting this action.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param reasonReference
                 *     Why is action (not) needed?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reasonReference(Collection<Reference> reasonReference) {
                    this.reasonReference = new ArrayList<>(reasonReference);
                    return this;
                }

                /**
                 * <p>
                 * Describes why the action is to be performed or not performed in textual form.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param reason
                 *     Why action is to be performed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reason(String... reason) {
                    for (String value : reason) {
                        this.reason.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Describes why the action is to be performed or not performed in textual form.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param reason
                 *     Why action is to be performed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reason(Collection<String> reason) {
                    this.reason = new ArrayList<>(reason);
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
                 * referenced form or QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param reasonLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reasonLinkId(String... reasonLinkId) {
                    for (String value : reasonLinkId) {
                        this.reasonLinkId.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
                 * referenced form or QuestionnaireResponse.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param reasonLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reasonLinkId(Collection<String> reasonLinkId) {
                    this.reasonLinkId = new ArrayList<>(reasonLinkId);
                    return this;
                }

                /**
                 * <p>
                 * Comments made about the term action made by the requester, performer, subject or other participants.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param note
                 *     Comments about the action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder note(Annotation... note) {
                    for (Annotation value : note) {
                        this.note.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Comments made about the term action made by the requester, performer, subject or other participants.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param note
                 *     Comments about the action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder note(Collection<Annotation> note) {
                    this.note = new ArrayList<>(note);
                    return this;
                }

                /**
                 * <p>
                 * Security labels that protects the action.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param securityLabelNumber
                 *     Action restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder securityLabelNumber(UnsignedInt... securityLabelNumber) {
                    for (UnsignedInt value : securityLabelNumber) {
                        this.securityLabelNumber.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Security labels that protects the action.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param securityLabelNumber
                 *     Action restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder securityLabelNumber(Collection<UnsignedInt> securityLabelNumber) {
                    this.securityLabelNumber = new ArrayList<>(securityLabelNumber);
                    return this;
                }

                @Override
                public Action build() {
                    return new Action(this);
                }

                private Builder from(Action action) {
                    id = action.id;
                    extension.addAll(action.extension);
                    modifierExtension.addAll(action.modifierExtension);
                    doNotPerform = action.doNotPerform;
                    subject.addAll(action.subject);
                    linkId.addAll(action.linkId);
                    context = action.context;
                    contextLinkId.addAll(action.contextLinkId);
                    occurrence = action.occurrence;
                    requester.addAll(action.requester);
                    requesterLinkId.addAll(action.requesterLinkId);
                    performerType.addAll(action.performerType);
                    performerRole = action.performerRole;
                    performer = action.performer;
                    performerLinkId.addAll(action.performerLinkId);
                    reasonCode.addAll(action.reasonCode);
                    reasonReference.addAll(action.reasonReference);
                    reason.addAll(action.reason);
                    reasonLinkId.addAll(action.reasonLinkId);
                    note.addAll(action.note);
                    securityLabelNumber.addAll(action.securityLabelNumber);
                    return this;
                }
            }

            /**
             * <p>
             * Entity of the action.
             * </p>
             */
            public static class Subject extends BackboneElement {
                private final List<Reference> reference;
                private final CodeableConcept role;

                private volatile int hashCode;

                private Subject(Builder builder) {
                    super(builder);
                    reference = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.reference, "reference"));
                    role = builder.role;
                    if (!hasChildren()) {
                        throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                    }
                }

                /**
                 * <p>
                 * The entity the action is performed or not performed on or for.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Reference}.
                 */
                public List<Reference> getReference() {
                    return reference;
                }

                /**
                 * <p>
                 * Role type of agent assigned roles in this Contract.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getRole() {
                    return role;
                }

                @Override
                protected boolean hasChildren() {
                    return super.hasChildren() || 
                        !reference.isEmpty() || 
                        (role != null);
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
                            accept(reference, "reference", visitor, Reference.class);
                            accept(role, "role", visitor);
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
                    Subject other = (Subject) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(reference, other.reference) && 
                        Objects.equals(role, other.role);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            reference, 
                            role);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(reference).from(this);
                }

                public Builder toBuilder(List<Reference> reference) {
                    return new Builder(reference).from(this);
                }

                public static Builder builder(List<Reference> reference) {
                    return new Builder(reference);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final List<Reference> reference;

                    // optional
                    private CodeableConcept role;

                    private Builder(List<Reference> reference) {
                        super();
                        this.reference = reference;
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
                     * Role type of agent assigned roles in this Contract.
                     * </p>
                     * 
                     * @param role
                     *     Role type of the agent
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder role(CodeableConcept role) {
                        this.role = role;
                        return this;
                    }

                    @Override
                    public Subject build() {
                        return new Subject(this);
                    }

                    private Builder from(Subject subject) {
                        id = subject.id;
                        extension.addAll(subject.extension);
                        modifierExtension.addAll(subject.modifierExtension);
                        role = subject.role;
                        return this;
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are 
     * any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the 
     * contract such as a notary or witness.
     * </p>
     */
    public static class Signer extends BackboneElement {
        private final Coding type;
        private final Reference party;
        private final List<Signature> signature;

        private volatile int hashCode;

        private Signer(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            party = ValidationSupport.requireNonNull(builder.party, "party");
            signature = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.signature, "signature"));
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Role of this Contract signer, e.g. notary, grantee.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Coding}.
         */
        public Coding getType() {
            return type;
        }

        /**
         * <p>
         * Party which is a signator to this Contract.
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
         * Legally binding Contract DSIG signature contents in Base64.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Signature}.
         */
        public List<Signature> getSignature() {
            return signature;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (party != null) || 
                !signature.isEmpty();
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
                    accept(party, "party", visitor);
                    accept(signature, "signature", visitor, Signature.class);
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
            Signer other = (Signer) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(party, other.party) && 
                Objects.equals(signature, other.signature);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    party, 
                    signature);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(type, party, signature).from(this);
        }

        public Builder toBuilder(Coding type, Reference party, List<Signature> signature) {
            return new Builder(type, party, signature).from(this);
        }

        public static Builder builder(Coding type, Reference party, List<Signature> signature) {
            return new Builder(type, party, signature);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Coding type;
            private final Reference party;
            private final List<Signature> signature;

            private Builder(Coding type, Reference party, List<Signature> signature) {
                super();
                this.type = type;
                this.party = party;
                this.signature = signature;
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

            @Override
            public Signer build() {
                return new Signer(this);
            }

            private Builder from(Signer signer) {
                id = signer.id;
                extension.addAll(signer.extension);
                modifierExtension.addAll(signer.modifierExtension);
                return this;
            }
        }
    }

    /**
     * <p>
     * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the 
     * representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a 
     * layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the 
     * Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
     * </p>
     */
    public static class Friendly extends BackboneElement {
        private final Element content;

        private volatile int hashCode;

        private Friendly(Builder builder) {
            super(builder);
            content = ValidationSupport.requireChoiceElement(builder.content, "content", Attachment.class, Reference.class);
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure 
         * understandability.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getContent() {
            return content;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (content != null);
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
                    accept(content, "content", visitor);
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
            Friendly other = (Friendly) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(content, other.content);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    content);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(content).from(this);
        }

        public Builder toBuilder(Element content) {
            return new Builder(content).from(this);
        }

        public static Builder builder(Element content) {
            return new Builder(content);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Element content;

            private Builder(Element content) {
                super();
                this.content = content;
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

            @Override
            public Friendly build() {
                return new Friendly(this);
            }

            private Builder from(Friendly friendly) {
                id = friendly.id;
                extension.addAll(friendly.extension);
                modifierExtension.addAll(friendly.modifierExtension);
                return this;
            }
        }
    }

    /**
     * <p>
     * List of Legal expressions or representations of this Contract.
     * </p>
     */
    public static class Legal extends BackboneElement {
        private final Element content;

        private volatile int hashCode;

        private Legal(Builder builder) {
            super(builder);
            content = ValidationSupport.requireChoiceElement(builder.content, "content", Attachment.class, Reference.class);
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Contract legal text in human renderable form.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getContent() {
            return content;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (content != null);
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
                    accept(content, "content", visitor);
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
            Legal other = (Legal) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(content, other.content);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    content);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(content).from(this);
        }

        public Builder toBuilder(Element content) {
            return new Builder(content).from(this);
        }

        public static Builder builder(Element content) {
            return new Builder(content);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Element content;

            private Builder(Element content) {
                super();
                this.content = content;
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

            @Override
            public Legal build() {
                return new Legal(this);
            }

            private Builder from(Legal legal) {
                id = legal.id;
                extension.addAll(legal.extension);
                modifierExtension.addAll(legal.modifierExtension);
                return this;
            }
        }
    }

    /**
     * <p>
     * List of Computable Policy Rule Language Representations of this Contract.
     * </p>
     */
    public static class Rule extends BackboneElement {
        private final Element content;

        private volatile int hashCode;

        private Rule(Builder builder) {
            super(builder);
            content = ValidationSupport.requireChoiceElement(builder.content, "content", Attachment.class, Reference.class);
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getContent() {
            return content;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (content != null);
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
                    accept(content, "content", visitor);
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
            Rule other = (Rule) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(content, other.content);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    content);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(content).from(this);
        }

        public Builder toBuilder(Element content) {
            return new Builder(content).from(this);
        }

        public static Builder builder(Element content) {
            return new Builder(content);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Element content;

            private Builder(Element content) {
                super();
                this.content = content;
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

            @Override
            public Rule build() {
                return new Rule(this);
            }

            private Builder from(Rule rule) {
                id = rule.id;
                extension.addAll(rule.extension);
                modifierExtension.addAll(rule.modifierExtension);
                return this;
            }
        }
    }
}
