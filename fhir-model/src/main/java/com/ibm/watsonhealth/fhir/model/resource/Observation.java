/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.ObservationStatus;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.SampledData;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Measurements and simple assertions made about a patient, device or other subject.
 * </p>
 */
@Constraint(
    id = "obs-3",
    level = "Rule",
    location = "Observation.referenceRange",
    description = "Must have at least a low or a high or text",
    expression = "low.exists() or high.exists() or text.exists()"
)
@Constraint(
    id = "obs-6",
    level = "Rule",
    location = "(base)",
    description = "dataAbsentReason SHALL only be present if Observation.value[x] is not present",
    expression = "dataAbsentReason.empty() or value.empty()"
)
@Constraint(
    id = "obs-7",
    level = "Rule",
    location = "(base)",
    description = "If Observation.code is the same as an Observation.component.code then the value element associated with the code SHALL NOT be present",
    expression = "value.empty() or component.code.where( (coding.code = %resource.code.coding.code) and (coding.system = %resource.code.coding.system)).empty()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Observation extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Reference> basedOn;
    private final List<Reference> partOf;
    private final ObservationStatus status;
    private final List<CodeableConcept> category;
    private final CodeableConcept code;
    private final Reference subject;
    private final List<Reference> focus;
    private final Reference encounter;
    private final Element effective;
    private final Instant issued;
    private final List<Reference> performer;
    private final Element value;
    private final CodeableConcept dataAbsentReason;
    private final List<CodeableConcept> interpretation;
    private final List<Annotation> note;
    private final CodeableConcept bodySite;
    private final CodeableConcept method;
    private final Reference specimen;
    private final Reference device;
    private final List<ReferenceRange> referenceRange;
    private final List<Reference> hasMember;
    private final List<Reference> derivedFrom;
    private final List<Component> component;

    private Observation(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.basedOn = builder.basedOn;
        this.partOf = builder.partOf;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.category = builder.category;
        this.code = ValidationSupport.requireNonNull(builder.code, "code");
        this.subject = builder.subject;
        this.focus = builder.focus;
        this.encounter = builder.encounter;
        this.effective = ValidationSupport.choiceElement(builder.effective, "effective", DateTime.class, Period.class, Timing.class, Instant.class);
        this.issued = builder.issued;
        this.performer = builder.performer;
        this.value = ValidationSupport.choiceElement(builder.value, "value", Quantity.class, CodeableConcept.class, String.class, Boolean.class, Integer.class, Range.class, Ratio.class, SampledData.class, Time.class, DateTime.class, Period.class);
        this.dataAbsentReason = builder.dataAbsentReason;
        this.interpretation = builder.interpretation;
        this.note = builder.note;
        this.bodySite = builder.bodySite;
        this.method = builder.method;
        this.specimen = builder.specimen;
        this.device = builder.device;
        this.referenceRange = builder.referenceRange;
        this.hasMember = builder.hasMember;
        this.derivedFrom = builder.derivedFrom;
        this.component = builder.component;
    }

    /**
     * <p>
     * A unique identifier assigned to this observation.
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
     * A plan, proposal or order that is fulfilled in whole or in part by this event. For example, a MedicationRequest may 
     * require a patient to have laboratory test performed before it is dispensed.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * <p>
     * A larger event of which this particular Observation is a component or step. For example, an observation as part of a 
     * procedure.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * <p>
     * The status of the result value.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ObservationStatus}.
     */
    public ObservationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * A code that classifies the general type of observation being made.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * Describes what was observed. Sometimes this is called the observation "name".
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
     * The patient, or group of patients, location, or device this observation is about and into whose record the observation 
     * is placed. If the actual focus of the observation is different from the subject (or a sample of, part, or region of 
     * the subject), the `focus` element or the `code` itself specifies the actual focus of the observation.
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
     * The actual focus of an observation when it is not the patient of record representing something or someone associated 
     * with the patient such as a spouse, parent, fetus, or donor. For example, fetus observations in a mother's record. The 
     * focus of an observation could also be an existing condition, an intervention, the subject's diet, another observation 
     * of the subject, or a body structure such as tumor or implanted device. An example use case would be using the 
     * Observation resource to capture whether the mother is trained to change her child's tracheostomy tube. In this 
     * example, the child is the patient of record and the mother is the focus.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getFocus() {
        return focus;
    }

    /**
     * <p>
     * The healthcare event (e.g. a patient and healthcare provider interaction) during which this observation is made.
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
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - 
     * this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of 
     * specimen collection, but very often the source of the date/time is not known, only the date/time itself.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getEffective() {
        return effective;
    }

    /**
     * <p>
     * The date and time this version of the observation was made available to providers, typically after the results have 
     * been reviewed and verified.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Instant}.
     */
    public Instant getIssued() {
        return issued;
    }

    /**
     * <p>
     * Who was responsible for asserting the observed value as "true".
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPerformer() {
        return performer;
    }

    /**
     * <p>
     * The information determined as a result of making the observation, if the information has a simple value.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getValue() {
        return value;
    }

    /**
     * <p>
     * Provides a reason why the expected value in the element Observation.value[x] is missing.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getDataAbsentReason() {
        return dataAbsentReason;
    }

    /**
     * <p>
     * A categorical assessment of an observation value. For example, high, low, normal.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getInterpretation() {
        return interpretation;
    }

    /**
     * <p>
     * Comments about the observation or the results.
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
     * Indicates the site on the subject's body where the observation was made (i.e. the target site).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getBodySite() {
        return bodySite;
    }

    /**
     * <p>
     * Indicates the mechanism used to perform the observation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getMethod() {
        return method;
    }

    /**
     * <p>
     * The specimen that was used when this observation was made.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSpecimen() {
        return specimen;
    }

    /**
     * <p>
     * The device used to generate the observation data.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getDevice() {
        return device;
    }

    /**
     * <p>
     * Guidance on how to interpret the value by comparison to a normal or recommended range. Multiple reference ranges are 
     * interpreted as an "OR". In other words, to represent two distinct target populations, two `referenceRange` elements 
     * would be used.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ReferenceRange}.
     */
    public List<ReferenceRange> getReferenceRange() {
        return referenceRange;
    }

    /**
     * <p>
     * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that 
     * includes the target as a member of the group.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getHasMember() {
        return hasMember;
    }

    /**
     * <p>
     * The target resource that represents a measurement from which this observation value is derived. For example, a 
     * calculated anion gap or a fetal measurement based on an ultrasound image.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * <p>
     * Some observations have multiple component observations. These component observations are expressed as separate code 
     * value pairs that share the same attributes. Examples include systolic and diastolic component observations for blood 
     * pressure measurement and multiple component observations for genetics observations.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Component}.
     */
    public List<Component> getComponent() {
        return component;
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
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(code, "code", visitor);
                accept(subject, "subject", visitor);
                accept(focus, "focus", visitor, Reference.class);
                accept(encounter, "encounter", visitor);
                accept(effective, "effective", visitor, true);
                accept(issued, "issued", visitor);
                accept(performer, "performer", visitor, Reference.class);
                accept(value, "value", visitor, true);
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, code);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.basedOn.addAll(basedOn);
        builder.partOf.addAll(partOf);
        builder.category.addAll(category);
        builder.subject = subject;
        builder.focus.addAll(focus);
        builder.encounter = encounter;
        builder.effective = effective;
        builder.issued = issued;
        builder.performer.addAll(performer);
        builder.value = value;
        builder.dataAbsentReason = dataAbsentReason;
        builder.interpretation.addAll(interpretation);
        builder.note.addAll(note);
        builder.bodySite = bodySite;
        builder.method = method;
        builder.specimen = specimen;
        builder.device = device;
        builder.referenceRange.addAll(referenceRange);
        builder.hasMember.addAll(hasMember);
        builder.derivedFrom.addAll(derivedFrom);
        builder.component.addAll(component);
        return builder;
    }

    public static Builder builder(ObservationStatus status, CodeableConcept code) {
        return new Builder(status, code);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ObservationStatus status;
        private final CodeableConcept code;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private List<CodeableConcept> category = new ArrayList<>();
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

        private Builder(ObservationStatus status, CodeableConcept code) {
            super();
            this.status = status;
            this.code = code;
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
         * A unique identifier assigned to this observation.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for observation
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
         * A unique identifier assigned to this observation.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for observation
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
         * A plan, proposal or order that is fulfilled in whole or in part by this event. For example, a MedicationRequest may 
         * require a patient to have laboratory test performed before it is dispensed.
         * </p>
         * 
         * @param basedOn
         *     Fulfills plan, proposal or order
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A plan, proposal or order that is fulfilled in whole or in part by this event. For example, a MedicationRequest may 
         * require a patient to have laboratory test performed before it is dispensed.
         * </p>
         * 
         * @param basedOn
         *     Fulfills plan, proposal or order
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn.addAll(basedOn);
            return this;
        }

        /**
         * <p>
         * A larger event of which this particular Observation is a component or step. For example, an observation as part of a 
         * procedure.
         * </p>
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A larger event of which this particular Observation is a component or step. For example, an observation as part of a 
         * procedure.
         * </p>
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf.addAll(partOf);
            return this;
        }

        /**
         * <p>
         * A code that classifies the general type of observation being made.
         * </p>
         * 
         * @param category
         *     Classification of type of observation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A code that classifies the general type of observation being made.
         * </p>
         * 
         * @param category
         *     Classification of type of observation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category.addAll(category);
            return this;
        }

        /**
         * <p>
         * The patient, or group of patients, location, or device this observation is about and into whose record the observation 
         * is placed. If the actual focus of the observation is different from the subject (or a sample of, part, or region of 
         * the subject), the `focus` element or the `code` itself specifies the actual focus of the observation.
         * </p>
         * 
         * @param subject
         *     Who and/or what the observation is about
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
         * The actual focus of an observation when it is not the patient of record representing something or someone associated 
         * with the patient such as a spouse, parent, fetus, or donor. For example, fetus observations in a mother's record. The 
         * focus of an observation could also be an existing condition, an intervention, the subject's diet, another observation 
         * of the subject, or a body structure such as tumor or implanted device. An example use case would be using the 
         * Observation resource to capture whether the mother is trained to change her child's tracheostomy tube. In this 
         * example, the child is the patient of record and the mother is the focus.
         * </p>
         * 
         * @param focus
         *     What the observation is about, when it is not about the subject of record
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder focus(Reference... focus) {
            for (Reference value : focus) {
                this.focus.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The actual focus of an observation when it is not the patient of record representing something or someone associated 
         * with the patient such as a spouse, parent, fetus, or donor. For example, fetus observations in a mother's record. The 
         * focus of an observation could also be an existing condition, an intervention, the subject's diet, another observation 
         * of the subject, or a body structure such as tumor or implanted device. An example use case would be using the 
         * Observation resource to capture whether the mother is trained to change her child's tracheostomy tube. In this 
         * example, the child is the patient of record and the mother is the focus.
         * </p>
         * 
         * @param focus
         *     What the observation is about, when it is not about the subject of record
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder focus(Collection<Reference> focus) {
            this.focus.addAll(focus);
            return this;
        }

        /**
         * <p>
         * The healthcare event (e.g. a patient and healthcare provider interaction) during which this observation is made.
         * </p>
         * 
         * @param encounter
         *     Healthcare event during which this observation is made
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
         * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - 
         * this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of 
         * specimen collection, but very often the source of the date/time is not known, only the date/time itself.
         * </p>
         * 
         * @param effective
         *     Clinically relevant time/time-period for observation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder effective(Element effective) {
            this.effective = effective;
            return this;
        }

        /**
         * <p>
         * The date and time this version of the observation was made available to providers, typically after the results have 
         * been reviewed and verified.
         * </p>
         * 
         * @param issued
         *     Date/Time this version was made available
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder issued(Instant issued) {
            this.issued = issued;
            return this;
        }

        /**
         * <p>
         * Who was responsible for asserting the observed value as "true".
         * </p>
         * 
         * @param performer
         *     Who is responsible for the observation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performer(Reference... performer) {
            for (Reference value : performer) {
                this.performer.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Who was responsible for asserting the observed value as "true".
         * </p>
         * 
         * @param performer
         *     Who is responsible for the observation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performer(Collection<Reference> performer) {
            this.performer.addAll(performer);
            return this;
        }

        /**
         * <p>
         * The information determined as a result of making the observation, if the information has a simple value.
         * </p>
         * 
         * @param value
         *     Actual result
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder value(Element value) {
            this.value = value;
            return this;
        }

        /**
         * <p>
         * Provides a reason why the expected value in the element Observation.value[x] is missing.
         * </p>
         * 
         * @param dataAbsentReason
         *     Why the result is missing
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dataAbsentReason(CodeableConcept dataAbsentReason) {
            this.dataAbsentReason = dataAbsentReason;
            return this;
        }

        /**
         * <p>
         * A categorical assessment of an observation value. For example, high, low, normal.
         * </p>
         * 
         * @param interpretation
         *     High, low, normal, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder interpretation(CodeableConcept... interpretation) {
            for (CodeableConcept value : interpretation) {
                this.interpretation.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A categorical assessment of an observation value. For example, high, low, normal.
         * </p>
         * 
         * @param interpretation
         *     High, low, normal, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder interpretation(Collection<CodeableConcept> interpretation) {
            this.interpretation.addAll(interpretation);
            return this;
        }

        /**
         * <p>
         * Comments about the observation or the results.
         * </p>
         * 
         * @param note
         *     Comments about the observation
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
         * Comments about the observation or the results.
         * </p>
         * 
         * @param note
         *     Comments about the observation
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
         * Indicates the site on the subject's body where the observation was made (i.e. the target site).
         * </p>
         * 
         * @param bodySite
         *     Observed body part
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder bodySite(CodeableConcept bodySite) {
            this.bodySite = bodySite;
            return this;
        }

        /**
         * <p>
         * Indicates the mechanism used to perform the observation.
         * </p>
         * 
         * @param method
         *     How it was done
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder method(CodeableConcept method) {
            this.method = method;
            return this;
        }

        /**
         * <p>
         * The specimen that was used when this observation was made.
         * </p>
         * 
         * @param specimen
         *     Specimen used for this observation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder specimen(Reference specimen) {
            this.specimen = specimen;
            return this;
        }

        /**
         * <p>
         * The device used to generate the observation data.
         * </p>
         * 
         * @param device
         *     (Measurement) Device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder device(Reference device) {
            this.device = device;
            return this;
        }

        /**
         * <p>
         * Guidance on how to interpret the value by comparison to a normal or recommended range. Multiple reference ranges are 
         * interpreted as an "OR". In other words, to represent two distinct target populations, two `referenceRange` elements 
         * would be used.
         * </p>
         * 
         * @param referenceRange
         *     Provides guide for interpretation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder referenceRange(ReferenceRange... referenceRange) {
            for (ReferenceRange value : referenceRange) {
                this.referenceRange.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Guidance on how to interpret the value by comparison to a normal or recommended range. Multiple reference ranges are 
         * interpreted as an "OR". In other words, to represent two distinct target populations, two `referenceRange` elements 
         * would be used.
         * </p>
         * 
         * @param referenceRange
         *     Provides guide for interpretation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder referenceRange(Collection<ReferenceRange> referenceRange) {
            this.referenceRange.addAll(referenceRange);
            return this;
        }

        /**
         * <p>
         * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that 
         * includes the target as a member of the group.
         * </p>
         * 
         * @param hasMember
         *     Related resource that belongs to the Observation group
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder hasMember(Reference... hasMember) {
            for (Reference value : hasMember) {
                this.hasMember.add(value);
            }
            return this;
        }

        /**
         * <p>
         * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that 
         * includes the target as a member of the group.
         * </p>
         * 
         * @param hasMember
         *     Related resource that belongs to the Observation group
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder hasMember(Collection<Reference> hasMember) {
            this.hasMember.addAll(hasMember);
            return this;
        }

        /**
         * <p>
         * The target resource that represents a measurement from which this observation value is derived. For example, a 
         * calculated anion gap or a fetal measurement based on an ultrasound image.
         * </p>
         * 
         * @param derivedFrom
         *     Related measurements the observation is made from
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder derivedFrom(Reference... derivedFrom) {
            for (Reference value : derivedFrom) {
                this.derivedFrom.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The target resource that represents a measurement from which this observation value is derived. For example, a 
         * calculated anion gap or a fetal measurement based on an ultrasound image.
         * </p>
         * 
         * @param derivedFrom
         *     Related measurements the observation is made from
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder derivedFrom(Collection<Reference> derivedFrom) {
            this.derivedFrom.addAll(derivedFrom);
            return this;
        }

        /**
         * <p>
         * Some observations have multiple component observations. These component observations are expressed as separate code 
         * value pairs that share the same attributes. Examples include systolic and diastolic component observations for blood 
         * pressure measurement and multiple component observations for genetics observations.
         * </p>
         * 
         * @param component
         *     Component results
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder component(Component... component) {
            for (Component value : component) {
                this.component.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Some observations have multiple component observations. These component observations are expressed as separate code 
         * value pairs that share the same attributes. Examples include systolic and diastolic component observations for blood 
         * pressure measurement and multiple component observations for genetics observations.
         * </p>
         * 
         * @param component
         *     Component results
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder component(Collection<Component> component) {
            this.component.addAll(component);
            return this;
        }

        @Override
        public Observation build() {
            return new Observation(this);
        }
    }

    /**
     * <p>
     * Guidance on how to interpret the value by comparison to a normal or recommended range. Multiple reference ranges are 
     * interpreted as an "OR". In other words, to represent two distinct target populations, two `referenceRange` elements 
     * would be used.
     * </p>
     */
    public static class ReferenceRange extends BackboneElement {
        private final Quantity low;
        private final Quantity high;
        private final CodeableConcept type;
        private final List<CodeableConcept> appliesTo;
        private final Range age;
        private final String text;

        private ReferenceRange(Builder builder) {
            super(builder);
            this.low = builder.low;
            this.high = builder.high;
            this.type = builder.type;
            this.appliesTo = builder.appliesTo;
            this.age = builder.age;
            this.text = builder.text;
        }

        /**
         * <p>
         * The value of the low bound of the reference range. The low bound of the reference range endpoint is inclusive of the 
         * value (e.g. reference range is &gt;=5 - &lt;=9). If the low bound is omitted, it is assumed to be meaningless (e.g. 
         * reference range is &lt;=2.3).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getLow() {
            return low;
        }

        /**
         * <p>
         * The value of the high bound of the reference range. The high bound of the reference range endpoint is inclusive of the 
         * value (e.g. reference range is &gt;=5 - &lt;=9). If the high bound is omitted, it is assumed to be meaningless (e.g. 
         * reference range is &gt;= 2.3).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getHigh() {
            return high;
        }

        /**
         * <p>
         * Codes to indicate the what part of the targeted reference population it applies to. For example, the normal or 
         * therapeutic range.
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
         * Codes to indicate the target population this reference range applies to. For example, a reference range may be based 
         * on the normal population or a particular sex or race. Multiple `appliesTo` are interpreted as an "AND" of the target 
         * populations. For example, to represent a target population of African American females, both a code of female and a 
         * code for African American would be used.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getAppliesTo() {
            return appliesTo;
        }

        /**
         * <p>
         * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the 
         * meaning says so.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Range}.
         */
        public Range getAge() {
            return age;
        }

        /**
         * <p>
         * Text based reference range in an observation which may be used when a quantitative range is not appropriate for an 
         * observation. An example would be a reference value of "Negative" or a list or table of "normals".
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getText() {
            return text;
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
                    accept(low, "low", visitor);
                    accept(high, "high", visitor);
                    accept(type, "type", visitor);
                    accept(appliesTo, "appliesTo", visitor, CodeableConcept.class);
                    accept(age, "age", visitor);
                    accept(text, "text", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private Quantity low;
            private Quantity high;
            private CodeableConcept type;
            private List<CodeableConcept> appliesTo = new ArrayList<>();
            private Range age;
            private String text;

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
             *     A reference to this Builder instance.
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * The value of the low bound of the reference range. The low bound of the reference range endpoint is inclusive of the 
             * value (e.g. reference range is &gt;=5 - &lt;=9). If the low bound is omitted, it is assumed to be meaningless (e.g. 
             * reference range is &lt;=2.3).
             * </p>
             * 
             * @param low
             *     Low Range, if relevant
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder low(Quantity low) {
                this.low = low;
                return this;
            }

            /**
             * <p>
             * The value of the high bound of the reference range. The high bound of the reference range endpoint is inclusive of the 
             * value (e.g. reference range is &gt;=5 - &lt;=9). If the high bound is omitted, it is assumed to be meaningless (e.g. 
             * reference range is &gt;= 2.3).
             * </p>
             * 
             * @param high
             *     High Range, if relevant
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder high(Quantity high) {
                this.high = high;
                return this;
            }

            /**
             * <p>
             * Codes to indicate the what part of the targeted reference population it applies to. For example, the normal or 
             * therapeutic range.
             * </p>
             * 
             * @param type
             *     Reference range qualifier
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * <p>
             * Codes to indicate the target population this reference range applies to. For example, a reference range may be based 
             * on the normal population or a particular sex or race. Multiple `appliesTo` are interpreted as an "AND" of the target 
             * populations. For example, to represent a target population of African American females, both a code of female and a 
             * code for African American would be used.
             * </p>
             * 
             * @param appliesTo
             *     Reference range population
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder appliesTo(CodeableConcept... appliesTo) {
                for (CodeableConcept value : appliesTo) {
                    this.appliesTo.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Codes to indicate the target population this reference range applies to. For example, a reference range may be based 
             * on the normal population or a particular sex or race. Multiple `appliesTo` are interpreted as an "AND" of the target 
             * populations. For example, to represent a target population of African American females, both a code of female and a 
             * code for African American would be used.
             * </p>
             * 
             * @param appliesTo
             *     Reference range population
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder appliesTo(Collection<CodeableConcept> appliesTo) {
                this.appliesTo.addAll(appliesTo);
                return this;
            }

            /**
             * <p>
             * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the 
             * meaning says so.
             * </p>
             * 
             * @param age
             *     Applicable age range, if relevant
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder age(Range age) {
                this.age = age;
                return this;
            }

            /**
             * <p>
             * Text based reference range in an observation which may be used when a quantitative range is not appropriate for an 
             * observation. An example would be a reference value of "Negative" or a list or table of "normals".
             * </p>
             * 
             * @param text
             *     Text based reference range in an observation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            @Override
            public ReferenceRange build() {
                return new ReferenceRange(this);
            }

            private static Builder from(ReferenceRange referenceRange) {
                Builder builder = new Builder();
                builder.id = referenceRange.id;
                builder.extension.addAll(referenceRange.extension);
                builder.modifierExtension.addAll(referenceRange.modifierExtension);
                builder.low = referenceRange.low;
                builder.high = referenceRange.high;
                builder.type = referenceRange.type;
                builder.appliesTo.addAll(referenceRange.appliesTo);
                builder.age = referenceRange.age;
                builder.text = referenceRange.text;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Some observations have multiple component observations. These component observations are expressed as separate code 
     * value pairs that share the same attributes. Examples include systolic and diastolic component observations for blood 
     * pressure measurement and multiple component observations for genetics observations.
     * </p>
     */
    public static class Component extends BackboneElement {
        private final CodeableConcept code;
        private final Element value;
        private final CodeableConcept dataAbsentReason;
        private final List<CodeableConcept> interpretation;
        private final List<Observation.ReferenceRange> referenceRange;

        private Component(Builder builder) {
            super(builder);
            this.code = ValidationSupport.requireNonNull(builder.code, "code");
            this.value = ValidationSupport.choiceElement(builder.value, "value", Quantity.class, CodeableConcept.class, String.class, Boolean.class, Integer.class, Range.class, Ratio.class, SampledData.class, Time.class, DateTime.class, Period.class);
            this.dataAbsentReason = builder.dataAbsentReason;
            this.interpretation = builder.interpretation;
            this.referenceRange = builder.referenceRange;
        }

        /**
         * <p>
         * Describes what was observed. Sometimes this is called the observation "code".
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
         * The information determined as a result of making the observation, if the information has a simple value.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getValue() {
            return value;
        }

        /**
         * <p>
         * Provides a reason why the expected value in the element Observation.component.value[x] is missing.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getDataAbsentReason() {
            return dataAbsentReason;
        }

        /**
         * <p>
         * A categorical assessment of an observation value. For example, high, low, normal.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getInterpretation() {
            return interpretation;
        }

        /**
         * <p>
         * Guidance on how to interpret the value by comparison to a normal or recommended range.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link ReferenceRange}.
         */
        public List<Observation.ReferenceRange> getReferenceRange() {
            return referenceRange;
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
                    accept(value, "value", visitor, true);
                    accept(dataAbsentReason, "dataAbsentReason", visitor);
                    accept(interpretation, "interpretation", visitor, CodeableConcept.class);
                    accept(referenceRange, "referenceRange", visitor, Observation.ReferenceRange.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept code) {
            return new Builder(code);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept code;

            // optional
            private Element value;
            private CodeableConcept dataAbsentReason;
            private List<CodeableConcept> interpretation = new ArrayList<>();
            private List<Observation.ReferenceRange> referenceRange = new ArrayList<>();

            private Builder(CodeableConcept code) {
                super();
                this.code = code;
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
             *     A reference to this Builder instance.
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * The information determined as a result of making the observation, if the information has a simple value.
             * </p>
             * 
             * @param value
             *     Actual component result
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * <p>
             * Provides a reason why the expected value in the element Observation.component.value[x] is missing.
             * </p>
             * 
             * @param dataAbsentReason
             *     Why the component result is missing
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder dataAbsentReason(CodeableConcept dataAbsentReason) {
                this.dataAbsentReason = dataAbsentReason;
                return this;
            }

            /**
             * <p>
             * A categorical assessment of an observation value. For example, high, low, normal.
             * </p>
             * 
             * @param interpretation
             *     High, low, normal, etc.
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder interpretation(CodeableConcept... interpretation) {
                for (CodeableConcept value : interpretation) {
                    this.interpretation.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A categorical assessment of an observation value. For example, high, low, normal.
             * </p>
             * 
             * @param interpretation
             *     High, low, normal, etc.
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder interpretation(Collection<CodeableConcept> interpretation) {
                this.interpretation.addAll(interpretation);
                return this;
            }

            /**
             * <p>
             * Guidance on how to interpret the value by comparison to a normal or recommended range.
             * </p>
             * 
             * @param referenceRange
             *     Provides guide for interpretation of component result
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder referenceRange(Observation.ReferenceRange... referenceRange) {
                for (Observation.ReferenceRange value : referenceRange) {
                    this.referenceRange.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Guidance on how to interpret the value by comparison to a normal or recommended range.
             * </p>
             * 
             * @param referenceRange
             *     Provides guide for interpretation of component result
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder referenceRange(Collection<Observation.ReferenceRange> referenceRange) {
                this.referenceRange.addAll(referenceRange);
                return this;
            }

            @Override
            public Component build() {
                return new Component(this);
            }

            private static Builder from(Component component) {
                Builder builder = new Builder(component.code);
                builder.id = component.id;
                builder.extension.addAll(component.extension);
                builder.modifierExtension.addAll(component.modifierExtension);
                builder.value = component.value;
                builder.dataAbsentReason = component.dataAbsentReason;
                builder.interpretation.addAll(component.interpretation);
                builder.referenceRange.addAll(component.referenceRange);
                return builder;
            }
        }
    }
}
