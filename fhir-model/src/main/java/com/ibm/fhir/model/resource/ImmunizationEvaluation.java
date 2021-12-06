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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ImmunizationEvaluationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Describes a comparison of an immunization event against published recommendations to determine if the administration 
 * is "valid" in relation to those recommendations.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ImmunizationEvaluation extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "ImmunizationEvaluationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The status of the evaluation being done.",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-evaluation-status|4.1.0"
    )
    @Required
    private final ImmunizationEvaluationStatus status;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference patient;
    private final DateTime date;
    @ReferenceTarget({ "Organization" })
    private final Reference authority;
    @Summary
    @Binding(
        bindingName = "EvaluationTargetDisease",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The vaccine preventable disease the dose is being evaluated against.",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-evaluation-target-disease"
    )
    @Required
    private final CodeableConcept targetDisease;
    @Summary
    @ReferenceTarget({ "Immunization" })
    @Required
    private final Reference immunizationEvent;
    @Summary
    @Binding(
        bindingName = "EvaluationDoseStatus",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The status of the administered dose relative to the published recommendations for the target disease.",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-evaluation-dose-status"
    )
    @Required
    private final CodeableConcept doseStatus;
    @Binding(
        bindingName = "EvaluationDoseStatusReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The reason the dose status was assigned.",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-evaluation-dose-status-reason"
    )
    private final List<CodeableConcept> doseStatusReason;
    private final String description;
    private final String series;
    @Choice({ PositiveInt.class, String.class })
    private final Element doseNumber;
    @Choice({ PositiveInt.class, String.class })
    private final Element seriesDoses;

    private ImmunizationEvaluation(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        patient = builder.patient;
        date = builder.date;
        authority = builder.authority;
        targetDisease = builder.targetDisease;
        immunizationEvent = builder.immunizationEvent;
        doseStatus = builder.doseStatus;
        doseStatusReason = Collections.unmodifiableList(builder.doseStatusReason);
        description = builder.description;
        series = builder.series;
        doseNumber = builder.doseNumber;
        seriesDoses = builder.seriesDoses;
    }

    /**
     * A unique identifier assigned to this immunization evaluation record.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Indicates the current status of the evaluation of the vaccination administration event.
     * 
     * @return
     *     An immutable object of type {@link ImmunizationEvaluationStatus} that is non-null.
     */
    public ImmunizationEvaluationStatus getStatus() {
        return status;
    }

    /**
     * The individual for whom the evaluation is being done.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * The date the evaluation of the vaccine administration event was performed.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * Indicates the authority who published the protocol (e.g. ACIP).
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAuthority() {
        return authority;
    }

    /**
     * The vaccine preventable disease the dose is being evaluated against.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getTargetDisease() {
        return targetDisease;
    }

    /**
     * The vaccine administration event being evaluated.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getImmunizationEvent() {
        return immunizationEvent;
    }

    /**
     * Indicates if the dose is valid or not valid with respect to the published recommendations.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getDoseStatus() {
        return doseStatus;
    }

    /**
     * Provides an explanation as to why the vaccine administration event is valid or not relative to the published 
     * recommendations.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getDoseStatusReason() {
        return doseStatusReason;
    }

    /**
     * Additional information about the evaluation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * One possible path to achieve presumed immunity against a disease - within the context of an authority.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getSeries() {
        return series;
    }

    /**
     * Nominal position in a series.
     * 
     * @return
     *     An immutable object of type {@link PositiveInt} or {@link String} that may be null.
     */
    public Element getDoseNumber() {
        return doseNumber;
    }

    /**
     * The recommended number of doses to achieve immunity.
     * 
     * @return
     *     An immutable object of type {@link PositiveInt} or {@link String} that may be null.
     */
    public Element getSeriesDoses() {
        return seriesDoses;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (patient != null) || 
            (date != null) || 
            (authority != null) || 
            (targetDisease != null) || 
            (immunizationEvent != null) || 
            (doseStatus != null) || 
            !doseStatusReason.isEmpty() || 
            (description != null) || 
            (series != null) || 
            (doseNumber != null) || 
            (seriesDoses != null);
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
                accept(status, "status", visitor);
                accept(patient, "patient", visitor);
                accept(date, "date", visitor);
                accept(authority, "authority", visitor);
                accept(targetDisease, "targetDisease", visitor);
                accept(immunizationEvent, "immunizationEvent", visitor);
                accept(doseStatus, "doseStatus", visitor);
                accept(doseStatusReason, "doseStatusReason", visitor, CodeableConcept.class);
                accept(description, "description", visitor);
                accept(series, "series", visitor);
                accept(doseNumber, "doseNumber", visitor);
                accept(seriesDoses, "seriesDoses", visitor);
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
        ImmunizationEvaluation other = (ImmunizationEvaluation) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(date, other.date) && 
            Objects.equals(authority, other.authority) && 
            Objects.equals(targetDisease, other.targetDisease) && 
            Objects.equals(immunizationEvent, other.immunizationEvent) && 
            Objects.equals(doseStatus, other.doseStatus) && 
            Objects.equals(doseStatusReason, other.doseStatusReason) && 
            Objects.equals(description, other.description) && 
            Objects.equals(series, other.series) && 
            Objects.equals(doseNumber, other.doseNumber) && 
            Objects.equals(seriesDoses, other.seriesDoses);
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
                status, 
                patient, 
                date, 
                authority, 
                targetDisease, 
                immunizationEvent, 
                doseStatus, 
                doseStatusReason, 
                description, 
                series, 
                doseNumber, 
                seriesDoses);
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
        private ImmunizationEvaluationStatus status;
        private Reference patient;
        private DateTime date;
        private Reference authority;
        private CodeableConcept targetDisease;
        private Reference immunizationEvent;
        private CodeableConcept doseStatus;
        private List<CodeableConcept> doseStatusReason = new ArrayList<>();
        private String description;
        private String series;
        private Element doseNumber;
        private Element seriesDoses;

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
         * A unique identifier assigned to this immunization evaluation record.
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
         * A unique identifier assigned to this immunization evaluation record.
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
         * Indicates the current status of the evaluation of the vaccination administration event.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     completed | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ImmunizationEvaluationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The individual for whom the evaluation is being done.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Who this evaluation is for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * The date the evaluation of the vaccine administration event was performed.
         * 
         * @param date
         *     Date evaluation was performed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * Indicates the authority who published the protocol (e.g. ACIP).
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param authority
         *     Who is responsible for publishing the recommendations
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authority(Reference authority) {
            this.authority = authority;
            return this;
        }

        /**
         * The vaccine preventable disease the dose is being evaluated against.
         * 
         * <p>This element is required.
         * 
         * @param targetDisease
         *     Evaluation target disease
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder targetDisease(CodeableConcept targetDisease) {
            this.targetDisease = targetDisease;
            return this;
        }

        /**
         * The vaccine administration event being evaluated.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Immunization}</li>
         * </ul>
         * 
         * @param immunizationEvent
         *     Immunization being evaluated
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder immunizationEvent(Reference immunizationEvent) {
            this.immunizationEvent = immunizationEvent;
            return this;
        }

        /**
         * Indicates if the dose is valid or not valid with respect to the published recommendations.
         * 
         * <p>This element is required.
         * 
         * @param doseStatus
         *     Status of the dose relative to published recommendations
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder doseStatus(CodeableConcept doseStatus) {
            this.doseStatus = doseStatus;
            return this;
        }

        /**
         * Provides an explanation as to why the vaccine administration event is valid or not relative to the published 
         * recommendations.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param doseStatusReason
         *     Reason for the dose status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder doseStatusReason(CodeableConcept... doseStatusReason) {
            for (CodeableConcept value : doseStatusReason) {
                this.doseStatusReason.add(value);
            }
            return this;
        }

        /**
         * Provides an explanation as to why the vaccine administration event is valid or not relative to the published 
         * recommendations.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param doseStatusReason
         *     Reason for the dose status
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder doseStatusReason(Collection<CodeableConcept> doseStatusReason) {
            this.doseStatusReason = new ArrayList<>(doseStatusReason);
            return this;
        }

        /**
         * Convenience method for setting {@code description}.
         * 
         * @param description
         *     Evaluation notes
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
         * Additional information about the evaluation.
         * 
         * @param description
         *     Evaluation notes
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Convenience method for setting {@code series}.
         * 
         * @param series
         *     Name of vaccine series
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #series(com.ibm.fhir.model.type.String)
         */
        public Builder series(java.lang.String series) {
            this.series = (series == null) ? null : String.of(series);
            return this;
        }

        /**
         * One possible path to achieve presumed immunity against a disease - within the context of an authority.
         * 
         * @param series
         *     Name of vaccine series
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder series(String series) {
            this.series = series;
            return this;
        }

        /**
         * Convenience method for setting {@code doseNumber} with choice type String.
         * 
         * @param doseNumber
         *     Dose number within series
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #doseNumber(Element)
         */
        public Builder doseNumber(java.lang.String doseNumber) {
            this.doseNumber = (doseNumber == null) ? null : String.of(doseNumber);
            return this;
        }

        /**
         * Nominal position in a series.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link PositiveInt}</li>
         * <li>{@link String}</li>
         * </ul>
         * 
         * @param doseNumber
         *     Dose number within series
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder doseNumber(Element doseNumber) {
            this.doseNumber = doseNumber;
            return this;
        }

        /**
         * Convenience method for setting {@code seriesDoses} with choice type String.
         * 
         * @param seriesDoses
         *     Recommended number of doses for immunity
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #seriesDoses(Element)
         */
        public Builder seriesDoses(java.lang.String seriesDoses) {
            this.seriesDoses = (seriesDoses == null) ? null : String.of(seriesDoses);
            return this;
        }

        /**
         * The recommended number of doses to achieve immunity.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link PositiveInt}</li>
         * <li>{@link String}</li>
         * </ul>
         * 
         * @param seriesDoses
         *     Recommended number of doses for immunity
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder seriesDoses(Element seriesDoses) {
            this.seriesDoses = seriesDoses;
            return this;
        }

        /**
         * Build the {@link ImmunizationEvaluation}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>patient</li>
         * <li>targetDisease</li>
         * <li>immunizationEvent</li>
         * <li>doseStatus</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ImmunizationEvaluation}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ImmunizationEvaluation per the base specification
         */
        @Override
        public ImmunizationEvaluation build() {
            ImmunizationEvaluation immunizationEvaluation = new ImmunizationEvaluation(this);
            if (validating) {
                validate(immunizationEvaluation);
            }
            return immunizationEvaluation;
        }

        protected void validate(ImmunizationEvaluation immunizationEvaluation) {
            super.validate(immunizationEvaluation);
            ValidationSupport.checkList(immunizationEvaluation.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(immunizationEvaluation.status, "status");
            ValidationSupport.requireNonNull(immunizationEvaluation.patient, "patient");
            ValidationSupport.requireNonNull(immunizationEvaluation.targetDisease, "targetDisease");
            ValidationSupport.requireNonNull(immunizationEvaluation.immunizationEvent, "immunizationEvent");
            ValidationSupport.requireNonNull(immunizationEvaluation.doseStatus, "doseStatus");
            ValidationSupport.checkList(immunizationEvaluation.doseStatusReason, "doseStatusReason", CodeableConcept.class);
            ValidationSupport.choiceElement(immunizationEvaluation.doseNumber, "doseNumber", PositiveInt.class, String.class);
            ValidationSupport.choiceElement(immunizationEvaluation.seriesDoses, "seriesDoses", PositiveInt.class, String.class);
            ValidationSupport.checkReferenceType(immunizationEvaluation.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(immunizationEvaluation.authority, "authority", "Organization");
            ValidationSupport.checkReferenceType(immunizationEvaluation.immunizationEvent, "immunizationEvent", "Immunization");
        }

        protected Builder from(ImmunizationEvaluation immunizationEvaluation) {
            super.from(immunizationEvaluation);
            identifier.addAll(immunizationEvaluation.identifier);
            status = immunizationEvaluation.status;
            patient = immunizationEvaluation.patient;
            date = immunizationEvaluation.date;
            authority = immunizationEvaluation.authority;
            targetDisease = immunizationEvaluation.targetDisease;
            immunizationEvent = immunizationEvaluation.immunizationEvent;
            doseStatus = immunizationEvaluation.doseStatus;
            doseStatusReason.addAll(immunizationEvaluation.doseStatusReason);
            description = immunizationEvaluation.description;
            series = immunizationEvaluation.series;
            doseNumber = immunizationEvaluation.doseNumber;
            seriesDoses = immunizationEvaluation.seriesDoses;
            return this;
        }
    }
}
