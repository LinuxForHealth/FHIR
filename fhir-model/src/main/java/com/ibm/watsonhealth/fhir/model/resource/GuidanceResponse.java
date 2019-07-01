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

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DataRequirement;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.GuidanceResponseStatus;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A guidance response is the formal response to a guidance request, including any output parameters returned by the 
 * evaluation, as well as the description of any proposed actions to be taken.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class GuidanceResponse extends DomainResource {
    private final Identifier requestIdentifier;
    private final List<Identifier> identifier;
    private final Element module;
    private final GuidanceResponseStatus status;
    private final Reference subject;
    private final Reference encounter;
    private final DateTime occurrenceDateTime;
    private final Reference performer;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<Annotation> note;
    private final List<Reference> evaluationMessage;
    private final Reference outputParameters;
    private final Reference result;
    private final List<DataRequirement> dataRequirement;

    private volatile int hashCode;

    private GuidanceResponse(Builder builder) {
        super(builder);
        requestIdentifier = builder.requestIdentifier;
        identifier = Collections.unmodifiableList(builder.identifier);
        module = ValidationSupport.requireChoiceElement(builder.module, "module", Uri.class, Canonical.class, CodeableConcept.class);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        subject = builder.subject;
        encounter = builder.encounter;
        occurrenceDateTime = builder.occurrenceDateTime;
        performer = builder.performer;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        note = Collections.unmodifiableList(builder.note);
        evaluationMessage = Collections.unmodifiableList(builder.evaluationMessage);
        outputParameters = builder.outputParameters;
        result = builder.result;
        dataRequirement = Collections.unmodifiableList(builder.dataRequirement);
    }

    /**
     * <p>
     * The identifier of the request associated with this response. If an identifier was given as part of the request, it 
     * will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getRequestIdentifier() {
        return requestIdentifier;
    }

    /**
     * <p>
     * Allows a service to provide unique, business identifiers for the response.
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
     * An identifier, CodeableConcept or canonical reference to the guidance that was requested.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getModule() {
        return module;
    }

    /**
     * <p>
     * The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in 
     * order to complete the evaluation, the engine may require more information. In this case, the status will be data-
     * required, and the response will contain a description of the additional required information. If the evaluation 
     * completed successfully, but the engine determines that a potentially more accurate response could be provided if more 
     * data was available, the status will be data-requested, and the response will contain a description of the additional 
     * requested information.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link GuidanceResponseStatus}.
     */
    public GuidanceResponseStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The patient for which the request was processed.
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
     * The encounter during which this response was created or to which the creation of this record is tightly associated.
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
     * Indicates when the guidance response was processed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getOccurrenceDateTime() {
        return occurrenceDateTime;
    }

    /**
     * <p>
     * Provides a reference to the device that performed the guidance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPerformer() {
        return performer;
    }

    /**
     * <p>
     * Describes the reason for the guidance response in coded or textual form.
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
     * Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed 
     * by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an 
     * indication of the cause for the response.
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
     * Provides a mechanism to communicate additional information about the response.
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
     * Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may 
     * produce informational or warning messages. These messages will be provided by this element.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getEvaluationMessage() {
        return evaluationMessage;
    }

    /**
     * <p>
     * The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as 
     * procedure or communication requests that are returned as part of the operation result. However, modules may define 
     * specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOutputParameters() {
        return outputParameters;
    }

    /**
     * <p>
     * The actions, if any, produced by the evaluation of the artifact.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getResult() {
        return result;
    }

    /**
     * <p>
     * If the evaluation could not be completed due to lack of information, or additional information would potentially 
     * result in a more accurate response, this element will a description of the data required in order to proceed with the 
     * evaluation. A subsequent request to the service should include this data.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link DataRequirement}.
     */
    public List<DataRequirement> getDataRequirement() {
        return dataRequirement;
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
                accept(requestIdentifier, "requestIdentifier", visitor);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(module, "module", visitor, true);
                accept(status, "status", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(occurrenceDateTime, "occurrenceDateTime", visitor);
                accept(performer, "performer", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(evaluationMessage, "evaluationMessage", visitor, Reference.class);
                accept(outputParameters, "outputParameters", visitor);
                accept(result, "result", visitor);
                accept(dataRequirement, "dataRequirement", visitor, DataRequirement.class);
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
        GuidanceResponse other = (GuidanceResponse) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(requestIdentifier, other.requestIdentifier) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(module, other.module) && 
            Objects.equals(status, other.status) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(occurrenceDateTime, other.occurrenceDateTime) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(note, other.note) && 
            Objects.equals(evaluationMessage, other.evaluationMessage) && 
            Objects.equals(outputParameters, other.outputParameters) && 
            Objects.equals(result, other.result) && 
            Objects.equals(dataRequirement, other.dataRequirement);
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
                requestIdentifier, 
                identifier, 
                module, 
                status, 
                subject, 
                encounter, 
                occurrenceDateTime, 
                performer, 
                reasonCode, 
                reasonReference, 
                note, 
                evaluationMessage, 
                outputParameters, 
                result, 
                dataRequirement);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(module, status).from(this);
    }

    public Builder toBuilder(Element module, GuidanceResponseStatus status) {
        return new Builder(module, status).from(this);
    }

    public static Builder builder(Element module, GuidanceResponseStatus status) {
        return new Builder(module, status);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Element module;
        private final GuidanceResponseStatus status;

        // optional
        private Identifier requestIdentifier;
        private List<Identifier> identifier = new ArrayList<>();
        private Reference subject;
        private Reference encounter;
        private DateTime occurrenceDateTime;
        private Reference performer;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Reference> evaluationMessage = new ArrayList<>();
        private Reference outputParameters;
        private Reference result;
        private List<DataRequirement> dataRequirement = new ArrayList<>();

        private Builder(Element module, GuidanceResponseStatus status) {
            super();
            this.module = module;
            this.status = status;
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
         * The identifier of the request associated with this response. If an identifier was given as part of the request, it 
         * will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.
         * </p>
         * 
         * @param requestIdentifier
         *     The identifier of the request associated with this response, if any
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder requestIdentifier(Identifier requestIdentifier) {
            this.requestIdentifier = requestIdentifier;
            return this;
        }

        /**
         * <p>
         * Allows a service to provide unique, business identifiers for the response.
         * </p>
         * 
         * @param identifier
         *     Business identifier
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
         * Allows a service to provide unique, business identifiers for the response.
         * </p>
         * 
         * @param identifier
         *     Business identifier
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
         * The patient for which the request was processed.
         * </p>
         * 
         * @param subject
         *     Patient the request was performed for
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * The encounter during which this response was created or to which the creation of this record is tightly associated.
         * </p>
         * 
         * @param encounter
         *     Encounter during which the response was returned
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
         * Indicates when the guidance response was processed.
         * </p>
         * 
         * @param occurrenceDateTime
         *     When the guidance response was processed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder occurrenceDateTime(DateTime occurrenceDateTime) {
            this.occurrenceDateTime = occurrenceDateTime;
            return this;
        }

        /**
         * <p>
         * Provides a reference to the device that performed the guidance.
         * </p>
         * 
         * @param performer
         *     Device returning the guidance
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performer(Reference performer) {
            this.performer = performer;
            return this;
        }

        /**
         * <p>
         * Describes the reason for the guidance response in coded or textual form.
         * </p>
         * 
         * @param reasonCode
         *     Why guidance is needed
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
         * Describes the reason for the guidance response in coded or textual form.
         * </p>
         * 
         * @param reasonCode
         *     Why guidance is needed
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
         * Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed 
         * by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an 
         * indication of the cause for the response.
         * </p>
         * 
         * @param reasonReference
         *     Why guidance is needed
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
         * Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed 
         * by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an 
         * indication of the cause for the response.
         * </p>
         * 
         * @param reasonReference
         *     Why guidance is needed
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
         * Provides a mechanism to communicate additional information about the response.
         * </p>
         * 
         * @param note
         *     Additional notes about the response
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
         * Provides a mechanism to communicate additional information about the response.
         * </p>
         * 
         * @param note
         *     Additional notes about the response
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
         * Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may 
         * produce informational or warning messages. These messages will be provided by this element.
         * </p>
         * 
         * @param evaluationMessage
         *     Messages resulting from the evaluation of the artifact or artifacts
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder evaluationMessage(Reference... evaluationMessage) {
            for (Reference value : evaluationMessage) {
                this.evaluationMessage.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may 
         * produce informational or warning messages. These messages will be provided by this element.
         * </p>
         * 
         * @param evaluationMessage
         *     Messages resulting from the evaluation of the artifact or artifacts
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder evaluationMessage(Collection<Reference> evaluationMessage) {
            this.evaluationMessage.addAll(evaluationMessage);
            return this;
        }

        /**
         * <p>
         * The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as 
         * procedure or communication requests that are returned as part of the operation result. However, modules may define 
         * specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.
         * </p>
         * 
         * @param outputParameters
         *     The output parameters of the evaluation, if any
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder outputParameters(Reference outputParameters) {
            this.outputParameters = outputParameters;
            return this;
        }

        /**
         * <p>
         * The actions, if any, produced by the evaluation of the artifact.
         * </p>
         * 
         * @param result
         *     Proposed actions, if any
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder result(Reference result) {
            this.result = result;
            return this;
        }

        /**
         * <p>
         * If the evaluation could not be completed due to lack of information, or additional information would potentially 
         * result in a more accurate response, this element will a description of the data required in order to proceed with the 
         * evaluation. A subsequent request to the service should include this data.
         * </p>
         * 
         * @param dataRequirement
         *     Additional required data
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dataRequirement(DataRequirement... dataRequirement) {
            for (DataRequirement value : dataRequirement) {
                this.dataRequirement.add(value);
            }
            return this;
        }

        /**
         * <p>
         * If the evaluation could not be completed due to lack of information, or additional information would potentially 
         * result in a more accurate response, this element will a description of the data required in order to proceed with the 
         * evaluation. A subsequent request to the service should include this data.
         * </p>
         * 
         * @param dataRequirement
         *     Additional required data
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dataRequirement(Collection<DataRequirement> dataRequirement) {
            this.dataRequirement.addAll(dataRequirement);
            return this;
        }

        @Override
        public GuidanceResponse build() {
            return new GuidanceResponse(this);
        }

        private Builder from(GuidanceResponse guidanceResponse) {
            id = guidanceResponse.id;
            meta = guidanceResponse.meta;
            implicitRules = guidanceResponse.implicitRules;
            language = guidanceResponse.language;
            text = guidanceResponse.text;
            contained.addAll(guidanceResponse.contained);
            extension.addAll(guidanceResponse.extension);
            modifierExtension.addAll(guidanceResponse.modifierExtension);
            requestIdentifier = guidanceResponse.requestIdentifier;
            identifier.addAll(guidanceResponse.identifier);
            subject = guidanceResponse.subject;
            encounter = guidanceResponse.encounter;
            occurrenceDateTime = guidanceResponse.occurrenceDateTime;
            performer = guidanceResponse.performer;
            reasonCode.addAll(guidanceResponse.reasonCode);
            reasonReference.addAll(guidanceResponse.reasonReference);
            note.addAll(guidanceResponse.note);
            evaluationMessage.addAll(guidanceResponse.evaluationMessage);
            outputParameters = guidanceResponse.outputParameters;
            result = guidanceResponse.result;
            dataRequirement.addAll(guidanceResponse.dataRequirement);
            return this;
        }
    }
}
