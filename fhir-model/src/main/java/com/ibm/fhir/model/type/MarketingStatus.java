/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The marketing status describes the date when a medicinal product is actually put on the market or the date as of which 
 * it is no longer available.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MarketingStatus extends BackboneElement {
    @Summary
    private final CodeableConcept country;
    @Summary
    private final CodeableConcept jurisdiction;
    @Summary
    @Required
    private final CodeableConcept status;
    @Summary
    private final Period dateRange;
    @Summary
    private final DateTime restoreDate;

    private MarketingStatus(Builder builder) {
        super(builder);
        country = builder.country;
        jurisdiction = builder.jurisdiction;
        status = builder.status;
        dateRange = builder.dateRange;
        restoreDate = builder.restoreDate;
    }

    /**
     * The country in which the marketing authorisation has been granted shall be specified It should be specified using the 
     * ISO 3166 ‑ 1 alpha-2 code elements.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCountry() {
        return country;
    }

    /**
     * Where a Medicines Regulatory Agency has granted a marketing authorisation for which specific provisions within a 
     * jurisdiction apply, the jurisdiction can be specified using an appropriate controlled terminology The controlled term 
     * and the controlled term identifier shall be specified.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getJurisdiction() {
        return jurisdiction;
    }

    /**
     * This attribute provides information on the status of the marketing of the medicinal product See ISO/TS 20443 for more 
     * information and examples.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getStatus() {
        return status;
    }

    /**
     * The date when the Medicinal Product is placed on the market by the Marketing Authorisation Holder (or where 
     * applicable, the manufacturer/distributor) in a country and/or jurisdiction shall be provided A complete date 
     * consisting of day, month and year shall be specified using the ISO 8601 date format NOTE “Placed on the market” refers 
     * to the release of the Medicinal Product into the distribution chain.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getDateRange() {
        return dateRange;
    }

    /**
     * The date when the Medicinal Product is placed on the market by the Marketing Authorisation Holder (or where 
     * applicable, the manufacturer/distributor) in a country and/or jurisdiction shall be provided A complete date 
     * consisting of day, month and year shall be specified using the ISO 8601 date format NOTE “Placed on the market” refers 
     * to the release of the Medicinal Product into the distribution chain.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getRestoreDate() {
        return restoreDate;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (country != null) || 
            (jurisdiction != null) || 
            (status != null) || 
            (dateRange != null) || 
            (restoreDate != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(country, "country", visitor);
                accept(jurisdiction, "jurisdiction", visitor);
                accept(status, "status", visitor);
                accept(dateRange, "dateRange", visitor);
                accept(restoreDate, "restoreDate", visitor);
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
        MarketingStatus other = (MarketingStatus) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(country, other.country) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(status, other.status) && 
            Objects.equals(dateRange, other.dateRange) && 
            Objects.equals(restoreDate, other.restoreDate);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                modifierExtension, 
                country, 
                jurisdiction, 
                status, 
                dateRange, 
                restoreDate);
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

    public static class Builder extends BackboneElement.Builder {
        private CodeableConcept country;
        private CodeableConcept jurisdiction;
        private CodeableConcept status;
        private Period dateRange;
        private DateTime restoreDate;

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
         * May be used to represent additional information that is not part of the basic definition of the element and that 
         * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
         * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
         * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
         * extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored even if unrecognized
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the element and that 
         * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
         * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
         * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
         * extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored even if unrecognized
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * The country in which the marketing authorisation has been granted shall be specified It should be specified using the 
         * ISO 3166 ‑ 1 alpha-2 code elements.
         * 
         * @param country
         *     The country in which the marketing authorisation has been granted shall be specified It should be specified using the 
         *     ISO 3166 ‑ 1 alpha-2 code elements
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder country(CodeableConcept country) {
            this.country = country;
            return this;
        }

        /**
         * Where a Medicines Regulatory Agency has granted a marketing authorisation for which specific provisions within a 
         * jurisdiction apply, the jurisdiction can be specified using an appropriate controlled terminology The controlled term 
         * and the controlled term identifier shall be specified.
         * 
         * @param jurisdiction
         *     Where a Medicines Regulatory Agency has granted a marketing authorisation for which specific provisions within a 
         *     jurisdiction apply, the jurisdiction can be specified using an appropriate controlled terminology The controlled term 
         *     and the controlled term identifier shall be specified
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdiction(CodeableConcept jurisdiction) {
            this.jurisdiction = jurisdiction;
            return this;
        }

        /**
         * This attribute provides information on the status of the marketing of the medicinal product See ISO/TS 20443 for more 
         * information and examples.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     This attribute provides information on the status of the marketing of the medicinal product See ISO/TS 20443 for more 
         *     information and examples
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CodeableConcept status) {
            this.status = status;
            return this;
        }

        /**
         * The date when the Medicinal Product is placed on the market by the Marketing Authorisation Holder (or where 
         * applicable, the manufacturer/distributor) in a country and/or jurisdiction shall be provided A complete date 
         * consisting of day, month and year shall be specified using the ISO 8601 date format NOTE “Placed on the market” refers 
         * to the release of the Medicinal Product into the distribution chain.
         * 
         * @param dateRange
         *     The date when the Medicinal Product is placed on the market by the Marketing Authorisation Holder (or where 
         *     applicable, the manufacturer/distributor) in a country and/or jurisdiction shall be provided A complete date 
         *     consisting of day, month and year shall be specified using the ISO 8601 date format NOTE “Placed on the market” refers 
         *     to the release of the Medicinal Product into the distribution chain
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dateRange(Period dateRange) {
            this.dateRange = dateRange;
            return this;
        }

        /**
         * The date when the Medicinal Product is placed on the market by the Marketing Authorisation Holder (or where 
         * applicable, the manufacturer/distributor) in a country and/or jurisdiction shall be provided A complete date 
         * consisting of day, month and year shall be specified using the ISO 8601 date format NOTE “Placed on the market” refers 
         * to the release of the Medicinal Product into the distribution chain.
         * 
         * @param restoreDate
         *     The date when the Medicinal Product is placed on the market by the Marketing Authorisation Holder (or where 
         *     applicable, the manufacturer/distributor) in a country and/or jurisdiction shall be provided A complete date 
         *     consisting of day, month and year shall be specified using the ISO 8601 date format NOTE “Placed on the market” refers 
         *     to the release of the Medicinal Product into the distribution chain
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder restoreDate(DateTime restoreDate) {
            this.restoreDate = restoreDate;
            return this;
        }

        /**
         * Build the {@link MarketingStatus}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MarketingStatus}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MarketingStatus per the base specification
         */
        @Override
        public MarketingStatus build() {
            MarketingStatus marketingStatus = new MarketingStatus(this);
            if (validating) {
                validate(marketingStatus);
            }
            return marketingStatus;
        }

        protected void validate(MarketingStatus marketingStatus) {
            super.validate(marketingStatus);
            ValidationSupport.requireNonNull(marketingStatus.status, "status");
            ValidationSupport.requireValueOrChildren(marketingStatus);
        }

        protected Builder from(MarketingStatus marketingStatus) {
            super.from(marketingStatus);
            country = marketingStatus.country;
            jurisdiction = marketingStatus.jurisdiction;
            status = marketingStatus.status;
            dateRange = marketingStatus.dateRange;
            restoreDate = marketingStatus.restoreDate;
            return this;
        }
    }
}
