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
import com.ibm.watsonhealth.fhir.model.type.ActionCardinalityBehavior;
import com.ibm.watsonhealth.fhir.model.type.ActionConditionKind;
import com.ibm.watsonhealth.fhir.model.type.ActionGroupingBehavior;
import com.ibm.watsonhealth.fhir.model.type.ActionParticipantType;
import com.ibm.watsonhealth.fhir.model.type.ActionPrecheckBehavior;
import com.ibm.watsonhealth.fhir.model.type.ActionRelationshipType;
import com.ibm.watsonhealth.fhir.model.type.ActionRequiredBehavior;
import com.ibm.watsonhealth.fhir.model.type.ActionSelectionBehavior;
import com.ibm.watsonhealth.fhir.model.type.Age;
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
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RelatedArtifact;
import com.ibm.watsonhealth.fhir.model.type.RequestPriority;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.TriggerDefinition;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. 
 * The resource is general enough to support the description of a broad range of clinical artifacts such as clinical 
 * decision support rules, order sets and protocols.
 * </p>
 */
@Constraint(
    id = "pdf-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class PlanDefinition extends DomainResource {
    private final Uri url;
    private final List<Identifier> identifier;
    private final String version;
    private final String name;
    private final String title;
    private final String subtitle;
    private final CodeableConcept type;
    private final PublicationStatus status;
    private final Boolean experimental;
    private final Element subject;
    private final DateTime date;
    private final String publisher;
    private final List<ContactDetail> contact;
    private final Markdown description;
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
    private final List<Goal> goal;
    private final List<Action> action;

    private volatile int hashCode;

    private PlanDefinition(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = builder.name;
        title = builder.title;
        subtitle = builder.subtitle;
        type = builder.type;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        experimental = builder.experimental;
        subject = ValidationSupport.choiceElement(builder.subject, "subject", CodeableConcept.class, Reference.class);
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
        library = Collections.unmodifiableList(builder.library);
        goal = Collections.unmodifiableList(builder.goal);
        action = Collections.unmodifiableList(builder.action);
    }

    /**
     * <p>
     * An absolute URI that is used to identify this plan definition when it is referenced in a specification, model, design 
     * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
     * at which at which an authoritative instance of this plan definition is (or will be) published. This URL can be the 
     * target of a canonical reference. It SHALL remain the same when the plan definition is stored on different servers.
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
     * A formal identifier that is used to identify this plan definition when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the plan definition when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be 
     * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
     * also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with 
     * the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on 
     * versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for 
     * non-experimental active artifacts.
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
     * A natural language name identifying the plan definition. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
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
     * A short, descriptive, user-friendly title for the plan definition.
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
     * An explanatory or alternate title for the plan definition giving additional information about its content.
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
     * A high-level category for the plan definition that distinguishes the kinds of systems that would be interested in the 
     * plan definition.
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
     * The status of this plan definition. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this plan definition is authored for testing purposes (or 
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
     * A code or group definition that describes the intended subject of the plan definition.
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
     * The date (and optionally time) when the plan definition was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the plan definition changes.
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
     * The name of the organization or individual that published the plan definition.
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
     * A free text natural language description of the plan definition from a consumer's perspective.
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
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate plan definition instances.
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
     * A legal or geographic region in which the plan definition is intended to be used.
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
     * Explanation of why this plan definition is needed and why it has been designed as it has.
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
     * A detailed description of how the plan definition is used from a clinical perspective.
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
     * A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the plan definition.
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
     * The period during which the plan definition content was or is planned to be in active use.
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
     * Descriptive topics related to the content of the plan definition. Topics provide a high-level categorization of the 
     * definition that can be useful for filtering and searching.
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
     * A reference to a Library resource containing any formal logic used by the plan definition.
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
     * Goals that describe what the activities within the plan are intended to achieve. For example, weight loss, restoring 
     * an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Goal}.
     */
    public List<Goal> getGoal() {
        return goal;
    }

    /**
     * <p>
     * An action or group of actions to be taken as part of the plan.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Action}.
     */
    public List<Action> getAction() {
        return action;
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
                accept(subtitle, "subtitle", visitor);
                accept(type, "type", visitor);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
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
                accept(library, "library", visitor, Canonical.class);
                accept(goal, "goal", visitor, Goal.class);
                accept(action, "action", visitor, Action.class);
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
        PlanDefinition other = (PlanDefinition) obj;
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
            Objects.equals(type, other.type) && 
            Objects.equals(status, other.status) && 
            Objects.equals(experimental, other.experimental) && 
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
            Objects.equals(library, other.library) && 
            Objects.equals(goal, other.goal) && 
            Objects.equals(action, other.action);
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
                type, 
                status, 
                experimental, 
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
                library, 
                goal, 
                action);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status).from(this);
    }

    public Builder toBuilder(PublicationStatus status) {
        return new Builder(status).from(this);
    }

    public static Builder builder(PublicationStatus status) {
        return new Builder(status);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PublicationStatus status;

        // optional
        private Uri url;
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String name;
        private String title;
        private String subtitle;
        private CodeableConcept type;
        private Boolean experimental;
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
        private List<Canonical> library = new ArrayList<>();
        private List<Goal> goal = new ArrayList<>();
        private List<Action> action = new ArrayList<>();

        private Builder(PublicationStatus status) {
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
         * An absolute URI that is used to identify this plan definition when it is referenced in a specification, model, design 
         * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
         * at which at which an authoritative instance of this plan definition is (or will be) published. This URL can be the 
         * target of a canonical reference. It SHALL remain the same when the plan definition is stored on different servers.
         * </p>
         * 
         * @param url
         *     Canonical identifier for this plan definition, represented as a URI (globally unique)
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
         * A formal identifier that is used to identify this plan definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the plan definition
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
         * A formal identifier that is used to identify this plan definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the plan definition
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
         * The identifier that is used to identify this version of the plan definition when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with 
         * the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on 
         * versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for 
         * non-experimental active artifacts.
         * </p>
         * 
         * @param version
         *     Business version of the plan definition
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
         * A natural language name identifying the plan definition. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
         * </p>
         * 
         * @param name
         *     Name for this plan definition (computer friendly)
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
         * A short, descriptive, user-friendly title for the plan definition.
         * </p>
         * 
         * @param title
         *     Name for this plan definition (human friendly)
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
         * An explanatory or alternate title for the plan definition giving additional information about its content.
         * </p>
         * 
         * @param subtitle
         *     Subordinate title of the plan definition
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
         * A high-level category for the plan definition that distinguishes the kinds of systems that would be interested in the 
         * plan definition.
         * </p>
         * 
         * @param type
         *     order-set | clinical-protocol | eca-rule | workflow-definition
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
         * A Boolean value to indicate that this plan definition is authored for testing purposes (or 
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
         * A code or group definition that describes the intended subject of the plan definition.
         * </p>
         * 
         * @param subject
         *     Type of individual the plan definition is focused on
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
         * The date (and optionally time) when the plan definition was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the plan definition changes.
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
         * The name of the organization or individual that published the plan definition.
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
         * Adds new element(s) to the existing list
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
         * A free text natural language description of the plan definition from a consumer's perspective.
         * </p>
         * 
         * @param description
         *     Natural language description of the plan definition
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate plan definition instances.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
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
         * may be used to assist with indexing and searching for appropriate plan definition instances.
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
         * A legal or geographic region in which the plan definition is intended to be used.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for plan definition (if applicable)
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
         * A legal or geographic region in which the plan definition is intended to be used.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for plan definition (if applicable)
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
         * Explanation of why this plan definition is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this plan definition is defined
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
         * A detailed description of how the plan definition is used from a clinical perspective.
         * </p>
         * 
         * @param usage
         *     Describes the clinical usage of the plan
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
         * A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the plan definition.
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
         *     When the plan definition was approved by publisher
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
         *     When the plan definition was last reviewed
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
         * The period during which the plan definition content was or is planned to be in active use.
         * </p>
         * 
         * @param effectivePeriod
         *     When the plan definition is expected to be used
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
         * Descriptive topics related to the content of the plan definition. Topics provide a high-level categorization of the 
         * definition that can be useful for filtering and searching.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param topic
         *     E.g. Education, Treatment, Assessment
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
         * Descriptive topics related to the content of the plan definition. Topics provide a high-level categorization of the 
         * definition that can be useful for filtering and searching.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param topic
         *     E.g. Education, Treatment, Assessment
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param relatedArtifact
         *     Additional documentation, citations
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
         *     Additional documentation, citations
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
         * A reference to a Library resource containing any formal logic used by the plan definition.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param library
         *     Logic used by the plan definition
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
         * A reference to a Library resource containing any formal logic used by the plan definition.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param library
         *     Logic used by the plan definition
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
         * Goals that describe what the activities within the plan are intended to achieve. For example, weight loss, restoring 
         * an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param goal
         *     What the plan is trying to accomplish
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder goal(Goal... goal) {
            for (Goal value : goal) {
                this.goal.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Goals that describe what the activities within the plan are intended to achieve. For example, weight loss, restoring 
         * an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param goal
         *     What the plan is trying to accomplish
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder goal(Collection<Goal> goal) {
            this.goal = new ArrayList<>(goal);
            return this;
        }

        /**
         * <p>
         * An action or group of actions to be taken as part of the plan.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param action
         *     Action defined by the plan
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
         * An action or group of actions to be taken as part of the plan.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param action
         *     Action defined by the plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder action(Collection<Action> action) {
            this.action = new ArrayList<>(action);
            return this;
        }

        @Override
        public PlanDefinition build() {
            return new PlanDefinition(this);
        }

        private Builder from(PlanDefinition planDefinition) {
            id = planDefinition.id;
            meta = planDefinition.meta;
            implicitRules = planDefinition.implicitRules;
            language = planDefinition.language;
            text = planDefinition.text;
            contained.addAll(planDefinition.contained);
            extension.addAll(planDefinition.extension);
            modifierExtension.addAll(planDefinition.modifierExtension);
            url = planDefinition.url;
            identifier.addAll(planDefinition.identifier);
            version = planDefinition.version;
            name = planDefinition.name;
            title = planDefinition.title;
            subtitle = planDefinition.subtitle;
            type = planDefinition.type;
            experimental = planDefinition.experimental;
            subject = planDefinition.subject;
            date = planDefinition.date;
            publisher = planDefinition.publisher;
            contact.addAll(planDefinition.contact);
            description = planDefinition.description;
            useContext.addAll(planDefinition.useContext);
            jurisdiction.addAll(planDefinition.jurisdiction);
            purpose = planDefinition.purpose;
            usage = planDefinition.usage;
            copyright = planDefinition.copyright;
            approvalDate = planDefinition.approvalDate;
            lastReviewDate = planDefinition.lastReviewDate;
            effectivePeriod = planDefinition.effectivePeriod;
            topic.addAll(planDefinition.topic);
            author.addAll(planDefinition.author);
            editor.addAll(planDefinition.editor);
            reviewer.addAll(planDefinition.reviewer);
            endorser.addAll(planDefinition.endorser);
            relatedArtifact.addAll(planDefinition.relatedArtifact);
            library.addAll(planDefinition.library);
            goal.addAll(planDefinition.goal);
            action.addAll(planDefinition.action);
            return this;
        }
    }

    /**
     * <p>
     * Goals that describe what the activities within the plan are intended to achieve. For example, weight loss, restoring 
     * an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
     * </p>
     */
    public static class Goal extends BackboneElement {
        private final CodeableConcept category;
        private final CodeableConcept description;
        private final CodeableConcept priority;
        private final CodeableConcept start;
        private final List<CodeableConcept> addresses;
        private final List<RelatedArtifact> documentation;
        private final List<Target> target;

        private volatile int hashCode;

        private Goal(Builder builder) {
            super(builder);
            category = builder.category;
            description = ValidationSupport.requireNonNull(builder.description, "description");
            priority = builder.priority;
            start = builder.start;
            addresses = Collections.unmodifiableList(builder.addresses);
            documentation = Collections.unmodifiableList(builder.documentation);
            target = Collections.unmodifiableList(builder.target);
        }

        /**
         * <p>
         * Indicates a category the goal falls within.
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
         * Human-readable and/or coded description of a specific desired objective of care, such as "control blood pressure" or 
         * "negotiate an obstacle course" or "dance with child at wedding".
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getDescription() {
            return description;
        }

        /**
         * <p>
         * Identifies the expected level of importance associated with reaching/sustaining the defined goal.
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
         * The event after which the goal should begin being pursued.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getStart() {
            return start;
        }

        /**
         * <p>
         * Identifies problems, conditions, issues, or concerns the goal is intended to address.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getAddresses() {
            return addresses;
        }

        /**
         * <p>
         * Didactic or other informational resources associated with the goal that provide further supporting information about 
         * the goal. Information resources can include inline text commentary and links to web resources.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact}.
         */
        public List<RelatedArtifact> getDocumentation() {
            return documentation;
        }

        /**
         * <p>
         * Indicates what should be done and within what timeframe.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Target}.
         */
        public List<Target> getTarget() {
            return target;
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
                    accept(category, "category", visitor);
                    accept(description, "description", visitor);
                    accept(priority, "priority", visitor);
                    accept(start, "start", visitor);
                    accept(addresses, "addresses", visitor, CodeableConcept.class);
                    accept(documentation, "documentation", visitor, RelatedArtifact.class);
                    accept(target, "target", visitor, Target.class);
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
            Goal other = (Goal) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(category, other.category) && 
                Objects.equals(description, other.description) && 
                Objects.equals(priority, other.priority) && 
                Objects.equals(start, other.start) && 
                Objects.equals(addresses, other.addresses) && 
                Objects.equals(documentation, other.documentation) && 
                Objects.equals(target, other.target);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    category, 
                    description, 
                    priority, 
                    start, 
                    addresses, 
                    documentation, 
                    target);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(description).from(this);
        }

        public Builder toBuilder(CodeableConcept description) {
            return new Builder(description).from(this);
        }

        public static Builder builder(CodeableConcept description) {
            return new Builder(description);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept description;

            // optional
            private CodeableConcept category;
            private CodeableConcept priority;
            private CodeableConcept start;
            private List<CodeableConcept> addresses = new ArrayList<>();
            private List<RelatedArtifact> documentation = new ArrayList<>();
            private List<Target> target = new ArrayList<>();

            private Builder(CodeableConcept description) {
                super();
                this.description = description;
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
             * Indicates a category the goal falls within.
             * </p>
             * 
             * @param category
             *     E.g. Treatment, dietary, behavioral
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
             * Identifies the expected level of importance associated with reaching/sustaining the defined goal.
             * </p>
             * 
             * @param priority
             *     high-priority | medium-priority | low-priority
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
             * The event after which the goal should begin being pursued.
             * </p>
             * 
             * @param start
             *     When goal pursuit begins
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder start(CodeableConcept start) {
                this.start = start;
                return this;
            }

            /**
             * <p>
             * Identifies problems, conditions, issues, or concerns the goal is intended to address.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param addresses
             *     What does the goal address
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder addresses(CodeableConcept... addresses) {
                for (CodeableConcept value : addresses) {
                    this.addresses.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Identifies problems, conditions, issues, or concerns the goal is intended to address.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param addresses
             *     What does the goal address
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder addresses(Collection<CodeableConcept> addresses) {
                this.addresses = new ArrayList<>(addresses);
                return this;
            }

            /**
             * <p>
             * Didactic or other informational resources associated with the goal that provide further supporting information about 
             * the goal. Information resources can include inline text commentary and links to web resources.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param documentation
             *     Supporting documentation for the goal
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(RelatedArtifact... documentation) {
                for (RelatedArtifact value : documentation) {
                    this.documentation.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Didactic or other informational resources associated with the goal that provide further supporting information about 
             * the goal. Information resources can include inline text commentary and links to web resources.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param documentation
             *     Supporting documentation for the goal
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(Collection<RelatedArtifact> documentation) {
                this.documentation = new ArrayList<>(documentation);
                return this;
            }

            /**
             * <p>
             * Indicates what should be done and within what timeframe.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param target
             *     Target outcome for the goal
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder target(Target... target) {
                for (Target value : target) {
                    this.target.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Indicates what should be done and within what timeframe.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param target
             *     Target outcome for the goal
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder target(Collection<Target> target) {
                this.target = new ArrayList<>(target);
                return this;
            }

            @Override
            public Goal build() {
                return new Goal(this);
            }

            private Builder from(Goal goal) {
                id = goal.id;
                extension.addAll(goal.extension);
                modifierExtension.addAll(goal.modifierExtension);
                category = goal.category;
                priority = goal.priority;
                start = goal.start;
                addresses.addAll(goal.addresses);
                documentation.addAll(goal.documentation);
                target.addAll(goal.target);
                return this;
            }
        }

        /**
         * <p>
         * Indicates what should be done and within what timeframe.
         * </p>
         */
        public static class Target extends BackboneElement {
            private final CodeableConcept measure;
            private final Element detail;
            private final Duration due;

            private volatile int hashCode;

            private Target(Builder builder) {
                super(builder);
                measure = builder.measure;
                detail = ValidationSupport.choiceElement(builder.detail, "detail", Quantity.class, Range.class, CodeableConcept.class);
                due = builder.due;
            }

            /**
             * <p>
             * The parameter whose value is to be tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getMeasure() {
                return measure;
            }

            /**
             * <p>
             * The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%. Either the 
             * high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is 
             * achieved at any value at or below the high value. Similarly, if the high value is missing, it indicates that the goal 
             * is achieved at any value at or above the low value.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getDetail() {
                return detail;
            }

            /**
             * <p>
             * Indicates the timeframe after the start of the goal in which the goal should be met.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Duration}.
             */
            public Duration getDue() {
                return due;
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
                        accept(measure, "measure", visitor);
                        accept(detail, "detail", visitor);
                        accept(due, "due", visitor);
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
                Target other = (Target) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(measure, other.measure) && 
                    Objects.equals(detail, other.detail) && 
                    Objects.equals(due, other.due);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        measure, 
                        detail, 
                        due);
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
                private CodeableConcept measure;
                private Element detail;
                private Duration due;

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
                 * The parameter whose value is to be tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.
                 * </p>
                 * 
                 * @param measure
                 *     The parameter whose value is to be tracked
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder measure(CodeableConcept measure) {
                    this.measure = measure;
                    return this;
                }

                /**
                 * <p>
                 * The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%. Either the 
                 * high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is 
                 * achieved at any value at or below the high value. Similarly, if the high value is missing, it indicates that the goal 
                 * is achieved at any value at or above the low value.
                 * </p>
                 * 
                 * @param detail
                 *     The target value to be achieved
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder detail(Element detail) {
                    this.detail = detail;
                    return this;
                }

                /**
                 * <p>
                 * Indicates the timeframe after the start of the goal in which the goal should be met.
                 * </p>
                 * 
                 * @param due
                 *     Reach goal within
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder due(Duration due) {
                    this.due = due;
                    return this;
                }

                @Override
                public Target build() {
                    return new Target(this);
                }

                private Builder from(Target target) {
                    id = target.id;
                    extension.addAll(target.extension);
                    modifierExtension.addAll(target.modifierExtension);
                    measure = target.measure;
                    detail = target.detail;
                    due = target.due;
                    return this;
                }
            }
        }
    }

    /**
     * <p>
     * An action or group of actions to be taken as part of the plan.
     * </p>
     */
    public static class Action extends BackboneElement {
        private final String prefix;
        private final String title;
        private final String description;
        private final String textEquivalent;
        private final RequestPriority priority;
        private final List<CodeableConcept> code;
        private final List<CodeableConcept> reason;
        private final List<RelatedArtifact> documentation;
        private final List<Id> goalId;
        private final Element subject;
        private final List<TriggerDefinition> trigger;
        private final List<Condition> condition;
        private final List<DataRequirement> input;
        private final List<DataRequirement> output;
        private final List<RelatedAction> relatedAction;
        private final Element timing;
        private final List<Participant> participant;
        private final CodeableConcept type;
        private final ActionGroupingBehavior groupingBehavior;
        private final ActionSelectionBehavior selectionBehavior;
        private final ActionRequiredBehavior requiredBehavior;
        private final ActionPrecheckBehavior precheckBehavior;
        private final ActionCardinalityBehavior cardinalityBehavior;
        private final Element definition;
        private final Canonical transform;
        private final List<DynamicValue> dynamicValue;
        private final List<PlanDefinition.Action> action;

        private volatile int hashCode;

        private Action(Builder builder) {
            super(builder);
            prefix = builder.prefix;
            title = builder.title;
            description = builder.description;
            textEquivalent = builder.textEquivalent;
            priority = builder.priority;
            code = Collections.unmodifiableList(builder.code);
            reason = Collections.unmodifiableList(builder.reason);
            documentation = Collections.unmodifiableList(builder.documentation);
            goalId = Collections.unmodifiableList(builder.goalId);
            subject = ValidationSupport.choiceElement(builder.subject, "subject", CodeableConcept.class, Reference.class);
            trigger = Collections.unmodifiableList(builder.trigger);
            condition = Collections.unmodifiableList(builder.condition);
            input = Collections.unmodifiableList(builder.input);
            output = Collections.unmodifiableList(builder.output);
            relatedAction = Collections.unmodifiableList(builder.relatedAction);
            timing = ValidationSupport.choiceElement(builder.timing, "timing", DateTime.class, Age.class, Period.class, Duration.class, Range.class, Timing.class);
            participant = Collections.unmodifiableList(builder.participant);
            type = builder.type;
            groupingBehavior = builder.groupingBehavior;
            selectionBehavior = builder.selectionBehavior;
            requiredBehavior = builder.requiredBehavior;
            precheckBehavior = builder.precheckBehavior;
            cardinalityBehavior = builder.cardinalityBehavior;
            definition = ValidationSupport.choiceElement(builder.definition, "definition", Canonical.class, Uri.class);
            transform = builder.transform;
            dynamicValue = Collections.unmodifiableList(builder.dynamicValue);
            action = Collections.unmodifiableList(builder.action);
        }

        /**
         * <p>
         * A user-visible prefix for the action.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * <p>
         * The title of the action displayed to a user.
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
         * A brief description of the action used to provide a summary to display to the user.
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
         * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when 
         * the definition is consumed by a system that might not be capable of interpreting it dynamically.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getTextEquivalent() {
            return textEquivalent;
        }

        /**
         * <p>
         * Indicates how quickly the action should be addressed with respect to other actions.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link RequestPriority}.
         */
        public RequestPriority getPriority() {
            return priority;
        }

        /**
         * <p>
         * A code that provides meaning for the action or action group. For example, a section may have a LOINC code for the 
         * section of a documentation template.
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
         * A description of why this action is necessary or appropriate.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getReason() {
            return reason;
        }

        /**
         * <p>
         * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
         * Information resources can include inline text commentary and links to web resources.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact}.
         */
        public List<RelatedArtifact> getDocumentation() {
            return documentation;
        }

        /**
         * <p>
         * Identifies goals that this action supports. The reference must be to a goal element defined within this plan 
         * definition.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Id}.
         */
        public List<Id> getGoalId() {
            return goalId;
        }

        /**
         * <p>
         * A code or group definition that describes the intended subject of the action and its children, if any.
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
         * A description of when the action should be triggered.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link TriggerDefinition}.
         */
        public List<TriggerDefinition> getTrigger() {
            return trigger;
        }

        /**
         * <p>
         * An expression that describes applicability criteria or start/stop conditions for the action.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Condition}.
         */
        public List<Condition> getCondition() {
            return condition;
        }

        /**
         * <p>
         * Defines input data requirements for the action.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DataRequirement}.
         */
        public List<DataRequirement> getInput() {
            return input;
        }

        /**
         * <p>
         * Defines the outputs of the action, if any.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DataRequirement}.
         */
        public List<DataRequirement> getOutput() {
            return output;
        }

        /**
         * <p>
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RelatedAction}.
         */
        public List<RelatedAction> getRelatedAction() {
            return relatedAction;
        }

        /**
         * <p>
         * An optional value describing when the action should be performed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getTiming() {
            return timing;
        }

        /**
         * <p>
         * Indicates who should participate in performing the action described.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Participant}.
         */
        public List<Participant> getParticipant() {
            return participant;
        }

        /**
         * <p>
         * The type of action to perform (create, update, remove).
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
         * Defines the grouping behavior for the action and its children.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActionGroupingBehavior}.
         */
        public ActionGroupingBehavior getGroupingBehavior() {
            return groupingBehavior;
        }

        /**
         * <p>
         * Defines the selection behavior for the action and its children.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActionSelectionBehavior}.
         */
        public ActionSelectionBehavior getSelectionBehavior() {
            return selectionBehavior;
        }

        /**
         * <p>
         * Defines the required behavior for the action.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActionRequiredBehavior}.
         */
        public ActionRequiredBehavior getRequiredBehavior() {
            return requiredBehavior;
        }

        /**
         * <p>
         * Defines whether the action should usually be preselected.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActionPrecheckBehavior}.
         */
        public ActionPrecheckBehavior getPrecheckBehavior() {
            return precheckBehavior;
        }

        /**
         * <p>
         * Defines whether the action can be selected multiple times.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActionCardinalityBehavior}.
         */
        public ActionCardinalityBehavior getCardinalityBehavior() {
            return cardinalityBehavior;
        }

        /**
         * <p>
         * A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that 
         * describes a series of actions to be taken.
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
         * A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource 
         * using the ActivityDefinition instance as the input.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getTransform() {
            return transform;
        }

        /**
         * <p>
         * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication 
         * must be computed based on the patient's weight, a customization would be used to specify an expression that calculated 
         * the weight, and the path on the resource that would contain the result.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DynamicValue}.
         */
        public List<DynamicValue> getDynamicValue() {
            return dynamicValue;
        }

        /**
         * <p>
         * Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-
         * actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen 
         * as part of realizing the action definition.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action}.
         */
        public List<PlanDefinition.Action> getAction() {
            return action;
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
                    accept(prefix, "prefix", visitor);
                    accept(title, "title", visitor);
                    accept(description, "description", visitor);
                    accept(textEquivalent, "textEquivalent", visitor);
                    accept(priority, "priority", visitor);
                    accept(code, "code", visitor, CodeableConcept.class);
                    accept(reason, "reason", visitor, CodeableConcept.class);
                    accept(documentation, "documentation", visitor, RelatedArtifact.class);
                    accept(goalId, "goalId", visitor, Id.class);
                    accept(subject, "subject", visitor);
                    accept(trigger, "trigger", visitor, TriggerDefinition.class);
                    accept(condition, "condition", visitor, Condition.class);
                    accept(input, "input", visitor, DataRequirement.class);
                    accept(output, "output", visitor, DataRequirement.class);
                    accept(relatedAction, "relatedAction", visitor, RelatedAction.class);
                    accept(timing, "timing", visitor);
                    accept(participant, "participant", visitor, Participant.class);
                    accept(type, "type", visitor);
                    accept(groupingBehavior, "groupingBehavior", visitor);
                    accept(selectionBehavior, "selectionBehavior", visitor);
                    accept(requiredBehavior, "requiredBehavior", visitor);
                    accept(precheckBehavior, "precheckBehavior", visitor);
                    accept(cardinalityBehavior, "cardinalityBehavior", visitor);
                    accept(definition, "definition", visitor);
                    accept(transform, "transform", visitor);
                    accept(dynamicValue, "dynamicValue", visitor, DynamicValue.class);
                    accept(action, "action", visitor, PlanDefinition.Action.class);
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
                Objects.equals(prefix, other.prefix) && 
                Objects.equals(title, other.title) && 
                Objects.equals(description, other.description) && 
                Objects.equals(textEquivalent, other.textEquivalent) && 
                Objects.equals(priority, other.priority) && 
                Objects.equals(code, other.code) && 
                Objects.equals(reason, other.reason) && 
                Objects.equals(documentation, other.documentation) && 
                Objects.equals(goalId, other.goalId) && 
                Objects.equals(subject, other.subject) && 
                Objects.equals(trigger, other.trigger) && 
                Objects.equals(condition, other.condition) && 
                Objects.equals(input, other.input) && 
                Objects.equals(output, other.output) && 
                Objects.equals(relatedAction, other.relatedAction) && 
                Objects.equals(timing, other.timing) && 
                Objects.equals(participant, other.participant) && 
                Objects.equals(type, other.type) && 
                Objects.equals(groupingBehavior, other.groupingBehavior) && 
                Objects.equals(selectionBehavior, other.selectionBehavior) && 
                Objects.equals(requiredBehavior, other.requiredBehavior) && 
                Objects.equals(precheckBehavior, other.precheckBehavior) && 
                Objects.equals(cardinalityBehavior, other.cardinalityBehavior) && 
                Objects.equals(definition, other.definition) && 
                Objects.equals(transform, other.transform) && 
                Objects.equals(dynamicValue, other.dynamicValue) && 
                Objects.equals(action, other.action);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    prefix, 
                    title, 
                    description, 
                    textEquivalent, 
                    priority, 
                    code, 
                    reason, 
                    documentation, 
                    goalId, 
                    subject, 
                    trigger, 
                    condition, 
                    input, 
                    output, 
                    relatedAction, 
                    timing, 
                    participant, 
                    type, 
                    groupingBehavior, 
                    selectionBehavior, 
                    requiredBehavior, 
                    precheckBehavior, 
                    cardinalityBehavior, 
                    definition, 
                    transform, 
                    dynamicValue, 
                    action);
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
            private String prefix;
            private String title;
            private String description;
            private String textEquivalent;
            private RequestPriority priority;
            private List<CodeableConcept> code = new ArrayList<>();
            private List<CodeableConcept> reason = new ArrayList<>();
            private List<RelatedArtifact> documentation = new ArrayList<>();
            private List<Id> goalId = new ArrayList<>();
            private Element subject;
            private List<TriggerDefinition> trigger = new ArrayList<>();
            private List<Condition> condition = new ArrayList<>();
            private List<DataRequirement> input = new ArrayList<>();
            private List<DataRequirement> output = new ArrayList<>();
            private List<RelatedAction> relatedAction = new ArrayList<>();
            private Element timing;
            private List<Participant> participant = new ArrayList<>();
            private CodeableConcept type;
            private ActionGroupingBehavior groupingBehavior;
            private ActionSelectionBehavior selectionBehavior;
            private ActionRequiredBehavior requiredBehavior;
            private ActionPrecheckBehavior precheckBehavior;
            private ActionCardinalityBehavior cardinalityBehavior;
            private Element definition;
            private Canonical transform;
            private List<DynamicValue> dynamicValue = new ArrayList<>();
            private List<PlanDefinition.Action> action = new ArrayList<>();

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
             * A user-visible prefix for the action.
             * </p>
             * 
             * @param prefix
             *     User-visible prefix for the action (e.g. 1. or A.)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder prefix(String prefix) {
                this.prefix = prefix;
                return this;
            }

            /**
             * <p>
             * The title of the action displayed to a user.
             * </p>
             * 
             * @param title
             *     User-visible title
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
             * A brief description of the action used to provide a summary to display to the user.
             * </p>
             * 
             * @param description
             *     Brief description of the action
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
             * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when 
             * the definition is consumed by a system that might not be capable of interpreting it dynamically.
             * </p>
             * 
             * @param textEquivalent
             *     Static text equivalent of the action, used if the dynamic aspects cannot be interpreted by the receiving system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder textEquivalent(String textEquivalent) {
                this.textEquivalent = textEquivalent;
                return this;
            }

            /**
             * <p>
             * Indicates how quickly the action should be addressed with respect to other actions.
             * </p>
             * 
             * @param priority
             *     routine | urgent | asap | stat
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder priority(RequestPriority priority) {
                this.priority = priority;
                return this;
            }

            /**
             * <p>
             * A code that provides meaning for the action or action group. For example, a section may have a LOINC code for the 
             * section of a documentation template.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param code
             *     Code representing the meaning of the action or sub-actions
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
             * A code that provides meaning for the action or action group. For example, a section may have a LOINC code for the 
             * section of a documentation template.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param code
             *     Code representing the meaning of the action or sub-actions
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
             * A description of why this action is necessary or appropriate.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param reason
             *     Why the action should be performed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reason(CodeableConcept... reason) {
                for (CodeableConcept value : reason) {
                    this.reason.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A description of why this action is necessary or appropriate.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param reason
             *     Why the action should be performed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reason(Collection<CodeableConcept> reason) {
                this.reason = new ArrayList<>(reason);
                return this;
            }

            /**
             * <p>
             * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
             * Information resources can include inline text commentary and links to web resources.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param documentation
             *     Supporting documentation for the intended performer of the action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(RelatedArtifact... documentation) {
                for (RelatedArtifact value : documentation) {
                    this.documentation.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
             * Information resources can include inline text commentary and links to web resources.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param documentation
             *     Supporting documentation for the intended performer of the action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(Collection<RelatedArtifact> documentation) {
                this.documentation = new ArrayList<>(documentation);
                return this;
            }

            /**
             * <p>
             * Identifies goals that this action supports. The reference must be to a goal element defined within this plan 
             * definition.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param goalId
             *     What goals this action supports
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder goalId(Id... goalId) {
                for (Id value : goalId) {
                    this.goalId.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Identifies goals that this action supports. The reference must be to a goal element defined within this plan 
             * definition.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param goalId
             *     What goals this action supports
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder goalId(Collection<Id> goalId) {
                this.goalId = new ArrayList<>(goalId);
                return this;
            }

            /**
             * <p>
             * A code or group definition that describes the intended subject of the action and its children, if any.
             * </p>
             * 
             * @param subject
             *     Type of individual the action is focused on
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
             * A description of when the action should be triggered.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param trigger
             *     When the action should be triggered
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder trigger(TriggerDefinition... trigger) {
                for (TriggerDefinition value : trigger) {
                    this.trigger.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A description of when the action should be triggered.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param trigger
             *     When the action should be triggered
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder trigger(Collection<TriggerDefinition> trigger) {
                this.trigger = new ArrayList<>(trigger);
                return this;
            }

            /**
             * <p>
             * An expression that describes applicability criteria or start/stop conditions for the action.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param condition
             *     Whether or not the action is applicable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder condition(Condition... condition) {
                for (Condition value : condition) {
                    this.condition.add(value);
                }
                return this;
            }

            /**
             * <p>
             * An expression that describes applicability criteria or start/stop conditions for the action.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param condition
             *     Whether or not the action is applicable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder condition(Collection<Condition> condition) {
                this.condition = new ArrayList<>(condition);
                return this;
            }

            /**
             * <p>
             * Defines input data requirements for the action.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param input
             *     Input data requirements
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder input(DataRequirement... input) {
                for (DataRequirement value : input) {
                    this.input.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Defines input data requirements for the action.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param input
             *     Input data requirements
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder input(Collection<DataRequirement> input) {
                this.input = new ArrayList<>(input);
                return this;
            }

            /**
             * <p>
             * Defines the outputs of the action, if any.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param output
             *     Output data definition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder output(DataRequirement... output) {
                for (DataRequirement value : output) {
                    this.output.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Defines the outputs of the action, if any.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param output
             *     Output data definition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder output(Collection<DataRequirement> output) {
                this.output = new ArrayList<>(output);
                return this;
            }

            /**
             * <p>
             * A relationship to another action such as "before" or "30-60 minutes after start of".
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param relatedAction
             *     Relationship to another action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder relatedAction(RelatedAction... relatedAction) {
                for (RelatedAction value : relatedAction) {
                    this.relatedAction.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A relationship to another action such as "before" or "30-60 minutes after start of".
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param relatedAction
             *     Relationship to another action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder relatedAction(Collection<RelatedAction> relatedAction) {
                this.relatedAction = new ArrayList<>(relatedAction);
                return this;
            }

            /**
             * <p>
             * An optional value describing when the action should be performed.
             * </p>
             * 
             * @param timing
             *     When the action should take place
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder timing(Element timing) {
                this.timing = timing;
                return this;
            }

            /**
             * <p>
             * Indicates who should participate in performing the action described.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param participant
             *     Who should participate in the action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder participant(Participant... participant) {
                for (Participant value : participant) {
                    this.participant.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Indicates who should participate in performing the action described.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param participant
             *     Who should participate in the action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder participant(Collection<Participant> participant) {
                this.participant = new ArrayList<>(participant);
                return this;
            }

            /**
             * <p>
             * The type of action to perform (create, update, remove).
             * </p>
             * 
             * @param type
             *     create | update | remove | fire-event
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
             * Defines the grouping behavior for the action and its children.
             * </p>
             * 
             * @param groupingBehavior
             *     visual-group | logical-group | sentence-group
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder groupingBehavior(ActionGroupingBehavior groupingBehavior) {
                this.groupingBehavior = groupingBehavior;
                return this;
            }

            /**
             * <p>
             * Defines the selection behavior for the action and its children.
             * </p>
             * 
             * @param selectionBehavior
             *     any | all | all-or-none | exactly-one | at-most-one | one-or-more
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder selectionBehavior(ActionSelectionBehavior selectionBehavior) {
                this.selectionBehavior = selectionBehavior;
                return this;
            }

            /**
             * <p>
             * Defines the required behavior for the action.
             * </p>
             * 
             * @param requiredBehavior
             *     must | could | must-unless-documented
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder requiredBehavior(ActionRequiredBehavior requiredBehavior) {
                this.requiredBehavior = requiredBehavior;
                return this;
            }

            /**
             * <p>
             * Defines whether the action should usually be preselected.
             * </p>
             * 
             * @param precheckBehavior
             *     yes | no
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder precheckBehavior(ActionPrecheckBehavior precheckBehavior) {
                this.precheckBehavior = precheckBehavior;
                return this;
            }

            /**
             * <p>
             * Defines whether the action can be selected multiple times.
             * </p>
             * 
             * @param cardinalityBehavior
             *     single | multiple
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder cardinalityBehavior(ActionCardinalityBehavior cardinalityBehavior) {
                this.cardinalityBehavior = cardinalityBehavior;
                return this;
            }

            /**
             * <p>
             * A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that 
             * describes a series of actions to be taken.
             * </p>
             * 
             * @param definition
             *     Description of the activity to be performed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder definition(Element definition) {
                this.definition = definition;
                return this;
            }

            /**
             * <p>
             * A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource 
             * using the ActivityDefinition instance as the input.
             * </p>
             * 
             * @param transform
             *     Transform to apply the template
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder transform(Canonical transform) {
                this.transform = transform;
                return this;
            }

            /**
             * <p>
             * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication 
             * must be computed based on the patient's weight, a customization would be used to specify an expression that calculated 
             * the weight, and the path on the resource that would contain the result.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param dynamicValue
             *     Dynamic aspects of the definition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dynamicValue(DynamicValue... dynamicValue) {
                for (DynamicValue value : dynamicValue) {
                    this.dynamicValue.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication 
             * must be computed based on the patient's weight, a customization would be used to specify an expression that calculated 
             * the weight, and the path on the resource that would contain the result.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param dynamicValue
             *     Dynamic aspects of the definition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dynamicValue(Collection<DynamicValue> dynamicValue) {
                this.dynamicValue = new ArrayList<>(dynamicValue);
                return this;
            }

            /**
             * <p>
             * Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-
             * actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen 
             * as part of realizing the action definition.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param action
             *     A sub-action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(PlanDefinition.Action... action) {
                for (PlanDefinition.Action value : action) {
                    this.action.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-
             * actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen 
             * as part of realizing the action definition.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param action
             *     A sub-action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(Collection<PlanDefinition.Action> action) {
                this.action = new ArrayList<>(action);
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
                prefix = action.prefix;
                title = action.title;
                description = action.description;
                textEquivalent = action.textEquivalent;
                priority = action.priority;
                code.addAll(action.code);
                reason.addAll(action.reason);
                documentation.addAll(action.documentation);
                goalId.addAll(action.goalId);
                subject = action.subject;
                trigger.addAll(action.trigger);
                condition.addAll(action.condition);
                input.addAll(action.input);
                output.addAll(action.output);
                relatedAction.addAll(action.relatedAction);
                timing = action.timing;
                participant.addAll(action.participant);
                type = action.type;
                groupingBehavior = action.groupingBehavior;
                selectionBehavior = action.selectionBehavior;
                requiredBehavior = action.requiredBehavior;
                precheckBehavior = action.precheckBehavior;
                cardinalityBehavior = action.cardinalityBehavior;
                definition = action.definition;
                transform = action.transform;
                dynamicValue.addAll(action.dynamicValue);
                this.action.addAll(action.action);
                return this;
            }
        }

        /**
         * <p>
         * An expression that describes applicability criteria or start/stop conditions for the action.
         * </p>
         */
        public static class Condition extends BackboneElement {
            private final ActionConditionKind kind;
            private final Expression expression;

            private volatile int hashCode;

            private Condition(Builder builder) {
                super(builder);
                kind = ValidationSupport.requireNonNull(builder.kind, "kind");
                expression = builder.expression;
            }

            /**
             * <p>
             * The kind of condition.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ActionConditionKind}.
             */
            public ActionConditionKind getKind() {
                return kind;
            }

            /**
             * <p>
             * An expression that returns true or false, indicating whether the condition is satisfied.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Expression}.
             */
            public Expression getExpression() {
                return expression;
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
                        accept(kind, "kind", visitor);
                        accept(expression, "expression", visitor);
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
                Condition other = (Condition) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(kind, other.kind) && 
                    Objects.equals(expression, other.expression);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        kind, 
                        expression);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(kind).from(this);
            }

            public Builder toBuilder(ActionConditionKind kind) {
                return new Builder(kind).from(this);
            }

            public static Builder builder(ActionConditionKind kind) {
                return new Builder(kind);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final ActionConditionKind kind;

                // optional
                private Expression expression;

                private Builder(ActionConditionKind kind) {
                    super();
                    this.kind = kind;
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
                 * An expression that returns true or false, indicating whether the condition is satisfied.
                 * </p>
                 * 
                 * @param expression
                 *     Boolean-valued expression
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder expression(Expression expression) {
                    this.expression = expression;
                    return this;
                }

                @Override
                public Condition build() {
                    return new Condition(this);
                }

                private Builder from(Condition condition) {
                    id = condition.id;
                    extension.addAll(condition.extension);
                    modifierExtension.addAll(condition.modifierExtension);
                    expression = condition.expression;
                    return this;
                }
            }
        }

        /**
         * <p>
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         * </p>
         */
        public static class RelatedAction extends BackboneElement {
            private final Id actionId;
            private final ActionRelationshipType relationship;
            private final Element offset;

            private volatile int hashCode;

            private RelatedAction(Builder builder) {
                super(builder);
                actionId = ValidationSupport.requireNonNull(builder.actionId, "actionId");
                relationship = ValidationSupport.requireNonNull(builder.relationship, "relationship");
                offset = ValidationSupport.choiceElement(builder.offset, "offset", Duration.class, Range.class);
            }

            /**
             * <p>
             * The element id of the related action.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Id}.
             */
            public Id getActionId() {
                return actionId;
            }

            /**
             * <p>
             * The relationship of this action to the related action.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ActionRelationshipType}.
             */
            public ActionRelationshipType getRelationship() {
                return relationship;
            }

            /**
             * <p>
             * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getOffset() {
                return offset;
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
                        accept(actionId, "actionId", visitor);
                        accept(relationship, "relationship", visitor);
                        accept(offset, "offset", visitor);
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
                RelatedAction other = (RelatedAction) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(actionId, other.actionId) && 
                    Objects.equals(relationship, other.relationship) && 
                    Objects.equals(offset, other.offset);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        actionId, 
                        relationship, 
                        offset);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(actionId, relationship).from(this);
            }

            public Builder toBuilder(Id actionId, ActionRelationshipType relationship) {
                return new Builder(actionId, relationship).from(this);
            }

            public static Builder builder(Id actionId, ActionRelationshipType relationship) {
                return new Builder(actionId, relationship);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Id actionId;
                private final ActionRelationshipType relationship;

                // optional
                private Element offset;

                private Builder(Id actionId, ActionRelationshipType relationship) {
                    super();
                    this.actionId = actionId;
                    this.relationship = relationship;
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
                 * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
                 * </p>
                 * 
                 * @param offset
                 *     Time offset for the relationship
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder offset(Element offset) {
                    this.offset = offset;
                    return this;
                }

                @Override
                public RelatedAction build() {
                    return new RelatedAction(this);
                }

                private Builder from(RelatedAction relatedAction) {
                    id = relatedAction.id;
                    extension.addAll(relatedAction.extension);
                    modifierExtension.addAll(relatedAction.modifierExtension);
                    offset = relatedAction.offset;
                    return this;
                }
            }
        }

        /**
         * <p>
         * Indicates who should participate in performing the action described.
         * </p>
         */
        public static class Participant extends BackboneElement {
            private final ActionParticipantType type;
            private final CodeableConcept role;

            private volatile int hashCode;

            private Participant(Builder builder) {
                super(builder);
                type = ValidationSupport.requireNonNull(builder.type, "type");
                role = builder.role;
            }

            /**
             * <p>
             * The type of participant in the action.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ActionParticipantType}.
             */
            public ActionParticipantType getType() {
                return type;
            }

            /**
             * <p>
             * The role the participant should play in performing the described action.
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
                        accept(type, "type", visitor);
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
                Participant other = (Participant) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(role, other.role);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        role);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(type).from(this);
            }

            public Builder toBuilder(ActionParticipantType type) {
                return new Builder(type).from(this);
            }

            public static Builder builder(ActionParticipantType type) {
                return new Builder(type);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final ActionParticipantType type;

                // optional
                private CodeableConcept role;

                private Builder(ActionParticipantType type) {
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
                 * The role the participant should play in performing the described action.
                 * </p>
                 * 
                 * @param role
                 *     E.g. Nurse, Surgeon, Parent
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder role(CodeableConcept role) {
                    this.role = role;
                    return this;
                }

                @Override
                public Participant build() {
                    return new Participant(this);
                }

                private Builder from(Participant participant) {
                    id = participant.id;
                    extension.addAll(participant.extension);
                    modifierExtension.addAll(participant.modifierExtension);
                    role = participant.role;
                    return this;
                }
            }
        }

        /**
         * <p>
         * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication 
         * must be computed based on the patient's weight, a customization would be used to specify an expression that calculated 
         * the weight, and the path on the resource that would contain the result.
         * </p>
         */
        public static class DynamicValue extends BackboneElement {
            private final String path;
            private final Expression expression;

            private volatile int hashCode;

            private DynamicValue(Builder builder) {
                super(builder);
                path = builder.path;
                expression = builder.expression;
            }

            /**
             * <p>
             * The path to the element to be customized. This is the path on the resource that will hold the result of the 
             * calculation defined by the expression. The specified path SHALL be a FHIRPath resolveable on the specified target type 
             * of the ActivityDefinition, and SHALL consist only of identifiers, constant indexers, and a restricted subset of 
             * functions. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to 
             * traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getPath() {
                return path;
            }

            /**
             * <p>
             * An expression specifying the value of the customized element.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Expression}.
             */
            public Expression getExpression() {
                return expression;
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
                        accept(path, "path", visitor);
                        accept(expression, "expression", visitor);
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
                DynamicValue other = (DynamicValue) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(path, other.path) && 
                    Objects.equals(expression, other.expression);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        path, 
                        expression);
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
                private String path;
                private Expression expression;

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
                 * The path to the element to be customized. This is the path on the resource that will hold the result of the 
                 * calculation defined by the expression. The specified path SHALL be a FHIRPath resolveable on the specified target type 
                 * of the ActivityDefinition, and SHALL consist only of identifiers, constant indexers, and a restricted subset of 
                 * functions. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to 
                 * traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).
                 * </p>
                 * 
                 * @param path
                 *     The path to the element to be set dynamically
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder path(String path) {
                    this.path = path;
                    return this;
                }

                /**
                 * <p>
                 * An expression specifying the value of the customized element.
                 * </p>
                 * 
                 * @param expression
                 *     An expression that provides the dynamic value for the customization
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder expression(Expression expression) {
                    this.expression = expression;
                    return this;
                }

                @Override
                public DynamicValue build() {
                    return new DynamicValue(this);
                }

                private Builder from(DynamicValue dynamicValue) {
                    id = dynamicValue.id;
                    extension.addAll(dynamicValue.extension);
                    modifierExtension.addAll(dynamicValue.modifierExtension);
                    path = dynamicValue.path;
                    expression = dynamicValue.expression;
                    return this;
                }
            }
        }
    }
}
