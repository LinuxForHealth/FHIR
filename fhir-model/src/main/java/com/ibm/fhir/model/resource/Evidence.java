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
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.EvidenceVariableHandling;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The Evidence Resource provides a machine-interpretable expression of an evidence concept including the evidence 
 * variables (eg population, exposures/interventions, comparators, outcomes, measured variables, confounding variables), 
 * the statistics, and the certainty of this evidence.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "cnl-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.exists() implies name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/Evidence"
)
@Constraint(
    id = "evidence-1",
    level = "Warning",
    location = "variableDefinition.variableRole",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/variable-role",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/variable-role', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Evidence",
    generated = true
)
@Constraint(
    id = "evidence-2",
    level = "Warning",
    location = "variableDefinition.directnessMatch",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/directness",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/directness', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Evidence",
    generated = true
)
@Constraint(
    id = "evidence-3",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/synthesis-type",
    expression = "synthesisType.exists() implies (synthesisType.memberOf('http://hl7.org/fhir/ValueSet/synthesis-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Evidence",
    generated = true
)
@Constraint(
    id = "evidence-4",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/study-type",
    expression = "studyType.exists() implies (studyType.memberOf('http://hl7.org/fhir/ValueSet/study-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Evidence",
    generated = true
)
@Constraint(
    id = "evidence-5",
    level = "Warning",
    location = "statistic.statisticType",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/statistic-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/statistic-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Evidence",
    generated = true
)
@Constraint(
    id = "evidence-6",
    level = "Warning",
    location = "statistic.attributeEstimate.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/attribute-estimate-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/attribute-estimate-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Evidence",
    generated = true
)
@Constraint(
    id = "evidence-7",
    level = "Warning",
    location = "statistic.modelCharacteristic.code",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/statistic-model-code",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/statistic-model-code', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Evidence",
    generated = true
)
@Constraint(
    id = "evidence-8",
    level = "Warning",
    location = "certainty.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/certainty-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/certainty-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Evidence",
    generated = true
)
@Constraint(
    id = "evidence-9",
    level = "Warning",
    location = "certainty.rating",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/certainty-rating",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/certainty-rating', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Evidence",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Evidence extends DomainResource {
    @Summary
    private final Uri url;
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final String version;
    @Summary
    private final String title;
    @ReferenceTarget({ "Citation" })
    @Choice({ Reference.class, Markdown.class })
    private final Element citeAs;
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
    private final DateTime date;
    @Summary
    private final List<UsageContext> useContext;
    private final Date approvalDate;
    private final Date lastReviewDate;
    @Summary
    private final String publisher;
    @Summary
    private final List<ContactDetail> contact;
    @Summary
    private final List<ContactDetail> author;
    private final List<ContactDetail> editor;
    private final List<ContactDetail> reviewer;
    @Summary
    private final List<ContactDetail> endorser;
    private final List<RelatedArtifact> relatedArtifact;
    private final Markdown description;
    private final Markdown assertion;
    private final List<Annotation> note;
    @Required
    private final List<VariableDefinition> variableDefinition;
    @Binding(
        bindingName = "SynthesisType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Types of combining results from a body of evidence (eg. summary data meta-analysis).",
        valueSet = "http://hl7.org/fhir/ValueSet/synthesis-type"
    )
    private final CodeableConcept synthesisType;
    @Binding(
        bindingName = "StudyType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The type of study the evidence was derived from.",
        valueSet = "http://hl7.org/fhir/ValueSet/study-type"
    )
    private final CodeableConcept studyType;
    private final List<Statistic> statistic;
    private final List<Certainty> certainty;

    private Evidence(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        title = builder.title;
        citeAs = builder.citeAs;
        status = builder.status;
        date = builder.date;
        useContext = Collections.unmodifiableList(builder.useContext);
        approvalDate = builder.approvalDate;
        lastReviewDate = builder.lastReviewDate;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        author = Collections.unmodifiableList(builder.author);
        editor = Collections.unmodifiableList(builder.editor);
        reviewer = Collections.unmodifiableList(builder.reviewer);
        endorser = Collections.unmodifiableList(builder.endorser);
        relatedArtifact = Collections.unmodifiableList(builder.relatedArtifact);
        description = builder.description;
        assertion = builder.assertion;
        note = Collections.unmodifiableList(builder.note);
        variableDefinition = Collections.unmodifiableList(builder.variableDefinition);
        synthesisType = builder.synthesisType;
        studyType = builder.studyType;
        statistic = Collections.unmodifiableList(builder.statistic);
        certainty = Collections.unmodifiableList(builder.certainty);
    }

    /**
     * An absolute URI that is used to identify this evidence when it is referenced in a specification, model, design or an 
     * instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this summary is (or will be) published. This URL can be the target of a 
     * canonical reference. It SHALL remain the same when the summary is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this summary when it is represented in other formats, or referenced in a 
     * specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the summary when it is referenced in a specification, model, 
     * design or instance. This is an arbitrary value managed by the summary author and is not expected to be globally 
     * unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no 
     * expectation that versions can be placed in a lexicographical sequence.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A short, descriptive, user-friendly title for the summary.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Citation Resource or display of suggested citation for this evidence.
     * 
     * @return
     *     An immutable object of type {@link Reference} or {@link Markdown} that may be null.
     */
    public Element getCiteAs() {
        return citeAs;
    }

    /**
     * The status of this summary. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * The date (and optionally time) when the summary was published. The date must change when the business version changes 
     * and it must change if the status code changes. In addition, it should change when the substantive content of the 
     * summary changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate evidence instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
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
     * The name of the organization or individual that published the evidence.
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
     * An individiual, organization, or device primarily involved in the creation and maintenance of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getAuthor() {
        return author;
    }

    /**
     * An individiual, organization, or device primarily responsible for internal coherence of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getEditor() {
        return editor;
    }

    /**
     * An individiual, organization, or device primarily responsible for review of some aspect of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getReviewer() {
        return reviewer;
    }

    /**
     * An individiual, organization, or device responsible for officially endorsing the content for use in some setting.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getEndorser() {
        return endorser;
    }

    /**
     * Link or citation to artifact associated with the summary.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact} that may be empty.
     */
    public List<RelatedArtifact> getRelatedArtifact() {
        return relatedArtifact;
    }

    /**
     * A free text natural language description of the evidence from a consumer's perspective.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * Declarative description of the Evidence.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getAssertion() {
        return assertion;
    }

    /**
     * Footnotes and/or explanatory notes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Evidence variable such as population, exposure, or outcome.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link VariableDefinition} that is non-empty.
     */
    public List<VariableDefinition> getVariableDefinition() {
        return variableDefinition;
    }

    /**
     * The method to combine studies.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSynthesisType() {
        return synthesisType;
    }

    /**
     * The type of study that produced this evidence.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStudyType() {
        return studyType;
    }

    /**
     * Values and parameters for a single statistic.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Statistic} that may be empty.
     */
    public List<Statistic> getStatistic() {
        return statistic;
    }

    /**
     * Assessment of certainty, confidence in the estimates, or quality of the evidence.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Certainty} that may be empty.
     */
    public List<Certainty> getCertainty() {
        return certainty;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            !identifier.isEmpty() || 
            (version != null) || 
            (title != null) || 
            (citeAs != null) || 
            (status != null) || 
            (date != null) || 
            !useContext.isEmpty() || 
            (approvalDate != null) || 
            (lastReviewDate != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            !author.isEmpty() || 
            !editor.isEmpty() || 
            !reviewer.isEmpty() || 
            !endorser.isEmpty() || 
            !relatedArtifact.isEmpty() || 
            (description != null) || 
            (assertion != null) || 
            !note.isEmpty() || 
            !variableDefinition.isEmpty() || 
            (synthesisType != null) || 
            (studyType != null) || 
            !statistic.isEmpty() || 
            !certainty.isEmpty();
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
                accept(title, "title", visitor);
                accept(citeAs, "citeAs", visitor);
                accept(status, "status", visitor);
                accept(date, "date", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(approvalDate, "approvalDate", visitor);
                accept(lastReviewDate, "lastReviewDate", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(author, "author", visitor, ContactDetail.class);
                accept(editor, "editor", visitor, ContactDetail.class);
                accept(reviewer, "reviewer", visitor, ContactDetail.class);
                accept(endorser, "endorser", visitor, ContactDetail.class);
                accept(relatedArtifact, "relatedArtifact", visitor, RelatedArtifact.class);
                accept(description, "description", visitor);
                accept(assertion, "assertion", visitor);
                accept(note, "note", visitor, Annotation.class);
                accept(variableDefinition, "variableDefinition", visitor, VariableDefinition.class);
                accept(synthesisType, "synthesisType", visitor);
                accept(studyType, "studyType", visitor);
                accept(statistic, "statistic", visitor, Statistic.class);
                accept(certainty, "certainty", visitor, Certainty.class);
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
        Evidence other = (Evidence) obj;
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
            Objects.equals(title, other.title) && 
            Objects.equals(citeAs, other.citeAs) && 
            Objects.equals(status, other.status) && 
            Objects.equals(date, other.date) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(approvalDate, other.approvalDate) && 
            Objects.equals(lastReviewDate, other.lastReviewDate) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(author, other.author) && 
            Objects.equals(editor, other.editor) && 
            Objects.equals(reviewer, other.reviewer) && 
            Objects.equals(endorser, other.endorser) && 
            Objects.equals(relatedArtifact, other.relatedArtifact) && 
            Objects.equals(description, other.description) && 
            Objects.equals(assertion, other.assertion) && 
            Objects.equals(note, other.note) && 
            Objects.equals(variableDefinition, other.variableDefinition) && 
            Objects.equals(synthesisType, other.synthesisType) && 
            Objects.equals(studyType, other.studyType) && 
            Objects.equals(statistic, other.statistic) && 
            Objects.equals(certainty, other.certainty);
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
                title, 
                citeAs, 
                status, 
                date, 
                useContext, 
                approvalDate, 
                lastReviewDate, 
                publisher, 
                contact, 
                author, 
                editor, 
                reviewer, 
                endorser, 
                relatedArtifact, 
                description, 
                assertion, 
                note, 
                variableDefinition, 
                synthesisType, 
                studyType, 
                statistic, 
                certainty);
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
        private String title;
        private Element citeAs;
        private PublicationStatus status;
        private DateTime date;
        private List<UsageContext> useContext = new ArrayList<>();
        private Date approvalDate;
        private Date lastReviewDate;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private List<ContactDetail> author = new ArrayList<>();
        private List<ContactDetail> editor = new ArrayList<>();
        private List<ContactDetail> reviewer = new ArrayList<>();
        private List<ContactDetail> endorser = new ArrayList<>();
        private List<RelatedArtifact> relatedArtifact = new ArrayList<>();
        private Markdown description;
        private Markdown assertion;
        private List<Annotation> note = new ArrayList<>();
        private List<VariableDefinition> variableDefinition = new ArrayList<>();
        private CodeableConcept synthesisType;
        private CodeableConcept studyType;
        private List<Statistic> statistic = new ArrayList<>();
        private List<Certainty> certainty = new ArrayList<>();

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
         * An absolute URI that is used to identify this evidence when it is referenced in a specification, model, design or an 
         * instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this summary is (or will be) published. This URL can be the target of a 
         * canonical reference. It SHALL remain the same when the summary is stored on different servers.
         * 
         * @param url
         *     Canonical identifier for this evidence, represented as a globally unique URI
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this summary when it is represented in other formats, or referenced in a 
         * specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the summary
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
         * A formal identifier that is used to identify this summary when it is represented in other formats, or referenced in a 
         * specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the summary
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
         *     Business version of this summary
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
         * The identifier that is used to identify this version of the summary when it is referenced in a specification, model, 
         * design or instance. This is an arbitrary value managed by the summary author and is not expected to be globally 
         * unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no 
         * expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of this summary
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Name for this summary (human friendly)
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
         * A short, descriptive, user-friendly title for the summary.
         * 
         * @param title
         *     Name for this summary (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Citation Resource or display of suggested citation for this evidence.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Reference}</li>
         * <li>{@link Markdown}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Citation}</li>
         * </ul>
         * 
         * @param citeAs
         *     Citation for this evidence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder citeAs(Element citeAs) {
            this.citeAs = citeAs;
            return this;
        }

        /**
         * The status of this summary. Enables tracking the life-cycle of the content.
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
         * The date (and optionally time) when the summary was published. The date must change when the business version changes 
         * and it must change if the status code changes. In addition, it should change when the substantive content of the 
         * summary changes.
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate evidence instances.
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
         * may be used to assist with indexing and searching for appropriate evidence instances.
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
         * Convenience method for setting {@code approvalDate}.
         * 
         * @param approvalDate
         *     When the summary was approved by publisher
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
         *     When the summary was approved by publisher
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
         *     When the summary was last reviewed
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
         *     When the summary was last reviewed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
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
         * The name of the organization or individual that published the evidence.
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
         * An individiual, organization, or device primarily involved in the creation and maintenance of the content.
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
         * An individiual, organization, or device primarily involved in the creation and maintenance of the content.
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
         * An individiual, organization, or device primarily responsible for internal coherence of the content.
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
         * An individiual, organization, or device primarily responsible for internal coherence of the content.
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
         * An individiual, organization, or device primarily responsible for review of some aspect of the content.
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
         * An individiual, organization, or device primarily responsible for review of some aspect of the content.
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
         * An individiual, organization, or device responsible for officially endorsing the content for use in some setting.
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
         * An individiual, organization, or device responsible for officially endorsing the content for use in some setting.
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
         * Link or citation to artifact associated with the summary.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     Link or citation to artifact associated with the summary
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
         * Link or citation to artifact associated with the summary.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     Link or citation to artifact associated with the summary
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
         * A free text natural language description of the evidence from a consumer's perspective.
         * 
         * @param description
         *     Description of the particular summary
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * Declarative description of the Evidence.
         * 
         * @param assertion
         *     Declarative description of the Evidence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder assertion(Markdown assertion) {
            this.assertion = assertion;
            return this;
        }

        /**
         * Footnotes and/or explanatory notes.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Footnotes and/or explanatory notes
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
         * Footnotes and/or explanatory notes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Footnotes and/or explanatory notes
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * Evidence variable such as population, exposure, or outcome.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param variableDefinition
         *     Evidence variable such as population, exposure, or outcome
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder variableDefinition(VariableDefinition... variableDefinition) {
            for (VariableDefinition value : variableDefinition) {
                this.variableDefinition.add(value);
            }
            return this;
        }

        /**
         * Evidence variable such as population, exposure, or outcome.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param variableDefinition
         *     Evidence variable such as population, exposure, or outcome
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder variableDefinition(Collection<VariableDefinition> variableDefinition) {
            this.variableDefinition = new ArrayList<>(variableDefinition);
            return this;
        }

        /**
         * The method to combine studies.
         * 
         * @param synthesisType
         *     The method to combine studies
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder synthesisType(CodeableConcept synthesisType) {
            this.synthesisType = synthesisType;
            return this;
        }

        /**
         * The type of study that produced this evidence.
         * 
         * @param studyType
         *     The type of study that produced this evidence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder studyType(CodeableConcept studyType) {
            this.studyType = studyType;
            return this;
        }

        /**
         * Values and parameters for a single statistic.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param statistic
         *     Values and parameters for a single statistic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statistic(Statistic... statistic) {
            for (Statistic value : statistic) {
                this.statistic.add(value);
            }
            return this;
        }

        /**
         * Values and parameters for a single statistic.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param statistic
         *     Values and parameters for a single statistic
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder statistic(Collection<Statistic> statistic) {
            this.statistic = new ArrayList<>(statistic);
            return this;
        }

        /**
         * Assessment of certainty, confidence in the estimates, or quality of the evidence.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param certainty
         *     Certainty or quality of the evidence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder certainty(Certainty... certainty) {
            for (Certainty value : certainty) {
                this.certainty.add(value);
            }
            return this;
        }

        /**
         * Assessment of certainty, confidence in the estimates, or quality of the evidence.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param certainty
         *     Certainty or quality of the evidence
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder certainty(Collection<Certainty> certainty) {
            this.certainty = new ArrayList<>(certainty);
            return this;
        }

        /**
         * Build the {@link Evidence}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>variableDefinition</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Evidence}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Evidence per the base specification
         */
        @Override
        public Evidence build() {
            Evidence evidence = new Evidence(this);
            if (validating) {
                validate(evidence);
            }
            return evidence;
        }

        protected void validate(Evidence evidence) {
            super.validate(evidence);
            ValidationSupport.checkList(evidence.identifier, "identifier", Identifier.class);
            ValidationSupport.choiceElement(evidence.citeAs, "citeAs", Reference.class, Markdown.class);
            ValidationSupport.requireNonNull(evidence.status, "status");
            ValidationSupport.checkList(evidence.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(evidence.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(evidence.author, "author", ContactDetail.class);
            ValidationSupport.checkList(evidence.editor, "editor", ContactDetail.class);
            ValidationSupport.checkList(evidence.reviewer, "reviewer", ContactDetail.class);
            ValidationSupport.checkList(evidence.endorser, "endorser", ContactDetail.class);
            ValidationSupport.checkList(evidence.relatedArtifact, "relatedArtifact", RelatedArtifact.class);
            ValidationSupport.checkList(evidence.note, "note", Annotation.class);
            ValidationSupport.checkNonEmptyList(evidence.variableDefinition, "variableDefinition", VariableDefinition.class);
            ValidationSupport.checkList(evidence.statistic, "statistic", Statistic.class);
            ValidationSupport.checkList(evidence.certainty, "certainty", Certainty.class);
            ValidationSupport.checkReferenceType(evidence.citeAs, "citeAs", "Citation");
        }

        protected Builder from(Evidence evidence) {
            super.from(evidence);
            url = evidence.url;
            identifier.addAll(evidence.identifier);
            version = evidence.version;
            title = evidence.title;
            citeAs = evidence.citeAs;
            status = evidence.status;
            date = evidence.date;
            useContext.addAll(evidence.useContext);
            approvalDate = evidence.approvalDate;
            lastReviewDate = evidence.lastReviewDate;
            publisher = evidence.publisher;
            contact.addAll(evidence.contact);
            author.addAll(evidence.author);
            editor.addAll(evidence.editor);
            reviewer.addAll(evidence.reviewer);
            endorser.addAll(evidence.endorser);
            relatedArtifact.addAll(evidence.relatedArtifact);
            description = evidence.description;
            assertion = evidence.assertion;
            note.addAll(evidence.note);
            variableDefinition.addAll(evidence.variableDefinition);
            synthesisType = evidence.synthesisType;
            studyType = evidence.studyType;
            statistic.addAll(evidence.statistic);
            certainty.addAll(evidence.certainty);
            return this;
        }
    }

    /**
     * Evidence variable such as population, exposure, or outcome.
     */
    public static class VariableDefinition extends BackboneElement {
        private final Markdown description;
        private final List<Annotation> note;
        @Summary
        @Binding(
            bindingName = "EvidenceVariableRole",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The role that the assertion variable plays.",
            valueSet = "http://hl7.org/fhir/ValueSet/variable-role"
        )
        @Required
        private final CodeableConcept variableRole;
        @Summary
        @ReferenceTarget({ "Group", "EvidenceVariable" })
        private final Reference observed;
        @ReferenceTarget({ "Group", "EvidenceVariable" })
        private final Reference intended;
        @Binding(
            bindingName = "EvidenceDirectness",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The quality of how direct the match is.",
            valueSet = "http://hl7.org/fhir/ValueSet/directness"
        )
        private final CodeableConcept directnessMatch;

        private VariableDefinition(Builder builder) {
            super(builder);
            description = builder.description;
            note = Collections.unmodifiableList(builder.note);
            variableRole = builder.variableRole;
            observed = builder.observed;
            intended = builder.intended;
            directnessMatch = builder.directnessMatch;
        }

        /**
         * A text description or summary of the variable.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDescription() {
            return description;
        }

        /**
         * Footnotes and/or explanatory notes.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
         */
        public List<Annotation> getNote() {
            return note;
        }

        /**
         * population | subpopulation | exposure | referenceExposure | measuredVariable | confounder.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getVariableRole() {
            return variableRole;
        }

        /**
         * Definition of the actual variable related to the statistic(s).
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getObserved() {
            return observed;
        }

        /**
         * Definition of the intended variable related to the Evidence.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getIntended() {
            return intended;
        }

        /**
         * Indication of quality of match between intended variable to actual variable.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getDirectnessMatch() {
            return directnessMatch;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                !note.isEmpty() || 
                (variableRole != null) || 
                (observed != null) || 
                (intended != null) || 
                (directnessMatch != null);
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
                    accept(description, "description", visitor);
                    accept(note, "note", visitor, Annotation.class);
                    accept(variableRole, "variableRole", visitor);
                    accept(observed, "observed", visitor);
                    accept(intended, "intended", visitor);
                    accept(directnessMatch, "directnessMatch", visitor);
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
            VariableDefinition other = (VariableDefinition) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(note, other.note) && 
                Objects.equals(variableRole, other.variableRole) && 
                Objects.equals(observed, other.observed) && 
                Objects.equals(intended, other.intended) && 
                Objects.equals(directnessMatch, other.directnessMatch);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    note, 
                    variableRole, 
                    observed, 
                    intended, 
                    directnessMatch);
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
            private Markdown description;
            private List<Annotation> note = new ArrayList<>();
            private CodeableConcept variableRole;
            private Reference observed;
            private Reference intended;
            private CodeableConcept directnessMatch;

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
             * A text description or summary of the variable.
             * 
             * @param description
             *     A text description or summary of the variable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(Markdown description) {
                this.description = description;
                return this;
            }

            /**
             * Footnotes and/or explanatory notes.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Footnotes and/or explanatory notes
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
             * Footnotes and/or explanatory notes.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Footnotes and/or explanatory notes
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder note(Collection<Annotation> note) {
                this.note = new ArrayList<>(note);
                return this;
            }

            /**
             * population | subpopulation | exposure | referenceExposure | measuredVariable | confounder.
             * 
             * <p>This element is required.
             * 
             * @param variableRole
             *     population | subpopulation | exposure | referenceExposure | measuredVariable | confounder
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder variableRole(CodeableConcept variableRole) {
                this.variableRole = variableRole;
                return this;
            }

            /**
             * Definition of the actual variable related to the statistic(s).
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Group}</li>
             * <li>{@link EvidenceVariable}</li>
             * </ul>
             * 
             * @param observed
             *     Definition of the actual variable related to the statistic(s)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder observed(Reference observed) {
                this.observed = observed;
                return this;
            }

            /**
             * Definition of the intended variable related to the Evidence.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Group}</li>
             * <li>{@link EvidenceVariable}</li>
             * </ul>
             * 
             * @param intended
             *     Definition of the intended variable related to the Evidence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder intended(Reference intended) {
                this.intended = intended;
                return this;
            }

            /**
             * Indication of quality of match between intended variable to actual variable.
             * 
             * @param directnessMatch
             *     low | moderate | high | exact
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder directnessMatch(CodeableConcept directnessMatch) {
                this.directnessMatch = directnessMatch;
                return this;
            }

            /**
             * Build the {@link VariableDefinition}
             * 
             * <p>Required elements:
             * <ul>
             * <li>variableRole</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link VariableDefinition}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid VariableDefinition per the base specification
             */
            @Override
            public VariableDefinition build() {
                VariableDefinition variableDefinition = new VariableDefinition(this);
                if (validating) {
                    validate(variableDefinition);
                }
                return variableDefinition;
            }

            protected void validate(VariableDefinition variableDefinition) {
                super.validate(variableDefinition);
                ValidationSupport.checkList(variableDefinition.note, "note", Annotation.class);
                ValidationSupport.requireNonNull(variableDefinition.variableRole, "variableRole");
                ValidationSupport.checkReferenceType(variableDefinition.observed, "observed", "Group", "EvidenceVariable");
                ValidationSupport.checkReferenceType(variableDefinition.intended, "intended", "Group", "EvidenceVariable");
                ValidationSupport.requireValueOrChildren(variableDefinition);
            }

            protected Builder from(VariableDefinition variableDefinition) {
                super.from(variableDefinition);
                description = variableDefinition.description;
                note.addAll(variableDefinition.note);
                variableRole = variableDefinition.variableRole;
                observed = variableDefinition.observed;
                intended = variableDefinition.intended;
                directnessMatch = variableDefinition.directnessMatch;
                return this;
            }
        }
    }

    /**
     * Values and parameters for a single statistic.
     */
    public static class Statistic extends BackboneElement {
        private final String description;
        private final List<Annotation> note;
        @Binding(
            bindingName = "StatisticType",
            strength = BindingStrength.Value.EXTENSIBLE,
            valueSet = "http://hl7.org/fhir/ValueSet/statistic-type"
        )
        private final CodeableConcept statisticType;
        private final CodeableConcept category;
        private final Quantity quantity;
        private final UnsignedInt numberOfEvents;
        private final UnsignedInt numberAffected;
        private final SampleSize sampleSize;
        private final List<AttributeEstimate> attributeEstimate;
        private final List<ModelCharacteristic> modelCharacteristic;

        private Statistic(Builder builder) {
            super(builder);
            description = builder.description;
            note = Collections.unmodifiableList(builder.note);
            statisticType = builder.statisticType;
            category = builder.category;
            quantity = builder.quantity;
            numberOfEvents = builder.numberOfEvents;
            numberAffected = builder.numberAffected;
            sampleSize = builder.sampleSize;
            attributeEstimate = Collections.unmodifiableList(builder.attributeEstimate);
            modelCharacteristic = Collections.unmodifiableList(builder.modelCharacteristic);
        }

        /**
         * A description of the content value of the statistic.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Footnotes and/or explanatory notes.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
         */
        public List<Annotation> getNote() {
            return note;
        }

        /**
         * Type of statistic, eg relative risk.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStatisticType() {
            return statisticType;
        }

        /**
         * When the measured variable is handled categorically, the category element is used to define which category the 
         * statistic is reporting.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * Statistic value.
         * 
         * @return
         *     An immutable object of type {@link Quantity} that may be null.
         */
        public Quantity getQuantity() {
            return quantity;
        }

        /**
         * The number of events associated with the statistic, where the unit of analysis is different from numberAffected, 
         * sampleSize.knownDataCount and sampleSize.numberOfParticipants.
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt} that may be null.
         */
        public UnsignedInt getNumberOfEvents() {
            return numberOfEvents;
        }

        /**
         * The number of participants affected where the unit of analysis is the same as sampleSize.knownDataCount and sampleSize.
         * numberOfParticipants.
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt} that may be null.
         */
        public UnsignedInt getNumberAffected() {
            return numberAffected;
        }

        /**
         * Number of samples in the statistic.
         * 
         * @return
         *     An immutable object of type {@link SampleSize} that may be null.
         */
        public SampleSize getSampleSize() {
            return sampleSize;
        }

        /**
         * A statistical attribute of the statistic such as a measure of heterogeneity.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link AttributeEstimate} that may be empty.
         */
        public List<AttributeEstimate> getAttributeEstimate() {
            return attributeEstimate;
        }

        /**
         * A component of the method to generate the statistic.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ModelCharacteristic} that may be empty.
         */
        public List<ModelCharacteristic> getModelCharacteristic() {
            return modelCharacteristic;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                !note.isEmpty() || 
                (statisticType != null) || 
                (category != null) || 
                (quantity != null) || 
                (numberOfEvents != null) || 
                (numberAffected != null) || 
                (sampleSize != null) || 
                !attributeEstimate.isEmpty() || 
                !modelCharacteristic.isEmpty();
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
                    accept(description, "description", visitor);
                    accept(note, "note", visitor, Annotation.class);
                    accept(statisticType, "statisticType", visitor);
                    accept(category, "category", visitor);
                    accept(quantity, "quantity", visitor);
                    accept(numberOfEvents, "numberOfEvents", visitor);
                    accept(numberAffected, "numberAffected", visitor);
                    accept(sampleSize, "sampleSize", visitor);
                    accept(attributeEstimate, "attributeEstimate", visitor, AttributeEstimate.class);
                    accept(modelCharacteristic, "modelCharacteristic", visitor, ModelCharacteristic.class);
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
            Statistic other = (Statistic) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(note, other.note) && 
                Objects.equals(statisticType, other.statisticType) && 
                Objects.equals(category, other.category) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(numberOfEvents, other.numberOfEvents) && 
                Objects.equals(numberAffected, other.numberAffected) && 
                Objects.equals(sampleSize, other.sampleSize) && 
                Objects.equals(attributeEstimate, other.attributeEstimate) && 
                Objects.equals(modelCharacteristic, other.modelCharacteristic);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    note, 
                    statisticType, 
                    category, 
                    quantity, 
                    numberOfEvents, 
                    numberAffected, 
                    sampleSize, 
                    attributeEstimate, 
                    modelCharacteristic);
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
            private String description;
            private List<Annotation> note = new ArrayList<>();
            private CodeableConcept statisticType;
            private CodeableConcept category;
            private Quantity quantity;
            private UnsignedInt numberOfEvents;
            private UnsignedInt numberAffected;
            private SampleSize sampleSize;
            private List<AttributeEstimate> attributeEstimate = new ArrayList<>();
            private List<ModelCharacteristic> modelCharacteristic = new ArrayList<>();

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
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Description of content
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #description(com.ibm.fhir.model.type.String)
             */
            public Builder description(java.lang.String description) {
                this.description = (description == null) ? null : String.of(description);
                return this;
            }

            /**
             * A description of the content value of the statistic.
             * 
             * @param description
             *     Description of content
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Footnotes and/or explanatory notes.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Footnotes and/or explanatory notes
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
             * Footnotes and/or explanatory notes.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Footnotes and/or explanatory notes
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder note(Collection<Annotation> note) {
                this.note = new ArrayList<>(note);
                return this;
            }

            /**
             * Type of statistic, eg relative risk.
             * 
             * @param statisticType
             *     Type of statistic, eg relative risk
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder statisticType(CodeableConcept statisticType) {
                this.statisticType = statisticType;
                return this;
            }

            /**
             * When the measured variable is handled categorically, the category element is used to define which category the 
             * statistic is reporting.
             * 
             * @param category
             *     Associated category for categorical variable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder category(CodeableConcept category) {
                this.category = category;
                return this;
            }

            /**
             * Statistic value.
             * 
             * @param quantity
             *     Statistic value
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(Quantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * The number of events associated with the statistic, where the unit of analysis is different from numberAffected, 
             * sampleSize.knownDataCount and sampleSize.numberOfParticipants.
             * 
             * @param numberOfEvents
             *     The number of events associated with the statistic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder numberOfEvents(UnsignedInt numberOfEvents) {
                this.numberOfEvents = numberOfEvents;
                return this;
            }

            /**
             * The number of participants affected where the unit of analysis is the same as sampleSize.knownDataCount and sampleSize.
             * numberOfParticipants.
             * 
             * @param numberAffected
             *     The number of participants affected
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder numberAffected(UnsignedInt numberAffected) {
                this.numberAffected = numberAffected;
                return this;
            }

            /**
             * Number of samples in the statistic.
             * 
             * @param sampleSize
             *     Number of samples in the statistic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sampleSize(SampleSize sampleSize) {
                this.sampleSize = sampleSize;
                return this;
            }

            /**
             * A statistical attribute of the statistic such as a measure of heterogeneity.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param attributeEstimate
             *     An attribute of the Statistic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder attributeEstimate(AttributeEstimate... attributeEstimate) {
                for (AttributeEstimate value : attributeEstimate) {
                    this.attributeEstimate.add(value);
                }
                return this;
            }

            /**
             * A statistical attribute of the statistic such as a measure of heterogeneity.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param attributeEstimate
             *     An attribute of the Statistic
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder attributeEstimate(Collection<AttributeEstimate> attributeEstimate) {
                this.attributeEstimate = new ArrayList<>(attributeEstimate);
                return this;
            }

            /**
             * A component of the method to generate the statistic.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modelCharacteristic
             *     An aspect of the statistical model
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder modelCharacteristic(ModelCharacteristic... modelCharacteristic) {
                for (ModelCharacteristic value : modelCharacteristic) {
                    this.modelCharacteristic.add(value);
                }
                return this;
            }

            /**
             * A component of the method to generate the statistic.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modelCharacteristic
             *     An aspect of the statistical model
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder modelCharacteristic(Collection<ModelCharacteristic> modelCharacteristic) {
                this.modelCharacteristic = new ArrayList<>(modelCharacteristic);
                return this;
            }

            /**
             * Build the {@link Statistic}
             * 
             * @return
             *     An immutable object of type {@link Statistic}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Statistic per the base specification
             */
            @Override
            public Statistic build() {
                Statistic statistic = new Statistic(this);
                if (validating) {
                    validate(statistic);
                }
                return statistic;
            }

            protected void validate(Statistic statistic) {
                super.validate(statistic);
                ValidationSupport.checkList(statistic.note, "note", Annotation.class);
                ValidationSupport.checkList(statistic.attributeEstimate, "attributeEstimate", AttributeEstimate.class);
                ValidationSupport.checkList(statistic.modelCharacteristic, "modelCharacteristic", ModelCharacteristic.class);
                ValidationSupport.requireValueOrChildren(statistic);
            }

            protected Builder from(Statistic statistic) {
                super.from(statistic);
                description = statistic.description;
                note.addAll(statistic.note);
                statisticType = statistic.statisticType;
                category = statistic.category;
                quantity = statistic.quantity;
                numberOfEvents = statistic.numberOfEvents;
                numberAffected = statistic.numberAffected;
                sampleSize = statistic.sampleSize;
                attributeEstimate.addAll(statistic.attributeEstimate);
                modelCharacteristic.addAll(statistic.modelCharacteristic);
                return this;
            }
        }

        /**
         * Number of samples in the statistic.
         */
        public static class SampleSize extends BackboneElement {
            private final String description;
            private final List<Annotation> note;
            private final UnsignedInt numberOfStudies;
            private final UnsignedInt numberOfParticipants;
            private final UnsignedInt knownDataCount;

            private SampleSize(Builder builder) {
                super(builder);
                description = builder.description;
                note = Collections.unmodifiableList(builder.note);
                numberOfStudies = builder.numberOfStudies;
                numberOfParticipants = builder.numberOfParticipants;
                knownDataCount = builder.knownDataCount;
            }

            /**
             * Human-readable summary of population sample size.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDescription() {
                return description;
            }

            /**
             * Footnote or explanatory note about the sample size.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
             */
            public List<Annotation> getNote() {
                return note;
            }

            /**
             * Number of participants in the population.
             * 
             * @return
             *     An immutable object of type {@link UnsignedInt} that may be null.
             */
            public UnsignedInt getNumberOfStudies() {
                return numberOfStudies;
            }

            /**
             * A human-readable string to clarify or explain concepts about the sample size.
             * 
             * @return
             *     An immutable object of type {@link UnsignedInt} that may be null.
             */
            public UnsignedInt getNumberOfParticipants() {
                return numberOfParticipants;
            }

            /**
             * Number of participants with known results for measured variables.
             * 
             * @return
             *     An immutable object of type {@link UnsignedInt} that may be null.
             */
            public UnsignedInt getKnownDataCount() {
                return knownDataCount;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (description != null) || 
                    !note.isEmpty() || 
                    (numberOfStudies != null) || 
                    (numberOfParticipants != null) || 
                    (knownDataCount != null);
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
                        accept(description, "description", visitor);
                        accept(note, "note", visitor, Annotation.class);
                        accept(numberOfStudies, "numberOfStudies", visitor);
                        accept(numberOfParticipants, "numberOfParticipants", visitor);
                        accept(knownDataCount, "knownDataCount", visitor);
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
                SampleSize other = (SampleSize) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(description, other.description) && 
                    Objects.equals(note, other.note) && 
                    Objects.equals(numberOfStudies, other.numberOfStudies) && 
                    Objects.equals(numberOfParticipants, other.numberOfParticipants) && 
                    Objects.equals(knownDataCount, other.knownDataCount);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        description, 
                        note, 
                        numberOfStudies, 
                        numberOfParticipants, 
                        knownDataCount);
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
                private String description;
                private List<Annotation> note = new ArrayList<>();
                private UnsignedInt numberOfStudies;
                private UnsignedInt numberOfParticipants;
                private UnsignedInt knownDataCount;

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
                 * Convenience method for setting {@code description}.
                 * 
                 * @param description
                 *     Textual description of sample size for statistic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #description(com.ibm.fhir.model.type.String)
                 */
                public Builder description(java.lang.String description) {
                    this.description = (description == null) ? null : String.of(description);
                    return this;
                }

                /**
                 * Human-readable summary of population sample size.
                 * 
                 * @param description
                 *     Textual description of sample size for statistic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Footnote or explanatory note about the sample size.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param note
                 *     Footnote or explanatory note about the sample size
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
                 * Footnote or explanatory note about the sample size.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param note
                 *     Footnote or explanatory note about the sample size
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder note(Collection<Annotation> note) {
                    this.note = new ArrayList<>(note);
                    return this;
                }

                /**
                 * Number of participants in the population.
                 * 
                 * @param numberOfStudies
                 *     Number of contributing studies
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder numberOfStudies(UnsignedInt numberOfStudies) {
                    this.numberOfStudies = numberOfStudies;
                    return this;
                }

                /**
                 * A human-readable string to clarify or explain concepts about the sample size.
                 * 
                 * @param numberOfParticipants
                 *     Cumulative number of participants
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder numberOfParticipants(UnsignedInt numberOfParticipants) {
                    this.numberOfParticipants = numberOfParticipants;
                    return this;
                }

                /**
                 * Number of participants with known results for measured variables.
                 * 
                 * @param knownDataCount
                 *     Number of participants with known results for measured variables
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder knownDataCount(UnsignedInt knownDataCount) {
                    this.knownDataCount = knownDataCount;
                    return this;
                }

                /**
                 * Build the {@link SampleSize}
                 * 
                 * @return
                 *     An immutable object of type {@link SampleSize}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid SampleSize per the base specification
                 */
                @Override
                public SampleSize build() {
                    SampleSize sampleSize = new SampleSize(this);
                    if (validating) {
                        validate(sampleSize);
                    }
                    return sampleSize;
                }

                protected void validate(SampleSize sampleSize) {
                    super.validate(sampleSize);
                    ValidationSupport.checkList(sampleSize.note, "note", Annotation.class);
                    ValidationSupport.requireValueOrChildren(sampleSize);
                }

                protected Builder from(SampleSize sampleSize) {
                    super.from(sampleSize);
                    description = sampleSize.description;
                    note.addAll(sampleSize.note);
                    numberOfStudies = sampleSize.numberOfStudies;
                    numberOfParticipants = sampleSize.numberOfParticipants;
                    knownDataCount = sampleSize.knownDataCount;
                    return this;
                }
            }
        }

        /**
         * A statistical attribute of the statistic such as a measure of heterogeneity.
         */
        public static class AttributeEstimate extends BackboneElement {
            private final String description;
            private final List<Annotation> note;
            @Binding(
                bindingName = "AttributeEstimateType",
                strength = BindingStrength.Value.EXTENSIBLE,
                valueSet = "http://hl7.org/fhir/ValueSet/attribute-estimate-type"
            )
            private final CodeableConcept type;
            private final Quantity quantity;
            private final Decimal level;
            private final Range range;
            private final List<Evidence.Statistic.AttributeEstimate> attributeEstimate;

            private AttributeEstimate(Builder builder) {
                super(builder);
                description = builder.description;
                note = Collections.unmodifiableList(builder.note);
                type = builder.type;
                quantity = builder.quantity;
                level = builder.level;
                range = builder.range;
                attributeEstimate = Collections.unmodifiableList(builder.attributeEstimate);
            }

            /**
             * Human-readable summary of the estimate.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDescription() {
                return description;
            }

            /**
             * Footnote or explanatory note about the estimate.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
             */
            public List<Annotation> getNote() {
                return note;
            }

            /**
             * The type of attribute estimate, eg confidence interval or p value.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * The singular quantity of the attribute estimate, for attribute estimates represented as single values; also used to 
             * report unit of measure.
             * 
             * @return
             *     An immutable object of type {@link Quantity} that may be null.
             */
            public Quantity getQuantity() {
                return quantity;
            }

            /**
             * Use 95 for a 95% confidence interval.
             * 
             * @return
             *     An immutable object of type {@link Decimal} that may be null.
             */
            public Decimal getLevel() {
                return level;
            }

            /**
             * Lower bound of confidence interval.
             * 
             * @return
             *     An immutable object of type {@link Range} that may be null.
             */
            public Range getRange() {
                return range;
            }

            /**
             * A nested attribute estimate; which is the attribute estimate of an attribute estimate.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link AttributeEstimate} that may be empty.
             */
            public List<Evidence.Statistic.AttributeEstimate> getAttributeEstimate() {
                return attributeEstimate;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (description != null) || 
                    !note.isEmpty() || 
                    (type != null) || 
                    (quantity != null) || 
                    (level != null) || 
                    (range != null) || 
                    !attributeEstimate.isEmpty();
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
                        accept(description, "description", visitor);
                        accept(note, "note", visitor, Annotation.class);
                        accept(type, "type", visitor);
                        accept(quantity, "quantity", visitor);
                        accept(level, "level", visitor);
                        accept(range, "range", visitor);
                        accept(attributeEstimate, "attributeEstimate", visitor, Evidence.Statistic.AttributeEstimate.class);
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
                AttributeEstimate other = (AttributeEstimate) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(description, other.description) && 
                    Objects.equals(note, other.note) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(quantity, other.quantity) && 
                    Objects.equals(level, other.level) && 
                    Objects.equals(range, other.range) && 
                    Objects.equals(attributeEstimate, other.attributeEstimate);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        description, 
                        note, 
                        type, 
                        quantity, 
                        level, 
                        range, 
                        attributeEstimate);
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
                private String description;
                private List<Annotation> note = new ArrayList<>();
                private CodeableConcept type;
                private Quantity quantity;
                private Decimal level;
                private Range range;
                private List<Evidence.Statistic.AttributeEstimate> attributeEstimate = new ArrayList<>();

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
                 * Convenience method for setting {@code description}.
                 * 
                 * @param description
                 *     Textual description of the attribute estimate
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #description(com.ibm.fhir.model.type.String)
                 */
                public Builder description(java.lang.String description) {
                    this.description = (description == null) ? null : String.of(description);
                    return this;
                }

                /**
                 * Human-readable summary of the estimate.
                 * 
                 * @param description
                 *     Textual description of the attribute estimate
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Footnote or explanatory note about the estimate.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param note
                 *     Footnote or explanatory note about the estimate
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
                 * Footnote or explanatory note about the estimate.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param note
                 *     Footnote or explanatory note about the estimate
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder note(Collection<Annotation> note) {
                    this.note = new ArrayList<>(note);
                    return this;
                }

                /**
                 * The type of attribute estimate, eg confidence interval or p value.
                 * 
                 * @param type
                 *     The type of attribute estimate, eg confidence interval or p value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * The singular quantity of the attribute estimate, for attribute estimates represented as single values; also used to 
                 * report unit of measure.
                 * 
                 * @param quantity
                 *     The singular quantity of the attribute estimate, for attribute estimates represented as single values; also used to 
                 *     report unit of measure
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder quantity(Quantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * Use 95 for a 95% confidence interval.
                 * 
                 * @param level
                 *     Level of confidence interval, eg 0.95 for 95% confidence interval
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder level(Decimal level) {
                    this.level = level;
                    return this;
                }

                /**
                 * Lower bound of confidence interval.
                 * 
                 * @param range
                 *     Lower and upper bound values of the attribute estimate
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder range(Range range) {
                    this.range = range;
                    return this;
                }

                /**
                 * A nested attribute estimate; which is the attribute estimate of an attribute estimate.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param attributeEstimate
                 *     A nested attribute estimate; which is the attribute estimate of an attribute estimate
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder attributeEstimate(Evidence.Statistic.AttributeEstimate... attributeEstimate) {
                    for (Evidence.Statistic.AttributeEstimate value : attributeEstimate) {
                        this.attributeEstimate.add(value);
                    }
                    return this;
                }

                /**
                 * A nested attribute estimate; which is the attribute estimate of an attribute estimate.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param attributeEstimate
                 *     A nested attribute estimate; which is the attribute estimate of an attribute estimate
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder attributeEstimate(Collection<Evidence.Statistic.AttributeEstimate> attributeEstimate) {
                    this.attributeEstimate = new ArrayList<>(attributeEstimate);
                    return this;
                }

                /**
                 * Build the {@link AttributeEstimate}
                 * 
                 * @return
                 *     An immutable object of type {@link AttributeEstimate}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid AttributeEstimate per the base specification
                 */
                @Override
                public AttributeEstimate build() {
                    AttributeEstimate attributeEstimate = new AttributeEstimate(this);
                    if (validating) {
                        validate(attributeEstimate);
                    }
                    return attributeEstimate;
                }

                protected void validate(AttributeEstimate attributeEstimate) {
                    super.validate(attributeEstimate);
                    ValidationSupport.checkList(attributeEstimate.note, "note", Annotation.class);
                    ValidationSupport.checkList(attributeEstimate.attributeEstimate, "attributeEstimate", Evidence.Statistic.AttributeEstimate.class);
                    ValidationSupport.requireValueOrChildren(attributeEstimate);
                }

                protected Builder from(AttributeEstimate attributeEstimate) {
                    super.from(attributeEstimate);
                    description = attributeEstimate.description;
                    note.addAll(attributeEstimate.note);
                    type = attributeEstimate.type;
                    quantity = attributeEstimate.quantity;
                    level = attributeEstimate.level;
                    range = attributeEstimate.range;
                    this.attributeEstimate.addAll(attributeEstimate.attributeEstimate);
                    return this;
                }
            }
        }

        /**
         * A component of the method to generate the statistic.
         */
        public static class ModelCharacteristic extends BackboneElement {
            @Binding(
                bindingName = "StatisticModelCode",
                strength = BindingStrength.Value.EXTENSIBLE,
                valueSet = "http://hl7.org/fhir/ValueSet/statistic-model-code"
            )
            @Required
            private final CodeableConcept code;
            private final SimpleQuantity value;
            private final List<Variable> variable;
            private final List<Evidence.Statistic.AttributeEstimate> attributeEstimate;

            private ModelCharacteristic(Builder builder) {
                super(builder);
                code = builder.code;
                value = builder.value;
                variable = Collections.unmodifiableList(builder.variable);
                attributeEstimate = Collections.unmodifiableList(builder.attributeEstimate);
            }

            /**
             * Description of a component of the method to generate the statistic.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * Further specification of the quantified value of the component of the method to generate the statistic.
             * 
             * @return
             *     An immutable object of type {@link SimpleQuantity} that may be null.
             */
            public SimpleQuantity getValue() {
                return value;
            }

            /**
             * A variable adjusted for in the adjusted analysis.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Variable} that may be empty.
             */
            public List<Variable> getVariable() {
                return variable;
            }

            /**
             * An attribute of the statistic used as a model characteristic.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link AttributeEstimate} that may be empty.
             */
            public List<Evidence.Statistic.AttributeEstimate> getAttributeEstimate() {
                return attributeEstimate;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (code != null) || 
                    (value != null) || 
                    !variable.isEmpty() || 
                    !attributeEstimate.isEmpty();
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
                        accept(code, "code", visitor);
                        accept(value, "value", visitor);
                        accept(variable, "variable", visitor, Variable.class);
                        accept(attributeEstimate, "attributeEstimate", visitor, Evidence.Statistic.AttributeEstimate.class);
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
                ModelCharacteristic other = (ModelCharacteristic) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(value, other.value) && 
                    Objects.equals(variable, other.variable) && 
                    Objects.equals(attributeEstimate, other.attributeEstimate);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        code, 
                        value, 
                        variable, 
                        attributeEstimate);
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
                private CodeableConcept code;
                private SimpleQuantity value;
                private List<Variable> variable = new ArrayList<>();
                private List<Evidence.Statistic.AttributeEstimate> attributeEstimate = new ArrayList<>();

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
                 * Description of a component of the method to generate the statistic.
                 * 
                 * <p>This element is required.
                 * 
                 * @param code
                 *     Model specification
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(CodeableConcept code) {
                    this.code = code;
                    return this;
                }

                /**
                 * Further specification of the quantified value of the component of the method to generate the statistic.
                 * 
                 * @param value
                 *     Numerical value to complete model specification
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(SimpleQuantity value) {
                    this.value = value;
                    return this;
                }

                /**
                 * A variable adjusted for in the adjusted analysis.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param variable
                 *     A variable adjusted for in the adjusted analysis
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder variable(Variable... variable) {
                    for (Variable value : variable) {
                        this.variable.add(value);
                    }
                    return this;
                }

                /**
                 * A variable adjusted for in the adjusted analysis.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param variable
                 *     A variable adjusted for in the adjusted analysis
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder variable(Collection<Variable> variable) {
                    this.variable = new ArrayList<>(variable);
                    return this;
                }

                /**
                 * An attribute of the statistic used as a model characteristic.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param attributeEstimate
                 *     An attribute of the statistic used as a model characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder attributeEstimate(Evidence.Statistic.AttributeEstimate... attributeEstimate) {
                    for (Evidence.Statistic.AttributeEstimate value : attributeEstimate) {
                        this.attributeEstimate.add(value);
                    }
                    return this;
                }

                /**
                 * An attribute of the statistic used as a model characteristic.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param attributeEstimate
                 *     An attribute of the statistic used as a model characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder attributeEstimate(Collection<Evidence.Statistic.AttributeEstimate> attributeEstimate) {
                    this.attributeEstimate = new ArrayList<>(attributeEstimate);
                    return this;
                }

                /**
                 * Build the {@link ModelCharacteristic}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>code</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link ModelCharacteristic}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid ModelCharacteristic per the base specification
                 */
                @Override
                public ModelCharacteristic build() {
                    ModelCharacteristic modelCharacteristic = new ModelCharacteristic(this);
                    if (validating) {
                        validate(modelCharacteristic);
                    }
                    return modelCharacteristic;
                }

                protected void validate(ModelCharacteristic modelCharacteristic) {
                    super.validate(modelCharacteristic);
                    ValidationSupport.requireNonNull(modelCharacteristic.code, "code");
                    ValidationSupport.checkList(modelCharacteristic.variable, "variable", Variable.class);
                    ValidationSupport.checkList(modelCharacteristic.attributeEstimate, "attributeEstimate", Evidence.Statistic.AttributeEstimate.class);
                    ValidationSupport.requireValueOrChildren(modelCharacteristic);
                }

                protected Builder from(ModelCharacteristic modelCharacteristic) {
                    super.from(modelCharacteristic);
                    code = modelCharacteristic.code;
                    value = modelCharacteristic.value;
                    variable.addAll(modelCharacteristic.variable);
                    attributeEstimate.addAll(modelCharacteristic.attributeEstimate);
                    return this;
                }
            }

            /**
             * A variable adjusted for in the adjusted analysis.
             */
            public static class Variable extends BackboneElement {
                @ReferenceTarget({ "Group", "EvidenceVariable" })
                @Required
                private final Reference variableDefinition;
                @Binding(
                    bindingName = "EvidenceVariableHandling",
                    strength = BindingStrength.Value.REQUIRED,
                    valueSet = "http://hl7.org/fhir/ValueSet/variable-handling|4.3.0-cibuild"
                )
                private final EvidenceVariableHandling handling;
                private final List<CodeableConcept> valueCategory;
                private final List<Quantity> valueQuantity;
                private final List<Range> valueRange;

                private Variable(Builder builder) {
                    super(builder);
                    variableDefinition = builder.variableDefinition;
                    handling = builder.handling;
                    valueCategory = Collections.unmodifiableList(builder.valueCategory);
                    valueQuantity = Collections.unmodifiableList(builder.valueQuantity);
                    valueRange = Collections.unmodifiableList(builder.valueRange);
                }

                /**
                 * Description of the variable.
                 * 
                 * @return
                 *     An immutable object of type {@link Reference} that is non-null.
                 */
                public Reference getVariableDefinition() {
                    return variableDefinition;
                }

                /**
                 * How the variable is classified for use in adjusted analysis.
                 * 
                 * @return
                 *     An immutable object of type {@link EvidenceVariableHandling} that may be null.
                 */
                public EvidenceVariableHandling getHandling() {
                    return handling;
                }

                /**
                 * Description for grouping of ordinal or polychotomous variables.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
                 */
                public List<CodeableConcept> getValueCategory() {
                    return valueCategory;
                }

                /**
                 * Discrete value for grouping of ordinal or polychotomous variables.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Quantity} that may be empty.
                 */
                public List<Quantity> getValueQuantity() {
                    return valueQuantity;
                }

                /**
                 * Range of values for grouping of ordinal or polychotomous variables.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Range} that may be empty.
                 */
                public List<Range> getValueRange() {
                    return valueRange;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (variableDefinition != null) || 
                        (handling != null) || 
                        !valueCategory.isEmpty() || 
                        !valueQuantity.isEmpty() || 
                        !valueRange.isEmpty();
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
                            accept(variableDefinition, "variableDefinition", visitor);
                            accept(handling, "handling", visitor);
                            accept(valueCategory, "valueCategory", visitor, CodeableConcept.class);
                            accept(valueQuantity, "valueQuantity", visitor, Quantity.class);
                            accept(valueRange, "valueRange", visitor, Range.class);
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
                    Variable other = (Variable) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(variableDefinition, other.variableDefinition) && 
                        Objects.equals(handling, other.handling) && 
                        Objects.equals(valueCategory, other.valueCategory) && 
                        Objects.equals(valueQuantity, other.valueQuantity) && 
                        Objects.equals(valueRange, other.valueRange);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            variableDefinition, 
                            handling, 
                            valueCategory, 
                            valueQuantity, 
                            valueRange);
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
                    private Reference variableDefinition;
                    private EvidenceVariableHandling handling;
                    private List<CodeableConcept> valueCategory = new ArrayList<>();
                    private List<Quantity> valueQuantity = new ArrayList<>();
                    private List<Range> valueRange = new ArrayList<>();

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
                     * Description of the variable.
                     * 
                     * <p>This element is required.
                     * 
                     * <p>Allowed resource types for this reference:
                     * <ul>
                     * <li>{@link Group}</li>
                     * <li>{@link EvidenceVariable}</li>
                     * </ul>
                     * 
                     * @param variableDefinition
                     *     Description of the variable
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder variableDefinition(Reference variableDefinition) {
                        this.variableDefinition = variableDefinition;
                        return this;
                    }

                    /**
                     * How the variable is classified for use in adjusted analysis.
                     * 
                     * @param handling
                     *     continuous | dichotomous | ordinal | polychotomous
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder handling(EvidenceVariableHandling handling) {
                        this.handling = handling;
                        return this;
                    }

                    /**
                     * Description for grouping of ordinal or polychotomous variables.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param valueCategory
                     *     Description for grouping of ordinal or polychotomous variables
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder valueCategory(CodeableConcept... valueCategory) {
                        for (CodeableConcept value : valueCategory) {
                            this.valueCategory.add(value);
                        }
                        return this;
                    }

                    /**
                     * Description for grouping of ordinal or polychotomous variables.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param valueCategory
                     *     Description for grouping of ordinal or polychotomous variables
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder valueCategory(Collection<CodeableConcept> valueCategory) {
                        this.valueCategory = new ArrayList<>(valueCategory);
                        return this;
                    }

                    /**
                     * Discrete value for grouping of ordinal or polychotomous variables.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param valueQuantity
                     *     Discrete value for grouping of ordinal or polychotomous variables
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder valueQuantity(Quantity... valueQuantity) {
                        for (Quantity value : valueQuantity) {
                            this.valueQuantity.add(value);
                        }
                        return this;
                    }

                    /**
                     * Discrete value for grouping of ordinal or polychotomous variables.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param valueQuantity
                     *     Discrete value for grouping of ordinal or polychotomous variables
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder valueQuantity(Collection<Quantity> valueQuantity) {
                        this.valueQuantity = new ArrayList<>(valueQuantity);
                        return this;
                    }

                    /**
                     * Range of values for grouping of ordinal or polychotomous variables.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param valueRange
                     *     Range of values for grouping of ordinal or polychotomous variables
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder valueRange(Range... valueRange) {
                        for (Range value : valueRange) {
                            this.valueRange.add(value);
                        }
                        return this;
                    }

                    /**
                     * Range of values for grouping of ordinal or polychotomous variables.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param valueRange
                     *     Range of values for grouping of ordinal or polychotomous variables
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder valueRange(Collection<Range> valueRange) {
                        this.valueRange = new ArrayList<>(valueRange);
                        return this;
                    }

                    /**
                     * Build the {@link Variable}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>variableDefinition</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Variable}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Variable per the base specification
                     */
                    @Override
                    public Variable build() {
                        Variable variable = new Variable(this);
                        if (validating) {
                            validate(variable);
                        }
                        return variable;
                    }

                    protected void validate(Variable variable) {
                        super.validate(variable);
                        ValidationSupport.requireNonNull(variable.variableDefinition, "variableDefinition");
                        ValidationSupport.checkList(variable.valueCategory, "valueCategory", CodeableConcept.class);
                        ValidationSupport.checkList(variable.valueQuantity, "valueQuantity", Quantity.class);
                        ValidationSupport.checkList(variable.valueRange, "valueRange", Range.class);
                        ValidationSupport.checkReferenceType(variable.variableDefinition, "variableDefinition", "Group", "EvidenceVariable");
                        ValidationSupport.requireValueOrChildren(variable);
                    }

                    protected Builder from(Variable variable) {
                        super.from(variable);
                        variableDefinition = variable.variableDefinition;
                        handling = variable.handling;
                        valueCategory.addAll(variable.valueCategory);
                        valueQuantity.addAll(variable.valueQuantity);
                        valueRange.addAll(variable.valueRange);
                        return this;
                    }
                }
            }
        }
    }

    /**
     * Assessment of certainty, confidence in the estimates, or quality of the evidence.
     */
    public static class Certainty extends BackboneElement {
        private final String description;
        private final List<Annotation> note;
        @Binding(
            bindingName = "EvidenceCertaintyType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The aspect of quality, confidence, or certainty.",
            valueSet = "http://hl7.org/fhir/ValueSet/certainty-type"
        )
        private final CodeableConcept type;
        @Binding(
            bindingName = "EvidenceCertaintyRating",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The assessment of quality, confidence, or certainty.",
            valueSet = "http://hl7.org/fhir/ValueSet/certainty-rating"
        )
        private final CodeableConcept rating;
        private final String rater;
        private final List<Evidence.Certainty> subcomponent;

        private Certainty(Builder builder) {
            super(builder);
            description = builder.description;
            note = Collections.unmodifiableList(builder.note);
            type = builder.type;
            rating = builder.rating;
            rater = builder.rater;
            subcomponent = Collections.unmodifiableList(builder.subcomponent);
        }

        /**
         * Textual description of certainty.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Footnotes and/or explanatory notes.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
         */
        public List<Annotation> getNote() {
            return note;
        }

        /**
         * Aspect of certainty being rated.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Assessment or judgement of the aspect.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRating() {
            return rating;
        }

        /**
         * Individual or group who did the rating.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getRater() {
            return rater;
        }

        /**
         * A domain or subdomain of certainty.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Certainty} that may be empty.
         */
        public List<Evidence.Certainty> getSubcomponent() {
            return subcomponent;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                !note.isEmpty() || 
                (type != null) || 
                (rating != null) || 
                (rater != null) || 
                !subcomponent.isEmpty();
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
                    accept(description, "description", visitor);
                    accept(note, "note", visitor, Annotation.class);
                    accept(type, "type", visitor);
                    accept(rating, "rating", visitor);
                    accept(rater, "rater", visitor);
                    accept(subcomponent, "subcomponent", visitor, Evidence.Certainty.class);
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
            Certainty other = (Certainty) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(note, other.note) && 
                Objects.equals(type, other.type) && 
                Objects.equals(rating, other.rating) && 
                Objects.equals(rater, other.rater) && 
                Objects.equals(subcomponent, other.subcomponent);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    note, 
                    type, 
                    rating, 
                    rater, 
                    subcomponent);
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
            private String description;
            private List<Annotation> note = new ArrayList<>();
            private CodeableConcept type;
            private CodeableConcept rating;
            private String rater;
            private List<Evidence.Certainty> subcomponent = new ArrayList<>();

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
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Textual description of certainty
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #description(com.ibm.fhir.model.type.String)
             */
            public Builder description(java.lang.String description) {
                this.description = (description == null) ? null : String.of(description);
                return this;
            }

            /**
             * Textual description of certainty.
             * 
             * @param description
             *     Textual description of certainty
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Footnotes and/or explanatory notes.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Footnotes and/or explanatory notes
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
             * Footnotes and/or explanatory notes.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Footnotes and/or explanatory notes
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder note(Collection<Annotation> note) {
                this.note = new ArrayList<>(note);
                return this;
            }

            /**
             * Aspect of certainty being rated.
             * 
             * @param type
             *     Aspect of certainty being rated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Assessment or judgement of the aspect.
             * 
             * @param rating
             *     Assessment or judgement of the aspect
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder rating(CodeableConcept rating) {
                this.rating = rating;
                return this;
            }

            /**
             * Convenience method for setting {@code rater}.
             * 
             * @param rater
             *     Individual or group who did the rating
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #rater(com.ibm.fhir.model.type.String)
             */
            public Builder rater(java.lang.String rater) {
                this.rater = (rater == null) ? null : String.of(rater);
                return this;
            }

            /**
             * Individual or group who did the rating.
             * 
             * @param rater
             *     Individual or group who did the rating
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder rater(String rater) {
                this.rater = rater;
                return this;
            }

            /**
             * A domain or subdomain of certainty.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param subcomponent
             *     A domain or subdomain of certainty
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subcomponent(Evidence.Certainty... subcomponent) {
                for (Evidence.Certainty value : subcomponent) {
                    this.subcomponent.add(value);
                }
                return this;
            }

            /**
             * A domain or subdomain of certainty.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param subcomponent
             *     A domain or subdomain of certainty
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder subcomponent(Collection<Evidence.Certainty> subcomponent) {
                this.subcomponent = new ArrayList<>(subcomponent);
                return this;
            }

            /**
             * Build the {@link Certainty}
             * 
             * @return
             *     An immutable object of type {@link Certainty}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Certainty per the base specification
             */
            @Override
            public Certainty build() {
                Certainty certainty = new Certainty(this);
                if (validating) {
                    validate(certainty);
                }
                return certainty;
            }

            protected void validate(Certainty certainty) {
                super.validate(certainty);
                ValidationSupport.checkList(certainty.note, "note", Annotation.class);
                ValidationSupport.checkList(certainty.subcomponent, "subcomponent", Evidence.Certainty.class);
                ValidationSupport.requireValueOrChildren(certainty);
            }

            protected Builder from(Certainty certainty) {
                super.from(certainty);
                description = certainty.description;
                note.addAll(certainty.note);
                type = certainty.type;
                rating = certainty.rating;
                rater = certainty.rater;
                subcomponent.addAll(certainty.subcomponent);
                return this;
            }
        }
    }
}
