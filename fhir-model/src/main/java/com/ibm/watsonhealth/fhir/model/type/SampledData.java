/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the 
 * data.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class SampledData extends Element {
    private final Quantity origin;
    private final Decimal period;
    private final Decimal factor;
    private final Decimal lowerLimit;
    private final Decimal upperLimit;
    private final PositiveInt dimensions;
    private final String data;

    private SampledData(Builder builder) {
        super(builder);
        this.origin = ValidationSupport.requireNonNull(builder.origin, "origin");
        this.period = ValidationSupport.requireNonNull(builder.period, "period");
        this.factor = builder.factor;
        this.lowerLimit = builder.lowerLimit;
        this.upperLimit = builder.upperLimit;
        this.dimensions = ValidationSupport.requireNonNull(builder.dimensions, "dimensions");
        this.data = builder.data;
    }

    /**
     * <p>
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire 
     * measurement series.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getOrigin() {
        return origin;
    }

    /**
     * <p>
     * The length of time between sampling times, measured in milliseconds.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Decimal}.
     */
    public Decimal getPeriod() {
        return period;
    }

    /**
     * <p>
     * A correction factor that is applied to the sampled data points before they are added to the origin.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Decimal}.
     */
    public Decimal getFactor() {
        return factor;
    }

    /**
     * <p>
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" 
     * (lower than detection limit).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Decimal}.
     */
    public Decimal getLowerLimit() {
        return lowerLimit;
    }

    /**
     * <p>
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" 
     * (higher than detection limit).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Decimal}.
     */
    public Decimal getUpperLimit() {
        return upperLimit;
    }

    /**
     * <p>
     * The number of sample points at each time point. If this value is greater than one, then the dimensions will be 
     * interlaced - all the sample points for a point in time will be recorded at once.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PositiveInt}.
     */
    public PositiveInt getDimensions() {
        return dimensions;
    }

    /**
     * <p>
     * A series of data points which are decimal values separated by a single space (character u20). The special values "E" 
     * (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getData() {
        return data;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(origin, period, dimensions);
        builder.id = id;
        builder.extension.addAll(extension);
        builder.factor = factor;
        builder.lowerLimit = lowerLimit;
        builder.upperLimit = upperLimit;
        builder.data = data;
        return builder;
    }

    public static Builder builder(Quantity origin, Decimal period, PositiveInt dimensions) {
        return new Builder(origin, period, dimensions);
    }

    public static class Builder extends Element.Builder {
        // required
        private final Quantity origin;
        private final Decimal period;
        private final PositiveInt dimensions;

        // optional
        private Decimal factor;
        private Decimal lowerLimit;
        private Decimal upperLimit;
        private String data;

        private Builder(Quantity origin, Decimal period, PositiveInt dimensions) {
            super();
            this.origin = origin;
            this.period = period;
            this.dimensions = dimensions;
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
         * A correction factor that is applied to the sampled data points before they are added to the origin.
         * </p>
         * 
         * @param factor
         *     Multiply data by this before adding to origin
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder factor(Decimal factor) {
            this.factor = factor;
            return this;
        }

        /**
         * <p>
         * The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" 
         * (lower than detection limit).
         * </p>
         * 
         * @param lowerLimit
         *     Lower limit of detection
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder lowerLimit(Decimal lowerLimit) {
            this.lowerLimit = lowerLimit;
            return this;
        }

        /**
         * <p>
         * The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" 
         * (higher than detection limit).
         * </p>
         * 
         * @param upperLimit
         *     Upper limit of detection
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder upperLimit(Decimal upperLimit) {
            this.upperLimit = upperLimit;
            return this;
        }

        /**
         * <p>
         * A series of data points which are decimal values separated by a single space (character u20). The special values "E" 
         * (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value.
         * </p>
         * 
         * @param data
         *     Decimal values with spaces, or "E" | "U" | "L"
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder data(String data) {
            this.data = data;
            return this;
        }

        @Override
        public SampledData build() {
            return new SampledData(this);
        }
    }
}
