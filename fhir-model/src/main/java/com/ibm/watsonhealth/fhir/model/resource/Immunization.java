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
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.ImmunizationStatus;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, 
 * a clinician or another party.
 * </p>
 */
@Constraint(
    id = "imm-1",
    level = "Rule",
    location = "Immunization.education",
    description = "One of documentType or reference SHALL be present",
    expression = "documentType.exists() or reference.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Immunization extends DomainResource {
    private final List<Identifier> identifier;
    private final ImmunizationStatus status;
    private final CodeableConcept statusReason;
    private final CodeableConcept vaccineCode;
    private final Reference patient;
    private final Reference encounter;
    private final Element occurrence;
    private final DateTime recorded;
    private final Boolean primarySource;
    private final CodeableConcept reportOrigin;
    private final Reference location;
    private final Reference manufacturer;
    private final String lotNumber;
    private final Date expirationDate;
    private final CodeableConcept site;
    private final CodeableConcept route;
    private final Quantity doseQuantity;
    private final List<Performer> performer;
    private final List<Annotation> note;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final Boolean isSubpotent;
    private final List<CodeableConcept> subpotentReason;
    private final List<Education> education;
    private final List<CodeableConcept> programEligibility;
    private final CodeableConcept fundingSource;
    private final List<Reaction> reaction;
    private final List<ProtocolApplied> protocolApplied;

    private volatile int hashCode;

    private Immunization(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        statusReason = builder.statusReason;
        vaccineCode = ValidationSupport.requireNonNull(builder.vaccineCode, "vaccineCode");
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        encounter = builder.encounter;
        occurrence = ValidationSupport.requireChoiceElement(builder.occurrence, "occurrence", DateTime.class, String.class);
        recorded = builder.recorded;
        primarySource = builder.primarySource;
        reportOrigin = builder.reportOrigin;
        location = builder.location;
        manufacturer = builder.manufacturer;
        lotNumber = builder.lotNumber;
        expirationDate = builder.expirationDate;
        site = builder.site;
        route = builder.route;
        doseQuantity = builder.doseQuantity;
        performer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.performer, "performer"));
        note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
        reasonCode = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonCode, "reasonCode"));
        reasonReference = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonReference, "reasonReference"));
        isSubpotent = builder.isSubpotent;
        subpotentReason = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.subpotentReason, "subpotentReason"));
        education = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.education, "education"));
        programEligibility = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.programEligibility, "programEligibility"));
        fundingSource = builder.fundingSource;
        reaction = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reaction, "reaction"));
        protocolApplied = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.protocolApplied, "protocolApplied"));
    }

    /**
     * <p>
     * A unique identifier assigned to this immunization record.
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
     * Indicates the current status of the immunization event.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ImmunizationStatus}.
     */
    public ImmunizationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Indicates the reason the immunization event was not performed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getStatusReason() {
        return statusReason;
    }

    /**
     * <p>
     * Vaccine that was administered or was to be administered.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getVaccineCode() {
        return vaccineCode;
    }

    /**
     * <p>
     * The patient who either received or did not receive the immunization.
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
     * The visit or admission or other contact between patient and health care provider the immunization was performed as 
     * part of.
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
     * Date vaccine administered or was to be administered.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getOccurrence() {
        return occurrence;
    }

    /**
     * <p>
     * The date the occurrence of the immunization was first captured in the record - potentially significantly after the 
     * occurrence of the event.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getRecorded() {
        return recorded;
    }

    /**
     * <p>
     * An indication that the content of the record is based on information from the person who administered the vaccine. 
     * This reflects the context under which the data was originally recorded.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getPrimarySource() {
        return primarySource;
    }

    /**
     * <p>
     * The source of the data when the report of the immunization event is not based on information from the person who 
     * administered the vaccine.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getReportOrigin() {
        return reportOrigin;
    }

    /**
     * <p>
     * The service delivery location where the vaccine administration occurred.
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
     * Name of vaccine manufacturer.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getManufacturer() {
        return manufacturer;
    }

    /**
     * <p>
     * Lot number of the vaccine product.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * <p>
     * Date vaccine batch expires.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * <p>
     * Body site where vaccine was administered.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getSite() {
        return site;
    }

    /**
     * <p>
     * The path by which the vaccine product is taken into the body.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getRoute() {
        return route;
    }

    /**
     * <p>
     * The quantity of vaccine product that was administered.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getDoseQuantity() {
        return doseQuantity;
    }

    /**
     * <p>
     * Indicates who performed the immunization event.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Performer}.
     */
    public List<Performer> getPerformer() {
        return performer;
    }

    /**
     * <p>
     * Extra information about the immunization that is not conveyed by the other attributes.
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
     * Reasons why the vaccine was administered.
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
     * Condition, Observation or DiagnosticReport that supports why the immunization was administered.
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
     * Indication if a dose is considered to be subpotent. By default, a dose should be considered to be potent.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getIsSubpotent() {
        return isSubpotent;
    }

    /**
     * <p>
     * Reason why a dose is considered to be subpotent.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getSubpotentReason() {
        return subpotentReason;
    }

    /**
     * <p>
     * Educational material presented to the patient (or guardian) at the time of vaccine administration.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Education}.
     */
    public List<Education> getEducation() {
        return education;
    }

    /**
     * <p>
     * Indicates a patient's eligibility for a funding program.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getProgramEligibility() {
        return programEligibility;
    }

    /**
     * <p>
     * Indicates the source of the vaccine actually administered. This may be different than the patient eligibility (e.g. 
     * the patient may be eligible for a publically purchased vaccine but due to inventory issues, vaccine purchased with 
     * private funds was actually administered).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getFundingSource() {
        return fundingSource;
    }

    /**
     * <p>
     * Categorical data indicating that an adverse event is associated in time to an immunization.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reaction}.
     */
    public List<Reaction> getReaction() {
        return reaction;
    }

    /**
     * <p>
     * The protocol (set of recommendations) being followed by the provider who administered the dose.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ProtocolApplied}.
     */
    public List<ProtocolApplied> getProtocolApplied() {
        return protocolApplied;
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
                accept(statusReason, "statusReason", visitor);
                accept(vaccineCode, "vaccineCode", visitor);
                accept(patient, "patient", visitor);
                accept(encounter, "encounter", visitor);
                accept(occurrence, "occurrence", visitor);
                accept(recorded, "recorded", visitor);
                accept(primarySource, "primarySource", visitor);
                accept(reportOrigin, "reportOrigin", visitor);
                accept(location, "location", visitor);
                accept(manufacturer, "manufacturer", visitor);
                accept(lotNumber, "lotNumber", visitor);
                accept(expirationDate, "expirationDate", visitor);
                accept(site, "site", visitor);
                accept(route, "route", visitor);
                accept(doseQuantity, "doseQuantity", visitor);
                accept(performer, "performer", visitor, Performer.class);
                accept(note, "note", visitor, Annotation.class);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(isSubpotent, "isSubpotent", visitor);
                accept(subpotentReason, "subpotentReason", visitor, CodeableConcept.class);
                accept(education, "education", visitor, Education.class);
                accept(programEligibility, "programEligibility", visitor, CodeableConcept.class);
                accept(fundingSource, "fundingSource", visitor);
                accept(reaction, "reaction", visitor, Reaction.class);
                accept(protocolApplied, "protocolApplied", visitor, ProtocolApplied.class);
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
        Immunization other = (Immunization) obj;
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
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(vaccineCode, other.vaccineCode) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(occurrence, other.occurrence) && 
            Objects.equals(recorded, other.recorded) && 
            Objects.equals(primarySource, other.primarySource) && 
            Objects.equals(reportOrigin, other.reportOrigin) && 
            Objects.equals(location, other.location) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(lotNumber, other.lotNumber) && 
            Objects.equals(expirationDate, other.expirationDate) && 
            Objects.equals(site, other.site) && 
            Objects.equals(route, other.route) && 
            Objects.equals(doseQuantity, other.doseQuantity) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(note, other.note) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(isSubpotent, other.isSubpotent) && 
            Objects.equals(subpotentReason, other.subpotentReason) && 
            Objects.equals(education, other.education) && 
            Objects.equals(programEligibility, other.programEligibility) && 
            Objects.equals(fundingSource, other.fundingSource) && 
            Objects.equals(reaction, other.reaction) && 
            Objects.equals(protocolApplied, other.protocolApplied);
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
                statusReason, 
                vaccineCode, 
                patient, 
                encounter, 
                occurrence, 
                recorded, 
                primarySource, 
                reportOrigin, 
                location, 
                manufacturer, 
                lotNumber, 
                expirationDate, 
                site, 
                route, 
                doseQuantity, 
                performer, 
                note, 
                reasonCode, 
                reasonReference, 
                isSubpotent, 
                subpotentReason, 
                education, 
                programEligibility, 
                fundingSource, 
                reaction, 
                protocolApplied);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status, vaccineCode, patient, occurrence).from(this);
    }

    public Builder toBuilder(ImmunizationStatus status, CodeableConcept vaccineCode, Reference patient, Element occurrence) {
        return new Builder(status, vaccineCode, patient, occurrence).from(this);
    }

    public static Builder builder(ImmunizationStatus status, CodeableConcept vaccineCode, Reference patient, Element occurrence) {
        return new Builder(status, vaccineCode, patient, occurrence);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ImmunizationStatus status;
        private final CodeableConcept vaccineCode;
        private final Reference patient;
        private final Element occurrence;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept statusReason;
        private Reference encounter;
        private DateTime recorded;
        private Boolean primarySource;
        private CodeableConcept reportOrigin;
        private Reference location;
        private Reference manufacturer;
        private String lotNumber;
        private Date expirationDate;
        private CodeableConcept site;
        private CodeableConcept route;
        private Quantity doseQuantity;
        private List<Performer> performer = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private Boolean isSubpotent;
        private List<CodeableConcept> subpotentReason = new ArrayList<>();
        private List<Education> education = new ArrayList<>();
        private List<CodeableConcept> programEligibility = new ArrayList<>();
        private CodeableConcept fundingSource;
        private List<Reaction> reaction = new ArrayList<>();
        private List<ProtocolApplied> protocolApplied = new ArrayList<>();

        private Builder(ImmunizationStatus status, CodeableConcept vaccineCode, Reference patient, Element occurrence) {
            super();
            this.status = status;
            this.vaccineCode = vaccineCode;
            this.patient = patient;
            this.occurrence = occurrence;
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
         * A unique identifier assigned to this immunization record.
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
         * A unique identifier assigned to this immunization record.
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
         * Indicates the reason the immunization event was not performed.
         * </p>
         * 
         * @param statusReason
         *     Reason not done
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusReason(CodeableConcept statusReason) {
            this.statusReason = statusReason;
            return this;
        }

        /**
         * <p>
         * The visit or admission or other contact between patient and health care provider the immunization was performed as 
         * part of.
         * </p>
         * 
         * @param encounter
         *     Encounter immunization was part of
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
         * The date the occurrence of the immunization was first captured in the record - potentially significantly after the 
         * occurrence of the event.
         * </p>
         * 
         * @param recorded
         *     When the immunization was first captured in the subject's record
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recorded(DateTime recorded) {
            this.recorded = recorded;
            return this;
        }

        /**
         * <p>
         * An indication that the content of the record is based on information from the person who administered the vaccine. 
         * This reflects the context under which the data was originally recorded.
         * </p>
         * 
         * @param primarySource
         *     Indicates context the data was recorded in
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder primarySource(Boolean primarySource) {
            this.primarySource = primarySource;
            return this;
        }

        /**
         * <p>
         * The source of the data when the report of the immunization event is not based on information from the person who 
         * administered the vaccine.
         * </p>
         * 
         * @param reportOrigin
         *     Indicates the source of a secondarily reported record
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reportOrigin(CodeableConcept reportOrigin) {
            this.reportOrigin = reportOrigin;
            return this;
        }

        /**
         * <p>
         * The service delivery location where the vaccine administration occurred.
         * </p>
         * 
         * @param location
         *     Where immunization occurred
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(Reference location) {
            this.location = location;
            return this;
        }

        /**
         * <p>
         * Name of vaccine manufacturer.
         * </p>
         * 
         * @param manufacturer
         *     Vaccine manufacturer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufacturer(Reference manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        /**
         * <p>
         * Lot number of the vaccine product.
         * </p>
         * 
         * @param lotNumber
         *     Vaccine lot number
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lotNumber(String lotNumber) {
            this.lotNumber = lotNumber;
            return this;
        }

        /**
         * <p>
         * Date vaccine batch expires.
         * </p>
         * 
         * @param expirationDate
         *     Vaccine expiration date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder expirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        /**
         * <p>
         * Body site where vaccine was administered.
         * </p>
         * 
         * @param site
         *     Body site vaccine was administered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder site(CodeableConcept site) {
            this.site = site;
            return this;
        }

        /**
         * <p>
         * The path by which the vaccine product is taken into the body.
         * </p>
         * 
         * @param route
         *     How vaccine entered body
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder route(CodeableConcept route) {
            this.route = route;
            return this;
        }

        /**
         * <p>
         * The quantity of vaccine product that was administered.
         * </p>
         * 
         * @param doseQuantity
         *     Amount of vaccine administered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder doseQuantity(Quantity doseQuantity) {
            this.doseQuantity = doseQuantity;
            return this;
        }

        /**
         * <p>
         * Indicates who performed the immunization event.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param performer
         *     Who performed event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Performer... performer) {
            for (Performer value : performer) {
                this.performer.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates who performed the immunization event.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param performer
         *     Who performed event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Collection<Performer> performer) {
            this.performer = new ArrayList<>(performer);
            return this;
        }

        /**
         * <p>
         * Extra information about the immunization that is not conveyed by the other attributes.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param note
         *     Additional immunization notes
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
         * Extra information about the immunization that is not conveyed by the other attributes.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Additional immunization notes
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
         * Reasons why the vaccine was administered.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param reasonCode
         *     Why immunization occurred
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
         * Reasons why the vaccine was administered.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonCode
         *     Why immunization occurred
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
         * Condition, Observation or DiagnosticReport that supports why the immunization was administered.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param reasonReference
         *     Why immunization occurred
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
         * Condition, Observation or DiagnosticReport that supports why the immunization was administered.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonReference
         *     Why immunization occurred
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
         * Indication if a dose is considered to be subpotent. By default, a dose should be considered to be potent.
         * </p>
         * 
         * @param isSubpotent
         *     Dose potency
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder isSubpotent(Boolean isSubpotent) {
            this.isSubpotent = isSubpotent;
            return this;
        }

        /**
         * <p>
         * Reason why a dose is considered to be subpotent.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param subpotentReason
         *     Reason for being subpotent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subpotentReason(CodeableConcept... subpotentReason) {
            for (CodeableConcept value : subpotentReason) {
                this.subpotentReason.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Reason why a dose is considered to be subpotent.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param subpotentReason
         *     Reason for being subpotent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subpotentReason(Collection<CodeableConcept> subpotentReason) {
            this.subpotentReason = new ArrayList<>(subpotentReason);
            return this;
        }

        /**
         * <p>
         * Educational material presented to the patient (or guardian) at the time of vaccine administration.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param education
         *     Educational material presented to patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder education(Education... education) {
            for (Education value : education) {
                this.education.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Educational material presented to the patient (or guardian) at the time of vaccine administration.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param education
         *     Educational material presented to patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder education(Collection<Education> education) {
            this.education = new ArrayList<>(education);
            return this;
        }

        /**
         * <p>
         * Indicates a patient's eligibility for a funding program.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param programEligibility
         *     Patient eligibility for a vaccination program
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder programEligibility(CodeableConcept... programEligibility) {
            for (CodeableConcept value : programEligibility) {
                this.programEligibility.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates a patient's eligibility for a funding program.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param programEligibility
         *     Patient eligibility for a vaccination program
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder programEligibility(Collection<CodeableConcept> programEligibility) {
            this.programEligibility = new ArrayList<>(programEligibility);
            return this;
        }

        /**
         * <p>
         * Indicates the source of the vaccine actually administered. This may be different than the patient eligibility (e.g. 
         * the patient may be eligible for a publically purchased vaccine but due to inventory issues, vaccine purchased with 
         * private funds was actually administered).
         * </p>
         * 
         * @param fundingSource
         *     Funding source for the vaccine
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder fundingSource(CodeableConcept fundingSource) {
            this.fundingSource = fundingSource;
            return this;
        }

        /**
         * <p>
         * Categorical data indicating that an adverse event is associated in time to an immunization.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param reaction
         *     Details of a reaction that follows immunization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reaction(Reaction... reaction) {
            for (Reaction value : reaction) {
                this.reaction.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Categorical data indicating that an adverse event is associated in time to an immunization.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reaction
         *     Details of a reaction that follows immunization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reaction(Collection<Reaction> reaction) {
            this.reaction = new ArrayList<>(reaction);
            return this;
        }

        /**
         * <p>
         * The protocol (set of recommendations) being followed by the provider who administered the dose.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param protocolApplied
         *     Protocol followed by the provider
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder protocolApplied(ProtocolApplied... protocolApplied) {
            for (ProtocolApplied value : protocolApplied) {
                this.protocolApplied.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The protocol (set of recommendations) being followed by the provider who administered the dose.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param protocolApplied
         *     Protocol followed by the provider
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder protocolApplied(Collection<ProtocolApplied> protocolApplied) {
            this.protocolApplied = new ArrayList<>(protocolApplied);
            return this;
        }

        @Override
        public Immunization build() {
            return new Immunization(this);
        }

        private Builder from(Immunization immunization) {
            id = immunization.id;
            meta = immunization.meta;
            implicitRules = immunization.implicitRules;
            language = immunization.language;
            text = immunization.text;
            contained.addAll(immunization.contained);
            extension.addAll(immunization.extension);
            modifierExtension.addAll(immunization.modifierExtension);
            identifier.addAll(immunization.identifier);
            statusReason = immunization.statusReason;
            encounter = immunization.encounter;
            recorded = immunization.recorded;
            primarySource = immunization.primarySource;
            reportOrigin = immunization.reportOrigin;
            location = immunization.location;
            manufacturer = immunization.manufacturer;
            lotNumber = immunization.lotNumber;
            expirationDate = immunization.expirationDate;
            site = immunization.site;
            route = immunization.route;
            doseQuantity = immunization.doseQuantity;
            performer.addAll(immunization.performer);
            note.addAll(immunization.note);
            reasonCode.addAll(immunization.reasonCode);
            reasonReference.addAll(immunization.reasonReference);
            isSubpotent = immunization.isSubpotent;
            subpotentReason.addAll(immunization.subpotentReason);
            education.addAll(immunization.education);
            programEligibility.addAll(immunization.programEligibility);
            fundingSource = immunization.fundingSource;
            reaction.addAll(immunization.reaction);
            protocolApplied.addAll(immunization.protocolApplied);
            return this;
        }
    }

    /**
     * <p>
     * Indicates who performed the immunization event.
     * </p>
     */
    public static class Performer extends BackboneElement {
        private final CodeableConcept function;
        private final Reference actor;

        private volatile int hashCode;

        private Performer(Builder builder) {
            super(builder);
            function = builder.function;
            actor = ValidationSupport.requireNonNull(builder.actor, "actor");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Describes the type of performance (e.g. ordering provider, administering provider, etc.).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getFunction() {
            return function;
        }

        /**
         * <p>
         * The practitioner or organization who performed the action.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getActor() {
            return actor;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (function != null) || 
                (actor != null);
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
                    accept(function, "function", visitor);
                    accept(actor, "actor", visitor);
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
            Performer other = (Performer) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(function, other.function) && 
                Objects.equals(actor, other.actor);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    function, 
                    actor);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(actor).from(this);
        }

        public Builder toBuilder(Reference actor) {
            return new Builder(actor).from(this);
        }

        public static Builder builder(Reference actor) {
            return new Builder(actor);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference actor;

            // optional
            private CodeableConcept function;

            private Builder(Reference actor) {
                super();
                this.actor = actor;
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
             * Describes the type of performance (e.g. ordering provider, administering provider, etc.).
             * </p>
             * 
             * @param function
             *     What type of performance was done
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder function(CodeableConcept function) {
                this.function = function;
                return this;
            }

            @Override
            public Performer build() {
                return new Performer(this);
            }

            private Builder from(Performer performer) {
                id = performer.id;
                extension.addAll(performer.extension);
                modifierExtension.addAll(performer.modifierExtension);
                function = performer.function;
                return this;
            }
        }
    }

    /**
     * <p>
     * Educational material presented to the patient (or guardian) at the time of vaccine administration.
     * </p>
     */
    public static class Education extends BackboneElement {
        private final String documentType;
        private final Uri reference;
        private final DateTime publicationDate;
        private final DateTime presentationDate;

        private volatile int hashCode;

        private Education(Builder builder) {
            super(builder);
            documentType = builder.documentType;
            reference = builder.reference;
            publicationDate = builder.publicationDate;
            presentationDate = builder.presentationDate;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Identifier of the material presented to the patient.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDocumentType() {
            return documentType;
        }

        /**
         * <p>
         * Reference pointer to the educational material given to the patient if the information was on line.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getReference() {
            return reference;
        }

        /**
         * <p>
         * Date the educational material was published.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getPublicationDate() {
            return publicationDate;
        }

        /**
         * <p>
         * Date the educational material was given to the patient.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getPresentationDate() {
            return presentationDate;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (documentType != null) || 
                (reference != null) || 
                (publicationDate != null) || 
                (presentationDate != null);
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
                    accept(documentType, "documentType", visitor);
                    accept(reference, "reference", visitor);
                    accept(publicationDate, "publicationDate", visitor);
                    accept(presentationDate, "presentationDate", visitor);
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
            Education other = (Education) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(documentType, other.documentType) && 
                Objects.equals(reference, other.reference) && 
                Objects.equals(publicationDate, other.publicationDate) && 
                Objects.equals(presentationDate, other.presentationDate);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    documentType, 
                    reference, 
                    publicationDate, 
                    presentationDate);
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
            private String documentType;
            private Uri reference;
            private DateTime publicationDate;
            private DateTime presentationDate;

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
             * Identifier of the material presented to the patient.
             * </p>
             * 
             * @param documentType
             *     Educational material document identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentType(String documentType) {
                this.documentType = documentType;
                return this;
            }

            /**
             * <p>
             * Reference pointer to the educational material given to the patient if the information was on line.
             * </p>
             * 
             * @param reference
             *     Educational material reference pointer
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reference(Uri reference) {
                this.reference = reference;
                return this;
            }

            /**
             * <p>
             * Date the educational material was published.
             * </p>
             * 
             * @param publicationDate
             *     Educational material publication date
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder publicationDate(DateTime publicationDate) {
                this.publicationDate = publicationDate;
                return this;
            }

            /**
             * <p>
             * Date the educational material was given to the patient.
             * </p>
             * 
             * @param presentationDate
             *     Educational material presentation date
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder presentationDate(DateTime presentationDate) {
                this.presentationDate = presentationDate;
                return this;
            }

            @Override
            public Education build() {
                return new Education(this);
            }

            private Builder from(Education education) {
                id = education.id;
                extension.addAll(education.extension);
                modifierExtension.addAll(education.modifierExtension);
                documentType = education.documentType;
                reference = education.reference;
                publicationDate = education.publicationDate;
                presentationDate = education.presentationDate;
                return this;
            }
        }
    }

    /**
     * <p>
     * Categorical data indicating that an adverse event is associated in time to an immunization.
     * </p>
     */
    public static class Reaction extends BackboneElement {
        private final DateTime date;
        private final Reference detail;
        private final Boolean reported;

        private volatile int hashCode;

        private Reaction(Builder builder) {
            super(builder);
            date = builder.date;
            detail = builder.detail;
            reported = builder.reported;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Date of reaction to the immunization.
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
         * Details of the reaction.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getDetail() {
            return detail;
        }

        /**
         * <p>
         * Self-reported indicator.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getReported() {
            return reported;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (date != null) || 
                (detail != null) || 
                (reported != null);
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
                    accept(date, "date", visitor);
                    accept(detail, "detail", visitor);
                    accept(reported, "reported", visitor);
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
            Reaction other = (Reaction) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(date, other.date) && 
                Objects.equals(detail, other.detail) && 
                Objects.equals(reported, other.reported);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    date, 
                    detail, 
                    reported);
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
            private DateTime date;
            private Reference detail;
            private Boolean reported;

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
             * Date of reaction to the immunization.
             * </p>
             * 
             * @param date
             *     When reaction started
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
             * Details of the reaction.
             * </p>
             * 
             * @param detail
             *     Additional information on reaction
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Reference detail) {
                this.detail = detail;
                return this;
            }

            /**
             * <p>
             * Self-reported indicator.
             * </p>
             * 
             * @param reported
             *     Indicates self-reported reaction
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reported(Boolean reported) {
                this.reported = reported;
                return this;
            }

            @Override
            public Reaction build() {
                return new Reaction(this);
            }

            private Builder from(Reaction reaction) {
                id = reaction.id;
                extension.addAll(reaction.extension);
                modifierExtension.addAll(reaction.modifierExtension);
                date = reaction.date;
                detail = reaction.detail;
                reported = reaction.reported;
                return this;
            }
        }
    }

    /**
     * <p>
     * The protocol (set of recommendations) being followed by the provider who administered the dose.
     * </p>
     */
    public static class ProtocolApplied extends BackboneElement {
        private final String series;
        private final Reference authority;
        private final List<CodeableConcept> targetDisease;
        private final Element doseNumber;
        private final Element seriesDoses;

        private volatile int hashCode;

        private ProtocolApplied(Builder builder) {
            super(builder);
            series = builder.series;
            authority = builder.authority;
            targetDisease = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.targetDisease, "targetDisease"));
            doseNumber = ValidationSupport.requireChoiceElement(builder.doseNumber, "doseNumber", PositiveInt.class, String.class);
            seriesDoses = ValidationSupport.choiceElement(builder.seriesDoses, "seriesDoses", PositiveInt.class, String.class);
            ValidationSupport.requireValueOrChildren(this);
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
         * Indicates the authority who published the protocol (e.g. ACIP) that is being followed.
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
         * The vaccine preventable disease the dose is being administered against.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getTargetDisease() {
            return targetDisease;
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
        public boolean hasChildren() {
            return super.hasChildren() || 
                (series != null) || 
                (authority != null) || 
                !targetDisease.isEmpty() || 
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
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(series, "series", visitor);
                    accept(authority, "authority", visitor);
                    accept(targetDisease, "targetDisease", visitor, CodeableConcept.class);
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
            ProtocolApplied other = (ProtocolApplied) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(series, other.series) && 
                Objects.equals(authority, other.authority) && 
                Objects.equals(targetDisease, other.targetDisease) && 
                Objects.equals(doseNumber, other.doseNumber) && 
                Objects.equals(seriesDoses, other.seriesDoses);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    series, 
                    authority, 
                    targetDisease, 
                    doseNumber, 
                    seriesDoses);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(doseNumber).from(this);
        }

        public Builder toBuilder(Element doseNumber) {
            return new Builder(doseNumber).from(this);
        }

        public static Builder builder(Element doseNumber) {
            return new Builder(doseNumber);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Element doseNumber;

            // optional
            private String series;
            private Reference authority;
            private List<CodeableConcept> targetDisease = new ArrayList<>();
            private Element seriesDoses;

            private Builder(Element doseNumber) {
                super();
                this.doseNumber = doseNumber;
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
             * Indicates the authority who published the protocol (e.g. ACIP) that is being followed.
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
             * The vaccine preventable disease the dose is being administered against.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param targetDisease
             *     Vaccine preventatable disease being targetted
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder targetDisease(CodeableConcept... targetDisease) {
                for (CodeableConcept value : targetDisease) {
                    this.targetDisease.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The vaccine preventable disease the dose is being administered against.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param targetDisease
             *     Vaccine preventatable disease being targetted
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder targetDisease(Collection<CodeableConcept> targetDisease) {
                this.targetDisease = new ArrayList<>(targetDisease);
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
            public ProtocolApplied build() {
                return new ProtocolApplied(this);
            }

            private Builder from(ProtocolApplied protocolApplied) {
                id = protocolApplied.id;
                extension.addAll(protocolApplied.extension);
                modifierExtension.addAll(protocolApplied.modifierExtension);
                series = protocolApplied.series;
                authority = protocolApplied.authority;
                targetDisease.addAll(protocolApplied.targetDisease);
                seriesDoses = protocolApplied.seriesDoses;
                return this;
            }
        }
    }
}
