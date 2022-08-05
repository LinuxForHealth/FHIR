/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema 
 * types gYear, gYearMonth and date. Dates SHALL be valid dates.
 */
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class Date extends Element {
    public static final DateTimeFormatter PARSER_FORMATTER = DateTimeFormatter.ofPattern("[yyyy[-MM[-dd]]]");

    private final TemporalAccessor value;

    private Date(Builder builder) {
        super(builder);
        value = builder.value;
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
        return !(value instanceof LocalDate);
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
     * Factory method for creating Date objects from a TemporalAccessor
     * 
     * @param value
     *     A TemporalAccessor, not null
     */
    public static Date of(TemporalAccessor value) {
        Objects.requireNonNull(value, "value");
        return Date.builder().value(value).build();
    }

    /**
     * Factory method for creating Date objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String value that can be parsed by {@link #PARSER_FORMATTER}, not null
     */
    public static Date of(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Date.builder().value(value).build();
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
        Date other = (Date) obj;
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
         *     Primitive value for date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(TemporalAccessor value) {
            this.value = value;
            return this;
        }

        public Builder value(java.lang.String value) {
            this.value = PARSER_FORMATTER.parseBest(value, LocalDate::from, YearMonth::from, Year::from);
            return this;
        }

        /**
         * Build the {@link Date}
         * 
         * @return
         *     An immutable object of type {@link Date}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Date per the base specification
         */
        @Override
        public Date build() {
            Date date = new Date(this);
            if (validating) {
                validate(date);
            }
            return date;
        }

        protected void validate(Date date) {
            super.validate(date);
            ValidationSupport.checkValueType(date.value, LocalDate.class, YearMonth.class, Year.class);
            ValidationSupport.requireValueOrChildren(date);
        }

        protected Builder from(Date date) {
            super.from(date);
            value = date.value;
            return this;
        }
    }
}
