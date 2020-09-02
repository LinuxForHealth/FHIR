/*
 * (C) Copyright IBM Corp. 2019, 2020
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
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AuditEventAction;
import com.ibm.fhir.model.type.code.AuditEventAgentNetworkType;
import com.ibm.fhir.model.type.code.AuditEventOutcome;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion 
 * attempts and monitoring for inappropriate usage.
 */
@Constraint(
    id = "sev-1",
    level = "Rule",
    location = "AuditEvent.entity",
    description = "Either a name or a query (NOT both)",
    expression = "name.empty() or query.empty()"
)
@Constraint(
    id = "auditEvent-2",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/audit-event-type",
    expression = "type.exists() and type.memberOf('http://hl7.org/fhir/ValueSet/audit-event-type', 'extensible')"
)
@Constraint(
    id = "auditEvent-3",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/audit-event-sub-type",
    expression = "subtype.exists() implies (subtype.all(memberOf('http://hl7.org/fhir/ValueSet/audit-event-sub-type', 'extensible')))"
)
@Constraint(
    id = "auditEvent-4",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://terminology.hl7.org/ValueSet/v3-PurposeOfUse",
    expression = "purposeOfEvent.exists() implies (purposeOfEvent.all(memberOf('http://terminology.hl7.org/ValueSet/v3-PurposeOfUse', 'extensible')))"
)
@Constraint(
    id = "auditEvent-5",
    level = "Warning",
    location = "agent.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/participation-role-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/participation-role-type', 'extensible')"
)
@Constraint(
    id = "auditEvent-6",
    level = "Warning",
    location = "agent.media",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/dicm-405-mediatype",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/dicm-405-mediatype', 'extensible')"
)
@Constraint(
    id = "auditEvent-7",
    level = "Warning",
    location = "agent.purposeOfUse",
    description = "SHALL, if possible, contain a code from value set http://terminology.hl7.org/ValueSet/v3-PurposeOfUse",
    expression = "$this.memberOf('http://terminology.hl7.org/ValueSet/v3-PurposeOfUse', 'extensible')"
)
@Constraint(
    id = "auditEvent-8",
    level = "Warning",
    location = "source.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/audit-source-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/audit-source-type', 'extensible')"
)
@Constraint(
    id = "auditEvent-9",
    level = "Warning",
    location = "entity.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/audit-entity-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/audit-entity-type', 'extensible')"
)
@Constraint(
    id = "auditEvent-10",
    level = "Warning",
    location = "entity.role",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/object-role",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/object-role', 'extensible')"
)
@Constraint(
    id = "auditEvent-11",
    level = "Warning",
    location = "entity.lifecycle",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/object-lifecycle-events",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/object-lifecycle-events', 'extensible')"
)
@Constraint(
    id = "auditEvent-12",
    level = "Warning",
    location = "entity.securityLabel",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/security-labels",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/security-labels', 'extensible')"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AuditEvent extends DomainResource {
    @Summary
    @Binding(
        bindingName = "AuditEventType",
        strength = BindingStrength.ValueSet.EXTENSIBLE,
        description = "Type of event.",
        valueSet = "http://hl7.org/fhir/ValueSet/audit-event-type"
    )
    @Required
    private final Coding type;
    @Summary
    @Binding(
        bindingName = "AuditEventSubType",
        strength = BindingStrength.ValueSet.EXTENSIBLE,
        description = "Sub-type of event.",
        valueSet = "http://hl7.org/fhir/ValueSet/audit-event-sub-type"
    )
    private final List<Coding> subtype;
    @Summary
    @Binding(
        bindingName = "AuditEventAction",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "Indicator for type of action performed during the event that generated the event.",
        valueSet = "http://hl7.org/fhir/ValueSet/audit-event-action|4.0.1"
    )
    private final AuditEventAction action;
    private final Period period;
    @Summary
    @Required
    private final Instant recorded;
    @Summary
    @Binding(
        bindingName = "AuditEventOutcome",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "Indicates whether the event succeeded or failed.",
        valueSet = "http://hl7.org/fhir/ValueSet/audit-event-outcome|4.0.1"
    )
    private final AuditEventOutcome outcome;
    @Summary
    private final String outcomeDesc;
    @Summary
    @Binding(
        bindingName = "AuditPurposeOfUse",
        strength = BindingStrength.ValueSet.EXTENSIBLE,
        description = "The reason the activity took place.",
        valueSet = "http://terminology.hl7.org/ValueSet/v3-PurposeOfUse"
    )
    private final List<CodeableConcept> purposeOfEvent;
    @Required
    private final List<Agent> agent;
    @Required
    private final Source source;
    private final List<Entity> entity;

    private volatile int hashCode;

    private AuditEvent(Builder builder) {
        super(builder);
        type = ValidationSupport.requireNonNull(builder.type, "type");
        subtype = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.subtype, "subtype"));
        action = builder.action;
        period = builder.period;
        recorded = ValidationSupport.requireNonNull(builder.recorded, "recorded");
        outcome = builder.outcome;
        outcomeDesc = builder.outcomeDesc;
        purposeOfEvent = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.purposeOfEvent, "purposeOfEvent"));
        agent = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.agent, "agent"));
        source = ValidationSupport.requireNonNull(builder.source, "source");
        entity = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.entity, "entity"));
        ValidationSupport.requireChildren(this);
    }

    /**
     * Identifier for a family of the event. For example, a menu item, program, rule, policy, function code, application name 
     * or URL. It identifies the performed function.
     * 
     * @return
     *     An immutable object of type {@link Coding} that is non-null.
     */
    public Coding getType() {
        return type;
    }

    /**
     * Identifier for the category of event.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
     */
    public List<Coding> getSubtype() {
        return subtype;
    }

    /**
     * Indicator for type of action performed during the event that generated the audit.
     * 
     * @return
     *     An immutable object of type {@link AuditEventAction} that may be null.
     */
    public AuditEventAction getAction() {
        return action;
    }

    /**
     * The period during which the activity occurred.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * The time when the event was recorded.
     * 
     * @return
     *     An immutable object of type {@link Instant} that is non-null.
     */
    public Instant getRecorded() {
        return recorded;
    }

    /**
     * Indicates whether the event succeeded or failed.
     * 
     * @return
     *     An immutable object of type {@link AuditEventOutcome} that may be null.
     */
    public AuditEventOutcome getOutcome() {
        return outcome;
    }

    /**
     * A free text description of the outcome of the event.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getOutcomeDesc() {
        return outcomeDesc;
    }

    /**
     * The purposeOfUse (reason) that was used during the event being recorded.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getPurposeOfEvent() {
        return purposeOfEvent;
    }

    /**
     * An actor taking an active role in the event or activity that is logged.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Agent} that is non-empty.
     */
    public List<Agent> getAgent() {
        return agent;
    }

    /**
     * The system that is reporting the event.
     * 
     * @return
     *     An immutable object of type {@link Source} that is non-null.
     */
    public Source getSource() {
        return source;
    }

    /**
     * Specific instances of data or objects that have been accessed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Entity} that may be empty.
     */
    public List<Entity> getEntity() {
        return entity;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (type != null) || 
            !subtype.isEmpty() || 
            (action != null) || 
            (period != null) || 
            (recorded != null) || 
            (outcome != null) || 
            (outcomeDesc != null) || 
            !purposeOfEvent.isEmpty() || 
            !agent.isEmpty() || 
            (source != null) || 
            !entity.isEmpty();
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
                accept(type, "type", visitor);
                accept(subtype, "subtype", visitor, Coding.class);
                accept(action, "action", visitor);
                accept(period, "period", visitor);
                accept(recorded, "recorded", visitor);
                accept(outcome, "outcome", visitor);
                accept(outcomeDesc, "outcomeDesc", visitor);
                accept(purposeOfEvent, "purposeOfEvent", visitor, CodeableConcept.class);
                accept(agent, "agent", visitor, Agent.class);
                accept(source, "source", visitor);
                accept(entity, "entity", visitor, Entity.class);
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
        AuditEvent other = (AuditEvent) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(type, other.type) && 
            Objects.equals(subtype, other.subtype) && 
            Objects.equals(action, other.action) && 
            Objects.equals(period, other.period) && 
            Objects.equals(recorded, other.recorded) && 
            Objects.equals(outcome, other.outcome) && 
            Objects.equals(outcomeDesc, other.outcomeDesc) && 
            Objects.equals(purposeOfEvent, other.purposeOfEvent) && 
            Objects.equals(agent, other.agent) && 
            Objects.equals(source, other.source) && 
            Objects.equals(entity, other.entity);
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
                type, 
                subtype, 
                action, 
                period, 
                recorded, 
                outcome, 
                outcomeDesc, 
                purposeOfEvent, 
                agent, 
                source, 
                entity);
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
        private Coding type;
        private List<Coding> subtype = new ArrayList<>();
        private AuditEventAction action;
        private Period period;
        private Instant recorded;
        private AuditEventOutcome outcome;
        private String outcomeDesc;
        private List<CodeableConcept> purposeOfEvent = new ArrayList<>();
        private List<Agent> agent = new ArrayList<>();
        private Source source;
        private List<Entity> entity = new ArrayList<>();

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
         * Identifier for a family of the event. For example, a menu item, program, rule, policy, function code, application name 
         * or URL. It identifies the performed function.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     Type/identifier of event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(Coding type) {
            this.type = type;
            return this;
        }

        /**
         * Identifier for the category of event.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param subtype
         *     More specific type/id for the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subtype(Coding... subtype) {
            for (Coding value : subtype) {
                this.subtype.add(value);
            }
            return this;
        }

        /**
         * Identifier for the category of event.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param subtype
         *     More specific type/id for the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subtype(Collection<Coding> subtype) {
            this.subtype = new ArrayList<>(subtype);
            return this;
        }

        /**
         * Indicator for type of action performed during the event that generated the audit.
         * 
         * @param action
         *     Type of action performed during the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder action(AuditEventAction action) {
            this.action = action;
            return this;
        }

        /**
         * The period during which the activity occurred.
         * 
         * @param period
         *     When the activity occurred
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * The time when the event was recorded.
         * 
         * <p>This element is required.
         * 
         * @param recorded
         *     Time when the event was recorded
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recorded(Instant recorded) {
            this.recorded = recorded;
            return this;
        }

        /**
         * Indicates whether the event succeeded or failed.
         * 
         * @param outcome
         *     Whether the event succeeded or failed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder outcome(AuditEventOutcome outcome) {
            this.outcome = outcome;
            return this;
        }

        /**
         * A free text description of the outcome of the event.
         * 
         * @param outcomeDesc
         *     Description of the event outcome
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder outcomeDesc(String outcomeDesc) {
            this.outcomeDesc = outcomeDesc;
            return this;
        }

        /**
         * The purposeOfUse (reason) that was used during the event being recorded.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param purposeOfEvent
         *     The purposeOfUse of the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purposeOfEvent(CodeableConcept... purposeOfEvent) {
            for (CodeableConcept value : purposeOfEvent) {
                this.purposeOfEvent.add(value);
            }
            return this;
        }

        /**
         * The purposeOfUse (reason) that was used during the event being recorded.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param purposeOfEvent
         *     The purposeOfUse of the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purposeOfEvent(Collection<CodeableConcept> purposeOfEvent) {
            this.purposeOfEvent = new ArrayList<>(purposeOfEvent);
            return this;
        }

        /**
         * An actor taking an active role in the event or activity that is logged.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * <p>This element is required.
         * 
         * @param agent
         *     Actor involved in the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder agent(Agent... agent) {
            for (Agent value : agent) {
                this.agent.add(value);
            }
            return this;
        }

        /**
         * An actor taking an active role in the event or activity that is logged.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * <p>This element is required.
         * 
         * @param agent
         *     Actor involved in the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder agent(Collection<Agent> agent) {
            this.agent = new ArrayList<>(agent);
            return this;
        }

        /**
         * The system that is reporting the event.
         * 
         * <p>This element is required.
         * 
         * @param source
         *     Audit Event Reporter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Source source) {
            this.source = source;
            return this;
        }

        /**
         * Specific instances of data or objects that have been accessed.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param entity
         *     Data or objects used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder entity(Entity... entity) {
            for (Entity value : entity) {
                this.entity.add(value);
            }
            return this;
        }

        /**
         * Specific instances of data or objects that have been accessed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param entity
         *     Data or objects used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder entity(Collection<Entity> entity) {
            this.entity = new ArrayList<>(entity);
            return this;
        }

        /**
         * Build the {@link AuditEvent}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * <li>recorded</li>
         * <li>agent</li>
         * <li>source</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link AuditEvent}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid AuditEvent per the base specification
         */
        @Override
        public AuditEvent build() {
            return new AuditEvent(this);
        }

        protected Builder from(AuditEvent auditEvent) {
            super.from(auditEvent);
            type = auditEvent.type;
            subtype.addAll(auditEvent.subtype);
            action = auditEvent.action;
            period = auditEvent.period;
            recorded = auditEvent.recorded;
            outcome = auditEvent.outcome;
            outcomeDesc = auditEvent.outcomeDesc;
            purposeOfEvent.addAll(auditEvent.purposeOfEvent);
            agent.addAll(auditEvent.agent);
            source = auditEvent.source;
            entity.addAll(auditEvent.entity);
            return this;
        }
    }

    /**
     * An actor taking an active role in the event or activity that is logged.
     */
    public static class Agent extends BackboneElement {
        @Binding(
            bindingName = "AuditAgentType",
            strength = BindingStrength.ValueSet.EXTENSIBLE,
            description = "The Participation type of the agent to the event.",
            valueSet = "http://hl7.org/fhir/ValueSet/participation-role-type"
        )
        private final CodeableConcept type;
        @Binding(
            bindingName = "AuditAgentRole",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "What security role enabled the agent to participate in the event.",
            valueSet = "http://hl7.org/fhir/ValueSet/security-role-type"
        )
        private final List<CodeableConcept> role;
        @Summary
        @ReferenceTarget({ "PractitionerRole", "Practitioner", "Organization", "Device", "Patient", "RelatedPerson" })
        private final Reference who;
        private final String altId;
        private final String name;
        @Summary
        @Required
        private final Boolean requestor;
        @ReferenceTarget({ "Location" })
        private final Reference location;
        private final List<Uri> policy;
        @Binding(
            bindingName = "DICOMMediaType",
            strength = BindingStrength.ValueSet.EXTENSIBLE,
            description = "Used when the event is about exporting/importing onto media.",
            valueSet = "http://hl7.org/fhir/ValueSet/dicm-405-mediatype"
        )
        private final Coding media;
        private final Network network;
        @Binding(
            bindingName = "AuditPurposeOfUse",
            strength = BindingStrength.ValueSet.EXTENSIBLE,
            description = "The reason the activity took place.",
            valueSet = "http://terminology.hl7.org/ValueSet/v3-PurposeOfUse"
        )
        private final List<CodeableConcept> purposeOfUse;

        private volatile int hashCode;

        private Agent(Builder builder) {
            super(builder);
            type = builder.type;
            role = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.role, "role"));
            who = builder.who;
            altId = builder.altId;
            name = builder.name;
            requestor = ValidationSupport.requireNonNull(builder.requestor, "requestor");
            location = builder.location;
            policy = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.policy, "policy"));
            media = builder.media;
            network = builder.network;
            purposeOfUse = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.purposeOfUse, "purposeOfUse"));
            ValidationSupport.checkReferenceType(who, "who", "PractitionerRole", "Practitioner", "Organization", "Device", "Patient", "RelatedPerson");
            ValidationSupport.checkReferenceType(location, "location", "Location");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Specification of the participation type the user plays when performing the event.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The security role that the user was acting under, that come from local codes defined by the access control security 
         * system (e.g. RBAC, ABAC) used in the local context.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getRole() {
            return role;
        }

        /**
         * Reference to who this agent is that was involved in the event.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getWho() {
            return who;
        }

        /**
         * Alternative agent Identifier. For a human, this should be a user identifier text string from authentication system. 
         * This identifier would be one known to a common authentication system (e.g. single sign-on), if available.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getAltId() {
            return altId;
        }

        /**
         * Human-meaningful name for the agent.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that is non-null.
         */
        public Boolean getRequestor() {
            return requestor;
        }

        /**
         * Where the event occurred.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getLocation() {
            return location;
        }

        /**
         * The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple 
         * applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security 
         * token used.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
         */
        public List<Uri> getPolicy() {
            return policy;
        }

        /**
         * Type of media involved. Used when the event is about exporting/importing onto media.
         * 
         * @return
         *     An immutable object of type {@link Coding} that may be null.
         */
        public Coding getMedia() {
            return media;
        }

        /**
         * Logical network location for application activity, if the activity has a network location.
         * 
         * @return
         *     An immutable object of type {@link Network} that may be null.
         */
        public Network getNetwork() {
            return network;
        }

        /**
         * The reason (purpose of use), specific to this agent, that was used during the event being recorded.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getPurposeOfUse() {
            return purposeOfUse;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                !role.isEmpty() || 
                (who != null) || 
                (altId != null) || 
                (name != null) || 
                (requestor != null) || 
                (location != null) || 
                !policy.isEmpty() || 
                (media != null) || 
                (network != null) || 
                !purposeOfUse.isEmpty();
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
                    accept(role, "role", visitor, CodeableConcept.class);
                    accept(who, "who", visitor);
                    accept(altId, "altId", visitor);
                    accept(name, "name", visitor);
                    accept(requestor, "requestor", visitor);
                    accept(location, "location", visitor);
                    accept(policy, "policy", visitor, Uri.class);
                    accept(media, "media", visitor);
                    accept(network, "network", visitor);
                    accept(purposeOfUse, "purposeOfUse", visitor, CodeableConcept.class);
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
            Agent other = (Agent) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(role, other.role) && 
                Objects.equals(who, other.who) && 
                Objects.equals(altId, other.altId) && 
                Objects.equals(name, other.name) && 
                Objects.equals(requestor, other.requestor) && 
                Objects.equals(location, other.location) && 
                Objects.equals(policy, other.policy) && 
                Objects.equals(media, other.media) && 
                Objects.equals(network, other.network) && 
                Objects.equals(purposeOfUse, other.purposeOfUse);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    role, 
                    who, 
                    altId, 
                    name, 
                    requestor, 
                    location, 
                    policy, 
                    media, 
                    network, 
                    purposeOfUse);
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
            private List<CodeableConcept> role = new ArrayList<>();
            private Reference who;
            private String altId;
            private String name;
            private Boolean requestor;
            private Reference location;
            private List<Uri> policy = new ArrayList<>();
            private Coding media;
            private Network network;
            private List<CodeableConcept> purposeOfUse = new ArrayList<>();

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
             * Specification of the participation type the user plays when performing the event.
             * 
             * @param type
             *     How agent participated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * The security role that the user was acting under, that come from local codes defined by the access control security 
             * system (e.g. RBAC, ABAC) used in the local context.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param role
             *     Agent role in the event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder role(CodeableConcept... role) {
                for (CodeableConcept value : role) {
                    this.role.add(value);
                }
                return this;
            }

            /**
             * The security role that the user was acting under, that come from local codes defined by the access control security 
             * system (e.g. RBAC, ABAC) used in the local context.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param role
             *     Agent role in the event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder role(Collection<CodeableConcept> role) {
                this.role = new ArrayList<>(role);
                return this;
            }

            /**
             * Reference to who this agent is that was involved in the event.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link Organization}</li>
             * <li>{@link Device}</li>
             * <li>{@link Patient}</li>
             * <li>{@link RelatedPerson}</li>
             * </ul>
             * 
             * @param who
             *     Identifier of who
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder who(Reference who) {
                this.who = who;
                return this;
            }

            /**
             * Alternative agent Identifier. For a human, this should be a user identifier text string from authentication system. 
             * This identifier would be one known to a common authentication system (e.g. single sign-on), if available.
             * 
             * @param altId
             *     Alternative User identity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder altId(String altId) {
                this.altId = altId;
                return this;
            }

            /**
             * Human-meaningful name for the agent.
             * 
             * @param name
             *     Human friendly name for the agent
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
             * 
             * <p>This element is required.
             * 
             * @param requestor
             *     Whether user is initiator
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder requestor(Boolean requestor) {
                this.requestor = requestor;
                return this;
            }

            /**
             * Where the event occurred.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Location}</li>
             * </ul>
             * 
             * @param location
             *     Where
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder location(Reference location) {
                this.location = location;
                return this;
            }

            /**
             * The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple 
             * applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security 
             * token used.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param policy
             *     Policy that authorized event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder policy(Uri... policy) {
                for (Uri value : policy) {
                    this.policy.add(value);
                }
                return this;
            }

            /**
             * The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple 
             * applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security 
             * token used.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param policy
             *     Policy that authorized event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder policy(Collection<Uri> policy) {
                this.policy = new ArrayList<>(policy);
                return this;
            }

            /**
             * Type of media involved. Used when the event is about exporting/importing onto media.
             * 
             * @param media
             *     Type of media
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder media(Coding media) {
                this.media = media;
                return this;
            }

            /**
             * Logical network location for application activity, if the activity has a network location.
             * 
             * @param network
             *     Logical network location for application activity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder network(Network network) {
                this.network = network;
                return this;
            }

            /**
             * The reason (purpose of use), specific to this agent, that was used during the event being recorded.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param purposeOfUse
             *     Reason given for this user
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder purposeOfUse(CodeableConcept... purposeOfUse) {
                for (CodeableConcept value : purposeOfUse) {
                    this.purposeOfUse.add(value);
                }
                return this;
            }

            /**
             * The reason (purpose of use), specific to this agent, that was used during the event being recorded.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param purposeOfUse
             *     Reason given for this user
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder purposeOfUse(Collection<CodeableConcept> purposeOfUse) {
                this.purposeOfUse = new ArrayList<>(purposeOfUse);
                return this;
            }

            /**
             * Build the {@link Agent}
             * 
             * <p>Required elements:
             * <ul>
             * <li>requestor</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Agent}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Agent per the base specification
             */
            @Override
            public Agent build() {
                return new Agent(this);
            }

            protected Builder from(Agent agent) {
                super.from(agent);
                type = agent.type;
                role.addAll(agent.role);
                who = agent.who;
                altId = agent.altId;
                name = agent.name;
                requestor = agent.requestor;
                location = agent.location;
                policy.addAll(agent.policy);
                media = agent.media;
                network = agent.network;
                purposeOfUse.addAll(agent.purposeOfUse);
                return this;
            }
        }

        /**
         * Logical network location for application activity, if the activity has a network location.
         */
        public static class Network extends BackboneElement {
            private final String address;
            @Binding(
                bindingName = "AuditEventAgentNetworkType",
                strength = BindingStrength.ValueSet.REQUIRED,
                description = "The type of network access point of this agent in the audit event.",
                valueSet = "http://hl7.org/fhir/ValueSet/network-type|4.0.1"
            )
            private final AuditEventAgentNetworkType type;

            private volatile int hashCode;

            private Network(Builder builder) {
                super(builder);
                address = builder.address;
                type = builder.type;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * An identifier for the network access point of the user device for the audit event.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getAddress() {
                return address;
            }

            /**
             * An identifier for the type of network access point that originated the audit event.
             * 
             * @return
             *     An immutable object of type {@link AuditEventAgentNetworkType} that may be null.
             */
            public AuditEventAgentNetworkType getType() {
                return type;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (address != null) || 
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
                        accept(address, "address", visitor);
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
                Network other = (Network) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(address, other.address) && 
                    Objects.equals(type, other.type);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        address, 
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
                private String address;
                private AuditEventAgentNetworkType type;

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
                 * An identifier for the network access point of the user device for the audit event.
                 * 
                 * @param address
                 *     Identifier for the network access point of the user device
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder address(String address) {
                    this.address = address;
                    return this;
                }

                /**
                 * An identifier for the type of network access point that originated the audit event.
                 * 
                 * @param type
                 *     The type of network access point
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(AuditEventAgentNetworkType type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Build the {@link Network}
                 * 
                 * @return
                 *     An immutable object of type {@link Network}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Network per the base specification
                 */
                @Override
                public Network build() {
                    return new Network(this);
                }

                protected Builder from(Network network) {
                    super.from(network);
                    address = network.address;
                    type = network.type;
                    return this;
                }
            }
        }
    }

    /**
     * The system that is reporting the event.
     */
    public static class Source extends BackboneElement {
        private final String site;
        @Summary
        @ReferenceTarget({ "PractitionerRole", "Practitioner", "Organization", "Device", "Patient", "RelatedPerson" })
        @Required
        private final Reference observer;
        @Binding(
            bindingName = "AuditEventSourceType",
            strength = BindingStrength.ValueSet.EXTENSIBLE,
            description = "Code specifying the type of system that detected and recorded the event.",
            valueSet = "http://hl7.org/fhir/ValueSet/audit-source-type"
        )
        private final List<Coding> type;

        private volatile int hashCode;

        private Source(Builder builder) {
            super(builder);
            site = builder.site;
            observer = ValidationSupport.requireNonNull(builder.observer, "observer");
            type = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.type, "type"));
            ValidationSupport.checkReferenceType(observer, "observer", "PractitionerRole", "Practitioner", "Organization", "Device", "Patient", "RelatedPerson");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Logical source location within the healthcare enterprise network. For example, a hospital or other provider location 
         * within a multi-entity provider group.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getSite() {
            return site;
        }

        /**
         * Identifier of the source where the event was detected.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getObserver() {
            return observer;
        }

        /**
         * Code specifying the type of source where event originated.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
         */
        public List<Coding> getType() {
            return type;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (site != null) || 
                (observer != null) || 
                !type.isEmpty();
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
                    accept(site, "site", visitor);
                    accept(observer, "observer", visitor);
                    accept(type, "type", visitor, Coding.class);
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
            Source other = (Source) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(site, other.site) && 
                Objects.equals(observer, other.observer) && 
                Objects.equals(type, other.type);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    site, 
                    observer, 
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
            private String site;
            private Reference observer;
            private List<Coding> type = new ArrayList<>();

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
             * Logical source location within the healthcare enterprise network. For example, a hospital or other provider location 
             * within a multi-entity provider group.
             * 
             * @param site
             *     Logical source location within the enterprise
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder site(String site) {
                this.site = site;
                return this;
            }

            /**
             * Identifier of the source where the event was detected.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link Organization}</li>
             * <li>{@link Device}</li>
             * <li>{@link Patient}</li>
             * <li>{@link RelatedPerson}</li>
             * </ul>
             * 
             * @param observer
             *     The identity of source detecting the event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder observer(Reference observer) {
                this.observer = observer;
                return this;
            }

            /**
             * Code specifying the type of source where event originated.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param type
             *     The type of source where event originated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(Coding... type) {
                for (Coding value : type) {
                    this.type.add(value);
                }
                return this;
            }

            /**
             * Code specifying the type of source where event originated.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param type
             *     The type of source where event originated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(Collection<Coding> type) {
                this.type = new ArrayList<>(type);
                return this;
            }

            /**
             * Build the {@link Source}
             * 
             * <p>Required elements:
             * <ul>
             * <li>observer</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Source}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Source per the base specification
             */
            @Override
            public Source build() {
                return new Source(this);
            }

            protected Builder from(Source source) {
                super.from(source);
                site = source.site;
                observer = source.observer;
                type.addAll(source.type);
                return this;
            }
        }
    }

    /**
     * Specific instances of data or objects that have been accessed.
     */
    public static class Entity extends BackboneElement {
        @Summary
        private final Reference what;
        @Binding(
            bindingName = "AuditEventEntityType",
            strength = BindingStrength.ValueSet.EXTENSIBLE,
            description = "Code for the entity type involved in the audit event.",
            valueSet = "http://hl7.org/fhir/ValueSet/audit-entity-type"
        )
        private final Coding type;
        @Binding(
            bindingName = "AuditEventEntityRole",
            strength = BindingStrength.ValueSet.EXTENSIBLE,
            description = "Code representing the role the entity played in the audit event.",
            valueSet = "http://hl7.org/fhir/ValueSet/object-role"
        )
        private final Coding role;
        @Binding(
            bindingName = "AuditEventEntityLifecycle",
            strength = BindingStrength.ValueSet.EXTENSIBLE,
            description = "Identifier for the data life-cycle stage for the entity.",
            valueSet = "http://hl7.org/fhir/ValueSet/object-lifecycle-events"
        )
        private final Coding lifecycle;
        @Binding(
            bindingName = "SecurityLabels",
            strength = BindingStrength.ValueSet.EXTENSIBLE,
            description = "Security Labels from the Healthcare Privacy and Security Classification System.",
            valueSet = "http://hl7.org/fhir/ValueSet/security-labels"
        )
        private final List<Coding> securityLabel;
        @Summary
        private final String name;
        private final String description;
        @Summary
        private final Base64Binary query;
        private final List<Detail> detail;

        private volatile int hashCode;

        private Entity(Builder builder) {
            super(builder);
            what = builder.what;
            type = builder.type;
            role = builder.role;
            lifecycle = builder.lifecycle;
            securityLabel = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.securityLabel, "securityLabel"));
            name = builder.name;
            description = builder.description;
            query = builder.query;
            detail = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.detail, "detail"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Identifies a specific instance of the entity. The reference should be version specific.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getWhat() {
            return what;
        }

        /**
         * The type of the object that was involved in this audit event.
         * 
         * @return
         *     An immutable object of type {@link Coding} that may be null.
         */
        public Coding getType() {
            return type;
        }

        /**
         * Code representing the role the entity played in the event being audited.
         * 
         * @return
         *     An immutable object of type {@link Coding} that may be null.
         */
        public Coding getRole() {
            return role;
        }

        /**
         * Identifier for the data life-cycle stage for the entity.
         * 
         * @return
         *     An immutable object of type {@link Coding} that may be null.
         */
        public Coding getLifecycle() {
            return lifecycle;
        }

        /**
         * Security labels for the identified entity.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
         */
        public List<Coding> getSecurityLabel() {
            return securityLabel;
        }

        /**
         * A name of the entity in the audit event.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * Text that describes the entity in more detail.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * The query parameters for a query-type entities.
         * 
         * @return
         *     An immutable object of type {@link Base64Binary} that may be null.
         */
        public Base64Binary getQuery() {
            return query;
        }

        /**
         * Tagged value pairs for conveying additional information about the entity.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Detail} that may be empty.
         */
        public List<Detail> getDetail() {
            return detail;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (what != null) || 
                (type != null) || 
                (role != null) || 
                (lifecycle != null) || 
                !securityLabel.isEmpty() || 
                (name != null) || 
                (description != null) || 
                (query != null) || 
                !detail.isEmpty();
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
                    accept(what, "what", visitor);
                    accept(type, "type", visitor);
                    accept(role, "role", visitor);
                    accept(lifecycle, "lifecycle", visitor);
                    accept(securityLabel, "securityLabel", visitor, Coding.class);
                    accept(name, "name", visitor);
                    accept(description, "description", visitor);
                    accept(query, "query", visitor);
                    accept(detail, "detail", visitor, Detail.class);
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
            Entity other = (Entity) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(what, other.what) && 
                Objects.equals(type, other.type) && 
                Objects.equals(role, other.role) && 
                Objects.equals(lifecycle, other.lifecycle) && 
                Objects.equals(securityLabel, other.securityLabel) && 
                Objects.equals(name, other.name) && 
                Objects.equals(description, other.description) && 
                Objects.equals(query, other.query) && 
                Objects.equals(detail, other.detail);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    what, 
                    type, 
                    role, 
                    lifecycle, 
                    securityLabel, 
                    name, 
                    description, 
                    query, 
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
            private Reference what;
            private Coding type;
            private Coding role;
            private Coding lifecycle;
            private List<Coding> securityLabel = new ArrayList<>();
            private String name;
            private String description;
            private Base64Binary query;
            private List<Detail> detail = new ArrayList<>();

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
             * Identifies a specific instance of the entity. The reference should be version specific.
             * 
             * @param what
             *     Specific instance of resource
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder what(Reference what) {
                this.what = what;
                return this;
            }

            /**
             * The type of the object that was involved in this audit event.
             * 
             * @param type
             *     Type of entity involved
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(Coding type) {
                this.type = type;
                return this;
            }

            /**
             * Code representing the role the entity played in the event being audited.
             * 
             * @param role
             *     What role the entity played
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder role(Coding role) {
                this.role = role;
                return this;
            }

            /**
             * Identifier for the data life-cycle stage for the entity.
             * 
             * @param lifecycle
             *     Life-cycle stage for the entity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder lifecycle(Coding lifecycle) {
                this.lifecycle = lifecycle;
                return this;
            }

            /**
             * Security labels for the identified entity.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param securityLabel
             *     Security labels on the entity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder securityLabel(Coding... securityLabel) {
                for (Coding value : securityLabel) {
                    this.securityLabel.add(value);
                }
                return this;
            }

            /**
             * Security labels for the identified entity.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param securityLabel
             *     Security labels on the entity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder securityLabel(Collection<Coding> securityLabel) {
                this.securityLabel = new ArrayList<>(securityLabel);
                return this;
            }

            /**
             * A name of the entity in the audit event.
             * 
             * @param name
             *     Descriptor for entity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Text that describes the entity in more detail.
             * 
             * @param description
             *     Descriptive text
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * The query parameters for a query-type entities.
             * 
             * @param query
             *     Query parameters
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder query(Base64Binary query) {
                this.query = query;
                return this;
            }

            /**
             * Tagged value pairs for conveying additional information about the entity.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param detail
             *     Additional Information about the entity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Detail... detail) {
                for (Detail value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * Tagged value pairs for conveying additional information about the entity.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param detail
             *     Additional Information about the entity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Collection<Detail> detail) {
                this.detail = new ArrayList<>(detail);
                return this;
            }

            /**
             * Build the {@link Entity}
             * 
             * @return
             *     An immutable object of type {@link Entity}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Entity per the base specification
             */
            @Override
            public Entity build() {
                return new Entity(this);
            }

            protected Builder from(Entity entity) {
                super.from(entity);
                what = entity.what;
                type = entity.type;
                role = entity.role;
                lifecycle = entity.lifecycle;
                securityLabel.addAll(entity.securityLabel);
                name = entity.name;
                description = entity.description;
                query = entity.query;
                detail.addAll(entity.detail);
                return this;
            }
        }

        /**
         * Tagged value pairs for conveying additional information about the entity.
         */
        public static class Detail extends BackboneElement {
            @Required
            private final String type;
            @Choice({ String.class, Base64Binary.class })
            @Required
            private final Element value;

            private volatile int hashCode;

            private Detail(Builder builder) {
                super(builder);
                type = ValidationSupport.requireNonNull(builder.type, "type");
                value = ValidationSupport.requireChoiceElement(builder.value, "value", String.class, Base64Binary.class);
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * The type of extra detail provided in the value.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getType() {
                return type;
            }

            /**
             * The value of the extra detail.
             * 
             * @return
             *     An immutable object of type {@link Element} that is non-null.
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
                Detail other = (Detail) obj;
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
                private String type;
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
                 * The type of extra detail provided in the value.
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     Name of the property
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(String type) {
                    this.type = type;
                    return this;
                }

                /**
                 * The value of the extra detail.
                 * 
                 * <p>This element is required.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link String}</li>
                 * <li>{@link Base64Binary}</li>
                 * </ul>
                 * 
                 * @param value
                 *     Property value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Element value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Build the {@link Detail}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * <li>value</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Detail}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Detail per the base specification
                 */
                @Override
                public Detail build() {
                    return new Detail(this);
                }

                protected Builder from(Detail detail) {
                    super.from(detail);
                    type = detail.type;
                    value = detail.value;
                    return this;
                }
            }
        }
    }
}
