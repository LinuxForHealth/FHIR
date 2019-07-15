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

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DeviceMetricCalibrationState;
import com.ibm.watsonhealth.fhir.model.type.DeviceMetricCalibrationType;
import com.ibm.watsonhealth.fhir.model.type.DeviceMetricCategory;
import com.ibm.watsonhealth.fhir.model.type.DeviceMetricColor;
import com.ibm.watsonhealth.fhir.model.type.DeviceMetricOperationalStatus;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Describes a measurement, calculation or setting capability of a medical device.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class DeviceMetric extends DomainResource {
    private final List<Identifier> identifier;
    private final CodeableConcept type;
    private final CodeableConcept unit;
    private final Reference source;
    private final Reference parent;
    private final DeviceMetricOperationalStatus operationalStatus;
    private final DeviceMetricColor color;
    private final DeviceMetricCategory category;
    private final Timing measurementPeriod;
    private final List<Calibration> calibration;

    private volatile int hashCode;

    private DeviceMetric(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        type = ValidationSupport.requireNonNull(builder.type, "type");
        unit = builder.unit;
        source = builder.source;
        parent = builder.parent;
        operationalStatus = builder.operationalStatus;
        color = builder.color;
        category = ValidationSupport.requireNonNull(builder.category, "category");
        measurementPeriod = builder.measurementPeriod;
        calibration = Collections.unmodifiableList(builder.calibration);
    }

    /**
     * <p>
     * Unique instance identifiers assigned to a device by the device or gateway software, manufacturers, other organizations 
     * or owners. For example: handle ID.
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
     * Describes the type of the metric. For example: Heart Rate, PEEP Setting, etc.
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
     * Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getUnit() {
        return unit;
    }

    /**
     * <p>
     * Describes the link to the Device that this DeviceMetric belongs to and that contains administrative device information 
     * such as manufacturer, serial number, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSource() {
        return source;
    }

    /**
     * <p>
     * Describes the link to the Device that this DeviceMetric belongs to and that provide information about the location of 
     * this DeviceMetric in the containment structure of the parent Device. An example would be a Device that represents a 
     * Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but 
     * should be interpreted based on their containment location.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getParent() {
        return parent;
    }

    /**
     * <p>
     * Indicates current operational state of the device. For example: On, Off, Standby, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DeviceMetricOperationalStatus}.
     */
    public DeviceMetricOperationalStatus getOperationalStatus() {
        return operationalStatus;
    }

    /**
     * <p>
     * Describes the color representation for the metric. This is often used to aid clinicians to track and identify 
     * parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the 
     * parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DeviceMetricColor}.
     */
    public DeviceMetricColor getColor() {
        return color;
    }

    /**
     * <p>
     * Indicates the category of the observation generation process. A DeviceMetric can be for example a setting, 
     * measurement, or calculation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DeviceMetricCategory}.
     */
    public DeviceMetricCategory getCategory() {
        return category;
    }

    /**
     * <p>
     * Describes the measurement repetition time. This is not necessarily the same as the update period. The measurement 
     * repetition time can range from milliseconds up to hours. An example for a measurement repetition time in the range of 
     * milliseconds is the sampling rate of an ECG. An example for a measurement repetition time in the range of hours is a 
     * NIBP that is triggered automatically every hour. The update period may be different than the measurement repetition 
     * time, if the device does not update the published observed value with the same frequency as it was measured.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Timing}.
     */
    public Timing getMeasurementPeriod() {
        return measurementPeriod;
    }

    /**
     * <p>
     * Describes the calibrations that have been performed or that are required to be performed.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Calibration}.
     */
    public List<Calibration> getCalibration() {
        return calibration;
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
        return new Builder(type, category).from(this);
    }

    public Builder toBuilder(CodeableConcept type, DeviceMetricCategory category) {
        return new Builder(type, category).from(this);
    }

    public static Builder builder(CodeableConcept type, DeviceMetricCategory category) {
        return new Builder(type, category);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final CodeableConcept type;
        private final DeviceMetricCategory category;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept unit;
        private Reference source;
        private Reference parent;
        private DeviceMetricOperationalStatus operationalStatus;
        private DeviceMetricColor color;
        private Timing measurementPeriod;
        private List<Calibration> calibration = new ArrayList<>();

        private Builder(CodeableConcept type, DeviceMetricCategory category) {
            super();
            this.type = type;
            this.category = category;
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
         * Unique instance identifiers assigned to a device by the device or gateway software, manufacturers, other organizations 
         * or owners. For example: handle ID.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Unique instance identifiers assigned to a device by the device or gateway software, manufacturers, other organizations 
         * or owners. For example: handle ID.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Instance identifier
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
         * Describes the unit that an observed value determined for this metric will have. For example: Percent, Seconds, etc.
         * </p>
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
         * <p>
         * Describes the link to the Device that this DeviceMetric belongs to and that contains administrative device information 
         * such as manufacturer, serial number, etc.
         * </p>
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
         * <p>
         * Describes the link to the Device that this DeviceMetric belongs to and that provide information about the location of 
         * this DeviceMetric in the containment structure of the parent Device. An example would be a Device that represents a 
         * Channel. This reference can be used by a client application to distinguish DeviceMetrics that have the same type, but 
         * should be interpreted based on their containment location.
         * </p>
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
         * <p>
         * Indicates current operational state of the device. For example: On, Off, Standby, etc.
         * </p>
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
         * <p>
         * Describes the color representation for the metric. This is often used to aid clinicians to track and identify 
         * parameter types by color. In practice, consider a Patient Monitor that has ECG/HR and Pleth for example; the 
         * parameters are displayed in different characteristic colors, such as HR-blue, BP-green, and PR and SpO2- magenta.
         * </p>
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
         * <p>
         * Describes the measurement repetition time. This is not necessarily the same as the update period. The measurement 
         * repetition time can range from milliseconds up to hours. An example for a measurement repetition time in the range of 
         * milliseconds is the sampling rate of an ECG. An example for a measurement repetition time in the range of hours is a 
         * NIBP that is triggered automatically every hour. The update period may be different than the measurement repetition 
         * time, if the device does not update the published observed value with the same frequency as it was measured.
         * </p>
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
         * <p>
         * Describes the calibrations that have been performed or that are required to be performed.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Describes the calibrations that have been performed or that are required to be performed.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param calibration
         *     Describes the calibrations that have been performed or that are required to be performed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder calibration(Collection<Calibration> calibration) {
            this.calibration = new ArrayList<>(calibration);
            return this;
        }

        @Override
        public DeviceMetric build() {
            return new DeviceMetric(this);
        }

        private Builder from(DeviceMetric deviceMetric) {
            id = deviceMetric.id;
            meta = deviceMetric.meta;
            implicitRules = deviceMetric.implicitRules;
            language = deviceMetric.language;
            text = deviceMetric.text;
            contained.addAll(deviceMetric.contained);
            extension.addAll(deviceMetric.extension);
            modifierExtension.addAll(deviceMetric.modifierExtension);
            identifier.addAll(deviceMetric.identifier);
            unit = deviceMetric.unit;
            source = deviceMetric.source;
            parent = deviceMetric.parent;
            operationalStatus = deviceMetric.operationalStatus;
            color = deviceMetric.color;
            measurementPeriod = deviceMetric.measurementPeriod;
            calibration.addAll(deviceMetric.calibration);
            return this;
        }
    }

    /**
     * <p>
     * Describes the calibrations that have been performed or that are required to be performed.
     * </p>
     */
    public static class Calibration extends BackboneElement {
        private final DeviceMetricCalibrationType type;
        private final DeviceMetricCalibrationState state;
        private final Instant time;

        private volatile int hashCode;

        private Calibration(Builder builder) {
            super(builder);
            type = builder.type;
            state = builder.state;
            time = builder.time;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Describes the type of the calibration method.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DeviceMetricCalibrationType}.
         */
        public DeviceMetricCalibrationType getType() {
            return type;
        }

        /**
         * <p>
         * Describes the state of the calibration.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DeviceMetricCalibrationState}.
         */
        public DeviceMetricCalibrationState getState() {
            return state;
        }

        /**
         * <p>
         * Describes the time last calibration has been performed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Instant}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(type, "type", visitor);
                    accept(state, "state", visitor);
                    accept(time, "time", visitor);
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
            // optional
            private DeviceMetricCalibrationType type;
            private DeviceMetricCalibrationState state;
            private Instant time;

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
             * Describes the type of the calibration method.
             * </p>
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
             * <p>
             * Describes the state of the calibration.
             * </p>
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
             * <p>
             * Describes the time last calibration has been performed.
             * </p>
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

            @Override
            public Calibration build() {
                return new Calibration(this);
            }

            private Builder from(Calibration calibration) {
                id = calibration.id;
                extension.addAll(calibration.extension);
                modifierExtension.addAll(calibration.modifierExtension);
                type = calibration.type;
                state = calibration.state;
                time = calibration.time;
                return this;
            }
        }
    }
}
