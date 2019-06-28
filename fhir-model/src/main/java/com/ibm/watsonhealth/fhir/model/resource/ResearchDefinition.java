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

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RelatedArtifact;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The ResearchDefinition resource describes the conditional state (population and any exposures being compared within 
 * the population) and outcome (if specified) that the knowledge (evidence, assertion, recommendation) is about.
 * </p>
 */
@Constraint(
    id = "rsd-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ResearchDefinition extends DomainResource {
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
    private final Reference population;
    private final Reference exposure;
    private final Reference exposureAlternative;
    private final Reference outcome;

    private ResearchDefinition(Builder builder) {
        super(builder);
        this.url = builder.url;
        this.identifier = builder.identifier;
        this.version = builder.version;
        this.name = builder.name;
        this.title = builder.title;
        this.shortTitle = builder.shortTitle;
        this.subtitle = builder.subtitle;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.experimental = builder.experimental;
        this.subject = ValidationSupport.choiceElement(builder.subject, "subject", CodeableConcept.class, Reference.class);
        this.date = builder.date;
        this.publisher = builder.publisher;
        this.contact = builder.contact;
        this.description = builder.description;
        this.comment = builder.comment;
        this.useContext = builder.useContext;
        this.jurisdiction = builder.jurisdiction;
        this.purpose = builder.purpose;
        this.usage = builder.usage;
        this.copyright = builder.copyright;
        this.approvalDate = builder.approvalDate;
        this.lastReviewDate = builder.lastReviewDate;
        this.effectivePeriod = builder.effectivePeriod;
        this.topic = builder.topic;
        this.author = builder.author;
        this.editor = builder.editor;
        this.reviewer = builder.reviewer;
        this.endorser = builder.endorser;
        this.relatedArtifact = builder.relatedArtifact;
        this.library = builder.library;
        this.population = ValidationSupport.requireNonNull(builder.population, "population");
        this.exposure = builder.exposure;
        this.exposureAlternative = builder.exposureAlternative;
        this.outcome = builder.outcome;
    }

    /**
     * <p>
     * An absolute URI that is used to identify this research definition when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this research definition is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the research definition is stored on 
     * different servers.
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
     * A formal identifier that is used to identify this research definition when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the research definition when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the research definition author and is 
     * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
     * available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
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
     * A natural language name identifying the research definition. This name should be usable as an identifier for the 
     * module by machine processing applications such as code generation.
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
     * A short, descriptive, user-friendly title for the research definition.
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
     * An explanatory or alternate title for the ResearchDefinition giving additional information about its content.
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
     * The status of this research definition. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this research definition is authored for testing purposes (or 
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
     * The intended subjects for the ResearchDefinition. If this element is not provided, a Patient subject is assumed, but 
     * the subject of the ResearchDefinition can be anything.
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
     * The date (and optionally time) when the research definition was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the research definition changes.
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
     * The name of the organization or individual that published the research definition.
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
     *     A list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * <p>
     * A free text natural language description of the research definition from a consumer's perspective.
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
     *     A list containing immutable objects of type {@link String}.
     */
    public List<String> getComment() {
        return comment;
    }

    /**
     * <p>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate research definition instances.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link UsageContext}.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * <p>
     * A legal or geographic region in which the research definition is intended to be used.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * <p>
     * Explanation of why this research definition is needed and why it has been designed as it has.
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
     * A detailed description, from a clinical perspective, of how the ResearchDefinition is used.
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
     * A copyright statement relating to the research definition and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the research definition.
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
     * The period during which the research definition content was or is planned to be in active use.
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
     * Descriptive topics related to the content of the ResearchDefinition. Topics provide a high-level categorization 
     * grouping types of ResearchDefinitions that can be useful for filtering and searching.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
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
     *     A list containing immutable objects of type {@link ContactDetail}.
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
     *     A list containing immutable objects of type {@link ContactDetail}.
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
     *     A list containing immutable objects of type {@link ContactDetail}.
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
     *     A list containing immutable objects of type {@link ContactDetail}.
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
     *     A list containing immutable objects of type {@link RelatedArtifact}.
     */
    public List<RelatedArtifact> getRelatedArtifact() {
        return relatedArtifact;
    }

    /**
     * <p>
     * A reference to a Library resource containing the formal logic used by the ResearchDefinition.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getLibrary() {
        return library;
    }

    /**
     * <p>
     * A reference to a ResearchElementDefinition resource that defines the population for the research.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPopulation() {
        return population;
    }

    /**
     * <p>
     * A reference to a ResearchElementDefinition resource that defines the exposure for the research.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getExposure() {
        return exposure;
    }

    /**
     * <p>
     * A reference to a ResearchElementDefinition resource that defines the exposureAlternative for the research.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getExposureAlternative() {
        return exposureAlternative;
    }

    /**
     * <p>
     * A reference to a ResearchElementDefinition resomece that defines the outcome for the research.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOutcome() {
        return outcome;
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
                accept(subject, "subject", visitor, true);
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
                accept(population, "population", visitor);
                accept(exposure, "exposure", visitor);
                accept(exposureAlternative, "exposureAlternative", visitor);
                accept(outcome, "outcome", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, population);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.url = url;
        builder.identifier.addAll(identifier);
        builder.version = version;
        builder.name = name;
        builder.title = title;
        builder.shortTitle = shortTitle;
        builder.subtitle = subtitle;
        builder.experimental = experimental;
        builder.subject = subject;
        builder.date = date;
        builder.publisher = publisher;
        builder.contact.addAll(contact);
        builder.description = description;
        builder.comment.addAll(comment);
        builder.useContext.addAll(useContext);
        builder.jurisdiction.addAll(jurisdiction);
        builder.purpose = purpose;
        builder.usage = usage;
        builder.copyright = copyright;
        builder.approvalDate = approvalDate;
        builder.lastReviewDate = lastReviewDate;
        builder.effectivePeriod = effectivePeriod;
        builder.topic.addAll(topic);
        builder.author.addAll(author);
        builder.editor.addAll(editor);
        builder.reviewer.addAll(reviewer);
        builder.endorser.addAll(endorser);
        builder.relatedArtifact.addAll(relatedArtifact);
        builder.library.addAll(library);
        builder.exposure = exposure;
        builder.exposureAlternative = exposureAlternative;
        builder.outcome = outcome;
        return builder;
    }

    public static Builder builder(PublicationStatus status, Reference population) {
        return new Builder(status, population);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PublicationStatus status;
        private final Reference population;

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
        private Reference exposure;
        private Reference exposureAlternative;
        private Reference outcome;

        private Builder(PublicationStatus status, Reference population) {
            super();
            this.status = status;
            this.population = population;
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
         * An absolute URI that is used to identify this research definition when it is referenced in a specification, model, 
         * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
         * address at which at which an authoritative instance of this research definition is (or will be) published. This URL 
         * can be the target of a canonical reference. It SHALL remain the same when the research definition is stored on 
         * different servers.
         * </p>
         * 
         * @param url
         *     Canonical identifier for this research definition, represented as a URI (globally unique)
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
         * A formal identifier that is used to identify this research definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the research definition
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
         * A formal identifier that is used to identify this research definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the research definition
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
         * The identifier that is used to identify this version of the research definition when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the research definition author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
         * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
         * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
         * version is required for non-experimental active artifacts.
         * </p>
         * 
         * @param version
         *     Business version of the research definition
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
         * A natural language name identifying the research definition. This name should be usable as an identifier for the 
         * module by machine processing applications such as code generation.
         * </p>
         * 
         * @param name
         *     Name for this research definition (computer friendly)
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
         * A short, descriptive, user-friendly title for the research definition.
         * </p>
         * 
         * @param title
         *     Name for this research definition (human friendly)
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
         * The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is 
         * not necessary.
         * </p>
         * 
         * @param shortTitle
         *     Title for use in informal contexts
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder shortTitle(String shortTitle) {
            this.shortTitle = shortTitle;
            return this;
        }

        /**
         * <p>
         * An explanatory or alternate title for the ResearchDefinition giving additional information about its content.
         * </p>
         * 
         * @param subtitle
         *     Subordinate title of the ResearchDefinition
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
         * A Boolean value to indicate that this research definition is authored for testing purposes (or 
         * education/evaluation/marketing) and is not intended to be used for genuine usage.
         * </p>
         * 
         * @param experimental
         *     For testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder experimental(Boolean experimental) {
            this.experimental = experimental;
            return this;
        }

        /**
         * <p>
         * The intended subjects for the ResearchDefinition. If this element is not provided, a Patient subject is assumed, but 
         * the subject of the ResearchDefinition can be anything.
         * </p>
         * 
         * @param subject
         *     E.g. Patient, Practitioner, RelatedPerson, Organization, Location, Device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subject(Element subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * The date (and optionally time) when the research definition was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the research definition changes.
         * </p>
         * 
         * @param date
         *     Date last changed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * <p>
         * The name of the organization or individual that published the research definition.
         * </p>
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        /**
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact.addAll(contact);
            return this;
        }

        /**
         * <p>
         * A free text natural language description of the research definition from a consumer's perspective.
         * </p>
         * 
         * @param description
         *     Natural language description of the research definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * <p>
         * A human-readable string to clarify or explain concepts about the resource.
         * </p>
         * 
         * @param comment
         *     Used for footnotes or explanatory notes
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param comment
         *     Used for footnotes or explanatory notes
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder comment(Collection<String> comment) {
            this.comment.addAll(comment);
            return this;
        }

        /**
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate research definition instances.
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance.
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
         * may be used to assist with indexing and searching for appropriate research definition instances.
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext.addAll(useContext);
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the research definition is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for research definition (if applicable)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder jurisdiction(CodeableConcept... jurisdiction) {
            for (CodeableConcept value : jurisdiction) {
                this.jurisdiction.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the research definition is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for research definition (if applicable)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction.addAll(jurisdiction);
            return this;
        }

        /**
         * <p>
         * Explanation of why this research definition is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this research definition is defined
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * <p>
         * A detailed description, from a clinical perspective, of how the ResearchDefinition is used.
         * </p>
         * 
         * @param usage
         *     Describes the clinical usage of the ResearchDefinition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder usage(String usage) {
            this.usage = usage;
            return this;
        }

        /**
         * <p>
         * A copyright statement relating to the research definition and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the research definition.
         * </p>
         * 
         * @param copyright
         *     Use and/or publishing restrictions
         * 
         * @return
         *     A reference to this Builder instance.
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
         *     When the research definition was approved by publisher
         * 
         * @return
         *     A reference to this Builder instance.
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
         *     When the research definition was last reviewed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder lastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
            return this;
        }

        /**
         * <p>
         * The period during which the research definition content was or is planned to be in active use.
         * </p>
         * 
         * @param effectivePeriod
         *     When the research definition is expected to be used
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder effectivePeriod(Period effectivePeriod) {
            this.effectivePeriod = effectivePeriod;
            return this;
        }

        /**
         * <p>
         * Descriptive topics related to the content of the ResearchDefinition. Topics provide a high-level categorization 
         * grouping types of ResearchDefinitions that can be useful for filtering and searching.
         * </p>
         * 
         * @param topic
         *     The category of the ResearchDefinition, such as Education, Treatment, Assessment, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder topic(CodeableConcept... topic) {
            for (CodeableConcept value : topic) {
                this.topic.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Descriptive topics related to the content of the ResearchDefinition. Topics provide a high-level categorization 
         * grouping types of ResearchDefinitions that can be useful for filtering and searching.
         * </p>
         * 
         * @param topic
         *     The category of the ResearchDefinition, such as Education, Treatment, Assessment, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder topic(Collection<CodeableConcept> topic) {
            this.topic.addAll(topic);
            return this;
        }

        /**
         * <p>
         * An individiual or organization primarily involved in the creation and maintenance of the content.
         * </p>
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder author(Collection<ContactDetail> author) {
            this.author.addAll(author);
            return this;
        }

        /**
         * <p>
         * An individual or organization primarily responsible for internal coherence of the content.
         * </p>
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder editor(Collection<ContactDetail> editor) {
            this.editor.addAll(editor);
            return this;
        }

        /**
         * <p>
         * An individual or organization primarily responsible for review of some aspect of the content.
         * </p>
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reviewer(Collection<ContactDetail> reviewer) {
            this.reviewer.addAll(reviewer);
            return this;
        }

        /**
         * <p>
         * An individual or organization responsible for officially endorsing the content for use in some setting.
         * </p>
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder endorser(Collection<ContactDetail> endorser) {
            this.endorser.addAll(endorser);
            return this;
        }

        /**
         * <p>
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         * </p>
         * 
         * @param relatedArtifact
         *     Additional documentation, citations, etc.
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param relatedArtifact
         *     Additional documentation, citations, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relatedArtifact(Collection<RelatedArtifact> relatedArtifact) {
            this.relatedArtifact.addAll(relatedArtifact);
            return this;
        }

        /**
         * <p>
         * A reference to a Library resource containing the formal logic used by the ResearchDefinition.
         * </p>
         * 
         * @param library
         *     Logic used by the ResearchDefinition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder library(Canonical... library) {
            for (Canonical value : library) {
                this.library.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A reference to a Library resource containing the formal logic used by the ResearchDefinition.
         * </p>
         * 
         * @param library
         *     Logic used by the ResearchDefinition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder library(Collection<Canonical> library) {
            this.library.addAll(library);
            return this;
        }

        /**
         * <p>
         * A reference to a ResearchElementDefinition resource that defines the exposure for the research.
         * </p>
         * 
         * @param exposure
         *     What exposure?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder exposure(Reference exposure) {
            this.exposure = exposure;
            return this;
        }

        /**
         * <p>
         * A reference to a ResearchElementDefinition resource that defines the exposureAlternative for the research.
         * </p>
         * 
         * @param exposureAlternative
         *     What alternative exposure state?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder exposureAlternative(Reference exposureAlternative) {
            this.exposureAlternative = exposureAlternative;
            return this;
        }

        /**
         * <p>
         * A reference to a ResearchElementDefinition resomece that defines the outcome for the research.
         * </p>
         * 
         * @param outcome
         *     What outcome?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder outcome(Reference outcome) {
            this.outcome = outcome;
            return this;
        }

        @Override
        public ResearchDefinition build() {
            return new ResearchDefinition(this);
        }
    }
}
