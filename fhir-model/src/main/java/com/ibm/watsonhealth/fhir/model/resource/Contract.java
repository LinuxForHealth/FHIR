/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    private Contract(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.url = builder.url;
        this.version = builder.version;
        this.status = builder.status;
        this.legalState = builder.legalState;
        this.instantiatesCanonical = builder.instantiatesCanonical;
        this.instantiatesUri = builder.instantiatesUri;
        this.contentDerivative = builder.contentDerivative;
        this.issued = builder.issued;
        this.applies = builder.applies;
        this.expirationType = builder.expirationType;
        this.subject = builder.subject;
        this.authority = builder.authority;
        this.domain = builder.domain;
        this.site = builder.site;
        this.name = builder.name;
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.alias = builder.alias;
        this.author = builder.author;
        this.scope = builder.scope;
        this.topic = ValidationSupport.choiceElement(builder.topic, "topic", CodeableConcept.class, Reference.class);
        this.type = builder.type;
        this.subType = builder.subType;
        this.contentDefinition = builder.contentDefinition;
        this.term = builder.term;
        this.supportingInfo = builder.supportingInfo;
        this.relevantHistory = builder.relevantHistory;
        this.signer = builder.signer;
        this.friendly = builder.friendly;
        this.legal = builder.legal;
        this.rule = builder.rule;
        this.legallyBinding = ValidationSupport.choiceElement(builder.legallyBinding, "legallyBinding", Attachment.class, Reference.class);
    }

    /**
     * <p>
     * Unique identifier for this Contract or a derivative that references a Source Contract.
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
     *     A list containing immutable objects of type {@link Reference}.
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
     *     A list containing immutable objects of type {@link Reference}.
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
     *     A list containing immutable objects of type {@link Reference}.
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
     *     A list containing immutable objects of type {@link Reference}.
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
     *     A list containing immutable objects of type {@link String}.
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
     *     A list containing immutable objects of type {@link CodeableConcept}.
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
     *     A list containing immutable objects of type {@link Term}.
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
     *     A list containing immutable objects of type {@link Reference}.
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
     *     A list containing immutable objects of type {@link Reference}.
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
     *     A list containing immutable objects of type {@link Signer}.
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
     *     A list containing immutable objects of type {@link Friendly}.
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
     *     A list containing immutable objects of type {@link Legal}.
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
     *     A list containing immutable objects of type {@link Rule}.
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
                accept(topic, "topic", visitor, true);
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
                accept(legallyBinding, "legallyBinding", visitor, true);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.url = url;
        builder.version = version;
        builder.status = status;
        builder.legalState = legalState;
        builder.instantiatesCanonical = instantiatesCanonical;
        builder.instantiatesUri = instantiatesUri;
        builder.contentDerivative = contentDerivative;
        builder.issued = issued;
        builder.applies = applies;
        builder.expirationType = expirationType;
        builder.subject.addAll(subject);
        builder.authority.addAll(authority);
        builder.domain.addAll(domain);
        builder.site.addAll(site);
        builder.name = name;
        builder.title = title;
        builder.subtitle = subtitle;
        builder.alias.addAll(alias);
        builder.author = author;
        builder.scope = scope;
        builder.topic = topic;
        builder.type = type;
        builder.subType.addAll(subType);
        builder.contentDefinition = contentDefinition;
        builder.term.addAll(term);
        builder.supportingInfo.addAll(supportingInfo);
        builder.relevantHistory.addAll(relevantHistory);
        builder.signer.addAll(signer);
        builder.friendly.addAll(friendly);
        builder.legal.addAll(legal);
        builder.rule.addAll(rule);
        builder.legallyBinding = legallyBinding;
        return builder;
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
         * Unique identifier for this Contract or a derivative that references a Source Contract.
         * </p>
         * 
         * @param identifier
         *     Contract number
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
         * Unique identifier for this Contract or a derivative that references a Source Contract.
         * </p>
         * 
         * @param identifier
         *     Contract number
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
         * Canonical identifier for this contract, represented as a URI (globally unique).
         * </p>
         * 
         * @param url
         *     Basal definition
         * 
         * @return
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
         */
        public Builder expirationType(CodeableConcept expirationType) {
            this.expirationType = expirationType;
            return this;
        }

        /**
         * <p>
         * The target entity impacted by or of interest to parties to the agreement.
         * </p>
         * 
         * @param subject
         *     Contract Target Entity
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param subject
         *     Contract Target Entity
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subject(Collection<Reference> subject) {
            this.subject.addAll(subject);
            return this;
        }

        /**
         * <p>
         * A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the 
         * purpose of achieving some form of collective action such as the promulgation, administration and enforcement of 
         * contracts and policies.
         * </p>
         * 
         * @param authority
         *     Authority under which this Contract has standing
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param authority
         *     Authority under which this Contract has standing
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder authority(Collection<Reference> authority) {
            this.authority.addAll(authority);
            return this;
        }

        /**
         * <p>
         * Recognized governance framework or system operating with a circumscribed scope in accordance with specified 
         * principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals 
         * relative to resources.
         * </p>
         * 
         * @param domain
         *     A sphere of control governed by an authoritative jurisdiction, organization, or person
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param domain
         *     A sphere of control governed by an authoritative jurisdiction, organization, or person
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder domain(Collection<Reference> domain) {
            this.domain.addAll(domain);
            return this;
        }

        /**
         * <p>
         * Sites in which the contract is complied with, exercised, or in force.
         * </p>
         * 
         * @param site
         *     Specific Location
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param site
         *     Specific Location
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder site(Collection<Reference> site) {
            this.site.addAll(site);
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         * 
         * @param alias
         *     Acronym or short name
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param alias
         *     Acronym or short name
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder alias(Collection<String> alias) {
            this.alias.addAll(alias);
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         * 
         * @param subType
         *     Subtype within the context of type
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param subType
         *     Subtype within the context of type
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subType(Collection<CodeableConcept> subType) {
            this.subType.addAll(subType);
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
         *     A reference to this Builder instance.
         */
        public Builder contentDefinition(ContentDefinition contentDefinition) {
            this.contentDefinition = contentDefinition;
            return this;
        }

        /**
         * <p>
         * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
         * </p>
         * 
         * @param term
         *     Contract Term List
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param term
         *     Contract Term List
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder term(Collection<Term> term) {
            this.term.addAll(term);
            return this;
        }

        /**
         * <p>
         * Information that may be needed by/relevant to the performer in their execution of this term action.
         * </p>
         * 
         * @param supportingInfo
         *     Extra Information
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param supportingInfo
         *     Extra Information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInfo(Collection<Reference> supportingInfo) {
            this.supportingInfo.addAll(supportingInfo);
            return this;
        }

        /**
         * <p>
         * Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key 
         * state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract. 
         * The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.
         * html#Provenance.entity.
         * </p>
         * 
         * @param relevantHistory
         *     Key event in Contract History
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param relevantHistory
         *     Key event in Contract History
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relevantHistory(Collection<Reference> relevantHistory) {
            this.relevantHistory.addAll(relevantHistory);
            return this;
        }

        /**
         * <p>
         * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are 
         * any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the 
         * contract such as a notary or witness.
         * </p>
         * 
         * @param signer
         *     Contract Signatory
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param signer
         *     Contract Signatory
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder signer(Collection<Signer> signer) {
            this.signer.addAll(signer);
            return this;
        }

        /**
         * <p>
         * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the 
         * representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a 
         * layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the 
         * Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
         * </p>
         * 
         * @param friendly
         *     Contract Friendly Language
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param friendly
         *     Contract Friendly Language
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder friendly(Collection<Friendly> friendly) {
            this.friendly.addAll(friendly);
            return this;
        }

        /**
         * <p>
         * List of Legal expressions or representations of this Contract.
         * </p>
         * 
         * @param legal
         *     Contract Legal Language
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param legal
         *     Contract Legal Language
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder legal(Collection<Legal> legal) {
            this.legal.addAll(legal);
            return this;
        }

        /**
         * <p>
         * List of Computable Policy Rule Language Representations of this Contract.
         * </p>
         * 
         * @param rule
         *     Computable Contract Language
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param rule
         *     Computable Contract Language
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder rule(Collection<Rule> rule) {
            this.rule.addAll(rule);
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
         *     A reference to this Builder instance.
         */
        public Builder legallyBinding(Element legallyBinding) {
            this.legallyBinding = legallyBinding;
            return this;
        }

        @Override
        public Contract build() {
            return new Contract(this);
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

        private ContentDefinition(Builder builder) {
            super(builder);
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
            this.subType = builder.subType;
            this.publisher = builder.publisher;
            this.publicationDate = builder.publicationDate;
            this.publicationStatus = ValidationSupport.requireNonNull(builder.publicationStatus, "publicationStatus");
            this.copyright = builder.copyright;
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
        public Builder toBuilder() {
            return Builder.from(this);
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
             * Detailed Precusory content type.
             * </p>
             * 
             * @param subType
             *     Detailed Content Type Definition
             * 
             * @return
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder copyright(Markdown copyright) {
                this.copyright = copyright;
                return this;
            }

            @Override
            public ContentDefinition build() {
                return new ContentDefinition(this);
            }

            private static Builder from(ContentDefinition contentDefinition) {
                Builder builder = new Builder(contentDefinition.type, contentDefinition.publicationStatus);
                builder.id = contentDefinition.id;
                builder.extension.addAll(contentDefinition.extension);
                builder.modifierExtension.addAll(contentDefinition.modifierExtension);
                builder.subType = contentDefinition.subType;
                builder.publisher = contentDefinition.publisher;
                builder.publicationDate = contentDefinition.publicationDate;
                builder.copyright = contentDefinition.copyright;
                return builder;
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

        private Term(Builder builder) {
            super(builder);
            this.identifier = builder.identifier;
            this.issued = builder.issued;
            this.applies = builder.applies;
            this.topic = ValidationSupport.choiceElement(builder.topic, "topic", CodeableConcept.class, Reference.class);
            this.type = builder.type;
            this.subType = builder.subType;
            this.text = builder.text;
            this.securityLabel = builder.securityLabel;
            this.offer = ValidationSupport.requireNonNull(builder.offer, "offer");
            this.asset = builder.asset;
            this.action = builder.action;
            this.group = builder.group;
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
         *     A list containing immutable objects of type {@link SecurityLabel}.
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
         *     A list containing immutable objects of type {@link Asset}.
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
         *     A list containing immutable objects of type {@link Action}.
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
         *     A list containing immutable objects of type {@link Term}.
         */
        public List<Contract.Term> getGroup() {
            return group;
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
                    accept(topic, "topic", visitor, true);
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
        public Builder toBuilder() {
            return Builder.from(this);
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
             * Unique identifier for this particular Contract Provision.
             * </p>
             * 
             * @param identifier
             *     Contract Term Number
             * 
             * @return
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             * 
             * @param securityLabel
             *     Protection for the Term
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param securityLabel
             *     Protection for the Term
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder securityLabel(Collection<SecurityLabel> securityLabel) {
                this.securityLabel.addAll(securityLabel);
                return this;
            }

            /**
             * <p>
             * Contract Term Asset List.
             * </p>
             * 
             * @param asset
             *     Contract Term Asset List
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param asset
             *     Contract Term Asset List
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder asset(Collection<Asset> asset) {
                this.asset.addAll(asset);
                return this;
            }

            /**
             * <p>
             * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
             * taking place.
             * </p>
             * 
             * @param action
             *     Entity being ascribed responsibility
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param action
             *     Entity being ascribed responsibility
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder action(Collection<Action> action) {
                this.action.addAll(action);
                return this;
            }

            /**
             * <p>
             * Nested group of Contract Provisions.
             * </p>
             * 
             * @param group
             *     Nested Contract Term Group
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param group
             *     Nested Contract Term Group
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder group(Collection<Contract.Term> group) {
                this.group.addAll(group);
                return this;
            }

            @Override
            public Term build() {
                return new Term(this);
            }

            private static Builder from(Term term) {
                Builder builder = new Builder(term.offer);
                builder.id = term.id;
                builder.extension.addAll(term.extension);
                builder.modifierExtension.addAll(term.modifierExtension);
                builder.identifier = term.identifier;
                builder.issued = term.issued;
                builder.applies = term.applies;
                builder.topic = term.topic;
                builder.type = term.type;
                builder.subType = term.subType;
                builder.text = term.text;
                builder.securityLabel.addAll(term.securityLabel);
                builder.asset.addAll(term.asset);
                builder.action.addAll(term.action);
                builder.group.addAll(term.group);
                return builder;
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

            private SecurityLabel(Builder builder) {
                super(builder);
                this.number = builder.number;
                this.classification = ValidationSupport.requireNonNull(builder.classification, "classification");
                this.category = builder.category;
                this.control = builder.control;
            }

            /**
             * <p>
             * Number used to link this term or term element to the applicable Security Label.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link UnsignedInt}.
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
             *     A list containing immutable objects of type {@link Coding}.
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
             *     A list containing immutable objects of type {@link Coding}.
             */
            public List<Coding> getControl() {
                return control;
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
            public Builder toBuilder() {
                return Builder.from(this);
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
                 * Number used to link this term or term element to the applicable Security Label.
                 * </p>
                 * 
                 * @param number
                 *     Link to Security Labels
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param number
                 *     Link to Security Labels
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder number(Collection<UnsignedInt> number) {
                    this.number.addAll(number);
                    return this;
                }

                /**
                 * <p>
                 * Security label privacy tag that species the applicable privacy and security policies governing this term and/or term 
                 * elements.
                 * </p>
                 * 
                 * @param category
                 *     Applicable Policy
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param category
                 *     Applicable Policy
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder category(Collection<Coding> category) {
                    this.category.addAll(category);
                    return this;
                }

                /**
                 * <p>
                 * Security label privacy tag that species the manner in which term and/or term elements are to be protected.
                 * </p>
                 * 
                 * @param control
                 *     Handling Instructions
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param control
                 *     Handling Instructions
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder control(Collection<Coding> control) {
                    this.control.addAll(control);
                    return this;
                }

                @Override
                public SecurityLabel build() {
                    return new SecurityLabel(this);
                }

                private static Builder from(SecurityLabel securityLabel) {
                    Builder builder = new Builder(securityLabel.classification);
                    builder.id = securityLabel.id;
                    builder.extension.addAll(securityLabel.extension);
                    builder.modifierExtension.addAll(securityLabel.modifierExtension);
                    builder.number.addAll(securityLabel.number);
                    builder.category.addAll(securityLabel.category);
                    builder.control.addAll(securityLabel.control);
                    return builder;
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

            private Offer(Builder builder) {
                super(builder);
                this.identifier = builder.identifier;
                this.party = builder.party;
                this.topic = builder.topic;
                this.type = builder.type;
                this.decision = builder.decision;
                this.decisionMode = builder.decisionMode;
                this.answer = builder.answer;
                this.text = builder.text;
                this.linkId = builder.linkId;
                this.securityLabelNumber = builder.securityLabelNumber;
            }

            /**
             * <p>
             * Unique identifier for this particular Contract Provision.
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
             * Offer Recipient.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Party}.
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
             *     A list containing immutable objects of type {@link CodeableConcept}.
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
             *     A list containing immutable objects of type {@link Answer}.
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
             *     A list containing immutable objects of type {@link String}.
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
             *     A list containing immutable objects of type {@link UnsignedInt}.
             */
            public List<UnsignedInt> getSecurityLabelNumber() {
                return securityLabelNumber;
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
            public Builder toBuilder() {
                return Builder.from(this);
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
                 * Unique identifier for this particular Contract Provision.
                 * </p>
                 * 
                 * @param identifier
                 *     Offer business ID
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
                 * Unique identifier for this particular Contract Provision.
                 * </p>
                 * 
                 * @param identifier
                 *     Offer business ID
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
                 * Offer Recipient.
                 * </p>
                 * 
                 * @param party
                 *     Offer Recipient
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param party
                 *     Offer Recipient
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder party(Collection<Party> party) {
                    this.party.addAll(party);
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
                 *     A reference to this Builder instance.
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
                 *     A reference to this Builder instance.
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
                 *     A reference to this Builder instance.
                 */
                public Builder decision(CodeableConcept decision) {
                    this.decision = decision;
                    return this;
                }

                /**
                 * <p>
                 * How the decision about a Contract was conveyed.
                 * </p>
                 * 
                 * @param decisionMode
                 *     How decision is conveyed
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param decisionMode
                 *     How decision is conveyed
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder decisionMode(Collection<CodeableConcept> decisionMode) {
                    this.decisionMode.addAll(decisionMode);
                    return this;
                }

                /**
                 * <p>
                 * Response to offer text.
                 * </p>
                 * 
                 * @param answer
                 *     Response to offer text
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param answer
                 *     Response to offer text
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder answer(Collection<Answer> answer) {
                    this.answer.addAll(answer);
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
                 *     A reference to this Builder instance.
                 */
                public Builder text(String text) {
                    this.text = text;
                    return this;
                }

                /**
                 * <p>
                 * The id of the clause or question text of the offer in the referenced questionnaire/response.
                 * </p>
                 * 
                 * @param linkId
                 *     Pointer to text
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param linkId
                 *     Pointer to text
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder linkId(Collection<String> linkId) {
                    this.linkId.addAll(linkId);
                    return this;
                }

                /**
                 * <p>
                 * Security labels that protects the offer.
                 * </p>
                 * 
                 * @param securityLabelNumber
                 *     Offer restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param securityLabelNumber
                 *     Offer restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder securityLabelNumber(Collection<UnsignedInt> securityLabelNumber) {
                    this.securityLabelNumber.addAll(securityLabelNumber);
                    return this;
                }

                @Override
                public Offer build() {
                    return new Offer(this);
                }

                private static Builder from(Offer offer) {
                    Builder builder = new Builder();
                    builder.id = offer.id;
                    builder.extension.addAll(offer.extension);
                    builder.modifierExtension.addAll(offer.modifierExtension);
                    builder.identifier.addAll(offer.identifier);
                    builder.party.addAll(offer.party);
                    builder.topic = offer.topic;
                    builder.type = offer.type;
                    builder.decision = offer.decision;
                    builder.decisionMode.addAll(offer.decisionMode);
                    builder.answer.addAll(offer.answer);
                    builder.text = offer.text;
                    builder.linkId.addAll(offer.linkId);
                    builder.securityLabelNumber.addAll(offer.securityLabelNumber);
                    return builder;
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

                private Party(Builder builder) {
                    super(builder);
                    this.reference = ValidationSupport.requireNonEmpty(builder.reference, "reference");
                    this.role = ValidationSupport.requireNonNull(builder.role, "role");
                }

                /**
                 * <p>
                 * Participant in the offer.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link Reference}.
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
                public Builder toBuilder() {
                    return Builder.from(this);
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

                    @Override
                    public Party build() {
                        return new Party(this);
                    }

                    private static Builder from(Party party) {
                        Builder builder = new Builder(party.reference, party.role);
                        builder.id = party.id;
                        builder.extension.addAll(party.extension);
                        builder.modifierExtension.addAll(party.modifierExtension);
                        return builder;
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

                private Answer(Builder builder) {
                    super(builder);
                    this.value = ValidationSupport.requireChoiceElement(builder.value, "value", Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Uri.class, Attachment.class, Coding.class, Quantity.class, Reference.class);
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
                public void accept(java.lang.String elementName, Visitor visitor) {
                    if (visitor.preVisit(this)) {
                        visitor.visitStart(elementName, this);
                        if (visitor.visit(elementName, this)) {
                            // visit children
                            accept(id, "id", visitor);
                            accept(extension, "extension", visitor, Extension.class);
                            accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                            accept(value, "value", visitor, true);
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

                    @Override
                    public Answer build() {
                        return new Answer(this);
                    }

                    private static Builder from(Answer answer) {
                        Builder builder = new Builder(answer.value);
                        builder.id = answer.id;
                        builder.extension.addAll(answer.extension);
                        builder.modifierExtension.addAll(answer.modifierExtension);
                        return builder;
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

            private Asset(Builder builder) {
                super(builder);
                this.scope = builder.scope;
                this.type = builder.type;
                this.typeReference = builder.typeReference;
                this.subtype = builder.subtype;
                this.relationship = builder.relationship;
                this.context = builder.context;
                this.condition = builder.condition;
                this.periodType = builder.periodType;
                this.period = builder.period;
                this.usePeriod = builder.usePeriod;
                this.text = builder.text;
                this.linkId = builder.linkId;
                this.answer = builder.answer;
                this.securityLabelNumber = builder.securityLabelNumber;
                this.valuedItem = builder.valuedItem;
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
             *     A list containing immutable objects of type {@link CodeableConcept}.
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
             *     A list containing immutable objects of type {@link Reference}.
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
             *     A list containing immutable objects of type {@link CodeableConcept}.
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
             *     A list containing immutable objects of type {@link Context}.
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
             *     A list containing immutable objects of type {@link CodeableConcept}.
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
             *     A list containing immutable objects of type {@link Period}.
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
             *     A list containing immutable objects of type {@link Period}.
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
             *     A list containing immutable objects of type {@link String}.
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
             *     A list containing immutable objects of type {@link Answer}.
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
             *     A list containing immutable objects of type {@link UnsignedInt}.
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
             *     A list containing immutable objects of type {@link ValuedItem}.
             */
            public List<ValuedItem> getValuedItem() {
                return valuedItem;
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
            public Builder toBuilder() {
                return Builder.from(this);
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
                 * Differentiates the kind of the asset .
                 * </p>
                 * 
                 * @param scope
                 *     Range of asset
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder scope(CodeableConcept scope) {
                    this.scope = scope;
                    return this;
                }

                /**
                 * <p>
                 * Target entity type about which the term may be concerned.
                 * </p>
                 * 
                 * @param type
                 *     Asset category
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param type
                 *     Asset category
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder type(Collection<CodeableConcept> type) {
                    this.type.addAll(type);
                    return this;
                }

                /**
                 * <p>
                 * Associated entities.
                 * </p>
                 * 
                 * @param typeReference
                 *     Associated entities
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param typeReference
                 *     Associated entities
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder typeReference(Collection<Reference> typeReference) {
                    this.typeReference.addAll(typeReference);
                    return this;
                }

                /**
                 * <p>
                 * May be a subtype or part of an offered asset.
                 * </p>
                 * 
                 * @param subtype
                 *     Asset sub-category
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param subtype
                 *     Asset sub-category
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder subtype(Collection<CodeableConcept> subtype) {
                    this.subtype.addAll(subtype);
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
                 *     A reference to this Builder instance.
                 */
                public Builder relationship(Coding relationship) {
                    this.relationship = relationship;
                    return this;
                }

                /**
                 * <p>
                 * Circumstance of the asset.
                 * </p>
                 * 
                 * @param context
                 *     Circumstance of the asset
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param context
                 *     Circumstance of the asset
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder context(Collection<Context> context) {
                    this.context.addAll(context);
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
                 *     A reference to this Builder instance.
                 */
                public Builder condition(String condition) {
                    this.condition = condition;
                    return this;
                }

                /**
                 * <p>
                 * Type of Asset availability for use or ownership.
                 * </p>
                 * 
                 * @param periodType
                 *     Asset availability types
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param periodType
                 *     Asset availability types
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder periodType(Collection<CodeableConcept> periodType) {
                    this.periodType.addAll(periodType);
                    return this;
                }

                /**
                 * <p>
                 * Asset relevant contractual time period.
                 * </p>
                 * 
                 * @param period
                 *     Time period of the asset
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param period
                 *     Time period of the asset
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder period(Collection<Period> period) {
                    this.period.addAll(period);
                    return this;
                }

                /**
                 * <p>
                 * Time period of asset use.
                 * </p>
                 * 
                 * @param usePeriod
                 *     Time period
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param usePeriod
                 *     Time period
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder usePeriod(Collection<Period> usePeriod) {
                    this.usePeriod.addAll(usePeriod);
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
                 *     A reference to this Builder instance.
                 */
                public Builder text(String text) {
                    this.text = text;
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.
                 * </p>
                 * 
                 * @param linkId
                 *     Pointer to asset text
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param linkId
                 *     Pointer to asset text
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder linkId(Collection<String> linkId) {
                    this.linkId.addAll(linkId);
                    return this;
                }

                /**
                 * <p>
                 * Response to assets.
                 * </p>
                 * 
                 * @param answer
                 *     Response to assets
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param answer
                 *     Response to assets
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder answer(Collection<Contract.Term.Offer.Answer> answer) {
                    this.answer.addAll(answer);
                    return this;
                }

                /**
                 * <p>
                 * Security labels that protects the asset.
                 * </p>
                 * 
                 * @param securityLabelNumber
                 *     Asset restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param securityLabelNumber
                 *     Asset restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder securityLabelNumber(Collection<UnsignedInt> securityLabelNumber) {
                    this.securityLabelNumber.addAll(securityLabelNumber);
                    return this;
                }

                /**
                 * <p>
                 * Contract Valued Item List.
                 * </p>
                 * 
                 * @param valuedItem
                 *     Contract Valued Item List
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param valuedItem
                 *     Contract Valued Item List
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder valuedItem(Collection<ValuedItem> valuedItem) {
                    this.valuedItem.addAll(valuedItem);
                    return this;
                }

                @Override
                public Asset build() {
                    return new Asset(this);
                }

                private static Builder from(Asset asset) {
                    Builder builder = new Builder();
                    builder.id = asset.id;
                    builder.extension.addAll(asset.extension);
                    builder.modifierExtension.addAll(asset.modifierExtension);
                    builder.scope = asset.scope;
                    builder.type.addAll(asset.type);
                    builder.typeReference.addAll(asset.typeReference);
                    builder.subtype.addAll(asset.subtype);
                    builder.relationship = asset.relationship;
                    builder.context.addAll(asset.context);
                    builder.condition = asset.condition;
                    builder.periodType.addAll(asset.periodType);
                    builder.period.addAll(asset.period);
                    builder.usePeriod.addAll(asset.usePeriod);
                    builder.text = asset.text;
                    builder.linkId.addAll(asset.linkId);
                    builder.answer.addAll(asset.answer);
                    builder.securityLabelNumber.addAll(asset.securityLabelNumber);
                    builder.valuedItem.addAll(asset.valuedItem);
                    return builder;
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

                private Context(Builder builder) {
                    super(builder);
                    this.reference = builder.reference;
                    this.code = builder.code;
                    this.text = builder.text;
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
                 *     A list containing immutable objects of type {@link CodeableConcept}.
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
                public Builder toBuilder() {
                    return Builder.from(this);
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
                     * Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository), 
                     * location held, e.g., building, jurisdiction.
                     * </p>
                     * 
                     * @param reference
                     *     Creator,custodian or owner
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder reference(Reference reference) {
                        this.reference = reference;
                        return this;
                    }

                    /**
                     * <p>
                     * Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.
                     * </p>
                     * 
                     * @param code
                     *     Codeable asset context
                     * 
                     * @return
                     *     A reference to this Builder instance.
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
                     * 
                     * @param code
                     *     Codeable asset context
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder code(Collection<CodeableConcept> code) {
                        this.code.addAll(code);
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
                     *     A reference to this Builder instance.
                     */
                    public Builder text(String text) {
                        this.text = text;
                        return this;
                    }

                    @Override
                    public Context build() {
                        return new Context(this);
                    }

                    private static Builder from(Context context) {
                        Builder builder = new Builder();
                        builder.id = context.id;
                        builder.extension.addAll(context.extension);
                        builder.modifierExtension.addAll(context.modifierExtension);
                        builder.reference = context.reference;
                        builder.code.addAll(context.code);
                        builder.text = context.text;
                        return builder;
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

                private ValuedItem(Builder builder) {
                    super(builder);
                    this.entity = ValidationSupport.choiceElement(builder.entity, "entity", CodeableConcept.class, Reference.class);
                    this.identifier = builder.identifier;
                    this.effectiveTime = builder.effectiveTime;
                    this.quantity = builder.quantity;
                    this.unitPrice = builder.unitPrice;
                    this.factor = builder.factor;
                    this.points = builder.points;
                    this.net = builder.net;
                    this.payment = builder.payment;
                    this.paymentDate = builder.paymentDate;
                    this.responsible = builder.responsible;
                    this.recipient = builder.recipient;
                    this.linkId = builder.linkId;
                    this.securityLabelNumber = builder.securityLabelNumber;
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
                 *     A list containing immutable objects of type {@link String}.
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
                 *     A list containing immutable objects of type {@link UnsignedInt}.
                 */
                public List<UnsignedInt> getSecurityLabelNumber() {
                    return securityLabelNumber;
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
                            accept(entity, "entity", visitor, true);
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
                public Builder toBuilder() {
                    return Builder.from(this);
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
                     * Specific type of Contract Valued Item that may be priced.
                     * </p>
                     * 
                     * @param entity
                     *     Contract Valued Item Type
                     * 
                     * @return
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     * 
                     * @param linkId
                     *     Pointer to specific item
                     * 
                     * @return
                     *     A reference to this Builder instance.
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
                     * 
                     * @param linkId
                     *     Pointer to specific item
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder linkId(Collection<String> linkId) {
                        this.linkId.addAll(linkId);
                        return this;
                    }

                    /**
                     * <p>
                     * A set of security labels that define which terms are controlled by this condition.
                     * </p>
                     * 
                     * @param securityLabelNumber
                     *     Security Labels that define affected terms
                     * 
                     * @return
                     *     A reference to this Builder instance.
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
                     * 
                     * @param securityLabelNumber
                     *     Security Labels that define affected terms
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder securityLabelNumber(Collection<UnsignedInt> securityLabelNumber) {
                        this.securityLabelNumber.addAll(securityLabelNumber);
                        return this;
                    }

                    @Override
                    public ValuedItem build() {
                        return new ValuedItem(this);
                    }

                    private static Builder from(ValuedItem valuedItem) {
                        Builder builder = new Builder();
                        builder.id = valuedItem.id;
                        builder.extension.addAll(valuedItem.extension);
                        builder.modifierExtension.addAll(valuedItem.modifierExtension);
                        builder.entity = valuedItem.entity;
                        builder.identifier = valuedItem.identifier;
                        builder.effectiveTime = valuedItem.effectiveTime;
                        builder.quantity = valuedItem.quantity;
                        builder.unitPrice = valuedItem.unitPrice;
                        builder.factor = valuedItem.factor;
                        builder.points = valuedItem.points;
                        builder.net = valuedItem.net;
                        builder.payment = valuedItem.payment;
                        builder.paymentDate = valuedItem.paymentDate;
                        builder.responsible = valuedItem.responsible;
                        builder.recipient = valuedItem.recipient;
                        builder.linkId.addAll(valuedItem.linkId);
                        builder.securityLabelNumber.addAll(valuedItem.securityLabelNumber);
                        return builder;
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

            private Action(Builder builder) {
                super(builder);
                this.doNotPerform = builder.doNotPerform;
                this.type = ValidationSupport.requireNonNull(builder.type, "type");
                this.subject = builder.subject;
                this.intent = ValidationSupport.requireNonNull(builder.intent, "intent");
                this.linkId = builder.linkId;
                this.status = ValidationSupport.requireNonNull(builder.status, "status");
                this.context = builder.context;
                this.contextLinkId = builder.contextLinkId;
                this.occurrence = ValidationSupport.choiceElement(builder.occurrence, "occurrence", DateTime.class, Period.class, Timing.class);
                this.requester = builder.requester;
                this.requesterLinkId = builder.requesterLinkId;
                this.performerType = builder.performerType;
                this.performerRole = builder.performerRole;
                this.performer = builder.performer;
                this.performerLinkId = builder.performerLinkId;
                this.reasonCode = builder.reasonCode;
                this.reasonReference = builder.reasonReference;
                this.reason = builder.reason;
                this.reasonLinkId = builder.reasonLinkId;
                this.note = builder.note;
                this.securityLabelNumber = builder.securityLabelNumber;
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
             *     A list containing immutable objects of type {@link Subject}.
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
             *     A list containing immutable objects of type {@link String}.
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
             *     A list containing immutable objects of type {@link String}.
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
             *     A list containing immutable objects of type {@link Reference}.
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
             *     A list containing immutable objects of type {@link String}.
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
             *     A list containing immutable objects of type {@link CodeableConcept}.
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
             *     A list containing immutable objects of type {@link String}.
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
             *     A list containing immutable objects of type {@link CodeableConcept}.
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
             *     A list containing immutable objects of type {@link Reference}.
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
             *     A list containing immutable objects of type {@link String}.
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
             *     A list containing immutable objects of type {@link String}.
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
             *     A list containing immutable objects of type {@link Annotation}.
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
             *     A list containing immutable objects of type {@link UnsignedInt}.
             */
            public List<UnsignedInt> getSecurityLabelNumber() {
                return securityLabelNumber;
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
                        accept(occurrence, "occurrence", visitor, true);
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
            public Builder toBuilder() {
                return Builder.from(this);
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
                 * True if the term prohibits the action.
                 * </p>
                 * 
                 * @param doNotPerform
                 *     True if the term prohibits the action
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder doNotPerform(Boolean doNotPerform) {
                    this.doNotPerform = doNotPerform;
                    return this;
                }

                /**
                 * <p>
                 * Entity of the action.
                 * </p>
                 * 
                 * @param subject
                 *     Entity of the action
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param subject
                 *     Entity of the action
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder subject(Collection<Subject> subject) {
                    this.subject.addAll(subject);
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to this action in the referenced form or 
                 * QuestionnaireResponse.
                 * </p>
                 * 
                 * @param linkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param linkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder linkId(Collection<String> linkId) {
                    this.linkId.addAll(linkId);
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
                 *     A reference to this Builder instance.
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
                 * 
                 * @param contextLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param contextLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder contextLinkId(Collection<String> contextLinkId) {
                    this.contextLinkId.addAll(contextLinkId);
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
                 *     A reference to this Builder instance.
                 */
                public Builder occurrence(Element occurrence) {
                    this.occurrence = occurrence;
                    return this;
                }

                /**
                 * <p>
                 * Who or what initiated the action and has responsibility for its activation.
                 * </p>
                 * 
                 * @param requester
                 *     Who asked for action
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param requester
                 *     Who asked for action
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder requester(Collection<Reference> requester) {
                    this.requester.addAll(requester);
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
                 * QuestionnaireResponse.
                 * </p>
                 * 
                 * @param requesterLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param requesterLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder requesterLinkId(Collection<String> requesterLinkId) {
                    this.requesterLinkId.addAll(requesterLinkId);
                    return this;
                }

                /**
                 * <p>
                 * The type of individual that is desired or required to perform or not perform the action.
                 * </p>
                 * 
                 * @param performerType
                 *     Kind of service performer
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param performerType
                 *     Kind of service performer
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder performerType(Collection<CodeableConcept> performerType) {
                    this.performerType.addAll(performerType);
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
                 *     A reference to this Builder instance.
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
                 *     A reference to this Builder instance.
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
                 * 
                 * @param performerLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param performerLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder performerLinkId(Collection<String> performerLinkId) {
                    this.performerLinkId.addAll(performerLinkId);
                    return this;
                }

                /**
                 * <p>
                 * Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.
                 * </p>
                 * 
                 * @param reasonCode
                 *     Why is action (not) needed?
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param reasonCode
                 *     Why is action (not) needed?
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
                    this.reasonCode.addAll(reasonCode);
                    return this;
                }

                /**
                 * <p>
                 * Indicates another resource whose existence justifies permitting or not permitting this action.
                 * </p>
                 * 
                 * @param reasonReference
                 *     Why is action (not) needed?
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param reasonReference
                 *     Why is action (not) needed?
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder reasonReference(Collection<Reference> reasonReference) {
                    this.reasonReference.addAll(reasonReference);
                    return this;
                }

                /**
                 * <p>
                 * Describes why the action is to be performed or not performed in textual form.
                 * </p>
                 * 
                 * @param reason
                 *     Why action is to be performed
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param reason
                 *     Why action is to be performed
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder reason(Collection<String> reason) {
                    this.reason.addAll(reason);
                    return this;
                }

                /**
                 * <p>
                 * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
                 * referenced form or QuestionnaireResponse.
                 * </p>
                 * 
                 * @param reasonLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param reasonLinkId
                 *     Pointer to specific item
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder reasonLinkId(Collection<String> reasonLinkId) {
                    this.reasonLinkId.addAll(reasonLinkId);
                    return this;
                }

                /**
                 * <p>
                 * Comments made about the term action made by the requester, performer, subject or other participants.
                 * </p>
                 * 
                 * @param note
                 *     Comments about the action
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param note
                 *     Comments about the action
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder note(Collection<Annotation> note) {
                    this.note.addAll(note);
                    return this;
                }

                /**
                 * <p>
                 * Security labels that protects the action.
                 * </p>
                 * 
                 * @param securityLabelNumber
                 *     Action restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param securityLabelNumber
                 *     Action restriction numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder securityLabelNumber(Collection<UnsignedInt> securityLabelNumber) {
                    this.securityLabelNumber.addAll(securityLabelNumber);
                    return this;
                }

                @Override
                public Action build() {
                    return new Action(this);
                }

                private static Builder from(Action action) {
                    Builder builder = new Builder(action.type, action.intent, action.status);
                    builder.id = action.id;
                    builder.extension.addAll(action.extension);
                    builder.modifierExtension.addAll(action.modifierExtension);
                    builder.doNotPerform = action.doNotPerform;
                    builder.subject.addAll(action.subject);
                    builder.linkId.addAll(action.linkId);
                    builder.context = action.context;
                    builder.contextLinkId.addAll(action.contextLinkId);
                    builder.occurrence = action.occurrence;
                    builder.requester.addAll(action.requester);
                    builder.requesterLinkId.addAll(action.requesterLinkId);
                    builder.performerType.addAll(action.performerType);
                    builder.performerRole = action.performerRole;
                    builder.performer = action.performer;
                    builder.performerLinkId.addAll(action.performerLinkId);
                    builder.reasonCode.addAll(action.reasonCode);
                    builder.reasonReference.addAll(action.reasonReference);
                    builder.reason.addAll(action.reason);
                    builder.reasonLinkId.addAll(action.reasonLinkId);
                    builder.note.addAll(action.note);
                    builder.securityLabelNumber.addAll(action.securityLabelNumber);
                    return builder;
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

                private Subject(Builder builder) {
                    super(builder);
                    this.reference = ValidationSupport.requireNonEmpty(builder.reference, "reference");
                    this.role = builder.role;
                }

                /**
                 * <p>
                 * The entity the action is performed or not performed on or for.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link Reference}.
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
                public Builder toBuilder() {
                    return Builder.from(this);
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
                     * Role type of agent assigned roles in this Contract.
                     * </p>
                     * 
                     * @param role
                     *     Role type of the agent
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder role(CodeableConcept role) {
                        this.role = role;
                        return this;
                    }

                    @Override
                    public Subject build() {
                        return new Subject(this);
                    }

                    private static Builder from(Subject subject) {
                        Builder builder = new Builder(subject.reference);
                        builder.id = subject.id;
                        builder.extension.addAll(subject.extension);
                        builder.modifierExtension.addAll(subject.modifierExtension);
                        builder.role = subject.role;
                        return builder;
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

        private Signer(Builder builder) {
            super(builder);
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
            this.party = ValidationSupport.requireNonNull(builder.party, "party");
            this.signature = ValidationSupport.requireNonEmpty(builder.signature, "signature");
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
         *     A list containing immutable objects of type {@link Signature}.
         */
        public List<Signature> getSignature() {
            return signature;
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
        public Builder toBuilder() {
            return Builder.from(this);
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

            @Override
            public Signer build() {
                return new Signer(this);
            }

            private static Builder from(Signer signer) {
                Builder builder = new Builder(signer.type, signer.party, signer.signature);
                builder.id = signer.id;
                builder.extension.addAll(signer.extension);
                builder.modifierExtension.addAll(signer.modifierExtension);
                return builder;
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

        private Friendly(Builder builder) {
            super(builder);
            this.content = ValidationSupport.requireChoiceElement(builder.content, "content", Attachment.class, Reference.class);
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(content, "content", visitor, true);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
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

            @Override
            public Friendly build() {
                return new Friendly(this);
            }

            private static Builder from(Friendly friendly) {
                Builder builder = new Builder(friendly.content);
                builder.id = friendly.id;
                builder.extension.addAll(friendly.extension);
                builder.modifierExtension.addAll(friendly.modifierExtension);
                return builder;
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

        private Legal(Builder builder) {
            super(builder);
            this.content = ValidationSupport.requireChoiceElement(builder.content, "content", Attachment.class, Reference.class);
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(content, "content", visitor, true);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
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

            @Override
            public Legal build() {
                return new Legal(this);
            }

            private static Builder from(Legal legal) {
                Builder builder = new Builder(legal.content);
                builder.id = legal.id;
                builder.extension.addAll(legal.extension);
                builder.modifierExtension.addAll(legal.modifierExtension);
                return builder;
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

        private Rule(Builder builder) {
            super(builder);
            this.content = ValidationSupport.requireChoiceElement(builder.content, "content", Attachment.class, Reference.class);
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(content, "content", visitor, true);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
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

            @Override
            public Rule build() {
                return new Rule(this);
            }

            private static Builder from(Rule rule) {
                Builder builder = new Builder(rule.content);
                builder.id = rule.id;
                builder.extension.addAll(rule.extension);
                builder.modifierExtension.addAll(rule.modifierExtension);
                return builder;
            }
        }
    }
}
