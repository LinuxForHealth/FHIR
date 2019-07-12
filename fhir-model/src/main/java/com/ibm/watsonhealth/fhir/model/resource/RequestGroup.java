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
import com.ibm.watsonhealth.fhir.model.type.ActionCardinalityBehavior;
import com.ibm.watsonhealth.fhir.model.type.ActionConditionKind;
import com.ibm.watsonhealth.fhir.model.type.ActionGroupingBehavior;
import com.ibm.watsonhealth.fhir.model.type.ActionPrecheckBehavior;
import com.ibm.watsonhealth.fhir.model.type.ActionRelationshipType;
import com.ibm.watsonhealth.fhir.model.type.ActionRequiredBehavior;
import com.ibm.watsonhealth.fhir.model.type.ActionSelectionBehavior;
import com.ibm.watsonhealth.fhir.model.type.Age;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Expression;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RelatedArtifact;
import com.ibm.watsonhealth.fhir.model.type.RequestIntent;
import com.ibm.watsonhealth.fhir.model.type.RequestPriority;
import com.ibm.watsonhealth.fhir.model.type.RequestStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A group of related requests that can be used to capture intended activities that have inter-dependencies such as "give 
 * this medication after that one".
 * </p>
 */
@Constraint(
    id = "rqg-1",
    level = "Rule",
    location = "RequestGroup.action",
    description = "Must have resource or action but not both",
    expression = "resource.exists() != action.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class RequestGroup extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Canonical> instantiatesCanonical;
    private final List<Uri> instantiatesUri;
    private final List<Reference> basedOn;
    private final List<Reference> replaces;
    private final Identifier groupIdentifier;
    private final RequestStatus status;
    private final RequestIntent intent;
    private final RequestPriority priority;
    private final CodeableConcept code;
    private final Reference subject;
    private final Reference encounter;
    private final DateTime authoredOn;
    private final Reference author;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<Annotation> note;
    private final List<Action> action;

    private volatile int hashCode;

    private RequestGroup(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
        instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        replaces = Collections.unmodifiableList(builder.replaces);
        groupIdentifier = builder.groupIdentifier;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        intent = ValidationSupport.requireNonNull(builder.intent, "intent");
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
     * <p>
     * Allows a service to provide a unique, business identifier for the request.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * A canonical URL referencing a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this request.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * <p>
     * A URL referencing an externally defined protocol, guideline, orderset or other definition that is adhered to in whole 
     * or in part by this request.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * <p>
     * A plan, proposal or order that is fulfilled in whole or in part by this request.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * <p>
     * Completed or terminated request(s) whose function is taken by this new request.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReplaces() {
        return replaces;
    }

    /**
     * <p>
     * A shared identifier common to all requests that were authorized more or less simultaneously by a single author, 
     * representing the identifier of the requisition, prescription or similar form.
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
     * The current state of the request. For request groups, the status reflects the status of all the requests in the group.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link RequestStatus}.
     */
    public RequestStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Indicates the level of authority/intentionality associated with the request and where the request fits into the 
     * workflow chain.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link RequestIntent}.
     */
    public RequestIntent getIntent() {
        return intent;
    }

    /**
     * <p>
     * Indicates how quickly the request should be addressed with respect to other requests.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link RequestPriority}.
     */
    public RequestPriority getPriority() {
        return priority;
    }

    /**
     * <p>
     * A code that identifies what the overall request group is.
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
     * The subject for which the request group was created.
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
     * Describes the context of the request group, if any.
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
     * Indicates when the request group was created.
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
     * Provides a reference to the author of the request group.
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
     * Describes the reason for the request group in coded or textual form.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * <p>
     * Indicates another resource whose existence justifies this request group.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * <p>
     * Provides a mechanism to communicate additional information about the response.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * <p>
     * The actions, if any, produced by the evaluation of the artifact.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Action}.
     */
    public List<Action> getAction() {
        return action;
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
        return new Builder(status, intent).from(this);
    }

    public Builder toBuilder(RequestStatus status, RequestIntent intent) {
        return new Builder(status, intent).from(this);
    }

    public static Builder builder(RequestStatus status, RequestIntent intent) {
        return new Builder(status, intent);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final RequestStatus status;
        private final RequestIntent intent;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Canonical> instantiatesCanonical = new ArrayList<>();
        private List<Uri> instantiatesUri = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> replaces = new ArrayList<>();
        private Identifier groupIdentifier;
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

        private Builder(RequestStatus status, RequestIntent intent) {
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * Allows a service to provide a unique, business identifier for the request.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Allows a service to provide a unique, business identifier for the request.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Business identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * <p>
         * A canonical URL referencing a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this request.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A canonical URL referencing a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this request.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * A URL referencing an externally defined protocol, guideline, orderset or other definition that is adhered to in whole 
         * or in part by this request.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A URL referencing an externally defined protocol, guideline, orderset or other definition that is adhered to in whole 
         * or in part by this request.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * A plan, proposal or order that is fulfilled in whole or in part by this request.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A plan, proposal or order that is fulfilled in whole or in part by this request.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param basedOn
         *     Fulfills plan, proposal, or order
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * <p>
         * Completed or terminated request(s) whose function is taken by this new request.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Completed or terminated request(s) whose function is taken by this new request.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param replaces
         *     Request(s) replaced by this request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder replaces(Collection<Reference> replaces) {
            this.replaces = new ArrayList<>(replaces);
            return this;
        }

        /**
         * <p>
         * A shared identifier common to all requests that were authorized more or less simultaneously by a single author, 
         * representing the identifier of the requisition, prescription or similar form.
         * </p>
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
         * <p>
         * Indicates how quickly the request should be addressed with respect to other requests.
         * </p>
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
         * <p>
         * A code that identifies what the overall request group is.
         * </p>
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
         * <p>
         * The subject for which the request group was created.
         * </p>
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
         * <p>
         * Describes the context of the request group, if any.
         * </p>
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
         * <p>
         * Indicates when the request group was created.
         * </p>
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
         * <p>
         * Provides a reference to the author of the request group.
         * </p>
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
         * <p>
         * Describes the reason for the request group in coded or textual form.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Describes the reason for the request group in coded or textual form.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonCode
         *     Why the request group is needed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * <p>
         * Indicates another resource whose existence justifies this request group.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Indicates another resource whose existence justifies this request group.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonReference
         *     Why the request group is needed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * <p>
         * Provides a mechanism to communicate additional information about the response.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Provides a mechanism to communicate additional information about the response.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Additional notes about the response
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * <p>
         * The actions, if any, produced by the evaluation of the artifact.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The actions, if any, produced by the evaluation of the artifact.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param action
         *     Proposed actions, if any
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder action(Collection<Action> action) {
            this.action = new ArrayList<>(action);
            return this;
        }

        @Override
        public RequestGroup build() {
            return new RequestGroup(this);
        }

        private Builder from(RequestGroup requestGroup) {
            id = requestGroup.id;
            meta = requestGroup.meta;
            implicitRules = requestGroup.implicitRules;
            language = requestGroup.language;
            text = requestGroup.text;
            contained.addAll(requestGroup.contained);
            extension.addAll(requestGroup.extension);
            modifierExtension.addAll(requestGroup.modifierExtension);
            identifier.addAll(requestGroup.identifier);
            instantiatesCanonical.addAll(requestGroup.instantiatesCanonical);
            instantiatesUri.addAll(requestGroup.instantiatesUri);
            basedOn.addAll(requestGroup.basedOn);
            replaces.addAll(requestGroup.replaces);
            groupIdentifier = requestGroup.groupIdentifier;
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
     * <p>
     * The actions, if any, produced by the evaluation of the artifact.
     * </p>
     */
    public static class Action extends BackboneElement {
        private final String prefix;
        private final String title;
        private final String description;
        private final String textEquivalent;
        private final RequestPriority priority;
        private final List<CodeableConcept> code;
        private final List<RelatedArtifact> documentation;
        private final List<Condition> condition;
        private final List<RelatedAction> relatedAction;
        private final Element timing;
        private final List<Reference> participant;
        private final CodeableConcept type;
        private final ActionGroupingBehavior groupingBehavior;
        private final ActionSelectionBehavior selectionBehavior;
        private final ActionRequiredBehavior requiredBehavior;
        private final ActionPrecheckBehavior precheckBehavior;
        private final ActionCardinalityBehavior cardinalityBehavior;
        private final Reference resource;
        private final List<RequestGroup.Action> action;

        private volatile int hashCode;

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
            timing = ValidationSupport.choiceElement(builder.timing, "timing", DateTime.class, Age.class, Period.class, Duration.class, Range.class, Timing.class);
            participant = Collections.unmodifiableList(builder.participant);
            type = builder.type;
            groupingBehavior = builder.groupingBehavior;
            selectionBehavior = builder.selectionBehavior;
            requiredBehavior = builder.requiredBehavior;
            precheckBehavior = builder.precheckBehavior;
            cardinalityBehavior = builder.cardinalityBehavior;
            resource = builder.resource;
            action = Collections.unmodifiableList(builder.action);
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * A user-visible prefix for the action.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * <p>
         * The title of the action displayed to a user.
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
         * A short description of the action used to provide a summary to display to the user.
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
         * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when 
         * the definition is consumed by a system that might not be capable of interpreting it dynamically.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getTextEquivalent() {
            return textEquivalent;
        }

        /**
         * <p>
         * Indicates how quickly the action should be addressed with respect to other actions.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link RequestPriority}.
         */
        public RequestPriority getPriority() {
            return priority;
        }

        /**
         * <p>
         * A code that provides meaning for the action or action group. For example, a section may have a LOINC code for a 
         * section of a documentation template.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getCode() {
            return code;
        }

        /**
         * <p>
         * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
         * Information resources can include inline text commentary and links to web resources.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact}.
         */
        public List<RelatedArtifact> getDocumentation() {
            return documentation;
        }

        /**
         * <p>
         * An expression that describes applicability criteria, or start/stop conditions for the action.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Condition}.
         */
        public List<Condition> getCondition() {
            return condition;
        }

        /**
         * <p>
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RelatedAction}.
         */
        public List<RelatedAction> getRelatedAction() {
            return relatedAction;
        }

        /**
         * <p>
         * An optional value describing when the action should be performed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getTiming() {
            return timing;
        }

        /**
         * <p>
         * The participant that should perform or be responsible for this action.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getParticipant() {
            return participant;
        }

        /**
         * <p>
         * The type of action to perform (create, update, remove).
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
         * Defines the grouping behavior for the action and its children.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActionGroupingBehavior}.
         */
        public ActionGroupingBehavior getGroupingBehavior() {
            return groupingBehavior;
        }

        /**
         * <p>
         * Defines the selection behavior for the action and its children.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActionSelectionBehavior}.
         */
        public ActionSelectionBehavior getSelectionBehavior() {
            return selectionBehavior;
        }

        /**
         * <p>
         * Defines expectations around whether an action is required.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActionRequiredBehavior}.
         */
        public ActionRequiredBehavior getRequiredBehavior() {
            return requiredBehavior;
        }

        /**
         * <p>
         * Defines whether the action should usually be preselected.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActionPrecheckBehavior}.
         */
        public ActionPrecheckBehavior getPrecheckBehavior() {
            return precheckBehavior;
        }

        /**
         * <p>
         * Defines whether the action can be selected multiple times.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ActionCardinalityBehavior}.
         */
        public ActionCardinalityBehavior getCardinalityBehavior() {
            return cardinalityBehavior;
        }

        /**
         * <p>
         * The resource that is the target of the action (e.g. CommunicationRequest).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getResource() {
            return resource;
        }

        /**
         * <p>
         * Sub actions.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action}.
         */
        public List<RequestGroup.Action> getAction() {
            return action;
        }

        @Override
        protected boolean hasChildren() {
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
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
            // optional
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
             * <p>
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * </p>
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
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
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
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
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
             * <p>
             * A user-visible prefix for the action.
             * </p>
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
             * <p>
             * The title of the action displayed to a user.
             * </p>
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
             * <p>
             * A short description of the action used to provide a summary to display to the user.
             * </p>
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
             * <p>
             * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when 
             * the definition is consumed by a system that might not be capable of interpreting it dynamically.
             * </p>
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
             * <p>
             * Indicates how quickly the action should be addressed with respect to other actions.
             * </p>
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
             * <p>
             * A code that provides meaning for the action or action group. For example, a section may have a LOINC code for a 
             * section of a documentation template.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * A code that provides meaning for the action or action group. For example, a section may have a LOINC code for a 
             * section of a documentation template.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param code
             *     Code representing the meaning of the action or sub-actions
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(Collection<CodeableConcept> code) {
                this.code = new ArrayList<>(code);
                return this;
            }

            /**
             * <p>
             * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
             * Information resources can include inline text commentary and links to web resources.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. 
             * Information resources can include inline text commentary and links to web resources.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param documentation
             *     Supporting documentation for the intended performer of the action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(Collection<RelatedArtifact> documentation) {
                this.documentation = new ArrayList<>(documentation);
                return this;
            }

            /**
             * <p>
             * An expression that describes applicability criteria, or start/stop conditions for the action.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * An expression that describes applicability criteria, or start/stop conditions for the action.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param condition
             *     Whether or not the action is applicable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder condition(Collection<Condition> condition) {
                this.condition = new ArrayList<>(condition);
                return this;
            }

            /**
             * <p>
             * A relationship to another action such as "before" or "30-60 minutes after start of".
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * A relationship to another action such as "before" or "30-60 minutes after start of".
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param relatedAction
             *     Relationship to another action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder relatedAction(Collection<RelatedAction> relatedAction) {
                this.relatedAction = new ArrayList<>(relatedAction);
                return this;
            }

            /**
             * <p>
             * An optional value describing when the action should be performed.
             * </p>
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
             * <p>
             * The participant that should perform or be responsible for this action.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * The participant that should perform or be responsible for this action.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param participant
             *     Who should perform the action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder participant(Collection<Reference> participant) {
                this.participant = new ArrayList<>(participant);
                return this;
            }

            /**
             * <p>
             * The type of action to perform (create, update, remove).
             * </p>
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
             * <p>
             * Defines the grouping behavior for the action and its children.
             * </p>
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
             * <p>
             * Defines the selection behavior for the action and its children.
             * </p>
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
             * <p>
             * Defines expectations around whether an action is required.
             * </p>
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
             * <p>
             * Defines whether the action should usually be preselected.
             * </p>
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
             * <p>
             * Defines whether the action can be selected multiple times.
             * </p>
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
             * <p>
             * The resource that is the target of the action (e.g. CommunicationRequest).
             * </p>
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
             * <p>
             * Sub actions.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Sub actions.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param action
             *     Sub action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(Collection<RequestGroup.Action> action) {
                this.action = new ArrayList<>(action);
                return this;
            }

            @Override
            public Action build() {
                return new Action(this);
            }

            private Builder from(Action action) {
                id = action.id;
                extension.addAll(action.extension);
                modifierExtension.addAll(action.modifierExtension);
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
         * <p>
         * An expression that describes applicability criteria, or start/stop conditions for the action.
         * </p>
         */
        public static class Condition extends BackboneElement {
            private final ActionConditionKind kind;
            private final Expression expression;

            private volatile int hashCode;

            private Condition(Builder builder) {
                super(builder);
                kind = ValidationSupport.requireNonNull(builder.kind, "kind");
                expression = builder.expression;
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * The kind of condition.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ActionConditionKind}.
             */
            public ActionConditionKind getKind() {
                return kind;
            }

            /**
             * <p>
             * An expression that returns true or false, indicating whether or not the condition is satisfied.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Expression}.
             */
            public Expression getExpression() {
                return expression;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (kind != null) || 
                    (expression != null);
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
                        accept(expression, "expression", visitor);
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
                return new Builder(kind).from(this);
            }

            public Builder toBuilder(ActionConditionKind kind) {
                return new Builder(kind).from(this);
            }

            public static Builder builder(ActionConditionKind kind) {
                return new Builder(kind);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final ActionConditionKind kind;

                // optional
                private Expression expression;

                private Builder(ActionConditionKind kind) {
                    super();
                    this.kind = kind;
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
                 *     A reference to this Builder instance
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
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
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
                 * <p>
                 * May be used to represent additional information that is not part of the basic definition of the element. To make the 
                 * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
                 * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
                 * of the definition of the extension.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
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
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
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
                 * <p>
                 * An expression that returns true or false, indicating whether or not the condition is satisfied.
                 * </p>
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

                @Override
                public Condition build() {
                    return new Condition(this);
                }

                private Builder from(Condition condition) {
                    id = condition.id;
                    extension.addAll(condition.extension);
                    modifierExtension.addAll(condition.modifierExtension);
                    expression = condition.expression;
                    return this;
                }
            }
        }

        /**
         * <p>
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         * </p>
         */
        public static class RelatedAction extends BackboneElement {
            private final Id actionId;
            private final ActionRelationshipType relationship;
            private final Element offset;

            private volatile int hashCode;

            private RelatedAction(Builder builder) {
                super(builder);
                actionId = ValidationSupport.requireNonNull(builder.actionId, "actionId");
                relationship = ValidationSupport.requireNonNull(builder.relationship, "relationship");
                offset = ValidationSupport.choiceElement(builder.offset, "offset", Duration.class, Range.class);
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * The element id of the action this is related to.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Id}.
             */
            public Id getActionId() {
                return actionId;
            }

            /**
             * <p>
             * The relationship of this action to the related action.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ActionRelationshipType}.
             */
            public ActionRelationshipType getRelationship() {
                return relationship;
            }

            /**
             * <p>
             * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getOffset() {
                return offset;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (actionId != null) || 
                    (relationship != null) || 
                    (offset != null);
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
                        accept(actionId, "actionId", visitor);
                        accept(relationship, "relationship", visitor);
                        accept(offset, "offset", visitor);
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
                return new Builder(actionId, relationship).from(this);
            }

            public Builder toBuilder(Id actionId, ActionRelationshipType relationship) {
                return new Builder(actionId, relationship).from(this);
            }

            public static Builder builder(Id actionId, ActionRelationshipType relationship) {
                return new Builder(actionId, relationship);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Id actionId;
                private final ActionRelationshipType relationship;

                // optional
                private Element offset;

                private Builder(Id actionId, ActionRelationshipType relationship) {
                    super();
                    this.actionId = actionId;
                    this.relationship = relationship;
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
                 *     A reference to this Builder instance
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
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
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
                 * <p>
                 * May be used to represent additional information that is not part of the basic definition of the element. To make the 
                 * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
                 * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
                 * of the definition of the extension.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
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
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
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
                 * <p>
                 * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
                 * </p>
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

                @Override
                public RelatedAction build() {
                    return new RelatedAction(this);
                }

                private Builder from(RelatedAction relatedAction) {
                    id = relatedAction.id;
                    extension.addAll(relatedAction.extension);
                    modifierExtension.addAll(relatedAction.modifierExtension);
                    offset = relatedAction.offset;
                    return this;
                }
            }
        }
    }
}
