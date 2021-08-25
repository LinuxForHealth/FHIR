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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DeviceMetricCalibrationState;
import com.ibm.fhir.model.type.code.DeviceMetricCalibrationType;
import com.ibm.fhir.model.type.code.DeviceMetricCategory;
import com.ibm.fhir.model.type.code.DeviceMetricColor;
import com.ibm.fhir.model.type.code.DeviceMetricOperationalStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Describes a measurement, calculation or setting capability of a medical device.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "deviceMetric-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/devicemetric-type",
    expression = "type.exists() and type.memberOf('http://hl7.org/fhir/ValueSet/devicemetric-type', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/DeviceMetric",
    generated = true
)
@Constraint(
    id = "deviceMetric-1",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/devicemetric-type",
    expression = "unit.exists() implies (unit.memberOf('http://hl7.org/fhir/ValueSet/devicemetric-type', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/DeviceMetric",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceMetric extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "MetricType",
        strength = BindingStrength.Value.PREFERRED,
        description = "Describes the metric type.",
        valueSet = "http://hl7.org/fhir/ValueSet/devicemetric-type"
    )
    @Required
    private final CodeableConcept type;
    @Summary
    @Binding(
        bindingName = "MetricUnit",
        strength = BindingStrength.Value.PREFERRED,
        description = "Describes the unit of the metric.",
        valueSet = "http://hl7.org/fhir/ValueSet/devicemetric-type"
    )
    private final CodeableConcept unit;
    @Summary
    @ReferenceTarget({ "Device" })
    private final Reference source;
    @Summary
    @ReferenceTarget({ "Device" })
    private final Reference parent;
    @Summary
    @Binding(
        bindingName = "DeviceMetricOperationalStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Describes the operational status of the DeviceMetric.",
        valueSet = "http://hl7.org/fhir/ValueSet/metric-operational-status|4.0.1"
    )
    private final DeviceMetricOperationalStatus operationalStatus;
    @Summary
    @Binding(
        bindingName = "DeviceMetricColor",
        strength = BindingStrength.Value.REQUIRED,
        description = "Describes the typical color of representation.",
        valueSet = "http://hl7.org/fhir/ValueSet/metric-color|4.0.1"
    )
    private final DeviceMetricColor color;
    @Summary
    @Binding(
        bindingName = "DeviceMetricCategory",
        strength = BindingStrength.Value.REQUIRED,
        description = "Describes the category of the metric.",
        valueSet = "http://hl7.org/fhir/ValueSet/metric-category|4.0.1"
    )
    @Required
    private final DeviceMetricCategory category;
    @Summary
    private final Timing measurementPeriod;
    @Summary
    private final List<Calibration> calibration;

    private DeviceMetric(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        type = builder.type;
        unit = builder.unit;
        source = builder.source;
        parent = builder.parent;
        operationalStatus = builder.operationalStatus;
        color = builder.color;
        category = builder.category;
        measurementPeriod = builder.measurementPeriod;
        calibration = Collections.unmodifiableList(builder.calibration);
    }

    /**
     * Unique instance identifiers assigned to a device by the device or gateway software, manufacturers, other organizations 
     * or owners. For example: handle ID.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getUnit() {
        return unit;
    }

    /**
     * Describes the link to the Device that this DeviceMetric belongs to and that contains administrative device information 
     * such as manufacturer, serial number, etc.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSource() {
        return source;
    }

    /**
     * Describes the link to the Device that this DeviceMetric belongs to and that provide information about the location of 
     * this DeviceMetric in the containment structure of the parent Device. An example would be a Device that represents a 
     * Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but 
     * should be interpreted based on their containment location.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getParent() {
        return parent;
    }

    /**
     * Indicates current operational state of the device. For example: On, Off, Standby, etc.
     * 
     * @return
     *     An immutable object of type {@link DeviceMetricOperationalStatus} that may be null.
     */
    public DeviceMetricOperationalStatus getOperationalStatus() {
        return operationalStatus;
    }

    /**
     * Describes the color representation for the metric. This is often used to aid clinicians to track and identify 
     * parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the 
     * parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta.
     * 
     * @return
     *     An immutable object of type {@link DeviceMetricColor} that may be null.
     */
    public DeviceMetricColor getColor() {
        return color;
    }

    /**
     * Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, 
     * measurement, or calculation.
     * 
     * @return
     *     An immutable object of type {@link DeviceMetricCategory} that is non-null.
     */
    public DeviceMetricCategory getCategory() {
        return category;
    }

    /**
     * Describes the measurement repetition time. This is not necessarily the same as the update period. The measurement 
     * repetition time can range from milliseconds up to hours. An example for a measurement repetition time in the range of 
     * milliseconds is the sampling rate of an ECG. An example for a measurement repetition time in the range of hours is a 
     * NIBP that is triggered automatically every hour. The update period may be different than the measurement repetition 
     * time, if the device does not update the published observed value with the same frequency as it was measured.
     * 
     * @return
     *     An immutable object of type {@link Timing} that may be null.
     */
    public Timing getMeasurementPeriod() {
        return measurementPeriod;
    }

    /**
     * Describes the calibrations that have been performed or that are required to be performed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Calibration} that may be empty.
     */
    public List<Calibration> getCalibration() {
        return calibration;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (type != null) || 
            (unit != null) || 
            (source != null) || 
            (parent != null) || 
            (operationalStatus != null) || 
            (color != null) || 
            (category != null) || 
            (measurementPeriod != null) || 
            !calibration.isEmpty();
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
                accept(type, "type", visitor);
                accept(unit, "unit", visitor);
                accept(source, "source", visitor);
                accept(parent, "parent", visitor);
                accept(operationalStatus, "operationalStatus", visitor);
                accept(color, "color", visitor);
                accept(category, "category", visitor);
                accept(measurementPeriod, "measurementPeriod", visitor);
                accept(calibration, "calibration", visitor, Calibration.class);
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
        DeviceMetric other = (DeviceMetric) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(type, other.type) && 
            Objects.equals(unit, other.unit) && 
            Objects.equals(source, other.source) && 
            Objects.equals(parent, other.parent) && 
            Objects.equals(operationalStatus, other.operationalStatus) && 
            Objects.equals(color, other.color) && 
            Objects.equals(category, other.category) && 
            Objects.equals(measurementPeriod, other.measurementPeriod) && 
            Objects.equals(calibration, other.calibration);
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
                type, 
                unit, 
                source, 
                parent, 
                operationalStatus, 
                color, 
                category, 
                measurementPeriod, 
                calibration);
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
        private CodeableConcept type;
        private CodeableConcept unit;
        private Reference source;
        private Reference parent;
        private DeviceMetricOperationalStatus operationalStatus;
        private DeviceMetricColor color;
        private DeviceMetricCategory category;
        private Timing measurementPeriod;
        private List<Calibration> calibration = new ArrayList<>();

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
         * Unique instance identifiers assigned to a device by the device or gateway software, manufacturers, other organizations 
         * or owners. For example: handle ID.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Instance identifier
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
         * Unique instance identifiers assigned to a device by the device or gateway software, manufacturers, other organizations 
         * or owners. For example: handle ID.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Instance identifier
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
         * Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     Identity of metric, for example Heart Rate or PEEP Setting
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc.
         * 
         * @param unit
         *     Unit of Measure for the Metric
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder unit(CodeableConcept unit) {
            this.unit = unit;
            return this;
        }

        /**
         * Describes the link to the Device that this DeviceMetric belongs to and that contains administrative device information 
         * such as manufacturer, serial number, etc.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param source
         *     Describes the link to the source Device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Reference source) {
            this.source = source;
            return this;
        }

        /**
         * Describes the link to the Device that this DeviceMetric belongs to and that provide information about the location of 
         * this DeviceMetric in the containment structure of the parent Device. An example would be a Device that represents a 
         * Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but 
         * should be interpreted based on their containment location.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param parent
         *     Describes the link to the parent Device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parent(Reference parent) {
            this.parent = parent;
            return this;
        }

        /**
         * Indicates current operational state of the device. For example: On, Off, Standby, etc.
         * 
         * @param operationalStatus
         *     on | off | standby | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder operationalStatus(DeviceMetricOperationalStatus operationalStatus) {
            this.operationalStatus = operationalStatus;
            return this;
        }

        /**
         * Describes the color representation for the metric. This is often used to aid clinicians to track and identify 
         * parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the 
         * parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta.
         * 
         * @param color
         *     black | red | green | yellow | blue | magenta | cyan | white
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder color(DeviceMetricColor color) {
            this.color = color;
            return this;
        }

        /**
         * Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, 
         * measurement, or calculation.
         * 
         * <p>This element is required.
         * 
         * @param category
         *     measurement | setting | calculation | unspecified
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(DeviceMetricCategory category) {
            this.category = category;
            return this;
        }

        /**
         * Describes the measurement repetition time. This is not necessarily the same as the update period. The measurement 
         * repetition time can range from milliseconds up to hours. An example for a measurement repetition time in the range of 
         * milliseconds is the sampling rate of an ECG. An example for a measurement repetition time in the range of hours is a 
         * NIBP that is triggered automatically every hour. The update period may be different than the measurement repetition 
         * time, if the device does not update the published observed value with the same frequency as it was measured.
         * 
         * @param measurementPeriod
         *     Describes the measurement repetition time
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder measurementPeriod(Timing measurementPeriod) {
            this.measurementPeriod = measurementPeriod;
            return this;
        }

        /**
         * Describes the calibrations that have been performed or that are required to be performed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param calibration
         *     Describes the calibrations that have been performed or that are required to be performed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder calibration(Calibration... calibration) {
            for (Calibration value : calibration) {
                this.calibration.add(value);
            }
            return this;
        }

        /**
         * Describes the calibrations that have been performed or that are required to be performed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param calibration
         *     Describes the calibrations that have been performed or that are required to be performed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder calibration(Collection<Calibration> calibration) {
            this.calibration = new ArrayList<>(calibration);
            return this;
        }

        /**
         * Build the {@link DeviceMetric}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * <li>category</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link DeviceMetric}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid DeviceMetric per the base specification
         */
        @Override
        public DeviceMetric build() {
            DeviceMetric deviceMetric = new DeviceMetric(this);
            if (validating) {
                validate(deviceMetric);
            }
            return deviceMetric;
        }

        protected void validate(DeviceMetric deviceMetric) {
            super.validate(deviceMetric);
            ValidationSupport.checkList(deviceMetric.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(deviceMetric.type, "type");
            ValidationSupport.requireNonNull(deviceMetric.category, "category");
            ValidationSupport.checkList(deviceMetric.calibration, "calibration", Calibration.class);
            ValidationSupport.checkReferenceType(deviceMetric.source, "source", "Device");
            ValidationSupport.checkReferenceType(deviceMetric.parent, "parent", "Device");
        }

        protected Builder from(DeviceMetric deviceMetric) {
            super.from(deviceMetric);
            identifier.addAll(deviceMetric.identifier);
            type = deviceMetric.type;
            unit = deviceMetric.unit;
            source = deviceMetric.source;
            parent = deviceMetric.parent;
            operationalStatus = deviceMetric.operationalStatus;
            color = deviceMetric.color;
            category = deviceMetric.category;
            measurementPeriod = deviceMetric.measurementPeriod;
            calibration.addAll(deviceMetric.calibration);
            return this;
        }
    }

    /**
     * Describes the calibrations that have been performed or that are required to be performed.
     */
    public static class Calibration extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "DeviceMetricCalibrationType",
            strength = BindingStrength.Value.REQUIRED,
            description = "Describes the type of a metric calibration.",
            valueSet = "http://hl7.org/fhir/ValueSet/metric-calibration-type|4.0.1"
        )
        private final DeviceMetricCalibrationType type;
        @Summary
        @Binding(
            bindingName = "DeviceMetricCalibrationState",
            strength = BindingStrength.Value.REQUIRED,
            description = "Describes the state of a metric calibration.",
            valueSet = "http://hl7.org/fhir/ValueSet/metric-calibration-state|4.0.1"
        )
        private final DeviceMetricCalibrationState state;
        @Summary
        private final Instant time;

        private Calibration(Builder builder) {
            super(builder);
            type = builder.type;
            state = builder.state;
            time = builder.time;
        }

        /**
         * Describes the type of the calibration method.
         * 
         * @return
         *     An immutable object of type {@link DeviceMetricCalibrationType} that may be null.
         */
        public DeviceMetricCalibrationType getType() {
            return type;
        }

        /**
         * Describes the state of the calibration.
         * 
         * @return
         *     An immutable object of type {@link DeviceMetricCalibrationState} that may be null.
         */
        public DeviceMetricCalibrationState getState() {
            return state;
        }

        /**
         * Describes the time last calibration has been performed.
         * 
         * @return
         *     An immutable object of type {@link Instant} that may be null.
         */
        public Instant getTime() {
            return time;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (state != null) || 
                (time != null);
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
                    accept(type, "type", visitor);
                    accept(state, "state", visitor);
                    accept(time, "time", visitor);
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
            Calibration other = (Calibration) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(state, other.state) && 
                Objects.equals(time, other.time);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    state, 
                    time);
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
            private DeviceMetricCalibrationType type;
            private DeviceMetricCalibrationState state;
            private Instant time;

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
             * Describes the type of the calibration method.
             * 
             * @param type
             *     unspecified | offset | gain | two-point
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(DeviceMetricCalibrationType type) {
                this.type = type;
                return this;
            }

            /**
             * Describes the state of the calibration.
             * 
             * @param state
             *     not-calibrated | calibration-required | calibrated | unspecified
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder state(DeviceMetricCalibrationState state) {
                this.state = state;
                return this;
            }

            /**
             * Convenience method for setting {@code time}.
             * 
             * @param time
             *     Describes the time last calibration has been performed
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #time(com.ibm.fhir.model.type.Instant)
             */
            public Builder time(java.time.ZonedDateTime time) {
                this.time = (time == null) ? null : Instant.of(time);
                return this;
            }

            /**
             * Describes the time last calibration has been performed.
             * 
             * @param time
             *     Describes the time last calibration has been performed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder time(Instant time) {
                this.time = time;
                return this;
            }

            /**
             * Build the {@link Calibration}
             * 
             * @return
             *     An immutable object of type {@link Calibration}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Calibration per the base specification
             */
            @Override
            public Calibration build() {
                Calibration calibration = new Calibration(this);
                if (validating) {
                    validate(calibration);
                }
                return calibration;
            }

            protected void validate(Calibration calibration) {
                super.validate(calibration);
                ValidationSupport.requireValueOrChildren(calibration);
            }

            protected Builder from(Calibration calibration) {
                super.from(calibration);
                type = calibration.type;
                state = calibration.state;
                time = calibration.time;
                return this;
            }
        }
    }
}
