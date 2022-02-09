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
import com.ibm.fhir.model.type.Age;
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
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.TriggerDefinition;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.ActionCardinalityBehavior;
import com.ibm.fhir.model.type.code.ActionConditionKind;
import com.ibm.fhir.model.type.code.ActionGroupingBehavior;
import com.ibm.fhir.model.type.code.ActionParticipantType;
import com.ibm.fhir.model.type.code.ActionPrecheckBehavior;
import com.ibm.fhir.model.type.code.ActionRelationshipType;
import com.ibm.fhir.model.type.code.ActionRequiredBehavior;
import com.ibm.fhir.model.type.code.ActionSelectionBehavior;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.RequestPriority;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * This resource allows for the definition of various types of plans as a sharable, consumable, and executable artifact. 
 * The resource is general enough to support the description of a broad range of clinical and non-clinical artifacts such 
 * as clinical decision support rules, order sets, protocols, and drug quality specifications.
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
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/PlanDefinition"
)
@Constraint(
    id = "planDefinition-1",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/plan-definition-type",
    expression = "type.exists() implies (type.memberOf('http://hl7.org/fhir/ValueSet/plan-definition-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/PlanDefinition",
    generated = true
)
@Constraint(
    id = "planDefinition-2",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/subject-type",
    expression = "subject.as(CodeableConcept).exists() implies (subject.as(CodeableConcept).memberOf('http://hl7.org/fhir/ValueSet/subject-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/PlanDefinition",
    generated = true
)
@Constraint(
    id = "planDefinition-3",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/PlanDefinition",
    generated = true
)
@Constraint(
    id = "planDefinition-4",
    level = "Warning",
    location = "goal.priority",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/goal-priority",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/goal-priority', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/PlanDefinition",
    generated = true
)
@Constraint(
    id = "planDefinition-5",
    level = "Warning",
    location = "action.subject",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/subject-type",
    expression = "$this.as(CodeableConcept).memberOf('http://hl7.org/fhir/ValueSet/subject-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/PlanDefinition",
    generated = true
)
@Constraint(
    id = "planDefinition-6",
    level = "Warning",
    location = "action.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/action-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/action-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/PlanDefinition",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class PlanDefinition extends DomainResource {
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
        bindingName = "PlanDefinitionType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The type of PlanDefinition.",
        valueSet = "http://hl7.org/fhir/ValueSet/plan-definition-type"
    )
    private final CodeableConcept type;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.3.0-CIBUILD"
    )
    @Required
    private final PublicationStatus status;
    @Summary
    private final Boolean experimental;
    @ReferenceTarget({ "Group", "MedicinalProductDefinition", "SubstanceDefinition", "AdministrableProductDefinition", "ManufacturedItemDefinition", "PackagedProductDefinition" })
    @Choice({ CodeableConcept.class, Reference.class, Canonical.class })
    @Binding(
        bindingName = "SubjectType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The possible types of subjects for a plan definition (E.g. Patient, Practitioner, Organization, Location, etc.).",
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
    private final List<Canonical> library;
    private final List<Goal> goal;
    private final List<Action> action;

    private PlanDefinition(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = builder.name;
        title = builder.title;
        subtitle = builder.subtitle;
        type = builder.type;
        status = builder.status;
        experimental = builder.experimental;
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
        library = Collections.unmodifiableList(builder.library);
        goal = Collections.unmodifiableList(builder.goal);
        action = Collections.unmodifiableList(builder.action);
    }

    /**
     * An absolute URI that is used to identify this plan definition when it is referenced in a specification, model, design 
     * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
     * at which at which an authoritative instance of this plan definition is (or will be) published. This URL can be the 
     * target of a canonical reference. It SHALL remain the same when the plan definition is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this plan definition when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the plan definition when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be 
     * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
     * also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with 
     * the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on 
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
     * A natural language name identifying the plan definition. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the plan definition.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * An explanatory or alternate title for the plan definition giving additional information about its content.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * A high-level category for the plan definition that distinguishes the kinds of systems that would be interested in the 
     * plan definition.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * The status of this plan definition. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this plan definition is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * A code, group definition, or canonical reference that describes or identifies the intended subject of the plan 
     * definition. Canonical references are allowed to support the definition of protocols for drug and substance quality 
     * specifications, and is allowed to reference a MedicinalProductDefinition, SubstanceDefinition, 
     * AdministrableProductDefinition, ManufacturedItemDefinition, or PackagedProductDefinition resource.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}, {@link Reference} or {@link Canonical} that may be null.
     */
    public Element getSubject() {
        return subject;
    }

    /**
     * The date (and optionally time) when the plan definition was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the plan definition changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the plan definition.
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
     * A free text natural language description of the plan definition from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate plan definition instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the plan definition is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this plan definition is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A detailed description of how the plan definition is used from a clinical perspective.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the plan definition.
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
     * The period during which the plan definition content was or is planned to be in active use.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getEffectivePeriod() {
        return effectivePeriod;
    }

    /**
     * Descriptive topics related to the content of the plan definition. Topics provide a high-level categorization of the 
     * definition that can be useful for filtering and searching.
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
     * A reference to a Library resource containing any formal logic used by the plan definition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getLibrary() {
        return library;
    }

    /**
     * A goal describes an expected outcome that activities within the plan are intended to achieve. For example, weight 
     * loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement 
     * objective, meeting the acceptance criteria for a test as specified by a quality specification, etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Goal} that may be empty.
     */
    public List<Goal> getGoal() {
        return goal;
    }

    /**
     * An action or group of actions to be taken as part of the plan. For example, in clinical care, an action would be to 
     * prescribe a particular indicated medication, or perform a particular test as appropriate. In pharmaceutical quality, 
     * an action would be the test that needs to be performed on a drug product as defined in the quality specification.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Action} that may be empty.
     */
    public List<Action> getAction() {
        return action;
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
            (type != null) || 
            (status != null) || 
            (experimental != null) || 
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
            !library.isEmpty() || 
            !goal.isEmpty() || 
            !action.isEmpty();
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
        private CodeableConcept type;
        private PublicationStatus status;
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
         * An absolute URI that is used to identify this plan definition when it is referenced in a specification, model, design 
         * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
         * at which at which an authoritative instance of this plan definition is (or will be) published. This URL can be the 
         * target of a canonical reference. It SHALL remain the same when the plan definition is stored on different servers.
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
         * A formal identifier that is used to identify this plan definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A formal identifier that is used to identify this plan definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the plan definition
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
         *     Business version of the plan definition
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
         * The identifier that is used to identify this version of the plan definition when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the plan definition author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with 
         * the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on 
         * versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for 
         * non-experimental active artifacts.
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
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     Name for this plan definition (computer friendly)
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
         * A natural language name identifying the plan definition. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
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
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Name for this plan definition (human friendly)
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
         * A short, descriptive, user-friendly title for the plan definition.
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
         * Convenience method for setting {@code subtitle}.
         * 
         * @param subtitle
         *     Subordinate title of the plan definition
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
         * An explanatory or alternate title for the plan definition giving additional information about its content.
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
         * A high-level category for the plan definition that distinguishes the kinds of systems that would be interested in the 
         * plan definition.
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
         * The status of this plan definition. Enables tracking the life-cycle of the content.
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
         * A Boolean value to indicate that this plan definition is authored for testing purposes (or 
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
         * A code, group definition, or canonical reference that describes or identifies the intended subject of the plan 
         * definition. Canonical references are allowed to support the definition of protocols for drug and substance quality 
         * specifications, and is allowed to reference a MedicinalProductDefinition, SubstanceDefinition, 
         * AdministrableProductDefinition, ManufacturedItemDefinition, or PackagedProductDefinition resource.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Reference}</li>
         * <li>{@link Canonical}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Group}</li>
         * <li>{@link MedicinalProductDefinition}</li>
         * <li>{@link SubstanceDefinition}</li>
         * <li>{@link AdministrableProductDefinition}</li>
         * <li>{@link ManufacturedItemDefinition}</li>
         * <li>{@link PackagedProductDefinition}</li>
         * </ul>
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
         * The date (and optionally time) when the plan definition was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the plan definition changes.
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
         * The name of the organization or individual that published the plan definition.
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
         * A free text natural language description of the plan definition from a consumer's perspective.
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate plan definition instances.
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
         * may be used to assist with indexing and searching for appropriate plan definition instances.
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
         * A legal or geographic region in which the plan definition is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A legal or geographic region in which the plan definition is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for plan definition (if applicable)
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
         * Explanation of why this plan definition is needed and why it has been designed as it has.
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
         * Convenience method for setting {@code usage}.
         * 
         * @param usage
         *     Describes the clinical usage of the plan
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
         * A detailed description of how the plan definition is used from a clinical perspective.
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
         * A copyright statement relating to the plan definition and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the plan definition.
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
         *     When the plan definition was approved by publisher
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
         * Convenience method for setting {@code lastReviewDate}.
         * 
         * @param lastReviewDate
         *     When the plan definition was last reviewed
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
         * The period during which the plan definition content was or is planned to be in active use.
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
         * Descriptive topics related to the content of the plan definition. Topics provide a high-level categorization of the 
         * definition that can be useful for filtering and searching.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Descriptive topics related to the content of the plan definition. Topics provide a high-level categorization of the 
         * definition that can be useful for filtering and searching.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param topic
         *     E.g. Education, Treatment, Assessment
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
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     Additional documentation, citations
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
         * A reference to a Library resource containing any formal logic used by the plan definition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A reference to a Library resource containing any formal logic used by the plan definition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param library
         *     Logic used by the plan definition
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
         * A goal describes an expected outcome that activities within the plan are intended to achieve. For example, weight 
         * loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement 
         * objective, meeting the acceptance criteria for a test as specified by a quality specification, etc.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A goal describes an expected outcome that activities within the plan are intended to achieve. For example, weight 
         * loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement 
         * objective, meeting the acceptance criteria for a test as specified by a quality specification, etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param goal
         *     What the plan is trying to accomplish
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder goal(Collection<Goal> goal) {
            this.goal = new ArrayList<>(goal);
            return this;
        }

        /**
         * An action or group of actions to be taken as part of the plan. For example, in clinical care, an action would be to 
         * prescribe a particular indicated medication, or perform a particular test as appropriate. In pharmaceutical quality, 
         * an action would be the test that needs to be performed on a drug product as defined in the quality specification.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * An action or group of actions to be taken as part of the plan. For example, in clinical care, an action would be to 
         * prescribe a particular indicated medication, or perform a particular test as appropriate. In pharmaceutical quality, 
         * an action would be the test that needs to be performed on a drug product as defined in the quality specification.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param action
         *     Action defined by the plan
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder action(Collection<Action> action) {
            this.action = new ArrayList<>(action);
            return this;
        }

        /**
         * Build the {@link PlanDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link PlanDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid PlanDefinition per the base specification
         */
        @Override
        public PlanDefinition build() {
            PlanDefinition planDefinition = new PlanDefinition(this);
            if (validating) {
                validate(planDefinition);
            }
            return planDefinition;
        }

        protected void validate(PlanDefinition planDefinition) {
            super.validate(planDefinition);
            ValidationSupport.checkList(planDefinition.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(planDefinition.status, "status");
            ValidationSupport.choiceElement(planDefinition.subject, "subject", CodeableConcept.class, Reference.class, Canonical.class);
            ValidationSupport.checkList(planDefinition.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(planDefinition.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(planDefinition.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(planDefinition.topic, "topic", CodeableConcept.class);
            ValidationSupport.checkList(planDefinition.author, "author", ContactDetail.class);
            ValidationSupport.checkList(planDefinition.editor, "editor", ContactDetail.class);
            ValidationSupport.checkList(planDefinition.reviewer, "reviewer", ContactDetail.class);
            ValidationSupport.checkList(planDefinition.endorser, "endorser", ContactDetail.class);
            ValidationSupport.checkList(planDefinition.relatedArtifact, "relatedArtifact", RelatedArtifact.class);
            ValidationSupport.checkList(planDefinition.library, "library", Canonical.class);
            ValidationSupport.checkList(planDefinition.goal, "goal", Goal.class);
            ValidationSupport.checkList(planDefinition.action, "action", Action.class);
            ValidationSupport.checkReferenceType(planDefinition.subject, "subject", "Group", "MedicinalProductDefinition", "SubstanceDefinition", "AdministrableProductDefinition", "ManufacturedItemDefinition", "PackagedProductDefinition");
        }

        protected Builder from(PlanDefinition planDefinition) {
            super.from(planDefinition);
            url = planDefinition.url;
            identifier.addAll(planDefinition.identifier);
            version = planDefinition.version;
            name = planDefinition.name;
            title = planDefinition.title;
            subtitle = planDefinition.subtitle;
            type = planDefinition.type;
            status = planDefinition.status;
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
     * A goal describes an expected outcome that activities within the plan are intended to achieve. For example, weight 
     * loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement 
     * objective, meeting the acceptance criteria for a test as specified by a quality specification, etc.
     */
    public static class Goal extends BackboneElement {
        @Binding(
            bindingName = "GoalCategory",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Example codes for grouping goals for filtering or presentation.",
            valueSet = "http://hl7.org/fhir/ValueSet/goal-category"
        )
        private final CodeableConcept category;
        @Binding(
            bindingName = "GoalDescription",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Describes goals that can be achieved.",
            valueSet = "http://hl7.org/fhir/ValueSet/clinical-findings"
        )
        @Required
        private final CodeableConcept description;
        @Binding(
            bindingName = "GoalPriority",
            strength = BindingStrength.Value.PREFERRED,
            description = "Indicates the level of importance associated with reaching or sustaining a goal.",
            valueSet = "http://hl7.org/fhir/ValueSet/goal-priority"
        )
        private final CodeableConcept priority;
        @Binding(
            bindingName = "GoalStartEvent",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Identifies the types of events that might trigger the start of a goal.",
            valueSet = "http://hl7.org/fhir/ValueSet/goal-start-event"
        )
        private final CodeableConcept start;
        @Binding(
            bindingName = "GoalAddresses",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Identifies problems, conditions, issues, or concerns that goals may address.",
            valueSet = "http://hl7.org/fhir/ValueSet/condition-code"
        )
        private final List<CodeableConcept> addresses;
        private final List<RelatedArtifact> documentation;
        private final List<Target> target;

        private Goal(Builder builder) {
            super(builder);
            category = builder.category;
            description = builder.description;
            priority = builder.priority;
            start = builder.start;
            addresses = Collections.unmodifiableList(builder.addresses);
            documentation = Collections.unmodifiableList(builder.documentation);
            target = Collections.unmodifiableList(builder.target);
        }

        /**
         * Indicates a category the goal falls within.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * Human-readable and/or coded description of a specific desired objective of care, such as "control blood pressure" or 
         * "negotiate an obstacle course" or "dance with child at wedding".
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getDescription() {
            return description;
        }

        /**
         * Identifies the expected level of importance associated with reaching/sustaining the defined goal.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getPriority() {
            return priority;
        }

        /**
         * The event after which the goal should begin being pursued.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStart() {
            return start;
        }

        /**
         * Identifies problems, conditions, issues, or concerns the goal is intended to address.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getAddresses() {
            return addresses;
        }

        /**
         * Didactic or other informational resources associated with the goal that provide further supporting information about 
         * the goal. Information resources can include inline text commentary and links to web resources.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact} that may be empty.
         */
        public List<RelatedArtifact> getDocumentation() {
            return documentation;
        }

        /**
         * Indicates what should be done and within what timeframe.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Target} that may be empty.
         */
        public List<Target> getTarget() {
            return target;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (category != null) || 
                (description != null) || 
                (priority != null) || 
                (start != null) || 
                !addresses.isEmpty() || 
                !documentation.isEmpty() || 
                !target.isEmpty();
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
                    accept(category, "category", visitor);
                    accept(description, "description", visitor);
                    accept(priority, "priority", visitor);
                    accept(start, "start", visitor);
                    accept(addresses, "addresses", visitor, CodeableConcept.class);
                    accept(documentation, "documentation", visitor, RelatedArtifact.class);
                    accept(target, "target", visitor, Target.class);
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept category;
            private CodeableConcept description;
            private CodeableConcept priority;
            private CodeableConcept start;
            private List<CodeableConcept> addresses = new ArrayList<>();
            private List<RelatedArtifact> documentation = new ArrayList<>();
            private List<Target> target = new ArrayList<>();

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
             * Indicates a category the goal falls within.
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
             * Human-readable and/or coded description of a specific desired objective of care, such as "control blood pressure" or 
             * "negotiate an obstacle course" or "dance with child at wedding".
             * 
             * <p>This element is required.
             * 
             * @param description
             *     Code or text describing the goal
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(CodeableConcept description) {
                this.description = description;
                return this;
            }

            /**
             * Identifies the expected level of importance associated with reaching/sustaining the defined goal.
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
             * The event after which the goal should begin being pursued.
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
             * Identifies problems, conditions, issues, or concerns the goal is intended to address.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Identifies problems, conditions, issues, or concerns the goal is intended to address.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param addresses
             *     What does the goal address
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder addresses(Collection<CodeableConcept> addresses) {
                this.addresses = new ArrayList<>(addresses);
                return this;
            }

            /**
             * Didactic or other informational resources associated with the goal that provide further supporting information about 
             * the goal. Information resources can include inline text commentary and links to web resources.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Didactic or other informational resources associated with the goal that provide further supporting information about 
             * the goal. Information resources can include inline text commentary and links to web resources.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param documentation
             *     Supporting documentation for the goal
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder documentation(Collection<RelatedArtifact> documentation) {
                this.documentation = new ArrayList<>(documentation);
                return this;
            }

            /**
             * Indicates what should be done and within what timeframe.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Indicates what should be done and within what timeframe.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param target
             *     Target outcome for the goal
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder target(Collection<Target> target) {
                this.target = new ArrayList<>(target);
                return this;
            }

            /**
             * Build the {@link Goal}
             * 
             * <p>Required elements:
             * <ul>
             * <li>description</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Goal}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Goal per the base specification
             */
            @Override
            public Goal build() {
                Goal goal = new Goal(this);
                if (validating) {
                    validate(goal);
                }
                return goal;
            }

            protected void validate(Goal goal) {
                super.validate(goal);
                ValidationSupport.requireNonNull(goal.description, "description");
                ValidationSupport.checkList(goal.addresses, "addresses", CodeableConcept.class);
                ValidationSupport.checkList(goal.documentation, "documentation", RelatedArtifact.class);
                ValidationSupport.checkList(goal.target, "target", Target.class);
                ValidationSupport.requireValueOrChildren(goal);
            }

            protected Builder from(Goal goal) {
                super.from(goal);
                category = goal.category;
                description = goal.description;
                priority = goal.priority;
                start = goal.start;
                addresses.addAll(goal.addresses);
                documentation.addAll(goal.documentation);
                target.addAll(goal.target);
                return this;
            }
        }

        /**
         * Indicates what should be done and within what timeframe.
         */
        public static class Target extends BackboneElement {
            @Binding(
                bindingName = "GoalTargetMeasure",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Identifies types of parameters that can be tracked to determine goal achievement.",
                valueSet = "http://hl7.org/fhir/ValueSet/observation-codes"
            )
            private final CodeableConcept measure;
            @Choice({ Quantity.class, Range.class, CodeableConcept.class })
            private final Element detail;
            private final Duration due;

            private Target(Builder builder) {
                super(builder);
                measure = builder.measure;
                detail = builder.detail;
                due = builder.due;
            }

            /**
             * The parameter whose value is to be tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getMeasure() {
                return measure;
            }

            /**
             * The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%, or in the 
             * case of pharmaceutical quality - NMT 0.6%, Clear solution, etc. Either the high or low or both values of the range can 
             * be specified. When a low value is missing, it indicates that the goal is achieved at any value at or below the high 
             * value. Similarly, if the high value is missing, it indicates that the goal is achieved at any value at or above the 
             * low value.
             * 
             * @return
             *     An immutable object of type {@link Quantity}, {@link Range} or {@link CodeableConcept} that may be null.
             */
            public Element getDetail() {
                return detail;
            }

            /**
             * Indicates the timeframe after the start of the goal in which the goal should be met.
             * 
             * @return
             *     An immutable object of type {@link Duration} that may be null.
             */
            public Duration getDue() {
                return due;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (measure != null) || 
                    (detail != null) || 
                    (due != null);
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
                        accept(measure, "measure", visitor);
                        accept(detail, "detail", visitor);
                        accept(due, "due", visitor);
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
                private CodeableConcept measure;
                private Element detail;
                private Duration due;

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
                 * The parameter whose value is to be tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.
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
                 * The target value of the measure to be achieved to signify fulfillment of the goal, e.g. 150 pounds or 7.0%, or in the 
                 * case of pharmaceutical quality - NMT 0.6%, Clear solution, etc. Either the high or low or both values of the range can 
                 * be specified. When a low value is missing, it indicates that the goal is achieved at any value at or below the high 
                 * value. Similarly, if the high value is missing, it indicates that the goal is achieved at any value at or above the 
                 * low value.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Quantity}</li>
                 * <li>{@link Range}</li>
                 * <li>{@link CodeableConcept}</li>
                 * </ul>
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
                 * Indicates the timeframe after the start of the goal in which the goal should be met.
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

                /**
                 * Build the {@link Target}
                 * 
                 * @return
                 *     An immutable object of type {@link Target}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Target per the base specification
                 */
                @Override
                public Target build() {
                    Target target = new Target(this);
                    if (validating) {
                        validate(target);
                    }
                    return target;
                }

                protected void validate(Target target) {
                    super.validate(target);
                    ValidationSupport.choiceElement(target.detail, "detail", Quantity.class, Range.class, CodeableConcept.class);
                    ValidationSupport.requireValueOrChildren(target);
                }

                protected Builder from(Target target) {
                    super.from(target);
                    measure = target.measure;
                    detail = target.detail;
                    due = target.due;
                    return this;
                }
            }
        }
    }

    /**
     * An action or group of actions to be taken as part of the plan. For example, in clinical care, an action would be to 
     * prescribe a particular indicated medication, or perform a particular test as appropriate. In pharmaceutical quality, 
     * an action would be the test that needs to be performed on a drug product as defined in the quality specification.
     */
    public static class Action extends BackboneElement {
        private final String prefix;
        private final String title;
        private final String description;
        private final String textEquivalent;
        @Binding(
            bindingName = "RequestPriority",
            strength = BindingStrength.Value.REQUIRED,
            description = "Identifies the level of importance to be assigned to actioning the request.",
            valueSet = "http://hl7.org/fhir/ValueSet/request-priority|4.3.0-CIBUILD"
        )
        private final RequestPriority priority;
        @Binding(
            bindingName = "ActionCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Provides examples of actions to be performed.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-code"
        )
        private final List<CodeableConcept> code;
        @Binding(
            bindingName = "ActionReasonCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Provides examples of reasons for actions to be performed.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-reason-code"
        )
        private final List<CodeableConcept> reason;
        private final List<RelatedArtifact> documentation;
        private final List<Id> goalId;
        @ReferenceTarget({ "Group" })
        @Choice({ CodeableConcept.class, Reference.class, Canonical.class })
        @Binding(
            bindingName = "SubjectType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The possible types of subjects for a plan definition (E.g. Patient, Practitioner, Organization, Location, etc.).",
            valueSet = "http://hl7.org/fhir/ValueSet/subject-type"
        )
        private final Element subject;
        private final List<TriggerDefinition> trigger;
        private final List<Condition> condition;
        private final List<DataRequirement> input;
        private final List<DataRequirement> output;
        private final List<RelatedAction> relatedAction;
        @Choice({ DateTime.class, Age.class, Period.class, Duration.class, Range.class, Timing.class })
        private final Element timing;
        private final List<Participant> participant;
        @Binding(
            bindingName = "ActionType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The type of action to be performed.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-type"
        )
        private final CodeableConcept type;
        @Binding(
            bindingName = "ActionGroupingBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Defines organization behavior of a group.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-grouping-behavior|4.3.0-CIBUILD"
        )
        private final ActionGroupingBehavior groupingBehavior;
        @Binding(
            bindingName = "ActionSelectionBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Defines selection behavior of a group.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-selection-behavior|4.3.0-CIBUILD"
        )
        private final ActionSelectionBehavior selectionBehavior;
        @Binding(
            bindingName = "ActionRequiredBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Defines expectations around whether an action or action group is required.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-required-behavior|4.3.0-CIBUILD"
        )
        private final ActionRequiredBehavior requiredBehavior;
        @Binding(
            bindingName = "ActionPrecheckBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Defines selection frequency behavior for an action or group.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-precheck-behavior|4.3.0-CIBUILD"
        )
        private final ActionPrecheckBehavior precheckBehavior;
        @Binding(
            bindingName = "ActionCardinalityBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Defines behavior for an action or a group for how many times that item may be repeated.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-cardinality-behavior|4.3.0-CIBUILD"
        )
        private final ActionCardinalityBehavior cardinalityBehavior;
        @Choice({ Canonical.class, Uri.class })
        private final Element definition;
        private final Canonical transform;
        private final List<DynamicValue> dynamicValue;
        private final List<PlanDefinition.Action> action;

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
            subject = builder.subject;
            trigger = Collections.unmodifiableList(builder.trigger);
            condition = Collections.unmodifiableList(builder.condition);
            input = Collections.unmodifiableList(builder.input);
            output = Collections.unmodifiableList(builder.output);
            relatedAction = Collections.unmodifiableList(builder.relatedAction);
            timing = builder.timing;
            participant = Collections.unmodifiableList(builder.participant);
            type = builder.type;
            groupingBehavior = builder.groupingBehavior;
            selectionBehavior = builder.selectionBehavior;
            requiredBehavior = builder.requiredBehavior;
            precheckBehavior = builder.precheckBehavior;
            cardinalityBehavior = builder.cardinalityBehavior;
            definition = builder.definition;
            transform = builder.transform;
            dynamicValue = Collections.unmodifiableList(builder.dynamicValue);
            action = Collections.unmodifiableList(builder.action);
        }

        /**
         * A user-visible prefix for the action.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * The textual description of the action displayed to a user. For example, when the action is a test to be performed, the 
         * title would be the title of the test such as Assay by HPLC.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getTitle() {
            return title;
        }

        /**
         * A brief description of the action used to provide a summary to display to the user.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when 
         * the definition is consumed by a system that might not be capable of interpreting it dynamically.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getTextEquivalent() {
            return textEquivalent;
        }

        /**
         * Indicates how quickly the action should be addressed with respect to other actions.
         * 
         * @return
         *     An immutable object of type {@link RequestPriority} that may be null.
         */
        public RequestPriority getPriority() {
            return priority;
        }

        /**
         * A code that provides a meaning, grouping, or classification for the action or action group. For example, a section may 
         * have a LOINC code for the section of a documentation template. In pharmaceutical quality, an action (Test) such as pH 
         * could be classified as a physical property.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getCode() {
            return code;
        }

        /**
         * A description of why this action is necessary or appropriate.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getReason() {
            return reason;
        }

        /**
         * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
         * Information resources can include inline text commentary and links to web resources.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact} that may be empty.
         */
        public List<RelatedArtifact> getDocumentation() {
            return documentation;
        }

        /**
         * Identifies goals that this action supports. The reference must be to a goal element defined within this plan 
         * definition. In pharmaceutical quality, a goal represents acceptance criteria (Goal) for a given action (Test), so the 
         * goalId would be the unique id of a defined goal element establishing the acceptance criteria for the action.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Id} that may be empty.
         */
        public List<Id> getGoalId() {
            return goalId;
        }

        /**
         * A code, group definition, or canonical reference that describes the intended subject of the action and its children, 
         * if any. Canonical references are allowed to support the definition of protocols for drug and substance quality 
         * specifications, and is allowed to reference a MedicinalProductDefinition, SubstanceDefinition, 
         * AdministrableProductDefinition, ManufacturedItemDefinition, or PackagedProductDefinition resource.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}, {@link Reference} or {@link Canonical} that may be null.
         */
        public Element getSubject() {
            return subject;
        }

        /**
         * A description of when the action should be triggered.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link TriggerDefinition} that may be empty.
         */
        public List<TriggerDefinition> getTrigger() {
            return trigger;
        }

        /**
         * An expression that describes applicability criteria or start/stop conditions for the action.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Condition} that may be empty.
         */
        public List<Condition> getCondition() {
            return condition;
        }

        /**
         * Defines input data requirements for the action.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DataRequirement} that may be empty.
         */
        public List<DataRequirement> getInput() {
            return input;
        }

        /**
         * Defines the outputs of the action, if any.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DataRequirement} that may be empty.
         */
        public List<DataRequirement> getOutput() {
            return output;
        }

        /**
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RelatedAction} that may be empty.
         */
        public List<RelatedAction> getRelatedAction() {
            return relatedAction;
        }

        /**
         * An optional value describing when the action should be performed.
         * 
         * @return
         *     An immutable object of type {@link DateTime}, {@link Age}, {@link Period}, {@link Duration}, {@link Range} or {@link 
         *     Timing} that may be null.
         */
        public Element getTiming() {
            return timing;
        }

        /**
         * Indicates who should participate in performing the action described.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Participant} that may be empty.
         */
        public List<Participant> getParticipant() {
            return participant;
        }

        /**
         * The type of action to perform (create, update, remove).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Defines the grouping behavior for the action and its children.
         * 
         * @return
         *     An immutable object of type {@link ActionGroupingBehavior} that may be null.
         */
        public ActionGroupingBehavior getGroupingBehavior() {
            return groupingBehavior;
        }

        /**
         * Defines the selection behavior for the action and its children.
         * 
         * @return
         *     An immutable object of type {@link ActionSelectionBehavior} that may be null.
         */
        public ActionSelectionBehavior getSelectionBehavior() {
            return selectionBehavior;
        }

        /**
         * Defines the required behavior for the action.
         * 
         * @return
         *     An immutable object of type {@link ActionRequiredBehavior} that may be null.
         */
        public ActionRequiredBehavior getRequiredBehavior() {
            return requiredBehavior;
        }

        /**
         * Defines whether the action should usually be preselected.
         * 
         * @return
         *     An immutable object of type {@link ActionPrecheckBehavior} that may be null.
         */
        public ActionPrecheckBehavior getPrecheckBehavior() {
            return precheckBehavior;
        }

        /**
         * Defines whether the action can be selected multiple times.
         * 
         * @return
         *     An immutable object of type {@link ActionCardinalityBehavior} that may be null.
         */
        public ActionCardinalityBehavior getCardinalityBehavior() {
            return cardinalityBehavior;
        }

        /**
         * A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that 
         * describes a series of actions to be taken.
         * 
         * @return
         *     An immutable object of type {@link Canonical} or {@link Uri} that may be null.
         */
        public Element getDefinition() {
            return definition;
        }

        /**
         * A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource 
         * using the ActivityDefinition instance as the input.
         * 
         * @return
         *     An immutable object of type {@link Canonical} that may be null.
         */
        public Canonical getTransform() {
            return transform;
        }

        /**
         * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication 
         * must be computed based on the patient's weight, a customization would be used to specify an expression that calculated 
         * the weight, and the path on the resource that would contain the result.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DynamicValue} that may be empty.
         */
        public List<DynamicValue> getDynamicValue() {
            return dynamicValue;
        }

        /**
         * Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-
         * actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen 
         * as part of realizing the action definition.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action} that may be empty.
         */
        public List<PlanDefinition.Action> getAction() {
            return action;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (prefix != null) || 
                (title != null) || 
                (description != null) || 
                (textEquivalent != null) || 
                (priority != null) || 
                !code.isEmpty() || 
                !reason.isEmpty() || 
                !documentation.isEmpty() || 
                !goalId.isEmpty() || 
                (subject != null) || 
                !trigger.isEmpty() || 
                !condition.isEmpty() || 
                !input.isEmpty() || 
                !output.isEmpty() || 
                !relatedAction.isEmpty() || 
                (timing != null) || 
                !participant.isEmpty() || 
                (type != null) || 
                (groupingBehavior != null) || 
                (selectionBehavior != null) || 
                (requiredBehavior != null) || 
                (precheckBehavior != null) || 
                (cardinalityBehavior != null) || 
                (definition != null) || 
                (transform != null) || 
                !dynamicValue.isEmpty() || 
                !action.isEmpty();
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
             * Convenience method for setting {@code prefix}.
             * 
             * @param prefix
             *     User-visible prefix for the action (e.g. 1. or A.)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #prefix(com.ibm.fhir.model.type.String)
             */
            public Builder prefix(java.lang.String prefix) {
                this.prefix = (prefix == null) ? null : String.of(prefix);
                return this;
            }

            /**
             * A user-visible prefix for the action.
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
             * Convenience method for setting {@code title}.
             * 
             * @param title
             *     User-visible title
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
             * The textual description of the action displayed to a user. For example, when the action is a test to be performed, the 
             * title would be the title of the test such as Assay by HPLC.
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
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Brief description of the action
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
             * A brief description of the action used to provide a summary to display to the user.
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
             * Convenience method for setting {@code textEquivalent}.
             * 
             * @param textEquivalent
             *     Static text equivalent of the action, used if the dynamic aspects cannot be interpreted by the receiving system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #textEquivalent(com.ibm.fhir.model.type.String)
             */
            public Builder textEquivalent(java.lang.String textEquivalent) {
                this.textEquivalent = (textEquivalent == null) ? null : String.of(textEquivalent);
                return this;
            }

            /**
             * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when 
             * the definition is consumed by a system that might not be capable of interpreting it dynamically.
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
             * Indicates how quickly the action should be addressed with respect to other actions.
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
             * A code that provides a meaning, grouping, or classification for the action or action group. For example, a section may 
             * have a LOINC code for the section of a documentation template. In pharmaceutical quality, an action (Test) such as pH 
             * could be classified as a physical property.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * A code that provides a meaning, grouping, or classification for the action or action group. For example, a section may 
             * have a LOINC code for the section of a documentation template. In pharmaceutical quality, an action (Test) such as pH 
             * could be classified as a physical property.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     Code representing the meaning of the action or sub-actions
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder code(Collection<CodeableConcept> code) {
                this.code = new ArrayList<>(code);
                return this;
            }

            /**
             * A description of why this action is necessary or appropriate.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * A description of why this action is necessary or appropriate.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param reason
             *     Why the action should be performed
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder reason(Collection<CodeableConcept> reason) {
                this.reason = new ArrayList<>(reason);
                return this;
            }

            /**
             * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
             * Information resources can include inline text commentary and links to web resources.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
             * Information resources can include inline text commentary and links to web resources.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param documentation
             *     Supporting documentation for the intended performer of the action
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder documentation(Collection<RelatedArtifact> documentation) {
                this.documentation = new ArrayList<>(documentation);
                return this;
            }

            /**
             * Identifies goals that this action supports. The reference must be to a goal element defined within this plan 
             * definition. In pharmaceutical quality, a goal represents acceptance criteria (Goal) for a given action (Test), so the 
             * goalId would be the unique id of a defined goal element establishing the acceptance criteria for the action.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Identifies goals that this action supports. The reference must be to a goal element defined within this plan 
             * definition. In pharmaceutical quality, a goal represents acceptance criteria (Goal) for a given action (Test), so the 
             * goalId would be the unique id of a defined goal element establishing the acceptance criteria for the action.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param goalId
             *     What goals this action supports
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder goalId(Collection<Id> goalId) {
                this.goalId = new ArrayList<>(goalId);
                return this;
            }

            /**
             * A code, group definition, or canonical reference that describes the intended subject of the action and its children, 
             * if any. Canonical references are allowed to support the definition of protocols for drug and substance quality 
             * specifications, and is allowed to reference a MedicinalProductDefinition, SubstanceDefinition, 
             * AdministrableProductDefinition, ManufacturedItemDefinition, or PackagedProductDefinition resource.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Reference}</li>
             * <li>{@link Canonical}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link Group}</li>
             * </ul>
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
             * A description of when the action should be triggered.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * A description of when the action should be triggered.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param trigger
             *     When the action should be triggered
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder trigger(Collection<TriggerDefinition> trigger) {
                this.trigger = new ArrayList<>(trigger);
                return this;
            }

            /**
             * An expression that describes applicability criteria or start/stop conditions for the action.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * An expression that describes applicability criteria or start/stop conditions for the action.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param condition
             *     Whether or not the action is applicable
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder condition(Collection<Condition> condition) {
                this.condition = new ArrayList<>(condition);
                return this;
            }

            /**
             * Defines input data requirements for the action.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Defines input data requirements for the action.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param input
             *     Input data requirements
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder input(Collection<DataRequirement> input) {
                this.input = new ArrayList<>(input);
                return this;
            }

            /**
             * Defines the outputs of the action, if any.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Defines the outputs of the action, if any.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param output
             *     Output data definition
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder output(Collection<DataRequirement> output) {
                this.output = new ArrayList<>(output);
                return this;
            }

            /**
             * A relationship to another action such as "before" or "30-60 minutes after start of".
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * A relationship to another action such as "before" or "30-60 minutes after start of".
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param relatedAction
             *     Relationship to another action
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder relatedAction(Collection<RelatedAction> relatedAction) {
                this.relatedAction = new ArrayList<>(relatedAction);
                return this;
            }

            /**
             * An optional value describing when the action should be performed.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link DateTime}</li>
             * <li>{@link Age}</li>
             * <li>{@link Period}</li>
             * <li>{@link Duration}</li>
             * <li>{@link Range}</li>
             * <li>{@link Timing}</li>
             * </ul>
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
             * Indicates who should participate in performing the action described.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Indicates who should participate in performing the action described.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param participant
             *     Who should participate in the action
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder participant(Collection<Participant> participant) {
                this.participant = new ArrayList<>(participant);
                return this;
            }

            /**
             * The type of action to perform (create, update, remove).
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
             * Defines the grouping behavior for the action and its children.
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
             * Defines the selection behavior for the action and its children.
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
             * Defines the required behavior for the action.
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
             * Defines whether the action should usually be preselected.
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
             * Defines whether the action can be selected multiple times.
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
             * A reference to an ActivityDefinition that describes the action to be taken in detail, or a PlanDefinition that 
             * describes a series of actions to be taken.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Canonical}</li>
             * <li>{@link Uri}</li>
             * </ul>
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
             * A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource 
             * using the ActivityDefinition instance as the input.
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
             * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication 
             * must be computed based on the patient's weight, a customization would be used to specify an expression that calculated 
             * the weight, and the path on the resource that would contain the result.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication 
             * must be computed based on the patient's weight, a customization would be used to specify an expression that calculated 
             * the weight, and the path on the resource that would contain the result.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param dynamicValue
             *     Dynamic aspects of the definition
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder dynamicValue(Collection<DynamicValue> dynamicValue) {
                this.dynamicValue = new ArrayList<>(dynamicValue);
                return this;
            }

            /**
             * Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-
             * actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen 
             * as part of realizing the action definition.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-
             * actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen 
             * as part of realizing the action definition.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param action
             *     A sub-action
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder action(Collection<PlanDefinition.Action> action) {
                this.action = new ArrayList<>(action);
                return this;
            }

            /**
             * Build the {@link Action}
             * 
             * @return
             *     An immutable object of type {@link Action}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Action per the base specification
             */
            @Override
            public Action build() {
                Action action = new Action(this);
                if (validating) {
                    validate(action);
                }
                return action;
            }

            protected void validate(Action action) {
                super.validate(action);
                ValidationSupport.checkList(action.code, "code", CodeableConcept.class);
                ValidationSupport.checkList(action.reason, "reason", CodeableConcept.class);
                ValidationSupport.checkList(action.documentation, "documentation", RelatedArtifact.class);
                ValidationSupport.checkList(action.goalId, "goalId", Id.class);
                ValidationSupport.choiceElement(action.subject, "subject", CodeableConcept.class, Reference.class, Canonical.class);
                ValidationSupport.checkList(action.trigger, "trigger", TriggerDefinition.class);
                ValidationSupport.checkList(action.condition, "condition", Condition.class);
                ValidationSupport.checkList(action.input, "input", DataRequirement.class);
                ValidationSupport.checkList(action.output, "output", DataRequirement.class);
                ValidationSupport.checkList(action.relatedAction, "relatedAction", RelatedAction.class);
                ValidationSupport.choiceElement(action.timing, "timing", DateTime.class, Age.class, Period.class, Duration.class, Range.class, Timing.class);
                ValidationSupport.checkList(action.participant, "participant", Participant.class);
                ValidationSupport.choiceElement(action.definition, "definition", Canonical.class, Uri.class);
                ValidationSupport.checkList(action.dynamicValue, "dynamicValue", DynamicValue.class);
                ValidationSupport.checkList(action.action, "action", PlanDefinition.Action.class);
                ValidationSupport.checkReferenceType(action.subject, "subject", "Group");
                ValidationSupport.requireValueOrChildren(action);
            }

            protected Builder from(Action action) {
                super.from(action);
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
         * An expression that describes applicability criteria or start/stop conditions for the action.
         */
        public static class Condition extends BackboneElement {
            @Binding(
                bindingName = "ActionConditionKind",
                strength = BindingStrength.Value.REQUIRED,
                description = "Defines the kinds of conditions that can appear on actions.",
                valueSet = "http://hl7.org/fhir/ValueSet/action-condition-kind|4.3.0-CIBUILD"
            )
            @Required
            private final ActionConditionKind kind;
            private final Expression expression;

            private Condition(Builder builder) {
                super(builder);
                kind = builder.kind;
                expression = builder.expression;
            }

            /**
             * The kind of condition.
             * 
             * @return
             *     An immutable object of type {@link ActionConditionKind} that is non-null.
             */
            public ActionConditionKind getKind() {
                return kind;
            }

            /**
             * An expression that returns true or false, indicating whether the condition is satisfied.
             * 
             * @return
             *     An immutable object of type {@link Expression} that may be null.
             */
            public Expression getExpression() {
                return expression;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (kind != null) || 
                    (expression != null);
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
                        accept(kind, "kind", visitor);
                        accept(expression, "expression", visitor);
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
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private ActionConditionKind kind;
                private Expression expression;

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
                 * The kind of condition.
                 * 
                 * <p>This element is required.
                 * 
                 * @param kind
                 *     applicability | start | stop
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder kind(ActionConditionKind kind) {
                    this.kind = kind;
                    return this;
                }

                /**
                 * An expression that returns true or false, indicating whether the condition is satisfied.
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

                /**
                 * Build the {@link Condition}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>kind</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Condition}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Condition per the base specification
                 */
                @Override
                public Condition build() {
                    Condition condition = new Condition(this);
                    if (validating) {
                        validate(condition);
                    }
                    return condition;
                }

                protected void validate(Condition condition) {
                    super.validate(condition);
                    ValidationSupport.requireNonNull(condition.kind, "kind");
                    ValidationSupport.requireValueOrChildren(condition);
                }

                protected Builder from(Condition condition) {
                    super.from(condition);
                    kind = condition.kind;
                    expression = condition.expression;
                    return this;
                }
            }
        }

        /**
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         */
        public static class RelatedAction extends BackboneElement {
            @Required
            private final Id actionId;
            @Binding(
                bindingName = "ActionRelationshipType",
                strength = BindingStrength.Value.REQUIRED,
                description = "Defines the types of relationships between actions.",
                valueSet = "http://hl7.org/fhir/ValueSet/action-relationship-type|4.3.0-CIBUILD"
            )
            @Required
            private final ActionRelationshipType relationship;
            @Choice({ Duration.class, Range.class })
            private final Element offset;

            private RelatedAction(Builder builder) {
                super(builder);
                actionId = builder.actionId;
                relationship = builder.relationship;
                offset = builder.offset;
            }

            /**
             * The element id of the related action.
             * 
             * @return
             *     An immutable object of type {@link Id} that is non-null.
             */
            public Id getActionId() {
                return actionId;
            }

            /**
             * The relationship of this action to the related action.
             * 
             * @return
             *     An immutable object of type {@link ActionRelationshipType} that is non-null.
             */
            public ActionRelationshipType getRelationship() {
                return relationship;
            }

            /**
             * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
             * 
             * @return
             *     An immutable object of type {@link Duration} or {@link Range} that may be null.
             */
            public Element getOffset() {
                return offset;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (actionId != null) || 
                    (relationship != null) || 
                    (offset != null);
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
                        accept(actionId, "actionId", visitor);
                        accept(relationship, "relationship", visitor);
                        accept(offset, "offset", visitor);
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
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private Id actionId;
                private ActionRelationshipType relationship;
                private Element offset;

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
                 * The element id of the related action.
                 * 
                 * <p>This element is required.
                 * 
                 * @param actionId
                 *     What action is this related to
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder actionId(Id actionId) {
                    this.actionId = actionId;
                    return this;
                }

                /**
                 * The relationship of this action to the related action.
                 * 
                 * <p>This element is required.
                 * 
                 * @param relationship
                 *     before-start | before | before-end | concurrent-with-start | concurrent | concurrent-with-end | after-start | after | 
                 *     after-end
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder relationship(ActionRelationshipType relationship) {
                    this.relationship = relationship;
                    return this;
                }

                /**
                 * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Duration}</li>
                 * <li>{@link Range}</li>
                 * </ul>
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

                /**
                 * Build the {@link RelatedAction}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>actionId</li>
                 * <li>relationship</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link RelatedAction}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid RelatedAction per the base specification
                 */
                @Override
                public RelatedAction build() {
                    RelatedAction relatedAction = new RelatedAction(this);
                    if (validating) {
                        validate(relatedAction);
                    }
                    return relatedAction;
                }

                protected void validate(RelatedAction relatedAction) {
                    super.validate(relatedAction);
                    ValidationSupport.requireNonNull(relatedAction.actionId, "actionId");
                    ValidationSupport.requireNonNull(relatedAction.relationship, "relationship");
                    ValidationSupport.choiceElement(relatedAction.offset, "offset", Duration.class, Range.class);
                    ValidationSupport.requireValueOrChildren(relatedAction);
                }

                protected Builder from(RelatedAction relatedAction) {
                    super.from(relatedAction);
                    actionId = relatedAction.actionId;
                    relationship = relatedAction.relationship;
                    offset = relatedAction.offset;
                    return this;
                }
            }
        }

        /**
         * Indicates who should participate in performing the action described.
         */
        public static class Participant extends BackboneElement {
            @Binding(
                bindingName = "ActionParticipantType",
                strength = BindingStrength.Value.REQUIRED,
                description = "The type of participant for the action.",
                valueSet = "http://hl7.org/fhir/ValueSet/action-participant-type|4.3.0-CIBUILD"
            )
            @Required
            private final ActionParticipantType type;
            @Binding(
                bindingName = "ActionParticipantRole",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Defines roles played by participants for the action.",
                valueSet = "http://terminology.hl7.org/ValueSet/action-participant-role"
            )
            private final CodeableConcept role;

            private Participant(Builder builder) {
                super(builder);
                type = builder.type;
                role = builder.role;
            }

            /**
             * The type of participant in the action.
             * 
             * @return
             *     An immutable object of type {@link ActionParticipantType} that is non-null.
             */
            public ActionParticipantType getType() {
                return type;
            }

            /**
             * The role the participant should play in performing the described action.
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
                    (type != null) || 
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
                        accept(type, "type", visitor);
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
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private ActionParticipantType type;
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
                 * The type of participant in the action.
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     patient | practitioner | related-person | device
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(ActionParticipantType type) {
                    this.type = type;
                    return this;
                }

                /**
                 * The role the participant should play in performing the described action.
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

                /**
                 * Build the {@link Participant}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Participant}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Participant per the base specification
                 */
                @Override
                public Participant build() {
                    Participant participant = new Participant(this);
                    if (validating) {
                        validate(participant);
                    }
                    return participant;
                }

                protected void validate(Participant participant) {
                    super.validate(participant);
                    ValidationSupport.requireNonNull(participant.type, "type");
                    ValidationSupport.requireValueOrChildren(participant);
                }

                protected Builder from(Participant participant) {
                    super.from(participant);
                    type = participant.type;
                    role = participant.role;
                    return this;
                }
            }
        }

        /**
         * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication 
         * must be computed based on the patient's weight, a customization would be used to specify an expression that calculated 
         * the weight, and the path on the resource that would contain the result.
         */
        public static class DynamicValue extends BackboneElement {
            private final String path;
            private final Expression expression;

            private DynamicValue(Builder builder) {
                super(builder);
                path = builder.path;
                expression = builder.expression;
            }

            /**
             * The path to the element to be customized. This is the path on the resource that will hold the result of the 
             * calculation defined by the expression. The specified path SHALL be a FHIRPath resolveable on the specified target type 
             * of the ActivityDefinition, and SHALL consist only of identifiers, constant indexers, and a restricted subset of 
             * functions. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to 
             * traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getPath() {
                return path;
            }

            /**
             * An expression specifying the value of the customized element.
             * 
             * @return
             *     An immutable object of type {@link Expression} that may be null.
             */
            public Expression getExpression() {
                return expression;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (path != null) || 
                    (expression != null);
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
                        accept(path, "path", visitor);
                        accept(expression, "expression", visitor);
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
                private String path;
                private Expression expression;

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
                 * Convenience method for setting {@code path}.
                 * 
                 * @param path
                 *     The path to the element to be set dynamically
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #path(com.ibm.fhir.model.type.String)
                 */
                public Builder path(java.lang.String path) {
                    this.path = (path == null) ? null : String.of(path);
                    return this;
                }

                /**
                 * The path to the element to be customized. This is the path on the resource that will hold the result of the 
                 * calculation defined by the expression. The specified path SHALL be a FHIRPath resolveable on the specified target type 
                 * of the ActivityDefinition, and SHALL consist only of identifiers, constant indexers, and a restricted subset of 
                 * functions. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to 
                 * traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).
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
                 * An expression specifying the value of the customized element.
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

                /**
                 * Build the {@link DynamicValue}
                 * 
                 * @return
                 *     An immutable object of type {@link DynamicValue}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid DynamicValue per the base specification
                 */
                @Override
                public DynamicValue build() {
                    DynamicValue dynamicValue = new DynamicValue(this);
                    if (validating) {
                        validate(dynamicValue);
                    }
                    return dynamicValue;
                }

                protected void validate(DynamicValue dynamicValue) {
                    super.validate(dynamicValue);
                    ValidationSupport.requireValueOrChildren(dynamicValue);
                }

                protected Builder from(DynamicValue dynamicValue) {
                    super.from(dynamicValue);
                    path = dynamicValue.path;
                    expression = dynamicValue.expression;
                    return this;
                }
            }
        }
    }
}
