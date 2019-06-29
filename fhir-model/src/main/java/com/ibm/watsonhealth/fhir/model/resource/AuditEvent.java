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

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.AuditEventAction;
import com.ibm.watsonhealth.fhir.model.type.AuditEventAgentNetworkType;
import com.ibm.watsonhealth.fhir.model.type.AuditEventOutcome;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion 
 * attempts and monitoring for inappropriate usage.
 * </p>
 */
@Constraint(
    id = "sev-1",
    level = "Rule",
    location = "AuditEvent.entity",
    description = "Either a name or a query (NOT both)",
    expression = "name.empty() or query.empty()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class AuditEvent extends DomainResource {
    private final Coding type;
    private final List<Coding> subtype;
    private final AuditEventAction action;
    private final Period period;
    private final Instant recorded;
    private final AuditEventOutcome outcome;
    private final String outcomeDesc;
    private final List<CodeableConcept> purposeOfEvent;
    private final List<Agent> agent;
    private final Source source;
    private final List<Entity> entity;

    private AuditEvent(Builder builder) {
        super(builder);
        type = ValidationSupport.requireNonNull(builder.type, "type");
        subtype = Collections.unmodifiableList(builder.subtype);
        action = builder.action;
        period = builder.period;
        recorded = ValidationSupport.requireNonNull(builder.recorded, "recorded");
        outcome = builder.outcome;
        outcomeDesc = builder.outcomeDesc;
        purposeOfEvent = Collections.unmodifiableList(builder.purposeOfEvent);
        agent = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.agent, "agent"));
        source = ValidationSupport.requireNonNull(builder.source, "source");
        entity = Collections.unmodifiableList(builder.entity);
    }

    /**
     * <p>
     * Identifier for a family of the event. For example, a menu item, program, rule, policy, function code, application name 
     * or URL. It identifies the performed function.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Coding}.
     */
    public Coding getType() {
        return type;
    }

    /**
     * <p>
     * Identifier for the category of event.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Coding}.
     */
    public List<Coding> getSubtype() {
        return subtype;
    }

    /**
     * <p>
     * Indicator for type of action performed during the event that generated the audit.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link AuditEventAction}.
     */
    public AuditEventAction getAction() {
        return action;
    }

    /**
     * <p>
     * The period during which the activity occurred.
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
     * The time when the event was recorded.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Instant}.
     */
    public Instant getRecorded() {
        return recorded;
    }

    /**
     * <p>
     * Indicates whether the event succeeded or failed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link AuditEventOutcome}.
     */
    public AuditEventOutcome getOutcome() {
        return outcome;
    }

    /**
     * <p>
     * A free text description of the outcome of the event.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getOutcomeDesc() {
        return outcomeDesc;
    }

    /**
     * <p>
     * The purposeOfUse (reason) that was used during the event being recorded.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getPurposeOfEvent() {
        return purposeOfEvent;
    }

    /**
     * <p>
     * An actor taking an active role in the event or activity that is logged.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Agent}.
     */
    public List<Agent> getAgent() {
        return agent;
    }

    /**
     * <p>
     * The system that is reporting the event.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Source}.
     */
    public Source getSource() {
        return source;
    }

    /**
     * <p>
     * Specific instances of data or objects that have been accessed.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Entity}.
     */
    public List<Entity> getEntity() {
        return entity;
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, recorded, agent, source).from(this);
    }

    public Builder toBuilder(Coding type, Instant recorded, List<Agent> agent, Source source) {
        return new Builder(type, recorded, agent, source).from(this);
    }

    public static Builder builder(Coding type, Instant recorded, List<Agent> agent, Source source) {
        return new Builder(type, recorded, agent, source);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Coding type;
        private final Instant recorded;
        private final List<Agent> agent;
        private final Source source;

        // optional
        private List<Coding> subtype = new ArrayList<>();
        private AuditEventAction action;
        private Period period;
        private AuditEventOutcome outcome;
        private String outcomeDesc;
        private List<CodeableConcept> purposeOfEvent = new ArrayList<>();
        private List<Entity> entity = new ArrayList<>();

        private Builder(Coding type, Instant recorded, List<Agent> agent, Source source) {
            super();
            this.type = type;
            this.recorded = recorded;
            this.agent = agent;
            this.source = source;
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
         * Identifier for the category of event.
         * </p>
         * 
         * @param subtype
         *     More specific type/id for the event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subtype(Coding... subtype) {
            for (Coding value : subtype) {
                this.subtype.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifier for the category of event.
         * </p>
         * 
         * @param subtype
         *     More specific type/id for the event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subtype(Collection<Coding> subtype) {
            this.subtype.addAll(subtype);
            return this;
        }

        /**
         * <p>
         * Indicator for type of action performed during the event that generated the audit.
         * </p>
         * 
         * @param action
         *     Type of action performed during the event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder action(AuditEventAction action) {
            this.action = action;
            return this;
        }

        /**
         * <p>
         * The period during which the activity occurred.
         * </p>
         * 
         * @param period
         *     When the activity occurred
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
         * Indicates whether the event succeeded or failed.
         * </p>
         * 
         * @param outcome
         *     Whether the event succeeded or failed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder outcome(AuditEventOutcome outcome) {
            this.outcome = outcome;
            return this;
        }

        /**
         * <p>
         * A free text description of the outcome of the event.
         * </p>
         * 
         * @param outcomeDesc
         *     Description of the event outcome
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder outcomeDesc(String outcomeDesc) {
            this.outcomeDesc = outcomeDesc;
            return this;
        }

        /**
         * <p>
         * The purposeOfUse (reason) that was used during the event being recorded.
         * </p>
         * 
         * @param purposeOfEvent
         *     The purposeOfUse of the event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder purposeOfEvent(CodeableConcept... purposeOfEvent) {
            for (CodeableConcept value : purposeOfEvent) {
                this.purposeOfEvent.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The purposeOfUse (reason) that was used during the event being recorded.
         * </p>
         * 
         * @param purposeOfEvent
         *     The purposeOfUse of the event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder purposeOfEvent(Collection<CodeableConcept> purposeOfEvent) {
            this.purposeOfEvent.addAll(purposeOfEvent);
            return this;
        }

        /**
         * <p>
         * Specific instances of data or objects that have been accessed.
         * </p>
         * 
         * @param entity
         *     Data or objects used
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder entity(Entity... entity) {
            for (Entity value : entity) {
                this.entity.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Specific instances of data or objects that have been accessed.
         * </p>
         * 
         * @param entity
         *     Data or objects used
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder entity(Collection<Entity> entity) {
            this.entity.addAll(entity);
            return this;
        }

        @Override
        public AuditEvent build() {
            return new AuditEvent(this);
        }

        private Builder from(AuditEvent auditEvent) {
            id = auditEvent.id;
            meta = auditEvent.meta;
            implicitRules = auditEvent.implicitRules;
            language = auditEvent.language;
            text = auditEvent.text;
            contained.addAll(auditEvent.contained);
            extension.addAll(auditEvent.extension);
            modifierExtension.addAll(auditEvent.modifierExtension);
            subtype.addAll(auditEvent.subtype);
            action = auditEvent.action;
            period = auditEvent.period;
            outcome = auditEvent.outcome;
            outcomeDesc = auditEvent.outcomeDesc;
            purposeOfEvent.addAll(auditEvent.purposeOfEvent);
            entity.addAll(auditEvent.entity);
            return this;
        }
    }

    /**
     * <p>
     * An actor taking an active role in the event or activity that is logged.
     * </p>
     */
    public static class Agent extends BackboneElement {
        private final CodeableConcept type;
        private final List<CodeableConcept> role;
        private final Reference who;
        private final String altId;
        private final String name;
        private final Boolean requestor;
        private final Reference location;
        private final List<Uri> policy;
        private final Coding media;
        private final Network network;
        private final List<CodeableConcept> purposeOfUse;

        private Agent(Builder builder) {
            super(builder);
            type = builder.type;
            role = Collections.unmodifiableList(builder.role);
            who = builder.who;
            altId = builder.altId;
            name = builder.name;
            requestor = ValidationSupport.requireNonNull(builder.requestor, "requestor");
            location = builder.location;
            policy = Collections.unmodifiableList(builder.policy);
            media = builder.media;
            network = builder.network;
            purposeOfUse = Collections.unmodifiableList(builder.purposeOfUse);
        }

        /**
         * <p>
         * Specification of the participation type the user plays when performing the event.
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
         * The security role that the user was acting under, that come from local codes defined by the access control security 
         * system (e.g. RBAC, ABAC) used in the local context.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getRole() {
            return role;
        }

        /**
         * <p>
         * Reference to who this agent is that was involved in the event.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getWho() {
            return who;
        }

        /**
         * <p>
         * Alternative agent Identifier. For a human, this should be a user identifier text string from authentication system. 
         * This identifier would be one known to a common authentication system (e.g. single sign-on), if available.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getAltId() {
            return altId;
        }

        /**
         * <p>
         * Human-meaningful name for the agent.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getName() {
            return name;
        }

        /**
         * <p>
         * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getRequestor() {
            return requestor;
        }

        /**
         * <p>
         * Where the event occurred.
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
         * The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple 
         * applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security 
         * token used.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Uri}.
         */
        public List<Uri> getPolicy() {
            return policy;
        }

        /**
         * <p>
         * Type of media involved. Used when the event is about exporting/importing onto media.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Coding}.
         */
        public Coding getMedia() {
            return media;
        }

        /**
         * <p>
         * Logical network location for application activity, if the activity has a network location.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Network}.
         */
        public Network getNetwork() {
            return network;
        }

        /**
         * <p>
         * The reason (purpose of use), specific to this agent, that was used during the event being recorded.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getPurposeOfUse() {
            return purposeOfUse;
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
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(requestor).from(this);
        }

        public Builder toBuilder(Boolean requestor) {
            return new Builder(requestor).from(this);
        }

        public static Builder builder(Boolean requestor) {
            return new Builder(requestor);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Boolean requestor;

            // optional
            private CodeableConcept type;
            private List<CodeableConcept> role = new ArrayList<>();
            private Reference who;
            private String altId;
            private String name;
            private Reference location;
            private List<Uri> policy = new ArrayList<>();
            private Coding media;
            private Network network;
            private List<CodeableConcept> purposeOfUse = new ArrayList<>();

            private Builder(Boolean requestor) {
                super();
                this.requestor = requestor;
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
             * Specification of the participation type the user plays when performing the event.
             * </p>
             * 
             * @param type
             *     How agent participated
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * <p>
             * The security role that the user was acting under, that come from local codes defined by the access control security 
             * system (e.g. RBAC, ABAC) used in the local context.
             * </p>
             * 
             * @param role
             *     Agent role in the event
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder role(CodeableConcept... role) {
                for (CodeableConcept value : role) {
                    this.role.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The security role that the user was acting under, that come from local codes defined by the access control security 
             * system (e.g. RBAC, ABAC) used in the local context.
             * </p>
             * 
             * @param role
             *     Agent role in the event
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder role(Collection<CodeableConcept> role) {
                this.role.addAll(role);
                return this;
            }

            /**
             * <p>
             * Reference to who this agent is that was involved in the event.
             * </p>
             * 
             * @param who
             *     Identifier of who
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder who(Reference who) {
                this.who = who;
                return this;
            }

            /**
             * <p>
             * Alternative agent Identifier. For a human, this should be a user identifier text string from authentication system. 
             * This identifier would be one known to a common authentication system (e.g. single sign-on), if available.
             * </p>
             * 
             * @param altId
             *     Alternative User identity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder altId(String altId) {
                this.altId = altId;
                return this;
            }

            /**
             * <p>
             * Human-meaningful name for the agent.
             * </p>
             * 
             * @param name
             *     Human friendly name for the agent
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * <p>
             * Where the event occurred.
             * </p>
             * 
             * @param location
             *     Where
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
             * The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple 
             * applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security 
             * token used.
             * </p>
             * 
             * @param policy
             *     Policy that authorized event
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder policy(Uri... policy) {
                for (Uri value : policy) {
                    this.policy.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple 
             * applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security 
             * token used.
             * </p>
             * 
             * @param policy
             *     Policy that authorized event
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder policy(Collection<Uri> policy) {
                this.policy.addAll(policy);
                return this;
            }

            /**
             * <p>
             * Type of media involved. Used when the event is about exporting/importing onto media.
             * </p>
             * 
             * @param media
             *     Type of media
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder media(Coding media) {
                this.media = media;
                return this;
            }

            /**
             * <p>
             * Logical network location for application activity, if the activity has a network location.
             * </p>
             * 
             * @param network
             *     Logical network location for application activity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder network(Network network) {
                this.network = network;
                return this;
            }

            /**
             * <p>
             * The reason (purpose of use), specific to this agent, that was used during the event being recorded.
             * </p>
             * 
             * @param purposeOfUse
             *     Reason given for this user
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder purposeOfUse(CodeableConcept... purposeOfUse) {
                for (CodeableConcept value : purposeOfUse) {
                    this.purposeOfUse.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The reason (purpose of use), specific to this agent, that was used during the event being recorded.
             * </p>
             * 
             * @param purposeOfUse
             *     Reason given for this user
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder purposeOfUse(Collection<CodeableConcept> purposeOfUse) {
                this.purposeOfUse.addAll(purposeOfUse);
                return this;
            }

            @Override
            public Agent build() {
                return new Agent(this);
            }

            private Builder from(Agent agent) {
                id = agent.id;
                extension.addAll(agent.extension);
                modifierExtension.addAll(agent.modifierExtension);
                type = agent.type;
                role.addAll(agent.role);
                who = agent.who;
                altId = agent.altId;
                name = agent.name;
                location = agent.location;
                policy.addAll(agent.policy);
                media = agent.media;
                network = agent.network;
                purposeOfUse.addAll(agent.purposeOfUse);
                return this;
            }
        }

        /**
         * <p>
         * Logical network location for application activity, if the activity has a network location.
         * </p>
         */
        public static class Network extends BackboneElement {
            private final String address;
            private final AuditEventAgentNetworkType type;

            private Network(Builder builder) {
                super(builder);
                address = builder.address;
                type = builder.type;
            }

            /**
             * <p>
             * An identifier for the network access point of the user device for the audit event.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getAddress() {
                return address;
            }

            /**
             * <p>
             * An identifier for the type of network access point that originated the audit event.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link AuditEventAgentNetworkType}.
             */
            public AuditEventAgentNetworkType getType() {
                return type;
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
                        accept(address, "address", visitor);
                        accept(type, "type", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
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
                private String address;
                private AuditEventAgentNetworkType type;

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
                 * An identifier for the network access point of the user device for the audit event.
                 * </p>
                 * 
                 * @param address
                 *     Identifier for the network access point of the user device
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder address(String address) {
                    this.address = address;
                    return this;
                }

                /**
                 * <p>
                 * An identifier for the type of network access point that originated the audit event.
                 * </p>
                 * 
                 * @param type
                 *     The type of network access point
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder type(AuditEventAgentNetworkType type) {
                    this.type = type;
                    return this;
                }

                @Override
                public Network build() {
                    return new Network(this);
                }

                private Builder from(Network network) {
                    id = network.id;
                    extension.addAll(network.extension);
                    modifierExtension.addAll(network.modifierExtension);
                    address = network.address;
                    type = network.type;
                    return this;
                }
            }
        }
    }

    /**
     * <p>
     * The system that is reporting the event.
     * </p>
     */
    public static class Source extends BackboneElement {
        private final String site;
        private final Reference observer;
        private final List<Coding> type;

        private Source(Builder builder) {
            super(builder);
            site = builder.site;
            observer = ValidationSupport.requireNonNull(builder.observer, "observer");
            type = Collections.unmodifiableList(builder.type);
        }

        /**
         * <p>
         * Logical source location within the healthcare enterprise network. For example, a hospital or other provider location 
         * within a multi-entity provider group.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getSite() {
            return site;
        }

        /**
         * <p>
         * Identifier of the source where the event was detected.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getObserver() {
            return observer;
        }

        /**
         * <p>
         * Code specifying the type of source where event originated.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Coding}.
         */
        public List<Coding> getType() {
            return type;
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
                    accept(site, "site", visitor);
                    accept(observer, "observer", visitor);
                    accept(type, "type", visitor, Coding.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(observer).from(this);
        }

        public Builder toBuilder(Reference observer) {
            return new Builder(observer).from(this);
        }

        public static Builder builder(Reference observer) {
            return new Builder(observer);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference observer;

            // optional
            private String site;
            private List<Coding> type = new ArrayList<>();

            private Builder(Reference observer) {
                super();
                this.observer = observer;
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
             * Logical source location within the healthcare enterprise network. For example, a hospital or other provider location 
             * within a multi-entity provider group.
             * </p>
             * 
             * @param site
             *     Logical source location within the enterprise
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder site(String site) {
                this.site = site;
                return this;
            }

            /**
             * <p>
             * Code specifying the type of source where event originated.
             * </p>
             * 
             * @param type
             *     The type of source where event originated
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(Coding... type) {
                for (Coding value : type) {
                    this.type.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Code specifying the type of source where event originated.
             * </p>
             * 
             * @param type
             *     The type of source where event originated
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(Collection<Coding> type) {
                this.type.addAll(type);
                return this;
            }

            @Override
            public Source build() {
                return new Source(this);
            }

            private Builder from(Source source) {
                id = source.id;
                extension.addAll(source.extension);
                modifierExtension.addAll(source.modifierExtension);
                site = source.site;
                type.addAll(source.type);
                return this;
            }
        }
    }

    /**
     * <p>
     * Specific instances of data or objects that have been accessed.
     * </p>
     */
    public static class Entity extends BackboneElement {
        private final Reference what;
        private final Coding type;
        private final Coding role;
        private final Coding lifecycle;
        private final List<Coding> securityLabel;
        private final String name;
        private final String description;
        private final Base64Binary query;
        private final List<Detail> detail;

        private Entity(Builder builder) {
            super(builder);
            what = builder.what;
            type = builder.type;
            role = builder.role;
            lifecycle = builder.lifecycle;
            securityLabel = Collections.unmodifiableList(builder.securityLabel);
            name = builder.name;
            description = builder.description;
            query = builder.query;
            detail = Collections.unmodifiableList(builder.detail);
        }

        /**
         * <p>
         * Identifies a specific instance of the entity. The reference should be version specific.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getWhat() {
            return what;
        }

        /**
         * <p>
         * The type of the object that was involved in this audit event.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Coding}.
         */
        public Coding getType() {
            return type;
        }

        /**
         * <p>
         * Code representing the role the entity played in the event being audited.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Coding}.
         */
        public Coding getRole() {
            return role;
        }

        /**
         * <p>
         * Identifier for the data life-cycle stage for the entity.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Coding}.
         */
        public Coding getLifecycle() {
            return lifecycle;
        }

        /**
         * <p>
         * Security labels for the identified entity.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Coding}.
         */
        public List<Coding> getSecurityLabel() {
            return securityLabel;
        }

        /**
         * <p>
         * A name of the entity in the audit event.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getName() {
            return name;
        }

        /**
         * <p>
         * Text that describes the entity in more detail.
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
         * The query parameters for a query-type entities.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Base64Binary}.
         */
        public Base64Binary getQuery() {
            return query;
        }

        /**
         * <p>
         * Tagged value pairs for conveying additional information about the entity.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Detail}.
         */
        public List<Detail> getDetail() {
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
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
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
             * Identifies a specific instance of the entity. The reference should be version specific.
             * </p>
             * 
             * @param what
             *     Specific instance of resource
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder what(Reference what) {
                this.what = what;
                return this;
            }

            /**
             * <p>
             * The type of the object that was involved in this audit event.
             * </p>
             * 
             * @param type
             *     Type of entity involved
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(Coding type) {
                this.type = type;
                return this;
            }

            /**
             * <p>
             * Code representing the role the entity played in the event being audited.
             * </p>
             * 
             * @param role
             *     What role the entity played
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder role(Coding role) {
                this.role = role;
                return this;
            }

            /**
             * <p>
             * Identifier for the data life-cycle stage for the entity.
             * </p>
             * 
             * @param lifecycle
             *     Life-cycle stage for the entity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder lifecycle(Coding lifecycle) {
                this.lifecycle = lifecycle;
                return this;
            }

            /**
             * <p>
             * Security labels for the identified entity.
             * </p>
             * 
             * @param securityLabel
             *     Security labels on the entity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder securityLabel(Coding... securityLabel) {
                for (Coding value : securityLabel) {
                    this.securityLabel.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Security labels for the identified entity.
             * </p>
             * 
             * @param securityLabel
             *     Security labels on the entity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder securityLabel(Collection<Coding> securityLabel) {
                this.securityLabel.addAll(securityLabel);
                return this;
            }

            /**
             * <p>
             * A name of the entity in the audit event.
             * </p>
             * 
             * @param name
             *     Descriptor for entity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * <p>
             * Text that describes the entity in more detail.
             * </p>
             * 
             * @param description
             *     Descriptive text
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
             * The query parameters for a query-type entities.
             * </p>
             * 
             * @param query
             *     Query parameters
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder query(Base64Binary query) {
                this.query = query;
                return this;
            }

            /**
             * <p>
             * Tagged value pairs for conveying additional information about the entity.
             * </p>
             * 
             * @param detail
             *     Additional Information about the entity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Detail... detail) {
                for (Detail value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Tagged value pairs for conveying additional information about the entity.
             * </p>
             * 
             * @param detail
             *     Additional Information about the entity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Collection<Detail> detail) {
                this.detail.addAll(detail);
                return this;
            }

            @Override
            public Entity build() {
                return new Entity(this);
            }

            private Builder from(Entity entity) {
                id = entity.id;
                extension.addAll(entity.extension);
                modifierExtension.addAll(entity.modifierExtension);
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
         * <p>
         * Tagged value pairs for conveying additional information about the entity.
         * </p>
         */
        public static class Detail extends BackboneElement {
            private final String type;
            private final Element value;

            private Detail(Builder builder) {
                super(builder);
                type = ValidationSupport.requireNonNull(builder.type, "type");
                value = ValidationSupport.requireChoiceElement(builder.value, "value", String.class, Base64Binary.class);
            }

            /**
             * <p>
             * The type of extra detail provided in the value.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getType() {
                return type;
            }

            /**
             * <p>
             * The value of the extra detail.
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
                return new Builder(type, value).from(this);
            }

            public Builder toBuilder(String type, Element value) {
                return new Builder(type, value).from(this);
            }

            public static Builder builder(String type, Element value) {
                return new Builder(type, value);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final String type;
                private final Element value;

                private Builder(String type, Element value) {
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
                public Detail build() {
                    return new Detail(this);
                }

                private Builder from(Detail detail) {
                    id = detail.id;
                    extension.addAll(detail.extension);
                    modifierExtension.addAll(detail.modifierExtension);
                    return this;
                }
            }
        }
    }
}
