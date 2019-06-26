/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema 
 * types gYear, gYearMonth and date. Dates SHALL be valid dates.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Date extends Element {
    private static final DateTimeFormatter PARSER = DateTimeFormatter.ofPattern("[yyyy[-MM[-dd]]]");

    private final TemporalAccessor value;

    private Date(Builder builder) {
        super(builder);
        this.value = builder.value;
        ValidationSupport.checkValueType(value, LocalDate.class, YearMonth.class, Year.class);
    }

    /**
     * <p>
     * The actual value
     * </p>
     * 
     * @return
     *     An immutable object of type {@link TemporalAccessor}.
     */
    public TemporalAccessor getValue() {
        return value;
    }

    public boolean isPartial() {
        return !(value instanceof LocalDate);
    }

    public static Date of(TemporalAccessor value) {
        return Date.builder().value(value).build();
    }

    public static Date of(java.lang.String value) {
        return Date.builder().value(value).build();
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(value, "value", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        // optional
        private TemporalAccessor value;

        private Builder() {
            super();
        }

        /**
         * <p>
         * unique id for the element within a resource (for internal references)
         * </p>
         * 
         * @param id
         *     xml:id (or equivalent in JSON)
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
         * The actual value
         * </p>
         * 
         * @param value
         *     Primitive value for date
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder value(TemporalAccessor value) {
            this.value = value;
            return this;
        }

        public Builder value(java.lang.String value) {
            this.value = PARSER.parseBest(value, LocalDate::from, YearMonth::from, Year::from);
            return this;
        }

        @Override
        public Date build() {
            return new Date(this);
        }
    }
}
