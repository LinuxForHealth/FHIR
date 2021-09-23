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
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.GoalLifecycleStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an 
 * activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "gol-1",
    level = "Rule",
    location = "Goal.target",
    description = "Goal.target.measure is required if Goal.target.detail is populated",
    expression = "(detail.exists() and measure.exists()) or detail.exists().not()",
    source = "http://hl7.org/fhir/StructureDefinition/Goal"
)
@Constraint(
    id = "goal-2",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/goal-achievement",
    expression = "achievementStatus.exists() implies (achievementStatus.memberOf('http://hl7.org/fhir/ValueSet/goal-achievement', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/Goal",
    generated = true
)
@Constraint(
    id = "goal-3",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/goal-priority",
    expression = "priority.exists() implies (priority.memberOf('http://hl7.org/fhir/ValueSet/goal-priority', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/Goal",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Goal extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "GoalLifecycleStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes that reflect the current state of a goal and whether the goal is still being targeted.",
        valueSet = "http://hl7.org/fhir/ValueSet/goal-status|4.0.1"
    )
    @Required
    private final GoalLifecycleStatus lifecycleStatus;
    @Summary
    @Binding(
        bindingName = "GoalAchievementStatus",
        strength = BindingStrength.Value.PREFERRED,
        description = "Indicates the progression, or lack thereof, towards the goal against the target.",
        valueSet = "http://hl7.org/fhir/ValueSet/goal-achievement"
    )
    private final CodeableConcept achievementStatus;
    @Summary
    @Binding(
        bindingName = "GoalCategory",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes for grouping and sorting goals.",
        valueSet = "http://hl7.org/fhir/ValueSet/goal-category"
    )
    private final List<CodeableConcept> category;
    @Summary
    @Binding(
        bindingName = "GoalPriority",
        strength = BindingStrength.Value.PREFERRED,
        description = "The level of importance associated with a goal.",
        valueSet = "http://hl7.org/fhir/ValueSet/goal-priority"
    )
    private final CodeableConcept priority;
    @Summary
    @Binding(
        bindingName = "GoalDescription",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes providing the details of a particular goal.  This will generally be system or implementation guide-specific.  In many systems, only the text element will be used.",
        valueSet = "http://hl7.org/fhir/ValueSet/clinical-findings"
    )
    @Required
    private final CodeableConcept description;
    @Summary
    @ReferenceTarget({ "Patient", "Group", "Organization" })
    @Required
    private final Reference subject;
    @Summary
    @Choice({ Date.class, CodeableConcept.class })
    @Binding(
        bindingName = "GoalStartEvent",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes describing events that can trigger the initiation of a goal.",
        valueSet = "http://hl7.org/fhir/ValueSet/goal-start-event"
    )
    private final Element start;
    private final List<Target> target;
    @Summary
    private final Date statusDate;
    private final String statusReason;
    @Summary
    @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson" })
    private final Reference expressedBy;
    @ReferenceTarget({ "Condition", "Observation", "MedicationStatement", "NutritionOrder", "ServiceRequest", "RiskAssessment" })
    private final List<Reference> addresses;
    private final List<Annotation> note;
    @Binding(
        bindingName = "GoalOutcome",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The result of the goal; e.g. \"25% increase in shoulder mobility\", \"Anxiety reduced to moderate levels\".  \"15 kg weight loss sustained over 6 months\".",
        valueSet = "http://hl7.org/fhir/ValueSet/clinical-findings"
    )
    private final List<CodeableConcept> outcomeCode;
    @ReferenceTarget({ "Observation" })
    private final List<Reference> outcomeReference;

    private Goal(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        lifecycleStatus = builder.lifecycleStatus;
        achievementStatus = builder.achievementStatus;
        category = Collections.unmodifiableList(builder.category);
        priority = builder.priority;
        description = builder.description;
        subject = builder.subject;
        start = builder.start;
        target = Collections.unmodifiableList(builder.target);
        statusDate = builder.statusDate;
        statusReason = builder.statusReason;
        expressedBy = builder.expressedBy;
        addresses = Collections.unmodifiableList(builder.addresses);
        note = Collections.unmodifiableList(builder.note);
        outcomeCode = Collections.unmodifiableList(builder.outcomeCode);
        outcomeReference = Collections.unmodifiableList(builder.outcomeReference);
    }

    /**
     * Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is 
     * updated and propagates from server to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The state of the goal throughout its lifecycle.
     * 
     * @return
     *     An immutable object of type {@link GoalLifecycleStatus} that is non-null.
     */
    public GoalLifecycleStatus getLifecycleStatus() {
        return lifecycleStatus;
    }

    /**
     * Describes the progression, or lack thereof, towards the goal against the target.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getAchievementStatus() {
        return achievementStatus;
    }

    /**
     * Indicates a category the goal falls within.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPriority() {
        return priority;
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
     * Identifies the patient, group or organization for whom the goal is being established.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The date or event after which the goal should begin being pursued.
     * 
     * @return
     *     An immutable object of type {@link Date} or {@link CodeableConcept} that may be null.
     */
    public Element getStart() {
        return start;
    }

    /**
     * Indicates what should be done by when.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Target} that may be empty.
     */
    public List<Target> getTarget() {
        return target;
    }

    /**
     * Identifies when the current status. I.e. When initially created, when achieved, when cancelled, etc.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getStatusDate() {
        return statusDate;
    }

    /**
     * Captures the reason for the current status.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getStatusReason() {
        return statusReason;
    }

    /**
     * Indicates whose goal this is - patient goal, practitioner goal, etc.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getExpressedBy() {
        return expressedBy;
    }

    /**
     * The identified conditions and other health record elements that are intended to be addressed by the goal.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAddresses() {
        return addresses;
    }

    /**
     * Any comments related to the goal.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Identifies the change (or lack of change) at the point when the status of the goal is assessed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getOutcomeCode() {
        return outcomeCode;
    }

    /**
     * Details of what's changed (or not changed).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getOutcomeReference() {
        return outcomeReference;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (lifecycleStatus != null) || 
            (achievementStatus != null) || 
            !category.isEmpty() || 
            (priority != null) || 
            (description != null) || 
            (subject != null) || 
            (start != null) || 
            !target.isEmpty() || 
            (statusDate != null) || 
            (statusReason != null) || 
            (expressedBy != null) || 
            !addresses.isEmpty() || 
            !note.isEmpty() || 
            !outcomeCode.isEmpty() || 
            !outcomeReference.isEmpty();
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
                accept(lifecycleStatus, "lifecycleStatus", visitor);
                accept(achievementStatus, "achievementStatus", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(priority, "priority", visitor);
                accept(description, "description", visitor);
                accept(subject, "subject", visitor);
                accept(start, "start", visitor);
                accept(target, "target", visitor, Target.class);
                accept(statusDate, "statusDate", visitor);
                accept(statusReason, "statusReason", visitor);
                accept(expressedBy, "expressedBy", visitor);
                accept(addresses, "addresses", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(outcomeCode, "outcomeCode", visitor, CodeableConcept.class);
                accept(outcomeReference, "outcomeReference", visitor, Reference.class);
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
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(lifecycleStatus, other.lifecycleStatus) && 
            Objects.equals(achievementStatus, other.achievementStatus) && 
            Objects.equals(category, other.category) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(description, other.description) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(start, other.start) && 
            Objects.equals(target, other.target) && 
            Objects.equals(statusDate, other.statusDate) && 
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(expressedBy, other.expressedBy) && 
            Objects.equals(addresses, other.addresses) && 
            Objects.equals(note, other.note) && 
            Objects.equals(outcomeCode, other.outcomeCode) && 
            Objects.equals(outcomeReference, other.outcomeReference);
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
                lifecycleStatus, 
                achievementStatus, 
                category, 
                priority, 
                description, 
                subject, 
                start, 
                target, 
                statusDate, 
                statusReason, 
                expressedBy, 
                addresses, 
                note, 
                outcomeCode, 
                outcomeReference);
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
        private GoalLifecycleStatus lifecycleStatus;
        private CodeableConcept achievementStatus;
        private List<CodeableConcept> category = new ArrayList<>();
        private CodeableConcept priority;
        private CodeableConcept description;
        private Reference subject;
        private Element start;
        private List<Target> target = new ArrayList<>();
        private Date statusDate;
        private String statusReason;
        private Reference expressedBy;
        private List<Reference> addresses = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<CodeableConcept> outcomeCode = new ArrayList<>();
        private List<Reference> outcomeReference = new ArrayList<>();

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
         * Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is 
         * updated and propagates from server to server.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External Ids for this goal
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
         * Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is 
         * updated and propagates from server to server.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External Ids for this goal
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
         * The state of the goal throughout its lifecycle.
         * 
         * <p>This element is required.
         * 
         * @param lifecycleStatus
         *     proposed | planned | accepted | active | on-hold | completed | cancelled | entered-in-error | rejected
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lifecycleStatus(GoalLifecycleStatus lifecycleStatus) {
            this.lifecycleStatus = lifecycleStatus;
            return this;
        }

        /**
         * Describes the progression, or lack thereof, towards the goal against the target.
         * 
         * @param achievementStatus
         *     in-progress | improving | worsening | no-change | achieved | sustaining | not-achieved | no-progress | not-attainable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder achievementStatus(CodeableConcept achievementStatus) {
            this.achievementStatus = achievementStatus;
            return this;
        }

        /**
         * Indicates a category the goal falls within.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     E.g. Treatment, dietary, behavioral, etc.
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
         * Indicates a category the goal falls within.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     E.g. Treatment, dietary, behavioral, etc.
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
         * Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.
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
         * Human-readable and/or coded description of a specific desired objective of care, such as "control blood pressure" or 
         * "negotiate an obstacle course" or "dance with child at wedding".
         * 
         * <p>This element is required.
         * 
         * @param description
         *     Code or text describing goal
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(CodeableConcept description) {
            this.description = description;
            return this;
        }

        /**
         * Identifies the patient, group or organization for whom the goal is being established.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param subject
         *     Who this goal is intended for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Convenience method for setting {@code start} with choice type Date.
         * 
         * @param start
         *     When goal pursuit begins
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #start(Element)
         */
        public Builder start(java.time.LocalDate start) {
            this.start = (start == null) ? null : Date.of(start);
            return this;
        }

        /**
         * The date or event after which the goal should begin being pursued.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Date}</li>
         * <li>{@link CodeableConcept}</li>
         * </ul>
         * 
         * @param start
         *     When goal pursuit begins
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder start(Element start) {
            this.start = start;
            return this;
        }

        /**
         * Indicates what should be done by when.
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
         * Indicates what should be done by when.
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
         * Convenience method for setting {@code statusDate}.
         * 
         * @param statusDate
         *     When goal status took effect
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #statusDate(com.ibm.fhir.model.type.Date)
         */
        public Builder statusDate(java.time.LocalDate statusDate) {
            this.statusDate = (statusDate == null) ? null : Date.of(statusDate);
            return this;
        }

        /**
         * Identifies when the current status. I.e. When initially created, when achieved, when cancelled, etc.
         * 
         * @param statusDate
         *     When goal status took effect
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusDate(Date statusDate) {
            this.statusDate = statusDate;
            return this;
        }

        /**
         * Convenience method for setting {@code statusReason}.
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #statusReason(com.ibm.fhir.model.type.String)
         */
        public Builder statusReason(java.lang.String statusReason) {
            this.statusReason = (statusReason == null) ? null : String.of(statusReason);
            return this;
        }

        /**
         * Captures the reason for the current status.
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusReason(String statusReason) {
            this.statusReason = statusReason;
            return this;
        }

        /**
         * Indicates whose goal this is - patient goal, practitioner goal, etc.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param expressedBy
         *     Who's responsible for creating Goal?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder expressedBy(Reference expressedBy) {
            this.expressedBy = expressedBy;
            return this;
        }

        /**
         * The identified conditions and other health record elements that are intended to be addressed by the goal.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link MedicationStatement}</li>
         * <li>{@link NutritionOrder}</li>
         * <li>{@link ServiceRequest}</li>
         * <li>{@link RiskAssessment}</li>
         * </ul>
         * 
         * @param addresses
         *     Issues addressed by this goal
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder addresses(Reference... addresses) {
            for (Reference value : addresses) {
                this.addresses.add(value);
            }
            return this;
        }

        /**
         * The identified conditions and other health record elements that are intended to be addressed by the goal.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link MedicationStatement}</li>
         * <li>{@link NutritionOrder}</li>
         * <li>{@link ServiceRequest}</li>
         * <li>{@link RiskAssessment}</li>
         * </ul>
         * 
         * @param addresses
         *     Issues addressed by this goal
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder addresses(Collection<Reference> addresses) {
            this.addresses = new ArrayList<>(addresses);
            return this;
        }

        /**
         * Any comments related to the goal.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments about the goal
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
         * Any comments related to the goal.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments about the goal
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
         * Identifies the change (or lack of change) at the point when the status of the goal is assessed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param outcomeCode
         *     What result was achieved regarding the goal?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder outcomeCode(CodeableConcept... outcomeCode) {
            for (CodeableConcept value : outcomeCode) {
                this.outcomeCode.add(value);
            }
            return this;
        }

        /**
         * Identifies the change (or lack of change) at the point when the status of the goal is assessed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param outcomeCode
         *     What result was achieved regarding the goal?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder outcomeCode(Collection<CodeableConcept> outcomeCode) {
            this.outcomeCode = new ArrayList<>(outcomeCode);
            return this;
        }

        /**
         * Details of what's changed (or not changed).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Observation}</li>
         * </ul>
         * 
         * @param outcomeReference
         *     Observation that resulted from goal
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder outcomeReference(Reference... outcomeReference) {
            for (Reference value : outcomeReference) {
                this.outcomeReference.add(value);
            }
            return this;
        }

        /**
         * Details of what's changed (or not changed).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Observation}</li>
         * </ul>
         * 
         * @param outcomeReference
         *     Observation that resulted from goal
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder outcomeReference(Collection<Reference> outcomeReference) {
            this.outcomeReference = new ArrayList<>(outcomeReference);
            return this;
        }

        /**
         * Build the {@link Goal}
         * 
         * <p>Required elements:
         * <ul>
         * <li>lifecycleStatus</li>
         * <li>description</li>
         * <li>subject</li>
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
            ValidationSupport.checkList(goal.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(goal.lifecycleStatus, "lifecycleStatus");
            ValidationSupport.checkList(goal.category, "category", CodeableConcept.class);
            ValidationSupport.requireNonNull(goal.description, "description");
            ValidationSupport.requireNonNull(goal.subject, "subject");
            ValidationSupport.choiceElement(goal.start, "start", Date.class, CodeableConcept.class);
            ValidationSupport.checkList(goal.target, "target", Target.class);
            ValidationSupport.checkList(goal.addresses, "addresses", Reference.class);
            ValidationSupport.checkList(goal.note, "note", Annotation.class);
            ValidationSupport.checkList(goal.outcomeCode, "outcomeCode", CodeableConcept.class);
            ValidationSupport.checkList(goal.outcomeReference, "outcomeReference", Reference.class);
            ValidationSupport.checkReferenceType(goal.subject, "subject", "Patient", "Group", "Organization");
            ValidationSupport.checkReferenceType(goal.expressedBy, "expressedBy", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson");
            ValidationSupport.checkReferenceType(goal.addresses, "addresses", "Condition", "Observation", "MedicationStatement", "NutritionOrder", "ServiceRequest", "RiskAssessment");
            ValidationSupport.checkReferenceType(goal.outcomeReference, "outcomeReference", "Observation");
        }

        protected Builder from(Goal goal) {
            super.from(goal);
            identifier.addAll(goal.identifier);
            lifecycleStatus = goal.lifecycleStatus;
            achievementStatus = goal.achievementStatus;
            category.addAll(goal.category);
            priority = goal.priority;
            description = goal.description;
            subject = goal.subject;
            start = goal.start;
            target.addAll(goal.target);
            statusDate = goal.statusDate;
            statusReason = goal.statusReason;
            expressedBy = goal.expressedBy;
            addresses.addAll(goal.addresses);
            note.addAll(goal.note);
            outcomeCode.addAll(goal.outcomeCode);
            outcomeReference.addAll(goal.outcomeReference);
            return this;
        }
    }

    /**
     * Indicates what should be done by when.
     */
    public static class Target extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "GoalTargetMeasure",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes to identify the value being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.",
            valueSet = "http://hl7.org/fhir/ValueSet/observation-codes"
        )
        private final CodeableConcept measure;
        @Summary
        @Choice({ Quantity.class, Range.class, CodeableConcept.class, String.class, Boolean.class, Integer.class, Ratio.class })
        @Binding(
            bindingName = "GoalTargetDetail",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes to identify the target value of the focus to be achieved to signify the fulfillment of the goal."
        )
        private final Element detail;
        @Summary
        @Choice({ Date.class, Duration.class })
        private final Element due;

        private Target(Builder builder) {
            super(builder);
            measure = builder.measure;
            detail = builder.detail;
            due = builder.due;
        }

        /**
         * The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getMeasure() {
            return measure;
        }

        /**
         * The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the 
         * high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is 
         * achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the 
         * goal is achieved at any focus value at or above the low value.
         * 
         * @return
         *     An immutable object of type {@link Quantity}, {@link Range}, {@link CodeableConcept}, {@link String}, {@link Boolean}, 
         *     {@link Integer} or {@link Ratio} that may be null.
         */
        public Element getDetail() {
            return detail;
        }

        /**
         * Indicates either the date or the duration after start by which the goal should be met.
         * 
         * @return
         *     An immutable object of type {@link Date} or {@link Duration} that may be null.
         */
        public Element getDue() {
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
            private Element due;

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
             * The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.
             * 
             * @param measure
             *     The parameter whose value is being tracked
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder measure(CodeableConcept measure) {
                this.measure = measure;
                return this;
            }

            /**
             * Convenience method for setting {@code detail} with choice type String.
             * 
             * @param detail
             *     The target value to be achieved
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #detail(Element)
             */
            public Builder detail(java.lang.String detail) {
                this.detail = (detail == null) ? null : String.of(detail);
                return this;
            }

            /**
             * Convenience method for setting {@code detail} with choice type Boolean.
             * 
             * @param detail
             *     The target value to be achieved
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #detail(Element)
             */
            public Builder detail(java.lang.Boolean detail) {
                this.detail = (detail == null) ? null : Boolean.of(detail);
                return this;
            }

            /**
             * Convenience method for setting {@code detail} with choice type Integer.
             * 
             * @param detail
             *     The target value to be achieved
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #detail(Element)
             */
            public Builder detail(java.lang.Integer detail) {
                this.detail = (detail == null) ? null : Integer.of(detail);
                return this;
            }

            /**
             * The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the 
             * high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is 
             * achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the 
             * goal is achieved at any focus value at or above the low value.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Quantity}</li>
             * <li>{@link Range}</li>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link String}</li>
             * <li>{@link Boolean}</li>
             * <li>{@link Integer}</li>
             * <li>{@link Ratio}</li>
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
             * Convenience method for setting {@code due} with choice type Date.
             * 
             * @param due
             *     Reach goal on or before
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #due(Element)
             */
            public Builder due(java.time.LocalDate due) {
                this.due = (due == null) ? null : Date.of(due);
                return this;
            }

            /**
             * Indicates either the date or the duration after start by which the goal should be met.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Date}</li>
             * <li>{@link Duration}</li>
             * </ul>
             * 
             * @param due
             *     Reach goal on or before
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder due(Element due) {
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
                ValidationSupport.choiceElement(target.detail, "detail", Quantity.class, Range.class, CodeableConcept.class, String.class, Boolean.class, Integer.class, Ratio.class);
                ValidationSupport.choiceElement(target.due, "due", Date.class, Duration.class);
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
