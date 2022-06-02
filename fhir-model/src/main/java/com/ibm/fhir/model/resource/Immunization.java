/*
 * (C) Copyright IBM Corp. 2019, 2022
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
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ImmunizationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a patient, 
 * a clinician or another party.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "imm-1",
    level = "Rule",
    location = "Immunization.education",
    description = "One of documentType or reference SHALL be present",
    expression = "documentType.exists() or reference.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Immunization"
)
@Constraint(
    id = "immunization-2",
    level = "Warning",
    location = "performer.function",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/immunization-function",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/immunization-function', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Immunization",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Immunization extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "ImmunizationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "x",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-status|4.3.0-cibuild"
    )
    @Required
    private final ImmunizationStatus status;
    @Binding(
        bindingName = "ImmunizationStatusReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "x",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-status-reason"
    )
    private final CodeableConcept statusReason;
    @Summary
    @Binding(
        bindingName = "VaccineCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "x",
        valueSet = "http://hl7.org/fhir/ValueSet/vaccine-code"
    )
    @Required
    private final CodeableConcept vaccineCode;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference patient;
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    @Choice({ DateTime.class, String.class })
    @Required
    private final Element occurrence;
    private final DateTime recorded;
    @Summary
    private final Boolean primarySource;
    @Binding(
        bindingName = "ImmunizationReportOrigin",
        strength = BindingStrength.Value.EXAMPLE,
        description = "x",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-origin"
    )
    private final CodeableConcept reportOrigin;
    @ReferenceTarget({ "Location" })
    private final Reference location;
    @ReferenceTarget({ "Organization" })
    private final Reference manufacturer;
    private final String lotNumber;
    private final Date expirationDate;
    @Binding(
        bindingName = "ImmunizationSite",
        strength = BindingStrength.Value.EXAMPLE,
        description = "x",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-site"
    )
    private final CodeableConcept site;
    @Binding(
        bindingName = "ImmunizationRoute",
        strength = BindingStrength.Value.EXAMPLE,
        description = "x",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-route"
    )
    private final CodeableConcept route;
    private final SimpleQuantity doseQuantity;
    @Summary
    private final List<Performer> performer;
    @Summary
    private final List<Annotation> note;
    @Binding(
        bindingName = "ImmunizationReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "x",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-reason"
    )
    private final List<CodeableConcept> reasonCode;
    @ReferenceTarget({ "Condition", "Observation", "DiagnosticReport" })
    private final List<Reference> reasonReference;
    @Summary
    private final Boolean isSubpotent;
    @Binding(
        bindingName = "SubpotentReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The reason why a dose is considered to be subpotent.",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-subpotent-reason"
    )
    private final List<CodeableConcept> subpotentReason;
    private final List<Education> education;
    @Binding(
        bindingName = "ProgramEligibility",
        strength = BindingStrength.Value.EXAMPLE,
        description = "x",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-program-eligibility"
    )
    private final List<CodeableConcept> programEligibility;
    @Binding(
        bindingName = "FundingSource",
        strength = BindingStrength.Value.EXAMPLE,
        description = "x",
        valueSet = "http://hl7.org/fhir/ValueSet/immunization-funding-source"
    )
    private final CodeableConcept fundingSource;
    private final List<Reaction> reaction;
    private final List<ProtocolApplied> protocolApplied;

    private Immunization(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        statusReason = builder.statusReason;
        vaccineCode = builder.vaccineCode;
        patient = builder.patient;
        encounter = builder.encounter;
        occurrence = builder.occurrence;
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
        performer = Collections.unmodifiableList(builder.performer);
        note = Collections.unmodifiableList(builder.note);
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        isSubpotent = builder.isSubpotent;
        subpotentReason = Collections.unmodifiableList(builder.subpotentReason);
        education = Collections.unmodifiableList(builder.education);
        programEligibility = Collections.unmodifiableList(builder.programEligibility);
        fundingSource = builder.fundingSource;
        reaction = Collections.unmodifiableList(builder.reaction);
        protocolApplied = Collections.unmodifiableList(builder.protocolApplied);
    }

    /**
     * A unique identifier assigned to this immunization record.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Indicates the current status of the immunization event.
     * 
     * @return
     *     An immutable object of type {@link ImmunizationStatus} that is non-null.
     */
    public ImmunizationStatus getStatus() {
        return status;
    }

    /**
     * Indicates the reason the immunization event was not performed.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatusReason() {
        return statusReason;
    }

    /**
     * Vaccine that was administered or was to be administered.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getVaccineCode() {
        return vaccineCode;
    }

    /**
     * The patient who either received or did not receive the immunization.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * The visit or admission or other contact between patient and health care provider the immunization was performed as 
     * part of.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * Date vaccine administered or was to be administered.
     * 
     * @return
     *     An immutable object of type {@link DateTime} or {@link String} that is non-null.
     */
    public Element getOccurrence() {
        return occurrence;
    }

    /**
     * The date the occurrence of the immunization was first captured in the record - potentially significantly after the 
     * occurrence of the event.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getRecorded() {
        return recorded;
    }

    /**
     * An indication that the content of the record is based on information from the person who administered the vaccine. 
     * This reflects the context under which the data was originally recorded.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getPrimarySource() {
        return primarySource;
    }

    /**
     * The source of the data when the report of the immunization event is not based on information from the person who 
     * administered the vaccine.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getReportOrigin() {
        return reportOrigin;
    }

    /**
     * The service delivery location where the vaccine administration occurred.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getLocation() {
        return location;
    }

    /**
     * Name of vaccine manufacturer.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getManufacturer() {
        return manufacturer;
    }

    /**
     * Lot number of the vaccine product.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * Date vaccine batch expires.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Body site where vaccine was administered.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSite() {
        return site;
    }

    /**
     * The path by which the vaccine product is taken into the body.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getRoute() {
        return route;
    }

    /**
     * The quantity of vaccine product that was administered.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getDoseQuantity() {
        return doseQuantity;
    }

    /**
     * Indicates who performed the immunization event.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Performer} that may be empty.
     */
    public List<Performer> getPerformer() {
        return performer;
    }

    /**
     * Extra information about the immunization that is not conveyed by the other attributes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Reasons why the vaccine was administered.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Condition, Observation or DiagnosticReport that supports why the immunization was administered.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * Indication if a dose is considered to be subpotent. By default, a dose should be considered to be potent.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getIsSubpotent() {
        return isSubpotent;
    }

    /**
     * Reason why a dose is considered to be subpotent.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getSubpotentReason() {
        return subpotentReason;
    }

    /**
     * Educational material presented to the patient (or guardian) at the time of vaccine administration.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Education} that may be empty.
     */
    public List<Education> getEducation() {
        return education;
    }

    /**
     * Indicates a patient's eligibility for a funding program.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getProgramEligibility() {
        return programEligibility;
    }

    /**
     * Indicates the source of the vaccine actually administered. This may be different than the patient eligibility (e.g. 
     * the patient may be eligible for a publically purchased vaccine but due to inventory issues, vaccine purchased with 
     * private funds was actually administered).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getFundingSource() {
        return fundingSource;
    }

    /**
     * Categorical data indicating that an adverse event is associated in time to an immunization.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reaction} that may be empty.
     */
    public List<Reaction> getReaction() {
        return reaction;
    }

    /**
     * The protocol (set of recommendations) being followed by the provider who administered the dose.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ProtocolApplied} that may be empty.
     */
    public List<ProtocolApplied> getProtocolApplied() {
        return protocolApplied;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (statusReason != null) || 
            (vaccineCode != null) || 
            (patient != null) || 
            (encounter != null) || 
            (occurrence != null) || 
            (recorded != null) || 
            (primarySource != null) || 
            (reportOrigin != null) || 
            (location != null) || 
            (manufacturer != null) || 
            (lotNumber != null) || 
            (expirationDate != null) || 
            (site != null) || 
            (route != null) || 
            (doseQuantity != null) || 
            !performer.isEmpty() || 
            !note.isEmpty() || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            (isSubpotent != null) || 
            !subpotentReason.isEmpty() || 
            !education.isEmpty() || 
            !programEligibility.isEmpty() || 
            (fundingSource != null) || 
            !reaction.isEmpty() || 
            !protocolApplied.isEmpty();
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
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private ImmunizationStatus status;
        private CodeableConcept statusReason;
        private CodeableConcept vaccineCode;
        private Reference patient;
        private Reference encounter;
        private Element occurrence;
        private DateTime recorded;
        private Boolean primarySource;
        private CodeableConcept reportOrigin;
        private Reference location;
        private Reference manufacturer;
        private String lotNumber;
        private Date expirationDate;
        private CodeableConcept site;
        private CodeableConcept route;
        private SimpleQuantity doseQuantity;
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
         * A unique identifier assigned to this immunization record.
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
         * A unique identifier assigned to this immunization record.
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
         * Indicates the current status of the immunization event.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     completed | entered-in-error | not-done
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ImmunizationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Indicates the reason the immunization event was not performed.
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
         * Vaccine that was administered or was to be administered.
         * 
         * <p>This element is required.
         * 
         * @param vaccineCode
         *     Vaccine product administered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder vaccineCode(CodeableConcept vaccineCode) {
            this.vaccineCode = vaccineCode;
            return this;
        }

        /**
         * The patient who either received or did not receive the immunization.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Who was immunized
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * The visit or admission or other contact between patient and health care provider the immunization was performed as 
         * part of.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
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
         * Convenience method for setting {@code occurrence} with choice type String.
         * 
         * <p>This element is required.
         * 
         * @param occurrence
         *     Vaccine administration date
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #occurrence(Element)
         */
        public Builder occurrence(java.lang.String occurrence) {
            this.occurrence = (occurrence == null) ? null : String.of(occurrence);
            return this;
        }

        /**
         * Date vaccine administered or was to be administered.
         * 
         * <p>This element is required.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link String}</li>
         * </ul>
         * 
         * @param occurrence
         *     Vaccine administration date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder occurrence(Element occurrence) {
            this.occurrence = occurrence;
            return this;
        }

        /**
         * The date the occurrence of the immunization was first captured in the record - potentially significantly after the 
         * occurrence of the event.
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
         * Convenience method for setting {@code primarySource}.
         * 
         * @param primarySource
         *     Indicates context the data was recorded in
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #primarySource(com.ibm.fhir.model.type.Boolean)
         */
        public Builder primarySource(java.lang.Boolean primarySource) {
            this.primarySource = (primarySource == null) ? null : Boolean.of(primarySource);
            return this;
        }

        /**
         * An indication that the content of the record is based on information from the person who administered the vaccine. 
         * This reflects the context under which the data was originally recorded.
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
         * The source of the data when the report of the immunization event is not based on information from the person who 
         * administered the vaccine.
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
         * The service delivery location where the vaccine administration occurred.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
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
         * Name of vaccine manufacturer.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
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
         * Convenience method for setting {@code lotNumber}.
         * 
         * @param lotNumber
         *     Vaccine lot number
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #lotNumber(com.ibm.fhir.model.type.String)
         */
        public Builder lotNumber(java.lang.String lotNumber) {
            this.lotNumber = (lotNumber == null) ? null : String.of(lotNumber);
            return this;
        }

        /**
         * Lot number of the vaccine product.
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
         * Convenience method for setting {@code expirationDate}.
         * 
         * @param expirationDate
         *     Vaccine expiration date
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #expirationDate(com.ibm.fhir.model.type.Date)
         */
        public Builder expirationDate(java.time.LocalDate expirationDate) {
            this.expirationDate = (expirationDate == null) ? null : Date.of(expirationDate);
            return this;
        }

        /**
         * Date vaccine batch expires.
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
         * Body site where vaccine was administered.
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
         * The path by which the vaccine product is taken into the body.
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
         * The quantity of vaccine product that was administered.
         * 
         * @param doseQuantity
         *     Amount of vaccine administered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder doseQuantity(SimpleQuantity doseQuantity) {
            this.doseQuantity = doseQuantity;
            return this;
        }

        /**
         * Indicates who performed the immunization event.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Indicates who performed the immunization event.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param performer
         *     Who performed event
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder performer(Collection<Performer> performer) {
            this.performer = new ArrayList<>(performer);
            return this;
        }

        /**
         * Extra information about the immunization that is not conveyed by the other attributes.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Extra information about the immunization that is not conveyed by the other attributes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Additional immunization notes
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
         * Reasons why the vaccine was administered.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Reasons why the vaccine was administered.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Why immunization occurred
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
         * Condition, Observation or DiagnosticReport that supports why the immunization was administered.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * </ul>
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
         * Condition, Observation or DiagnosticReport that supports why the immunization was administered.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Why immunization occurred
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
         * Convenience method for setting {@code isSubpotent}.
         * 
         * @param isSubpotent
         *     Dose potency
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #isSubpotent(com.ibm.fhir.model.type.Boolean)
         */
        public Builder isSubpotent(java.lang.Boolean isSubpotent) {
            this.isSubpotent = (isSubpotent == null) ? null : Boolean.of(isSubpotent);
            return this;
        }

        /**
         * Indication if a dose is considered to be subpotent. By default, a dose should be considered to be potent.
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
         * Reason why a dose is considered to be subpotent.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Reason why a dose is considered to be subpotent.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param subpotentReason
         *     Reason for being subpotent
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder subpotentReason(Collection<CodeableConcept> subpotentReason) {
            this.subpotentReason = new ArrayList<>(subpotentReason);
            return this;
        }

        /**
         * Educational material presented to the patient (or guardian) at the time of vaccine administration.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Educational material presented to the patient (or guardian) at the time of vaccine administration.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param education
         *     Educational material presented to patient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder education(Collection<Education> education) {
            this.education = new ArrayList<>(education);
            return this;
        }

        /**
         * Indicates a patient's eligibility for a funding program.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Indicates a patient's eligibility for a funding program.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param programEligibility
         *     Patient eligibility for a vaccination program
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder programEligibility(Collection<CodeableConcept> programEligibility) {
            this.programEligibility = new ArrayList<>(programEligibility);
            return this;
        }

        /**
         * Indicates the source of the vaccine actually administered. This may be different than the patient eligibility (e.g. 
         * the patient may be eligible for a publically purchased vaccine but due to inventory issues, vaccine purchased with 
         * private funds was actually administered).
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
         * Categorical data indicating that an adverse event is associated in time to an immunization.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Categorical data indicating that an adverse event is associated in time to an immunization.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reaction
         *     Details of a reaction that follows immunization
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reaction(Collection<Reaction> reaction) {
            this.reaction = new ArrayList<>(reaction);
            return this;
        }

        /**
         * The protocol (set of recommendations) being followed by the provider who administered the dose.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The protocol (set of recommendations) being followed by the provider who administered the dose.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param protocolApplied
         *     Protocol followed by the provider
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder protocolApplied(Collection<ProtocolApplied> protocolApplied) {
            this.protocolApplied = new ArrayList<>(protocolApplied);
            return this;
        }

        /**
         * Build the {@link Immunization}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>vaccineCode</li>
         * <li>patient</li>
         * <li>occurrence</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Immunization}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Immunization per the base specification
         */
        @Override
        public Immunization build() {
            Immunization immunization = new Immunization(this);
            if (validating) {
                validate(immunization);
            }
            return immunization;
        }

        protected void validate(Immunization immunization) {
            super.validate(immunization);
            ValidationSupport.checkList(immunization.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(immunization.status, "status");
            ValidationSupport.requireNonNull(immunization.vaccineCode, "vaccineCode");
            ValidationSupport.requireNonNull(immunization.patient, "patient");
            ValidationSupport.requireChoiceElement(immunization.occurrence, "occurrence", DateTime.class, String.class);
            ValidationSupport.checkList(immunization.performer, "performer", Performer.class);
            ValidationSupport.checkList(immunization.note, "note", Annotation.class);
            ValidationSupport.checkList(immunization.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.checkList(immunization.reasonReference, "reasonReference", Reference.class);
            ValidationSupport.checkList(immunization.subpotentReason, "subpotentReason", CodeableConcept.class);
            ValidationSupport.checkList(immunization.education, "education", Education.class);
            ValidationSupport.checkList(immunization.programEligibility, "programEligibility", CodeableConcept.class);
            ValidationSupport.checkList(immunization.reaction, "reaction", Reaction.class);
            ValidationSupport.checkList(immunization.protocolApplied, "protocolApplied", ProtocolApplied.class);
            ValidationSupport.checkReferenceType(immunization.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(immunization.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(immunization.location, "location", "Location");
            ValidationSupport.checkReferenceType(immunization.manufacturer, "manufacturer", "Organization");
            ValidationSupport.checkReferenceType(immunization.reasonReference, "reasonReference", "Condition", "Observation", "DiagnosticReport");
        }

        protected Builder from(Immunization immunization) {
            super.from(immunization);
            identifier.addAll(immunization.identifier);
            status = immunization.status;
            statusReason = immunization.statusReason;
            vaccineCode = immunization.vaccineCode;
            patient = immunization.patient;
            encounter = immunization.encounter;
            occurrence = immunization.occurrence;
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
     * Indicates who performed the immunization event.
     */
    public static class Performer extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "ImmunizationFunction",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "x",
            valueSet = "http://hl7.org/fhir/ValueSet/immunization-function"
        )
        private final CodeableConcept function;
        @Summary
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
        @Required
        private final Reference actor;

        private Performer(Builder builder) {
            super(builder);
            function = builder.function;
            actor = builder.actor;
        }

        /**
         * Describes the type of performance (e.g. ordering provider, administering provider, etc.).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getFunction() {
            return function;
        }

        /**
         * The practitioner or organization who performed the action.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept function;
            private Reference actor;

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
             * Describes the type of performance (e.g. ordering provider, administering provider, etc.).
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

            /**
             * The practitioner or organization who performed the action.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param actor
             *     Individual or organization who was performing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder actor(Reference actor) {
                this.actor = actor;
                return this;
            }

            /**
             * Build the {@link Performer}
             * 
             * <p>Required elements:
             * <ul>
             * <li>actor</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Performer}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Performer per the base specification
             */
            @Override
            public Performer build() {
                Performer performer = new Performer(this);
                if (validating) {
                    validate(performer);
                }
                return performer;
            }

            protected void validate(Performer performer) {
                super.validate(performer);
                ValidationSupport.requireNonNull(performer.actor, "actor");
                ValidationSupport.checkReferenceType(performer.actor, "actor", "Practitioner", "PractitionerRole", "Organization");
                ValidationSupport.requireValueOrChildren(performer);
            }

            protected Builder from(Performer performer) {
                super.from(performer);
                function = performer.function;
                actor = performer.actor;
                return this;
            }
        }
    }

    /**
     * Educational material presented to the patient (or guardian) at the time of vaccine administration.
     */
    public static class Education extends BackboneElement {
        private final String documentType;
        private final Uri reference;
        private final DateTime publicationDate;
        private final DateTime presentationDate;

        private Education(Builder builder) {
            super(builder);
            documentType = builder.documentType;
            reference = builder.reference;
            publicationDate = builder.publicationDate;
            presentationDate = builder.presentationDate;
        }

        /**
         * Identifier of the material presented to the patient.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDocumentType() {
            return documentType;
        }

        /**
         * Reference pointer to the educational material given to the patient if the information was on line.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getReference() {
            return reference;
        }

        /**
         * Date the educational material was published.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getPublicationDate() {
            return publicationDate;
        }

        /**
         * Date the educational material was given to the patient.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
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
            private String documentType;
            private Uri reference;
            private DateTime publicationDate;
            private DateTime presentationDate;

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
             * Convenience method for setting {@code documentType}.
             * 
             * @param documentType
             *     Educational material document identifier
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #documentType(com.ibm.fhir.model.type.String)
             */
            public Builder documentType(java.lang.String documentType) {
                this.documentType = (documentType == null) ? null : String.of(documentType);
                return this;
            }

            /**
             * Identifier of the material presented to the patient.
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
             * Reference pointer to the educational material given to the patient if the information was on line.
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
             * Date the educational material was published.
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
             * Date the educational material was given to the patient.
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

            /**
             * Build the {@link Education}
             * 
             * @return
             *     An immutable object of type {@link Education}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Education per the base specification
             */
            @Override
            public Education build() {
                Education education = new Education(this);
                if (validating) {
                    validate(education);
                }
                return education;
            }

            protected void validate(Education education) {
                super.validate(education);
                ValidationSupport.requireValueOrChildren(education);
            }

            protected Builder from(Education education) {
                super.from(education);
                documentType = education.documentType;
                reference = education.reference;
                publicationDate = education.publicationDate;
                presentationDate = education.presentationDate;
                return this;
            }
        }
    }

    /**
     * Categorical data indicating that an adverse event is associated in time to an immunization.
     */
    public static class Reaction extends BackboneElement {
        private final DateTime date;
        @ReferenceTarget({ "Observation" })
        private final Reference detail;
        private final Boolean reported;

        private Reaction(Builder builder) {
            super(builder);
            date = builder.date;
            detail = builder.detail;
            reported = builder.reported;
        }

        /**
         * Date of reaction to the immunization.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getDate() {
            return date;
        }

        /**
         * Details of the reaction.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getDetail() {
            return detail;
        }

        /**
         * Self-reported indicator.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
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
            private DateTime date;
            private Reference detail;
            private Boolean reported;

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
             * Date of reaction to the immunization.
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
             * Details of the reaction.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Observation}</li>
             * </ul>
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
             * Convenience method for setting {@code reported}.
             * 
             * @param reported
             *     Indicates self-reported reaction
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #reported(com.ibm.fhir.model.type.Boolean)
             */
            public Builder reported(java.lang.Boolean reported) {
                this.reported = (reported == null) ? null : Boolean.of(reported);
                return this;
            }

            /**
             * Self-reported indicator.
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

            /**
             * Build the {@link Reaction}
             * 
             * @return
             *     An immutable object of type {@link Reaction}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Reaction per the base specification
             */
            @Override
            public Reaction build() {
                Reaction reaction = new Reaction(this);
                if (validating) {
                    validate(reaction);
                }
                return reaction;
            }

            protected void validate(Reaction reaction) {
                super.validate(reaction);
                ValidationSupport.checkReferenceType(reaction.detail, "detail", "Observation");
                ValidationSupport.requireValueOrChildren(reaction);
            }

            protected Builder from(Reaction reaction) {
                super.from(reaction);
                date = reaction.date;
                detail = reaction.detail;
                reported = reaction.reported;
                return this;
            }
        }
    }

    /**
     * The protocol (set of recommendations) being followed by the provider who administered the dose.
     */
    public static class ProtocolApplied extends BackboneElement {
        private final String series;
        @ReferenceTarget({ "Organization" })
        private final Reference authority;
        @Binding(
            bindingName = "TargetDisease",
            strength = BindingStrength.Value.EXAMPLE,
            description = "x",
            valueSet = "http://hl7.org/fhir/ValueSet/immunization-target-disease"
        )
        private final List<CodeableConcept> targetDisease;
        @Choice({ PositiveInt.class, String.class })
        @Required
        private final Element doseNumber;
        @Choice({ PositiveInt.class, String.class })
        private final Element seriesDoses;

        private ProtocolApplied(Builder builder) {
            super(builder);
            series = builder.series;
            authority = builder.authority;
            targetDisease = Collections.unmodifiableList(builder.targetDisease);
            doseNumber = builder.doseNumber;
            seriesDoses = builder.seriesDoses;
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
         * Indicates the authority who published the protocol (e.g. ACIP) that is being followed.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getAuthority() {
            return authority;
        }

        /**
         * The vaccine preventable disease the dose is being administered against.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getTargetDisease() {
            return targetDisease;
        }

        /**
         * Nominal position in a series.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} or {@link String} that is non-null.
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private String series;
            private Reference authority;
            private List<CodeableConcept> targetDisease = new ArrayList<>();
            private Element doseNumber;
            private Element seriesDoses;

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
             * Indicates the authority who published the protocol (e.g. ACIP) that is being followed.
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
             * The vaccine preventable disease the dose is being administered against.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * The vaccine preventable disease the dose is being administered against.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param targetDisease
             *     Vaccine preventatable disease being targetted
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder targetDisease(Collection<CodeableConcept> targetDisease) {
                this.targetDisease = new ArrayList<>(targetDisease);
                return this;
            }

            /**
             * Convenience method for setting {@code doseNumber} with choice type String.
             * 
             * <p>This element is required.
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
             * <p>This element is required.
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
             * Build the {@link ProtocolApplied}
             * 
             * <p>Required elements:
             * <ul>
             * <li>doseNumber</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link ProtocolApplied}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ProtocolApplied per the base specification
             */
            @Override
            public ProtocolApplied build() {
                ProtocolApplied protocolApplied = new ProtocolApplied(this);
                if (validating) {
                    validate(protocolApplied);
                }
                return protocolApplied;
            }

            protected void validate(ProtocolApplied protocolApplied) {
                super.validate(protocolApplied);
                ValidationSupport.checkList(protocolApplied.targetDisease, "targetDisease", CodeableConcept.class);
                ValidationSupport.requireChoiceElement(protocolApplied.doseNumber, "doseNumber", PositiveInt.class, String.class);
                ValidationSupport.choiceElement(protocolApplied.seriesDoses, "seriesDoses", PositiveInt.class, String.class);
                ValidationSupport.checkReferenceType(protocolApplied.authority, "authority", "Organization");
                ValidationSupport.requireValueOrChildren(protocolApplied);
            }

            protected Builder from(ProtocolApplied protocolApplied) {
                super.from(protocolApplied);
                series = protocolApplied.series;
                authority = protocolApplied.authority;
                targetDisease.addAll(protocolApplied.targetDisease);
                doseNumber = protocolApplied.doseNumber;
                seriesDoses = protocolApplied.seriesDoses;
                return this;
            }
        }
    }
}
