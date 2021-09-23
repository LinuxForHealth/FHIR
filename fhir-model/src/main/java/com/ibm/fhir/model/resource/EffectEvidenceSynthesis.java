/*
 * (C) Copyright IBM Corp. 2019, 2021
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
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ExposureState;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The EffectEvidenceSynthesis resource describes the difference in an outcome between exposures states in a population 
 * where the effect estimate is derived from a combination of research studies.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "ees-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis"
)
@Constraint(
    id = "effectEvidenceSynthesis-1",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis",
    generated = true
)
@Constraint(
    id = "effectEvidenceSynthesis-2",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/synthesis-type",
    expression = "synthesisType.exists() implies (synthesisType.memberOf('http://hl7.org/fhir/ValueSet/synthesis-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis",
    generated = true
)
@Constraint(
    id = "effectEvidenceSynthesis-3",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/study-type",
    expression = "studyType.exists() implies (studyType.memberOf('http://hl7.org/fhir/ValueSet/study-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis",
    generated = true
)
@Constraint(
    id = "effectEvidenceSynthesis-4",
    level = "Warning",
    location = "resultsByExposure.variantState",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/evidence-variant-state",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/evidence-variant-state', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis",
    generated = true
)
@Constraint(
    id = "effectEvidenceSynthesis-5",
    level = "Warning",
    location = "effectEstimate.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/effect-estimate-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/effect-estimate-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis",
    generated = true
)
@Constraint(
    id = "effectEvidenceSynthesis-6",
    level = "Warning",
    location = "effectEstimate.variantState",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/evidence-variant-state",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/evidence-variant-state', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis",
    generated = true
)
@Constraint(
    id = "effectEvidenceSynthesis-7",
    level = "Warning",
    location = "effectEstimate.precisionEstimate.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/precision-estimate-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/precision-estimate-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis",
    generated = true
)
@Constraint(
    id = "effectEvidenceSynthesis-8",
    level = "Warning",
    location = "certainty.rating",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/evidence-quality",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/evidence-quality', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis",
    generated = true
)
@Constraint(
    id = "effectEvidenceSynthesis-9",
    level = "Warning",
    location = "certainty.certaintySubcomponent.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/certainty-subcomponent-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/certainty-subcomponent-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis",
    generated = true
)
@Constraint(
    id = "effectEvidenceSynthesis-10",
    level = "Warning",
    location = "certainty.certaintySubcomponent.rating",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/certainty-subcomponent-rating",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/certainty-subcomponent-rating', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/EffectEvidenceSynthesis",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class EffectEvidenceSynthesis extends DomainResource {
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
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.0.1"
    )
    @Required
    private final PublicationStatus status;
    @Summary
    private final DateTime date;
    @Summary
    private final String publisher;
    @Summary
    private final List<ContactDetail> contact;
    @Summary
    private final Markdown description;
    private final List<Annotation> note;
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
        description = "Types of research studies (types of research methods).",
        valueSet = "http://hl7.org/fhir/ValueSet/study-type"
    )
    private final CodeableConcept studyType;
    @Summary
    @ReferenceTarget({ "EvidenceVariable" })
    @Required
    private final Reference population;
    @Summary
    @ReferenceTarget({ "EvidenceVariable" })
    @Required
    private final Reference exposure;
    @Summary
    @ReferenceTarget({ "EvidenceVariable" })
    @Required
    private final Reference exposureAlternative;
    @Summary
    @ReferenceTarget({ "EvidenceVariable" })
    @Required
    private final Reference outcome;
    private final SampleSize sampleSize;
    private final List<ResultsByExposure> resultsByExposure;
    @Summary
    private final List<EffectEstimate> effectEstimate;
    private final List<Certainty> certainty;

    private EffectEvidenceSynthesis(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = builder.name;
        title = builder.title;
        status = builder.status;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        note = Collections.unmodifiableList(builder.note);
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
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
        synthesisType = builder.synthesisType;
        studyType = builder.studyType;
        population = builder.population;
        exposure = builder.exposure;
        exposureAlternative = builder.exposureAlternative;
        outcome = builder.outcome;
        sampleSize = builder.sampleSize;
        resultsByExposure = Collections.unmodifiableList(builder.resultsByExposure);
        effectEstimate = Collections.unmodifiableList(builder.effectEstimate);
        certainty = Collections.unmodifiableList(builder.certainty);
    }

    /**
     * An absolute URI that is used to identify this effect evidence synthesis when it is referenced in a specification, 
     * model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a 
     * literal address at which at which an authoritative instance of this effect evidence synthesis is (or will be) 
     * published. This URL can be the target of a canonical reference. It SHALL remain the same when the effect evidence 
     * synthesis is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this effect evidence synthesis when it is represented in other formats, 
     * or referenced in a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the effect evidence synthesis when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the effect evidence synthesis author 
     * and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version 
     * is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the effect evidence synthesis. This name should be usable as an identifier for the 
     * module by machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the effect evidence synthesis.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The status of this effect evidence synthesis. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * The date (and optionally time) when the effect evidence synthesis was published. The date must change when the 
     * business version changes and it must change if the status code changes. In addition, it should change when the 
     * substantive content of the effect evidence synthesis changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the effect evidence synthesis.
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
     * A free text natural language description of the effect evidence synthesis from a consumer's perspective.
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
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate effect evidence synthesis instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the effect evidence synthesis is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * A copyright statement relating to the effect evidence synthesis and/or its contents. Copyright statements are 
     * generally legal restrictions on the use and publishing of the effect evidence synthesis.
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
     * The period during which the effect evidence synthesis content was or is planned to be in active use.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getEffectivePeriod() {
        return effectivePeriod;
    }

    /**
     * Descriptive topics related to the content of the EffectEvidenceSynthesis. Topics provide a high-level categorization 
     * grouping types of EffectEvidenceSynthesiss that can be useful for filtering and searching.
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
     * Type of synthesis eg meta-analysis.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSynthesisType() {
        return synthesisType;
    }

    /**
     * Type of study eg randomized trial.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStudyType() {
        return studyType;
    }

    /**
     * A reference to a EvidenceVariable resource that defines the population for the research.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPopulation() {
        return population;
    }

    /**
     * A reference to a EvidenceVariable resource that defines the exposure for the research.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getExposure() {
        return exposure;
    }

    /**
     * A reference to a EvidenceVariable resource that defines the comparison exposure for the research.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getExposureAlternative() {
        return exposureAlternative;
    }

    /**
     * A reference to a EvidenceVariable resomece that defines the outcome for the research.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getOutcome() {
        return outcome;
    }

    /**
     * A description of the size of the sample involved in the synthesis.
     * 
     * @return
     *     An immutable object of type {@link SampleSize} that may be null.
     */
    public SampleSize getSampleSize() {
        return sampleSize;
    }

    /**
     * A description of the results for each exposure considered in the effect estimate.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ResultsByExposure} that may be empty.
     */
    public List<ResultsByExposure> getResultsByExposure() {
        return resultsByExposure;
    }

    /**
     * The estimated effect of the exposure variant.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link EffectEstimate} that may be empty.
     */
    public List<EffectEstimate> getEffectEstimate() {
        return effectEstimate;
    }

    /**
     * A description of the certainty of the effect estimate.
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
            (name != null) || 
            (title != null) || 
            (status != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (description != null) || 
            !note.isEmpty() || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
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
            (synthesisType != null) || 
            (studyType != null) || 
            (population != null) || 
            (exposure != null) || 
            (exposureAlternative != null) || 
            (outcome != null) || 
            (sampleSize != null) || 
            !resultsByExposure.isEmpty() || 
            !effectEstimate.isEmpty() || 
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
                accept(name, "name", visitor);
                accept(title, "title", visitor);
                accept(status, "status", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(note, "note", visitor, Annotation.class);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
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
                accept(synthesisType, "synthesisType", visitor);
                accept(studyType, "studyType", visitor);
                accept(population, "population", visitor);
                accept(exposure, "exposure", visitor);
                accept(exposureAlternative, "exposureAlternative", visitor);
                accept(outcome, "outcome", visitor);
                accept(sampleSize, "sampleSize", visitor);
                accept(resultsByExposure, "resultsByExposure", visitor, ResultsByExposure.class);
                accept(effectEstimate, "effectEstimate", visitor, EffectEstimate.class);
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
        EffectEvidenceSynthesis other = (EffectEvidenceSynthesis) obj;
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
            Objects.equals(status, other.status) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(description, other.description) && 
            Objects.equals(note, other.note) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
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
            Objects.equals(synthesisType, other.synthesisType) && 
            Objects.equals(studyType, other.studyType) && 
            Objects.equals(population, other.population) && 
            Objects.equals(exposure, other.exposure) && 
            Objects.equals(exposureAlternative, other.exposureAlternative) && 
            Objects.equals(outcome, other.outcome) && 
            Objects.equals(sampleSize, other.sampleSize) && 
            Objects.equals(resultsByExposure, other.resultsByExposure) && 
            Objects.equals(effectEstimate, other.effectEstimate) && 
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
                name, 
                title, 
                status, 
                date, 
                publisher, 
                contact, 
                description, 
                note, 
                useContext, 
                jurisdiction, 
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
                synthesisType, 
                studyType, 
                population, 
                exposure, 
                exposureAlternative, 
                outcome, 
                sampleSize, 
                resultsByExposure, 
                effectEstimate, 
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
        private String name;
        private String title;
        private PublicationStatus status;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<Annotation> note = new ArrayList<>();
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
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
        private CodeableConcept synthesisType;
        private CodeableConcept studyType;
        private Reference population;
        private Reference exposure;
        private Reference exposureAlternative;
        private Reference outcome;
        private SampleSize sampleSize;
        private List<ResultsByExposure> resultsByExposure = new ArrayList<>();
        private List<EffectEstimate> effectEstimate = new ArrayList<>();
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
         * An absolute URI that is used to identify this effect evidence synthesis when it is referenced in a specification, 
         * model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a 
         * literal address at which at which an authoritative instance of this effect evidence synthesis is (or will be) 
         * published. This URL can be the target of a canonical reference. It SHALL remain the same when the effect evidence 
         * synthesis is stored on different servers.
         * 
         * @param url
         *     Canonical identifier for this effect evidence synthesis, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this effect evidence synthesis when it is represented in other formats, 
         * or referenced in a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the effect evidence synthesis
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
         * A formal identifier that is used to identify this effect evidence synthesis when it is represented in other formats, 
         * or referenced in a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the effect evidence synthesis
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
         *     Business version of the effect evidence synthesis
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
         * The identifier that is used to identify this version of the effect evidence synthesis when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the effect evidence synthesis author 
         * and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version 
         * is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the effect evidence synthesis
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
         *     Name for this effect evidence synthesis (computer friendly)
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
         * A natural language name identifying the effect evidence synthesis. This name should be usable as an identifier for the 
         * module by machine processing applications such as code generation.
         * 
         * @param name
         *     Name for this effect evidence synthesis (computer friendly)
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
         *     Name for this effect evidence synthesis (human friendly)
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
         * A short, descriptive, user-friendly title for the effect evidence synthesis.
         * 
         * @param title
         *     Name for this effect evidence synthesis (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The status of this effect evidence synthesis. Enables tracking the life-cycle of the content.
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
         * The date (and optionally time) when the effect evidence synthesis was published. The date must change when the 
         * business version changes and it must change if the status code changes. In addition, it should change when the 
         * substantive content of the effect evidence synthesis changes.
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
         * The name of the organization or individual that published the effect evidence synthesis.
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
         * A free text natural language description of the effect evidence synthesis from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the effect evidence synthesis
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * A human-readable string to clarify or explain concepts about the resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Used for footnotes or explanatory notes
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
         * A human-readable string to clarify or explain concepts about the resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Used for footnotes or explanatory notes
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate effect evidence synthesis instances.
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
         * may be used to assist with indexing and searching for appropriate effect evidence synthesis instances.
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
         * A legal or geographic region in which the effect evidence synthesis is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for effect evidence synthesis (if applicable)
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
         * A legal or geographic region in which the effect evidence synthesis is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for effect evidence synthesis (if applicable)
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
         * A copyright statement relating to the effect evidence synthesis and/or its contents. Copyright statements are 
         * generally legal restrictions on the use and publishing of the effect evidence synthesis.
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
         *     When the effect evidence synthesis was approved by publisher
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
         *     When the effect evidence synthesis was approved by publisher
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
         *     When the effect evidence synthesis was last reviewed
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
         *     When the effect evidence synthesis was last reviewed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
            return this;
        }

        /**
         * The period during which the effect evidence synthesis content was or is planned to be in active use.
         * 
         * @param effectivePeriod
         *     When the effect evidence synthesis is expected to be used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effectivePeriod(Period effectivePeriod) {
            this.effectivePeriod = effectivePeriod;
            return this;
        }

        /**
         * Descriptive topics related to the content of the EffectEvidenceSynthesis. Topics provide a high-level categorization 
         * grouping types of EffectEvidenceSynthesiss that can be useful for filtering and searching.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param topic
         *     The category of the EffectEvidenceSynthesis, such as Education, Treatment, Assessment, etc.
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
         * Descriptive topics related to the content of the EffectEvidenceSynthesis. Topics provide a high-level categorization 
         * grouping types of EffectEvidenceSynthesiss that can be useful for filtering and searching.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param topic
         *     The category of the EffectEvidenceSynthesis, such as Education, Treatment, Assessment, etc.
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
         * Type of synthesis eg meta-analysis.
         * 
         * @param synthesisType
         *     Type of synthesis
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder synthesisType(CodeableConcept synthesisType) {
            this.synthesisType = synthesisType;
            return this;
        }

        /**
         * Type of study eg randomized trial.
         * 
         * @param studyType
         *     Type of study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder studyType(CodeableConcept studyType) {
            this.studyType = studyType;
            return this;
        }

        /**
         * A reference to a EvidenceVariable resource that defines the population for the research.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link EvidenceVariable}</li>
         * </ul>
         * 
         * @param population
         *     What population?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder population(Reference population) {
            this.population = population;
            return this;
        }

        /**
         * A reference to a EvidenceVariable resource that defines the exposure for the research.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link EvidenceVariable}</li>
         * </ul>
         * 
         * @param exposure
         *     What exposure?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder exposure(Reference exposure) {
            this.exposure = exposure;
            return this;
        }

        /**
         * A reference to a EvidenceVariable resource that defines the comparison exposure for the research.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link EvidenceVariable}</li>
         * </ul>
         * 
         * @param exposureAlternative
         *     What comparison exposure?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder exposureAlternative(Reference exposureAlternative) {
            this.exposureAlternative = exposureAlternative;
            return this;
        }

        /**
         * A reference to a EvidenceVariable resomece that defines the outcome for the research.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link EvidenceVariable}</li>
         * </ul>
         * 
         * @param outcome
         *     What outcome?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder outcome(Reference outcome) {
            this.outcome = outcome;
            return this;
        }

        /**
         * A description of the size of the sample involved in the synthesis.
         * 
         * @param sampleSize
         *     What sample size was involved?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sampleSize(SampleSize sampleSize) {
            this.sampleSize = sampleSize;
            return this;
        }

        /**
         * A description of the results for each exposure considered in the effect estimate.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param resultsByExposure
         *     What was the result per exposure?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder resultsByExposure(ResultsByExposure... resultsByExposure) {
            for (ResultsByExposure value : resultsByExposure) {
                this.resultsByExposure.add(value);
            }
            return this;
        }

        /**
         * A description of the results for each exposure considered in the effect estimate.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param resultsByExposure
         *     What was the result per exposure?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder resultsByExposure(Collection<ResultsByExposure> resultsByExposure) {
            this.resultsByExposure = new ArrayList<>(resultsByExposure);
            return this;
        }

        /**
         * The estimated effect of the exposure variant.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param effectEstimate
         *     What was the estimated effect
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effectEstimate(EffectEstimate... effectEstimate) {
            for (EffectEstimate value : effectEstimate) {
                this.effectEstimate.add(value);
            }
            return this;
        }

        /**
         * The estimated effect of the exposure variant.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param effectEstimate
         *     What was the estimated effect
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder effectEstimate(Collection<EffectEstimate> effectEstimate) {
            this.effectEstimate = new ArrayList<>(effectEstimate);
            return this;
        }

        /**
         * A description of the certainty of the effect estimate.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param certainty
         *     How certain is the effect
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
         * A description of the certainty of the effect estimate.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param certainty
         *     How certain is the effect
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
         * Build the {@link EffectEvidenceSynthesis}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>population</li>
         * <li>exposure</li>
         * <li>exposureAlternative</li>
         * <li>outcome</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link EffectEvidenceSynthesis}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid EffectEvidenceSynthesis per the base specification
         */
        @Override
        public EffectEvidenceSynthesis build() {
            EffectEvidenceSynthesis effectEvidenceSynthesis = new EffectEvidenceSynthesis(this);
            if (validating) {
                validate(effectEvidenceSynthesis);
            }
            return effectEvidenceSynthesis;
        }

        protected void validate(EffectEvidenceSynthesis effectEvidenceSynthesis) {
            super.validate(effectEvidenceSynthesis);
            ValidationSupport.checkList(effectEvidenceSynthesis.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(effectEvidenceSynthesis.status, "status");
            ValidationSupport.checkList(effectEvidenceSynthesis.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.note, "note", Annotation.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.topic, "topic", CodeableConcept.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.author, "author", ContactDetail.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.editor, "editor", ContactDetail.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.reviewer, "reviewer", ContactDetail.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.endorser, "endorser", ContactDetail.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.relatedArtifact, "relatedArtifact", RelatedArtifact.class);
            ValidationSupport.requireNonNull(effectEvidenceSynthesis.population, "population");
            ValidationSupport.requireNonNull(effectEvidenceSynthesis.exposure, "exposure");
            ValidationSupport.requireNonNull(effectEvidenceSynthesis.exposureAlternative, "exposureAlternative");
            ValidationSupport.requireNonNull(effectEvidenceSynthesis.outcome, "outcome");
            ValidationSupport.checkList(effectEvidenceSynthesis.resultsByExposure, "resultsByExposure", ResultsByExposure.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.effectEstimate, "effectEstimate", EffectEstimate.class);
            ValidationSupport.checkList(effectEvidenceSynthesis.certainty, "certainty", Certainty.class);
            ValidationSupport.checkReferenceType(effectEvidenceSynthesis.population, "population", "EvidenceVariable");
            ValidationSupport.checkReferenceType(effectEvidenceSynthesis.exposure, "exposure", "EvidenceVariable");
            ValidationSupport.checkReferenceType(effectEvidenceSynthesis.exposureAlternative, "exposureAlternative", "EvidenceVariable");
            ValidationSupport.checkReferenceType(effectEvidenceSynthesis.outcome, "outcome", "EvidenceVariable");
        }

        protected Builder from(EffectEvidenceSynthesis effectEvidenceSynthesis) {
            super.from(effectEvidenceSynthesis);
            url = effectEvidenceSynthesis.url;
            identifier.addAll(effectEvidenceSynthesis.identifier);
            version = effectEvidenceSynthesis.version;
            name = effectEvidenceSynthesis.name;
            title = effectEvidenceSynthesis.title;
            status = effectEvidenceSynthesis.status;
            date = effectEvidenceSynthesis.date;
            publisher = effectEvidenceSynthesis.publisher;
            contact.addAll(effectEvidenceSynthesis.contact);
            description = effectEvidenceSynthesis.description;
            note.addAll(effectEvidenceSynthesis.note);
            useContext.addAll(effectEvidenceSynthesis.useContext);
            jurisdiction.addAll(effectEvidenceSynthesis.jurisdiction);
            copyright = effectEvidenceSynthesis.copyright;
            approvalDate = effectEvidenceSynthesis.approvalDate;
            lastReviewDate = effectEvidenceSynthesis.lastReviewDate;
            effectivePeriod = effectEvidenceSynthesis.effectivePeriod;
            topic.addAll(effectEvidenceSynthesis.topic);
            author.addAll(effectEvidenceSynthesis.author);
            editor.addAll(effectEvidenceSynthesis.editor);
            reviewer.addAll(effectEvidenceSynthesis.reviewer);
            endorser.addAll(effectEvidenceSynthesis.endorser);
            relatedArtifact.addAll(effectEvidenceSynthesis.relatedArtifact);
            synthesisType = effectEvidenceSynthesis.synthesisType;
            studyType = effectEvidenceSynthesis.studyType;
            population = effectEvidenceSynthesis.population;
            exposure = effectEvidenceSynthesis.exposure;
            exposureAlternative = effectEvidenceSynthesis.exposureAlternative;
            outcome = effectEvidenceSynthesis.outcome;
            sampleSize = effectEvidenceSynthesis.sampleSize;
            resultsByExposure.addAll(effectEvidenceSynthesis.resultsByExposure);
            effectEstimate.addAll(effectEvidenceSynthesis.effectEstimate);
            certainty.addAll(effectEvidenceSynthesis.certainty);
            return this;
        }
    }

    /**
     * A description of the size of the sample involved in the synthesis.
     */
    public static class SampleSize extends BackboneElement {
        private final String description;
        private final Integer numberOfStudies;
        private final Integer numberOfParticipants;

        private SampleSize(Builder builder) {
            super(builder);
            description = builder.description;
            numberOfStudies = builder.numberOfStudies;
            numberOfParticipants = builder.numberOfParticipants;
        }

        /**
         * Human-readable summary of sample size.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Number of studies included in this evidence synthesis.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getNumberOfStudies() {
            return numberOfStudies;
        }

        /**
         * Number of participants included in this evidence synthesis.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getNumberOfParticipants() {
            return numberOfParticipants;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (numberOfStudies != null) || 
                (numberOfParticipants != null);
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
                    accept(numberOfStudies, "numberOfStudies", visitor);
                    accept(numberOfParticipants, "numberOfParticipants", visitor);
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
                Objects.equals(numberOfStudies, other.numberOfStudies) && 
                Objects.equals(numberOfParticipants, other.numberOfParticipants);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    numberOfStudies, 
                    numberOfParticipants);
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
            private Integer numberOfStudies;
            private Integer numberOfParticipants;

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
             *     Description of sample size
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
             * Human-readable summary of sample size.
             * 
             * @param description
             *     Description of sample size
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Convenience method for setting {@code numberOfStudies}.
             * 
             * @param numberOfStudies
             *     How many studies?
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #numberOfStudies(com.ibm.fhir.model.type.Integer)
             */
            public Builder numberOfStudies(java.lang.Integer numberOfStudies) {
                this.numberOfStudies = (numberOfStudies == null) ? null : Integer.of(numberOfStudies);
                return this;
            }

            /**
             * Number of studies included in this evidence synthesis.
             * 
             * @param numberOfStudies
             *     How many studies?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder numberOfStudies(Integer numberOfStudies) {
                this.numberOfStudies = numberOfStudies;
                return this;
            }

            /**
             * Convenience method for setting {@code numberOfParticipants}.
             * 
             * @param numberOfParticipants
             *     How many participants?
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #numberOfParticipants(com.ibm.fhir.model.type.Integer)
             */
            public Builder numberOfParticipants(java.lang.Integer numberOfParticipants) {
                this.numberOfParticipants = (numberOfParticipants == null) ? null : Integer.of(numberOfParticipants);
                return this;
            }

            /**
             * Number of participants included in this evidence synthesis.
             * 
             * @param numberOfParticipants
             *     How many participants?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder numberOfParticipants(Integer numberOfParticipants) {
                this.numberOfParticipants = numberOfParticipants;
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
                ValidationSupport.requireValueOrChildren(sampleSize);
            }

            protected Builder from(SampleSize sampleSize) {
                super.from(sampleSize);
                description = sampleSize.description;
                numberOfStudies = sampleSize.numberOfStudies;
                numberOfParticipants = sampleSize.numberOfParticipants;
                return this;
            }
        }
    }

    /**
     * A description of the results for each exposure considered in the effect estimate.
     */
    public static class ResultsByExposure extends BackboneElement {
        private final String description;
        @Binding(
            bindingName = "ExposureState",
            strength = BindingStrength.Value.REQUIRED,
            description = "Whether the results by exposure is describing the results for the primary exposure of interest (exposure) or the alternative state (exposureAlternative).",
            valueSet = "http://hl7.org/fhir/ValueSet/exposure-state|4.0.1"
        )
        private final ExposureState exposureState;
        @Binding(
            bindingName = "EvidenceVariantState",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Used for results by exposure in variant states such as low-risk, medium-risk and high-risk states.",
            valueSet = "http://hl7.org/fhir/ValueSet/evidence-variant-state"
        )
        private final CodeableConcept variantState;
        @ReferenceTarget({ "RiskEvidenceSynthesis" })
        @Required
        private final Reference riskEvidenceSynthesis;

        private ResultsByExposure(Builder builder) {
            super(builder);
            description = builder.description;
            exposureState = builder.exposureState;
            variantState = builder.variantState;
            riskEvidenceSynthesis = builder.riskEvidenceSynthesis;
        }

        /**
         * Human-readable summary of results by exposure state.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Whether these results are for the exposure state or alternative exposure state.
         * 
         * @return
         *     An immutable object of type {@link ExposureState} that may be null.
         */
        public ExposureState getExposureState() {
            return exposureState;
        }

        /**
         * Used to define variant exposure states such as low-risk state.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getVariantState() {
            return variantState;
        }

        /**
         * Reference to a RiskEvidenceSynthesis resource.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getRiskEvidenceSynthesis() {
            return riskEvidenceSynthesis;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (exposureState != null) || 
                (variantState != null) || 
                (riskEvidenceSynthesis != null);
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
                    accept(exposureState, "exposureState", visitor);
                    accept(variantState, "variantState", visitor);
                    accept(riskEvidenceSynthesis, "riskEvidenceSynthesis", visitor);
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
            ResultsByExposure other = (ResultsByExposure) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(exposureState, other.exposureState) && 
                Objects.equals(variantState, other.variantState) && 
                Objects.equals(riskEvidenceSynthesis, other.riskEvidenceSynthesis);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    exposureState, 
                    variantState, 
                    riskEvidenceSynthesis);
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
            private ExposureState exposureState;
            private CodeableConcept variantState;
            private Reference riskEvidenceSynthesis;

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
             *     Description of results by exposure
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
             * Human-readable summary of results by exposure state.
             * 
             * @param description
             *     Description of results by exposure
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Whether these results are for the exposure state or alternative exposure state.
             * 
             * @param exposureState
             *     exposure | exposure-alternative
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder exposureState(ExposureState exposureState) {
                this.exposureState = exposureState;
                return this;
            }

            /**
             * Used to define variant exposure states such as low-risk state.
             * 
             * @param variantState
             *     Variant exposure states
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder variantState(CodeableConcept variantState) {
                this.variantState = variantState;
                return this;
            }

            /**
             * Reference to a RiskEvidenceSynthesis resource.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link RiskEvidenceSynthesis}</li>
             * </ul>
             * 
             * @param riskEvidenceSynthesis
             *     Risk evidence synthesis
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder riskEvidenceSynthesis(Reference riskEvidenceSynthesis) {
                this.riskEvidenceSynthesis = riskEvidenceSynthesis;
                return this;
            }

            /**
             * Build the {@link ResultsByExposure}
             * 
             * <p>Required elements:
             * <ul>
             * <li>riskEvidenceSynthesis</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link ResultsByExposure}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ResultsByExposure per the base specification
             */
            @Override
            public ResultsByExposure build() {
                ResultsByExposure resultsByExposure = new ResultsByExposure(this);
                if (validating) {
                    validate(resultsByExposure);
                }
                return resultsByExposure;
            }

            protected void validate(ResultsByExposure resultsByExposure) {
                super.validate(resultsByExposure);
                ValidationSupport.requireNonNull(resultsByExposure.riskEvidenceSynthesis, "riskEvidenceSynthesis");
                ValidationSupport.checkReferenceType(resultsByExposure.riskEvidenceSynthesis, "riskEvidenceSynthesis", "RiskEvidenceSynthesis");
                ValidationSupport.requireValueOrChildren(resultsByExposure);
            }

            protected Builder from(ResultsByExposure resultsByExposure) {
                super.from(resultsByExposure);
                description = resultsByExposure.description;
                exposureState = resultsByExposure.exposureState;
                variantState = resultsByExposure.variantState;
                riskEvidenceSynthesis = resultsByExposure.riskEvidenceSynthesis;
                return this;
            }
        }
    }

    /**
     * The estimated effect of the exposure variant.
     */
    public static class EffectEstimate extends BackboneElement {
        private final String description;
        @Binding(
            bindingName = "EffectEstimateType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Whether the effect estimate is an absolute effect estimate (absolute difference) or a relative effect estimate (relative difference), and the specific type of effect estimate (eg relative risk or median difference).",
            valueSet = "http://hl7.org/fhir/ValueSet/effect-estimate-type"
        )
        private final CodeableConcept type;
        @Binding(
            bindingName = "EvidenceVariantState",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Used for results by exposure in variant states such as low-risk, medium-risk and high-risk states.",
            valueSet = "http://hl7.org/fhir/ValueSet/evidence-variant-state"
        )
        private final CodeableConcept variantState;
        private final Decimal value;
        @Binding(
            bindingName = "UCUMUnits",
            strength = BindingStrength.Value.REQUIRED,
            description = "Unified Code for Units of Measure (UCUM).",
            valueSet = "http://hl7.org/fhir/ValueSet/ucum-units|4.0.1"
        )
        private final CodeableConcept unitOfMeasure;
        private final List<PrecisionEstimate> precisionEstimate;

        private EffectEstimate(Builder builder) {
            super(builder);
            description = builder.description;
            type = builder.type;
            variantState = builder.variantState;
            value = builder.value;
            unitOfMeasure = builder.unitOfMeasure;
            precisionEstimate = Collections.unmodifiableList(builder.precisionEstimate);
        }

        /**
         * Human-readable summary of effect estimate.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Examples include relative risk and mean difference.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Used to define variant exposure states such as low-risk state.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getVariantState() {
            return variantState;
        }

        /**
         * The point estimate of the effect estimate.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getValue() {
            return value;
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
         * A description of the precision of the estimate for the effect.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PrecisionEstimate} that may be empty.
         */
        public List<PrecisionEstimate> getPrecisionEstimate() {
            return precisionEstimate;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (type != null) || 
                (variantState != null) || 
                (value != null) || 
                (unitOfMeasure != null) || 
                !precisionEstimate.isEmpty();
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
                    accept(type, "type", visitor);
                    accept(variantState, "variantState", visitor);
                    accept(value, "value", visitor);
                    accept(unitOfMeasure, "unitOfMeasure", visitor);
                    accept(precisionEstimate, "precisionEstimate", visitor, PrecisionEstimate.class);
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
            EffectEstimate other = (EffectEstimate) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(type, other.type) && 
                Objects.equals(variantState, other.variantState) && 
                Objects.equals(value, other.value) && 
                Objects.equals(unitOfMeasure, other.unitOfMeasure) && 
                Objects.equals(precisionEstimate, other.precisionEstimate);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    type, 
                    variantState, 
                    value, 
                    unitOfMeasure, 
                    precisionEstimate);
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
            private CodeableConcept type;
            private CodeableConcept variantState;
            private Decimal value;
            private CodeableConcept unitOfMeasure;
            private List<PrecisionEstimate> precisionEstimate = new ArrayList<>();

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
             *     Description of effect estimate
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
             * Human-readable summary of effect estimate.
             * 
             * @param description
             *     Description of effect estimate
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Examples include relative risk and mean difference.
             * 
             * @param type
             *     Type of efffect estimate
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Used to define variant exposure states such as low-risk state.
             * 
             * @param variantState
             *     Variant exposure states
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder variantState(CodeableConcept variantState) {
                this.variantState = variantState;
                return this;
            }

            /**
             * The point estimate of the effect estimate.
             * 
             * @param value
             *     Point estimate
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Decimal value) {
                this.value = value;
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
             * A description of the precision of the estimate for the effect.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param precisionEstimate
             *     How precise the estimate is
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder precisionEstimate(PrecisionEstimate... precisionEstimate) {
                for (PrecisionEstimate value : precisionEstimate) {
                    this.precisionEstimate.add(value);
                }
                return this;
            }

            /**
             * A description of the precision of the estimate for the effect.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param precisionEstimate
             *     How precise the estimate is
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder precisionEstimate(Collection<PrecisionEstimate> precisionEstimate) {
                this.precisionEstimate = new ArrayList<>(precisionEstimate);
                return this;
            }

            /**
             * Build the {@link EffectEstimate}
             * 
             * @return
             *     An immutable object of type {@link EffectEstimate}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid EffectEstimate per the base specification
             */
            @Override
            public EffectEstimate build() {
                EffectEstimate effectEstimate = new EffectEstimate(this);
                if (validating) {
                    validate(effectEstimate);
                }
                return effectEstimate;
            }

            protected void validate(EffectEstimate effectEstimate) {
                super.validate(effectEstimate);
                ValidationSupport.checkList(effectEstimate.precisionEstimate, "precisionEstimate", PrecisionEstimate.class);
                ValidationSupport.checkValueSetBinding(effectEstimate.unitOfMeasure, "unitOfMeasure", "http://hl7.org/fhir/ValueSet/ucum-units", "http://unitsofmeasure.org");
                ValidationSupport.requireValueOrChildren(effectEstimate);
            }

            protected Builder from(EffectEstimate effectEstimate) {
                super.from(effectEstimate);
                description = effectEstimate.description;
                type = effectEstimate.type;
                variantState = effectEstimate.variantState;
                value = effectEstimate.value;
                unitOfMeasure = effectEstimate.unitOfMeasure;
                precisionEstimate.addAll(effectEstimate.precisionEstimate);
                return this;
            }
        }

        /**
         * A description of the precision of the estimate for the effect.
         */
        public static class PrecisionEstimate extends BackboneElement {
            @Binding(
                bindingName = "PrecisionEstimateType",
                strength = BindingStrength.Value.EXTENSIBLE,
                description = "Method of reporting variability of estimates, such as confidence intervals, interquartile range or standard deviation.",
                valueSet = "http://hl7.org/fhir/ValueSet/precision-estimate-type"
            )
            private final CodeableConcept type;
            private final Decimal level;
            private final Decimal from;
            private final Decimal to;

            private PrecisionEstimate(Builder builder) {
                super(builder);
                type = builder.type;
                level = builder.level;
                from = builder.from;
                to = builder.to;
            }

            /**
             * Examples include confidence interval and interquartile range.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getType() {
                return type;
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
             *     An immutable object of type {@link Decimal} that may be null.
             */
            public Decimal getFrom() {
                return from;
            }

            /**
             * Upper bound of confidence interval.
             * 
             * @return
             *     An immutable object of type {@link Decimal} that may be null.
             */
            public Decimal getTo() {
                return to;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (level != null) || 
                    (from != null) || 
                    (to != null);
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
                        accept(level, "level", visitor);
                        accept(from, "from", visitor);
                        accept(to, "to", visitor);
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
                PrecisionEstimate other = (PrecisionEstimate) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(level, other.level) && 
                    Objects.equals(from, other.from) && 
                    Objects.equals(to, other.to);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        level, 
                        from, 
                        to);
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
                private Decimal level;
                private Decimal from;
                private Decimal to;

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
                 * Examples include confidence interval and interquartile range.
                 * 
                 * @param type
                 *     Type of precision estimate
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Use 95 for a 95% confidence interval.
                 * 
                 * @param level
                 *     Level of confidence interval
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
                 * @param from
                 *     Lower bound
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder from(Decimal from) {
                    this.from = from;
                    return this;
                }

                /**
                 * Upper bound of confidence interval.
                 * 
                 * @param to
                 *     Upper bound
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder to(Decimal to) {
                    this.to = to;
                    return this;
                }

                /**
                 * Build the {@link PrecisionEstimate}
                 * 
                 * @return
                 *     An immutable object of type {@link PrecisionEstimate}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid PrecisionEstimate per the base specification
                 */
                @Override
                public PrecisionEstimate build() {
                    PrecisionEstimate precisionEstimate = new PrecisionEstimate(this);
                    if (validating) {
                        validate(precisionEstimate);
                    }
                    return precisionEstimate;
                }

                protected void validate(PrecisionEstimate precisionEstimate) {
                    super.validate(precisionEstimate);
                    ValidationSupport.requireValueOrChildren(precisionEstimate);
                }

                protected Builder from(PrecisionEstimate precisionEstimate) {
                    super.from(precisionEstimate);
                    type = precisionEstimate.type;
                    level = precisionEstimate.level;
                    from = precisionEstimate.from;
                    to = precisionEstimate.to;
                    return this;
                }
            }
        }
    }

    /**
     * A description of the certainty of the effect estimate.
     */
    public static class Certainty extends BackboneElement {
        @Binding(
            bindingName = "QualityOfEvidenceRating",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The quality of the evidence described. The code system used specifies the quality scale used to grade this evidence source while the code specifies the actual quality score (represented as a coded value) associated with the evidence.",
            valueSet = "http://hl7.org/fhir/ValueSet/evidence-quality"
        )
        private final List<CodeableConcept> rating;
        private final List<Annotation> note;
        private final List<CertaintySubcomponent> certaintySubcomponent;

        private Certainty(Builder builder) {
            super(builder);
            rating = Collections.unmodifiableList(builder.rating);
            note = Collections.unmodifiableList(builder.note);
            certaintySubcomponent = Collections.unmodifiableList(builder.certaintySubcomponent);
        }

        /**
         * A rating of the certainty of the effect estimate.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getRating() {
            return rating;
        }

        /**
         * A human-readable string to clarify or explain concepts about the resource.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
         */
        public List<Annotation> getNote() {
            return note;
        }

        /**
         * A description of a component of the overall certainty.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CertaintySubcomponent} that may be empty.
         */
        public List<CertaintySubcomponent> getCertaintySubcomponent() {
            return certaintySubcomponent;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !rating.isEmpty() || 
                !note.isEmpty() || 
                !certaintySubcomponent.isEmpty();
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
                    accept(rating, "rating", visitor, CodeableConcept.class);
                    accept(note, "note", visitor, Annotation.class);
                    accept(certaintySubcomponent, "certaintySubcomponent", visitor, CertaintySubcomponent.class);
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
                Objects.equals(rating, other.rating) && 
                Objects.equals(note, other.note) && 
                Objects.equals(certaintySubcomponent, other.certaintySubcomponent);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    rating, 
                    note, 
                    certaintySubcomponent);
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
            private List<CodeableConcept> rating = new ArrayList<>();
            private List<Annotation> note = new ArrayList<>();
            private List<CertaintySubcomponent> certaintySubcomponent = new ArrayList<>();

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
             * A rating of the certainty of the effect estimate.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param rating
             *     Certainty rating
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder rating(CodeableConcept... rating) {
                for (CodeableConcept value : rating) {
                    this.rating.add(value);
                }
                return this;
            }

            /**
             * A rating of the certainty of the effect estimate.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param rating
             *     Certainty rating
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder rating(Collection<CodeableConcept> rating) {
                this.rating = new ArrayList<>(rating);
                return this;
            }

            /**
             * A human-readable string to clarify or explain concepts about the resource.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Used for footnotes or explanatory notes
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
             * A human-readable string to clarify or explain concepts about the resource.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Used for footnotes or explanatory notes
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
             * A description of a component of the overall certainty.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param certaintySubcomponent
             *     A component that contributes to the overall certainty
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder certaintySubcomponent(CertaintySubcomponent... certaintySubcomponent) {
                for (CertaintySubcomponent value : certaintySubcomponent) {
                    this.certaintySubcomponent.add(value);
                }
                return this;
            }

            /**
             * A description of a component of the overall certainty.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param certaintySubcomponent
             *     A component that contributes to the overall certainty
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder certaintySubcomponent(Collection<CertaintySubcomponent> certaintySubcomponent) {
                this.certaintySubcomponent = new ArrayList<>(certaintySubcomponent);
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
                ValidationSupport.checkList(certainty.rating, "rating", CodeableConcept.class);
                ValidationSupport.checkList(certainty.note, "note", Annotation.class);
                ValidationSupport.checkList(certainty.certaintySubcomponent, "certaintySubcomponent", CertaintySubcomponent.class);
                ValidationSupport.requireValueOrChildren(certainty);
            }

            protected Builder from(Certainty certainty) {
                super.from(certainty);
                rating.addAll(certainty.rating);
                note.addAll(certainty.note);
                certaintySubcomponent.addAll(certainty.certaintySubcomponent);
                return this;
            }
        }

        /**
         * A description of a component of the overall certainty.
         */
        public static class CertaintySubcomponent extends BackboneElement {
            @Binding(
                bindingName = "CertaintySubcomponentType",
                strength = BindingStrength.Value.EXTENSIBLE,
                description = "The subcomponent classification of quality of evidence rating systems.",
                valueSet = "http://hl7.org/fhir/ValueSet/certainty-subcomponent-type"
            )
            private final CodeableConcept type;
            @Binding(
                bindingName = "CertaintySubcomponentRating",
                strength = BindingStrength.Value.EXTENSIBLE,
                description = "The quality rating of the subcomponent of a quality of evidence rating.",
                valueSet = "http://hl7.org/fhir/ValueSet/certainty-subcomponent-rating"
            )
            private final List<CodeableConcept> rating;
            private final List<Annotation> note;

            private CertaintySubcomponent(Builder builder) {
                super(builder);
                type = builder.type;
                rating = Collections.unmodifiableList(builder.rating);
                note = Collections.unmodifiableList(builder.note);
            }

            /**
             * Type of subcomponent of certainty rating.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * A rating of a subcomponent of rating certainty.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getRating() {
                return rating;
            }

            /**
             * A human-readable string to clarify or explain concepts about the resource.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
             */
            public List<Annotation> getNote() {
                return note;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    !rating.isEmpty() || 
                    !note.isEmpty();
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
                        accept(rating, "rating", visitor, CodeableConcept.class);
                        accept(note, "note", visitor, Annotation.class);
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
                CertaintySubcomponent other = (CertaintySubcomponent) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(rating, other.rating) && 
                    Objects.equals(note, other.note);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        rating, 
                        note);
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
                private List<CodeableConcept> rating = new ArrayList<>();
                private List<Annotation> note = new ArrayList<>();

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
                 * Type of subcomponent of certainty rating.
                 * 
                 * @param type
                 *     Type of subcomponent of certainty rating
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * A rating of a subcomponent of rating certainty.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param rating
                 *     Subcomponent certainty rating
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder rating(CodeableConcept... rating) {
                    for (CodeableConcept value : rating) {
                        this.rating.add(value);
                    }
                    return this;
                }

                /**
                 * A rating of a subcomponent of rating certainty.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param rating
                 *     Subcomponent certainty rating
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder rating(Collection<CodeableConcept> rating) {
                    this.rating = new ArrayList<>(rating);
                    return this;
                }

                /**
                 * A human-readable string to clarify or explain concepts about the resource.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param note
                 *     Used for footnotes or explanatory notes
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
                 * A human-readable string to clarify or explain concepts about the resource.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param note
                 *     Used for footnotes or explanatory notes
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
                 * Build the {@link CertaintySubcomponent}
                 * 
                 * @return
                 *     An immutable object of type {@link CertaintySubcomponent}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid CertaintySubcomponent per the base specification
                 */
                @Override
                public CertaintySubcomponent build() {
                    CertaintySubcomponent certaintySubcomponent = new CertaintySubcomponent(this);
                    if (validating) {
                        validate(certaintySubcomponent);
                    }
                    return certaintySubcomponent;
                }

                protected void validate(CertaintySubcomponent certaintySubcomponent) {
                    super.validate(certaintySubcomponent);
                    ValidationSupport.checkList(certaintySubcomponent.rating, "rating", CodeableConcept.class);
                    ValidationSupport.checkList(certaintySubcomponent.note, "note", Annotation.class);
                    ValidationSupport.requireValueOrChildren(certaintySubcomponent);
                }

                protected Builder from(CertaintySubcomponent certaintySubcomponent) {
                    super.from(certaintySubcomponent);
                    type = certaintySubcomponent.type;
                    rating.addAll(certaintySubcomponent.rating);
                    note.addAll(certaintySubcomponent.note);
                    return this;
                }
            }
        }
    }
}
