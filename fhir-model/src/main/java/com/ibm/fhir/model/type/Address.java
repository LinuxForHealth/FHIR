/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.AddressType;
import com.ibm.fhir.model.type.code.AddressUse;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * An address expressed using postal conventions (as opposed to GPS or other location definition formats). This data type 
 * may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be valid 
 * for mail delivery. There are a variety of postal address formats defined around the world.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Address extends Element {
    @Summary
    @Binding(
        bindingName = "AddressUse",
        strength = BindingStrength.Value.REQUIRED,
        description = "The use of an address (home / work / etc.).",
        valueSet = "http://hl7.org/fhir/ValueSet/address-use|4.3.0-CIBUILD"
    )
    private final AddressUse use;
    @Summary
    @Binding(
        bindingName = "AddressType",
        strength = BindingStrength.Value.REQUIRED,
        description = "The type of an address (physical / postal).",
        valueSet = "http://hl7.org/fhir/ValueSet/address-type|4.3.0-CIBUILD"
    )
    private final AddressType type;
    @Summary
    private final String text;
    @Summary
    private final List<String> line;
    @Summary
    private final String city;
    @Summary
    private final String district;
    @Summary
    private final String state;
    @Summary
    private final String postalCode;
    @Summary
    private final String country;
    @Summary
    private final Period period;

    private Address(Builder builder) {
        super(builder);
        use = builder.use;
        type = builder.type;
        text = builder.text;
        line = Collections.unmodifiableList(builder.line);
        city = builder.city;
        district = builder.district;
        state = builder.state;
        postalCode = builder.postalCode;
        country = builder.country;
        period = builder.period;
    }

    /**
     * The purpose of this address.
     * 
     * @return
     *     An immutable object of type {@link AddressUse} that may be null.
     */
    public AddressUse getUse() {
        return use;
    }

    /**
     * Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of 
     * addresses). Most addresses are both.
     * 
     * @return
     *     An immutable object of type {@link AddressType} that may be null.
     */
    public AddressType getType() {
        return type;
    }

    /**
     * Specifies the entire address as it should be displayed e.g. on a postal label. This may be provided instead of or as 
     * well as the specific parts.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getText() {
        return text;
    }

    /**
     * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery 
     * hints, and similar address information.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getLine() {
        return line;
    }

    /**
     * The name of the city, town, suburb, village or other community or delivery center.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getCity() {
        return city;
    }

    /**
     * The name of the administrative area (county).
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in 
     * common use (e.g. US 2 letter state codes).
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getState() {
        return state;
    }

    /**
     * A postal code designating a region defined by the postal service.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Country - a nation as commonly understood or generally accepted.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Time period when address was/is in use.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (use != null) || 
            (type != null) || 
            (text != null) || 
            !line.isEmpty() || 
            (city != null) || 
            (district != null) || 
            (state != null) || 
            (postalCode != null) || 
            (country != null) || 
            (period != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(use, "use", visitor);
                accept(type, "type", visitor);
                accept(text, "text", visitor);
                accept(line, "line", visitor, String.class);
                accept(city, "city", visitor);
                accept(district, "district", visitor);
                accept(state, "state", visitor);
                accept(postalCode, "postalCode", visitor);
                accept(country, "country", visitor);
                accept(period, "period", visitor);
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
        Address other = (Address) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(use, other.use) && 
            Objects.equals(type, other.type) && 
            Objects.equals(text, other.text) && 
            Objects.equals(line, other.line) && 
            Objects.equals(city, other.city) && 
            Objects.equals(district, other.district) && 
            Objects.equals(state, other.state) && 
            Objects.equals(postalCode, other.postalCode) && 
            Objects.equals(country, other.country) && 
            Objects.equals(period, other.period);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                use, 
                type, 
                text, 
                line, 
                city, 
                district, 
                state, 
                postalCode, 
                country, 
                period);
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
        private AddressUse use;
        private AddressType type;
        private String text;
        private List<String> line = new ArrayList<>();
        private String city;
        private String district;
        private String state;
        private String postalCode;
        private String country;
        private Period period;

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
         * The purpose of this address.
         * 
         * @param use
         *     home | work | temp | old | billing - purpose of this address
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder use(AddressUse use) {
            this.use = use;
            return this;
        }

        /**
         * Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of 
         * addresses). Most addresses are both.
         * 
         * @param type
         *     postal | physical | both
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(AddressType type) {
            this.type = type;
            return this;
        }

        /**
         * Convenience method for setting {@code text}.
         * 
         * @param text
         *     Text representation of the address
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #text(com.ibm.fhir.model.type.String)
         */
        public Builder text(java.lang.String text) {
            this.text = (text == null) ? null : String.of(text);
            return this;
        }

        /**
         * Specifies the entire address as it should be displayed e.g. on a postal label. This may be provided instead of or as 
         * well as the specific parts.
         * 
         * @param text
         *     Text representation of the address
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * Convenience method for setting {@code line}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param line
         *     Street name, number, direction &amp; P.O. Box etc.
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #line(com.ibm.fhir.model.type.String)
         */
        public Builder line(java.lang.String... line) {
            for (java.lang.String value : line) {
                this.line.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery 
         * hints, and similar address information.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param line
         *     Street name, number, direction &amp; P.O. Box etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder line(String... line) {
            for (String value : line) {
                this.line.add(value);
            }
            return this;
        }

        /**
         * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery 
         * hints, and similar address information.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param line
         *     Street name, number, direction &amp; P.O. Box etc.
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder line(Collection<String> line) {
            this.line = new ArrayList<>(line);
            return this;
        }

        /**
         * Convenience method for setting {@code city}.
         * 
         * @param city
         *     Name of city, town etc.
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #city(com.ibm.fhir.model.type.String)
         */
        public Builder city(java.lang.String city) {
            this.city = (city == null) ? null : String.of(city);
            return this;
        }

        /**
         * The name of the city, town, suburb, village or other community or delivery center.
         * 
         * @param city
         *     Name of city, town etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder city(String city) {
            this.city = city;
            return this;
        }

        /**
         * Convenience method for setting {@code district}.
         * 
         * @param district
         *     District name (aka county)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #district(com.ibm.fhir.model.type.String)
         */
        public Builder district(java.lang.String district) {
            this.district = (district == null) ? null : String.of(district);
            return this;
        }

        /**
         * The name of the administrative area (county).
         * 
         * @param district
         *     District name (aka county)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder district(String district) {
            this.district = district;
            return this;
        }

        /**
         * Convenience method for setting {@code state}.
         * 
         * @param state
         *     Sub-unit of country (abbreviations ok)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #state(com.ibm.fhir.model.type.String)
         */
        public Builder state(java.lang.String state) {
            this.state = (state == null) ? null : String.of(state);
            return this;
        }

        /**
         * Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in 
         * common use (e.g. US 2 letter state codes).
         * 
         * @param state
         *     Sub-unit of country (abbreviations ok)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder state(String state) {
            this.state = state;
            return this;
        }

        /**
         * Convenience method for setting {@code postalCode}.
         * 
         * @param postalCode
         *     Postal code for area
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #postalCode(com.ibm.fhir.model.type.String)
         */
        public Builder postalCode(java.lang.String postalCode) {
            this.postalCode = (postalCode == null) ? null : String.of(postalCode);
            return this;
        }

        /**
         * A postal code designating a region defined by the postal service.
         * 
         * @param postalCode
         *     Postal code for area
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        /**
         * Convenience method for setting {@code country}.
         * 
         * @param country
         *     Country (e.g. can be ISO 3166 2 or 3 letter code)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #country(com.ibm.fhir.model.type.String)
         */
        public Builder country(java.lang.String country) {
            this.country = (country == null) ? null : String.of(country);
            return this;
        }

        /**
         * Country - a nation as commonly understood or generally accepted.
         * 
         * @param country
         *     Country (e.g. can be ISO 3166 2 or 3 letter code)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder country(String country) {
            this.country = country;
            return this;
        }

        /**
         * Time period when address was/is in use.
         * 
         * @param period
         *     Time period when address was/is in use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * Build the {@link Address}
         * 
         * @return
         *     An immutable object of type {@link Address}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Address per the base specification
         */
        @Override
        public Address build() {
            Address address = new Address(this);
            if (validating) {
                validate(address);
            }
            return address;
        }

        protected void validate(Address address) {
            super.validate(address);
            ValidationSupport.checkList(address.line, "line", String.class);
            ValidationSupport.requireValueOrChildren(address);
        }

        protected Builder from(Address address) {
            super.from(address);
            use = address.use;
            type = address.type;
            text = address.text;
            line.addAll(address.line);
            city = address.city;
            district = address.district;
            state = address.state;
            postalCode = address.postalCode;
            country = address.country;
            period = address.period;
            return this;
        }
    }
}
