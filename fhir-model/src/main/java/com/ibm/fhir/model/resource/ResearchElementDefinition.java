/*
 * (C) Copyright IBM Corp. 2019, 2022
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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.DataRequirement;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Expression;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.GroupMeasure;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResearchElementType;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.VariableType;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The ResearchElementDefinition resource describes a "PICO" element that knowledge (evidence, assertion, recommendation) 
 * is about.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "red-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.exists() implies name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/ResearchElementDefinition"
)
@Constraint(
    id = "researchElementDefinition-1",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/subject-type",
    expression = "subject.as(CodeableConcept).exists() implies (subject.as(CodeableConcept).memberOf('http://hl7.org/fhir/ValueSet/subject-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/ResearchElementDefinition",
    generated = true
)
@Constraint(
    id = "researchElementDefinition-2",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/ResearchElementDefinition",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ResearchElementDefinition extends DomainResource {
    @Summary
    private final Uri url;
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final String version;
    @Summary
    private final String name;
    @Summary
    private final String title;
    @Summary
    private final String shortTitle;
    private final String subtitle;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.3.0-cibuild"
    )
    @Required
    private final PublicationStatus status;
    @Summary
    private final Boolean experimental;
    @ReferenceTarget({ "Group" })
    @Choice({ CodeableConcept.class, Reference.class })
    @Binding(
        bindingName = "SubjectType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The possible types of subjects for a measure (E.g. Patient, Practitioner, Organization, Location, etc.).",
        valueSet = "http://hl7.org/fhir/ValueSet/subject-type"
    )
    private final Element subject;
    @Summary
    private final DateTime date;
    @Summary
    private final String publisher;
    @Summary
    private final List<ContactDetail> contact;
    @Summary
    private final Markdown description;
    private final List<String> comment;
    @Summary
    private final List<UsageContext> useContext;
    @Summary
    @Binding(
        bindingName = "Jurisdiction",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Countries and regions within which this artifact is targeted for use.",
        valueSet = "http://hl7.org/fhir/ValueSet/jurisdiction"
    )
    private final List<CodeableConcept> jurisdiction;
    private final Markdown purpose;
    private final String usage;
    private final Markdown copyright;
    private final Date approvalDate;
    private final Date lastReviewDate;
    @Summary
    private final Period effectivePeriod;
    @Binding(
        bindingName = "DefinitionTopic",
        strength = BindingStrength.Value.EXAMPLE,
        description = "High-level categorization of the definition, used for searching, sorting, and filtering.",
        valueSet = "http://hl7.org/fhir/ValueSet/definition-topic"
    )
    private final List<CodeableConcept> topic;
    private final List<ContactDetail> author;
    private final List<ContactDetail> editor;
    private final List<ContactDetail> reviewer;
    private final List<ContactDetail> endorser;
    private final List<RelatedArtifact> relatedArtifact;
    private final List<Canonical> library;
    @Summary
    @Binding(
        bindingName = "ResearchElementType",
        strength = BindingStrength.Value.REQUIRED,
        description = "The possible types of research elements (E.g. Population, Exposure, Outcome).",
        valueSet = "http://hl7.org/fhir/ValueSet/research-element-type|4.3.0-cibuild"
    )
    @Required
    private final ResearchElementType type;
    @Binding(
        bindingName = "VariableType",
        strength = BindingStrength.Value.REQUIRED,
        description = "The possible types of variables for exposures or outcomes (E.g. Dichotomous, Continuous, Descriptive).",
        valueSet = "http://hl7.org/fhir/ValueSet/variable-type|4.3.0-cibuild"
    )
    private final VariableType variableType;
    @Summary
    @Required
    private final List<Characteristic> characteristic;

    private ResearchElementDefinition(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = builder.name;
        title = builder.title;
        shortTitle = builder.shortTitle;
        subtitle = builder.subtitle;
        status = builder.status;
        experimental = builder.experimental;
        subject = builder.subject;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        comment = Collections.unmodifiableList(builder.comment);
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        purpose = builder.purpose;
        usage = builder.usage;
        copyright = builder.copyright;
        approvalDate = builder.approvalDate;
        lastReviewDate = builder.lastReviewDate;
        effectivePeriod = builder.effectivePeriod;
        topic = Collections.unmodifiableList(builder.topic);
        author = Collections.unmodifiableList(builder.author);
        editor = Collections.unmodifiableList(builder.editor);
        reviewer = Collections.unmodifiableList(builder.reviewer);
        endorser = Collections.unmodifiableList(builder.endorser);
        relatedArtifact = Collections.unmodifiableList(builder.relatedArtifact);
        library = Collections.unmodifiableList(builder.library);
        type = builder.type;
        variableType = builder.variableType;
        characteristic = Collections.unmodifiableList(builder.characteristic);
    }

    /**
     * An absolute URI that is used to identify this research element definition when it is referenced in a specification, 
     * model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a 
     * literal address at which at which an authoritative instance of this research element definition is (or will be) 
     * published. This URL can be the target of a canonical reference. It SHALL remain the same when the research element 
     * definition is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this research element definition when it is represented in other formats, 
     * or referenced in a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the research element definition when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the research element definition author 
     * and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version 
     * is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
     * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
     * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
     * version is required for non-experimental active artifacts.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the research element definition. This name should be usable as an identifier for 
     * the module by machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the research element definition.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is 
     * not necessary.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * The status of this research element definition. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this research element definition is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is 
     * assumed, but the subject of the ResearchElementDefinition can be anything.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} or {@link Reference} that may be null.
     */
    public Element getSubject() {
        return subject;
    }

    /**
     * The date (and optionally time) when the research element definition was published. The date must change when the 
     * business version changes and it must change if the status code changes. In addition, it should change when the 
     * substantive content of the research element definition changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the research element definition.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * A free text natural language description of the research element definition from a consumer's perspective.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * A human-readable string to clarify or explain concepts about the resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getComment() {
        return comment;
    }

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate research element definition instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the research element definition is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this research element definition is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * A copyright statement relating to the research element definition and/or its contents. Copyright statements are 
     * generally legal restrictions on the use and publishing of the research element definition.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is 
     * officially approved for usage.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * The date on which the resource content was last reviewed. Review happens periodically after approval but does not 
     * change the original approval date.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    /**
     * The period during which the research element definition content was or is planned to be in active use.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getEffectivePeriod() {
        return effectivePeriod;
    }

    /**
     * Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization 
     * grouping types of ResearchElementDefinitions that can be useful for filtering and searching.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getTopic() {
        return topic;
    }

    /**
     * An individiual or organization primarily involved in the creation and maintenance of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getAuthor() {
        return author;
    }

    /**
     * An individual or organization primarily responsible for internal coherence of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getEditor() {
        return editor;
    }

    /**
     * An individual or organization primarily responsible for review of some aspect of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getReviewer() {
        return reviewer;
    }

    /**
     * An individual or organization responsible for officially endorsing the content for use in some setting.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getEndorser() {
        return endorser;
    }

    /**
     * Related artifacts such as additional documentation, justification, or bibliographic references.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact} that may be empty.
     */
    public List<RelatedArtifact> getRelatedArtifact() {
        return relatedArtifact;
    }

    /**
     * A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getLibrary() {
        return library;
    }

    /**
     * The type of research element, a population, an exposure, or an outcome.
     * 
     * @return
     *     An immutable object of type {@link ResearchElementType} that is non-null.
     */
    public ResearchElementType getType() {
        return type;
    }

    /**
     * The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).
     * 
     * @return
     *     An immutable object of type {@link VariableType} that may be null.
     */
    public VariableType getVariableType() {
        return variableType;
    }

    /**
     * A characteristic that defines the members of the research element. Multiple characteristics are applied with "and" 
     * semantics.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Characteristic} that is non-empty.
     */
    public List<Characteristic> getCharacteristic() {
        return characteristic;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            !identifier.isEmpty() || 
            (version != null) || 
            (name != null) || 
            (title != null) || 
            (shortTitle != null) || 
            (subtitle != null) || 
            (status != null) || 
            (experimental != null) || 
            (subject != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (description != null) || 
            !comment.isEmpty() || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (purpose != null) || 
            (usage != null) || 
            (copyright != null) || 
            (approvalDate != null) || 
            (lastReviewDate != null) || 
            (effectivePeriod != null) || 
            !topic.isEmpty() || 
            !author.isEmpty() || 
            !editor.isEmpty() || 
            !reviewer.isEmpty() || 
            !endorser.isEmpty() || 
            !relatedArtifact.isEmpty() || 
            !library.isEmpty() || 
            (type != null) || 
            (variableType != null) || 
            !characteristic.isEmpty();
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
                accept(url, "url", visitor);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(version, "version", visitor);
                accept(name, "name", visitor);
                accept(title, "title", visitor);
                accept(shortTitle, "shortTitle", visitor);
                accept(subtitle, "subtitle", visitor);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(subject, "subject", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(comment, "comment", visitor, String.class);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(purpose, "purpose", visitor);
                accept(usage, "usage", visitor);
                accept(copyright, "copyright", visitor);
                accept(approvalDate, "approvalDate", visitor);
                accept(lastReviewDate, "lastReviewDate", visitor);
                accept(effectivePeriod, "effectivePeriod", visitor);
                accept(topic, "topic", visitor, CodeableConcept.class);
                accept(author, "author", visitor, ContactDetail.class);
                accept(editor, "editor", visitor, ContactDetail.class);
                accept(reviewer, "reviewer", visitor, ContactDetail.class);
                accept(endorser, "endorser", visitor, ContactDetail.class);
                accept(relatedArtifact, "relatedArtifact", visitor, RelatedArtifact.class);
                accept(library, "library", visitor, Canonical.class);
                accept(type, "type", visitor);
                accept(variableType, "variableType", visitor);
                accept(characteristic, "characteristic", visitor, Characteristic.class);
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
        ResearchElementDefinition other = (ResearchElementDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(url, other.url) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(version, other.version) && 
            Objects.equals(name, other.name) && 
            Objects.equals(title, other.title) && 
            Objects.equals(shortTitle, other.shortTitle) && 
            Objects.equals(subtitle, other.subtitle) && 
            Objects.equals(status, other.status) && 
            Objects.equals(experimental, other.experimental) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(description, other.description) && 
            Objects.equals(comment, other.comment) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(usage, other.usage) && 
            Objects.equals(copyright, other.copyright) && 
            Objects.equals(approvalDate, other.approvalDate) && 
            Objects.equals(lastReviewDate, other.lastReviewDate) && 
            Objects.equals(effectivePeriod, other.effectivePeriod) && 
            Objects.equals(topic, other.topic) && 
            Objects.equals(author, other.author) && 
            Objects.equals(editor, other.editor) && 
            Objects.equals(reviewer, other.reviewer) && 
            Objects.equals(endorser, other.endorser) && 
            Objects.equals(relatedArtifact, other.relatedArtifact) && 
            Objects.equals(library, other.library) && 
            Objects.equals(type, other.type) && 
            Objects.equals(variableType, other.variableType) && 
            Objects.equals(characteristic, other.characteristic);
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
                url, 
                identifier, 
                version, 
                name, 
                title, 
                shortTitle, 
                subtitle, 
                status, 
                experimental, 
                subject, 
                date, 
                publisher, 
                contact, 
                description, 
                comment, 
                useContext, 
                jurisdiction, 
                purpose, 
                usage, 
                copyright, 
                approvalDate, 
                lastReviewDate, 
                effectivePeriod, 
                topic, 
                author, 
                editor, 
                reviewer, 
                endorser, 
                relatedArtifact, 
                library, 
                type, 
                variableType, 
                characteristic);
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
        private Uri url;
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String name;
        private String title;
        private String shortTitle;
        private String subtitle;
        private PublicationStatus status;
        private Boolean experimental;
        private Element subject;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<String> comment = new ArrayList<>();
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private String usage;
        private Markdown copyright;
        private Date approvalDate;
        private Date lastReviewDate;
        private Period effectivePeriod;
        private List<CodeableConcept> topic = new ArrayList<>();
        private List<ContactDetail> author = new ArrayList<>();
        private List<ContactDetail> editor = new ArrayList<>();
        private List<ContactDetail> reviewer = new ArrayList<>();
        private List<ContactDetail> endorser = new ArrayList<>();
        private List<RelatedArtifact> relatedArtifact = new ArrayList<>();
        private List<Canonical> library = new ArrayList<>();
        private ResearchElementType type;
        private VariableType variableType;
        private List<Characteristic> characteristic = new ArrayList<>();

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
         * An absolute URI that is used to identify this research element definition when it is referenced in a specification, 
         * model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a 
         * literal address at which at which an authoritative instance of this research element definition is (or will be) 
         * published. This URL can be the target of a canonical reference. It SHALL remain the same when the research element 
         * definition is stored on different servers.
         * 
         * @param url
         *     Canonical identifier for this research element definition, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this research element definition when it is represented in other formats, 
         * or referenced in a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the research element definition
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
         * A formal identifier that is used to identify this research element definition when it is represented in other formats, 
         * or referenced in a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the research element definition
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
         * Convenience method for setting {@code version}.
         * 
         * @param version
         *     Business version of the research element definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #version(com.ibm.fhir.model.type.String)
         */
        public Builder version(java.lang.String version) {
            this.version = (version == null) ? null : String.of(version);
            return this;
        }

        /**
         * The identifier that is used to identify this version of the research element definition when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the research element definition author 
         * and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version 
         * is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
         * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
         * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
         * version is required for non-experimental active artifacts.
         * 
         * @param version
         *     Business version of the research element definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     Name for this research element definition (computer friendly)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #name(com.ibm.fhir.model.type.String)
         */
        public Builder name(java.lang.String name) {
            this.name = (name == null) ? null : String.of(name);
            return this;
        }

        /**
         * A natural language name identifying the research element definition. This name should be usable as an identifier for 
         * the module by machine processing applications such as code generation.
         * 
         * @param name
         *     Name for this research element definition (computer friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Name for this research element definition (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #title(com.ibm.fhir.model.type.String)
         */
        public Builder title(java.lang.String title) {
            this.title = (title == null) ? null : String.of(title);
            return this;
        }

        /**
         * A short, descriptive, user-friendly title for the research element definition.
         * 
         * @param title
         *     Name for this research element definition (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Convenience method for setting {@code shortTitle}.
         * 
         * @param shortTitle
         *     Title for use in informal contexts
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #shortTitle(com.ibm.fhir.model.type.String)
         */
        public Builder shortTitle(java.lang.String shortTitle) {
            this.shortTitle = (shortTitle == null) ? null : String.of(shortTitle);
            return this;
        }

        /**
         * The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is 
         * not necessary.
         * 
         * @param shortTitle
         *     Title for use in informal contexts
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder shortTitle(String shortTitle) {
            this.shortTitle = shortTitle;
            return this;
        }

        /**
         * Convenience method for setting {@code subtitle}.
         * 
         * @param subtitle
         *     Subordinate title of the ResearchElementDefinition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #subtitle(com.ibm.fhir.model.type.String)
         */
        public Builder subtitle(java.lang.String subtitle) {
            this.subtitle = (subtitle == null) ? null : String.of(subtitle);
            return this;
        }

        /**
         * An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.
         * 
         * @param subtitle
         *     Subordinate title of the ResearchElementDefinition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        /**
         * The status of this research element definition. Enables tracking the life-cycle of the content.
         * 
         * <p>This element is required.
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
         * Convenience method for setting {@code experimental}.
         * 
         * @param experimental
         *     For testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #experimental(com.ibm.fhir.model.type.Boolean)
         */
        public Builder experimental(java.lang.Boolean experimental) {
            this.experimental = (experimental == null) ? null : Boolean.of(experimental);
            return this;
        }

        /**
         * A Boolean value to indicate that this research element definition is authored for testing purposes (or 
         * education/evaluation/marketing) and is not intended to be used for genuine usage.
         * 
         * @param experimental
         *     For testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder experimental(Boolean experimental) {
            this.experimental = experimental;
            return this;
        }

        /**
         * The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is 
         * assumed, but the subject of the ResearchElementDefinition can be anything.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Reference}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param subject
         *     E.g. Patient, Practitioner, RelatedPerson, Organization, Location, Device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Element subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The date (and optionally time) when the research element definition was published. The date must change when the 
         * business version changes and it must change if the status code changes. In addition, it should change when the 
         * substantive content of the research element definition changes.
         * 
         * @param date
         *     Date last changed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * Convenience method for setting {@code publisher}.
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #publisher(com.ibm.fhir.model.type.String)
         */
        public Builder publisher(java.lang.String publisher) {
            this.publisher = (publisher == null) ? null : String.of(publisher);
            return this;
        }

        /**
         * The name of the organization or individual that published the research element definition.
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        /**
         * Contact details to assist a user in finding and communicating with the publisher.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(ContactDetail... contact) {
            for (ContactDetail value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * Contact details to assist a user in finding and communicating with the publisher.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * A free text natural language description of the research element definition from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the research element definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * Convenience method for setting {@code comment}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param comment
         *     Used for footnotes or explanatory notes
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #comment(com.ibm.fhir.model.type.String)
         */
        public Builder comment(java.lang.String... comment) {
            for (java.lang.String value : comment) {
                this.comment.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * A human-readable string to clarify or explain concepts about the resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param comment
         *     Used for footnotes or explanatory notes
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comment(String... comment) {
            for (String value : comment) {
                this.comment.add(value);
            }
            return this;
        }

        /**
         * A human-readable string to clarify or explain concepts about the resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param comment
         *     Used for footnotes or explanatory notes
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder comment(Collection<String> comment) {
            this.comment = new ArrayList<>(comment);
            return this;
        }

        /**
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate research element definition instances.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder useContext(UsageContext... useContext) {
            for (UsageContext value : useContext) {
                this.useContext.add(value);
            }
            return this;
        }

        /**
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate research element definition instances.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext = new ArrayList<>(useContext);
            return this;
        }

        /**
         * A legal or geographic region in which the research element definition is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for research element definition (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdiction(CodeableConcept... jurisdiction) {
            for (CodeableConcept value : jurisdiction) {
                this.jurisdiction.add(value);
            }
            return this;
        }

        /**
         * A legal or geographic region in which the research element definition is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for research element definition (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction = new ArrayList<>(jurisdiction);
            return this;
        }

        /**
         * Explanation of why this research element definition is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this research element definition is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * Convenience method for setting {@code usage}.
         * 
         * @param usage
         *     Describes the clinical usage of the ResearchElementDefinition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #usage(com.ibm.fhir.model.type.String)
         */
        public Builder usage(java.lang.String usage) {
            this.usage = (usage == null) ? null : String.of(usage);
            return this;
        }

        /**
         * A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.
         * 
         * @param usage
         *     Describes the clinical usage of the ResearchElementDefinition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder usage(String usage) {
            this.usage = usage;
            return this;
        }

        /**
         * A copyright statement relating to the research element definition and/or its contents. Copyright statements are 
         * generally legal restrictions on the use and publishing of the research element definition.
         * 
         * @param copyright
         *     Use and/or publishing restrictions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder copyright(Markdown copyright) {
            this.copyright = copyright;
            return this;
        }

        /**
         * Convenience method for setting {@code approvalDate}.
         * 
         * @param approvalDate
         *     When the research element definition was approved by publisher
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #approvalDate(com.ibm.fhir.model.type.Date)
         */
        public Builder approvalDate(java.time.LocalDate approvalDate) {
            this.approvalDate = (approvalDate == null) ? null : Date.of(approvalDate);
            return this;
        }

        /**
         * The date on which the resource content was approved by the publisher. Approval happens once when the content is 
         * officially approved for usage.
         * 
         * @param approvalDate
         *     When the research element definition was approved by publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder approvalDate(Date approvalDate) {
            this.approvalDate = approvalDate;
            return this;
        }

        /**
         * Convenience method for setting {@code lastReviewDate}.
         * 
         * @param lastReviewDate
         *     When the research element definition was last reviewed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #lastReviewDate(com.ibm.fhir.model.type.Date)
         */
        public Builder lastReviewDate(java.time.LocalDate lastReviewDate) {
            this.lastReviewDate = (lastReviewDate == null) ? null : Date.of(lastReviewDate);
            return this;
        }

        /**
         * The date on which the resource content was last reviewed. Review happens periodically after approval but does not 
         * change the original approval date.
         * 
         * @param lastReviewDate
         *     When the research element definition was last reviewed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
            return this;
        }

        /**
         * The period during which the research element definition content was or is planned to be in active use.
         * 
         * @param effectivePeriod
         *     When the research element definition is expected to be used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effectivePeriod(Period effectivePeriod) {
            this.effectivePeriod = effectivePeriod;
            return this;
        }

        /**
         * Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization 
         * grouping types of ResearchElementDefinitions that can be useful for filtering and searching.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param topic
         *     The category of the ResearchElementDefinition, such as Education, Treatment, Assessment, etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder topic(CodeableConcept... topic) {
            for (CodeableConcept value : topic) {
                this.topic.add(value);
            }
            return this;
        }

        /**
         * Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization 
         * grouping types of ResearchElementDefinitions that can be useful for filtering and searching.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param topic
         *     The category of the ResearchElementDefinition, such as Education, Treatment, Assessment, etc.
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder topic(Collection<CodeableConcept> topic) {
            this.topic = new ArrayList<>(topic);
            return this;
        }

        /**
         * An individiual or organization primarily involved in the creation and maintenance of the content.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(ContactDetail... author) {
            for (ContactDetail value : author) {
                this.author.add(value);
            }
            return this;
        }

        /**
         * An individiual or organization primarily involved in the creation and maintenance of the content.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder author(Collection<ContactDetail> author) {
            this.author = new ArrayList<>(author);
            return this;
        }

        /**
         * An individual or organization primarily responsible for internal coherence of the content.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder editor(ContactDetail... editor) {
            for (ContactDetail value : editor) {
                this.editor.add(value);
            }
            return this;
        }

        /**
         * An individual or organization primarily responsible for internal coherence of the content.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder editor(Collection<ContactDetail> editor) {
            this.editor = new ArrayList<>(editor);
            return this;
        }

        /**
         * An individual or organization primarily responsible for review of some aspect of the content.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reviewer(ContactDetail... reviewer) {
            for (ContactDetail value : reviewer) {
                this.reviewer.add(value);
            }
            return this;
        }

        /**
         * An individual or organization primarily responsible for review of some aspect of the content.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reviewer(Collection<ContactDetail> reviewer) {
            this.reviewer = new ArrayList<>(reviewer);
            return this;
        }

        /**
         * An individual or organization responsible for officially endorsing the content for use in some setting.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder endorser(ContactDetail... endorser) {
            for (ContactDetail value : endorser) {
                this.endorser.add(value);
            }
            return this;
        }

        /**
         * An individual or organization responsible for officially endorsing the content for use in some setting.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder endorser(Collection<ContactDetail> endorser) {
            this.endorser = new ArrayList<>(endorser);
            return this;
        }

        /**
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     Additional documentation, citations, etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatedArtifact(RelatedArtifact... relatedArtifact) {
            for (RelatedArtifact value : relatedArtifact) {
                this.relatedArtifact.add(value);
            }
            return this;
        }

        /**
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     Additional documentation, citations, etc.
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder relatedArtifact(Collection<RelatedArtifact> relatedArtifact) {
            this.relatedArtifact = new ArrayList<>(relatedArtifact);
            return this;
        }

        /**
         * A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param library
         *     Logic used by the ResearchElementDefinition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder library(Canonical... library) {
            for (Canonical value : library) {
                this.library.add(value);
            }
            return this;
        }

        /**
         * A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param library
         *     Logic used by the ResearchElementDefinition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder library(Collection<Canonical> library) {
            this.library = new ArrayList<>(library);
            return this;
        }

        /**
         * The type of research element, a population, an exposure, or an outcome.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     population | exposure | outcome
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(ResearchElementType type) {
            this.type = type;
            return this;
        }

        /**
         * The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).
         * 
         * @param variableType
         *     dichotomous | continuous | descriptive
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder variableType(VariableType variableType) {
            this.variableType = variableType;
            return this;
        }

        /**
         * A characteristic that defines the members of the research element. Multiple characteristics are applied with "and" 
         * semantics.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param characteristic
         *     What defines the members of the research element
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder characteristic(Characteristic... characteristic) {
            for (Characteristic value : characteristic) {
                this.characteristic.add(value);
            }
            return this;
        }

        /**
         * A characteristic that defines the members of the research element. Multiple characteristics are applied with "and" 
         * semantics.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param characteristic
         *     What defines the members of the research element
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder characteristic(Collection<Characteristic> characteristic) {
            this.characteristic = new ArrayList<>(characteristic);
            return this;
        }

        /**
         * Build the {@link ResearchElementDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>type</li>
         * <li>characteristic</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ResearchElementDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ResearchElementDefinition per the base specification
         */
        @Override
        public ResearchElementDefinition build() {
            ResearchElementDefinition researchElementDefinition = new ResearchElementDefinition(this);
            if (validating) {
                validate(researchElementDefinition);
            }
            return researchElementDefinition;
        }

        protected void validate(ResearchElementDefinition researchElementDefinition) {
            super.validate(researchElementDefinition);
            ValidationSupport.checkList(researchElementDefinition.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(researchElementDefinition.status, "status");
            ValidationSupport.choiceElement(researchElementDefinition.subject, "subject", CodeableConcept.class, Reference.class);
            ValidationSupport.checkList(researchElementDefinition.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(researchElementDefinition.comment, "comment", String.class);
            ValidationSupport.checkList(researchElementDefinition.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(researchElementDefinition.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(researchElementDefinition.topic, "topic", CodeableConcept.class);
            ValidationSupport.checkList(researchElementDefinition.author, "author", ContactDetail.class);
            ValidationSupport.checkList(researchElementDefinition.editor, "editor", ContactDetail.class);
            ValidationSupport.checkList(researchElementDefinition.reviewer, "reviewer", ContactDetail.class);
            ValidationSupport.checkList(researchElementDefinition.endorser, "endorser", ContactDetail.class);
            ValidationSupport.checkList(researchElementDefinition.relatedArtifact, "relatedArtifact", RelatedArtifact.class);
            ValidationSupport.checkList(researchElementDefinition.library, "library", Canonical.class);
            ValidationSupport.requireNonNull(researchElementDefinition.type, "type");
            ValidationSupport.checkNonEmptyList(researchElementDefinition.characteristic, "characteristic", Characteristic.class);
            ValidationSupport.checkReferenceType(researchElementDefinition.subject, "subject", "Group");
        }

        protected Builder from(ResearchElementDefinition researchElementDefinition) {
            super.from(researchElementDefinition);
            url = researchElementDefinition.url;
            identifier.addAll(researchElementDefinition.identifier);
            version = researchElementDefinition.version;
            name = researchElementDefinition.name;
            title = researchElementDefinition.title;
            shortTitle = researchElementDefinition.shortTitle;
            subtitle = researchElementDefinition.subtitle;
            status = researchElementDefinition.status;
            experimental = researchElementDefinition.experimental;
            subject = researchElementDefinition.subject;
            date = researchElementDefinition.date;
            publisher = researchElementDefinition.publisher;
            contact.addAll(researchElementDefinition.contact);
            description = researchElementDefinition.description;
            comment.addAll(researchElementDefinition.comment);
            useContext.addAll(researchElementDefinition.useContext);
            jurisdiction.addAll(researchElementDefinition.jurisdiction);
            purpose = researchElementDefinition.purpose;
            usage = researchElementDefinition.usage;
            copyright = researchElementDefinition.copyright;
            approvalDate = researchElementDefinition.approvalDate;
            lastReviewDate = researchElementDefinition.lastReviewDate;
            effectivePeriod = researchElementDefinition.effectivePeriod;
            topic.addAll(researchElementDefinition.topic);
            author.addAll(researchElementDefinition.author);
            editor.addAll(researchElementDefinition.editor);
            reviewer.addAll(researchElementDefinition.reviewer);
            endorser.addAll(researchElementDefinition.endorser);
            relatedArtifact.addAll(researchElementDefinition.relatedArtifact);
            library.addAll(researchElementDefinition.library);
            type = researchElementDefinition.type;
            variableType = researchElementDefinition.variableType;
            characteristic.addAll(researchElementDefinition.characteristic);
            return this;
        }
    }

    /**
     * A characteristic that defines the members of the research element. Multiple characteristics are applied with "and" 
     * semantics.
     */
    public static class Characteristic extends BackboneElement {
        @Summary
        @Choice({ CodeableConcept.class, Canonical.class, Expression.class, DataRequirement.class })
        @Required
        private final Element definition;
        private final List<UsageContext> usageContext;
        private final Boolean exclude;
        @Binding(
            bindingName = "UCUMUnits",
            strength = BindingStrength.Value.REQUIRED,
            description = "Unified Code for Units of Measure (UCUM).",
            valueSet = "http://hl7.org/fhir/ValueSet/ucum-units|4.3.0-cibuild"
        )
        private final CodeableConcept unitOfMeasure;
        private final String studyEffectiveDescription;
        @Choice({ DateTime.class, Period.class, Duration.class, Timing.class })
        private final Element studyEffective;
        private final Duration studyEffectiveTimeFromStart;
        @Binding(
            bindingName = "GroupMeasure",
            strength = BindingStrength.Value.REQUIRED,
            description = "Possible group measure aggregates (E.g. Mean, Median).",
            valueSet = "http://hl7.org/fhir/ValueSet/group-measure|4.3.0-cibuild"
        )
        private final GroupMeasure studyEffectiveGroupMeasure;
        private final String participantEffectiveDescription;
        @Choice({ DateTime.class, Period.class, Duration.class, Timing.class })
        private final Element participantEffective;
        private final Duration participantEffectiveTimeFromStart;
        @Binding(
            bindingName = "GroupMeasure",
            strength = BindingStrength.Value.REQUIRED,
            description = "Possible group measure aggregates (E.g. Mean, Median).",
            valueSet = "http://hl7.org/fhir/ValueSet/group-measure|4.3.0-cibuild"
        )
        private final GroupMeasure participantEffectiveGroupMeasure;

        private Characteristic(Builder builder) {
            super(builder);
            definition = builder.definition;
            usageContext = Collections.unmodifiableList(builder.usageContext);
            exclude = builder.exclude;
            unitOfMeasure = builder.unitOfMeasure;
            studyEffectiveDescription = builder.studyEffectiveDescription;
            studyEffective = builder.studyEffective;
            studyEffectiveTimeFromStart = builder.studyEffectiveTimeFromStart;
            studyEffectiveGroupMeasure = builder.studyEffectiveGroupMeasure;
            participantEffectiveDescription = builder.participantEffectiveDescription;
            participantEffective = builder.participantEffective;
            participantEffectiveTimeFromStart = builder.participantEffectiveTimeFromStart;
            participantEffectiveGroupMeasure = builder.participantEffectiveGroupMeasure;
        }

        /**
         * Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( 
         * using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the 
         * last year).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}, {@link Canonical}, {@link Expression} or {@link DataRequirement} 
         *     that is non-null.
         */
        public Element getDefinition() {
            return definition;
        }

        /**
         * Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
         */
        public List<UsageContext> getUsageContext() {
            return usageContext;
        }

        /**
         * When true, members with this characteristic are excluded from the element.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getExclude() {
            return exclude;
        }

        /**
         * Specifies the UCUM unit for the outcome.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getUnitOfMeasure() {
            return unitOfMeasure;
        }

        /**
         * A narrative description of the time period the study covers.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getStudyEffectiveDescription() {
            return studyEffectiveDescription;
        }

        /**
         * Indicates what effective period the study covers.
         * 
         * @return
         *     An immutable object of type {@link DateTime}, {@link Period}, {@link Duration} or {@link Timing} that may be null.
         */
        public Element getStudyEffective() {
            return studyEffective;
        }

        /**
         * Indicates duration from the study initiation.
         * 
         * @return
         *     An immutable object of type {@link Duration} that may be null.
         */
        public Duration getStudyEffectiveTimeFromStart() {
            return studyEffectiveTimeFromStart;
        }

        /**
         * Indicates how elements are aggregated within the study effective period.
         * 
         * @return
         *     An immutable object of type {@link GroupMeasure} that may be null.
         */
        public GroupMeasure getStudyEffectiveGroupMeasure() {
            return studyEffectiveGroupMeasure;
        }

        /**
         * A narrative description of the time period the study covers.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getParticipantEffectiveDescription() {
            return participantEffectiveDescription;
        }

        /**
         * Indicates what effective period the study covers.
         * 
         * @return
         *     An immutable object of type {@link DateTime}, {@link Period}, {@link Duration} or {@link Timing} that may be null.
         */
        public Element getParticipantEffective() {
            return participantEffective;
        }

        /**
         * Indicates duration from the participant's study entry.
         * 
         * @return
         *     An immutable object of type {@link Duration} that may be null.
         */
        public Duration getParticipantEffectiveTimeFromStart() {
            return participantEffectiveTimeFromStart;
        }

        /**
         * Indicates how elements are aggregated within the study effective period.
         * 
         * @return
         *     An immutable object of type {@link GroupMeasure} that may be null.
         */
        public GroupMeasure getParticipantEffectiveGroupMeasure() {
            return participantEffectiveGroupMeasure;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (definition != null) || 
                !usageContext.isEmpty() || 
                (exclude != null) || 
                (unitOfMeasure != null) || 
                (studyEffectiveDescription != null) || 
                (studyEffective != null) || 
                (studyEffectiveTimeFromStart != null) || 
                (studyEffectiveGroupMeasure != null) || 
                (participantEffectiveDescription != null) || 
                (participantEffective != null) || 
                (participantEffectiveTimeFromStart != null) || 
                (participantEffectiveGroupMeasure != null);
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
                    accept(definition, "definition", visitor);
                    accept(usageContext, "usageContext", visitor, UsageContext.class);
                    accept(exclude, "exclude", visitor);
                    accept(unitOfMeasure, "unitOfMeasure", visitor);
                    accept(studyEffectiveDescription, "studyEffectiveDescription", visitor);
                    accept(studyEffective, "studyEffective", visitor);
                    accept(studyEffectiveTimeFromStart, "studyEffectiveTimeFromStart", visitor);
                    accept(studyEffectiveGroupMeasure, "studyEffectiveGroupMeasure", visitor);
                    accept(participantEffectiveDescription, "participantEffectiveDescription", visitor);
                    accept(participantEffective, "participantEffective", visitor);
                    accept(participantEffectiveTimeFromStart, "participantEffectiveTimeFromStart", visitor);
                    accept(participantEffectiveGroupMeasure, "participantEffectiveGroupMeasure", visitor);
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
            Characteristic other = (Characteristic) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(definition, other.definition) && 
                Objects.equals(usageContext, other.usageContext) && 
                Objects.equals(exclude, other.exclude) && 
                Objects.equals(unitOfMeasure, other.unitOfMeasure) && 
                Objects.equals(studyEffectiveDescription, other.studyEffectiveDescription) && 
                Objects.equals(studyEffective, other.studyEffective) && 
                Objects.equals(studyEffectiveTimeFromStart, other.studyEffectiveTimeFromStart) && 
                Objects.equals(studyEffectiveGroupMeasure, other.studyEffectiveGroupMeasure) && 
                Objects.equals(participantEffectiveDescription, other.participantEffectiveDescription) && 
                Objects.equals(participantEffective, other.participantEffective) && 
                Objects.equals(participantEffectiveTimeFromStart, other.participantEffectiveTimeFromStart) && 
                Objects.equals(participantEffectiveGroupMeasure, other.participantEffectiveGroupMeasure);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    definition, 
                    usageContext, 
                    exclude, 
                    unitOfMeasure, 
                    studyEffectiveDescription, 
                    studyEffective, 
                    studyEffectiveTimeFromStart, 
                    studyEffectiveGroupMeasure, 
                    participantEffectiveDescription, 
                    participantEffective, 
                    participantEffectiveTimeFromStart, 
                    participantEffectiveGroupMeasure);
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
            private Element definition;
            private List<UsageContext> usageContext = new ArrayList<>();
            private Boolean exclude;
            private CodeableConcept unitOfMeasure;
            private String studyEffectiveDescription;
            private Element studyEffective;
            private Duration studyEffectiveTimeFromStart;
            private GroupMeasure studyEffectiveGroupMeasure;
            private String participantEffectiveDescription;
            private Element participantEffective;
            private Duration participantEffectiveTimeFromStart;
            private GroupMeasure participantEffectiveGroupMeasure;

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
             * Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( 
             * using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the 
             * last year).
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Canonical}</li>
             * <li>{@link Expression}</li>
             * <li>{@link DataRequirement}</li>
             * </ul>
             * 
             * @param definition
             *     What code or expression defines members?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder definition(Element definition) {
                this.definition = definition;
                return this;
            }

            /**
             * Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param usageContext
             *     What code/value pairs define members?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder usageContext(UsageContext... usageContext) {
                for (UsageContext value : usageContext) {
                    this.usageContext.add(value);
                }
                return this;
            }

            /**
             * Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param usageContext
             *     What code/value pairs define members?
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder usageContext(Collection<UsageContext> usageContext) {
                this.usageContext = new ArrayList<>(usageContext);
                return this;
            }

            /**
             * Convenience method for setting {@code exclude}.
             * 
             * @param exclude
             *     Whether the characteristic includes or excludes members
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #exclude(com.ibm.fhir.model.type.Boolean)
             */
            public Builder exclude(java.lang.Boolean exclude) {
                this.exclude = (exclude == null) ? null : Boolean.of(exclude);
                return this;
            }

            /**
             * When true, members with this characteristic are excluded from the element.
             * 
             * @param exclude
             *     Whether the characteristic includes or excludes members
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder exclude(Boolean exclude) {
                this.exclude = exclude;
                return this;
            }

            /**
             * Specifies the UCUM unit for the outcome.
             * 
             * @param unitOfMeasure
             *     What unit is the outcome described in?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder unitOfMeasure(CodeableConcept unitOfMeasure) {
                this.unitOfMeasure = unitOfMeasure;
                return this;
            }

            /**
             * Convenience method for setting {@code studyEffectiveDescription}.
             * 
             * @param studyEffectiveDescription
             *     What time period does the study cover
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #studyEffectiveDescription(com.ibm.fhir.model.type.String)
             */
            public Builder studyEffectiveDescription(java.lang.String studyEffectiveDescription) {
                this.studyEffectiveDescription = (studyEffectiveDescription == null) ? null : String.of(studyEffectiveDescription);
                return this;
            }

            /**
             * A narrative description of the time period the study covers.
             * 
             * @param studyEffectiveDescription
             *     What time period does the study cover
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder studyEffectiveDescription(String studyEffectiveDescription) {
                this.studyEffectiveDescription = studyEffectiveDescription;
                return this;
            }

            /**
             * Indicates what effective period the study covers.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link DateTime}</li>
             * <li>{@link Period}</li>
             * <li>{@link Duration}</li>
             * <li>{@link Timing}</li>
             * </ul>
             * 
             * @param studyEffective
             *     What time period does the study cover
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder studyEffective(Element studyEffective) {
                this.studyEffective = studyEffective;
                return this;
            }

            /**
             * Indicates duration from the study initiation.
             * 
             * @param studyEffectiveTimeFromStart
             *     Observation time from study start
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder studyEffectiveTimeFromStart(Duration studyEffectiveTimeFromStart) {
                this.studyEffectiveTimeFromStart = studyEffectiveTimeFromStart;
                return this;
            }

            /**
             * Indicates how elements are aggregated within the study effective period.
             * 
             * @param studyEffectiveGroupMeasure
             *     mean | median | mean-of-mean | mean-of-median | median-of-mean | median-of-median
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder studyEffectiveGroupMeasure(GroupMeasure studyEffectiveGroupMeasure) {
                this.studyEffectiveGroupMeasure = studyEffectiveGroupMeasure;
                return this;
            }

            /**
             * Convenience method for setting {@code participantEffectiveDescription}.
             * 
             * @param participantEffectiveDescription
             *     What time period do participants cover
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #participantEffectiveDescription(com.ibm.fhir.model.type.String)
             */
            public Builder participantEffectiveDescription(java.lang.String participantEffectiveDescription) {
                this.participantEffectiveDescription = (participantEffectiveDescription == null) ? null : String.of(participantEffectiveDescription);
                return this;
            }

            /**
             * A narrative description of the time period the study covers.
             * 
             * @param participantEffectiveDescription
             *     What time period do participants cover
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder participantEffectiveDescription(String participantEffectiveDescription) {
                this.participantEffectiveDescription = participantEffectiveDescription;
                return this;
            }

            /**
             * Indicates what effective period the study covers.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link DateTime}</li>
             * <li>{@link Period}</li>
             * <li>{@link Duration}</li>
             * <li>{@link Timing}</li>
             * </ul>
             * 
             * @param participantEffective
             *     What time period do participants cover
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder participantEffective(Element participantEffective) {
                this.participantEffective = participantEffective;
                return this;
            }

            /**
             * Indicates duration from the participant's study entry.
             * 
             * @param participantEffectiveTimeFromStart
             *     Observation time from study start
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder participantEffectiveTimeFromStart(Duration participantEffectiveTimeFromStart) {
                this.participantEffectiveTimeFromStart = participantEffectiveTimeFromStart;
                return this;
            }

            /**
             * Indicates how elements are aggregated within the study effective period.
             * 
             * @param participantEffectiveGroupMeasure
             *     mean | median | mean-of-mean | mean-of-median | median-of-mean | median-of-median
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder participantEffectiveGroupMeasure(GroupMeasure participantEffectiveGroupMeasure) {
                this.participantEffectiveGroupMeasure = participantEffectiveGroupMeasure;
                return this;
            }

            /**
             * Build the {@link Characteristic}
             * 
             * <p>Required elements:
             * <ul>
             * <li>definition</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Characteristic}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Characteristic per the base specification
             */
            @Override
            public Characteristic build() {
                Characteristic characteristic = new Characteristic(this);
                if (validating) {
                    validate(characteristic);
                }
                return characteristic;
            }

            protected void validate(Characteristic characteristic) {
                super.validate(characteristic);
                ValidationSupport.requireChoiceElement(characteristic.definition, "definition", CodeableConcept.class, Canonical.class, Expression.class, DataRequirement.class);
                ValidationSupport.checkList(characteristic.usageContext, "usageContext", UsageContext.class);
                ValidationSupport.choiceElement(characteristic.studyEffective, "studyEffective", DateTime.class, Period.class, Duration.class, Timing.class);
                ValidationSupport.choiceElement(characteristic.participantEffective, "participantEffective", DateTime.class, Period.class, Duration.class, Timing.class);
                ValidationSupport.checkValueSetBinding(characteristic.unitOfMeasure, "unitOfMeasure", "http://hl7.org/fhir/ValueSet/ucum-units", "http://unitsofmeasure.org");
                ValidationSupport.requireValueOrChildren(characteristic);
            }

            protected Builder from(Characteristic characteristic) {
                super.from(characteristic);
                definition = characteristic.definition;
                usageContext.addAll(characteristic.usageContext);
                exclude = characteristic.exclude;
                unitOfMeasure = characteristic.unitOfMeasure;
                studyEffectiveDescription = characteristic.studyEffectiveDescription;
                studyEffective = characteristic.studyEffective;
                studyEffectiveTimeFromStart = characteristic.studyEffectiveTimeFromStart;
                studyEffectiveGroupMeasure = characteristic.studyEffectiveGroupMeasure;
                participantEffectiveDescription = characteristic.participantEffectiveDescription;
                participantEffective = characteristic.participantEffective;
                participantEffectiveTimeFromStart = characteristic.participantEffectiveTimeFromStart;
                participantEffectiveGroupMeasure = characteristic.participantEffectiveGroupMeasure;
                return this;
            }
        }
    }
}
