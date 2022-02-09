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
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SampledData;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ObservationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Measurements and simple assertions made about a patient, device or other subject.
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Constraint(
    id = "obs-3",
    level = "Rule",
    location = "Observation.referenceRange",
    description = "Must have at least a low or a high or text",
    expression = "low.exists() or high.exists() or text.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Observation"
)
@Constraint(
    id = "obs-6",
    level = "Rule",
    location = "(base)",
    description = "dataAbsentReason SHALL only be present if Observation.value[x] is not present",
    expression = "dataAbsentReason.empty() or value.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/Observation"
)
@Constraint(
    id = "obs-7",
    level = "Rule",
    location = "(base)",
    description = "If Observation.code is the same as an Observation.component.code then the value element associated with the code SHALL NOT be present",
    expression = "value.empty() or component.code.where(coding.intersect(%resource.code.coding).exists()).empty()",
    source = "http://hl7.org/fhir/StructureDefinition/Observation"
)
@Constraint(
    id = "observation-8",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/observation-category",
    expression = "category.exists() implies (category.all(memberOf('http://hl7.org/fhir/ValueSet/observation-category', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/Observation",
    generated = true
)
@Constraint(
    id = "observation-9",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/data-absent-reason",
    expression = "dataAbsentReason.exists() implies (dataAbsentReason.memberOf('http://hl7.org/fhir/ValueSet/data-absent-reason', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Observation",
    generated = true
)
@Constraint(
    id = "observation-10",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/observation-interpretation",
    expression = "interpretation.exists() implies (interpretation.all(memberOf('http://hl7.org/fhir/ValueSet/observation-interpretation', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/Observation",
    generated = true
)
@Constraint(
    id = "observation-11",
    level = "Warning",
    location = "referenceRange.type",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/referencerange-meaning",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/referencerange-meaning', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/Observation",
    generated = true
)
@Constraint(
    id = "observation-12",
    level = "Warning",
    location = "component.dataAbsentReason",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/data-absent-reason",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/data-absent-reason', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Observation",
    generated = true
)
@Constraint(
    id = "observation-13",
    level = "Warning",
    location = "component.interpretation",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/observation-interpretation",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/observation-interpretation', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Observation",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Observation extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @ReferenceTarget({ "CarePlan", "DeviceRequest", "ImmunizationRecommendation", "MedicationRequest", "NutritionOrder", "ServiceRequest" })
    private final List<Reference> basedOn;
    @Summary
    @ReferenceTarget({ "MedicationAdministration", "MedicationDispense", "MedicationStatement", "Procedure", "Immunization", "ImagingStudy" })
    private final List<Reference> partOf;
    @Summary
    @Binding(
        bindingName = "ObservationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes providing the status of an observation.",
        valueSet = "http://hl7.org/fhir/ValueSet/observation-status|4.3.0-CIBUILD"
    )
    @Required
    private final ObservationStatus status;
    @Binding(
        bindingName = "ObservationCategory",
        strength = BindingStrength.Value.PREFERRED,
        description = "Codes for high level observation categories.",
        valueSet = "http://hl7.org/fhir/ValueSet/observation-category"
    )
    private final List<CodeableConcept> category;
    @Summary
    @Binding(
        bindingName = "ObservationCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes identifying names of simple observations.",
        valueSet = "http://hl7.org/fhir/ValueSet/observation-codes"
    )
    @Required
    private final CodeableConcept code;
    @Summary
    @ReferenceTarget({ "Patient", "Group", "Device", "Location", "Organization", "Procedure", "Practitioner", "Medication", "Substance" })
    private final Reference subject;
    @Summary
    private final List<Reference> focus;
    @Summary
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    @Choice({ DateTime.class, Period.class, Timing.class, Instant.class })
    private final Element effective;
    @Summary
    private final Instant issued;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "CareTeam", "Patient", "RelatedPerson" })
    private final List<Reference> performer;
    @Summary
    @Choice({ Quantity.class, CodeableConcept.class, String.class, Boolean.class, Integer.class, Range.class, Ratio.class, SampledData.class, Time.class, DateTime.class, Period.class })
    private final Element value;
    @Binding(
        bindingName = "ObservationValueAbsentReason",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Codes specifying why the result (`Observation.value[x]`) is missing.",
        valueSet = "http://hl7.org/fhir/ValueSet/data-absent-reason"
    )
    private final CodeableConcept dataAbsentReason;
    @Binding(
        bindingName = "ObservationInterpretation",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Codes identifying interpretations of observations.",
        valueSet = "http://hl7.org/fhir/ValueSet/observation-interpretation"
    )
    private final List<CodeableConcept> interpretation;
    private final List<Annotation> note;
    @Binding(
        bindingName = "BodySite",
        strength = BindingStrength.Value.EXAMPLE,
        description = "SNOMED CT Body site concepts",
        valueSet = "http://hl7.org/fhir/ValueSet/body-site"
    )
    private final CodeableConcept bodySite;
    @Binding(
        bindingName = "ObservationMethod",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Methods for simple observations.",
        valueSet = "http://hl7.org/fhir/ValueSet/observation-methods"
    )
    private final CodeableConcept method;
    @ReferenceTarget({ "Specimen" })
    private final Reference specimen;
    @ReferenceTarget({ "Device", "DeviceMetric" })
    private final Reference device;
    private final List<ReferenceRange> referenceRange;
    @Summary
    @ReferenceTarget({ "Observation", "QuestionnaireResponse", "MolecularSequence" })
    private final List<Reference> hasMember;
    @Summary
    @ReferenceTarget({ "DocumentReference", "ImagingStudy", "Media", "QuestionnaireResponse", "Observation", "MolecularSequence" })
    private final List<Reference> derivedFrom;
    @Summary
    private final List<Component> component;

    private Observation(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        partOf = Collections.unmodifiableList(builder.partOf);
        status = builder.status;
        category = Collections.unmodifiableList(builder.category);
        code = builder.code;
        subject = builder.subject;
        focus = Collections.unmodifiableList(builder.focus);
        encounter = builder.encounter;
        effective = builder.effective;
        issued = builder.issued;
        performer = Collections.unmodifiableList(builder.performer);
        value = builder.value;
        dataAbsentReason = builder.dataAbsentReason;
        interpretation = Collections.unmodifiableList(builder.interpretation);
        note = Collections.unmodifiableList(builder.note);
        bodySite = builder.bodySite;
        method = builder.method;
        specimen = builder.specimen;
        device = builder.device;
        referenceRange = Collections.unmodifiableList(builder.referenceRange);
        hasMember = Collections.unmodifiableList(builder.hasMember);
        derivedFrom = Collections.unmodifiableList(builder.derivedFrom);
        component = Collections.unmodifiableList(builder.component);
    }

    /**
     * A unique identifier assigned to this observation.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A plan, proposal or order that is fulfilled in whole or in part by this event. For example, a MedicationRequest may 
     * require a patient to have laboratory test performed before it is dispensed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * A larger event of which this particular Observation is a component or step. For example, an observation as part of a 
     * procedure.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * The status of the result value.
     * 
     * @return
     *     An immutable object of type {@link ObservationStatus} that is non-null.
     */
    public ObservationStatus getStatus() {
        return status;
    }

    /**
     * A code that classifies the general type of observation being made.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * Describes what was observed. Sometimes this is called the observation "name".
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * The patient, or group of patients, location, or device this observation is about and into whose record the observation 
     * is placed. If the actual focus of the observation is different from the subject (or a sample of, part, or region of 
     * the subject), the `focus` element or the `code` itself specifies the actual focus of the observation.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The actual focus of an observation when it is not the patient of record representing something or someone associated 
     * with the patient such as a spouse, parent, fetus, or donor. For example, fetus observations in a mother's record. The 
     * focus of an observation could also be an existing condition, an intervention, the subject's diet, another observation 
     * of the subject, or a body structure such as tumor or implanted device. An example use case would be using the 
     * Observation resource to capture whether the mother is trained to change her child's tracheostomy tube. In this 
     * example, the child is the patient of record and the mother is the focus.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getFocus() {
        return focus;
    }

    /**
     * The healthcare event (e.g. a patient and healthcare provider interaction) during which this observation is made.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - 
     * this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of 
     * specimen collection, but very often the source of the date/time is not known, only the date/time itself.
     * 
     * @return
     *     An immutable object of type {@link DateTime}, {@link Period}, {@link Timing} or {@link Instant} that may be null.
     */
    public Element getEffective() {
        return effective;
    }

    /**
     * The date and time this version of the observation was made available to providers, typically after the results have 
     * been reviewed and verified.
     * 
     * @return
     *     An immutable object of type {@link Instant} that may be null.
     */
    public Instant getIssued() {
        return issued;
    }

    /**
     * Who was responsible for asserting the observed value as "true".
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPerformer() {
        return performer;
    }

    /**
     * The information determined as a result of making the observation, if the information has a simple value.
     * 
     * @return
     *     An immutable object of type {@link Quantity}, {@link CodeableConcept}, {@link String}, {@link Boolean}, {@link 
     *     Integer}, {@link Range}, {@link Ratio}, {@link SampledData}, {@link Time}, {@link DateTime} or {@link Period} that may 
     *     be null.
     */
    public Element getValue() {
        return value;
    }

    /**
     * Provides a reason why the expected value in the element Observation.value[x] is missing.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getDataAbsentReason() {
        return dataAbsentReason;
    }

    /**
     * A categorical assessment of an observation value. For example, high, low, normal.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getInterpretation() {
        return interpretation;
    }

    /**
     * Comments about the observation or the results.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Indicates the site on the subject's body where the observation was made (i.e. the target site).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getBodySite() {
        return bodySite;
    }

    /**
     * Indicates the mechanism used to perform the observation.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getMethod() {
        return method;
    }

    /**
     * The specimen that was used when this observation was made.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSpecimen() {
        return specimen;
    }

    /**
     * The device used to generate the observation data.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getDevice() {
        return device;
    }

    /**
     * Guidance on how to interpret the value by comparison to a normal or recommended range. Multiple reference ranges are 
     * interpreted as an "OR". In other words, to represent two distinct target populations, two `referenceRange` elements 
     * would be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ReferenceRange} that may be empty.
     */
    public List<ReferenceRange> getReferenceRange() {
        return referenceRange;
    }

    /**
     * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that 
     * includes the target as a member of the group.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getHasMember() {
        return hasMember;
    }

    /**
     * The target resource that represents a measurement from which this observation value is derived. For example, a 
     * calculated anion gap or a fetal measurement based on an ultrasound image.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * Some observations have multiple component observations. These component observations are expressed as separate code 
     * value pairs that share the same attributes. Examples include systolic and diastolic component observations for blood 
     * pressure measurement and multiple component observations for genetics observations.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Component} that may be empty.
     */
    public List<Component> getComponent() {
        return component;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !basedOn.isEmpty() || 
            !partOf.isEmpty() || 
            (status != null) || 
            !category.isEmpty() || 
            (code != null) || 
            (subject != null) || 
            !focus.isEmpty() || 
            (encounter != null) || 
            (effective != null) || 
            (issued != null) || 
            !performer.isEmpty() || 
            (value != null) || 
            (dataAbsentReason != null) || 
            !interpretation.isEmpty() || 
            !note.isEmpty() || 
            (bodySite != null) || 
            (method != null) || 
            (specimen != null) || 
            (device != null) || 
            !referenceRange.isEmpty() || 
            !hasMember.isEmpty() || 
            !derivedFrom.isEmpty() || 
            !component.isEmpty();
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
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(code, "code", visitor);
                accept(subject, "subject", visitor);
                accept(focus, "focus", visitor, Reference.class);
                accept(encounter, "encounter", visitor);
                accept(effective, "effective", visitor);
                accept(issued, "issued", visitor);
                accept(performer, "performer", visitor, Reference.class);
                accept(value, "value", visitor);
                accept(dataAbsentReason, "dataAbsentReason", visitor);
                accept(interpretation, "interpretation", visitor, CodeableConcept.class);
                accept(note, "note", visitor, Annotation.class);
                accept(bodySite, "bodySite", visitor);
                accept(method, "method", visitor);
                accept(specimen, "specimen", visitor);
                accept(device, "device", visitor);
                accept(referenceRange, "referenceRange", visitor, ReferenceRange.class);
                accept(hasMember, "hasMember", visitor, Reference.class);
                accept(derivedFrom, "derivedFrom", visitor, Reference.class);
                accept(component, "component", visitor, Component.class);
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
        Observation other = (Observation) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(status, other.status) && 
            Objects.equals(category, other.category) && 
            Objects.equals(code, other.code) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(focus, other.focus) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(effective, other.effective) && 
            Objects.equals(issued, other.issued) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(value, other.value) && 
            Objects.equals(dataAbsentReason, other.dataAbsentReason) && 
            Objects.equals(interpretation, other.interpretation) && 
            Objects.equals(note, other.note) && 
            Objects.equals(bodySite, other.bodySite) && 
            Objects.equals(method, other.method) && 
            Objects.equals(specimen, other.specimen) && 
            Objects.equals(device, other.device) && 
            Objects.equals(referenceRange, other.referenceRange) && 
            Objects.equals(hasMember, other.hasMember) && 
            Objects.equals(derivedFrom, other.derivedFrom) && 
            Objects.equals(component, other.component);
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
                basedOn, 
                partOf, 
                status, 
                category, 
                code, 
                subject, 
                focus, 
                encounter, 
                effective, 
                issued, 
                performer, 
                value, 
                dataAbsentReason, 
                interpretation, 
                note, 
                bodySite, 
                method, 
                specimen, 
                device, 
                referenceRange, 
                hasMember, 
                derivedFrom, 
                component);
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
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private ObservationStatus status;
        private List<CodeableConcept> category = new ArrayList<>();
        private CodeableConcept code;
        private Reference subject;
        private List<Reference> focus = new ArrayList<>();
        private Reference encounter;
        private Element effective;
        private Instant issued;
        private List<Reference> performer = new ArrayList<>();
        private Element value;
        private CodeableConcept dataAbsentReason;
        private List<CodeableConcept> interpretation = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private CodeableConcept bodySite;
        private CodeableConcept method;
        private Reference specimen;
        private Reference device;
        private List<ReferenceRange> referenceRange = new ArrayList<>();
        private List<Reference> hasMember = new ArrayList<>();
        private List<Reference> derivedFrom = new ArrayList<>();
        private List<Component> component = new ArrayList<>();

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
         * A unique identifier assigned to this observation.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for observation
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
         * A unique identifier assigned to this observation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for observation
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
         * A plan, proposal or order that is fulfilled in whole or in part by this event. For example, a MedicationRequest may 
         * require a patient to have laboratory test performed before it is dispensed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link CarePlan}</li>
         * <li>{@link DeviceRequest}</li>
         * <li>{@link ImmunizationRecommendation}</li>
         * <li>{@link MedicationRequest}</li>
         * <li>{@link NutritionOrder}</li>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     Fulfills plan, proposal or order
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
         * A plan, proposal or order that is fulfilled in whole or in part by this event. For example, a MedicationRequest may 
         * require a patient to have laboratory test performed before it is dispensed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link CarePlan}</li>
         * <li>{@link DeviceRequest}</li>
         * <li>{@link ImmunizationRecommendation}</li>
         * <li>{@link MedicationRequest}</li>
         * <li>{@link NutritionOrder}</li>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     Fulfills plan, proposal or order
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
         * A larger event of which this particular Observation is a component or step. For example, an observation as part of a 
         * procedure.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicationAdministration}</li>
         * <li>{@link MedicationDispense}</li>
         * <li>{@link MedicationStatement}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Immunization}</li>
         * <li>{@link ImagingStudy}</li>
         * </ul>
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * A larger event of which this particular Observation is a component or step. For example, an observation as part of a 
         * procedure.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicationAdministration}</li>
         * <li>{@link MedicationDispense}</li>
         * <li>{@link MedicationStatement}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Immunization}</li>
         * <li>{@link ImagingStudy}</li>
         * </ul>
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * The status of the result value.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     registered | preliminary | final | amended +
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ObservationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * A code that classifies the general type of observation being made.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Classification of type of observation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * A code that classifies the general type of observation being made.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Classification of type of observation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * Describes what was observed. Sometimes this is called the observation "name".
         * 
         * <p>This element is required.
         * 
         * @param code
         *     Type of observation (code / type)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * The patient, or group of patients, location, or device this observation is about and into whose record the observation 
         * is placed. If the actual focus of the observation is different from the subject (or a sample of, part, or region of 
         * the subject), the `focus` element or the `code` itself specifies the actual focus of the observation.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * <li>{@link Device}</li>
         * <li>{@link Location}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link Medication}</li>
         * <li>{@link Substance}</li>
         * </ul>
         * 
         * @param subject
         *     Who and/or what the observation is about
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The actual focus of an observation when it is not the patient of record representing something or someone associated 
         * with the patient such as a spouse, parent, fetus, or donor. For example, fetus observations in a mother's record. The 
         * focus of an observation could also be an existing condition, an intervention, the subject's diet, another observation 
         * of the subject, or a body structure such as tumor or implanted device. An example use case would be using the 
         * Observation resource to capture whether the mother is trained to change her child's tracheostomy tube. In this 
         * example, the child is the patient of record and the mother is the focus.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param focus
         *     What the observation is about, when it is not about the subject of record
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder focus(Reference... focus) {
            for (Reference value : focus) {
                this.focus.add(value);
            }
            return this;
        }

        /**
         * The actual focus of an observation when it is not the patient of record representing something or someone associated 
         * with the patient such as a spouse, parent, fetus, or donor. For example, fetus observations in a mother's record. The 
         * focus of an observation could also be an existing condition, an intervention, the subject's diet, another observation 
         * of the subject, or a body structure such as tumor or implanted device. An example use case would be using the 
         * Observation resource to capture whether the mother is trained to change her child's tracheostomy tube. In this 
         * example, the child is the patient of record and the mother is the focus.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param focus
         *     What the observation is about, when it is not about the subject of record
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder focus(Collection<Reference> focus) {
            this.focus = new ArrayList<>(focus);
            return this;
        }

        /**
         * The healthcare event (e.g. a patient and healthcare provider interaction) during which this observation is made.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param encounter
         *     Healthcare event during which this observation is made
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * Convenience method for setting {@code effective} with choice type Instant.
         * 
         * @param effective
         *     Clinically relevant time/time-period for observation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #effective(Element)
         */
        public Builder effective(java.time.ZonedDateTime effective) {
            this.effective = (effective == null) ? null : Instant.of(effective);
            return this;
        }

        /**
         * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - 
         * this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of 
         * specimen collection, but very often the source of the date/time is not known, only the date/time itself.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link Period}</li>
         * <li>{@link Timing}</li>
         * <li>{@link Instant}</li>
         * </ul>
         * 
         * @param effective
         *     Clinically relevant time/time-period for observation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effective(Element effective) {
            this.effective = effective;
            return this;
        }

        /**
         * Convenience method for setting {@code issued}.
         * 
         * @param issued
         *     Date/Time this version was made available
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #issued(com.ibm.fhir.model.type.Instant)
         */
        public Builder issued(java.time.ZonedDateTime issued) {
            this.issued = (issued == null) ? null : Instant.of(issued);
            return this;
        }

        /**
         * The date and time this version of the observation was made available to providers, typically after the results have 
         * been reviewed and verified.
         * 
         * @param issued
         *     Date/Time this version was made available
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder issued(Instant issued) {
            this.issued = issued;
            return this;
        }

        /**
         * Who was responsible for asserting the observed value as "true".
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * <li>{@link CareTeam}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param performer
         *     Who is responsible for the observation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Reference... performer) {
            for (Reference value : performer) {
                this.performer.add(value);
            }
            return this;
        }

        /**
         * Who was responsible for asserting the observed value as "true".
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * <li>{@link CareTeam}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param performer
         *     Who is responsible for the observation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder performer(Collection<Reference> performer) {
            this.performer = new ArrayList<>(performer);
            return this;
        }

        /**
         * Convenience method for setting {@code value} with choice type String.
         * 
         * @param value
         *     Actual result
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #value(Element)
         */
        public Builder value(java.lang.String value) {
            this.value = (value == null) ? null : String.of(value);
            return this;
        }

        /**
         * Convenience method for setting {@code value} with choice type Boolean.
         * 
         * @param value
         *     Actual result
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #value(Element)
         */
        public Builder value(java.lang.Boolean value) {
            this.value = (value == null) ? null : Boolean.of(value);
            return this;
        }

        /**
         * Convenience method for setting {@code value} with choice type Integer.
         * 
         * @param value
         *     Actual result
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #value(Element)
         */
        public Builder value(java.lang.Integer value) {
            this.value = (value == null) ? null : Integer.of(value);
            return this;
        }

        /**
         * Convenience method for setting {@code value} with choice type Time.
         * 
         * @param value
         *     Actual result
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #value(Element)
         */
        public Builder value(java.time.LocalTime value) {
            this.value = (value == null) ? null : Time.of(value);
            return this;
        }

        /**
         * The information determined as a result of making the observation, if the information has a simple value.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Quantity}</li>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link String}</li>
         * <li>{@link Boolean}</li>
         * <li>{@link Integer}</li>
         * <li>{@link Range}</li>
         * <li>{@link Ratio}</li>
         * <li>{@link SampledData}</li>
         * <li>{@link Time}</li>
         * <li>{@link DateTime}</li>
         * <li>{@link Period}</li>
         * </ul>
         * 
         * @param value
         *     Actual result
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Element value) {
            this.value = value;
            return this;
        }

        /**
         * Provides a reason why the expected value in the element Observation.value[x] is missing.
         * 
         * @param dataAbsentReason
         *     Why the result is missing
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dataAbsentReason(CodeableConcept dataAbsentReason) {
            this.dataAbsentReason = dataAbsentReason;
            return this;
        }

        /**
         * A categorical assessment of an observation value. For example, high, low, normal.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param interpretation
         *     High, low, normal, etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder interpretation(CodeableConcept... interpretation) {
            for (CodeableConcept value : interpretation) {
                this.interpretation.add(value);
            }
            return this;
        }

        /**
         * A categorical assessment of an observation value. For example, high, low, normal.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param interpretation
         *     High, low, normal, etc.
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder interpretation(Collection<CodeableConcept> interpretation) {
            this.interpretation = new ArrayList<>(interpretation);
            return this;
        }

        /**
         * Comments about the observation or the results.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments about the observation
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
         * Comments about the observation or the results.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments about the observation
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
         * Indicates the site on the subject's body where the observation was made (i.e. the target site).
         * 
         * @param bodySite
         *     Observed body part
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder bodySite(CodeableConcept bodySite) {
            this.bodySite = bodySite;
            return this;
        }

        /**
         * Indicates the mechanism used to perform the observation.
         * 
         * @param method
         *     How it was done
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder method(CodeableConcept method) {
            this.method = method;
            return this;
        }

        /**
         * The specimen that was used when this observation was made.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Specimen}</li>
         * </ul>
         * 
         * @param specimen
         *     Specimen used for this observation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specimen(Reference specimen) {
            this.specimen = specimen;
            return this;
        }

        /**
         * The device used to generate the observation data.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * <li>{@link DeviceMetric}</li>
         * </ul>
         * 
         * @param device
         *     (Measurement) Device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder device(Reference device) {
            this.device = device;
            return this;
        }

        /**
         * Guidance on how to interpret the value by comparison to a normal or recommended range. Multiple reference ranges are 
         * interpreted as an "OR". In other words, to represent two distinct target populations, two `referenceRange` elements 
         * would be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param referenceRange
         *     Provides guide for interpretation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referenceRange(ReferenceRange... referenceRange) {
            for (ReferenceRange value : referenceRange) {
                this.referenceRange.add(value);
            }
            return this;
        }

        /**
         * Guidance on how to interpret the value by comparison to a normal or recommended range. Multiple reference ranges are 
         * interpreted as an "OR". In other words, to represent two distinct target populations, two `referenceRange` elements 
         * would be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param referenceRange
         *     Provides guide for interpretation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder referenceRange(Collection<ReferenceRange> referenceRange) {
            this.referenceRange = new ArrayList<>(referenceRange);
            return this;
        }

        /**
         * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that 
         * includes the target as a member of the group.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Observation}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * <li>{@link MolecularSequence}</li>
         * </ul>
         * 
         * @param hasMember
         *     Related resource that belongs to the Observation group
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder hasMember(Reference... hasMember) {
            for (Reference value : hasMember) {
                this.hasMember.add(value);
            }
            return this;
        }

        /**
         * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that 
         * includes the target as a member of the group.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Observation}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * <li>{@link MolecularSequence}</li>
         * </ul>
         * 
         * @param hasMember
         *     Related resource that belongs to the Observation group
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder hasMember(Collection<Reference> hasMember) {
            this.hasMember = new ArrayList<>(hasMember);
            return this;
        }

        /**
         * The target resource that represents a measurement from which this observation value is derived. For example, a 
         * calculated anion gap or a fetal measurement based on an ultrasound image.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DocumentReference}</li>
         * <li>{@link ImagingStudy}</li>
         * <li>{@link Media}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * <li>{@link Observation}</li>
         * <li>{@link MolecularSequence}</li>
         * </ul>
         * 
         * @param derivedFrom
         *     Related measurements the observation is made from
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder derivedFrom(Reference... derivedFrom) {
            for (Reference value : derivedFrom) {
                this.derivedFrom.add(value);
            }
            return this;
        }

        /**
         * The target resource that represents a measurement from which this observation value is derived. For example, a 
         * calculated anion gap or a fetal measurement based on an ultrasound image.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DocumentReference}</li>
         * <li>{@link ImagingStudy}</li>
         * <li>{@link Media}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * <li>{@link Observation}</li>
         * <li>{@link MolecularSequence}</li>
         * </ul>
         * 
         * @param derivedFrom
         *     Related measurements the observation is made from
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder derivedFrom(Collection<Reference> derivedFrom) {
            this.derivedFrom = new ArrayList<>(derivedFrom);
            return this;
        }

        /**
         * Some observations have multiple component observations. These component observations are expressed as separate code 
         * value pairs that share the same attributes. Examples include systolic and diastolic component observations for blood 
         * pressure measurement and multiple component observations for genetics observations.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param component
         *     Component results
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder component(Component... component) {
            for (Component value : component) {
                this.component.add(value);
            }
            return this;
        }

        /**
         * Some observations have multiple component observations. These component observations are expressed as separate code 
         * value pairs that share the same attributes. Examples include systolic and diastolic component observations for blood 
         * pressure measurement and multiple component observations for genetics observations.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param component
         *     Component results
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder component(Collection<Component> component) {
            this.component = new ArrayList<>(component);
            return this;
        }

        /**
         * Build the {@link Observation}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>code</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Observation}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Observation per the base specification
         */
        @Override
        public Observation build() {
            Observation observation = new Observation(this);
            if (validating) {
                validate(observation);
            }
            return observation;
        }

        protected void validate(Observation observation) {
            super.validate(observation);
            ValidationSupport.checkList(observation.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(observation.basedOn, "basedOn", Reference.class);
            ValidationSupport.checkList(observation.partOf, "partOf", Reference.class);
            ValidationSupport.requireNonNull(observation.status, "status");
            ValidationSupport.checkList(observation.category, "category", CodeableConcept.class);
            ValidationSupport.requireNonNull(observation.code, "code");
            ValidationSupport.checkList(observation.focus, "focus", Reference.class);
            ValidationSupport.choiceElement(observation.effective, "effective", DateTime.class, Period.class, Timing.class, Instant.class);
            ValidationSupport.checkList(observation.performer, "performer", Reference.class);
            ValidationSupport.choiceElement(observation.value, "value", Quantity.class, CodeableConcept.class, String.class, Boolean.class, Integer.class, Range.class, Ratio.class, SampledData.class, Time.class, DateTime.class, Period.class);
            ValidationSupport.checkList(observation.interpretation, "interpretation", CodeableConcept.class);
            ValidationSupport.checkList(observation.note, "note", Annotation.class);
            ValidationSupport.checkList(observation.referenceRange, "referenceRange", ReferenceRange.class);
            ValidationSupport.checkList(observation.hasMember, "hasMember", Reference.class);
            ValidationSupport.checkList(observation.derivedFrom, "derivedFrom", Reference.class);
            ValidationSupport.checkList(observation.component, "component", Component.class);
            ValidationSupport.checkReferenceType(observation.basedOn, "basedOn", "CarePlan", "DeviceRequest", "ImmunizationRecommendation", "MedicationRequest", "NutritionOrder", "ServiceRequest");
            ValidationSupport.checkReferenceType(observation.partOf, "partOf", "MedicationAdministration", "MedicationDispense", "MedicationStatement", "Procedure", "Immunization", "ImagingStudy");
            ValidationSupport.checkReferenceType(observation.subject, "subject", "Patient", "Group", "Device", "Location", "Organization", "Procedure", "Practitioner", "Medication", "Substance");
            ValidationSupport.checkReferenceType(observation.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(observation.performer, "performer", "Practitioner", "PractitionerRole", "Organization", "CareTeam", "Patient", "RelatedPerson");
            ValidationSupport.checkReferenceType(observation.specimen, "specimen", "Specimen");
            ValidationSupport.checkReferenceType(observation.device, "device", "Device", "DeviceMetric");
            ValidationSupport.checkReferenceType(observation.hasMember, "hasMember", "Observation", "QuestionnaireResponse", "MolecularSequence");
            ValidationSupport.checkReferenceType(observation.derivedFrom, "derivedFrom", "DocumentReference", "ImagingStudy", "Media", "QuestionnaireResponse", "Observation", "MolecularSequence");
        }

        protected Builder from(Observation observation) {
            super.from(observation);
            identifier.addAll(observation.identifier);
            basedOn.addAll(observation.basedOn);
            partOf.addAll(observation.partOf);
            status = observation.status;
            category.addAll(observation.category);
            code = observation.code;
            subject = observation.subject;
            focus.addAll(observation.focus);
            encounter = observation.encounter;
            effective = observation.effective;
            issued = observation.issued;
            performer.addAll(observation.performer);
            value = observation.value;
            dataAbsentReason = observation.dataAbsentReason;
            interpretation.addAll(observation.interpretation);
            note.addAll(observation.note);
            bodySite = observation.bodySite;
            method = observation.method;
            specimen = observation.specimen;
            device = observation.device;
            referenceRange.addAll(observation.referenceRange);
            hasMember.addAll(observation.hasMember);
            derivedFrom.addAll(observation.derivedFrom);
            component.addAll(observation.component);
            return this;
        }
    }

    /**
     * Guidance on how to interpret the value by comparison to a normal or recommended range. Multiple reference ranges are 
     * interpreted as an "OR". In other words, to represent two distinct target populations, two `referenceRange` elements 
     * would be used.
     */
    public static class ReferenceRange extends BackboneElement {
        private final SimpleQuantity low;
        private final SimpleQuantity high;
        @Binding(
            bindingName = "ObservationRangeMeaning",
            strength = BindingStrength.Value.PREFERRED,
            description = "Code for the meaning of a reference range.",
            valueSet = "http://hl7.org/fhir/ValueSet/referencerange-meaning"
        )
        private final CodeableConcept type;
        @Binding(
            bindingName = "ObservationRangeType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes identifying the population the reference range applies to.",
            valueSet = "http://hl7.org/fhir/ValueSet/referencerange-appliesto"
        )
        private final List<CodeableConcept> appliesTo;
        private final Range age;
        private final String text;

        private ReferenceRange(Builder builder) {
            super(builder);
            low = builder.low;
            high = builder.high;
            type = builder.type;
            appliesTo = Collections.unmodifiableList(builder.appliesTo);
            age = builder.age;
            text = builder.text;
        }

        /**
         * The value of the low bound of the reference range. The low bound of the reference range endpoint is inclusive of the 
         * value (e.g. reference range is &gt;=5 - &lt;=9). If the low bound is omitted, it is assumed to be meaningless (e.g. 
         * reference range is &lt;=2.3).
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getLow() {
            return low;
        }

        /**
         * The value of the high bound of the reference range. The high bound of the reference range endpoint is inclusive of the 
         * value (e.g. reference range is &gt;=5 - &lt;=9). If the high bound is omitted, it is assumed to be meaningless (e.g. 
         * reference range is &gt;= 2.3).
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getHigh() {
            return high;
        }

        /**
         * Codes to indicate the what part of the targeted reference population it applies to. For example, the normal or 
         * therapeutic range.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Codes to indicate the target population this reference range applies to. For example, a reference range may be based 
         * on the normal population or a particular sex or race. Multiple `appliesTo` are interpreted as an "AND" of the target 
         * populations. For example, to represent a target population of African American females, both a code of female and a 
         * code for African American would be used.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getAppliesTo() {
            return appliesTo;
        }

        /**
         * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the 
         * meaning says so.
         * 
         * @return
         *     An immutable object of type {@link Range} that may be null.
         */
        public Range getAge() {
            return age;
        }

        /**
         * Text based reference range in an observation which may be used when a quantitative range is not appropriate for an 
         * observation. An example would be a reference value of "Negative" or a list or table of "normals".
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getText() {
            return text;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (low != null) || 
                (high != null) || 
                (type != null) || 
                !appliesTo.isEmpty() || 
                (age != null) || 
                (text != null);
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
                    accept(low, "low", visitor);
                    accept(high, "high", visitor);
                    accept(type, "type", visitor);
                    accept(appliesTo, "appliesTo", visitor, CodeableConcept.class);
                    accept(age, "age", visitor);
                    accept(text, "text", visitor);
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
            ReferenceRange other = (ReferenceRange) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(low, other.low) && 
                Objects.equals(high, other.high) && 
                Objects.equals(type, other.type) && 
                Objects.equals(appliesTo, other.appliesTo) && 
                Objects.equals(age, other.age) && 
                Objects.equals(text, other.text);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    low, 
                    high, 
                    type, 
                    appliesTo, 
                    age, 
                    text);
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
            private SimpleQuantity low;
            private SimpleQuantity high;
            private CodeableConcept type;
            private List<CodeableConcept> appliesTo = new ArrayList<>();
            private Range age;
            private String text;

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
             * The value of the low bound of the reference range. The low bound of the reference range endpoint is inclusive of the 
             * value (e.g. reference range is &gt;=5 - &lt;=9). If the low bound is omitted, it is assumed to be meaningless (e.g. 
             * reference range is &lt;=2.3).
             * 
             * @param low
             *     Low Range, if relevant
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder low(SimpleQuantity low) {
                this.low = low;
                return this;
            }

            /**
             * The value of the high bound of the reference range. The high bound of the reference range endpoint is inclusive of the 
             * value (e.g. reference range is &gt;=5 - &lt;=9). If the high bound is omitted, it is assumed to be meaningless (e.g. 
             * reference range is &gt;= 2.3).
             * 
             * @param high
             *     High Range, if relevant
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder high(SimpleQuantity high) {
                this.high = high;
                return this;
            }

            /**
             * Codes to indicate the what part of the targeted reference population it applies to. For example, the normal or 
             * therapeutic range.
             * 
             * @param type
             *     Reference range qualifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Codes to indicate the target population this reference range applies to. For example, a reference range may be based 
             * on the normal population or a particular sex or race. Multiple `appliesTo` are interpreted as an "AND" of the target 
             * populations. For example, to represent a target population of African American females, both a code of female and a 
             * code for African American would be used.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param appliesTo
             *     Reference range population
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder appliesTo(CodeableConcept... appliesTo) {
                for (CodeableConcept value : appliesTo) {
                    this.appliesTo.add(value);
                }
                return this;
            }

            /**
             * Codes to indicate the target population this reference range applies to. For example, a reference range may be based 
             * on the normal population or a particular sex or race. Multiple `appliesTo` are interpreted as an "AND" of the target 
             * populations. For example, to represent a target population of African American females, both a code of female and a 
             * code for African American would be used.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param appliesTo
             *     Reference range population
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder appliesTo(Collection<CodeableConcept> appliesTo) {
                this.appliesTo = new ArrayList<>(appliesTo);
                return this;
            }

            /**
             * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the 
             * meaning says so.
             * 
             * @param age
             *     Applicable age range, if relevant
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder age(Range age) {
                this.age = age;
                return this;
            }

            /**
             * Convenience method for setting {@code text}.
             * 
             * @param text
             *     Text based reference range in an observation
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #text(com.ibm.fhir.model.type.String)
             */
            public Builder text(java.lang.String text) {
                this.text = (text == null) ? null : String.of(text);
                return this;
            }

            /**
             * Text based reference range in an observation which may be used when a quantitative range is not appropriate for an 
             * observation. An example would be a reference value of "Negative" or a list or table of "normals".
             * 
             * @param text
             *     Text based reference range in an observation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * Build the {@link ReferenceRange}
             * 
             * @return
             *     An immutable object of type {@link ReferenceRange}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ReferenceRange per the base specification
             */
            @Override
            public ReferenceRange build() {
                ReferenceRange referenceRange = new ReferenceRange(this);
                if (validating) {
                    validate(referenceRange);
                }
                return referenceRange;
            }

            protected void validate(ReferenceRange referenceRange) {
                super.validate(referenceRange);
                ValidationSupport.checkList(referenceRange.appliesTo, "appliesTo", CodeableConcept.class);
                ValidationSupport.requireValueOrChildren(referenceRange);
            }

            protected Builder from(ReferenceRange referenceRange) {
                super.from(referenceRange);
                low = referenceRange.low;
                high = referenceRange.high;
                type = referenceRange.type;
                appliesTo.addAll(referenceRange.appliesTo);
                age = referenceRange.age;
                text = referenceRange.text;
                return this;
            }
        }
    }

    /**
     * Some observations have multiple component observations. These component observations are expressed as separate code 
     * value pairs that share the same attributes. Examples include systolic and diastolic component observations for blood 
     * pressure measurement and multiple component observations for genetics observations.
     */
    public static class Component extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "ObservationCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes identifying names of simple observations.",
            valueSet = "http://hl7.org/fhir/ValueSet/observation-codes"
        )
        @Required
        private final CodeableConcept code;
        @Summary
        @Choice({ Quantity.class, CodeableConcept.class, String.class, Boolean.class, Integer.class, Range.class, Ratio.class, SampledData.class, Time.class, DateTime.class, Period.class })
        private final Element value;
        @Binding(
            bindingName = "ObservationValueAbsentReason",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Codes specifying why the result (`Observation.value[x]`) is missing.",
            valueSet = "http://hl7.org/fhir/ValueSet/data-absent-reason"
        )
        private final CodeableConcept dataAbsentReason;
        @Binding(
            bindingName = "ObservationInterpretation",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Codes identifying interpretations of observations.",
            valueSet = "http://hl7.org/fhir/ValueSet/observation-interpretation"
        )
        private final List<CodeableConcept> interpretation;
        private final List<Observation.ReferenceRange> referenceRange;

        private Component(Builder builder) {
            super(builder);
            code = builder.code;
            value = builder.value;
            dataAbsentReason = builder.dataAbsentReason;
            interpretation = Collections.unmodifiableList(builder.interpretation);
            referenceRange = Collections.unmodifiableList(builder.referenceRange);
        }

        /**
         * Describes what was observed. Sometimes this is called the observation "code".
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * The information determined as a result of making the observation, if the information has a simple value.
         * 
         * @return
         *     An immutable object of type {@link Quantity}, {@link CodeableConcept}, {@link String}, {@link Boolean}, {@link 
         *     Integer}, {@link Range}, {@link Ratio}, {@link SampledData}, {@link Time}, {@link DateTime} or {@link Period} that may 
         *     be null.
         */
        public Element getValue() {
            return value;
        }

        /**
         * Provides a reason why the expected value in the element Observation.component.value[x] is missing.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getDataAbsentReason() {
            return dataAbsentReason;
        }

        /**
         * A categorical assessment of an observation value. For example, high, low, normal.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getInterpretation() {
            return interpretation;
        }

        /**
         * Guidance on how to interpret the value by comparison to a normal or recommended range.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ReferenceRange} that may be empty.
         */
        public List<Observation.ReferenceRange> getReferenceRange() {
            return referenceRange;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (value != null) || 
                (dataAbsentReason != null) || 
                !interpretation.isEmpty() || 
                !referenceRange.isEmpty();
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
                    accept(dataAbsentReason, "dataAbsentReason", visitor);
                    accept(interpretation, "interpretation", visitor, CodeableConcept.class);
                    accept(referenceRange, "referenceRange", visitor, Observation.ReferenceRange.class);
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
            Component other = (Component) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(value, other.value) && 
                Objects.equals(dataAbsentReason, other.dataAbsentReason) && 
                Objects.equals(interpretation, other.interpretation) && 
                Objects.equals(referenceRange, other.referenceRange);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    value, 
                    dataAbsentReason, 
                    interpretation, 
                    referenceRange);
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
            private Element value;
            private CodeableConcept dataAbsentReason;
            private List<CodeableConcept> interpretation = new ArrayList<>();
            private List<Observation.ReferenceRange> referenceRange = new ArrayList<>();

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
             * Describes what was observed. Sometimes this is called the observation "code".
             * 
             * <p>This element is required.
             * 
             * @param code
             *     Type of component observation (code / type)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type String.
             * 
             * @param value
             *     Actual component result
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.String value) {
                this.value = (value == null) ? null : String.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Boolean.
             * 
             * @param value
             *     Actual component result
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.Boolean value) {
                this.value = (value == null) ? null : Boolean.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Integer.
             * 
             * @param value
             *     Actual component result
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.Integer value) {
                this.value = (value == null) ? null : Integer.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Time.
             * 
             * @param value
             *     Actual component result
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.LocalTime value) {
                this.value = (value == null) ? null : Time.of(value);
                return this;
            }

            /**
             * The information determined as a result of making the observation, if the information has a simple value.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Quantity}</li>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link String}</li>
             * <li>{@link Boolean}</li>
             * <li>{@link Integer}</li>
             * <li>{@link Range}</li>
             * <li>{@link Ratio}</li>
             * <li>{@link SampledData}</li>
             * <li>{@link Time}</li>
             * <li>{@link DateTime}</li>
             * <li>{@link Period}</li>
             * </ul>
             * 
             * @param value
             *     Actual component result
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Provides a reason why the expected value in the element Observation.component.value[x] is missing.
             * 
             * @param dataAbsentReason
             *     Why the component result is missing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dataAbsentReason(CodeableConcept dataAbsentReason) {
                this.dataAbsentReason = dataAbsentReason;
                return this;
            }

            /**
             * A categorical assessment of an observation value. For example, high, low, normal.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param interpretation
             *     High, low, normal, etc.
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder interpretation(CodeableConcept... interpretation) {
                for (CodeableConcept value : interpretation) {
                    this.interpretation.add(value);
                }
                return this;
            }

            /**
             * A categorical assessment of an observation value. For example, high, low, normal.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param interpretation
             *     High, low, normal, etc.
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder interpretation(Collection<CodeableConcept> interpretation) {
                this.interpretation = new ArrayList<>(interpretation);
                return this;
            }

            /**
             * Guidance on how to interpret the value by comparison to a normal or recommended range.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param referenceRange
             *     Provides guide for interpretation of component result
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder referenceRange(Observation.ReferenceRange... referenceRange) {
                for (Observation.ReferenceRange value : referenceRange) {
                    this.referenceRange.add(value);
                }
                return this;
            }

            /**
             * Guidance on how to interpret the value by comparison to a normal or recommended range.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param referenceRange
             *     Provides guide for interpretation of component result
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder referenceRange(Collection<Observation.ReferenceRange> referenceRange) {
                this.referenceRange = new ArrayList<>(referenceRange);
                return this;
            }

            /**
             * Build the {@link Component}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Component}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Component per the base specification
             */
            @Override
            public Component build() {
                Component component = new Component(this);
                if (validating) {
                    validate(component);
                }
                return component;
            }

            protected void validate(Component component) {
                super.validate(component);
                ValidationSupport.requireNonNull(component.code, "code");
                ValidationSupport.choiceElement(component.value, "value", Quantity.class, CodeableConcept.class, String.class, Boolean.class, Integer.class, Range.class, Ratio.class, SampledData.class, Time.class, DateTime.class, Period.class);
                ValidationSupport.checkList(component.interpretation, "interpretation", CodeableConcept.class);
                ValidationSupport.checkList(component.referenceRange, "referenceRange", Observation.ReferenceRange.class);
                ValidationSupport.requireValueOrChildren(component);
            }

            protected Builder from(Component component) {
                super.from(component);
                code = component.code;
                value = component.value;
                dataAbsentReason = component.dataAbsentReason;
                interpretation.addAll(component.interpretation);
                referenceRange.addAll(component.referenceRange);
                return this;
            }
        }
    }
}
