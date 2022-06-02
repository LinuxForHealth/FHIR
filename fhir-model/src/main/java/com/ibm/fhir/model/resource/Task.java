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
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Contributor;
import com.ibm.fhir.model.type.Count;
import com.ibm.fhir.model.type.DataRequirement;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Distance;
import com.ibm.fhir.model.type.Dosage;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Expression;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Oid;
import com.ibm.fhir.model.type.ParameterDefinition;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.SampledData;
import com.ibm.fhir.model.type.Signature;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.TriggerDefinition;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.TaskIntent;
import com.ibm.fhir.model.type.code.TaskPriority;
import com.ibm.fhir.model.type.code.TaskStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A task to be performed.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "inv-1",
    level = "Rule",
    location = "(base)",
    description = "Last modified date must be greater than or equal to authored-on date.",
    expression = "lastModified.exists().not() or authoredOn.exists().not() or lastModified >= authoredOn",
    source = "http://hl7.org/fhir/StructureDefinition/Task"
)
@Constraint(
    id = "task-2",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/performer-role",
    expression = "performerType.exists() implies (performerType.all(memberOf('http://hl7.org/fhir/ValueSet/performer-role', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/Task",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Task extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    private final Canonical instantiatesCanonical;
    @Summary
    private final Uri instantiatesUri;
    @Summary
    private final List<Reference> basedOn;
    @Summary
    private final Identifier groupIdentifier;
    @Summary
    @ReferenceTarget({ "Task" })
    private final List<Reference> partOf;
    @Summary
    @Binding(
        bindingName = "TaskStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The current status of the task.",
        valueSet = "http://hl7.org/fhir/ValueSet/task-status|4.3.0-cibuild"
    )
    @Required
    private final TaskStatus status;
    @Summary
    @Binding(
        bindingName = "TaskStatusReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes to identify the reason for current status.  These will typically be specific to a particular workflow."
    )
    private final CodeableConcept statusReason;
    @Summary
    @Binding(
        bindingName = "TaskBusinessStatus",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The domain-specific business-contextual sub-state of the task.  For example: \"Blood drawn\", \"IV inserted\", \"Awaiting physician signature\", etc."
    )
    private final CodeableConcept businessStatus;
    @Summary
    @Binding(
        bindingName = "TaskIntent",
        strength = BindingStrength.Value.REQUIRED,
        description = "Distinguishes whether the task is a proposal, plan or full order.",
        valueSet = "http://hl7.org/fhir/ValueSet/task-intent|4.3.0-cibuild"
    )
    @Required
    private final TaskIntent intent;
    @Binding(
        bindingName = "TaskPriority",
        strength = BindingStrength.Value.REQUIRED,
        description = "The priority of a task (may affect service level applied to the task).",
        valueSet = "http://hl7.org/fhir/ValueSet/request-priority|4.3.0-cibuild"
    )
    private final TaskPriority priority;
    @Summary
    @Binding(
        bindingName = "TaskCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes to identify what the task involves.  These will typically be specific to a particular workflow.",
        valueSet = "http://hl7.org/fhir/ValueSet/task-code"
    )
    private final CodeableConcept code;
    @Summary
    private final String description;
    @Summary
    private final Reference focus;
    @Summary
    private final Reference _for;
    @Summary
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    private final Period executionPeriod;
    private final DateTime authoredOn;
    @Summary
    private final DateTime lastModified;
    @Summary
    @ReferenceTarget({ "Device", "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson" })
    private final Reference requester;
    @Binding(
        bindingName = "TaskPerformerType",
        strength = BindingStrength.Value.PREFERRED,
        description = "The type(s) of task performers allowed.",
        valueSet = "http://hl7.org/fhir/ValueSet/performer-role"
    )
    private final List<CodeableConcept> performerType;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "CareTeam", "HealthcareService", "Patient", "Device", "RelatedPerson" })
    private final Reference owner;
    @Summary
    @ReferenceTarget({ "Location" })
    private final Reference location;
    @Binding(
        bindingName = "TaskReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Indicates why the task is needed.  E.g. Suspended because patient admitted to hospital."
    )
    private final CodeableConcept reasonCode;
    private final Reference reasonReference;
    @ReferenceTarget({ "Coverage", "ClaimResponse" })
    private final List<Reference> insurance;
    private final List<Annotation> note;
    @ReferenceTarget({ "Provenance" })
    private final List<Reference> relevantHistory;
    private final Restriction restriction;
    private final List<Input> input;
    private final List<Output> output;

    private Task(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiatesCanonical = builder.instantiatesCanonical;
        instantiatesUri = builder.instantiatesUri;
        basedOn = Collections.unmodifiableList(builder.basedOn);
        groupIdentifier = builder.groupIdentifier;
        partOf = Collections.unmodifiableList(builder.partOf);
        status = builder.status;
        statusReason = builder.statusReason;
        businessStatus = builder.businessStatus;
        intent = builder.intent;
        priority = builder.priority;
        code = builder.code;
        description = builder.description;
        focus = builder.focus;
        _for = builder._for;
        encounter = builder.encounter;
        executionPeriod = builder.executionPeriod;
        authoredOn = builder.authoredOn;
        lastModified = builder.lastModified;
        requester = builder.requester;
        performerType = Collections.unmodifiableList(builder.performerType);
        owner = builder.owner;
        location = builder.location;
        reasonCode = builder.reasonCode;
        reasonReference = builder.reasonReference;
        insurance = Collections.unmodifiableList(builder.insurance);
        note = Collections.unmodifiableList(builder.note);
        relevantHistory = Collections.unmodifiableList(builder.relevantHistory);
        restriction = builder.restriction;
        input = Collections.unmodifiableList(builder.input);
        output = Collections.unmodifiableList(builder.output);
    }

    /**
     * The business identifier for this task.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or 
     * in part by this Task.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * The URL pointing to an *externally* maintained protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this Task.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * BasedOn refers to a higher-level authorization that triggered the creation of the task. It references a "request" 
     * resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the 
     * "request" resource the task is seeking to fulfill. This latter resource is referenced by FocusOn. For example, based 
     * on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen 
     * from a patient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * An identifier that links together multiple tasks and other requests that were created in the same context.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getGroupIdentifier() {
        return groupIdentifier;
    }

    /**
     * Task that this particular task is part of.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * The current status of the task.
     * 
     * @return
     *     An immutable object of type {@link TaskStatus} that is non-null.
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * An explanation as to why this task is held, failed, was refused, etc.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatusReason() {
        return statusReason;
    }

    /**
     * Contains business-specific nuances of the business state.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getBusinessStatus() {
        return businessStatus;
    }

    /**
     * Indicates the "level" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, 
     * an actionable task, etc.
     * 
     * @return
     *     An immutable object of type {@link TaskIntent} that is non-null.
     */
    public TaskIntent getIntent() {
        return intent;
    }

    /**
     * Indicates how quickly the Task should be addressed with respect to other requests.
     * 
     * @return
     *     An immutable object of type {@link TaskPriority} that may be null.
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * A name or code (or both) briefly describing what the task involves.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * A free-text description of what is to be performed.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The request being actioned or the resource being manipulated by this task.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getFocus() {
        return focus;
    }

    /**
     * The entity who benefits from the performance of the service specified in the task (e.g., the patient).
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getFor() {
        return _for;
    }

    /**
     * The healthcare event (e.g. a patient and healthcare provider interaction) during which this task was created.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * Identifies the time action was first taken against the task (start) and/or the time final action was taken against the 
     * task prior to marking it as completed (end).
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * The date and time this task was created.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getAuthoredOn() {
        return authoredOn;
    }

    /**
     * The date and time of last modification to this task.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getLastModified() {
        return lastModified;
    }

    /**
     * The creator of the task.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRequester() {
        return requester;
    }

    /**
     * The kind of participant that should perform the task.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getPerformerType() {
        return performerType;
    }

    /**
     * Individual organization or Device currently responsible for task execution.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getOwner() {
        return owner;
    }

    /**
     * Principal physical location where the this task is performed.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getLocation() {
        return location;
    }

    /**
     * A description or code indicating why this task needs to be performed.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getReasonCode() {
        return reasonCode;
    }

    /**
     * A resource reference indicating why this task needs to be performed.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getReasonReference() {
        return reasonReference;
    }

    /**
     * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getInsurance() {
        return insurance;
    }

    /**
     * Free-text information captured about the task as it progresses.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Links to Provenance records for past versions of this Task that identify key state transitions or updates that are 
     * likely to be relevant to a user looking at the current version of the task.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getRelevantHistory() {
        return relevantHistory;
    }

    /**
     * If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be 
     * actioned), this element identifies any limitations on what parts of the referenced request should be actioned.
     * 
     * @return
     *     An immutable object of type {@link Restriction} that may be null.
     */
    public Restriction getRestriction() {
        return restriction;
    }

    /**
     * Additional information that may be needed in the execution of the task.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Input} that may be empty.
     */
    public List<Input> getInput() {
        return input;
    }

    /**
     * Outputs produced by the Task.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Output} that may be empty.
     */
    public List<Output> getOutput() {
        return output;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (instantiatesCanonical != null) || 
            (instantiatesUri != null) || 
            !basedOn.isEmpty() || 
            (groupIdentifier != null) || 
            !partOf.isEmpty() || 
            (status != null) || 
            (statusReason != null) || 
            (businessStatus != null) || 
            (intent != null) || 
            (priority != null) || 
            (code != null) || 
            (description != null) || 
            (focus != null) || 
            (_for != null) || 
            (encounter != null) || 
            (executionPeriod != null) || 
            (authoredOn != null) || 
            (lastModified != null) || 
            (requester != null) || 
            !performerType.isEmpty() || 
            (owner != null) || 
            (location != null) || 
            (reasonCode != null) || 
            (reasonReference != null) || 
            !insurance.isEmpty() || 
            !note.isEmpty() || 
            !relevantHistory.isEmpty() || 
            (restriction != null) || 
            !input.isEmpty() || 
            !output.isEmpty();
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
        Task other = (Task) obj;
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
            Objects.equals(groupIdentifier, other.groupIdentifier) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(businessStatus, other.businessStatus) && 
            Objects.equals(intent, other.intent) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(code, other.code) && 
            Objects.equals(description, other.description) && 
            Objects.equals(focus, other.focus) && 
            Objects.equals(_for, other._for) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(executionPeriod, other.executionPeriod) && 
            Objects.equals(authoredOn, other.authoredOn) && 
            Objects.equals(lastModified, other.lastModified) && 
            Objects.equals(requester, other.requester) && 
            Objects.equals(performerType, other.performerType) && 
            Objects.equals(owner, other.owner) && 
            Objects.equals(location, other.location) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(insurance, other.insurance) && 
            Objects.equals(note, other.note) && 
            Objects.equals(relevantHistory, other.relevantHistory) && 
            Objects.equals(restriction, other.restriction) && 
            Objects.equals(input, other.input) && 
            Objects.equals(output, other.output);
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
                groupIdentifier, 
                partOf, 
                status, 
                statusReason, 
                businessStatus, 
                intent, 
                priority, 
                code, 
                description, 
                focus, 
                _for, 
                encounter, 
                executionPeriod, 
                authoredOn, 
                lastModified, 
                requester, 
                performerType, 
                owner, 
                location, 
                reasonCode, 
                reasonReference, 
                insurance, 
                note, 
                relevantHistory, 
                restriction, 
                input, 
                output);
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
        private Canonical instantiatesCanonical;
        private Uri instantiatesUri;
        private List<Reference> basedOn = new ArrayList<>();
        private Identifier groupIdentifier;
        private List<Reference> partOf = new ArrayList<>();
        private TaskStatus status;
        private CodeableConcept statusReason;
        private CodeableConcept businessStatus;
        private TaskIntent intent;
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
         * The business identifier for this task.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Task Instance Identifier
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
         * The business identifier for this task.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Task Instance Identifier
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
         * The URL pointing to a *FHIR*-defined protocol, guideline, orderset or other definition that is adhered to in whole or 
         * in part by this Task.
         * 
         * @param instantiatesCanonical
         *     Formal definition of task
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesCanonical(Canonical instantiatesCanonical) {
            this.instantiatesCanonical = instantiatesCanonical;
            return this;
        }

        /**
         * The URL pointing to an *externally* maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this Task.
         * 
         * @param instantiatesUri
         *     Formal definition of task
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesUri(Uri instantiatesUri) {
            this.instantiatesUri = instantiatesUri;
            return this;
        }

        /**
         * BasedOn refers to a higher-level authorization that triggered the creation of the task. It references a "request" 
         * resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the 
         * "request" resource the task is seeking to fulfill. This latter resource is referenced by FocusOn. For example, based 
         * on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen 
         * from a patient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param basedOn
         *     Request fulfilled by this task
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
         * BasedOn refers to a higher-level authorization that triggered the creation of the task. It references a "request" 
         * resource such as a ServiceRequest, MedicationRequest, ServiceRequest, CarePlan, etc. which is distinct from the 
         * "request" resource the task is seeking to fulfill. This latter resource is referenced by FocusOn. For example, based 
         * on a ServiceRequest (= BasedOn), a task is created to fulfill a procedureRequest ( = FocusOn ) to collect a specimen 
         * from a patient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param basedOn
         *     Request fulfilled by this task
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * An identifier that links together multiple tasks and other requests that were created in the same context.
         * 
         * @param groupIdentifier
         *     Requisition or grouper id
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder groupIdentifier(Identifier groupIdentifier) {
            this.groupIdentifier = groupIdentifier;
            return this;
        }

        /**
         * Task that this particular task is part of.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Task}</li>
         * </ul>
         * 
         * @param partOf
         *     Composite task
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
         * Task that this particular task is part of.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Task}</li>
         * </ul>
         * 
         * @param partOf
         *     Composite task
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
         * The current status of the task.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     draft | requested | received | accepted | +
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        /**
         * An explanation as to why this task is held, failed, was refused, etc.
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
         * Contains business-specific nuances of the business state.
         * 
         * @param businessStatus
         *     E.g. "Specimen collected", "IV prepped"
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder businessStatus(CodeableConcept businessStatus) {
            this.businessStatus = businessStatus;
            return this;
        }

        /**
         * Indicates the "level" of actionability associated with the Task, i.e. i+R[9]Cs this a proposed task, a planned task, 
         * an actionable task, etc.
         * 
         * <p>This element is required.
         * 
         * @param intent
         *     unknown | proposal | plan | order | original-order | reflex-order | filler-order | instance-order | option
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder intent(TaskIntent intent) {
            this.intent = intent;
            return this;
        }

        /**
         * Indicates how quickly the Task should be addressed with respect to other requests.
         * 
         * @param priority
         *     routine | urgent | asap | stat
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priority(TaskPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * A name or code (or both) briefly describing what the task involves.
         * 
         * @param code
         *     Task Type
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * Convenience method for setting {@code description}.
         * 
         * @param description
         *     Human-readable explanation of task
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
         * A free-text description of what is to be performed.
         * 
         * @param description
         *     Human-readable explanation of task
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * The request being actioned or the resource being manipulated by this task.
         * 
         * @param focus
         *     What task is acting on
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder focus(Reference focus) {
            this.focus = focus;
            return this;
        }

        /**
         * The entity who benefits from the performance of the service specified in the task (e.g., the patient).
         * 
         * @param _for
         *     Beneficiary of the Task
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder _for(Reference _for) {
            this._for = _for;
            return this;
        }

        /**
         * The healthcare event (e.g. a patient and healthcare provider interaction) during which this task was created.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param encounter
         *     Healthcare event during which this task originated
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * Identifies the time action was first taken against the task (start) and/or the time final action was taken against the 
         * task prior to marking it as completed (end).
         * 
         * @param executionPeriod
         *     Start and end time of execution
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder executionPeriod(Period executionPeriod) {
            this.executionPeriod = executionPeriod;
            return this;
        }

        /**
         * The date and time this task was created.
         * 
         * @param authoredOn
         *     Task Creation Date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authoredOn(DateTime authoredOn) {
            this.authoredOn = authoredOn;
            return this;
        }

        /**
         * The date and time of last modification to this task.
         * 
         * @param lastModified
         *     Task Last Modified Date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastModified(DateTime lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        /**
         * The creator of the task.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param requester
         *     Who is asking for task to be done
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder requester(Reference requester) {
            this.requester = requester;
            return this;
        }

        /**
         * The kind of participant that should perform the task.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param performerType
         *     Requested performer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performerType(CodeableConcept... performerType) {
            for (CodeableConcept value : performerType) {
                this.performerType.add(value);
            }
            return this;
        }

        /**
         * The kind of participant that should perform the task.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param performerType
         *     Requested performer
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder performerType(Collection<CodeableConcept> performerType) {
            this.performerType = new ArrayList<>(performerType);
            return this;
        }

        /**
         * Individual organization or Device currently responsible for task execution.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * <li>{@link CareTeam}</li>
         * <li>{@link HealthcareService}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Device}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param owner
         *     Responsible individual
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder owner(Reference owner) {
            this.owner = owner;
            return this;
        }

        /**
         * Principal physical location where the this task is performed.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param location
         *     Where task occurs
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(Reference location) {
            this.location = location;
            return this;
        }

        /**
         * A description or code indicating why this task needs to be performed.
         * 
         * @param reasonCode
         *     Why task is needed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(CodeableConcept reasonCode) {
            this.reasonCode = reasonCode;
            return this;
        }

        /**
         * A resource reference indicating why this task needs to be performed.
         * 
         * @param reasonReference
         *     Why task is needed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Reference reasonReference) {
            this.reasonReference = reasonReference;
            return this;
        }

        /**
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Coverage}</li>
         * <li>{@link ClaimResponse}</li>
         * </ul>
         * 
         * @param insurance
         *     Associated insurance coverage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Reference... insurance) {
            for (Reference value : insurance) {
                this.insurance.add(value);
            }
            return this;
        }

        /**
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be relevant to the Task.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Coverage}</li>
         * <li>{@link ClaimResponse}</li>
         * </ul>
         * 
         * @param insurance
         *     Associated insurance coverage
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder insurance(Collection<Reference> insurance) {
            this.insurance = new ArrayList<>(insurance);
            return this;
        }

        /**
         * Free-text information captured about the task as it progresses.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the task
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
         * Free-text information captured about the task as it progresses.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the task
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
         * Links to Provenance records for past versions of this Task that identify key state transitions or updates that are 
         * likely to be relevant to a user looking at the current version of the task.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Provenance}</li>
         * </ul>
         * 
         * @param relevantHistory
         *     Key events in history of the Task
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relevantHistory(Reference... relevantHistory) {
            for (Reference value : relevantHistory) {
                this.relevantHistory.add(value);
            }
            return this;
        }

        /**
         * Links to Provenance records for past versions of this Task that identify key state transitions or updates that are 
         * likely to be relevant to a user looking at the current version of the task.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Provenance}</li>
         * </ul>
         * 
         * @param relevantHistory
         *     Key events in history of the Task
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder relevantHistory(Collection<Reference> relevantHistory) {
            this.relevantHistory = new ArrayList<>(relevantHistory);
            return this;
        }

        /**
         * If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be 
         * actioned), this element identifies any limitations on what parts of the referenced request should be actioned.
         * 
         * @param restriction
         *     Constraints on fulfillment tasks
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder restriction(Restriction restriction) {
            this.restriction = restriction;
            return this;
        }

        /**
         * Additional information that may be needed in the execution of the task.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param input
         *     Information used to perform task
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder input(Input... input) {
            for (Input value : input) {
                this.input.add(value);
            }
            return this;
        }

        /**
         * Additional information that may be needed in the execution of the task.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param input
         *     Information used to perform task
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder input(Collection<Input> input) {
            this.input = new ArrayList<>(input);
            return this;
        }

        /**
         * Outputs produced by the Task.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param output
         *     Information produced as part of task
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder output(Output... output) {
            for (Output value : output) {
                this.output.add(value);
            }
            return this;
        }

        /**
         * Outputs produced by the Task.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param output
         *     Information produced as part of task
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder output(Collection<Output> output) {
            this.output = new ArrayList<>(output);
            return this;
        }

        /**
         * Build the {@link Task}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>intent</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Task}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Task per the base specification
         */
        @Override
        public Task build() {
            Task task = new Task(this);
            if (validating) {
                validate(task);
            }
            return task;
        }

        protected void validate(Task task) {
            super.validate(task);
            ValidationSupport.checkList(task.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(task.basedOn, "basedOn", Reference.class);
            ValidationSupport.checkList(task.partOf, "partOf", Reference.class);
            ValidationSupport.requireNonNull(task.status, "status");
            ValidationSupport.requireNonNull(task.intent, "intent");
            ValidationSupport.checkList(task.performerType, "performerType", CodeableConcept.class);
            ValidationSupport.checkList(task.insurance, "insurance", Reference.class);
            ValidationSupport.checkList(task.note, "note", Annotation.class);
            ValidationSupport.checkList(task.relevantHistory, "relevantHistory", Reference.class);
            ValidationSupport.checkList(task.input, "input", Input.class);
            ValidationSupport.checkList(task.output, "output", Output.class);
            ValidationSupport.checkReferenceType(task.partOf, "partOf", "Task");
            ValidationSupport.checkReferenceType(task.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(task.requester, "requester", "Device", "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson");
            ValidationSupport.checkReferenceType(task.owner, "owner", "Practitioner", "PractitionerRole", "Organization", "CareTeam", "HealthcareService", "Patient", "Device", "RelatedPerson");
            ValidationSupport.checkReferenceType(task.location, "location", "Location");
            ValidationSupport.checkReferenceType(task.insurance, "insurance", "Coverage", "ClaimResponse");
            ValidationSupport.checkReferenceType(task.relevantHistory, "relevantHistory", "Provenance");
        }

        protected Builder from(Task task) {
            super.from(task);
            identifier.addAll(task.identifier);
            instantiatesCanonical = task.instantiatesCanonical;
            instantiatesUri = task.instantiatesUri;
            basedOn.addAll(task.basedOn);
            groupIdentifier = task.groupIdentifier;
            partOf.addAll(task.partOf);
            status = task.status;
            statusReason = task.statusReason;
            businessStatus = task.businessStatus;
            intent = task.intent;
            priority = task.priority;
            code = task.code;
            description = task.description;
            focus = task.focus;
            _for = task._for;
            encounter = task.encounter;
            executionPeriod = task.executionPeriod;
            authoredOn = task.authoredOn;
            lastModified = task.lastModified;
            requester = task.requester;
            performerType.addAll(task.performerType);
            owner = task.owner;
            location = task.location;
            reasonCode = task.reasonCode;
            reasonReference = task.reasonReference;
            insurance.addAll(task.insurance);
            note.addAll(task.note);
            relevantHistory.addAll(task.relevantHistory);
            restriction = task.restriction;
            input.addAll(task.input);
            output.addAll(task.output);
            return this;
        }
    }

    /**
     * If the Task.focus is a request resource and the task is seeking fulfillment (i.e. is asking for the request to be 
     * actioned), this element identifies any limitations on what parts of the referenced request should be actioned.
     */
    public static class Restriction extends BackboneElement {
        private final PositiveInt repetitions;
        private final Period period;
        @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Group", "Organization" })
        private final List<Reference> recipient;

        private Restriction(Builder builder) {
            super(builder);
            repetitions = builder.repetitions;
            period = builder.period;
            recipient = Collections.unmodifiableList(builder.recipient);
        }

        /**
         * Indicates the number of times the requested action should occur.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that may be null.
         */
        public PositiveInt getRepetitions() {
            return repetitions;
        }

        /**
         * Over what time-period is fulfillment sought.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getRecipient() {
            return recipient;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (repetitions != null) || 
                (period != null) || 
                !recipient.isEmpty();
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
                    accept(repetitions, "repetitions", visitor);
                    accept(period, "period", visitor);
                    accept(recipient, "recipient", visitor, Reference.class);
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
            Restriction other = (Restriction) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(repetitions, other.repetitions) && 
                Objects.equals(period, other.period) && 
                Objects.equals(recipient, other.recipient);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    repetitions, 
                    period, 
                    recipient);
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
            private PositiveInt repetitions;
            private Period period;
            private List<Reference> recipient = new ArrayList<>();

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
             * Indicates the number of times the requested action should occur.
             * 
             * @param repetitions
             *     How many times to repeat
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder repetitions(PositiveInt repetitions) {
                this.repetitions = repetitions;
                return this;
            }

            /**
             * Over what time-period is fulfillment sought.
             * 
             * @param period
             *     When fulfillment sought
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Patient}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Group}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param recipient
             *     For whom is fulfillment sought?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder recipient(Reference... recipient) {
                for (Reference value : recipient) {
                    this.recipient.add(value);
                }
                return this;
            }

            /**
             * For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Patient}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Group}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param recipient
             *     For whom is fulfillment sought?
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder recipient(Collection<Reference> recipient) {
                this.recipient = new ArrayList<>(recipient);
                return this;
            }

            /**
             * Build the {@link Restriction}
             * 
             * @return
             *     An immutable object of type {@link Restriction}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Restriction per the base specification
             */
            @Override
            public Restriction build() {
                Restriction restriction = new Restriction(this);
                if (validating) {
                    validate(restriction);
                }
                return restriction;
            }

            protected void validate(Restriction restriction) {
                super.validate(restriction);
                ValidationSupport.checkList(restriction.recipient, "recipient", Reference.class);
                ValidationSupport.checkReferenceType(restriction.recipient, "recipient", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Group", "Organization");
                ValidationSupport.requireValueOrChildren(restriction);
            }

            protected Builder from(Restriction restriction) {
                super.from(restriction);
                repetitions = restriction.repetitions;
                period = restriction.period;
                recipient.addAll(restriction.recipient);
                return this;
            }
        }
    }

    /**
     * Additional information that may be needed in the execution of the task.
     */
    public static class Input extends BackboneElement {
        @Binding(
            bindingName = "TaskInputParameterType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes to identify types of input parameters.  These will typically be specific to a particular workflow.  E.g. \"Comparison source\", \"Applicable consent\", \"Concomitent Medications\", etc."
        )
        @Required
        private final CodeableConcept type;
        @Choice({ Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class })
        @Required
        private final Element value;

        private Input(Builder builder) {
            super(builder);
            type = builder.type;
            value = builder.value;
        }

        /**
         * A code or description indicating how the input is intended to be used as part of the task execution.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The value of the input parameter as a basic type.
         * 
         * @return
         *     An immutable object of type {@link Base64Binary}, {@link Boolean}, {@link Canonical}, {@link Code}, {@link Date}, 
         *     {@link DateTime}, {@link Decimal}, {@link Id}, {@link Instant}, {@link Integer}, {@link Markdown}, {@link Oid}, {@link 
         *     PositiveInt}, {@link String}, {@link Time}, {@link UnsignedInt}, {@link Uri}, {@link Url}, {@link Uuid}, {@link 
         *     Address}, {@link Age}, {@link Annotation}, {@link Attachment}, {@link CodeableConcept}, {@link Coding}, {@link 
         *     ContactPoint}, {@link Count}, {@link Distance}, {@link Duration}, {@link HumanName}, {@link Identifier}, {@link 
         *     Money}, {@link Period}, {@link Quantity}, {@link Range}, {@link Ratio}, {@link Reference}, {@link SampledData}, {@link 
         *     Signature}, {@link Timing}, {@link ContactDetail}, {@link Contributor}, {@link DataRequirement}, {@link Expression}, 
         *     {@link ParameterDefinition}, {@link RelatedArtifact}, {@link TriggerDefinition}, {@link UsageContext}, {@link Dosage} 
         *     or {@link Meta} that is non-null.
         */
        public Element getValue() {
            return value;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (value != null);
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
                    accept(value, "value", visitor);
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
            Input other = (Input) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    value);
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
            private Element value;

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
             * A code or description indicating how the input is intended to be used as part of the task execution.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Label for the input
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Boolean.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Content to use in performing the task
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.Boolean value) {
                this.value = (value == null) ? null : Boolean.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Date.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Content to use in performing the task
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.LocalDate value) {
                this.value = (value == null) ? null : Date.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Instant.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Content to use in performing the task
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.ZonedDateTime value) {
                this.value = (value == null) ? null : Instant.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Integer.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Content to use in performing the task
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.Integer value) {
                this.value = (value == null) ? null : Integer.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type String.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Content to use in performing the task
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.String value) {
                this.value = (value == null) ? null : String.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Time.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Content to use in performing the task
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.LocalTime value) {
                this.value = (value == null) ? null : Time.of(value);
                return this;
            }

            /**
             * The value of the input parameter as a basic type.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Base64Binary}</li>
             * <li>{@link Boolean}</li>
             * <li>{@link Canonical}</li>
             * <li>{@link Code}</li>
             * <li>{@link Date}</li>
             * <li>{@link DateTime}</li>
             * <li>{@link Decimal}</li>
             * <li>{@link Id}</li>
             * <li>{@link Instant}</li>
             * <li>{@link Integer}</li>
             * <li>{@link Markdown}</li>
             * <li>{@link Oid}</li>
             * <li>{@link PositiveInt}</li>
             * <li>{@link String}</li>
             * <li>{@link Time}</li>
             * <li>{@link UnsignedInt}</li>
             * <li>{@link Uri}</li>
             * <li>{@link Url}</li>
             * <li>{@link Uuid}</li>
             * <li>{@link Address}</li>
             * <li>{@link Age}</li>
             * <li>{@link Annotation}</li>
             * <li>{@link Attachment}</li>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Coding}</li>
             * <li>{@link ContactPoint}</li>
             * <li>{@link Count}</li>
             * <li>{@link Distance}</li>
             * <li>{@link Duration}</li>
             * <li>{@link HumanName}</li>
             * <li>{@link Identifier}</li>
             * <li>{@link Money}</li>
             * <li>{@link Period}</li>
             * <li>{@link Quantity}</li>
             * <li>{@link Range}</li>
             * <li>{@link Ratio}</li>
             * <li>{@link Reference}</li>
             * <li>{@link SampledData}</li>
             * <li>{@link Signature}</li>
             * <li>{@link Timing}</li>
             * <li>{@link ContactDetail}</li>
             * <li>{@link Contributor}</li>
             * <li>{@link DataRequirement}</li>
             * <li>{@link Expression}</li>
             * <li>{@link ParameterDefinition}</li>
             * <li>{@link RelatedArtifact}</li>
             * <li>{@link TriggerDefinition}</li>
             * <li>{@link UsageContext}</li>
             * <li>{@link Dosage}</li>
             * <li>{@link Meta}</li>
             * </ul>
             * 
             * @param value
             *     Content to use in performing the task
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Build the {@link Input}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>value</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Input}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Input per the base specification
             */
            @Override
            public Input build() {
                Input input = new Input(this);
                if (validating) {
                    validate(input);
                }
                return input;
            }

            protected void validate(Input input) {
                super.validate(input);
                ValidationSupport.requireNonNull(input.type, "type");
                ValidationSupport.requireChoiceElement(input.value, "value", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class);
                ValidationSupport.requireValueOrChildren(input);
            }

            protected Builder from(Input input) {
                super.from(input);
                type = input.type;
                value = input.value;
                return this;
            }
        }
    }

    /**
     * Outputs produced by the Task.
     */
    public static class Output extends BackboneElement {
        @Binding(
            bindingName = "TaskOutputParameterType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes to identify types of input parameters.  These will typically be specific to a particular workflow.  E.g. \"Identified issues\", \"Preliminary results\", \"Filler order\", \"Final results\", etc."
        )
        @Required
        private final CodeableConcept type;
        @Choice({ Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class })
        @Required
        private final Element value;

        private Output(Builder builder) {
            super(builder);
            type = builder.type;
            value = builder.value;
        }

        /**
         * The name of the Output parameter.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The value of the Output parameter as a basic type.
         * 
         * @return
         *     An immutable object of type {@link Base64Binary}, {@link Boolean}, {@link Canonical}, {@link Code}, {@link Date}, 
         *     {@link DateTime}, {@link Decimal}, {@link Id}, {@link Instant}, {@link Integer}, {@link Markdown}, {@link Oid}, {@link 
         *     PositiveInt}, {@link String}, {@link Time}, {@link UnsignedInt}, {@link Uri}, {@link Url}, {@link Uuid}, {@link 
         *     Address}, {@link Age}, {@link Annotation}, {@link Attachment}, {@link CodeableConcept}, {@link Coding}, {@link 
         *     ContactPoint}, {@link Count}, {@link Distance}, {@link Duration}, {@link HumanName}, {@link Identifier}, {@link 
         *     Money}, {@link Period}, {@link Quantity}, {@link Range}, {@link Ratio}, {@link Reference}, {@link SampledData}, {@link 
         *     Signature}, {@link Timing}, {@link ContactDetail}, {@link Contributor}, {@link DataRequirement}, {@link Expression}, 
         *     {@link ParameterDefinition}, {@link RelatedArtifact}, {@link TriggerDefinition}, {@link UsageContext}, {@link Dosage} 
         *     or {@link Meta} that is non-null.
         */
        public Element getValue() {
            return value;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (value != null);
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
                    accept(value, "value", visitor);
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
            Output other = (Output) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    value);
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
            private Element value;

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
             * The name of the Output parameter.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Label for output
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Boolean.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Result of output
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.Boolean value) {
                this.value = (value == null) ? null : Boolean.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Date.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Result of output
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.LocalDate value) {
                this.value = (value == null) ? null : Date.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Instant.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Result of output
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.ZonedDateTime value) {
                this.value = (value == null) ? null : Instant.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Integer.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Result of output
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.Integer value) {
                this.value = (value == null) ? null : Integer.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type String.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Result of output
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.String value) {
                this.value = (value == null) ? null : String.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Time.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Result of output
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.LocalTime value) {
                this.value = (value == null) ? null : Time.of(value);
                return this;
            }

            /**
             * The value of the Output parameter as a basic type.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Base64Binary}</li>
             * <li>{@link Boolean}</li>
             * <li>{@link Canonical}</li>
             * <li>{@link Code}</li>
             * <li>{@link Date}</li>
             * <li>{@link DateTime}</li>
             * <li>{@link Decimal}</li>
             * <li>{@link Id}</li>
             * <li>{@link Instant}</li>
             * <li>{@link Integer}</li>
             * <li>{@link Markdown}</li>
             * <li>{@link Oid}</li>
             * <li>{@link PositiveInt}</li>
             * <li>{@link String}</li>
             * <li>{@link Time}</li>
             * <li>{@link UnsignedInt}</li>
             * <li>{@link Uri}</li>
             * <li>{@link Url}</li>
             * <li>{@link Uuid}</li>
             * <li>{@link Address}</li>
             * <li>{@link Age}</li>
             * <li>{@link Annotation}</li>
             * <li>{@link Attachment}</li>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Coding}</li>
             * <li>{@link ContactPoint}</li>
             * <li>{@link Count}</li>
             * <li>{@link Distance}</li>
             * <li>{@link Duration}</li>
             * <li>{@link HumanName}</li>
             * <li>{@link Identifier}</li>
             * <li>{@link Money}</li>
             * <li>{@link Period}</li>
             * <li>{@link Quantity}</li>
             * <li>{@link Range}</li>
             * <li>{@link Ratio}</li>
             * <li>{@link Reference}</li>
             * <li>{@link SampledData}</li>
             * <li>{@link Signature}</li>
             * <li>{@link Timing}</li>
             * <li>{@link ContactDetail}</li>
             * <li>{@link Contributor}</li>
             * <li>{@link DataRequirement}</li>
             * <li>{@link Expression}</li>
             * <li>{@link ParameterDefinition}</li>
             * <li>{@link RelatedArtifact}</li>
             * <li>{@link TriggerDefinition}</li>
             * <li>{@link UsageContext}</li>
             * <li>{@link Dosage}</li>
             * <li>{@link Meta}</li>
             * </ul>
             * 
             * @param value
             *     Result of output
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Build the {@link Output}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>value</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Output}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Output per the base specification
             */
            @Override
            public Output build() {
                Output output = new Output(this);
                if (validating) {
                    validate(output);
                }
                return output;
            }

            protected void validate(Output output) {
                super.validate(output);
                ValidationSupport.requireNonNull(output.type, "type");
                ValidationSupport.requireChoiceElement(output.value, "value", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class);
                ValidationSupport.requireValueOrChildren(output);
            }

            protected Builder from(Output output) {
                super.from(output);
                type = output.type;
                value = output.value;
                return this;
            }
        }
    }
}
