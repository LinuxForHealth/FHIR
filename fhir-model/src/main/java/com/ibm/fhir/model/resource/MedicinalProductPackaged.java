/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.MarketingStatus;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.ProdCharacteristic;
import com.ibm.fhir.model.type.ProductShelfLife;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A medicinal product in a container or package.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicinalProductPackaged extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @ReferenceTarget({ "MedicinalProduct" })
    private final List<Reference> subject;
    @Summary
    private final String description;
    @Summary
    private final CodeableConcept legalStatusOfSupply;
    @Summary
    private final List<MarketingStatus> marketingStatus;
    @Summary
    @ReferenceTarget({ "MedicinalProductAuthorization" })
    private final Reference marketingAuthorization;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final List<Reference> manufacturer;
    @Summary
    private final List<BatchIdentifier> batchIdentifier;
    @Summary
    @Required
    private final List<PackageItem> packageItem;

    private MedicinalProductPackaged(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        subject = Collections.unmodifiableList(builder.subject);
        description = builder.description;
        legalStatusOfSupply = builder.legalStatusOfSupply;
        marketingStatus = Collections.unmodifiableList(builder.marketingStatus);
        marketingAuthorization = builder.marketingAuthorization;
        manufacturer = Collections.unmodifiableList(builder.manufacturer);
        batchIdentifier = Collections.unmodifiableList(builder.batchIdentifier);
        packageItem = Collections.unmodifiableList(builder.packageItem);
    }

    /**
     * Unique identifier.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The product with this is a pack for.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSubject() {
        return subject;
    }

    /**
     * Textual description.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The legal status of supply of the medicinal product as classified by the regulator.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getLegalStatusOfSupply() {
        return legalStatusOfSupply;
    }

    /**
     * Marketing information.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MarketingStatus} that may be empty.
     */
    public List<MarketingStatus> getMarketingStatus() {
        return marketingStatus;
    }

    /**
     * Manufacturer of this Package Item.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getMarketingAuthorization() {
        return marketingAuthorization;
    }

    /**
     * Manufacturer of this Package Item.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getManufacturer() {
        return manufacturer;
    }

    /**
     * Batch numbering.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link BatchIdentifier} that may be empty.
     */
    public List<BatchIdentifier> getBatchIdentifier() {
        return batchIdentifier;
    }

    /**
     * A packaging item, as a contained for medicine, possibly with other packaging items within.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link PackageItem} that is non-empty.
     */
    public List<PackageItem> getPackageItem() {
        return packageItem;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !subject.isEmpty() || 
            (description != null) || 
            (legalStatusOfSupply != null) || 
            !marketingStatus.isEmpty() || 
            (marketingAuthorization != null) || 
            !manufacturer.isEmpty() || 
            !batchIdentifier.isEmpty() || 
            !packageItem.isEmpty();
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(meta, "meta", visitor);
                accept(implicitRules, "implicitRules", visitor);
                accept(language, "language", visitor);
                accept(text, "text", visitor);
                accept(contained, "contained", visitor, Resource.class);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(subject, "subject", visitor, Reference.class);
                accept(description, "description", visitor);
                accept(legalStatusOfSupply, "legalStatusOfSupply", visitor);
                accept(marketingStatus, "marketingStatus", visitor, MarketingStatus.class);
                accept(marketingAuthorization, "marketingAuthorization", visitor);
                accept(manufacturer, "manufacturer", visitor, Reference.class);
                accept(batchIdentifier, "batchIdentifier", visitor, BatchIdentifier.class);
                accept(packageItem, "packageItem", visitor, PackageItem.class);
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
        MedicinalProductPackaged other = (MedicinalProductPackaged) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(description, other.description) && 
            Objects.equals(legalStatusOfSupply, other.legalStatusOfSupply) && 
            Objects.equals(marketingStatus, other.marketingStatus) && 
            Objects.equals(marketingAuthorization, other.marketingAuthorization) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(batchIdentifier, other.batchIdentifier) && 
            Objects.equals(packageItem, other.packageItem);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                text, 
                contained, 
                extension, 
                modifierExtension, 
                identifier, 
                subject, 
                description, 
                legalStatusOfSupply, 
                marketingStatus, 
                marketingAuthorization, 
                manufacturer, 
                batchIdentifier, 
                packageItem);
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

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> subject = new ArrayList<>();
        private String description;
        private CodeableConcept legalStatusOfSupply;
        private List<MarketingStatus> marketingStatus = new ArrayList<>();
        private Reference marketingAuthorization;
        private List<Reference> manufacturer = new ArrayList<>();
        private List<BatchIdentifier> batchIdentifier = new ArrayList<>();
        private List<PackageItem> packageItem = new ArrayList<>();

        private Builder() {
            super();
        }

        /**
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * The base language in which the resource is written.
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
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
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
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
         * Unique identifier.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * Unique identifier.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique identifier
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * The product with this is a pack for.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProduct}</li>
         * </ul>
         * 
         * @param subject
         *     The product with this is a pack for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference... subject) {
            for (Reference value : subject) {
                this.subject.add(value);
            }
            return this;
        }

        /**
         * The product with this is a pack for.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProduct}</li>
         * </ul>
         * 
         * @param subject
         *     The product with this is a pack for
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder subject(Collection<Reference> subject) {
            this.subject = new ArrayList<>(subject);
            return this;
        }

        /**
         * Convenience method for setting {@code description}.
         * 
         * @param description
         *     Textual description
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #description(com.ibm.fhir.model.type.String)
         */
        public Builder description(java.lang.String description) {
            this.description = (description == null) ? null : String.of(description);
            return this;
        }

        /**
         * Textual description.
         * 
         * @param description
         *     Textual description
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * The legal status of supply of the medicinal product as classified by the regulator.
         * 
         * @param legalStatusOfSupply
         *     The legal status of supply of the medicinal product as classified by the regulator
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder legalStatusOfSupply(CodeableConcept legalStatusOfSupply) {
            this.legalStatusOfSupply = legalStatusOfSupply;
            return this;
        }

        /**
         * Marketing information.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param marketingStatus
         *     Marketing information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder marketingStatus(MarketingStatus... marketingStatus) {
            for (MarketingStatus value : marketingStatus) {
                this.marketingStatus.add(value);
            }
            return this;
        }

        /**
         * Marketing information.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param marketingStatus
         *     Marketing information
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder marketingStatus(Collection<MarketingStatus> marketingStatus) {
            this.marketingStatus = new ArrayList<>(marketingStatus);
            return this;
        }

        /**
         * Manufacturer of this Package Item.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link MedicinalProductAuthorization}</li>
         * </ul>
         * 
         * @param marketingAuthorization
         *     Manufacturer of this Package Item
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder marketingAuthorization(Reference marketingAuthorization) {
            this.marketingAuthorization = marketingAuthorization;
            return this;
        }

        /**
         * Manufacturer of this Package Item.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param manufacturer
         *     Manufacturer of this Package Item
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufacturer(Reference... manufacturer) {
            for (Reference value : manufacturer) {
                this.manufacturer.add(value);
            }
            return this;
        }

        /**
         * Manufacturer of this Package Item.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param manufacturer
         *     Manufacturer of this Package Item
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder manufacturer(Collection<Reference> manufacturer) {
            this.manufacturer = new ArrayList<>(manufacturer);
            return this;
        }

        /**
         * Batch numbering.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param batchIdentifier
         *     Batch numbering
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder batchIdentifier(BatchIdentifier... batchIdentifier) {
            for (BatchIdentifier value : batchIdentifier) {
                this.batchIdentifier.add(value);
            }
            return this;
        }

        /**
         * Batch numbering.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param batchIdentifier
         *     Batch numbering
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder batchIdentifier(Collection<BatchIdentifier> batchIdentifier) {
            this.batchIdentifier = new ArrayList<>(batchIdentifier);
            return this;
        }

        /**
         * A packaging item, as a contained for medicine, possibly with other packaging items within.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param packageItem
         *     A packaging item, as a contained for medicine, possibly with other packaging items within
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder packageItem(PackageItem... packageItem) {
            for (PackageItem value : packageItem) {
                this.packageItem.add(value);
            }
            return this;
        }

        /**
         * A packaging item, as a contained for medicine, possibly with other packaging items within.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param packageItem
         *     A packaging item, as a contained for medicine, possibly with other packaging items within
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder packageItem(Collection<PackageItem> packageItem) {
            this.packageItem = new ArrayList<>(packageItem);
            return this;
        }

        /**
         * Build the {@link MedicinalProductPackaged}
         * 
         * <p>Required elements:
         * <ul>
         * <li>packageItem</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MedicinalProductPackaged}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicinalProductPackaged per the base specification
         */
        @Override
        public MedicinalProductPackaged build() {
            MedicinalProductPackaged medicinalProductPackaged = new MedicinalProductPackaged(this);
            if (validating) {
                validate(medicinalProductPackaged);
            }
            return medicinalProductPackaged;
        }

        protected void validate(MedicinalProductPackaged medicinalProductPackaged) {
            super.validate(medicinalProductPackaged);
            ValidationSupport.checkList(medicinalProductPackaged.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(medicinalProductPackaged.subject, "subject", Reference.class);
            ValidationSupport.checkList(medicinalProductPackaged.marketingStatus, "marketingStatus", MarketingStatus.class);
            ValidationSupport.checkList(medicinalProductPackaged.manufacturer, "manufacturer", Reference.class);
            ValidationSupport.checkList(medicinalProductPackaged.batchIdentifier, "batchIdentifier", BatchIdentifier.class);
            ValidationSupport.checkNonEmptyList(medicinalProductPackaged.packageItem, "packageItem", PackageItem.class);
            ValidationSupport.checkReferenceType(medicinalProductPackaged.subject, "subject", "MedicinalProduct");
            ValidationSupport.checkReferenceType(medicinalProductPackaged.marketingAuthorization, "marketingAuthorization", "MedicinalProductAuthorization");
            ValidationSupport.checkReferenceType(medicinalProductPackaged.manufacturer, "manufacturer", "Organization");
        }

        protected Builder from(MedicinalProductPackaged medicinalProductPackaged) {
            super.from(medicinalProductPackaged);
            identifier.addAll(medicinalProductPackaged.identifier);
            subject.addAll(medicinalProductPackaged.subject);
            description = medicinalProductPackaged.description;
            legalStatusOfSupply = medicinalProductPackaged.legalStatusOfSupply;
            marketingStatus.addAll(medicinalProductPackaged.marketingStatus);
            marketingAuthorization = medicinalProductPackaged.marketingAuthorization;
            manufacturer.addAll(medicinalProductPackaged.manufacturer);
            batchIdentifier.addAll(medicinalProductPackaged.batchIdentifier);
            packageItem.addAll(medicinalProductPackaged.packageItem);
            return this;
        }
    }

    /**
     * Batch numbering.
     */
    public static class BatchIdentifier extends BackboneElement {
        @Summary
        @Required
        private final Identifier outerPackaging;
        @Summary
        private final Identifier immediatePackaging;

        private BatchIdentifier(Builder builder) {
            super(builder);
            outerPackaging = builder.outerPackaging;
            immediatePackaging = builder.immediatePackaging;
        }

        /**
         * A number appearing on the outer packaging of a specific batch.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that is non-null.
         */
        public Identifier getOuterPackaging() {
            return outerPackaging;
        }

        /**
         * A number appearing on the immediate packaging (and not the outer packaging).
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getImmediatePackaging() {
            return immediatePackaging;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (outerPackaging != null) || 
                (immediatePackaging != null);
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
                    accept(outerPackaging, "outerPackaging", visitor);
                    accept(immediatePackaging, "immediatePackaging", visitor);
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
            BatchIdentifier other = (BatchIdentifier) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(outerPackaging, other.outerPackaging) && 
                Objects.equals(immediatePackaging, other.immediatePackaging);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    outerPackaging, 
                    immediatePackaging);
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
            private Identifier outerPackaging;
            private Identifier immediatePackaging;

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
             * A number appearing on the outer packaging of a specific batch.
             * 
             * <p>This element is required.
             * 
             * @param outerPackaging
             *     A number appearing on the outer packaging of a specific batch
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder outerPackaging(Identifier outerPackaging) {
                this.outerPackaging = outerPackaging;
                return this;
            }

            /**
             * A number appearing on the immediate packaging (and not the outer packaging).
             * 
             * @param immediatePackaging
             *     A number appearing on the immediate packaging (and not the outer packaging)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder immediatePackaging(Identifier immediatePackaging) {
                this.immediatePackaging = immediatePackaging;
                return this;
            }

            /**
             * Build the {@link BatchIdentifier}
             * 
             * <p>Required elements:
             * <ul>
             * <li>outerPackaging</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link BatchIdentifier}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid BatchIdentifier per the base specification
             */
            @Override
            public BatchIdentifier build() {
                BatchIdentifier batchIdentifier = new BatchIdentifier(this);
                if (validating) {
                    validate(batchIdentifier);
                }
                return batchIdentifier;
            }

            protected void validate(BatchIdentifier batchIdentifier) {
                super.validate(batchIdentifier);
                ValidationSupport.requireNonNull(batchIdentifier.outerPackaging, "outerPackaging");
                ValidationSupport.requireValueOrChildren(batchIdentifier);
            }

            protected Builder from(BatchIdentifier batchIdentifier) {
                super.from(batchIdentifier);
                outerPackaging = batchIdentifier.outerPackaging;
                immediatePackaging = batchIdentifier.immediatePackaging;
                return this;
            }
        }
    }

    /**
     * A packaging item, as a contained for medicine, possibly with other packaging items within.
     */
    public static class PackageItem extends BackboneElement {
        @Summary
        private final List<Identifier> identifier;
        @Summary
        @Required
        private final CodeableConcept type;
        @Summary
        @Required
        private final Quantity quantity;
        @Summary
        private final List<CodeableConcept> material;
        @Summary
        private final List<CodeableConcept> alternateMaterial;
        @Summary
        @ReferenceTarget({ "DeviceDefinition" })
        private final List<Reference> device;
        @Summary
        @ReferenceTarget({ "MedicinalProductManufactured" })
        private final List<Reference> manufacturedItem;
        @Summary
        private final List<MedicinalProductPackaged.PackageItem> packageItem;
        @Summary
        private final ProdCharacteristic physicalCharacteristics;
        @Summary
        private final List<CodeableConcept> otherCharacteristics;
        @Summary
        private final List<ProductShelfLife> shelfLifeStorage;
        @Summary
        @ReferenceTarget({ "Organization" })
        private final List<Reference> manufacturer;

        private PackageItem(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(builder.identifier);
            type = builder.type;
            quantity = builder.quantity;
            material = Collections.unmodifiableList(builder.material);
            alternateMaterial = Collections.unmodifiableList(builder.alternateMaterial);
            device = Collections.unmodifiableList(builder.device);
            manufacturedItem = Collections.unmodifiableList(builder.manufacturedItem);
            packageItem = Collections.unmodifiableList(builder.packageItem);
            physicalCharacteristics = builder.physicalCharacteristics;
            otherCharacteristics = Collections.unmodifiableList(builder.otherCharacteristics);
            shelfLifeStorage = Collections.unmodifiableList(builder.shelfLifeStorage);
            manufacturer = Collections.unmodifiableList(builder.manufacturer);
        }

        /**
         * Including possibly Data Carrier Identifier.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
         */
        public List<Identifier> getIdentifier() {
            return identifier;
        }

        /**
         * The physical type of the container of the medicine.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1.
         * 
         * @return
         *     An immutable object of type {@link Quantity} that is non-null.
         */
        public Quantity getQuantity() {
            return quantity;
        }

        /**
         * Material type of the package item.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getMaterial() {
            return material;
        }

        /**
         * A possible alternate material for the packaging.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getAlternateMaterial() {
            return alternateMaterial;
        }

        /**
         * A device accompanying a medicinal product.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getDevice() {
            return device;
        }

        /**
         * The manufactured item as contained in the packaged medicinal product.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getManufacturedItem() {
            return manufacturedItem;
        }

        /**
         * Allows containers within containers.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PackageItem} that may be empty.
         */
        public List<MedicinalProductPackaged.PackageItem> getPackageItem() {
            return packageItem;
        }

        /**
         * Dimensions, color etc.
         * 
         * @return
         *     An immutable object of type {@link ProdCharacteristic} that may be null.
         */
        public ProdCharacteristic getPhysicalCharacteristics() {
            return physicalCharacteristics;
        }

        /**
         * Other codeable characteristics.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getOtherCharacteristics() {
            return otherCharacteristics;
        }

        /**
         * Shelf Life and storage information.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ProductShelfLife} that may be empty.
         */
        public List<ProductShelfLife> getShelfLifeStorage() {
            return shelfLifeStorage;
        }

        /**
         * Manufacturer of this Package Item.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getManufacturer() {
            return manufacturer;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !identifier.isEmpty() || 
                (type != null) || 
                (quantity != null) || 
                !material.isEmpty() || 
                !alternateMaterial.isEmpty() || 
                !device.isEmpty() || 
                !manufacturedItem.isEmpty() || 
                !packageItem.isEmpty() || 
                (physicalCharacteristics != null) || 
                !otherCharacteristics.isEmpty() || 
                !shelfLifeStorage.isEmpty() || 
                !manufacturer.isEmpty();
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
                    accept(identifier, "identifier", visitor, Identifier.class);
                    accept(type, "type", visitor);
                    accept(quantity, "quantity", visitor);
                    accept(material, "material", visitor, CodeableConcept.class);
                    accept(alternateMaterial, "alternateMaterial", visitor, CodeableConcept.class);
                    accept(device, "device", visitor, Reference.class);
                    accept(manufacturedItem, "manufacturedItem", visitor, Reference.class);
                    accept(packageItem, "packageItem", visitor, MedicinalProductPackaged.PackageItem.class);
                    accept(physicalCharacteristics, "physicalCharacteristics", visitor);
                    accept(otherCharacteristics, "otherCharacteristics", visitor, CodeableConcept.class);
                    accept(shelfLifeStorage, "shelfLifeStorage", visitor, ProductShelfLife.class);
                    accept(manufacturer, "manufacturer", visitor, Reference.class);
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
            PackageItem other = (PackageItem) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(type, other.type) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(material, other.material) && 
                Objects.equals(alternateMaterial, other.alternateMaterial) && 
                Objects.equals(device, other.device) && 
                Objects.equals(manufacturedItem, other.manufacturedItem) && 
                Objects.equals(packageItem, other.packageItem) && 
                Objects.equals(physicalCharacteristics, other.physicalCharacteristics) && 
                Objects.equals(otherCharacteristics, other.otherCharacteristics) && 
                Objects.equals(shelfLifeStorage, other.shelfLifeStorage) && 
                Objects.equals(manufacturer, other.manufacturer);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    type, 
                    quantity, 
                    material, 
                    alternateMaterial, 
                    device, 
                    manufacturedItem, 
                    packageItem, 
                    physicalCharacteristics, 
                    otherCharacteristics, 
                    shelfLifeStorage, 
                    manufacturer);
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
            private List<Identifier> identifier = new ArrayList<>();
            private CodeableConcept type;
            private Quantity quantity;
            private List<CodeableConcept> material = new ArrayList<>();
            private List<CodeableConcept> alternateMaterial = new ArrayList<>();
            private List<Reference> device = new ArrayList<>();
            private List<Reference> manufacturedItem = new ArrayList<>();
            private List<MedicinalProductPackaged.PackageItem> packageItem = new ArrayList<>();
            private ProdCharacteristic physicalCharacteristics;
            private List<CodeableConcept> otherCharacteristics = new ArrayList<>();
            private List<ProductShelfLife> shelfLifeStorage = new ArrayList<>();
            private List<Reference> manufacturer = new ArrayList<>();

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
             * Including possibly Data Carrier Identifier.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     Including possibly Data Carrier Identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier... identifier) {
                for (Identifier value : identifier) {
                    this.identifier.add(value);
                }
                return this;
            }

            /**
             * Including possibly Data Carrier Identifier.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     Including possibly Data Carrier Identifier
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder identifier(Collection<Identifier> identifier) {
                this.identifier = new ArrayList<>(identifier);
                return this;
            }

            /**
             * The physical type of the container of the medicine.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     The physical type of the container of the medicine
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1.
             * 
             * <p>This element is required.
             * 
             * @param quantity
             *     The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(Quantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * Material type of the package item.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param material
             *     Material type of the package item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder material(CodeableConcept... material) {
                for (CodeableConcept value : material) {
                    this.material.add(value);
                }
                return this;
            }

            /**
             * Material type of the package item.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param material
             *     Material type of the package item
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder material(Collection<CodeableConcept> material) {
                this.material = new ArrayList<>(material);
                return this;
            }

            /**
             * A possible alternate material for the packaging.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param alternateMaterial
             *     A possible alternate material for the packaging
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder alternateMaterial(CodeableConcept... alternateMaterial) {
                for (CodeableConcept value : alternateMaterial) {
                    this.alternateMaterial.add(value);
                }
                return this;
            }

            /**
             * A possible alternate material for the packaging.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param alternateMaterial
             *     A possible alternate material for the packaging
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder alternateMaterial(Collection<CodeableConcept> alternateMaterial) {
                this.alternateMaterial = new ArrayList<>(alternateMaterial);
                return this;
            }

            /**
             * A device accompanying a medicinal product.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DeviceDefinition}</li>
             * </ul>
             * 
             * @param device
             *     A device accompanying a medicinal product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder device(Reference... device) {
                for (Reference value : device) {
                    this.device.add(value);
                }
                return this;
            }

            /**
             * A device accompanying a medicinal product.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DeviceDefinition}</li>
             * </ul>
             * 
             * @param device
             *     A device accompanying a medicinal product
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder device(Collection<Reference> device) {
                this.device = new ArrayList<>(device);
                return this;
            }

            /**
             * The manufactured item as contained in the packaged medicinal product.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link MedicinalProductManufactured}</li>
             * </ul>
             * 
             * @param manufacturedItem
             *     The manufactured item as contained in the packaged medicinal product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder manufacturedItem(Reference... manufacturedItem) {
                for (Reference value : manufacturedItem) {
                    this.manufacturedItem.add(value);
                }
                return this;
            }

            /**
             * The manufactured item as contained in the packaged medicinal product.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link MedicinalProductManufactured}</li>
             * </ul>
             * 
             * @param manufacturedItem
             *     The manufactured item as contained in the packaged medicinal product
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder manufacturedItem(Collection<Reference> manufacturedItem) {
                this.manufacturedItem = new ArrayList<>(manufacturedItem);
                return this;
            }

            /**
             * Allows containers within containers.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param packageItem
             *     Allows containers within containers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder packageItem(MedicinalProductPackaged.PackageItem... packageItem) {
                for (MedicinalProductPackaged.PackageItem value : packageItem) {
                    this.packageItem.add(value);
                }
                return this;
            }

            /**
             * Allows containers within containers.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param packageItem
             *     Allows containers within containers
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder packageItem(Collection<MedicinalProductPackaged.PackageItem> packageItem) {
                this.packageItem = new ArrayList<>(packageItem);
                return this;
            }

            /**
             * Dimensions, color etc.
             * 
             * @param physicalCharacteristics
             *     Dimensions, color etc.
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder physicalCharacteristics(ProdCharacteristic physicalCharacteristics) {
                this.physicalCharacteristics = physicalCharacteristics;
                return this;
            }

            /**
             * Other codeable characteristics.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param otherCharacteristics
             *     Other codeable characteristics
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder otherCharacteristics(CodeableConcept... otherCharacteristics) {
                for (CodeableConcept value : otherCharacteristics) {
                    this.otherCharacteristics.add(value);
                }
                return this;
            }

            /**
             * Other codeable characteristics.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param otherCharacteristics
             *     Other codeable characteristics
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder otherCharacteristics(Collection<CodeableConcept> otherCharacteristics) {
                this.otherCharacteristics = new ArrayList<>(otherCharacteristics);
                return this;
            }

            /**
             * Shelf Life and storage information.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param shelfLifeStorage
             *     Shelf Life and storage information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder shelfLifeStorage(ProductShelfLife... shelfLifeStorage) {
                for (ProductShelfLife value : shelfLifeStorage) {
                    this.shelfLifeStorage.add(value);
                }
                return this;
            }

            /**
             * Shelf Life and storage information.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param shelfLifeStorage
             *     Shelf Life and storage information
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder shelfLifeStorage(Collection<ProductShelfLife> shelfLifeStorage) {
                this.shelfLifeStorage = new ArrayList<>(shelfLifeStorage);
                return this;
            }

            /**
             * Manufacturer of this Package Item.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param manufacturer
             *     Manufacturer of this Package Item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder manufacturer(Reference... manufacturer) {
                for (Reference value : manufacturer) {
                    this.manufacturer.add(value);
                }
                return this;
            }

            /**
             * Manufacturer of this Package Item.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param manufacturer
             *     Manufacturer of this Package Item
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder manufacturer(Collection<Reference> manufacturer) {
                this.manufacturer = new ArrayList<>(manufacturer);
                return this;
            }

            /**
             * Build the {@link PackageItem}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>quantity</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link PackageItem}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid PackageItem per the base specification
             */
            @Override
            public PackageItem build() {
                PackageItem packageItem = new PackageItem(this);
                if (validating) {
                    validate(packageItem);
                }
                return packageItem;
            }

            protected void validate(PackageItem packageItem) {
                super.validate(packageItem);
                ValidationSupport.checkList(packageItem.identifier, "identifier", Identifier.class);
                ValidationSupport.requireNonNull(packageItem.type, "type");
                ValidationSupport.requireNonNull(packageItem.quantity, "quantity");
                ValidationSupport.checkList(packageItem.material, "material", CodeableConcept.class);
                ValidationSupport.checkList(packageItem.alternateMaterial, "alternateMaterial", CodeableConcept.class);
                ValidationSupport.checkList(packageItem.device, "device", Reference.class);
                ValidationSupport.checkList(packageItem.manufacturedItem, "manufacturedItem", Reference.class);
                ValidationSupport.checkList(packageItem.packageItem, "packageItem", MedicinalProductPackaged.PackageItem.class);
                ValidationSupport.checkList(packageItem.otherCharacteristics, "otherCharacteristics", CodeableConcept.class);
                ValidationSupport.checkList(packageItem.shelfLifeStorage, "shelfLifeStorage", ProductShelfLife.class);
                ValidationSupport.checkList(packageItem.manufacturer, "manufacturer", Reference.class);
                ValidationSupport.checkReferenceType(packageItem.device, "device", "DeviceDefinition");
                ValidationSupport.checkReferenceType(packageItem.manufacturedItem, "manufacturedItem", "MedicinalProductManufactured");
                ValidationSupport.checkReferenceType(packageItem.manufacturer, "manufacturer", "Organization");
                ValidationSupport.requireValueOrChildren(packageItem);
            }

            protected Builder from(PackageItem packageItem) {
                super.from(packageItem);
                identifier.addAll(packageItem.identifier);
                type = packageItem.type;
                quantity = packageItem.quantity;
                material.addAll(packageItem.material);
                alternateMaterial.addAll(packageItem.alternateMaterial);
                device.addAll(packageItem.device);
                manufacturedItem.addAll(packageItem.manufacturedItem);
                this.packageItem.addAll(packageItem.packageItem);
                physicalCharacteristics = packageItem.physicalCharacteristics;
                otherCharacteristics.addAll(packageItem.otherCharacteristics);
                shelfLifeStorage.addAll(packageItem.shelfLifeStorage);
                manufacturer.addAll(packageItem.manufacturer);
                return this;
            }
        }
    }
}
