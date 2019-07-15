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
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
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
 * A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional 
 * supporting justification.
 * </p>
 */
@Constraint(
    id = "imr-1",
    level = "Rule",
    location = "ImmunizationRecommendation.recommendation",
    description = "One of vaccineCode or targetDisease SHALL be present",
    expression = "vaccineCode.exists() or targetDisease.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ImmunizationRecommendation extends DomainResource {
    private final List<Identifier> identifier;
    private final Reference patient;
    private final DateTime date;
    private final Reference authority;
    private final List<Recommendation> recommendation;

    private volatile int hashCode;

    private ImmunizationRecommendation(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        date = ValidationSupport.requireNonNull(builder.date, "date");
        authority = builder.authority;
        recommendation = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.recommendation, "recommendation"));
    }

    /**
     * <p>
     * A unique identifier assigned to this particular recommendation record.
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
     * The patient the recommendation(s) are for.
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
     * The date the immunization recommendation(s) were created.
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
     * Vaccine administration recommendations.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Recommendation}.
     */
    public List<Recommendation> getRecommendation() {
        return recommendation;
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
                accept(patient, "patient", visitor);
                accept(date, "date", visitor);
                accept(authority, "authority", visitor);
                accept(recommendation, "recommendation", visitor, Recommendation.class);
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
        return new Builder(patient, date, recommendation).from(this);
    }

    public Builder toBuilder(Reference patient, DateTime date, List<Recommendation> recommendation) {
        return new Builder(patient, date, recommendation).from(this);
    }

    public static Builder builder(Reference patient, DateTime date, List<Recommendation> recommendation) {
        return new Builder(patient, date, recommendation);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Reference patient;
        private final DateTime date;
        private final List<Recommendation> recommendation;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Reference authority;

        private Builder(Reference patient, DateTime date, List<Recommendation> recommendation) {
            super();
            this.patient = patient;
            this.date = date;
            this.recommendation = new ArrayList<>(recommendation);
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
         * A unique identifier assigned to this particular recommendation record.
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
         * A unique identifier assigned to this particular recommendation record.
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
         * Indicates the authority who published the protocol (e.g. ACIP).
         * </p>
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

        @Override
        public ImmunizationRecommendation build() {
            return new ImmunizationRecommendation(this);
        }

        private Builder from(ImmunizationRecommendation immunizationRecommendation) {
            id = immunizationRecommendation.id;
            meta = immunizationRecommendation.meta;
            implicitRules = immunizationRecommendation.implicitRules;
            language = immunizationRecommendation.language;
            text = immunizationRecommendation.text;
            contained.addAll(immunizationRecommendation.contained);
            extension.addAll(immunizationRecommendation.extension);
            modifierExtension.addAll(immunizationRecommendation.modifierExtension);
            identifier.addAll(immunizationRecommendation.identifier);
            authority = immunizationRecommendation.authority;
            return this;
        }
    }

    /**
     * <p>
     * Vaccine administration recommendations.
     * </p>
     */
    public static class Recommendation extends BackboneElement {
        private final List<CodeableConcept> vaccineCode;
        private final CodeableConcept targetDisease;
        private final List<CodeableConcept> contraindicatedVaccineCode;
        private final CodeableConcept forecastStatus;
        private final List<CodeableConcept> forecastReason;
        private final List<DateCriterion> dateCriterion;
        private final String description;
        private final String series;
        private final Element doseNumber;
        private final Element seriesDoses;
        private final List<Reference> supportingImmunization;
        private final List<Reference> supportingPatientInformation;

        private volatile int hashCode;

        private Recommendation(Builder builder) {
            super(builder);
            vaccineCode = Collections.unmodifiableList(builder.vaccineCode);
            targetDisease = builder.targetDisease;
            contraindicatedVaccineCode = Collections.unmodifiableList(builder.contraindicatedVaccineCode);
            forecastStatus = ValidationSupport.requireNonNull(builder.forecastStatus, "forecastStatus");
            forecastReason = Collections.unmodifiableList(builder.forecastReason);
            dateCriterion = Collections.unmodifiableList(builder.dateCriterion);
            description = builder.description;
            series = builder.series;
            doseNumber = ValidationSupport.choiceElement(builder.doseNumber, "doseNumber", PositiveInt.class, String.class);
            seriesDoses = ValidationSupport.choiceElement(builder.seriesDoses, "seriesDoses", PositiveInt.class, String.class);
            supportingImmunization = Collections.unmodifiableList(builder.supportingImmunization);
            supportingPatientInformation = Collections.unmodifiableList(builder.supportingPatientInformation);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Vaccine(s) or vaccine group that pertain to the recommendation.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getVaccineCode() {
            return vaccineCode;
        }

        /**
         * <p>
         * The targeted disease for the recommendation.
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
         * Vaccine(s) which should not be used to fulfill the recommendation.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getContraindicatedVaccineCode() {
            return contraindicatedVaccineCode;
        }

        /**
         * <p>
         * Indicates the patient status with respect to the path to immunity for the target disease.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getForecastStatus() {
            return forecastStatus;
        }

        /**
         * <p>
         * The reason for the assigned forecast status.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getForecastReason() {
            return forecastReason;
        }

        /**
         * <p>
         * Vaccine date recommendations. For example, earliest date to administer, latest date to administer, etc.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DateCriterion}.
         */
        public List<DateCriterion> getDateCriterion() {
            return dateCriterion;
        }

        /**
         * <p>
         * Contains the description about the protocol under which the vaccine was administered.
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
         * Nominal position of the recommended dose in a series (e.g. dose 2 is the next recommended dose).
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

        /**
         * <p>
         * Immunization event history and/or evaluation that supports the status and recommendation.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getSupportingImmunization() {
            return supportingImmunization;
        }

        /**
         * <p>
         * Patient Information that supports the status and recommendation. This includes patient observations, adverse reactions 
         * and allergy/intolerance information.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
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
            return new Builder(forecastStatus).from(this);
        }

        public Builder toBuilder(CodeableConcept forecastStatus) {
            return new Builder(forecastStatus).from(this);
        }

        public static Builder builder(CodeableConcept forecastStatus) {
            return new Builder(forecastStatus);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept forecastStatus;

            // optional
            private List<CodeableConcept> vaccineCode = new ArrayList<>();
            private CodeableConcept targetDisease;
            private List<CodeableConcept> contraindicatedVaccineCode = new ArrayList<>();
            private List<CodeableConcept> forecastReason = new ArrayList<>();
            private List<DateCriterion> dateCriterion = new ArrayList<>();
            private String description;
            private String series;
            private Element doseNumber;
            private Element seriesDoses;
            private List<Reference> supportingImmunization = new ArrayList<>();
            private List<Reference> supportingPatientInformation = new ArrayList<>();

            private Builder(CodeableConcept forecastStatus) {
                super();
                this.forecastStatus = forecastStatus;
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
             * Adds new element(s) to existing list
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
             * Vaccine(s) or vaccine group that pertain to the recommendation.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Vaccine(s) or vaccine group that pertain to the recommendation.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param vaccineCode
             *     Vaccine or vaccine group recommendation applies to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder vaccineCode(Collection<CodeableConcept> vaccineCode) {
                this.vaccineCode = new ArrayList<>(vaccineCode);
                return this;
            }

            /**
             * <p>
             * The targeted disease for the recommendation.
             * </p>
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
             * <p>
             * Vaccine(s) which should not be used to fulfill the recommendation.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Vaccine(s) which should not be used to fulfill the recommendation.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param contraindicatedVaccineCode
             *     Vaccine which is contraindicated to fulfill the recommendation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder contraindicatedVaccineCode(Collection<CodeableConcept> contraindicatedVaccineCode) {
                this.contraindicatedVaccineCode = new ArrayList<>(contraindicatedVaccineCode);
                return this;
            }

            /**
             * <p>
             * The reason for the assigned forecast status.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * The reason for the assigned forecast status.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param forecastReason
             *     Vaccine administration status reason
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder forecastReason(Collection<CodeableConcept> forecastReason) {
                this.forecastReason = new ArrayList<>(forecastReason);
                return this;
            }

            /**
             * <p>
             * Vaccine date recommendations. For example, earliest date to administer, latest date to administer, etc.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Vaccine date recommendations. For example, earliest date to administer, latest date to administer, etc.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param dateCriterion
             *     Dates governing proposed immunization
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dateCriterion(Collection<DateCriterion> dateCriterion) {
                this.dateCriterion = new ArrayList<>(dateCriterion);
                return this;
            }

            /**
             * <p>
             * Contains the description about the protocol under which the vaccine was administered.
             * </p>
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
             * <p>
             * One possible path to achieve presumed immunity against a disease - within the context of an authority.
             * </p>
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
             * <p>
             * Nominal position of the recommended dose in a series (e.g. dose 2 is the next recommended dose).
             * </p>
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

            /**
             * <p>
             * Immunization event history and/or evaluation that supports the status and recommendation.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Immunization event history and/or evaluation that supports the status and recommendation.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param supportingImmunization
             *     Past immunizations supporting recommendation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder supportingImmunization(Collection<Reference> supportingImmunization) {
                this.supportingImmunization = new ArrayList<>(supportingImmunization);
                return this;
            }

            /**
             * <p>
             * Patient Information that supports the status and recommendation. This includes patient observations, adverse reactions 
             * and allergy/intolerance information.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Patient Information that supports the status and recommendation. This includes patient observations, adverse reactions 
             * and allergy/intolerance information.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param supportingPatientInformation
             *     Patient observations supporting recommendation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder supportingPatientInformation(Collection<Reference> supportingPatientInformation) {
                this.supportingPatientInformation = new ArrayList<>(supportingPatientInformation);
                return this;
            }

            @Override
            public Recommendation build() {
                return new Recommendation(this);
            }

            private Builder from(Recommendation recommendation) {
                id = recommendation.id;
                extension.addAll(recommendation.extension);
                modifierExtension.addAll(recommendation.modifierExtension);
                vaccineCode.addAll(recommendation.vaccineCode);
                targetDisease = recommendation.targetDisease;
                contraindicatedVaccineCode.addAll(recommendation.contraindicatedVaccineCode);
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
         * <p>
         * Vaccine date recommendations. For example, earliest date to administer, latest date to administer, etc.
         * </p>
         */
        public static class DateCriterion extends BackboneElement {
            private final CodeableConcept code;
            private final DateTime value;

            private volatile int hashCode;

            private DateCriterion(Builder builder) {
                super(builder);
                code = ValidationSupport.requireNonNull(builder.code, "code");
                value = ValidationSupport.requireNonNull(builder.value, "value");
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Date classification of recommendation. For example, earliest date to give, latest date to give, etc.
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
             * The date whose meaning is specified by dateCriterion.code.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link DateTime}.
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
            public void accept(java.lang.String elementName, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, this);
                    if (visitor.visit(elementName, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(code, "code", visitor);
                        accept(value, "value", visitor);
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
                return new Builder(code, value).from(this);
            }

            public Builder toBuilder(CodeableConcept code, DateTime value) {
                return new Builder(code, value).from(this);
            }

            public static Builder builder(CodeableConcept code, DateTime value) {
                return new Builder(code, value);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept code;
                private final DateTime value;

                private Builder(CodeableConcept code, DateTime value) {
                    super();
                    this.code = code;
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
                 * Adds new element(s) to existing list
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

                @Override
                public DateCriterion build() {
                    return new DateCriterion(this);
                }

                private Builder from(DateCriterion dateCriterion) {
                    id = dateCriterion.id;
                    extension.addAll(dateCriterion.extension);
                    modifierExtension.addAll(dateCriterion.modifierExtension);
                    return this;
                }
            }
        }
    }
}
