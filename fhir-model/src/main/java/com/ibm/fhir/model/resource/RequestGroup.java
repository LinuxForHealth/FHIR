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
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Expression;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ActionCardinalityBehavior;
import com.ibm.fhir.model.type.code.ActionConditionKind;
import com.ibm.fhir.model.type.code.ActionGroupingBehavior;
import com.ibm.fhir.model.type.code.ActionPrecheckBehavior;
import com.ibm.fhir.model.type.code.ActionRelationshipType;
import com.ibm.fhir.model.type.code.ActionRequiredBehavior;
import com.ibm.fhir.model.type.code.ActionSelectionBehavior;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.RequestIntent;
import com.ibm.fhir.model.type.code.RequestPriority;
import com.ibm.fhir.model.type.code.RequestStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A group of related requests that can be used to capture intended activities that have inter-dependencies such as "give 
 * this medication after that one".
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "rqg-1",
    level = "Rule",
    location = "RequestGroup.action",
    description = "Must have resource or action but not both",
    expression = "resource.exists() != action.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/RequestGroup"
)
@Constraint(
    id = "requestGroup-2",
    level = "Warning",
    location = "action.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/action-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/action-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/RequestGroup",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class RequestGroup extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final List<Canonical> instantiatesCanonical;
    @Summary
    private final List<Uri> instantiatesUri;
    private final List<Reference> basedOn;
    private final List<Reference> replaces;
    @Summary
    private final Identifier groupIdentifier;
    @Summary
    @Binding(
        bindingName = "RequestStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes identifying the lifecycle stage of a request.",
        valueSet = "http://hl7.org/fhir/ValueSet/request-status|4.3.0-CIBUILD"
    )
    @Required
    private final RequestStatus status;
    @Summary
    @Binding(
        bindingName = "RequestIntent",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes indicating the degree of authority/intentionality associated with a request.",
        valueSet = "http://hl7.org/fhir/ValueSet/request-intent|4.3.0-CIBUILD"
    )
    @Required
    private final RequestIntent intent;
    @Summary
    @Binding(
        bindingName = "RequestPriority",
        strength = BindingStrength.Value.REQUIRED,
        description = "Identifies the level of importance to be assigned to actioning the request.",
        valueSet = "http://hl7.org/fhir/ValueSet/request-priority|4.3.0-CIBUILD"
    )
    private final RequestPriority priority;
    @Summary
    private final CodeableConcept code;
    @ReferenceTarget({ "Patient", "Group" })
    private final Reference subject;
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    private final DateTime authoredOn;
    @ReferenceTarget({ "Device", "Practitioner", "PractitionerRole" })
    private final Reference author;
    private final List<CodeableConcept> reasonCode;
    @ReferenceTarget({ "Condition", "Observation", "DiagnosticReport", "DocumentReference" })
    private final List<Reference> reasonReference;
    private final List<Annotation> note;
    private final List<Action> action;

    private RequestGroup(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
        instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        replaces = Collections.unmodifiableList(builder.replaces);
        groupIdentifier = builder.groupIdentifier;
        status = builder.status;
        intent = builder.intent;
        priority = builder.priority;
        code = builder.code;
        subject = builder.subject;
        encounter = builder.encounter;
        authoredOn = builder.authoredOn;
        author = builder.author;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        note = Collections.unmodifiableList(builder.note);
        action = Collections.unmodifiableList(builder.action);
    }

    /**
     * Allows a service to provide a unique, business identifier for the request.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A canonical URL referencing a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this request.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * A URL referencing an externally defined protocol, guideline, orderset or other definition that is adhered to in whole 
     * or in part by this request.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * A plan, proposal or order that is fulfilled in whole or in part by this request.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * Completed or terminated request(s) whose function is taken by this new request.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReplaces() {
        return replaces;
    }

    /**
     * A shared identifier common to all requests that were authorized more or less simultaneously by a single author, 
     * representing the identifier of the requisition, prescription or similar form.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getGroupIdentifier() {
        return groupIdentifier;
    }

    /**
     * The current state of the request. For request groups, the status reflects the status of all the requests in the group.
     * 
     * @return
     *     An immutable object of type {@link RequestStatus} that is non-null.
     */
    public RequestStatus getStatus() {
        return status;
    }

    /**
     * Indicates the level of authority/intentionality associated with the request and where the request fits into the 
     * workflow chain.
     * 
     * @return
     *     An immutable object of type {@link RequestIntent} that is non-null.
     */
    public RequestIntent getIntent() {
        return intent;
    }

    /**
     * Indicates how quickly the request should be addressed with respect to other requests.
     * 
     * @return
     *     An immutable object of type {@link RequestPriority} that may be null.
     */
    public RequestPriority getPriority() {
        return priority;
    }

    /**
     * A code that identifies what the overall request group is.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * The subject for which the request group was created.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * Describes the context of the request group, if any.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * Indicates when the request group was created.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getAuthoredOn() {
        return authoredOn;
    }

    /**
     * Provides a reference to the author of the request group.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * Describes the reason for the request group in coded or textual form.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Indicates another resource whose existence justifies this request group.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * Provides a mechanism to communicate additional information about the response.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * The actions, if any, produced by the evaluation of the artifact.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Action} that may be empty.
     */
    public List<Action> getAction() {
        return action;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !instantiatesCanonical.isEmpty() || 
            !instantiatesUri.isEmpty() || 
            !basedOn.isEmpty() || 
            !replaces.isEmpty() || 
            (groupIdentifier != null) || 
            (status != null) || 
            (intent != null) || 
            (priority != null) || 
            (code != null) || 
            (subject != null) || 
            (encounter != null) || 
            (authoredOn != null) || 
            (author != null) || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            !note.isEmpty() || 
            !action.isEmpty();
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
                accept(groupIdentifier, "groupIdentifier", visitor);
                accept(status, "status", visitor);
                accept(intent, "intent", visitor);
                accept(priority, "priority", visitor);
                accept(code, "code", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(authoredOn, "authoredOn", visitor);
                accept(author, "author", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(action, "action", visitor, Action.class);
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
        RequestGroup other = (RequestGroup) obj;
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
            Objects.equals(groupIdentifier, other.groupIdentifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(intent, other.intent) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(code, other.code) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(authoredOn, other.authoredOn) && 
            Objects.equals(author, other.author) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(note, other.note) && 
            Objects.equals(action, other.action);
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
                groupIdentifier, 
                status, 
                intent, 
                priority, 
                code, 
                subject, 
                encounter, 
                authoredOn, 
                author, 
                reasonCode, 
                reasonReference, 
                note, 
                action);
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
        private Identifier groupIdentifier;
        private RequestStatus status;
        private RequestIntent intent;
        private RequestPriority priority;
        private CodeableConcept code;
        private Reference subject;
        private Reference encounter;
        private DateTime authoredOn;
        private Reference author;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Action> action = new ArrayList<>();

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
         * Allows a service to provide a unique, business identifier for the request.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier
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
         * Allows a service to provide a unique, business identifier for the request.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier
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
         * A canonical URL referencing a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this request.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A canonical URL referencing a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this request.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical = new ArrayList<>(instantiatesCanonical);
            return this;
        }

        /**
         * A URL referencing an externally defined protocol, guideline, orderset or other definition that is adhered to in whole 
         * or in part by this request.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A URL referencing an externally defined protocol, guideline, orderset or other definition that is adhered to in whole 
         * or in part by this request.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri = new ArrayList<>(instantiatesUri);
            return this;
        }

        /**
         * A plan, proposal or order that is fulfilled in whole or in part by this request.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param basedOn
         *     Fulfills plan, proposal, or order
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
         * A plan, proposal or order that is fulfilled in whole or in part by this request.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param basedOn
         *     Fulfills plan, proposal, or order
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
         * Completed or terminated request(s) whose function is taken by this new request.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param replaces
         *     Request(s) replaced by this request
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
         * Completed or terminated request(s) whose function is taken by this new request.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param replaces
         *     Request(s) replaced by this request
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder replaces(Collection<Reference> replaces) {
            this.replaces = new ArrayList<>(replaces);
            return this;
        }

        /**
         * A shared identifier common to all requests that were authorized more or less simultaneously by a single author, 
         * representing the identifier of the requisition, prescription or similar form.
         * 
         * @param groupIdentifier
         *     Composite request this is part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder groupIdentifier(Identifier groupIdentifier) {
            this.groupIdentifier = groupIdentifier;
            return this;
        }

        /**
         * The current state of the request. For request groups, the status reflects the status of all the requests in the group.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     draft | active | on-hold | revoked | completed | entered-in-error | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(RequestStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Indicates the level of authority/intentionality associated with the request and where the request fits into the 
         * workflow chain.
         * 
         * <p>This element is required.
         * 
         * @param intent
         *     proposal | plan | directive | order | original-order | reflex-order | filler-order | instance-order | option
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder intent(RequestIntent intent) {
            this.intent = intent;
            return this;
        }

        /**
         * Indicates how quickly the request should be addressed with respect to other requests.
         * 
         * @param priority
         *     routine | urgent | asap | stat
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priority(RequestPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * A code that identifies what the overall request group is.
         * 
         * @param code
         *     What's being requested/ordered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * The subject for which the request group was created.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param subject
         *     Who the request group is about
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Describes the context of the request group, if any.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param encounter
         *     Created as part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * Indicates when the request group was created.
         * 
         * @param authoredOn
         *     When the request group was authored
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authoredOn(DateTime authoredOn) {
            this.authoredOn = authoredOn;
            return this;
        }

        /**
         * Provides a reference to the author of the request group.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param author
         *     Device or practitioner that authored the request group
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Reference author) {
            this.author = author;
            return this;
        }

        /**
         * Describes the reason for the request group in coded or textual form.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Why the request group is needed
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
         * Describes the reason for the request group in coded or textual form.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Why the request group is needed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * Indicates another resource whose existence justifies this request group.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Why the request group is needed
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
         * Indicates another resource whose existence justifies this request group.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Why the request group is needed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * Provides a mechanism to communicate additional information about the response.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Additional notes about the response
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
         * Provides a mechanism to communicate additional information about the response.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Additional notes about the response
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
         * The actions, if any, produced by the evaluation of the artifact.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param action
         *     Proposed actions, if any
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder action(Action... action) {
            for (Action value : action) {
                this.action.add(value);
            }
            return this;
        }

        /**
         * The actions, if any, produced by the evaluation of the artifact.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param action
         *     Proposed actions, if any
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder action(Collection<Action> action) {
            this.action = new ArrayList<>(action);
            return this;
        }

        /**
         * Build the {@link RequestGroup}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>intent</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link RequestGroup}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid RequestGroup per the base specification
         */
        @Override
        public RequestGroup build() {
            RequestGroup requestGroup = new RequestGroup(this);
            if (validating) {
                validate(requestGroup);
            }
            return requestGroup;
        }

        protected void validate(RequestGroup requestGroup) {
            super.validate(requestGroup);
            ValidationSupport.checkList(requestGroup.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(requestGroup.instantiatesCanonical, "instantiatesCanonical", Canonical.class);
            ValidationSupport.checkList(requestGroup.instantiatesUri, "instantiatesUri", Uri.class);
            ValidationSupport.checkList(requestGroup.basedOn, "basedOn", Reference.class);
            ValidationSupport.checkList(requestGroup.replaces, "replaces", Reference.class);
            ValidationSupport.requireNonNull(requestGroup.status, "status");
            ValidationSupport.requireNonNull(requestGroup.intent, "intent");
            ValidationSupport.checkList(requestGroup.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.checkList(requestGroup.reasonReference, "reasonReference", Reference.class);
            ValidationSupport.checkList(requestGroup.note, "note", Annotation.class);
            ValidationSupport.checkList(requestGroup.action, "action", Action.class);
            ValidationSupport.checkReferenceType(requestGroup.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(requestGroup.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(requestGroup.author, "author", "Device", "Practitioner", "PractitionerRole");
            ValidationSupport.checkReferenceType(requestGroup.reasonReference, "reasonReference", "Condition", "Observation", "DiagnosticReport", "DocumentReference");
        }

        protected Builder from(RequestGroup requestGroup) {
            super.from(requestGroup);
            identifier.addAll(requestGroup.identifier);
            instantiatesCanonical.addAll(requestGroup.instantiatesCanonical);
            instantiatesUri.addAll(requestGroup.instantiatesUri);
            basedOn.addAll(requestGroup.basedOn);
            replaces.addAll(requestGroup.replaces);
            groupIdentifier = requestGroup.groupIdentifier;
            status = requestGroup.status;
            intent = requestGroup.intent;
            priority = requestGroup.priority;
            code = requestGroup.code;
            subject = requestGroup.subject;
            encounter = requestGroup.encounter;
            authoredOn = requestGroup.authoredOn;
            author = requestGroup.author;
            reasonCode.addAll(requestGroup.reasonCode);
            reasonReference.addAll(requestGroup.reasonReference);
            note.addAll(requestGroup.note);
            action.addAll(requestGroup.action);
            return this;
        }
    }

    /**
     * The actions, if any, produced by the evaluation of the artifact.
     */
    public static class Action extends BackboneElement {
        private final String prefix;
        private final String title;
        @Summary
        private final String description;
        @Summary
        private final String textEquivalent;
        @Binding(
            bindingName = "RequestPriority",
            strength = BindingStrength.Value.REQUIRED,
            description = "Identifies the level of importance to be assigned to actioning the request.",
            valueSet = "http://hl7.org/fhir/ValueSet/request-priority|4.3.0-CIBUILD"
        )
        private final RequestPriority priority;
        private final List<CodeableConcept> code;
        private final List<RelatedArtifact> documentation;
        private final List<Condition> condition;
        private final List<RelatedAction> relatedAction;
        @Choice({ DateTime.class, Age.class, Period.class, Duration.class, Range.class, Timing.class })
        private final Element timing;
        @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Device" })
        private final List<Reference> participant;
        @Binding(
            bindingName = "ActionType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The type of action to be performed.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-type"
        )
        private final CodeableConcept type;
        @Binding(
            bindingName = "ActionGroupingBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Defines organization behavior of a group.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-grouping-behavior|4.3.0-CIBUILD"
        )
        private final ActionGroupingBehavior groupingBehavior;
        @Binding(
            bindingName = "ActionSelectionBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Defines selection behavior of a group.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-selection-behavior|4.3.0-CIBUILD"
        )
        private final ActionSelectionBehavior selectionBehavior;
        @Binding(
            bindingName = "ActionRequiredBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Defines expectations around whether an action or action group is required.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-required-behavior|4.3.0-CIBUILD"
        )
        private final ActionRequiredBehavior requiredBehavior;
        @Binding(
            bindingName = "ActionPrecheckBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Defines selection frequency behavior for an action or group.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-precheck-behavior|4.3.0-CIBUILD"
        )
        private final ActionPrecheckBehavior precheckBehavior;
        @Binding(
            bindingName = "ActionCardinalityBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Defines behavior for an action or a group for how many times that item may be repeated.",
            valueSet = "http://hl7.org/fhir/ValueSet/action-cardinality-behavior|4.3.0-CIBUILD"
        )
        private final ActionCardinalityBehavior cardinalityBehavior;
        private final Reference resource;
        private final List<RequestGroup.Action> action;

        private Action(Builder builder) {
            super(builder);
            prefix = builder.prefix;
            title = builder.title;
            description = builder.description;
            textEquivalent = builder.textEquivalent;
            priority = builder.priority;
            code = Collections.unmodifiableList(builder.code);
            documentation = Collections.unmodifiableList(builder.documentation);
            condition = Collections.unmodifiableList(builder.condition);
            relatedAction = Collections.unmodifiableList(builder.relatedAction);
            timing = builder.timing;
            participant = Collections.unmodifiableList(builder.participant);
            type = builder.type;
            groupingBehavior = builder.groupingBehavior;
            selectionBehavior = builder.selectionBehavior;
            requiredBehavior = builder.requiredBehavior;
            precheckBehavior = builder.precheckBehavior;
            cardinalityBehavior = builder.cardinalityBehavior;
            resource = builder.resource;
            action = Collections.unmodifiableList(builder.action);
        }

        /**
         * A user-visible prefix for the action.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * The title of the action displayed to a user.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getTitle() {
            return title;
        }

        /**
         * A short description of the action used to provide a summary to display to the user.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when 
         * the definition is consumed by a system that might not be capable of interpreting it dynamically.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getTextEquivalent() {
            return textEquivalent;
        }

        /**
         * Indicates how quickly the action should be addressed with respect to other actions.
         * 
         * @return
         *     An immutable object of type {@link RequestPriority} that may be null.
         */
        public RequestPriority getPriority() {
            return priority;
        }

        /**
         * A code that provides meaning for the action or action group. For example, a section may have a LOINC code for a 
         * section of a documentation template.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getCode() {
            return code;
        }

        /**
         * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
         * Information resources can include inline text commentary and links to web resources.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact} that may be empty.
         */
        public List<RelatedArtifact> getDocumentation() {
            return documentation;
        }

        /**
         * An expression that describes applicability criteria, or start/stop conditions for the action.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Condition} that may be empty.
         */
        public List<Condition> getCondition() {
            return condition;
        }

        /**
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RelatedAction} that may be empty.
         */
        public List<RelatedAction> getRelatedAction() {
            return relatedAction;
        }

        /**
         * An optional value describing when the action should be performed.
         * 
         * @return
         *     An immutable object of type {@link DateTime}, {@link Age}, {@link Period}, {@link Duration}, {@link Range} or {@link 
         *     Timing} that may be null.
         */
        public Element getTiming() {
            return timing;
        }

        /**
         * The participant that should perform or be responsible for this action.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getParticipant() {
            return participant;
        }

        /**
         * The type of action to perform (create, update, remove).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Defines the grouping behavior for the action and its children.
         * 
         * @return
         *     An immutable object of type {@link ActionGroupingBehavior} that may be null.
         */
        public ActionGroupingBehavior getGroupingBehavior() {
            return groupingBehavior;
        }

        /**
         * Defines the selection behavior for the action and its children.
         * 
         * @return
         *     An immutable object of type {@link ActionSelectionBehavior} that may be null.
         */
        public ActionSelectionBehavior getSelectionBehavior() {
            return selectionBehavior;
        }

        /**
         * Defines expectations around whether an action is required.
         * 
         * @return
         *     An immutable object of type {@link ActionRequiredBehavior} that may be null.
         */
        public ActionRequiredBehavior getRequiredBehavior() {
            return requiredBehavior;
        }

        /**
         * Defines whether the action should usually be preselected.
         * 
         * @return
         *     An immutable object of type {@link ActionPrecheckBehavior} that may be null.
         */
        public ActionPrecheckBehavior getPrecheckBehavior() {
            return precheckBehavior;
        }

        /**
         * Defines whether the action can be selected multiple times.
         * 
         * @return
         *     An immutable object of type {@link ActionCardinalityBehavior} that may be null.
         */
        public ActionCardinalityBehavior getCardinalityBehavior() {
            return cardinalityBehavior;
        }

        /**
         * The resource that is the target of the action (e.g. CommunicationRequest).
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getResource() {
            return resource;
        }

        /**
         * Sub actions.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action} that may be empty.
         */
        public List<RequestGroup.Action> getAction() {
            return action;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (prefix != null) || 
                (title != null) || 
                (description != null) || 
                (textEquivalent != null) || 
                (priority != null) || 
                !code.isEmpty() || 
                !documentation.isEmpty() || 
                !condition.isEmpty() || 
                !relatedAction.isEmpty() || 
                (timing != null) || 
                !participant.isEmpty() || 
                (type != null) || 
                (groupingBehavior != null) || 
                (selectionBehavior != null) || 
                (requiredBehavior != null) || 
                (precheckBehavior != null) || 
                (cardinalityBehavior != null) || 
                (resource != null) || 
                !action.isEmpty();
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
                    accept(prefix, "prefix", visitor);
                    accept(title, "title", visitor);
                    accept(description, "description", visitor);
                    accept(textEquivalent, "textEquivalent", visitor);
                    accept(priority, "priority", visitor);
                    accept(code, "code", visitor, CodeableConcept.class);
                    accept(documentation, "documentation", visitor, RelatedArtifact.class);
                    accept(condition, "condition", visitor, Condition.class);
                    accept(relatedAction, "relatedAction", visitor, RelatedAction.class);
                    accept(timing, "timing", visitor);
                    accept(participant, "participant", visitor, Reference.class);
                    accept(type, "type", visitor);
                    accept(groupingBehavior, "groupingBehavior", visitor);
                    accept(selectionBehavior, "selectionBehavior", visitor);
                    accept(requiredBehavior, "requiredBehavior", visitor);
                    accept(precheckBehavior, "precheckBehavior", visitor);
                    accept(cardinalityBehavior, "cardinalityBehavior", visitor);
                    accept(resource, "resource", visitor);
                    accept(action, "action", visitor, RequestGroup.Action.class);
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
            Action other = (Action) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(prefix, other.prefix) && 
                Objects.equals(title, other.title) && 
                Objects.equals(description, other.description) && 
                Objects.equals(textEquivalent, other.textEquivalent) && 
                Objects.equals(priority, other.priority) && 
                Objects.equals(code, other.code) && 
                Objects.equals(documentation, other.documentation) && 
                Objects.equals(condition, other.condition) && 
                Objects.equals(relatedAction, other.relatedAction) && 
                Objects.equals(timing, other.timing) && 
                Objects.equals(participant, other.participant) && 
                Objects.equals(type, other.type) && 
                Objects.equals(groupingBehavior, other.groupingBehavior) && 
                Objects.equals(selectionBehavior, other.selectionBehavior) && 
                Objects.equals(requiredBehavior, other.requiredBehavior) && 
                Objects.equals(precheckBehavior, other.precheckBehavior) && 
                Objects.equals(cardinalityBehavior, other.cardinalityBehavior) && 
                Objects.equals(resource, other.resource) && 
                Objects.equals(action, other.action);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    prefix, 
                    title, 
                    description, 
                    textEquivalent, 
                    priority, 
                    code, 
                    documentation, 
                    condition, 
                    relatedAction, 
                    timing, 
                    participant, 
                    type, 
                    groupingBehavior, 
                    selectionBehavior, 
                    requiredBehavior, 
                    precheckBehavior, 
                    cardinalityBehavior, 
                    resource, 
                    action);
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
            private String prefix;
            private String title;
            private String description;
            private String textEquivalent;
            private RequestPriority priority;
            private List<CodeableConcept> code = new ArrayList<>();
            private List<RelatedArtifact> documentation = new ArrayList<>();
            private List<Condition> condition = new ArrayList<>();
            private List<RelatedAction> relatedAction = new ArrayList<>();
            private Element timing;
            private List<Reference> participant = new ArrayList<>();
            private CodeableConcept type;
            private ActionGroupingBehavior groupingBehavior;
            private ActionSelectionBehavior selectionBehavior;
            private ActionRequiredBehavior requiredBehavior;
            private ActionPrecheckBehavior precheckBehavior;
            private ActionCardinalityBehavior cardinalityBehavior;
            private Reference resource;
            private List<RequestGroup.Action> action = new ArrayList<>();

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
             * Convenience method for setting {@code prefix}.
             * 
             * @param prefix
             *     User-visible prefix for the action (e.g. 1. or A.)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #prefix(com.ibm.fhir.model.type.String)
             */
            public Builder prefix(java.lang.String prefix) {
                this.prefix = (prefix == null) ? null : String.of(prefix);
                return this;
            }

            /**
             * A user-visible prefix for the action.
             * 
             * @param prefix
             *     User-visible prefix for the action (e.g. 1. or A.)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder prefix(String prefix) {
                this.prefix = prefix;
                return this;
            }

            /**
             * Convenience method for setting {@code title}.
             * 
             * @param title
             *     User-visible title
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
             * The title of the action displayed to a user.
             * 
             * @param title
             *     User-visible title
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder title(String title) {
                this.title = title;
                return this;
            }

            /**
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Short description of the action
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
             * A short description of the action used to provide a summary to display to the user.
             * 
             * @param description
             *     Short description of the action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Convenience method for setting {@code textEquivalent}.
             * 
             * @param textEquivalent
             *     Static text equivalent of the action, used if the dynamic aspects cannot be interpreted by the receiving system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #textEquivalent(com.ibm.fhir.model.type.String)
             */
            public Builder textEquivalent(java.lang.String textEquivalent) {
                this.textEquivalent = (textEquivalent == null) ? null : String.of(textEquivalent);
                return this;
            }

            /**
             * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when 
             * the definition is consumed by a system that might not be capable of interpreting it dynamically.
             * 
             * @param textEquivalent
             *     Static text equivalent of the action, used if the dynamic aspects cannot be interpreted by the receiving system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder textEquivalent(String textEquivalent) {
                this.textEquivalent = textEquivalent;
                return this;
            }

            /**
             * Indicates how quickly the action should be addressed with respect to other actions.
             * 
             * @param priority
             *     routine | urgent | asap | stat
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder priority(RequestPriority priority) {
                this.priority = priority;
                return this;
            }

            /**
             * A code that provides meaning for the action or action group. For example, a section may have a LOINC code for a 
             * section of a documentation template.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     Code representing the meaning of the action or sub-actions
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept... code) {
                for (CodeableConcept value : code) {
                    this.code.add(value);
                }
                return this;
            }

            /**
             * A code that provides meaning for the action or action group. For example, a section may have a LOINC code for a 
             * section of a documentation template.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     Code representing the meaning of the action or sub-actions
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder code(Collection<CodeableConcept> code) {
                this.code = new ArrayList<>(code);
                return this;
            }

            /**
             * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
             * Information resources can include inline text commentary and links to web resources.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param documentation
             *     Supporting documentation for the intended performer of the action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(RelatedArtifact... documentation) {
                for (RelatedArtifact value : documentation) {
                    this.documentation.add(value);
                }
                return this;
            }

            /**
             * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
             * Information resources can include inline text commentary and links to web resources.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param documentation
             *     Supporting documentation for the intended performer of the action
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder documentation(Collection<RelatedArtifact> documentation) {
                this.documentation = new ArrayList<>(documentation);
                return this;
            }

            /**
             * An expression that describes applicability criteria, or start/stop conditions for the action.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param condition
             *     Whether or not the action is applicable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder condition(Condition... condition) {
                for (Condition value : condition) {
                    this.condition.add(value);
                }
                return this;
            }

            /**
             * An expression that describes applicability criteria, or start/stop conditions for the action.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param condition
             *     Whether or not the action is applicable
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder condition(Collection<Condition> condition) {
                this.condition = new ArrayList<>(condition);
                return this;
            }

            /**
             * A relationship to another action such as "before" or "30-60 minutes after start of".
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param relatedAction
             *     Relationship to another action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder relatedAction(RelatedAction... relatedAction) {
                for (RelatedAction value : relatedAction) {
                    this.relatedAction.add(value);
                }
                return this;
            }

            /**
             * A relationship to another action such as "before" or "30-60 minutes after start of".
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param relatedAction
             *     Relationship to another action
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder relatedAction(Collection<RelatedAction> relatedAction) {
                this.relatedAction = new ArrayList<>(relatedAction);
                return this;
            }

            /**
             * An optional value describing when the action should be performed.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link DateTime}</li>
             * <li>{@link Age}</li>
             * <li>{@link Period}</li>
             * <li>{@link Duration}</li>
             * <li>{@link Range}</li>
             * <li>{@link Timing}</li>
             * </ul>
             * 
             * @param timing
             *     When the action should take place
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder timing(Element timing) {
                this.timing = timing;
                return this;
            }

            /**
             * The participant that should perform or be responsible for this action.
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
             * <li>{@link Device}</li>
             * </ul>
             * 
             * @param participant
             *     Who should perform the action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder participant(Reference... participant) {
                for (Reference value : participant) {
                    this.participant.add(value);
                }
                return this;
            }

            /**
             * The participant that should perform or be responsible for this action.
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
             * <li>{@link Device}</li>
             * </ul>
             * 
             * @param participant
             *     Who should perform the action
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder participant(Collection<Reference> participant) {
                this.participant = new ArrayList<>(participant);
                return this;
            }

            /**
             * The type of action to perform (create, update, remove).
             * 
             * @param type
             *     create | update | remove | fire-event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Defines the grouping behavior for the action and its children.
             * 
             * @param groupingBehavior
             *     visual-group | logical-group | sentence-group
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder groupingBehavior(ActionGroupingBehavior groupingBehavior) {
                this.groupingBehavior = groupingBehavior;
                return this;
            }

            /**
             * Defines the selection behavior for the action and its children.
             * 
             * @param selectionBehavior
             *     any | all | all-or-none | exactly-one | at-most-one | one-or-more
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder selectionBehavior(ActionSelectionBehavior selectionBehavior) {
                this.selectionBehavior = selectionBehavior;
                return this;
            }

            /**
             * Defines expectations around whether an action is required.
             * 
             * @param requiredBehavior
             *     must | could | must-unless-documented
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder requiredBehavior(ActionRequiredBehavior requiredBehavior) {
                this.requiredBehavior = requiredBehavior;
                return this;
            }

            /**
             * Defines whether the action should usually be preselected.
             * 
             * @param precheckBehavior
             *     yes | no
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder precheckBehavior(ActionPrecheckBehavior precheckBehavior) {
                this.precheckBehavior = precheckBehavior;
                return this;
            }

            /**
             * Defines whether the action can be selected multiple times.
             * 
             * @param cardinalityBehavior
             *     single | multiple
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder cardinalityBehavior(ActionCardinalityBehavior cardinalityBehavior) {
                this.cardinalityBehavior = cardinalityBehavior;
                return this;
            }

            /**
             * The resource that is the target of the action (e.g. CommunicationRequest).
             * 
             * @param resource
             *     The target of the action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Reference resource) {
                this.resource = resource;
                return this;
            }

            /**
             * Sub actions.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param action
             *     Sub action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(RequestGroup.Action... action) {
                for (RequestGroup.Action value : action) {
                    this.action.add(value);
                }
                return this;
            }

            /**
             * Sub actions.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param action
             *     Sub action
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder action(Collection<RequestGroup.Action> action) {
                this.action = new ArrayList<>(action);
                return this;
            }

            /**
             * Build the {@link Action}
             * 
             * @return
             *     An immutable object of type {@link Action}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Action per the base specification
             */
            @Override
            public Action build() {
                Action action = new Action(this);
                if (validating) {
                    validate(action);
                }
                return action;
            }

            protected void validate(Action action) {
                super.validate(action);
                ValidationSupport.checkList(action.code, "code", CodeableConcept.class);
                ValidationSupport.checkList(action.documentation, "documentation", RelatedArtifact.class);
                ValidationSupport.checkList(action.condition, "condition", Condition.class);
                ValidationSupport.checkList(action.relatedAction, "relatedAction", RelatedAction.class);
                ValidationSupport.choiceElement(action.timing, "timing", DateTime.class, Age.class, Period.class, Duration.class, Range.class, Timing.class);
                ValidationSupport.checkList(action.participant, "participant", Reference.class);
                ValidationSupport.checkList(action.action, "action", RequestGroup.Action.class);
                ValidationSupport.checkReferenceType(action.participant, "participant", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Device");
                ValidationSupport.requireValueOrChildren(action);
            }

            protected Builder from(Action action) {
                super.from(action);
                prefix = action.prefix;
                title = action.title;
                description = action.description;
                textEquivalent = action.textEquivalent;
                priority = action.priority;
                code.addAll(action.code);
                documentation.addAll(action.documentation);
                condition.addAll(action.condition);
                relatedAction.addAll(action.relatedAction);
                timing = action.timing;
                participant.addAll(action.participant);
                type = action.type;
                groupingBehavior = action.groupingBehavior;
                selectionBehavior = action.selectionBehavior;
                requiredBehavior = action.requiredBehavior;
                precheckBehavior = action.precheckBehavior;
                cardinalityBehavior = action.cardinalityBehavior;
                resource = action.resource;
                this.action.addAll(action.action);
                return this;
            }
        }

        /**
         * An expression that describes applicability criteria, or start/stop conditions for the action.
         */
        public static class Condition extends BackboneElement {
            @Binding(
                bindingName = "ActionConditionKind",
                strength = BindingStrength.Value.REQUIRED,
                description = "The kind of condition for the action.",
                valueSet = "http://hl7.org/fhir/ValueSet/action-condition-kind|4.3.0-CIBUILD"
            )
            @Required
            private final ActionConditionKind kind;
            private final Expression expression;

            private Condition(Builder builder) {
                super(builder);
                kind = builder.kind;
                expression = builder.expression;
            }

            /**
             * The kind of condition.
             * 
             * @return
             *     An immutable object of type {@link ActionConditionKind} that is non-null.
             */
            public ActionConditionKind getKind() {
                return kind;
            }

            /**
             * An expression that returns true or false, indicating whether or not the condition is satisfied.
             * 
             * @return
             *     An immutable object of type {@link Expression} that may be null.
             */
            public Expression getExpression() {
                return expression;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (kind != null) || 
                    (expression != null);
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
                        accept(expression, "expression", visitor);
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
                Condition other = (Condition) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(kind, other.kind) && 
                    Objects.equals(expression, other.expression);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        kind, 
                        expression);
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
                private ActionConditionKind kind;
                private Expression expression;

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
                 * The kind of condition.
                 * 
                 * <p>This element is required.
                 * 
                 * @param kind
                 *     applicability | start | stop
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder kind(ActionConditionKind kind) {
                    this.kind = kind;
                    return this;
                }

                /**
                 * An expression that returns true or false, indicating whether or not the condition is satisfied.
                 * 
                 * @param expression
                 *     Boolean-valued expression
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder expression(Expression expression) {
                    this.expression = expression;
                    return this;
                }

                /**
                 * Build the {@link Condition}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>kind</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Condition}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Condition per the base specification
                 */
                @Override
                public Condition build() {
                    Condition condition = new Condition(this);
                    if (validating) {
                        validate(condition);
                    }
                    return condition;
                }

                protected void validate(Condition condition) {
                    super.validate(condition);
                    ValidationSupport.requireNonNull(condition.kind, "kind");
                    ValidationSupport.requireValueOrChildren(condition);
                }

                protected Builder from(Condition condition) {
                    super.from(condition);
                    kind = condition.kind;
                    expression = condition.expression;
                    return this;
                }
            }
        }

        /**
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         */
        public static class RelatedAction extends BackboneElement {
            @Required
            private final Id actionId;
            @Binding(
                bindingName = "ActionRelationshipType",
                strength = BindingStrength.Value.REQUIRED,
                description = "Defines the types of relationships between actions.",
                valueSet = "http://hl7.org/fhir/ValueSet/action-relationship-type|4.3.0-CIBUILD"
            )
            @Required
            private final ActionRelationshipType relationship;
            @Choice({ Duration.class, Range.class })
            private final Element offset;

            private RelatedAction(Builder builder) {
                super(builder);
                actionId = builder.actionId;
                relationship = builder.relationship;
                offset = builder.offset;
            }

            /**
             * The element id of the action this is related to.
             * 
             * @return
             *     An immutable object of type {@link Id} that is non-null.
             */
            public Id getActionId() {
                return actionId;
            }

            /**
             * The relationship of this action to the related action.
             * 
             * @return
             *     An immutable object of type {@link ActionRelationshipType} that is non-null.
             */
            public ActionRelationshipType getRelationship() {
                return relationship;
            }

            /**
             * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
             * 
             * @return
             *     An immutable object of type {@link Duration} or {@link Range} that may be null.
             */
            public Element getOffset() {
                return offset;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (actionId != null) || 
                    (relationship != null) || 
                    (offset != null);
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
                        accept(actionId, "actionId", visitor);
                        accept(relationship, "relationship", visitor);
                        accept(offset, "offset", visitor);
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
                RelatedAction other = (RelatedAction) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(actionId, other.actionId) && 
                    Objects.equals(relationship, other.relationship) && 
                    Objects.equals(offset, other.offset);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        actionId, 
                        relationship, 
                        offset);
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
                private Id actionId;
                private ActionRelationshipType relationship;
                private Element offset;

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
                 * The element id of the action this is related to.
                 * 
                 * <p>This element is required.
                 * 
                 * @param actionId
                 *     What action this is related to
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder actionId(Id actionId) {
                    this.actionId = actionId;
                    return this;
                }

                /**
                 * The relationship of this action to the related action.
                 * 
                 * <p>This element is required.
                 * 
                 * @param relationship
                 *     before-start | before | before-end | concurrent-with-start | concurrent | concurrent-with-end | after-start | after | 
                 *     after-end
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder relationship(ActionRelationshipType relationship) {
                    this.relationship = relationship;
                    return this;
                }

                /**
                 * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Duration}</li>
                 * <li>{@link Range}</li>
                 * </ul>
                 * 
                 * @param offset
                 *     Time offset for the relationship
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder offset(Element offset) {
                    this.offset = offset;
                    return this;
                }

                /**
                 * Build the {@link RelatedAction}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>actionId</li>
                 * <li>relationship</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link RelatedAction}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid RelatedAction per the base specification
                 */
                @Override
                public RelatedAction build() {
                    RelatedAction relatedAction = new RelatedAction(this);
                    if (validating) {
                        validate(relatedAction);
                    }
                    return relatedAction;
                }

                protected void validate(RelatedAction relatedAction) {
                    super.validate(relatedAction);
                    ValidationSupport.requireNonNull(relatedAction.actionId, "actionId");
                    ValidationSupport.requireNonNull(relatedAction.relationship, "relationship");
                    ValidationSupport.choiceElement(relatedAction.offset, "offset", Duration.class, Range.class);
                    ValidationSupport.requireValueOrChildren(relatedAction);
                }

                protected Builder from(RelatedAction relatedAction) {
                    super.from(relatedAction);
                    actionId = relatedAction.actionId;
                    relationship = relatedAction.relationship;
                    offset = relatedAction.offset;
                    return this;
                }
            }
        }
    }
}
