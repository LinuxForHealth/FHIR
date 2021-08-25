/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the 
 * data.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SampledData extends Element {
    @Summary
    @Required
    private final SimpleQuantity origin;
    @Summary
    @Required
    private final Decimal period;
    @Summary
    private final Decimal factor;
    @Summary
    private final Decimal lowerLimit;
    @Summary
    private final Decimal upperLimit;
    @Summary
    @Required
    private final PositiveInt dimensions;
    private final String data;

    private SampledData(Builder builder) {
        super(builder);
        origin = builder.origin;
        period = builder.period;
        factor = builder.factor;
        lowerLimit = builder.lowerLimit;
        upperLimit = builder.upperLimit;
        dimensions = builder.dimensions;
        data = builder.data;
    }

    /**
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire 
     * measurement series.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that is non-null.
     */
    public SimpleQuantity getOrigin() {
        return origin;
    }

    /**
     * The length of time between sampling times, measured in milliseconds.
     * 
     * @return
     *     An immutable object of type {@link Decimal} that is non-null.
     */
    public Decimal getPeriod() {
        return period;
    }

    /**
     * A correction factor that is applied to the sampled data points before they are added to the origin.
     * 
     * @return
     *     An immutable object of type {@link Decimal} that may be null.
     */
    public Decimal getFactor() {
        return factor;
    }

    /**
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" 
     * (lower than detection limit).
     * 
     * @return
     *     An immutable object of type {@link Decimal} that may be null.
     */
    public Decimal getLowerLimit() {
        return lowerLimit;
    }

    /**
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" 
     * (higher than detection limit).
     * 
     * @return
     *     An immutable object of type {@link Decimal} that may be null.
     */
    public Decimal getUpperLimit() {
        return upperLimit;
    }

    /**
     * The number of sample points at each time point. If this value is greater than one, then the dimensions will be 
     * interlaced - all the sample points for a point in time will be recorded at once.
     * 
     * @return
     *     An immutable object of type {@link PositiveInt} that is non-null.
     */
    public PositiveInt getDimensions() {
        return dimensions;
    }

    /**
     * A series of data points which are decimal values separated by a single space (character u20). The special values "E" 
     * (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getData() {
        return data;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (origin != null) || 
            (period != null) || 
            (factor != null) || 
            (lowerLimit != null) || 
            (upperLimit != null) || 
            (dimensions != null) || 
            (data != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(origin, "origin", visitor);
                accept(period, "period", visitor);
                accept(factor, "factor", visitor);
                accept(lowerLimit, "lowerLimit", visitor);
                accept(upperLimit, "upperLimit", visitor);
                accept(dimensions, "dimensions", visitor);
                accept(data, "data", visitor);
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
        SampledData other = (SampledData) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(origin, other.origin) && 
            Objects.equals(period, other.period) && 
            Objects.equals(factor, other.factor) && 
            Objects.equals(lowerLimit, other.lowerLimit) && 
            Objects.equals(upperLimit, other.upperLimit) && 
            Objects.equals(dimensions, other.dimensions) && 
            Objects.equals(data, other.data);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                origin, 
                period, 
                factor, 
                lowerLimit, 
                upperLimit, 
                dimensions, 
                data);
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

    public static class Builder extends Element.Builder {
        private SimpleQuantity origin;
        private Decimal period;
        private Decimal factor;
        private Decimal lowerLimit;
        private Decimal upperLimit;
        private PositiveInt dimensions;
        private String data;

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
         * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire 
         * measurement series.
         * 
         * <p>This element is required.
         * 
         * @param origin
         *     Zero value and units
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder origin(SimpleQuantity origin) {
            this.origin = origin;
            return this;
        }

        /**
         * The length of time between sampling times, measured in milliseconds.
         * 
         * <p>This element is required.
         * 
         * @param period
         *     Number of milliseconds between samples
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Decimal period) {
            this.period = period;
            return this;
        }

        /**
         * A correction factor that is applied to the sampled data points before they are added to the origin.
         * 
         * @param factor
         *     Multiply data by this before adding to origin
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder factor(Decimal factor) {
            this.factor = factor;
            return this;
        }

        /**
         * The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" 
         * (lower than detection limit).
         * 
         * @param lowerLimit
         *     Lower limit of detection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lowerLimit(Decimal lowerLimit) {
            this.lowerLimit = lowerLimit;
            return this;
        }

        /**
         * The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" 
         * (higher than detection limit).
         * 
         * @param upperLimit
         *     Upper limit of detection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder upperLimit(Decimal upperLimit) {
            this.upperLimit = upperLimit;
            return this;
        }

        /**
         * The number of sample points at each time point. If this value is greater than one, then the dimensions will be 
         * interlaced - all the sample points for a point in time will be recorded at once.
         * 
         * <p>This element is required.
         * 
         * @param dimensions
         *     Number of sample points at each time point
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dimensions(PositiveInt dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        /**
         * Convenience method for setting {@code data}.
         * 
         * @param data
         *     Decimal values with spaces, or "E" | "U" | "L"
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #data(com.ibm.fhir.model.type.String)
         */
        public Builder data(java.lang.String data) {
            this.data = (data == null) ? null : String.of(data);
            return this;
        }

        /**
         * A series of data points which are decimal values separated by a single space (character u20). The special values "E" 
         * (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value.
         * 
         * @param data
         *     Decimal values with spaces, or "E" | "U" | "L"
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder data(String data) {
            this.data = data;
            return this;
        }

        /**
         * Build the {@link SampledData}
         * 
         * <p>Required elements:
         * <ul>
         * <li>origin</li>
         * <li>period</li>
         * <li>dimensions</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link SampledData}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SampledData per the base specification
         */
        @Override
        public SampledData build() {
            SampledData sampledData = new SampledData(this);
            if (validating) {
                validate(sampledData);
            }
            return sampledData;
        }

        protected void validate(SampledData sampledData) {
            super.validate(sampledData);
            ValidationSupport.requireNonNull(sampledData.origin, "origin");
            ValidationSupport.requireNonNull(sampledData.period, "period");
            ValidationSupport.requireNonNull(sampledData.dimensions, "dimensions");
            ValidationSupport.requireValueOrChildren(sampledData);
        }

        protected Builder from(SampledData sampledData) {
            super.from(sampledData);
            origin = sampledData.origin;
            period = sampledData.period;
            factor = sampledData.factor;
            lowerLimit = sampledData.lowerLimit;
            upperLimit = sampledData.upperLimit;
            dimensions = sampledData.dimensions;
            data = sampledData.data;
            return this;
        }
    }
}
