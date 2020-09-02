/*
 * (C) Copyright IBM Corp. 2019, 2020
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
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Signature;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ContractPublicationStatus;
import com.ibm.fhir.model.type.code.ContractStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
 */
@Constraint(
    id = "contract-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/contract-legalstate",
    expression = "legalState.exists() implies (legalState.memberOf('http://hl7.org/fhir/ValueSet/contract-legalstate', 'extensible'))"
)
@Constraint(
    id = "contract-1",
    level = "Warning",
    location = "term.offer.decision",
    description = "SHALL, if possible, contain a code from value set http://terminology.hl7.org/ValueSet/v3-ActConsentDirective",
    expression = "$this.memberOf('http://terminology.hl7.org/ValueSet/v3-ActConsentDirective', 'extensible')"
)
@Constraint(
    id = "contract-2",
    level = "Warning",
    location = "term.asset.relationship",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/consent-content-class",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/consent-content-class', 'extensible')"
)
@Constraint(
    id = "contract-3",
    level = "Warning",
    location = "signer.type",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/contract-signer-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/contract-signer-type', 'preferred')"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Contract extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    private final Uri url;
    @Summary
    private final String version;
    @Summary
    @Binding(
        bindingName = "ContractStatus",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "A code specifying the state of the resource instance.",
        valueSet = "http://hl7.org/fhir/ValueSet/contract-status|4.0.1"
    )
    private final ContractStatus status;
    @Binding(
        bindingName = "ContractLegalState",
        strength = BindingStrength.ValueSet.EXTENSIBLE,
        description = "Detailed codes for the legal state of a contract.",
        valueSet = "http://hl7.org/fhir/ValueSet/contract-legalstate"
    )
    private final CodeableConcept legalState;
    @ReferenceTarget({ "Contract" })
    private final Reference instantiatesCanonical;
    private final Uri instantiatesUri;
    @Binding(
        bindingName = "ContractContentDerivative",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "This is an example set of Content Derivative type codes, which represent the minimal content derived from the basal information source.",
        valueSet = "http://hl7.org/fhir/ValueSet/contract-content-derivative"
    )
    private final CodeableConcept contentDerivative;
    @Summary
    private final DateTime issued;
    @Summary
    private final Period applies;
    @Binding(
        bindingName = "ContractExpiration",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "Codes for the Cessation of Contracts.",
        valueSet = "http://hl7.org/fhir/ValueSet/contract-expiration-type"
    )
    private final CodeableConcept expirationType;
    @Summary
    private final List<Reference> subject;
    private final List<Reference> authority;
    private final List<Reference> domain;
    private final List<Reference> site;
    @Summary
    private final String name;
    @Summary
    private final String title;
    private final String subtitle;
    private final List<String> alias;
    @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "Organization" })
    private final Reference author;
    @Binding(
        bindingName = "ContractScope",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "Codes for the range of legal concerns.",
        valueSet = "http://hl7.org/fhir/ValueSet/contract-scope"
    )
    private final CodeableConcept scope;
    @Choice({ CodeableConcept.class, Reference.class })
    private final Element topic;
    @Summary
    @Binding(
        bindingName = "ContractType",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "List of overall contract codes.",
        valueSet = "http://hl7.org/fhir/ValueSet/contract-type"
    )
    private final CodeableConcept type;
    @Summary
    @Binding(
        bindingName = "ContractSubtype",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "Detailed codes within the above.",
        valueSet = "http://hl7.org/fhir/ValueSet/contract-subtype"
    )
    private final List<CodeableConcept> subType;
    private final ContentDefinition contentDefinition;
    private final List<Term> term;
    private final List<Reference> supportingInfo;
    private final List<Reference> relevantHistory;
    private final List<Signer> signer;
    private final List<Friendly> friendly;
    private final List<Legal> legal;
    private final List<Rule> rule;
    @Choice({ Attachment.class, Reference.class })
    private final Element legallyBinding;

    private volatile int hashCode;

    private Contract(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
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
        subject = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.subject, "subject"));
        authority = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.authority, "authority"));
        domain = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.domain, "domain"));
        site = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.site, "site"));
        name = builder.name;
        title = builder.title;
        subtitle = builder.subtitle;
        alias = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.alias, "alias"));
        author = builder.author;
        scope = builder.scope;
        topic = ValidationSupport.choiceElement(builder.topic, "topic", CodeableConcept.class, Reference.class);
        type = builder.type;
        subType = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.subType, "subType"));
        contentDefinition = builder.contentDefinition;
        term = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.term, "term"));
        supportingInfo = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.supportingInfo, "supportingInfo"));
        relevantHistory = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.relevantHistory, "relevantHistory"));
        signer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.signer, "signer"));
        friendly = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.friendly, "friendly"));
        legal = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.legal, "legal"));
        rule = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.rule, "rule"));
        legallyBinding = ValidationSupport.choiceElement(builder.legallyBinding, "legallyBinding", Attachment.class, Reference.class);
        ValidationSupport.checkReferenceType(instantiatesCanonical, "instantiatesCanonical", "Contract");
        ValidationSupport.checkReferenceType(author, "author", "Patient", "Practitioner", "PractitionerRole", "Organization");
        ValidationSupport.requireChildren(this);
    }

    /**
     * Unique identifier for this Contract or a derivative that references a Source Contract.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Canonical identifier for this contract, represented as a URI (globally unique).
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * An edition identifier used for business purposes to label business significant variants.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * The status of the resource instance.
     * 
     * @return
     *     An immutable object of type {@link ContractStatus} that may be null.
     */
    public ContractStatus getStatus() {
        return status;
    }

    /**
     * Legal states of the formation of a legal instrument, which is a formally executed written document that can be 
     * formally attributed to its author, records and formally expresses a legally enforceable act, process, or contractual 
     * duty, obligation, or right, and therefore evidences that act, process, or agreement.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getLegalState() {
        return legalState;
    }

    /**
     * The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * The minimal content derived from the basal information source at a specific stage in its lifecycle.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getContentDerivative() {
        return contentDerivative;
    }

    /**
     * When this Contract was issued.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getIssued() {
        return issued;
    }

    /**
     * Relevant time or time-period when this Contract is applicable.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getApplies() {
        return applies;
    }

    /**
     * Event resulting in discontinuation or termination of this Contract instance by one or more parties to the contract.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getExpirationType() {
        return expirationType;
    }

    /**
     * The target entity impacted by or of interest to parties to the agreement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSubject() {
        return subject;
    }

    /**
     * A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the 
     * purpose of achieving some form of collective action such as the promulgation, administration and enforcement of 
     * contracts and policies.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAuthority() {
        return authority;
    }

    /**
     * Recognized governance framework or system operating with a circumscribed scope in accordance with specified 
     * principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals 
     * relative to resources.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getDomain() {
        return domain;
    }

    /**
     * Sites in which the contract is complied with, exercised, or in force.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSite() {
        return site;
    }

    /**
     * A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides 
     * additional information about its content. This name should be usable as an identifier for the module by machine 
     * processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t 
     * giving additional information about its content.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal 
     * state.t giving additional information about its content.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.
     * g., a domain specific contract number related to legislation.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getAlias() {
        return alias;
    }

    /**
     * The individual or organization that authored the Contract definition, derivative, or instance in any legal state.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * A selector of legal concerns for this Contract definition, derivative, or instance in any legal state.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getScope() {
        return scope;
    }

    /**
     * Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getTopic() {
        return topic;
    }

    /**
     * A high-level category for the legal instrument, whether constructed as a Contract definition, derivative, or instance 
     * in any legal state. Provides additional information about its content within the context of the Contract's scope to 
     * distinguish the kinds of systems that would be interested in the contract.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within 
     * the context of the Contract's scope.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getSubType() {
        return subType;
    }

    /**
     * Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be 
     * associated with and transformable into a Contract.
     * 
     * @return
     *     An immutable object of type {@link ContentDefinition} that may be null.
     */
    public ContentDefinition getContentDefinition() {
        return contentDefinition;
    }

    /**
     * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Term} that may be empty.
     */
    public List<Term> getTerm() {
        return term;
    }

    /**
     * Information that may be needed by/relevant to the performer in their execution of this term action.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key 
     * state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract. 
     * The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.
     * html#Provenance.entity.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getRelevantHistory() {
        return relevantHistory;
    }

    /**
     * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are 
     * any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the 
     * contract such as a notary or witness.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Signer} that may be empty.
     */
    public List<Signer> getSigner() {
        return signer;
    }

    /**
     * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the 
     * representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a 
     * layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the 
     * Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Friendly} that may be empty.
     */
    public List<Friendly> getFriendly() {
        return friendly;
    }

    /**
     * List of Legal expressions or representations of this Contract.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Legal} that may be empty.
     */
    public List<Legal> getLegal() {
        return legal;
    }

    /**
     * List of Computable Policy Rule Language Representations of this Contract.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Rule} that may be empty.
     */
    public List<Rule> getRule() {
        return rule;
    }

    /**
     * Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is 
     * considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getLegallyBinding() {
        return legallyBinding;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (url != null) || 
            (version != null) || 
            (status != null) || 
            (legalState != null) || 
            (instantiatesCanonical != null) || 
            (instantiatesUri != null) || 
            (contentDerivative != null) || 
            (issued != null) || 
            (applies != null) || 
            (expirationType != null) || 
            !subject.isEmpty() || 
            !authority.isEmpty() || 
            !domain.isEmpty() || 
            !site.isEmpty() || 
            (name != null) || 
            (title != null) || 
            (subtitle != null) || 
            !alias.isEmpty() || 
            (author != null) || 
            (scope != null) || 
            (topic != null) || 
            (type != null) || 
            !subType.isEmpty() || 
            (contentDefinition != null) || 
            !term.isEmpty() || 
            !supportingInfo.isEmpty() || 
            !relevantHistory.isEmpty() || 
            !signer.isEmpty() || 
            !friendly.isEmpty() || 
            !legal.isEmpty() || 
            !rule.isEmpty() || 
            (legallyBinding != null);
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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Unique identifier for this Contract or a derivative that references a Source Contract.
         * 
         * <p>Adds new element(s) to the existing list
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
         * Unique identifier for this Contract or a derivative that references a Source Contract.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Canonical identifier for this contract, represented as a URI (globally unique).
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
         * An edition identifier used for business purposes to label business significant variants.
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
         * The status of the resource instance.
         * 
         * @param status
         *     amended | appended | cancelled | disputed | entered-in-error | executable | executed | negotiable | offered | policy | 
         *     rejected | renewed | revoked | resolved | terminated
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ContractStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Legal states of the formation of a legal instrument, which is a formally executed written document that can be 
         * formally attributed to its author, records and formally expresses a legally enforceable act, process, or contractual 
         * duty, obligation, or right, and therefore evidences that act, process, or agreement.
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
         * The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Contract}</li>
         * </ul>
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
         * The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.
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
         * The minimal content derived from the basal information source at a specific stage in its lifecycle.
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
         * When this Contract was issued.
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
         * Relevant time or time-period when this Contract is applicable.
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
         * Event resulting in discontinuation or termination of this Contract instance by one or more parties to the contract.
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
         * The target entity impacted by or of interest to parties to the agreement.
         * 
         * <p>Adds new element(s) to the existing list
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
         * The target entity impacted by or of interest to parties to the agreement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the 
         * purpose of achieving some form of collective action such as the promulgation, administration and enforcement of 
         * contracts and policies.
         * 
         * <p>Adds new element(s) to the existing list
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
         * A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the 
         * purpose of achieving some form of collective action such as the promulgation, administration and enforcement of 
         * contracts and policies.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Recognized governance framework or system operating with a circumscribed scope in accordance with specified 
         * principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals 
         * relative to resources.
         * 
         * <p>Adds new element(s) to the existing list
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
         * Recognized governance framework or system operating with a circumscribed scope in accordance with specified 
         * principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals 
         * relative to resources.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Sites in which the contract is complied with, exercised, or in force.
         * 
         * <p>Adds new element(s) to the existing list
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
         * Sites in which the contract is complied with, exercised, or in force.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides 
         * additional information about its content. This name should be usable as an identifier for the module by machine 
         * processing applications such as code generation.
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
         * A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t 
         * giving additional information about its content.
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
         * An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal 
         * state.t giving additional information about its content.
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
         * Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.
         * g., a domain specific contract number related to legislation.
         * 
         * <p>Adds new element(s) to the existing list
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
         * Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.
         * g., a domain specific contract number related to legislation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * The individual or organization that authored the Contract definition, derivative, or instance in any legal state.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
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
         * A selector of legal concerns for this Contract definition, derivative, or instance in any legal state.
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
         * Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Reference}</li>
         * </ul>
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
         * A high-level category for the legal instrument, whether constructed as a Contract definition, derivative, or instance 
         * in any legal state. Provides additional information about its content within the context of the Contract's scope to 
         * distinguish the kinds of systems that would be interested in the contract.
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
         * Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within 
         * the context of the Contract's scope.
         * 
         * <p>Adds new element(s) to the existing list
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
         * Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within 
         * the context of the Contract's scope.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be 
         * associated with and transformable into a Contract.
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
         * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
         * 
         * <p>Adds new element(s) to the existing list
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
         * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Information that may be needed by/relevant to the performer in their execution of this term action.
         * 
         * <p>Adds new element(s) to the existing list
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
         * Information that may be needed by/relevant to the performer in their execution of this term action.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key 
         * state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract. 
         * The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.
         * html#Provenance.entity.
         * 
         * <p>Adds new element(s) to the existing list
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
         * Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key 
         * state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract. 
         * The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.
         * html#Provenance.entity.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are 
         * any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the 
         * contract such as a notary or witness.
         * 
         * <p>Adds new element(s) to the existing list
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
         * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are 
         * any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the 
         * contract such as a notary or witness.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the 
         * representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a 
         * layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the 
         * Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
         * 
         * <p>Adds new element(s) to the existing list
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
         * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the 
         * representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a 
         * layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the 
         * Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * List of Legal expressions or representations of this Contract.
         * 
         * <p>Adds new element(s) to the existing list
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
         * List of Legal expressions or representations of this Contract.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * List of Computable Policy Rule Language Representations of this Contract.
         * 
         * <p>Adds new element(s) to the existing list
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
         * List of Computable Policy Rule Language Representations of this Contract.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is 
         * considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Attachment}</li>
         * <li>{@link Reference}</li>
         * </ul>
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

        /**
         * Build the {@link Contract}
         * 
         * @return
         *     An immutable object of type {@link Contract}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Contract per the base specification
         */
        @Override
        public Contract build() {
            return new Contract(this);
        }

        protected Builder from(Contract contract) {
            super.from(contract);
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
     * Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be 
     * associated with and transformable into a Contract.
     */
    public static class ContentDefinition extends BackboneElement {
        @Binding(
            bindingName = "ContractDefinitionType",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "Detailed codes for the definition of contracts.",
            valueSet = "http://hl7.org/fhir/ValueSet/contract-definition-type"
        )
        @Required
        private final CodeableConcept type;
        @Binding(
            bindingName = "ContractDefinitionSubtype",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "Detailed codes for the additional definition of contracts.",
            valueSet = "http://hl7.org/fhir/ValueSet/contract-definition-subtype"
        )
        private final CodeableConcept subType;
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
        private final Reference publisher;
        private final DateTime publicationDate;
        @Binding(
            bindingName = "ContractPublicationStatus",
            strength = BindingStrength.ValueSet.REQUIRED,
            description = "Status of the publication of contract content.",
            valueSet = "http://hl7.org/fhir/ValueSet/contract-publicationstatus|4.0.1"
        )
        @Required
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
            ValidationSupport.checkReferenceType(publisher, "publisher", "Practitioner", "PractitionerRole", "Organization");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Precusory content structure and use, i.e., a boilerplate, template, application for a contract such as an insurance 
         * policy or benefits under a program, e.g., workers compensation.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Detailed Precusory content type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getSubType() {
            return subType;
        }

        /**
         * The individual or organization that published the Contract precursor content.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getPublisher() {
            return publisher;
        }

        /**
         * The date (and optionally time) when the contract was published. The date must change when the business version changes 
         * and it must change if the status code changes. In addition, it should change when the substantive content of the 
         * contract changes.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getPublicationDate() {
            return publicationDate;
        }

        /**
         * amended | appended | cancelled | disputed | entered-in-error | executable | executed | negotiable | offered | policy | 
         * rejected | renewed | revoked | resolved | terminated.
         * 
         * @return
         *     An immutable object of type {@link ContractPublicationStatus} that is non-null.
         */
        public ContractPublicationStatus getPublicationStatus() {
            return publicationStatus;
        }

        /**
         * A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on 
         * the use and publishing of the Contract precursor content.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getCopyright() {
            return copyright;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (subType != null) || 
                (publisher != null) || 
                (publicationDate != null) || 
                (publicationStatus != null) || 
                (copyright != null);
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
                    accept(subType, "subType", visitor);
                    accept(publisher, "publisher", visitor);
                    accept(publicationDate, "publicationDate", visitor);
                    accept(publicationStatus, "publicationStatus", visitor);
                    accept(copyright, "copyright", visitor);
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private CodeableConcept subType;
            private Reference publisher;
            private DateTime publicationDate;
            private ContractPublicationStatus publicationStatus;
            private Markdown copyright;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * Precusory content structure and use, i.e., a boilerplate, template, application for a contract such as an insurance 
             * policy or benefits under a program, e.g., workers compensation.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Content structure and use
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Detailed Precusory content type.
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
             * The individual or organization that published the Contract precursor content.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * </ul>
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
             * The date (and optionally time) when the contract was published. The date must change when the business version changes 
             * and it must change if the status code changes. In addition, it should change when the substantive content of the 
             * contract changes.
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
             * amended | appended | cancelled | disputed | entered-in-error | executable | executed | negotiable | offered | policy | 
             * rejected | renewed | revoked | resolved | terminated.
             * 
             * <p>This element is required.
             * 
             * @param publicationStatus
             *     amended | appended | cancelled | disputed | entered-in-error | executable | executed | negotiable | offered | policy | 
             *     rejected | renewed | revoked | resolved | terminated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder publicationStatus(ContractPublicationStatus publicationStatus) {
                this.publicationStatus = publicationStatus;
                return this;
            }

            /**
             * A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on 
             * the use and publishing of the Contract precursor content.
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

            /**
             * Build the {@link ContentDefinition}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>publicationStatus</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link ContentDefinition}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ContentDefinition per the base specification
             */
            @Override
            public ContentDefinition build() {
                return new ContentDefinition(this);
            }

            protected Builder from(ContentDefinition contentDefinition) {
                super.from(contentDefinition);
                type = contentDefinition.type;
                subType = contentDefinition.subType;
                publisher = contentDefinition.publisher;
                publicationDate = contentDefinition.publicationDate;
                publicationStatus = contentDefinition.publicationStatus;
                copyright = contentDefinition.copyright;
                return this;
            }
        }
    }

    /**
     * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
     */
    public static class Term extends BackboneElement {
        @Summary
        private final Identifier identifier;
        @Summary
        private final DateTime issued;
        @Summary
        private final Period applies;
        @Choice({ CodeableConcept.class, Reference.class })
        private final Element topic;
        @Binding(
            bindingName = "ContractTermType",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "Detailed codes for the types of contract provisions.",
            valueSet = "http://hl7.org/fhir/ValueSet/contract-term-type"
        )
        private final CodeableConcept type;
        @Binding(
            bindingName = "ContractTermSubType",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "Detailed codes for the subtypes of contract provisions.",
            valueSet = "http://hl7.org/fhir/ValueSet/contract-term-subtype"
        )
        private final CodeableConcept subType;
        @Summary
        private final String text;
        private final List<SecurityLabel> securityLabel;
        @Required
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
            securityLabel = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.securityLabel, "securityLabel"));
            offer = ValidationSupport.requireNonNull(builder.offer, "offer");
            asset = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.asset, "asset"));
            action = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.action, "action"));
            group = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.group, "group"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Unique identifier for this particular Contract Provision.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * When this Contract Provision was issued.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getIssued() {
            return issued;
        }

        /**
         * Relevant time or time-period when this Contract Provision is applicable.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getApplies() {
            return applies;
        }

        /**
         * The entity that the term applies to.
         * 
         * @return
         *     An immutable object of type {@link Element} that may be null.
         */
        public Element getTopic() {
            return topic;
        }

        /**
         * A legal clause or condition contained within a contract that requires one or both parties to perform a particular 
         * requirement by some specified time or prevents one or both parties from performing a particular requirement by some 
         * specified time.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * A specialized legal clause or condition based on overarching contract type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getSubType() {
            return subType;
        }

        /**
         * Statement of a provision in a policy or a contract.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getText() {
            return text;
        }

        /**
         * Security labels that protect the handling of information about the term and its elements, which may be specifically 
         * identified..
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link SecurityLabel} that may be empty.
         */
        public List<SecurityLabel> getSecurityLabel() {
            return securityLabel;
        }

        /**
         * The matter of concern in the context of this provision of the agrement.
         * 
         * @return
         *     An immutable object of type {@link Offer} that is non-null.
         */
        public Offer getOffer() {
            return offer;
        }

        /**
         * Contract Term Asset List.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Asset} that may be empty.
         */
        public List<Asset> getAsset() {
            return asset;
        }

        /**
         * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
         * taking place.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action} that may be empty.
         */
        public List<Action> getAction() {
            return action;
        }

        /**
         * Nested group of Contract Provisions.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Term} that may be empty.
         */
        public List<Contract.Term> getGroup() {
            return group;
        }

        @Override
        public boolean hasChildren() {
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
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private Identifier identifier;
            private DateTime issued;
            private Period applies;
            private Element topic;
            private CodeableConcept type;
            private CodeableConcept subType;
            private String text;
            private List<SecurityLabel> securityLabel = new ArrayList<>();
            private Offer offer;
            private List<Asset> asset = new ArrayList<>();
            private List<Action> action = new ArrayList<>();
            private List<Contract.Term> group = new ArrayList<>();

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * Unique identifier for this particular Contract Provision.
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
             * When this Contract Provision was issued.
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
             * Relevant time or time-period when this Contract Provision is applicable.
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
             * The entity that the term applies to.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Reference}</li>
             * </ul>
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
             * A legal clause or condition contained within a contract that requires one or both parties to perform a particular 
             * requirement by some specified time or prevents one or both parties from performing a particular requirement by some 
             * specified time.
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
             * A specialized legal clause or condition based on overarching contract type.
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
             * Statement of a provision in a policy or a contract.
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
             * Security labels that protect the handling of information about the term and its elements, which may be specifically 
             * identified..
             * 
             * <p>Adds new element(s) to the existing list
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
             * Security labels that protect the handling of information about the term and its elements, which may be specifically 
             * identified..
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * The matter of concern in the context of this provision of the agrement.
             * 
             * <p>This element is required.
             * 
             * @param offer
             *     Context of the Contract term
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder offer(Offer offer) {
                this.offer = offer;
                return this;
            }

            /**
             * Contract Term Asset List.
             * 
             * <p>Adds new element(s) to the existing list
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
             * Contract Term Asset List.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
             * taking place.
             * 
             * <p>Adds new element(s) to the existing list
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
             * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
             * taking place.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * Nested group of Contract Provisions.
             * 
             * <p>Adds new element(s) to the existing list
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
             * Nested group of Contract Provisions.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
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

            /**
             * Build the {@link Term}
             * 
             * <p>Required elements:
             * <ul>
             * <li>offer</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Term}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Term per the base specification
             */
            @Override
            public Term build() {
                return new Term(this);
            }

            protected Builder from(Term term) {
                super.from(term);
                identifier = term.identifier;
                issued = term.issued;
                applies = term.applies;
                topic = term.topic;
                type = term.type;
                subType = term.subType;
                text = term.text;
                securityLabel.addAll(term.securityLabel);
                offer = term.offer;
                asset.addAll(term.asset);
                action.addAll(term.action);
                group.addAll(term.group);
                return this;
            }
        }

        /**
         * Security labels that protect the handling of information about the term and its elements, which may be specifically 
         * identified..
         */
        public static class SecurityLabel extends BackboneElement {
            private final List<UnsignedInt> number;
            @Binding(
                bindingName = "ContractSecurityClassification",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Codes for confidentiality protection.",
                valueSet = "http://hl7.org/fhir/ValueSet/contract-security-classification"
            )
            @Required
            private final Coding classification;
            @Binding(
                bindingName = "ContractSecurityCategory",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Codes for policy category.",
                valueSet = "http://hl7.org/fhir/ValueSet/contract-security-category"
            )
            private final List<Coding> category;
            @Binding(
                bindingName = "ContractSecurityControl",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Codes for handling instructions.",
                valueSet = "http://hl7.org/fhir/ValueSet/contract-security-control"
            )
            private final List<Coding> control;

            private volatile int hashCode;

            private SecurityLabel(Builder builder) {
                super(builder);
                number = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.number, "number"));
                classification = ValidationSupport.requireNonNull(builder.classification, "classification");
                category = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.category, "category"));
                control = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.control, "control"));
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * Number used to link this term or term element to the applicable Security Label.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link UnsignedInt} that may be empty.
             */
            public List<UnsignedInt> getNumber() {
                return number;
            }

            /**
             * Security label privacy tag that species the level of confidentiality protection required for this term and/or term 
             * elements.
             * 
             * @return
             *     An immutable object of type {@link Coding} that is non-null.
             */
            public Coding getClassification() {
                return classification;
            }

            /**
             * Security label privacy tag that species the applicable privacy and security policies governing this term and/or term 
             * elements.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
             */
            public List<Coding> getCategory() {
                return category;
            }

            /**
             * Security label privacy tag that species the manner in which term and/or term elements are to be protected.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
             */
            public List<Coding> getControl() {
                return control;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    !number.isEmpty() || 
                    (classification != null) || 
                    !category.isEmpty() || 
                    !control.isEmpty();
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
                        accept(number, "number", visitor, UnsignedInt.class);
                        accept(classification, "classification", visitor);
                        accept(category, "category", visitor, Coding.class);
                        accept(control, "control", visitor, Coding.class);
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
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private List<UnsignedInt> number = new ArrayList<>();
                private Coding classification;
                private List<Coding> category = new ArrayList<>();
                private List<Coding> control = new ArrayList<>();

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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Number used to link this term or term element to the applicable Security Label.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Number used to link this term or term element to the applicable Security Label.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Security label privacy tag that species the level of confidentiality protection required for this term and/or term 
                 * elements.
                 * 
                 * <p>This element is required.
                 * 
                 * @param classification
                 *     Confidentiality Protection
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder classification(Coding classification) {
                    this.classification = classification;
                    return this;
                }

                /**
                 * Security label privacy tag that species the applicable privacy and security policies governing this term and/or term 
                 * elements.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Security label privacy tag that species the applicable privacy and security policies governing this term and/or term 
                 * elements.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Security label privacy tag that species the manner in which term and/or term elements are to be protected.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Security label privacy tag that species the manner in which term and/or term elements are to be protected.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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

                /**
                 * Build the {@link SecurityLabel}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>classification</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link SecurityLabel}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid SecurityLabel per the base specification
                 */
                @Override
                public SecurityLabel build() {
                    return new SecurityLabel(this);
                }

                protected Builder from(SecurityLabel securityLabel) {
                    super.from(securityLabel);
                    number.addAll(securityLabel.number);
                    classification = securityLabel.classification;
                    category.addAll(securityLabel.category);
                    control.addAll(securityLabel.control);
                    return this;
                }
            }
        }

        /**
         * The matter of concern in the context of this provision of the agrement.
         */
        public static class Offer extends BackboneElement {
            private final List<Identifier> identifier;
            private final List<Party> party;
            @Summary
            private final Reference topic;
            @Binding(
                bindingName = "ContractTermType",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Detailed codes for the types of contract provisions.",
                valueSet = "http://hl7.org/fhir/ValueSet/contract-term-type"
            )
            private final CodeableConcept type;
            @Binding(
                bindingName = "ContractDecisionType",
                strength = BindingStrength.ValueSet.EXTENSIBLE,
                description = "The type of decision made by a grantor with respect to an offer made by a grantee.",
                valueSet = "http://terminology.hl7.org/ValueSet/v3-ActConsentDirective"
            )
            private final CodeableConcept decision;
            @Binding(
                bindingName = "ContractDecisionMode",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Codes for conveying a decision.",
                valueSet = "http://hl7.org/fhir/ValueSet/contract-decision-mode"
            )
            private final List<CodeableConcept> decisionMode;
            private final List<Answer> answer;
            private final String text;
            private final List<String> linkId;
            private final List<UnsignedInt> securityLabelNumber;

            private volatile int hashCode;

            private Offer(Builder builder) {
                super(builder);
                identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
                party = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.party, "party"));
                topic = builder.topic;
                type = builder.type;
                decision = builder.decision;
                decisionMode = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.decisionMode, "decisionMode"));
                answer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.answer, "answer"));
                text = builder.text;
                linkId = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.linkId, "linkId"));
                securityLabelNumber = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.securityLabelNumber, "securityLabelNumber"));
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * Unique identifier for this particular Contract Provision.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
             */
            public List<Identifier> getIdentifier() {
                return identifier;
            }

            /**
             * Offer Recipient.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Party} that may be empty.
             */
            public List<Party> getParty() {
                return party;
            }

            /**
             * The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in 
             * any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).
             * 
             * @return
             *     An immutable object of type {@link Reference} that may be null.
             */
            public Reference getTopic() {
                return topic;
            }

            /**
             * Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life 
             * time maximum benefit.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * Type of choice made by accepting party with respect to an offer made by an offeror/ grantee.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getDecision() {
                return decision;
            }

            /**
             * How the decision about a Contract was conveyed.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getDecisionMode() {
                return decisionMode;
            }

            /**
             * Response to offer text.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Answer} that may be empty.
             */
            public List<Answer> getAnswer() {
                return answer;
            }

            /**
             * Human readable form of this Contract Offer.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getText() {
                return text;
            }

            /**
             * The id of the clause or question text of the offer in the referenced questionnaire/response.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getLinkId() {
                return linkId;
            }

            /**
             * Security labels that protects the offer.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link UnsignedInt} that may be empty.
             */
            public List<UnsignedInt> getSecurityLabelNumber() {
                return securityLabelNumber;
            }

            @Override
            public boolean hasChildren() {
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
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Unique identifier for this particular Contract Provision.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Unique identifier for this particular Contract Provision.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Offer Recipient.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Offer Recipient.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in 
                 * any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).
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
                 * Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life 
                 * time maximum benefit.
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
                 * Type of choice made by accepting party with respect to an offer made by an offeror/ grantee.
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
                 * How the decision about a Contract was conveyed.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * How the decision about a Contract was conveyed.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Response to offer text.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Response to offer text.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Human readable form of this Contract Offer.
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
                 * The id of the clause or question text of the offer in the referenced questionnaire/response.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * The id of the clause or question text of the offer in the referenced questionnaire/response.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Security labels that protects the offer.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Security labels that protects the offer.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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

                /**
                 * Build the {@link Offer}
                 * 
                 * @return
                 *     An immutable object of type {@link Offer}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Offer per the base specification
                 */
                @Override
                public Offer build() {
                    return new Offer(this);
                }

                protected Builder from(Offer offer) {
                    super.from(offer);
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
             * Offer Recipient.
             */
            public static class Party extends BackboneElement {
                @Required
                private final List<Reference> reference;
                @Binding(
                    bindingName = "ContractPartyRole",
                    strength = BindingStrength.ValueSet.EXAMPLE,
                    description = "Codes for offer participant roles.",
                    valueSet = "http://hl7.org/fhir/ValueSet/contract-party-role"
                )
                @Required
                private final CodeableConcept role;

                private volatile int hashCode;

                private Party(Builder builder) {
                    super(builder);
                    reference = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.reference, "reference"));
                    role = ValidationSupport.requireNonNull(builder.role, "role");
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * Participant in the offer.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Reference} that is non-empty.
                 */
                public List<Reference> getReference() {
                    return reference;
                }

                /**
                 * How the party participates in the offer.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that is non-null.
                 */
                public CodeableConcept getRole() {
                    return role;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        !reference.isEmpty() || 
                        (role != null);
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
                            accept(reference, "reference", visitor, Reference.class);
                            accept(role, "role", visitor);
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
                    return new Builder().from(this);
                }

                public static Builder builder() {
                    return new Builder();
                }

                public static class Builder extends BackboneElement.Builder {
                    private List<Reference> reference = new ArrayList<>();
                    private CodeableConcept role;

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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * Participant in the offer.
                     * 
                     * <p>Adds new element(s) to the existing list
                     * 
                     * <p>This element is required.
                     * 
                     * @param reference
                     *     Referenced entity
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder reference(Reference... reference) {
                        for (Reference value : reference) {
                            this.reference.add(value);
                        }
                        return this;
                    }

                    /**
                     * Participant in the offer.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection
                     * 
                     * <p>This element is required.
                     * 
                     * @param reference
                     *     Referenced entity
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder reference(Collection<Reference> reference) {
                        this.reference = new ArrayList<>(reference);
                        return this;
                    }

                    /**
                     * How the party participates in the offer.
                     * 
                     * <p>This element is required.
                     * 
                     * @param role
                     *     Participant engagement type
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder role(CodeableConcept role) {
                        this.role = role;
                        return this;
                    }

                    /**
                     * Build the {@link Party}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>reference</li>
                     * <li>role</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Party}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Party per the base specification
                     */
                    @Override
                    public Party build() {
                        return new Party(this);
                    }

                    protected Builder from(Party party) {
                        super.from(party);
                        reference.addAll(party.reference);
                        role = party.role;
                        return this;
                    }
                }
            }

            /**
             * Response to offer text.
             */
            public static class Answer extends BackboneElement {
                @Choice({ Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Uri.class, Attachment.class, Coding.class, Quantity.class, Reference.class })
                @Required
                private final Element value;

                private volatile int hashCode;

                private Answer(Builder builder) {
                    super(builder);
                    value = ValidationSupport.requireChoiceElement(builder.value, "value", Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Uri.class, Attachment.class, Coding.class, Quantity.class, Reference.class);
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * Response to an offer clause or question text, which enables selection of values to be agreed to, e.g., the period of 
                 * participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further 
                 * research.
                 * 
                 * @return
                 *     An immutable object of type {@link Element} that is non-null.
                 */
                public Element getValue() {
                    return value;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
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
                    return new Builder().from(this);
                }

                public static Builder builder() {
                    return new Builder();
                }

                public static class Builder extends BackboneElement.Builder {
                    private Element value;

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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * Response to an offer clause or question text, which enables selection of values to be agreed to, e.g., the period of 
                     * participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further 
                     * research.
                     * 
                     * <p>This element is required.
                     * 
                     * <p>This is a choice element with the following allowed types:
                     * <ul>
                     * <li>{@link Boolean}</li>
                     * <li>{@link Decimal}</li>
                     * <li>{@link Integer}</li>
                     * <li>{@link Date}</li>
                     * <li>{@link DateTime}</li>
                     * <li>{@link Time}</li>
                     * <li>{@link String}</li>
                     * <li>{@link Uri}</li>
                     * <li>{@link Attachment}</li>
                     * <li>{@link Coding}</li>
                     * <li>{@link Quantity}</li>
                     * <li>{@link Reference}</li>
                     * </ul>
                     * 
                     * @param value
                     *     The actual answer response
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder value(Element value) {
                        this.value = value;
                        return this;
                    }

                    /**
                     * Build the {@link Answer}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>value</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Answer}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Answer per the base specification
                     */
                    @Override
                    public Answer build() {
                        return new Answer(this);
                    }

                    protected Builder from(Answer answer) {
                        super.from(answer);
                        value = answer.value;
                        return this;
                    }
                }
            }
        }

        /**
         * Contract Term Asset List.
         */
        public static class Asset extends BackboneElement {
            @Binding(
                bindingName = "ContractAssetScope",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Codes for scoping an asset.",
                valueSet = "http://hl7.org/fhir/ValueSet/contract-assetscope"
            )
            private final CodeableConcept scope;
            @Binding(
                bindingName = "ContractAssetType",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Condes for the type of an asset.",
                valueSet = "http://hl7.org/fhir/ValueSet/contract-assettype"
            )
            private final List<CodeableConcept> type;
            private final List<Reference> typeReference;
            @Binding(
                bindingName = "ContractAssetSubtype",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Condes for the sub-type of an asset.",
                valueSet = "http://hl7.org/fhir/ValueSet/contract-assetsubtype"
            )
            private final List<CodeableConcept> subtype;
            @Binding(
                bindingName = "ConsentContentClass",
                strength = BindingStrength.ValueSet.EXTENSIBLE,
                description = "The class (type) of information a consent rule covers.",
                valueSet = "http://hl7.org/fhir/ValueSet/consent-content-class"
            )
            private final Coding relationship;
            private final List<Context> context;
            private final String condition;
            @Binding(
                bindingName = "AssetAvailabilityType",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Codes for asset availability.",
                valueSet = "http://hl7.org/fhir/ValueSet/asset-availability"
            )
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
                type = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.type, "type"));
                typeReference = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.typeReference, "typeReference"));
                subtype = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.subtype, "subtype"));
                relationship = builder.relationship;
                context = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.context, "context"));
                condition = builder.condition;
                periodType = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.periodType, "periodType"));
                period = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.period, "period"));
                usePeriod = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.usePeriod, "usePeriod"));
                text = builder.text;
                linkId = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.linkId, "linkId"));
                answer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.answer, "answer"));
                securityLabelNumber = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.securityLabelNumber, "securityLabelNumber"));
                valuedItem = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.valuedItem, "valuedItem"));
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * Differentiates the kind of the asset .
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getScope() {
                return scope;
            }

            /**
             * Target entity type about which the term may be concerned.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getType() {
                return type;
            }

            /**
             * Associated entities.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
             */
            public List<Reference> getTypeReference() {
                return typeReference;
            }

            /**
             * May be a subtype or part of an offered asset.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getSubtype() {
                return subtype;
            }

            /**
             * Specifies the applicability of the term to an asset resource instance, and instances it refers to orinstances that 
             * refer to it, and/or are owned by the offeree.
             * 
             * @return
             *     An immutable object of type {@link Coding} that may be null.
             */
            public Coding getRelationship() {
                return relationship;
            }

            /**
             * Circumstance of the asset.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Context} that may be empty.
             */
            public List<Context> getContext() {
                return context;
            }

            /**
             * Description of the quality and completeness of the asset that imay be a factor in its valuation.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getCondition() {
                return condition;
            }

            /**
             * Type of Asset availability for use or ownership.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getPeriodType() {
                return periodType;
            }

            /**
             * Asset relevant contractual time period.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Period} that may be empty.
             */
            public List<Period> getPeriod() {
                return period;
            }

            /**
             * Time period of asset use.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Period} that may be empty.
             */
            public List<Period> getUsePeriod() {
                return usePeriod;
            }

            /**
             * Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in 
             * the formation of the contract.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getText() {
                return text;
            }

            /**
             * Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getLinkId() {
                return linkId;
            }

            /**
             * Response to assets.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Answer} that may be empty.
             */
            public List<Contract.Term.Offer.Answer> getAnswer() {
                return answer;
            }

            /**
             * Security labels that protects the asset.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link UnsignedInt} that may be empty.
             */
            public List<UnsignedInt> getSecurityLabelNumber() {
                return securityLabelNumber;
            }

            /**
             * Contract Valued Item List.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link ValuedItem} that may be empty.
             */
            public List<ValuedItem> getValuedItem() {
                return valuedItem;
            }

            @Override
            public boolean hasChildren() {
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
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Differentiates the kind of the asset .
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
                 * Target entity type about which the term may be concerned.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Target entity type about which the term may be concerned.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Associated entities.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Associated entities.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * May be a subtype or part of an offered asset.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * May be a subtype or part of an offered asset.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Specifies the applicability of the term to an asset resource instance, and instances it refers to orinstances that 
                 * refer to it, and/or are owned by the offeree.
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
                 * Circumstance of the asset.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Circumstance of the asset.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Description of the quality and completeness of the asset that imay be a factor in its valuation.
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
                 * Type of Asset availability for use or ownership.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Type of Asset availability for use or ownership.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Asset relevant contractual time period.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Asset relevant contractual time period.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Time period of asset use.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Time period of asset use.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in 
                 * the formation of the contract.
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
                 * Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Response to assets.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Response to assets.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Security labels that protects the asset.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Security labels that protects the asset.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Contract Valued Item List.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Contract Valued Item List.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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

                /**
                 * Build the {@link Asset}
                 * 
                 * @return
                 *     An immutable object of type {@link Asset}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Asset per the base specification
                 */
                @Override
                public Asset build() {
                    return new Asset(this);
                }

                protected Builder from(Asset asset) {
                    super.from(asset);
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
             * Circumstance of the asset.
             */
            public static class Context extends BackboneElement {
                private final Reference reference;
                @Binding(
                    bindingName = "ContractAssetContext",
                    strength = BindingStrength.ValueSet.EXAMPLE,
                    description = "Codes for the context of the asset.",
                    valueSet = "http://hl7.org/fhir/ValueSet/contract-assetcontext"
                )
                private final List<CodeableConcept> code;
                private final String text;

                private volatile int hashCode;

                private Context(Builder builder) {
                    super(builder);
                    reference = builder.reference;
                    code = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.code, "code"));
                    text = builder.text;
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository), 
                 * location held, e.g., building, jurisdiction.
                 * 
                 * @return
                 *     An immutable object of type {@link Reference} that may be null.
                 */
                public Reference getReference() {
                    return reference;
                }

                /**
                 * Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
                 */
                public List<CodeableConcept> getCode() {
                    return code;
                }

                /**
                 * Context description.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getText() {
                    return text;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (reference != null) || 
                        !code.isEmpty() || 
                        (text != null);
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
                            accept(reference, "reference", visitor);
                            accept(code, "code", visitor, CodeableConcept.class);
                            accept(text, "text", visitor);
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
                    private Reference reference;
                    private List<CodeableConcept> code = new ArrayList<>();
                    private String text;

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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository), 
                     * location held, e.g., building, jurisdiction.
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
                     * Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.
                     * 
                     * <p>Adds new element(s) to the existing list
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
                     * Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * Context description.
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

                    /**
                     * Build the {@link Context}
                     * 
                     * @return
                     *     An immutable object of type {@link Context}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Context per the base specification
                     */
                    @Override
                    public Context build() {
                        return new Context(this);
                    }

                    protected Builder from(Context context) {
                        super.from(context);
                        reference = context.reference;
                        code.addAll(context.code);
                        text = context.text;
                        return this;
                    }
                }
            }

            /**
             * Contract Valued Item List.
             */
            public static class ValuedItem extends BackboneElement {
                @Choice({ CodeableConcept.class, Reference.class })
                private final Element entity;
                private final Identifier identifier;
                private final DateTime effectiveTime;
                private final SimpleQuantity quantity;
                private final Money unitPrice;
                private final Decimal factor;
                private final Decimal points;
                private final Money net;
                private final String payment;
                private final DateTime paymentDate;
                @ReferenceTarget({ "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson" })
                private final Reference responsible;
                @ReferenceTarget({ "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson" })
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
                    linkId = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.linkId, "linkId"));
                    securityLabelNumber = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.securityLabelNumber, "securityLabelNumber"));
                    ValidationSupport.checkReferenceType(responsible, "responsible", "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson");
                    ValidationSupport.checkReferenceType(recipient, "recipient", "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson");
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * Specific type of Contract Valued Item that may be priced.
                 * 
                 * @return
                 *     An immutable object of type {@link Element} that may be null.
                 */
                public Element getEntity() {
                    return entity;
                }

                /**
                 * Identifies a Contract Valued Item instance.
                 * 
                 * @return
                 *     An immutable object of type {@link Identifier} that may be null.
                 */
                public Identifier getIdentifier() {
                    return identifier;
                }

                /**
                 * Indicates the time during which this Contract ValuedItem information is effective.
                 * 
                 * @return
                 *     An immutable object of type {@link DateTime} that may be null.
                 */
                public DateTime getEffectiveTime() {
                    return effectiveTime;
                }

                /**
                 * Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or 
                 * measurable Contract Valued Item instances.
                 * 
                 * @return
                 *     An immutable object of type {@link SimpleQuantity} that may be null.
                 */
                public SimpleQuantity getQuantity() {
                    return quantity;
                }

                /**
                 * A Contract Valued Item unit valuation measure.
                 * 
                 * @return
                 *     An immutable object of type {@link Money} that may be null.
                 */
                public Money getUnitPrice() {
                    return unitPrice;
                }

                /**
                 * A real number that represents a multiplier used in determining the overall value of the Contract Valued Item 
                 * delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                 * 
                 * @return
                 *     An immutable object of type {@link Decimal} that may be null.
                 */
                public Decimal getFactor() {
                    return factor;
                }

                /**
                 * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the 
                 * Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued 
                 * Item, such that a monetary amount can be assigned to each point.
                 * 
                 * @return
                 *     An immutable object of type {@link Decimal} that may be null.
                 */
                public Decimal getPoints() {
                    return points;
                }

                /**
                 * Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit 
                 * Quantity * unit Price (Cost per Point) * factor Number * points = net Amount. Quantity, factor and points are assumed 
                 * to be 1 if not supplied.
                 * 
                 * @return
                 *     An immutable object of type {@link Money} that may be null.
                 */
                public Money getNet() {
                    return net;
                }

                /**
                 * Terms of valuation.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getPayment() {
                    return payment;
                }

                /**
                 * When payment is due.
                 * 
                 * @return
                 *     An immutable object of type {@link DateTime} that may be null.
                 */
                public DateTime getPaymentDate() {
                    return paymentDate;
                }

                /**
                 * Who will make payment.
                 * 
                 * @return
                 *     An immutable object of type {@link Reference} that may be null.
                 */
                public Reference getResponsible() {
                    return responsible;
                }

                /**
                 * Who will receive payment.
                 * 
                 * @return
                 *     An immutable object of type {@link Reference} that may be null.
                 */
                public Reference getRecipient() {
                    return recipient;
                }

                /**
                 * Id of the clause or question text related to the context of this valuedItem in the referenced form or 
                 * QuestionnaireResponse.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
                 */
                public List<String> getLinkId() {
                    return linkId;
                }

                /**
                 * A set of security labels that define which terms are controlled by this condition.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link UnsignedInt} that may be empty.
                 */
                public List<UnsignedInt> getSecurityLabelNumber() {
                    return securityLabelNumber;
                }

                @Override
                public boolean hasChildren() {
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
                public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                    if (visitor.preVisit(this)) {
                        visitor.visitStart(elementName, elementIndex, this);
                        if (visitor.visit(elementName, elementIndex, this)) {
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
                    private Element entity;
                    private Identifier identifier;
                    private DateTime effectiveTime;
                    private SimpleQuantity quantity;
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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * Specific type of Contract Valued Item that may be priced.
                     * 
                     * <p>This is a choice element with the following allowed types:
                     * <ul>
                     * <li>{@link CodeableConcept}</li>
                     * <li>{@link Reference}</li>
                     * </ul>
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
                     * Identifies a Contract Valued Item instance.
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
                     * Indicates the time during which this Contract ValuedItem information is effective.
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
                     * Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or 
                     * measurable Contract Valued Item instances.
                     * 
                     * @param quantity
                     *     Count of Contract Valued Items
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder quantity(SimpleQuantity quantity) {
                        this.quantity = quantity;
                        return this;
                    }

                    /**
                     * A Contract Valued Item unit valuation measure.
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
                     * A real number that represents a multiplier used in determining the overall value of the Contract Valued Item 
                     * delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
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
                     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the 
                     * Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued 
                     * Item, such that a monetary amount can be assigned to each point.
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
                     * Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit 
                     * Quantity * unit Price (Cost per Point) * factor Number * points = net Amount. Quantity, factor and points are assumed 
                     * to be 1 if not supplied.
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
                     * Terms of valuation.
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
                     * When payment is due.
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
                     * Who will make payment.
                     * 
                     * <p>Allowed resource types for this reference:
                     * <ul>
                     * <li>{@link Organization}</li>
                     * <li>{@link Patient}</li>
                     * <li>{@link Practitioner}</li>
                     * <li>{@link PractitionerRole}</li>
                     * <li>{@link RelatedPerson}</li>
                     * </ul>
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
                     * Who will receive payment.
                     * 
                     * <p>Allowed resource types for this reference:
                     * <ul>
                     * <li>{@link Organization}</li>
                     * <li>{@link Patient}</li>
                     * <li>{@link Practitioner}</li>
                     * <li>{@link PractitionerRole}</li>
                     * <li>{@link RelatedPerson}</li>
                     * </ul>
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
                     * Id of the clause or question text related to the context of this valuedItem in the referenced form or 
                     * QuestionnaireResponse.
                     * 
                     * <p>Adds new element(s) to the existing list
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
                     * Id of the clause or question text related to the context of this valuedItem in the referenced form or 
                     * QuestionnaireResponse.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * A set of security labels that define which terms are controlled by this condition.
                     * 
                     * <p>Adds new element(s) to the existing list
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
                     * A set of security labels that define which terms are controlled by this condition.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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

                    /**
                     * Build the {@link ValuedItem}
                     * 
                     * @return
                     *     An immutable object of type {@link ValuedItem}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid ValuedItem per the base specification
                     */
                    @Override
                    public ValuedItem build() {
                        return new ValuedItem(this);
                    }

                    protected Builder from(ValuedItem valuedItem) {
                        super.from(valuedItem);
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
         * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
         * taking place.
         */
        public static class Action extends BackboneElement {
            private final Boolean doNotPerform;
            @Binding(
                bindingName = "ContractAction",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Detailed codes for the contract action.",
                valueSet = "http://hl7.org/fhir/ValueSet/contract-action"
            )
            @Required
            private final CodeableConcept type;
            private final List<Subject> subject;
            @Binding(
                bindingName = "ContractActionReason",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Detailed codes for the contract action reason.",
                valueSet = "http://terminology.hl7.org/ValueSet/v3-PurposeOfUse"
            )
            @Required
            private final CodeableConcept intent;
            private final List<String> linkId;
            @Binding(
                bindingName = "ContractActionStatus",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Codes for the status of an term action.",
                valueSet = "http://hl7.org/fhir/ValueSet/contract-actionstatus"
            )
            @Required
            private final CodeableConcept status;
            @ReferenceTarget({ "Encounter", "EpisodeOfCare" })
            private final Reference context;
            private final List<String> contextLinkId;
            @Choice({ DateTime.class, Period.class, Timing.class })
            private final Element occurrence;
            private final List<Reference> requester;
            private final List<String> requesterLinkId;
            @Binding(
                bindingName = "ContractActionPerformerType",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Codes for the types of action perfomer.",
                valueSet = "http://hl7.org/fhir/ValueSet/provenance-agent-type"
            )
            private final List<CodeableConcept> performerType;
            @Binding(
                bindingName = "ContractActionPerformerRole",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Codes for the role of the action performer.",
                valueSet = "http://hl7.org/fhir/ValueSet/provenance-agent-role"
            )
            private final CodeableConcept performerRole;
            @ReferenceTarget({ "RelatedPerson", "Patient", "Practitioner", "PractitionerRole", "CareTeam", "Device", "Substance", "Organization", "Location" })
            private final Reference performer;
            private final List<String> performerLinkId;
            @Binding(
                bindingName = "ContractActionReason",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Detailed codes for the contract action reason.",
                valueSet = "http://terminology.hl7.org/ValueSet/v3-PurposeOfUse"
            )
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
                subject = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.subject, "subject"));
                intent = ValidationSupport.requireNonNull(builder.intent, "intent");
                linkId = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.linkId, "linkId"));
                status = ValidationSupport.requireNonNull(builder.status, "status");
                context = builder.context;
                contextLinkId = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.contextLinkId, "contextLinkId"));
                occurrence = ValidationSupport.choiceElement(builder.occurrence, "occurrence", DateTime.class, Period.class, Timing.class);
                requester = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.requester, "requester"));
                requesterLinkId = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.requesterLinkId, "requesterLinkId"));
                performerType = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.performerType, "performerType"));
                performerRole = builder.performerRole;
                performer = builder.performer;
                performerLinkId = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.performerLinkId, "performerLinkId"));
                reasonCode = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonCode, "reasonCode"));
                reasonReference = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonReference, "reasonReference"));
                reason = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reason, "reason"));
                reasonLinkId = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonLinkId, "reasonLinkId"));
                note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
                securityLabelNumber = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.securityLabelNumber, "securityLabelNumber"));
                ValidationSupport.checkReferenceType(context, "context", "Encounter", "EpisodeOfCare");
                ValidationSupport.checkReferenceType(performer, "performer", "RelatedPerson", "Patient", "Practitioner", "PractitionerRole", "CareTeam", "Device", "Substance", "Organization", "Location");
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * True if the term prohibits the action.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getDoNotPerform() {
                return doNotPerform;
            }

            /**
             * Activity or service obligation to be done or not done, performed or not performed, effectuated or not by this Contract 
             * term.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * Entity of the action.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Subject} that may be empty.
             */
            public List<Subject> getSubject() {
                return subject;
            }

            /**
             * Reason or purpose for the action stipulated by this Contract Provision.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getIntent() {
                return intent;
            }

            /**
             * Id [identifier??] of the clause or question text related to this action in the referenced form or 
             * QuestionnaireResponse.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getLinkId() {
                return linkId;
            }

            /**
             * Current state of the term action.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getStatus() {
                return status;
            }

            /**
             * Encounter or Episode with primary association to specified term activity.
             * 
             * @return
             *     An immutable object of type {@link Reference} that may be null.
             */
            public Reference getContext() {
                return context;
            }

            /**
             * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
             * QuestionnaireResponse.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getContextLinkId() {
                return contextLinkId;
            }

            /**
             * When action happens.
             * 
             * @return
             *     An immutable object of type {@link Element} that may be null.
             */
            public Element getOccurrence() {
                return occurrence;
            }

            /**
             * Who or what initiated the action and has responsibility for its activation.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
             */
            public List<Reference> getRequester() {
                return requester;
            }

            /**
             * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
             * QuestionnaireResponse.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getRequesterLinkId() {
                return requesterLinkId;
            }

            /**
             * The type of individual that is desired or required to perform or not perform the action.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getPerformerType() {
                return performerType;
            }

            /**
             * The type of role or competency of an individual desired or required to perform or not perform the action.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getPerformerRole() {
                return performerRole;
            }

            /**
             * Indicates who or what is being asked to perform (or not perform) the ction.
             * 
             * @return
             *     An immutable object of type {@link Reference} that may be null.
             */
            public Reference getPerformer() {
                return performer;
            }

            /**
             * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
             * referenced form or QuestionnaireResponse.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getPerformerLinkId() {
                return performerLinkId;
            }

            /**
             * Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getReasonCode() {
                return reasonCode;
            }

            /**
             * Indicates another resource whose existence justifies permitting or not permitting this action.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
             */
            public List<Reference> getReasonReference() {
                return reasonReference;
            }

            /**
             * Describes why the action is to be performed or not performed in textual form.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getReason() {
                return reason;
            }

            /**
             * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
             * referenced form or QuestionnaireResponse.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getReasonLinkId() {
                return reasonLinkId;
            }

            /**
             * Comments made about the term action made by the requester, performer, subject or other participants.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
             */
            public List<Annotation> getNote() {
                return note;
            }

            /**
             * Security labels that protects the action.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link UnsignedInt} that may be empty.
             */
            public List<UnsignedInt> getSecurityLabelNumber() {
                return securityLabelNumber;
            }

            @Override
            public boolean hasChildren() {
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
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
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
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private Boolean doNotPerform;
                private CodeableConcept type;
                private List<Subject> subject = new ArrayList<>();
                private CodeableConcept intent;
                private List<String> linkId = new ArrayList<>();
                private CodeableConcept status;
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * True if the term prohibits the action.
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
                 * Activity or service obligation to be done or not done, performed or not performed, effectuated or not by this Contract 
                 * term.
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     Type or form of the action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Entity of the action.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Entity of the action.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Reason or purpose for the action stipulated by this Contract Provision.
                 * 
                 * <p>This element is required.
                 * 
                 * @param intent
                 *     Purpose for the Contract Term Action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder intent(CodeableConcept intent) {
                    this.intent = intent;
                    return this;
                }

                /**
                 * Id [identifier??] of the clause or question text related to this action in the referenced form or 
                 * QuestionnaireResponse.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Id [identifier??] of the clause or question text related to this action in the referenced form or 
                 * QuestionnaireResponse.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Current state of the term action.
                 * 
                 * <p>This element is required.
                 * 
                 * @param status
                 *     State of the action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder status(CodeableConcept status) {
                    this.status = status;
                    return this;
                }

                /**
                 * Encounter or Episode with primary association to specified term activity.
                 * 
                 * <p>Allowed resource types for this reference:
                 * <ul>
                 * <li>{@link Encounter}</li>
                 * <li>{@link EpisodeOfCare}</li>
                 * </ul>
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
                 * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
                 * QuestionnaireResponse.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
                 * QuestionnaireResponse.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * When action happens.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link DateTime}</li>
                 * <li>{@link Period}</li>
                 * <li>{@link Timing}</li>
                 * </ul>
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
                 * Who or what initiated the action and has responsibility for its activation.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Who or what initiated the action and has responsibility for its activation.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
                 * QuestionnaireResponse.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or 
                 * QuestionnaireResponse.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * The type of individual that is desired or required to perform or not perform the action.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * The type of individual that is desired or required to perform or not perform the action.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * The type of role or competency of an individual desired or required to perform or not perform the action.
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
                 * Indicates who or what is being asked to perform (or not perform) the ction.
                 * 
                 * <p>Allowed resource types for this reference:
                 * <ul>
                 * <li>{@link RelatedPerson}</li>
                 * <li>{@link Patient}</li>
                 * <li>{@link Practitioner}</li>
                 * <li>{@link PractitionerRole}</li>
                 * <li>{@link CareTeam}</li>
                 * <li>{@link Device}</li>
                 * <li>{@link Substance}</li>
                 * <li>{@link Organization}</li>
                 * <li>{@link Location}</li>
                 * </ul>
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
                 * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
                 * referenced form or QuestionnaireResponse.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
                 * referenced form or QuestionnaireResponse.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Indicates another resource whose existence justifies permitting or not permitting this action.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Indicates another resource whose existence justifies permitting or not permitting this action.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Describes why the action is to be performed or not performed in textual form.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Describes why the action is to be performed or not performed in textual form.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
                 * referenced form or QuestionnaireResponse.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Id [identifier??] of the clause or question text related to the reason type or reference of this action in the 
                 * referenced form or QuestionnaireResponse.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Comments made about the term action made by the requester, performer, subject or other participants.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Comments made about the term action made by the requester, performer, subject or other participants.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * Security labels that protects the action.
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * Security labels that protects the action.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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

                /**
                 * Build the {@link Action}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * <li>intent</li>
                 * <li>status</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Action}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Action per the base specification
                 */
                @Override
                public Action build() {
                    return new Action(this);
                }

                protected Builder from(Action action) {
                    super.from(action);
                    doNotPerform = action.doNotPerform;
                    type = action.type;
                    subject.addAll(action.subject);
                    intent = action.intent;
                    linkId.addAll(action.linkId);
                    status = action.status;
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
             * Entity of the action.
             */
            public static class Subject extends BackboneElement {
                @Required
                private final List<Reference> reference;
                @Binding(
                    bindingName = "ContractActorRole",
                    strength = BindingStrength.ValueSet.EXAMPLE,
                    description = "Detailed codes for the contract actor role.",
                    valueSet = "http://hl7.org/fhir/ValueSet/contract-actorrole"
                )
                private final CodeableConcept role;

                private volatile int hashCode;

                private Subject(Builder builder) {
                    super(builder);
                    reference = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.reference, "reference"));
                    role = builder.role;
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * The entity the action is performed or not performed on or for.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Reference} that is non-empty.
                 */
                public List<Reference> getReference() {
                    return reference;
                }

                /**
                 * Role type of agent assigned roles in this Contract.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that may be null.
                 */
                public CodeableConcept getRole() {
                    return role;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        !reference.isEmpty() || 
                        (role != null);
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
                            accept(reference, "reference", visitor, Reference.class);
                            accept(role, "role", visitor);
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
                    return new Builder().from(this);
                }

                public static Builder builder() {
                    return new Builder();
                }

                public static class Builder extends BackboneElement.Builder {
                    private List<Reference> reference = new ArrayList<>();
                    private CodeableConcept role;

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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * The entity the action is performed or not performed on or for.
                     * 
                     * <p>Adds new element(s) to the existing list
                     * 
                     * <p>This element is required.
                     * 
                     * @param reference
                     *     Entity of the action
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder reference(Reference... reference) {
                        for (Reference value : reference) {
                            this.reference.add(value);
                        }
                        return this;
                    }

                    /**
                     * The entity the action is performed or not performed on or for.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection
                     * 
                     * <p>This element is required.
                     * 
                     * @param reference
                     *     Entity of the action
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder reference(Collection<Reference> reference) {
                        this.reference = new ArrayList<>(reference);
                        return this;
                    }

                    /**
                     * Role type of agent assigned roles in this Contract.
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

                    /**
                     * Build the {@link Subject}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>reference</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Subject}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Subject per the base specification
                     */
                    @Override
                    public Subject build() {
                        return new Subject(this);
                    }

                    protected Builder from(Subject subject) {
                        super.from(subject);
                        reference.addAll(subject.reference);
                        role = subject.role;
                        return this;
                    }
                }
            }
        }
    }

    /**
     * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are 
     * any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the 
     * contract such as a notary or witness.
     */
    public static class Signer extends BackboneElement {
        @Binding(
            bindingName = "ContractSignerType",
            strength = BindingStrength.ValueSet.PREFERRED,
            description = "List of parties who may be signing.",
            valueSet = "http://hl7.org/fhir/ValueSet/contract-signer-type"
        )
        @Required
        private final Coding type;
        @ReferenceTarget({ "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson" })
        @Required
        private final Reference party;
        @Required
        private final List<Signature> signature;

        private volatile int hashCode;

        private Signer(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            party = ValidationSupport.requireNonNull(builder.party, "party");
            signature = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.signature, "signature"));
            ValidationSupport.checkReferenceType(party, "party", "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Role of this Contract signer, e.g. notary, grantee.
         * 
         * @return
         *     An immutable object of type {@link Coding} that is non-null.
         */
        public Coding getType() {
            return type;
        }

        /**
         * Party which is a signator to this Contract.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getParty() {
            return party;
        }

        /**
         * Legally binding Contract DSIG signature contents in Base64.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Signature} that is non-empty.
         */
        public List<Signature> getSignature() {
            return signature;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (party != null) || 
                !signature.isEmpty();
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
                    accept(party, "party", visitor);
                    accept(signature, "signature", visitor, Signature.class);
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private Coding type;
            private Reference party;
            private List<Signature> signature = new ArrayList<>();

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * Role of this Contract signer, e.g. notary, grantee.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Contract Signatory Role
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(Coding type) {
                this.type = type;
                return this;
            }

            /**
             * Party which is a signator to this Contract.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * <li>{@link Patient}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link RelatedPerson}</li>
             * </ul>
             * 
             * @param party
             *     Contract Signatory Party
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder party(Reference party) {
                this.party = party;
                return this;
            }

            /**
             * Legally binding Contract DSIG signature contents in Base64.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * <p>This element is required.
             * 
             * @param signature
             *     Contract Documentation Signature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder signature(Signature... signature) {
                for (Signature value : signature) {
                    this.signature.add(value);
                }
                return this;
            }

            /**
             * Legally binding Contract DSIG signature contents in Base64.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * <p>This element is required.
             * 
             * @param signature
             *     Contract Documentation Signature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder signature(Collection<Signature> signature) {
                this.signature = new ArrayList<>(signature);
                return this;
            }

            /**
             * Build the {@link Signer}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>party</li>
             * <li>signature</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Signer}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Signer per the base specification
             */
            @Override
            public Signer build() {
                return new Signer(this);
            }

            protected Builder from(Signer signer) {
                super.from(signer);
                type = signer.type;
                party = signer.party;
                signature.addAll(signer.signature);
                return this;
            }
        }
    }

    /**
     * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the 
     * representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a 
     * layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the 
     * Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
     */
    public static class Friendly extends BackboneElement {
        @Choice({ Attachment.class, Reference.class })
        @Required
        private final Element content;

        private volatile int hashCode;

        private Friendly(Builder builder) {
            super(builder);
            content = ValidationSupport.requireChoiceElement(builder.content, "content", Attachment.class, Reference.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure 
         * understandability.
         * 
         * @return
         *     An immutable object of type {@link Element} that is non-null.
         */
        public Element getContent() {
            return content;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (content != null);
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
                    accept(content, "content", visitor);
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private Element content;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure 
             * understandability.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Attachment}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * @param content
             *     Easily comprehended representation of this Contract
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder content(Element content) {
                this.content = content;
                return this;
            }

            /**
             * Build the {@link Friendly}
             * 
             * <p>Required elements:
             * <ul>
             * <li>content</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Friendly}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Friendly per the base specification
             */
            @Override
            public Friendly build() {
                return new Friendly(this);
            }

            protected Builder from(Friendly friendly) {
                super.from(friendly);
                content = friendly.content;
                return this;
            }
        }
    }

    /**
     * List of Legal expressions or representations of this Contract.
     */
    public static class Legal extends BackboneElement {
        @Choice({ Attachment.class, Reference.class })
        @Required
        private final Element content;

        private volatile int hashCode;

        private Legal(Builder builder) {
            super(builder);
            content = ValidationSupport.requireChoiceElement(builder.content, "content", Attachment.class, Reference.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Contract legal text in human renderable form.
         * 
         * @return
         *     An immutable object of type {@link Element} that is non-null.
         */
        public Element getContent() {
            return content;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (content != null);
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
                    accept(content, "content", visitor);
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private Element content;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * Contract legal text in human renderable form.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Attachment}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * @param content
             *     Contract Legal Text
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder content(Element content) {
                this.content = content;
                return this;
            }

            /**
             * Build the {@link Legal}
             * 
             * <p>Required elements:
             * <ul>
             * <li>content</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Legal}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Legal per the base specification
             */
            @Override
            public Legal build() {
                return new Legal(this);
            }

            protected Builder from(Legal legal) {
                super.from(legal);
                content = legal.content;
                return this;
            }
        }
    }

    /**
     * List of Computable Policy Rule Language Representations of this Contract.
     */
    public static class Rule extends BackboneElement {
        @Choice({ Attachment.class, Reference.class })
        @Required
        private final Element content;

        private volatile int hashCode;

        private Rule(Builder builder) {
            super(builder);
            content = ValidationSupport.requireChoiceElement(builder.content, "content", Attachment.class, Reference.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).
         * 
         * @return
         *     An immutable object of type {@link Element} that is non-null.
         */
        public Element getContent() {
            return content;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (content != null);
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
                    accept(content, "content", visitor);
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private Element content;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Attachment}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * @param content
             *     Computable Contract Rules
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder content(Element content) {
                this.content = content;
                return this;
            }

            /**
             * Build the {@link Rule}
             * 
             * <p>Required elements:
             * <ul>
             * <li>content</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Rule}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Rule per the base specification
             */
            @Override
            public Rule build() {
                return new Rule(this);
            }

            protected Builder from(Rule rule) {
                super.from(rule);
                content = rule.content;
                return this;
            }
        }
    }
}
