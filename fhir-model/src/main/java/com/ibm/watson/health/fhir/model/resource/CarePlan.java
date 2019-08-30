/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watson.health.fhir.model.annotation.Choice;
import com.ibm.watson.health.fhir.model.annotation.Constraint;
import com.ibm.watson.health.fhir.model.annotation.Required;
import com.ibm.watson.health.fhir.model.type.Annotation;
import com.ibm.watson.health.fhir.model.type.BackboneElement;
import com.ibm.watson.health.fhir.model.type.Boolean;
import com.ibm.watson.health.fhir.model.type.Canonical;
import com.ibm.watson.health.fhir.model.type.CarePlanActivityKind;
import com.ibm.watson.health.fhir.model.type.CarePlanActivityStatus;
import com.ibm.watson.health.fhir.model.type.CarePlanIntent;
import com.ibm.watson.health.fhir.model.type.CarePlanStatus;
import com.ibm.watson.health.fhir.model.type.Code;
import com.ibm.watson.health.fhir.model.type.CodeableConcept;
import com.ibm.watson.health.fhir.model.type.DateTime;
import com.ibm.watson.health.fhir.model.type.Element;
import com.ibm.watson.health.fhir.model.type.Extension;
import com.ibm.watson.health.fhir.model.type.Id;
import com.ibm.watson.health.fhir.model.type.Identifier;
import com.ibm.watson.health.fhir.model.type.Meta;
import com.ibm.watson.health.fhir.model.type.Narrative;
import com.ibm.watson.health.fhir.model.type.Period;
import com.ibm.watson.health.fhir.model.type.Reference;
import com.ibm.watson.health.fhir.model.type.SimpleQuantity;
import com.ibm.watson.health.fhir.model.type.String;
import com.ibm.watson.health.fhir.model.type.Timing;
import com.ibm.watson.health.fhir.model.type.Uri;
import com.ibm.watson.health.fhir.model.util.ValidationSupport;
import com.ibm.watson.health.fhir.model.visitor.Visitor;

/**
 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or 
 * community for a period of time, possibly limited to care for a specific condition or set of conditions.
 */
@Constraint(
    id = "cpl-3",
    level = "Rule",
    location = "CarePlan.activity",
    description = "Provide a reference or detail, not both",
    expression = "detail.empty() or reference.empty()"
)
@Generated("com.ibm.watson.health.fhir.tools.CodeGenerator")
public class CarePlan extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Canonical> instantiatesCanonical;
    private final List<Uri> instantiatesUri;
    private final List<Reference> basedOn;
    private final List<Reference> replaces;
    private final List<Reference> partOf;
    @Required
    private final CarePlanStatus status;
    @Required
    private final CarePlanIntent intent;
    private final List<CodeableConcept> category;
    private final String title;
    private final String description;
    @Required
    private final Reference subject;
    private final Reference encounter;
    private final Period period;
    private final DateTime created;
    private final Reference author;
    private final List<Reference> contributor;
    private final List<Reference> careTeam;
    private final List<Reference> addresses;
    private final List<Reference> supportingInfo;
    private final List<Reference> goal;
    private final List<Activity> activity;
    private final List<Annotation> note;

    private volatile int hashCode;

    private CarePlan(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        instantiatesCanonical = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.instantiatesCanonical, "instantiatesCanonical"));
        instantiatesUri = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.instantiatesUri, "instantiatesUri"));
        basedOn = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.basedOn, "basedOn"));
        replaces = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.replaces, "replaces"));
        partOf = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.partOf, "partOf"));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        intent = ValidationSupport.requireNonNull(builder.intent, "intent");
        category = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.category, "category"));
        title = builder.title;
        description = builder.description;
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        encounter = builder.encounter;
        period = builder.period;
        created = builder.created;
        author = builder.author;
        contributor = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.contributor, "contributor"));
        careTeam = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.careTeam, "careTeam"));
        addresses = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.addresses, "addresses"));
        supportingInfo = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.supportingInfo, "supportingInfo"));
        goal = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.goal, "goal"));
        activity = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.activity, "activity"));
        note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
        ValidationSupport.requireChildren(this);
    }

    /**
     * Business identifiers assigned to this care plan by the performer or other systems which remain constant as the 
     * resource is updated and propagates from server to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
     * or in part by this CarePlan.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
     * in whole or in part by this CarePlan.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * A care plan that is fulfilled in whole or in part by this care plan.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * Completed or terminated care plan whose function is taken by this new care plan.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReplaces() {
        return replaces;
    }

    /**
     * A larger care plan of which this particular care plan is a component or step.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.
     * 
     * @return
     *     An immutable object of type {@link CarePlanStatus}.
     */
    public CarePlanStatus getStatus() {
        return status;
    }

    /**
     * Indicates the level of authority/intentionality associated with the care plan and where the care plan fits into the 
     * workflow chain.
     * 
     * @return
     *     An immutable object of type {@link CarePlanIntent}.
     */
    public CarePlanIntent getIntent() {
        return intent;
    }

    /**
     * Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans; e.g. "Home 
     * health", "psychiatric", "asthma", "disease management", "wellness plan", etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * Human-friendly name for the care plan.
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getTitle() {
        return title;
    }

    /**
     * A description of the scope and nature of the plan.
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Identifies the patient or group whose intended care is described by the plan.
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The Encounter during which this CarePlan was created or to which the creation of this record is tightly associated.
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * Indicates when the plan did (or is intended to) come into effect and end.
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Represents when this particular CarePlan record was created in the system, which is often a system-generated date.
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * When populated, the author is responsible for the care plan. The care plan is attributed to the author.
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * Identifies the individual(s) or organization who provided the contents of the care plan.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getContributor() {
        return contributor;
    }

    /**
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getCareTeam() {
        return careTeam;
    }

    /**
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAddresses() {
        return addresses;
    }

    /**
     * Identifies portions of the patient's record that specifically influenced the formation of the plan. These might 
     * include comorbidities, recent procedures, limitations, recent assessments, etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * Describes the intended objective(s) of carrying out the care plan.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getGoal() {
        return goal;
    }

    /**
     * Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, 
     * self-monitoring, education, etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Activity}.
     */
    public List<Activity> getActivity() {
        return activity;
    }

    /**
     * General notes about the care plan not covered elsewhere.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !instantiatesCanonical.isEmpty() || 
            !instantiatesUri.isEmpty() || 
            !basedOn.isEmpty() || 
            !replaces.isEmpty() || 
            !partOf.isEmpty() || 
            (status != null) || 
            (intent != null) || 
            !category.isEmpty() || 
            (title != null) || 
            (description != null) || 
            (subject != null) || 
            (encounter != null) || 
            (period != null) || 
            (created != null) || 
            (author != null) || 
            !contributor.isEmpty() || 
            !careTeam.isEmpty() || 
            !addresses.isEmpty() || 
            !supportingInfo.isEmpty() || 
            !goal.isEmpty() || 
            !activity.isEmpty() || 
            !note.isEmpty();
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
                accept(instantiatesCanonical, "instantiatesCanonical", visitor, Canonical.class);
                accept(instantiatesUri, "instantiatesUri", visitor, Uri.class);
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(replaces, "replaces", visitor, Reference.class);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(intent, "intent", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(title, "title", visitor);
                accept(description, "description", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(period, "period", visitor);
                accept(created, "created", visitor);
                accept(author, "author", visitor);
                accept(contributor, "contributor", visitor, Reference.class);
                accept(careTeam, "careTeam", visitor, Reference.class);
                accept(addresses, "addresses", visitor, Reference.class);
                accept(supportingInfo, "supportingInfo", visitor, Reference.class);
                accept(goal, "goal", visitor, Reference.class);
                accept(activity, "activity", visitor, Activity.class);
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
        CarePlan other = (CarePlan) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(instantiatesCanonical, other.instantiatesCanonical) && 
            Objects.equals(instantiatesUri, other.instantiatesUri) && 
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(replaces, other.replaces) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(status, other.status) && 
            Objects.equals(intent, other.intent) && 
            Objects.equals(category, other.category) && 
            Objects.equals(title, other.title) && 
            Objects.equals(description, other.description) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(period, other.period) && 
            Objects.equals(created, other.created) && 
            Objects.equals(author, other.author) && 
            Objects.equals(contributor, other.contributor) && 
            Objects.equals(careTeam, other.careTeam) && 
            Objects.equals(addresses, other.addresses) && 
            Objects.equals(supportingInfo, other.supportingInfo) && 
            Objects.equals(goal, other.goal) && 
            Objects.equals(activity, other.activity) && 
            Objects.equals(note, other.note);
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
                instantiatesCanonical, 
                instantiatesUri, 
                basedOn, 
                replaces, 
                partOf, 
                status, 
                intent, 
                category, 
                title, 
                description, 
                subject, 
                encounter, 
                period, 
                created, 
                author, 
                contributor, 
                careTeam, 
                addresses, 
                supportingInfo, 
                goal, 
                activity, 
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

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private List<Canonical> instantiatesCanonical = new ArrayList<>();
        private List<Uri> instantiatesUri = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> replaces = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private CarePlanStatus status;
        private CarePlanIntent intent;
        private List<CodeableConcept> category = new ArrayList<>();
        private String title;
        private String description;
        private Reference subject;
        private Reference encounter;
        private Period period;
        private DateTime created;
        private Reference author;
        private List<Reference> contributor = new ArrayList<>();
        private List<Reference> careTeam = new ArrayList<>();
        private List<Reference> addresses = new ArrayList<>();
        private List<Reference> supportingInfo = new ArrayList<>();
        private List<Reference> goal = new ArrayList<>();
        private List<Activity> activity = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();

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
        public Builder id(Id id) {
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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Business identifiers assigned to this care plan by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param identifier
         *     External Ids for this plan
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
         * Business identifiers assigned to this care plan by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param identifier
         *     External Ids for this plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
         * or in part by this CarePlan.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesCanonical(Canonical... instantiatesCanonical) {
            for (Canonical value : instantiatesCanonical) {
                this.instantiatesCanonical.add(value);
            }
            return this;
        }

        /**
         * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
         * or in part by this CarePlan.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical = new ArrayList<>(instantiatesCanonical);
            return this;
        }

        /**
         * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
         * in whole or in part by this CarePlan.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesUri(Uri... instantiatesUri) {
            for (Uri value : instantiatesUri) {
                this.instantiatesUri.add(value);
            }
            return this;
        }

        /**
         * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
         * in whole or in part by this CarePlan.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri = new ArrayList<>(instantiatesUri);
            return this;
        }

        /**
         * A care plan that is fulfilled in whole or in part by this care plan.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param basedOn
         *     Fulfills CarePlan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * A care plan that is fulfilled in whole or in part by this care plan.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param basedOn
         *     Fulfills CarePlan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * Completed or terminated care plan whose function is taken by this new care plan.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param replaces
         *     CarePlan replaced by this CarePlan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder replaces(Reference... replaces) {
            for (Reference value : replaces) {
                this.replaces.add(value);
            }
            return this;
        }

        /**
         * Completed or terminated care plan whose function is taken by this new care plan.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param replaces
         *     CarePlan replaced by this CarePlan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder replaces(Collection<Reference> replaces) {
            this.replaces = new ArrayList<>(replaces);
            return this;
        }

        /**
         * A larger care plan of which this particular care plan is a component or step.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param partOf
         *     Part of referenced CarePlan
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
         * A larger care plan of which this particular care plan is a component or step.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param partOf
         *     Part of referenced CarePlan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     draft | active | suspended | completed | entered-in-error | cancelled | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CarePlanStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Indicates the level of authority/intentionality associated with the care plan and where the care plan fits into the 
         * workflow chain.
         * 
         * <p>This element is required.
         * 
         * @param intent
         *     proposal | plan | order | option
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder intent(CarePlanIntent intent) {
            this.intent = intent;
            return this;
        }

        /**
         * Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans; e.g. "Home 
         * health", "psychiatric", "asthma", "disease management", "wellness plan", etc.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param category
         *     Type of plan
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
         * Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans; e.g. "Home 
         * health", "psychiatric", "asthma", "disease management", "wellness plan", etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param category
         *     Type of plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * Human-friendly name for the care plan.
         * 
         * @param title
         *     Human-friendly name for the care plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * A description of the scope and nature of the plan.
         * 
         * @param description
         *     Summary of nature of plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Identifies the patient or group whose intended care is described by the plan.
         * 
         * <p>This element is required.
         * 
         * @param subject
         *     Who the care plan is for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The Encounter during which this CarePlan was created or to which the creation of this record is tightly associated.
         * 
         * @param encounter
         *     Encounter created as part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * Indicates when the plan did (or is intended to) come into effect and end.
         * 
         * @param period
         *     Time period plan covers
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * Represents when this particular CarePlan record was created in the system, which is often a system-generated date.
         * 
         * @param created
         *     Date record was first recorded
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        /**
         * When populated, the author is responsible for the care plan. The care plan is attributed to the author.
         * 
         * @param author
         *     Who is the designated responsible party
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Reference author) {
            this.author = author;
            return this;
        }

        /**
         * Identifies the individual(s) or organization who provided the contents of the care plan.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param contributor
         *     Who provided the content of the care plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contributor(Reference... contributor) {
            for (Reference value : contributor) {
                this.contributor.add(value);
            }
            return this;
        }

        /**
         * Identifies the individual(s) or organization who provided the contents of the care plan.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param contributor
         *     Who provided the content of the care plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contributor(Collection<Reference> contributor) {
            this.contributor = new ArrayList<>(contributor);
            return this;
        }

        /**
         * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param careTeam
         *     Who's involved in plan?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder careTeam(Reference... careTeam) {
            for (Reference value : careTeam) {
                this.careTeam.add(value);
            }
            return this;
        }

        /**
         * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param careTeam
         *     Who's involved in plan?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder careTeam(Collection<Reference> careTeam) {
            this.careTeam = new ArrayList<>(careTeam);
            return this;
        }

        /**
         * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param addresses
         *     Health issues this plan addresses
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
         * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param addresses
         *     Health issues this plan addresses
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder addresses(Collection<Reference> addresses) {
            this.addresses = new ArrayList<>(addresses);
            return this;
        }

        /**
         * Identifies portions of the patient's record that specifically influenced the formation of the plan. These might 
         * include comorbidities, recent procedures, limitations, recent assessments, etc.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param supportingInfo
         *     Information considered as part of plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInfo(Reference... supportingInfo) {
            for (Reference value : supportingInfo) {
                this.supportingInfo.add(value);
            }
            return this;
        }

        /**
         * Identifies portions of the patient's record that specifically influenced the formation of the plan. These might 
         * include comorbidities, recent procedures, limitations, recent assessments, etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param supportingInfo
         *     Information considered as part of plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInfo(Collection<Reference> supportingInfo) {
            this.supportingInfo = new ArrayList<>(supportingInfo);
            return this;
        }

        /**
         * Describes the intended objective(s) of carrying out the care plan.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param goal
         *     Desired outcome of plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder goal(Reference... goal) {
            for (Reference value : goal) {
                this.goal.add(value);
            }
            return this;
        }

        /**
         * Describes the intended objective(s) of carrying out the care plan.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param goal
         *     Desired outcome of plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder goal(Collection<Reference> goal) {
            this.goal = new ArrayList<>(goal);
            return this;
        }

        /**
         * Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, 
         * self-monitoring, education, etc.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param activity
         *     Action to occur as part of plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder activity(Activity... activity) {
            for (Activity value : activity) {
                this.activity.add(value);
            }
            return this;
        }

        /**
         * Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, 
         * self-monitoring, education, etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param activity
         *     Action to occur as part of plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder activity(Collection<Activity> activity) {
            this.activity = new ArrayList<>(activity);
            return this;
        }

        /**
         * General notes about the care plan not covered elsewhere.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param note
         *     Comments about the plan
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
         * General notes about the care plan not covered elsewhere.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param note
         *     Comments about the plan
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * Build the {@link CarePlan}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>intent</li>
         * <li>subject</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link CarePlan}
         */
        @Override
        public CarePlan build() {
            return new CarePlan(this);
        }

        protected Builder from(CarePlan carePlan) {
            super.from(carePlan);
            identifier.addAll(carePlan.identifier);
            instantiatesCanonical.addAll(carePlan.instantiatesCanonical);
            instantiatesUri.addAll(carePlan.instantiatesUri);
            basedOn.addAll(carePlan.basedOn);
            replaces.addAll(carePlan.replaces);
            partOf.addAll(carePlan.partOf);
            status = carePlan.status;
            intent = carePlan.intent;
            category.addAll(carePlan.category);
            title = carePlan.title;
            description = carePlan.description;
            subject = carePlan.subject;
            encounter = carePlan.encounter;
            period = carePlan.period;
            created = carePlan.created;
            author = carePlan.author;
            contributor.addAll(carePlan.contributor);
            careTeam.addAll(carePlan.careTeam);
            addresses.addAll(carePlan.addresses);
            supportingInfo.addAll(carePlan.supportingInfo);
            goal.addAll(carePlan.goal);
            activity.addAll(carePlan.activity);
            note.addAll(carePlan.note);
            return this;
        }
    }

    /**
     * Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, 
     * self-monitoring, education, etc.
     */
    public static class Activity extends BackboneElement {
        private final List<CodeableConcept> outcomeCodeableConcept;
        private final List<Reference> outcomeReference;
        private final List<Annotation> progress;
        private final Reference reference;
        private final Detail detail;

        private volatile int hashCode;

        private Activity(Builder builder) {
            super(builder);
            outcomeCodeableConcept = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.outcomeCodeableConcept, "outcomeCodeableConcept"));
            outcomeReference = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.outcomeReference, "outcomeReference"));
            progress = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.progress, "progress"));
            reference = builder.reference;
            detail = builder.detail;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Identifies the outcome at the point when the status of the activity is assessed. For example, the outcome of an 
         * education activity could be patient understands (or not).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getOutcomeCodeableConcept() {
            return outcomeCodeableConcept;
        }

        /**
         * Details of the outcome or action resulting from the activity. The reference to an "event" resource, such as Procedure 
         * or Encounter or Observation, is the result/outcome of the activity itself. The activity can be conveyed using CarePlan.
         * activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getOutcomeReference() {
            return outcomeReference;
        }

        /**
         * Notes about the adherence/status/progress of the activity.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Annotation}.
         */
        public List<Annotation> getProgress() {
            return progress;
        }

        /**
         * The details of the proposed activity represented in a specific resource.
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getReference() {
            return reference;
        }

        /**
         * A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know 
         * about specific resources such as procedure etc.
         * 
         * @return
         *     An immutable object of type {@link Detail}.
         */
        public Detail getDetail() {
            return detail;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !outcomeCodeableConcept.isEmpty() || 
                !outcomeReference.isEmpty() || 
                !progress.isEmpty() || 
                (reference != null) || 
                (detail != null);
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
                    accept(outcomeCodeableConcept, "outcomeCodeableConcept", visitor, CodeableConcept.class);
                    accept(outcomeReference, "outcomeReference", visitor, Reference.class);
                    accept(progress, "progress", visitor, Annotation.class);
                    accept(reference, "reference", visitor);
                    accept(detail, "detail", visitor);
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
            Activity other = (Activity) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(outcomeCodeableConcept, other.outcomeCodeableConcept) && 
                Objects.equals(outcomeReference, other.outcomeReference) && 
                Objects.equals(progress, other.progress) && 
                Objects.equals(reference, other.reference) && 
                Objects.equals(detail, other.detail);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    outcomeCodeableConcept, 
                    outcomeReference, 
                    progress, 
                    reference, 
                    detail);
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
            private List<CodeableConcept> outcomeCodeableConcept = new ArrayList<>();
            private List<Reference> outcomeReference = new ArrayList<>();
            private List<Annotation> progress = new ArrayList<>();
            private Reference reference;
            private Detail detail;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * Identifies the outcome at the point when the status of the activity is assessed. For example, the outcome of an 
             * education activity could be patient understands (or not).
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param outcomeCodeableConcept
             *     Results of the activity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder outcomeCodeableConcept(CodeableConcept... outcomeCodeableConcept) {
                for (CodeableConcept value : outcomeCodeableConcept) {
                    this.outcomeCodeableConcept.add(value);
                }
                return this;
            }

            /**
             * Identifies the outcome at the point when the status of the activity is assessed. For example, the outcome of an 
             * education activity could be patient understands (or not).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param outcomeCodeableConcept
             *     Results of the activity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder outcomeCodeableConcept(Collection<CodeableConcept> outcomeCodeableConcept) {
                this.outcomeCodeableConcept = new ArrayList<>(outcomeCodeableConcept);
                return this;
            }

            /**
             * Details of the outcome or action resulting from the activity. The reference to an "event" resource, such as Procedure 
             * or Encounter or Observation, is the result/outcome of the activity itself. The activity can be conveyed using CarePlan.
             * activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource).
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param outcomeReference
             *     Appointment, Encounter, Procedure, etc.
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
             * Details of the outcome or action resulting from the activity. The reference to an "event" resource, such as Procedure 
             * or Encounter or Observation, is the result/outcome of the activity itself. The activity can be conveyed using CarePlan.
             * activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param outcomeReference
             *     Appointment, Encounter, Procedure, etc.
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder outcomeReference(Collection<Reference> outcomeReference) {
                this.outcomeReference = new ArrayList<>(outcomeReference);
                return this;
            }

            /**
             * Notes about the adherence/status/progress of the activity.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param progress
             *     Comments about the activity status/progress
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder progress(Annotation... progress) {
                for (Annotation value : progress) {
                    this.progress.add(value);
                }
                return this;
            }

            /**
             * Notes about the adherence/status/progress of the activity.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param progress
             *     Comments about the activity status/progress
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder progress(Collection<Annotation> progress) {
                this.progress = new ArrayList<>(progress);
                return this;
            }

            /**
             * The details of the proposed activity represented in a specific resource.
             * 
             * @param reference
             *     Activity details defined in specific resource
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reference(Reference reference) {
                this.reference = reference;
                return this;
            }

            /**
             * A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know 
             * about specific resources such as procedure etc.
             * 
             * @param detail
             *     In-line definition of activity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Detail detail) {
                this.detail = detail;
                return this;
            }

            /**
             * Build the {@link Activity}
             * 
             * @return
             *     An immutable object of type {@link Activity}
             */
            @Override
            public Activity build() {
                return new Activity(this);
            }

            protected Builder from(Activity activity) {
                super.from(activity);
                outcomeCodeableConcept.addAll(activity.outcomeCodeableConcept);
                outcomeReference.addAll(activity.outcomeReference);
                progress.addAll(activity.progress);
                reference = activity.reference;
                detail = activity.detail;
                return this;
            }
        }

        /**
         * A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know 
         * about specific resources such as procedure etc.
         */
        public static class Detail extends BackboneElement {
            private final CarePlanActivityKind kind;
            private final List<Canonical> instantiatesCanonical;
            private final List<Uri> instantiatesUri;
            private final CodeableConcept code;
            private final List<CodeableConcept> reasonCode;
            private final List<Reference> reasonReference;
            private final List<Reference> goal;
            @Required
            private final CarePlanActivityStatus status;
            private final CodeableConcept statusReason;
            private final Boolean doNotPerform;
            @Choice({ Timing.class, Period.class, String.class })
            private final Element scheduled;
            private final Reference location;
            private final List<Reference> performer;
            @Choice({ CodeableConcept.class, Reference.class })
            private final Element product;
            private final SimpleQuantity dailyAmount;
            private final SimpleQuantity quantity;
            private final String description;

            private volatile int hashCode;

            private Detail(Builder builder) {
                super(builder);
                kind = builder.kind;
                instantiatesCanonical = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.instantiatesCanonical, "instantiatesCanonical"));
                instantiatesUri = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.instantiatesUri, "instantiatesUri"));
                code = builder.code;
                reasonCode = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonCode, "reasonCode"));
                reasonReference = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonReference, "reasonReference"));
                goal = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.goal, "goal"));
                status = ValidationSupport.requireNonNull(builder.status, "status");
                statusReason = builder.statusReason;
                doNotPerform = builder.doNotPerform;
                scheduled = ValidationSupport.choiceElement(builder.scheduled, "scheduled", Timing.class, Period.class, String.class);
                location = builder.location;
                performer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.performer, "performer"));
                product = ValidationSupport.choiceElement(builder.product, "product", CodeableConcept.class, Reference.class);
                dailyAmount = builder.dailyAmount;
                quantity = builder.quantity;
                description = builder.description;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * A description of the kind of resource the in-line definition of a care plan activity is representing. The CarePlan.
             * activity.detail is an in-line definition when a resource is not referenced using CarePlan.activity.reference. For 
             * example, a MedicationRequest, a ServiceRequest, or a CommunicationRequest.
             * 
             * @return
             *     An immutable object of type {@link CarePlanActivityKind}.
             */
            public CarePlanActivityKind getKind() {
                return kind;
            }

            /**
             * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
             * or in part by this CarePlan activity.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Canonical}.
             */
            public List<Canonical> getInstantiatesCanonical() {
                return instantiatesCanonical;
            }

            /**
             * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
             * in whole or in part by this CarePlan activity.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Uri}.
             */
            public List<Uri> getInstantiatesUri() {
                return instantiatesUri;
            }

            /**
             * Detailed description of the type of planned activity; e.g. what lab test, what procedure, what kind of encounter.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * Provides the rationale that drove the inclusion of this particular activity as part of the plan or the reason why the 
             * activity was prohibited.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getReasonCode() {
                return reasonCode;
            }

            /**
             * Indicates another resource, such as the health condition(s), whose existence justifies this request and drove the 
             * inclusion of this particular activity as part of the plan.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Reference}.
             */
            public List<Reference> getReasonReference() {
                return reasonReference;
            }

            /**
             * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Reference}.
             */
            public List<Reference> getGoal() {
                return goal;
            }

            /**
             * Identifies what progress is being made for the specific activity.
             * 
             * @return
             *     An immutable object of type {@link CarePlanActivityStatus}.
             */
            public CarePlanActivityStatus getStatus() {
                return status;
            }

            /**
             * Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getStatusReason() {
                return statusReason;
            }

            /**
             * If true, indicates that the described activity is one that must NOT be engaged in when following the plan. If false, 
             * or missing, indicates that the described activity is one that should be engaged in when following the plan.
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getDoNotPerform() {
                return doNotPerform;
            }

            /**
             * The period, timing or frequency upon which the described activity is to occur.
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getScheduled() {
                return scheduled;
            }

            /**
             * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
             * 
             * @return
             *     An immutable object of type {@link Reference}.
             */
            public Reference getLocation() {
                return location;
            }

            /**
             * Identifies who's expected to be involved in the activity.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Reference}.
             */
            public List<Reference> getPerformer() {
                return performer;
            }

            /**
             * Identifies the food, drug or other product to be consumed or supplied in the activity.
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getProduct() {
                return product;
            }

            /**
             * Identifies the quantity expected to be consumed in a given day.
             * 
             * @return
             *     An immutable object of type {@link SimpleQuantity}.
             */
            public SimpleQuantity getDailyAmount() {
                return dailyAmount;
            }

            /**
             * Identifies the quantity expected to be supplied, administered or consumed by the subject.
             * 
             * @return
             *     An immutable object of type {@link SimpleQuantity}.
             */
            public SimpleQuantity getQuantity() {
                return quantity;
            }

            /**
             * This provides a textual description of constraints on the intended activity occurrence, including relation to other 
             * activities. It may also include objectives, pre-conditions and end-conditions. Finally, it may convey specifics about 
             * the activity such as body site, method, route, etc.
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getDescription() {
                return description;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (kind != null) || 
                    !instantiatesCanonical.isEmpty() || 
                    !instantiatesUri.isEmpty() || 
                    (code != null) || 
                    !reasonCode.isEmpty() || 
                    !reasonReference.isEmpty() || 
                    !goal.isEmpty() || 
                    (status != null) || 
                    (statusReason != null) || 
                    (doNotPerform != null) || 
                    (scheduled != null) || 
                    (location != null) || 
                    !performer.isEmpty() || 
                    (product != null) || 
                    (dailyAmount != null) || 
                    (quantity != null) || 
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
                        accept(kind, "kind", visitor);
                        accept(instantiatesCanonical, "instantiatesCanonical", visitor, Canonical.class);
                        accept(instantiatesUri, "instantiatesUri", visitor, Uri.class);
                        accept(code, "code", visitor);
                        accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                        accept(reasonReference, "reasonReference", visitor, Reference.class);
                        accept(goal, "goal", visitor, Reference.class);
                        accept(status, "status", visitor);
                        accept(statusReason, "statusReason", visitor);
                        accept(doNotPerform, "doNotPerform", visitor);
                        accept(scheduled, "scheduled", visitor);
                        accept(location, "location", visitor);
                        accept(performer, "performer", visitor, Reference.class);
                        accept(product, "product", visitor);
                        accept(dailyAmount, "dailyAmount", visitor);
                        accept(quantity, "quantity", visitor);
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
                Detail other = (Detail) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(kind, other.kind) && 
                    Objects.equals(instantiatesCanonical, other.instantiatesCanonical) && 
                    Objects.equals(instantiatesUri, other.instantiatesUri) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(reasonCode, other.reasonCode) && 
                    Objects.equals(reasonReference, other.reasonReference) && 
                    Objects.equals(goal, other.goal) && 
                    Objects.equals(status, other.status) && 
                    Objects.equals(statusReason, other.statusReason) && 
                    Objects.equals(doNotPerform, other.doNotPerform) && 
                    Objects.equals(scheduled, other.scheduled) && 
                    Objects.equals(location, other.location) && 
                    Objects.equals(performer, other.performer) && 
                    Objects.equals(product, other.product) && 
                    Objects.equals(dailyAmount, other.dailyAmount) && 
                    Objects.equals(quantity, other.quantity) && 
                    Objects.equals(description, other.description);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        kind, 
                        instantiatesCanonical, 
                        instantiatesUri, 
                        code, 
                        reasonCode, 
                        reasonReference, 
                        goal, 
                        status, 
                        statusReason, 
                        doNotPerform, 
                        scheduled, 
                        location, 
                        performer, 
                        product, 
                        dailyAmount, 
                        quantity, 
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
                private CarePlanActivityKind kind;
                private List<Canonical> instantiatesCanonical = new ArrayList<>();
                private List<Uri> instantiatesUri = new ArrayList<>();
                private CodeableConcept code;
                private List<CodeableConcept> reasonCode = new ArrayList<>();
                private List<Reference> reasonReference = new ArrayList<>();
                private List<Reference> goal = new ArrayList<>();
                private CarePlanActivityStatus status;
                private CodeableConcept statusReason;
                private Boolean doNotPerform;
                private Element scheduled;
                private Reference location;
                private List<Reference> performer = new ArrayList<>();
                private Element product;
                private SimpleQuantity dailyAmount;
                private SimpleQuantity quantity;
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * A description of the kind of resource the in-line definition of a care plan activity is representing. The CarePlan.
                 * activity.detail is an in-line definition when a resource is not referenced using CarePlan.activity.reference. For 
                 * example, a MedicationRequest, a ServiceRequest, or a CommunicationRequest.
                 * 
                 * @param kind
                 *     Kind of resource
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder kind(CarePlanActivityKind kind) {
                    this.kind = kind;
                    return this;
                }

                /**
                 * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
                 * or in part by this CarePlan activity.
                 * 
                 * <p>Adds new element(s) to the existing list
                 * 
                 * @param instantiatesCanonical
                 *     Instantiates FHIR protocol or definition
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder instantiatesCanonical(Canonical... instantiatesCanonical) {
                    for (Canonical value : instantiatesCanonical) {
                        this.instantiatesCanonical.add(value);
                    }
                    return this;
                }

                /**
                 * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
                 * or in part by this CarePlan activity.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param instantiatesCanonical
                 *     Instantiates FHIR protocol or definition
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
                    this.instantiatesCanonical = new ArrayList<>(instantiatesCanonical);
                    return this;
                }

                /**
                 * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
                 * in whole or in part by this CarePlan activity.
                 * 
                 * <p>Adds new element(s) to the existing list
                 * 
                 * @param instantiatesUri
                 *     Instantiates external protocol or definition
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder instantiatesUri(Uri... instantiatesUri) {
                    for (Uri value : instantiatesUri) {
                        this.instantiatesUri.add(value);
                    }
                    return this;
                }

                /**
                 * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
                 * in whole or in part by this CarePlan activity.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param instantiatesUri
                 *     Instantiates external protocol or definition
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
                    this.instantiatesUri = new ArrayList<>(instantiatesUri);
                    return this;
                }

                /**
                 * Detailed description of the type of planned activity; e.g. what lab test, what procedure, what kind of encounter.
                 * 
                 * @param code
                 *     Detail type of activity
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(CodeableConcept code) {
                    this.code = code;
                    return this;
                }

                /**
                 * Provides the rationale that drove the inclusion of this particular activity as part of the plan or the reason why the 
                 * activity was prohibited.
                 * 
                 * <p>Adds new element(s) to the existing list
                 * 
                 * @param reasonCode
                 *     Why activity should be done or why activity was prohibited
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reasonCode(CodeableConcept... reasonCode) {
                    for (CodeableConcept value : reasonCode) {
                        this.reasonCode.add(value);
                    }
                    return this;
                }

                /**
                 * Provides the rationale that drove the inclusion of this particular activity as part of the plan or the reason why the 
                 * activity was prohibited.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param reasonCode
                 *     Why activity should be done or why activity was prohibited
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
                    this.reasonCode = new ArrayList<>(reasonCode);
                    return this;
                }

                /**
                 * Indicates another resource, such as the health condition(s), whose existence justifies this request and drove the 
                 * inclusion of this particular activity as part of the plan.
                 * 
                 * <p>Adds new element(s) to the existing list
                 * 
                 * @param reasonReference
                 *     Why activity is needed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reasonReference(Reference... reasonReference) {
                    for (Reference value : reasonReference) {
                        this.reasonReference.add(value);
                    }
                    return this;
                }

                /**
                 * Indicates another resource, such as the health condition(s), whose existence justifies this request and drove the 
                 * inclusion of this particular activity as part of the plan.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param reasonReference
                 *     Why activity is needed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reasonReference(Collection<Reference> reasonReference) {
                    this.reasonReference = new ArrayList<>(reasonReference);
                    return this;
                }

                /**
                 * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
                 * 
                 * <p>Adds new element(s) to the existing list
                 * 
                 * @param goal
                 *     Goals this activity relates to
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder goal(Reference... goal) {
                    for (Reference value : goal) {
                        this.goal.add(value);
                    }
                    return this;
                }

                /**
                 * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param goal
                 *     Goals this activity relates to
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder goal(Collection<Reference> goal) {
                    this.goal = new ArrayList<>(goal);
                    return this;
                }

                /**
                 * Identifies what progress is being made for the specific activity.
                 * 
                 * <p>This element is required.
                 * 
                 * @param status
                 *     not-started | scheduled | in-progress | on-hold | completed | cancelled | stopped | unknown | entered-in-error
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder status(CarePlanActivityStatus status) {
                    this.status = status;
                    return this;
                }

                /**
                 * Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.
                 * 
                 * @param statusReason
                 *     Reason for current status
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder statusReason(CodeableConcept statusReason) {
                    this.statusReason = statusReason;
                    return this;
                }

                /**
                 * If true, indicates that the described activity is one that must NOT be engaged in when following the plan. If false, 
                 * or missing, indicates that the described activity is one that should be engaged in when following the plan.
                 * 
                 * @param doNotPerform
                 *     If true, activity is prohibiting action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder doNotPerform(Boolean doNotPerform) {
                    this.doNotPerform = doNotPerform;
                    return this;
                }

                /**
                 * The period, timing or frequency upon which the described activity is to occur.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Timing}</li>
                 * <li>{@link Period}</li>
                 * <li>{@link String}</li>
                 * </ul>
                 * 
                 * @param scheduled
                 *     When activity is to occur
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder scheduled(Element scheduled) {
                    this.scheduled = scheduled;
                    return this;
                }

                /**
                 * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
                 * 
                 * @param location
                 *     Where it should happen
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder location(Reference location) {
                    this.location = location;
                    return this;
                }

                /**
                 * Identifies who's expected to be involved in the activity.
                 * 
                 * <p>Adds new element(s) to the existing list
                 * 
                 * @param performer
                 *     Who will be responsible?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder performer(Reference... performer) {
                    for (Reference value : performer) {
                        this.performer.add(value);
                    }
                    return this;
                }

                /**
                 * Identifies who's expected to be involved in the activity.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param performer
                 *     Who will be responsible?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder performer(Collection<Reference> performer) {
                    this.performer = new ArrayList<>(performer);
                    return this;
                }

                /**
                 * Identifies the food, drug or other product to be consumed or supplied in the activity.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link CodeableConcept}</li>
                 * <li>{@link Reference}</li>
                 * </ul>
                 * 
                 * @param product
                 *     What is to be administered/supplied
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder product(Element product) {
                    this.product = product;
                    return this;
                }

                /**
                 * Identifies the quantity expected to be consumed in a given day.
                 * 
                 * @param dailyAmount
                 *     How to consume/day?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder dailyAmount(SimpleQuantity dailyAmount) {
                    this.dailyAmount = dailyAmount;
                    return this;
                }

                /**
                 * Identifies the quantity expected to be supplied, administered or consumed by the subject.
                 * 
                 * @param quantity
                 *     How much to administer/supply/consume
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder quantity(SimpleQuantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * This provides a textual description of constraints on the intended activity occurrence, including relation to other 
                 * activities. It may also include objectives, pre-conditions and end-conditions. Finally, it may convey specifics about 
                 * the activity such as body site, method, route, etc.
                 * 
                 * @param description
                 *     Extra info describing activity to perform
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Build the {@link Detail}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>status</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Detail}
                 */
                @Override
                public Detail build() {
                    return new Detail(this);
                }

                protected Builder from(Detail detail) {
                    super.from(detail);
                    kind = detail.kind;
                    instantiatesCanonical.addAll(detail.instantiatesCanonical);
                    instantiatesUri.addAll(detail.instantiatesUri);
                    code = detail.code;
                    reasonCode.addAll(detail.reasonCode);
                    reasonReference.addAll(detail.reasonReference);
                    goal.addAll(detail.goal);
                    status = detail.status;
                    statusReason = detail.statusReason;
                    doNotPerform = detail.doNotPerform;
                    scheduled = detail.scheduled;
                    location = detail.location;
                    performer.addAll(detail.performer);
                    product = detail.product;
                    dailyAmount = detail.dailyAmount;
                    quantity = detail.quantity;
                    description = detail.description;
                    return this;
                }
            }
        }
    }
}
