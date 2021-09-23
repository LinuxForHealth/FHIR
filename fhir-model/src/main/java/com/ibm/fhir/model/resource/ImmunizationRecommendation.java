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
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional 
 * supporting justification.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "imr-1",
    level = "Rule",
    location = "ImmunizationRecommendation.recommendation",
    description = "One of vaccineCode or targetDisease SHALL be present",
    expression = "vaccineCode.exists() or targetDisease.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/ImmunizationRecommendation"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ImmunizationRecommendation extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference patient;
    @Summary
    @Required
    private final DateTime date;
    @ReferenceTarget({ "Organization" })
    private final Reference authority;
    @Summary
    @Required
    private final List<Recommendation> recommendation;

    private ImmunizationRecommendation(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        patient = builder.patient;
        date = builder.date;
        authority = builder.authority;
        recommendation = Collections.unmodifiableList(builder.recommendation);
    }

    /**
     * A unique identifier assigned to this particular recommendation record.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The patient the recommendation(s) are for.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * The date the immunization recommendation(s) were created.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
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
     * Vaccine administration recommendations.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Recommendation} that is non-empty.
     */
    public List<Recommendation> getRecommendation() {
        return recommendation;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (patient != null) || 
            (date != null) || 
            (authority != null) || 
            !recommendation.isEmpty();
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
                accept(patient, "patient", visitor);
                accept(date, "date", visitor);
                accept(authority, "authority", visitor);
                accept(recommendation, "recommendation", visitor, Recommendation.class);
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
        ImmunizationRecommendation other = (ImmunizationRecommendation) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(date, other.date) && 
            Objects.equals(authority, other.authority) && 
            Objects.equals(recommendation, other.recommendation);
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
                patient, 
                date, 
                authority, 
                recommendation);
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
        private Reference patient;
        private DateTime date;
        private Reference authority;
        private List<Recommendation> recommendation = new ArrayList<>();

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
         * A unique identifier assigned to this particular recommendation record.
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
         * A unique identifier assigned to this particular recommendation record.
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
         * The patient the recommendation(s) are for.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Who this profile is for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * The date the immunization recommendation(s) were created.
         * 
         * <p>This element is required.
         * 
         * @param date
         *     Date recommendation(s) created
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
         *     Who is responsible for protocol
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authority(Reference authority) {
            this.authority = authority;
            return this;
        }

        /**
         * Vaccine administration recommendations.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param recommendation
         *     Vaccine administration recommendations
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recommendation(Recommendation... recommendation) {
            for (Recommendation value : recommendation) {
                this.recommendation.add(value);
            }
            return this;
        }

        /**
         * Vaccine administration recommendations.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param recommendation
         *     Vaccine administration recommendations
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder recommendation(Collection<Recommendation> recommendation) {
            this.recommendation = new ArrayList<>(recommendation);
            return this;
        }

        /**
         * Build the {@link ImmunizationRecommendation}
         * 
         * <p>Required elements:
         * <ul>
         * <li>patient</li>
         * <li>date</li>
         * <li>recommendation</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ImmunizationRecommendation}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ImmunizationRecommendation per the base specification
         */
        @Override
        public ImmunizationRecommendation build() {
            ImmunizationRecommendation immunizationRecommendation = new ImmunizationRecommendation(this);
            if (validating) {
                validate(immunizationRecommendation);
            }
            return immunizationRecommendation;
        }

        protected void validate(ImmunizationRecommendation immunizationRecommendation) {
            super.validate(immunizationRecommendation);
            ValidationSupport.checkList(immunizationRecommendation.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(immunizationRecommendation.patient, "patient");
            ValidationSupport.requireNonNull(immunizationRecommendation.date, "date");
            ValidationSupport.checkNonEmptyList(immunizationRecommendation.recommendation, "recommendation", Recommendation.class);
            ValidationSupport.checkReferenceType(immunizationRecommendation.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(immunizationRecommendation.authority, "authority", "Organization");
        }

        protected Builder from(ImmunizationRecommendation immunizationRecommendation) {
            super.from(immunizationRecommendation);
            identifier.addAll(immunizationRecommendation.identifier);
            patient = immunizationRecommendation.patient;
            date = immunizationRecommendation.date;
            authority = immunizationRecommendation.authority;
            recommendation.addAll(immunizationRecommendation.recommendation);
            return this;
        }
    }

    /**
     * Vaccine administration recommendations.
     */
    public static class Recommendation extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "VaccineCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The type of vaccine administered.",
            valueSet = "http://hl7.org/fhir/ValueSet/vaccine-code"
        )
        private final List<CodeableConcept> vaccineCode;
        @Summary
        @Binding(
            bindingName = "TargetDisease",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The disease that the recommended vaccination targets.",
            valueSet = "http://hl7.org/fhir/ValueSet/immunization-recommendation-target-disease"
        )
        private final CodeableConcept targetDisease;
        @Summary
        @Binding(
            bindingName = "VaccineCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The type of vaccine administered.",
            valueSet = "http://hl7.org/fhir/ValueSet/vaccine-code"
        )
        private final List<CodeableConcept> contraindicatedVaccineCode;
        @Summary
        @Binding(
            bindingName = "ImmunizationRecommendationStatus",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The patient's status with respect to a vaccination protocol.",
            valueSet = "http://hl7.org/fhir/ValueSet/immunization-recommendation-status"
        )
        @Required
        private final CodeableConcept forecastStatus;
        @Summary
        @Binding(
            bindingName = "ImmunizationRecommendationReason",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The reason for the patient's status with respect to a vaccination protocol.",
            valueSet = "http://hl7.org/fhir/ValueSet/immunization-recommendation-reason"
        )
        private final List<CodeableConcept> forecastReason;
        private final List<DateCriterion> dateCriterion;
        private final String description;
        private final String series;
        @Summary
        @Choice({ PositiveInt.class, String.class })
        private final Element doseNumber;
        @Choice({ PositiveInt.class, String.class })
        private final Element seriesDoses;
        @ReferenceTarget({ "Immunization", "ImmunizationEvaluation" })
        private final List<Reference> supportingImmunization;
        private final List<Reference> supportingPatientInformation;

        private Recommendation(Builder builder) {
            super(builder);
            vaccineCode = Collections.unmodifiableList(builder.vaccineCode);
            targetDisease = builder.targetDisease;
            contraindicatedVaccineCode = Collections.unmodifiableList(builder.contraindicatedVaccineCode);
            forecastStatus = builder.forecastStatus;
            forecastReason = Collections.unmodifiableList(builder.forecastReason);
            dateCriterion = Collections.unmodifiableList(builder.dateCriterion);
            description = builder.description;
            series = builder.series;
            doseNumber = builder.doseNumber;
            seriesDoses = builder.seriesDoses;
            supportingImmunization = Collections.unmodifiableList(builder.supportingImmunization);
            supportingPatientInformation = Collections.unmodifiableList(builder.supportingPatientInformation);
        }

        /**
         * Vaccine(s) or vaccine group that pertain to the recommendation.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getVaccineCode() {
            return vaccineCode;
        }

        /**
         * The targeted disease for the recommendation.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getTargetDisease() {
            return targetDisease;
        }

        /**
         * Vaccine(s) which should not be used to fulfill the recommendation.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getContraindicatedVaccineCode() {
            return contraindicatedVaccineCode;
        }

        /**
         * Indicates the patient status with respect to the path to immunity for the target disease.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getForecastStatus() {
            return forecastStatus;
        }

        /**
         * The reason for the assigned forecast status.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getForecastReason() {
            return forecastReason;
        }

        /**
         * Vaccine date recommendations. For example, earliest date to administer, latest date to administer, etc.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DateCriterion} that may be empty.
         */
        public List<DateCriterion> getDateCriterion() {
            return dateCriterion;
        }

        /**
         * Contains the description about the protocol under which the vaccine was administered.
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
         * Nominal position of the recommended dose in a series (e.g. dose 2 is the next recommended dose).
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

        /**
         * Immunization event history and/or evaluation that supports the status and recommendation.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getSupportingImmunization() {
            return supportingImmunization;
        }

        /**
         * Patient Information that supports the status and recommendation. This includes patient observations, adverse reactions 
         * and allergy/intolerance information.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getSupportingPatientInformation() {
            return supportingPatientInformation;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !vaccineCode.isEmpty() || 
                (targetDisease != null) || 
                !contraindicatedVaccineCode.isEmpty() || 
                (forecastStatus != null) || 
                !forecastReason.isEmpty() || 
                !dateCriterion.isEmpty() || 
                (description != null) || 
                (series != null) || 
                (doseNumber != null) || 
                (seriesDoses != null) || 
                !supportingImmunization.isEmpty() || 
                !supportingPatientInformation.isEmpty();
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
                    accept(vaccineCode, "vaccineCode", visitor, CodeableConcept.class);
                    accept(targetDisease, "targetDisease", visitor);
                    accept(contraindicatedVaccineCode, "contraindicatedVaccineCode", visitor, CodeableConcept.class);
                    accept(forecastStatus, "forecastStatus", visitor);
                    accept(forecastReason, "forecastReason", visitor, CodeableConcept.class);
                    accept(dateCriterion, "dateCriterion", visitor, DateCriterion.class);
                    accept(description, "description", visitor);
                    accept(series, "series", visitor);
                    accept(doseNumber, "doseNumber", visitor);
                    accept(seriesDoses, "seriesDoses", visitor);
                    accept(supportingImmunization, "supportingImmunization", visitor, Reference.class);
                    accept(supportingPatientInformation, "supportingPatientInformation", visitor, Reference.class);
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
            Recommendation other = (Recommendation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(vaccineCode, other.vaccineCode) && 
                Objects.equals(targetDisease, other.targetDisease) && 
                Objects.equals(contraindicatedVaccineCode, other.contraindicatedVaccineCode) && 
                Objects.equals(forecastStatus, other.forecastStatus) && 
                Objects.equals(forecastReason, other.forecastReason) && 
                Objects.equals(dateCriterion, other.dateCriterion) && 
                Objects.equals(description, other.description) && 
                Objects.equals(series, other.series) && 
                Objects.equals(doseNumber, other.doseNumber) && 
                Objects.equals(seriesDoses, other.seriesDoses) && 
                Objects.equals(supportingImmunization, other.supportingImmunization) && 
                Objects.equals(supportingPatientInformation, other.supportingPatientInformation);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    vaccineCode, 
                    targetDisease, 
                    contraindicatedVaccineCode, 
                    forecastStatus, 
                    forecastReason, 
                    dateCriterion, 
                    description, 
                    series, 
                    doseNumber, 
                    seriesDoses, 
                    supportingImmunization, 
                    supportingPatientInformation);
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
            private List<CodeableConcept> vaccineCode = new ArrayList<>();
            private CodeableConcept targetDisease;
            private List<CodeableConcept> contraindicatedVaccineCode = new ArrayList<>();
            private CodeableConcept forecastStatus;
            private List<CodeableConcept> forecastReason = new ArrayList<>();
            private List<DateCriterion> dateCriterion = new ArrayList<>();
            private String description;
            private String series;
            private Element doseNumber;
            private Element seriesDoses;
            private List<Reference> supportingImmunization = new ArrayList<>();
            private List<Reference> supportingPatientInformation = new ArrayList<>();

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
             * Vaccine(s) or vaccine group that pertain to the recommendation.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param vaccineCode
             *     Vaccine or vaccine group recommendation applies to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder vaccineCode(CodeableConcept... vaccineCode) {
                for (CodeableConcept value : vaccineCode) {
                    this.vaccineCode.add(value);
                }
                return this;
            }

            /**
             * Vaccine(s) or vaccine group that pertain to the recommendation.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param vaccineCode
             *     Vaccine or vaccine group recommendation applies to
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder vaccineCode(Collection<CodeableConcept> vaccineCode) {
                this.vaccineCode = new ArrayList<>(vaccineCode);
                return this;
            }

            /**
             * The targeted disease for the recommendation.
             * 
             * @param targetDisease
             *     Disease to be immunized against
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder targetDisease(CodeableConcept targetDisease) {
                this.targetDisease = targetDisease;
                return this;
            }

            /**
             * Vaccine(s) which should not be used to fulfill the recommendation.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param contraindicatedVaccineCode
             *     Vaccine which is contraindicated to fulfill the recommendation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder contraindicatedVaccineCode(CodeableConcept... contraindicatedVaccineCode) {
                for (CodeableConcept value : contraindicatedVaccineCode) {
                    this.contraindicatedVaccineCode.add(value);
                }
                return this;
            }

            /**
             * Vaccine(s) which should not be used to fulfill the recommendation.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param contraindicatedVaccineCode
             *     Vaccine which is contraindicated to fulfill the recommendation
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder contraindicatedVaccineCode(Collection<CodeableConcept> contraindicatedVaccineCode) {
                this.contraindicatedVaccineCode = new ArrayList<>(contraindicatedVaccineCode);
                return this;
            }

            /**
             * Indicates the patient status with respect to the path to immunity for the target disease.
             * 
             * <p>This element is required.
             * 
             * @param forecastStatus
             *     Vaccine recommendation status
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder forecastStatus(CodeableConcept forecastStatus) {
                this.forecastStatus = forecastStatus;
                return this;
            }

            /**
             * The reason for the assigned forecast status.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param forecastReason
             *     Vaccine administration status reason
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder forecastReason(CodeableConcept... forecastReason) {
                for (CodeableConcept value : forecastReason) {
                    this.forecastReason.add(value);
                }
                return this;
            }

            /**
             * The reason for the assigned forecast status.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param forecastReason
             *     Vaccine administration status reason
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder forecastReason(Collection<CodeableConcept> forecastReason) {
                this.forecastReason = new ArrayList<>(forecastReason);
                return this;
            }

            /**
             * Vaccine date recommendations. For example, earliest date to administer, latest date to administer, etc.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param dateCriterion
             *     Dates governing proposed immunization
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dateCriterion(DateCriterion... dateCriterion) {
                for (DateCriterion value : dateCriterion) {
                    this.dateCriterion.add(value);
                }
                return this;
            }

            /**
             * Vaccine date recommendations. For example, earliest date to administer, latest date to administer, etc.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param dateCriterion
             *     Dates governing proposed immunization
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder dateCriterion(Collection<DateCriterion> dateCriterion) {
                this.dateCriterion = new ArrayList<>(dateCriterion);
                return this;
            }

            /**
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Protocol details
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
             * Contains the description about the protocol under which the vaccine was administered.
             * 
             * @param description
             *     Protocol details
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
             *     Name of vaccination series
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
             *     Name of vaccination series
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
             *     Recommended dose number within series
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
             * Nominal position of the recommended dose in a series (e.g. dose 2 is the next recommended dose).
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link PositiveInt}</li>
             * <li>{@link String}</li>
             * </ul>
             * 
             * @param doseNumber
             *     Recommended dose number within series
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
             * Immunization event history and/or evaluation that supports the status and recommendation.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Immunization}</li>
             * <li>{@link ImmunizationEvaluation}</li>
             * </ul>
             * 
             * @param supportingImmunization
             *     Past immunizations supporting recommendation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder supportingImmunization(Reference... supportingImmunization) {
                for (Reference value : supportingImmunization) {
                    this.supportingImmunization.add(value);
                }
                return this;
            }

            /**
             * Immunization event history and/or evaluation that supports the status and recommendation.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Immunization}</li>
             * <li>{@link ImmunizationEvaluation}</li>
             * </ul>
             * 
             * @param supportingImmunization
             *     Past immunizations supporting recommendation
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder supportingImmunization(Collection<Reference> supportingImmunization) {
                this.supportingImmunization = new ArrayList<>(supportingImmunization);
                return this;
            }

            /**
             * Patient Information that supports the status and recommendation. This includes patient observations, adverse reactions 
             * and allergy/intolerance information.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param supportingPatientInformation
             *     Patient observations supporting recommendation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder supportingPatientInformation(Reference... supportingPatientInformation) {
                for (Reference value : supportingPatientInformation) {
                    this.supportingPatientInformation.add(value);
                }
                return this;
            }

            /**
             * Patient Information that supports the status and recommendation. This includes patient observations, adverse reactions 
             * and allergy/intolerance information.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param supportingPatientInformation
             *     Patient observations supporting recommendation
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder supportingPatientInformation(Collection<Reference> supportingPatientInformation) {
                this.supportingPatientInformation = new ArrayList<>(supportingPatientInformation);
                return this;
            }

            /**
             * Build the {@link Recommendation}
             * 
             * <p>Required elements:
             * <ul>
             * <li>forecastStatus</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Recommendation}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Recommendation per the base specification
             */
            @Override
            public Recommendation build() {
                Recommendation recommendation = new Recommendation(this);
                if (validating) {
                    validate(recommendation);
                }
                return recommendation;
            }

            protected void validate(Recommendation recommendation) {
                super.validate(recommendation);
                ValidationSupport.checkList(recommendation.vaccineCode, "vaccineCode", CodeableConcept.class);
                ValidationSupport.checkList(recommendation.contraindicatedVaccineCode, "contraindicatedVaccineCode", CodeableConcept.class);
                ValidationSupport.requireNonNull(recommendation.forecastStatus, "forecastStatus");
                ValidationSupport.checkList(recommendation.forecastReason, "forecastReason", CodeableConcept.class);
                ValidationSupport.checkList(recommendation.dateCriterion, "dateCriterion", DateCriterion.class);
                ValidationSupport.choiceElement(recommendation.doseNumber, "doseNumber", PositiveInt.class, String.class);
                ValidationSupport.choiceElement(recommendation.seriesDoses, "seriesDoses", PositiveInt.class, String.class);
                ValidationSupport.checkList(recommendation.supportingImmunization, "supportingImmunization", Reference.class);
                ValidationSupport.checkList(recommendation.supportingPatientInformation, "supportingPatientInformation", Reference.class);
                ValidationSupport.checkReferenceType(recommendation.supportingImmunization, "supportingImmunization", "Immunization", "ImmunizationEvaluation");
                ValidationSupport.requireValueOrChildren(recommendation);
            }

            protected Builder from(Recommendation recommendation) {
                super.from(recommendation);
                vaccineCode.addAll(recommendation.vaccineCode);
                targetDisease = recommendation.targetDisease;
                contraindicatedVaccineCode.addAll(recommendation.contraindicatedVaccineCode);
                forecastStatus = recommendation.forecastStatus;
                forecastReason.addAll(recommendation.forecastReason);
                dateCriterion.addAll(recommendation.dateCriterion);
                description = recommendation.description;
                series = recommendation.series;
                doseNumber = recommendation.doseNumber;
                seriesDoses = recommendation.seriesDoses;
                supportingImmunization.addAll(recommendation.supportingImmunization);
                supportingPatientInformation.addAll(recommendation.supportingPatientInformation);
                return this;
            }
        }

        /**
         * Vaccine date recommendations. For example, earliest date to administer, latest date to administer, etc.
         */
        public static class DateCriterion extends BackboneElement {
            @Binding(
                bindingName = "ImmunizationRecommendationDateCriterion",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Classifies date criterion with respect to conveying information about a patient's vaccination status (e.g. due date, latest to give date, etc.).",
                valueSet = "http://hl7.org/fhir/ValueSet/immunization-recommendation-date-criterion"
            )
            @Required
            private final CodeableConcept code;
            @Required
            private final DateTime value;

            private DateCriterion(Builder builder) {
                super(builder);
                code = builder.code;
                value = builder.value;
            }

            /**
             * Date classification of recommendation. For example, earliest date to give, latest date to give, etc.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * The date whose meaning is specified by dateCriterion.code.
             * 
             * @return
             *     An immutable object of type {@link DateTime} that is non-null.
             */
            public DateTime getValue() {
                return value;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (code != null) || 
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
                        accept(code, "code", visitor);
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
                DateCriterion other = (DateCriterion) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(value, other.value);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        code, 
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
                private CodeableConcept code;
                private DateTime value;

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
                 * Date classification of recommendation. For example, earliest date to give, latest date to give, etc.
                 * 
                 * <p>This element is required.
                 * 
                 * @param code
                 *     Type of date
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(CodeableConcept code) {
                    this.code = code;
                    return this;
                }

                /**
                 * The date whose meaning is specified by dateCriterion.code.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Recommended date
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(DateTime value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Build the {@link DateCriterion}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>code</li>
                 * <li>value</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link DateCriterion}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid DateCriterion per the base specification
                 */
                @Override
                public DateCriterion build() {
                    DateCriterion dateCriterion = new DateCriterion(this);
                    if (validating) {
                        validate(dateCriterion);
                    }
                    return dateCriterion;
                }

                protected void validate(DateCriterion dateCriterion) {
                    super.validate(dateCriterion);
                    ValidationSupport.requireNonNull(dateCriterion.code, "code");
                    ValidationSupport.requireNonNull(dateCriterion.value, "value");
                    ValidationSupport.requireValueOrChildren(dateCriterion);
                }

                protected Builder from(DateCriterion dateCriterion) {
                    super.from(dateCriterion);
                    code = dateCriterion.code;
                    value = dateCriterion.value;
                    return this;
                }
            }
        }
    }
}
