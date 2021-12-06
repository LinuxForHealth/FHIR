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
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ResearchStudyStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A process where a researcher or organization plans and then executes a series of steps intended to increase the field 
 * of healthcare-related knowledge. This includes studies of safety, efficacy, comparative effectiveness and other 
 * information about medications, devices, therapies and other interventional and investigative techniques. A 
 * ResearchStudy involves the gathering of information about human or animal subjects.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "researchStudy-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/research-study-prim-purp-type",
    expression = "primaryPurposeType.exists() implies (primaryPurposeType.memberOf('http://hl7.org/fhir/ValueSet/research-study-prim-purp-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/ResearchStudy",
    generated = true
)
@Constraint(
    id = "researchStudy-1",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "location.exists() implies (location.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/ResearchStudy",
    generated = true
)
@Constraint(
    id = "researchStudy-2",
    level = "Warning",
    location = "objective.type",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/research-study-objective-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/research-study-objective-type', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/ResearchStudy",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ResearchStudy extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final String title;
    @Summary
    @ReferenceTarget({ "PlanDefinition" })
    private final List<Reference> protocol;
    @Summary
    @ReferenceTarget({ "ResearchStudy" })
    private final List<Reference> partOf;
    @Summary
    @Binding(
        bindingName = "ResearchStudyStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes that convey the current status of the research study.",
        valueSet = "http://hl7.org/fhir/ValueSet/research-study-status|4.1.0"
    )
    @Required
    private final ResearchStudyStatus status;
    @Summary
    @Binding(
        bindingName = "ResearchStudyPrimaryPurposeType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Codes for the main intent of the study.",
        valueSet = "http://hl7.org/fhir/ValueSet/research-study-prim-purp-type"
    )
    private final CodeableConcept primaryPurposeType;
    @Summary
    @Binding(
        bindingName = "ResearchStudyPhase",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes for the stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market evaluation.",
        valueSet = "http://hl7.org/fhir/ValueSet/research-study-phase"
    )
    private final CodeableConcept phase;
    @Summary
    @Binding(
        bindingName = "ResearchStudyCategory",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes that describe the type of research study.  E.g. Study phase, Interventional/Observational, blinding type, etc."
    )
    private final List<CodeableConcept> category;
    @Summary
    @Binding(
        bindingName = "ResearchStudyFocus",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes for medications, devices and other interventions."
    )
    private final List<CodeableConcept> focus;
    @Summary
    @Binding(
        bindingName = "ConditionCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Identification of the condition or diagnosis.",
        valueSet = "http://hl7.org/fhir/ValueSet/condition-code"
    )
    private final List<CodeableConcept> condition;
    @Summary
    private final List<ContactDetail> contact;
    private final List<RelatedArtifact> relatedArtifact;
    @Summary
    @Binding(
        bindingName = "ResearchStudyKeyword",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Words associated with the study that may be useful in discovery."
    )
    private final List<CodeableConcept> keyword;
    @Summary
    @Binding(
        bindingName = "Jurisdiction",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Countries and regions within which this artifact is targeted for use.",
        valueSet = "http://hl7.org/fhir/ValueSet/jurisdiction"
    )
    private final List<CodeableConcept> location;
    private final Markdown description;
    @Summary
    @ReferenceTarget({ "Group" })
    private final List<Reference> enrollment;
    @Summary
    private final Period period;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference sponsor;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole" })
    private final Reference principalInvestigator;
    @Summary
    @ReferenceTarget({ "Location" })
    private final List<Reference> site;
    @Summary
    @Binding(
        bindingName = "ResearchStudyReasonStopped",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes for why the study ended prematurely.",
        valueSet = "http://hl7.org/fhir/ValueSet/research-study-reason-stopped"
    )
    private final CodeableConcept reasonStopped;
    private final List<Annotation> note;
    private final List<Arm> arm;
    private final List<Objective> objective;

    private ResearchStudy(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        title = builder.title;
        protocol = Collections.unmodifiableList(builder.protocol);
        partOf = Collections.unmodifiableList(builder.partOf);
        status = builder.status;
        primaryPurposeType = builder.primaryPurposeType;
        phase = builder.phase;
        category = Collections.unmodifiableList(builder.category);
        focus = Collections.unmodifiableList(builder.focus);
        condition = Collections.unmodifiableList(builder.condition);
        contact = Collections.unmodifiableList(builder.contact);
        relatedArtifact = Collections.unmodifiableList(builder.relatedArtifact);
        keyword = Collections.unmodifiableList(builder.keyword);
        location = Collections.unmodifiableList(builder.location);
        description = builder.description;
        enrollment = Collections.unmodifiableList(builder.enrollment);
        period = builder.period;
        sponsor = builder.sponsor;
        principalInvestigator = builder.principalInvestigator;
        site = Collections.unmodifiableList(builder.site);
        reasonStopped = builder.reasonStopped;
        note = Collections.unmodifiableList(builder.note);
        arm = Collections.unmodifiableList(builder.arm);
        objective = Collections.unmodifiableList(builder.objective);
    }

    /**
     * Identifiers assigned to this research study by the sponsor or other systems.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A short, descriptive user-friendly label for the study.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The set of steps expected to be performed as part of the execution of the study.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getProtocol() {
        return protocol;
    }

    /**
     * A larger research study of which this particular study is a component or step.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * The current state of the study.
     * 
     * @return
     *     An immutable object of type {@link ResearchStudyStatus} that is non-null.
     */
    public ResearchStudyStatus getStatus() {
        return status;
    }

    /**
     * The type of study based upon the intent of the study's activities. A classification of the intent of the study.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPrimaryPurposeType() {
        return primaryPurposeType;
    }

    /**
     * The stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market 
     * evaluation.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPhase() {
        return phase;
    }

    /**
     * Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of 
     * randomization, safety vs. efficacy, etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to 
     * gain more information about.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getFocus() {
        return focus;
    }

    /**
     * The condition that is the focus of the study. For example, In a study to examine risk factors for Lupus, might have as 
     * an inclusion criterion "healthy volunteer", but the target condition code would be a Lupus SNOMED code.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCondition() {
        return condition;
    }

    /**
     * Contact details to assist a user in learning more about or engaging with the study.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * Citations, references and other related documents.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact} that may be empty.
     */
    public List<RelatedArtifact> getRelatedArtifact() {
        return relatedArtifact;
    }

    /**
     * Key terms to aid in searching for or filtering the study.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getKeyword() {
        return keyword;
    }

    /**
     * Indicates a country, state or other region where the study is taking place.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getLocation() {
        return location;
    }

    /**
     * A full description of how the study is being conducted.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * Reference to a Group that defines the criteria for and quantity of subjects participating in the study. E.g. " 200 
     * female Europeans between the ages of 20 and 45 with early onset diabetes".
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getEnrollment() {
        return enrollment;
    }

    /**
     * Identifies the start date and the expected (or actual, depending on status) end date for the study.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * An organization that initiates the investigation and is legally responsible for the study.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSponsor() {
        return sponsor;
    }

    /**
     * A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, 
     * protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, 
     * interpretation and presentation.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPrincipalInvestigator() {
        return principalInvestigator;
    }

    /**
     * A facility in which study activities are conducted.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSite() {
        return site;
    }

    /**
     * A description and/or code explaining the premature termination of the study.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getReasonStopped() {
        return reasonStopped;
    }

    /**
     * Comments made about the study by the performer, subject or other participants.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Describes an expected sequence of events for one of the participants of a study. E.g. Exposure to drug A, wash-out, 
     * exposure to drug B, wash-out, follow-up.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Arm} that may be empty.
     */
    public List<Arm> getArm() {
        return arm;
    }

    /**
     * A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data 
     * collected during the study.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Objective} that may be empty.
     */
    public List<Objective> getObjective() {
        return objective;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (title != null) || 
            !protocol.isEmpty() || 
            !partOf.isEmpty() || 
            (status != null) || 
            (primaryPurposeType != null) || 
            (phase != null) || 
            !category.isEmpty() || 
            !focus.isEmpty() || 
            !condition.isEmpty() || 
            !contact.isEmpty() || 
            !relatedArtifact.isEmpty() || 
            !keyword.isEmpty() || 
            !location.isEmpty() || 
            (description != null) || 
            !enrollment.isEmpty() || 
            (period != null) || 
            (sponsor != null) || 
            (principalInvestigator != null) || 
            !site.isEmpty() || 
            (reasonStopped != null) || 
            !note.isEmpty() || 
            !arm.isEmpty() || 
            !objective.isEmpty();
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
                accept(title, "title", visitor);
                accept(protocol, "protocol", visitor, Reference.class);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(primaryPurposeType, "primaryPurposeType", visitor);
                accept(phase, "phase", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(focus, "focus", visitor, CodeableConcept.class);
                accept(condition, "condition", visitor, CodeableConcept.class);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(relatedArtifact, "relatedArtifact", visitor, RelatedArtifact.class);
                accept(keyword, "keyword", visitor, CodeableConcept.class);
                accept(location, "location", visitor, CodeableConcept.class);
                accept(description, "description", visitor);
                accept(enrollment, "enrollment", visitor, Reference.class);
                accept(period, "period", visitor);
                accept(sponsor, "sponsor", visitor);
                accept(principalInvestigator, "principalInvestigator", visitor);
                accept(site, "site", visitor, Reference.class);
                accept(reasonStopped, "reasonStopped", visitor);
                accept(note, "note", visitor, Annotation.class);
                accept(arm, "arm", visitor, Arm.class);
                accept(objective, "objective", visitor, Objective.class);
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
        ResearchStudy other = (ResearchStudy) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(title, other.title) && 
            Objects.equals(protocol, other.protocol) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(status, other.status) && 
            Objects.equals(primaryPurposeType, other.primaryPurposeType) && 
            Objects.equals(phase, other.phase) && 
            Objects.equals(category, other.category) && 
            Objects.equals(focus, other.focus) && 
            Objects.equals(condition, other.condition) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(relatedArtifact, other.relatedArtifact) && 
            Objects.equals(keyword, other.keyword) && 
            Objects.equals(location, other.location) && 
            Objects.equals(description, other.description) && 
            Objects.equals(enrollment, other.enrollment) && 
            Objects.equals(period, other.period) && 
            Objects.equals(sponsor, other.sponsor) && 
            Objects.equals(principalInvestigator, other.principalInvestigator) && 
            Objects.equals(site, other.site) && 
            Objects.equals(reasonStopped, other.reasonStopped) && 
            Objects.equals(note, other.note) && 
            Objects.equals(arm, other.arm) && 
            Objects.equals(objective, other.objective);
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
                title, 
                protocol, 
                partOf, 
                status, 
                primaryPurposeType, 
                phase, 
                category, 
                focus, 
                condition, 
                contact, 
                relatedArtifact, 
                keyword, 
                location, 
                description, 
                enrollment, 
                period, 
                sponsor, 
                principalInvestigator, 
                site, 
                reasonStopped, 
                note, 
                arm, 
                objective);
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
        private String title;
        private List<Reference> protocol = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private ResearchStudyStatus status;
        private CodeableConcept primaryPurposeType;
        private CodeableConcept phase;
        private List<CodeableConcept> category = new ArrayList<>();
        private List<CodeableConcept> focus = new ArrayList<>();
        private List<CodeableConcept> condition = new ArrayList<>();
        private List<ContactDetail> contact = new ArrayList<>();
        private List<RelatedArtifact> relatedArtifact = new ArrayList<>();
        private List<CodeableConcept> keyword = new ArrayList<>();
        private List<CodeableConcept> location = new ArrayList<>();
        private Markdown description;
        private List<Reference> enrollment = new ArrayList<>();
        private Period period;
        private Reference sponsor;
        private Reference principalInvestigator;
        private List<Reference> site = new ArrayList<>();
        private CodeableConcept reasonStopped;
        private List<Annotation> note = new ArrayList<>();
        private List<Arm> arm = new ArrayList<>();
        private List<Objective> objective = new ArrayList<>();

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
         * Identifiers assigned to this research study by the sponsor or other systems.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for study
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
         * Identifiers assigned to this research study by the sponsor or other systems.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for study
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
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Name for this study
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
         * A short, descriptive user-friendly label for the study.
         * 
         * @param title
         *     Name for this study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The set of steps expected to be performed as part of the execution of the study.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link PlanDefinition}</li>
         * </ul>
         * 
         * @param protocol
         *     Steps followed in executing study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder protocol(Reference... protocol) {
            for (Reference value : protocol) {
                this.protocol.add(value);
            }
            return this;
        }

        /**
         * The set of steps expected to be performed as part of the execution of the study.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link PlanDefinition}</li>
         * </ul>
         * 
         * @param protocol
         *     Steps followed in executing study
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder protocol(Collection<Reference> protocol) {
            this.protocol = new ArrayList<>(protocol);
            return this;
        }

        /**
         * A larger research study of which this particular study is a component or step.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ResearchStudy}</li>
         * </ul>
         * 
         * @param partOf
         *     Part of larger study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * A larger research study of which this particular study is a component or step.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ResearchStudy}</li>
         * </ul>
         * 
         * @param partOf
         *     Part of larger study
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * The current state of the study.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | administratively-completed | approved | closed-to-accrual | closed-to-accrual-and-intervention | completed | 
         *     disapproved | in-review | temporarily-closed-to-accrual | temporarily-closed-to-accrual-and-intervention | withdrawn
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ResearchStudyStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The type of study based upon the intent of the study's activities. A classification of the intent of the study.
         * 
         * @param primaryPurposeType
         *     treatment | prevention | diagnostic | supportive-care | screening | health-services-research | basic-science | device-
         *     feasibility
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder primaryPurposeType(CodeableConcept primaryPurposeType) {
            this.primaryPurposeType = primaryPurposeType;
            return this;
        }

        /**
         * The stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market 
         * evaluation.
         * 
         * @param phase
         *     n-a | early-phase-1 | phase-1 | phase-1-phase-2 | phase-2 | phase-2-phase-3 | phase-3 | phase-4
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder phase(CodeableConcept phase) {
            this.phase = phase;
            return this;
        }

        /**
         * Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of 
         * randomization, safety vs. efficacy, etc.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Classifications for the study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of 
         * randomization, safety vs. efficacy, etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Classifications for the study
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to 
         * gain more information about.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param focus
         *     Drugs, devices, etc. under study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder focus(CodeableConcept... focus) {
            for (CodeableConcept value : focus) {
                this.focus.add(value);
            }
            return this;
        }

        /**
         * The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to 
         * gain more information about.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param focus
         *     Drugs, devices, etc. under study
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder focus(Collection<CodeableConcept> focus) {
            this.focus = new ArrayList<>(focus);
            return this;
        }

        /**
         * The condition that is the focus of the study. For example, In a study to examine risk factors for Lupus, might have as 
         * an inclusion criterion "healthy volunteer", but the target condition code would be a Lupus SNOMED code.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param condition
         *     Condition being studied
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder condition(CodeableConcept... condition) {
            for (CodeableConcept value : condition) {
                this.condition.add(value);
            }
            return this;
        }

        /**
         * The condition that is the focus of the study. For example, In a study to examine risk factors for Lupus, might have as 
         * an inclusion criterion "healthy volunteer", but the target condition code would be a Lupus SNOMED code.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param condition
         *     Condition being studied
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder condition(Collection<CodeableConcept> condition) {
            this.condition = new ArrayList<>(condition);
            return this;
        }

        /**
         * Contact details to assist a user in learning more about or engaging with the study.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for the study
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
         * Contact details to assist a user in learning more about or engaging with the study.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for the study
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
         * Citations, references and other related documents.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     References and dependencies
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
         * Citations, references and other related documents.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     References and dependencies
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
         * Key terms to aid in searching for or filtering the study.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param keyword
         *     Used to search for the study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder keyword(CodeableConcept... keyword) {
            for (CodeableConcept value : keyword) {
                this.keyword.add(value);
            }
            return this;
        }

        /**
         * Key terms to aid in searching for or filtering the study.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param keyword
         *     Used to search for the study
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder keyword(Collection<CodeableConcept> keyword) {
            this.keyword = new ArrayList<>(keyword);
            return this;
        }

        /**
         * Indicates a country, state or other region where the study is taking place.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param location
         *     Geographic region(s) for study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(CodeableConcept... location) {
            for (CodeableConcept value : location) {
                this.location.add(value);
            }
            return this;
        }

        /**
         * Indicates a country, state or other region where the study is taking place.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param location
         *     Geographic region(s) for study
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder location(Collection<CodeableConcept> location) {
            this.location = new ArrayList<>(location);
            return this;
        }

        /**
         * A full description of how the study is being conducted.
         * 
         * @param description
         *     What this is study doing
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * Reference to a Group that defines the criteria for and quantity of subjects participating in the study. E.g. " 200 
         * female Europeans between the ages of 20 and 45 with early onset diabetes".
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param enrollment
         *     Inclusion &amp; exclusion criteria
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder enrollment(Reference... enrollment) {
            for (Reference value : enrollment) {
                this.enrollment.add(value);
            }
            return this;
        }

        /**
         * Reference to a Group that defines the criteria for and quantity of subjects participating in the study. E.g. " 200 
         * female Europeans between the ages of 20 and 45 with early onset diabetes".
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param enrollment
         *     Inclusion &amp; exclusion criteria
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder enrollment(Collection<Reference> enrollment) {
            this.enrollment = new ArrayList<>(enrollment);
            return this;
        }

        /**
         * Identifies the start date and the expected (or actual, depending on status) end date for the study.
         * 
         * @param period
         *     When the study began and ended
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * An organization that initiates the investigation and is legally responsible for the study.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param sponsor
         *     Organization that initiates and is legally responsible for the study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sponsor(Reference sponsor) {
            this.sponsor = sponsor;
            return this;
        }

        /**
         * A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, 
         * protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, 
         * interpretation and presentation.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param principalInvestigator
         *     Researcher who oversees multiple aspects of the study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder principalInvestigator(Reference principalInvestigator) {
            this.principalInvestigator = principalInvestigator;
            return this;
        }

        /**
         * A facility in which study activities are conducted.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param site
         *     Facility where study activities are conducted
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
         * A facility in which study activities are conducted.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param site
         *     Facility where study activities are conducted
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder site(Collection<Reference> site) {
            this.site = new ArrayList<>(site);
            return this;
        }

        /**
         * A description and/or code explaining the premature termination of the study.
         * 
         * @param reasonStopped
         *     accrual-goal-met | closed-due-to-toxicity | closed-due-to-lack-of-study-progress | temporarily-closed-per-study-design
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonStopped(CodeableConcept reasonStopped) {
            this.reasonStopped = reasonStopped;
            return this;
        }

        /**
         * Comments made about the study by the performer, subject or other participants.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the study
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
         * Comments made about the study by the performer, subject or other participants.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the study
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
         * Describes an expected sequence of events for one of the participants of a study. E.g. Exposure to drug A, wash-out, 
         * exposure to drug B, wash-out, follow-up.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param arm
         *     Defined path through the study for a subject
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder arm(Arm... arm) {
            for (Arm value : arm) {
                this.arm.add(value);
            }
            return this;
        }

        /**
         * Describes an expected sequence of events for one of the participants of a study. E.g. Exposure to drug A, wash-out, 
         * exposure to drug B, wash-out, follow-up.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param arm
         *     Defined path through the study for a subject
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder arm(Collection<Arm> arm) {
            this.arm = new ArrayList<>(arm);
            return this;
        }

        /**
         * A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data 
         * collected during the study.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param objective
         *     A goal for the study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder objective(Objective... objective) {
            for (Objective value : objective) {
                this.objective.add(value);
            }
            return this;
        }

        /**
         * A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data 
         * collected during the study.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param objective
         *     A goal for the study
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder objective(Collection<Objective> objective) {
            this.objective = new ArrayList<>(objective);
            return this;
        }

        /**
         * Build the {@link ResearchStudy}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ResearchStudy}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ResearchStudy per the base specification
         */
        @Override
        public ResearchStudy build() {
            ResearchStudy researchStudy = new ResearchStudy(this);
            if (validating) {
                validate(researchStudy);
            }
            return researchStudy;
        }

        protected void validate(ResearchStudy researchStudy) {
            super.validate(researchStudy);
            ValidationSupport.checkList(researchStudy.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(researchStudy.protocol, "protocol", Reference.class);
            ValidationSupport.checkList(researchStudy.partOf, "partOf", Reference.class);
            ValidationSupport.requireNonNull(researchStudy.status, "status");
            ValidationSupport.checkList(researchStudy.category, "category", CodeableConcept.class);
            ValidationSupport.checkList(researchStudy.focus, "focus", CodeableConcept.class);
            ValidationSupport.checkList(researchStudy.condition, "condition", CodeableConcept.class);
            ValidationSupport.checkList(researchStudy.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(researchStudy.relatedArtifact, "relatedArtifact", RelatedArtifact.class);
            ValidationSupport.checkList(researchStudy.keyword, "keyword", CodeableConcept.class);
            ValidationSupport.checkList(researchStudy.location, "location", CodeableConcept.class);
            ValidationSupport.checkList(researchStudy.enrollment, "enrollment", Reference.class);
            ValidationSupport.checkList(researchStudy.site, "site", Reference.class);
            ValidationSupport.checkList(researchStudy.note, "note", Annotation.class);
            ValidationSupport.checkList(researchStudy.arm, "arm", Arm.class);
            ValidationSupport.checkList(researchStudy.objective, "objective", Objective.class);
            ValidationSupport.checkReferenceType(researchStudy.protocol, "protocol", "PlanDefinition");
            ValidationSupport.checkReferenceType(researchStudy.partOf, "partOf", "ResearchStudy");
            ValidationSupport.checkReferenceType(researchStudy.enrollment, "enrollment", "Group");
            ValidationSupport.checkReferenceType(researchStudy.sponsor, "sponsor", "Organization");
            ValidationSupport.checkReferenceType(researchStudy.principalInvestigator, "principalInvestigator", "Practitioner", "PractitionerRole");
            ValidationSupport.checkReferenceType(researchStudy.site, "site", "Location");
        }

        protected Builder from(ResearchStudy researchStudy) {
            super.from(researchStudy);
            identifier.addAll(researchStudy.identifier);
            title = researchStudy.title;
            protocol.addAll(researchStudy.protocol);
            partOf.addAll(researchStudy.partOf);
            status = researchStudy.status;
            primaryPurposeType = researchStudy.primaryPurposeType;
            phase = researchStudy.phase;
            category.addAll(researchStudy.category);
            focus.addAll(researchStudy.focus);
            condition.addAll(researchStudy.condition);
            contact.addAll(researchStudy.contact);
            relatedArtifact.addAll(researchStudy.relatedArtifact);
            keyword.addAll(researchStudy.keyword);
            location.addAll(researchStudy.location);
            description = researchStudy.description;
            enrollment.addAll(researchStudy.enrollment);
            period = researchStudy.period;
            sponsor = researchStudy.sponsor;
            principalInvestigator = researchStudy.principalInvestigator;
            site.addAll(researchStudy.site);
            reasonStopped = researchStudy.reasonStopped;
            note.addAll(researchStudy.note);
            arm.addAll(researchStudy.arm);
            objective.addAll(researchStudy.objective);
            return this;
        }
    }

    /**
     * Describes an expected sequence of events for one of the participants of a study. E.g. Exposure to drug A, wash-out, 
     * exposure to drug B, wash-out, follow-up.
     */
    public static class Arm extends BackboneElement {
        @Required
        private final String name;
        private final CodeableConcept type;
        private final String description;

        private Arm(Builder builder) {
            super(builder);
            name = builder.name;
            type = builder.type;
            description = builder.description;
        }

        /**
         * Unique, human-readable label for this arm of the study.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getName() {
            return name;
        }

        /**
         * Categorization of study arm, e.g. experimental, active comparator, placebo comparater.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * A succinct description of the path through the study that would be followed by a subject adhering to this arm.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (type != null) || 
                (description != null);
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
                    accept(name, "name", visitor);
                    accept(type, "type", visitor);
                    accept(description, "description", visitor);
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
            Arm other = (Arm) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(type, other.type) && 
                Objects.equals(description, other.description);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    type, 
                    description);
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
            private String name;
            private CodeableConcept type;
            private String description;

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
             * Convenience method for setting {@code name}.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     Label for study arm
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
             * Unique, human-readable label for this arm of the study.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     Label for study arm
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Categorization of study arm, e.g. experimental, active comparator, placebo comparater.
             * 
             * @param type
             *     Categorization of study arm
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Short explanation of study path
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
             * A succinct description of the path through the study that would be followed by a subject adhering to this arm.
             * 
             * @param description
             *     Short explanation of study path
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Build the {@link Arm}
             * 
             * <p>Required elements:
             * <ul>
             * <li>name</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Arm}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Arm per the base specification
             */
            @Override
            public Arm build() {
                Arm arm = new Arm(this);
                if (validating) {
                    validate(arm);
                }
                return arm;
            }

            protected void validate(Arm arm) {
                super.validate(arm);
                ValidationSupport.requireNonNull(arm.name, "name");
                ValidationSupport.requireValueOrChildren(arm);
            }

            protected Builder from(Arm arm) {
                super.from(arm);
                name = arm.name;
                type = arm.type;
                description = arm.description;
                return this;
            }
        }
    }

    /**
     * A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data 
     * collected during the study.
     */
    public static class Objective extends BackboneElement {
        private final String name;
        @Binding(
            bindingName = "ResearchStudyObjectiveType",
            strength = BindingStrength.Value.PREFERRED,
            description = "Codes for the kind of study objective.",
            valueSet = "http://hl7.org/fhir/ValueSet/research-study-objective-type"
        )
        private final CodeableConcept type;

        private Objective(Builder builder) {
            super(builder);
            name = builder.name;
            type = builder.type;
        }

        /**
         * Unique, human-readable label for this objective of the study.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * The kind of study objective.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (type != null);
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
                    accept(name, "name", visitor);
                    accept(type, "type", visitor);
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
            Objective other = (Objective) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(type, other.type);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    type);
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
            private String name;
            private CodeableConcept type;

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
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     Label for the objective
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
             * Unique, human-readable label for this objective of the study.
             * 
             * @param name
             *     Label for the objective
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * The kind of study objective.
             * 
             * @param type
             *     primary | secondary | exploratory
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Build the {@link Objective}
             * 
             * @return
             *     An immutable object of type {@link Objective}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Objective per the base specification
             */
            @Override
            public Objective build() {
                Objective objective = new Objective(this);
                if (validating) {
                    validate(objective);
                }
                return objective;
            }

            protected void validate(Objective objective) {
                super.validate(objective);
                ValidationSupport.requireValueOrChildren(objective);
            }

            protected Builder from(Objective objective) {
                super.from(objective);
                name = objective.name;
                type = objective.type;
                return this;
            }
        }
    }
}
