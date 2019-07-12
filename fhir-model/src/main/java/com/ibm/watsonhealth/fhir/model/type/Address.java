/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.AddressType;
import com.ibm.watsonhealth.fhir.model.type.AddressUse;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * An address expressed using postal conventions (as opposed to GPS or other location definition formats). This data type 
 * may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be valid 
 * for mail delivery. There are a variety of postal address formats defined around the world.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Address extends Element {
    private final AddressUse use;
    private final AddressType type;
    private final String text;
    private final List<String> line;
    private final String city;
    private final String district;
    private final String state;
    private final String postalCode;
    private final String country;
    private final Period period;

    private volatile int hashCode;

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
        if (!hasChildren()) {
            throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
        }
    }

    /**
     * <p>
     * The purpose of this address.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link AddressUse}.
     */
    public AddressUse getUse() {
        return use;
    }

    /**
     * <p>
     * Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of 
     * addresses). Most addresses are both.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link AddressType}.
     */
    public AddressType getType() {
        return type;
    }

    /**
     * <p>
     * Specifies the entire address as it should be displayed e.g. on a postal label. This may be provided instead of or as 
     * well as the specific parts.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getText() {
        return text;
    }

    /**
     * <p>
     * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery 
     * hints, and similar address information.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String}.
     */
    public List<String> getLine() {
        return line;
    }

    /**
     * <p>
     * The name of the city, town, suburb, village or other community or delivery center.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getCity() {
        return city;
    }

    /**
     * <p>
     * The name of the administrative area (county).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDistrict() {
        return district;
    }

    /**
     * <p>
     * Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in 
     * common use (e.g. US 2 letter state codes).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getState() {
        return state;
    }

    /**
     * <p>
     * A postal code designating a region defined by the postal service.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * <p>
     * Country - a nation as commonly understood or generally accepted.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getCountry() {
        return country;
    }

    /**
     * <p>
     * Time period when address was/is in use.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getPeriod() {
        return period;
    }

    @Override
    protected boolean hasChildren() {
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
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
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
        // optional
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
         * <p>
         * Unique id for the element within a resource (for internal references). This may be any string value that does not 
         * contain spaces.
         * </p>
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
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
         * The purpose of this address.
         * </p>
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
         * <p>
         * Distinguishes between physical addresses (those you can visit) and mailing addresses (e.g. PO Boxes and care-of 
         * addresses). Most addresses are both.
         * </p>
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
         * <p>
         * Specifies the entire address as it should be displayed e.g. on a postal label. This may be provided instead of or as 
         * well as the specific parts.
         * </p>
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
         * <p>
         * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery 
         * hints, and similar address information.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * This component contains the house number, apartment number, street name, street direction, P.O. Box number, delivery 
         * hints, and similar address information.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param line
         *     Street name, number, direction &amp; P.O. Box etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder line(Collection<String> line) {
            this.line = new ArrayList<>(line);
            return this;
        }

        /**
         * <p>
         * The name of the city, town, suburb, village or other community or delivery center.
         * </p>
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
         * <p>
         * The name of the administrative area (county).
         * </p>
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
         * <p>
         * Sub-unit of a country with limited sovereignty in a federally organized country. A code may be used if codes are in 
         * common use (e.g. US 2 letter state codes).
         * </p>
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
         * <p>
         * A postal code designating a region defined by the postal service.
         * </p>
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
         * <p>
         * Country - a nation as commonly understood or generally accepted.
         * </p>
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
         * <p>
         * Time period when address was/is in use.
         * </p>
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

        @Override
        public Address build() {
            return new Address(this);
        }

        private Builder from(Address address) {
            id = address.id;
            extension.addAll(address.extension);
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
