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
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.GoalLifecycleStatus;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an 
 * activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
 * </p>
 */
@Constraint(
    id = "gol-1",
    level = "Rule",
    location = "Goal.target",
    description = "Goal.target.measure is required if Goal.target.detail is populated",
    expression = "(detail.exists() and measure.exists()) or detail.exists().not()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Goal extends DomainResource {
    private final List<Identifier> identifier;
    private final GoalLifecycleStatus lifecycleStatus;
    private final CodeableConcept achievementStatus;
    private final List<CodeableConcept> category;
    private final CodeableConcept priority;
    private final CodeableConcept description;
    private final Reference subject;
    private final Element start;
    private final List<Target> target;
    private final Date statusDate;
    private final String statusReason;
    private final Reference expressedBy;
    private final List<Reference> addresses;
    private final List<Annotation> note;
    private final List<CodeableConcept> outcomeCode;
    private final List<Reference> outcomeReference;

    private volatile int hashCode;

    private Goal(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        lifecycleStatus = ValidationSupport.requireNonNull(builder.lifecycleStatus, "lifecycleStatus");
        achievementStatus = builder.achievementStatus;
        category = Collections.unmodifiableList(builder.category);
        priority = builder.priority;
        description = ValidationSupport.requireNonNull(builder.description, "description");
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        start = ValidationSupport.choiceElement(builder.start, "start", Date.class, CodeableConcept.class);
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
     * <p>
     * Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is 
     * updated and propagates from server to server.
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
     * The state of the goal throughout its lifecycle.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link GoalLifecycleStatus}.
     */
    public GoalLifecycleStatus getLifecycleStatus() {
        return lifecycleStatus;
    }

    /**
     * <p>
     * Describes the progression, or lack thereof, towards the goal against the target.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getAchievementStatus() {
        return achievementStatus;
    }

    /**
     * <p>
     * Indicates a category the goal falls within.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.
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
     * Identifies the patient, group or organization for whom the goal is being established.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * <p>
     * The date or event after which the goal should begin being pursued.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getStart() {
        return start;
    }

    /**
     * <p>
     * Indicates what should be done by when.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Target}.
     */
    public List<Target> getTarget() {
        return target;
    }

    /**
     * <p>
     * Identifies when the current status. I.e. When initially created, when achieved, when cancelled, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getStatusDate() {
        return statusDate;
    }

    /**
     * <p>
     * Captures the reason for the current status.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getStatusReason() {
        return statusReason;
    }

    /**
     * <p>
     * Indicates whose goal this is - patient goal, practitioner goal, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getExpressedBy() {
        return expressedBy;
    }

    /**
     * <p>
     * The identified conditions and other health record elements that are intended to be addressed by the goal.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAddresses() {
        return addresses;
    }

    /**
     * <p>
     * Any comments related to the goal.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * <p>
     * Identifies the change (or lack of change) at the point when the status of the goal is assessed.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getOutcomeCode() {
        return outcomeCode;
    }

    /**
     * <p>
     * Details of what's changed (or not changed).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getOutcomeReference() {
        return outcomeReference;
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
        return new Builder(lifecycleStatus, description, subject).from(this);
    }

    public Builder toBuilder(GoalLifecycleStatus lifecycleStatus, CodeableConcept description, Reference subject) {
        return new Builder(lifecycleStatus, description, subject).from(this);
    }

    public static Builder builder(GoalLifecycleStatus lifecycleStatus, CodeableConcept description, Reference subject) {
        return new Builder(lifecycleStatus, description, subject);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final GoalLifecycleStatus lifecycleStatus;
        private final CodeableConcept description;
        private final Reference subject;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept achievementStatus;
        private List<CodeableConcept> category = new ArrayList<>();
        private CodeableConcept priority;
        private Element start;
        private List<Target> target = new ArrayList<>();
        private Date statusDate;
        private String statusReason;
        private Reference expressedBy;
        private List<Reference> addresses = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<CodeableConcept> outcomeCode = new ArrayList<>();
        private List<Reference> outcomeReference = new ArrayList<>();

        private Builder(GoalLifecycleStatus lifecycleStatus, CodeableConcept description, Reference subject) {
            super();
            this.lifecycleStatus = lifecycleStatus;
            this.description = description;
            this.subject = subject;
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
         * Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is 
         * updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     External Ids for this goal
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
         * Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is 
         * updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     External Ids for this goal
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
         * Describes the progression, or lack thereof, towards the goal against the target.
         * </p>
         * 
         * @param achievementStatus
         *     in-progress | improving | worsening | no-change | achieved | sustaining | not-achieved | no-progress | not-attainable
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder achievementStatus(CodeableConcept achievementStatus) {
            this.achievementStatus = achievementStatus;
            return this;
        }

        /**
         * <p>
         * Indicates a category the goal falls within.
         * </p>
         * 
         * @param category
         *     E.g. Treatment, dietary, behavioral, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates a category the goal falls within.
         * </p>
         * 
         * @param category
         *     E.g. Treatment, dietary, behavioral, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category.addAll(category);
            return this;
        }

        /**
         * <p>
         * Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.
         * </p>
         * 
         * @param priority
         *     high-priority | medium-priority | low-priority
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder priority(CodeableConcept priority) {
            this.priority = priority;
            return this;
        }

        /**
         * <p>
         * The date or event after which the goal should begin being pursued.
         * </p>
         * 
         * @param start
         *     When goal pursuit begins
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder start(Element start) {
            this.start = start;
            return this;
        }

        /**
         * <p>
         * Indicates what should be done by when.
         * </p>
         * 
         * @param target
         *     Target outcome for the goal
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder target(Target... target) {
            for (Target value : target) {
                this.target.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates what should be done by when.
         * </p>
         * 
         * @param target
         *     Target outcome for the goal
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder target(Collection<Target> target) {
            this.target.addAll(target);
            return this;
        }

        /**
         * <p>
         * Identifies when the current status. I.e. When initially created, when achieved, when cancelled, etc.
         * </p>
         * 
         * @param statusDate
         *     When goal status took effect
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder statusDate(Date statusDate) {
            this.statusDate = statusDate;
            return this;
        }

        /**
         * <p>
         * Captures the reason for the current status.
         * </p>
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder statusReason(String statusReason) {
            this.statusReason = statusReason;
            return this;
        }

        /**
         * <p>
         * Indicates whose goal this is - patient goal, practitioner goal, etc.
         * </p>
         * 
         * @param expressedBy
         *     Who's responsible for creating Goal?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder expressedBy(Reference expressedBy) {
            this.expressedBy = expressedBy;
            return this;
        }

        /**
         * <p>
         * The identified conditions and other health record elements that are intended to be addressed by the goal.
         * </p>
         * 
         * @param addresses
         *     Issues addressed by this goal
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder addresses(Reference... addresses) {
            for (Reference value : addresses) {
                this.addresses.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The identified conditions and other health record elements that are intended to be addressed by the goal.
         * </p>
         * 
         * @param addresses
         *     Issues addressed by this goal
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder addresses(Collection<Reference> addresses) {
            this.addresses.addAll(addresses);
            return this;
        }

        /**
         * <p>
         * Any comments related to the goal.
         * </p>
         * 
         * @param note
         *     Comments about the goal
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Any comments related to the goal.
         * </p>
         * 
         * @param note
         *     Comments about the goal
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        /**
         * <p>
         * Identifies the change (or lack of change) at the point when the status of the goal is assessed.
         * </p>
         * 
         * @param outcomeCode
         *     What result was achieved regarding the goal?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder outcomeCode(CodeableConcept... outcomeCode) {
            for (CodeableConcept value : outcomeCode) {
                this.outcomeCode.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies the change (or lack of change) at the point when the status of the goal is assessed.
         * </p>
         * 
         * @param outcomeCode
         *     What result was achieved regarding the goal?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder outcomeCode(Collection<CodeableConcept> outcomeCode) {
            this.outcomeCode.addAll(outcomeCode);
            return this;
        }

        /**
         * <p>
         * Details of what's changed (or not changed).
         * </p>
         * 
         * @param outcomeReference
         *     Observation that resulted from goal
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder outcomeReference(Reference... outcomeReference) {
            for (Reference value : outcomeReference) {
                this.outcomeReference.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Details of what's changed (or not changed).
         * </p>
         * 
         * @param outcomeReference
         *     Observation that resulted from goal
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder outcomeReference(Collection<Reference> outcomeReference) {
            this.outcomeReference.addAll(outcomeReference);
            return this;
        }

        @Override
        public Goal build() {
            return new Goal(this);
        }

        private Builder from(Goal goal) {
            id = goal.id;
            meta = goal.meta;
            implicitRules = goal.implicitRules;
            language = goal.language;
            text = goal.text;
            contained.addAll(goal.contained);
            extension.addAll(goal.extension);
            modifierExtension.addAll(goal.modifierExtension);
            identifier.addAll(goal.identifier);
            achievementStatus = goal.achievementStatus;
            category.addAll(goal.category);
            priority = goal.priority;
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
     * <p>
     * Indicates what should be done by when.
     * </p>
     */
    public static class Target extends BackboneElement {
        private final CodeableConcept measure;
        private final Element detail;
        private final Element due;

        private volatile int hashCode;

        private Target(Builder builder) {
            super(builder);
            measure = builder.measure;
            detail = ValidationSupport.choiceElement(builder.detail, "detail", Quantity.class, Range.class, CodeableConcept.class, String.class, Boolean.class, Integer.class, Ratio.class);
            due = ValidationSupport.choiceElement(builder.due, "due", Date.class, Duration.class);
        }

        /**
         * <p>
         * The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.
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
         * The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the 
         * high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is 
         * achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the 
         * goal is achieved at any focus value at or above the low value.
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
         * Indicates either the date or the duration after start by which the goal should be met.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getDue() {
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
            private Element due;

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
             *     A reference to this Builder instance.
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.
             * </p>
             * 
             * @param measure
             *     The parameter whose value is being tracked
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder measure(CodeableConcept measure) {
                this.measure = measure;
                return this;
            }

            /**
             * <p>
             * The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the 
             * high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is 
             * achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the 
             * goal is achieved at any focus value at or above the low value.
             * </p>
             * 
             * @param detail
             *     The target value to be achieved
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Element detail) {
                this.detail = detail;
                return this;
            }

            /**
             * <p>
             * Indicates either the date or the duration after start by which the goal should be met.
             * </p>
             * 
             * @param due
             *     Reach goal on or before
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder due(Element due) {
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
