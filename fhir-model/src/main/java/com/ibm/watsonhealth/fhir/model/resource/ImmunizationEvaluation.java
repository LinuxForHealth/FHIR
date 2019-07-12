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

import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.ImmunizationEvaluationStatus;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Describes a comparison of an immunization event against published recommendations to determine if the administration 
 * is "valid" in relation to those recommendations.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ImmunizationEvaluation extends DomainResource {
    private final List<Identifier> identifier;
    private final ImmunizationEvaluationStatus status;
    private final Reference patient;
    private final DateTime date;
    private final Reference authority;
    private final CodeableConcept targetDisease;
    private final Reference immunizationEvent;
    private final CodeableConcept doseStatus;
    private final List<CodeableConcept> doseStatusReason;
    private final String description;
    private final String series;
    private final Element doseNumber;
    private final Element seriesDoses;

    private volatile int hashCode;

    private ImmunizationEvaluation(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        date = builder.date;
        authority = builder.authority;
        targetDisease = ValidationSupport.requireNonNull(builder.targetDisease, "targetDisease");
        immunizationEvent = ValidationSupport.requireNonNull(builder.immunizationEvent, "immunizationEvent");
        doseStatus = ValidationSupport.requireNonNull(builder.doseStatus, "doseStatus");
        doseStatusReason = Collections.unmodifiableList(builder.doseStatusReason);
        description = builder.description;
        series = builder.series;
        doseNumber = ValidationSupport.choiceElement(builder.doseNumber, "doseNumber", PositiveInt.class, String.class);
        seriesDoses = ValidationSupport.choiceElement(builder.seriesDoses, "seriesDoses", PositiveInt.class, String.class);
    }

    /**
     * <p>
     * A unique identifier assigned to this immunization evaluation record.
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
     * Indicates the current status of the evaluation of the vaccination administration event.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ImmunizationEvaluationStatus}.
     */
    public ImmunizationEvaluationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The individual for whom the evaluation is being done.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * <p>
     * The date the evaluation of the vaccine administration event was performed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * <p>
     * Indicates the authority who published the protocol (e.g. ACIP).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAuthority() {
        return authority;
    }

    /**
     * <p>
     * The vaccine preventable disease the dose is being evaluated against.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getTargetDisease() {
        return targetDisease;
    }

    /**
     * <p>
     * The vaccine administration event being evaluated.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getImmunizationEvent() {
        return immunizationEvent;
    }

    /**
     * <p>
     * Indicates if the dose is valid or not valid with respect to the published recommendations.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getDoseStatus() {
        return doseStatus;
    }

    /**
     * <p>
     * Provides an explanation as to why the vaccine administration event is valid or not relative to the published 
     * recommendations.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getDoseStatusReason() {
        return doseStatusReason;
    }

    /**
     * <p>
     * Additional information about the evaluation.
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
     * One possible path to achieve presumed immunity against a disease - within the context of an authority.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getSeries() {
        return series;
    }

    /**
     * <p>
     * Nominal position in a series.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getDoseNumber() {
        return doseNumber;
    }

    /**
     * <p>
     * The recommended number of doses to achieve immunity.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getSeriesDoses() {
        return seriesDoses;
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
        return new Builder(status, patient, targetDisease, immunizationEvent, doseStatus).from(this);
    }

    public Builder toBuilder(ImmunizationEvaluationStatus status, Reference patient, CodeableConcept targetDisease, Reference immunizationEvent, CodeableConcept doseStatus) {
        return new Builder(status, patient, targetDisease, immunizationEvent, doseStatus).from(this);
    }

    public static Builder builder(ImmunizationEvaluationStatus status, Reference patient, CodeableConcept targetDisease, Reference immunizationEvent, CodeableConcept doseStatus) {
        return new Builder(status, patient, targetDisease, immunizationEvent, doseStatus);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ImmunizationEvaluationStatus status;
        private final Reference patient;
        private final CodeableConcept targetDisease;
        private final Reference immunizationEvent;
        private final CodeableConcept doseStatus;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private DateTime date;
        private Reference authority;
        private List<CodeableConcept> doseStatusReason = new ArrayList<>();
        private String description;
        private String series;
        private Element doseNumber;
        private Element seriesDoses;

        private Builder(ImmunizationEvaluationStatus status, Reference patient, CodeableConcept targetDisease, Reference immunizationEvent, CodeableConcept doseStatus) {
            super();
            this.status = status;
            this.patient = patient;
            this.targetDisease = targetDisease;
            this.immunizationEvent = immunizationEvent;
            this.doseStatus = doseStatus;
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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * A unique identifier assigned to this immunization evaluation record.
         * </p>
         * <p>
         * Adds new element(s) to existing list
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
         * A unique identifier assigned to this immunization evaluation record.
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
         * The date the evaluation of the vaccine administration event was performed.
         * </p>
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
         * <p>
         * Indicates the authority who published the protocol (e.g. ACIP).
         * </p>
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
         * <p>
         * Provides an explanation as to why the vaccine administration event is valid or not relative to the published 
         * recommendations.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Provides an explanation as to why the vaccine administration event is valid or not relative to the published 
         * recommendations.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param doseStatusReason
         *     Reason for the dose status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder doseStatusReason(Collection<CodeableConcept> doseStatusReason) {
            this.doseStatusReason = new ArrayList<>(doseStatusReason);
            return this;
        }

        /**
         * <p>
         * Additional information about the evaluation.
         * </p>
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
         * <p>
         * One possible path to achieve presumed immunity against a disease - within the context of an authority.
         * </p>
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
         * <p>
         * Nominal position in a series.
         * </p>
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
         * <p>
         * The recommended number of doses to achieve immunity.
         * </p>
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

        @Override
        public ImmunizationEvaluation build() {
            return new ImmunizationEvaluation(this);
        }

        private Builder from(ImmunizationEvaluation immunizationEvaluation) {
            id = immunizationEvaluation.id;
            meta = immunizationEvaluation.meta;
            implicitRules = immunizationEvaluation.implicitRules;
            language = immunizationEvaluation.language;
            text = immunizationEvaluation.text;
            contained.addAll(immunizationEvaluation.contained);
            extension.addAll(immunizationEvaluation.extension);
            modifierExtension.addAll(immunizationEvaluation.modifierExtension);
            identifier.addAll(immunizationEvaluation.identifier);
            date = immunizationEvaluation.date;
            authority = immunizationEvaluation.authority;
            doseStatusReason.addAll(immunizationEvaluation.doseStatusReason);
            description = immunizationEvaluation.description;
            series = immunizationEvaluation.series;
            doseNumber = immunizationEvaluation.doseNumber;
            seriesDoses = immunizationEvaluation.seriesDoses;
            return this;
        }
    }
}
