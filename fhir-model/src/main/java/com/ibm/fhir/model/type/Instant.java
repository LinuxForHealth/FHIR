/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * An instant in time - known at least to the second
 * 
 * <p>Fractions of seconds may be specified up to nanosecond precision (9 digits). However, any fractions of seconds 
 * specified to greater than microsecond precision (6 digits) will be truncated to microsecond precision when stored.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Instant extends Element {
    public static final DateTimeFormatter PARSER_FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss").optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd().appendPattern("XXX").toFormatter();

    private final ZonedDateTime value;

    private Instant(Builder builder) {
        super(builder);
        value = ModelSupport.truncateTime(builder.value, ChronoUnit.MICROS);
    }

    /**
     * The actual value
     * 
     * @return
     *     An immutable object of type {@link java.time.ZonedDateTime} that may be null.
     */
    public ZonedDateTime getValue() {
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
     * Factory method for creating Instant objects from a ZonedDateTime
     * 
     * @param value
     *     A ZonedDateTime, not null
     */
    public static Instant of(ZonedDateTime value) {
        Objects.requireNonNull(value, "value");
        return Instant.builder().value(value).build();
    }

    /**
     * Factory method for creating Instant objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String value that can be parsed by {@link #PARSER_FORMATTER}, not null
     */
    public static Instant of(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Instant.builder().value(value).build();
    }

    /**
     * Factory method for creating a Instant that represents the current Instant
     */
    public static Instant now() {
        return Instant.builder().value(ZonedDateTime.now()).build();
    }

    /**
     * Factory method for creating a Instant that represents the current Instant in the passed time zone
     * 
     * @param offset
     *     the ZoneOffset for the desired time zone, not null
     */
    public static Instant now(ZoneOffset offset) {
        Objects.requireNonNull(offset, "offset");
        return Instant.builder().value(ZonedDateTime.now(offset)).build();
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
        Instant other = (Instant) obj;
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
        private ZonedDateTime value;

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
         *     Primitive value for instant
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(ZonedDateTime value) {
            this.value = value;
            return this;
        }

        public Builder value(java.lang.String value) {
            this.value = PARSER_FORMATTER.parse(value, ZonedDateTime::from);
            return this;
        }

        /**
         * Build the {@link Instant}
         * 
         * @return
         *     An immutable object of type {@link Instant}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Instant per the base specification
         */
        @Override
        public Instant build() {
            Instant instant = new Instant(this);
            if (validating) {
                validate(instant);
            }
            return instant;
        }

        protected void validate(Instant instant) {
            super.validate(instant);
            ValidationSupport.requireValueOrChildren(instant);
        }

        protected Builder from(Instant instant) {
            super.from(instant);
            value = instant.value;
            return this;
        }
    }
}
