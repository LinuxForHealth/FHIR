/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A date, date-time or partial date (e.g. just year or year + month). If hours and minutes are specified, a time zone 
 * SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be 
 * provided due to schema type constraints but may be zero-filled and may be ignored. Dates SHALL be valid dates.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class DateTime extends Element {
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder().appendPattern("yyyy").optionalStart().appendPattern("-MM").optionalStart().appendPattern("-dd").optionalStart().appendPattern("'T'HH:mm:ss").optionalStart().appendFraction(ChronoField.MICRO_OF_SECOND, 1, 6, true).optionalEnd().appendPattern("XXX").optionalEnd().optionalEnd().optionalEnd().toFormatter();

    private final TemporalAccessor value;

    private volatile int hashCode;

    private DateTime(Builder builder) {
        super(builder);
        value = builder.value;
        ValidationSupport.checkValueType(value, ZonedDateTime.class, LocalDate.class, YearMonth.class, Year.class);
        if (!hasChildren()) {
            throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
        }
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
        return !(value instanceof ZonedDateTime);
    }

    @Override
    protected boolean hasChildren() {
        return super.hasChildren() || 
            (value != null);
    }

    public static DateTime of(TemporalAccessor value) {
        return DateTime.builder().value(value).build();
    }

    public static DateTime of(java.lang.String value) {
        return DateTime.builder().value(value).build();
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
        DateTime other = (DateTime) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                value);
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
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to the existing list
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
         * The actual value
         * </p>
         * 
         * @param value
         *     Primitive value for dateTime
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(TemporalAccessor value) {
            this.value = value;
            return this;
        }

        public Builder value(java.lang.String value) {
            this.value = PARSER.parseBest(value, ZonedDateTime::from, LocalDate::from, YearMonth::from, Year::from);
            return this;
        }

        @Override
        public DateTime build() {
            return new DateTime(this);
        }

        private Builder from(DateTime dateTime) {
            id = dateTime.id;
            extension.addAll(dateTime.extension);
            value = dateTime.value;
            return this;
        }
    }
}
