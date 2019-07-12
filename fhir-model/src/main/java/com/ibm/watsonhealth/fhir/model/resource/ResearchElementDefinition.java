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

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.DataRequirement;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Expression;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.GroupMeasure;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RelatedArtifact;
import com.ibm.watsonhealth.fhir.model.type.ResearchElementType;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.type.VariableType;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The ResearchElementDefinition resource describes a "PICO" element that knowledge (evidence, assertion, recommendation) 
 * is about.
 * </p>
 */
@Constraint(
    id = "red-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ResearchElementDefinition extends DomainResource {
    private final Uri url;
    private final List<Identifier> identifier;
    private final String version;
    private final String name;
    private final String title;
    private final String shortTitle;
    private final String subtitle;
    private final PublicationStatus status;
    private final Boolean experimental;
    private final Element subject;
    private final DateTime date;
    private final String publisher;
    private final List<ContactDetail> contact;
    private final Markdown description;
    private final List<String> comment;
    private final List<UsageContext> useContext;
    private final List<CodeableConcept> jurisdiction;
    private final Markdown purpose;
    private final String usage;
    private final Markdown copyright;
    private final Date approvalDate;
    private final Date lastReviewDate;
    private final Period effectivePeriod;
    private final List<CodeableConcept> topic;
    private final List<ContactDetail> author;
    private final List<ContactDetail> editor;
    private final List<ContactDetail> reviewer;
    private final List<ContactDetail> endorser;
    private final List<RelatedArtifact> relatedArtifact;
    private final List<Canonical> library;
    private final ResearchElementType type;
    private final VariableType variableType;
    private final List<Characteristic> characteristic;

    private volatile int hashCode;

    private ResearchElementDefinition(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = builder.name;
        title = builder.title;
        shortTitle = builder.shortTitle;
        subtitle = builder.subtitle;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        experimental = builder.experimental;
        subject = ValidationSupport.choiceElement(builder.subject, "subject", CodeableConcept.class, Reference.class);
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
        type = ValidationSupport.requireNonNull(builder.type, "type");
        variableType = builder.variableType;
        characteristic = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.characteristic, "characteristic"));
    }

    /**
     * <p>
     * An absolute URI that is used to identify this research element definition when it is referenced in a specification, 
     * model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a 
     * literal address at which at which an authoritative instance of this research element definition is (or will be) 
     * published. This URL can be the target of a canonical reference. It SHALL remain the same when the research element 
     * definition is stored on different servers.
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
     * A formal identifier that is used to identify this research element definition when it is represented in other formats, 
     * or referenced in a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the research element definition when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the research element definition author 
     * and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version 
     * is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
     * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
     * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
     * version is required for non-experimental active artifacts.
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
     * A natural language name identifying the research element definition. This name should be usable as an identifier for 
     * the module by machine processing applications such as code generation.
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
     * A short, descriptive, user-friendly title for the research element definition.
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
     * The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is 
     * not necessary.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * <p>
     * An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.
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
     * The status of this research element definition. Enables tracking the life-cycle of the content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus}.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * A Boolean value to indicate that this research element definition is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * <p>
     * The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is 
     * assumed, but the subject of the ResearchElementDefinition can be anything.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getSubject() {
        return subject;
    }

    /**
     * <p>
     * The date (and optionally time) when the research element definition was published. The date must change when the 
     * business version changes and it must change if the status code changes. In addition, it should change when the 
     * substantive content of the research element definition changes.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * <p>
     * The name of the organization or individual that published the research element definition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * <p>
     * Contact details to assist a user in finding and communicating with the publisher.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * <p>
     * A free text natural language description of the research element definition from a consumer's perspective.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * <p>
     * A human-readable string to clarify or explain concepts about the resource.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String}.
     */
    public List<String> getComment() {
        return comment;
    }

    /**
     * <p>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate research element definition instances.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext}.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * <p>
     * A legal or geographic region in which the research element definition is intended to be used.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * <p>
     * Explanation of why this research element definition is needed and why it has been designed as it has.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * <p>
     * A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * <p>
     * A copyright statement relating to the research element definition and/or its contents. Copyright statements are 
     * generally legal restrictions on the use and publishing of the research element definition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * <p>
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is 
     * officially approved for usage.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * <p>
     * The date on which the resource content was last reviewed. Review happens periodically after approval but does not 
     * change the original approval date.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    /**
     * <p>
     * The period during which the research element definition content was or is planned to be in active use.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getEffectivePeriod() {
        return effectivePeriod;
    }

    /**
     * <p>
     * Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization 
     * grouping types of ResearchElementDefinitions that can be useful for filtering and searching.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getTopic() {
        return topic;
    }

    /**
     * <p>
     * An individiual or organization primarily involved in the creation and maintenance of the content.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getAuthor() {
        return author;
    }

    /**
     * <p>
     * An individual or organization primarily responsible for internal coherence of the content.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getEditor() {
        return editor;
    }

    /**
     * <p>
     * An individual or organization primarily responsible for review of some aspect of the content.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getReviewer() {
        return reviewer;
    }

    /**
     * <p>
     * An individual or organization responsible for officially endorsing the content for use in some setting.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getEndorser() {
        return endorser;
    }

    /**
     * <p>
     * Related artifacts such as additional documentation, justification, or bibliographic references.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact}.
     */
    public List<RelatedArtifact> getRelatedArtifact() {
        return relatedArtifact;
    }

    /**
     * <p>
     * A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getLibrary() {
        return library;
    }

    /**
     * <p>
     * The type of research element, a population, an exposure, or an outcome.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ResearchElementType}.
     */
    public ResearchElementType getType() {
        return type;
    }

    /**
     * <p>
     * The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link VariableType}.
     */
    public VariableType getVariableType() {
        return variableType;
    }

    /**
     * <p>
     * A characteristic that defines the members of the research element. Multiple characteristics are applied with "and" 
     * semantics.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Characteristic}.
     */
    public List<Characteristic> getCharacteristic() {
        return characteristic;
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
        return new Builder(status, type, characteristic).from(this);
    }

    public Builder toBuilder(PublicationStatus status, ResearchElementType type, List<Characteristic> characteristic) {
        return new Builder(status, type, characteristic).from(this);
    }

    public static Builder builder(PublicationStatus status, ResearchElementType type, List<Characteristic> characteristic) {
        return new Builder(status, type, characteristic);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PublicationStatus status;
        private final ResearchElementType type;
        private final List<Characteristic> characteristic;

        // optional
        private Uri url;
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String name;
        private String title;
        private String shortTitle;
        private String subtitle;
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
        private VariableType variableType;

        private Builder(PublicationStatus status, ResearchElementType type, List<Characteristic> characteristic) {
            super();
            this.status = status;
            this.type = type;
            this.characteristic = new ArrayList<>(characteristic);
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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * An absolute URI that is used to identify this research element definition when it is referenced in a specification, 
         * model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a 
         * literal address at which at which an authoritative instance of this research element definition is (or will be) 
         * published. This URL can be the target of a canonical reference. It SHALL remain the same when the research element 
         * definition is stored on different servers.
         * </p>
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
         * <p>
         * A formal identifier that is used to identify this research element definition when it is represented in other formats, 
         * or referenced in a specification, model, design or an instance.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A formal identifier that is used to identify this research element definition when it is represented in other formats, 
         * or referenced in a specification, model, design or an instance.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the research element definition
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
         * The identifier that is used to identify this version of the research element definition when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the research element definition author 
         * and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version 
         * is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
         * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
         * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
         * version is required for non-experimental active artifacts.
         * </p>
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
         * <p>
         * A natural language name identifying the research element definition. This name should be usable as an identifier for 
         * the module by machine processing applications such as code generation.
         * </p>
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
         * <p>
         * A short, descriptive, user-friendly title for the research element definition.
         * </p>
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
         * <p>
         * The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is 
         * not necessary.
         * </p>
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
         * <p>
         * An explanatory or alternate title for the ResearchElementDefinition giving additional information about its content.
         * </p>
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
         * <p>
         * A Boolean value to indicate that this research element definition is authored for testing purposes (or 
         * education/evaluation/marketing) and is not intended to be used for genuine usage.
         * </p>
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
         * <p>
         * The intended subjects for the ResearchElementDefinition. If this element is not provided, a Patient subject is 
         * assumed, but the subject of the ResearchElementDefinition can be anything.
         * </p>
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
         * <p>
         * The date (and optionally time) when the research element definition was published. The date must change when the 
         * business version changes and it must change if the status code changes. In addition, it should change when the 
         * substantive content of the research element definition changes.
         * </p>
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
         * <p>
         * The name of the organization or individual that published the research element definition.
         * </p>
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
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * <p>
         * A free text natural language description of the research element definition from a consumer's perspective.
         * </p>
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
         * <p>
         * A human-readable string to clarify or explain concepts about the resource.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A human-readable string to clarify or explain concepts about the resource.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param comment
         *     Used for footnotes or explanatory notes
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comment(Collection<String> comment) {
            this.comment = new ArrayList<>(comment);
            return this;
        }

        /**
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate research element definition instances.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate research element definition instances.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext = new ArrayList<>(useContext);
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the research element definition is intended to be used.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A legal or geographic region in which the research element definition is intended to be used.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for research element definition (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction = new ArrayList<>(jurisdiction);
            return this;
        }

        /**
         * <p>
         * Explanation of why this research element definition is needed and why it has been designed as it has.
         * </p>
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
         * <p>
         * A detailed description, from a clinical perspective, of how the ResearchElementDefinition is used.
         * </p>
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
         * <p>
         * A copyright statement relating to the research element definition and/or its contents. Copyright statements are 
         * generally legal restrictions on the use and publishing of the research element definition.
         * </p>
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
         * <p>
         * The date on which the resource content was approved by the publisher. Approval happens once when the content is 
         * officially approved for usage.
         * </p>
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
         * <p>
         * The date on which the resource content was last reviewed. Review happens periodically after approval but does not 
         * change the original approval date.
         * </p>
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
         * <p>
         * The period during which the research element definition content was or is planned to be in active use.
         * </p>
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
         * <p>
         * Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization 
         * grouping types of ResearchElementDefinitions that can be useful for filtering and searching.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Descriptive topics related to the content of the ResearchElementDefinition. Topics provide a high-level categorization 
         * grouping types of ResearchElementDefinitions that can be useful for filtering and searching.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param topic
         *     The category of the ResearchElementDefinition, such as Education, Treatment, Assessment, etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder topic(Collection<CodeableConcept> topic) {
            this.topic = new ArrayList<>(topic);
            return this;
        }

        /**
         * <p>
         * An individiual or organization primarily involved in the creation and maintenance of the content.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * An individiual or organization primarily involved in the creation and maintenance of the content.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Collection<ContactDetail> author) {
            this.author = new ArrayList<>(author);
            return this;
        }

        /**
         * <p>
         * An individual or organization primarily responsible for internal coherence of the content.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * An individual or organization primarily responsible for internal coherence of the content.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder editor(Collection<ContactDetail> editor) {
            this.editor = new ArrayList<>(editor);
            return this;
        }

        /**
         * <p>
         * An individual or organization primarily responsible for review of some aspect of the content.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * An individual or organization primarily responsible for review of some aspect of the content.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reviewer(Collection<ContactDetail> reviewer) {
            this.reviewer = new ArrayList<>(reviewer);
            return this;
        }

        /**
         * <p>
         * An individual or organization responsible for officially endorsing the content for use in some setting.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * An individual or organization responsible for officially endorsing the content for use in some setting.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder endorser(Collection<ContactDetail> endorser) {
            this.endorser = new ArrayList<>(endorser);
            return this;
        }

        /**
         * <p>
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param relatedArtifact
         *     Additional documentation, citations, etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatedArtifact(Collection<RelatedArtifact> relatedArtifact) {
            this.relatedArtifact = new ArrayList<>(relatedArtifact);
            return this;
        }

        /**
         * <p>
         * A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A reference to a Library resource containing the formal logic used by the ResearchElementDefinition.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param library
         *     Logic used by the ResearchElementDefinition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder library(Collection<Canonical> library) {
            this.library = new ArrayList<>(library);
            return this;
        }

        /**
         * <p>
         * The type of the outcome (e.g. Dichotomous, Continuous, or Descriptive).
         * </p>
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

        @Override
        public ResearchElementDefinition build() {
            return new ResearchElementDefinition(this);
        }

        private Builder from(ResearchElementDefinition researchElementDefinition) {
            id = researchElementDefinition.id;
            meta = researchElementDefinition.meta;
            implicitRules = researchElementDefinition.implicitRules;
            language = researchElementDefinition.language;
            text = researchElementDefinition.text;
            contained.addAll(researchElementDefinition.contained);
            extension.addAll(researchElementDefinition.extension);
            modifierExtension.addAll(researchElementDefinition.modifierExtension);
            url = researchElementDefinition.url;
            identifier.addAll(researchElementDefinition.identifier);
            version = researchElementDefinition.version;
            name = researchElementDefinition.name;
            title = researchElementDefinition.title;
            shortTitle = researchElementDefinition.shortTitle;
            subtitle = researchElementDefinition.subtitle;
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
            variableType = researchElementDefinition.variableType;
            return this;
        }
    }

    /**
     * <p>
     * A characteristic that defines the members of the research element. Multiple characteristics are applied with "and" 
     * semantics.
     * </p>
     */
    public static class Characteristic extends BackboneElement {
        private final Element definition;
        private final List<UsageContext> usageContext;
        private final Boolean exclude;
        private final CodeableConcept unitOfMeasure;
        private final String studyEffectiveDescription;
        private final Element studyEffective;
        private final Duration studyEffectiveTimeFromStart;
        private final GroupMeasure studyEffectiveGroupMeasure;
        private final String participantEffectiveDescription;
        private final Element participantEffective;
        private final Duration participantEffectiveTimeFromStart;
        private final GroupMeasure participantEffectiveGroupMeasure;

        private volatile int hashCode;

        private Characteristic(Builder builder) {
            super(builder);
            definition = ValidationSupport.requireChoiceElement(builder.definition, "definition", CodeableConcept.class, Canonical.class, Expression.class, DataRequirement.class);
            usageContext = Collections.unmodifiableList(builder.usageContext);
            exclude = builder.exclude;
            unitOfMeasure = builder.unitOfMeasure;
            studyEffectiveDescription = builder.studyEffectiveDescription;
            studyEffective = ValidationSupport.choiceElement(builder.studyEffective, "studyEffective", DateTime.class, Period.class, Duration.class, Timing.class);
            studyEffectiveTimeFromStart = builder.studyEffectiveTimeFromStart;
            studyEffectiveGroupMeasure = builder.studyEffectiveGroupMeasure;
            participantEffectiveDescription = builder.participantEffectiveDescription;
            participantEffective = ValidationSupport.choiceElement(builder.participantEffective, "participantEffective", DateTime.class, Period.class, Duration.class, Timing.class);
            participantEffectiveTimeFromStart = builder.participantEffectiveTimeFromStart;
            participantEffectiveGroupMeasure = builder.participantEffectiveGroupMeasure;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Define members of the research element using Codes (such as condition, medication, or observation), Expressions ( 
         * using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the 
         * last year).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getDefinition() {
            return definition;
        }

        /**
         * <p>
         * Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link UsageContext}.
         */
        public List<UsageContext> getUsageContext() {
            return usageContext;
        }

        /**
         * <p>
         * When true, members with this characteristic are excluded from the element.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getExclude() {
            return exclude;
        }

        /**
         * <p>
         * Specifies the UCUM unit for the outcome.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getUnitOfMeasure() {
            return unitOfMeasure;
        }

        /**
         * <p>
         * A narrative description of the time period the study covers.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getStudyEffectiveDescription() {
            return studyEffectiveDescription;
        }

        /**
         * <p>
         * Indicates what effective period the study covers.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getStudyEffective() {
            return studyEffective;
        }

        /**
         * <p>
         * Indicates duration from the study initiation.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Duration}.
         */
        public Duration getStudyEffectiveTimeFromStart() {
            return studyEffectiveTimeFromStart;
        }

        /**
         * <p>
         * Indicates how elements are aggregated within the study effective period.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link GroupMeasure}.
         */
        public GroupMeasure getStudyEffectiveGroupMeasure() {
            return studyEffectiveGroupMeasure;
        }

        /**
         * <p>
         * A narrative description of the time period the study covers.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getParticipantEffectiveDescription() {
            return participantEffectiveDescription;
        }

        /**
         * <p>
         * Indicates what effective period the study covers.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getParticipantEffective() {
            return participantEffective;
        }

        /**
         * <p>
         * Indicates duration from the participant's study entry.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Duration}.
         */
        public Duration getParticipantEffectiveTimeFromStart() {
            return participantEffectiveTimeFromStart;
        }

        /**
         * <p>
         * Indicates how elements are aggregated within the study effective period.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link GroupMeasure}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
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
            return new Builder(definition).from(this);
        }

        public Builder toBuilder(Element definition) {
            return new Builder(definition).from(this);
        }

        public static Builder builder(Element definition) {
            return new Builder(definition);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Element definition;

            // optional
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

            private Builder(Element definition) {
                super();
                this.definition = definition;
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
             * Adds new element(s) to existing list
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
             * Adds new element(s) to existing list
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
             * Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Use UsageContext to define the members of the population, such as Age Ranges, Genders, Settings.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param usageContext
             *     What code/value pairs define members?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder usageContext(Collection<UsageContext> usageContext) {
                this.usageContext = new ArrayList<>(usageContext);
                return this;
            }

            /**
             * <p>
             * When true, members with this characteristic are excluded from the element.
             * </p>
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
             * <p>
             * Specifies the UCUM unit for the outcome.
             * </p>
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
             * <p>
             * A narrative description of the time period the study covers.
             * </p>
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
             * <p>
             * Indicates what effective period the study covers.
             * </p>
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
             * <p>
             * Indicates duration from the study initiation.
             * </p>
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
             * <p>
             * Indicates how elements are aggregated within the study effective period.
             * </p>
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
             * <p>
             * A narrative description of the time period the study covers.
             * </p>
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
             * <p>
             * Indicates what effective period the study covers.
             * </p>
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
             * <p>
             * Indicates duration from the participant's study entry.
             * </p>
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
             * <p>
             * Indicates how elements are aggregated within the study effective period.
             * </p>
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

            @Override
            public Characteristic build() {
                return new Characteristic(this);
            }

            private Builder from(Characteristic characteristic) {
                id = characteristic.id;
                extension.addAll(characteristic.extension);
                modifierExtension.addAll(characteristic.modifierExtension);
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
