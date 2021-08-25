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
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Signature;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ProvenanceEntityRole;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Provenance of a resource is a record that describes entities and processes involved in producing and delivering or 
 * otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling 
 * trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become 
 * important records with their own provenance. Provenance statement indicates clinical significance in terms of 
 * confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document 
 * Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust 
 * policies.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "provenance-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://terminology.hl7.org/ValueSet/v3-PurposeOfUse",
    expression = "reason.exists() implies (reason.all(memberOf('http://terminology.hl7.org/ValueSet/v3-PurposeOfUse', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/Provenance",
    generated = true
)
@Constraint(
    id = "provenance-1",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/provenance-activity-type",
    expression = "activity.exists() implies (activity.memberOf('http://hl7.org/fhir/ValueSet/provenance-activity-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Provenance",
    generated = true
)
@Constraint(
    id = "provenance-2",
    level = "Warning",
    location = "agent.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/provenance-agent-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/provenance-agent-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Provenance",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Provenance extends DomainResource {
    @Summary
    @Required
    private final List<Reference> target;
    @Choice({ Period.class, DateTime.class })
    private final Element occurred;
    @Summary
    @Required
    private final Instant recorded;
    private final List<Uri> policy;
    @ReferenceTarget({ "Location" })
    private final Reference location;
    @Binding(
        bindingName = "ProvenanceReason",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The reason the activity took place.",
        valueSet = "http://terminology.hl7.org/ValueSet/v3-PurposeOfUse"
    )
    private final List<CodeableConcept> reason;
    @Binding(
        bindingName = "ProvenanceActivity",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The activity that took place.",
        valueSet = "http://hl7.org/fhir/ValueSet/provenance-activity-type"
    )
    private final CodeableConcept activity;
    @Required
    private final List<Agent> agent;
    private final List<Entity> entity;
    private final List<Signature> signature;

    private Provenance(Builder builder) {
        super(builder);
        target = Collections.unmodifiableList(builder.target);
        occurred = builder.occurred;
        recorded = builder.recorded;
        policy = Collections.unmodifiableList(builder.policy);
        location = builder.location;
        reason = Collections.unmodifiableList(builder.reason);
        activity = builder.activity;
        agent = Collections.unmodifiableList(builder.agent);
        entity = Collections.unmodifiableList(builder.entity);
        signature = Collections.unmodifiableList(builder.signature);
    }

    /**
     * The Reference(s) that were generated or updated by the activity described in this resource. A provenance can point to 
     * more than one target if multiple resources were created/updated by the same activity.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that is non-empty.
     */
    public List<Reference> getTarget() {
        return target;
    }

    /**
     * The period during which the activity occurred.
     * 
     * @return
     *     An immutable object of type {@link Period} or {@link DateTime} that may be null.
     */
    public Element getOccurred() {
        return occurred;
    }

    /**
     * The instant of time at which the activity was recorded.
     * 
     * @return
     *     An immutable object of type {@link Instant} that is non-null.
     */
    public Instant getRecorded() {
        return recorded;
    }

    /**
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy 
     * documents, such as patient consent, guarantor funding, etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
     */
    public List<Uri> getPolicy() {
        return policy;
    }

    /**
     * Where the activity occurred, if relevant.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getLocation() {
        return location;
    }

    /**
     * The reason that the activity was taking place.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReason() {
        return reason;
    }

    /**
     * An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, 
     * processing, transforming, modifying, relocating, using, or generating entities.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getActivity() {
        return activity;
    }

    /**
     * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
     * taking place.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Agent} that is non-empty.
     */
    public List<Agent> getAgent() {
        return agent;
    }

    /**
     * An entity used in this activity.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Entity} that may be empty.
     */
    public List<Entity> getEntity() {
        return entity;
    }

    /**
     * A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the 
     * signature is indicated.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Signature} that may be empty.
     */
    public List<Signature> getSignature() {
        return signature;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !target.isEmpty() || 
            (occurred != null) || 
            (recorded != null) || 
            !policy.isEmpty() || 
            (location != null) || 
            !reason.isEmpty() || 
            (activity != null) || 
            !agent.isEmpty() || 
            !entity.isEmpty() || 
            !signature.isEmpty();
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
                accept(target, "target", visitor, Reference.class);
                accept(occurred, "occurred", visitor);
                accept(recorded, "recorded", visitor);
                accept(policy, "policy", visitor, Uri.class);
                accept(location, "location", visitor);
                accept(reason, "reason", visitor, CodeableConcept.class);
                accept(activity, "activity", visitor);
                accept(agent, "agent", visitor, Agent.class);
                accept(entity, "entity", visitor, Entity.class);
                accept(signature, "signature", visitor, Signature.class);
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
        Provenance other = (Provenance) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(target, other.target) && 
            Objects.equals(occurred, other.occurred) && 
            Objects.equals(recorded, other.recorded) && 
            Objects.equals(policy, other.policy) && 
            Objects.equals(location, other.location) && 
            Objects.equals(reason, other.reason) && 
            Objects.equals(activity, other.activity) && 
            Objects.equals(agent, other.agent) && 
            Objects.equals(entity, other.entity) && 
            Objects.equals(signature, other.signature);
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
                target, 
                occurred, 
                recorded, 
                policy, 
                location, 
                reason, 
                activity, 
                agent, 
                entity, 
                signature);
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
        private List<Reference> target = new ArrayList<>();
        private Element occurred;
        private Instant recorded;
        private List<Uri> policy = new ArrayList<>();
        private Reference location;
        private List<CodeableConcept> reason = new ArrayList<>();
        private CodeableConcept activity;
        private List<Agent> agent = new ArrayList<>();
        private List<Entity> entity = new ArrayList<>();
        private List<Signature> signature = new ArrayList<>();

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
         * The Reference(s) that were generated or updated by the activity described in this resource. A provenance can point to 
         * more than one target if multiple resources were created/updated by the same activity.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param target
         *     Target Reference(s) (usually version specific)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder target(Reference... target) {
            for (Reference value : target) {
                this.target.add(value);
            }
            return this;
        }

        /**
         * The Reference(s) that were generated or updated by the activity described in this resource. A provenance can point to 
         * more than one target if multiple resources were created/updated by the same activity.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param target
         *     Target Reference(s) (usually version specific)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder target(Collection<Reference> target) {
            this.target = new ArrayList<>(target);
            return this;
        }

        /**
         * The period during which the activity occurred.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Period}</li>
         * <li>{@link DateTime}</li>
         * </ul>
         * 
         * @param occurred
         *     When the activity occurred
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder occurred(Element occurred) {
            this.occurred = occurred;
            return this;
        }

        /**
         * Convenience method for setting {@code recorded}.
         * 
         * <p>This element is required.
         * 
         * @param recorded
         *     When the activity was recorded / updated
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #recorded(com.ibm.fhir.model.type.Instant)
         */
        public Builder recorded(java.time.ZonedDateTime recorded) {
            this.recorded = (recorded == null) ? null : Instant.of(recorded);
            return this;
        }

        /**
         * The instant of time at which the activity was recorded.
         * 
         * <p>This element is required.
         * 
         * @param recorded
         *     When the activity was recorded / updated
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recorded(Instant recorded) {
            this.recorded = recorded;
            return this;
        }

        /**
         * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy 
         * documents, such as patient consent, guarantor funding, etc.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param policy
         *     Policy or plan the activity was defined by
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
         * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy 
         * documents, such as patient consent, guarantor funding, etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param policy
         *     Policy or plan the activity was defined by
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder policy(Collection<Uri> policy) {
            this.policy = new ArrayList<>(policy);
            return this;
        }

        /**
         * Where the activity occurred, if relevant.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param location
         *     Where the activity occurred, if relevant
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(Reference location) {
            this.location = location;
            return this;
        }

        /**
         * The reason that the activity was taking place.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reason
         *     Reason the activity is occurring
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
         * The reason that the activity was taking place.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reason
         *     Reason the activity is occurring
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
         * An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, 
         * processing, transforming, modifying, relocating, using, or generating entities.
         * 
         * @param activity
         *     Activity that occurred
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder activity(CodeableConcept activity) {
            this.activity = activity;
            return this;
        }

        /**
         * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
         * taking place.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param agent
         *     Actor involved
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
         * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
         * taking place.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param agent
         *     Actor involved
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder agent(Collection<Agent> agent) {
            this.agent = new ArrayList<>(agent);
            return this;
        }

        /**
         * An entity used in this activity.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param entity
         *     An entity used in this activity
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
         * An entity used in this activity.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param entity
         *     An entity used in this activity
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder entity(Collection<Entity> entity) {
            this.entity = new ArrayList<>(entity);
            return this;
        }

        /**
         * A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the 
         * signature is indicated.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param signature
         *     Signature on target
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder signature(Signature... signature) {
            for (Signature value : signature) {
                this.signature.add(value);
            }
            return this;
        }

        /**
         * A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the 
         * signature is indicated.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param signature
         *     Signature on target
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder signature(Collection<Signature> signature) {
            this.signature = new ArrayList<>(signature);
            return this;
        }

        /**
         * Build the {@link Provenance}
         * 
         * <p>Required elements:
         * <ul>
         * <li>target</li>
         * <li>recorded</li>
         * <li>agent</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Provenance}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Provenance per the base specification
         */
        @Override
        public Provenance build() {
            Provenance provenance = new Provenance(this);
            if (validating) {
                validate(provenance);
            }
            return provenance;
        }

        protected void validate(Provenance provenance) {
            super.validate(provenance);
            ValidationSupport.checkNonEmptyList(provenance.target, "target", Reference.class);
            ValidationSupport.choiceElement(provenance.occurred, "occurred", Period.class, DateTime.class);
            ValidationSupport.requireNonNull(provenance.recorded, "recorded");
            ValidationSupport.checkList(provenance.policy, "policy", Uri.class);
            ValidationSupport.checkList(provenance.reason, "reason", CodeableConcept.class);
            ValidationSupport.checkNonEmptyList(provenance.agent, "agent", Agent.class);
            ValidationSupport.checkList(provenance.entity, "entity", Entity.class);
            ValidationSupport.checkList(provenance.signature, "signature", Signature.class);
            ValidationSupport.checkReferenceType(provenance.location, "location", "Location");
        }

        protected Builder from(Provenance provenance) {
            super.from(provenance);
            target.addAll(provenance.target);
            occurred = provenance.occurred;
            recorded = provenance.recorded;
            policy.addAll(provenance.policy);
            location = provenance.location;
            reason.addAll(provenance.reason);
            activity = provenance.activity;
            agent.addAll(provenance.agent);
            entity.addAll(provenance.entity);
            signature.addAll(provenance.signature);
            return this;
        }
    }

    /**
     * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity 
     * taking place.
     */
    public static class Agent extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "ProvenanceAgentType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The type of participation that a provenance agent played with respect to the activity.",
            valueSet = "http://hl7.org/fhir/ValueSet/provenance-agent-type"
        )
        private final CodeableConcept type;
        @Binding(
            bindingName = "ProvenanceAgentRole",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The role that a provenance agent played with respect to the activity.",
            valueSet = "http://hl7.org/fhir/ValueSet/security-role-type"
        )
        private final List<CodeableConcept> role;
        @Summary
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "RelatedPerson", "Patient", "Device", "Organization" })
        @Required
        private final Reference who;
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "RelatedPerson", "Patient", "Device", "Organization" })
        private final Reference onBehalfOf;

        private Agent(Builder builder) {
            super(builder);
            type = builder.type;
            role = Collections.unmodifiableList(builder.role);
            who = builder.who;
            onBehalfOf = builder.onBehalfOf;
        }

        /**
         * The participation the agent had with respect to the activity.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The function of the agent with respect to the activity. The security role enabling the agent with respect to the 
         * activity.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getRole() {
            return role;
        }

        /**
         * The individual, device or organization that participated in the event.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getWho() {
            return who;
        }

        /**
         * The individual, device, or organization for whom the change was made.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getOnBehalfOf() {
            return onBehalfOf;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                !role.isEmpty() || 
                (who != null) || 
                (onBehalfOf != null);
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
                    accept(onBehalfOf, "onBehalfOf", visitor);
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
                Objects.equals(onBehalfOf, other.onBehalfOf);
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
                    onBehalfOf);
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
            private Reference onBehalfOf;

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
             * The participation the agent had with respect to the activity.
             * 
             * @param type
             *     How the agent participated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * The function of the agent with respect to the activity. The security role enabling the agent with respect to the 
             * activity.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param role
             *     What the agents role was
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
             * The function of the agent with respect to the activity. The security role enabling the agent with respect to the 
             * activity.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param role
             *     What the agents role was
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder role(Collection<CodeableConcept> role) {
                this.role = new ArrayList<>(role);
                return this;
            }

            /**
             * The individual, device or organization that participated in the event.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Patient}</li>
             * <li>{@link Device}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param who
             *     Who participated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder who(Reference who) {
                this.who = who;
                return this;
            }

            /**
             * The individual, device, or organization for whom the change was made.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Patient}</li>
             * <li>{@link Device}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param onBehalfOf
             *     Who the agent is representing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder onBehalfOf(Reference onBehalfOf) {
                this.onBehalfOf = onBehalfOf;
                return this;
            }

            /**
             * Build the {@link Agent}
             * 
             * <p>Required elements:
             * <ul>
             * <li>who</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Agent}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Agent per the base specification
             */
            @Override
            public Agent build() {
                Agent agent = new Agent(this);
                if (validating) {
                    validate(agent);
                }
                return agent;
            }

            protected void validate(Agent agent) {
                super.validate(agent);
                ValidationSupport.checkList(agent.role, "role", CodeableConcept.class);
                ValidationSupport.requireNonNull(agent.who, "who");
                ValidationSupport.checkReferenceType(agent.who, "who", "Practitioner", "PractitionerRole", "RelatedPerson", "Patient", "Device", "Organization");
                ValidationSupport.checkReferenceType(agent.onBehalfOf, "onBehalfOf", "Practitioner", "PractitionerRole", "RelatedPerson", "Patient", "Device", "Organization");
                ValidationSupport.requireValueOrChildren(agent);
            }

            protected Builder from(Agent agent) {
                super.from(agent);
                type = agent.type;
                role.addAll(agent.role);
                who = agent.who;
                onBehalfOf = agent.onBehalfOf;
                return this;
            }
        }
    }

    /**
     * An entity used in this activity.
     */
    public static class Entity extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "ProvenanceEntityRole",
            strength = BindingStrength.Value.REQUIRED,
            description = "How an entity was used in an activity.",
            valueSet = "http://hl7.org/fhir/ValueSet/provenance-entity-role|4.0.1"
        )
        @Required
        private final ProvenanceEntityRole role;
        @Summary
        @Required
        private final Reference what;
        private final List<Provenance.Agent> agent;

        private Entity(Builder builder) {
            super(builder);
            role = builder.role;
            what = builder.what;
            agent = Collections.unmodifiableList(builder.agent);
        }

        /**
         * How the entity was used during the activity.
         * 
         * @return
         *     An immutable object of type {@link ProvenanceEntityRole} that is non-null.
         */
        public ProvenanceEntityRole getRole() {
            return role;
        }

        /**
         * Identity of the Entity used. May be a logical or physical uri and maybe absolute or relative.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getWhat() {
            return what;
        }

        /**
         * The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other 
         * agents. This description can be understood as shorthand for saying that the agent was responsible for the activity 
         * which generated the entity.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Agent} that may be empty.
         */
        public List<Provenance.Agent> getAgent() {
            return agent;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (role != null) || 
                (what != null) || 
                !agent.isEmpty();
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
                    accept(role, "role", visitor);
                    accept(what, "what", visitor);
                    accept(agent, "agent", visitor, Provenance.Agent.class);
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
                Objects.equals(role, other.role) && 
                Objects.equals(what, other.what) && 
                Objects.equals(agent, other.agent);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    role, 
                    what, 
                    agent);
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
            private ProvenanceEntityRole role;
            private Reference what;
            private List<Provenance.Agent> agent = new ArrayList<>();

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
             * How the entity was used during the activity.
             * 
             * <p>This element is required.
             * 
             * @param role
             *     derivation | revision | quotation | source | removal
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder role(ProvenanceEntityRole role) {
                this.role = role;
                return this;
            }

            /**
             * Identity of the Entity used. May be a logical or physical uri and maybe absolute or relative.
             * 
             * <p>This element is required.
             * 
             * @param what
             *     Identity of entity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder what(Reference what) {
                this.what = what;
                return this;
            }

            /**
             * The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other 
             * agents. This description can be understood as shorthand for saying that the agent was responsible for the activity 
             * which generated the entity.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param agent
             *     Entity is attributed to this agent
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder agent(Provenance.Agent... agent) {
                for (Provenance.Agent value : agent) {
                    this.agent.add(value);
                }
                return this;
            }

            /**
             * The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other 
             * agents. This description can be understood as shorthand for saying that the agent was responsible for the activity 
             * which generated the entity.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param agent
             *     Entity is attributed to this agent
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder agent(Collection<Provenance.Agent> agent) {
                this.agent = new ArrayList<>(agent);
                return this;
            }

            /**
             * Build the {@link Entity}
             * 
             * <p>Required elements:
             * <ul>
             * <li>role</li>
             * <li>what</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Entity}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Entity per the base specification
             */
            @Override
            public Entity build() {
                Entity entity = new Entity(this);
                if (validating) {
                    validate(entity);
                }
                return entity;
            }

            protected void validate(Entity entity) {
                super.validate(entity);
                ValidationSupport.requireNonNull(entity.role, "role");
                ValidationSupport.requireNonNull(entity.what, "what");
                ValidationSupport.checkList(entity.agent, "agent", Provenance.Agent.class);
                ValidationSupport.requireValueOrChildren(entity);
            }

            protected Builder from(Entity entity) {
                super.from(entity);
                role = entity.role;
                what = entity.what;
                agent.addAll(entity.agent);
                return this;
            }
        }
    }
}
