/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * An instant in time - known at least to the second
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Instant extends Element {
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss").optionalStart().appendFraction(ChronoField.MICRO_OF_SECOND, 1, 6, true).optionalEnd().appendPattern("XXX").toFormatter();

    private final ZonedDateTime value;

    private Instant(Builder builder) {
        super(builder);
        this.value = builder.value;
    }

    /**
     * <p>
     * The actual value
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ZonedDateTime}.
     */
    public ZonedDateTime getValue() {
        return value;
    }

    public static Instant of(ZonedDateTime value) {
        return Instant.builder().value(value).build();
    }

    public static Instant of(java.lang.String value) {
        return Instant.builder().value(value).build();
    }

    public static Instant now(boolean normalize) {
        ZonedDateTime now = ZonedDateTime.now();
        if (normalize) {
            // normalize to UTC
            now = now.withZoneSameInstant(ZoneOffset.UTC);
        }
        return Instant.builder().value(now).build();
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
        private ZonedDateTime value;

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
         *     Primitive value for instant
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder value(ZonedDateTime value) {
            this.value = value;
            return this;
        }

        public Builder value(java.lang.String value) {
            this.value = PARSER.parse(value, ZonedDateTime::from);
            return this;
        }

        @Override
        public Instant build() {
            return new Instant(this);
        }
    }
}
