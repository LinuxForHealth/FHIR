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
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.DataRequirement;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.ParameterDefinition;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and 
 * expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a 
 * collection of knowledge assets.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "cnl-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.exists() implies name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/Library"
)
@Constraint(
    id = "library-1",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/library-type",
    expression = "type.exists() and type.memberOf('http://hl7.org/fhir/ValueSet/library-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Library",
    generated = true
)
@Constraint(
    id = "library-2",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/subject-type",
    expression = "subject.as(CodeableConcept).exists() implies (subject.as(CodeableConcept).memberOf('http://hl7.org/fhir/ValueSet/subject-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Library",
    generated = true
)
@Constraint(
    id = "library-3",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/Library",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Library extends DomainResource {
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
    @Summary
    @Binding(
        bindingName = "LibraryType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The type of knowledge asset this library contains.",
        valueSet = "http://hl7.org/fhir/ValueSet/library-type"
    )
    @Required
    private final CodeableConcept type;
    @ReferenceTarget({ "Group" })
    @Choice({ CodeableConcept.class, Reference.class })
    @Binding(
        bindingName = "SubjectType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The possible types of subjects for a library (E.g. Patient, Practitioner, Organization, Location, etc.).",
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
    private final List<ParameterDefinition> parameter;
    private final List<DataRequirement> dataRequirement;
    @Summary
    private final List<Attachment> content;

    private Library(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = builder.name;
        title = builder.title;
        subtitle = builder.subtitle;
        status = builder.status;
        experimental = builder.experimental;
        type = builder.type;
        subject = builder.subject;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
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
        parameter = Collections.unmodifiableList(builder.parameter);
        dataRequirement = Collections.unmodifiableList(builder.dataRequirement);
        content = Collections.unmodifiableList(builder.content);
    }

    /**
     * An absolute URI that is used to identify this library when it is referenced in a specification, model, design or an 
     * instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this library is (or will be) published. This URL can be the target of a 
     * canonical reference. It SHALL remain the same when the library is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this library when it is represented in other formats, or referenced in a 
     * specification, model, design or an instance. e.g. CMS or NQF identifiers for a measure artifact. Note that at least 
     * one identifier is required for non-experimental active artifacts.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the library when it is referenced in a specification, model, 
     * design or instance. This is an arbitrary value managed by the library author and is not expected to be globally 
     * unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no 
     * expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the 
     * Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on 
     * versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for 
     * non-experimental active artifacts.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the library. This name should be usable as an identifier for the module by machine 
     * processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the library.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * An explanatory or alternate title for the library giving additional information about its content.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * The status of this library. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this library is authored for testing purposes (or education/evaluation/marketing) and 
     * is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * Identifies the type of library such as a Logic Library, Model Definition, Asset Collection, or Module Definition.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * A code or group definition that describes the intended subject of the contents of the library.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} or {@link Reference} that may be null.
     */
    public Element getSubject() {
        return subject;
    }

    /**
     * The date (and optionally time) when the library was published. The date must change when the business version changes 
     * and it must change if the status code changes. In addition, it should change when the substantive content of the 
     * library changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the library.
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
     * A free text natural language description of the library from a consumer's perspective.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate library instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the library is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this library is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A detailed description of how the library is used from a clinical perspective.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * A copyright statement relating to the library and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the library.
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
     * The period during which the library content was or is planned to be in active use.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getEffectivePeriod() {
        return effectivePeriod;
    }

    /**
     * Descriptive topics related to the content of the library. Topics provide a high-level categorization of the library 
     * that can be useful for filtering and searching.
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
     * The parameter element defines parameters used by the library.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ParameterDefinition} that may be empty.
     */
    public List<ParameterDefinition> getParameter() {
        return parameter;
    }

    /**
     * Describes a set of data that must be provided in order to be able to successfully perform the computations defined by 
     * the library.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DataRequirement} that may be empty.
     */
    public List<DataRequirement> getDataRequirement() {
        return dataRequirement;
    }

    /**
     * The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a 
     * base-64 string. Either way, the contentType of the attachment determines how to interpret the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Attachment} that may be empty.
     */
    public List<Attachment> getContent() {
        return content;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            !identifier.isEmpty() || 
            (version != null) || 
            (name != null) || 
            (title != null) || 
            (subtitle != null) || 
            (status != null) || 
            (experimental != null) || 
            (type != null) || 
            (subject != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (description != null) || 
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
            !parameter.isEmpty() || 
            !dataRequirement.isEmpty() || 
            !content.isEmpty();
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
                accept(subtitle, "subtitle", visitor);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(type, "type", visitor);
                accept(subject, "subject", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
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
                accept(parameter, "parameter", visitor, ParameterDefinition.class);
                accept(dataRequirement, "dataRequirement", visitor, DataRequirement.class);
                accept(content, "content", visitor, Attachment.class);
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
        Library other = (Library) obj;
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
            Objects.equals(subtitle, other.subtitle) && 
            Objects.equals(status, other.status) && 
            Objects.equals(experimental, other.experimental) && 
            Objects.equals(type, other.type) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(description, other.description) && 
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
            Objects.equals(parameter, other.parameter) && 
            Objects.equals(dataRequirement, other.dataRequirement) && 
            Objects.equals(content, other.content);
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
                subtitle, 
                status, 
                experimental, 
                type, 
                subject, 
                date, 
                publisher, 
                contact, 
                description, 
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
                parameter, 
                dataRequirement, 
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

    public static class Builder extends DomainResource.Builder {
        private Uri url;
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String name;
        private String title;
        private String subtitle;
        private PublicationStatus status;
        private Boolean experimental;
        private CodeableConcept type;
        private Element subject;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
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
        private List<ParameterDefinition> parameter = new ArrayList<>();
        private List<DataRequirement> dataRequirement = new ArrayList<>();
        private List<Attachment> content = new ArrayList<>();

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
         * An absolute URI that is used to identify this library when it is referenced in a specification, model, design or an 
         * instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this library is (or will be) published. This URL can be the target of a 
         * canonical reference. It SHALL remain the same when the library is stored on different servers.
         * 
         * @param url
         *     Canonical identifier for this library, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this library when it is represented in other formats, or referenced in a 
         * specification, model, design or an instance. e.g. CMS or NQF identifiers for a measure artifact. Note that at least 
         * one identifier is required for non-experimental active artifacts.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the library
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
         * A formal identifier that is used to identify this library when it is represented in other formats, or referenced in a 
         * specification, model, design or an instance. e.g. CMS or NQF identifiers for a measure artifact. Note that at least 
         * one identifier is required for non-experimental active artifacts.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the library
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
         *     Business version of the library
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
         * The identifier that is used to identify this version of the library when it is referenced in a specification, model, 
         * design or instance. This is an arbitrary value managed by the library author and is not expected to be globally 
         * unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no 
         * expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the 
         * Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on 
         * versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for 
         * non-experimental active artifacts.
         * 
         * @param version
         *     Business version of the library
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
         *     Name for this library (computer friendly)
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
         * A natural language name identifying the library. This name should be usable as an identifier for the module by machine 
         * processing applications such as code generation.
         * 
         * @param name
         *     Name for this library (computer friendly)
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
         *     Name for this library (human friendly)
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
         * A short, descriptive, user-friendly title for the library.
         * 
         * @param title
         *     Name for this library (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Convenience method for setting {@code subtitle}.
         * 
         * @param subtitle
         *     Subordinate title of the library
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
         * An explanatory or alternate title for the library giving additional information about its content.
         * 
         * @param subtitle
         *     Subordinate title of the library
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        /**
         * The status of this library. Enables tracking the life-cycle of the content.
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
         * A Boolean value to indicate that this library is authored for testing purposes (or education/evaluation/marketing) and 
         * is not intended to be used for genuine usage.
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
         * Identifies the type of library such as a Logic Library, Model Definition, Asset Collection, or Module Definition.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     logic-library | model-definition | asset-collection | module-definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * A code or group definition that describes the intended subject of the contents of the library.
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
         *     Type of individual the library content is focused on
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Element subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The date (and optionally time) when the library was published. The date must change when the business version changes 
         * and it must change if the status code changes. In addition, it should change when the substantive content of the 
         * library changes.
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
         * The name of the organization or individual that published the library.
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
         * A free text natural language description of the library from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the library
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate library instances.
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
         * may be used to assist with indexing and searching for appropriate library instances.
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
         * A legal or geographic region in which the library is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for library (if applicable)
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
         * A legal or geographic region in which the library is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for library (if applicable)
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
         * Explanation of why this library is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this library is defined
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
         *     Describes the clinical usage of the library
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
         * A detailed description of how the library is used from a clinical perspective.
         * 
         * @param usage
         *     Describes the clinical usage of the library
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder usage(String usage) {
            this.usage = usage;
            return this;
        }

        /**
         * A copyright statement relating to the library and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the library.
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
         *     When the library was approved by publisher
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
         *     When the library was approved by publisher
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
         *     When the library was last reviewed
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
         *     When the library was last reviewed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
            return this;
        }

        /**
         * The period during which the library content was or is planned to be in active use.
         * 
         * @param effectivePeriod
         *     When the library is expected to be used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effectivePeriod(Period effectivePeriod) {
            this.effectivePeriod = effectivePeriod;
            return this;
        }

        /**
         * Descriptive topics related to the content of the library. Topics provide a high-level categorization of the library 
         * that can be useful for filtering and searching.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param topic
         *     E.g. Education, Treatment, Assessment, etc.
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
         * Descriptive topics related to the content of the library. Topics provide a high-level categorization of the library 
         * that can be useful for filtering and searching.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param topic
         *     E.g. Education, Treatment, Assessment, etc.
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
         * The parameter element defines parameters used by the library.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parameter
         *     Parameters defined by the library
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parameter(ParameterDefinition... parameter) {
            for (ParameterDefinition value : parameter) {
                this.parameter.add(value);
            }
            return this;
        }

        /**
         * The parameter element defines parameters used by the library.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parameter
         *     Parameters defined by the library
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder parameter(Collection<ParameterDefinition> parameter) {
            this.parameter = new ArrayList<>(parameter);
            return this;
        }

        /**
         * Describes a set of data that must be provided in order to be able to successfully perform the computations defined by 
         * the library.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dataRequirement
         *     What data is referenced by this library
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dataRequirement(DataRequirement... dataRequirement) {
            for (DataRequirement value : dataRequirement) {
                this.dataRequirement.add(value);
            }
            return this;
        }

        /**
         * Describes a set of data that must be provided in order to be able to successfully perform the computations defined by 
         * the library.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dataRequirement
         *     What data is referenced by this library
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder dataRequirement(Collection<DataRequirement> dataRequirement) {
            this.dataRequirement = new ArrayList<>(dataRequirement);
            return this;
        }

        /**
         * The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a 
         * base-64 string. Either way, the contentType of the attachment determines how to interpret the content.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param content
         *     Contents of the library, either embedded or referenced
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder content(Attachment... content) {
            for (Attachment value : content) {
                this.content.add(value);
            }
            return this;
        }

        /**
         * The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a 
         * base-64 string. Either way, the contentType of the attachment determines how to interpret the content.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param content
         *     Contents of the library, either embedded or referenced
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder content(Collection<Attachment> content) {
            this.content = new ArrayList<>(content);
            return this;
        }

        /**
         * Build the {@link Library}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>type</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Library}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Library per the base specification
         */
        @Override
        public Library build() {
            Library library = new Library(this);
            if (validating) {
                validate(library);
            }
            return library;
        }

        protected void validate(Library library) {
            super.validate(library);
            ValidationSupport.checkList(library.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(library.status, "status");
            ValidationSupport.requireNonNull(library.type, "type");
            ValidationSupport.choiceElement(library.subject, "subject", CodeableConcept.class, Reference.class);
            ValidationSupport.checkList(library.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(library.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(library.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(library.topic, "topic", CodeableConcept.class);
            ValidationSupport.checkList(library.author, "author", ContactDetail.class);
            ValidationSupport.checkList(library.editor, "editor", ContactDetail.class);
            ValidationSupport.checkList(library.reviewer, "reviewer", ContactDetail.class);
            ValidationSupport.checkList(library.endorser, "endorser", ContactDetail.class);
            ValidationSupport.checkList(library.relatedArtifact, "relatedArtifact", RelatedArtifact.class);
            ValidationSupport.checkList(library.parameter, "parameter", ParameterDefinition.class);
            ValidationSupport.checkList(library.dataRequirement, "dataRequirement", DataRequirement.class);
            ValidationSupport.checkList(library.content, "content", Attachment.class);
            ValidationSupport.checkReferenceType(library.subject, "subject", "Group");
        }

        protected Builder from(Library library) {
            super.from(library);
            url = library.url;
            identifier.addAll(library.identifier);
            version = library.version;
            name = library.name;
            title = library.title;
            subtitle = library.subtitle;
            status = library.status;
            experimental = library.experimental;
            type = library.type;
            subject = library.subject;
            date = library.date;
            publisher = library.publisher;
            contact.addAll(library.contact);
            description = library.description;
            useContext.addAll(library.useContext);
            jurisdiction.addAll(library.jurisdiction);
            purpose = library.purpose;
            usage = library.usage;
            copyright = library.copyright;
            approvalDate = library.approvalDate;
            lastReviewDate = library.lastReviewDate;
            effectivePeriod = library.effectivePeriod;
            topic.addAll(library.topic);
            author.addAll(library.author);
            editor.addAll(library.editor);
            reviewer.addAll(library.reviewer);
            endorser.addAll(library.endorser);
            relatedArtifact.addAll(library.relatedArtifact);
            parameter.addAll(library.parameter);
            dataRequirement.addAll(library.dataRequirement);
            content.addAll(library.content);
            return this;
        }
    }
}
