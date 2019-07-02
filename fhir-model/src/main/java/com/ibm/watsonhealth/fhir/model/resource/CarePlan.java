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
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.CarePlanActivityKind;
import com.ibm.watsonhealth.fhir.model.type.CarePlanActivityStatus;
import com.ibm.watsonhealth.fhir.model.type.CarePlanIntent;
import com.ibm.watsonhealth.fhir.model.type.CarePlanStatus;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or 
 * community for a period of time, possibly limited to care for a specific condition or set of conditions.
 * </p>
 */
@Constraint(
    id = "cpl-3",
    level = "Rule",
    location = "CarePlan.activity",
    description = "Provide a reference or detail, not both",
    expression = "detail.empty() or reference.empty()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class CarePlan extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Canonical> instantiatesCanonical;
    private final List<Uri> instantiatesUri;
    private final List<Reference> basedOn;
    private final List<Reference> replaces;
    private final List<Reference> partOf;
    private final CarePlanStatus status;
    private final CarePlanIntent intent;
    private final List<CodeableConcept> category;
    private final String title;
    private final String description;
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
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
        instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        replaces = Collections.unmodifiableList(builder.replaces);
        partOf = Collections.unmodifiableList(builder.partOf);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        intent = ValidationSupport.requireNonNull(builder.intent, "intent");
        category = Collections.unmodifiableList(builder.category);
        title = builder.title;
        description = builder.description;
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        encounter = builder.encounter;
        period = builder.period;
        created = builder.created;
        author = builder.author;
        contributor = Collections.unmodifiableList(builder.contributor);
        careTeam = Collections.unmodifiableList(builder.careTeam);
        addresses = Collections.unmodifiableList(builder.addresses);
        supportingInfo = Collections.unmodifiableList(builder.supportingInfo);
        goal = Collections.unmodifiableList(builder.goal);
        activity = Collections.unmodifiableList(builder.activity);
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * <p>
     * Business identifiers assigned to this care plan by the performer or other systems which remain constant as the 
     * resource is updated and propagates from server to server.
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
     * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
     * or in part by this CarePlan.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * <p>
     * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
     * in whole or in part by this CarePlan.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * <p>
     * A care plan that is fulfilled in whole or in part by this care plan.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * <p>
     * Completed or terminated care plan whose function is taken by this new care plan.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReplaces() {
        return replaces;
    }

    /**
     * <p>
     * A larger care plan of which this particular care plan is a component or step.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * <p>
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CarePlanStatus}.
     */
    public CarePlanStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Indicates the level of authority/intentionality associated with the care plan and where the care plan fits into the 
     * workflow chain.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CarePlanIntent}.
     */
    public CarePlanIntent getIntent() {
        return intent;
    }

    /**
     * <p>
     * Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans; e.g. "Home 
     * health", "psychiatric", "asthma", "disease management", "wellness plan", etc.
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
     * Human-friendly name for the care plan.
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
     * A description of the scope and nature of the plan.
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
     * Identifies the patient or group whose intended care is described by the plan.
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
     * The Encounter during which this CarePlan was created or to which the creation of this record is tightly associated.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * <p>
     * Indicates when the plan did (or is intended to) come into effect and end.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * <p>
     * Represents when this particular CarePlan record was created in the system, which is often a system-generated date.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * <p>
     * When populated, the author is responsible for the care plan. The care plan is attributed to the author.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * <p>
     * Identifies the individual(s) or organization who provided the contents of the care plan.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getContributor() {
        return contributor;
    }

    /**
     * <p>
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getCareTeam() {
        return careTeam;
    }

    /**
     * <p>
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
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
     * Identifies portions of the patient's record that specifically influenced the formation of the plan. These might 
     * include comorbidities, recent procedures, limitations, recent assessments, etc.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * <p>
     * Describes the intended objective(s) of carrying out the care plan.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getGoal() {
        return goal;
    }

    /**
     * <p>
     * Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, 
     * self-monitoring, education, etc.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Activity}.
     */
    public List<Activity> getActivity() {
        return activity;
    }

    /**
     * <p>
     * General notes about the care plan not covered elsewhere.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
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
        return new Builder(status, intent, subject).from(this);
    }

    public Builder toBuilder(CarePlanStatus status, CarePlanIntent intent, Reference subject) {
        return new Builder(status, intent, subject).from(this);
    }

    public static Builder builder(CarePlanStatus status, CarePlanIntent intent, Reference subject) {
        return new Builder(status, intent, subject);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final CarePlanStatus status;
        private final CarePlanIntent intent;
        private final Reference subject;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Canonical> instantiatesCanonical = new ArrayList<>();
        private List<Uri> instantiatesUri = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> replaces = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private List<CodeableConcept> category = new ArrayList<>();
        private String title;
        private String description;
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

        private Builder(CarePlanStatus status, CarePlanIntent intent, Reference subject) {
            super();
            this.status = status;
            this.intent = intent;
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
         * Business identifiers assigned to this care plan by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     External Ids for this plan
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
         * Business identifiers assigned to this care plan by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     External Ids for this plan
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
         * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
         * or in part by this CarePlan.
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesCanonical(Canonical... instantiatesCanonical) {
            for (Canonical value : instantiatesCanonical) {
                this.instantiatesCanonical.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
         * or in part by this CarePlan.
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical.addAll(instantiatesCanonical);
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
         * in whole or in part by this CarePlan.
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesUri(Uri... instantiatesUri) {
            for (Uri value : instantiatesUri) {
                this.instantiatesUri.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
         * in whole or in part by this CarePlan.
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri.addAll(instantiatesUri);
            return this;
        }

        /**
         * <p>
         * A care plan that is fulfilled in whole or in part by this care plan.
         * </p>
         * 
         * @param basedOn
         *     Fulfills CarePlan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A care plan that is fulfilled in whole or in part by this care plan.
         * </p>
         * 
         * @param basedOn
         *     Fulfills CarePlan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn.addAll(basedOn);
            return this;
        }

        /**
         * <p>
         * Completed or terminated care plan whose function is taken by this new care plan.
         * </p>
         * 
         * @param replaces
         *     CarePlan replaced by this CarePlan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder replaces(Reference... replaces) {
            for (Reference value : replaces) {
                this.replaces.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Completed or terminated care plan whose function is taken by this new care plan.
         * </p>
         * 
         * @param replaces
         *     CarePlan replaced by this CarePlan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder replaces(Collection<Reference> replaces) {
            this.replaces.addAll(replaces);
            return this;
        }

        /**
         * <p>
         * A larger care plan of which this particular care plan is a component or step.
         * </p>
         * 
         * @param partOf
         *     Part of referenced CarePlan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A larger care plan of which this particular care plan is a component or step.
         * </p>
         * 
         * @param partOf
         *     Part of referenced CarePlan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf.addAll(partOf);
            return this;
        }

        /**
         * <p>
         * Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans; e.g. "Home 
         * health", "psychiatric", "asthma", "disease management", "wellness plan", etc.
         * </p>
         * 
         * @param category
         *     Type of plan
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
         * Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans; e.g. "Home 
         * health", "psychiatric", "asthma", "disease management", "wellness plan", etc.
         * </p>
         * 
         * @param category
         *     Type of plan
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
         * Human-friendly name for the care plan.
         * </p>
         * 
         * @param title
         *     Human-friendly name for the care plan
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
         * A description of the scope and nature of the plan.
         * </p>
         * 
         * @param description
         *     Summary of nature of plan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * <p>
         * The Encounter during which this CarePlan was created or to which the creation of this record is tightly associated.
         * </p>
         * 
         * @param encounter
         *     Encounter created as part of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * <p>
         * Indicates when the plan did (or is intended to) come into effect and end.
         * </p>
         * 
         * @param period
         *     Time period plan covers
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * <p>
         * Represents when this particular CarePlan record was created in the system, which is often a system-generated date.
         * </p>
         * 
         * @param created
         *     Date record was first recorded
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        /**
         * <p>
         * When populated, the author is responsible for the care plan. The care plan is attributed to the author.
         * </p>
         * 
         * @param author
         *     Who is the designated responsible party
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder author(Reference author) {
            this.author = author;
            return this;
        }

        /**
         * <p>
         * Identifies the individual(s) or organization who provided the contents of the care plan.
         * </p>
         * 
         * @param contributor
         *     Who provided the content of the care plan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contributor(Reference... contributor) {
            for (Reference value : contributor) {
                this.contributor.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies the individual(s) or organization who provided the contents of the care plan.
         * </p>
         * 
         * @param contributor
         *     Who provided the content of the care plan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contributor(Collection<Reference> contributor) {
            this.contributor.addAll(contributor);
            return this;
        }

        /**
         * <p>
         * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
         * </p>
         * 
         * @param careTeam
         *     Who's involved in plan?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder careTeam(Reference... careTeam) {
            for (Reference value : careTeam) {
                this.careTeam.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
         * </p>
         * 
         * @param careTeam
         *     Who's involved in plan?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder careTeam(Collection<Reference> careTeam) {
            this.careTeam.addAll(careTeam);
            return this;
        }

        /**
         * <p>
         * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
         * </p>
         * 
         * @param addresses
         *     Health issues this plan addresses
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
         * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
         * </p>
         * 
         * @param addresses
         *     Health issues this plan addresses
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
         * Identifies portions of the patient's record that specifically influenced the formation of the plan. These might 
         * include comorbidities, recent procedures, limitations, recent assessments, etc.
         * </p>
         * 
         * @param supportingInfo
         *     Information considered as part of plan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInfo(Reference... supportingInfo) {
            for (Reference value : supportingInfo) {
                this.supportingInfo.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies portions of the patient's record that specifically influenced the formation of the plan. These might 
         * include comorbidities, recent procedures, limitations, recent assessments, etc.
         * </p>
         * 
         * @param supportingInfo
         *     Information considered as part of plan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInfo(Collection<Reference> supportingInfo) {
            this.supportingInfo.addAll(supportingInfo);
            return this;
        }

        /**
         * <p>
         * Describes the intended objective(s) of carrying out the care plan.
         * </p>
         * 
         * @param goal
         *     Desired outcome of plan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder goal(Reference... goal) {
            for (Reference value : goal) {
                this.goal.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Describes the intended objective(s) of carrying out the care plan.
         * </p>
         * 
         * @param goal
         *     Desired outcome of plan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder goal(Collection<Reference> goal) {
            this.goal.addAll(goal);
            return this;
        }

        /**
         * <p>
         * Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, 
         * self-monitoring, education, etc.
         * </p>
         * 
         * @param activity
         *     Action to occur as part of plan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder activity(Activity... activity) {
            for (Activity value : activity) {
                this.activity.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, 
         * self-monitoring, education, etc.
         * </p>
         * 
         * @param activity
         *     Action to occur as part of plan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder activity(Collection<Activity> activity) {
            this.activity.addAll(activity);
            return this;
        }

        /**
         * <p>
         * General notes about the care plan not covered elsewhere.
         * </p>
         * 
         * @param note
         *     Comments about the plan
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
         * General notes about the care plan not covered elsewhere.
         * </p>
         * 
         * @param note
         *     Comments about the plan
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        @Override
        public CarePlan build() {
            return new CarePlan(this);
        }

        private Builder from(CarePlan carePlan) {
            id = carePlan.id;
            meta = carePlan.meta;
            implicitRules = carePlan.implicitRules;
            language = carePlan.language;
            text = carePlan.text;
            contained.addAll(carePlan.contained);
            extension.addAll(carePlan.extension);
            modifierExtension.addAll(carePlan.modifierExtension);
            identifier.addAll(carePlan.identifier);
            instantiatesCanonical.addAll(carePlan.instantiatesCanonical);
            instantiatesUri.addAll(carePlan.instantiatesUri);
            basedOn.addAll(carePlan.basedOn);
            replaces.addAll(carePlan.replaces);
            partOf.addAll(carePlan.partOf);
            category.addAll(carePlan.category);
            title = carePlan.title;
            description = carePlan.description;
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
     * <p>
     * Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, 
     * self-monitoring, education, etc.
     * </p>
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
            outcomeCodeableConcept = Collections.unmodifiableList(builder.outcomeCodeableConcept);
            outcomeReference = Collections.unmodifiableList(builder.outcomeReference);
            progress = Collections.unmodifiableList(builder.progress);
            reference = builder.reference;
            detail = builder.detail;
        }

        /**
         * <p>
         * Identifies the outcome at the point when the status of the activity is assessed. For example, the outcome of an 
         * education activity could be patient understands (or not).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getOutcomeCodeableConcept() {
            return outcomeCodeableConcept;
        }

        /**
         * <p>
         * Details of the outcome or action resulting from the activity. The reference to an "event" resource, such as Procedure 
         * or Encounter or Observation, is the result/outcome of the activity itself. The activity can be conveyed using CarePlan.
         * activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getOutcomeReference() {
            return outcomeReference;
        }

        /**
         * <p>
         * Notes about the adherence/status/progress of the activity.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Annotation}.
         */
        public List<Annotation> getProgress() {
            return progress;
        }

        /**
         * <p>
         * The details of the proposed activity represented in a specific resource.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getReference() {
            return reference;
        }

        /**
         * <p>
         * A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know 
         * about specific resources such as procedure etc.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Detail}.
         */
        public Detail getDetail() {
            return detail;
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
                    accept(outcomeCodeableConcept, "outcomeCodeableConcept", visitor, CodeableConcept.class);
                    accept(outcomeReference, "outcomeReference", visitor, Reference.class);
                    accept(progress, "progress", visitor, Annotation.class);
                    accept(reference, "reference", visitor);
                    accept(detail, "detail", visitor);
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
            // optional
            private List<CodeableConcept> outcomeCodeableConcept = new ArrayList<>();
            private List<Reference> outcomeReference = new ArrayList<>();
            private List<Annotation> progress = new ArrayList<>();
            private Reference reference;
            private Detail detail;

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
             * Identifies the outcome at the point when the status of the activity is assessed. For example, the outcome of an 
             * education activity could be patient understands (or not).
             * </p>
             * 
             * @param outcomeCodeableConcept
             *     Results of the activity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder outcomeCodeableConcept(CodeableConcept... outcomeCodeableConcept) {
                for (CodeableConcept value : outcomeCodeableConcept) {
                    this.outcomeCodeableConcept.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Identifies the outcome at the point when the status of the activity is assessed. For example, the outcome of an 
             * education activity could be patient understands (or not).
             * </p>
             * 
             * @param outcomeCodeableConcept
             *     Results of the activity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder outcomeCodeableConcept(Collection<CodeableConcept> outcomeCodeableConcept) {
                this.outcomeCodeableConcept.addAll(outcomeCodeableConcept);
                return this;
            }

            /**
             * <p>
             * Details of the outcome or action resulting from the activity. The reference to an "event" resource, such as Procedure 
             * or Encounter or Observation, is the result/outcome of the activity itself. The activity can be conveyed using CarePlan.
             * activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource).
             * </p>
             * 
             * @param outcomeReference
             *     Appointment, Encounter, Procedure, etc.
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
             * Details of the outcome or action resulting from the activity. The reference to an "event" resource, such as Procedure 
             * or Encounter or Observation, is the result/outcome of the activity itself. The activity can be conveyed using CarePlan.
             * activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource).
             * </p>
             * 
             * @param outcomeReference
             *     Appointment, Encounter, Procedure, etc.
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder outcomeReference(Collection<Reference> outcomeReference) {
                this.outcomeReference.addAll(outcomeReference);
                return this;
            }

            /**
             * <p>
             * Notes about the adherence/status/progress of the activity.
             * </p>
             * 
             * @param progress
             *     Comments about the activity status/progress
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder progress(Annotation... progress) {
                for (Annotation value : progress) {
                    this.progress.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Notes about the adherence/status/progress of the activity.
             * </p>
             * 
             * @param progress
             *     Comments about the activity status/progress
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder progress(Collection<Annotation> progress) {
                this.progress.addAll(progress);
                return this;
            }

            /**
             * <p>
             * The details of the proposed activity represented in a specific resource.
             * </p>
             * 
             * @param reference
             *     Activity details defined in specific resource
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder reference(Reference reference) {
                this.reference = reference;
                return this;
            }

            /**
             * <p>
             * A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know 
             * about specific resources such as procedure etc.
             * </p>
             * 
             * @param detail
             *     In-line definition of activity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Detail detail) {
                this.detail = detail;
                return this;
            }

            @Override
            public Activity build() {
                return new Activity(this);
            }

            private Builder from(Activity activity) {
                id = activity.id;
                extension.addAll(activity.extension);
                modifierExtension.addAll(activity.modifierExtension);
                outcomeCodeableConcept.addAll(activity.outcomeCodeableConcept);
                outcomeReference.addAll(activity.outcomeReference);
                progress.addAll(activity.progress);
                reference = activity.reference;
                detail = activity.detail;
                return this;
            }
        }

        /**
         * <p>
         * A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know 
         * about specific resources such as procedure etc.
         * </p>
         */
        public static class Detail extends BackboneElement {
            private final CarePlanActivityKind kind;
            private final List<Canonical> instantiatesCanonical;
            private final List<Uri> instantiatesUri;
            private final CodeableConcept code;
            private final List<CodeableConcept> reasonCode;
            private final List<Reference> reasonReference;
            private final List<Reference> goal;
            private final CarePlanActivityStatus status;
            private final CodeableConcept statusReason;
            private final Boolean doNotPerform;
            private final Element scheduled;
            private final Reference location;
            private final List<Reference> performer;
            private final Element product;
            private final Quantity dailyAmount;
            private final Quantity quantity;
            private final String description;

            private volatile int hashCode;

            private Detail(Builder builder) {
                super(builder);
                kind = builder.kind;
                instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
                instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
                code = builder.code;
                reasonCode = Collections.unmodifiableList(builder.reasonCode);
                reasonReference = Collections.unmodifiableList(builder.reasonReference);
                goal = Collections.unmodifiableList(builder.goal);
                status = ValidationSupport.requireNonNull(builder.status, "status");
                statusReason = builder.statusReason;
                doNotPerform = builder.doNotPerform;
                scheduled = ValidationSupport.choiceElement(builder.scheduled, "scheduled", Timing.class, Period.class, String.class);
                location = builder.location;
                performer = Collections.unmodifiableList(builder.performer);
                product = ValidationSupport.choiceElement(builder.product, "product", CodeableConcept.class, Reference.class);
                dailyAmount = builder.dailyAmount;
                quantity = builder.quantity;
                description = builder.description;
            }

            /**
             * <p>
             * A description of the kind of resource the in-line definition of a care plan activity is representing. The CarePlan.
             * activity.detail is an in-line definition when a resource is not referenced using CarePlan.activity.reference. For 
             * example, a MedicationRequest, a ServiceRequest, or a CommunicationRequest.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CarePlanActivityKind}.
             */
            public CarePlanActivityKind getKind() {
                return kind;
            }

            /**
             * <p>
             * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
             * or in part by this CarePlan activity.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Canonical}.
             */
            public List<Canonical> getInstantiatesCanonical() {
                return instantiatesCanonical;
            }

            /**
             * <p>
             * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
             * in whole or in part by this CarePlan activity.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Uri}.
             */
            public List<Uri> getInstantiatesUri() {
                return instantiatesUri;
            }

            /**
             * <p>
             * Detailed description of the type of planned activity; e.g. what lab test, what procedure, what kind of encounter.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * <p>
             * Provides the rationale that drove the inclusion of this particular activity as part of the plan or the reason why the 
             * activity was prohibited.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getReasonCode() {
                return reasonCode;
            }

            /**
             * <p>
             * Indicates another resource, such as the health condition(s), whose existence justifies this request and drove the 
             * inclusion of this particular activity as part of the plan.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Reference}.
             */
            public List<Reference> getReasonReference() {
                return reasonReference;
            }

            /**
             * <p>
             * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Reference}.
             */
            public List<Reference> getGoal() {
                return goal;
            }

            /**
             * <p>
             * Identifies what progress is being made for the specific activity.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CarePlanActivityStatus}.
             */
            public CarePlanActivityStatus getStatus() {
                return status;
            }

            /**
             * <p>
             * Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getStatusReason() {
                return statusReason;
            }

            /**
             * <p>
             * If true, indicates that the described activity is one that must NOT be engaged in when following the plan. If false, 
             * or missing, indicates that the described activity is one that should be engaged in when following the plan.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getDoNotPerform() {
                return doNotPerform;
            }

            /**
             * <p>
             * The period, timing or frequency upon which the described activity is to occur.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getScheduled() {
                return scheduled;
            }

            /**
             * <p>
             * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Reference}.
             */
            public Reference getLocation() {
                return location;
            }

            /**
             * <p>
             * Identifies who's expected to be involved in the activity.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Reference}.
             */
            public List<Reference> getPerformer() {
                return performer;
            }

            /**
             * <p>
             * Identifies the food, drug or other product to be consumed or supplied in the activity.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getProduct() {
                return product;
            }

            /**
             * <p>
             * Identifies the quantity expected to be consumed in a given day.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Quantity}.
             */
            public Quantity getDailyAmount() {
                return dailyAmount;
            }

            /**
             * <p>
             * Identifies the quantity expected to be supplied, administered or consumed by the subject.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Quantity}.
             */
            public Quantity getQuantity() {
                return quantity;
            }

            /**
             * <p>
             * This provides a textual description of constraints on the intended activity occurrence, including relation to other 
             * activities. It may also include objectives, pre-conditions and end-conditions. Finally, it may convey specifics about 
             * the activity such as body site, method, route, etc.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getDescription() {
                return description;
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
                return new Builder(status).from(this);
            }

            public Builder toBuilder(CarePlanActivityStatus status) {
                return new Builder(status).from(this);
            }

            public static Builder builder(CarePlanActivityStatus status) {
                return new Builder(status);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CarePlanActivityStatus status;

                // optional
                private CarePlanActivityKind kind;
                private List<Canonical> instantiatesCanonical = new ArrayList<>();
                private List<Uri> instantiatesUri = new ArrayList<>();
                private CodeableConcept code;
                private List<CodeableConcept> reasonCode = new ArrayList<>();
                private List<Reference> reasonReference = new ArrayList<>();
                private List<Reference> goal = new ArrayList<>();
                private CodeableConcept statusReason;
                private Boolean doNotPerform;
                private Element scheduled;
                private Reference location;
                private List<Reference> performer = new ArrayList<>();
                private Element product;
                private Quantity dailyAmount;
                private Quantity quantity;
                private String description;

                private Builder(CarePlanActivityStatus status) {
                    super();
                    this.status = status;
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
                 * A description of the kind of resource the in-line definition of a care plan activity is representing. The CarePlan.
                 * activity.detail is an in-line definition when a resource is not referenced using CarePlan.activity.reference. For 
                 * example, a MedicationRequest, a ServiceRequest, or a CommunicationRequest.
                 * </p>
                 * 
                 * @param kind
                 *     Kind of resource
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder kind(CarePlanActivityKind kind) {
                    this.kind = kind;
                    return this;
                }

                /**
                 * <p>
                 * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
                 * or in part by this CarePlan activity.
                 * </p>
                 * 
                 * @param instantiatesCanonical
                 *     Instantiates FHIR protocol or definition
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder instantiatesCanonical(Canonical... instantiatesCanonical) {
                    for (Canonical value : instantiatesCanonical) {
                        this.instantiatesCanonical.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The URL pointing to a FHIR-defined protocol, guideline, questionnaire or other definition that is adhered to in whole 
                 * or in part by this CarePlan activity.
                 * </p>
                 * 
                 * @param instantiatesCanonical
                 *     Instantiates FHIR protocol or definition
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
                    this.instantiatesCanonical.addAll(instantiatesCanonical);
                    return this;
                }

                /**
                 * <p>
                 * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
                 * in whole or in part by this CarePlan activity.
                 * </p>
                 * 
                 * @param instantiatesUri
                 *     Instantiates external protocol or definition
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder instantiatesUri(Uri... instantiatesUri) {
                    for (Uri value : instantiatesUri) {
                        this.instantiatesUri.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The URL pointing to an externally maintained protocol, guideline, questionnaire or other definition that is adhered to 
                 * in whole or in part by this CarePlan activity.
                 * </p>
                 * 
                 * @param instantiatesUri
                 *     Instantiates external protocol or definition
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
                    this.instantiatesUri.addAll(instantiatesUri);
                    return this;
                }

                /**
                 * <p>
                 * Detailed description of the type of planned activity; e.g. what lab test, what procedure, what kind of encounter.
                 * </p>
                 * 
                 * @param code
                 *     Detail type of activity
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder code(CodeableConcept code) {
                    this.code = code;
                    return this;
                }

                /**
                 * <p>
                 * Provides the rationale that drove the inclusion of this particular activity as part of the plan or the reason why the 
                 * activity was prohibited.
                 * </p>
                 * 
                 * @param reasonCode
                 *     Why activity should be done or why activity was prohibited
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder reasonCode(CodeableConcept... reasonCode) {
                    for (CodeableConcept value : reasonCode) {
                        this.reasonCode.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Provides the rationale that drove the inclusion of this particular activity as part of the plan or the reason why the 
                 * activity was prohibited.
                 * </p>
                 * 
                 * @param reasonCode
                 *     Why activity should be done or why activity was prohibited
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
                    this.reasonCode.addAll(reasonCode);
                    return this;
                }

                /**
                 * <p>
                 * Indicates another resource, such as the health condition(s), whose existence justifies this request and drove the 
                 * inclusion of this particular activity as part of the plan.
                 * </p>
                 * 
                 * @param reasonReference
                 *     Why activity is needed
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder reasonReference(Reference... reasonReference) {
                    for (Reference value : reasonReference) {
                        this.reasonReference.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Indicates another resource, such as the health condition(s), whose existence justifies this request and drove the 
                 * inclusion of this particular activity as part of the plan.
                 * </p>
                 * 
                 * @param reasonReference
                 *     Why activity is needed
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder reasonReference(Collection<Reference> reasonReference) {
                    this.reasonReference.addAll(reasonReference);
                    return this;
                }

                /**
                 * <p>
                 * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
                 * </p>
                 * 
                 * @param goal
                 *     Goals this activity relates to
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder goal(Reference... goal) {
                    for (Reference value : goal) {
                        this.goal.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
                 * </p>
                 * 
                 * @param goal
                 *     Goals this activity relates to
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder goal(Collection<Reference> goal) {
                    this.goal.addAll(goal);
                    return this;
                }

                /**
                 * <p>
                 * Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.
                 * </p>
                 * 
                 * @param statusReason
                 *     Reason for current status
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder statusReason(CodeableConcept statusReason) {
                    this.statusReason = statusReason;
                    return this;
                }

                /**
                 * <p>
                 * If true, indicates that the described activity is one that must NOT be engaged in when following the plan. If false, 
                 * or missing, indicates that the described activity is one that should be engaged in when following the plan.
                 * </p>
                 * 
                 * @param doNotPerform
                 *     If true, activity is prohibiting action
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder doNotPerform(Boolean doNotPerform) {
                    this.doNotPerform = doNotPerform;
                    return this;
                }

                /**
                 * <p>
                 * The period, timing or frequency upon which the described activity is to occur.
                 * </p>
                 * 
                 * @param scheduled
                 *     When activity is to occur
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder scheduled(Element scheduled) {
                    this.scheduled = scheduled;
                    return this;
                }

                /**
                 * <p>
                 * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
                 * </p>
                 * 
                 * @param location
                 *     Where it should happen
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder location(Reference location) {
                    this.location = location;
                    return this;
                }

                /**
                 * <p>
                 * Identifies who's expected to be involved in the activity.
                 * </p>
                 * 
                 * @param performer
                 *     Who will be responsible?
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder performer(Reference... performer) {
                    for (Reference value : performer) {
                        this.performer.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Identifies who's expected to be involved in the activity.
                 * </p>
                 * 
                 * @param performer
                 *     Who will be responsible?
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder performer(Collection<Reference> performer) {
                    this.performer.addAll(performer);
                    return this;
                }

                /**
                 * <p>
                 * Identifies the food, drug or other product to be consumed or supplied in the activity.
                 * </p>
                 * 
                 * @param product
                 *     What is to be administered/supplied
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder product(Element product) {
                    this.product = product;
                    return this;
                }

                /**
                 * <p>
                 * Identifies the quantity expected to be consumed in a given day.
                 * </p>
                 * 
                 * @param dailyAmount
                 *     How to consume/day?
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder dailyAmount(Quantity dailyAmount) {
                    this.dailyAmount = dailyAmount;
                    return this;
                }

                /**
                 * <p>
                 * Identifies the quantity expected to be supplied, administered or consumed by the subject.
                 * </p>
                 * 
                 * @param quantity
                 *     How much to administer/supply/consume
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder quantity(Quantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * <p>
                 * This provides a textual description of constraints on the intended activity occurrence, including relation to other 
                 * activities. It may also include objectives, pre-conditions and end-conditions. Finally, it may convey specifics about 
                 * the activity such as body site, method, route, etc.
                 * </p>
                 * 
                 * @param description
                 *     Extra info describing activity to perform
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                @Override
                public Detail build() {
                    return new Detail(this);
                }

                private Builder from(Detail detail) {
                    id = detail.id;
                    extension.addAll(detail.extension);
                    modifierExtension.addAll(detail.modifierExtension);
                    kind = detail.kind;
                    instantiatesCanonical.addAll(detail.instantiatesCanonical);
                    instantiatesUri.addAll(detail.instantiatesUri);
                    code = detail.code;
                    reasonCode.addAll(detail.reasonCode);
                    reasonReference.addAll(detail.reasonReference);
                    goal.addAll(detail.goal);
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
