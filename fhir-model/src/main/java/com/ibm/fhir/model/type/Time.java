/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A time during the day, with no date specified
 * 
 * <p>Fractions of seconds may be specified up to nanosecond precision (9 digits). However, any fractions of seconds 
 * specified to greater than microsecond precision (6 digits) will be truncated to microsecond precision when stored.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Time extends Element {
    public static final DateTimeFormatter PARSER_FORMATTER = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss").optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd().toFormatter();

    private final LocalTime value;

    private Time(Builder builder) {
        super(builder);
        value = ModelSupport.truncateTime(builder.value, ChronoUnit.MICROS);
    }

    /**
     * The actual value
     * 
     * @return
     *     An immutable object of type {@link java.time.LocalTime} that may be null.
     */
    public LocalTime getValue() {
        return value;
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
     * Factory method for creating Time objects from a LocalTime
     * 
     * @param value
     *     A LocalTime, not null
     */
    public static Time of(LocalTime value) {
        Objects.requireNonNull(value, "value");
        return Time.builder().value(value).build();
    }

    /**
     * Factory method for creating Time objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String value that can be parsed by {@link #PARSER_FORMATTER}, not null
     */
    public static Time of(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Time.builder().value(value).build();
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
        Time other = (Time) obj;
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
        private LocalTime value;

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
         *     Primitive value for time
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(LocalTime value) {
            this.value = value;
            return this;
        }

        public Builder value(java.lang.String value) {
            this.value = PARSER_FORMATTER.parse(value, LocalTime::from);
            return this;
        }

        /**
         * Build the {@link Time}
         * 
         * @return
         *     An immutable object of type {@link Time}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Time per the base specification
         */
        @Override
        public Time build() {
            Time time = new Time(this);
            if (validating) {
                validate(time);
            }
            return time;
        }

        protected void validate(Time time) {
            super.validate(time);
            ValidationSupport.requireValueOrChildren(time);
        }

        protected Builder from(Time time) {
            super.from(time);
            value = time.value;
            return this;
        }
    }
}
