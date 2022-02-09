/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A date, date-time or partial date (e.g. just year or year + month). If hours and minutes are specified, a time zone 
 * SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be 
 * provided due to schema type constraints but may be zero-filled and may be ignored. Dates SHALL be valid dates.
 * 
 * <p>If seconds are specified, fractions of seconds may be specified up to nanosecond precision (9 digits). However, any 
 * fractions of seconds specified to greater than microsecond precision (6 digits) will be truncated to microsecond 
 * precision when stored.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DateTime extends Element {
    public static final DateTimeFormatter PARSER_FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy").optionalStart().appendPattern("-MM").optionalStart().appendPattern("-dd").optionalStart().appendPattern("'T'HH:mm:ss").optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd().appendPattern("XXX").optionalEnd().optionalEnd().optionalEnd().toFormatter();

    private final TemporalAccessor value;

    private DateTime(Builder builder) {
        super(builder);
        value = ModelSupport.truncateTime(builder.value, ChronoUnit.MICROS);
    }

    /**
     * The actual value
     * 
     * @return
     *     An immutable object of type {@link java.time.TemporalAccessor} that may be null.
     */
    public TemporalAccessor getValue() {
        return value;
    }

    public boolean isPartial() {
        return !(value instanceof ZonedDateTime);
    }

    @Override
    public boolean hasValue() {
        return (value != null);
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren();
    }

    /**
     * Factory method for creating DateTime objects from a TemporalAccessor
     * 
     * @param value
     *     A TemporalAccessor, not null
     */
    public static DateTime of(TemporalAccessor value) {
        Objects.requireNonNull(value, "value");
        return DateTime.builder().value(value).build();
    }

    /**
     * Factory method for creating DateTime objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String value that can be parsed by {@link #PARSER_FORMATTER}, not null
     */
    public static DateTime of(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return DateTime.builder().value(value).build();
    }

    /**
     * Factory method for creating a DateTime that represents the current DateTime
     */
    public static DateTime now() {
        return DateTime.builder().value(ZonedDateTime.now()).build();
    }

    /**
     * Factory method for creating a DateTime that represents the current DateTime in the passed time zone
     * 
     * @param offset
     *     The ZoneOffset for the desired time zone, not null
     */
    public static DateTime now(ZoneOffset offset) {
        Objects.requireNonNull(offset, "offset");
        return DateTime.builder().value(ZonedDateTime.now(offset)).build();
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(value, "value", visitor);
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
        private TemporalAccessor value;

        private Builder() {
            super();
        }

        /**
         * unique id for the element within a resource (for internal references)
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
         * The actual value
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
            this.value = PARSER_FORMATTER.parseBest(value, ZonedDateTime::from, LocalDate::from, YearMonth::from, Year::from);
            return this;
        }

        /**
         * Build the {@link DateTime}
         * 
         * @return
         *     An immutable object of type {@link DateTime}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid DateTime per the base specification
         */
        @Override
        public DateTime build() {
            DateTime dateTime = new DateTime(this);
            if (validating) {
                validate(dateTime);
            }
            return dateTime;
        }

        protected void validate(DateTime dateTime) {
            super.validate(dateTime);
            ValidationSupport.checkValueType(dateTime.value, ZonedDateTime.class, LocalDate.class, YearMonth.class, Year.class);
            ValidationSupport.requireValueOrChildren(dateTime);
        }

        protected Builder from(DateTime dateTime) {
            super.from(dateTime);
            value = dateTime.value;
            return this;
        }
    }
}
