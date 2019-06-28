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
import com.ibm.watsonhealth.fhir.model.type.Address;
import com.ibm.watsonhealth.fhir.model.type.Age;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Contributor;
import com.ibm.watsonhealth.fhir.model.type.Count;
import com.ibm.watsonhealth.fhir.model.type.DataRequirement;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Distance;
import com.ibm.watsonhealth.fhir.model.type.Dosage;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Expression;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Oid;
import com.ibm.watsonhealth.fhir.model.type.ParameterDefinition;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RelatedArtifact;
import com.ibm.watsonhealth.fhir.model.type.SampledData;
import com.ibm.watsonhealth.fhir.model.type.Signature;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.TaskIntent;
import com.ibm.watsonhealth.fhir.model.type.TaskPriority;
import com.ibm.watsonhealth.fhir.model.type.TaskStatus;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.TriggerDefinition;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Url;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.type.Uuid;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A task to be performed.
 * </p>
 */
@Constraint(
    id = "inv-1",
    level = "Rule",
    location = "(base)",
    description = "Last modified date must be greater than or equal to authored-on date.",
    expression = "lastModified.exists().not() or authoredOn.exists().not() or lastModified >= authoredOn"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Task extends DomainResource {
    private final List<Identifier> identifier;
    private final Canonical instantiatesCanonical;
    private final Uri instantiatesUri;
    private final List<Reference> basedOn;
    private final Identifier groupIdentifier;
    private final List<Reference> partOf;
    private final TaskStatus status;
    private final CodeableConcept statusReason;
    private final CodeableConcept businessStatus;
    private final TaskIntent intent;
    private final TaskPriority priority;
    private final CodeableConcept code;
    private final String description;
    private final Reference focus;
    private final Reference _for;
    private final Reference encounter;
    private final Period executionPeriod;
    private final DateTime authoredOn;
    private final DateTime lastModified;
    private final Reference requester;
    private final List<CodeableConcept> performerType;
    private final Reference owner;
    private final Reference location;
    private final CodeableConcept reasonCode;
    private final Reference reasonReference;
    private final List<Reference> insurance;
    private final List<Annotation> note;
    private final List<Reference> relevantHistory;
    private final Restriction restriction;
    private final List<Input> input;
    private final List<Output> output;

    private Task(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.instantiatesCanonical = builder.instantiatesCanonical;
        this.instantiatesUri = builder.instantiatesUri;
        this.basedOn = builder.basedOn;
        this.groupIdentifier = builder.groupIdentifier;
        this.partOf = builder.partOf;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.statusReason = builder.statusReason;
        this.businessStatus = builder.businessStatus;
        this.intent = ValidationSupport.requireNonNull(builder.intent, "intent");
        this.priority = builder.priority;
        this.code = builder.code;
        this.description = builder.description;
        this.focus = builder.focus;
        this._for = builder._for;
        this.encounter = builder.encounter;
        this.executionPeriod = builder.executionPeriod;
        this.authoredOn = builder.authoredOn;
        this.lastModified = builder.lastModified;
        this.requester = builder.requester;
        this.performerType = builder.performerType;
        this.owner = builder.owner;
        this.location = builder.location;
        this.reasonCode = builder.reasonCode;
        this.reasonReference = builder.reasonReference;
        this.insurance = builder.insurance;
        this.note = builder.note;
        this.relevantHistory = builder.relevantHistory;
        this.restriction = builder.restriction;
        this.input = builder.input;
        this.output = builder.output;
    }

    /**
     * <p>
     * The business identifier for this task.
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
     * The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or 
     * in part by this Task.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * <p>
     * The URL pointing to an *externally* maintained protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this Task.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * <p>
     * BasedOn refers to a higher-level authorization that triggered the creation of the task. It references a "request" 
     * resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the 
     * "request" resource the task is seeking to fulfill. This latter resource is referenced by FocusOn. For example, based 
     * on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen 
     * from a patient.
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
     * An identifier that links together multiple tasks and other requests that were created in the same context.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getGroupIdentifier() {
        return groupIdentifier;
    }

    /**
     * <p>
     * Task that this particular task is part of.
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
     * The current status of the task.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link TaskStatus}.
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * An explanation as to why this task is held, failed, was refused, etc.
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
     * Contains business-specific nuances of the business state.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getBusinessStatus() {
        return businessStatus;
    }

    /**
     * <p>
     * Indicates the "level" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, 
     * an actionable task, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link TaskIntent}.
     */
    public TaskIntent getIntent() {
        return intent;
    }

    /**
     * <p>
     * Indicates how quickly the Task should be addressed with respect to other requests.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link TaskPriority}.
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * <p>
     * A name or code (or both) briefly describing what the task involves.
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
     * A free-text description of what is to be performed.
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
     * The request being actioned or the resource being manipulated by this task.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getFocus() {
        return focus;
    }

    /**
     * <p>
     * The entity who benefits from the performance of the service specified in the task (e.g., the patient).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getfor() {
        return _for;
    }

    /**
     * <p>
     * The healthcare event (e.g. a patient and healthcare provider interaction) during which this task was created.
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
     * Identifies the time action was first taken against the task (start) and/or the time final action was taken against the 
     * task prior to marking it as completed (end).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * <p>
     * The date and time this task was created.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getAuthoredOn() {
        return authoredOn;
    }

    /**
     * <p>
     * The date and time of last modification to this task.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getLastModified() {
        return lastModified;
    }

    /**
     * <p>
     * The creator of the task.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRequester() {
        return requester;
    }

    /**
     * <p>
     * The kind of participant that should perform the task.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getPerformerType() {
        return performerType;
    }

    /**
     * <p>
     * Individual organization or Device currently responsible for task execution.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOwner() {
        return owner;
    }

    /**
     * <p>
     * Principal physical location where the this task is performed.
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
     * A description or code indicating why this task needs to be performed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getReasonCode() {
        return reasonCode;
    }

    /**
     * <p>
     * A resource reference indicating why this task needs to be performed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getReasonReference() {
        return reasonReference;
    }

    /**
     * <p>
     * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getInsurance() {
        return insurance;
    }

    /**
     * <p>
     * Free-text information captured about the task as it progresses.
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
     * Links to Provenance records for past versions of this Task that identify key state transitions or updates that are 
     * likely to be relevant to a user looking at the current version of the task.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getRelevantHistory() {
        return relevantHistory;
    }

    /**
     * <p>
     * If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be 
     * actioned), this element identifies any limitations on what parts of the referenced request should be actioned.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Restriction}.
     */
    public Restriction getRestriction() {
        return restriction;
    }

    /**
     * <p>
     * Additional information that may be needed in the execution of the task.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Input}.
     */
    public List<Input> getInput() {
        return input;
    }

    /**
     * <p>
     * Outputs produced by the Task.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Output}.
     */
    public List<Output> getOutput() {
        return output;
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
                accept(instantiatesCanonical, "instantiatesCanonical", visitor);
                accept(instantiatesUri, "instantiatesUri", visitor);
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(groupIdentifier, "groupIdentifier", visitor);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(statusReason, "statusReason", visitor);
                accept(businessStatus, "businessStatus", visitor);
                accept(intent, "intent", visitor);
                accept(priority, "priority", visitor);
                accept(code, "code", visitor);
                accept(description, "description", visitor);
                accept(focus, "focus", visitor);
                accept(_for, "for", visitor);
                accept(encounter, "encounter", visitor);
                accept(executionPeriod, "executionPeriod", visitor);
                accept(authoredOn, "authoredOn", visitor);
                accept(lastModified, "lastModified", visitor);
                accept(requester, "requester", visitor);
                accept(performerType, "performerType", visitor, CodeableConcept.class);
                accept(owner, "owner", visitor);
                accept(location, "location", visitor);
                accept(reasonCode, "reasonCode", visitor);
                accept(reasonReference, "reasonReference", visitor);
                accept(insurance, "insurance", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(relevantHistory, "relevantHistory", visitor, Reference.class);
                accept(restriction, "restriction", visitor);
                accept(input, "input", visitor, Input.class);
                accept(output, "output", visitor, Output.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, intent);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.instantiatesCanonical = instantiatesCanonical;
        builder.instantiatesUri = instantiatesUri;
        builder.basedOn.addAll(basedOn);
        builder.groupIdentifier = groupIdentifier;
        builder.partOf.addAll(partOf);
        builder.statusReason = statusReason;
        builder.businessStatus = businessStatus;
        builder.priority = priority;
        builder.code = code;
        builder.description = description;
        builder.focus = focus;
        builder._for = _for;
        builder.encounter = encounter;
        builder.executionPeriod = executionPeriod;
        builder.authoredOn = authoredOn;
        builder.lastModified = lastModified;
        builder.requester = requester;
        builder.performerType.addAll(performerType);
        builder.owner = owner;
        builder.location = location;
        builder.reasonCode = reasonCode;
        builder.reasonReference = reasonReference;
        builder.insurance.addAll(insurance);
        builder.note.addAll(note);
        builder.relevantHistory.addAll(relevantHistory);
        builder.restriction = restriction;
        builder.input.addAll(input);
        builder.output.addAll(output);
        return builder;
    }

    public static Builder builder(TaskStatus status, TaskIntent intent) {
        return new Builder(status, intent);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final TaskStatus status;
        private final TaskIntent intent;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Canonical instantiatesCanonical;
        private Uri instantiatesUri;
        private List<Reference> basedOn = new ArrayList<>();
        private Identifier groupIdentifier;
        private List<Reference> partOf = new ArrayList<>();
        private CodeableConcept statusReason;
        private CodeableConcept businessStatus;
        private TaskPriority priority;
        private CodeableConcept code;
        private String description;
        private Reference focus;
        private Reference _for;
        private Reference encounter;
        private Period executionPeriod;
        private DateTime authoredOn;
        private DateTime lastModified;
        private Reference requester;
        private List<CodeableConcept> performerType = new ArrayList<>();
        private Reference owner;
        private Reference location;
        private CodeableConcept reasonCode;
        private Reference reasonReference;
        private List<Reference> insurance = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Reference> relevantHistory = new ArrayList<>();
        private Restriction restriction;
        private List<Input> input = new ArrayList<>();
        private List<Output> output = new ArrayList<>();

        private Builder(TaskStatus status, TaskIntent intent) {
            super();
            this.status = status;
            this.intent = intent;
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
         * The business identifier for this task.
         * </p>
         * 
         * @param identifier
         *     Task Instance Identifier
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
         * The business identifier for this task.
         * </p>
         * 
         * @param identifier
         *     Task Instance Identifier
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
         * The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or 
         * in part by this Task.
         * </p>
         * 
         * @param instantiatesCanonical
         *     Formal definition of task
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesCanonical(Canonical instantiatesCanonical) {
            this.instantiatesCanonical = instantiatesCanonical;
            return this;
        }

        /**
         * <p>
         * The URL pointing to an *externally* maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this Task.
         * </p>
         * 
         * @param instantiatesUri
         *     Formal definition of task
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesUri(Uri instantiatesUri) {
            this.instantiatesUri = instantiatesUri;
            return this;
        }

        /**
         * <p>
         * BasedOn refers to a higher-level authorization that triggered the creation of the task. It references a "request" 
         * resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the 
         * "request" resource the task is seeking to fulfill. This latter resource is referenced by FocusOn. For example, based 
         * on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen 
         * from a patient.
         * </p>
         * 
         * @param basedOn
         *     Request fulfilled by this task
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
         * BasedOn refers to a higher-level authorization that triggered the creation of the task. It references a "request" 
         * resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the 
         * "request" resource the task is seeking to fulfill. This latter resource is referenced by FocusOn. For example, based 
         * on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen 
         * from a patient.
         * </p>
         * 
         * @param basedOn
         *     Request fulfilled by this task
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
         * An identifier that links together multiple tasks and other requests that were created in the same context.
         * </p>
         * 
         * @param groupIdentifier
         *     Requisition or grouper id
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder groupIdentifier(Identifier groupIdentifier) {
            this.groupIdentifier = groupIdentifier;
            return this;
        }

        /**
         * <p>
         * Task that this particular task is part of.
         * </p>
         * 
         * @param partOf
         *     Composite task
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
         * Task that this particular task is part of.
         * </p>
         * 
         * @param partOf
         *     Composite task
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
         * An explanation as to why this task is held, failed, was refused, etc.
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
         * Contains business-specific nuances of the business state.
         * </p>
         * 
         * @param businessStatus
         *     E.g. "Specimen collected", "IV prepped"
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder businessStatus(CodeableConcept businessStatus) {
            this.businessStatus = businessStatus;
            return this;
        }

        /**
         * <p>
         * Indicates how quickly the Task should be addressed with respect to other requests.
         * </p>
         * 
         * @param priority
         *     routine | urgent | asap | stat
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder priority(TaskPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * <p>
         * A name or code (or both) briefly describing what the task involves.
         * </p>
         * 
         * @param code
         *     Task Type
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
         * A free-text description of what is to be performed.
         * </p>
         * 
         * @param description
         *     Human-readable explanation of task
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
         * The request being actioned or the resource being manipulated by this task.
         * </p>
         * 
         * @param focus
         *     What task is acting on
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder focus(Reference focus) {
            this.focus = focus;
            return this;
        }

        /**
         * <p>
         * The entity who benefits from the performance of the service specified in the task (e.g., the patient).
         * </p>
         * 
         * @param _for
         *     Beneficiary of the Task
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder _for(Reference _for) {
            this._for = _for;
            return this;
        }

        /**
         * <p>
         * The healthcare event (e.g. a patient and healthcare provider interaction) during which this task was created.
         * </p>
         * 
         * @param encounter
         *     Healthcare event during which this task originated
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
         * Identifies the time action was first taken against the task (start) and/or the time final action was taken against the 
         * task prior to marking it as completed (end).
         * </p>
         * 
         * @param executionPeriod
         *     Start and end time of execution
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder executionPeriod(Period executionPeriod) {
            this.executionPeriod = executionPeriod;
            return this;
        }

        /**
         * <p>
         * The date and time this task was created.
         * </p>
         * 
         * @param authoredOn
         *     Task Creation Date
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder authoredOn(DateTime authoredOn) {
            this.authoredOn = authoredOn;
            return this;
        }

        /**
         * <p>
         * The date and time of last modification to this task.
         * </p>
         * 
         * @param lastModified
         *     Task Last Modified Date
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder lastModified(DateTime lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        /**
         * <p>
         * The creator of the task.
         * </p>
         * 
         * @param requester
         *     Who is asking for task to be done
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder requester(Reference requester) {
            this.requester = requester;
            return this;
        }

        /**
         * <p>
         * The kind of participant that should perform the task.
         * </p>
         * 
         * @param performerType
         *     Requested performer
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performerType(CodeableConcept... performerType) {
            for (CodeableConcept value : performerType) {
                this.performerType.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The kind of participant that should perform the task.
         * </p>
         * 
         * @param performerType
         *     Requested performer
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performerType(Collection<CodeableConcept> performerType) {
            this.performerType.addAll(performerType);
            return this;
        }

        /**
         * <p>
         * Individual organization or Device currently responsible for task execution.
         * </p>
         * 
         * @param owner
         *     Responsible individual
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder owner(Reference owner) {
            this.owner = owner;
            return this;
        }

        /**
         * <p>
         * Principal physical location where the this task is performed.
         * </p>
         * 
         * @param location
         *     Where task occurs
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
         * A description or code indicating why this task needs to be performed.
         * </p>
         * 
         * @param reasonCode
         *     Why task is needed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonCode(CodeableConcept reasonCode) {
            this.reasonCode = reasonCode;
            return this;
        }

        /**
         * <p>
         * A resource reference indicating why this task needs to be performed.
         * </p>
         * 
         * @param reasonReference
         *     Why task is needed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonReference(Reference reasonReference) {
            this.reasonReference = reasonReference;
            return this;
        }

        /**
         * <p>
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.
         * </p>
         * 
         * @param insurance
         *     Associated insurance coverage
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder insurance(Reference... insurance) {
            for (Reference value : insurance) {
                this.insurance.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.
         * </p>
         * 
         * @param insurance
         *     Associated insurance coverage
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder insurance(Collection<Reference> insurance) {
            this.insurance.addAll(insurance);
            return this;
        }

        /**
         * <p>
         * Free-text information captured about the task as it progresses.
         * </p>
         * 
         * @param note
         *     Comments made about the task
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
         * Free-text information captured about the task as it progresses.
         * </p>
         * 
         * @param note
         *     Comments made about the task
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
         * Links to Provenance records for past versions of this Task that identify key state transitions or updates that are 
         * likely to be relevant to a user looking at the current version of the task.
         * </p>
         * 
         * @param relevantHistory
         *     Key events in history of the Task
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relevantHistory(Reference... relevantHistory) {
            for (Reference value : relevantHistory) {
                this.relevantHistory.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Links to Provenance records for past versions of this Task that identify key state transitions or updates that are 
         * likely to be relevant to a user looking at the current version of the task.
         * </p>
         * 
         * @param relevantHistory
         *     Key events in history of the Task
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relevantHistory(Collection<Reference> relevantHistory) {
            this.relevantHistory.addAll(relevantHistory);
            return this;
        }

        /**
         * <p>
         * If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be 
         * actioned), this element identifies any limitations on what parts of the referenced request should be actioned.
         * </p>
         * 
         * @param restriction
         *     Constraints on fulfillment tasks
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder restriction(Restriction restriction) {
            this.restriction = restriction;
            return this;
        }

        /**
         * <p>
         * Additional information that may be needed in the execution of the task.
         * </p>
         * 
         * @param input
         *     Information used to perform task
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder input(Input... input) {
            for (Input value : input) {
                this.input.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Additional information that may be needed in the execution of the task.
         * </p>
         * 
         * @param input
         *     Information used to perform task
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder input(Collection<Input> input) {
            this.input.addAll(input);
            return this;
        }

        /**
         * <p>
         * Outputs produced by the Task.
         * </p>
         * 
         * @param output
         *     Information produced as part of task
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder output(Output... output) {
            for (Output value : output) {
                this.output.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Outputs produced by the Task.
         * </p>
         * 
         * @param output
         *     Information produced as part of task
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder output(Collection<Output> output) {
            this.output.addAll(output);
            return this;
        }

        @Override
        public Task build() {
            return new Task(this);
        }
    }

    /**
     * <p>
     * If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be 
     * actioned), this element identifies any limitations on what parts of the referenced request should be actioned.
     * </p>
     */
    public static class Restriction extends BackboneElement {
        private final PositiveInt repetitions;
        private final Period period;
        private final List<Reference> recipient;

        private Restriction(Builder builder) {
            super(builder);
            this.repetitions = builder.repetitions;
            this.period = builder.period;
            this.recipient = builder.recipient;
        }

        /**
         * <p>
         * Indicates the number of times the requested action should occur.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getRepetitions() {
            return repetitions;
        }

        /**
         * <p>
         * Over what time-period is fulfillment sought.
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
         * For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getRecipient() {
            return recipient;
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
                    accept(repetitions, "repetitions", visitor);
                    accept(period, "period", visitor);
                    accept(recipient, "recipient", visitor, Reference.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private PositiveInt repetitions;
            private Period period;
            private List<Reference> recipient = new ArrayList<>();

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
             * Indicates the number of times the requested action should occur.
             * </p>
             * 
             * @param repetitions
             *     How many times to repeat
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder repetitions(PositiveInt repetitions) {
                this.repetitions = repetitions;
                return this;
            }

            /**
             * <p>
             * Over what time-period is fulfillment sought.
             * </p>
             * 
             * @param period
             *     When fulfillment sought
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
             * For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?
             * </p>
             * 
             * @param recipient
             *     For whom is fulfillment sought?
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder recipient(Reference... recipient) {
                for (Reference value : recipient) {
                    this.recipient.add(value);
                }
                return this;
            }

            /**
             * <p>
             * For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?
             * </p>
             * 
             * @param recipient
             *     For whom is fulfillment sought?
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder recipient(Collection<Reference> recipient) {
                this.recipient.addAll(recipient);
                return this;
            }

            @Override
            public Restriction build() {
                return new Restriction(this);
            }

            private static Builder from(Restriction restriction) {
                Builder builder = new Builder();
                builder.id = restriction.id;
                builder.extension.addAll(restriction.extension);
                builder.modifierExtension.addAll(restriction.modifierExtension);
                builder.repetitions = restriction.repetitions;
                builder.period = restriction.period;
                builder.recipient.addAll(restriction.recipient);
                return builder;
            }
        }
    }

    /**
     * <p>
     * Additional information that may be needed in the execution of the task.
     * </p>
     */
    public static class Input extends BackboneElement {
        private final CodeableConcept type;
        private final Element value;

        private Input(Builder builder) {
            super(builder);
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
            this.value = ValidationSupport.requireChoiceElement(builder.value, "value", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class);
        }

        /**
         * <p>
         * A code or description indicating how the input is intended to be used as part of the task execution.
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
         * The value of the input parameter as a basic type.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getValue() {
            return value;
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
                    accept(value, "value", visitor, true);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept type, Element value) {
            return new Builder(type, value);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;
            private final Element value;

            private Builder(CodeableConcept type, Element value) {
                super();
                this.type = type;
                this.value = value;
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

            @Override
            public Input build() {
                return new Input(this);
            }

            private static Builder from(Input input) {
                Builder builder = new Builder(input.type, input.value);
                builder.id = input.id;
                builder.extension.addAll(input.extension);
                builder.modifierExtension.addAll(input.modifierExtension);
                return builder;
            }
        }
    }

    /**
     * <p>
     * Outputs produced by the Task.
     * </p>
     */
    public static class Output extends BackboneElement {
        private final CodeableConcept type;
        private final Element value;

        private Output(Builder builder) {
            super(builder);
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
            this.value = ValidationSupport.requireChoiceElement(builder.value, "value", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class);
        }

        /**
         * <p>
         * The name of the Output parameter.
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
         * The value of the Output parameter as a basic type.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getValue() {
            return value;
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
                    accept(value, "value", visitor, true);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept type, Element value) {
            return new Builder(type, value);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;
            private final Element value;

            private Builder(CodeableConcept type, Element value) {
                super();
                this.type = type;
                this.value = value;
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

            @Override
            public Output build() {
                return new Output(this);
            }

            private static Builder from(Output output) {
                Builder builder = new Builder(output.type, output.value);
                builder.id = output.id;
                builder.extension.addAll(output.extension);
                builder.modifierExtension.addAll(output.modifierExtension);
                return builder;
            }
        }
    }
}
